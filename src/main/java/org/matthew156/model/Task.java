package org.matthew156.model;

import org.matthew156.utils.Status;

import java.util.UUID;

/**
 * Represents a single task item in a task management system.
 * Each task has a unique identifier, a description, a status (e.g., TODO, IN_PROGRESS, DONE),
 * and timestamps for creation and last update.
 *
 * @author Matthew156 (Or your actual name/alias)
 * @version 1.0
 */
public class Task {
    private UUID id;
    private String description;
    private String status;
    private String createdAt;
    private String updatedAt;

    /**
     * Constructs a new Task with a description, creation timestamp, and update timestamp.
     * The ID is not set by this constructor (it's assumed to be generated later or not needed immediately).
     * The status is initialized to {@link Status#TODO}.
     *
     * @param description The textual description of the task.
     * @param createdAt The timestamp (as a String) indicating when the task was created.
     * @param updatedAt The timestamp (as a String) indicating when the task was last updated.
     *                  Often the same as createdAt on initial creation.
     */
    public Task( String description,  String createdAt, String updatedAt) {
        this.description = description;
        this.status = Status.TODO.getName(); // Assuming Status.TODO.getName() returns "TODO" or similar
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Constructs a new Task with a specific ID, description, creation timestamp, and update timestamp.
     * The status is initialized to {@link Status#TODO}.
     *
     * @param id The unique identifier for the task.
     * @param description The textual description of the task.
     * @param createdAt The timestamp (as a String) indicating when the task was created.
     * @param updatedAt The timestamp (as a String) indicating when the task was last updated.
     */
    public Task( UUID id, String description,  String createdAt, String updatedAt) {
        this.id = id;
        this.description = description;
        this.status = Status.TODO.name(); // Assuming Status.TODO.name() returns the enum constant name like "TODO"
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Default no-argument constructor.
     * Creates an empty Task object. Fields should be set using setter methods.
     */
    public Task( ) {
    }

    /**
     * Gets the unique identifier of the task.
     *
     * @return The {@link UUID} of the task.
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the task.
     *
     * @param id The new {@link UUID} for the task.
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Gets the description of the task.
     *
     * @return The task's description as a String.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the task.
     *
     * @param description The new description for the task.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the creation timestamp of the task.
     * This is typically a string representation of a date/time.
     *
     * @return The creation timestamp as a String.
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp of the task.
     *
     * @param createdAt The new creation timestamp (as a String).
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the last update timestamp of the task.
     * This is typically a string representation of a date/time.
     *
     * @return The last update timestamp as a String.
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets the last update timestamp of the task.
     *
     * @param updatedAt The new last update timestamp (as a String).
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Gets the current status of the task.
     * The status is a string representation (e.g., "TODO", "IN_PROGRESS", "DONE"),
     * typically corresponding to an enum value from {@link org.matthew156.utils.Status}.
     *
     * @return The current status of the task as a String.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the current status of the task.
     *
     * @param status The new status for the task (e.g., "IN_PROGRESS").
     *               It's recommended this string matches a value from {@link org.matthew156.utils.Status}.
     */
    public void setStatus(String status) {
        this.status = status;
    }
}