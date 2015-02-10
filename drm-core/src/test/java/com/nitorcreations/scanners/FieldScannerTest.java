package com.nitorcreations.scanners;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.junit.Test;

import com.nitorcreations.testdomain.Company;
import com.nitorcreations.testdomain.person.Person;

public class FieldScannerTest {
    @Test
    public void twoWayLink() throws ClassNotFoundException {
        FieldScanner mapper = new FieldScanner(Arrays.asList(Person.class, Company.class));
        assertThat(mapper.getLinks().size(), is(1));
    }
}
