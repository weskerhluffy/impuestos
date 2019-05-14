package org.nada.controller;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.xmlbeans.XmlException;
import org.nada.dao.FacturaDAO;
import org.nada.dao.FacturaVigenteDAO;
import org.nada.dao.MontoFacturaDAO;
import org.nada.models.Factura;
import org.nada.models.FacturaVigente;
import org.nada.models.FacturaVigenteExtendida;
import org.nada.models.FechaInicioDepreciacionFactura;
import org.nada.models.MontoDeducibleFactura;
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
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

import com.opencsv.CSVReader;

import mx.gob.sat.cfd.x3.ComprobanteDocument;
import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante.Conceptos.Concepto;

// XXX: http://zetcode.com/springboot/controller/
@Controller
public class FacturasPeriodoController {
	private static final Logger LOGGER = LoggerFactory.getLogger(FacturasPeriodoController.class);
	private static final String UPLOADING_DIR = System.getProperty("user.dir") + "/uploadingDir/";
	private static final SimpleDateFormat FORMATEADOR_FECHA = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

	private final FacturaVigenteDAO facturaVigenteDAO;
	private final EntityManager entityManager;
	private final FacturaDAO facturaDAO;
	private final MontoFacturaDAO montoFacturaDAO;

	@Autowired
	public FacturasPeriodoController(FacturaVigenteDAO facturaVigenteDAO, EntityManager entityManager,
			FacturaDAO facturaDAO, MontoFacturaDAO montoFacturaDAO) {
		this.facturaVigenteDAO = facturaVigenteDAO;
		this.entityManager = entityManager;
		this.facturaDAO = facturaDAO;
		this.montoFacturaDAO = montoFacturaDAO;
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
		var facturasDepreciadasOriginales = facturaVigenteDAO.findFacturasDepreciadas(periodo);
		var facturasDepreciadas = facturasDepreciadasOriginales.stream().map(f -> new FacturaVigenteExtendida(f))
				.collect(Collectors.toList());
		var montoDepreciacionMensualAcumuladaPorFacturaId = new HashMap<String, Double>();
		for (FacturaVigenteExtendida facturaVigente : facturasDepreciadas) {
			// @formatter:off
			// XXX: https://learn2program.wordpress.com/2008/07/23/how-to-use-int-as-the-key-of-a-map-to-display-in-freemarker/
            // @formatter:on
			LOGGER.debug("TMPH factura ext {}", facturaVigente);
			montoDepreciacionMensualAcumuladaPorFacturaId.put(facturaVigente.getId().toString(),
					calculaMontoDepreciacionAcumuladaMensual(facturaVigente, periodo));
		}
		// @formatter:off
		// XXX: https://stackoverflow.com/questions/41240414/equivalent-of-scalas-foldleft-in-java-8
        // @formatter:on
		Double sumaNoDepreciadas = facturasNoDepreciadas.stream().map(FacturaVigente::getMonto).reduce(0.0,
				(acc, c) -> acc + c);
		Double sumaDepreciadas = facturasDepreciadas.stream()
				.map(f -> calculaMontoDepreciacionAcumuladaMensual(new FacturaVigenteExtendida(f), periodo))
				.reduce(0.0, (acc, c) -> acc + c);
		params.put("montoDepreciacionMensualAcumuladaPorFacturaId", montoDepreciacionMensualAcumuladaPorFacturaId);
		params.put("facturasNoDepreciadas", facturasNoDepreciadas);
		params.put("facturasDepreciadas", facturasDepreciadas);
		params.put("sumaNoDepreciadas", sumaNoDepreciadas);
		params.put("sumaDepreciadas", sumaDepreciadas);

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
				Map.of(0, "periodo", 1, "folio", 2, "descripcion", 3, "rfcEmisor", 4, "razonSocialEmisor"),
				MontoFactura.class, Map.of(5, "monto"), FechaInicioDepreciacionFactura.class, Map.of(9, "fecha"),
				PorcentajeDepreciacionAnualFactura.class, Map.of(8, "porcentaje"));
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
				Factura facturaSalvada = facturaDAO.findByRfcEmisorAndFolio(factura.getRfcEmisor(), factura.getFolio());
				if (facturaSalvada != null) {
					factura = facturaSalvada;
				} else {
					factura.setFechaCreacion(fechaActual);
					factura.setFechaUltimaModificacion(fechaActual);
					entityManager.persist(factura);
					entityManager.flush();
				}
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
	private Double calculaMontoDepreciacionAcumuladaMensual(FacturaVigenteExtendida facturaVigente, Date periodo) {
		Double monto = facturaVigente.getMontoDepreciacionMensual();
		// @formatter:off
		// XXX: https://stackoverflow.com/questions/9474121/i-want-to-get-year-month-day-etc-from-java-date-to-compare-with-gregorian-cal/32363174#32363174
		// @formatter:on

		LocalDate periodoLocalDate = periodo.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		var ano = periodoLocalDate.getYear();
		LOGGER.debug("TMPH Ano de periodo {} es {}", ano, FORMATEADOR_FECHA.format(periodo));

		LocalDate inicioAnoLocalDate = LocalDate.of(ano, 1, 1);
		// @formatter:off
		// XXX: https://stackoverflow.com/questions/23215299/how-to-convert-a-localdate-to-an-instant
		// @formatter:on
//		YearMonth inicioAnoYearMonth = YearMonth.from(Instant.from(inicioAnoLocalDate));

		// @formatter:off
		// XXX: https://stackoverflow.com/questions/1086396/java-date-month-difference/34811261#34811261
		// @formatter:on
		var mesesTranscurridosDelAno = ChronoUnit.MONTHS.between(inicioAnoLocalDate, periodoLocalDate);
		LOGGER.debug("TMPH Meses entre {} y {} son {}", inicioAnoLocalDate, periodoLocalDate, mesesTranscurridosDelAno);

		return monto * mesesTranscurridosDelAno;
	}

