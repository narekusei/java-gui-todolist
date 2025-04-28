package com.narekusei.todolist;

import javax.swing.SwingUtilities; // Import SwingUtilities

/**
 * The main entry point for the GUI To-Do List application.
 */
public class Main {

    /**
     * Creates and shows the main application window on the Event Dispatch Thread (EDT).
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        // IMPORTANT: Swing GUI updates should happen on the Event Dispatch Thread (EDT).
        // SwingUtilities.invokeLater ensures that the GUI creation code runs on the EDT.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Create the main application window
                TodoListApp app = new TodoListApp();
                // Make the window visible
                app.setVisible(true);
            }
        });
    }
}
