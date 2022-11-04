package com.orion.bitbucket.entities.PREntities;

import com.orion.bitbucket.entities.ITopEntity;
import com.orion.bitbucket.entities.common.CommonBaseEntity;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("allPRS")
public class PREntity extends CommonBaseEntity implements ITopEntity {

    private PRValuesEntity values; // List<ValuesEntity>
    public PREntity(){};

    public PREntity(int size, int limit, boolean isLastPage, int start, int nextPageStart, PRValuesEntity values) {
        super(size, limit, isLastPage, start, nextPageStart);
        this.values = values;
    }

    public PRValuesEntity getValues() {
        return values;
    }

    public void setValues(PRValuesEntity values) {
        this.values = values;
    }
}
