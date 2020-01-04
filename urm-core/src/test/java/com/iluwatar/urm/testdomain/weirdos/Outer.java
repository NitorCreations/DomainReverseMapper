package com.iluwatar.urm.testdomain.weirdos;

import java.util.List;

public class Outer {

  public class Inner {
  }

  public static class StaticInner {
  }

  public class InnerReferencingOuter {
    private Outer outer;
  }

  public class InnerReferencingMultipleOuter {
    private List<Outer> outerList;
  }

  public class OuterReferencingInner {
  }

  private OuterReferencingInner outerReferencingInner;

  public class OuterReferencingMultipleInner {
  }

  private List<OuterReferencingMultipleInner> outerReferencingInnerList;

}
