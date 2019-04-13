package org.nada.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FacturaVigenteDAOTests {

	@Autowired
	FacturaVigenteDAO facturaVigenteDAO;

	@Test
	public void pruebaCustomizacion() {
		facturaVigenteDAO.metodoPrueba();
	}
}
