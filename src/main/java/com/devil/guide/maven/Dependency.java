package com.devil.guide.maven;

/**
 * @author Devil
 * @since 2021/11/29
 */
public class Dependency {

    private String groupId;
    private String artifactId;
    private String version;

    public Dependency(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    /**
     * 1 大于 -1 小于 0 相同 -2 表示位数不同
     * @param other
     * @return
     */
    public int greaterThan(Dependency other) {
        if (!groupId.equals(other.getGroupId()) || !artifactId.equals(other.getArtifactId())) {
            throw new IllegalArgumentException("can't compare different dependency");
        }

        String[] v1 = version.split("\\.");
        String[] v2 = other.getVersion().split("\\.");

        int length = Math.min(v1.length, v2.length);
        int diff = v1.length - v2.length;
        for (int i = 0; i < length; i++) {
            if (v1[i].compareTo(v2[i]) > 0) {
                return 1;
            } else if (v1[i].compareTo(v2[i]) < 0) {
                return -1;
            }
        }
        if (diff == 0) {
            return 0;
        } else {
            return -2;
        }
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getVersion() {
        return version;
    }
}
