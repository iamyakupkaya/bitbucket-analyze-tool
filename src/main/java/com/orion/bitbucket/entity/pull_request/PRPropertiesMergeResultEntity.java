package com.orion.bitbucket.entity.pull_request;

public class PRPropertiesMergeResultEntity {
    private String outcome;
    private boolean current;

    public PRPropertiesMergeResultEntity() {
    }

    public PRPropertiesMergeResultEntity(String outcome, boolean current) {
        this.outcome = outcome;
        this.current = current;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }
}
