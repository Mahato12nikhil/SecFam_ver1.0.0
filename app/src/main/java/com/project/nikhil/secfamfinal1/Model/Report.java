package com.project.nikhil.secfamfinal1.Model;

import java.util.Map;

public class Report {
    String issue;
    Map<String, String> timestamp;

    public Report() {
    }

    public Report(String issue, Map<String, String> timestamp) {
        this.issue = issue;
        this.timestamp = timestamp;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public Map<String, String> getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Map<String, String> timestamp) {
        this.timestamp = timestamp;
    }
}
