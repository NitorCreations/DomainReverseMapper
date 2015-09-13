package com.iluwatar;

import com.iluwatar.testdomain.Selfie;
import com.iluwatar.testdomain.Task;
import com.iluwatar.testdomain.Timesheet;
import com.iluwatar.testdomain.person.DoubleReferer;
import com.iluwatar.testdomain.person.Employee;
import com.iluwatar.testdomain.person.Manager;
import com.iluwatar.testdomain.person.Person;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.iluwatar.presenters.DefaultGraphvizPresenter.DEFAULTS;
import static com.iluwatar.presenters.DefaultGraphvizPresenter.DOMAIN_DECLARATION;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertThat;

public class DomainMapperTest {
    private DomainMapper domainMapper;

    @Test
    public void testDescribeDomain_singleClass() throws ClassNotFoundException {
        List<Class<?>> list = new ArrayList<>();
        list.add(Employee.class);
        domainMapper = new DomainMapper(list);
        String description = DOMAIN_DECLARATION + DEFAULTS + "\n  subgraph cluster_0 {\n    label = \"com.iluwatar.testdomain.person\";\n    Employee\n  }\n}";
        assertThat(domainMapper.describeDomain(), is(description));
    }

    @Test
    public void testDescribeDomain_selfie() throws ClassNotFoundException {
        List<Class<?>> list = new ArrayList<>();
        list.add(Selfie.class);
        domainMapper = new DomainMapper(list);
        String description = DOMAIN_DECLARATION + DEFAULTS + "\n  subgraph cluster_0 {\n    label = \"com.iluwatar.testdomain\";\n    Selfie\n  }\n  Selfie -> Selfie [ dir=back arrowtail=odiamond color=slategray];\n}";
        assertThat(domainMapper.describeDomain(), is(description));
    }

    @Test
    public void testDescribeDomain_doubleReferer() throws ClassNotFoundException {
        domainMapper = new DomainMapper(Arrays.asList(DoubleReferer.class, Manager.class));
        String description = DOMAIN_DECLARATION + DEFAULTS + "\n  subgraph cluster_0 {\n    label = \"com.iluwatar.testdomain.person\";\n    DoubleReferer\n    Manager\n  }\n  DoubleReferer -> Manager [ dir=back arrowtail=odiamond color=slategray];\n  DoubleReferer -> Manager [ dir=back arrowtail=odiamond color=slategray];\n}";
        assertThat(domainMapper.describeDomain(), is(description));
    }

    @Test
    public void testDescribeDomain_simpleInheritance() throws ClassNotFoundException {
        domainMapper = new DomainMapper(Arrays.<Class<?>>asList(Manager.class, Person.class));
        String description = DOMAIN_DECLARATION + DEFAULTS + "\n  subgraph cluster_0 {\n    label = \"com.iluwatar.testdomain.person\";\n    Manager\n    Person\n  }\n  Manager -> Person [arrowhead=empty color=slategray];\n}";
        assertThat(domainMapper.describeDomain(), is(description));
    }

    @Test
    public void testDescribeDomain() throws ClassNotFoundException {
        domainMapper = new DomainMapper(Arrays.asList(Employee.class, Task.class, Manager.class, Timesheet.class, Person.class));
        String description = DOMAIN_DECLARATION + DEFAULTS + "\n  subgraph cluster_0 {\n    label = \"com.iluwatar.testdomain\";\n    Task\n    Timesheet\n  }\n  subgraph cluster_1 {\n    label = \"com.iluwatar.testdomain.person\";\n    Employee\n    Manager\n    Person\n  }\n  Timesheet -> Task [ dir=back arrowtail=odiamond color=slategray];\n  Task -> Manager [ dir=back arrowtail=odiamond color=slategray];\n  Timesheet -> Employee [ dir=back arrowtail=odiamond color=slategray];\n  Task -> Employee [ dir=back arrowtail=odiamond color=slategray];\n  Employee -> Person [arrowhead=empty color=slategray];\n  Manager -> Person [arrowhead=empty color=slategray];\n}";
        assertThat(domainMapper.describeDomain(), is(description));
    }

    @Test
    public void testCreate() throws Exception {
        DomainMapper mapper = DomainMapper.create(Arrays.asList("com.iluwatar.testdomain"));
        assertThat(mapper.getClasses(), hasItems(Timesheet.class, Task.class, Person.class, Manager.class, Employee.class));
    }

}
