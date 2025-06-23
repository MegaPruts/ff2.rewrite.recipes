package com.ff2.rewrite.recipes.java;

import static org.openrewrite.java.Assertions.*;

import org.junit.jupiter.api.Test;
import org.openrewrite.test.RewriteTest;

public class ReplaceJakartaListSetterTest implements RewriteTest {
    //@BeforeAll
    //public static void setupLogging() {
    //    Logger rootLogger = LogManager.getLogManager().getLogger("");
    //    rootLogger.setLevel(Level.FINE);
    //    Arrays.stream(rootLogger.getHandlers()).forEach(h -> h.setLevel(Level.FINE));
    //}

    @Test
    public void migrateMethodInvocation() {
        final String parentType = "my.dumb.DeeltijdResponse";
        final String attributeType = "java.util.List<why.now.Double>";
        final String attributeGetter = "getToegestaneUittreedPercentages";
        final String includeFiles = null;
        final String excludeFiles = null;
        rewriteRun(spec -> spec.recipe(
                        new ReplaceJakartaListSetter(parentType, attributeType,
                                attributeGetter, includeFiles, excludeFiles)),
                java("""
                         package why.now;
                         public class Double{}
                                        
                        """

                ), java("""
                        package my.dumb;
                        import java.util.ArrayList;
                        import java.util.List;
                        import why.now.Double;
                        public class DeeltijdResponse {
                        protected List<Double> toegestaneUittreedPercentages;
                        public List<Double> getToegestaneUittreedPercentages() {
                            if (toegestaneUittreedPercentages == null) {
                                toegestaneUittreedPercentages = new ArrayList<>();
                            }
                            return this.toegestaneUittreedPercentages;
                        }
                        public void setToegestaneUittreedPercentages(List<Double> toegestaneUittreedPercentages) {}
                        }
                        """

                ), java("""
                        import java.util.ArrayList;
                        import java.util.List;
                        import why.now.Double;
                        import my.dumb.DeeltijdResponse;
                                                
                        public class ParametersDeeltijdDelegateImpl{
                        public DeeltijdResponse bepaalParametersDeeltijd() {
                            final DeeltijdResponse response = new DeeltijdResponse();
                            final List<Double> uittreedPercentages = new ArrayList<>();
                            response.setToegestaneUittreedPercentages(uittreedPercentages);
                            return response;
                        }
                        }
                        """, """
                        import java.util.ArrayList;
                        import java.util.List;
                        import why.now.Double;
                        import my.dumb.DeeltijdResponse;
                                                
                        public class ParametersDeeltijdDelegateImpl{
                        public DeeltijdResponse bepaalParametersDeeltijd() {
                            final DeeltijdResponse response = new DeeltijdResponse();
                            final List<Double> uittreedPercentages= response.getToegestaneUittreedPercentages();
                            return response;
                        }
                        }
                        """

                ));
    }

    @Test
    public void migrateMethodInvocation_met_onnodige_type_parameter_voor_ArrayList() {
        final String parentType = "my.dumb.DeeltijdResponse";
        final String attributeType = "java.util.List<why.now.Double>";
        final String attributeGetter = "getToegestaneUittreedPercentages";
        final String includeFiles = null;
        final String excludeFiles = null;
        rewriteRun(spec -> spec.recipe(
                        new ReplaceJakartaListSetter(parentType, attributeType,
                                attributeGetter, includeFiles, excludeFiles)),
                java("""
                         package why.now;
                         public class Double{}
                                        
                        """

                ), java("""
                        package my.dumb;
                        import java.util.ArrayList;
                        import java.util.List;
                        import why.now.Double;
                        public class DeeltijdResponse {
                        protected List<Double> toegestaneUittreedPercentages;
                        public List<Double> getToegestaneUittreedPercentages() {
                            if (toegestaneUittreedPercentages == null) {
                                toegestaneUittreedPercentages = new ArrayList<>();
                            }
                            return this.toegestaneUittreedPercentages;
                        }
                        public void setToegestaneUittreedPercentages(List<Double> toegestaneUittreedPercentages) {}
                        }
                        """

                ), java("""
                        import java.util.ArrayList;
                        import java.util.List;
                        import why.now.Double;
                        import my.dumb.DeeltijdResponse;
                                                
                        public class ParametersDeeltijdDelegateImpl{
                        public DeeltijdResponse bepaalParametersDeeltijd() {
                            final DeeltijdResponse target = new DeeltijdResponse();
                            final List<Double> uittreedPercentages = new ArrayList<Double>();
                            uittreedPercentages.clear();
                            target.setToegestaneUittreedPercentages(uittreedPercentages);
                            return target;
                        }
                        }
                        """, """
                        import java.util.ArrayList;
                        import java.util.List;
                        import why.now.Double;
                        import my.dumb.DeeltijdResponse;
                                                
                        public class ParametersDeeltijdDelegateImpl{
                        public DeeltijdResponse bepaalParametersDeeltijd() {
                            final DeeltijdResponse target = new DeeltijdResponse();
                            final List<Double> uittreedPercentages= target.getToegestaneUittreedPercentages();
                            uittreedPercentages.clear();
                            return target;
                        }
                        }
                        """

                ));
    }

