package com.ff2.rewrite.recipes.java;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Pattern;

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

    public static List<String> unPackagedTypeNames(String typeName) {
        List<String> result = new ArrayList<>();

        // Match the outer type and the generic parameter
        Pattern pattern = Pattern.compile("([^<]+)<([^>]+)>");
        java.util.regex.Matcher matcher = pattern.matcher(typeName);

        if (matcher.matches()) {
            // Group 1 is the outer class, group 2 is the inner type parameter
            String outer = matcher.group(1);
            String inner = matcher.group(2);

            // Extract simple names
            String outerSimple = outer.substring(outer.lastIndexOf('.') + 1);
            String innerSimple = inner.substring(inner.lastIndexOf('.') + 1);

            result.add(outerSimple);
            result.add(innerSimple);
        } else {
            // No generic found, just return the simple class name
            String simple = typeName.substring(typeName.lastIndexOf('.') + 1);
            result.add(simple);
        }

        return result;
    }
    public static String stripPackages(String type) {
        // Remove outer package if present
        int angleStart = type.indexOf('<');
        int angleEnd = type.lastIndexOf('>');

        if (angleStart != -1 && angleEnd != -1 && angleEnd > angleStart) {
            String outer = type.substring(0, angleStart).trim();
            String inner = type.substring(angleStart + 1, angleEnd).trim();

            String simpleOuter = outer.substring(outer.lastIndexOf('.') + 1);
            String simpleInner = inner.substring(inner.lastIndexOf('.') + 1);

            return simpleOuter + "<" + simpleInner + ">";
        } else {
            // Not parameterized — just strip package
            return type.substring(type.lastIndexOf('.') + 1);
        }
    }

    public static class Java {
        public static NewInstruction newInstruction(){
            return new NewInstruction();
        }
        public static class NewInstruction implements Initializer {
            public Type type;

            @Override
            public boolean match(J.VariableDeclarations.NamedVariable variable) {
                @Nullable Expression initializer = variable.getInitializer();

                return initializer!=null && initializer instanceof J.NewClass;
            }

            public Initializer withType(Type type) {
                this.type=type;
                return this;
            }
        }

    }


    public static NewInstance newInstance(final String type) {
        return new NewInstance(type);
    }

    public static Type type(final String typeName) {
        return new Type(typeName);
    }

    public static Variable variable(final String variableName) {
        return new Variable(variableName);
    }

    public static VariableDeclarationMatcher variableDeclarationMatcher() {
        return new VariableDeclarationMatcher(null, null);
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
        final JavaType returnType = null;
        final List<JavaType> thrownExceptions = new ArrayList<>();
        final List<JavaType.FullyQualified> annotations = new ArrayList<>();
        final List<String> defaultValue = new ArrayList<>();
        final List<String> declaredFormalTypeNames = new ArrayList<>();
        return new JavaType.Method(managedReference, flagsBitMap, declaringType, name, returnType, parameterNames, parameterTypes, thrownExceptions, annotations, defaultValue, declaredFormalTypeNames);
    }
}
