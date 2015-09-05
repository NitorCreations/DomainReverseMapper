package com.iluwatar.domain;

import org.junit.Test;

import static com.iluwatar.domain.EdgeType.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class EdgeTypeTest {

    @Test
    public void oneToOneInBothEdges() {
        assertThat(resolveEdgeType(ONE_TO_ONE, ONE_TO_ONE), is(ONE_TO_ONE));
    }

    @Test
    public void manyTomanyInBothEdges() {
        assertThat(resolveEdgeType(MANY_TO_MANY, MANY_TO_MANY), is(MANY_TO_MANY));
    }

    @Test
    public void manyToOneInSource() {
        assertThat(resolveEdgeType(MANY_TO_ONE, ONE_TO_ONE), is(MANY_TO_ONE));
    }

    @Test
    public void manyToOneInTarget() {
        assertThat(resolveEdgeType(ONE_TO_ONE, MANY_TO_ONE), is(MANY_TO_ONE));
    }

    @Test
    public void manyToManyInSource() {
        assertThat(resolveEdgeType(MANY_TO_MANY, ONE_TO_ONE), is(MANY_TO_MANY));
    }

    @Test
    public void manyToManyInTarget() {
        assertThat(resolveEdgeType(ONE_TO_ONE, MANY_TO_MANY), is(MANY_TO_MANY));
    }
}
