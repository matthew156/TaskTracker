package org.matthew156.utils;

/**
 * Represents the possible states or statuses a task can be in.
 * This enumeration provides a type-safe way to manage and refer to
 * the different stages of a task's lifecycle, such as "To Do",
 * "In Progress", or "Done". Each status has an associated string
 * representation.
 *
 * @author Matthew156 (Or your actual name/alias)
 * @version 1.0
 */
public enum Status {
    /**
     * Represents a task that has been created but not yet started.
     * The string representation is "todo".
     */
    TODO("todo"),

    /**
     * Represents a task that is currently being worked on.
     * The string representation is "in-progress".
     */
    IN_PROGRESS("in-progress"),

    /**
     * Represents a task that has been completed.
     * The string representation is "done".
     */
    DONE("done");

    /**
     * The string representation of the status.
     * For example, "todo", "in-progress", or "done".
     */
    private String name;

    /**
     * Constructs a new Status enum constant with its associated string name.
     * This constructor is private, as enum constants are defined statically.
     *
     * @param name The string representation of the status (e.g., "todo", "in-progress").
     */
    Status(String name) {
        this.name = name;
    }

    /**
     * Gets the string representation of this status.
     * <p>
     * For example, {@code Status.TODO.getName()} would return "todo".
     * </p>
     *
     * @return The lowercase string name associated with the enum constant.
     */
    public String getName() {
        return name;
    }
}