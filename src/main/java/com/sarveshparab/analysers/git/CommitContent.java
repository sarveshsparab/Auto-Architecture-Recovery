package com.sarveshparab.analysers.git;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CommitContent {

    private String id;
    private String message;
    private List<String> affectedFiles;
    private int affectedFilesCount;
    private Set<String> analysedMsg;
    private String issueId;
    private String commitURL;
    private int secFilesCount;

    public CommitContent() {
        id = "";
        message = "";
        affectedFiles = new ArrayList<>();
        affectedFilesCount = 0;
        analysedMsg = new HashSet<>();
        issueId = "";
        commitURL = "";
        secFilesCount = 0;
    }

    public CommitContent(String id, String message, List<String> affectedFiles, int affectedFilesCount, Set<String> analysedMsg, String issueId, String commitURL, int secFilesCount) {
        this.id = id;
        this.message = message;
        this.affectedFiles = affectedFiles;
        this.affectedFilesCount = affectedFilesCount;
        this.analysedMsg = analysedMsg;
        this.issueId = issueId;
        this.commitURL = commitURL;
        this.secFilesCount = secFilesCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getAffectedFiles() {
        return affectedFiles;
    }

    public void setAffectedFiles(List<String> affectedFiles) {
        this.affectedFiles = affectedFiles;
    }

    public int getAffectedFilesCount() {
        return affectedFilesCount;
    }

    public void setAffectedFilesCount(int affectedFilesCount) {
        this.affectedFilesCount = affectedFilesCount;
    }

    public Set<String> getAnalysedMsg() {
        return analysedMsg;
    }

    public void setAnalysedMsg(Set<String> analysedMsg) {
        this.analysedMsg = analysedMsg;
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public String getCommitURL() {
        return commitURL;
    }

    public void setCommitURL(String commitURL) {
        this.commitURL = commitURL;
    }

    public int getSecFilesCount() {
        return secFilesCount;
    }

    public void setSecFilesCount(int secFilesCount) {
        this.secFilesCount = secFilesCount;
    }
}
