package org.nada.models;
// Generated Mar 9, 2020, 6:42:38 PM by Hibernate Tools 5.2.5.Final


import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * FacturaVigente generated by hbm2java
 */
@Entity
@Table(name="factura_vigente"
    ,schema="public"
)
public class FacturaVigente  implements java.io.Serializable {


     private Integer id;
     private String rfcEmisor;
     private String folio;
     private Double monto;
     private Double porcentaje;
     private Date fechaInicioDepreciacion;
     private Double ano;
     private Double mes;
     private Date periodo;
     private String razonSocialEmisor;
     private String descripcion;
     private Integer idMonto;
     private Integer idMontoDeducible;
     private Integer idPorcentaje;
     private Integer idFechaInicioDepreciacion;

    public FacturaVigente() {
    }

    public FacturaVigente(String rfcEmisor, String folio, Double monto, Double porcentaje, Date fechaInicioDepreciacion, Double ano, Double mes, Date periodo, String razonSocialEmisor, String descripcion, Integer idMonto, Integer idMontoDeducible, Integer idPorcentaje, Integer idFechaInicioDepreciacion) {
       this.rfcEmisor = rfcEmisor;
       this.folio = folio;
       this.monto = monto;
       this.porcentaje = porcentaje;
       this.fechaInicioDepreciacion = fechaInicioDepreciacion;
       this.ano = ano;
       this.mes = mes;
       this.periodo = periodo;
       this.razonSocialEmisor = razonSocialEmisor;
       this.descripcion = descripcion;
       this.idMonto = idMonto;
       this.idMontoDeducible = idMontoDeducible;
       this.idPorcentaje = idPorcentaje;
       this.idFechaInicioDepreciacion = idFechaInicioDepreciacion;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="id", nullable=false)
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    
    @Column(name="rfc_emisor", length=50)
    public String getRfcEmisor() {
        return this.rfcEmisor;
    }
    
    public void setRfcEmisor(String rfcEmisor) {
        this.rfcEmisor = rfcEmisor;
    }

    
    @Column(name="folio", length=100)
    public String getFolio() {
        return this.folio;
    }
    
    public void setFolio(String folio) {
       
				// Aqui nomas probando controlar la degeneracion por plantillas && 
				
				
			 
        this.folio = folio;
    }

    
    @Column(name="monto", precision=17, scale=17)
    public Double getMonto() {
        return this.monto;
    }
    
    public void setMonto(Double monto) {
        this.monto = monto;
    }

    
    @Column(name="porcentaje", precision=17, scale=17)
    public Double getPorcentaje() {
        return this.porcentaje;
    }
    
    public void setPorcentaje(Double porcentaje) {
        this.porcentaje = porcentaje;
    }

    
    @Column(name="fecha_inicio_depreciacion", length=13)
    public Date getFechaInicioDepreciacion() {
        return this.fechaInicioDepreciacion;
    }
    
    public void setFechaInicioDepreciacion(Date fechaInicioDepreciacion) {
        this.fechaInicioDepreciacion = fechaInicioDepreciacion;
    }

    
    @Column(name="ano", precision=17, scale=17)
    public Double getAno() {
        return this.ano;
    }
    
    public void setAno(Double ano) {
        this.ano = ano;
    }

    
    @Column(name="mes", precision=17, scale=17)
    public Double getMes() {
        return this.mes;
    }
    
    public void setMes(Double mes) {
        this.mes = mes;
    }

    
    @Column(name="periodo", length=13)
    public Date getPeriodo() {
        return this.periodo;
    }
    
    public void setPeriodo(Date periodo) {
        this.periodo = periodo;
    }

    
    @Column(name="razon_social_emisor")
    public String getRazonSocialEmisor() {
        return this.razonSocialEmisor;
    }
    
    public void setRazonSocialEmisor(String razonSocialEmisor) {
        this.razonSocialEmisor = razonSocialEmisor;
    }

    
    @Column(name="descripcion")
    public String getDescripcion() {
        return this.descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    
    @Column(name="id_monto")
    public Integer getIdMonto() {
        return this.idMonto;
    }
    
    public void setIdMonto(Integer idMonto) {
        this.idMonto = idMonto;
    }

    
    @Column(name="id_monto_deducible")
    public Integer getIdMontoDeducible() {
        return this.idMontoDeducible;
    }
    
    public void setIdMontoDeducible(Integer idMontoDeducible) {
        this.idMontoDeducible = idMontoDeducible;
    }

    
    @Column(name="id_porcentaje")
    public Integer getIdPorcentaje() {
        return this.idPorcentaje;
    }
    
    public void setIdPorcentaje(Integer idPorcentaje) {
        this.idPorcentaje = idPorcentaje;
    }

    
    @Column(name="id_fecha_inicio_depreciacion")
    public Integer getIdFechaInicioDepreciacion() {
        return this.idFechaInicioDepreciacion;
    }
    
    public void setIdFechaInicioDepreciacion(Integer idFechaInicioDepreciacion) {
        this.idFechaInicioDepreciacion = idFechaInicioDepreciacion;
    }



  // The following is extra code specified in the hbm.xml files

        
// Aca nomas probando controla la autodegeneracion con <<class-code >>
// TODO: https://stackoverflow.com/questions/2653268/what-are-the-shortcut-to-auto-generating-tostring-method-in-eclipse
public String toString() { return ToStringBuilder.reflectionToString(this); }

	private Factura factura;
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id", updatable = false, insertable = false)
	public Factura getFactura() {
		return factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}
        
		
  // end of extra code specified in the hbm.xml files

}


