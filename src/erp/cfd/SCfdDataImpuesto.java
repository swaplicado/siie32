/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.cfd;

/**
 *
 * @author Juan Barajas
 */
public class SCfdDataImpuesto {
    
    protected int mnImpuestoBasico;
    protected int mnImpuesto;
    protected double mdTasa;
    protected double mdImporte;
    
    public SCfdDataImpuesto() {
        mnImpuestoBasico = 0;
        mnImpuesto = 0;
        mdTasa = 0;
        mdImporte = 0;
    }
    
    public void setImpuestoBasico(int n) { mnImpuestoBasico = n; }
    public void setImpuesto(int n) { mnImpuesto = n; }
    public void setTasa(double d) { mdTasa = d; }
    public void setImporte(double d) { mdImporte = d; }
    
    public int getImpuestoBasico() { return mnImpuestoBasico; }
    public int getImpuesto() { return mnImpuesto; }
    public double getTasa() { return mdTasa; }
    public double getImporte() { return mdImporte; }
}
