package com.sarveshparab.analysers.git;


import com.sarveshparab.util.GitTreeUtil;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;


public class GitCommitAnalyser {

    Git git = null;
    Repository repository = null;


    // takes location of remote git and address where you want to clone the repo
    public void gitRemoteClone(String gitURL,String filePath) {

        System.out.println(" Starting to clone remote git repo");
        File localPath = new File(filePath);
        try {
             git = Git.cloneRepository()
                    .setURI(gitURL)
                    .setDirectory(localPath)
                    .call();

            git = Git.open(localPath);
            repository = git.getRepository();


            System.out.println(" Git and Repository successfully set ");
        } catch (GitAPIException e) {
            System.out.println(" Error in setting git & repository");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println(" Error in setting git & repository");
            e.printStackTrace();
        }

    }


    public String getCommitMessage(String commitId){

        String commitMessage = null;

        try {
            ObjectId id = repository.resolve(commitId);

            try (RevWalk walk = new RevWalk(repository)) {
                RevCommit commit = walk.parseCommit(id);
                System.out.println("Commit Message for commit id " + commitId);
                System.out.println(commit.getFullMessage());
                commitMessage = commit.getFullMessage();
            }
        } catch (IOException e) {
            System.out.println("Commit message retrival failed");
            e.printStackTrace();
        }

        return commitMessage;
    }


    public CommitFileObject getCommitFiles(String newCommitIden){

        CommitFileObject commitFileObject = getCommitFiles(newCommitIden,null);

        return commitFileObject;
    }


    public CommitFileObject getCommitFiles(String newCommitIden,String oldCommitIden){


        CommitFileObject commitFileObject = new CommitFileObject();

        try {

            // get new commit
            ObjectId newCommitId = repository.resolve(newCommitIden);
            ObjectId oldCommitId = oldCommitIden!=null?repository.resolve(oldCommitIden):null;

            try (RevWalk walk = new RevWalk(repository)) {
               RevCommit newCommit = walk.parseCommit(newCommitId);
               RevCommit oldCommit;

               if(oldCommitId!=null){
                   oldCommit = walk.parseCommit(oldCommitId);
               }else{
                   oldCommit = GitTreeUtil.getPrevHash(newCommit,repository);
               }

                commitFileObject =  GitTreeUtil.listDiff(git,repository,oldCommit,newCommit);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return commitFileObject;
    }





    public void deleteGitRemoteClone(String repoLocation){

        try {
            FileUtils.deleteDirectory(new File(repoLocation));
        } catch (IOException e) {
            System.out.println(" Repo did not get deleted");
            e.printStackTrace();
        }

    }


}







