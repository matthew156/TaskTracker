package org.matthew156.model;

import org.matthew156.utils.Status;

import java.util.Date;

public class Task {
    private int id;
    private String description;
    private Status status;
    private Date createdAt;
    private Date updatedAt;


    public Task( String description,  Date createdAt, Date updatedAt) {
        this.description = description;
        this.status = Status.TODO;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }


}
