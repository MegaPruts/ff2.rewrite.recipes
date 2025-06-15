package com.ff2.rewrite.recipes.java;

import java.util.HashMap;
import java.util.Map;

public class MethodModificators {
    private final Map<String, MethodModificator> methodModificators = new HashMap<>();

    public void add(MethodModificator methodModificator) {
        methodModificators.put(methodModificator.name(), methodModificator);
    }

    public MethodModificator get(final String name) {
        return methodModificators.get(name);
    }

    //public boolean match(final J.MethodDeclaration method) {
    //    return methodModificators.values().stream().allMatch(c -> c.match(method.getBody().getStatements()));
    //}

    //public Optional<MethodMatcher> match(final Statement statement) {
    //    return methodModificators.values().stream().filter(c -> c.match(statement)).findFirst();
    //
    //}
}
