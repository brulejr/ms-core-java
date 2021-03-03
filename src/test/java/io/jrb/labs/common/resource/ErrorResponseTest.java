package io.jrb.labs.common.resource;

import io.jrb.labs.common.test.Testable;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.MatcherAssert.assertThat;

class ErrorResponseTest implements Testable {

    @Test
    void testCustomBuilder() {
        final String error = randomString(10, 25);
        final ErrorResponse response = ErrorResponse.build(HttpStatus.I_AM_A_TEAPOT, error);

        givenNotNull(response, (rsp) -> {
            assertThat(rsp.getMessage(), is(error));
            assertThat(rsp.getStatus(), is(HttpStatus.I_AM_A_TEAPOT));
            assertThat(rsp.getCode(), is(HttpStatus.I_AM_A_TEAPOT.value()));
            assertThat(rsp.getBindingErrors(), is(not(empty())));
        });
    }

}
