package com.ff2.rewrite.recipes.java;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.Statement;

public class MethodMatchers {
    private final Map<String, Matcher> methodMatchers = new HashMap<>();

    public void add(Matcher methodMatcher) {
        System.out.printf("Registering MethodMatcher: %s\n", methodMatcher.name);
        methodMatchers.put(methodMatcher.name(), methodMatcher);
    }

    public boolean allMatch(final J.Block block) {
        return methodMatchers.values().stream().allMatch(m -> m.anyMatch(block.getStatements()));
    }

    public Optional<Matcher> match(final Statement statement) {
        return methodMatchers.values().stream().filter(c -> c.match(statement)).findFirst();

    }

    public Matcher get(final String collectorName) {
        return methodMatchers.get(collectorName);
    }

    public void resetAll() {
        methodMatchers.values().stream().forEach(m -> m.reset());
    }

    public boolean isEmpty() {
        return this.methodMatchers.isEmpty();
    }

    public Map<String, Matcher> get() {
        return methodMatchers;
    }
}
