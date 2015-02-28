package com.nitorcreations.scanners;

import com.nitorcreations.domain.Direction;
import com.nitorcreations.domain.DomainObject;
import com.nitorcreations.domain.Edge;
import com.nitorcreations.domain.EdgeType;
import com.nitorcreations.testdomain.person.DoubleReferer;
import com.nitorcreations.testdomain.person.Manager;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

public class FieldScannerTest {

    private Edge ownerEmployeerReferences = new Edge(new DomainObject(Company.class, "owner"),
            new DomainObject(Person.class, "employeer"), EdgeType.ONE_TO_ONE, Direction.BI_DIRECTIONAL);
    private Edge customerServiceNumberReference = new Edge(new DomainObject(Company.class, "customerServiceNumber"),
            new DomainObject(PhoneNumber.class), EdgeType.ONE_TO_ONE, Direction.UNI_DIRECTIONAL);
    private Edge personalNumbersReference = new Edge(new DomainObject(Person.class, "personalNumbers"),
            new DomainObject(PhoneNumber.class), EdgeType.ONE_TO_MANY, Direction.UNI_DIRECTIONAL);

    private Edge firstReference = new Edge(new DomainObject(DoubleReferer.class, "myBoss"),
            new DomainObject(Manager.class), EdgeType.ONE_TO_ONE, Direction.UNI_DIRECTIONAL);
    private Edge secondReference = new Edge(new DomainObject(DoubleReferer.class, "myTeamManager"),
            new DomainObject(Manager.class), EdgeType.ONE_TO_ONE, Direction.UNI_DIRECTIONAL);

    private Edge motherToChilds = new Edge(new DomainObject(Mother.class, "childs"),
            new DomainObject(Child.class, "mommy"), EdgeType.ONE_TO_MANY, Direction.BI_DIRECTIONAL);
    private Edge motherToFavorite = new Edge(new DomainObject(Mother.class, "favorite"),
            new DomainObject(Child.class, "mommy"), EdgeType.ONE_TO_ONE, Direction.BI_DIRECTIONAL);

    @Test
    public void resolvesCorrectFieldEdges() {
        List<Class<?>> domainClasses = new ArrayList<>();
        domainClasses.add(Company.class);
        domainClasses.add(Person.class);
        domainClasses.add(PhoneNumber.class);
        FieldScanner scanner = new FieldScanner(domainClasses);

        List<Edge> edges = scanner.getEdges();
        assertThat(edges, containsInAnyOrder(ownerEmployeerReferences,
                customerServiceNumberReference, personalNumbersReference));
    }

    @Test
    public void domainModelHasMultipleReferencesToSameDomainObject() {
        List<Class<?>> domainClasses = new ArrayList<>();
        domainClasses.add(Manager.class);
        domainClasses.add(DoubleReferer.class);
        FieldScanner scanner = new FieldScanner(domainClasses);

        List<Edge> edges = scanner.getEdges();
        assertThat(edges, containsInAnyOrder(firstReference, secondReference));
    }

    @Test
    public void domainModelHasBiDirectionalReferencesMappedToBothReferences() {
        List<Class<?>> domainClasses = new ArrayList<>();
        domainClasses.add(Mother.class);
        domainClasses.add(Child.class);
        FieldScanner scanner = new FieldScanner(domainClasses);

        List<Edge> edges = scanner.getEdges();
        assertThat(edges, containsInAnyOrder(motherToChilds, motherToFavorite));
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

    private class Mother {
        List<Child> childs;
        Child favorite;
    }

    private class Child {
        Mother mommy;
    }
}
