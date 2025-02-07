import subprocess
import jpype
import jpype.imports
from jpype.types import *


# Commands
def help():
    pass


def newCom(name):
    """Creates a new Command. Opens in a tkinter text interface.
    Saves to []"CIPHER_MASTER/custom_commands"].
    Inputs are ["name_of_file"]. The file starts with a template in the chosen language.
    The availeble languages are ["Python", "C/C#/C++", "Swift", "Java"]."""

import subprocess
import os

# Define the path to your Java classes
JAVA_FOLDER = "java_classes"

# Compile all Java files in the folder
def compile_java():
    try:
        subprocess.run(["javac", "-d", JAVA_FOLDER, f"{JAVA_FOLDER}/*.java"], check=True)
        print("Compilation successful!")
    except subprocess.CalledProcessError:
        print("Compilation failed.")

# Run the main Java class
def run_java(main_class):
    try:
        subprocess.run(["java", "-cp", JAVA_FOLDER, main_class], check=True)
    except subprocess.CalledProcessError:
        print("Execution failed.")

if __name__ == "__main__":
    compile_java()
    run_java("CipherMain")  # Change to your actual main class name

