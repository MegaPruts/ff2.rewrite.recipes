package com.ff2.rewrite.recipes.java;

import static com.ff2.rewrite.recipes.java.R.*;

import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.Statement;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VariableDeclarationMatcher extends Matcher {
    public Type type;
    public Variable variable;
    public Initializer initializer;
    private J.VariableDeclarations variableDeclaration;

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
            if (statement == this.variableDeclaration)
                return true;
            final J.VariableDeclarations variableDeclaration = (J.VariableDeclarations) statement;

            final J.VariableDeclarations.NamedVariable namedVariable = variableDeclaration.getVariables().get(0);

            boolean result = type == null || type.equals(declaredType(variableDeclaration))||type.equals(type(namedVariable.getType().toString()));
            result = result && (variable == null || variable.match(namedVariable.getName()));
            result = result && (initializer == null || initializer.match(namedVariable));
            log.debug("Statement: %s %s matched by %s".formatted(statement, result ? "" : "not", this));
            if("logischBlokAlgemeenVs".equals(namedVariable.getName().getSimpleName())){
                log.debug("final List<LogischBlokAlgemeenV> logischBlokAlgemeenVs = new ArrayList<>(); - declaredType: {}",declaredType(variableDeclaration).typeName);
                log.debug("final List<LogischBlokAlgemeenV> logischBlokAlgemeenVs = new ArrayList<>(); - variableType: {}",namedVariable.getType().toString());
            }
            if (result)
                this.variableDeclaration = variableDeclaration;
            return result;

        }
        if (statement != null)
            log.debug("Statement not the expected J.VariableDeclaration but: %s".formatted(statement.getClass()));

        return false;
    }

    @Override
    void reset() {
        this.variableDeclaration = null;
    }

    public VariableDeclarationMatcher withType(Type type) {
        this.type = type;
        return this;
    }

    public VariableDeclarationMatcher withInitializer(Initializer initializer) {
        this.initializer = initializer;
        return this;
    }

    public VariableDeclarationMatcher withName(String name) {
        this.name = name;
        return this;
    }

    public String toString() {
        return "name: %s type: %s variable: %s".formatted(this.name, this.type.typeName, this.variable);
    }
}
