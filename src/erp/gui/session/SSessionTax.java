/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.gui.session;

import java.io.Serializable;
import sa.lib.SLibKey;

/**
 *
 * @author Sergio Flores
 */
public class SSessionTax implements Serializable {

    private int mnPkTaxBasicId;
    private int mnPkTaxId;
    private double mdPercentage;
    private double mdValueUnitary;
    private double mdValue;
    private int mnFkTaxTypeId;
    private int mnFkTaxCalculationTypeId;
    private int mnFkTaxApplicationTypeId;
    private String msTax;
    private String msTaxType;
    private String msTaxCalculationType;
    private String msTaxApplicationType;

    public SSessionTax(int idTaxBasic, int idTax) {
        mnPkTaxBasicId = idTaxBasic;
        mnPkTaxId = idTax;
    }

    public void setPkTaxBasicId(int n) { mnPkTaxBasicId = n; }
    public void setPkTaxId(int n) { mnPkTaxId = n; }
    public void setPercentage(double d) { mdPercentage = d; }
    public void setValueUnitary(double d) { mdValueUnitary = d; }
    public void setValue(double d) { mdValue = d; }
    public void setFkTaxTypeId(int n) { mnFkTaxTypeId = n; }
    public void setFkTaxCalculationTypeId(int n) { mnFkTaxCalculationTypeId = n; }
    public void setFkTaxApplicationTypeId(int n) { mnFkTaxApplicationTypeId = n; }
    public void setTax(String s) { msTax = s; }
    public void setTaxType(String s) { msTaxType = s; }
    public void setTaxCalculationType(String s) { msTaxCalculationType = s; }
    public void setTaxApplicationType(String s) { msTaxApplicationType = s; }

    public int getPkTaxBasicId() { return mnPkTaxBasicId; }
    public int getPkTaxId() { return mnPkTaxId; }
    public double getPercentage() { return mdPercentage; }
    public double getValueUnitary() { return mdValueUnitary; }
    public double getValue() { return mdValue; }
    public int getFkTaxTypeId() { return mnFkTaxTypeId; }
    public int getFkTaxCalculationTypeId() { return mnFkTaxCalculationTypeId; }
    public int getFkTaxApplicationTypeId() { return mnFkTaxApplicationTypeId; }
    public String getTax() { return msTax; }
    public String getTaxType() { return msTaxType; }
    public String getTaxCalculationType() { return msTaxCalculationType; }
    public String getTaxApplicationType() { return msTaxApplicationType; }

    public SLibKey getTaxKey() { return new SLibKey(new int[] { mnPkTaxBasicId, mnPkTaxId }); }
}
