package com.orion.bitbucket.repository;

import com.orion.bitbucket.entity.project.ProjectEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

// @Repository is not necessary because it extented MongoRepository so MongoRepository already has that @annototaion
@Repository
public interface ProjectRepository extends MongoRepository<ProjectEntity, String>, TopRepository {


    // custom methods..
    // aşağıdaki şekilde finByBlaBla diyerek aramak istediğimiz değişken türüne göre metod tanımlayabiliriz.
   // List<AuthorEntity> findBySurname(String surname);

}
