package com.dyan.weiyan.model;

import java.util.Date;

/**
 * Created by Dyan on 17/7/25.
 */
public class Comment {
    
    private int id;
    private String content;

    private int entryId;
    private int entryType;

    private Date createdDate;
    private int userId;
    private int status;


    public int getId() { return id; }

    public void setId(int id) { this.id = id; }
    

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    
    public int getEntryId() {
        return entryId;
    }

    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }
    
    
    public int getEntryType() { return entryType; }
    
    public void setEntryType(int entryType){ this.entryType = entryType;}

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
