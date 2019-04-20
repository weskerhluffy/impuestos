package org.nada.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	private static final Logger LOGGER = LoggerFactory.getLogger(WebConfig.class);

	@Override
	public void addFormatters(FormatterRegistry registry) {
		LOGGER.info("TMPH Registrando date conv");
		registry.addConverter(new StringToDateConverter());
	}
}
