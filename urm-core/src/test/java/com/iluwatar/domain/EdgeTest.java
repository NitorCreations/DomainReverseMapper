package com.iluwatar.domain;

import com.iluwatar.testdomain.person.Manager;
import com.iluwatar.testdomain.person.Person;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

public class EdgeTest {

    @Test
    public void testEqualsContract() throws Exception {
        EqualsVerifier.forClass(Edge.class).verify();
    }

    @Test
    public void toStringWorks() {
        String toString = new Edge(new DomainObject(Person.class), new DomainObject(Manager.class), EdgeType.ONE_TO_ONE, Direction.BI_DIRECTIONAL).toString();
        assertThat(toString, containsString("source"));
        assertThat(toString, containsString("target"));
    }

}