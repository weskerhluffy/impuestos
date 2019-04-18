package org.nada.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.nada.dao.FacturaVigenteDAO;
import org.nada.models.FacturaVigente;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.opencsv.CSVReader;

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

	@GetMapping(value = "/calculaGastos")
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

	// XXX: https://spring.io/guides/gs/uploading-files/
	@GetMapping(value = "/aSubirCsv")
	public String subeCsv() {
		return "formaSubeCsv";
	}

	@PostMapping("/subeCsv")
	public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

		redirectAttributes.addFlashAttribute("message", "You successfully uploadeo" + file.getOriginalFilename() + "!");
		LOGGER.debug("TMPH se subio {}", file.getOriginalFilename());

		// XXX:
		// https://stackoverflow.com/questions/24339990/how-to-convert-a-multipart-file-to-file
		var convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
		CSVReader reader = null;
		try {
			file.transferTo(convFile);
			reader = new CSVReader(new FileReader(convFile));
			String[] line;
			while ((line = reader.readNext()) != null) {
				Integer i = 0;
				for (String linea : line) {
					LOGGER.debug("Campo {} tiene valor {}", i, linea);
					i++;
				}
//				System.out.println("Country [id= " + line[0] + ", code= " + line[1] + " , name=" + line[2] + "]");
			}
		} catch (IllegalStateException | IOException e1) {
			LOGGER.error("No se pudo leer archivo {}, la razon {}", file.getOriginalFilename(),
					ExceptionUtils.getStackTrace(e1));
		}

		return "redirect:/aSubirCsv";
	}

	// TODO: Mover a modelo
	private Double calculaMontoDepreciacionMensual(FacturaVigente facturaVigente) {
		Double monto;
		monto = (facturaVigente.getMonto() * (facturaVigente.getPorcentaje() / 1200));
		return monto;
	}

}
