package com.branow.logger;

import com.branow.edition.FileText;
import com.branow.print.Printer;
import com.branow.print.TextColor;
import com.branow.print.TextType;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

import static com.branow.logger.Note.*;

public class Logger {

    private final FileText text;
    private final boolean console;

    public Logger() {
        this(true, null);
    }

    public Logger(boolean console, FileText fileText) {
        this.console = console;
        this.text = fileText;
    }

    public void logInfo (String name) {
        log(Type.INFO, name);
    }

    public void logInfo (String name, String details) {
        log(Type.INFO, name, details);
    }

    public void logWarning (String name) {
        log(Type.WARNING, name, "");
    }

    public void logWarning (String name, String details) {
        log(Type.WARNING, name, details);
    }

    public void logError (String name) {
        log(Type.ERROR, name, "");
    }

    public void logError (String name, String details) {
        log(Type.ERROR, name, details);
    }

    public void logError (Exception e) {
        String name = e.getClass().getSimpleName() + " : " + e.getMessage();
        String details = Arrays.stream(e.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.joining("\n"));
        log(Type.ERROR, name, details);
    }

    public void log(Note.Type type, String name) {
        log(type, name, "");
    }

    public void log(Note.Type type, String name, String detail) {
        log(new Note(LocalDateTime.now(), type, name, detail));
    }

    public void log(Note note) {
        if(isConsole()) logToConsole(note);
        if(isFile()) logToFile(note);
    }


    public FileText getText() {
        return text;
    }

    public boolean isConsole() {
        return console;
    }

    public boolean isFile() {
        return text != null;
    }


    private void logToConsole(Note note) {
        TextColor color = getColor(note.getType());
        TextColor bright = getBrightColor(note.getType());
        Printer.print(NoteFormatter.formatDate(note.getDate()), TextType.UNDERLINED, bright);
        System.out.print(" ");
        Printer.print(NoteFormatter.formatType(note.getType()), TextType.BOLD, bright);
        System.out.print(" ");
        Printer.print(note.getName(), bright);
        Printer.println(NoteFormatter.formatDetails(note.getDetails()), color);
    }

    private void logToFile(Note note) {
        String appending = NoteFormatter.formatNote(note);
        if(!text.isEmpty()) {
            appending = NoteFormatter.NOTE_SEPARATOR + appending;
        }
        text.append(appending);
    }


    private TextColor getColor(Type type) {
        return switch (type) {
            case INFO -> TextColor.BLUE;
            case WARNING -> TextColor.YELLOW;
            case ERROR -> TextColor.RED;
        };
    }

    private TextColor getBrightColor(Type type) {
        return switch (type) {
            case INFO -> TextColor.BLUE_BRIGHT;
            case WARNING -> TextColor.YELLOW_BRIGHT;
            case ERROR -> TextColor.RED_BRIGHT;
        };
    }

}
