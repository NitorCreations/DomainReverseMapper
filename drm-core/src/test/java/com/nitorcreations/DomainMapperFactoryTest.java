package com.nitorcreations;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.junit.Test;

import com.nitorcreations.domain.Task;
import com.nitorcreations.domain.Timesheet;
import com.nitorcreations.domain.person.Employee;
import com.nitorcreations.domain.person.Manager;
import com.nitorcreations.domain.person.Person;

public class DomainMapperFactoryTest {
    DomainMapperFactory factory = new DomainMapperFactory();

    @Test
    public void testCreate() throws Exception {
        DomainMapper mapper = factory.create(Arrays.asList("com.nitorcreations.domain"));
        assertThat(mapper.getClasses(), hasItems(Timesheet.class, Task.class, Person.class, Manager.class, Employee.class));
    }
}
