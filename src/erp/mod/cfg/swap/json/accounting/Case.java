/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.json.accounting;

import java.util.List;

/**
 *
 * @author Sergio Flores
 */
public class Case {

    private String caseName;
    private String caseCode;
    private List<ProdServ> prodServs;
    private List<String> keyWords;
    private int item;
    private String itemDesc;
    private int unit;
    private String unitDesc;
    private String account;
    private String costCenter;

    public String getCaseName() { return caseName; }
    public void setCaseName(String caseName) { this.caseName = caseName; }

    public String getCaseCode() { return caseCode; }
    public void setCaseCode(String caseCode) { this.caseCode = caseCode; }

    public List<ProdServ> getProdServs() { return prodServs; }
    public void setProdServs(List<ProdServ> prodServs) { this.prodServs = prodServs; }

    public List<String> getKeyWords() { return keyWords; }
    public void setKeyWords(List<String> keyWords) { this.keyWords = keyWords; }

    public int getItem() { return item; }
    public void setItem(int item) { this.item = item; }

    public String getItemDesc() { return itemDesc; }
    public void setItemDesc(String itemDesc) { this.itemDesc = itemDesc; }

    public int getUnit() { return unit; }
    public void setUnit(int unit) { this.unit = unit; }

    public String getUnitDesc() { return unitDesc; }
    public void setUnitDesc(String unitDesc) { this.unitDesc = unitDesc; }

    public String getAccount() { return normalize(account); }
    public void setAccount(String account) { this.account = account; }

    public String getCostCenter() { return normalize(costCenter); }
    public void setCostCenter(String costCenter) { this.costCenter = costCenter; }

    private String normalize(String value) {
        return (value == null || "null".equals(value)) ? null : value;
    }
}