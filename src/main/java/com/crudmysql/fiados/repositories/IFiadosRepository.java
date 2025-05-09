package com.crudmysql.fiados.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crudmysql.fiados.models.FiadosModel;

@Repository 
public interface IFiadosRepository extends JpaRepository<FiadosModel, Long> {

}
