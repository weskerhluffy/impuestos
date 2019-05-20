package org.nada.models;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// XXX: https://stackoverflow.com/questions/5713508/hibernate-reverse-engineering-to-include-a-transient-property
/*
@Entity
@DiscriminatorValue("1") 
*/
public class FacturaVigenteExtendida extends FacturaVigente {
	private static final Logger LOGGER = LoggerFactory.getLogger(FacturaVigenteExtendida.class);

	public FacturaVigenteExtendida(FacturaVigente facturaVigente) {
		super(facturaVigente.getRfcEmisor(), facturaVigente.getFolio(), facturaVigente.getMonto(),
				facturaVigente.getPorcentaje(), facturaVigente.getFechaInicioDepreciacion(), facturaVigente.getAno(),
				facturaVigente.getMes(), facturaVigente.getPeriodo(), facturaVigente.getRazonSocialEmisor(),
				facturaVigente.getDescripcion(), facturaVigente.getIdMonto(), facturaVigente.getIdMontoDeducible(),
				facturaVigente.getIdPorcentaje(), facturaVigente.getIdFechaInicioDepreciacion());
		this.setId(facturaVigente.getId());
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -4604037152256894077L;

	@Transient
	public Double getMontoDepreciacionMensual() {
		LOGGER.debug("TMPH calculando monto mensual {}", this);
		var montoDepreciacionMensual = (this.getMonto() * (this.getPorcentaje() / 1200));
		return montoDepreciacionMensual;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
