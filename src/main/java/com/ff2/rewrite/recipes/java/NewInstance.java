package com.ff2.rewrite.recipes.java;

import org.openrewrite.java.tree.J;

public class NewInstance implements Initializer {
    public final String typeName;

    public NewInstance(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public boolean match(final J.VariableDeclarations.NamedVariable namedVariable) {
        return namedVariable!=null && namedVariable.getType()!=null&&typeName.equals(namedVariable.getType().toString());
           }
}
