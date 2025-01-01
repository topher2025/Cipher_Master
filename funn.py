import tkinter as tk
from tkinter import filedialog


def open_file():
        filepath = filedialog.askopenfilename(filetypes=[("Python Files", "*.py"), ("All Files", "*.*")])
        if filepath:
            with open(filepath, "r") as file:
                text_editor.delete("1.0", tk.END)
                text_editor.insert(tk.END, file.read())
            root.title(f"Editing: {filepath}")

def save_file():
    filepath = filedialog.asksaveasfilename(defaultextension=".*", filetypes=[("Python Files", "*.py"), ("All Files", "*.*")])
    if filepath:
        with open(filepath, "w") as file:
            file.write(text_editor.get("1.0", tk.END))
        root.title(f"Editing: {filepath}")

# GUI setup
root = tk.Tk()
root.title(f"Python File Editor")

# Text editor
text_editor = tk.Text(root, wrap="word", font=("Consolas", 12))
text_editor.pack(expand=1, fill="both")

# Menu
menu_bar = tk.Menu(root)
file_menu = tk.Menu(menu_bar, tearoff=0)
file_menu.add_command(label="Open", command=open_file)
file_menu.add_command(label="Save", command=save_file)
menu_bar.add_cascade(label="File", menu=file_menu)
root.config(menu=menu_bar)

# Run the application
root.mainloop()
