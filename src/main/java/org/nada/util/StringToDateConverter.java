package org.nada.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

// XXX: https://www.baeldung.com/spring-type-conversions
// XXX: https://stackoverflow.com/questions/46728972/custom-converter-for-requestparam-in-spring-mvc
// XXX: https://blog.codecentric.de/en/2017/08/parsing-of-localdate-query-parameters-in-spring-boot/
public class StringToDateConverter implements Converter<String, Date> {
	private static final SimpleDateFormat FORMATEADOR_FECHA = new SimpleDateFormat("yyyy-MM-dd");
	private static final Logger LOGGER = LoggerFactory.getLogger(StringToDateConverter.class);

	@Override
	public Date convert(String source) {
		Date fecha = null;
		LOGGER.debug("TMPH convirtiendo '{}'", source);
		try {
			fecha = FORMATEADOR_FECHA.parse(source);
		} catch (ParseException e) {
			LOGGER.error("No se pudo convertir de {} a fecha {}", source, ExceptionUtils.getStackTrace(e));
//			throw new RuntimeException(e);
		}
		return fecha;
	}

}
