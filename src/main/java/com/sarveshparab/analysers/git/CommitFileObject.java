package com.sarveshparab.analysers.git;

import com.sarveshparab.config.Conf;
import com.sarveshparab.util.StringManipulator;

import java.util.ArrayList;
import java.util.List;

public class CommitFileObject {


    List<String> addedFiles = new ArrayList<>();
    List<String> modifiedFiles = new ArrayList<>();
    List<String> deletedFiles = new ArrayList<>();
    List<String> copiedFiles = new ArrayList<>();
    List<String> renamedFiles = new ArrayList<>();
    List<String> affectedFiles = new ArrayList<>();

    public CommitFileObject(){

    }


    public CommitFileObject(List<String> addedFiles, List<String> modifiedFiles, List<String> deletedFiles) {
        this.addedFiles = addedFiles;
        this.modifiedFiles = modifiedFiles;
        this.deletedFiles = deletedFiles;
        affectedFiles = new ArrayList<>();
    }

    public List<String> getAddedFiles() {
        return addedFiles;
    }

    public void setAddedFiles(List<String> addedFiles) {
        this.addedFiles = addedFiles;
    }

    public List<String> getModifiedFiles() {
        return modifiedFiles;
    }

    public void setModifiedFiles(List<String> modifiedFiles) {
        this.modifiedFiles = modifiedFiles;
    }

    public List<String> getDeletedFiles() {
        return deletedFiles;
    }

    public void setDeletedFiles(List<String> deletedFiles) {
        this.deletedFiles = deletedFiles;
    }

    public List<String> getCopiedFiles() {
        return copiedFiles;
    }

    public void setCopiedFiles(List<String> copiedFiles) {
        this.copiedFiles = copiedFiles;
    }

    public List<String> getRenamedFiles() {
        return renamedFiles;
    }

    public void setRenamedFiles(List<String> renamedFiles) {
        this.renamedFiles = renamedFiles;
    }

    public List<String> getAffectedFiles() {

        affectedFiles.addAll(changeSysPathToPackage(this.getAddedFiles()));
        affectedFiles.addAll(changeSysPathToPackage(this.getCopiedFiles()));
        affectedFiles.addAll(changeSysPathToPackage(this.getDeletedFiles()));
        affectedFiles.addAll(changeSysPathToPackage(this.getModifiedFiles()));
        affectedFiles.addAll(changeSysPathToPackage(this.getRenamedFiles()));

        return affectedFiles;
    }


    private List<String> changeSysPathToPackage(List<String> fileNames){

        List<String> filesModified = new ArrayList<>();
        for(String file : fileNames){
            file= StringManipulator.sysPathToPackagePath(file,"/");
            if(file !=null){
                filesModified.add(file);
            }
        }


//        return fileNames.stream();


        return filesModified;
    }
}
