package com.ff2.rewrite.recipes.java;

import static org.junit.jupiter.api.Assertions.*;
import static org.openrewrite.java.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.openrewrite.test.RewriteTest;

public class MigrateJaxbListSetterTest implements RewriteTest {

    //@Test
    //public void migrateMethodInvocation() {
    //    String csvSource = """
    //            objectType,attributeType,attributeGetter
    //            my.dumb.DeeltijdResponse,why.now.Double,getToegestaneUittreedPercentages
    //            """;
    //    final List<JaxbListSetterMigration> migrations = MigrateJaxbListSetter.getMigrations(csvSource);
    //    assertNotNull(migrations);
    //    assertEquals(1, migrations.size());
    //
    //    //final MigrateJaxbListSetter migrateJaxbListSetter = new MigrateJaxbListSetter(migrationFile,
    //    //        MigrateJaxbListSetter.getMigrations(Path.of(migrationFile)));
    //    //migrateJaxbListSetter.migrationFile = "MigrateJaxbListSetter_migrateMethodInvocation.csv";
    //
    //    rewriteRun(spec -> spec.recipe(new MigrateJaxbListSetter("dummyFileName", migrations)),
    //            java("""
    //                     package why.now;
    //                     public class Double{}
    //
    //                    """
    //
    //            ), java("""
    //                    package my.dumb;
    //                    import java.util.ArrayList;
    //                    import java.util.List;
    //                    import why.now.Double;
    //                    public class DeeltijdResponse {
    //                    protected List<Double> toegestaneUittreedPercentages;
    //                    public List<Double> getToegestaneUittreedPercentages() {
    //                        if (toegestaneUittreedPercentages == null) {
    //                            toegestaneUittreedPercentages = new ArrayList<>();
    //                        }
    //                        return this.toegestaneUittreedPercentages;
    //                    }
    //                    public void setToegestaneUittreedPercentages(List<Double> toegestaneUittreedPercentages) {}
    //                    }
    //                    """
    //
    //            ), java("""
    //                    import java.util.ArrayList;
    //                    import java.util.List;
    //                    import why.now.Double;
    //                    import my.dumb.DeeltijdResponse;
    //
    //                    public class ParametersDeeltijdDelegateImpl{
    //                    public DeeltijdResponse bepaalParametersDeeltijd() {
    //                        final DeeltijdResponse response = new DeeltijdResponse();
    //                        final List<Double> uittreedPercentages = new ArrayList<>();
    //                        response.setToegestaneUittreedPercentages(uittreedPercentages);
    //                        return response;
    //                    }
    //                    }
    //                    """, """
    //                    import java.util.ArrayList;
    //                    import java.util.List;
    //                    import why.now.Double;
    //                    import my.dumb.DeeltijdResponse;
    //
    //                    public class ParametersDeeltijdDelegateImpl{
    //                    public DeeltijdResponse bepaalParametersDeeltijd() {
    //                        final DeeltijdResponse response = new DeeltijdResponse();
    //                        final List<Double> uittreedPercentages= response.getToegestaneUittreedPercentages();
    //                        return response;
    //                    }
    //                    }
    //                    """
    //
    //            ));
    //}
    //
    //@Test
    //public void migrateMethodInvocation_met_onnodige_type_parameter_voor_ArrayList() {
    //    final String parentType = "my.dumb.DeeltijdResponse";
    //    final String attributeType = "java.util.List<why.now.Double>";
    //    final String attributeGetter = "getToegestaneUittreedPercentages";
    //    final String includeFiles = null;
    //    final String excludeFiles = null;
    //    rewriteRun(
    //            spec -> spec.recipe(new ReplaceJakartaListSetter(parentType, attributeType, attributeGetter, includeFiles, excludeFiles)),
    //            java("""
    //                     package why.now;
    //                     public class Double{}
    //
    //                    """
    //
    //            ), java("""
    //                    package my.dumb;
    //                    import java.util.ArrayList;
    //                    import java.util.List;
    //                    import why.now.Double;
    //                    public class DeeltijdResponse {
    //                    protected List<Double> toegestaneUittreedPercentages;
    //                    public List<Double> getToegestaneUittreedPercentages() {
    //                        if (toegestaneUittreedPercentages == null) {
    //                            toegestaneUittreedPercentages = new ArrayList<>();
    //                        }
    //                        return this.toegestaneUittreedPercentages;
    //                    }
    //                    public void setToegestaneUittreedPercentages(List<Double> toegestaneUittreedPercentages) {}
    //                    }
    //                    """
    //
    //            ), java("""
    //                    import java.util.ArrayList;
    //                    import java.util.List;
    //                    import why.now.Double;
    //                    import my.dumb.DeeltijdResponse;
    //
    //                    public class ParametersDeeltijdDelegateImpl{
    //                    public DeeltijdResponse bepaalParametersDeeltijd() {
    //                        final DeeltijdResponse target = new DeeltijdResponse();
    //                        final List<Double> uittreedPercentages = new ArrayList<Double>();
    //                        uittreedPercentages.clear();
    //                        target.setToegestaneUittreedPercentages(uittreedPercentages);
    //                        return target;
    //                    }
    //                    }
    //                    """, """
    //                    import java.util.ArrayList;
    //                    import java.util.List;
    //                    import why.now.Double;
    //                    import my.dumb.DeeltijdResponse;
    //
    //                    public class ParametersDeeltijdDelegateImpl{
    //                    public DeeltijdResponse bepaalParametersDeeltijd() {
    //                        final DeeltijdResponse target = new DeeltijdResponse();
    //                        final List<Double> uittreedPercentages= target.getToegestaneUittreedPercentages();
    //                        uittreedPercentages.clear();
    //                        return target;
    //                    }
    //                    }
    //                    """
    //
    //            ));
    //}

