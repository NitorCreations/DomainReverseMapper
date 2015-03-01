package com.nitorcreations.domain;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class DomainObjectTest {

    @Test
    public void domainObjectConstructedProperlyFromClass() {
        DomainObject viaClassConstructor = new DomainObject(DomainObjectTest.class);
        assertThat(viaClassConstructor,
                new DomainObjectAssertion("com.nitorcreations.domain", "DomainObjectTest"));
    }

    @Test
    public void domainConstructedProperlyFromInnerClass() {
        DomainObject viaClassConstructor = new DomainObject(DomainObjectTest.DomainObjectAssertion.class);
        assertThat(viaClassConstructor,
                new DomainObjectAssertion("com.nitorcreations.domain", "DomainObjectAssertion"));
    }

    private static class DomainObjectAssertion extends TypeSafeMatcher<DomainObject> {
        private String expectedPackageName;
        private String expectedClassName;

        public DomainObjectAssertion(String expectedPackageName, String expectedClassName) {
            this.expectedPackageName = expectedPackageName;
            this.expectedClassName = expectedClassName;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("faulty domain object construction");
        }

        @Override
        protected boolean matchesSafely(DomainObject domainObject) {
            assertThat(domainObject.packageName, is(expectedPackageName));
            assertThat(domainObject.className, is(expectedClassName));
            return true;
        }
    }
}
