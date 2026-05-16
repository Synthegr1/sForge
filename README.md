# sForge

**sForge** est un DSL utilisé pour simplifier la compilation, l'assemblage, le ld et l'exécution (sous machine virtuelle) de système d'exploitation dans le cadre d'OsDev. 

Simple DSL for OsDev

Les fichiers sForge ont comme extension de fichier .forge, par exemple 'main.forge'.
sForge est parser et compiler en Java.

## **Syntaxe :**
sForge est composés de 5 grandes **parties essentielles** au bon fonctionnement de l'execution et du nom de la branch :

  ### Base de la synthaxe :
  Toutes les instructions se terminent par un ';'.
  Les parties, branches etc sont englobés par des '{}' accolades.
  

  ### Branch
  La **branch main** sera toujours executé comme branche finale, il est possible de créé plusieurs branches, qui doivent toutes contenir les parties essentielles.

    //This is an comment
    branch main {
    
    }
  
  ### Project
  Cette partie est essentielle pour connaître les spécifications du projet : nom du projet, architecture de compilation (x86_32...) et si le langage est utilisé dans le cadre d'OsDev.
  Trois éléments doivent impérativement être présents dans la catégorie projet : le *nom*, *l'architecture* et la variable de type boolean *osdev*.

    project myOs {
    
        arch _[architecture];
        osdev = [value];
        out '[out_name].bin';
  
        //Exemple :
        arch _x86_32;
        osdev = true;
        out 'main.bin';
        
    }

  ### Objects
  Cette catégorie (définit par l'identifiant *cat*) permet de définir les différents fichiers de notre os, mais sous forme d'objets. Il existe 5 types d'objets différents : *asm* pour les fichiers en Assembleur, *c* pour les fichiers en C, *rs* pour les fichiers Rust et enfin *ld* pour le linker. Sur ces objets seront appliqués plus tard des fonctions tel que *.compile()* ou autre. Cette catégorie (seule définit avec l'identifiants *cat*) peut s'apellé librement, elle sera uniquement utilisé pour définir les objets

    cat objects { // ou 'mesObjets' ou encore 'helloWorld'
    
        [type de l'objet] [nom de l'objet] = '[chemin d'acces]';
        // Types disponibles : asm, c, rust, ld

        c kernel = 'path/to/kernel.c';
        asm boot = 'path/to/bootloader.asm';
        rs autre_nom = 'path/to/maths.rs';
        ld linker = 'path/to/linker.ld';
        
    }

  ### Compile
  A partir de maintenant, les catégories suivantes seront définies par le même mot clef : **forge**. La catégorie compile permet de compiler (pas linker !) les différents objets (c, rust...) en éléments '.o' avant le linkage. Il est possible d'appliquer la fonction *.compile()* pour
  
