package org.vitzay.entity;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Note {

    private int id;
    private String name;
    private String description;
    private Timestamp creationDate;
    private Long chatId;

    public Note(int id, String name, String description, Timestamp creationDate, Long chatId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.creationDate = creationDate;
        this.chatId = chatId;
    }

    public Note(int id, String name, Timestamp creationDate, Long chatId) {
        this.id = id;
        this.name = name;
        this.creationDate = creationDate;
        this.chatId = chatId;
    }

    public Note(String name, Timestamp creationDate, Long chatId) {
        this.name = name;
        this.creationDate = creationDate;
        this.chatId = chatId;
    }

    public Note(String name, String description, Timestamp creationDate, Long chatId) {
        this.name = name;
        this.description = description;
        this.creationDate = creationDate;
        this.chatId = chatId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    @Override
    public String toString() {
        LocalDateTime date = creationDate.toLocalDateTime();
        String formatedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return id + ". " + name + " | " + description + " | " + formatedDate;
    }
}