    @Test
    public void fJADSFSSaaa() {
        final String parentType = "nl.apg.gpsv.relatiemanagementindividuelepersonen.inkomsten.v0100.model.InkomensOntvangersWSO";
        final String attributeType =
                "java.util.List<nl.apg.gpsv.relatiemanagementindividuelepersonen.inkomsten.v0100.model.InkomensOntvangerWSO>";
        final String attributeGetter = "getOntvanger";
        final String includeFiles = null;
        final String excludeFiles = null;
        rewriteRun(spec -> spec.recipe(
                        new ReplaceJakartaListSetter(parentType, attributeType,
                                attributeGetter, includeFiles, excludeFiles)),
                java("""
                        public class InkomensOntvangerTO {}
                         """),
                java("""
                        import java.util.List;
                                          
                        public class SimulatieResultaatTO {
                        public List<InkomensOntvangerTO> getOntvangers() {
                             return null;
                             }
                        }
                        """),
                java("""       
                        import nl.apg.gpsv.relatiemanagementindividuelepersonen.inkomsten.v0100.model.InkomensOntvangersWSO;
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
                                           
                            public List<InkomensOntvangerWSO> getOntvanger() {
                            return null;
                            }
                                                
                            public void setOntvanger(List<InkomensOntvangerWSO> wsoOntvangers){}
                                                
                                                
                            public void setOntvanger(List<InkomensOntvangerWSO> value) {}
                        }
                        """),

                java("""
                                 import java.util.List;
                                 import java.util.ArrayList;

                                 import nl.apg.gpsv.relatiemanagementindividuelepersonen.inkomsten.v0100.model.InkomensOntvangerWSO;
                                 import nl.apg.gpsv.relatiemanagementindividuelepersonen.inkomsten.v0100.model.InkomensOntvangersWSO;
                                 
                                                
                                 public class ToSimulatieResultaatWSOMapper {
                                 
                                 public void performMapping(final SimulatieResultaatTO source, final SimulatieResultaatWSO target)  {
                                final List<InkomensOntvangerTO> ontvangers = new ArrayList<>();
                                final List<InkomensOntvangerWSO> wsoOntvangers = new ArrayList<InkomensOntvangerWSO>();
                                final InkomensOntvangersWSO targetOntvangers = new InkomensOntvangersWSO();
                                for (final InkomensOntvangerTO ontvanger : ontvangers) {
                                }
                                targetOntvangers.setOntvanger(wsoOntvangers);
                                target.setOntvangers(targetOntvangers);
                                }
                                                
                                private InkomensOntvangerWSO mapBean(InkomensOntvangerTO ontvanger){
                                return null;
                                }
                                }
                                """
                        ,
                        """
                                 import java.util.List;
                                 import java.util.ArrayList;

                                 import nl.apg.gpsv.relatiemanagementindividuelepersonen.inkomsten.v0100.model.InkomensOntvangerWSO;
                                 import nl.apg.gpsv.relatiemanagementindividuelepersonen.inkomsten.v0100.model.InkomensOntvangersWSO;
                                 
                                                
                                 public class ToSimulatieResultaatWSOMapper {
                                 
                                 public void performMapping(final SimulatieResultaatTO source, final SimulatieResultaatWSO target)  {
                                final List<InkomensOntvangerTO> ontvangers = new ArrayList<>();
                                    final List<InkomensOntvangerWSO> wsoOntvangers = targetOntvangers.getOntvanger();
                                final InkomensOntvangersWSO targetOntvangers = new InkomensOntvangersWSO();
                                for (final InkomensOntvangerTO ontvanger : ontvangers) {
                                }
                                target.setOntvangers(targetOntvangers);
                                }
                                                
                                private InkomensOntvangerWSO mapBean(InkomensOntvangerTO ontvanger){
                                return null;
                                }
                                }
                                        """
                ));
    }

}
