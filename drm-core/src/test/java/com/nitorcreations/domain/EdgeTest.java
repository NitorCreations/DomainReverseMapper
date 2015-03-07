package com.nitorcreations.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class EdgeTest {

    @Test
    public void testEqualsContract() throws Exception {
        EqualsVerifier.forClass(Edge.class).verify();
    }

}