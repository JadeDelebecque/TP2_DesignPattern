# **Rapport - TP4 Liste d'Épicerie**

## **Ce qu'on a fait**

Pour ce TP4, on a dû ajouter une nouvelle commande "info" à notre appli de liste d'épicerie et restructurer notre code suite aux retours du prof sur le TP3.

## **Notre appli**

C'est toujours la même appli de liste d'épicerie en ligne de commande. On peut:
- Ajouter des articles (avec quantité et catégorie)
- Voir la liste
- Supprimer des articles
- Maintenant, afficher des infos système avec la commande "info"

## **La nouvelle commande "info"**

Pour la commande "info", on affiche:
- La date du jour (format YYYY-MM-DD)
- Le système d'exploitation
- La version de Java

Cette commande est super simple mais ça nous a permis de pratiquer notre nouvelle structure de code. Elle ignore tous les arguments qu'on lui donne, donc même si on tape `-s`, `-f` ou `-c`, elle n'exécute rien.

## **La grosse restructuration**

La partie principale du TP était de restructurer notre code.

### **Ce qu'on a changé:**

1. **CommandeManager:** On a créé cette classe qui s'occupe de recevoir et dispatcher les commandes au bon endroit.

2. **Classes de commandes séparées:**
    - `AddElement` pour ajouter des trucs
    - `RemoveElement` pour supprimer
    - `ListCategory` pour afficher
    - `InfoCommand` pour la nouvelle commande

3. **Main allégé:** Notre Main est beaucoup plus simple maintenant, il parse juste les arguments et passe tout au CommandeManager.

### **Nos améliorations:**

- On a renommé les méthodes pour qu'elles soient plus claires
- On a commenté les classes et méthodes importantes
- On a fait des tests pour notre nouvelle commande "info"

## **Comment ça marche maintenant**

Quand l'utilisateur tape une commande:
1. Le Main récupère les arguments
2. Il crée un CommandeManager
3. Le CommandeManager appelle la bonne classe de commande
4. La classe de commande fait son truc

C'est beaucoup plus propre qu'avant où le Main faisait presque tout.

## **Les tests**

On a créé des tests pour vérifier que notre commande "info" marche bien. On vérifie:
- Qu'elle affiche bien la date, l'OS et la version Java
- Qu'elle fonctionne via le CommandeManager
- Qu'elle fonctionne via le Main, même avec des arguments en plus

## **Les problèmes rencontrés**

1. **Restructurer sans tout casser:** C'était pas facile de changer toute la structure sans faire bugger les trucs qui marchaient déjà.

2. **L'option `-s` optionnelle:** C'était galère de faire en sorte que l'option `-s` soit obligatoire pour toutes les commandes sauf "info".

## **Ce qu'on a appris**

1. **Comment alléger le Main:** On a compris pourquoi c'est important de pas tout mettre dans la classe principale.

2. **L'importance des noms explicites:** C'est mieux de donner des noms clairs aux méthodes plutôt que de mettre des tonnes de commentaires.

## **Pour la suite**

Si on avait plus de temps, on pourrait:
- Ajouter d'autres commandes utiles
- Améliorer les messages d'erreur
- Rendre l'affichage plus joli avec des couleurs

Bref, ce TP nous a permis de mieux comprendre comment structurer du code et pourquoi c'est important de pas tout mettre dans une seule classe.