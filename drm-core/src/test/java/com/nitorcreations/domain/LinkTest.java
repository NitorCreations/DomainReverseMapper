package com.nitorcreations.domain;

import com.nitorcreations.domain.Link;
import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

public class LinkTest {
    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Link.class).usingGetClass().verify();
    }
}
