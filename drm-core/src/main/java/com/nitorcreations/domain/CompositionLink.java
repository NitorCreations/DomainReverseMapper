package com.nitorcreations.domain;

import org.apache.commons.lang3.StringUtils;

public class CompositionLink extends Link {
    private final String atobname;
    private final String btoaname;
    private final boolean collectionA;
    private final boolean collectionB;

    public CompositionLink(final Class<?> a, final String atobname, final boolean collectionA, final Class<?> b, final String btoaname, final boolean collectionB) {
        super(a, b);
        this.atobname = atobname;
        this.btoaname = btoaname;
        this.collectionA = collectionA;
        this.collectionB = collectionB;
    }

    public boolean isCollectionA() {
        return collectionA;
    }

    public boolean isCollectionB() {
        return collectionB;
    }

    public CompositionLink getInverse() {
        return new CompositionLink(b, btoaname, collectionB, a, atobname, collectionA);
    }

    public boolean isInverse(final CompositionLink other) {
        return getInverse().equals(other);
    }

    public String getBtoAname() {
        return btoaname;
    }

    public String getAtoBname() {
        return atobname;
    }

    public boolean isDoubleReferer(final CompositionLink other) {
        return (!StringUtils.equals(atobname, other.atobname) && b == other.b && StringUtils.equals(btoaname, other.btoaname)) || (!StringUtils.equals(btoaname, other.btoaname) && a == other.a && StringUtils.equals(atobname, other.atobname));
    }
}
