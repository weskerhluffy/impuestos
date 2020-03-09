package org.nada.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.nada.models.FacturaVigente;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		// TODO: Crear named query, tiene que ser en plantillas de autogeneracion de
		// pojos
		// XXX:
		// https://stackoverflow.com/questions/15948795/is-it-possible-to-use-raw-sql-within-a-spring-repository
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);

		Query q = entityManager.createNativeQuery(
				"select f.*,0 as dtype from factura_vigente f where f.ano=:ano and f.mes=:mes and f.porcentaje is null order by f.periodo",
				FacturaVigente.class);
		// XXX: https://www.baeldung.com/java-year-month-day
		q.setParameter("ano", calendar.get(Calendar.YEAR));
		q.setParameter("mes", calendar.get(Calendar.MONTH) + 1);

		facturas = q.getResultList();
		for (FacturaVigente facturaVigente2 : facturas) {
			if (facturaVigente2.getFechaInicioDepreciacion() != null || facturaVigente2.getPorcentaje() != null) {
				throw new RuntimeException("Factura " + facturaVigente2.getId() + " no es consistente");
			}
		}
		return facturas;
	}

}
