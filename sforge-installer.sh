#!/bin/bash

git clone https://github.com/Synthegr1/sForge
sudo chmod +x "$HOME/sForge/sforge"
sudo chmod +x "$HOME/sForge/sforge-update.sh"
mkdir -p "$HOME/.local/bin"
mv sforge "$HOME/.local/bin/sforge"
mv "$HOME/sForge/sforge.jar" "$HOME/sforge.jar"
