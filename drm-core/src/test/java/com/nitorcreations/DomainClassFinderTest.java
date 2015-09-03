package com.nitorcreations;

import net.sf.qualitytest.CoverageForPrivateConstructor;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class DomainClassFinderTest {

    @Test
    public void withSinglePackage() throws Exception {
        List<Class<?>> classes = findClasses("com.nitorcreations.testdomain.person");
        assertThat(classes.size(), is(4));
    }

    @Test
    public void withMultiPackages() throws Exception {
        List<Class<?>> classes = findClasses("com.nitorcreations.testdomain.person", "com.nitorcreations.testdomain.another");
        assertThat(classes.size(), is(5));
    }

    @Test
    public void filtersAnonymousClasses() throws Exception {
        List<Class<?>> classes = findClasses("com.nitorcreations.testdomain.withanonymousclass");
        assertThat(classes.size(), is(1));
    }

    @Test
    public void privateConstructor() {
        CoverageForPrivateConstructor.giveMeCoverage(DomainClassFinder.class);
    }

    private List<Class<?>> findClasses(String... packages) {
        return DomainClassFinder.findClasses(Arrays.asList(packages), null);
    }
}