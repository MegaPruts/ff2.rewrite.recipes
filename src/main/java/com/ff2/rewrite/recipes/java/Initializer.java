package com.ff2.rewrite.recipes.java;

import org.openrewrite.java.tree.J;

public interface Initializer {

    boolean match(J.VariableDeclarations.NamedVariable variable);
}
