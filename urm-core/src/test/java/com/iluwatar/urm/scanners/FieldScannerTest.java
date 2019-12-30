package com.iluwatar.urm.scanners;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.Is.is;

import com.iluwatar.urm.domain.Direction;
import com.iluwatar.urm.domain.DomainClass;
import com.iluwatar.urm.domain.Edge;
import com.iluwatar.urm.domain.EdgeType;
import com.iluwatar.urm.testdomain.Company;
import com.iluwatar.urm.testdomain.PhoneNumber;
import com.iluwatar.urm.testdomain.Selfie;
import com.iluwatar.urm.testdomain.Task;
import com.iluwatar.urm.testdomain.Timesheet;
import com.iluwatar.urm.testdomain.family.Child;
import com.iluwatar.urm.testdomain.family.Husband;
import com.iluwatar.urm.testdomain.family.Mother;
import com.iluwatar.urm.testdomain.family.Wife;
import com.iluwatar.urm.testdomain.person.DoubleReferer;
import com.iluwatar.urm.testdomain.person.Manager;
import com.iluwatar.urm.testdomain.person.Person;
import com.iluwatar.urm.testdomain.weirdos.Outer;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class FieldScannerTest {

  private final Edge firstReference = createReference(
      DoubleReferer.class, "myBoss", Manager.class, null,
      EdgeType.ONE_TO_MANY, Direction.UNI_DIRECTIONAL);

  private final Edge motherToChilds = createReference(
      Mother.class, "childs", Child.class, "mommy",
      EdgeType.ONE_TO_MANY, Direction.BI_DIRECTIONAL);
  private final Edge motherToFavorite = createReference(
      Mother.class, "favorite", Child.class, "mommy",
      EdgeType.ONE_TO_ONE, Direction.BI_DIRECTIONAL);

  private final Edge selfReference = createReference(
      Selfie.class, "me", Selfie.class, null,
      EdgeType.ONE_TO_ONE, Direction.UNI_DIRECTIONAL);

  private final Edge phoneNumbersReference = createReference(
      Person.class, "contactNumbers", PhoneNumber.class, null,
      EdgeType.ONE_TO_MANY, Direction.UNI_DIRECTIONAL);

  private final Edge companyPersonReferences = createReference(
      Company.class, "employees", Person.class, "company",
      EdgeType.ONE_TO_MANY, Direction.BI_DIRECTIONAL);

  private final Edge marriageReference = createReference(
      Husband.class, "wife", Wife.class, "husband",
      EdgeType.ONE_TO_ONE, Direction.BI_DIRECTIONAL);

  private final Edge timeSheetToTask = createReference(
      Timesheet.class, "task", Task.class, null,
      EdgeType.ONE_TO_ONE, Direction.UNI_DIRECTIONAL);
  private final Edge taskToManager = createReference(
      Task.class, "manager", Manager.class, null,
      EdgeType.ONE_TO_ONE, Direction.UNI_DIRECTIONAL);

  private final Edge innerClassToOuter = createReference(
      Outer.Inner.class, null, Outer.class, null,
      EdgeType.INNER_CLASS, Direction.UNI_DIRECTIONAL);
  private final Edge staticInnerClassToOuter = createReference(
      Outer.StaticInner.class, null, Outer.class, null,
      EdgeType.STATIC_INNER_CLASS, Direction.UNI_DIRECTIONAL);
  private final Edge[] innerReferencingOuter = new Edge[]{
      createReference(Outer.InnerReferencingOuter.class, null,
          Outer.class, null,
          EdgeType.INNER_CLASS, Direction.UNI_DIRECTIONAL),
      createReference(Outer.InnerReferencingOuter.class, "outer",
          Outer.class, null,
          EdgeType.ONE_TO_ONE, Direction.UNI_DIRECTIONAL)
  };
  private final Edge[] innerReferencingMultipleOuter = new Edge[]{
      createReference(Outer.InnerReferencingMultipleOuter.class, null,
          Outer.class, null,
          EdgeType.INNER_CLASS, Direction.UNI_DIRECTIONAL),
      createReference(Outer.InnerReferencingMultipleOuter.class, "outerList",
          Outer.class, null,
          EdgeType.ONE_TO_MANY, Direction.UNI_DIRECTIONAL)
  };
  private final Edge[] outerReferencingInner = new Edge[]{
      createReference(Outer.OuterReferencingInner.class, null,
          Outer.class, null,
          EdgeType.INNER_CLASS, Direction.UNI_DIRECTIONAL),
      createReference(Outer.class, "outerReferencingInner",
          Outer.OuterReferencingInner.class, null,
          EdgeType.ONE_TO_ONE, Direction.UNI_DIRECTIONAL)
  };
  private final Edge[] outerReferencingMultipleInner = new Edge[]{
      createReference(Outer.OuterReferencingMultipleInner.class, null,
          Outer.class, null,
          EdgeType.INNER_CLASS, Direction.UNI_DIRECTIONAL),
      createReference(Outer.class, "outerReferencingInnerList",
          Outer.OuterReferencingMultipleInner.class, null,
          EdgeType.ONE_TO_MANY, Direction.UNI_DIRECTIONAL)
  };

  private List<Class<?>> testedSetOfDomainClasses;

  @Before
  public void setup() {
    testedSetOfDomainClasses = new ArrayList<>();
  }

  @Test
  public void testSimpleReferenceChain() {
    testedSetOfDomainClasses.add(Timesheet.class);
    testedSetOfDomainClasses.add(Task.class);
    testedSetOfDomainClasses.add(Manager.class);
    List<Edge> edges = resolveEdges(testedSetOfDomainClasses);
    assertThat(edges, containsInAnyOrder(timeSheetToTask, taskToManager));
  }

  @Test
  public void testSelfReference() {
    testedSetOfDomainClasses.add(Selfie.class);
    List<Edge> edges = resolveEdges(testedSetOfDomainClasses);
    assertThat(edges, containsInAnyOrder(selfReference));
  }

  @Test
  public void testCollectionReferences() {
    testedSetOfDomainClasses.add(PhoneNumber.class);
    testedSetOfDomainClasses.add(Person.class);
    List<Edge> edges = resolveEdges(testedSetOfDomainClasses);
    assertThat(edges, containsInAnyOrder(phoneNumbersReference));
  }

  @Test
  public void testBiDirectionalReferences() {
    testedSetOfDomainClasses.add(Company.class);
    testedSetOfDomainClasses.add(Person.class);
    testedSetOfDomainClasses.add(Husband.class);
    testedSetOfDomainClasses.add(Wife.class);
    List<Edge> edges = resolveEdges(testedSetOfDomainClasses);
    assertThat(edges, containsInAnyOrder(companyPersonReferences, marriageReference));
  }

  @Test
  public void testMultipleReferencesToSameDomainObject() {
    testedSetOfDomainClasses.add(Manager.class);
    testedSetOfDomainClasses.add(DoubleReferer.class);
    List<Edge> edges = resolveEdges(testedSetOfDomainClasses);
    assertThat(edges.size(), is(1));
    assertThat(edges, containsInAnyOrder(firstReference));
  }

  @Test
  public void testBiDirectionalReferencesMappedToBothReferences() {
    testedSetOfDomainClasses.add(Mother.class);
    testedSetOfDomainClasses.add(Child.class);
    List<Edge> edges = resolveEdges(testedSetOfDomainClasses);
    assertThat(edges, containsInAnyOrder(motherToChilds, motherToFavorite));
  }

  @Test
  public void testInnerClass() {
    testedSetOfDomainClasses.add(Outer.Inner.class);
    testedSetOfDomainClasses.add(Outer.class);
    List<Edge> edges = resolveEdges(testedSetOfDomainClasses);
    assertThat(edges, containsInAnyOrder(innerClassToOuter));
  }

  @Test
  public void testStaticInnerClass() {
    testedSetOfDomainClasses.add(Outer.StaticInner.class);
    testedSetOfDomainClasses.add(Outer.class);
    List<Edge> edges = resolveEdges(testedSetOfDomainClasses);
    assertThat(edges, containsInAnyOrder(staticInnerClassToOuter));
  }

  @Test
  public void testInnerReferencingOuter() {
    testedSetOfDomainClasses.add(Outer.InnerReferencingOuter.class);
    testedSetOfDomainClasses.add(Outer.class);
    List<Edge> edges = resolveEdges(testedSetOfDomainClasses);
    assertThat(edges, containsInAnyOrder(innerReferencingOuter));
  }

  @Test
  public void testInnerReferencingMultipleOuter() {
    testedSetOfDomainClasses.add(Outer.InnerReferencingMultipleOuter.class);
    testedSetOfDomainClasses.add(Outer.class);
    List<Edge> edges = resolveEdges(testedSetOfDomainClasses);
    assertThat(edges, containsInAnyOrder(innerReferencingMultipleOuter));
  }

  @Test
  public void testOuterReferencingInner() {
    testedSetOfDomainClasses.add(Outer.OuterReferencingInner.class);
    testedSetOfDomainClasses.add(Outer.class);
    List<Edge> edges = resolveEdges(testedSetOfDomainClasses);
    assertThat(edges, containsInAnyOrder(outerReferencingInner));
  }

  @Test
  public void testOuterReferencingMultipleInner() {
    testedSetOfDomainClasses.add(Outer.OuterReferencingMultipleInner.class);
    testedSetOfDomainClasses.add(Outer.class);
    List<Edge> edges = resolveEdges(testedSetOfDomainClasses);
    assertThat(edges, containsInAnyOrder(outerReferencingMultipleInner));
  }

  @After
  public void cleanup() {
    testedSetOfDomainClasses = null;
  }

  private static List<Edge> resolveEdges(List<Class<?>> domainClasses) {
    FieldScanner scanner = new FieldScanner(domainClasses);
    return scanner.getEdges();
  }

  /**
   * method to create a new edge.
   * @param source type of class
   * @param sourceDescription type of string
   * @param target type of class
   * @param targetDescription type of string
   * @param type type of EdgeType
   * @param direction type of Direction
   * @return
   */
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