	// @formatter:off
	// XXX: https://hellokoding.com/uploading-multiple-files-example-with-spring-boot/
	// @formatter:on
	@GetMapping("/subeXml")
	public ModelAndView uploading() {
		File file = new File(UPLOADING_DIR);
		var params = new HashMap<String, Object>();

		params.put("kha", "NOOO");
		params.put("files", file.listFiles());

		return new ModelAndView("uploading", params);
	}

	@Transactional
	@PostMapping(value = "/subeXml")
	public String uploadingPost(@RequestParam("uploadingFiles") MultipartFile[] uploadingFiles)
			throws IOException, DOMException, ParseException {
		Date ahora = new Date();
		for (MultipartFile uploadedFile : uploadingFiles) {
			File file = new File(UPLOADING_DIR + uploadedFile.getOriginalFilename());
			// XXX: https://www.baeldung.com/java-how-to-create-a-file
			FileUtils.touch(file);
			LOGGER.debug("TMPH arch temp {} existe {}", file, Files.exists(file.toPath()));
			uploadedFile.transferTo(file);

			ComprobanteDocument comprobanteDocument = null;
			try {
				comprobanteDocument = ComprobanteDocument.Factory.parse(file);
			} catch (XmlException e) {
				LOGGER.error("No se pudo parsear {} error {}", file, ExceptionUtils.getStackTrace(e));
			}

			if (comprobanteDocument != null) {

				var atributos = comprobanteDocument.getComprobante().getEmisor().getDomNode().getAttributes();
				var numeroAtributos = atributos.getLength();
				for (Integer i = 0; i < numeroAtributos; i++) {
					var atributo = atributos.item(i);
					LOGGER.debug("TMPH el echizo {}:{}", atributo.getNodeName(), atributo.getNodeValue());
				}
				var rfc = atributos.getNamedItem("Rfc").getNodeValue();
				var razonSocial = atributos.getNamedItem("Nombre").getNodeValue();

				var complemento = comprobanteDocument.getComprobante().getComplemento();
				var numeroHijosComplemento = complemento.getDomNode().getChildNodes().getLength();
				Node timbre = null;
				for (int i = 0; i < numeroHijosComplemento; i++) {
					var hijo = complemento.getDomNode().getChildNodes().item(i);
					LOGGER.debug("TMPH nombre {}", hijo.getNodeName());
					if (StringUtils.equals("tfd:TimbreFiscalDigital", hijo.getNodeName())) {
						timbre = hijo;
					}
				}
				var folio = timbre.getAttributes().getNamedItem("UUID").getNodeValue();
//				LOGGER.debug("TMPH el timbre {}:{}", folio.getNodeName(), folio.getNodeValue());
				var periodo = FORMATEADOR_FECHA
						.parse(timbre.getAttributes().getNamedItem("FechaTimbrado").getNodeValue());
//				LOGGER.debug("TMPH la fecha {}:{}", periodo.getNodeName(), periodo.getNodeValue());

				var conceptos = comprobanteDocument.getComprobante().getConceptos();
				for (Concepto concepto : conceptos.getConceptoArray()) {
					var atributosConcepto = concepto.getDomNode().getAttributes();
					LOGGER.debug("TMPH elc oncep {}:{}", atributosConcepto.getNamedItem("Descripcion").getNodeValue(),
							atributosConcepto.getNamedItem("Importe").getNodeValue());
				}
				var descripcion = String.join(";;;",
						Arrays.stream(conceptos.getConceptoArray())
								.map(c -> String.format("%s->%s",
										c.getDomNode().getAttributes().getNamedItem("Descripcion").getNodeValue(),
										c.getDomNode().getAttributes().getNamedItem("Importe").getNodeValue()))
								.collect(Collectors.toUnmodifiableList()));

				LOGGER.debug("TMPH la descripcion global {}", descripcion);

				var monto = Arrays.stream(conceptos.getConceptoArray())
						.map(c -> c.getDomNode().getAttributes().getNamedItem("Importe").getNodeValue())
						.collect(Collectors.summingDouble(i -> Double.valueOf(i)));
				LOGGER.debug("TMPH la suma de todos los males {}", monto);

				Factura factura = null;
				try {
					if ((factura = facturaDAO.findByRfcEmisorAndFolio(rfc, folio)) == null) {
						factura = new Factura(rfc, razonSocial, descripcion, folio, periodo, ahora, ahora);
						LOGGER.debug("TMPH factura nueva kedo {}", factura);
						factura = facturaDAO.save(factura);
						MontoFactura montoFactura = new MontoFactura(factura, monto, ahora);
						montoFactura = montoFacturaDAO.save(montoFactura);
						LOGGER.debug("TMPH se dio de alta monto {}", montoFactura);

					} else {
						LOGGER.debug("factura {} ya existia", factura);
					}

				} catch (RuntimeException e) {
					// XXX:
					// https://stackoverflow.com/questions/40301779/how-to-handle-a-psqlexception-in-java
					LOGGER.error("Factura {} ya esta dada de alta {}", factura, ExceptionUtils.getStackTrace(e));
				}
			}

		}

		return "redirect:/subeXml";
	}

