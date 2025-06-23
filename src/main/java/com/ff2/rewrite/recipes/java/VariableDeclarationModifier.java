package com.ff2.rewrite.recipes.java;

import static com.ff2.rewrite.recipes.java.R.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.openrewrite.Tree;
import org.openrewrite.java.tree.Expression;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JContainer;
import org.openrewrite.java.tree.JLeftPadded;
import org.openrewrite.java.tree.JRightPadded;
import org.openrewrite.java.tree.JavaType;
import org.openrewrite.java.tree.Space;
import org.openrewrite.java.tree.Statement;
import org.openrewrite.marker.Markers;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VariableDeclarationModifier implements MethodModificator<J.VariableDeclarations> {
    public final String collectorName;
    public final Supplier<String> object;
    public final Supplier<String> methodName;
    public final Supplier<String> parameters;
    public Supplier<Type> objectTypeSupplier;

    public VariableDeclarationModifier(final String collectorName, final Supplier<String> object, final Supplier<String> methodName,
            Supplier<String> parameters) {
        this.collectorName = collectorName;
        this.object = object;
        this.methodName = methodName;
        this.parameters = parameters;

    }

    @Override
    public String name() {
        return collectorName;
    }

    //@Override
    //public JavaTemplate template(final Statement statement, final MethodMatchers methodMatchers) {
    //    final Object matcher = methodMatchers.get(collectorName);
    //    J.VariableDeclarations variableDeclaration = (J.VariableDeclarations) statement;
    //    final List<J.Modifier> modifiers = variableDeclaration.getModifiers();
    //
    //    String variableType = variableDeclaration.getType().toString();
    //    //variableType = "List<Double>";
    //    //final String[] typePartsArray = variableDeclaration.getType().toString().split("\\.");
    //    //String simpleType = null;
    //    //if (typePartsArray.length > 0) {
    //    //    simpleType = typePartsArray[typePartsArray.length - 1];
    //    //}
    //    final J.Identifier variableName = variableDeclaration.getVariables().get(0).getName();
    //
    //    String typeInfo = "#{any(java.util.List<why.now.Double>)}";
    //    //typeInfo = "#{any(List<Double>)}";
    //
    //    final String newVariableDeclaration =
    //            String.format("%s %s %s = %s;", modifiersSting(modifiers), variableType, variableName, typeInfo);
    //    System.out.println(newVariableDeclaration);
    //    return JavaTemplate.builder(newVariableDeclaration).contextSensitive().build();
    //}

    @Override
    public String[] arguments() {
        final String methodInvocation = methodInvocation(object == null ? null : object.get(), methodName == null ? null : methodName.get(),
                parameters == null ? null : parameters.get()).toString();
        final String[] result = new String[1];
        result[0] = methodInvocation;
        return result;
    }

    @Override
    public J.VariableDeclarations modify(final Statement statement) {
        final J.VariableDeclarations currentVariableDeclaration = (J.VariableDeclarations) statement;

        final Type currentType = declaredType(currentVariableDeclaration);
        final String currentVariableName = variableName(currentVariableDeclaration);

        Markers markers = Markers.EMPTY;

        List<J.Annotation> annotations = new ArrayList<>();

        J.Modifier.Type finalModifierType = J.Modifier.Type.Final;
        J.Modifier finalModifier = new J.Modifier(Tree.randomId(), Space.EMPTY, markers, null, finalModifierType, null);
        final List<J.Modifier> finalModifierList = asList(finalModifier);

        final Type objectType = objectTypeSupplier.get();
        //JavaType.Class variableType = JavaType.ShallowClass.build(objectType.shortTypeName);
        final String variableObject = this.object.get();
        J.Identifier listIdentifier = identifier().withSimpleName("List").withType(JavaType.ShallowClass.build("java.util.List"))
                //.withFieldType(listType)
                ;

        J.Identifier variableIdentifier =
                identifier().withType(JavaType.ShallowClass.build(currentType.shortTypeName))
                        .withSimpleName(currentType.typeParameters.get(0))
                //.withFieldType(variableType)
                ;

        //J.ParameterizedType listOfDouble =
        //        jparameterizedType().withClazz(listIdentifier).withType(listType).withTypeParameters(asList(variableIdentifier));

        JContainer<J.TypeParameter> doubleTypeParameters = JContainer.build(Collections.singletonList(JRightPadded.build(
                new J.TypeParameter(Tree.randomId(), space, markers, annotations, finalModifierList, variableIdentifier, null))));

        J.Identifier response = identifier().withSimpleName(object.get()).withType(JavaType.ShallowClass.build("DeeltijdResponse"));

        J.Identifier getToegestaneUittreedPercentages =
                identifier().withSimpleName(this.methodName.get()).withType(JavaType.ShallowClass.build("DeeltijdResponse"));

        J.MethodInvocation methodInvocation = jmethodInvocation().withSelect(response).withName(getToegestaneUittreedPercentages)
                .withMethodType(
                        jmethod()
                                .withDeclaringType(JavaType.ShallowClass.build("why.now.Double"))
                                .withName("getToegestaneUittreedPercentages")
                                .withReturnType(JavaType.ShallowClass.build("java.util.List"))
                );
        //@NonNull
        final JLeftPadded<Expression> initializer = JLeftPadded.build((Expression) methodInvocation.withPrefix(space)).withBefore(space);

        J.VariableDeclarations.NamedVariable namedVariable =
                jnamedVariable().withInitializer(methodInvocation).withName(identifier().withSimpleName(currentVariableName));

        J.ParameterizedType parameterizedType =
                jparameterizedType().withClazz(listIdentifier).withTypeParameters(asList(variableIdentifier));

        parameterizedType = parameterizedType.withPrefix(space);
        namedVariable = namedVariable.withPrefix(space);
        methodInvocation = methodInvocation.withPrefix(space);
        namedVariable = namedVariable.getPadding().withInitializer(initializer);
        //namedVariable = namedVariable.withInitializer((Expression) methodInvocation);
        //namedVariable = namedVariable.withDimensionsAfterName(asList(JLeftPadded.build(space)));
        //namedVariable.with

        final J.VariableDeclarations result = variableDeclaration()
                .withPrefix(Space.format("\n"))
                .withModifiers(finalModifierList)
                .withTypeExpression(parameterizedType)
                .withVariables(asList(namedVariable));
        log.info("Statement modified from %s to %s".formatted(statement, result));
        return result;

    }

    private String modifiersSting(final List<J.Modifier> modifiers) {
        return String.join(" ", modifiers.stream().map(m -> m.toString()).collect(Collectors.toList()));
    }

    public VariableDeclarationModifier with(Supplier<Type> objectTypeSupplier) {
        this.objectTypeSupplier = objectTypeSupplier;
        return this;
    }

}