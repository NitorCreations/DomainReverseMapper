package com.nitorcreations.presenters;

import com.nitorcreations.domain.DomainObject;
import com.nitorcreations.domain.Edge;

import java.util.List;

public interface Presenter {

    String describe(List<DomainObject> domainObjects, List<Edge> edges);

}
