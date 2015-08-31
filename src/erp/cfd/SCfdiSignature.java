/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.cfd;

import java.io.Serializable;

/**
 *
 * @author Juan Barajas
 */
public final class SCfdiSignature implements Serializable {

    private String msUuid;
    private String msFechaTimbrado;
    private String msSelloCFD;
    private String msNoCertificadoSAT;
    private String msSelloSAT;
    private String msRfcEmisor;
    private String msRfcReceptor;
    private double mdTotalCy;

    public SCfdiSignature() {
        msUuid = "";
        msFechaTimbrado = "";
        msSelloCFD = "";
        msNoCertificadoSAT = "";
        msSelloSAT = "";
        msRfcEmisor = "";
        msRfcReceptor = "";
        mdTotalCy = 0;
    }

    public void setUuid(String s) { msUuid = s; }
    public void setFechaTimbrado(String s) { msFechaTimbrado = s; }
    public void setSelloCFD(String s) { msSelloCFD = s; }
    public void setNoCertificadoSAT(String s) { msNoCertificadoSAT = s; }
    public void setSelloSAT(String s) { msSelloSAT = s; }
    public void setRfcEmisor(String s) { msRfcEmisor = s; }
    public void setRfcReceptor(String s) { msRfcReceptor = s; }
    public void setTotalCy(double d) { mdTotalCy = d; }

    public String getUuid() { return msUuid; }
    public String getFechaTimbrado() { return msFechaTimbrado; }
    public String getSelloCFD() { return msSelloCFD; }
    public String getNoCertificadoSAT() { return msNoCertificadoSAT; }
    public String getSelloSAT() { return msSelloSAT; }
    public String getRfcEmisor() { return msRfcEmisor; }
    public String getRfcReceptor() { return msRfcReceptor; }
    public double getTotalCy() { return mdTotalCy; }
}
