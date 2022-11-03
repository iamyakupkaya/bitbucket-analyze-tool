package com.orion.bitbucket.entities.projectEntities;

import com.orion.bitbucket.entities.ITopEntity;
import com.orion.bitbucket.entities.common.CommonBaseEntity;
import org.springframework.data.mongodb.core.mapping.Document;

// this class inherit some fields from CommonBaseEntity.!
// DO NOT DELETE
@Document(collection = "projects")
public class ProjectEntity extends CommonBaseEntity implements ITopEntity {

    private ValuesEntity values;
    public ProjectEntity() {
    }

    public ProjectEntity(int size, int limit, boolean isLastPage, int start, int nextPageStart, ValuesEntity values) {
        super(size, limit, isLastPage, start, nextPageStart);
        this.values = values;
    }

    public ValuesEntity getValues() {
        return values;
    }

    public void setValues(ValuesEntity values) {
        this.values = values;
    }
}

