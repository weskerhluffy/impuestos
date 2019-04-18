package org.nada.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;

import org.nada.dao.FacturaVigenteDAO;
import org.nada.models.FacturaVigente;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

// XXX: http://zetcode.com/springboot/controller/
@Controller
public class FacturasPeriodoController {
	private static final Logger LOGGER = LoggerFactory.getLogger(FacturasPeriodoController.class);
	private FacturaVigenteDAO facturaVigenteDAO;

	@Autowired
	public FacturasPeriodoController(FacturaVigenteDAO facturaVigenteDAO) {
		this.facturaVigenteDAO = facturaVigenteDAO;
	}

	@RequestMapping(value = "/getDateAndTime")
	public ModelAndView getDateAndTime() {

		var now = LocalDateTime.now();
		var dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		var date_time = dtf.format(now);

		var params = new HashMap<String, Object>();
		params.put("date_time", date_time);

		return new ModelAndView("showMessage", params);
	}

	@RequestMapping(value = "/calculaGastos", method = RequestMethod.GET)
	// XXX:
	// https://stackoverflow.com/questions/15164864/how-to-accept-date-params-in-a-get-request-to-spring-mvc-controller
	public ModelAndView calculaGastos(@RequestParam("periodo") @DateTimeFormat(pattern = "yyyy-MM-dd") Date periodo) {
		LOGGER.debug("TMPH periodo {}", periodo);
		var params = new HashMap<String, Object>();
		params.put("periodo", periodo);
		var facturasNoDepreciadas = facturaVigenteDAO.getFacturasSinDepreciacion(periodo);
		var facturasDepreciadas = facturaVigenteDAO.findFacturasDepreciadas(periodo);
		var montoDepreciacionAnualPorFacturaId = new HashMap<Integer, Double>();
		for (FacturaVigente facturaVigente : facturasDepreciadas) {
			montoDepreciacionAnualPorFacturaId.put(facturaVigente.getId(),
					calculaMontoDepreciacionMensual(facturaVigente));
		}
		params.put("montoDepreciacionAnualPorFacturaId", montoDepreciacionAnualPorFacturaId);
		params.put("facturasNoDepreciadas", facturasNoDepreciadas);
		params.put("facturasDepreciadas", facturasDepreciadas);

		return new ModelAndView("tabla_gastos", params);
	}

	// TODO: Mover a modelo
	private Double calculaMontoDepreciacionMensual(FacturaVigente facturaVigente) {
		Double monto;
		monto = (facturaVigente.getMonto() * (facturaVigente.getPorcentaje() / 1200));
		return monto;
	}

}
