package org.nada.dao;

import org.nada.models.DeclaracionVigente;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeclaracionVigenteDAO extends CrudRepository<DeclaracionVigente, Integer> {

}
