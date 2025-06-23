package com.ff2.rewrite.recipes.java;

import static com.ff2.rewrite.recipes.java.R.*;

import java.util.ArrayList;
import java.util.List;

public class Type {
    public static final Type NULL_TYPE = new Type("nulll");
    public final String typeName;
    public final List<String> unPackagedTypeNames;
    public final String shortTypeName;
    public final List<String> typeParameters;
    public final String unPackagedType;

    public Type(String typeName) {
        this.typeName = typeName;
        unPackagedTypeNames = R.unPackagedTypeNames(typeName);
        this.shortTypeName = unPackagedTypeNames.get(0);
        this.typeParameters = unPackagedTypeNames.subList(1, unPackagedTypeNames.size());
        this.unPackagedType = R.stripPackages(typeName);

    }

    public boolean match(final String pTypeName) {
        return this.equals(R.type(pTypeName));
    }

    public boolean equals(Type other) {
        return other != null && this.typeName != null && this.typeName.equals(other.typeName);
    }

    public Type typeParameter(final int index) {
        return typeParameters().get(index);

    }

    public List<Type> typeParameters() {
        List<Type> result = new ArrayList<>();

        // Match the outer type and the generic parameter
        java.util.regex.Matcher matcher = TYPE_PARAMETER_SPLITTER_PATTERN.matcher(typeName);

        if (matcher.matches()) {
            // Group 0 is the complete type
            // Group 1 is the outer class
            // Group 2 is the first type parameter
            for (int index = 2; index <= matcher.groupCount(); index++) {
                result.add(type(matcher.group(index)));
            }
        }

        return result;

    }
}
