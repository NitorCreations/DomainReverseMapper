package de.markusmo3.urm;

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
        System.setProperty("de.markusmo3.urm.DomainClassFinder.allowFindingInternalClasses", "true");
    }

    @Test
    public void withSinglePackage() throws Exception {
        cli.run(new String[]{"-p", "de.markusmo3.urm.testdomain.person"});
        assertThat(cli.domainMapper.getClasses().size(), is(4));
    }

    @Test
    public void withSinglePackageAndIgnore() throws Exception {
        cli.run(new String[]{"-p", "de.markusmo3.urm.testdomain.person", "-i", "Manager"});
        assertThat(cli.domainMapper.getClasses().size(), is(3));
    }

    @Test
    public void withMultiPackages() throws Exception {
        cli.run(new String[]{"-p", "\"de.markusmo3.urm.testdomain.person, de.markusmo3.urm.testdomain.another\""});
        assertThat(cli.domainMapper.getClasses().size(), is(5));
    }

    @Test
    public void withMultiPackagesAndIgnores() throws Exception {
        cli.run(new String[]{"-p", "\"de.markusmo3.urm.testdomain.person, de.markusmo3.urm.testdomain.another\"", "-i", "\"Employee, Another\""});
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

}
