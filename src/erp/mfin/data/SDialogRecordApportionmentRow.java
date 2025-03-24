/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mfin.data;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.table.STableRow;
import erp.mcfg.data.SDataCurrency;

/**
 *
 * @author Isabel Servín
 */
public final class SDialogRecordApportionmentRow extends STableRow {

    private SClientInterface miClient;
    
    private int mnNum;
    private double mdApportionment;
    private double mdDebit;
    private double mdCredit;
    private double mdDebitCy;
    private double mdCreditCy;
    
    private int mnCurId;
    private SDataCurrency moCur;
    private SDataCostCenter moCostCenter;

    public SDialogRecordApportionmentRow(SClientInterface client, String costCenterId) {
        try {
            if (!costCenterId.isEmpty()) {
                moCostCenter = new SDataCostCenter();
                moCostCenter.read(new Object[] { costCenterId }, client.getSession().getStatement());
            }
            
            miClient = client;
        }
        catch (Exception e) {
            client.showMsgBoxWarning(e.getMessage());
        }
    }
    
    public void setNum(int num) {
        mnNum = num;
    }
    
    public void setApportionment(double apportionment) {
        mdApportionment = apportionment;
    }
    
    public void setDebit(double debit) {
        mdDebit = debit;
    }
    
    public void setCredit(double credit) {
        mdCredit = credit;
    }
    
    public void setDebitCy(double debitCy) {
        mdDebitCy = debitCy;
    }
    
    public void setCreditCy(double creditCy) {
        mdCreditCy = creditCy;
    }
    
    public void setCurId(int curId) {
        mnCurId = curId;
        moCur = (SDataCurrency) SDataUtilities.readRegistry(miClient, SDataConstants.CFGU_CUR, new int[] { mnCurId }, SLibConstants.EXEC_MODE_SILENT);
    }
    
    public int getNum() {
        return mnNum;
    }
    
    public double getApportionment() {
        return mdApportionment;
    }
    
    public double getDebit() {
        return mdDebit;
    }
    
    public double getCredit() {
        return mdCredit;
    }
    
    public double getDebitCy() {
        return mdDebitCy;
    }
    
    public double getCreditCy() {
        return mdCreditCy;
    }
    
    @Override
    public void prepareTableRow() {
        mvValues.clear();
        mvValues.add(mnNum);
        mvValues.add(moCostCenter != null ? moCostCenter.getPkCostCenterIdXXX() : "");
        mvValues.add(moCostCenter != null ? moCostCenter.getCostCenter() : "");
        mvValues.add(mdApportionment);
        mvValues.add(mdDebit);
        mvValues.add(mdCredit);
        mvValues.add(mdDebitCy);
        mvValues.add(mdCreditCy);
        mvValues.add(moCur.getKey());
    }
}