	@GetMapping(value = "/modificaDepreciacion")
	public ModelAndView modificaDepreciacion(
			@RequestParam("periodo") @DateTimeFormat(pattern = "yyyy-MM-dd") Date periodo) {
		// @formatter:off
		// XXX: https://stackoverflow.com/questions/9474121/i-want-to-get-year-month-day-etc-from-java-date-to-compare-with-gregorian-cal/32363174#32363174
		// @formatter:on
		LocalDate localDate = periodo.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		Integer year = localDate.getYear();
		Integer month = localDate.getMonthValue();
		var facturas = facturaVigenteDAO.findByAnoAndMesOrderByPeriodo(year * 1.0, month * 1.0);
		var params = new HashMap<String, Object>();
		params.put("facturas", facturas);
		params.put("periodo", periodo);
		LOGGER.debug("TMPH ano {} mes {} facs {}", year, month, facturas);
		return new ModelAndView("modifica_montos", params);
	}

	@Transactional
	@PostMapping(value = "/modificaDepreciacion")
	public String actualizaDepreciacion(FacturaContainer facturaContainer,
			@DateTimeFormat(pattern = "yyyy-MM-dd") Date periodo) {
		Date tiempoCreacion = new Date();
		LOGGER.debug("TMPH AAAA {}", periodo);
		for (FacturaVigente facturaVigente : facturaContainer.getFacturas()) {
			Factura factura = facturaDAO.findById(facturaVigente.getId()).get();
			LOGGER.debug("TMPH procesando fact {}", facturaVigente);
			if ((facturaVigente.getPorcentaje() != null) != (facturaVigente.getFechaInicioDepreciacion() != null)) {
				throw new RuntimeException(String.format("factura %s tiene datos erroneos", facturaVigente));
			}
			if (facturaVigente.getPorcentaje() != null) {
				PorcentajeDepreciacionAnualFactura porcentajeDepreciacionAnualFactura = new PorcentajeDepreciacionAnualFactura(
						factura, facturaVigente.getPorcentaje(), tiempoCreacion);
				FechaInicioDepreciacionFactura fechaInicioDepreciacionFactura = new FechaInicioDepreciacionFactura(
						factura, facturaVigente.getFechaInicioDepreciacion(), tiempoCreacion);
				LOGGER.debug("TMPH guardando datos dep {}", porcentajeDepreciacionAnualFactura,
						fechaInicioDepreciacionFactura);
				entityManager.persist(porcentajeDepreciacionAnualFactura);
				entityManager.persist(fechaInicioDepreciacionFactura);
				entityManager.flush();
			}
			FacturaVigente facturaVigente2 = facturaVigenteDAO.findById(facturaVigente.getId()).get();
			if (facturaVigente.getMonto() != facturaVigente2.getMonto()) {
				MontoDeducibleFactura montoDeducibleFactura = new MontoDeducibleFactura(factura,
						facturaVigente.getMonto(), tiempoCreacion);
				LOGGER.debug("TMPH guardando datos monto {}", montoDeducibleFactura);
				entityManager.persist(montoDeducibleFactura);
			}
		}
		return "redirect:/modificaDepreciacion?periodo=" + FORMATEADOR_FECHA.format(periodo);
	}

}
