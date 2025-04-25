package com.fges.Commande;

import com.fges.File.File;
import com.fges.GroceryItem;

import java.io.IOException;

/**
 * Classe pour gérer la suppression d'éléments de la liste
 */
public class RemoveElement {

    private File file;

    public RemoveElement(File file){

        this.file = file;

    }

    public void remove(String name) throws IOException{

        /*
         * Supprime complètement un élément de la liste
         */

        var itemsList = file.loadFile();
        var deletion = itemsList.remove(name);
        if (deletion == null){
            throw new  IllegalArgumentException("Le produit n'existe pas");
        }
        file.saveFile(itemsList);
    }

    public void removeQuantity(String name, int quantity) throws IOException{
        /*
        * Retire une quantité précise d'un item
        */
        var itemsList = file.loadFile();
        if (!itemsList.containsKey(name) || quantity <= 0) {
            throw new IllegalArgumentException("Element inconnus ou quantité <= 0");
        }

        GroceryItem item = itemsList.get(name);
        int newQuantity = item.getQuantity() - quantity;

        if (newQuantity <= 0) {
            itemsList.remove(name);
        } else {
            item.setQuantity(newQuantity);
        }

        file.saveFile(itemsList);

    }
}