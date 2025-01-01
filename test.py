from flask import Flask, request, render_template_string

app = Flask(__name__)

# HTML template for the editor
HTML_TEMPLATE = """
<!doctype html>
<title>Python File Editor</title>
<h1>File Editor</h1>
<form method="POST">
    <textarea name="content" rows="20" cols="80">{{ content }}</textarea><br>
    <button type="submit">Save</button>
</form>
"""

@app.route("/", methods=["GET", "POST"])
def editor():
    filename = "example.py"
    if request.method == "POST":
        # Save the content to the file
        content = request.form["content"]
        with open(filename, "w") as file:
            file.write(content)
        return f"File '{filename}' saved successfully!<br><a href='/'>Go back</a>"

    # Load the content of the file
    content = ""
    try:
        with open(filename, "r") as file:
            content = file.read()
    except FileNotFoundError:
        pass  # No file yet

    return render_template_string(HTML_TEMPLATE, content=content)

if __name__ == "__main__":
    app.run(debug=True)
