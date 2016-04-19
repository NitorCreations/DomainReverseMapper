package com.iluwatar.presenters;

import com.iluwatar.domain.DomainClass;
import com.iluwatar.domain.Edge;

import java.util.List;

public interface Presenter {

    Representation describe(List<DomainClass> domainObjects, List<Edge> edges);

    /**
     * Fuzzy parser for presenter Strings
     * @param presenterString
     * @return corresponding Presenter or the Default PlantUMLPresenter
     */
    static Presenter parse(String presenterString) {
        // Fuzzy selection for better usability
        if (presenterString == null || presenterString.contains("plant")) {
            return new PlantUMLPresenter();
        } else if (presenterString.contains("graph")) {
            return new GraphvizPresenter();
        }
        return new PlantUMLPresenter();
    }
}
