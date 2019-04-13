package org.nada.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class FacturaVigenteDAOCustomImpl implements FacturaVigenteDAOCustom {

	private static final Logger LOGGER = LoggerFactory.getLogger(FacturaVigenteDAOCustomImpl.class);
	// XXX:
	// https://stackoverflow.com/questions/26606608/how-to-manually-start-a-transaction-on-a-shared-entitymanager-in-spring
	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public void metodoPrueba() {
		// TODO Auto-generated method stub
		System.out.print("TMPH ALGO\n");
		LOGGER.debug("TMPH BABY shark {}",
				entityManager.createNativeQuery("select * from factura_vigente limit 1").getResultList());
	}

}
