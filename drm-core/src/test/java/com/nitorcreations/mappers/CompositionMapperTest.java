package com.nitorcreations.mappers;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.junit.Test;

import com.nitorcreations.testdomain.Company;
import com.nitorcreations.testdomain.person.Person;

public class CompositionMapperTest {
    @Test
    public void twoWayLink() throws ClassNotFoundException {
        CompositionMapper mapper = new CompositionMapper(Arrays.asList(Person.class, Company.class));
        assertThat(mapper.getLinks().size(), is(1));
    }
}
