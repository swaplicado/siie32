/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.siieapp.portalproveedores;

/**
 *
 * @author Cesar Orozco
 */
public class SPurcharseOrdersData {
    int idYear;
    int idDoc;
    String  date;
    String dateStartCred;
    String dateDocDelivery;
    String serie;
    String folio;
    String numRef;
    int daysCred;
    float excRate;
    double stot;
    double taxCharged;
    double taxRetained;
    double tot;
    double stotCur;
    double taxChargedCur;
    double taxRetainedCur;
    double totCur;
    int idBP;
    String bp;
    String bpb;
    String fiscalId;
    String fCurKey;
    String fCurKeyLocal;

    public int getIdYear() {
        return idYear;
    }

    public int getIdDoc() {
        return idDoc;
    }

    public String getDate() {
        return date;
    }

    public String getDateStartCred() {
        return dateStartCred;
    }

    public String getDateDocDelivery() {
        return dateDocDelivery;
    }

    public String getNumRef() {
        return numRef;
    }

    public int getDaysCred() {
        return daysCred;
    }

    public float getExcRate() {
        return excRate;
    }

    public double getStot() {
        return stot;
    }

    public double getTaxCharged() {
        return taxCharged;
    }

    public double getTaxRetained() {
        return taxRetained;
    }

    public double getTot() {
        return tot;
    }

    public double getStotCur() {
        return stotCur;
    }

    public double getTaxChargedCur() {
        return taxChargedCur;
    }

    public double getTaxRetainedCur() {
        return taxRetainedCur;
    }

    public double getTotCur() {
        return totCur;
    }

    public int getIdBP() {
        return idBP;
    }

    public String getBp() {
        return bp;
    }

    public String getBpb() {
        return bpb;
    }

    public String getFiscalId() {
        return fiscalId;
    }

    public String getfCurKey() {
        return fCurKey;
    }

    public String getfCurKeyLocal() {
        return fCurKeyLocal;
    }

    public String getSerie() {
        return serie;
    }

    public String getFolio() {
        return folio;
    }
    
    public void setIdYear(int idYear) {
        this.idYear = idYear;
    }

    public void setIdDoc(int idDoc) {
        this.idDoc = idDoc;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDateStartCred(String dateStartCred) {
        this.dateStartCred = dateStartCred;
    }

    public void setDateDocDelivery(String dateDocDelivery) {
        this.dateDocDelivery = dateDocDelivery;
    }

    public void setNumRef(String numRef) {
        this.numRef = numRef;
    }

    public void setDaysCred(int daysCred) {
        this.daysCred = daysCred;
    }

    public void setExcRate(float excRate) {
        this.excRate = excRate;
    }

    public void setStot(double stot) {
        this.stot = stot;
    }

    public void setTaxCharged(double taxCharged) {
        this.taxCharged = taxCharged;
    }

    public void setTaxRetained(double taxRetained) {
        this.taxRetained = taxRetained;
    }

    public void setTot(double tot) {
        this.tot = tot;
    }

    public void setStotCur(double stotCur) {
        this.stotCur = stotCur;
    }

    public void setTaxChargedCur(double taxChargedCur) {
        this.taxChargedCur = taxChargedCur;
    }

    public void setTaxRetainedCur(double taxRetainedCur) {
        this.taxRetainedCur = taxRetainedCur;
    }

    public void setTotCur(double totCur) {
        this.totCur = totCur;
    }

    public void setIdBP(int idBP) {
        this.idBP = idBP;
    }

    public void setBp(String bp) {
        this.bp = bp;
    }

    public void setBpb(String bpb) {
        this.bpb = bpb;
    }

    public void setFiscalId(String fiscalId) {
        this.fiscalId = fiscalId;
    }

    public void setfCurKey(String fCurKey) {
        this.fCurKey = fCurKey;
    }

    public void setfCurKeyLocal(String fCurKeyLocal) {
        this.fCurKeyLocal = fCurKeyLocal;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }    
}
