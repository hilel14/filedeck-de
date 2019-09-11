package org.hilel14.filedeck.de;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hilel14
 */
public class Job {

    private String userName;
    private String paperCode;
    private String baseJob;
    private String baseVersion;
    private final List<String> envelopes = new ArrayList<>();

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the paperCode
     */
    public String getPaperCode() {
        return paperCode;
    }

    /**
     * @param paperCode the paperCode to set
     */
    public void setPaperCode(String paperCode) {
        this.paperCode = paperCode;
    }

    /**
     * @return the baseJob
     */
    public String getBaseJob() {
        return baseJob;
    }

    /**
     * @param baseJob the baseJob to set
     */
    public void setBaseJob(String baseJob) {
        this.baseJob = baseJob;
    }

    /**
     * @return the baseVersion
     */
    public String getBaseVersion() {
        return baseVersion;
    }

    /**
     * @param baseVersion the baseVersion to set
     */
    public void setBaseVersion(String baseVersion) {
        this.baseVersion = baseVersion;
    }

    /**
     * @return the envelopes
     */
    public List<String> getEnvelopes() {
        return envelopes;
    }

}
