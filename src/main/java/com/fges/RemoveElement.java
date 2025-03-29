package com.fges;

import java.util.Map;

/**
 * Classe pour gérer la suppression d'éléments de la liste
 */
public class RemoveElement {

    /**
     * Supprime complètement un élément de la liste
     * @param items Map des articles existants
     * @param name Nom de l'article à supprimer
     * @return true si l'élément a été supprimé, false sinon
     */
    public static boolean remove(Map<String, GroceryItem> items, String name) {
        return items.remove(name) != null;
    }

    /**
     * Réduit la quantité d'un élément de la liste
     * Si la quantité devient ≤ 0, l'élément est supprimé
     * @param items Map des articles existants
     * @param name Nom de l'article
     * @param quantity Quantité à retirer
     * @return true si la quantité a été réduite, false sinon
     */
    public static boolean removeQuantité(Map<String, GroceryItem> items, String name, int quantity) {
        if (!items.containsKey(name) || quantity <= 0) {
            return false;
        }

        GroceryItem item = items.get(name);
        int newQuantity = item.getQuantity() - quantity;

        if (newQuantity <= 0) {
            items.remove(name);
        } else {
            item.setQuantity(newQuantity);
        }

        return true;
    }
}