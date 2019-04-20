package org.nada.dao;

import org.nada.models.Factura;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacturaDAO extends CrudRepository<Factura, Integer> {
	// XXX: https://www.concretepage.com/spring-5/spring-data-crudrepository-example
	public Factura findByRfcEmisorAndFolio(String rfcEmisor, String folio);
}
