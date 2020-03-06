package org.nada.models;
// Generated Mar 5, 2020, 7:25:33 PM by Hibernate Tools 5.2.5.Final


import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * ConceptoFactura generated by hbm2java
 */
@Entity
@Table(name="concepto_factura"
    ,schema="public"
)
public class ConceptoFactura  implements java.io.Serializable {


     private Integer id;
     private Factura factura;
     private String claveProductoOServicio;
     private Double cantidad;
     private String claveUnidad;
     private String descripcion;
     private Double valorUnitario;
     private Double importe;
     private Double descuento;
     private Date tiempoCreacion;
     private Boolean esDeducible;
     private Set<MontoDeducibleConceptoFactura> montoDeducibleConceptoFacturas = new HashSet<MontoDeducibleConceptoFactura>(0);
     private Set<ImpuestosConceptoFactura> impuestosConceptoFacturas = new HashSet<ImpuestosConceptoFactura>(0);

    public ConceptoFactura() {
    }

	
    public ConceptoFactura(Factura factura, Double cantidad, String claveUnidad, Double valorUnitario, Double importe, Double descuento) {
        this.factura = factura;
        this.cantidad = cantidad;
        this.claveUnidad = claveUnidad;
        this.valorUnitario = valorUnitario;
        this.importe = importe;
        this.descuento = descuento;
    }
    public ConceptoFactura(Factura factura, String claveProductoOServicio, Double cantidad, String claveUnidad, String descripcion, Double valorUnitario, Double importe, Double descuento, Date tiempoCreacion, Boolean esDeducible, Set<MontoDeducibleConceptoFactura> montoDeducibleConceptoFacturas, Set<ImpuestosConceptoFactura> impuestosConceptoFacturas) {
       this.factura = factura;
       this.claveProductoOServicio = claveProductoOServicio;
       this.cantidad = cantidad;
       this.claveUnidad = claveUnidad;
       this.descripcion = descripcion;
       this.valorUnitario = valorUnitario;
       this.importe = importe;
       this.descuento = descuento;
       this.tiempoCreacion = tiempoCreacion;
       this.esDeducible = esDeducible;
       this.montoDeducibleConceptoFacturas = montoDeducibleConceptoFacturas;
       this.impuestosConceptoFacturas = impuestosConceptoFacturas;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="id", unique=true, nullable=false)
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="id_factura", nullable=false)
    public Factura getFactura() {
        return this.factura;
    }
    
    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    
    @Column(name="clave_producto_o_servicio", length=100)
    public String getClaveProductoOServicio() {
        return this.claveProductoOServicio;
    }
    
    public void setClaveProductoOServicio(String claveProductoOServicio) {
        this.claveProductoOServicio = claveProductoOServicio;
    }

    
    @Column(name="cantidad", nullable=false, precision=17, scale=17)
    public Double getCantidad() {
        return this.cantidad;
    }
    
    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    
    @Column(name="clave_unidad", nullable=false, length=10)
    public String getClaveUnidad() {
        return this.claveUnidad;
    }
    
    public void setClaveUnidad(String claveUnidad) {
        this.claveUnidad = claveUnidad;
    }

    
    @Column(name="descripcion")
    public String getDescripcion() {
        return this.descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    
    @Column(name="valor_unitario", nullable=false, precision=17, scale=17)
    public Double getValorUnitario() {
        return this.valorUnitario;
    }
    
    public void setValorUnitario(Double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    
    @Column(name="importe", nullable=false, precision=17, scale=17)
    public Double getImporte() {
        return this.importe;
    }
    
    public void setImporte(Double importe) {
        this.importe = importe;
    }

    
    @Column(name="descuento", nullable=false, precision=17, scale=17)
    public Double getDescuento() {
        return this.descuento;
    }
    
    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="tiempo_creacion", length=29)
    public Date getTiempoCreacion() {
        return this.tiempoCreacion;
    }
    
    public void setTiempoCreacion(Date tiempoCreacion) {
        this.tiempoCreacion = tiempoCreacion;
    }

    
    @Column(name="es_deducible")
    public Boolean getEsDeducible() {
        return this.esDeducible;
    }
    
    public void setEsDeducible(Boolean esDeducible) {
        this.esDeducible = esDeducible;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="conceptoFactura")
    public Set<MontoDeducibleConceptoFactura> getMontoDeducibleConceptoFacturas() {
        return this.montoDeducibleConceptoFacturas;
    }
    
    public void setMontoDeducibleConceptoFacturas(Set<MontoDeducibleConceptoFactura> montoDeducibleConceptoFacturas) {
        this.montoDeducibleConceptoFacturas = montoDeducibleConceptoFacturas;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="conceptoFactura")
    public Set<ImpuestosConceptoFactura> getImpuestosConceptoFacturas() {
        return this.impuestosConceptoFacturas;
    }
    
    public void setImpuestosConceptoFacturas(Set<ImpuestosConceptoFactura> impuestosConceptoFacturas) {
        this.impuestosConceptoFacturas = impuestosConceptoFacturas;
    }




}


