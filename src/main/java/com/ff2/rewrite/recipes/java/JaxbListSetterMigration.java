package com.ff2.rewrite.recipes.java;

import static com.ff2.rewrite.recipes.java.R.*;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import org.apache.commons.csv.CSVRecord;
import org.openrewrite.SourceFile;
import org.openrewrite.java.tree.J;

public record JaxbListSetterMigration(String parentType, String attributeType, String attributeGetter,
                                      JavaStatementMofifier... javaStatementMofifiers) {

    public static JaxbListSetterMigration build(final CSVRecord csvRecord) {

        final String objectType = csvRecord.get("objectType");
        final String attributeType = "java.util.List<%s>".formatted(csvRecord.get("attributeType"));
        final String attributeGetter = csvRecord.get("attributeGetter");
        return build(objectType, attributeType, attributeGetter);
    }

    public static JaxbListSetterMigration build(final String objectType, final String attributeType, final String attributeGetter) {
        final Type _objectType = type(objectType);
        final Type _attributeType = type(attributeType);

        MethodInvocationMatcher methodInvocationMatcher = methodInvocationMatcher("methodInvocation", _objectType, _attributeType);
        final RemoveStatementModifier removeStatementModifier = removeStatementModifier(methodInvocationMatcher);
        final JavaStatementMofifier methodInvocationRemover = R.javaStatementMofifier()
                .withMatcher(methodInvocationMatcher)
                .withModifier(removeStatementModifier);

        final VariableDeclarationMatcher attributeDeclarationMatcher = variableDeclarationMatcher().withName("attributeDeclaration")
                .withType(_attributeType)
                .withInitializer(R.Java.newInstruction());

        final Supplier<String> methodInvocationSupplier =
                () -> "%s.%s()".formatted(methodInvocationMatcher.methodInvocation.getName().getSimpleName(), attributeGetter);

        final VariableDeclarationModifier variableDeclarationModifier =
                variableDeclarationModifier("attributeDeclaration", methodInvocationMatcher::object, () -> attributeGetter,
                        methodInvocationSupplier).with(() -> type(_attributeType.typeParameters.get(0)));

        final JavaStatementMofifier variableDeclarationMigration = R.javaStatementMofifier()
                .withMatcher(attributeDeclarationMatcher)
                .withModifier(variableDeclarationModifier);

        return new JaxbListSetterMigration(objectType, attributeType, attributeGetter, variableDeclarationMigration,
                methodInvocationRemover);
    }

    public boolean isApplicable(final SourceFile sourceFile) {
        final List<Type> necessaryImports =
                asList(type("java.util.List"), type("java.util.ArrayList"), type(parentType), type(attributeType).typeParameter(0));
        return sourceFile instanceof J.CompilationUnit && necessaryImports.stream()
                .allMatch(t -> ((J.CompilationUnit) sourceFile).getImports().stream().anyMatch(i -> t.equals(type(i.getTypeName()))));

    }

    public boolean isApplicable(final J.MethodDeclaration method) {
        final boolean result = method.getBody() != null && Arrays.stream(javaStatementMofifiers)
                .allMatch(jsm -> method.getBody().getStatements().stream().anyMatch(jsm::matches));
        return result;
    }

    //public void migrate(final Statement statement) {
    //    return this.
    //}
}
