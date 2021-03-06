package com.sarveshparab.analysers.git;

import com.sarveshparab.config.Conf;
import com.sarveshparab.util.GitTreeUtil;
import com.sarveshparab.util.StringManipulator;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.*;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

public class GitCommitAnalyser {

    Git git = null;
    Repository repository = null;
    File localPath = null;

    public GitCommitAnalyser() {
    }

    // takes location of remote git and address where you want to clone the repo
    public void gitRemoteClone(String gitURL, String filePath) {

        System.out.println(" Starting to clone remote git repo");
        localPath = new File(filePath);
        try {
            git = Git.cloneRepository()
                    .setURI(gitURL)
                    .setDirectory(localPath)
                    .call();

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

    public String getCommitMessage(String commitId) {

        String commitMessage = null;

        try {
            ObjectId id = repository.resolve(commitId);

            try (RevWalk walk = new RevWalk(repository)) {
                RevCommit commit = walk.parseCommit(id);
                commitMessage = commit.getFullMessage();
            }
        } catch (IOException e) {
            System.out.println("Commit message retrival failed");
            e.printStackTrace();
        }

        return commitMessage;
    }

    public CommitFileObject getCommitFiles(String newCommitIden) {
        CommitFileObject commitFileObject = getCommitFiles(newCommitIden, null);
        return commitFileObject;
    }

    public CommitFileObject getCommitFiles(String newCommitIden, String oldCommitIden) {
        CommitFileObject commitFileObject = new CommitFileObject();

        try {

            // get new commit
            ObjectId newCommitId = repository.resolve(newCommitIden);
            ObjectId oldCommitId = oldCommitIden != null ? repository.resolve(oldCommitIden) : null;

            try (RevWalk walk = new RevWalk(repository)) {
                RevCommit newCommit = walk.parseCommit(newCommitId);
                RevCommit oldCommit;

                if (oldCommitId != null) {
                    oldCommit = walk.parseCommit(oldCommitId);
                } else {
                    oldCommit = GitTreeUtil.getPrevHash(newCommit, repository);
                }

                commitFileObject = GitTreeUtil.listDiff(git, repository, oldCommit, newCommit);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return commitFileObject;
    }


    public List<String> getCommitIds(String filePath) {
        filePath = Conf.ZK_REMOTE_SRC_PATH + StringManipulator.packagePathToSysPath(filePath, "/") + Conf.JAVA_FILE_EXT;
        List<String> commitIds = new ArrayList<>();

        try {

            Git gitSample = new Git(repository);

            Iterable<RevCommit> logs = gitSample.log()
                    .addPath(filePath)
                    .call();

            int count = 0;
            for (RevCommit rev : logs) {
                commitIds.add(rev.getName());
                count++;
            }

        } catch (GitAPIException e) {
            System.out.println(" GIT API FAULT");
            e.printStackTrace();
        }

        return commitIds;
    }

    public Map<String, Integer> buildFreqMap(Set<String> fileNames) {

        List<String> cummlativeCommitIds = new ArrayList<>();

        for (String fileName : fileNames) {
            List<String> commitIdsOfFile = getCommitIds(fileName);
            cummlativeCommitIds.addAll(commitIdsOfFile);

        }

        Map<String, Integer> freqMap = new HashMap<>();

        for (String commitId : cummlativeCommitIds) {
            freqMap.put(commitId, freqMap.getOrDefault(commitId, 0) + 1);
        }

        final Map<String, Integer> sortedByCount = freqMap.entrySet()
                .stream()
                .sorted((Map.Entry.<String, Integer>comparingByValue().reversed()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        return sortedByCount;

    }

    public void deleteGitRemoteClone(String repoLocation) {

        try {
            FileUtils.deleteDirectory(new File(repoLocation));
        } catch (IOException e) {
            System.out.println(" Repo did not get deleted");
            e.printStackTrace();
        }
    }
}







