package com.ff2.rewrite.recipes.java;

public class NewInstance implements Initializer {
    public final String typeName;

    public NewInstance(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public boolean match(final String pTypename) {
        return typeName.equals(pTypename);
    }
}
