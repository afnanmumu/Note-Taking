import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Main extends JFrame {
    private JPanel notesPanel;

    public Main() {
        // Set up the main JFrame
        setTitle("Note App");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a button panel
        JPanel buttonPanel = new JPanel();
        JButton openNewScreenButton = new JButton("Add New Note");
        JButton getNotesButton = new JButton("Get Notes");

        openNewScreenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openNewScreen();
            }
        });

        getNotesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getNotesFromApi();
            }
        });

        buttonPanel.add(openNewScreenButton);
        buttonPanel.add(getNotesButton);

        // Create a notes panel
        notesPanel = new JPanel();
        notesPanel.setLayout(new BoxLayout(notesPanel, BoxLayout.Y_AXIS));
        notesPanel.setPreferredSize(new Dimension(400, 1000));
        JScrollPane scrollPane = new JScrollPane(notesPanel);
        // Set up the layout
        setLayout(new BorderLayout());
        add(buttonPanel, BorderLayout.SOUTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void openNewScreen() {
        // Create a new JFrame for the second screen
        JFrame newScreenFrame = new JFrame("Add New Note");
        newScreenFrame.setSize(400, 300);

        // Create input fields and a submit button
        JTextField titleField = new JTextField(20);
        JTextField descriptionField = new JTextField(20);
        JButton submitButton = new JButton("Submit");

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle the submission (you can customize this part)
                String title = titleField.getText();
                String description = descriptionField.getText();
                postToApi(title, description);
                JOptionPane.showMessageDialog(null, "Title: " + title + "\nDescription: " + description + "\n Note Added Successfully");
                getNotesFromApi();
                newScreenFrame.dispose(); // Close the second screen after submission
            }
        });


        // Add components to the second screen
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));
        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);
        panel.add(submitButton);

        newScreenFrame.getContentPane().add(panel);
        newScreenFrame.setVisible(true);
    }
    private void postToApi(String title, String description) {
        try {
            String apiUrl = "https://api.nstack.in/v1/todos";
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set up the connection for POST request
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Create JSON payload
            String jsonInputString = "{\"title\": \"" + title + "\", \"description\": \"" + description + "\"}";

            // Write the payload to the connection's output stream
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Get the response code (optional, for debugging purposes)
            int responseCode = connection.getResponseCode();
            System.out.println("POST Response Code: " + responseCode);

            // Close the connection
            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void getNotesFromApi() {
        try {
            String apiUrl = "https://api.nstack.in/v1/todos";
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set up the connection for GET request
            connection.setRequestMethod("GET");

            // Get the response code
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response from the API
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();

                // Parse the JSON response and display notes
                List<Note> notesList = parseJsonResponse(response.toString());
                displayNotes(notesList);

            } else {
                // Handle error cases
                JOptionPane.showMessageDialog(this, "Error retrieving notes. Response Code: " + responseCode, "Error", JOptionPane.ERROR_MESSAGE);
            }

            // Close the connection
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private List<Note> parseJsonResponse(String jsonResponse) {
        // Parse the JSON response and extract note titles and descriptions
        List<Note> notesList = new ArrayList<>();

        try {
            // Parse the JSON array from the response
            JSONObject json = new JSONObject(jsonResponse);
            JSONArray items = json.getJSONArray("items");

            // Iterate through each item and extract the required information
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                String id = item.getString("_id");
                String title = item.getString("title");
                String description = item.getString("description");

                // Add the note to the list
                notesList.add(new Note(id, title, description));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return notesList;
    }

    private void displayNotes(List<Note> notesList) {
        // Display the retrieved notes in the notesPanel
        notesPanel.removeAll();  // Clear previous components

        for (Note note : notesList) {
            // Create a panel to hold note information and buttons
            JPanel notePanel = new JPanel();
            notePanel.setLayout(new BorderLayout());
            notePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

            // Create a button for deleting the note (small size)
            JButton editButton = new JButton("Edit");
            JButton deleteButton = new JButton("Delete");
            editButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Handle button click (edit note)
                    openEditScreen(note);
                }
            });
            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Handle button click (delete note)
                    deleteNoteById(note.getId());
                }
            });
            editButton.setPreferredSize(new Dimension(100, 30));
            deleteButton.setPreferredSize(new Dimension(100, 30));

            // Create a panel to hold note information
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

            // Add note information to infoPanel
            infoPanel.add(new JLabel("Title: " + note.getTitle()));
            infoPanel.add(new JLabel("Description: " + note.getDescription()));
//            infoPanel.add(new JLabel("ID: " + note.getId()));

            // Add the infoPanel and deleteButton to notePanel
            notePanel.add(infoPanel, BorderLayout.CENTER);
            notePanel.add(editButton, BorderLayout.WEST);
            notePanel.add(deleteButton, BorderLayout.EAST);

            // Add the notePanel to the notesPanel
            notesPanel.add(notePanel);
        }

        // Repaint the UI
        notesPanel.revalidate();
        notesPanel.repaint();
    }

    private void openEditScreen(Note note) {
        // Create a new JFrame for the edit screen
        JFrame editScreenFrame = new JFrame("Edit Note");
        editScreenFrame.setSize(400, 300);

        // Create input fields, set existing values, and a submit button
        JTextField titleField = new JTextField(20);
        JTextField descriptionField = new JTextField(20);
        JButton submitButton = new JButton("Update");

        // Set existing values to input fields
        titleField.setText(note.getTitle());
        descriptionField.setText(note.getDescription());

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle the submission (you can customize this part)
                String newTitle = titleField.getText();
                String newDescription = descriptionField.getText();
                editNoteById(note.getId(), newTitle, newDescription);
                JOptionPane.showMessageDialog(null, "Title: " + newTitle + "\nDescription: " + newDescription + "\n Note Edited Successfully");
                getNotesFromApi();
                editScreenFrame.dispose(); // Close the edit screen after submission
            }
        });

        // Add components to the edit screen
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));
        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);
        panel.add(submitButton);

        editScreenFrame.getContentPane().add(panel);
        editScreenFrame.setVisible(true);
    }

    private void editNoteById(String noteId, String newTitle, String newDescription) {
        try {
            String apiUrl = "https://api.nstack.in/v1/todos/" + noteId;
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set up the connection for PUT request (update)
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Create JSON payload for the update
            String jsonInputString = "{\"title\": \"" + newTitle + "\", \"description\": \"" + newDescription + "\"}";

            // Write the payload to the connection's output stream
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Get the response code (optional, for debugging purposes)
            int responseCode = connection.getResponseCode();
            System.out.println("PUT Response Code: " + responseCode);

            // Close the connection
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static class Note {
        private String id;
        private String title;
        private String description;

        public Note(String id, String title, String description) {
            this.id = id;
            this.title = title;
            this.description = description;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }
    }

    private void deleteNoteById(String noteId) {
        try {
            String apiUrl = "https://api.nstack.in/v1/todos/" + noteId;
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set up the connection for DELETE request
            connection.setRequestMethod("DELETE");

            // Get the response code
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Successfully deleted
                getNotesFromApi(); // Refresh the notes after deletion
            } else {
                // Handle error cases
                JOptionPane.showMessageDialog(this, "Error deleting note. Response Code: " + responseCode, "Error", JOptionPane.ERROR_MESSAGE);
            }

            // Close the connection
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main().setVisible(true);
            }
        });
    }
}
