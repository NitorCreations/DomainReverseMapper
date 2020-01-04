package com.iluwatar.urm;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;





public class DomainMapperCliTest extends DomainMapperCli { // extends to silence cobertura

  @InjectMocks
  DomainMapperCli cli;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    DomainClassFinder.ALLOW_FINDING_INTERNAL_CLASSES = true;
  }

  @Test
  public void withSinglePackage() throws Exception {
    cli.run(new String[]{"-p", "com.iluwatar.urm.testdomain.person"});
    assertThat(cli.domainMapper.getClasses().size(), is(4));
  }

  @Test
  public void withSinglePackageAndIgnore() throws Exception {
    cli.run(new String[]{"-p", "com.iluwatar.urm.testdomain.person", "-i", "Manager"});
    assertThat(cli.domainMapper.getClasses().size(), is(3));
  }

  @Test
  public void withMultiPackages() throws Exception {
    cli.run(new String[]{"-p", "\"com.iluwatar.urm.testdomain.person,"
        + " com.iluwatar.urm.testdomain.another\""});
    assertThat(cli.domainMapper.getClasses().size(), is(5));
  }

  @Test
  public void withMultiPackagesAndIgnores() throws Exception {
    cli.run(new String[]{"-p", "\"com.iluwatar.urm.testdomain.person,"
        + " com.iluwatar.urm.testdomain.another\"", "-i", "\"Employee, Another\""});
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
