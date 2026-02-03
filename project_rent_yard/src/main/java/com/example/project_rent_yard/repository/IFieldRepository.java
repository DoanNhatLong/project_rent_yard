package com.example.project_rent_yard.repository;

import com.example.project_rent_yard.entity.Field;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

@Repository
public interface IFieldRepository extends JpaRepository<Field, Integer>{
    @Query("""
select f from Field f
where f.isDeleted=false
and (:fieldType is null or f.fieldType=:fieldType)
and (:fieldStatus is null or f.status=:fieldStatus)
and (:minPrice is null or f.price>=:minPrice)
and (:maxPrice is null or f.price<=:maxPrice)
""")
    Page<Field> showFields(
            @Param("fieldType") Field.FieldType fieldType,
            @Param("fieldStatus") Field.FieldStatus fieldStatus,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            Pageable pageable
    );
}
