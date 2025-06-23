package com.ff2.rewrite.recipes.java;

import static com.ff2.rewrite.recipes.java.R.*;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.jspecify.annotations.Nullable;
import org.openrewrite.ExecutionContext;
import org.openrewrite.NlsRewrite;
import org.openrewrite.Option;
import org.openrewrite.Recipe;
import org.openrewrite.SourceFile;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.tree.Expression;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.Statement;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.java.Log;

@AllArgsConstructor
@NoArgsConstructor
@Log
@Setter
public class ReplaceJakartaListSetter extends Recipe {
    static {
        Logger rootLogger = LogManager.getLogManager().getLogger("");
        rootLogger.setLevel(Level.FINE);
        Arrays.stream(rootLogger.getHandlers()).forEach(h -> h.setLevel(Level.FINE));
    }


    //@NonNull
    @Option(displayName = "Type of the Parent", description = "De class die de List<..> bevat. Bijv. DeeltijdResponse")
    public String parentType;

    //@NonNull
    @Option(displayName = "Type of the Attribute",
            description = "Het attribuut die List<..> als type heeft. Bijv. 'java.util.List<why.now.Double>'")
    public String attributeType;

    //@NonNull
    @Option(displayName = "Method name of the attribute getter",
            description = "De getter om de List<..> op te halen. Bijv. 'getToegestaneUittreedPercentages'")
    public String attributeGetter;

    @Nullable
    @Option(displayName = "The filter for files to proces", description = "For instance: glob:**/*Migratable.java")
    public String includeFiles;

    @Nullable
    @Option(displayName = "The filter for files to exclude from processing",
            description = "For instance: glob:**/org/w3/_2001/xmlschema/*.java")
    public String excludeFiles;
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

        System.out.println("*------------------------");
        System.out.printf("parentType: %s\n", parentType);
        System.out.printf("attributeType: %s\n", attributeType);
        System.out.printf("attributeGetter: %s\n", attributeGetter);
        System.out.println("ReplaceJakartaListSetter.initializeMatchers.");

        final MethodMatchers methodMatchers = new MethodMatchers();
        final MethodModificators methodModificators = new MethodModificators();

        final Type _parentType = type(parentType);
        final Type _attributeType = type(attributeType);

        VariableDeclarationMatcher attributeDeclaration = variableDeclarationMatcher().withName("attributeDeclaration")
                .withType(_attributeType)
                .withInitializer(R.Java.newInstruction());
        methodMatchers.add(attributeDeclaration);

        MethodInvocationMatcher methodInvocationMatcher = methodInvocationMatcher("methodInvocation", _parentType, _attributeType);
        methodMatchers.add(methodInvocationMatcher);

        // "response.getToegestaneUittreedPercentages()"
        final Supplier<String> methodInvocationSupplier =
                () -> "%s.%s()".formatted(methodInvocationMatcher.methodInvocation.getName().getSimpleName(), attributeGetter);

        methodModificators.add(
                variableDeclarationModifier("attributeDeclaration", () -> methodInvocationMatcher.object(), () -> attributeGetter,
                        methodInvocationSupplier).with(() -> type(_attributeType.typeParameters.get(0))));
        methodModificators.add(removeStatementModifier(methodInvocationMatcher));

