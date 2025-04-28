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

        //Window Listener for saving on close
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


        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTask();
            }
        });

        completeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                markTaskComplete();
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeTask();
            }
        });

        // Also allow adding task by pressing Enter in the text field
        taskInputTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTask();
            }
        });

        // Make the window visible
        setVisible(true);
    }


    // --- Helper Methods ---

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
    private void addTask() {
        String description = taskInputTextField.getText().trim();
        if (description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Task description cannot be empty.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return; // Stop if description is empty
        }

        try {
            Task newTask = new Task(description); // Use Task constructor validation
            taskList.add(newTask);         // Add to the underlying list
            updateListModel();            // Refresh the JList display
            taskInputTextField.setText(""); // Clear the input field
            System.out.println("Task added: " + description);
        } catch (IllegalArgumentException ex) {
             // This shouldn't happen if trim() and isEmpty check works, but good practice
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void markTaskComplete() {
        int selectedIndex = taskJList.getSelectedIndex(); // Get index of the selected item

        if (selectedIndex == -1) { // Nothing selected
            JOptionPane.showMessageDialog(this, "Please select a task to mark as complete.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Task selectedTask = listModel.getElementAt(selectedIndex); // Get Task object from the model

        if (!selectedTask.isDone()) {
            selectedTask.setDone(true);
             // The list needs to be visually updated. Firing an event is complex,
             // so the simplest way is to just refresh the whole model.
            updateListModel();
             // To maintain selection (optional, slightly advanced):
            taskJList.setSelectedIndex(selectedIndex);
            System.out.println("Task marked complete: " + selectedTask.getDescription());
        } else {
            JOptionPane.showMessageDialog(this, "Task is already marked as complete.", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void removeTask() {
        int selectedIndex = taskJList.getSelectedIndex();

         if (selectedIndex == -1) { // Nothing selected
            JOptionPane.showMessageDialog(this, "Please select a task to remove.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

         // Optional: Confirmation dialog
        Task taskToRemove = listModel.getElementAt(selectedIndex);
        int confirmation = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to remove this task?\n\"" + taskToRemove.getDescription() + "\"",
                "Confirm Removal",
                JOptionPane.YES_NO_OPTION);

        if (confirmation == JOptionPane.YES_OPTION) {
             taskList.remove(taskToRemove); // Remove from the underlying list
             updateListModel();             // Refresh the JList display
            System.out.println("Task removed: " + taskToRemove.getDescription());
        }
    }

} // End of TodoListApp class