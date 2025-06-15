package com.ff2.rewrite.recipes.java;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.Statement;

public class MethodMatchers {
    private final Map<String, Matcher> methodMatchers = new HashMap<>();

    public void add(Matcher methodMatcher) {
        methodMatchers.put(methodMatcher.name(), methodMatcher);
    }

    public boolean match(final J.Block block) {
        return methodMatchers.values().stream().allMatch(c -> c.match(block.getStatements()));
    }

    public Optional<Matcher> match(final Statement statement) {
        return methodMatchers.values().stream().filter(c -> c.match(statement)).findFirst();

    }

    public Matcher get(final String collectorName) {
        return methodMatchers.get(collectorName);
    }
}
