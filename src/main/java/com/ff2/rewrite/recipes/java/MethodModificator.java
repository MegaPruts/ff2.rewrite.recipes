package com.ff2.rewrite.recipes.java;

import org.openrewrite.java.tree.Statement;

public interface MethodModificator<T extends Statement> {
    String name();

    //JavaTemplate template(final Statement statement, final MethodMatchers methodMatchers);

    default String[] arguments() {
        return new String[0];
        //return null;
    }

    Statement modify(final Statement statement);


}
