package com.iluwatar.presenters;

import com.iluwatar.domain.DomainObject;
import com.iluwatar.domain.Edge;

import java.util.List;

public interface Presenter {

    String describe(List<DomainObject> domainObjects, List<Edge> edges);

}
