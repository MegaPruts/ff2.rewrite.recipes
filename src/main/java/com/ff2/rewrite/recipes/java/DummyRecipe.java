package com.ff2.rewrite.recipes.java;

import org.openrewrite.NlsRewrite;
import org.openrewrite.Recipe;

public class DummyRecipe extends Recipe {

    public DummyRecipe() {
        System.out.println("DummyRecipe.constructor()");
    }

    @Override
    public @NlsRewrite.DisplayName String getDisplayName() {
        return "dummy";
    }

    @Override
    public @NlsRewrite.Description String getDescription() {
        return getDisplayName() + ".";
    }
}
