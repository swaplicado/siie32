/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.data;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Vector;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Néstor Ávalos
 */

public class SDataCostEmployee extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkYearId;
    protected int mnPkTypeCostObjectId;
    protected int mnPkRefCompanyBranchId;
    protected int mnPkRefReferenceId;
    protected int mnPkRefEntityId;
    protected int mnPkBizPartnerId;
    protected java.util.Date mtPkDateId;
    protected int mnPkTypeHourId;
    protected double mdQuantity;
    protected boolean mbIsAccounting;
    protected boolean mbIsDeleted;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected int mnDbmsType;
    protected java.lang.String msDbmsBizPartner;
    protected java.util.Date mtDbmsDateStart;
    protected java.util.Date mtDbmsDateEnd;
    
    private java.util.Vector<erp.mmfg.data.SDataCostEmployee> mvDbmsCost;

    public SDataCostEmployee() {
        super(SDataConstants.MFG_COST);

        mvDbmsCost = new Vector<SDataCostEmployee>();

        reset();
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkTypeCostObjectId(int n) { mnPkTypeCostObjectId = n; }
    public void setPkRefCompanyBranchId(int n) { mnPkRefCompanyBranchId = n; }
    public void setPkRefReferenceId(int n) { mnPkRefReferenceId = n; }
    public void setPkRefEntityId(int n) { mnPkRefEntityId = n; }
    public void setPkBizPartnerId(int n) { mnPkBizPartnerId = n; }
    public void setPkDateId(java.util.Date t) { mtPkDateId = t; }
    public void setPkTypeHourId(int n) { mnPkTypeHourId = n; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setIsAccounting(boolean b) { mbIsAccounting = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkTypeCostObjectId() { return mnPkTypeCostObjectId; }
    public int getPkRefCompanyBranchId() { return mnPkRefCompanyBranchId; }
    public int getPkRefReferenceId() { return mnPkRefReferenceId; }
    public int getPkRefEntityId() { return mnPkRefEntityId; }
    public int getPkBizPartnerId() { return mnPkBizPartnerId; }
    public java.util.Date getPkDateId() { return mtPkDateId; }
    public int getPkTypeHourId() { return mnPkTypeHourId; }
    public double getQuantity() { return mdQuantity; }
    public boolean getIsAccounting() { return mbIsAccounting; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setDbmsBizPartner(java.lang.String s) { msDbmsBizPartner = s; }
    public void setDbmsDateStart(java.util.Date t) { mtDbmsDateStart = t; }
    public void setDbmsDateEnd(java.util.Date t) { mtDbmsDateEnd = t; }

    public java.util.Date getDbmsDateStart() { return mtDbmsDateStart; }
    public java.util.Date getDbmsDateEnd() { return mtDbmsDateEnd; }
    public java.lang.String getDbmsBizPartner() { return msDbmsBizPartner; }

    public java.util.Vector<SDataCostEmployee> getDbmsCost() { return mvDbmsCost; }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkYearId = 0;
        mnPkTypeCostObjectId = 0;
        mnPkRefCompanyBranchId = 0;
        mnPkRefReferenceId = 0;
        mnPkRefEntityId = 0;
        mnPkBizPartnerId = 0;
        mtPkDateId = null;
        mnPkTypeHourId = 0;
        mdQuantity = 0;
        mbIsAccounting = false;
        mbIsDeleted = false;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        mtDbmsDateStart = null;
        mtDbmsDateEnd = null;
        msDbmsBizPartner = "";
        mvDbmsCost.clear();
    }
    
    @Override
    public void setPrimaryKey(java.lang.Object key) {

        /****
         * This application has a different form for save and read registry,
         * the registry is read for range of dates (start date - end date)
         ****/

        mnPkYearId = ((int[]) key)[0];
        mnPkTypeCostObjectId = ((int[]) key)[1];
        mnPkRefCompanyBranchId = ((int[]) key)[2];
        mnPkRefReferenceId = ((int[]) key)[3];
        mnPkRefEntityId = ((int[]) key)[4];
        mtDbmsDateStart = ((java.util.Date[]) key)[5];
        mtDbmsDateEnd = ((java.util.Date[]) key)[6];
    }

    @Override
    public java.lang.Object[] getPrimaryKey() {
        return new java.lang.Object[] { mnPkYearId, mnPkTypeCostObjectId, mnPkRefCompanyBranchId, mnPkRefReferenceId, mnPkRefEntityId, mnPkBizPartnerId, mtPkDateId, mnPkTypeHourId };
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        Object[] key = (Object[]) pk;
        String sql = "";

        SDataCostEmployee oCost = null;
        ResultSet resultSet = null;
        
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            /*sql = "SELECT c.* " +
                "FROM mfg_cost AS c " +
                "WHERE c.id_year = " + key[0] + " AND c.id_tp_cost_obj = " + key[1] + " AND c.id_bp = " + key[2] + " AND c.id_dt = '" + key[4] + "' AND c.id_tp_hr = " + key[5];
                */

            System.out.println("ANTES DE SQL");
            sql = "SELECT bp.bp, b.bpb, t.tp_cost_obj, " +
                ((Integer)((Object[]) key)[1] == SDataConstantsSys.MFGS_TP_COST_ORD ? "o.ref " :
                 (Integer)((Object[]) key)[1] == SDataConstantsSys.MFGS_TP_COST_LINE ? "l.line " :
                 (Integer)((Object[]) key)[1] == SDataConstantsSys.MFGS_TP_COST_PLT ? "e.ent " : "b.bpb ") +
                "AS f_cost_obj, c.* " +
                "FROM mfg_cost AS c " +
                "INNER JOIN erp.mfgs_tp_cost_obj AS t ON c.id_tp_cost_obj = t.id_tp_cost_obj " +
                "INNER JOIN erp.bpsu_bp AS bp ON c.id_bp = bp.id_bp " +
                "INNER JOIN erp.bpsu_bpb AS b ON c.id_ref_cob = b.id_bpb " +
                ((Integer)((Object[]) key)[1] == SDataConstantsSys.MFGS_TP_COST_ORD ? "INNER JOIN mfg_ord AS o ON c.id_ref_ref = o.id_year AND c.id_ref_ent = o.id_ord " :
                 (Integer)((Object[]) key)[1] == SDataConstantsSys.MFGS_TP_COST_LINE ? "INNER JOIN mfg_line AS l ON c.id_ref_cob = l.id_cob AND c.id_ref_ref = l.id_ent AND c.id_ref_ent = l.id_line " :
                 (Integer)((Object[]) key)[1] == SDataConstantsSys.MFGS_TP_COST_PLT ? "INNER JOIN erp.cfgu_cob_ent AS e ON c.id_ref_ref = e.id_cob AND c.id_ref_ent = e.id_ent " : "") +
                "WHERE " +
                "c.id_year = " + (Integer)((Object[]) key)[0] +
                " AND c.id_tp_cost_obj = " + (Integer)((Object[]) key)[1] +
                " AND c.id_ref_cob = " + (Integer)((Object[]) key)[2] +
                " AND c.id_ref_ref = " + (Integer)((Object[]) key)[3] +
                " AND c.id_ref_ent = " + (Integer)((Object[]) key)[4] +
                " AND c.id_dt BETWEEN '" + (java.util.Date)((Object[]) key)[5] + "' AND '" + (java.util.Date)((Object[]) key)[6] + "' " +
                " AND c.b_del = 0 " +
                // "GROUP BY c.id_year, c.id_tp_cost_obj, c.fid_cob, c.fid_cob_n, c.fid_ent_n, c.fid_ord_year_n, c.fid_ord_ord_n, c.fid_line_cob_n, c.fid_ent_n, c.fid_line_line_n " +
                "ORDER BY bp.bp, c.id_year, c.id_tp_cost_obj, c.id_ref_cob, c.id_ref_ref, c.id_ref_ent, c.id_bp, c.id_dt, c.id_tp_hr ";

            mtDbmsDateStart = (java.util.Date)((Object[]) key)[5];
            mtDbmsDateEnd = (java.util.Date)((Object[]) key)[6];

            System.out.println("sql: " + sql);

            resultSet = statement.executeQuery(sql);
            mvDbmsCost.removeAllElements();
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkYearId = resultSet.getInt("c.id_year");
                mnPkTypeCostObjectId = resultSet.getInt("c.id_tp_cost_obj");
                mnPkRefCompanyBranchId = resultSet.getInt("c.id_ref_cob");
                mnPkRefReferenceId = resultSet.getInt("c.id_ref_ref");
                mnPkRefEntityId = resultSet.getInt("c.id_ref_ent");
                mnPkBizPartnerId = resultSet.getInt("c.id_bp");
                mtPkDateId = resultSet.getDate("c.id_dt");
                mnPkTypeHourId = resultSet.getInt("c.id_tp_hr");
                mdQuantity = resultSet.getDouble("c.qty");
                mbIsAccounting = resultSet.getBoolean("c.b_acc");
                mbIsDeleted = resultSet.getBoolean("c.b_del");
                mnFkUserNewId = resultSet.getInt("c.fid_usr_new");
                mnFkUserEditId = resultSet.getInt("c.fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("c.fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("c.ts_new");
                mtUserEditTs = resultSet.getTimestamp("c.ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("c.ts_del");

                resultSet.beforeFirst();
                while (resultSet.next()) {
                    oCost = new SDataCostEmployee();

                    oCost.setPkYearId(resultSet.getInt("c.id_year"));
                    oCost.setPkTypeCostObjectId(resultSet.getInt("c.id_tp_cost_obj"));
                    oCost.setPkRefCompanyBranchId(resultSet.getInt("c.id_ref_cob"));
                    oCost.setPkRefReferenceId(resultSet.getInt("c.id_ref_ref"));
                    oCost.setPkRefEntityId(resultSet.getInt("c.id_ref_ent"));
                    oCost.setPkBizPartnerId(resultSet.getInt("c.id_bp"));
                    oCost.setPkDateId(resultSet.getDate("c.id_dt"));
                    oCost.setPkTypeHourId(resultSet.getInt("c.id_tp_hr"));
                    oCost.setQuantity(resultSet.getDouble("c.qty"));
                    oCost.setIsAccounting(resultSet.getBoolean("c.b_acc"));
                    oCost.setIsDeleted(resultSet.getBoolean("c.b_del"));
                    oCost.setFkUserNewId(resultSet.getInt("c.fid_usr_new"));
                    oCost.setFkUserEditId(resultSet.getInt("c.fid_usr_edit"));
                    oCost.setFkUserDeleteId(resultSet.getInt("c.fid_usr_del"));
                    oCost.setUserNewTs(resultSet.getTimestamp("c.ts_new"));
                    oCost.setUserEditTs(resultSet.getTimestamp("c.ts_edit"));
                    oCost.setUserDeleteTs(resultSet.getTimestamp("c.ts_del"));

                    oCost.setDbmsBizPartner(resultSet.getString("bp.bp"));

                    mvDbmsCost.add(oCost);
                }

                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
            }
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public int save(java.sql.Connection connection) {
        int i=0;
        int nParam=1;

        CallableStatement callableStatement = null;
        SDataCostEmployee oCost = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            for (i=0; i<mvDbmsCost.size(); i++) {

                oCost = mvDbmsCost.get(i);
                nParam=1;
                callableStatement = connection.prepareCall(
                        "{ CALL mfg_cost_save(" +
                            "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                            "?, ?, ?, ?) }");
                callableStatement.setInt(nParam++, oCost.getPkYearId());
                callableStatement.setInt(nParam++, oCost.getPkTypeCostObjectId());
                callableStatement.setInt(nParam++, oCost.getPkRefCompanyBranchId());
                callableStatement.setInt(nParam++, oCost.getPkRefReferenceId());
                callableStatement.setInt(nParam++, oCost.getPkRefEntityId());
                callableStatement.setInt(nParam++, oCost.getPkBizPartnerId());
                callableStatement.setDate(nParam++, new java.sql.Date(oCost.getPkDateId().getTime()));
                callableStatement.setInt(nParam++, oCost.getPkTypeHourId());
                callableStatement.setDouble(nParam++, oCost.getQuantity());
                callableStatement.setBoolean(nParam++, oCost.getIsAccounting());
                callableStatement.setBoolean(nParam++, oCost.getIsDeleted());
                callableStatement.setInt(nParam++, mbIsRegistryNew ? oCost.getFkUserNewId() : oCost.getFkUserEditId());
                callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
                callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
                callableStatement.execute();

                mnDbmsErrorId = callableStatement.getInt(nParam - 2);
                msDbmsError = callableStatement.getString(nParam - 1);
            }

            mbIsRegistryNew = false;
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public int delete(java.sql.Connection connection) {        
        return mnLastDbActionResult;
    }

    @Override
    public Date getLastDbUpdate() {
        return null;
    }
}
