/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.mkt.db;

import erp.data.SDataConstantsSys;
import erp.mcfg.data.SDataParamsErp;
import erp.mmkt.data.SDataConfigurationSalesAgent;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Néstor Ávalos
 */
public class SMktCommissionCalculate extends SDbRegistryUser {

    protected int mnBizPartnerId;
    protected Date mtDateCommissionsCalculate;
    protected Date mtDateStart;
    protected Date mtDateEnd;

    protected int mnAuxParamInBizPartnerId;
    protected int mnAuxParamInBizPartnerTypeId;
    protected int mnAuxParamInItemId;
    protected Date mtAuxParamInDate;
    protected int mnAuxParamOutLinkTypeId;
    protected int mnAuxParamOutReferenceId;
    protected double mdAuxParamOutPercentage;
    protected double mdAuxParamOutValueUnitary;
    protected double mdAuxParamOutValue;
    protected boolean mbAuxParamOutCommissionTypeSource;
    protected boolean mbAuxParamOutResult;

    protected boolean mbAuxCalcultateCommsForAllDocs;

    protected SDbCommissionPayment moCommissionPayment;

    protected ArrayList<SMktCommissionsPayableRow> maMktCommissionsPayableRows;
    protected ArrayList<SMktCommissionsPayableRow> maMktCommissionsPayedRows;

    public SMktCommissionCalculate() {
        super(SModConsts.MKTX_COMMS_CALC);
    }

    /*
     * Private methods
     */

    private void obtainLinkTypeReference(final SGuiSession session, final boolean isAgent) throws Exception {
        int nParam = 1;
        CallableStatement callableStatement = null;

        try {
            mnAuxParamOutLinkTypeId = 0;
            mnAuxParamOutReferenceId = 0;
            mdAuxParamOutPercentage = 0;
            mdAuxParamOutValueUnitary = 0;
            mdAuxParamOutValue = 0;
            mbAuxParamOutCommissionTypeSource = false;
            mbAuxParamOutResult = false;

            callableStatement = session.getDatabase().getConnection().prepareCall("{ " +
                    "CALL mkt_get_cfg_comms(" +
                        "?, ?, ?, ?, ?, " +
                        "?, ?, ?, ?, ?, " +
                        "?, ?) }");
            callableStatement.setBoolean(nParam, (java.lang.Boolean) isAgent);
            nParam++;
            callableStatement.setInt(nParam, (java.lang.Integer) mnAuxParamInBizPartnerId);
            nParam++;
            callableStatement.setInt(nParam, (java.lang.Integer) mnAuxParamInBizPartnerTypeId);
            nParam++;
            callableStatement.setInt(nParam, (java.lang.Integer) mnAuxParamInItemId);
            nParam++;
            callableStatement.setDate(nParam, (java.sql.Date) mtAuxParamInDate);
            nParam++;
            callableStatement.registerOutParameter(nParam, java.sql.Types.INTEGER);
            nParam++;
            callableStatement.registerOutParameter(nParam, java.sql.Types.INTEGER);
            nParam++;
            callableStatement.registerOutParameter(nParam, java.sql.Types.DOUBLE);
            nParam++;
            callableStatement.registerOutParameter(nParam, java.sql.Types.DOUBLE);
            nParam++;
            callableStatement.registerOutParameter(nParam, java.sql.Types.DOUBLE);
            nParam++;
            callableStatement.registerOutParameter(nParam, java.sql.Types.BOOLEAN);
            nParam++;
            callableStatement.registerOutParameter(nParam, java.sql.Types.BOOLEAN);
            callableStatement.execute();

            mbAuxParamOutResult = callableStatement.getBoolean(nParam);
            nParam--;
            mbAuxParamOutCommissionTypeSource = callableStatement.getBoolean(nParam);
            nParam--;
            mdAuxParamOutValue = callableStatement.getDouble(nParam);
            nParam--;
            mdAuxParamOutValueUnitary = callableStatement.getDouble(nParam);
            nParam--;
            mdAuxParamOutPercentage = callableStatement.getDouble(nParam);
            nParam--;
            mnAuxParamOutReferenceId = callableStatement.getInt(nParam);
            nParam--;
            mnAuxParamOutLinkTypeId = callableStatement.getInt(nParam);
        }
        catch (java.lang.Exception e) {
            throw new Exception(e);
        }
    }

