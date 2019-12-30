package com.iluwatar.urm.domain;

import org.hamcrest.MatcherAssert;
import org.hamcrest.core.Is;
import org.junit.Test;

public class EdgeTypeTest {

  @Test
  public void oneToOneInBothEdges() {
    MatcherAssert.assertThat(EdgeType.resolveEdgeType(EdgeType.ONE_TO_ONE,
        EdgeType.ONE_TO_ONE), Is.is(EdgeType.ONE_TO_ONE));
  }

  @Test
  public void manyTomanyInBothEdges() {
    MatcherAssert.assertThat(EdgeType.resolveEdgeType(EdgeType.MANY_TO_MANY,
        EdgeType.MANY_TO_MANY), Is.is(EdgeType.MANY_TO_MANY));
  }

  @Test
  public void manyToOneInSource() {
    MatcherAssert.assertThat(EdgeType.resolveEdgeType(EdgeType.MANY_TO_ONE,
        EdgeType.ONE_TO_ONE), Is.is(EdgeType.MANY_TO_ONE));
  }

  @Test
  public void manyToOneInTarget() {
    MatcherAssert.assertThat(EdgeType.resolveEdgeType(EdgeType.ONE_TO_ONE,
        EdgeType.MANY_TO_ONE), Is.is(EdgeType.MANY_TO_ONE));
  }

  @Test
  public void manyToManyInSource() {
    MatcherAssert.assertThat(EdgeType.resolveEdgeType(EdgeType.MANY_TO_MANY,
        EdgeType.ONE_TO_ONE), Is.is(EdgeType.MANY_TO_MANY));
  }

  @Test
  public void manyToManyInTarget() {
    MatcherAssert.assertThat(EdgeType.resolveEdgeType(EdgeType.ONE_TO_ONE,
        EdgeType.MANY_TO_MANY), Is.is(EdgeType.MANY_TO_MANY));
  }
}
