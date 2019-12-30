package com.iluwatar.urm.domain;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import com.iluwatar.urm.testdomain.person.Manager;
import com.iluwatar.urm.testdomain.person.Person;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;


public class EdgeTest {

  @Test
  public void testEqualsContract() throws Exception {
    EqualsVerifier.forClass(Edge.class).withPrefabValues(DomainClass.class,
        new DomainClass(Person.class), new DomainClass(Manager.class)).verify();
  }

  @Test
  public void toStringWorks() {
    String toString = new Edge(new DomainClass(Person.class),
        new DomainClass(Manager.class), EdgeType.ONE_TO_ONE, Direction.BI_DIRECTIONAL).toString();
    assertThat(toString, containsString("source"));
    assertThat(toString, containsString("target"));
  }

}
