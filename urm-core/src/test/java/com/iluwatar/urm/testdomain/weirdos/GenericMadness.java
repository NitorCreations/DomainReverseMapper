package com.iluwatar.urm.testdomain.weirdos;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Stream;
import org.apache.commons.lang3.tuple.Triple;


/**
 * Created by moe on 12.08.16.
 */
public class GenericMadness<X, Y extends X> {

  private Map<List<Set<Queue<Class<? extends X>>>>,
      Stream<Map.Entry<String, Integer>>> collectionsCollection;

  public <A, B extends A> void simple(A a, B b) {
  }

  public <C> void allWildCardParameters(Class<C> aclass, Class<? extends C> extendsAClass,
                                        Class<? super C> superAClass) {
  }

  public <C> Triple<Class<C>, Class<? extends C>, Class<? super C>> allWildCardReturns(C c) {
    return null;
  }

  private class Inner<D extends List<Y> & Serializable> {
    private class InnerInner<E extends D> {
      E eclass;
      D dclass;
    }
  }

  private class Inner2<H extends Inner2> {

  }
}
