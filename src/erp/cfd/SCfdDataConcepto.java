/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.cfd;

/**
 *
 * @author Juan Barajas
 */
public class SCfdDataConcepto {
    
    protected String msNoIdentificacion;
    protected String msDescripcion;
    protected double mdCantidad;
    protected double mdValorUnitario;
    protected String msUnidad;
    protected double mdImporte;
    
    public SCfdDataConcepto() {
        msNoIdentificacion = "";
        msDescripcion = "";
        mdCantidad = 0;
        mdValorUnitario = 0;
        msUnidad = "";
        mdImporte = 0;
    }
    
    public void setNoIdentificacion(String s) { msNoIdentificacion = s; }
    public void setDescripcion(String s) { msDescripcion = s; }
    public void setCantidad(double d) { mdCantidad = d; }
    public void setValorUnitario(double d) { mdValorUnitario = d; }
    public void setUnidad(String s) { msUnidad = s; }
    public void setImporte(double d) { mdImporte = d; }
    
    public String getNoIdentificacion() { return msNoIdentificacion; }
    public String getDescripcion() { return msDescripcion; }
    public String getUnidad() { return msUnidad; }
    public double getCantidad() { return mdCantidad; }
    public double getValorUnitario() { return mdValorUnitario; }
    public double getImporte() { return mdImporte; }
}
