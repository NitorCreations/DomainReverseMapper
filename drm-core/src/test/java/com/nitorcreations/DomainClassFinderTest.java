package com.nitorcreations;

import net.sf.qualitytest.CoverageForPrivateConstructor;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class DomainClassFinderTest {

    @Test
    public void withsinglePackage() throws Exception {
        List<Class<?>> classes = DomainClassFinder.findClasses(Arrays.asList("com.nitorcreations.testdomain.person"));

        assertThat(classes.size(), is(4));
    }

    @Test
    public void withMultiPackages() throws Exception {
        List<Class<?>> classes = DomainClassFinder.findClasses(Arrays.asList("com.nitorcreations.testdomain.person", "com.nitorcreations.testdomain.another"));

        assertThat(classes.size(), is(5));
    }


    @Test
    public void privateConstructor() {
        CoverageForPrivateConstructor.giveMeCoverage(DomainClassFinder.class);
    }
}