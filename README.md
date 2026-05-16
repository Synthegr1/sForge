# sForge

**sForge** est un DSL utilisé pour simplifier la compilation, l'assemblage, le ld et l'exécution (sous machine virtuelle) de système d'exploitation dans le cadre d'OsDev. 

Simple DSL for OsDev

Les fichiers sForge ont comme extension de fichier .forge, par exemple 'main.forge'.
sForge est parser et compiler en Java.

## **Syntaxe :**

sForge est composés de 5 grandes **parties essentielles** au bon fonctionnement de l'execution :

  ### Project
    Cette partie est essentielle pour connaître les spécifications du projet : nom du projet, architecture de compilation (x86_32...) et si le langage est utilisé dans le cadre d'OsDev.
    Trois éléments doivent impérativement être présents dans la catégorie projet : le *nom*, *l'architecture* et la variable de type boolean *osdev*
