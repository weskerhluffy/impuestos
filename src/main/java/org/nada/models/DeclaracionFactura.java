package org.nada.models;
// Generated 18 may 2019 18:06:05 by Hibernate Tools 5.2.5.Final

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * DeclaracionFactura generated by hbm2java
 */
@Entity
@Table(name = "declaracion_factura", schema = "public")
public class DeclaracionFactura implements java.io.Serializable {

	private Integer id;
	private Declaracion declaracion;
	private Factura factura;
	private FechaInicioDepreciacionFactura fechaInicioDepreciacionFactura;
	private MontoDeducibleFactura montoDeducibleFactura;
	private MontoFactura montoFactura;
	private PorcentajeDepreciacionAnualFactura porcentajeDepreciacionAnualFactura;
	private Date tiempoCreacion;

	private DeclaracionVigente declaracionVigente;

	@ManyToOne(targetEntity = DeclaracionVigente.class)
	@JoinColumn(name = "id_declaracion", updatable = false, insertable = false)
	public DeclaracionVigente getDeclaracionVigente() {
		return declaracionVigente;
	}

	public void setDeclaracionVigente(DeclaracionVigente declaracionVigente) {
		this.declaracionVigente = declaracionVigente;
	}

	public DeclaracionFactura() {
	}

	public DeclaracionFactura(Declaracion declaracion, Factura factura, MontoFactura montoFactura,
			Date tiempoCreacion) {
		this.declaracion = declaracion;
		this.factura = factura;
		this.montoFactura = montoFactura;
		this.tiempoCreacion = tiempoCreacion;
	}

	public DeclaracionFactura(Declaracion declaracion, Factura factura,
			FechaInicioDepreciacionFactura fechaInicioDepreciacionFactura, MontoDeducibleFactura montoDeducibleFactura,
			MontoFactura montoFactura, PorcentajeDepreciacionAnualFactura porcentajeDepreciacionAnualFactura,
			Date tiempoCreacion) {
		this.declaracion = declaracion;
		this.factura = factura;
		this.fechaInicioDepreciacionFactura = fechaInicioDepreciacionFactura;
		this.montoDeducibleFactura = montoDeducibleFactura;
		this.montoFactura = montoFactura;
		this.porcentajeDepreciacionAnualFactura = porcentajeDepreciacionAnualFactura;
		this.tiempoCreacion = tiempoCreacion;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_declaracion", nullable = false)
	public Declaracion getDeclaracion() {
		return this.declaracion;
	}

	public void setDeclaracion(Declaracion declaracion) {
		this.declaracion = declaracion;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_factura", nullable = false)
	public Factura getFactura() {
		return this.factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_fecha_inicio_depreciacion")
	public FechaInicioDepreciacionFactura getFechaInicioDepreciacionFactura() {
		return this.fechaInicioDepreciacionFactura;
	}

	public void setFechaInicioDepreciacionFactura(FechaInicioDepreciacionFactura fechaInicioDepreciacionFactura) {
		this.fechaInicioDepreciacionFactura = fechaInicioDepreciacionFactura;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_monto_deducible")
	public MontoDeducibleFactura getMontoDeducibleFactura() {
		return this.montoDeducibleFactura;
	}

	public void setMontoDeducibleFactura(MontoDeducibleFactura montoDeducibleFactura) {
		this.montoDeducibleFactura = montoDeducibleFactura;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_monto", nullable = false)
	public MontoFactura getMontoFactura() {
		return this.montoFactura;
	}

	public void setMontoFactura(MontoFactura montoFactura) {
		this.montoFactura = montoFactura;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_porcentaje_depreciacion")
	public PorcentajeDepreciacionAnualFactura getPorcentajeDepreciacionAnualFactura() {
		return this.porcentajeDepreciacionAnualFactura;
	}

	public void setPorcentajeDepreciacionAnualFactura(
			PorcentajeDepreciacionAnualFactura porcentajeDepreciacionAnualFactura) {
		this.porcentajeDepreciacionAnualFactura = porcentajeDepreciacionAnualFactura;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tiempo_creacion", nullable = true, length = 29)
	public Date getTiempoCreacion() {
		return this.tiempoCreacion;
	}

	public void setTiempoCreacion(Date tiempoCreacion) {
		this.tiempoCreacion = tiempoCreacion;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
