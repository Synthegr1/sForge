import subprocess
import os
import shutil

os.chdir("/home/gabrielrebillat/Downloads/sForge-main/src")

subprocess.run(["javac", "Main.java"])
subprocess.run(["jar", "cfe", "sforge.jar", "Main", "Main.class"])

shutil.move("sforge.jar", "../sforge.jar")

print("sForge - Update Complete !")