        return new ReplaceJakartaListVisitor(includeFiles, excludeFiles, methodMatchers, methodModificators);
    }

    private static class ReplaceJakartaListVisitor extends JavaIsoVisitor<ExecutionContext> {

        private final String includeFiles;
        private final String excludeFiles;
        private final MethodMatchers methodMatchers;
        private final MethodModificators methodModificators;

        private String currentMethod;
        private String currentClass;
        private boolean dumped;
        private boolean methodMatched;

        public ReplaceJakartaListVisitor(final String includeFiles, final String excludeFiles, final MethodMatchers methodMatchers,
                final MethodModificators methodModificators) {
            this.includeFiles = includeFiles;
            this.excludeFiles = excludeFiles;
            this.methodMatchers = methodMatchers;
            this.methodModificators = methodModificators;
        }

        @Override
        public boolean isAcceptable(final SourceFile sourceFile, final ExecutionContext executionContext) {
            // SourceFile matches a given filter
            return matches(sourceFile.getSourcePath(), includeFiles)
                    && (excludeFiles==null||!matches(sourceFile.getSourcePath(), excludeFiles))
                    && super.isAcceptable(sourceFile, executionContext);
        }

        //@Override
        //public Expression visitExpression(final Expression pExpression, final ExecutionContext executionContext) {
        //    //System.out.printf("ReplaceJakartaListSetter.visitExpression: %s\n", pExpression);
        //    final Expression expression = super.visitExpression(pExpression, executionContext);
        //
        //    //System.out.printf("methodMatched: %s\n", methodMatched);
        //    //System.out.printf("expressionType: %s\n", expression.getClass());
        //    if (currentClass == null && currentMethod == null)
        //        return expression;
        //    if (methodMatched && expression instanceof J.MethodInvocation) {
        //        J.MethodInvocation methodInvocation = (J.MethodInvocation) expression;
        //        final Optional<Matcher> optionalMatcher = methodMatchers.match(methodInvocation);
        //        if (optionalMatcher.isPresent()) {
        //            System.out.printf("ReplaceJakartaListSetter.matcher found : %s\n", optionalMatcher.get());
        //
        //            final MethodModificator modificator = methodModificators.get(optionalMatcher.get().name);
        //            if (modificator == null)
        //                return expression;
        //            System.out.printf("ReplaceJakartaListSetter.modificator found : %s\n", modificator);
        //
        //            final Statement newStatement =
        //                    modificator.modify(methodInvocation);
        //
        //            return newStatement;
        //
        //        } else {
        //            System.out.printf("No matcher found for: %s\n", expression);
        //        }
        //    }
        //
        //    return expression;
        //}

        //@Override
        //public Statement visitStatement(Statement pStatement, ExecutionContext context) {
        //
        //    final Statement statement = super.visitStatement(pStatement, context);
        //    if (currentClass == null && currentMethod == null)
        //        return statement;
        //
        //    System.out.printf(
        //            "ReplaceJakartaListSetter.visitStatement class: %s method: %s methodMatched: %s statementType: %s statement: %s\n",
        //            currentClass, currentMethod, methodMatched, statement.getClass(), statement.toString());
        //
        //    if (methodMatched) {
        //        if (methodMatchers.isEmpty())
        //            System.out.println("No matchers declared!!");
        //
        //        final Optional<Matcher> optionalMatcher = methodMatchers.match(statement);
        //        if (optionalMatcher.isPresent()) {
        //            System.out.printf("ReplaceJakartaListSetter.matcher found : %s\n", optionalMatcher.get());
        //
        //            final MethodModificator modificator = methodModificators.get(optionalMatcher.get().name);
        //            if (modificator == null)
        //                return statement;
        //            System.out.printf("ReplaceJakartaListSetter.modificator found : %s\n", modificator);
        //
        //            final Statement newStatement = modificator.modify(statement, J.VariableDeclarations.class);
        //
        //            return newStatement;
        //
        //        }
        //    }
        //    return statement;
        //}

        //@Override
        //public J.ClassDeclaration visitClassDeclaration(final J.ClassDeclaration classDecl, final ExecutionContext executionContext) {
        //    currentClass = classDecl.getName().toString();
        //    System.out.printf("\nReplaceJakartaListSetter.visitClassDeclaration: %s\n", classDecl.getName());
        //
        //    return super.visitClassDeclaration(classDecl, executionContext);
        //}
        //
        //@Override
        //public J.MethodDeclaration visitMethodDeclaration(final J.MethodDeclaration method, final ExecutionContext executionContext) {
        //    if (method.getBody() != null) {
        //        currentMethod = method.getName().getSimpleName();
        //        System.out.printf("ReplaceJakartaListSetter.methodDeclaration class: %s method: %s methodMatched: %s\n", currentClass,
        //                currentMethod, methodMatched);
        //
        //        methodMatchers.resetAll();
        //        if (methodMatchers.isEmpty())
        //            System.out.println("ReplaceJakartaListSetter.methodDeclaration: No matchers declared !!");
        //        setMethodMatched(methodMatchers.isEmpty() ? false : methodMatchers.allMatch(method.getBody()) || methodMatched);
        //        if (!methodMatchers.isEmpty() && !methodMatched)
        //            System.out.println("ReplaceJakartaListSetter.methodDeclaration: Method not matched !!");
        //    }
        //    return super.visitMethodDeclaration(method, executionContext);
        //}
        //
        //private void setMethodMatched(final boolean matched) {
        //    methodMatched = matched;
        //    System.out.printf("ReplaceJakartaListSetter.methodMatch : %s\n", methodMatched);

        //}

    }

}
