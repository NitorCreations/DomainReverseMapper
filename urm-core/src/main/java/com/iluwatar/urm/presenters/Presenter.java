package com.iluwatar.urm.presenters;

import com.iluwatar.urm.domain.DomainClass;
import com.iluwatar.urm.domain.Edge;

import java.util.List;

public interface Presenter {

    Representation describe(List<DomainClass> domainObjects, List<Edge> edges);

    String getFileEnding();

    /**
     * Factory method for {@link Presenter}
     * @param presenterString
     * @return chosen Presenter
     */
    static Presenter parse(String presenterString) {
        if (presenterString == null || presenterString.equalsIgnoreCase("plantuml")) {
            return new PlantUMLPresenter();
        } else if (presenterString.equalsIgnoreCase("graphviz")) {
            return new GraphvizPresenter();
        }
        return new PlantUMLPresenter();
    }
}
