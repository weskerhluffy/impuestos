package org.nada.dao;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.stream.StreamSupport;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nada.models.FacturaVigente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
// XXX: https://stackoverflow.com/questions/39690094/spring-boot-default-profile-for-integration-tests
@ActiveProfiles("test")
public class FacturaVigenteDAOTests {

	@Autowired
	FacturaVigenteDAO facturaVigenteDAO;

	@Test
	public void pruebaCustomizacion() {
		facturaVigenteDAO.metodoPrueba();
	}

	@Test
	public void pruebaCargaTodo() {
		Iterable<FacturaVigente> todos = facturaVigenteDAO.findAll();
		assertTrue(StreamSupport.stream(todos.spliterator(), false).count() > 0);
	}
}
