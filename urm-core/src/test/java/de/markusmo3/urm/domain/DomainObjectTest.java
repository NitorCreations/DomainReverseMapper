package de.markusmo3.urm.domain;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class DomainObjectTest {

    @Test
    public void domainObjectConstructedProperlyFromClass() {
        DomainClass viaClassConstructor = new DomainClass(DomainObjectTest.class);
        assertThat(viaClassConstructor,
                new DomainObjectAssertion("de.markusmo3.urm.domain", "DomainObjectTest"));
    }

    @Test
    public void domainConstructedProperlyFromInnerClass() {
        DomainClass viaClassConstructor = new DomainClass(DomainObjectTest.DomainObjectAssertion.class);
        assertThat(viaClassConstructor,
                new DomainObjectAssertion("de.markusmo3.urm.domain", "DomainObjectAssertion"));
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
