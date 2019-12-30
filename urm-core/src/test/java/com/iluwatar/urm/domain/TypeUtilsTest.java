package com.iluwatar.urm.domain;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

import com.iluwatar.urm.testdomain.observ.Observable;
import com.iluwatar.urm.testdomain.observ.Observer;
import com.iluwatar.urm.testdomain.weirdos.GenericMadness;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;


/**
 * Created by moe on 12.08.16.
 */
public class TypeUtilsTest {

  final String[] normalMembers = {"GenericMadness<X, Y extends X>", "void", "void",
      "Map<List<Set<Queue<Class<? extends X>>>>, Stream<Map.Entry<String, Integer>>>",
      "Triple<Class<C>, Class<? extends C>, Class<? super C>>",
      "Inner<D extends List<Y> & Serializable>",
      "InnerInner<E extends D>",
      "Inner2<H extends Inner2>"
  };

  final String[] observableMembers = {"Observable<S extends Observable,"
      + " O extends Observer<S, O, A>, A>",
      "void", "void", "void", "List<O extends Observer<S, O, A>>"
  };

  final String[] observerMembers = {"Observer<S extends Observable<S, O, A>,"
      + " O extends Observer, A>", "void"};

  @Test
  public void testNormal() {
    test(GenericMadness.class, normalMembers);
    test(Observable.class, observableMembers);
    test(Observer.class, observerMembers);
  }

  /**
   * test method.
   * @param clazz type of class
   * @param shouldBeMembers type of string array
   */
  public void test(Class<?> clazz, String[] shouldBeMembers) {
    List<String> members = new LinkedList<>();
    members.add(TypeUtils.getSimpleName(clazz));

    Method[] methods = clazz.getDeclaredMethods();
    Field[] fields = clazz.getDeclaredFields();
    Class[] classes = clazz.getDeclaredClasses();
    for (Method method : methods) {
      if (!DomainClass.IGNORED_METHODS.contains(method.getName())) {
        members.add(TypeUtils.getSimpleName(method.getGenericReturnType()));
      }
    }
    for (Field field : fields) {
      if (!DomainClass.IGNORED_FIELDS.contains(field.getName())) {
        members.add(TypeUtils.getSimpleName(field.getGenericType()));
      }
    }
    for (Class innerClazz : classes) {
      members.add(TypeUtils.getSimpleName(innerClazz));
      for (Class innerClazz2 : innerClazz.getDeclaredClasses()) {
        members.add(TypeUtils.getSimpleName(innerClazz2));
      }
    }
    //System.out.println(members.stream().collect(Collectors.joining("\n|")));
    assertThat(members, containsInAnyOrder(shouldBeMembers));
  }

}
