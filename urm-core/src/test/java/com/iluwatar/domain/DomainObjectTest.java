package com.iluwatar.domain;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class DomainObjectTest {

    @Test
    public void domainObjectConstructedProperlyFromClass() {
        DomainClass viaClassConstructor = new DomainClass(DomainObjectTest.class);
        assertThat(viaClassConstructor,
                new DomainObjectAssertion("com.iluwatar.domain", "DomainObjectTest"));
    }

    @Test
    public void domainConstructedProperlyFromInnerClass() {
        DomainClass viaClassConstructor = new DomainClass(DomainObjectTest.DomainObjectAssertion.class);
        assertThat(viaClassConstructor,
                new DomainObjectAssertion("com.iluwatar.domain", "DomainObjectAssertion"));
    }

    @Test
    public void toStringWorks() {
        String toString = new DomainClass(DomainObjectAssertion.class).toString();
        assertThat(toString, containsString("packageName"));
        assertThat(toString, containsString("className"));
    }

    private static class DomainObjectAssertion extends TypeSafeMatcher<DomainClass> {
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
        protected boolean matchesSafely(DomainClass domainObject) {
            assertThat(domainObject.getPackageName(), is(expectedPackageName));
            assertThat(domainObject.getClassName(), is(expectedClassName));
            return true;
        }
    }
}
