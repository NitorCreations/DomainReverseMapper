package com.nitorcreations;


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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((a == null) ? 0 : a.hashCode());
        result = prime * result + ((atobname == null) ? 0 : atobname.hashCode());
        result = prime * result + ((b == null) ? 0 : b.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        CompositionLink other = (CompositionLink) obj;
        if (a == null) {
            if (other.a != null) {
                return false;
            }
        } else if (!a.equals(other.a)) {
            return false;
        }
        if (atobname == null) {
            if (other.atobname != null) {
                return false;
            }
        } else if (!atobname.equals(other.atobname)) {
            return false;
        }
        if (b == null) {
            if (other.b != null) {
                return false;
            }
        } else if (!b.equals(other.b)) {
            return false;
        }
        return true;
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
}
