/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data.cfd;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import sa.lib.SLibUtils;

/**
 *
 * @author Isabel Servín
 */
public final class SDataReceiptPaymentPayTax extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkReceiptId;
    protected int mnPkPaymentId;
    protected int mnPkTaxId;
    protected double mdBase;
    protected java.lang.String msFactorCode;
    protected double mdRate;
    protected double mdTax;
    protected int mnFkCfdTaxId;
    protected int mnFkTaxTypeId;
    
    public SDataReceiptPaymentPayTax() {
        super(SDataConstants.TRN_PAY_PAY_TAX);
        reset();
    }
    
    public void setPkReceiptId(int n) { mnPkReceiptId = n; }
    public void setPkPaymentId(int n) { mnPkPaymentId = n; }
    public void setPkTaxId(int n) { mnPkTaxId = n; }
    public void setBase(double d) { mdBase = d; }
    public void setFactorCode(java.lang.String s) { msFactorCode = s; }
    public void setRate(double d) { mdRate = d; }
    public void setTax(double d) { mdTax = d; }
    public void setFkCfdTaxId(int n) { mnFkCfdTaxId = n; }
    public void setFkTaxTypeId(int n) { mnFkTaxTypeId = n; }
    
    public int getPkReceiptId() { return mnPkReceiptId; }
    public int getPkPaymentId() { return mnPkPaymentId; }
    public int getPkTaxId() { return mnPkTaxId; }
    public double getBase() { return mdBase; }
    public java.lang.String getFactorCode() { return msFactorCode; }
    public double getRate() { return mdRate; }
    public double getTax() { return mdTax; }
    public int getFkCfdTaxId() { return mnFkCfdTaxId; }
    public int getFkTaxTypeId() { return mnFkTaxTypeId; }
    
    
    @Override
    public void setPrimaryKey(Object pk) {
        mnPkReceiptId = ((int[]) pk)[0];
        mnPkPaymentId = ((int[]) pk)[1];
        mnPkTaxId = ((int[]) pk)[2];
    }

    @Override
    public Object getPrimaryKey() {
        return new int[] { mnPkReceiptId, mnPkPaymentId, mnPkTaxId };
    }

    @Override
    public void reset() {
        super.resetRegistry();
        
        mnPkReceiptId = 0;
        mnPkPaymentId = 0;
        mnPkTaxId = 0;
        mdBase = 0;
        msFactorCode = "";
        mdRate = 0;
        mdTax = 0;
        mnFkCfdTaxId = 0;
        mnFkTaxTypeId = 0;
        
    }

    @Override
    public int read(Object pk, Statement statement) {
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();
        
        try {
            int[] key = (int[]) pk;
            String sql = "SELECT pt.* "
                    + "FROM trn_pay_pay_tax AS pt "
                    + "WHERE id_rcp = " + key[0] + " "
                    + "AND id_pay = " + key[1] + " "
                    + "AND pt.id_tax = " + key[2] + ";";
            try (ResultSet resultSet = statement.executeQuery(sql)){
                if (!resultSet.next()) {
                    throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT + "\nImpuestos pago #" + SLibUtils.textImplode(key, "-") + ".");
                }
                else {
                    mnPkReceiptId = resultSet.getInt("id_rcp");
                    mnPkPaymentId = resultSet.getInt("id_pay");
                    mnPkTaxId = resultSet.getInt("id_tax");
                    mdBase = resultSet.getDouble("base");
                    msFactorCode = resultSet.getString("factor_code");
                    mdRate = resultSet.getDouble("rate");
                    mdTax = resultSet.getDouble("tax");
                    mnFkCfdTaxId = resultSet.getInt("fid_cfd_tax");
                    mnFkTaxTypeId = resultSet.getInt("fid_tp_tax");
                    
                    mbIsRegistryNew = false;
                    mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
                }
            }
        }
        catch (Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        } 
        
        return mnLastDbActionResult;
    }

    @Override
    public int save(Connection connection) {
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        
        try {
            try (Statement statement = connection.createStatement()){
                String sql;
                
                if (mnPkTaxId == 0) {
                    sql = "SELECT COALESCE(MAX(id_tax), 0) + 1 "
                            + "FROM trn_pay_pay_tax "
                            + "WHERE id_rcp = " + mnPkReceiptId + " "
                            + "AND id_pay = " + mnPkPaymentId + ";"; 
                    
                    ResultSet resultSet = statement.executeQuery(sql);
                    if (resultSet.next()) {
                        mnPkTaxId = resultSet.getInt(1);
                    }
                    
                    sql = "INSERT INTO trn_pay_pay_tax VALUES (" +
                            mnPkReceiptId + ", " +
                            mnPkPaymentId + ", " +
                            mnPkTaxId + ", " +
                            mdBase + ", " +
                            "'" + msFactorCode + "', " +
                            mdRate + ", " +
                            mdTax + ", " +
                            mnFkCfdTaxId + ", " +
                            mnFkTaxTypeId + " " +
                            ");";
                }
                else {
                    sql = "UPDATE trn_pay_pay_tax SET " +
                            //"id_rcp=" + mnPkReceiptId + ", " +
                            //"id_pay=" + mnPkPaymentId + ", " +
                            //"id_tax=" + mnPkTaxId + ", " +
                            "base = " + mdBase + ", " +
                            "factor_code = '" + msFactorCode + "', " +
                            "rate = " + mdRate + ", " +
                            "tax = " + mdTax + ", " +
                            "fid_cfd_tax = " + mnFkCfdTaxId + ", " +
                            "fid_tp_tax = " + mnFkTaxTypeId + "; ";
                }
                
                statement.execute(sql);
                
                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
            }
        }
        catch (Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        
        return mnLastDbActionResult;
    }

    @Override
    public Date getLastDbUpdate() {
        return null;
    }
    
    @Override
    public erp.mtrn.data.cfd.SDataReceiptPaymentPayTax clone() throws CloneNotSupportedException {
        SDataReceiptPaymentPayTax clone = new SDataReceiptPaymentPayTax();
        
        clone.setIsRegistryNew(mbIsRegistryNew);
        
        clone.setPkReceiptId(mnPkReceiptId);
        clone.setPkPaymentId(mnPkPaymentId);
        clone.setPkTaxId(mnPkTaxId);
        clone.setBase(mdBase);
        clone.setFactorCode(msFactorCode);
        clone.setRate(mdRate);
        clone.setTax(mdTax);
        clone.setFkCfdTaxId(mnFkCfdTaxId);
        clone.setFkTaxTypeId(mnFkTaxTypeId);
        
        return clone;
    }
}
