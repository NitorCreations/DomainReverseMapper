package com.nitorcreations;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

public class LinkTest {
    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Link.class).usingGetClass().verify();
    }
}
