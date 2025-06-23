package com.ff2.rewrite.recipes.java;

import org.openrewrite.java.tree.Statement;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RemoveStatementModifier implements MethodModificator<Statement> {

    public final String collectorName;

    public RemoveStatementModifier(final String collectorName) {
        this.collectorName = collectorName;

    }

    @Override
    public String name() {
        return collectorName;
    }

    //@Override
    //public JavaTemplate template(final Statement statement, final MethodMatchers methodMatchers) {
    //    return null;
    //}

    @Override
    public Statement modify(final Statement statement) {
        log.info("Statement removed %s".formatted(statement));

        return null;
    }

}