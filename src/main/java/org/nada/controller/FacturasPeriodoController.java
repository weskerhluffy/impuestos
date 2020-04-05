package org.nada.controller;

import java.beans.PropertyEditorSupport;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.nada.dao.DeclaracionVigenteDAO;
import org.nada.dao.FacturaDAO;
import org.nada.dao.FacturaVigenteDAO;
import org.nada.dao.MontoFacturaDAO;
import org.nada.models.ConceptoFactura;
import org.nada.models.Declaracion;
import org.nada.models.DeclaracionConceptoFactura;
import org.nada.models.DeclaracionFactura;
import org.nada.models.DeclaracionVigente;
import org.nada.models.Factura;
import org.nada.models.FacturaVigente;
import org.nada.models.FacturaVigenteExtendida;
import org.nada.models.FechaInicioDepreciacionFactura;
import org.nada.models.ImpuestosConceptoFactura;
import org.nada.models.MontoDeducibleFactura;
import org.nada.models.MontoFactura;
import org.nada.models.PorcentajeDepreciacionAnualFactura;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

import com.google.common.collect.Iterables;
import com.google.common.reflect.TypeToken;
import com.opencsv.CSVReader;

import mx.gob.sat.cfd._3.Comprobante;
import mx.gob.sat.cfd._3.Comprobante.Conceptos.Concepto;

// XXX: http://zetcode.com/springboot/controller/
@Controller
public class FacturasPeriodoController {
	private static final Logger LOGGER = LoggerFactory.getLogger(FacturasPeriodoController.class);
	private static final String UPLOADING_DIR = System.getProperty("user.dir") + "/uploadingDir/";
	private static final SimpleDateFormat FORMATEADOR_FECHA = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	private static final Unmarshaller jaxbUnmarshaller = generaXmlParser();

	private final FacturaVigenteDAO facturaVigenteDAO;
	private final EntityManager entityManager;
	private final FacturaDAO facturaDAO;
	private final MontoFacturaDAO montoFacturaDAO;
	private final DeclaracionVigenteDAO declaracionVigenteDAO;

	@Autowired
	public FacturasPeriodoController(FacturaVigenteDAO facturaVigenteDAO, EntityManager entityManager,
			FacturaDAO facturaDAO, MontoFacturaDAO montoFacturaDAO, DeclaracionVigenteDAO declaracionVigenteDAO) {
		this.facturaVigenteDAO = facturaVigenteDAO;
		this.entityManager = entityManager;
		this.facturaDAO = facturaDAO;
		this.montoFacturaDAO = montoFacturaDAO;
		this.declaracionVigenteDAO = declaracionVigenteDAO;
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
	// @formatter:off
	// XXX:
	// https://stackoverflow.com/questions/15164864/how-to-accept-date-params-in-a-get-request-to-spring-mvc-controller
	// @formatter:on
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
			// XXX:
			// https://learn2program.wordpress.com/2008/07/23/how-to-use-int-as-the-key-of-a-map-to-display-in-freemarker/
			// @formatter:on
			LOGGER.debug("TMPH factura ext {}", facturaVigente);
			montoDepreciacionMensualAcumuladaPorFacturaId.put(facturaVigente.getId().toString(),
					calculaMontoDepreciacionAcumuladaMensual(facturaVigente, periodo));
		}
		// @formatter:off
		// XXX:
		// https://stackoverflow.com/questions/41240414/equivalent-of-scalas-foldleft-in-java-8
		// @formatter:on
		Double sumaDepreciadas = facturasDepreciadas.stream()
				.map(f -> calculaMontoDepreciacionAcumuladaMensual(new FacturaVigenteExtendida(f), periodo))
				.reduce(0.0, (acc, c) -> acc + c);
		params.put("montoDepreciacionMensualAcumuladaPorFacturaId", montoDepreciacionMensualAcumuladaPorFacturaId);
		params.put("facturasNoDepreciadas", facturasNoDepreciadas);
		params.put("facturasDepreciadas", facturasDepreciadas);
		params.put("sumaDepreciadas", sumaDepreciadas);

