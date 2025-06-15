package com.ff2.rewrite.recipes.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.openrewrite.java.JavaTemplate;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.Statement;

public class MethodModifier {
    private final Map<String, Matcher> methodMatchers = new HashMap<>();
    private J.MethodDeclaration method;
    private Map<String, MethodModificator> methodModificators = new HashMap<>();
    private Statement currentStatement;

    public void add(Matcher methodMatcher) {
        methodMatchers.put(methodMatcher.name(), methodMatcher);
    }

    public void add(MethodModificator methodModificator) {
        methodModificators.put(methodModificator.name(), methodModificator);
    }

    public MethodModifier accept(final J.MethodDeclaration method) {
        this.method = method;
        return this;
    }

    private ArrayList<Statement> doReplacements(final J.MethodDeclaration method) {
        final ArrayList<Statement> newBody = new ArrayList<>();
        //for (Statement statement : method.getBody().getStatements()) {
        //    currentStatement=statement;
        //    JavaTemplate template = null;
        //    boolean modified = false;
        //    for (MethodMatcher methodMatcher : methodMatchers.values()) {
        //        if (methodMatcher.match(statement)) {
        //            if (methodModificators.containsKey(methodMatcher.name)) {
        //                template = methodModificators.get(methodMatcher.name).template(this);
        //                modified = true;
        //            }
        //        }
        //    }
        //    if (modified) {
        //        if (newStatement != null)
        //            newBody.add(template.apply(statement));
        //    } else
        //        newBody.add(statement);
        //}
        return newBody;

    }

    public J.MethodDeclaration perform() {
        if (methodMatchers.size() == 0)
            throw new RuntimeException("No matchers specified");

        if (methodMatchers.values().stream().allMatch(c -> c.match(method.getBody().getStatements()))) {
            final J.Block newBody = method.getBody().withStatements(doReplacements(method));
            return method.withBody(newBody);
        }
        return method;
    }

    public Matcher collector(final String collectorName) {
        return this.methodMatchers.get(collectorName);
    }

    public VariableDeclarationMatcher variableDeclaration(String collectorName) {
        return (VariableDeclarationMatcher) methodMatchers.get(collectorName);
    }

    public <T> T currentStatement(final Class<T> statementType) {
        return (T) currentStatement;
    }
}
