package com.sarveshparab.config;

public class Conf {

    public static final String ZK_PRE_CODE_BASE_PATH = "src/main/resources/subjectsystem/pre/sourcecode/src/java/main/";
    public static final String ZK_POST_CODE_BASE_PATH = "src/main/resources/subjectsystem/post/sourcecode/src/java/main/";

    public static final String ZK_PRE_BUILD_PATH = "src/main/resources/subjectsystem/pre/sourcecode/build/";
    public static final String ZK_POST_BUILD_PATH = "src/main/resources/subjectsystem/post/sourcecode/build/";

    public static final String JAVA_FILE_EXT = ".java";

    public static final String MAP_SEPARATOR = "-";

    public static final String BANNED_PACKAGE_PATH="src/main/java/com/sarveshparab/domain/banned.txt";

    public static final String SECURITY_DOMAIN_FILE_PATH="src/main/java/com/sarveshparab/domain/securitydomainlist.txt";

    public static final String wordsListPath = "src/main/resources/data/EnglishWords/words.txt";

    public static final String ZK_REMOTE_GIT_URL = "https://github.com/apache/zookeeper.git";
    public static final String ZK_REMOTE_GIT_REPO = "src/main/resources/data/tmp/gitRepo";
    public static final String ZK_COMMIT_ID = "0383625b5b4adb14b774f322704456596e7ab185";
    public static final String ZK_REMOTE_SRC_PATH = "src/java/main/";
    public static final String ZK_REMOTE_TEST_PATH = "src/java/test/";
    public static final String ZK_COMMIT_BASE_URL = "https://github.com/apache/zookeeper/tree/";

    public static final String ZK_PRE_SMELL_FILE = "src/main/resources/subjectsystem/pre/arcoutput/zk_pre_arc_smells.ser";

    public static final String REFLECT_ANALYSIS_DUMP_DIR = "src/main/resources/data/ReflectionData/";

    public static final String JAVA_KEY_WORDS_FILE = "src/main/resources/data/PLWords/javaKeyWords.txt";
    public static final String C_KEY_WORDS_FILE = "src/main/resources/data/PLWords/cKeyWords.txt";

    public static final String SECURITY_CLUSTER_DUMP = "src/main/resources/data/output/SecurityClustering/secClusters.data";
    public static final String SECURITY_CLUSTER_DUMP_WITH_IMPORTS_DIFF = "src/main/resources/data/output/SecurityClustering/secClusters_withImportDiff.data";
    public static final String SMELLY_ANALYSIS_DUMP = "src/main/resources/data/output/SmellAnalysis/smells.data";
    public static final String COMMIT_CONTENT_DUMP = "src/main/resources/data/output/CommitAnalysis/commitContent.json";

}