		List<DeclaracionVigente> declaracionVigentes = new ArrayList<>();
		// @formatter:off
		// XXX:
		// https://stackoverflow.com/questions/6416706/easy-way-to-convert-iterable-to-collection
		// @formatter:on
		declaracionVigenteDAO.findAll().forEach(declaracionVigentes::add);
//		LOGGER.debug("TMPH declaraciones vigentes {}", declaracionVigentes);

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
		// TODO: Mapear varias propiedades a un solo indice
		Map<Class<?>, Map<Integer, String>> nombresPropiedades = Map.of(Factura.class,
				Map.of(0, "periodo", 1, "folio", 2, "descripcion", 3, "rfcEmisor", 4, "razonSocialEmisor"),
				MontoFactura.class, Map.of(5, "monto"), FechaInicioDepreciacionFactura.class, Map.of(9, "fecha"),
				PorcentajeDepreciacionAnualFactura.class, Map.of(8, "porcentaje"), ConceptoFactura.class,
				Map.of(2, "descripcion", 5, "importe"));

		Map<Class<?>, Map<String, ?>> valoresFijos = Map.of(ConceptoFactura.class,
				Map.of("claveProductoOServicio", "Clave manual csv", "cantidad", 1.0, "valorUnitario", 1.0, "descuento",
						0.0, "esDeducible", false, "claveUnidad", "manual_csv"));
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
					// @formatter:off
					// XXX:
					// https://stackoverflow.com/questions/13692700/good-way-to-get-any-value-from-a-java-set
					// @formatter:on
					Integer indiceCualquiera = nombresPropiedadesPorIndice.keySet().iterator().next();
					// @formatter:off
					// XXX:
					// https://stackoverflow.com/questions/14721397/checking-if-a-string-is-empty-or-null-in-java/14721414
					// @formatter:on
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

