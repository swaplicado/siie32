/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

/**
 *
 * @author Sergio Flores
 */
public class SFinAccountConfigEntry implements java.io.Serializable {

    protected java.lang.String msAccountId;
    protected java.lang.String msCostCenterId;
    protected double mdPercentage;

    public SFinAccountConfigEntry(String account, String costCenter, double percentage) {
        setAccountId(account);
        setCostCenterId(costCenter);
        setPercentage(percentage);
    }

    public final void setAccountId(java.lang.String s) { msAccountId = s == null ? "" : s; }
    public final void setCostCenterId(java.lang.String s) { msCostCenterId = s == null ? "" : s; }
    public final void setPercentage(double d) { mdPercentage = d; }

    public java.lang.String getAccountId() { return msAccountId; }
    public java.lang.String getCostCenterId() { return msCostCenterId; }
    public double getPercentage() { return mdPercentage; }
}
