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
package io.jrb.labs.common.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.List;

/**
 * Defines an error response for a web service request.
 */
@Value
@Builder(toBuilder = true)
@JsonDeserialize(builder = ErrorResponse.ErrorResponseBuilder.class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorResponse {

    HttpStatus status;

    Integer code;

    String message;

    @Builder.Default
    Instant timestamp = Instant.now();

    @Singular
    List<String> bindingErrors;

    public static ErrorResponse build(final HttpStatus status, final String message) {
        return builder()
                .status(status)
                .code(status.value())
                .message(message)
                .build();
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class ErrorResponseBuilder {
    }

}
