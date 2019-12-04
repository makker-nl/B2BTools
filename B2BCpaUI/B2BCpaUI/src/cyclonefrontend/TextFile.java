package cyclonefrontend;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

import oracle.xml.parser.v2.DOMParser;

import org.w3c.dom.Node;

import org.xml.sax.SAXException;

public class TextFile {
    private String text;
    private String filePath;
    private File file;
    private String error;

    public TextFile(String text, String filePath) {
        this.text = text;
        this.filePath = filePath;
        this.file = new File(this.filePath);
        this.error = null;
    }

    public TextFile(String filePath) {
        this.filePath = filePath;
        this.file = new File(this.filePath);
        this.error = null;
    }

    public TextFile(File file) {
        this.file = file;
        this.filePath = file.getAbsolutePath();
        this.error = null;
    }

    public void save() {
        FileWriter to = null; // Stream to write to destination
        try {
            char[] buffer = text.toCharArray();
            int length = text.length();
            to = new FileWriter(file); // Create output stream
            to.write(buffer, 0, length); // write
        } catch (FileNotFoundException e) {
            showError(e.toString());
        } catch (IOException e) {
            showError(e.toString());

        }
        // Always close the streams, even if exceptions were thrown
        finally {
            if (to != null)
                try {
                    to.close();
                } catch (IOException e) {
                    showError(e.toString());
                }
        }
    }

    public void load() {
        Reader inputReader;
        try {
            int fileLength = (int)file.length();
            char[] buffer = new char[fileLength];
            inputReader = new FileReader(file);
            int numChar = inputReader.read(buffer);
            text = new String(buffer);
            System.out.println("Number of characters read from " + filePath +
                               ": " + numChar);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
        this.file = new File(filePath);
    }

    public String getFilePath() {
        return filePath;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    private void showError(String error) {
        this.error = error;
        System.out.println(this.error);
    }

    public void setFile(File file) {
        this.file = file;
        this.filePath = file.getAbsolutePath();
    }

    public File getFile() {
        return file;
    }
}
