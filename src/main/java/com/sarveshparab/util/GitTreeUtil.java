package com.sarveshparab.util;

import com.sarveshparab.analysers.git.CommitFileObject;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

import java.io.IOException;
import java.util.List;

public class GitTreeUtil {

    public static RevCommit getPrevHash(RevCommit commit, Repository repository)  throws IOException {

        try (RevWalk walk = new RevWalk(repository)) {
            // Starting point
            walk.markStart(commit);
            int count = 0;
            for (RevCommit rev : walk) {
                // got the previous commit.
                if (count == 1) {
                    return rev;
                }
                count++;
            }
            walk.dispose();
        }
        //Reached end and no previous commits.
        return null;
    }


    public static AbstractTreeIterator prepareTreeParser(RevCommit commit,Repository repository) throws IOException {
        // from the commit we can build the tree which allows us to construct the TreeParser
        // noinspection Duplicates
        try (RevWalk walk = new RevWalk(repository)) {
            RevTree tree = walk.parseTree(commit.getTree().getId());

            CanonicalTreeParser treeParser = new CanonicalTreeParser();
            try (ObjectReader reader = repository.newObjectReader()) {
                treeParser.reset(reader, tree.getId());
            }

            walk.dispose();

            return treeParser;
        }

    }

    public static CommitFileObject listDiff(Git git, Repository repository,RevCommit oldCommit, RevCommit newCommit) {

        CommitFileObject commitFileObject = new CommitFileObject();

        List<DiffEntry> diffs;

        try {
            diffs = git.diff()
                    .setOldTree(prepareTreeParser(oldCommit,repository))
                    .setNewTree(prepareTreeParser(newCommit,repository))
                    .call();

            System.out.println("Found: " + diffs.size() + " differences");
            for (DiffEntry diff : diffs) {

                String changeType = diff.getChangeType().toString();

                String newPath = diff.getNewPath();
                String oldPath = diff.getOldPath();

                if(newPath.contains("src")){
                    newPath = newPath.substring(newPath.indexOf("src"));
                }
                if(oldPath.contains("src")){
                    oldPath = oldPath.substring(oldPath.indexOf("src"));
                }


                if (changeType.equals("MODIFY")){
                    commitFileObject.getModifiedFiles().add(newPath);
                }
                else if(changeType.equals("ADD")){
                    commitFileObject.getAddedFiles().add(newPath);
                }
                else if (changeType.equals("DELETE")){
                    commitFileObject.getDeletedFiles().add(oldPath);
                }
                else if(changeType.equals("RENAME")){
                    commitFileObject.getRenamedFiles().add(newPath);
                }else{
                    commitFileObject.getCopiedFiles().add(newPath);
                }

                System.out.println("Diff: " +changeType + ": " +
                        (oldPath.equals(newPath) ? newPath: oldPath + " -> " +newPath));

            }

        } catch (GitAPIException e) {
            System.out.println("Error in parsing tree[GIT API ERROR]");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error in parsing tree[IO EXCEPTION]");
            e.printStackTrace();
        }


        return commitFileObject;
    }


}
