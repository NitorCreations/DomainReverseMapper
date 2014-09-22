package com.nitorcreations;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class DomainMapperMojoTest {
    @Spy
    File outputDirectory = new File("");
    @Spy
    List<String> packages = new ArrayList<>();
    @Mock
    MavenProject project;
    @Mock
    SafeWriter writer;
    @InjectMocks
    DomainMapperMojo mojo = new DomainMapperMojo();
    final List<Class<?>> dummyList = Arrays.<Class<?>> asList(String.class, Integer.class);

    @SuppressWarnings("unchecked")
    @Before
    public void setup() throws ClassNotFoundException, DependencyResolutionRequiredException {
        MockitoAnnotations.initMocks(this);
        when(project.getCompileClasspathElements()).thenReturn(Arrays.asList("foo", "bar"));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void testExecute() throws MojoExecutionException, ClassNotFoundException, MojoFailureException, IOException {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        doNothing().when(writer).writeToFile(any(File.class), captor.capture());
        packages.add("com.nitorcreations.testdomain");
        mojo.execute();
        assertThat(captor.getValue(), containsString("TestPojo"));
    }

    @Test(expected = MojoFailureException.class)
    public void failsWithNoPackagesDefined() throws MojoExecutionException, ClassNotFoundException, MojoFailureException {
        mojo.execute();
    }
}
