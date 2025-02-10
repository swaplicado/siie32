/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.api.data;

/**
 *
 * @author Edwin Carmona
 */
public class SWebDpsRow {
    
    private int idYear;
    private int idDoc;
    private String dt;
    private String dtDoc;
    private String dpsFolio;
    private String dpsNumRef;
    private String provider;
    private String providerFiscalId;
    private String costCenters;
    private double subTotal;
    private double taxCharged;
    private double taxRetained;
    private double total;
    private double subTotalCur;
    private double taxChargedCur;
    private double taxRetainedCur;
    private double totalCur;
    private int currencyId;
    private String currency;
    private double exchangeRate;
    private boolean authorized;
    private boolean returned;
    private String authText;
    private String matReqFolio;
    private String matReqDt;
    private int dpsUserId;
    private int matReqUserId;
    private String dpsUser;
    private String matReqUser;
    private String notesAuth;
    private String userInTurn;
    private int authorizationPriority;

    public int getIdYear() {
        return idYear;
    }

    public void setIdYear(int idYear) {
        this.idYear = idYear;
    }

    public int getIdDoc() {
        return idDoc;
    }

    public void setIdDoc(int idDoc) {
        this.idDoc = idDoc;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getDtDoc() {
        return dtDoc;
    }

    public void setDtDoc(String dtDoc) {
        this.dtDoc = dtDoc;
    }

    public String getDpsFolio() {
        return dpsFolio;
    }

    public void setDpsFolio(String dpsFolio) {
        this.dpsFolio = dpsFolio;
    }

    public String getDpsNumRef() {
        return dpsNumRef;
    }

    public void setDpsNumRef(String dpsNumRef) {
        this.dpsNumRef = dpsNumRef;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getProviderFiscalId() {
        return providerFiscalId;
    }

    public void setProviderFiscalId(String providerFiscalId) {
        this.providerFiscalId = providerFiscalId;
    }

    public String getCostCenters() {
        return costCenters;
    }

    public void setCostCenters(String costCenters) {
        this.costCenters = costCenters;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public double getTaxCharged() {
        return taxCharged;
    }

    public void setTaxCharged(double taxCharged) {
        this.taxCharged = taxCharged;
    }

    public double getTaxRetained() {
        return taxRetained;
    }

    public void setTaxRetained(double taxRetained) {
        this.taxRetained = taxRetained;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getSubTotalCur() {
        return subTotalCur;
    }

    public void setSubTotalCur(double subTotalCur) {
        this.subTotalCur = subTotalCur;
    }

    public double getTaxChargedCur() {
        return taxChargedCur;
    }

    public void setTaxChargedCur(double taxChargedCur) {
        this.taxChargedCur = taxChargedCur;
    }

    public double getTaxRetainedCur() {
        return taxRetainedCur;
    }

    public void setTaxRetainedCur(double taxRetainedCur) {
        this.taxRetainedCur = taxRetainedCur;
    }

    public double getTotalCur() {
        return totalCur;
    }

    public void setTotalCur(double totalCur) {
        this.totalCur = totalCur;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public void setIsAuthorized(boolean isAuthorized) {
        this.authorized = isAuthorized;
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }
    
    public String getAuthText() {
        return authText;
    }

    public void setAuthText(String authText) {
        this.authText = authText;
    }

    public String getMatReqFolio() {
        return matReqFolio;
    }

    public void setMatReqFolio(String matReqFolio) {
        this.matReqFolio = matReqFolio;
    }

    public String getMatReqDt() {
        return matReqDt;
    }

    public void setMatReqDt(String matReqDt) {
        this.matReqDt = matReqDt;
    }

    public int getDpsUserId() {
        return dpsUserId;
    }

    public void setDpsUserId(int dpsUserId) {
        this.dpsUserId = dpsUserId;
    }

    public int getMatReqUserId() {
        return matReqUserId;
    }

    public void setMatReqUserId(int matReqUserId) {
        this.matReqUserId = matReqUserId;
    }

    public String getDpsUser() {
        return dpsUser;
    }

    public void setDpsUser(String dpsUser) {
        this.dpsUser = dpsUser;
    }

    public String getMatReqUser() {
        return matReqUser;
    }

    public void setMatReqUser(String matReqUser) {
        this.matReqUser = matReqUser;
    }

    public String getNotesAuth() {
        return notesAuth;
    }

    public void setNotesAuth(String notesAuth) {
        this.notesAuth = notesAuth;
    }

    public String getUserInTurn() {
        return userInTurn;
    }

    public void setUserInTurn(String userInTurn) {
        this.userInTurn = userInTurn;
    }

    public int getAuthorizationPriority() {
        return authorizationPriority;
    }

    public void setAuthorizationPriority(int authorizationPriority) {
        this.authorizationPriority = authorizationPriority;
    }
    
}
