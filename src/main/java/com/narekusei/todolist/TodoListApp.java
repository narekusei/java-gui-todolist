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
        listModel.clear(); // Remove all elements from the GUI model
        if (taskList != null) { // Add tasks from our underlying list
            for (Task task : taskList) {
                listModel.addElement(task); // Add task to the GUI model (JList updates)
            }
        }
    }

    @SuppressWarnings("unchecked") // Suppress warning for the list cast
    private List<Task> loadTasksFromFile() {
        List<Task> loadedTasks = new ArrayList<>(); // Default to empty list
        File dataFile = new File(DATA_FILE_NAME);
        if (dataFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile))) {
                Object readObject = ois.readObject();
                if (readObject instanceof List) { // Basic check for safety
                     // Ensure the list contains Task objects (more robust check)
                    List<?> potentiallyTasks = (List<?>) readObject;
                    if (potentiallyTasks.isEmpty() || potentiallyTasks.get(0) instanceof Task) {
                        loadedTasks = (List<Task>) potentiallyTasks; // Perform the cast
                        System.out.println("Tasks loaded successfully from " + DATA_FILE_NAME);
                    } else {
                        System.err.println("Warning: Data file contains list of unexpected type. Starting fresh.");
                         // Optionally show error dialog
                    }
                } else {
                    System.err.println("Warning: Data file format is incorrect. Starting fresh.");
                     // Optionally show error dialog
                }
            } catch (FileNotFoundException e) {
                // Should not happen due to exists() check, but good practice
                System.out.println("Data file not found. Starting with an empty list.");
            } catch (IOException | ClassNotFoundException | ClassCastException e) {
                 // Catch potential issues during deserialization or casting
                System.err.println("Error loading tasks: " + e.getMessage() + ". Starting fresh.");
                // Optionally show error dialog:
                JOptionPane.showMessageDialog(this, "Error loading tasks:\n" + e.getMessage() + "\nStarting with an empty list.", "Load Error", JOptionPane.ERROR_MESSAGE);
                loadedTasks = new ArrayList<>(); // Ensure we return an empty list on error
            }
        } else {
            System.out.println("No existing task file found (" + DATA_FILE_NAME + "). Starting with an empty list.");
        }
        // Ensure taskList instance variable is updated IF loading succeeded OR if starting fresh
        // If load failed and returned default empty, we still need taskList to be that empty list
        this.taskList = loadedTasks; // Make sure the main taskList is updated
        return loadedTasks; // Return the loaded (or empty) list
    }


    private void saveTasksToFile() {
        if (taskList == null) {
            System.err.println("Task list is null, cannot save.");
            return;
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE_NAME))) {
            oos.writeObject(taskList); // Save the entire list
            System.out.println("Tasks saved to " + DATA_FILE_NAME);
        } catch (IOException e) {
            System.err.println("Error saving tasks: " + e.getMessage());
             // Optionally show error dialog:
            JOptionPane.showMessageDialog(this, "Error saving tasks:\n" + e.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    // --- Action Listener Methods (Implement in next steps) ---

    // (addActionListener, completeActionListener, removeActionListener)

} // End of TodoListApp class