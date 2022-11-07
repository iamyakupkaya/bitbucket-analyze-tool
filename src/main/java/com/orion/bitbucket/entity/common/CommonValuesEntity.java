package com.orion.bitbucket.entity.common;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class CommonValuesEntity {
    private String key;
    @Id
    private String id;
    private String name;
    @Field("public") // it will be showed as public in MongoDB
    private boolean Public;
    private String type;


    public CommonValuesEntity() {
    }

    public CommonValuesEntity(String key, String id, String name, boolean aPublic, String type) {
        this.key = key;
        this.id = id;
        this.name = name;
        Public = aPublic;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPublic() {
        return Public;
    }

    public void setPublic(boolean aPublic) {
        Public = aPublic;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
