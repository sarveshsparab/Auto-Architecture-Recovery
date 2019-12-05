package com.sarveshparab.analysers.git;


import com.sarveshparab.config.Conf;
import com.sarveshparab.util.GitTreeUtil;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
    File localPath = null;


    // takes location of remote git and address where you want to clone the repo
    public void gitRemoteClone(String gitURL,String filePath) {

        System.out.println(" Starting to clone remote git repo");
        localPath = new File(filePath);
        try {
             git = Git.cloneRepository()
                    .setURI(gitURL)
                    .setDirectory(localPath)
                    .call();
//
//
//            git.checkout()
//                    .setCreateBranch(true)
//                    .setName("new-branch")
//                    .setStartPoint("<id-to-commit>")
//                    .call();
           // git =
            Git.open(localPath).checkout().setCreateBranch(true)
                    .setName("new-branch")
                    .setStartPoint(Conf.ZK_COMMIT_ID)
                    .call();
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


    public List<String> getCommitIds(String filePath){
        List<String> commitIds = new ArrayList<>();


        try {
//            Git gitSample = new Git(repository);
//            Iterable<RevCommit> logs1 = gitSample.log()
//                    .all()
//                    .call();
//            int count = 0;
//            for (RevCommit rev : logs1) {
//                System.out.println("Commit: " + rev.getShortMessage()  + ", name: " + rev.getName() + ", id: " + rev.getId().getName());
//                count++;
//            }
//            System.out.println(" All commits :- " + count);


            Iterable<RevCommit> logs = git.log()
                    .addPath(filePath)
                    .call();
            System.out.println("Logs retrieved ");

            int count = 0;
            for (RevCommit rev : logs) {
                System.out.println("Commit: " + rev.getShortMessage()  + ", name: " + rev.getName() + ", id: " + rev.getId() );
                commitIds.add(rev.getName());
                count++;
            }
            System.out.println(" All  commits  in file:- " + count);

        } catch (GitAPIException e) {
            e.printStackTrace();
        }


        return commitIds;

    }


}







