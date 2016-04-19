package com.iluwatar;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class DomainMapperCliTest extends DomainMapperCli { // extends to silence cobertura

    @InjectMocks
    DomainMapperCli cli;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void withsinglePackage() throws Exception {
        cli.run(new String[]{"-p", "com.iluwatar.testdomain.person"});
        assertThat(cli.domainMapper.getClasses().size(), is(4));
    }

    @Test
    public void withsinglePackageAndIgnore() throws Exception {
        cli.run(new String[]{"-p", "com.iluwatar.testdomain.person", "-i", "com.iluwatar.testdomain.person.Manager"});
        assertThat(cli.domainMapper.getClasses().size(), is(3));
    }

    @Test
    public void withMultiPackages() throws Exception {
        cli.run(new String[]{"-p", "\"com.iluwatar.testdomain.person, com.iluwatar.testdomain.another\""});
        assertThat(cli.domainMapper.getClasses().size(), is(5));
    }

    @Test
    public void withMultiPackagesAndIgnores() throws Exception {
        cli.run(new String[]{"-p", "\"com.iluwatar.testdomain.person, com.iluwatar.testdomain.another\"", "-i", "\"com.iluwatar.testdomain.person.Employee, com.iluwatar.testdomain.another.Another\""});
        assertThat(cli.domainMapper.getClasses().size(), is(3));
    }

    @Test
    public void withNoPackages_noError() throws Exception {
        cli.run(new String[]{});
    }

    @Test
    public void withWriteToFile() throws Exception {
        cli.run(new String[]{"-p", "foo.bar", "-f", "foofile.txt"});

        assertThat(Files.exists(Paths.get("foofile.txt")), is(true));
        Files.delete(Paths.get("foofile.txt"));
    }

    @Test
    public void customWithSinglePackage() throws Exception {
        cli.run(new String[]{"-p", "com.iluwatar.async.method.invocation"});
        assertThat(cli.domainMapper.getClasses().size(), is(4));
    }

}
