package org.nada.dao;

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.StreamSupport;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nada.models.FacturaVigente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
// XXX: https://stackoverflow.com/questions/39690094/spring-boot-default-profile-for-integration-tests
@ActiveProfiles("test")
public class FacturaVigenteDAOTests {

	private static final String FECHA_INICIAL = "2019-01-01";
	private static final LocalDate FECHA_INICIAL_LOCAL_DATE = LocalDate.parse(FECHA_INICIAL);
	@Autowired
	FacturaVigenteDAO facturaVigenteDAO;

	@Test
	public void pruebaCustomizacion() {
		facturaVigenteDAO.metodoPrueba();
	}

	@Test
	public void pruebaCargaTodo() {
		Iterable<FacturaVigente> todos = facturaVigenteDAO.findAll();
		// XXX:
		// https://stackoverflow.com/questions/9720195/what-is-the-best-way-to-get-the-count-length-size-of-an-iterator
		assertTrue(StreamSupport.stream(todos.spliterator(), false).count() > 0);
	}

	@Test
	@Sql({ "/test_facturas_sin_depreciar.sql" })
	public void pruebaFacturasSinDepreciar() {
		Date ahora = Date.from(FECHA_INICIAL_LOCAL_DATE.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		List<FacturaVigente> facturaVigentes = facturaVigenteDAO.getFacturasSinDepreciacion(ahora);
		assertTrue(facturaVigentes.size() == 3);
	}
}