    private String obtainQuery(final SGuiSession session, final int[] dpsPrimaryKey) {
        return "SELECT de.id_year, de.id_doc, de.id_ety, de.fid_item, de.fid_unit, tp.code, d.fid_ct_dps, d.fid_cl_dps, d.fid_tp_dps, " +
            "b.bp, d.dt, CONCAT(d.num_ser, IF(LENGTH(d.num_ser) = 0, '', '-'), d.num) AS f_num, " +
            "COALESCE(d.fid_sal_agt_n, 0) AS f_id_sal_agt, COALESCE(d.fid_sal_sup_n, 0) AS f_id_sal_sup, d.stot_r, c.cur_key, " +
            "de.stot_r AS f_entry_stot_r, i.item, i.item_key, de.qty, u.symbol, " +

            "COALESCE((SELECT COALESCE(SUM(da.stot_r), 0) " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_DPS_ADJ) + " AS dda " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS da ON " +
            "dda.id_adj_year = da.id_year AND dda.id_adj_doc = da.id_doc AND da.b_del = 0 AND da.fid_st_dps = " + SModSysConsts.TRNS_ST_DPS_EMITED + " " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " AS dae ON " +
            "dda.id_adj_year = dae.id_year AND dda.id_adj_doc = dae.id_doc AND dda.id_adj_ety = dae.id_ety AND dae.b_del = 0 " +
            "WHERE de.id_year = dda.id_dps_year AND de.id_doc = dda.id_dps_doc AND de.id_ety = dda.id_dps_ety " +
            "GROUP BY dda.id_dps_year, dda.id_dps_doc), 0) AS f_adj, " +

            "(SELECT SUM(sre.debit - sre.credit) " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS sr " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS sre ON " +
            "sr.id_year = sre.id_year AND sr.id_per = sre.id_per AND sr.id_bkc = sre.id_bkc AND sr.id_tp_rec = sre.id_tp_rec AND " +
            "sr.id_num = sre.id_num AND sr.b_del = 0 AND sre.b_del = 0 AND sre.fid_ct_sys_mov_xxx = " + SModSysConsts.FINS_TP_SYS_MOV_BPS_CUS[0] + " AND " +
            "sre.fid_tp_sys_mov_xxx = " + SModSysConsts.FINS_TP_SYS_MOV_BPS_CUS[1] + " " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS sd ON " +
            "sre.fid_dps_year_n = sd.id_year AND sre.fid_dps_doc_n = sd.id_doc AND sd.fid_ct_dps = " + SModSysConsts.TRNS_CT_DPS_SAL + " AND " +
            "sd.fid_cl_dps = " + SModSysConsts.TRNS_CL_DPS_SAL_DOC[1] + " AND sd.fid_st_dps = " + SModSysConsts.TRNS_ST_DPS_EMITED + " AND " +
            "sd.fid_st_dps_val = " + SModSysConsts.TRNS_ST_DPS_VAL_EFF + " AND sd.b_del = 0 " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " AS sde ON " +
            "sd.id_year = sde.id_year AND sd.id_doc = sde.id_doc AND sde.b_del = 0 " +
            "WHERE sr.id_tp_rec <> '" + SModSysConsts.FINU_TP_REC_FY_OPEN + "' AND sd.id_year = d.id_year AND sd.id_doc = d.id_doc AND sde.id_ety = de.id_ety AND " +
                "sr.dt <= '" + SLibUtils.DbmsDateFormatDate.format(mtDateEnd) + "' " +
            "GROUP BY sde.id_year, sde.id_doc)  AS f_balance, " +

