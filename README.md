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
  Trois éléments doivent impérativement être présents dans la catégorie projet : le *nom*, *l'architecture* et la variable de type boolean *osdev*.<br>
  Il existe dans sForge 2 architectures possibles : en 64 bits **x86_32** et en 32 bits **i386**.

    project myOs {
    
        arch _[architecture];
        osdev = [value];
        out '[out_name].bin';
  
        //Exemple :
        arch _x86_32;
        osdev = true;
        out 'main';
        
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
  A partir de maintenant, les catégories suivantes seront définies par le même mot clef : **forge**. La catégorie compile permet de compiler (pas linker !) les différents objets (c, rust...) en éléments '.o' avant le linkage. Il est possible d'appliquer la fonction *.compile()* pour effectuer cette tâche. *.compile()* prend deux à trois arguments en fonction du type de l'objet. Pour des objects de type *asm* ou *c* et *rs*, elle ne prend que deux arguments : le type de fichier en sortie (par exemple *o* pour une sortie en '.o') et, pour le C et le Rust les informations sur les bibliotheques standard (compilation avec ou sans les std), pour l'asm si c'est un fichier de boot ou autre (avec les mots-clefs *boot* et *other*). /!\ Pour les fichiers rust, ils seront nécéssairement compiler en panic si osdev = true et tous les éléments dans l'architecture indiqué au début du programme (par exemple, *arch* est égal à *i386*, alors, ils seront compiler (avec gcc) en *-m32*).

    forge compile {

        [nom de l'objet].compile([arg 1], [arg 2]);
        
        // Exemple :
        kernel.compile(o, no_std);
        autre_noms.compile(o, no_std);
        boot.compile(o, boot);
        
    }

  ### Link
  C'est la partie où le ld prendra son utilité. Ici, deux fonctions sont disponibles : *ld()* et *trans()*. *ld()* prend un argument : le nom de l'objet qui pointe vers le fichier .ld, ici l'objet *linker*. Le format de la sortie sera déduis en fonction de l'*arch* définit en haut du programme (par exemple : si *arch* est = à *i386*, alors, le ld aura en sortie un fichier *elf_i386*. Pour la fonction *trans()*, elle permet de convertir un fichier (en fonction de l'out de *ld()*) en un fichier d'un autre type (présisé en argument de la fonction *trans()*). Ce fichier sorti sera la version final, prêt à l'execution de votre OS. Il aura comme nom celui définit dans le *out* dans la section project, par exemple ici 'main.bin'.

    forge link {

        ld([nom de l'objet ld]);
        trans([type de sortie final);

        // Exemple :
        ld(linker);
        trans(bin);
        
    }

  ### Run
  C'est la partie où nous allons pouvoir executer tous notre os avec une fonction disponible : *qemu()* qui ne prend... aucun arguments. Elle va commiler tout de suite le fichier de sortie du *trans()*.

    forge run {

      qemu();

    }

## Environnemment nécéssaire :
Java
## Bonne pratique !
