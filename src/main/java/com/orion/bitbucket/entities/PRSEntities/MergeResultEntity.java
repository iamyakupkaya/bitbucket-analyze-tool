package com.orion.bitbucket.entities.PRSEntities;

public class MergeResultEntity {
    private String outcome;
    private boolean current;

    public MergeResultEntity() {
    }

    public MergeResultEntity(String outcome, boolean current) {
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
