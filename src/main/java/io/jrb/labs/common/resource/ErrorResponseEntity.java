/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 Jon Brule <brulejr@gmail.com>
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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ErrorResponseEntity extends ResponseEntity<ErrorResponse> {

    public static ErrorResponseEntity badRequest(final String message) {
        return new ErrorResponseEntity(ErrorResponse.build(HttpStatus.BAD_REQUEST, message));
    }

    public static ErrorResponseEntity conflict(final String message) {
        return new ErrorResponseEntity(ErrorResponse.build(HttpStatus.CONFLICT, message));
    }

    public static ErrorResponseEntity notFound(final String message) {
        return new ErrorResponseEntity(ErrorResponse.build(HttpStatus.NOT_FOUND, message));
    }

    public static ErrorResponseEntity serverError(final String message) {
        return new ErrorResponseEntity(ErrorResponse.build(HttpStatus.INTERNAL_SERVER_ERROR, message));
    }

    public ErrorResponseEntity(final ErrorResponse body) {
        super(body, body.getStatus());
    }

}