					for (Map.Entry<String, ?> llaveYValor : valoresFijos.getOrDefault(clase, Map.of()).entrySet()) {
						var nombrePropiedad = llaveYValor.getKey();
						var valor = llaveYValor.getValue();
						try {
							Field propiedad = clase.getDeclaredField(nombrePropiedad);
							propiedad.setAccessible(true);
							propiedad.set(instancia, valor);
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
		// XXX:
		// https://stackoverflow.com/questions/9474121/i-want-to-get-year-month-day-etc-from-java-date-to-compare-with-gregorian-cal/32363174#32363174
		// @formatter:on

		LocalDate periodoLocalDate = periodo.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		var ano = periodoLocalDate.getYear();
		LOGGER.debug("TMPH Ano de periodo {} es {}", ano, FORMATEADOR_FECHA.format(periodo));

		LocalDate inicioAnoLocalDate = LocalDate.of(ano, 1, 1);
		LocalDate inicioDepreciacion = inicioAnoLocalDate;

		if (convertToLocalDateViaInstant(facturaVigente.getFechaInicioDepreciacion())
				.compareTo(inicioAnoLocalDate) > 0) {
			inicioDepreciacion = convertToLocalDateViaInstant(facturaVigente.getFechaInicioDepreciacion());
		}

		// @formatter:off
		// XXX:
		// https://stackoverflow.com/questions/23215299/how-to-convert-a-localdate-to-an-instant
		// @formatter:on
//		YearMonth inicioAnoYearMonth = YearMonth.from(Instant.from(inicioAnoLocalDate));

		// @formatter:off
		// XXX:
		// https://stackoverflow.com/questions/1086396/java-date-month-difference/34811261#34811261
		// @formatter:on
		var mesesTranscurridosDelAno = ChronoUnit.MONTHS.between(inicioDepreciacion, periodoLocalDate) + 1;
		LOGGER.debug("TMPH Meses entre {} y {} son {}", inicioDepreciacion, periodoLocalDate, mesesTranscurridosDelAno);

		return monto * mesesTranscurridosDelAno;
	}

	// @formatter:off
	// XXX:
	// https://hellokoding.com/uploading-multiple-files-example-with-spring-boot/
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
			throws IOException, DOMException, ParseException, JAXBException {
		Date ahora = new Date();
		for (MultipartFile uploadedFile : uploadingFiles) {
			File file = new File(UPLOADING_DIR + uploadedFile.getOriginalFilename());
			// XXX: https://www.baeldung.com/java-how-to-create-a-file
			FileUtils.touch(file);
			LOGGER.debug("TMPH arch temp {} existe {}", file, Files.exists(file.toPath()));
			uploadedFile.transferTo(file);

			// XXX: https://howtodoinjava.com/jaxb/read-xml-to-java-object/
			Comprobante comprobante = (Comprobante) jaxbUnmarshaller.unmarshal(file);

			var rfc = comprobante.getEmisor().getRfc();
			var razonSocial = comprobante.getEmisor().getNombre();

			var complemento = comprobante.getComplemento();
			Element timbre = null;
			for (int i = 0; i < complemento.size(); i++) {
				for (Object complementoObj : complemento.get(i).getAny()) {
					// XXX: https://dzone.com/articles/jaxbs-xmlanyelementlaxtrue
					var hijo = (Element) complementoObj;
					LOGGER.debug("TMPH nombre {}", hijo.getNodeName());
					if (StringUtils.equals("tfd:TimbreFiscalDigital", hijo.getNodeName())) {
						timbre = hijo;
					}
				}
			}
			var folio = timbre.getAttributes().getNamedItem("UUID").getNodeValue();
//				LOGGER.debug("TMPH el timbre {}:{}", folio.getNodeName(), folio.getNodeValue());
			var periodo = FORMATEADOR_FECHA.parse(timbre.getAttributes().getNamedItem("FechaTimbrado").getNodeValue());
//				LOGGER.debug("TMPH la fecha {}:{}", periodo.getNodeName(), periodo.getNodeValue());

			var conceptos = comprobante.getConceptos().getConcepto();
			for (Concepto concepto : conceptos) {
				LOGGER.debug("TMPH elc oncep {}:{}", concepto.getDescripcion(), concepto.getImporte());
			}

			var descripcion = String.join(";;;",
					conceptos.stream()
							.map(c -> String.format("%s->%s", c.getDescripcion(),
									c.getImporte().doubleValue() - obtenValor(c.getDescuento())))
							.collect(Collectors.toUnmodifiableList()));

			LOGGER.debug("TMPH la descripcion global {}", descripcion);

			var monto = conceptos.stream().map(c -> c.getImporte().doubleValue() - obtenValor(c.getDescuento()))
					.collect(Collectors.summingDouble(i -> i.doubleValue()));
			LOGGER.debug("TMPH la suma de todos los males {}", monto);

			Factura factura = null;
			LOGGER.debug("TMPH buscando rfc {} folio {}", rfc, folio);
			try {
				if ((factura = facturaDAO.findByRfcEmisorAndFolio(rfc, folio)) == null) {
					factura = new Factura(rfc, razonSocial, descripcion, folio, periodo, ahora, ahora);
					LOGGER.debug("TMPH factura nueva kedo {}", factura);
					factura = facturaDAO.save(factura);
					MontoFactura montoFactura = new MontoFactura(factura, monto, ahora, null);
					montoFactura = montoFacturaDAO.save(montoFactura);
					LOGGER.debug("TMPH se dio de alta monto {}", montoFactura);

					for (Concepto concepto : conceptos) {
						ConceptoFactura conceptoFactura = new ConceptoFactura(factura, concepto.getClaveProdServ(),
								concepto.getCantidad().doubleValue(), concepto.getClaveUnidad(),
								concepto.getDescripcion(), concepto.getValorUnitario().doubleValue(),
								concepto.getImporte().doubleValue(), obtenValor(concepto.getDescuento()), ahora, false,
								null, null, null);
						entityManager.persist(conceptoFactura);
						for (var impuesto : concepto.getImpuestos().getTraslados().getTraslado()) {
							LOGGER.debug("Inpuesto {} antes de guardar ", impuesto);
							Double base = impuesto.getBase() != null ? impuesto.getBase().doubleValue() : 0;
							Double tasaOCuota = impuesto.getTasaOCuota() != null
									? impuesto.getTasaOCuota().doubleValue()
									: 0;
							Double importe = impuesto.getImporte() != null ? impuesto.getImporte().doubleValue() : 0;
							ImpuestosConceptoFactura impuestosConceptoFactura = new ImpuestosConceptoFactura(
									conceptoFactura, base, impuesto.getImpuesto(), impuesto.getTipoFactor().value(),
									tasaOCuota, importe, "trasladado");
							entityManager.persist(impuestosConceptoFactura);
						}
						LOGGER.debug("Concepto {} creado", conceptoFactura);
					}

				} else {
					LOGGER.debug("factura {} ya existia", factura);
				}

			} catch (RuntimeException e) {
				// @formatter:off
				// XXX:
				// https://stackoverflow.com/questions/40301779/how-to-handle-a-psqlexception-in-java
				// @formatter:on

				LOGGER.error("Factura {} ya esta dada de alta {}", factura, ExceptionUtils.getStackTrace(e));
			}

		}

		return "redirect:/subeXml";
	}

	@GetMapping(value = "/modificaDepreciacion")
	public ModelAndView modificaDepreciacion(
			@RequestParam("periodo") @DateTimeFormat(pattern = "yyyy-MM-dd") Date periodo) {
		// @formatter:off
		// XXX:
		// https://stackoverflow.com/questions/9474121/i-want-to-get-year-month-day-etc-from-java-date-to-compare-with-gregorian-cal/32363174#32363174
		// @formatter:on
		LocalDate localDate = periodo.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		Integer year = localDate.getYear();
		Integer month = localDate.getMonthValue();
		var facturas = facturaVigenteDAO.findByAnoAndMesOrderByPeriodo(year * 1.0, month * 1.0);
		var params = new HashMap<String, Object>();
		params.put("facturas", facturas);
		params.put("periodo", periodo);
//		LOGGER.debug("TMPH ano {} mes {} facs {}", year, month, facturas);
		return new ModelAndView("modifica_montos", params);
	}

	@Transactional
	@PostMapping(value = "/modificaDepreciacion")
	public String actualizaDepreciacion(FacturaContainer facturaContainer,
			@DateTimeFormat(pattern = "yyyy-MM-dd") Date periodo) {
		Date tiempoCreacion = new Date();
		LOGGER.debug("TMPH AAAA {}", periodo);
		for (FacturaVigente facturaVigente : facturaContainer.getFacturas()) {
			LOGGER.debug("TMPH procesando fact {}", facturaVigente);
			Factura factura = facturaDAO.findById(facturaVigente.getId()).get();
			LOGGER.debug("TMPH procesando conceptos {}", facturaVigente.getFactura().getConceptoFacturas());
			if ((facturaVigente.getPorcentaje() != null) != (facturaVigente.getFechaInicioDepreciacion() != null)) {
				throw new RuntimeException(String.format("factura %s tiene datos erroneos", facturaVigente));
			}
			if (facturaVigente.getPorcentaje() != null) {
				PorcentajeDepreciacionAnualFactura porcentajeDepreciacionAnualFactura = new PorcentajeDepreciacionAnualFactura(
						factura, facturaVigente.getPorcentaje(), tiempoCreacion, null);
				FechaInicioDepreciacionFactura fechaInicioDepreciacionFactura = new FechaInicioDepreciacionFactura(
						factura, facturaVigente.getFechaInicioDepreciacion(), tiempoCreacion, null);
				LOGGER.debug("TMPH guardando datos dep {}", porcentajeDepreciacionAnualFactura,
						fechaInicioDepreciacionFactura);
				entityManager.persist(porcentajeDepreciacionAnualFactura);
				entityManager.persist(fechaInicioDepreciacionFactura);
				entityManager.flush();
			}
			// TODO: Monto deducible de conceptos
			/*
			 * FacturaVigente facturaVigente2 =
			 * facturaVigenteDAO.findById(facturaVigente.getId()).get(); if
			 * (facturaVigente.getMonto() != facturaVigente2.getMonto()) {
			 * MontoDeducibleFactura montoDeducibleFactura = new
			 * MontoDeducibleFactura(factura, facturaVigente.getMonto(), tiempoCreacion,
			 * null); LOGGER.debug("TMPH guardando datos monto {} {}",
			 * facturaVigente.getMonto(), facturaVigente2.getMonto());
			 * entityManager.persist(montoDeducibleFactura);
			 * LOGGER.debug("TMPH guardados datos monto {}:{}", montoDeducibleFactura,
			 * montoDeducibleFactura.getId()); }
			 */
			for (var entry : facturaVigente.getFactura().getConceptoFacturasMapa().entrySet()) {
				var idConceptoFactura = entry.getKey();
				var conceptoFactura = entry.getValue();
				var conceptoFacturaGuardado = entityManager.find(ConceptoFactura.class, idConceptoFactura);
				if (conceptoFactura.getEsDeducible() != conceptoFacturaGuardado.getEsDeducible()) {
					conceptoFacturaGuardado.setEsDeducible(conceptoFactura.getEsDeducible());
					entityManager.persist(conceptoFacturaGuardado);
				}
			}
		}
		return "redirect:/modificaDepreciacion?periodo=" + FORMATEADOR_FECHA.format(periodo);
	}

	@Transactional
	@PostMapping(value = "/registraDeclaracion")
	public String registraDeclaracion(DeclaracionFacturasContainer declaracionFacturasContainer) {
		Date ahora = new Date();
		// TODO: Validar que sean del periodo o depreciadas vigentes
		Date periodoFactura = declaracionFacturasContainer.getDeclaracionFacturasNoDepreciadas().get(0).getFactura()
				.getPeriodo();

		LocalDate localDate = periodoFactura.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		Integer ano = localDate.getYear();
		Integer mes = localDate.getMonthValue();
		LocalDate periodoLocalDate = LocalDate.of(ano, mes, 1);
		var periodo = java.util.Date.from(periodoLocalDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

		Declaracion declaracion = new Declaracion(periodo, ahora);
		entityManager.persist(declaracion);
		LOGGER.debug("TMPH declaracion {}", declaracion);

		// XXX: https://www.baeldung.com/java-combine-multiple-collections
		for (DeclaracionFactura declaracionFactura : Iterables.concat(
				declaracionFacturasContainer.getDeclaracionFacturasNoDepreciadas(),
				declaracionFacturasContainer.getDeclaracionFacturasDepreciadas())) {
			declaracionFactura.setIdDeclaracion(declaracion.getId());
			declaracionFactura.setTiempoCreacion(ahora);
			// TODO: Validar que si hay porcentaje hay fecha inicio depreciacion
			entityManager.persist(declaracionFactura);

			for (ConceptoFactura conceptoFactura : declaracionFactura.getFactura().getConceptoFacturasMapa().values()) {
				DeclaracionConceptoFactura declaracionConceptoFactura = new DeclaracionConceptoFactura(conceptoFactura,
						declaracionFactura, ahora);
				entityManager.persist(declaracionConceptoFactura);
			}
			LOGGER.debug("TMPH declaracion fact {}", declaracionFactura);
		}
		return "redirect:/visualizaDeclaracion?periodo=" + FORMATEADOR_FECHA.format(periodo);
	}

	@GetMapping(value = "/visualizaDeclaracion")
	public ModelAndView visualizaDeclaracion(@DateTimeFormat(pattern = "yyyy-MM-dd") Date periodo) {
		DeclaracionVigente declaracionVigente = declaracionVigenteDAO.findByPeriodo(periodo);
		LOGGER.debug("TMPH declaracion vigente de {} es {}", periodo, declaracionVigente);
		LOGGER.debug("TMPH declaracion facts {}", periodo, declaracionVigente.getDeclaracionFacturas());
		var params = new HashMap<String, Object>();
		params.put("declaracionVigente", declaracionVigente);

		var montoDepreciacionMensualAcumuladaPorFacturaId = new HashMap<String, Double>();
		var declaracionFacturasDepreciadas = declaracionVigente.getDeclaracionFacturas().stream()
				.filter(df -> df.getFechaInicioDepreciacionFactura() != null).collect(Collectors.toList());
		var facturasNoDepreciadas = declaracionVigente.getDeclaracionFacturas().stream()
				.filter(df -> df.getFechaInicioDepreciacionFactura() == null).map(df -> df.getFacturaVigenteExtendida())
				.collect(Collectors.toList());
//		LOGGER.debug("TMPH facturas decl depr {}", declaracionFacturasDepreciadas);
		for (FacturaVigenteExtendida facturaVigente : declaracionFacturasDepreciadas.stream()
				.map(df -> new FacturaVigenteExtendida(df.getFacturaVigente())).collect(Collectors.toList())) {
			LOGGER.debug("TMPH factura ext {}", facturaVigente);
			montoDepreciacionMensualAcumuladaPorFacturaId.put(facturaVigente.getId().toString(),
					calculaMontoDepreciacionAcumuladaMensual(facturaVigente, periodo));
		}
		params.put("montoDepreciacionMensualAcumuladaPorFacturaId", montoDepreciacionMensualAcumuladaPorFacturaId);
		Double sumaDepreciadas = declaracionFacturasDepreciadas.stream()
				.map(f -> calculaMontoDepreciacionAcumuladaMensual(new FacturaVigenteExtendida(f.getFacturaVigente()),
						periodo))
				.reduce(0.0, (acc, c) -> acc + c);
		params.put("sumaDepreciadas", sumaDepreciadas);

		return new ModelAndView("visualizaDeclaracion", params);
	}

	// @formatter:off
	// XXX:
	// https://stackoverflow.com/questions/12544479/spring-mvc-type-conversion-propertyeditor-or-converter
	// XXX:
	// https://docs.spring.io/spring/docs/2.5.5/reference/mvc.html#mvc-ann-webdatabinder
	// XXX:
	// https://stackoverflow.com/questions/67980/how-do-i-register-a-custom-type-converter-in-spring
	// @formatter:on
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(MontoFactura.class, new MontoFacturaEditor(entityManager));
		binder.registerCustomEditor(Factura.class, new FacturaEditor(entityManager));
		binder.registerCustomEditor(MontoDeducibleFactura.class, new MontoDeducibleFacturaEditor(entityManager));
		binder.registerCustomEditor(FechaInicioDepreciacionFactura.class,
				new FechaInicioDepreciacionFacturaEditor(entityManager));
		binder.registerCustomEditor(PorcentajeDepreciacionAnualFactura.class,
				new PorcentajeDepreciacionAnualFacturaEditor(entityManager));
	}

	// XXX: https://www.baeldung.com/java-date-to-localdate-and-localdatetime
	public static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
		return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static Date convertToDateViaInstant(LocalDate dateToConvert) {
		return java.util.Date.from(dateToConvert.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}

	// XXX:
	// https://stackoverflow.com/questions/19650917/how-to-convert-bigdecimal-to-double-in-java
	public static Double obtenValor(BigDecimal valor) {
		return valor != null ? valor.doubleValue() : 0;
	}

	private static Unmarshaller generaXmlParser() {
		// XXX: https://howtodoinjava.com/jaxb/read-xml-to-java-object/
		try {
			return JAXBContext.newInstance(Comprobante.class).createUnmarshaller();
		} catch (JAXBException e) {
			LOGGER.error("No se pudo generar parse de factura {}", ExceptionUtils.getStackTrace(e));
			throw new Error(e);
		}
	}
}

// XXX: https://www.baeldung.com/spring-mvc-custom-property-editor
class MontoFacturaEditor extends ModeloFacturaEditorImpl<MontoFactura> {

	public MontoFacturaEditor(EntityManager entityManager) {
		super(entityManager);
	}
}

class FacturaEditor extends ModeloFacturaEditorImpl<Factura> {

	public FacturaEditor(EntityManager arg0) {
		super(arg0);
	}

}

interface ModeloFacturaEditor<T> {
	public String getAsText();

	public void setAsText(String text) throws IllegalArgumentException;
}

// XXX: https://stackoverflow.com/questions/3437897/how-to-get-a-class-instance-of-generics-type-t/22675121
class ModeloFacturaEditorImpl<T> extends PropertyEditorSupport implements ModeloFacturaEditor<T> {
	private final EntityManager entityManager;
	private static final Logger LOGGER = LoggerFactory.getLogger(ModeloFacturaEditor.class);

	private Class<T> entityBeanType;

	@SuppressWarnings("unchecked")
	public ModeloFacturaEditorImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
		this.entityBeanType = ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0]);
		LOGGER.debug("TMPH tiipo {}", entityBeanType);
	}

	@Override
	public String getAsText() {
		@SuppressWarnings("unchecked")
		T modelo = (T) getValue();
		String idStr = null;
		if (modelo != null) {
			Field field;
			try {
				// @formatter:off
				// XXX:
				// https://stackoverflow.com/questions/13400075/reflection-generic-get-field-value
				// @formatter:on
				field = modelo.getClass().getDeclaredField("id");
				field.setAccessible(true);
				Integer id = (Integer) field.get(modelo);
				field.setAccessible(false);
				if (id != null) {
					idStr = id.toString();
				}
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				LOGGER.debug("No se pudo obtener id de {} error {}", modelo, ExceptionUtils.getStackTrace(e));
			}
		}

		return idStr;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		LOGGER.debug("TMPH transformando de {} a {}", text, entityBeanType);
		// @formatter:off
		// XXX:
		// https://stackoverflow.com/questions/5439529/determine-if-a-string-is-an-integer-in-java
		// @formatter:on
		if (StringUtils.isEmpty(text) || !StringUtils.isNumeric(text)) {
			setValue(null);
		} else {
			Integer id = Integer.parseInt(text);
			LOGGER.debug("TMPH id es {}", id);
			TypeToken<T> t = new TypeToken<T>(getClass()) {

				/**
				 * 
				 */
				private static final long serialVersionUID = -8926339339378748079L;
			};
			LOGGER.debug("TMPH type tok es {} {}", t, t.getRawType());

			T modelo = (T) entityManager.find(entityBeanType, id);
			LOGGER.debug("TMPH pasado de {} a {}", id, modelo);
			setValue(modelo);
		}
	}

}

