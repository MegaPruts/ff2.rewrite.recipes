package com.ff2.rewrite.recipes.java;

import static com.ff2.rewrite.recipes.java.R.*;
import static com.ff2.rewrite.recipes.java.R.methodInvocationMatcher;

import java.util.Optional;
import java.util.function.Supplier;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.openrewrite.ExecutionContext;
import org.openrewrite.NlsRewrite;
import org.openrewrite.Option;
import org.openrewrite.Recipe;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.Statement;

@AllArgsConstructor
@NoArgsConstructor
public class ReplaceJakartaListSetter extends Recipe {
    private static transient MethodMatchers methodMatchers;
    private static transient MethodModificators methodModificators;

    @Override
    public @NlsRewrite.DisplayName String getDisplayName() {
        return "Replace new Arraylist & setListAttribute with classInstance.getListAttribute";
    }

    @Override
    public @NlsRewrite.Description String getDescription() {
        return "Replaces : 'new ArrayList<>()' with getListAttribute() & Removes : 'setListAttribute'.";
    }

    @Option(displayName = "parentType", description = "De class die de List<..> bevat. Bijv. DeeltijdResponse")
    private String parentType;

    @Option(displayName = "attributeType", description = "Het attribuut die List<..> als type heeft. Bijv. 'java.util.List<why.now.Double>'")
    private String attributeType;

    @Option(displayName = "attributeGetter", description = "De getter om de List<..> op te halen. Bijv. 'getToegestaneUittreedPercentages'")
    private String attributeGetter;


    @Override
    public JavaIsoVisitor<ExecutionContext> getVisitor() {
        if (methodMatchers == null && methodModificators == null) {
            methodMatchers = new MethodMatchers();
            methodModificators = new MethodModificators();

            final Type _parentType = type(parentType);
            final Type _attributeType = type(attributeType);

            VariableDeclarationMatcher responseInstantiation = variableDeclarationMatcher().withName("responseInstantiation").withType(_parentType).withInitializer(R.Java.newInstruction());
            methodMatchers.add(responseInstantiation);

            VariableDeclarationMatcher attributeDeclaration = variableDeclarationMatcher().withName("attributeDeclaration").withType(_attributeType).withInitializer(R.Java.newInstruction());
            methodMatchers.add(attributeDeclaration);

            MethodInvocationMatcher methodInvocationMatcher = methodInvocationMatcher("methodInvocation", _parentType, _attributeType);
            methodMatchers.add(methodInvocationMatcher);

            // "response.getToegestaneUittreedPercentages()"
            final Supplier<String> methodInvocationSupplier = () -> "%s.%s()".formatted(methodInvocationMatcher.methodInvocation.getName().getSimpleName(), attributeGetter);

            methodModificators.add(variableDeclarationReplacer("attributeDeclaration", null, null, methodInvocationSupplier));
            methodModificators.add(collectedMethodRemover("methodInvocation"));
        }
        return new JavaIsoVisitor<>() {
            private boolean methodMatched;

            @Override
            public Statement visitStatement(Statement pStatement, ExecutionContext context) {
                final Statement statement = super.visitStatement(pStatement, context);
                if (statement instanceof J.Block) {
                    methodMatched = methodMatchers.match((J.Block) statement);
                }

                if (methodMatched) {
                    final Optional<Matcher> optionalMatcher = methodMatchers.match(statement);
                    if (optionalMatcher.isPresent()) {
                        final MethodModificator modificator = methodModificators.get(optionalMatcher.get().name);
                        if (modificator == null) return statement;

                        final Statement newStatement = modificator.statement(J.VariableDeclarations.class);

                        return newStatement;

                    }
                }
                return statement;
            }
        };


    }


//    private MethodModifier methodModifier() {
//        final MethodModifier methodModifier = new MethodModifier();
//
//        final Type objectType = type("DeeltijdResponse");
//        final Type attributeType = type("List<Double>");
//
//        methodModifier.add(variableDeclarationMatcher("objectDeclaration", objectType));
//        methodModifier.add(variableDeclarationMatcher("attributeDeclaration", attributeType));
//        methodModifier.add(methodInvocationMatcher("methodInvocation", objectType, attributeType));
//
//        final Supplier<String> variableTypeSupplier = () -> variableType(methodModifier.currentStatement(J.VariableDeclarations.class));
//        final Supplier<String> variableNameSupplier = () -> variableName(methodModifier.currentStatement(J.VariableDeclarations.class));
//        final Supplier<String> methodInvocationSupplier = () -> "response.getToegestaneUittreedPercentages()";
//
//        methodModifier.add(
//                variableDeclarationReplacer("attributeDeclaration", variableTypeSupplier, variableNameSupplier, methodInvocationSupplier));
//        methodModifier.add(collectedMethodRemover("methodInvocation"));
//
//        return methodModifier;
//    }

}
