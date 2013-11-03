package com.nitorcreations;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.AdditionalMatchers;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DomainMapperFactory.class)
public class DomainMapperLauncherTest {
    DomainMapperLauncher launcher;

    @Before
    public void setup() {
        PowerMockito.mockStatic(DomainMapperFactory.class);
        launcher = new DomainMapperLauncher();
    }

    @Test
    public void testRun_singlePackage() throws Exception {
        when(DomainMapperFactory.create(any(String[].class))).thenReturn(mock(DomainMapper.class));
        launcher.run(new String[] { "-p", "foo.bar" });
        verifyStatic(times(1));
        DomainMapperFactory.create(AdditionalMatchers.aryEq(new String[] { "foo.bar" }));
    }

    @Test
    public void testRun_multiPackages() throws Exception {
        when(DomainMapperFactory.create(any(String[].class))).thenReturn(mock(DomainMapper.class));
        launcher.run(new String[] { "-p", "\"foo.bar, baz.qux\"" });
        verifyStatic(times(1));
        DomainMapperFactory.create(AdditionalMatchers.aryEq(new String[] { "foo.bar", "baz.qux" }));
    }

    @Test
    public void testRun_noPackages_noError() throws Exception {
        when(DomainMapperFactory.create(any(String[].class))).thenReturn(mock(DomainMapper.class));
        launcher.run(new String[] {});
    }
}