    @Test
    public void fJADSFSSaaa() {
        String csvSource = """
                objectType,attributeType,attributeGetter
                nl.apg.gpsv.relatiemanagementindividuelepersonen.inkomsten.v0100.model.InkomensOntvangersWSO,nl.apg.gpsv.relatiemanagementindividuelepersonen.inkomsten.v0100.model.InkomensOntvangerWSO,getOntvangers
                """;
        final List<JaxbListSetterMigration> migrations = MigrateJaxbListSetter.getMigrations(csvSource);
        assertNotNull(migrations);
        assertEquals(1, migrations.size());

        rewriteRun(spec -> spec.recipe(new MigrateJaxbListSetter("dummyFileName", migrations)),
                java("""
                        package nl.apg.gpsv.relatiemanagementindividuelepersonen.inkomsten.v0100.model;
                        public class InkomensOntvangerTO {}
                         """),
                java("""
                        package nl.apg.gpsv.relatiemanagementindividuelepersonen.inkomsten.v0100.model;
                        import java.util.List;
                                          
                        public class SimulatieResultaatTO {
                        public List<InkomensOntvangerTO> getOntvangers() {
                             return null;
                             }
                        }
                        """), java("""
                        package nl.apg.gpsv.relatiemanagementindividuelepersonen.inkomsten.v0100.model;
                        public class SimulatieResultaatWSO {
                        public void setOntvangers(InkomensOntvangersWSO targetOntvangers){}
                        }
                        """),

                java("""
                        package nl.apg.gpsv.relatiemanagementindividuelepersonen.inkomsten.v0100.model;
                        public class InkomensOntvangerWSO {}
                        """),
                java("""
                        package nl.apg.gpsv.relatiemanagementindividuelepersonen.inkomsten.v0100.model;
                        import java.util.List;
                                          
                        public class InkomensOntvangersWSO {
                            protected List<InkomensOntvangerWSO> ontvanger;
                               
                            public List<InkomensOntvangerTO> getOntvangers() {
                            return null;
                            }            
                            public List<InkomensOntvangerWSO> getOntvanger() {
                            return null;
                            }
                                                
                            public void setOntvangers(List<InkomensOntvangerWSO> wsoOntvangers){}
                                                
                                                
                            public void setOntvanger(List<InkomensOntvangerWSO> value) {}
                        }
                        """),

                java("""
                        import java.util.List;
                        import java.util.ArrayList;

                        import nl.apg.gpsv.relatiemanagementindividuelepersonen.inkomsten.v0100.model.InkomensOntvangerWSO;
                        import nl.apg.gpsv.relatiemanagementindividuelepersonen.inkomsten.v0100.model.InkomensOntvangersWSO;                      
                        import nl.apg.gpsv.relatiemanagementindividuelepersonen.inkomsten.v0100.model.InkomensOntvangerTO;                      
                        import nl.apg.gpsv.relatiemanagementindividuelepersonen.inkomsten.v0100.model.SimulatieResultaatTO;
                        import nl.apg.gpsv.relatiemanagementindividuelepersonen.inkomsten.v0100.model.SimulatieResultaatWSO;
                                        
                        public class ToSimulatieResultaatWSOMapper {
                         
                        public void performMapping(final SimulatieResultaatTO source, final SimulatieResultaatWSO target)  {
                        final InkomensOntvangersWSO targetOntvangers = new InkomensOntvangersWSO();
                        final List<InkomensOntvangerTO> ontvangers = new ArrayList<>();
                        final List<InkomensOntvangerWSO> wsoOntvangers = new ArrayList<>(); // modification
                        for (final InkomensOntvangerTO ontvanger : ontvangers) {
                        }
                        targetOntvangers.setOntvanger(wsoOntvangers);// to be deleted
                        target.setOntvangers(targetOntvangers);
                        }
                                        
                        private InkomensOntvangerWSO mapBean(InkomensOntvangerTO ontvanger){
                        return null;
                        }
                        }
                        """, """
                        import java.util.List;
                        import java.util.ArrayList;

                        import nl.apg.gpsv.relatiemanagementindividuelepersonen.inkomsten.v0100.model.InkomensOntvangerWSO;
                        import nl.apg.gpsv.relatiemanagementindividuelepersonen.inkomsten.v0100.model.InkomensOntvangersWSO;                      
                        import nl.apg.gpsv.relatiemanagementindividuelepersonen.inkomsten.v0100.model.InkomensOntvangerTO;                      
                        import nl.apg.gpsv.relatiemanagementindividuelepersonen.inkomsten.v0100.model.SimulatieResultaatTO;
                        import nl.apg.gpsv.relatiemanagementindividuelepersonen.inkomsten.v0100.model.SimulatieResultaatWSO;
                                       
                        public class ToSimulatieResultaatWSOMapper {
                         
                        public void performMapping(final SimulatieResultaatTO source, final SimulatieResultaatWSO target)  {
                        final InkomensOntvangersWSO targetOntvangers = new InkomensOntvangersWSO();
                        final List<InkomensOntvangerTO> ontvangers = new ArrayList<>();
                        final List<InkomensOntvangerWSO> wsoOntvangers = targetOntvangers.getOntvangers(); // modification
                        for (final InkomensOntvangerTO ontvanger : ontvangers) {
                        }// to be deleted
                        target.setOntvangers(targetOntvangers);
                        }
                                        
                        private InkomensOntvangerWSO mapBean(InkomensOntvangerTO ontvanger){
                        return null;
                        }
                        }
                                """));
    }

}
