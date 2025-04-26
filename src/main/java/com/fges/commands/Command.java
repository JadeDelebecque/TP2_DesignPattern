package com.fges.commands;

// Interface de base pour toutes les commandes de l'application
public interface Command {
    /**
     * Exécute la commande avec les arguments fournis
     * @param args Arguments de la ligne de commande
     * @return Code de retour (0 pour succès, autre pour erreur)
     * @throws Exception En cas d'erreur pendant l'exécution
     */
    int execute(String[] args) throws Exception;

    /**
     * Vérifie si cette commande peut traiter le nom de commande donné
     * @param commandName Nom de la commande à vérifier
     * @return true si cette commande peut traiter la demande
     */
    boolean canHandle(String commandName);

    /**
     * Retourne une description de l'utilisation de cette commande
     * @return Message d'aide pour cette commande
     */
    String getUsage();
}