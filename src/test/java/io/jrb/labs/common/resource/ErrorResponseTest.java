package io.jrb.labs.common.resource;

import io.jrb.labs.common.test.Testable;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;

class ErrorResponseTest implements Testable {

    @Test
    void testDefaultBuilder() {
        final int code = randomInt(10, 25);
        final String message = randomString(10, 25);
        final String bindError1 = randomString(10, 25);
        final String bindError2 = randomString(10, 25);
        final ErrorResponse response = ErrorResponse.builder()
                .status(HttpStatus.ACCEPTED)
                .code(code)
                .message(message)
                .bindingError(bindError1)
                .bindingError(bindError2)
                .timestamp(randomTimestamp(25))
                .build();

        givenNotNull(response, (rsp) -> {
            assertThat(rsp.getMessage(), is(message));
            assertThat(rsp.getStatus(), is(HttpStatus.ACCEPTED));
            assertThat(rsp.getCode(), is(code));
            assertThat(rsp.getBindingErrors(), hasItems(bindError1, bindError2));
            assertThat(rsp.getTimestamp(), is(notNullValue()));
        });
    }

    @Test
    void testCustomBuilder() {
        final String message = randomString(10, 25);
        final ErrorResponse response = ErrorResponse.build(HttpStatus.I_AM_A_TEAPOT, message);

        givenNotNull(response, (rsp) -> {
            assertThat(rsp.getMessage(), is(message));
            assertThat(rsp.getStatus(), is(HttpStatus.I_AM_A_TEAPOT));
            assertThat(rsp.getCode(), is(HttpStatus.I_AM_A_TEAPOT.value()));
            assertThat(rsp.getBindingErrors(), is(empty()));
            assertThat(rsp.getTimestamp(), is(notNullValue()));
        });
    }

}
