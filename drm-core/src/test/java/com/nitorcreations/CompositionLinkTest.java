package com.nitorcreations;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

public class CompositionLinkTest {
    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(CompositionLink.class).usingGetClass().verify();
    }

    @Test
    public void testInverse() {
        CompositionLink link = new CompositionLink(String.class, "foo", false, Integer.class, "bar", true);
        assertThat(link.getInverse(), equalTo(new CompositionLink(Integer.class, "bar", true, String.class, "foo", false)));
    }
}
