/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2021 Jon Brule <brulejr@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.jrb.labs.common.service.crud;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import io.jrb.labs.common.entity.Entity;
import io.jrb.labs.common.entity.EntityBuilder;
import io.jrb.labs.common.repository.EntityRepository;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;
import java.util.function.Function;

public abstract class CrudServiceSupport<E extends Entity, EB extends EntityBuilder<E, EB>> {

    private final EntityRepository<E> entityRepository;

    private final String entityType;

    private final ObjectMapper objectMapper;

    public CrudServiceSupport(
            final Class<E> entityClass,
            final EntityRepository<E> entityRepository,
            final ObjectMapper objectMapper
    ) {
        this.entityRepository = entityRepository;
        this.objectMapper = objectMapper;

        this.entityType = entityClass.getSimpleName();
    }

    protected <R> R applyPatch(
            final UUID guid, final JsonPatch patch, final R resource, final Class<R> resourceClass
    ) {
        try {
            final JsonNode patched = patch.apply(objectMapper.convertValue(resource, JsonNode.class));
            return objectMapper.treeToValue(patched, resourceClass);
        } catch (final Exception e) {
            throw new PatchInvalidException(resourceClass.getSimpleName(), guid, e);
        }
    }

    protected Mono<E> createEntity(final EB entityBuilder) {
        return Mono.defer(() -> {
            final Instant timestamp = Instant.now();
            final E entity = entityBuilder
                    .guid(UUID.randomUUID())
                    .createdOn(timestamp)
                    .modifiedOn(timestamp)
                    .build();
            return entityRepository.save(entity)
                    .onErrorResume(handleMonoError(t -> new CreateEntityException(entityType, t)));
        });
    }

    protected Mono<Void> deleteEntity(final UUID guid, final Function<E, Mono<Void>> fnDelete) {
        return entityRepository.findByGuid(guid)
                .switchIfEmpty(Mono.error(new EntityNotFoundException(entityType, guid)))
                .flatMap(fnDelete)
                .onErrorResume(handleMonoError(t -> new DeleteEntityException(entityType, guid, t)));
    }

    protected Mono<E> findEntityByGuid(final UUID guid) {
        return entityRepository.findByGuid(guid)
                .switchIfEmpty(Mono.error(new EntityNotFoundException(entityType, guid)))
                .onErrorResume(handleMonoError(t -> new FindEntityException(entityType, guid, t)));
    }

    protected Flux<E> retrieveEntities() {
        return entityRepository.findAll()
                .onErrorResume(handleFluxError(t -> new RetrieveEntitiesException(entityType, t)));
    }

    protected Mono<E> updateEntity(final UUID guid, final Function<E, EB> fnUpdate) {
        return entityRepository.findByGuid(guid)
                .switchIfEmpty(Mono.error(new EntityNotFoundException(entityType, guid)))
                .map(entity -> {
                    final Instant timestamp = Instant.now();
                    return fnUpdate.apply(entity)
                            .id(entity.getId())
                            .createdBy(entity.getCreatedBy())
                            .createdOn(entity.getCreatedOn())
                            .modifiedBy(entity.getModifiedBy())
                            .modifiedOn(timestamp)
                            .build();
                })
                .flatMap(entityRepository::save)
                .onErrorResume(handleMonoError(t -> new UpdateEntityException(entityType, guid, t)));
    }

    private <T> Function<? super Throwable, ? extends Publisher<? extends T>> handleFluxError(
            final Function<? super Throwable, CrudServiceException> errorHandler
    ) {
        return t -> Mono.error(
                (t instanceof CrudServiceException) ? t : errorHandler.apply(t)
        );
    }

    private <T> Function<? super Throwable, ? extends Mono<? extends T>> handleMonoError(
            final Function<? super Throwable, CrudServiceException> errorHandler
    ) {
        return t -> Mono.error(
                (t instanceof CrudServiceException) ? t : errorHandler.apply(t)
        );
    }

}
