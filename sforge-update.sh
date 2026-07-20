#!/bin/bash

javac "$HOME/sForge/src/Main.java"
jar cvfe "$HOME/sForge/sforge.jar" Main -C "$HOME/sForge/src/" Main.class
rm "$HOME/sforge.jar"
mv "$HOME/sForge/sforge.jar" "$HOME/sforge.jar"
