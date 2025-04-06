# **Rapport \- Application de Liste d'Épicerie \- TP3**

## **Ce que fait le projet**

(Meme chose que le TP2)

Notre projet est une application simple de liste d'épicerie en ligne de commande qui permet de gérer les articles à acheter. L'utilisateur peut ajouter des articles, consulter sa liste et supprimer des articles qu'il ne souhaite plus acheter.

L'application stocke la liste d'épicerie dans un fichier JSON ou CSV, ce qui permet de conserver les informations même après avoir fermé le programme. C'est pratique pour maintenir une liste permanente qui peut être mise à jour au fil du temps.

## **Fonctionnalités principales**

**Mettre le Fichier en paramètre des commandes afin “d’alléger” le main**

**Ajouter une Catégorie aux Objets** 

## **Modifications principales**

1. **J'ai ajouté un attribut `category` à la classe `GroceryItem`**  
2. **J'ai créé une option `--category` (ou `-c`) pour la ligne de commande**  
3. **J'ai modifié l'affichage pour grouper les articles par catégorie**  
4. **J'ai mis à jour les formats JSON et CSV pour stocker les catégories**

## **Comment nous avons structuré notre code**

Comparé à la semaine dernière, on a décidé de revoir la structure de notre code. Dans notre rendus, le “Main” avait la main sur toutes les actions et avait les informations du fichier à tout moment. Il avait donc beaucoup de responsabilités. Nous avons donc décidé de réduire ses responsabilités de la manière suivante : 

- On a changer nos classes en retirant le paramètre static  
- On a créé un constructeur pour chaque classe qui prend le fichier comme paramètre.  
- On appelle les fonctions des classes en créant des instances de nos classes.

## **Les défis que nous avons rencontrés**

- ### **Réorganiser notre Main et nos classes** 

Au début, on avait beaucoup de mal à comprendre ce que vous vouliez dire par alléger le main, mais après avoir réalisé l'amélioration sur une de nos classes, c'était beaucoup plus simple a comprendre.

## **Ce que nous avons appris**

1. **Mieux organiser notre code et les fonctionnalités** :On a appris à mieux simplifier le code et on a commencé à mieux comprendre

## **Améliorations futures ?**

Comme future amélioration, on a pensé à créer une classe pour les commandes toujours dans l'optique d’alléger le main.

