package com.ff2.rewrite.recipes.java;

import static com.ff2.rewrite.recipes.java.R.*;
import static java.util.Arrays.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.openrewrite.Tree;
import org.openrewrite.java.JavaTemplate;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JContainer;
import org.openrewrite.java.tree.JRightPadded;
import org.openrewrite.java.tree.JavaType;
import org.openrewrite.java.tree.Space;
import org.openrewrite.java.tree.Statement;
import org.openrewrite.marker.Markers;

public class VariableDeclarationReplacer implements MethodModificator<J.VariableDeclarations> {
    public final String collectorName;

    public final Supplier<String> object;
    public final Supplier<String> methodName;
    public final Supplier<String> parameters;

    public VariableDeclarationReplacer(final String collectorName, final Supplier<String> object, final Supplier<String> methodName,
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

    @Override
    public JavaTemplate template(final Statement statement, final MethodMatchers methodMatchers) {
        final Object matcher = methodMatchers.get(collectorName);
        J.VariableDeclarations variableDeclaration = (J.VariableDeclarations) statement;
        final List<J.Modifier> modifiers = variableDeclaration.getModifiers();

        String variableType = variableDeclaration.getType().toString();
        //variableType = "List<Double>";
        //final String[] typePartsArray = variableDeclaration.getType().toString().split("\\.");
        //String simpleType = null;
        //if (typePartsArray.length > 0) {
        //    simpleType = typePartsArray[typePartsArray.length - 1];
        //}
        final J.Identifier variableName = variableDeclaration.getVariables().get(0).getName();

        String typeInfo = "#{any(java.util.List<why.now.Double>)}";
        //typeInfo = "#{any(List<Double>)}";

        final String newVariableDeclaration =
                String.format("%s %s %s = %s;", modifiersSting(modifiers), variableType, variableName, typeInfo);
        System.out.println(newVariableDeclaration);
        return JavaTemplate.builder(newVariableDeclaration).contextSensitive().build();
    }

    @Override
    public String[] arguments() {
        final String methodInvocation = methodInvocation(object == null ? null : object.get(), methodName == null ? null : methodName.get(),
                parameters == null ? null : parameters.get()).toString();
        final String[] result = new String[1];
        result[0] = methodInvocation;
        return result;
    }

    @Override
    public J.VariableDeclarations statement(final Class<J.VariableDeclarations> statementClass) {

        Markers markers = Markers.EMPTY;

        List<J.Annotation> annotations = new ArrayList<>();

        J.Modifier.Type finalModifierType = J.Modifier.Type.Final;
        J.Modifier finalModifier = new J.Modifier(Tree.randomId(), space, markers, null, finalModifierType, null);
        final List<J.Modifier> finalModifierList = asList(finalModifier);

        JavaType.Class listType = JavaType.ShallowClass.build("java.util.List");
        JavaType.Class doubleType = JavaType.ShallowClass.build("why.now.Double");

        J.Identifier listIdentifier = identifier().withSimpleName("List").withType(listType)
                //.withFieldType(listType)
                ;

        J.Identifier doubleIdentifier = identifier().withSimpleName("Double").withType(doubleType)
                //.withFieldType(doubleType)
                ;

        J.ParameterizedType listOfDouble =
                jparameterizedType().withClazz(listIdentifier).withType(listType).withTypeParameters(asList(doubleIdentifier));

        JContainer<J.TypeParameter> doubleTypeParameters = JContainer.build(Collections.singletonList(JRightPadded.build(
                new J.TypeParameter(Tree.randomId(), space, markers, annotations, finalModifierList, doubleIdentifier, null))));

        J.Identifier response = identifier().withSimpleName("response").withType(JavaType.ShallowClass.build("DeeltijdResponse"));

        J.Identifier getToegestaneUittreedPercentages =
                identifier().withSimpleName("getToegestaneUittreedPercentages").withType(JavaType.ShallowClass.build("DeeltijdResponse"));

        J.MethodInvocation methodInvocation = jmethodInvocation().withSelect(response).withName(getToegestaneUittreedPercentages)
                .withMethodType(
                        jmethod()
                                .withDeclaringType(JavaType.ShallowClass.build("why.now.Double"))
                                .withName("getToegestaneUittreedPercentages")
                                .withReturnType(JavaType.ShallowClass.build("java.util.List"))
                );

        J.VariableDeclarations.NamedVariable namedVariable =
                jnamedVariable().withInitializer(methodInvocation).withName(identifier().withSimpleName("uittreedPercentages"));

        listOfDouble = listOfDouble.withPrefix(space);
        namedVariable = namedVariable.withPrefix(space);
        namedVariable = namedVariable.withInitializer(namedVariable.getInitializer().withPrefix(space));
        methodInvocation = methodInvocation.withPrefix(space);

        return variableDeclaration().withPrefix(Space.format("\n   "))
                .withModifiers(finalModifierList)
                //.withType(listOfDouble)
                .withTypeExpression(listOfDouble)
                .withVariables(asList(namedVariable));

        //new J.VariableDeclarations(Tree.randomId(), space, markers, annotations, finalModifierList, listIdentifier, doubleTypeParameters,
        //        null,
        //        Collections.singletonList(namedVariable), null);
    }

    private String modifiersSting(final List<J.Modifier> modifiers) {
        return String.join(" ", modifiers.stream().map(m -> m.toString()).collect(Collectors.toList()));
    }

}