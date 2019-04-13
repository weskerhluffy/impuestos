package org.nada.dao;

import org.springframework.data.repository.CrudRepository;

// XXX: https://www.mkyong.com/spring-data/spring-data-add-custom-method-to-repository/
public interface FacturaVigenteDAO extends CrudRepository<FacturaVigenteDAO, Integer>, FacturaVigenteDAOCustom {

}
