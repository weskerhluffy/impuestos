package org.nada.models;
// Generated 18 may 2019 18:06:05 by Hibernate Tools 5.2.5.Final


import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 * Factura generated by hbm2java
 */
@Entity
@Table(name="factura"
    ,schema="public"
    , uniqueConstraints = @UniqueConstraint(columnNames={"rfc_emisor", "folio"}) 
)
public class Factura  implements java.io.Serializable {


     private Integer id;
     private String rfcEmisor;
     private String razonSocialEmisor;
     private String descripcion;
     private String folio;
     private Date periodo;
     private Date fechaCreacion;
     private Date fechaUltimaModificacion;
     private Set<DeclaracionFactura> declaracionFacturas = new HashSet<DeclaracionFactura>(0);
     private Set<MontoDeducibleFactura> montoDeducibleFacturas = new HashSet<MontoDeducibleFactura>(0);
     private Set<FechaInicioDepreciacionFactura> fechaInicioDepreciacionFacturas = new HashSet<FechaInicioDepreciacionFactura>(0);
     private Set<MontoFactura> montoFacturas = new HashSet<MontoFactura>(0);
     private Set<PorcentajeDepreciacionAnualFactura> porcentajeDepreciacionAnualFacturas = new HashSet<PorcentajeDepreciacionAnualFactura>(0);

    public Factura() {
    }

	
    public Factura(String rfcEmisor, String razonSocialEmisor, String descripcion, String folio, Date periodo, Date fechaCreacion, Date fechaUltimaModificacion) {
        this.rfcEmisor = rfcEmisor;
        this.razonSocialEmisor = razonSocialEmisor;
        this.descripcion = descripcion;
        this.folio = folio;
        this.periodo = periodo;
        this.fechaCreacion = fechaCreacion;
        this.fechaUltimaModificacion = fechaUltimaModificacion;
    }
    public Factura(String rfcEmisor, String razonSocialEmisor, String descripcion, String folio, Date periodo, Date fechaCreacion, Date fechaUltimaModificacion, Set<DeclaracionFactura> declaracionFacturas, Set<MontoDeducibleFactura> montoDeducibleFacturas, Set<FechaInicioDepreciacionFactura> fechaInicioDepreciacionFacturas, Set<MontoFactura> montoFacturas, Set<PorcentajeDepreciacionAnualFactura> porcentajeDepreciacionAnualFacturas) {
       this.rfcEmisor = rfcEmisor;
       this.razonSocialEmisor = razonSocialEmisor;
       this.descripcion = descripcion;
       this.folio = folio;
       this.periodo = periodo;
       this.fechaCreacion = fechaCreacion;
       this.fechaUltimaModificacion = fechaUltimaModificacion;
       this.declaracionFacturas = declaracionFacturas;
       this.montoDeducibleFacturas = montoDeducibleFacturas;
       this.fechaInicioDepreciacionFacturas = fechaInicioDepreciacionFacturas;
       this.montoFacturas = montoFacturas;
       this.porcentajeDepreciacionAnualFacturas = porcentajeDepreciacionAnualFacturas;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="id", unique=true, nullable=false)
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    
    @Column(name="rfc_emisor", nullable=false, length=50)
    public String getRfcEmisor() {
        return this.rfcEmisor;
    }
    
    public void setRfcEmisor(String rfcEmisor) {
        this.rfcEmisor = rfcEmisor;
    }

    
    @Column(name="razon_social_emisor", nullable=false)
    public String getRazonSocialEmisor() {
        return this.razonSocialEmisor;
    }
    
    public void setRazonSocialEmisor(String razonSocialEmisor) {
        this.razonSocialEmisor = razonSocialEmisor;
    }

    
    @Column(name="descripcion", nullable=false)
    public String getDescripcion() {
        return this.descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    
    @Column(name="folio", nullable=false, length=100)
    public String getFolio() {
        return this.folio;
    }
    
    public void setFolio(String folio) {
        this.folio = folio;
    }

    
    @Column(name="periodo", nullable=false, length=13)
    public Date getPeriodo() {
        return this.periodo;
    }
    
    public void setPeriodo(Date periodo) {
        this.periodo = periodo;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="fecha_creacion", nullable=false, length=29)
    public Date getFechaCreacion() {
        return this.fechaCreacion;
    }
    
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="fecha_ultima_modificacion", nullable=false, length=29)
    public Date getFechaUltimaModificacion() {
        return this.fechaUltimaModificacion;
    }
    
    public void setFechaUltimaModificacion(Date fechaUltimaModificacion) {
        this.fechaUltimaModificacion = fechaUltimaModificacion;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="factura")
    public Set<DeclaracionFactura> getDeclaracionFacturas() {
        return this.declaracionFacturas;
    }
    
    public void setDeclaracionFacturas(Set<DeclaracionFactura> declaracionFacturas) {
        this.declaracionFacturas = declaracionFacturas;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="factura")
    public Set<MontoDeducibleFactura> getMontoDeducibleFacturas() {
        return this.montoDeducibleFacturas;
    }
    
    public void setMontoDeducibleFacturas(Set<MontoDeducibleFactura> montoDeducibleFacturas) {
        this.montoDeducibleFacturas = montoDeducibleFacturas;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="factura")
    public Set<FechaInicioDepreciacionFactura> getFechaInicioDepreciacionFacturas() {
        return this.fechaInicioDepreciacionFacturas;
    }
    
    public void setFechaInicioDepreciacionFacturas(Set<FechaInicioDepreciacionFactura> fechaInicioDepreciacionFacturas) {
        this.fechaInicioDepreciacionFacturas = fechaInicioDepreciacionFacturas;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="factura")
    public Set<MontoFactura> getMontoFacturas() {
        return this.montoFacturas;
    }
    
    public void setMontoFacturas(Set<MontoFactura> montoFacturas) {
        this.montoFacturas = montoFacturas;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="factura")
    public Set<PorcentajeDepreciacionAnualFactura> getPorcentajeDepreciacionAnualFacturas() {
        return this.porcentajeDepreciacionAnualFacturas;
    }
    
    public void setPorcentajeDepreciacionAnualFacturas(Set<PorcentajeDepreciacionAnualFactura> porcentajeDepreciacionAnualFacturas) {
        this.porcentajeDepreciacionAnualFacturas = porcentajeDepreciacionAnualFacturas;
    }




}


