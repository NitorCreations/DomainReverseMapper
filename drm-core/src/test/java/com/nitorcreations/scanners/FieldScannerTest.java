package com.nitorcreations.scanners;

import com.nitorcreations.domain.Direction;
import com.nitorcreations.domain.DomainObject;
import com.nitorcreations.domain.Edge;
import com.nitorcreations.domain.EdgeType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

public class FieldScannerTest {

    private Edge ownerEmployeerReference = new Edge(new DomainObject(Company.class, "owner"),
            new DomainObject(Person.class, "employeer"), EdgeType.ONE_TO_ONE, Direction.BI_DIRECTIONAL);
    private Edge customerServiceNumberReference = new Edge(new DomainObject(Company.class, "customerServiceNumber"),
            new DomainObject(PhoneNumber.class), EdgeType.ONE_TO_ONE, Direction.UNI_DIRECTIONAL);
    private Edge personalNumbersReference = new Edge(new DomainObject(Person.class, "personalNumbers"),
            new DomainObject(PhoneNumber.class), EdgeType.ONE_TO_MANY, Direction.UNI_DIRECTIONAL);

    @Test
    public void resolvesCorrectFieldEdges() {
        List<Class<?>> domainClasses = new ArrayList<>();
        domainClasses.add(Company.class);
        domainClasses.add(Person.class);
        domainClasses.add(PhoneNumber.class);
        FieldScanner scanner = new FieldScanner(domainClasses);

        List<Edge> edges = scanner.getEdges();
        assertThat(edges, containsInAnyOrder(ownerEmployeerReference,
                customerServiceNumberReference, personalNumbersReference));
    }

    private class Company {
        Person owner;
        PhoneNumber customerServiceNumber;
    }

    private class Person {
        Company employeer;
        List<PhoneNumber> personalNumbers;
    }

    private class PhoneNumber {
        String number;
    }
}
