package com.orion.bitbucket.entities.PRSEntities;

public class PRSPropertiesMergeResultEntity {
    private String outcome;
    private boolean current;

    public PRSPropertiesMergeResultEntity() {
    }

    public PRSPropertiesMergeResultEntity(String outcome, boolean current) {
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
