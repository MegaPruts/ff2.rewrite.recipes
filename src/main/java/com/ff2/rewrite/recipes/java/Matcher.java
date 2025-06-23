package com.ff2.rewrite.recipes.java;

import java.util.List;

import org.openrewrite.java.tree.Statement;

public abstract class Matcher {

    public String name;

    public Matcher(final String name) {
        this.name = name;
    }

    final boolean anyMatch(List<Statement> statements) {
        return statements.stream().anyMatch(this::match);
    }

    abstract boolean match(Statement statement);

    public String name() {
        return name;
    }

    abstract void reset();


}
