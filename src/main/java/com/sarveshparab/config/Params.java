package com.sarveshparab.config;

public class Params {

    // All Flags
    public static final boolean USE_PRE = true;
    public static final boolean LOAD_SMELL_ANALYSIS = true;
    public static final boolean LOAD_COMMIT_ANALYSIS = true;
    public static final boolean LOAD_SECURITY_CLUSTERS = true;
    public static final boolean LOAD_PRE_COMMIT_REFLECTION_ANALYSIS = true;
    public static final boolean LOAD_POST_COMMIT_REFLECTION_ANALYSIS = true;
    public static final boolean CONSIDER_ONLY_ADDITIONS = true;
    public static final boolean LOAD_POST_GIT_ANALYSIS = true;
    public static final boolean COMPUTE_AFFECTED_FILES_STATS = false;
    public static final boolean GENERATE_FINAL_CLUSTERS = true;

    // All Thresholds
    public static final double SIMILARITY_MEASURE_THRESHOLD = 0.30;
    public static final double HYBRID_SIM_MEASURE_THRESHOLD = 0.80;

    public static final int PRE_COMMIT_EMATCH_THRESHOLD = 3;
    public static final int PRE_COMMIT_SMATCH_THRESHOLD = 2;
    public static final int PRE_COMMIT_HMATCH_THRESHOLD = 10;

    public static final int COMMIT_FREQ_THRESHOLD = 10;
    public static final int POST_COMMIT_EMATCH_THRESHOLD = 1;


    public static final int FINAL_CLUSTER_EMATCH_THRESHOLD = 2;
}
