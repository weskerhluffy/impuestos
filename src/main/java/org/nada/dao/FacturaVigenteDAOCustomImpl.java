package org.nada.dao;

import java.util.Date;
import java.util.List;

import javax.management.RuntimeErrorException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.nada.models.FacturaVigente;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class FacturaVigenteDAOCustomImpl implements FacturaVigenteDAOCustom {

	private static final Logger LOGGER = LoggerFactory.getLogger(FacturaVigenteDAOCustomImpl.class);
	// XXX:
	// https://stackoverflow.com/questions/26606608/how-to-manually-start-a-transaction-on-a-shared-entitymanager-in-spring
	// XXX: https://github.com/spring-projects/spring-framework/issues/15076
	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public void metodoPrueba() {
		// TODO Auto-generated method stub
		System.out.print("TMPH ALGO\n");
		LOGGER.debug("TMPH BABY shark {}",
				entityManager.createNativeQuery("select * from factura_vigente limit 1").getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<FacturaVigente> getFacturasSinDepreciacion(Date fecha) {
		List<FacturaVigente> facturas = null;
		Session session = (Session) entityManager.getDelegate();
		FacturaVigente facturaVigente = new FacturaVigente();
		facturaVigente.setPeriodo(fecha);
		facturaVigente.setPorcentaje(null);
		Example facturaVigenteEjemplo = Example.create(facturaVigente);
		// XXX: https://dzone.com/articles/hibernate-query-example-qbe
		@SuppressWarnings("deprecation")
		Criteria criteria = session.createCriteria(FacturaVigente.class).add(facturaVigenteEjemplo);
		facturas = criteria.list();
		for (FacturaVigente facturaVigente2 : facturas) {
			if (facturaVigente2.getFechaInicioDepreciacion() != null || facturaVigente2.getPorcentaje() != null) {
				throw new RuntimeException("Factura " + facturaVigente2.getId() + " no es consistente");
			}
		}
		return facturas;
	}

}
