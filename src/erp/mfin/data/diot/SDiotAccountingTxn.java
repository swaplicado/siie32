/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mfin.data.diot;

import erp.data.SDataConstantsSys;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import sa.lib.SLibUtils;

/**
 *
 * @author Sergio Flores
 */
public class SDiotAccountingTxn {
    
    private final Statement miStatement;
    private final int mnEntryUserId;
    private final Object[] maoRecordKey;
    private final int[] manBkkNumberKey;
    private final int[] manDpsKey;
    
    private ArrayList<Entry> maEntries;
    
    /**
     * Create the representation of an accounting transaction, at the document level, if applicable.
     * @param statement
     * @param entryUserId
     * @param recordKey
     * @param bkkNumberKey
     * @throws Exception 
     */
    public SDiotAccountingTxn(final Statement statement, final int entryUserId, final Object[] recordKey, final int[] bkkNumberKey, final int[] dpsKey) throws Exception {
        miStatement = statement;
        mnEntryUserId = entryUserId;
        maoRecordKey = recordKey;
        manBkkNumberKey = bkkNumberKey;
        manDpsKey = dpsKey != null ? dpsKey : new int[2];
        
        createEntries();
    }
    
    /**
     * Create individual sumarized entries of system-movement types.
     * Should be called only by private method <code>createChildren()</code>.
     * @throws Exception 
     */
    private void createEntries() throws Exception {
        maEntries = new ArrayList<>();
        
        String sql = "SELECT re.fid_ct_sys_mov_xxx, re.fid_tp_sys_mov_xxx, re.fid_tax_bas_n, re.fid_tax_n, "
                + "SUM(re.debit) AS _debit, SUM(re.credit) AS _credit, "
                + "(SELECT SUM(det.tax) "
                + " FROM trn_dps_ety_tax AS det "
                + " WHERE det.id_year = " + manDpsKey[0] + " AND det.id_doc = " + manDpsKey[1] + " AND det.id_tax_bas = re.fid_tax_bas_n AND det.id_tax = re.fid_tax_n) AS _tax, "
                + "(SELECT SUM(de.stot_r) "
                + " FROM trn_dps_ety AS de "
                + " INNER JOIN trn_dps_ety_tax AS det ON de.id_year = det.id_year AND de.id_doc = det.id_doc AND de.id_ety = det.id_ety "
                + " WHERE NOT de.b_del AND de.id_year = " + manDpsKey[0] + " AND de.id_doc = " + manDpsKey[1] + " AND det.id_tax_bas = re.fid_tax_bas_n AND det.id_tax = re.fid_tax_n) AS _stot "
                + "FROM fin_rec AS r "
                + "INNER JOIN fin_rec_ety AS re ON "
                + "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num "
                + "WHERE NOT r.b_del AND NOT re.b_del AND "
                + "re.usr_id = " + mnEntryUserId + " AND "
                + "r.id_year = " + maoRecordKey[0] + " AND r.id_per = " + maoRecordKey[1] + " AND r.id_bkc = " + maoRecordKey[2] + " AND r.id_tp_rec = '" + maoRecordKey[3] + "' AND r.id_num = " + maoRecordKey[4] + " AND "
                + "re.fid_bkk_year_n " + (manBkkNumberKey[0] == 0 ? "IS NULL" : "= " + manBkkNumberKey[0]) + " AND re.fid_bkk_num_n " + (manBkkNumberKey[1] == 0 ? "IS NULL" : "= " + manBkkNumberKey[1]) + " AND "
                + "re.fid_dps_year_n " + (manDpsKey[0] == 0 ? "IS NULL" : "= " + manDpsKey[0]) + " AND re.fid_dps_doc_n " + (manDpsKey[1] == 0 ? "IS NULL" : "= " + manDpsKey[1]) + " "
                + "GROUP BY re.fid_ct_sys_mov_xxx, re.fid_tp_sys_mov_xxx, re.fid_tax_bas_n, re.fid_tax_n "
                + "ORDER BY re.fid_ct_sys_mov_xxx, re.fid_tp_sys_mov_xxx, re.fid_tax_bas_n, re.fid_tax_n;";

        try (ResultSet resultSet = miStatement.executeQuery(sql)) {
            while (resultSet.next()) {
                maEntries.add(new Entry(
                        new int[] { resultSet.getInt("re.fid_ct_sys_mov_xxx"), resultSet.getInt("re.fid_tp_sys_mov_xxx") },
                        new int[] { resultSet.getInt("re.fid_tax_bas_n"), resultSet.getInt("re.fid_tax_n") },
                        resultSet.getDouble("_debit"),
                        resultSet.getDouble("_credit"),
                        resultSet.getDouble("_tax"),
                        resultSet.getDouble("_stot")
                ));
            }
        }
    }
    
