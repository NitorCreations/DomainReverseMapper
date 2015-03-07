package com.nitorcreations;

import org.junit.Test;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DomainMapperCliTest extends DomainMapperCli { // extends to silence cobertura
    DomainMapperLauncher launcher = mock(DomainMapperLauncher.class);

    @Test
    public void testLaunch() throws ClassNotFoundException, IOException {
        String[] args = new String[0];
        DomainMapperCli.setLauncher(launcher);
        DomainMapperCli.main(args);
        verify(launcher).run(args);
    }
}
