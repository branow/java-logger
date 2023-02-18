package com.branow.logger;

import javax.swing.plaf.SpinnerUI;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;

public class Note {

    public enum Type {
        ERROR, WARNING, INFO;

        @Override
        public String toString() {
            return this.name();
        }
    }

    private LocalDateTime date;
    private Type type;
    private String name;
    private String details;

    public Note(LocalDateTime date, Type type, String name) {
        this(date, type, name, "");
    }

    public Note(LocalDateTime date, Type type, String name, String details) {
        this.date = date;
        this.type = type;
        this.name = name;
        this.details = details;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
