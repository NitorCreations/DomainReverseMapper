package com.nitorcreations;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
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

    @Test
    public void doubleReferer() {
        CompositionLink link = new CompositionLink(String.class, "foo", false, Integer.class, null, true);
        assertThat(link.isDoubleReferer(new CompositionLink(String.class, "foo2", false, Integer.class, null, true)), is(true));
        assertThat(link.isDoubleReferer(new CompositionLink(String.class, "foo", false, Integer.class, null, true)), is(false));
        assertThat(link.isDoubleReferer(new CompositionLink(String.class, "foo", false, Integer.class, "baz", true)), is(true));
        assertThat(link.isDoubleReferer(new CompositionLink(String.class, "foo2", false, Integer.class, "baz", true)), is(false));
        assertThat(link.isDoubleReferer(new CompositionLink(String.class, "foo2", false, Double.class, null, true)), is(false));
        assertThat(link.isDoubleReferer(new CompositionLink(Double.class, "foo2", false, Integer.class, null, true)), is(true));
        assertThat(link.isDoubleReferer(new CompositionLink(Double.class, "foo2", false, Integer.class, "buz", true)), is(false));
    }
}
