import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

import org.fife.ui.rsyntaxtextarea.*;
import org.fife.ui.rtextarea.*;

public class MiniIDE extends JFrame {

    // RSyntaxTextArea provides syntax highlighting and line numbering
    private RSyntaxTextArea codeArea;
    private RTextScrollPane codeScrollPane;

    // Output areas
    private JTextArea outputArea;          // For program output messages
    private DefaultListModel<String> errorListModel; // For compiler/runtime errors
    private JList<String> errorList;       // Displays errors

    // Top controls
    private JComboBox<String> languageCombo;
    private JButton saveButton, runButton;

    public MiniIDE() {
        super("Mini IDE");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        initComponents();
    }

    private void initComponents() {
        // Create the RSyntaxTextArea for code editing
        codeArea = new RSyntaxTextArea(30, 80);
        codeArea.setCodeFoldingEnabled(true);
        setSyntaxStyle((String) "Java");  // default to Java syntax
        codeScrollPane = new RTextScrollPane(codeArea);

        // Create output area for normal output
        outputArea = new JTextArea(8, 80);
        outputArea.setEditable(false);
        JScrollPane outputScroll = new JScrollPane(outputArea);

        // Create error list for clickable errors
        errorListModel = new DefaultListModel<>();
        errorList = new JList<>(errorListModel);
        JScrollPane errorScroll = new JScrollPane(errorList);
        errorScroll.setPreferredSize(new Dimension(400, 100));

        // Create the language selector
        languageCombo = new JComboBox<>(new String[]{"Java", "Python", "C", "Swift"});
        languageCombo.addActionListener(e -> {
            String lang = (String) languageCombo.getSelectedItem();
            setSyntaxStyle(lang);
        });

        // Create Save and Run buttons
        saveButton = new JButton("Save");
        runButton = new JButton("Run");

        // Top panel for controls
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Language:"));
        topPanel.add(languageCombo);
        topPanel.add(saveButton);
        topPanel.add(runButton);

        // Panel for error messages (with a label)
        JPanel errorPanel = new JPanel(new BorderLayout());
        errorPanel.add(new JLabel("Errors (double-click to jump):"), BorderLayout.NORTH);
        errorPanel.add(errorScroll, BorderLayout.CENTER);

        // Main layout: code editor center, output and error panels at bottom.
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(outputScroll, BorderLayout.CENTER);
        bottomPanel.add(errorPanel, BorderLayout.SOUTH);

        // Add panels to frame
        add(topPanel, BorderLayout.NORTH);
        add(codeScrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Add action listeners
        saveButton.addActionListener(e -> saveFile());
        runButton.addActionListener(e -> runCode());

        // Add mouse listener to error list for clickable error messages.
        errorList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = errorList.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        String errorMsg = errorListModel.getElementAt(index);
                        jumpToError(errorMsg);
                    }
                }
            }
        });
    }

    /**
     * Set the RSyntaxTextArea syntax highlighting style based on the language.
     */
    private void setSyntaxStyle(String language) {
        switch (language) {
            case "Java":
                codeArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
                break;
            case "Python":
                codeArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PYTHON);
                break;
            case "C":
                codeArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_C);
                break;
            case "Swift":
                // RSyntaxTextArea does not have a built-in Swift style.
                // Use plain text or create a custom style.
                codeArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
                break;
            default:
                codeArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
        }
    }

    /**
     * Opens a file chooser to save the current code.
     */
    private void saveFile() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(this);
        if(option == JFileChooser.APPROVE_OPTION){
            File file = fileChooser.getSelectedFile();
            try(PrintWriter out = new PrintWriter(file)) {
                out.print(codeArea.getText());
                outputArea.setText("File saved: " + file.getAbsolutePath());
            } catch(IOException ex) {
                outputArea.setText("Error saving file: " + ex.getMessage());
            }
        }
    }

    /**
     * Writes the code to a temporary file, compiles/runs it based on language,
     * parses errors, and displays output and errors.
     */
    private void runCode() {
        String language = (String) languageCombo.getSelectedItem();
        errorListModel.clear(); // clear previous errors
        outputArea.setText(""); // clear output area
        try {
            // Write the code to a temporary file with an appropriate extension.
            File tempFile = File.createTempFile("tempcode", getFileExtension(language));
            tempFile.deleteOnExit();
            try(PrintWriter out = new PrintWriter(tempFile)) {
                out.print(codeArea.getText());
            }

            ProcessBuilder pb = null;
            String compileErrors = "";

            if(language.equals("Python")) {
                // For Python, simply run the interpreter.
                pb = new ProcessBuilder("python", tempFile.getAbsolutePath());
            }
            else if(language.equals("Java")) {
                // For Java, we expect a public class named Main.
                // Rename temp file to Main.java.
                File javaFile = new File(tempFile.getParent(), "Main.java");
                if (!tempFile.renameTo(javaFile)) {
                    outputArea.setText("Error renaming file for Java compilation.");
                    return;
                }
                // Compile Java source.
                pb = new ProcessBuilder("javac", javaFile.getAbsolutePath());
                Process compileProc = pb.start();
                compileErrors = streamToString(compileProc.getErrorStream());
                compileProc.waitFor();
                if(!compileErrors.isEmpty()){
                    parseAndDisplayErrors(compileErrors);
                    return;
                } else {
                    outputArea.append("Compilation Successful.\n");
                }
                // Run the compiled class.
                pb = new ProcessBuilder("java", "-cp", javaFile.getParent(), "Main");
            }
            else if(language.equals("C")) {
                // For C, compile with gcc.
                pb = new ProcessBuilder("gcc", tempFile.getAbsolutePath(), "-o", tempFile.getAbsolutePath() + ".out");
                Process compileProc = pb.start();
                compileErrors = streamToString(compileProc.getErrorStream());
                compileProc.waitFor();
                if(!compileErrors.isEmpty()){
                    parseAndDisplayErrors(compileErrors);
                    return;
                } else {
                    outputArea.append("Compilation Successful.\n");
                }
                pb = new ProcessBuilder(tempFile.getAbsolutePath() + ".out");
            }
            else if(language.equals("Swift")) {
                // For Swift, compile with swiftc.
                pb = new ProcessBuilder("swiftc", tempFile.getAbsolutePath(), "-o", tempFile.getAbsolutePath() + ".out");
                Process compileProc = pb.start();
                compileErrors = streamToString(compileProc.getErrorStream());
                compileProc.waitFor();
                if(!compileErrors.isEmpty()){
                    parseAndDisplayErrors(compileErrors);
                    return;
                } else {
                    outputArea.append("Compilation Successful.\n");
                }
                pb = new ProcessBuilder(tempFile.getAbsolutePath() + ".out");
            }

            // Run the process
            if(pb != null) {
                Process proc = pb.start();
                String runOutput = streamToString(proc.getInputStream());
                String runErrors = streamToString(proc.getErrorStream());
                proc.waitFor();
                outputArea.append("\nProgram Output:\n" + runOutput);
                if(!runErrors.isEmpty()){
                    parseAndDisplayErrors(runErrors);
                }
            }

        } catch(Exception ex) {
            outputArea.setText("Error: " + ex.getMessage());
        }
    }

    /**
     * Determines the file extension based on the language.
     */
    private String getFileExtension(String language) {
        switch(language) {
            case "Python": return ".py";
            case "Java":   return ".java";
            case "C":      return ".c";
            case "Swift":  return ".swift";
            default:       return ".txt";
        }
    }

    /**
     * Reads an InputStream fully and returns its contents as a String.
     */
    private String streamToString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        while((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

    /**
     * Parses error messages (assuming they include a file name and line number)
     * and displays them in the error list.
     */
    private void parseAndDisplayErrors(String errors) {
        outputArea.append("Errors Found.\n");
        // Split error output into lines.
        String[] lines = errors.split("\n");
        for(String line : lines) {
            if (!line.trim().isEmpty()) {
                errorListModel.addElement(line);
            }
        }
    }

    /**
     * When an error message is double-clicked, attempt to jump to the line in the code.
     * This example assumes error messages include a colon-separated line number, e.g.:
     * "Main.java:5: error: ...", where 5 is the line number.
     */
    private void jumpToError(String errorMsg) {
        // Try to extract the line number using a simple split.
        // This works for errors of the form: filename:line: ...
        try {
            String[] parts = errorMsg.split(":");
            if (parts.length >= 2) {
                int lineNumber = Integer.parseInt(parts[1].trim());
                // Convert to 0-based index for RSyntaxTextArea.
                codeArea.setCaretPosition(codeArea.getLineStartOffset(lineNumber - 1));
                codeArea.requestFocus();
            }
        } catch(Exception ex) {
            // If parsing fails, do nothing.
            System.err.println("Could not parse line number from error: " + errorMsg);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MiniIDE ide = new MiniIDE();
            ide.setVisible(true);
        });
    }
}
