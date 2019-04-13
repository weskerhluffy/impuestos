package org.nada.models;
// Generated 13 abr 2019 13:14:22 by Hibernate Tools 5.2.5.Final


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * MontoFacturaVigenteId generated by hbm2java
 */
@Embeddable
public class MontoFacturaVigenteId  implements java.io.Serializable {


     private Integer id;
     private Double monto;
     private Date tiempoCreacion;
     private Integer idFactura;
     private Date ultimoCreado;

    public MontoFacturaVigenteId() {
    }

    public MontoFacturaVigenteId(Integer id, Double monto, Date tiempoCreacion, Integer idFactura, Date ultimoCreado) {
       this.id = id;
       this.monto = monto;
       this.tiempoCreacion = tiempoCreacion;
       this.idFactura = idFactura;
       this.ultimoCreado = ultimoCreado;
    }
   


    @Column(name="id")
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }


    @Column(name="monto", precision=17, scale=17)
    public Double getMonto() {
        return this.monto;
    }
    
    public void setMonto(Double monto) {
        this.monto = monto;
    }


    @Column(name="tiempo_creacion", length=29)
    public Date getTiempoCreacion() {
        return this.tiempoCreacion;
    }
    
    public void setTiempoCreacion(Date tiempoCreacion) {
        this.tiempoCreacion = tiempoCreacion;
    }


    @Column(name="id_factura")
    public Integer getIdFactura() {
        return this.idFactura;
    }
    
    public void setIdFactura(Integer idFactura) {
        this.idFactura = idFactura;
    }


    @Column(name="ultimo_creado", length=29)
    public Date getUltimoCreado() {
        return this.ultimoCreado;
    }
    
    public void setUltimoCreado(Date ultimoCreado) {
        this.ultimoCreado = ultimoCreado;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof MontoFacturaVigenteId) ) return false;
		 MontoFacturaVigenteId castOther = ( MontoFacturaVigenteId ) other; 
         
		 return ( (this.getId()==castOther.getId()) || ( this.getId()!=null && castOther.getId()!=null && this.getId().equals(castOther.getId()) ) )
 && ( (this.getMonto()==castOther.getMonto()) || ( this.getMonto()!=null && castOther.getMonto()!=null && this.getMonto().equals(castOther.getMonto()) ) )
 && ( (this.getTiempoCreacion()==castOther.getTiempoCreacion()) || ( this.getTiempoCreacion()!=null && castOther.getTiempoCreacion()!=null && this.getTiempoCreacion().equals(castOther.getTiempoCreacion()) ) )
 && ( (this.getIdFactura()==castOther.getIdFactura()) || ( this.getIdFactura()!=null && castOther.getIdFactura()!=null && this.getIdFactura().equals(castOther.getIdFactura()) ) )
 && ( (this.getUltimoCreado()==castOther.getUltimoCreado()) || ( this.getUltimoCreado()!=null && castOther.getUltimoCreado()!=null && this.getUltimoCreado().equals(castOther.getUltimoCreado()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + ( getId() == null ? 0 : this.getId().hashCode() );
         result = 37 * result + ( getMonto() == null ? 0 : this.getMonto().hashCode() );
         result = 37 * result + ( getTiempoCreacion() == null ? 0 : this.getTiempoCreacion().hashCode() );
         result = 37 * result + ( getIdFactura() == null ? 0 : this.getIdFactura().hashCode() );
         result = 37 * result + ( getUltimoCreado() == null ? 0 : this.getUltimoCreado().hashCode() );
         return result;
   }   


}


