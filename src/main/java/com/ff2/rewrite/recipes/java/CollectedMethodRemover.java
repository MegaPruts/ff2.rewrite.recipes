package com.ff2.rewrite.recipes.java;

import org.openrewrite.java.JavaTemplate;
import org.openrewrite.java.tree.Statement;

public class CollectedMethodRemover implements MethodModificator {

    public final String collectorName;

    public CollectedMethodRemover(final String collectorName) {
        this.collectorName = collectorName;

    }

    @Override
    public String name() {
        return collectorName;
    }

    @Override
    public JavaTemplate template(final Statement statement, final MethodMatchers methodMatchers) {
        return null;
    }

    @Override
    public Statement statement(final Class statementClass) {
        return null;
    }

}