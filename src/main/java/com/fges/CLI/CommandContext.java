package com.fges.CLI;

/**
 * Encapsule les options spécifiques à une commande
 */
public class CommandContext {
    private String category;
    // Pour les nouvelles options on rajoute ça ici

    public CommandContext() {}

    public CommandContext(String category) {
        this.category = category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public boolean hasCategory() {
        return category != null && !category.isEmpty();
    }


    // Si y'a de nouveaux parametres on l'utilise ici
}