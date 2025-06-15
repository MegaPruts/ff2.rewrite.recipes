package com.ff2.rewrite.recipes.java;

import org.openrewrite.java.tree.Expression;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType;
import org.openrewrite.java.tree.Statement;

public class MethodInvocationMatcher extends Matcher {
    public final Type type;
    public final Type parameterType;
    public J.MethodInvocation methodInvocation;

    public MethodInvocationMatcher(final String name, final Type type, final Type parameterType) {
        super(name);
        this.type = type;
        this.parameterType = parameterType;
    }

    @Override
    boolean match(final Statement statement) {
        if (statement != null && statement instanceof J.MethodInvocation) {
            final J.MethodInvocation methodInvocation = (J.MethodInvocation) statement;

            if (methodInvocation.getArguments() != null && methodInvocation.getArguments().size() > 0) {
                final Expression variable = methodInvocation.getArguments().get(0);
                if (variable instanceof J.Identifier) {
                    final JavaType parm = ((J.Identifier) variable).getFieldType().getType();
                    boolean result = type == null || type.match(methodInvocation.getMethodType().getDeclaringType().getClassName());
                    result = result && (parameterType == null || parameterType.match(parm.toString()));
                    System.out.printf("Statement: %s %s matched by %s\n", statement, result?"":"not", this);
                    if (result) this.methodInvocation = methodInvocation;

                    return result;
                }
            }
            System.out.printf("Statement: %s %s matched by %s\n", statement, "not", this);
        }
        return false;
    }
}
