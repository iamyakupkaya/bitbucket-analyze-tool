package com.orion.bitbucket.entity.project;

import com.orion.bitbucket.entity.ITopEntity;
import com.orion.bitbucket.entity.common.CommonBaseEntity;
import com.orion.bitbucket.helper.DatabaseHelper;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

// this class inherit some fields from CommonBaseEntity.!
// DO NOT DELETE
@Document(collection = DatabaseHelper.PROJECTS)
public class ProjectEntity extends CommonBaseEntity implements ITopEntity {
    private ProjectValuesEntity values;

    @Id
    @Field("_id")
    private String id;
    public ProjectEntity() {
    }
    public ProjectEntity(int size, int limit, boolean isLastPage, int start, int nextPageStart, ProjectValuesEntity values, String id) {
        super(size, limit, isLastPage, start, nextPageStart);
        this.values = values;
        this.id=id;
    }
    public ProjectValuesEntity getValues() {
        return values;
    }
    public void setValues(ProjectValuesEntity values) {
        this.values = values;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

