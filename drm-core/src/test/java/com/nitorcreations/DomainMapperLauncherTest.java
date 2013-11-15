package com.nitorcreations;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@SuppressWarnings("unchecked")
public class DomainMapperLauncherTest {
    @Mock
    DomainMapperFactory factory;
    @Mock
    SafeWriter writer;
    @InjectMocks
    DomainMapperLauncher launcher = new DomainMapperLauncher();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void withsinglePackage() throws Exception {
        when(factory.create(anyList())).thenReturn(mock(DomainMapper.class));
        launcher.run(new String[] { "-p", "foo.bar" });
        verify(factory).create((List<String>) argThat(hasItem("foo.bar")));
    }

    @Test
    public void withMultiPackages() throws Exception {
        when(factory.create(anyList())).thenReturn(mock(DomainMapper.class));
        launcher.run(new String[] { "-p", "\"foo.bar, baz.qux\"" });
        verify(factory).create((List<String>) argThat(hasItems("foo.bar", "baz.qux")));
    }

    @Test
    public void withNoPackages_noError() throws Exception {
        when(factory.create(anyList())).thenReturn(mock(DomainMapper.class));
        launcher.run(new String[] {});
    }

    @Test
    public void withWriteToFile() throws Exception {
        when(factory.create(anyList())).thenReturn(mock(DomainMapper.class));
        launcher.run(new String[] { "-p", "foo.bar", "-f", "foofile.txt" });
        verify(factory).create((List<String>) argThat(hasItem("foo.bar")));
        ArgumentCaptor<File> captor = ArgumentCaptor.forClass(File.class);
        verify(writer).writeToFile(captor.capture(), any(String.class));
    }
}
