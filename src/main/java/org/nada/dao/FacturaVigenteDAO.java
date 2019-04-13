package org.nada.dao;

import org.nada.models.FacturaVigente;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

// XXX: https://www.mkyong.com/spring-data/spring-data-add-custom-method-to-repository/
@Repository
public interface FacturaVigenteDAO extends CrudRepository<FacturaVigente, Integer>, FacturaVigenteDAOCustom {

}
