/*
 * Copyright © Software Aplicado SA de CV. All rights reserverd.
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
    protected int mnBasicTax_n;
    protected int mnTax_n;

    public SFinAccountConfigEntry(String account, String costCenter, double percentage) {
        setAccountId(account);
        setCostCenterId(costCenter);
        setPercentage(percentage);
    }

    public SFinAccountConfigEntry(String msAccountId, String msCostCenterId, double mdPercentage, int mnBasicTax_n, int mnTax_n) {
        this.msAccountId = msAccountId;
        this.msCostCenterId = msCostCenterId;
        this.mdPercentage = mdPercentage;
        this.mnBasicTax_n = mnBasicTax_n;
        this.mnTax_n = mnTax_n;
    }

    public final void setAccountId(java.lang.String s) { msAccountId = s == null ? "" : s; }
    public final void setCostCenterId(java.lang.String s) { msCostCenterId = s == null ? "" : s; }
    public final void setPercentage(double d) { mdPercentage = d; }
    public final void setBasicTax(int n) { mnBasicTax_n = n; }
    public final void setTax(int n) { mnTax_n = n; }

    public java.lang.String getAccountId() { return msAccountId; }
    public java.lang.String getCostCenterId() { return msCostCenterId; }
    public double getPercentage() { return mdPercentage; }
    public int getBasicTax() { return mnBasicTax_n; }
    public int getTax() { return mnTax_n; }
}
