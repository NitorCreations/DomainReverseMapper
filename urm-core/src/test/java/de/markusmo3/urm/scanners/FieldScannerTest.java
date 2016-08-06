package de.markusmo3.urm.scanners;

import de.markusmo3.urm.domain.Direction;
import de.markusmo3.urm.domain.DomainClass;
import de.markusmo3.urm.domain.Edge;
import de.markusmo3.urm.domain.EdgeType;
import de.markusmo3.urm.testdomain.*;
import de.markusmo3.urm.testdomain.family.Child;
import de.markusmo3.urm.testdomain.family.Husband;
import de.markusmo3.urm.testdomain.family.Mother;
import de.markusmo3.urm.testdomain.family.Wife;
import de.markusmo3.urm.testdomain.person.DoubleReferer;
import de.markusmo3.urm.testdomain.person.Manager;
import de.markusmo3.urm.testdomain.person.Person;
import de.markusmo3.urm.testdomain.weirdos.Outer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.Is.is;

public class FieldScannerTest {

    private final Edge firstReference = createReference(
            DoubleReferer.class, "myBoss", Manager.class, null, EdgeType.ONE_TO_MANY, Direction.UNI_DIRECTIONAL);

    private final Edge motherToChilds = createReference(
            Mother.class, "childs", Child.class, "mommy", EdgeType.ONE_TO_MANY, Direction.BI_DIRECTIONAL);
    private final Edge motherToFavorite = createReference(
            Mother.class, "favorite", Child.class, "mommy", EdgeType.ONE_TO_ONE, Direction.BI_DIRECTIONAL);

    private final Edge selfReference = createReference(
            Selfie.class, "me", Selfie.class, null, EdgeType.ONE_TO_ONE, Direction.UNI_DIRECTIONAL);

    private final Edge phoneNumbersReference = createReference(
            Person.class, "contactNumbers", PhoneNumber.class, null, EdgeType.ONE_TO_MANY, Direction.UNI_DIRECTIONAL);

    private final Edge companyPersonReferences = createReference(
            Company.class, "employees", Person.class, "company", EdgeType.ONE_TO_MANY, Direction.BI_DIRECTIONAL);

    private final Edge marriageReference = createReference(
            Husband.class, "wife", Wife.class, "husband", EdgeType.ONE_TO_ONE, Direction.BI_DIRECTIONAL);

    private final Edge timeSheetToTask = createReference(
            Timesheet.class, "task", Task.class, null, EdgeType.ONE_TO_ONE, Direction.UNI_DIRECTIONAL);
    private final Edge taskToManager = createReference(
            Task.class, "manager", Manager.class, null, EdgeType.ONE_TO_ONE, Direction.UNI_DIRECTIONAL);

    private final Edge innerClassToOuter = createReference(
            Outer.Inner.class, null, Outer.class, null, EdgeType.INNER_CLASS, Direction.UNI_DIRECTIONAL);

    private List<Class<?>> testedSetOfDomainClasses;

    @Before
    public void setup() {
        testedSetOfDomainClasses = new ArrayList<>();
    }

    @Test
    public void simpleReferenceChain() {
        testedSetOfDomainClasses.add(Timesheet.class);
        testedSetOfDomainClasses.add(Task.class);
        testedSetOfDomainClasses.add(Manager.class);
        List<Edge> edges = resolveEdges(testedSetOfDomainClasses);
        assertThat(edges, containsInAnyOrder(timeSheetToTask, taskToManager));
    }

    @Test
    public void selfReference() {
        testedSetOfDomainClasses.add(Selfie.class);
        List<Edge> edges = resolveEdges(testedSetOfDomainClasses);
        assertThat(edges, containsInAnyOrder(selfReference));
    }

    @Test
    public void collectionReferences() {
        testedSetOfDomainClasses.add(PhoneNumber.class);
        testedSetOfDomainClasses.add(Person.class);
        List<Edge> edges = resolveEdges(testedSetOfDomainClasses);
        assertThat(edges, containsInAnyOrder(phoneNumbersReference));
    }

    @Test
    public void bidirectionalReferences() {
        testedSetOfDomainClasses.add(Company.class);
        testedSetOfDomainClasses.add(Person.class);
        testedSetOfDomainClasses.add(Husband.class);
        testedSetOfDomainClasses.add(Wife.class);
        List<Edge> edges = resolveEdges(testedSetOfDomainClasses);
        assertThat(edges, containsInAnyOrder(companyPersonReferences, marriageReference));
    }

    @Test
    public void multipleReferencesToSameDomainObject() {
        testedSetOfDomainClasses.add(Manager.class);
        testedSetOfDomainClasses.add(DoubleReferer.class);
        List<Edge> edges = resolveEdges(testedSetOfDomainClasses);
        assertThat(edges.size(), is(1));
        assertThat(edges, containsInAnyOrder(firstReference));
    }

    @Test
    public void BiDirectionalReferencesMappedToBothReferences() {
        testedSetOfDomainClasses.add(Mother.class);
        testedSetOfDomainClasses.add(Child.class);
        List<Edge> edges = resolveEdges(testedSetOfDomainClasses);
        assertThat(edges, containsInAnyOrder(motherToChilds, motherToFavorite));
    }

    @Test
    public void InnerClass() {
        testedSetOfDomainClasses.add(Outer.Inner.class);
        testedSetOfDomainClasses.add(Outer.class);
        List<Edge> edges = resolveEdges(testedSetOfDomainClasses);
        assertThat(edges, containsInAnyOrder(innerClassToOuter));
    }

    @After
    public void cleanup() {
        testedSetOfDomainClasses = null;
    }

    private static List<Edge> resolveEdges(List<Class<?>> domainClasses) {
        FieldScanner scanner = new FieldScanner(domainClasses);
        return scanner.getEdges();
    }

    public static Edge createReference(Class<?> source, String sourceDescription,
                                       Class<?> target, String targetDescription,
                                       EdgeType type, Direction direction) {
        return new Edge(
                new DomainClass(source, sourceDescription),
                new DomainClass(target, targetDescription),
                type,
                direction
        );
    }
}
