#!/bin/bash

echo "Installaton de sForge démarée..."
echo "  *Vérification de l'installation de Java"
inst=0
java --version || inst=1

if [ $inst -eq 1 ]
then 
  echo "    /!\ Java n'est pas installé, veuillez entrer le nom de votre gestionnaire de paquet :"
  read gest
  echo "Installation..."
  sudo $gest install openjdk-25-jdk
else
  echo "Java déjà installé"
fi

rm "$HOME/sForge/sforge.jar"
javac "$HOME/sForge/src/Main.java"
jar cfe "$HOME/sForge/sforge.jar" Main -C "$HOME/sForge/src/" Main.class
sudo chmod +x "$HOME/sForge/sforge"
sudo chmod +x "$HOME/sForge/sforge-update.sh"
mkdir -p "$HOME/.local/bin"
mv sforge "$HOME/.local/bin/sforge"
mv "$HOME/sForge/sforge.jar" "$HOME/sforge.jar"
echo "Installaton de sForge terminée !"
