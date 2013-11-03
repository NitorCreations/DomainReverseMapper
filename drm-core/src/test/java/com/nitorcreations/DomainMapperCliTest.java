package com.nitorcreations;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import org.junit.Test;

public class DomainMapperCliTest {
    DomainMapperLauncher launcher = mock(DomainMapperLauncher.class);

    @Test
    public void testLaunch() throws ClassNotFoundException, FileNotFoundException, UnsupportedEncodingException {
        String[] args = new String[0];
        DomainMapperCli.setLauncher(launcher);
        DomainMapperCli.main(args);
        verify(launcher).run(args);
    }
}
