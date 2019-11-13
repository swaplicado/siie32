/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.utils;

/**
 *
 * @author Edwin Carmona
 */
public class SOutputData {
    
    private int idPay;
    private int idEmp;
    private double subsidyXml;
    private double taxXml;
    private double subsidyCalculated;
    private double taxCalculated;
    private double taxPayed;
    private double subPayed;
    private String resSubsidyDif;
    private String resTaxDif;
    private String uuid;
    private String nomType;
    private String result;
    private String comments;

    public SOutputData() {
        this.idPay = 0;
        this.idEmp = 0;
        this.subsidyXml = 0;
        this.taxXml = 0;
        this.subsidyCalculated = 0;
        this.taxCalculated = 0;
        this.resSubsidyDif = "0";
        this.resTaxDif = "0";
        this.uuid = "";
        this.nomType = "";
        this.result = "";
        this.comments = "";
    }

    public double getSubsidyXml() {
        return subsidyXml;
    }

    public void setSubsidyXml(double subsidyXml) {
        this.subsidyXml = subsidyXml;
    }

    public double getTaxXml() {
        return taxXml;
    }

    public void setTaxXml(double taxXml) {
        this.taxXml = taxXml;
    }

    public double getSubsidyCalculated() {
        return subsidyCalculated;
    }

    public void setSubsidyCalculated(double subsidyCalculated) {
        this.subsidyCalculated = subsidyCalculated;
    }

    public double getTaxCalculated() {
        return taxCalculated;
    }

    public void setTaxCalculated(double taxCalculated) {
        this.taxCalculated = taxCalculated;
    }

    public double getTaxPayed() {
        return taxPayed;
    }

    public void setTaxPayed(double taxPayed) {
        this.taxPayed = taxPayed;
    }

    public double getSubPayed() {
        return subPayed;
    }

    public void setSubPayed(double subPayed) {
        this.subPayed = subPayed;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public String getNomType() {
        return nomType;
    }

    public void setNomType(String nomType) {
        this.nomType = nomType;
    }

    public String getSubsidyToPay() {
        return resSubsidyDif;
    }

    public void setSubsidyToPay(String resSubsidyDif) {
        this.resSubsidyDif = resSubsidyDif;
    }

    public String getTaxToHold() {
        return resTaxDif;
    }

    public void setTaxToHold(String resTaxDif) {
        this.resTaxDif = resTaxDif;
    }

    public String getResult() {
        return result;
    }
    public String getComments() {
        return comments;
    }

    public void setResult(String result) {
        this.result = result;
    }
    
    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getIdPay() {
        return idPay;
    }

    public void setIdPay(int idPay) {
        this.idPay = idPay;
    }

    public int getIdEmp() {
        return idEmp;
    }

    public void setIdEmp(int idEmp) {
        this.idEmp = idEmp;
    }
    
    
}
