package com.iluwatar;

import net.sf.qualitytest.CoverageForPrivateConstructor;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class DomainClassFinderTest {

    @Test
    public void withSinglePackage() throws Exception {
        List<Class<?>> classes = findClasses("com.iluwatar.testdomain.person");
        assertThat(classes.size(), is(4));
    }

    @Test
    public void withSinglePackageAndIgnore() throws Exception {
        List<Class<?>> classes = DomainClassFinder.findClasses(Arrays.asList("com.iluwatar.testdomain.person"),
                Arrays.asList("com.iluwatar.testdomain.person.Manager"), null);
        assertThat(classes.size(), is(3));
    }

    @Test
    public void withMultiPackages() throws Exception {
        List<Class<?>> classes = findClasses("com.iluwatar.testdomain.person", "com.iluwatar.testdomain.another");
        assertThat(classes.size(), is(5));
    }

    @Test
    public void withMultiPackagesAndIgnores() throws Exception {
        List<Class<?>> classes = DomainClassFinder.findClasses(Arrays.asList("com.iluwatar.testdomain.person", "com.iluwatar.testdomain.another.Another"),
                Arrays.asList("com.iluwatar.testdomain.person.Manager", "com.iluwatar.testdomain.another.Another"), null);
        assertThat(classes.size(), is(3));
    }

    @Test
    public void filtersAnonymousClasses() throws Exception {
        List<Class<?>> classes = findClasses("com.iluwatar.testdomain.withanonymousclass");
        assertThat(classes.size(), is(1));
    }

    @Test
    public void privateConstructor() {
        CoverageForPrivateConstructor.giveMeCoverage(DomainClassFinder.class);
    }

    private List<Class<?>> findClasses(String... packages) {
        return DomainClassFinder.findClasses(Arrays.asList(packages), new ArrayList<>(), null);
    }
}