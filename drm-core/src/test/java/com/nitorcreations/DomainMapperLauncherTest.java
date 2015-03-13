package com.nitorcreations;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@SuppressWarnings("unchecked")
public class DomainMapperLauncherTest {
    @InjectMocks
    DomainMapperLauncher launcher = new DomainMapperLauncher();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void withsinglePackage() throws Exception {
        launcher.run(new String[]{"-p", "com.nitorcreations.testdomain.person"});
        assertThat(launcher.domainMapper.getClasses().size(), is(4));
    }

    @Test
    public void withMultiPackages() throws Exception {
        launcher.run(new String[]{"-p", "\"com.nitorcreations.testdomain.person, com.nitorcreations.testdomain.another\""});
        assertThat(launcher.domainMapper.getClasses().size(), is(5));
    }

    @Test
    public void withNoPackages_noError() throws Exception {
        launcher.run(new String[]{});
    }

    @Test
    public void withWriteToFile() throws Exception {
        launcher.run(new String[]{"-p", "foo.bar", "-f", "foofile.txt"});

        assertThat(Files.exists(Paths.get("foofile.txt")), is(true));
        Files.delete(Paths.get("foofile.txt"));
    }

}
