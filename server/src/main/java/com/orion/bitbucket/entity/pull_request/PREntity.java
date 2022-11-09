package com.orion.bitbucket.entity.pull_request;

import com.orion.bitbucket.entity.ITopEntity;
import com.orion.bitbucket.entity.common.CommonBaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Data
@NoArgsConstructor
@Document(collection = "#{T(com.orion.bitbucket.util.CollectionNameHolder).get()}")
public class PREntity extends CommonBaseEntity {

    private PRValuesEntity values;


}
