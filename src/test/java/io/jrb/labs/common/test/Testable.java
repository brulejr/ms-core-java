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
package io.jrb.labs.common.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.time.ZoneOffset.UTC;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public interface Testable {

    default <T> void givenNotNull(final T value, final Consumer<T> consumer) {
        assertThat(value, is(notNullValue()));
        consumer.accept(value);
    }

    default LocalDate randomLocalDate() {
        final int offset = randomInt(0, 100);
        return LocalDate.ofEpochDay(LocalDate.now().toEpochDay() - 50 + offset);
    }

    default <E extends Enum<E>> E randomEnum(final Class<E> clazz) {
        final int x = randomInt(0, clazz.getEnumConstants().length - 1);
        return clazz.getEnumConstants()[x];
    }

    default int randomInt(final int min, final int max) {
        return RandomUtils.nextInt(min, max);
    }

    default <T> List<T> randomList(final int maxSize, final Supplier<T> supplier) {
        final int size = randomInt(1, maxSize);
        return IntStream.range(1, size)
                .mapToObj(t -> supplier.get())
                .collect(Collectors.toUnmodifiableList());
    }

    default List<String> randomListOfStrings(final int size) {
        return randomList(size, () -> RandomStringUtils.randomAlphanumeric(2 * size));
    }

    default long randomLong(final long min, final long max) {
        return RandomUtils.nextLong(min, max);
    }

    default <K, V> Map<K, V> randomMap(final int maxSize, final Supplier<K> keySupplier, final Supplier<V> valueSupplier) {
        final int size = randomInt(1, maxSize);
        return IntStream.range(1, size)
                .mapToObj(t -> Pair.of(keySupplier.get(), valueSupplier.get()))
                .collect(Collectors.toUnmodifiableMap(Pair::getKey, Pair::getValue));
    }

    default <T> Set<T> randomSet(final int maxSize, final Supplier<T> supplier) {
        final int size = randomInt(1, maxSize);
        return IntStream.range(1, size)
                .mapToObj(t -> supplier.get())
                .collect(Collectors.toUnmodifiableSet());
    }

    default String randomString(final int minlen, final int maxlen) {
        return RandomStringUtils.randomAlphanumeric(minlen, maxlen);
    }

    default Instant randomTimestamp(final long offset) {
        final LocalDate now = LocalDate.now();
        final long endpoint1 = now.minusDays(offset).atStartOfDay().toInstant(UTC).toEpochMilli();
        final long endpoint2 = now.plusDays(offset).atStartOfDay().toInstant(UTC).toEpochMilli();
        final long timestamp = randomLong(endpoint1, endpoint2);
        return Instant.ofEpochMilli(timestamp);
    }


}
