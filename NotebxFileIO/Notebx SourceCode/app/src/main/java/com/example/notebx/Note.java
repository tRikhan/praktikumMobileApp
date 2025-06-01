package com.example.notebx;

public class Note {
    private String title;
    private String content;
    private String fileName;    public Note(String title, String content, String fileName) {
        this.title = title != null ? title : "";
        this.content = content != null ? content : "";
        this.fileName = fileName != null ? fileName : "";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
