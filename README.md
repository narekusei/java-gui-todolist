# Java Swing GUI To-Do List

A simple desktop To-Do List application built with Java Swing. Allows users to manage tasks graphically, with data persisted between sessions.

## Features
*   Add new tasks via text input.
*   View tasks in a scrollable list.
*   Mark tasks as complete (updates display `[ ]` -> `[X]`).
*   Remove selected tasks (with confirmation).
*   Tasks are automatically saved to `tasks.dat` when the application window is closed.
*   Tasks are loaded from `tasks.dat` on startup.

## Technologies Used
*   Java (JDK 8 or higher)
*   Java Swing (for GUI components and layout)
*   Java AWT (for event handling, layout managers)
*   Java I/O (Serialization for persistence)

## How to Compile and Run

1.  **Prerequisites:**
    *   Java Development Kit (JDK) installed (version 8 or higher recommended).
    *   Git (for cloning).

2.  **Clone the Repository:**
    ```bash
    git clone https://github.com/narekusei/java-gui-todolist.git
    cd java-gui-todolist
    ```

3.  **Compile:**
    Open a terminal/command prompt in the project's root directory (`java-gui-todolist`).
    ```bash
    mkdir bin
    javac -d bin src/main/java/com/narekusei/todolist/*.java
    ```

4.  **Run:**
    Execute the application, specifying the classpath:
    ```bash
    java -cp bin com.narekusei.todolist.Main
    ```
    The GUI window should appear. Tasks are saved/loaded from `tasks.dat` in the project root.
