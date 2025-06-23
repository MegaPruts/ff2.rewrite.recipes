package com.ff2.rewrite.recipes.java;

import static com.ff2.rewrite.recipes.java.R.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.csv.CSVRecord;
import org.openrewrite.ExecutionContext;
import org.openrewrite.NlsRewrite;
import org.openrewrite.Option;
import org.openrewrite.Recipe;
import org.openrewrite.SourceFile;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.Statement;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
public class MigrateJaxbListSetter extends Recipe {

    private static final String METHOD_MIGRATIONS_TO_RUN = "migrations_to_run";
    //@NonNull
    @Option(displayName = "Path to the migration file", description = "Path to the migration file")
    public String migrationFile;
    private transient List<JaxbListSetterMigration> migrations;

    //public MigrateJaxbListSetter(final String migrationFile, final List<JaxbListSetterMigration> migrations) {
    //    this("unspecified", new HashMap<>());
    //}
    public MigrateJaxbListSetter() {
        this(null, null);
    }

    public MigrateJaxbListSetter(String migrationFile) {
        this(migrationFile, MigrateJaxbListSetter.getMigrations(Path.of(migrationFile)));
    }

    public MigrateJaxbListSetter(final String migrationFile, final List<JaxbListSetterMigration> migrations) {
        this.migrationFile = migrationFile;
        this.migrations = migrations;
    }

    public static List<JaxbListSetterMigration> getMigrations(final String csvData) {
        return getMigrations(bufferedReader(csvData));
    }

    public static List<JaxbListSetterMigration> getMigrations(final Path migrationFile) {
        log.info("Loading migrations from {}", migrationFile);

        final BufferedReader bufferedReader = bufferedReader(migrationFile);
        try (bufferedReader) {
            return getMigrations(bufferedReader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<JaxbListSetterMigration> getMigrations(final BufferedReader migrationFileReader) {
        final CSVRecordFormat migrationFileRecordFormat = csvRecordFormat("objectType", "attributeType", "attributeGetter");

        final List<JaxbListSetterMigration> migrations = CSVReader.withReader(migrationFileReader)
                .withRecordFormat(migrationFileRecordFormat)
                .run(JaxbListSetterMigration::build)
                .toList();
        //.collect(Collectors.toMap(m -> R.string(m.parentType(), m.attributeType()), Function.identity()));
        log.info("Loaded {} migrations.", migrations.size());
        return migrations;
    }

    private static JaxbListSetterMigration migration(CSVRecord csvRecord) {
        return JaxbListSetterMigration.build(csvRecord);
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
        if (migrations == null || migrations.size() == 0) {
            final Path migrationFilePath = Path.of(migrationFile);
            migrations = getMigrations(migrationFilePath);
        }
        return new MigrateJaxbListSetter.MigrateJaxbListSetterVisitor();
    }

    public class MigrateJaxbListSetterVisitor extends JavaIsoVisitor<ExecutionContext> {

        @Override
        public boolean isAcceptable(final SourceFile sourceFile, final ExecutionContext executionContext) {
            final boolean result = migrations.stream().anyMatch(m -> m.isApplicable(sourceFile));
            if (result)
                log.info("isAcceptable - Migration applicable for SourceFile {}", sourceFile.getSourcePath());
            else
                log.debug("isAcceptable - Migration not applicable for SourceFile {} migrationFile {} migrations {}",
                        sourceFile.getSourcePath(), migrationFile, migrations == null ? null : migrations.size());

            return result;
        }

        @Override
        public J.MethodDeclaration visitMethodDeclaration(final J.MethodDeclaration method, final ExecutionContext executionContext) {
            //final J.MethodDeclaration method = super.visitMethodDeclaration(pMethod);
            final List<JaxbListSetterMigration> migrationsToRun = migrations.stream().filter(m -> m.isApplicable(method)).toList();
            if (migrationsToRun.size() > 0) {
                log.info("visitMethodDeclaration - Found {} migrations for method {}", migrationsToRun.size(), method.getName());
                final List<JavaStatementMofifier> statementModifiers =
                        migrationsToRun.stream().flatMap(m -> Arrays.stream(m.javaStatementMofifiers())).toList();

                final List<Statement> newStatements = new ArrayList<>();

                boolean methodModified = false;
                for (Statement statement : method.getBody().getStatements()) {
                    final Optional<JavaStatementMofifier> statementModifier = statementModifiers.stream()
                            .filter(m -> m.matches(statement))
                            .findFirst();
                    if (statementModifier.isPresent()) {
                        final Statement modifiedStatement = statementModifier.get().modify(statement);
                        if (modifiedStatement != null) {
                            newStatements.add(modifiedStatement);
                            methodModified = true;
                        }
                    } else
                        newStatements.add(statement);
                }
                if (methodModified)
                    log.info("Method %s modified".formatted(method.getName()));

                return method.withBody(method.getBody().withStatements(newStatements));
            }

            return method;
        }
    }

}


