package com.fges;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe principale pour gérer la liste d'épicerie
 */
public class GererListe {
    private File fichier;
    private Map<String, GroceryItem> items;
    private String filePath;

    /**
     * Crée une nouvelle instance de gestion de liste
     * @param filePath Chemin du fichier
     * @throws IOException En cas d'erreur de lecture du fichier
     */
    public GererListe(String filePath) throws IOException {
        this.filePath = filePath;
        this.fichier = new File();
        this.fichier.Fichier(filePath);
        this.items = this.fichier.entrée();
    }


    /**
     * Crée une nouvelle liste vide dans un fichier
     * @throws IOException En cas d'erreur d'écriture
     */
    public void créerListe() throws IOException {
        this.items = new HashMap<>();
    }

    /**
     * Modifie un élément dans la liste
     * @param name Nom de l'article
     * @param quantity Nouvelle quantité
     * @return true si la modification a réussi, false sinon
     * @throws IOException En cas d'erreur d'écriture
     */
    public boolean modifier(String name, int quantity) throws IOException {
        if (!items.containsKey(name) || quantity <= 0) {
            return false;
        }

        items.get(name).setQuantity(quantity);
        return true;
    }

    /**
     * Supprime la liste entière
     * @throws IOException En cas d'erreur de suppression
     */
    public void supprimer() throws IOException {
        this.items.clear();
        Files.deleteIfExists(Paths.get(filePath));
    }

    /**
     * Ajoute un élément à la liste
     * @param name Nom de l'article
     * @param quantity Quantité à ajouter
     * @return true si l'ajout a réussi, false sinon
     * @throws IOException En cas d'erreur d'écriture
     */
    public void ajouter(String name, int quantity) throws IOException {
        AddElement addElement = new AddElement(this.fichier);
        addElement.ajouterElement(name, quantity);
        this.items = this.fichier.entrée();
    }

    /**
     * Supprime un élément de la liste
     * @param name Nom de l'article à supprimer
     * @return true si la suppression a réussi, false sinon
     * @throws IOException En cas d'erreur d'écriture
     */
    public void enlever(String name) throws IOException {
        RemoveElement removeElement = new RemoveElement(this.fichier);
        removeElement.remove(name);
        this.items = this.fichier.entrée();
    }

    /**
     * Réduit la quantité d'un article
     * @param name Nom de l'article
     * @param quantity Quantité à retirer
     * @return true si la réduction a réussi, false sinon
     * @throws IOException En cas d'erreur d'écriture
     */
    public void réduireQuantité(String name, int quantity) throws IOException {
        RemoveElement removeElement = new RemoveElement(this.fichier);
        removeElement.removeQuantité(name, quantity);
        this.items = this.fichier.entrée();
    }

    /**
     * Affiche tous les éléments de la liste
     */
    public void afficher() {
        if (items.isEmpty()) {
            System.out.println("La liste est vide.");
        } else {
            for (GroceryItem item : items.values()) {
                System.out.println(item);
            }
        }
    }


    /**
     * Récupère la liste actuelle
     * @return Map des articles
     */
    public Map<String, GroceryItem> getItems() {
        return items;
    }
}