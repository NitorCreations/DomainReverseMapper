package com.nitorcreations;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

public class DomainMapperMojoTest {
    @Spy
    File outputDirectory = new File("");
    @Spy
    List<String> packages = new ArrayList<String>();
    @Mock
    MavenProject project;
    @Mock
    SafeWriter writer;
    @Mock
    DomainMapperFactory factory;
    @Mock
    DomainMapper mapper;
    @InjectMocks
    DomainMapperMojo mojo = new DomainMapperMojo();
    final List<Class<?>> dummyList = Arrays.<Class<?>> asList(String.class, Integer.class);

    @SuppressWarnings("unchecked")
    @Before
    public void setup() throws ClassNotFoundException, DependencyResolutionRequiredException {
        MockitoAnnotations.initMocks(this);
        when(factory.create(any(List.class), any(URLClassLoader.class))).thenReturn(mapper);
        when(project.getCompileClasspathElements()).thenReturn(Arrays.asList("foo", "bar"));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void testExecute() throws MojoExecutionException, ClassNotFoundException, MojoFailureException {
        packages.add("foo.bar");
        mojo.execute();
        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
        verify(factory).create(captor.capture(), any(URLClassLoader.class));
        assertThat((List<String>) captor.getValue(), hasItems("foo.bar"));
    }

    @Test(expected = MojoFailureException.class)
    public void failsWithNoPackagesDefined() throws MojoExecutionException, ClassNotFoundException, MojoFailureException {
        mojo.execute();
    }

    @Test(expected = MojoFailureException.class)
    public void failsWhenClassNotFound() throws MojoExecutionException, ClassNotFoundException, MojoFailureException {
        when(mapper.describeDomain()).thenThrow(new ClassNotFoundException());
        mojo.execute();
    }
}
