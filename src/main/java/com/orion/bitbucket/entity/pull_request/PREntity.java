package com.orion.bitbucket.entity.pull_request;

import com.orion.bitbucket.entity.ITopEntity;
import com.orion.bitbucket.entity.common.CommonBaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Data
public abstract class PREntity extends CommonBaseEntity implements ITopEntity {

    private PRValuesEntity values; // List<ValuesEntity>
    public PREntity(){};

    public PREntity(int size, int limit, boolean isLastPage, int start, int nextPageStart, PRValuesEntity values) {
        super(size, limit, isLastPage, start, nextPageStart);
        this.values = values;
    }

}
