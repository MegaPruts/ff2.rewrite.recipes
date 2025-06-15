package com.ff2.rewrite.recipes.java;

import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.Statement;

public class VariableDeclarationMatcher extends Matcher { public String name;
    public Type type;
    public Variable variable;
    public Initializer initializer;

    public VariableDeclarationMatcher(final String name, final Type type) {
        this(name, type, null, null);
    }

    public VariableDeclarationMatcher(final String name, final Type type, final Variable variable,
            final Initializer initializer) {
        super(name);
        this.type = type;
        this.variable = variable;
        this.initializer = initializer;
    }

    public String variableName() {
        return variable.variableName;
    }

    @Override
    boolean match(final Statement statement) {
        if (statement != null && statement instanceof J.VariableDeclarations) {
            final J.VariableDeclarations variableDeclaration = (J.VariableDeclarations) statement;

            final J.VariableDeclarations.NamedVariable v = variableDeclaration.getVariables().get(0);

            boolean result = type == null || type.match(variableDeclaration.getTypeExpression().getType().toString());
            result = result && (variable == null || variable.match(v.getName()));
            result = result && (initializer == null || initializer.match(v.getInitializer().toString()));
            return result;

        }
        return false;
    }
}
