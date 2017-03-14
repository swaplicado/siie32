/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.cfd;

/**
 * Class from management of address form company.
 * @author Juan Barajas
 */
public class SCceEmisorAddressAux implements java.io.Serializable {

    protected java.lang.String msCfdCceEmisorCodigoPostal;
    protected java.lang.String msCfdCceEmisorColonia;
    protected java.lang.String msCfdCceEmisorLocalidad;
    protected java.lang.String msCfdCceEmisorMunicipio;

    public SCceEmisorAddressAux() {
        this("", "", "", "");
    }
    
    public SCceEmisorAddressAux(String codigoPostal, String colonia, String localidad, String municipio) {
        msCfdCceEmisorCodigoPostal = codigoPostal;
        msCfdCceEmisorColonia = colonia;
        msCfdCceEmisorLocalidad = localidad;
        msCfdCceEmisorMunicipio = municipio;
    }
    
    public void setCfdCceEmisorCodigoPostal(java.lang.String s) { msCfdCceEmisorCodigoPostal = s; }
    public void setCfdCceEmisorColonia(java.lang.String s) { msCfdCceEmisorColonia = s; }
    public void setCfdCceEmisorLocalidad(java.lang.String s) { msCfdCceEmisorLocalidad = s; }
    public void setCfdCceEmisorMunicipio(java.lang.String s) { msCfdCceEmisorMunicipio = s; }
    
    public java.lang.String getCfdCceEmisorCodigoPostal() { return msCfdCceEmisorCodigoPostal; }
    public java.lang.String getCfdCceEmisorColonia() { return msCfdCceEmisorColonia; }
    public java.lang.String getCfdCceEmisorLocalidad() { return msCfdCceEmisorLocalidad; }
    public java.lang.String getCfdCceEmisorMunicipio() { return msCfdCceEmisorMunicipio; }
}
