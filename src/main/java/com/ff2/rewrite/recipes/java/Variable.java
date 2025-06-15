package com.ff2.rewrite.recipes.java;

import org.openrewrite.java.tree.J;

public class Variable {
    public final String variableName;

    public Variable(String variableName) {
        this.variableName = variableName;
    }

    public boolean match(final J.Identifier statement)
    {
        return false;
    }
}
