package com.ff2.rewrite.recipes.java;

import static com.ff2.rewrite.recipes.java.R.*;

import java.util.Optional;
import java.util.function.Supplier;

import org.openrewrite.ExecutionContext;
import org.openrewrite.NlsRewrite;
import org.openrewrite.Recipe;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.tree.Expression;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType;
import org.openrewrite.java.tree.Statement;

public class ReplaceJakartaListSetter extends Recipe {
    private transient MethodMatchers methodMatchers = new MethodMatchers();
    private transient MethodModificators methodModificators = new MethodModificators();

    @Override
    public @NlsRewrite.DisplayName String getDisplayName() {
        return "Replace new Arraylist & setListAttribute with classInstance.getListAttribute";
    }

    @Override
    public @NlsRewrite.Description String getDescription() {
        return "Replaces : 'new ArrayList<>()' with getListAttribute() & Removes : 'setListAttribute'.";
    }

    @Override
    public JavaIsoVisitor<ExecutionContext> getVisitor() {
        final Type objectType = type("DeeltijdResponse");
        final Type attributeType = type("java.util.List<why.now.Double>");

        methodMatchers.add(variableDeclarationMatcher("objectDeclaration", objectType));
        methodMatchers.add(variableDeclarationMatcher("attributeDeclaration", attributeType));
        methodMatchers.add(methodInvocationMatcher("methodInvocation", objectType, attributeType));

        //final Supplier<String> variableTypeSupplier = () -> variableType(methodModifier.currentStatement(J.VariableDeclarations.class));
        //final Supplier<String> variableNameSupplier = () -> variableName(methodModifier.currentStatement(J.VariableDeclarations.class));
        final Supplier<String> methodInvocationSupplier = () -> "response.getToegestaneUittreedPercentages()";

        methodModificators.add(
                variableDeclarationReplacer("attributeDeclaration", null, null, methodInvocationSupplier));
        methodModificators.add(collectedMethodRemover("methodInvocation"));

        return new JavaIsoVisitor<>() {
            private boolean methodMatched;

            @Override
            public Statement visitStatement(Statement pStatement, ExecutionContext context) {
                final Statement statement = super.visitStatement(pStatement, context);
                System.out.printf("Statement: %s\n", statement.toString());
                if (statement instanceof J.Block) {
                    methodMatched = methodMatchers.match((J.Block) statement);
                    System.out.printf("BlockMatched: %s\n", methodMatched);
                }

                if (methodMatched) {
                    final Optional<Matcher> optionalMatcher = methodMatchers.match(statement);
                    if (optionalMatcher.isPresent()) {
                        final MethodModificator modificator = methodModificators.get(optionalMatcher.get().name);
                        if (modificator == null)
                            return statement;

                        final Statement newStatement = modificator.statement(J.VariableDeclarations.class);

                        return newStatement;

                    }
                }
                return statement;
            }
        };
    }



    private MethodModifier methodModifier() {
        final MethodModifier methodModifier = new MethodModifier();

        final Type objectType = type("DeeltijdResponse");
        final Type attributeType = type("List<Double>");

        methodModifier.add(variableDeclarationMatcher("objectDeclaration", objectType));
        methodModifier.add(variableDeclarationMatcher("attributeDeclaration", attributeType));
        methodModifier.add(methodInvocationMatcher("methodInvocation", objectType, attributeType));

        final Supplier<String> variableTypeSupplier = () -> variableType(methodModifier.currentStatement(J.VariableDeclarations.class));
        final Supplier<String> variableNameSupplier = () -> variableName(methodModifier.currentStatement(J.VariableDeclarations.class));
        final Supplier<String> methodInvocationSupplier = () -> "response.getToegestaneUittreedPercentages()";

        methodModifier.add(
                variableDeclarationReplacer("attributeDeclaration", variableTypeSupplier, variableNameSupplier, methodInvocationSupplier));
        methodModifier.add(collectedMethodRemover("methodInvocation"));

        //Supplier<MethodInvocationReplacer> methodInvocationProvider = () -> methodInvocationProvider(
        //        methodModifier.variableDeclaration("objectDeclaration").variableName(),
        //        methodModifier.methodInvocation("methodInvocation").methodName.replace("set", "get")
        //);
        //
        //methodModifier.replace("attributeDeclaration", methodInvocationProvider);

        //Supplier<String> methodNameSupplier = () -> methodModifier.collector("listAssignment").variableName().replace("set", "get");
        //methodModifier.collect("theAssignment",
        //        MethodModifier.methodInvocation(object(() -> methodModifier.collector("objectDeclaration").variableName()),
        //                method(methodModifier.collector("listAssignment"), (String s) -> s.replace("set", "get"))),
        //        newInstance("ArrayList<>()")));

        //methodModifier.collect("listAssignment",
        //        MethodModifier.variableDeclaration()
        //                .withSelect(()->methodModifier.collector("objectDeclaration").variableName())
        //                .withArgument(collectorVariableName("listDeclaration")));
        //
        //methodModifier.collect("listAssignment")
        //        .with(J.MethodInvocation.class)
        //        .withSelect(CollectorVariableName("objectDeclaration"))
        //        .withArgument(CollectorVariableName("listDeclaration"));
        //
        //methodModifier.replace("listDeclaration")
        //        .with(MethodInvocation(CollectorVariableName("DeeltijdResponse"),
        //                MethodName(CollectorMethodName("listAssignment"), (String s) -> s.replace("set", "get"))));

        return methodModifier;
    }

}
