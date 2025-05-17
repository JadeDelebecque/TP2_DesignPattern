# Rapport Final - TP4 
Jade Delebecque
Carlos Okinda

## Ce que nous n'avons pas eu le temps de faire

Dans le cadre de ce projet, nous n'avons pas eu le temps d'implémenter une interface web plus élaborée. La commande "web" fonctionne correctement mais propose une interface assez basique. Nous aurions aimé ajouter des fonctionnalités comme le tri des produits directement dans l'interface web ou un système de filtrage plus avancé pour les catégories. Également, l'ajout d'un système d'authentification pour protéger les données aurait été intéressant.

## Ce qui était difficile

La partie la plus difficile était la mise en place de la connexion entre notre code Java existant et le serveur web. Il fallait s'assurer que les modifications apportées à la liste d'épicerie via l'interface web soient correctement synchronisées avec les fichiers locaux. Comprendre comment adapter notre système de fichiers (JSON/CSV) pour qu'il fonctionne correctement avec le serveur.

## Les design patterns utilisés et pourquoi

Nous avons principalement utilisé le pattern **Command** qui nous a permis d'encapsuler chaque action (ajouter, supprimer, lister) dans des classes séparées. Ce pattern était idéal car il permettait d'ajouter facilement de nouvelles commandes comme "web" et "info" sans modifier le code existant. Le pattern **Factory** a été partiellement implémenté pour la création des différents formats de fichiers, ce qui nous donne la flexibilité de changer facilement entre JSON et CSV.

## Réponses aux questions

### Pour creer une nouvelle commande :

- Creer une classe implementant l'interface de commande pour la nouvelle commande -> execute et fonction.
- Ce rendre dans le "CommandRegistry"  pour ajouter la commande dans notre registre de commandes avec un lien vers la classe de la nouvelle commande

### Pour ajouter une nouvelle source de données :
- Creer une nouvelle classe implementant FileFormat
- Ajouter une ligne dans formatAFile et formatAFileWithSpecifiedFormat

### Pour spécifier un magasin où ajouter les courses :

- Ajouter un attribut "store" dans la classe GroceryItem avec getter/setter
- Modifier les constructeurs pour prendre en compte ce nouveau paramètre
- Mettre à jour la classe CommandContext pour ajouter un attribut store et les méthodes associées
- Modifier les formats de fichiers (JsonFormat et CsvFormat) pour sauvegarder et lire cette information
- Ajouter une option "-st" ou "--store" dans la classe CommandOptions
- Mettre à jour la commande add pour prendre en compte le magasin lors de l'ajout d'un article