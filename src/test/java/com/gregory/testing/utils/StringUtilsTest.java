package com.gregory.testing.utils;

import org.junit.Test;

import static com.gregory.testing.utils.StringUtils.join;
import static org.fest.assertions.Assertions.assertThat;

public final class StringUtilsTest {

    @Test
    public void it_should_join_strings_with_a_separator() {
        assertThat(join("-", "one", "two")).isEqualTo("one-two");
    }

}
