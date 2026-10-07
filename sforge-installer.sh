#!/bin/bash

echo "Installaton de sForge démarée..."                                                                                 
rm "$HOME/sForge/sforge.jar"                                                                                            
javac "$HOME/sForge/src/Main.java"                                                                                      
jar cfe "$HOME/sForge/sforge.jar" Main -c "$HOME/sForge/src/Main.class"                                                 
sudo chmod +x "$HOME/sForge/sforge"                                                                                     
sudo chmod +x "$HOME/sForge/sforge-update.sh"                                                                           
mkdir -p "$HOME/.local/bin"                                                                                             
mv sforge "$HOME/.local/bin/sforge"                                                                                     
mv "$HOME/sForge/sforge.jar" "$HOME/sforge.jar"
