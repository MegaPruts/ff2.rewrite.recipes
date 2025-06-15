package com.ff2.rewrite.recipes.java;



import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.jspecify.annotations.Nullable;
import org.openrewrite.Tree;
import org.openrewrite.java.tree.Expression;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JContainer;
import org.openrewrite.java.tree.JavaType;
import org.openrewrite.java.tree.Space;
import org.openrewrite.marker.Markers;

public class R {
    public static final Space space = Space.format(" ");
    private static final List<?> EMTPY_LIST = new ArrayList<>();
    private static final Space EMPTY_SPACE = Space.EMPTY;

    public static NewInstance newInstance(final String type) {
        return new NewInstance(type);
    }

    public static Type type(final String typeName) {
        return new Type(typeName);
    }

    public static Variable variable(final String variableName) {
        return new Variable(variableName);
    }

    public static VariableDeclarationMatcher variableDeclarationMatcher(final String name, final Type type) {
        return new VariableDeclarationMatcher(name, type);
    }

    public static MethodInvocationMatcher methodInvocationMatcher(final String name, final Type type, final Type parameterType) {
        return new MethodInvocationMatcher(name, type, parameterType);
    }

    public static VariableDeclarationReplacer variableDeclarationReplacer(
            final String collectorName,
            final Supplier<String> object,
            final Supplier<String> method) {
        return variableDeclarationReplacer(collectorName, object, method, null);
    }

    public static VariableDeclarationReplacer variableDeclarationReplacer(
            final String collectorName,
            final Supplier<String> object,
            final Supplier<String> method,
            final Supplier<String> parameter) {
        return new VariableDeclarationReplacer(collectorName, object, method, parameter);
    }

    public static CollectedMethodRemover collectedMethodRemover(final String collectorName) {
        return new CollectedMethodRemover(collectorName);
    }

    public static String variableName(final J.VariableDeclarations variableDeclarations) {
        return variableDeclarations.getVariables().get(0).getName().toString();
    }

    public static String variableType(final J.VariableDeclarations variableDeclarations) {
        return variableDeclarations.getVariables().get(0).getType().toString();
    }

    public static MethodInvocation methodInvocation(final String object, final String method, final String parameter) {
        return new MethodInvocation(object, method, parameter);
    }

    public static J.VariableDeclarations variableDeclaration() {
        return new J.VariableDeclarations(
                Tree.randomId(),
                Space.EMPTY,
                Markers.EMPTY,
                null,
                null, null, null, new ArrayList<>(), new ArrayList<>());
    }

    public static J.VariableDeclarations.NamedVariable jnamedVariable() {
        return new J.VariableDeclarations.NamedVariable(null, null, null, null, new ArrayList<>(), null, null)
                .withId(Tree.randomId())
                .withPrefix(Space.EMPTY)
                .withMarkers(Markers.EMPTY);
    }

    public static J.MethodInvocation jmethodInvocation() {
        return new J.MethodInvocation(Tree.randomId(), Space.EMPTY, Markers.EMPTY, null, null, null, jcontainer(), null)
                .withId(Tree.randomId())
                .withPrefix(Space.EMPTY)
                .withMarkers(Markers.EMPTY)
                ;
    }

    public static JContainer<Expression> jcontainer() {
        return JContainer.build(Space.EMPTY, new ArrayList<>(), Markers.EMPTY);
    }

    public static J.Identifier identifier() {
        return new J.Identifier(Tree.randomId(), EMPTY_SPACE, Markers.EMPTY, null, null, null, null);
    }

    public static J.ParameterizedType jparameterizedType() {
        return new J.ParameterizedType(Tree.randomId(), EMPTY_SPACE, Markers.EMPTY, null, null, null);
    }

    public static JavaType.Method jmethod() {
        final List<JavaType> parameterTypes = new ArrayList<>();
        final List<String> parameterNames = new ArrayList<>();

        final Integer managedReference = null;
        final long flagsBitMap = 0L;
        final JavaType.FullyQualified declaringType = null;
        final String name = null;
        final JavaType returnType=null;
        final List<JavaType> thrownExceptions = new ArrayList<>();
        final  List<JavaType.FullyQualified>annotations=new ArrayList<>();
        final List<String> defaultValue =new ArrayList<>();
        final List<String> declaredFormalTypeNames = new ArrayList<>();
        return new JavaType.Method(managedReference, flagsBitMap, declaringType,name,returnType, parameterNames, parameterTypes, thrownExceptions, annotations, defaultValue, declaredFormalTypeNames);
    }
}
