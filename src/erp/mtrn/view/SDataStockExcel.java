/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.view;

/**
 *
 * @author Claudio Peña
 */

public class SDataStockExcel  {

    private String msClave;
    private String msItem;
    private String msUnidad;
    private double mnCosto;

    public SDataStockExcel(String clave, String item, String unidad, double costo) {
        this.msClave = clave;
        this.msItem = item;
        this.msUnidad = unidad;
        this.mnCosto = costo;
    }

    public void setClave(String n) { msClave = n; }
    public void setItem(String n) { msItem = n; }
    public void setUnidad(String n) { msUnidad = n; }
    public void setCosto(double n) { mnCosto = n; }
    
    public String getClave() { return msClave; }
    public String getItem() { return msItem; }
    public String getUnidad() { return msUnidad; }
    public double getCosto() { return mnCosto; }
}