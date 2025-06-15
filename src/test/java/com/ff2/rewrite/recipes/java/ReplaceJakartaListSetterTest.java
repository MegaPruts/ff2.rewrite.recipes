package com.ff2.rewrite.recipes.java;

import static org.openrewrite.java.Assertions.*;

import org.junit.jupiter.api.Test;
import org.openrewrite.test.RewriteTest;

public class ReplaceJakartaListSetterTest implements RewriteTest {

    @Test
    public void migrateMethodInvocation() {
        rewriteRun(spec -> spec.recipe(new ReplaceJakartaListSetter()), java("""
                 package why.now;
                 public class Double{}
                 
                """

        ), java("""
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
                                              
                public class ParametersDeeltijdDelegateImpl{
                public DeeltijdResponse bepaalParametersDeeltijd() {
                    final DeeltijdResponse response = new DeeltijdResponse();
                    final List<Double> uittreedPercentages = new ArrayList<>();
                    response.setToegestaneUittreedPercentages(uittreedPercentages);
                    return response;
                }
                }
                """,
                """
                import java.util.ArrayList;
                import java.util.List;
                import why.now.Double;               
                                              
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
}
