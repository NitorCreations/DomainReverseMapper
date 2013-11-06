package com.nitorcreations;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.File;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

public class DomainMapperMojoTest {
    @Spy
    File outputDirectory = new File("");
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

    @Before
    public void setup() throws ClassNotFoundException {
        MockitoAnnotations.initMocks(this);
        when(factory.create(any(String[].class), any(URLClassLoader.class))).thenReturn(mapper);
    }

    @Test
    public void testExecute() throws MojoExecutionException {
        mojo.execute();
    }
}
