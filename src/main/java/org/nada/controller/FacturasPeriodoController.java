package org.nada.controller;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.nada.dao.FacturaVigenteDAO;
import org.nada.models.Factura;
import org.nada.models.FacturaVigente;
import org.nada.models.FechaInicioDepreciacionFactura;
import org.nada.models.MontoFactura;
import org.nada.models.PorcentajeDepreciacionAnualFactura;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.opencsv.CSVReader;

// XXX: http://zetcode.com/springboot/controller/
@Controller
public class FacturasPeriodoController {
	private static final Logger LOGGER = LoggerFactory.getLogger(FacturasPeriodoController.class);
	private FacturaVigenteDAO facturaVigenteDAO;
	private EntityManager entityManager;

	@Autowired
	public FacturasPeriodoController(FacturaVigenteDAO facturaVigenteDAO, EntityManager entityManager) {
		this.facturaVigenteDAO = facturaVigenteDAO;
		this.entityManager = entityManager;
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
		var montoDepreciacionAnualPorFacturaId = new HashMap<String, Double>();
		for (FacturaVigente facturaVigente : facturasDepreciadas) {
			// XXX: https://learn2program.wordpress.com/2008/07/23/how-to-use-int-as-the-key-of-a-map-to-display-in-freemarker/
			montoDepreciacionAnualPorFacturaId.put(facturaVigente.getId().toString(),
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

	@Transactional
	@PostMapping("/subeCsv")
	public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, NoSuchFieldException {

		redirectAttributes.addFlashAttribute("message", "You successfully uploadeo" + file.getOriginalFilename() + "!");
		LOGGER.debug("TMPH se subio {}", file.getOriginalFilename());
		// XXX:
		// https://stackoverflow.com/questions/2625546/is-using-the-class-instance-as-a-map-key-a-best-practice
		Map<Class<?>, Map<Integer, String>> nombresPropiedades = Map.of(Factura.class,
				Map.of(0, "periodo", 1, "folio", 3, "rfcEmisor"), MontoFactura.class, Map.of(5, "monto"),
				FechaInicioDepreciacionFactura.class, Map.of(9, "fecha"), PorcentajeDepreciacionAnualFactura.class,
				Map.of(8, "porcentaje"));
		// XXX:
		// https://stackoverflow.com/questions/24339990/how-to-convert-a-multipart-file-to-file
		var convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
		CSVReader reader = null;
		try {
			file.transferTo(convFile);
			// XXX: https://www.mkyong.com/java/how-to-read-and-parse-csv-file-in-java/
			reader = new CSVReader(new FileReader(convFile));
			String[] line;
			while ((line = reader.readNext()) != null) {
				Map<Class<?>, Serializable> instanciasDeLinea = new HashMap<>();
				for (Map.Entry<Class<?>, Map<Integer, String>> entry : nombresPropiedades.entrySet()) {
					Class<?> clase = entry.getKey();
					Map<Integer, String> nombresPropiedadesPorIndice = entry.getValue();
					// XXX:
					// https://stackoverflow.com/questions/13692700/good-way-to-get-any-value-from-a-java-set
					Integer indiceCualquiera = nombresPropiedadesPorIndice.keySet().iterator().next();
					// XXX:
					// https://stackoverflow.com/questions/14721397/checking-if-a-string-is-empty-or-null-in-java/14721414
					if (!StringUtils.isEmpty(line[indiceCualquiera])) {
						// XXX:
						// https://stackoverflow.com/questions/46393863/what-to-use-instead-of-class-newinstance
						Serializable instancia = (Serializable) clase.getDeclaredConstructor().newInstance();
						for (Map.Entry<Integer, String> entry1 : nombresPropiedadesPorIndice.entrySet()) {
							Integer indice = entry1.getKey();
							String nombrePropiedad = entry1.getValue();

							String valorPropiedad = line[indice];

							Field propiedad = clase.getDeclaredField(nombrePropiedad);
							// XXX:
							// https://stackoverflow.com/questions/1239581/why-is-it-allowed-to-access-java-private-fields-via-reflection
							propiedad.setAccessible(true);
							Class<?> tipoPropiedad = propiedad.getType();
							if (Objects.equals(tipoPropiedad, Double.class)) {
								propiedad.set(instancia, Double.valueOf(valorPropiedad));
							} else {
								if (Objects.equals(tipoPropiedad, Date.class)) {
									// XXX: https://www.mkyong.com/java8/java-8-how-to-convert-string-to-localdate/
									LocalDate localDate = LocalDate.parse(valorPropiedad);
									Date fecha = java.util.Date
											.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
									propiedad.set(instancia, fecha);
								} else {
									propiedad.set(instancia, valorPropiedad);
								}
							}
							propiedad.setAccessible(false);
						}
						instanciasDeLinea.put(clase, instancia);
						LOGGER.debug("TMPH objecto {} creado", instancia);
					}
				}

				Factura factura = (Factura) instanciasDeLinea.get(Factura.class);
				Date fechaActual = new Date();
				factura.setFechaCreacion(fechaActual);
				factura.setFechaUltimaModificacion(fechaActual);
				entityManager.persist(factura);
				entityManager.flush();
				LOGGER.debug("TMPH factura guardada {}", factura);
				for (Map.Entry<Class<?>, Serializable> entry : instanciasDeLinea.entrySet()) {
					Class<?> clase = entry.getKey();
					Serializable instancia = entry.getValue();
					try {
						Field propiedad = clase.getDeclaredField("factura");
						propiedad.setAccessible(true);
						propiedad.set(instancia, factura);
						propiedad.setAccessible(false);
					} catch (NoSuchFieldException e) {
						continue;
					}

					LOGGER.debug("TMPH objecto {} seteada fact", instancia);
					for (String nombrePropiedad : List.of("tiempoCreacion")) {
						try {
							Field propiedad = clase.getDeclaredField(nombrePropiedad);
							propiedad.setAccessible(true);
							propiedad.set(instancia, fechaActual);
							propiedad.setAccessible(false);
						} catch (NoSuchFieldException e) {
							continue;
						}
					}
					entityManager.persist(instancia);
					LOGGER.debug("TMPH objecto {} setadas prop", instancia);

				}
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
		LOGGER.debug("TMPH calculando monto mensual {}", facturaVigente);
		monto = (facturaVigente.getMonto() * (facturaVigente.getPorcentaje() / 1200));
		return monto;
	}

}