    /**
     * Get Entry according to type of system movement and VAT.
     * @param sysMovTypeXxxKey Key of type of system movement
     * @param vatKey Key of required VAT. Can be null.
     * @return Found entry, otherwise <code>null</code>.
     */
    private Entry getEntry(int[] sysMovTypeXxxKey, final int[] vatKey) {
        Entry entry = null;
        
        for (Entry e : maEntries) {
            if (SLibUtils.compareKeys(e.SysMovTypeXxxKey, sysMovTypeXxxKey) && SLibUtils.compareKeys(e.VatKey, vatKey)) {
                entry = e;
                break;
            }
        }
        
        return entry;
    }
    
    /**
     * Get debit or credit of entry according to type of system movement and VAT.
     * @param sysMovTypeXxxKey Key of type of system movement
     * @param vatKey Key of required VAT. Can be null.
     * @return Debit or credit of entry.
     */
    private double getEntryAmount(int[] sysMovTypeXxxKey, final int[] vatKey) {
        double amount = 0;
        Entry entry = getEntry(sysMovTypeXxxKey, vatKey);
        
        if (entry != null) {
            if (SLibUtils.compareKeys(sysMovTypeXxxKey, SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP)) {
                amount = entry.getAmountAsDebit();
            }
            else if (SLibUtils.compareKeys(sysMovTypeXxxKey, SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT)) {
                amount = entry.getAmountAsDebit();
            }
            else if (SLibUtils.compareKeys(sysMovTypeXxxKey, SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT)) {
                amount = entry.getAmountAsCredit();
            }
        }
        
        return amount;
    }
    
    /**
     * Get payment amount.
     * @return Payment amount.
     */
    public double getPaymentAmount() {
        return getEntryAmount(SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP, null);
    }
    
    /**
     * Get DPS original subtotal according to type of system movement and VAT.
     * @param sysMovTypeXxxKey Key of type of system movement
     * @param vatKey Key of required VAT. Can be null.
     * @return DPS original tax.
     */
    public double getEntryDpsSubtotal(int[] sysMovTypeXxxKey, final int[] vatKey) {
        double dpsSubtotal = 0;
        Entry entry = getEntry(sysMovTypeXxxKey, vatKey);
        
        if (entry != null) {
            dpsSubtotal = entry.DpsSubtotal;
        }
        
        return dpsSubtotal;
    }
    
    private class Entry {
        public int[] SysMovTypeXxxKey;
        public int[] VatKey;
        public double Debit;
        public double Credit;
        public double DpsTax;
        public double DpsSubtotal;
        
        public Entry(final int[] sysMovTypeXxxKey, final int[] vatKey, final double debit, final double credit, final double dpsTax, final double dpsSubtotal) {
            SysMovTypeXxxKey = sysMovTypeXxxKey;
            VatKey = vatKey == null ? null : (vatKey[0] == 0 && vatKey[1] == 0 ? null : vatKey);
            Debit = debit;
            Credit = credit;
            DpsTax = dpsTax;
            DpsSubtotal = dpsSubtotal;
        }
        
        public double getAmountAsDebit() {
            double amount = 0;
            
            if (Debit != 0) {
                amount = Debit;
            }
            else if (Credit < 0) {
                amount = -Credit;
            }
            
            return amount;
        }
        
        public double getAmountAsCredit() {
            double amount = 0;
            
            if (Credit != 0) {
                amount = Credit;
            }
            else if (Debit < 0) {
                amount = -Debit;
            }
            
            return amount;
        }
    }
}
