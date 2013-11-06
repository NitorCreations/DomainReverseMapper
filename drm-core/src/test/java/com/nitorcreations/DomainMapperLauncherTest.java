package com.nitorcreations;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class DomainMapperLauncherTest {
    @Mock
    DomainMapperFactory factory;
    @InjectMocks
    DomainMapperLauncher launcher = new DomainMapperLauncher();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRun_singlePackage() throws Exception {
        when(factory.create(any(String[].class))).thenReturn(mock(DomainMapper.class));
        launcher.run(new String[] { "-p", "foo.bar" });
        verify(factory).create(AdditionalMatchers.aryEq(new String[] { "foo.bar" }));
    }

    @Test
    public void testRun_multiPackages() throws Exception {
        when(factory.create(any(String[].class))).thenReturn(mock(DomainMapper.class));
        launcher.run(new String[] { "-p", "\"foo.bar, baz.qux\"" });
        verify(factory).create(AdditionalMatchers.aryEq(new String[] { "foo.bar", "baz.qux" }));
    }

    @Test
    public void testRun_noPackages_noError() throws Exception {
        when(factory.create(any(String[].class))).thenReturn(mock(DomainMapper.class));
        launcher.run(new String[] {});
    }
}
