package com.fges;

import java.util.Map;
import java.io.IOException;

/**
 * Classe pour gérer la suppression d'éléments de la liste
 */
public class RemoveElement {

    private File file;
    public RemoveElement(File file){
        this.file = file;
    }

    /**
     * Supprime complètement un élément de la liste
     * @param name liste des articles;
     */
    public void remove(String name) throws IOException{
        var items = file.entrée();
        var suppresion = items.remove(name);
        if (suppresion == null){
            throw new  IllegalArgumentException("Le produit n'existe pas");
        }
        file.sortie(items);
    }

    public void removeQuantité(String name, int quantity) throws IOException{
        var items = file.entrée();
        if (!items.containsKey(name) || quantity <= 0) {
            throw new IllegalArgumentException("Element inconnus ou quantité <= 0");
        }

        GroceryItem item = items.get(name);
        int newQuantity = item.getQuantity() - quantity;

        if (newQuantity <= 0) {
            items.remove(name);
        } else {
            item.setQuantity(newQuantity);
        }

        file.sortie(items);

    }
}