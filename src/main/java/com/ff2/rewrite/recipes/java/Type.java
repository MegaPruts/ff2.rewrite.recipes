package com.ff2.rewrite.recipes.java;

import java.util.List;

public class Type {
    public final String typeName;
    public final List<String> unPackagedTypeNames;
    public final String shortTypeName;
    public final List<String> typeParameters;
    public final String unPackagedType;

    public Type(String typeName) {
        this.typeName = typeName;
        unPackagedTypeNames=R.unPackagedTypeNames(typeName);
        this.shortTypeName=unPackagedTypeNames.get(0);
        this.typeParameters=unPackagedTypeNames.subList(1, unPackagedTypeNames.size());
        this.unPackagedType = R.stripPackages(typeName);

    }

    public boolean match(final String pTypeName) {
        return pTypeName.equals(typeName)||pTypeName.equals(unPackagedType);
    }
}