            "(SELECT MAX(sr.dt) " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS sr " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS sre ON " +
            "sr.id_year = sre.id_year AND sr.id_per = sre.id_per AND sr.id_bkc = sre.id_bkc AND sr.id_tp_rec = sre.id_tp_rec AND " +
            "sr.id_num = sre.id_num AND sr.b_del = 0 AND sre.b_del = 0 AND sre.fid_ct_sys_mov_xxx = " + SModSysConsts.FINS_TP_SYS_MOV_BPS_CUS[0] + " AND " +
            "sre.fid_tp_sys_mov_xxx = " + SModSysConsts.FINS_TP_SYS_MOV_BPS_CUS[1] + " " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS sd ON " +
            "sre.fid_dps_year_n = sd.id_year AND sre.fid_dps_doc_n = sd.id_doc AND sd.fid_ct_dps = " + SModSysConsts.TRNS_CT_DPS_SAL + " AND " +
            "sd.fid_cl_dps = " + SModSysConsts.TRNS_CL_DPS_SAL_DOC[1] + " AND sd.fid_st_dps = " + SModSysConsts.TRNS_ST_DPS_EMITED + " AND " +
            "sd.fid_st_dps_val = " + SModSysConsts.TRNS_ST_DPS_VAL_EFF + " AND sd.b_del = 0 " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " AS sde ON " +
            "sd.id_year = sde.id_year AND sd.id_doc = sde.id_doc " +
            "WHERE sr.id_tp_rec <> '" + SModSysConsts.FINU_TP_REC_FY_OPEN + "' AND sd.id_year = d.id_year AND sd.id_doc = d.id_doc AND sde.id_ety = de.id_ety " +
            "GROUP BY sde.id_year, sde.id_doc)  AS f_dt_pay, " +

