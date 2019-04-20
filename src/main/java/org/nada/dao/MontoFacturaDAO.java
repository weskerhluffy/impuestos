package org.nada.dao;

import org.nada.models.MontoFactura;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MontoFacturaDAO extends CrudRepository<MontoFactura, Integer> {

}
