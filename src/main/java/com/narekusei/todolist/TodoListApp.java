package com.narekusei.todolist;

import java.awt.*; // Import Swing components
import java.awt.event.*;    // Import AWT classes (Layout managers, Dimension, etc.)
import java.io.*; // Import event handling classes
import java.util.ArrayList;     // Import I/O for saving/loading
import java.util.List;
import javax.swing.*;

/**
 * The main application window for the To-Do List GUI.
 * Extends JFrame to act as the main window.
 */
public class TodoListApp extends JFrame {

    private static final String DATA_FILE_NAME = "tasks.dat";

    // Data Model
    private List<Task> taskList; // The underlying list of Task objects
    private DefaultListModel<Task> listModel; // The model for the JList component

    // GUI Components
    private JList<Task> taskJList;
    private JTextField taskInputTextField;
    private JButton addButton;
    private JButton completeButton;
    private JButton removeButton;

    /**
     * Constructor: Sets up the main frame and initializes components.
     */
    public TodoListApp() {
        // 1. Initialize Data Structures
        taskList = loadTasksFromFile(); // Load tasks first
        listModel = new DefaultListModel<>();
        updateListModel(); // Populate listModel from taskList

        // 2. Configure the main window (JFrame)
        setTitle("Simple To-Do List");
        setSize(450, 350); // Set initial window size
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // We'll handle closing manually to save
        setLocationRelativeTo(null); // Center the window on screen

        // 3. Create GUI components
        listModel = new DefaultListModel<>(); // Model holds data for JList
        taskJList = new JList<>(listModel);   // JList displays data from the model
        taskJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Allow only one item selected

        taskInputTextField = new JTextField(25); // Text field for adding tasks
        addButton = new JButton("Add Task");
        completeButton = new JButton("Mark Complete");
        removeButton = new JButton("Remove Task");

        // 4. Set up Layout
        // Use BorderLayout for the main frame content pane
        setLayout(new BorderLayout(5, 5)); // Gaps between components

        // Panel for input and add button (using FlowLayout)
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.add(new JLabel("New Task:"));
        inputPanel.add(taskInputTextField);
        inputPanel.add(addButton);

        // Panel for action buttons (complete, remove)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(completeButton);
        buttonPanel.add(removeButton);

        // Add components to the frame's content pane
        add(inputPanel, BorderLayout.NORTH); // Input at the top
        add(new JScrollPane(taskJList), BorderLayout.CENTER); // List in the middle (scrollable)
        add(buttonPanel, BorderLayout.SOUTH); // Action buttons at the bottom

        // 5. Add Window Listener for saving on close (Implementation later)
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Action to perform when user clicks the close button
                saveTasksToFile();
                System.out.println("Tasks saved. Exiting.");
                dispose(); // Close the window
                System.exit(0); // Terminate the application
            }
        });


        // (Action Listeners for buttons will be added in the next steps)

        // Make the window visible - DO THIS LAST after adding all components
        // setVisible(true); // We'll call this from Main.java using SwingUtilities
    }


    // --- Helper Methods (Implement in next steps) ---

    private void updateListModel() {
        // TODO: Clear listModel and add all tasks from taskList
         listModel.clear();
         for (Task task : taskList) {
             listModel.addElement(task);
         }
    }

    private List<Task> loadTasksFromFile() {
        // TODO: Implement loading logic (similar to console version)
        // Temporary: Return empty list for now
         List<Task> loadedTasks = new ArrayList<>();
         File dataFile = new File(DATA_FILE_NAME);
         if (dataFile.exists()) {
             try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile))) {
                 Object readObject = ois.readObject();
                 if (readObject instanceof List) {
                     // Basic check - could be more robust
                     loadedTasks = (List<Task>) readObject;
                     System.out.println("Tasks loaded from " + DATA_FILE_NAME);
                 }
             } catch (FileNotFoundException e) { /* Already checked exists */ }
             catch (IOException | ClassNotFoundException e) {
                 System.err.println("Error loading tasks: " + e.getMessage() + ". Starting fresh.");
                 // Optionally show a dialog to the user:
                 // JOptionPane.showMessageDialog(this, "Error loading tasks:\n" + e.getMessage(), "Load Error", JOptionPane.ERROR_MESSAGE);
             }
         }
         return loadedTasks;
    }

    private void saveTasksToFile() {
        // TODO: Implement saving logic (similar to console version)
         try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE_NAME))) {
             oos.writeObject(taskList);
         } catch (IOException e) {
             System.err.println("Error saving tasks: " + e.getMessage());
              // Optionally show a dialog to the user:
             // JOptionPane.showMessageDialog(this, "Error saving tasks:\n" + e.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
         }
    }

    // --- Action Listener Methods (Implement in next steps) ---

    // (addActionListener, completeActionListener, removeActionListener)

} // End of TodoListApp class