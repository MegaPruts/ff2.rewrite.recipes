package com.ff2.rewrite.recipes.java;

public class MethodInvocation {
    public final String object;
    public final String method;
    public final String parameter;

    public MethodInvocation(final String object, final String method, final String parameter) {
        this.object = object;
        this.method = method;
        this.parameter = parameter == null ? "" : parameter;
    }

    public String toString() {
        if (object == null && method == null && parameter != null)
            return parameter;
        return String.format("%s.%s(%s)", object, method, parameter);
    }
}
