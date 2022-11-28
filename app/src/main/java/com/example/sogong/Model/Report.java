package com.example.sogong.Model;

import com.google.gson.annotations.SerializedName;

public class Report {
    @SerializedName("reporter")
    private String reporter;
    @SerializedName("contents")
    private String contents;
    @SerializedName("post_id")
    private int post_id;
    @SerializedName("post_type")
    private int post_type;

    public Report() {}
    public Report(String reporter, String contents, int post_id, int post_type) {
        this.reporter = reporter;
        this.contents = contents;
        this.post_id = post_id;
        this.post_type = post_type;
    }

    @Override
    public String toString() {
        return "Report{" +
                "reporter='" + reporter + '\'' +
                ", contents='" + contents + '\'' +
                ", post_id=" + post_id +
                ", post_type=" + post_type +
                '}';
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public int getPost_type() {
        return post_type;
    }

    public void setPost_type(int post_type) {
        this.post_type = post_type;
    }
}
