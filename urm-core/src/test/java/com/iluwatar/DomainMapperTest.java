package com.iluwatar;

import com.iluwatar.presenters.GraphvizPresenter;
import com.iluwatar.presenters.Presenter;
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

import static com.iluwatar.presenters.GraphvizPresenter.DEFAULTS;
import static com.iluwatar.presenters.GraphvizPresenter.DOMAIN_DECLARATION;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertThat;

/**
 * This test only runs with the GraphvizPresenter
 */
public class DomainMapperTest {

    private Presenter presenter = new GraphvizPresenter();
    private DomainMapper domainMapper;

    @Test
    public void testDescribeDomain_singleClass() throws ClassNotFoundException {
        List<Class<?>> list = new ArrayList<>();
        list.add(Employee.class);
        domainMapper = new DomainMapper(presenter, list);
        String description = DOMAIN_DECLARATION + DEFAULTS + "\n  subgraph cluster_0";
        assertThat(getRepresentationContent(), containsString(description));
        String pkg = "label = \"com.iluwatar.testdomain.person\";";
        assertThat(getRepresentationContent(), containsString(pkg));
        String employee = "Employee [ label = \"{Employee | + public String getDepartment()\\l+ public String getName()\\l}\" ]";
        assertThat(getRepresentationContent(), containsString(employee));
    }
    @Test
    public void testDescribeDomain_selfie() throws ClassNotFoundException {
        List<Class<?>> list = new ArrayList<>();
        list.add(Selfie.class);
        domainMapper = new DomainMapper(presenter, list);
        String description = DOMAIN_DECLARATION + DEFAULTS + "\n  subgraph cluster_0";
        assertThat(getRepresentationContent(), containsString(description));
        String pkg = "label = \"com.iluwatar.testdomain\";";
        assertThat(getRepresentationContent(), containsString(pkg));
        String selfie = "Selfie [ label = \"{Selfie | }\" ]";
        assertThat(getRepresentationContent(), containsString(selfie));
        String relation = "Selfie -> Selfie [ dir=back arrowtail=odiamond color=slategray];";
        assertThat(getRepresentationContent(), containsString(relation));
    }

    @Test
    public void testDescribeDomain_doubleReferer() throws ClassNotFoundException {
        domainMapper = new DomainMapper(presenter, Arrays.asList(DoubleReferer.class, Manager.class));
        String description = DOMAIN_DECLARATION + DEFAULTS + "\n  subgraph cluster_0";
        assertThat(getRepresentationContent(), containsString(description));
        String pkg = "label = \"com.iluwatar.testdomain.person\";";
        assertThat(getRepresentationContent(), containsString(description));
        String doubleReferer = "DoubleReferer [ label = \"{DoubleReferer | }\" ]";
        assertThat(getRepresentationContent(), containsString(doubleReferer));
        String manager = "Manager [ label = \"{Manager | + public String getName()\\l}\" ]";
        assertThat(getRepresentationContent(), containsString(manager));
        String relation = "DoubleReferer -> Manager [ dir=back arrowtail=odiamond color=slategray];";
        assertThat(getRepresentationContent(), containsString(relation));
    }

    @Test
    public void testDescribeDomain_simpleInheritance() throws ClassNotFoundException {
        domainMapper = new DomainMapper(presenter, Arrays.<Class<?>>asList(Manager.class, Person.class));
        String description = DOMAIN_DECLARATION + DEFAULTS + "\n  subgraph cluster_0";
        assertThat(getRepresentationContent(), containsString(description));
        String pkg = "label = \"com.iluwatar.testdomain.person\";";
        assertThat(getRepresentationContent(), containsString(pkg));
        String manager = "Manager [ label = \"{Manager | + public String getName()\\l}\" ]";
        assertThat(getRepresentationContent(), containsString(manager));
        String person = "Person [ label = \"{Person | }\" ]";
        assertThat(getRepresentationContent(), containsString(person));
        String relation = "Manager -> Person [arrowhead=empty color=slategray];";
        assertThat(getRepresentationContent(), containsString(relation));
    }

    @Test
    public void testDescribeDomain() throws ClassNotFoundException {
        domainMapper = new DomainMapper(presenter, Arrays.asList(Employee.class, Task.class, Manager.class, Timesheet.class, Person.class));
        String description1 = DOMAIN_DECLARATION + DEFAULTS + "\n  subgraph cluster_0";
        assertThat(getRepresentationContent(), containsString(description1));
        String pkg1 = "label = \"com.iluwatar.testdomain\";";
        assertThat(getRepresentationContent(), containsString(pkg1));
        String task = "Task [ label = \"{Task | + public List getAssignedEmployees()\\l+ public Manager getManager()\\l+ public void addEmployee(Employee)\\l+ public void completeTask()\\l+ public void removeEmployee(Employee)\\l}\" ]";
        assertThat(getRepresentationContent(), containsString(task));
        String timesheet = "Timesheet [ label = \"{Timesheet | + public Employee getWho()\\l+ public Integer getHours()\\l+ public String toString()\\l+ public Task getTask()\\l+ public void alterHours(Integer)\\l}\" ]";
        assertThat(getRepresentationContent(), containsString(timesheet));
        String description2 = "subgraph cluster_1";
        assertThat(getRepresentationContent(), containsString(description2));
        String pkg2 = "label = \"com.iluwatar.testdomain.person\";";
        assertThat(getRepresentationContent(), containsString(pkg2));
        String employee = "Employee [ label = \"{Employee | + public String getDepartment()\\l+ public String getName()\\l}\" ]";
        assertThat(getRepresentationContent(), containsString(employee));
        String manager = "Manager [ label = \"{Manager | + public String getName()\\l}\" ]";
        assertThat(getRepresentationContent(), containsString(manager));
        String person = "Person [ label = \"{Person | }\" ]";
        assertThat(getRepresentationContent(), containsString(person));
        String relation1 = "Timesheet -> Task [ dir=back arrowtail=odiamond color=slategray];";
        assertThat(getRepresentationContent(), containsString(relation1));
        String relation2 = "Task -> Manager [ dir=back arrowtail=odiamond color=slategray];";
        assertThat(getRepresentationContent(), containsString(relation2));
        String relation3 = "Timesheet -> Employee [ dir=back arrowtail=odiamond color=slategray];";
        assertThat(getRepresentationContent(), containsString(relation3));
        String relation4 = "Task -> Employee [ dir=back arrowtail=odiamond color=slategray];";
        assertThat(getRepresentationContent(), containsString(relation4));
        String relation5 = "Employee -> Person [arrowhead=empty color=slategray];";
        assertThat(getRepresentationContent(), containsString(relation5));
        String relation6 = "Manager -> Person [arrowhead=empty color=slategray];";
        assertThat(getRepresentationContent(), containsString(relation6));
    }

    @Test
    public void testCreate() throws Exception {
        DomainMapper mapper = DomainMapper.create(presenter, Arrays.asList("com.iluwatar.testdomain"));
        assertThat(mapper.getClasses(), hasItems(Timesheet.class, Task.class, Person.class, Manager.class, Employee.class));
    }

    private String getRepresentationContent() throws ClassNotFoundException {
        return domainMapper.describeDomain().getContent();
    }
}
