package com.narekusei.todolist;

import java.io.Serializable;

/**
 * Represents a single task in the To-Do list. (Identical to console version)
 * Implements Serializable to allow saving/loading.
 */
public class Task implements Serializable {

    private static final long serialVersionUID = 1L; // For serialization compatibility
    private String description;
    private boolean isDone;

    public Task(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Task description cannot be empty.");
        }
        this.description = description.trim();
        this.isDone = false;
    }

    // Getters
    public String getDescription() { return description; }
    public boolean isDone() { return isDone; }

    // Setter
    public void setDone(boolean done) { isDone = done; }

    /**
     * Provides the string representation displayed in the JList.
     * Includes completion status.
     */
    @Override
    public String toString() {
        return "[" + (isDone ? "X" : " ") + "] " + description;
    }
}