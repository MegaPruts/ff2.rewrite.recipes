package com.ff2.rewrite.recipes.java;

import static com.ff2.rewrite.recipes.java.R.*;
import static org.junit.jupiter.api.Assertions.*;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;

public class MigrationJaxbListSetterTest {

    @Test
    public void test_getMigrations_from_inlineSource() {
        String csvSource = """
                objectType,attributeType,attributeGetter
                nl.abp.cis.pio.bepalen_processtap_keuze_gps.v0200.ArrOfLogischBlokAdditioneelGegeven,nl.abp.cis.pio.bepalen_processtap_keuze_gps.v0200.LogischBlokAdditioneelGegeven,getLogischBlokAdditioneelGegeven
                nl.abp.cis.pio.bepalen_processtap_keuze_gps.v0200.ArrOfLogischBlokSituatie,nl.abp.cis.pio.bepalen_processtap_keuze_gps.v0200.LogischBlokSituatie,getLogischBlokSituatie
                nl.abp.cis.pio.bepalen_processtap_keuze_gps.v0200.CallServiceEchoResponse$CallServiceEchoResult,java.lang.Object,getContent
                nl.abp.cis.pio.bepalenopbouwgegevens.v0100.ArrayOfLogischBlokAlgemeenA,nl.abp.cis.pio.bepalenopbouwgegevens.v0100.LogischBlokAlgemeenA,getLogischBlokAlgemeenA
                """;

        final List<JaxbListSetterMigration> migrations = MigrateJaxbListSetter.getMigrations(csvSource);
        assertNotNull(migrations);
        assertEquals(4, migrations.size());
        assertTrue(migrations.contains(string("nl.abp.cis.pio.bepalen_processtap_keuze_gps.v0200.ArrOfLogischBlokAdditioneelGegeven",
                "java.util.List<nl.abp.cis.pio.bepalen_processtap_keuze_gps.v0200.LogischBlokAdditioneelGegeven>")));

    }

    @Test
    public void test_migrations_from_csvFile() throws URISyntaxException {
        final String csvFileName = "JaxbListSetterMigrationLoaderTest.csv";

        final List<JaxbListSetterMigration> migrations =
                MigrateJaxbListSetter.getMigrations(Path.of(this.getClass().getClassLoader().getResource(csvFileName).toURI()));
        assertNotNull(migrations);
        assertEquals(4, migrations.size());

        assertTrue(migrations.contains(string("nl.abp.cis.pio.bepalen_processtap_keuze_gps.v0200.ArrOfLogischBlokSituatie",
                "java.util.List<nl.abp.cis.pio.bepalen_processtap_keuze_gps.v0200.LogischBlokSituatie>")));

    }
}
