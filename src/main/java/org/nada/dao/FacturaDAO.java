package org.nada.dao;

import org.nada.models.Factura;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacturaDAO extends CrudRepository<Factura, Integer> {
	public Factura findByRfcEmisorAndFolio(String rfcEmisor, String folio);
}
