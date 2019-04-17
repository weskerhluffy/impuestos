package org.nada.dao;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nada.models.Factura;
import org.nada.models.FacturaVigente;
import org.nada.models.FechaInicioDepreciacionFactura;
import org.nada.models.MontoDeducibleFactura;
import org.nada.models.MontoFactura;
import org.nada.models.PorcentajeDepreciacionAnualFactura;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;

// XXX: https://stackoverflow.com/questions/35004139/how-to-populate-test-data-programmatically-for-integration-tests-in-spring
@Slf4j
@RunWith(SpringRunner.class)
@DataJpaTest
// TODO: Use lombok
// XXX: https://stackoverflow.com/questions/36849673/running-datajpatest-with-postgresql
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class FacturaVigenteBDRealDAOTests {
	private static final Logger LOGGER = LoggerFactory.getLogger(FacturaVigenteBDRealDAOTests.class);

	// XXX:
	// https://stackoverflow.com/questions/4699381/best-way-to-inject-hibernate-session-by-spring-3
	private List<Factura> facturas = new ArrayList<>();
	private Map<Integer, MontoFactura> ultimoMontoFacturas = new HashMap<>();
	private Map<Integer, MontoDeducibleFactura> ultimoMontoDeducibleFacturas = new HashMap<>();
	private Map<Integer, FechaInicioDepreciacionFactura> ultimaFechaInicioDepreciacionFacturas = new HashMap<>();
	private Map<Integer, PorcentajeDepreciacionAnualFactura> ultimoPorcentajeDepreciacionAnualFacturas = new HashMap<>();
	@Autowired
	private TestEntityManager entityManager;

	@Before
	public void setup() {
		facturas = new ArrayList<Factura>();
		Date ahora = new Date();
		ultimoMontoDeducibleFacturas = new HashMap<>();
		ultimaFechaInicioDepreciacionFacturas = new HashMap<>();
		ultimoPorcentajeDepreciacionAnualFacturas = new HashMap<>();

		for (Integer i = 0; i < 10; i++) {
			// XXX:
			// https://stackoverflow.com/questions/6389827/string-variable-interpolation-java
			Factura factura = new Factura(String.format("rfc%02d", i), String.format("folio_%02d", i), ahora, ahora,
					ahora);
			Integer facturaId = (Integer) entityManager.persistAndGetId(factura);
			factura.setId(facturaId);

			for (Integer j = 0; j < 10; j++) {
				MontoFactura montoFactura = new MontoFactura(factura, i * 10, new Date());
				MontoDeducibleFactura montoDeducibleFactura = new MontoDeducibleFactura(factura, j * 0.9, new Date());
				FechaInicioDepreciacionFactura fechaInicioDepreciacionFactura = new FechaInicioDepreciacionFactura(
						factura, new Date(), new Date());
				PorcentajeDepreciacionAnualFactura porcentajeDepreciacionAnualFactura = new PorcentajeDepreciacionAnualFactura(
						factura, Math.min((j + 1) * 10, 99), new Date());
				LOGGER.debug("TMPH porcentaje dep {} para fact {}", porcentajeDepreciacionAnualFactura, factura);

				entityManager.persist(montoFactura);
				entityManager.persist(montoDeducibleFactura);
				entityManager.persist(fechaInicioDepreciacionFactura);
				entityManager.persist(porcentajeDepreciacionAnualFactura);

				ultimoMontoFacturas.put(facturaId, montoFactura);
				ultimoMontoDeducibleFacturas.put(facturaId, montoDeducibleFactura);
				ultimaFechaInicioDepreciacionFacturas.put(facturaId, fechaInicioDepreciacionFactura);
				ultimoPorcentajeDepreciacionAnualFacturas.put(facturaId, porcentajeDepreciacionAnualFactura);

			}

			facturas.add(factura);
		}
		entityManager.flush();
	}

	@Test
	public void testVigentes() {
		for (Factura factura : facturas) {
			Integer facturaId = factura.getId();
			FacturaVigente facturaVigente = entityManager.find(FacturaVigente.class, facturaId);
			// TODO: Anadir tostring a plantilla de pojo
			LOGGER.debug("TMPH factura vigente enc {} ultima fecha inicio dep {}",
					facturaVigente.getFechaInicioDepreciacion().toString(),
					ultimaFechaInicioDepreciacionFacturas.get(facturaId).getFecha());
			assertTrue(facturaVigente.getMonto() == ultimoMontoDeducibleFacturas.get(facturaId).getMonto());
			// XXX:
			// https://stackoverflow.com/questions/1439779/how-to-compare-two-dates-without-the-time-portion
			assertTrue(DateUtils.isSameDay(facturaVigente.getFechaInicioDepreciacion(),
					ultimaFechaInicioDepreciacionFacturas.get(facturaId).getFecha()));
			assertTrue(Objects.equals(facturaVigente.getPorcentaje(),
					ultimoPorcentajeDepreciacionAnualFacturas.get(facturaId).getPorcentaje()));
		}
	}

	@Test
	public void testVigenteSinDepreciacion() {
		Date ahora = new Date();
		Factura factura = new Factura(String.format("rfc%02d", 99), String.format("folio_%02d", 99), ahora, ahora,
				ahora);
		Integer facturaId = (Integer) entityManager.persistAndGetId(factura);

		for (Integer i = 0; i < 10; i++) {
			MontoFactura montoFactura = new MontoFactura(factura, i * 10, new Date());

			entityManager.persist(montoFactura);

			ultimoMontoFacturas.put(facturaId, montoFactura);
		}

		FacturaVigente facturaVigente = entityManager.find(FacturaVigente.class, facturaId);
		LOGGER.debug("TMPH fact vig {}", facturaVigente);

		assertTrue(facturaVigente.getMonto() == ultimoMontoFacturas.get(facturaId).getMonto());
		assertNull(facturaVigente.getPorcentaje());
		assertNull(facturaVigente.getFechaInicioDepreciacion());
	}
}
