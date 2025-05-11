package org.matthew156.model;

import org.matthew156.utils.Status;

import java.util.UUID;

public class Task {
    private UUID id;
    private String description;
    private String status;
    private String createdAt;
    private String updatedAt;


    public Task( String description,  String createdAt, String updatedAt) {
        this.description = description;
        this.status = Status.TODO.getName();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Task( UUID id, String description,  String createdAt, String updatedAt) {
        this.id = id;
        this.description = description;
        this.status = Status.TODO.name();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public Task( ) {
    }
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
