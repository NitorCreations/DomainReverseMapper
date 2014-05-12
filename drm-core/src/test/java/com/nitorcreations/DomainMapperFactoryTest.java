package com.nitorcreations;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.junit.Test;

import com.nitorcreations.testdomain.Task;
import com.nitorcreations.testdomain.Timesheet;
import com.nitorcreations.testdomain.person.Employee;
import com.nitorcreations.testdomain.person.Manager;
import com.nitorcreations.testdomain.person.Person;

public class DomainMapperFactoryTest {
    DomainMapperFactory factory = new DomainMapperFactory();

    @Test
    public void testCreate() throws Exception {
        DomainMapper mapper = factory.create(Arrays.asList("com.nitorcreations.testdomain"));
        assertThat(mapper.getClasses(), hasItems(Timesheet.class, Task.class, Person.class, Manager.class, Employee.class));
    }
}
