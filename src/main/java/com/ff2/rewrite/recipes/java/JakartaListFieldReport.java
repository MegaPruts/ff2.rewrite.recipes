package com.ff2.rewrite.recipes.java;

import org.openrewrite.Column;
import org.openrewrite.DataTable;

import lombok.Value;

public class JakartaListFieldReport extends DataTable<JakartaListFieldReport.Row> {

    public JakartaListFieldReport(JakartaListFieldCollector jakartaListFieldCollector) {
        super(jakartaListFieldCollector,
                "Jakarta listField report",
                "Classes met List<..> attributes"
        );
    }

    @Value
    public static class Row {
        @Column(displayName = "parentType",
                description = "Class met List<..> attributen"
        )
        String parentType;
        @Column(displayName = "attribute",
                description = "Attribute met type List<..>"
        )
        String attribute;
        @Column(displayName = "attributeGetter",
                description = "getter for the attribute"
        )
        String attributeGetter;
    }
}