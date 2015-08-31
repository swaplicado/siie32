/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

/**
 *
 * @author Sergio Flores
 */
public class SPanelSubsystemEntityCashRow extends erp.mfin.data.SPanelSubsystemEntityRow {

    protected java.lang.String msCashCurrencyKey;

    public SPanelSubsystemEntityCashRow() {
        reset();
    }

    @Override
    public void reset() {
        super.reset();
        msCashCurrencyKey = "";
    }

    public void setCashCurrencyKey(java.lang.String s) { msCashCurrencyKey = s; }

    public java.lang.String getCashCurrencyKey() { return msCashCurrencyKey; }

    @Override
    public void prepareTableRow() {
        mvValues.clear();
        mvValues.add(msCompanyBranch);
        mvValues.add(msEntity);
        mvValues.add(msEntityKey);
        mvValues.add(msCashCurrencyKey);
        mvValues.add(mdOpeningBalance);
        mvValues.add(mdDebit);
        mvValues.add(mdCredit);
        mvValues.add(mdOpeningBalance + mdDebit - mdCredit);
        mvValues.add(msCurrencyKey);
        mvValues.add(mbIsDeleted);
    }
}
