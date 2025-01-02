import tkinter as tk
from tkinter import filedialog
from classes import Template
import os



def main(name, exstension):
    template = Template(exstension=exstension)

    SAVE_DIRECTORY = "custom_commands"

    def open_file(name):
        filepath = os.path.join(SAVE_DIRECTORY, name)
        if filepath:
            with open(filepath, "r") as file:
                text_editor.delete("1.0", tk.END)
                text_editor.insert(tk.END, file.read())
            root.title(f"Editing: {filepath}")

    def save_file(name):
        os.makedirs(SAVE_DIRECTORY, exist_ok=True)
        filepath = os.path.join(SAVE_DIRECTORY, name)
        with open(filepath, "w") as file:
            file.write(text_editor.get("1.0", tk.END))
        root.title(f"Editing: {filepath}")

    # GUI setup
    root = tk.Tk()
    root.title(f"{template} File Editor")

    # Text editor
    text_editor = tk.Text(root, wrap="word", font=("Consolas", 12))
    text_editor.pack(expand=1, fill="both")

    # Menu
    menu_bar = tk.Menu(root)
    file_menu = tk.Menu(menu_bar, tearoff=0)
    file_menu.add_command(label="Open", command=open_file(name))
    file_menu.add_command(label="Save", command=save_file(name))
    menu_bar.add_cascade(label="File", menu=file_menu)
    root.config(menu=menu_bar)

    # Run the application
    root.mainloop()


main("fun", ".py")