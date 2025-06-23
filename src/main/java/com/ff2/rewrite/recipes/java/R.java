package com.ff2.rewrite.recipes.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jspecify.annotations.Nullable;
import org.openrewrite.Tree;
import org.openrewrite.java.tree.Expression;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JContainer;
import org.openrewrite.java.tree.JavaType;
import org.openrewrite.java.tree.Space;
import org.openrewrite.java.tree.Statement;
import org.openrewrite.marker.Markers;

public class R {
    public static final Space space = Space.format(" ");
    public static final Pattern TYPE_PARAMETER_SPLITTER_PATTERN = Pattern.compile("([^<]+)<([^>]+)>");
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

    public static Type declaredType(final J.VariableDeclarations variableDeclarations) {
        return type(variableDeclarations.getTypeExpression().getType());
    }

    public static Type type(final JavaType.FullyQualified declaringType) {
        return type(declaringType.getFullyQualifiedName());
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

    public static VariableDeclarationModifier variableDeclarationModifier(
            final String collectorName,
            final Supplier<String> object,
            final Supplier<String> method) {
        return variableDeclarationModifier(collectorName, object, method, null);
    }

    public static VariableDeclarationModifier variableDeclarationModifier(
            final String collectorName,
            final Supplier<String> object,
            final Supplier<String> method,
            final Supplier<String> parameter) {
        return new VariableDeclarationModifier(collectorName, object, method, parameter);
    }

    public static RemoveStatementModifier removeStatementModifier(final MethodInvocationMatcher methodInvocationMatcher) {
        return new RemoveStatementModifier(methodInvocationMatcher.name);
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
        final J.VariableDeclarations.NamedVariable namedVariable = new J.VariableDeclarations.NamedVariable(
                null,
                null,
                null,
                null,
                new ArrayList<>(),
                null,
                null
        )
                .withId(Tree.randomId())
                .withPrefix(Space.EMPTY)
                .withMarkers(Markers.EMPTY);
        //final J.VariableDeclarations.NamedVariable.Padding padding = namedVariable.getPadding();
        //padding.withInitializer()
        //return new J.VariableDeclarations.NamedVariable(
        //        padding.
        //        JLeftPadded.build(space),
        //        namedVariable.getId(),
        //        namedVariable.getPrefix(),
        //        namedVariable.getMarkers(),
        //        namedVariable.getName(),
        //        namedVariable.getDimensionsAfterName(),
        //        namedVariable.getInitializer(),
        //        namedVariable.getVariableType()
        //
        //        );
        return namedVariable;
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
        return new JavaType.Method(managedReference, flagsBitMap, declaringType, name, returnType, parameterNames, parameterTypes,
                thrownExceptions, annotations, defaultValue, declaredFormalTypeNames);
    }

    public static Type type(final JavaType type) {
        return type == null ? Type.NULL_TYPE : type(type.toString());
    }

    public static boolean matches(final Path sourcePath, final String filePathFilter) {
        return filePathFilter == null || matcher(filePathFilter).matches(sourcePath);
    }

    private static PathMatcher matcher(final String syntaxAndPattern) {
        return FileSystems.getDefault().getPathMatcher(syntaxAndPattern);
    }

    public static JavaStatementMofifier javaStatementMofifier() {
        return new JavaStatementMofifier();
    }

    public static <T> List<T> asList(final T... entries) {
        return Arrays.stream(entries).toList();
    }

    public static BufferedReader bufferedReader(final String csvSource) {
        return new BufferedReader(new StringReader(csvSource));
    }

    public static BufferedReader bufferedReader(final Path filePath) {
        try {
            return Files.newBufferedReader(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to build BufferedReader for: " + filePath, e);
        }
    }

    public static CSVRecordFormat csvRecordFormat(final String... csvFields) {
        return new CSVRecordFormat(csvFields);
    }

    public static String string(final String... stringArray) {
        return String.join("|", stringArray);
    }

    //public record Pair<T>(T first, T second) {
    //    public static <T> Pair<T> of(final T first, final T second) {
    //        return new Pair<>(first, second);
    //    }
    //}
    //public static String pair(String first, String second) {
    //    return new Pair<>(first, second);
    //}

    public static class Java {
        public static NewInstruction newInstruction() {
            return new NewInstruction();
        }

        public static class NewInstruction implements Initializer {
            public Type type;

            @Override
            public boolean match(J.VariableDeclarations.NamedVariable variable) {
                @Nullable Expression initializer = variable.getInitializer();

                return initializer != null && initializer instanceof J.NewClass;
            }

            public Initializer withType(Type type) {
                this.type = type;
                return this;
            }
        }

    }

    //public record String(String first, String second) {
    //}
    //
    //public static class PairDeserializer extends KeyDeserializer {
    //
    //    @Override
    //    public Object deserializeKey(String key, DeserializationContext context) throws IOException {
    //        String[] parts = key.split("\\|", 2);
    //        if (parts.length != 2) {
    //            throw new IllegalArgumentException("Invalid key format for Pair: " + key);
    //        }
    //        return pair(parts[0], parts[1]);
    //    }
    //}
    //
    //public static class ParSerializer extends JsonSerializer<Pair> {
    //
    //    @Override
    //    public void serialize(final Pair pair, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider)
    //            throws IOException {
    //        jsonGenerator.writeFieldName(pair.first + "|" + pair.second);
    //    }
    //}
    //
    //public static class PairModule extends SimpleModule {
    //    public PairModule() {
    //        addKeyDeserializer(Pair.class, new PairDeserializer());
    //        //addKeySerializer(Pair.class, new PairSerializer());
    //    }
    //}

    public static class JavaStatementMofifier {
        public final Matcher attributeDeclarationMatcher;
        private final MethodModificator attributeDeclarationModifier;

        private JavaStatementMofifier() {
            attributeDeclarationMatcher = null;
            attributeDeclarationModifier = null;
        }

        private JavaStatementMofifier(final Matcher attributeDeclarationMatcher,
                final MethodModificator attributeDeclarationModifier) {
            this.attributeDeclarationMatcher = attributeDeclarationMatcher;
            this.attributeDeclarationModifier = attributeDeclarationModifier;
        }

        public JavaStatementMofifier withMatcher(final Matcher attributeDeclarationMatcher) {
            return new JavaStatementMofifier(attributeDeclarationMatcher, this.attributeDeclarationModifier);
        }

        public JavaStatementMofifier withModifier(final MethodModificator attributeDeclarationModifier) {
            return new JavaStatementMofifier(this.attributeDeclarationMatcher, attributeDeclarationModifier);

        }

        public boolean matches(Statement statement) {
            final boolean result = attributeDeclarationMatcher.match(statement);
            return result;
        }

        public Statement modify(final Statement s) {
            return this.attributeDeclarationModifier.modify(s);
        }
    }

    public static class CSVReader {
        public final BufferedReader bufferedReader;
        private final CSVRecordFormat csvRecordFormat;

        private CSVReader(final BufferedReader bufferedReader) {
            this(bufferedReader, null);
        }

        private CSVReader(final BufferedReader bufferedReader, final CSVRecordFormat csvRecordFormat) {
            this.bufferedReader = bufferedReader;
            this.csvRecordFormat = csvRecordFormat;
        }

        public static CSVReader withReader(final BufferedReader bufferedReader) {
            return new CSVReader(bufferedReader);
        }

        public CSVReader withRecordFormat(final CSVRecordFormat csvRecordFormat) {
            return new CSVReader(this.bufferedReader, csvRecordFormat);
        }

        public <D> Stream<D> run(Function<CSVRecord, D> dataObjectSupplier) {
            try {
                final CSVParser parser = CSVFormat.DEFAULT
                        .withHeader(csvRecordFormat.header)
                        .withSkipHeaderRecord()
                        .parse(this.bufferedReader);
                return parser.stream().map(dataObjectSupplier);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class CSVRecordFormat {
        public String[] header;

        public CSVRecordFormat(final String[] csvFields) {
            this.header = csvFields;
        }
    }
}
