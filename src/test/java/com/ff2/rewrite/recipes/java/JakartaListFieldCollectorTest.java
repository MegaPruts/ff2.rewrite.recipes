package com.ff2.rewrite.recipes.java;

import static org.openrewrite.java.Assertions.*;

import org.junit.jupiter.api.Test;
import org.openrewrite.test.RewriteTest;

public class JakartaListFieldCollectorTest implements RewriteTest {
    //@BeforeAll
    //public static void setupLogging() {
    //    Logger rootLogger = LogManager.getLogManager().getLogger("");
    //    rootLogger.setLevel(Level.FINE);
    //    Arrays.stream(rootLogger.getHandlers()).forEach(h -> h.setLevel(Level.FINE));
    //}

    @Test
    public void migrateMethodInvocation() {
        rewriteRun(spec -> spec.recipe(new JakartaListFieldCollector()),
                java("""
                        package nl.apg.pensioenen.gpsv.wow.awouitgaandwowdossiergpsv.v0100;
                        public class Bijlage{}
                        """

                ), java("""
                        package nl.apg.pensioenen.gpsv.wow.awouitgaandwowdossiergpsv.v0100;
                                                
                        import java.util.ArrayList;
                        import java.util.List;
                                                
                        public class ArrayOfBijlage {
                            protected List<Bijlage> bijlage;
                            public List<Bijlage> getBijlage() {
                                if (bijlage == null) {
                                    bijlage = new ArrayList<Bijlage>();
                                }
                                return this.bijlage;
                            }
                            public void setBijlage(List<Bijlage> value) {
                                this.bijlage = value;
                            }
                        }
                        """));
    }
}

