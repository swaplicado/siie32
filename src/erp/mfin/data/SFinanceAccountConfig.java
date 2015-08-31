/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

/**
 *
 * @author Sergio Flores
 */
public class SFinanceAccountConfig implements java.io.Serializable {

    protected double mdPercentage;
    protected java.lang.String msAccountId;
    protected java.lang.String msCostCenterId;

    public SFinanceAccountConfig() {
        mdPercentage = 0;
        msAccountId = "";
        msCostCenterId = "";
    }

    public void setPercentage(double d) { mdPercentage = d; }
    public void setAccountId(java.lang.String s) { msAccountId = s == null ? "" : s; }
    public void setCostCenterId(java.lang.String s) { msCostCenterId = s == null ? "" : s; }

    public double getPercentage() { return mdPercentage; }
    public java.lang.String getAccountId() { return msAccountId; }
    public java.lang.String getCostCenterId() { return msCostCenterId; }
}
