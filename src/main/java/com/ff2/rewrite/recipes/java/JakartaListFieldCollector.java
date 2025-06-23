package com.ff2.rewrite.recipes.java;

import java.util.stream.Collectors;

import org.openrewrite.ExecutionContext;
import org.openrewrite.NlsRewrite;
import org.openrewrite.Recipe;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.tree.J;

import lombok.Value;

//@AllArgsConstructor
//@NoArgsConstructor
@Value
//@Incubating(since = "1.0.0")
public class JakartaListFieldCollector extends Recipe {

    transient JakartaListFieldReport jakartaListFieldReport = new JakartaListFieldReport(this);

    public JakartaListFieldCollector() {
        System.out.println("JakartaListFieldCollector.constructor()");
    }

    @Override
    public @NlsRewrite.DisplayName String getDisplayName() {
        return "Vind alle classes met een, of meer,  List<...> attribuut";
    }

    @Override
    public @NlsRewrite.Description String getDescription() {
        return "Vind alle classes met een, of meer,  List<...> attribuut" + ".";
    }

    @Override
    public JavaIsoVisitor<ExecutionContext> getVisitor() {
        return new JavaIsoVisitor<>() {

            @Override
            public J.ClassDeclaration visitClassDeclaration(final J.ClassDeclaration pClassDeclaration,
                    final ExecutionContext executionContext) {
                System.out.printf("ReplaceJakartaListSetter.visitClassDeclaration: %s\n", pClassDeclaration.getName());
                final J.ClassDeclaration classDeclaration = super.visitClassDeclaration(pClassDeclaration, executionContext);

                System.out.printf("ReplaceJakartaListSetter.class - statements: %s\n", classDeclaration.getBody().getStatements().size());
                System.out.printf("ReplaceJakartaListSetter.class - J.VariableDeclarations: %s\n",
                        classDeclaration.getBody().getStatements().stream()
                                .filter(J.VariableDeclarations.class::isInstance).count());
                System.out.printf("ReplaceJakartaListSetter.class - List<...> attributes: %s\n",
                        classDeclaration.getBody().getStatements().stream()
                                .filter(J.VariableDeclarations.class::isInstance)
                                .map(s -> ((J.VariableDeclarations) s).getType().toString())
                                .collect(Collectors.toList())
                )
                ;

                classDeclaration.getBody()
                        .getStatements()
                        .stream()
                        .filter(J.VariableDeclarations.class::isInstance)
                        .filter(vd -> ((J.VariableDeclarations) vd).getTypeExpression() instanceof J.ParameterizedType)
                        .filter(vd -> "List".equals(
                                ((J.ParameterizedType) ((J.VariableDeclarations) vd).getTypeExpression()).getClazz().toString()))
                        .forEach(t -> {
                            final J.VariableDeclarations vDeclaration = (J.VariableDeclarations) t;

                            final Type vdType = R.type(classDeclaration.getType());
                            final Type attributeType = R.type(vDeclaration.getType());
                            final String attributeName = R.variableName(vDeclaration);
                            final String attributeGetter = "get" + attributeName.substring(0,1).toUpperCase() + attributeName.substring(1);

                            jakartaListFieldReport.insertRow(executionContext,
                                    new JakartaListFieldReport.Row(
                                            vdType.typeName,
                                            attributeType.typeName,
                                            attributeGetter
                                    ));
                        });

                return classDeclaration;
            }

        };

    }

}
