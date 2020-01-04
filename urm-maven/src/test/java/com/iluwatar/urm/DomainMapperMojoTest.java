package com.iluwatar.urm;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

public class DomainMapperMojoTest {
  public static final String PROJECT_NAME = "domain-mapper-mojo-test";
  @Spy
  File outputDirectory = new File("");
  @Mock
  MavenProject project;
  @Spy
  List<String> packages = new ArrayList<>();
  @Spy
  List<String> ignores = new ArrayList<>();
  @InjectMocks
  DomainMapperMojo mojo = new DomainMapperMojo();
  final List<Class<?>> dummyList = Arrays.<Class<?>>asList(String.class, Integer.class);

  /**
   * setup method for the test.
   * @throws ClassNotFoundException exception
   * @throws DependencyResolutionRequiredException exception
   */
  @SuppressWarnings("unchecked")
  @Before
  public void setup() throws ClassNotFoundException, DependencyResolutionRequiredException {
    MockitoAnnotations.initMocks(this);
    when(project.getCompileClasspathElements()).thenReturn(Arrays.asList("foo", "bar"));
    when(project.getName()).thenReturn(PROJECT_NAME);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  @Test
  public void testPlantUmlExecute() throws MojoExecutionException,
      ClassNotFoundException, MojoFailureException, IOException {
    packages.add("com.iluwatar.testdomain");
    mojo.execute();

    Path pumlPath = Paths.get(PROJECT_NAME + ".urm.puml");
    assertThat(Files.exists(pumlPath), is(true));
    Files.delete(pumlPath);
  }

  @Test(expected = MojoFailureException.class)
  public void failsWithNoPackagesDefined() throws MojoExecutionException,
      ClassNotFoundException, MojoFailureException {
    mojo.execute();
  }
}
