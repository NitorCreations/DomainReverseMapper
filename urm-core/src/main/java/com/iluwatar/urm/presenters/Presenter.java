package com.iluwatar.urm.presenters;

import com.iluwatar.urm.domain.DomainClass;
import com.iluwatar.urm.domain.Edge;

import java.util.List;

public interface Presenter {

    Representation describe(List<DomainClass> domainObjects, List<Edge> edges);

    String getFileEnding();

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
        // default presenter
        return new PlantUMLPresenter();
    }
}
