package com.nitorcreations;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.nitorcreations.domain.Task;
import com.nitorcreations.domain.Timesheet;
import com.nitorcreations.domain.person.Employee;
import com.nitorcreations.domain.person.Manager;
import com.nitorcreations.domain.person.Person;

public class DomainMapperFactoryTest {
    @Test
    public void testCreate() throws Exception {
        DomainMapper mapper = DomainMapperFactory.create(new String[] { "com.nitorcreations.domain" });
        assertThat(mapper.getClasses(), hasItems(Timesheet.class, Task.class, Person.class, Manager.class, Employee.class));
    }
}