class DeclaracionFacturasContainer {
	private List<DeclaracionFactura> declaracionFacturasNoDepreciadas;
	private List<DeclaracionFactura> declaracionFacturasDepreciadas;

	public List<DeclaracionFactura> getDeclaracionFacturasNoDepreciadas() {
		return declaracionFacturasNoDepreciadas;
	}

	public void setDeclaracionFacturasNoDepreciadas(List<DeclaracionFactura> declaracionFacturasNoDepreciadas) {
		this.declaracionFacturasNoDepreciadas = declaracionFacturasNoDepreciadas;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public List<DeclaracionFactura> getDeclaracionFacturasDepreciadas() {
		return declaracionFacturasDepreciadas;
	}

	public void setDeclaracionFacturasDepreciadas(List<DeclaracionFactura> declaracionFacturasDepreciadas) {
		this.declaracionFacturasDepreciadas = declaracionFacturasDepreciadas;
	}

}

class MontoDeducibleFacturaEditor extends ModeloFacturaEditorImpl<MontoDeducibleFactura> {

	public MontoDeducibleFacturaEditor(EntityManager entityManager) {
		super(entityManager);
	}
}

class FechaInicioDepreciacionFacturaEditor extends ModeloFacturaEditorImpl<FechaInicioDepreciacionFactura> {

	public FechaInicioDepreciacionFacturaEditor(EntityManager entityManager) {
		super(entityManager);
	}
}

class PorcentajeDepreciacionAnualFacturaEditor extends ModeloFacturaEditorImpl<PorcentajeDepreciacionAnualFactura> {

	public PorcentajeDepreciacionAnualFacturaEditor(EntityManager entityManager) {
		super(entityManager);
	}
}
