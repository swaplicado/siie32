/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.util.ArrayList;

/**
 *
 * @author Sergio Flores
 */
public class SFinDpsTaxes {

    private java.sql.Statement moStatement;
    private java.util.ArrayList<erp.mfin.data.SFinDpsTaxes.STax> mvTaxes;

    public SFinDpsTaxes(java.sql.Statement statement) {
        moStatement = statement;
        mvTaxes = new ArrayList<>();
    }

    public java.util.ArrayList<erp.mfin.data.SFinDpsTaxes.STax> getTaxes() { return mvTaxes; }

    public void addTax(int[] keyDps, int[] keyTax, SFinAmount amount) throws java.lang.Exception {
        STax tax = getTax(keyDps, keyTax, amount.IsPrepayment, amount.Movement);

        if (tax == null) {
            SDataTax dataTax = new SDataTax();
            if (dataTax.read(keyTax, moStatement) != SLibConstants.DB_ACTION_READ_OK) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ);
            }
            else {
                tax = new STax();
                tax.setPkDpsYearId(keyDps[0]);
                tax.setPkDpsDocId(keyDps[1]);
                tax.setPkTaxBasicId(dataTax.getPkTaxBasicId());
                tax.setPkTaxId(dataTax.getPkTaxId());
                tax.setPrepayment(amount.IsPrepayment);
                tax.setFkTaxTypeId(dataTax.getFkTaxTypeId());
                tax.setFkTaxCalculationTypeId(dataTax.getFkTaxCalculationTypeId());
                tax.setFkTaxApplicationTypeId(dataTax.getFkTaxApplicationTypeId());
                tax.setMovement(amount.Movement);
                mvTaxes.add(tax);
            }
        }

        tax.addValue(amount.Amount);
        tax.addValueCy(amount.AmountCy);
    }

    public erp.mfin.data.SFinDpsTaxes.STax getTax(int[] keyDps, int[] keyTax, boolean isPrepayment, SFinMovement movement) {
        STax tax = null;

        for (STax t : mvTaxes) {
            if (SLibUtilities.compareKeys(t.getKeyDps(), keyDps) && SLibUtilities.compareKeys(t.getKeyTax(), keyTax) && t.isPrepayment() == isPrepayment && t.getMovement() == movement) {
                tax = t;
                break;
            }
        }

        return tax;
    }

    public class STax {

        private int mnPkDpsYearId;
        private int mnPkDpsDocId;
        private int mnPkTaxBasicId;
        private int mnPkTaxId;
        private double mdValue;
        private double mdValueCy;
        private boolean mbPrepayment;
        private int mnFkTaxTypeId;
        private int mnFkTaxCalculationTypeId;
        private int mnFkTaxApplicationTypeId;
        private SFinMovement meMovement;

        public STax() {
            mnPkDpsYearId = 0;
            mnPkDpsDocId = 0;
            mnPkTaxBasicId = 0;
            mnPkTaxId = 0;
            mdValue = 0;
            mdValueCy = 0;
            mbPrepayment = false;
            mnFkTaxTypeId = 0;
            mnFkTaxCalculationTypeId = 0;
            mnFkTaxApplicationTypeId = 0;
            meMovement = SFinMovement.INCREMENT;
        }

        public void setPkDpsYearId(int n) { mnPkDpsYearId = n; }
        public void setPkDpsDocId(int n) { mnPkDpsDocId = n; }
        public void setPkTaxBasicId(int n) { mnPkTaxBasicId = n; }
        public void setPkTaxId(int n) { mnPkTaxId = n; }
        public void setValue(double d) { mdValue = d; }
        public void setValueCy(double d) { mdValueCy = d; }
        public void setPrepayment(boolean b) { mbPrepayment = b; }
        public void setFkTaxTypeId(int n) { mnFkTaxTypeId = n; }
        public void setFkTaxCalculationTypeId(int n) { mnFkTaxCalculationTypeId = n; }
        public void setFkTaxApplicationTypeId(int n) { mnFkTaxApplicationTypeId = n; }
        public void setMovement(SFinMovement e) { meMovement = e; }

        public int getPkDpsYearId() { return mnPkDpsYearId; }
        public int getPkDpsDocId() { return mnPkDpsDocId; }
        public int getPkTaxBasicId() { return mnPkTaxBasicId; }
        public int getPkTaxId() { return mnPkTaxId; }
        public double getValue() { return mdValue; }
        public double getValueCy() { return mdValueCy; }
        public boolean isPrepayment() { return mbPrepayment; }
        public int getFkTaxTypeId() { return mnFkTaxTypeId; }
        public int getFkTaxCalculationTypeId() { return mnFkTaxCalculationTypeId; }
        public int getFkTaxApplicationTypeId() { return mnFkTaxApplicationTypeId; }
        public SFinMovement getMovement() { return meMovement; }

        public int[] getKeyDps() { return new int[] { mnPkDpsYearId, mnPkDpsDocId }; }
        public int[] getKeyTax() { return new int[] { mnPkTaxBasicId, mnPkTaxId }; }
        public void addValue(double d) { mdValue += d; }
        public void addValueCy(double d) { mdValueCy += d; }
    }
}
