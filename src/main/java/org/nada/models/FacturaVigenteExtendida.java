package org.nada.models;

// XXX: https://stackoverflow.com/questions/5713508/hibernate-reverse-engineering-to-include-a-transient-property
public class FacturaVigenteExtendida extends FacturaVigente {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4604037152256894077L;
	
	private Double montoDepreciacionMensual;

	public Double getMontoDepreciacionMensual() {
		return montoDepreciacionMensual;
	}

	public void setMontoDepreciacionMensual(Double montoDepreciacionMensual) {
		this.montoDepreciacionMensual = montoDepreciacionMensual;
	}

}
