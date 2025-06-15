package com.ff2.rewrite.recipes.java;

public class Type {
    public final String typeName;

    public Type(String typeName) {
        this.typeName = typeName;
    }

    public boolean match(final String pTypeName) {
        return pTypeName.endsWith(typeName);
    }
}
