package org.nada.dao;

import java.util.Date;
import java.util.List;

import org.nada.models.FacturaVigente;

public interface FacturaVigenteDAOCustom {
	public void metodoPrueba();

	public List<FacturaVigente> getFacturasSinDepreciacion(Date fecha);

}
