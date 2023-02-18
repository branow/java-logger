package com.branow.logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

class NoteFormatter {

    private static final DateTimeFormatter DATE_TIME_FORMATTER
            = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
    protected static final String SEPARATOR = " ~ ", NOTE_SEPARATOR = "#\n";


    public static String parseNotes(List<Note> notes) {
        return notes.stream()
                .map(NoteFormatter::formatNote)
                .collect(Collectors.joining(NOTE_SEPARATOR));
    }

    public static List<Note> formatNotes(String text) {
        String[] notes = text.split(NOTE_SEPARATOR);
        return Arrays.stream(notes)
                .map(NoteFormatter::parseNote).toList();
    }

    public static String formatNote(Note note) {
        String[] property = {formatDate(note.getDate()), formatType(note.getType()), note.getName(), formatDetails(note.getDetails())};
        return String.join(SEPARATOR, property);
    }

    public static Note parseNote(String text) {
        String[] prop = text.split(SEPARATOR);
        if (prop.length < 3) throw new NoSuchElementException();
        LocalDateTime date = parseDate(prop[0]);
        Note.Type type = parseType(prop[1]);
        String name = prop[2];
        String details = prop.length > 3 ? parseDetails(prop[3]) : "";
        return new Note(date, type, name, details);
    }

    public static String formatDate(LocalDateTime date) {
        return date.format(DATE_TIME_FORMATTER);
    }

    public static LocalDateTime parseDate(String text) {
        return LocalDateTime.parse(text, DATE_TIME_FORMATTER);
    }

    public static String formatType(Note.Type type) {
        return "[" + type.toString() + "]";
    }

    public static Note.Type parseType(String text) {
        return Note.Type.valueOf(text.replaceAll("[\\[\\]]", ""));
    }

    public static String formatDetails(String text) {
        if (text.isEmpty()) return text;
        return "\n\t" + text.replaceAll("\n", "\n\t");
    }

    public static String parseDetails(String text) {
        return text.replaceFirst("\n\t", "").replaceAll("\n\t", "\n");
    }

}
