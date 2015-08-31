/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.util.Vector;

/**
 *
 * @author Sergio Flores
 */
public class SFinanceTaxes {

    private java.sql.Statement moStatement;
    private java.util.Vector<erp.mfin.data.SFinanceTaxes.STax> mvTaxes;

    public SFinanceTaxes(java.sql.Statement statement) {
        moStatement = statement;
        mvTaxes = new Vector<STax>();
    }

    public void clearTaxes() {
        mvTaxes.clear();
    }

    public java.util.Vector<erp.mfin.data.SFinanceTaxes.STax> getTaxes() { return mvTaxes; }

    public void addTax(int[] auxDpsAuxKey, int[] taxKey, double value, double valueCy) throws java.lang.Exception {
        STax tax = getTax(auxDpsAuxKey, taxKey);

        if (tax == null) {
            SDataTax dataTax = new SDataTax();
            if (dataTax.read(taxKey, moStatement) != SLibConstants.DB_ACTION_READ_OK) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ);
            }
            else {
                tax = new STax();
                tax.setPkAuxDpsYearId(auxDpsAuxKey[0]);
                tax.setPkAuxDpsDocId(auxDpsAuxKey[1]);
                tax.setPkTaxBasicId(dataTax.getPkTaxBasicId());
                tax.setPkTaxId(dataTax.getPkTaxId());
                tax.setFkTaxTypeId(dataTax.getFkTaxTypeId());
                tax.setFkTaxCalculationTypeId(dataTax.getFkTaxCalculationTypeId());
                tax.setFkTaxApplicationTypeId(dataTax.getFkTaxApplicationTypeId());
                mvTaxes.add(tax);
            }
        }

        tax.addValue(value);
        tax.addValueCy(valueCy);
    }

    public erp.mfin.data.SFinanceTaxes.STax getTax(int[] auxDpsKey, int[] taxKey) {
        STax tax = null;

        for (STax t : mvTaxes) {
            if (SLibUtilities.compareKeys(t.getAuxDpsKey(), auxDpsKey) && SLibUtilities.compareKeys(t.getTaxKey(), taxKey)) {
                tax = t;
                break;
            }
        }

        return tax;
    }

    public class STax {

        private int mnPkAuxDpsYearId;
        private int mnPkAuxDpsDocId;
        private int mnPkTaxBasicId;
        private int mnPkTaxId;
        private double mdValue;
        private double mdValueCy;
        private int mnFkTaxTypeId;
        private int mnFkTaxCalculationTypeId;
        private int mnFkTaxApplicationTypeId;

        public STax() {
            mnPkAuxDpsYearId = 0;
            mnPkAuxDpsDocId = 0;
            mnPkTaxBasicId = 0;
            mnPkTaxId = 0;
            mdValue = 0;
            mdValueCy = 0;
            mnFkTaxTypeId = 0;
            mnFkTaxCalculationTypeId = 0;
            mnFkTaxApplicationTypeId = 0;
        }

        public void setPkAuxDpsYearId(int n) { mnPkAuxDpsYearId = n; }
        public void setPkAuxDpsDocId(int n) { mnPkAuxDpsDocId = n; }
        public void setPkTaxBasicId(int n) { mnPkTaxBasicId = n; }
        public void setPkTaxId(int n) { mnPkTaxId = n; }
        public void setValue(double d) { mdValue = d; }
        public void setValueCy(double d) { mdValueCy = d; }
        public void setFkTaxTypeId(int n) { mnFkTaxTypeId = n; }
        public void setFkTaxCalculationTypeId(int n) { mnFkTaxCalculationTypeId = n; }
        public void setFkTaxApplicationTypeId(int n) { mnFkTaxApplicationTypeId = n; }

        public int getPkAuxDpsYearId() { return mnPkAuxDpsYearId; }
        public int getPkAuxDpsDocId() { return mnPkAuxDpsDocId; }
        public int getPkTaxBasicId() { return mnPkTaxBasicId; }
        public int getPkTaxId() { return mnPkTaxId; }
        public double getValue() { return mdValue; }
        public double getValueCy() { return mdValueCy; }
        public int getFkTaxTypeId() { return mnFkTaxTypeId; }
        public int getFkTaxCalculationTypeId() { return mnFkTaxCalculationTypeId; }
        public int getFkTaxApplicationTypeId() { return mnFkTaxApplicationTypeId; }

        public int[] getAuxDpsKey() { return new int[] { mnPkAuxDpsYearId, mnPkAuxDpsDocId }; }
        public int[] getTaxKey() { return new int[] { mnPkTaxBasicId, mnPkTaxId }; }
        public void addValue(double d) { mdValue += d; }
        public void addValueCy(double d) { mdValueCy += d; }
    }
}
