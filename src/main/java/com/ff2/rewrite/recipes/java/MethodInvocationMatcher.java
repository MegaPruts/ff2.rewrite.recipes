package com.ff2.rewrite.recipes.java;

import static com.ff2.rewrite.recipes.java.R.*;

import org.openrewrite.java.tree.Expression;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType;
import org.openrewrite.java.tree.Statement;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
            if (statement == this.methodInvocation)
                return true;
            J.MethodInvocation methodInvocation = (J.MethodInvocation) statement;

            JavaType parm = null;
            if (methodInvocation.getArguments() != null && methodInvocation.getArguments().size() > 0) {
                final Expression variable = methodInvocation.getArguments().get(0);
                if (variable instanceof J.Identifier && ((J.Identifier) variable).getFieldType() != null) {
                    parm = ((J.Identifier) variable).getFieldType().getType();
                    boolean result = type == null || methodInvocation.getMethodType() != null && type.equals(
                            type(methodInvocation.getMethodType().getDeclaringType()));
                    result = result && (parameterType == null || parameterType.equals(type(((J.Identifier) variable).getFieldType().getType())));
                    log.debug("Statement: %s %s matched by %s".formatted( statement, result ? "" : "not", this));
                    if (result)
                        this.methodInvocation = methodInvocation;

                    return result;
                }
                log.debug("type %s =? %s\n".formatted(type.typeName, methodInvocation.getMethodType() == null
                        ? null
                        : methodInvocation.getMethodType().getDeclaringType().getClassName()));
                log.debug("parameterType %s =? %s".formatted(parameterType, parm));

            }
            log.debug("Statement: %s %s matched by %s".formatted(statement, "not", this));

        }
        if (statement != null)
            log.debug("Statement not the expected J.VariableDeclaration but: %s".formatted(statement.getClass()));

        return false;
    }

    @Override
    void reset() {
        this.methodInvocation = null;
    }

    public String object() {
        return methodInvocation == null ? null : ((J.Identifier) methodInvocation.getSelect()).getSimpleName();
    }

    public String toString() {
        return "name: %s type: %s parameterType: %s".formatted(this.name, this.type, this.parameterType);
    }
}