            "COALESCE((SELECT COALESCE(dae.stot_r, 0) " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_DPS_ADJ) + " AS dda " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS da ON " +
            "dda.id_adj_year = da.id_year AND dda.id_adj_doc = da.id_doc AND da.b_del = 0 AND da.fid_st_dps = " + SModSysConsts.TRNS_ST_DPS_EMITED + " " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " AS dae ON " +
            "dda.id_adj_year = dae.id_year AND dda.id_adj_doc = dae.id_doc AND dda.id_adj_ety = dae.id_ety AND dae.b_del = 0 " +
            "WHERE de.id_year = dda.id_dps_year AND de.id_doc = dda.id_dps_doc AND de.id_ety = dda.id_dps_ety " +
            "GROUP BY dda.id_dps_year, dda.id_dps_doc, dda.id_dps_ety), 0) AS f_adj_stot_ety, " +

            "COALESCE((SELECT SUM(pe.pay - pe.rfd) " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.MKT_COMMS_PAY) + " AS p " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MKT_COMMS_PAY_ETY) + " AS pe ON " +
            "p.id_pay = pe.id_pay " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " AS sde ON " +
            "pe.fk_year = sde.id_year AND pe.fk_doc = sde.id_doc AND pe.fk_ety = sde.id_ety " +
            "WHERE p.b_del = 0 AND pe.fk_year = de.id_year AND pe.fk_doc = de.id_doc AND pe.fk_ety = de.id_ety AND pe.fk_sal_agt = " + mnBizPartnerId + " " +
            "GROUP BY pe.fk_year, pe.fk_doc, pe.fk_ety, pe.fk_sal_agt), 0) AS f_paid " +

            "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS re ON " +
            "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND " +
            "r.id_num = re.id_num AND r.b_del = 0 AND re.b_del = 0 AND re.fid_ct_sys_mov_xxx = " + SModSysConsts.FINS_TP_SYS_MOV_BPS_CUS[0] + " AND " +
            "re.fid_tp_sys_mov_xxx = " + SModSysConsts.FINS_TP_SYS_MOV_BPS_CUS[1] + " " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d ON " +
            "re.fid_dps_year_n = d.id_year AND re.fid_dps_doc_n = d.id_doc AND d.fid_ct_dps = " + SModSysConsts.TRNS_CT_DPS_SAL + " AND " +
            "d.fid_cl_dps = " + SModSysConsts.TRNS_CL_DPS_SAL_DOC[1] + " AND d.fid_st_dps = " + SModSysConsts.TRNS_ST_DPS_EMITED + " AND " +
            "d.fid_st_dps_val = " + SModSysConsts.TRNS_ST_DPS_VAL_EFF + " AND d.b_del = 0 " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNU_TP_DPS) + " AS tp ON " +
            "d.fid_ct_dps = tp.id_ct_dps AND d.fid_cl_dps = tp.id_cl_dps AND d.fid_tp_dps = tp.id_tp_dps " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b ON " +
            "d.fid_bp_r = b.id_bp " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS c ON " +
            "c.id_cur = " + ((SDataParamsErp) session.getConfigSystem()).getFkCurrencyId() + " " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " AS de ON " +
            "d.id_year = de.id_year AND d.id_doc = de.id_doc AND de.b_del = 0 " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON " +
            "de.fid_item = i.id_item " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS u ON " +
            "de.fid_unit = u.id_unit " +
            "WHERE r.id_tp_rec <> '" + SModSysConsts.FINU_TP_REC_FY_OPEN + "' AND r.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "' AND " +
                "'" + SLibUtils.DbmsDateFormatDate.format(mtDateEnd) + "' AND (d.fid_sal_agt_n = " + mnBizPartnerId + " OR d.fid_sal_sup_n = " + mnBizPartnerId + ") " +
                (dpsPrimaryKey == null ? "" : "AND d.id_year = " + dpsPrimaryKey[0] + " AND d.id_doc = " + dpsPrimaryKey[1]) + " " +
            "GROUP BY de.id_year, de.id_doc" + (dpsPrimaryKey == null ? "" : ", de.id_ety ") + " " +
            "HAVING f_balance = 0 " +
            "ORDER BY de.id_year, de.id_doc" + (dpsPrimaryKey == null ? "" : ", de.id_ety ") + "; ";
    }

    private void deleteCommissionsCalculated(final SGuiSession session, final int nPkLogId) throws Exception {

        Statement statement = session.getDatabase().getConnection().createStatement();
        statement.execute("DELETE FROM " + SModConsts.TablesMap.get(SModConsts.MKT_COMMS) + " WHERE fk_log = " + nPkLogId + "; ");
    }

    private void initializeConsts(final SGuiSession session) throws Exception {

        for (String cons : SDataConstantsSys.DB_SETTINGS) {
            session.getStatement().execute(cons + "; ");
        }
    }


    /*
     * Public methods
     */

    public void setBizPartnerId(int n) { mnBizPartnerId = n; }
    public void setDateCommissionsCalcuate(Date t) { mtDateCommissionsCalculate = t; }
    public void setDateStart(Date t) { mtDateStart = t; }
    public void setDateEnd(Date t) { mtDateEnd = t; }

    public int getBizPartnerId() { return mnBizPartnerId; }
    public Date getDateCommissionsCalcuate() { return mtDateCommissionsCalculate; }
    public Date getDateStart() { return mtDateStart; }
    public Date getDateEnd() { return mtDateEnd; }

    public void setAuxParamInBizPartnerId(int n) { mnAuxParamInBizPartnerId = n; }
    public void setAuxParamInBizPartnerTypeId(int n) { mnAuxParamInBizPartnerTypeId = n; }
    public void setAuxParamInItemId(int n) { mnAuxParamInItemId = n; }
    public void setAuxParamInDate(Date t) { mtAuxParamInDate = t; }
    public void setAuxParamOutLinkTypeId(int n) { mnAuxParamOutLinkTypeId = n; }
    public void setAuxParamOutReferenceId(int n) { mnAuxParamOutReferenceId = n; }
    public void setAuxParamOutPercentage(double d) { mdAuxParamOutPercentage = d; }
    public void setAuxParamOutValueUnitary(double d) { mdAuxParamOutValueUnitary = d; }
    public void setAuxParamOutValue(double d) { mdAuxParamOutValue = d; }
    public void setAuxParamOutCommissionTypeSource(boolean b) { mbAuxParamOutCommissionTypeSource = b; }
    public void setAuxParamOutResult(boolean b) { mbAuxParamOutResult = b; }
    public void setAuxCalcultateCommsForAllDocs(boolean b) { mbAuxCalcultateCommsForAllDocs = b; }

    public int getAuxParamInBizPartnerId() { return mnAuxParamInBizPartnerId; }
    public int getAuxParamInBizPartnerTypeId() { return mnAuxParamInBizPartnerTypeId; }
    public int getAuxParamInItemId() { return mnAuxParamInItemId; }
    public Date getAuxParamInDate() { return mtAuxParamInDate; }
    public int getAuxParamOutLinkTypeId() { return mnAuxParamOutLinkTypeId; }
    public int getAuxParamOutReferenceId() { return mnAuxParamOutReferenceId; }
    public double getAuxParamOutPercentage() { return mdAuxParamOutPercentage; }
    public double getAuxParamOutValueUnitary() { return mdAuxParamOutValueUnitary; }
    public double getAuxParamOutValue() { return mdAuxParamOutValue; }
    public boolean getAuxParamOutCommissionTypeSource() { return mbAuxParamOutCommissionTypeSource; }
    public boolean getAuxParamOutResult() { return mbAuxParamOutResult; }
    public boolean getAuxCalcultateCommsForAllDocs() { return mbAuxCalcultateCommsForAllDocs; }

    public void setCommissionPayment(SDbCommissionPayment o) { moCommissionPayment = o; }

    public SDbCommissionPayment getCommissionPayment() { return moCommissionPayment; }

    public ArrayList<SMktCommissionsPayableRow> getMktCommissionsPayableRows() { return maMktCommissionsPayableRows; }
    public ArrayList<SMktCommissionsPayableRow> getMktCommissionsPayedRows() { return maMktCommissionsPayedRows; }

    @Override
    public void setPrimaryKey(int[] pk) {

    }

    @Override
    public int[] getPrimaryKey() {
        return null;
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnAuxParamInBizPartnerId = 0;
        mnAuxParamInBizPartnerTypeId = 0;
        mnAuxParamInItemId = 0;
        mtAuxParamInDate = null;
        mnAuxParamOutLinkTypeId = 0;
        mnAuxParamOutReferenceId = 0;
        mdAuxParamOutPercentage = 0;
        mdAuxParamOutValueUnitary = 0;
        mdAuxParamOutValue = 0;
        mbAuxParamOutCommissionTypeSource = false;
        mbAuxParamOutResult = false;
        mbAuxCalcultateCommsForAllDocs = false;

        moCommissionPayment = null;

        maMktCommissionsPayableRows = new ArrayList<SMktCommissionsPayableRow>();
        maMktCommissionsPayedRows = new ArrayList<SMktCommissionsPayableRow>();
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {

    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        boolean isAgent = false;
        double dCommissionAdjustmentEntry = 0;

        Statement statement = session.getDatabase().getConnection().createStatement();
        Statement statementDps = session.getDatabase().getConnection().createStatement();
        ResultSet resultSetDps = null;
        Statement statementDpsEntry = session.getDatabase().getConnection().createStatement();
        ResultSet resultSetDpsEntry = null;

        SDbCommission commission = null;
        SMktCommissionsPayableRow mktCommissionPayableRow = null;
        SDataConfigurationSalesAgent cfgSalesAgent = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        initializeConsts(session);
        msSql = obtainQuery(session, null);
        resultSetDps = statementDps.executeQuery(msSql);
        while (resultSetDps.next()) {

            mktCommissionPayableRow = new SMktCommissionsPayableRow();
            mktCommissionPayableRow.setAuxDpsType(resultSetDps.getString("tp.code"));
            mktCommissionPayableRow.setAuxDpsNumber(resultSetDps.getString("f_num"));
            mktCommissionPayableRow.setAuxDpsDateDoc(resultSetDps.getDate("d.dt"));
            mktCommissionPayableRow.setAuxDpsDateCommissionMaterialization(resultSetDps.getDate("f_dt_pay"));
            mktCommissionPayableRow.setAuxDpsBizPartner(resultSetDps.getString("b.bp"));
            mktCommissionPayableRow.setAuxDpsSubtotal(resultSetDps.getDouble("d.stot_r"));
            mktCommissionPayableRow.setAuxDpsAdjustment(resultSetDps.getDouble("f_adj"));
            mktCommissionPayableRow.setAuxDpsCurrencyKey(resultSetDps.getString("c.cur_key"));

            msSql = obtainQuery(session, new int[] { resultSetDps.getInt("de.id_year"), resultSetDps.getInt("de.id_doc") });
            resultSetDpsEntry = statementDpsEntry.executeQuery(msSql);
            while(resultSetDpsEntry.next()) {

                // Check if is necessary calculate commission:

                if (mbAuxCalcultateCommsForAllDocs ||
                        resultSetDpsEntry.getDouble("f_paid") == 0) {

                    // Obtain sales agent/supervisor type:

                    cfgSalesAgent = new SDataConfigurationSalesAgent();
                    if (resultSetDps.getInt("f_id_sal_agt") == mnBizPartnerId) {

                        cfgSalesAgent.read(new int[] { resultSetDps.getInt("f_id_sal_agt") }, statement);
                        isAgent = true;
                    }
                    else {

                        cfgSalesAgent.read(new int[] { resultSetDps.getInt("f_id_sal_sup") }, statement);
                        isAgent = false;
                    }

                    // Obtain tp_link and reference by sales agent or sales agent/supervisor type:

                    mnAuxParamInBizPartnerId = isAgent ? resultSetDps.getInt("f_id_sal_agt") : resultSetDps.getInt("f_id_sal_sup");
                    mnAuxParamInBizPartnerTypeId = cfgSalesAgent.getFkSalesAgentTypeId();
                    mnAuxParamInItemId = resultSetDpsEntry.getInt("de.fid_item");
                    mtAuxParamInDate = resultSetDps.getDate("d.dt");
                    obtainLinkTypeReference(session, isAgent);

                    // Calculate commission:

                    dCommissionAdjustmentEntry = 0;
                    commission = new SDbCommission();

                    commission.setPkYearId(resultSetDpsEntry.getInt("de.id_year"));
                    commission.setPkDocId(resultSetDpsEntry.getInt("de.id_doc"));
                    commission.setPkEntryId(resultSetDpsEntry.getInt("de.id_ety"));
                    commission.setPkSalesAgentId(isAgent ? resultSetDps.getInt("f_id_sal_agt") : resultSetDps.getInt("f_id_sal_sup"));
                    commission.setDate(mtDateCommissionsCalculate);
                    commission.setDateCommissions(resultSetDps.getDate("f_dt_pay"));
                    commission.setPercentage(mdAuxParamOutPercentage);
                    commission.setValueUnitary(mdAuxParamOutValueUnitary);
                    commission.setValue(mdAuxParamOutValue);
                    commission.calculateCommission(SModSysConsts.MKTS_TP_COMMS_PER, mdAuxParamOutPercentage, 0, 0, (resultSetDpsEntry.getDouble("f_entry_stot_r")));
                    commission.setCommissionsModality(isAgent ? SModSysConsts.MKTS_COMMS_MOD_AGT : SModSysConsts.MKTS_COMMS_MOD_SUP);
                    commission.setCommissionsSource(!mbAuxParamOutCommissionTypeSource ? SModSysConsts.MKTS_COMMS_SRC_DIR : SModSysConsts.MKTS_COMMS_SRC_TYPE);
                    commission.setFkOriginCommissionsId(SModSysConsts.MKTS_ORIG_COMMS_DOC);
                    commission.setFkCommissionsTypeId(SModSysConsts.MKTS_TP_COMMS_PER);
                    commission.setFkSalesAgentTypeId_n(cfgSalesAgent.getFkSalesAgentTypeId());
                    commission.setFkLinkTypeId(mnAuxParamOutLinkTypeId > 0 ? mnAuxParamOutLinkTypeId : SModSysConsts.TRNS_TP_LINK_ITEM);
                    commission.setFkReferenceId(mnAuxParamOutReferenceId > 0 ? mnAuxParamOutReferenceId : resultSetDpsEntry.getInt("de.fid_item"));
                    commission.setFkItemId(resultSetDpsEntry.getInt("de.fid_item"));

                    // Validate if dps entry have a adjustment:

                    if (resultSetDpsEntry.getDouble("f_adj_stot_ety") > 0) {

                        dCommissionAdjustmentEntry = resultSetDpsEntry.getDouble("f_adj_stot_ety") * mdAuxParamOutPercentage * -1;
                        commission.setCommissions(commission.getCommissions() + dCommissionAdjustmentEntry);
                    }

                    commission.setAuxDpsEntryItemKey(resultSetDpsEntry.getString("i.item_key"));
                    commission.setAuxDpsEntryItem(resultSetDpsEntry.getString("i.item"));
                    commission.setAuxDpsEntryQuantity(resultSetDpsEntry.getDouble("de.qty"));
                    commission.setAuxDpsEntryUnit(resultSetDpsEntry.getString("u.symbol"));
                    commission.setAuxDpsEntrySubtotal(resultSetDpsEntry.getDouble("f_entry_stot_r"));
                    commission.setAuxDpsEntryAdjustment(resultSetDpsEntry.getDouble("f_adj_stot_ety"));
                    commission.setAuxDpsEntryCommission(commission.getCommissions() - dCommissionAdjustmentEntry);
                    commission.setAuxDpsEntryCommissionAdjustment(dCommissionAdjustmentEntry);
                    commission.setAuxDpsEntryCommissionTotal(commission.getCommissions());
                    commission.setAuxDpsEntryCommissionPaid(resultSetDpsEntry.getDouble("f_paid"));
                    commission.setAuxDpsEntryCurrencyKey(resultSetDps.getString("c.cur_key"));
                    commission.setAuxCommissionsModality(isAgent ? "A" : "S");

                    // Update dps row values:

                    mktCommissionPayableRow.setAuxDpsCommission(mktCommissionPayableRow.getAuxDpsCommission() + commission.getAuxDpsEntryCommission());
                    mktCommissionPayableRow.setAuxDpsCommissionAdjustment(mktCommissionPayableRow.getAuxDpsCommissionAdjustment() + commission.getAuxDpsEntryCommissionAdjustment());
                    mktCommissionPayableRow.setAuxDpsCommissionTotal(mktCommissionPayableRow.getAuxDpsCommission() + mktCommissionPayableRow.getAuxDpsCommissionAdjustment());
                    mktCommissionPayableRow.setAuxDpsCommissionPaid(mktCommissionPayableRow.getAuxDpsCommissionPaid() + commission.getAuxDpsEntryCommissionPaid());

                    mktCommissionPayableRow.getMktCommisionsEntryPayableRow().add(commission);

                }
            }

            if (mktCommissionPayableRow.getMktCommisionsEntryPayableRow().size() > 0) {
                maMktCommissionsPayableRows.add(mktCommissionPayableRow);
            }
        }

        moCommissionPayment = new SDbCommissionPayment();
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        SMktCommissionsPayableRow mktCommissionPayableRow = null;
        SDbCommissionLog log = null;

        Statement statement = session.getDatabase().getConnection().createStatement();

        // Delete commisions:

        for (int i=0; i<maMktCommissionsPayableRows.size(); i++) {

            mktCommissionPayableRow = maMktCommissionsPayableRows.get(i);
            for (SDbCommission comms : mktCommissionPayableRow.getMktCommisionsEntryPayableRow()) {
                if (comms.canDelete(session)) {

                    msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.MKT_COMMS) + " WHERE id_year = " + comms.getPkYearId() + " AND id_doc = " + comms.getPkDocId() + " AND " +
                            "id_ety = " + comms.getPkEntryId() + " AND id_sal_agt = " + comms.getPkSalesAgentId() + ";";
                    statement.execute(msSql);
                }
                else {
                    throw new Exception(comms.getQueryResult());
                }
            }
        }

        for (int i=0; i<maMktCommissionsPayedRows.size(); i++) {

            mktCommissionPayableRow = maMktCommissionsPayedRows.get(i);
            for (SDbCommission comms : mktCommissionPayableRow.getMktCommisionsEntryPayableRow()) {
                if (comms.canDelete(session)) {

                    msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.MKT_COMMS) + " WHERE id_year = " + comms.getPkYearId() + " AND id_doc = " + comms.getPkDocId() + " AND " +
                            "id_ety = " + comms.getPkEntryId() + " AND id_sal_agt = " + comms.getPkSalesAgentId() + ";";
                    statement.execute(msSql);
                }
                else {
                    throw new Exception(comms.getQueryResult());
                }
            }
        }

        // Create commission log:

        log = new SDbCommissionLog();
        log.setDateStart(mtDateStart);
        log.setDateEnd(mtDateEnd);
        log.save(session);

        // Save commissions:

        for (int i=0; i<maMktCommissionsPayableRows.size(); i++) {
            mktCommissionPayableRow = maMktCommissionsPayableRows.get(i);
            for (SDbCommission comms : mktCommissionPayableRow.getMktCommisionsEntryPayableRow()) {

                comms.setFkLogId(log.getPkLogId());
                if (comms.canSave(session)) {

                    comms.save(session);
                    if (comms.getQueryResultId() != SDbConsts.SAVE_OK) {

                        deleteCommissionsCalculated(session, log.getPkLogId());
                        throw new Exception(comms.getQueryResult());
                    }
                }
                else {

                    deleteCommissionsCalculated(session, log.getPkLogId());
                    throw new Exception(comms.getQueryResult());
                }
            }
        }

        // Save payment:

        if (moCommissionPayment.canSave(session)) {

            moCommissionPayment.save(session);
            if (moCommissionPayment.getQueryResultId() != SDbConsts.SAVE_OK) {

                deleteCommissionsCalculated(session, log.getPkLogId());
                throw new Exception(moCommissionPayment.getQueryResult());
            }
        }
        else {

            deleteCommissionsCalculated(session, log.getPkLogId());
            throw new Exception(moCommissionPayment.getQueryResult());
        }

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SMktCommissionCalculate clone() throws CloneNotSupportedException {
        SMktCommissionCalculate registry = new SMktCommissionCalculate();

        registry.setBizPartnerId(this.getBizPartnerId());
        registry.setDateCommissionsCalcuate(this.getDateCommissionsCalcuate());
        registry.setDateStart(this.getDateStart());
        registry.setDateEnd(this.getDateEnd());

        registry.setAuxParamInBizPartnerId(this.getAuxParamInBizPartnerId());
        registry.setAuxParamInBizPartnerTypeId(this.getAuxParamInBizPartnerTypeId());
        registry.setAuxParamInItemId(this.getAuxParamInItemId());
        registry.setAuxParamInDate(this.getAuxParamInDate());
        registry.setAuxParamOutLinkTypeId(this.getAuxParamOutLinkTypeId());
        registry.setAuxParamOutReferenceId(this.getAuxParamOutReferenceId());
        registry.setAuxParamOutPercentage(this.getAuxParamOutPercentage());
        registry.setAuxParamOutValueUnitary(this.getAuxParamOutValueUnitary());
        registry.setAuxParamOutValue(this.getAuxParamOutValue());
        registry.setAuxParamOutCommissionTypeSource(this.getAuxParamOutCommissionTypeSource());
        registry.setAuxParamOutResult(this.getAuxParamOutResult());

        registry.setCommissionPayment(this.getCommissionPayment().clone());

        for (SMktCommissionsPayableRow row : this.getMktCommissionsPayableRows()) {
            registry.getMktCommissionsPayableRows().add(row);
        }

        for (SMktCommissionsPayableRow row : this.getMktCommissionsPayedRows()) {
            registry.getMktCommissionsPayedRows().add(row);
        }

        return registry;
    }
}
