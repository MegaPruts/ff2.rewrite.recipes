package com.ff2.rewrite.recipes.java;

import java.util.Optional;

import org.openrewrite.ExecutionContext;
import org.openrewrite.NlsRewrite;
import org.openrewrite.Option;
import org.openrewrite.Recipe;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.tree.Expression;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.Statement;

//@Value
public class RecipeParameterDumper extends Recipe {

    @Option(displayName = "Type of the Parent", description = "De class die de List<..> bevat. Bijv. DeeltijdResponse")
    String parameterOne;
    private boolean getVisitor;

    public void setParameterOne(String pParameterOne) {
        this.parameterOne = pParameterOne;
    }

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
        
        if(!getVisitor) {
            System.out.println("RecipeParameterDumper.getVisitor()");
            getVisitor=true;
        }
        return new JavaIsoVisitor<>() {
            
            
            

            private boolean dumped;

            @Override
            public Expression visitExpression(final Expression expression, final ExecutionContext executionContext) {
                System.out.printf("ReplaceParameterDumper.visitExpression: %s\n", expression);
                return super.visitExpression(expression, executionContext);
            }



            @Override
            public J.Assignment visitAssignment(final J.Assignment assignment, final ExecutionContext executionContext) {
                System.out.printf("ReplaceParameterDumper.visitAssignment: %s\n", assignment);
                return super.visitAssignment(assignment, executionContext);
            }

            @Override
            public J.AssignmentOperation visitAssignmentOperation(final J.AssignmentOperation assignOp,
                    final ExecutionContext executionContext) {
                System.out.printf("ReplaceParameterDumper.visitAssignmentOperation: %s\n", assignOp);
                return super.visitAssignmentOperation(assignOp, executionContext);
            }

            @Override
            public J.ClassDeclaration visitClassDeclaration(final J.ClassDeclaration classDecl, final ExecutionContext executionContext) {
                System.out.printf("ReplaceParameterDumper.visitClassDeclaration: %s\n", classDecl);
                return super.visitClassDeclaration(classDecl, executionContext);
            }

            @Override
            public J.FieldAccess visitFieldAccess(final J.FieldAccess fieldAccess, final ExecutionContext executionContext) {
                System.out.printf("ReplaceParameterDumper.visitFieldAccess: %s\n", fieldAccess);
                return super.visitFieldAccess(fieldAccess, executionContext);
            }

            @Override
            public J.MemberReference visitMemberReference(final J.MemberReference memberRef, final ExecutionContext executionContext) {
                System.out.printf("ReplaceParameterDumper.visitMemberReference: %s\n", memberRef);
                return super.visitMemberReference(memberRef, executionContext);
            }

            @Override
            public J.VariableDeclarations visitVariableDeclarations(final J.VariableDeclarations multiVariable,
                    final ExecutionContext executionContext) {
                System.out.printf("ReplaceParameterDumper.visitVariableDeclarations: %s\n", multiVariable);
                return super.visitVariableDeclarations(multiVariable, executionContext);
            }


        };

    }

}
