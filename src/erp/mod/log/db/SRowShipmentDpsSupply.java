/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.log.db;

import erp.data.SDataConstantsSys;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Néstor Ávalos
 */
public class SRowShipmentDpsSupply extends SDbRegistry implements SGridRow {

    protected int mnPkYearId;
    protected int mnPkDocId;
    protected Date mtDate;
    protected String msDocType;
    protected String msNumber;
    protected String msReference;
    protected String msBizPartner;
    protected String msBizPartnerCode;
    protected double mdTotal;
    protected String msCurrency;
    protected int mnShipments;

    protected ArrayList<SRowShipmentDpsSupply> maRowsShipmentDpsSupply;

    public SRowShipmentDpsSupply() {
        super(SModConsts.LOGX_SHIP_DPS);
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setDocType(String s) { msDocType = s; }
    public void setNumber(String s) { msNumber = s; }
    public void setReference(String s) { msReference = s; }
    public void setBizPartner(String s) { msBizPartner = s; }
    public void setBizPartnerCode(String s) { msBizPartnerCode = s; }
    public void setTotal(double d) { mdTotal = d; }
    public void setCurrency(String s) { msCurrency = s; }
    public void setShipments(int n) { mnShipments = n; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public Date getDate() { return mtDate; }
    public String getDocType() { return msDocType; }
    public String getNumber() { return msNumber; }
    public String getReference() { return msReference; }
    public String getBizPartner() { return msBizPartner; }
    public String getBizPartnerCode() { return msBizPartnerCode; }
    public double getTotal() { return mdTotal; }
    public String getCurrency() { return msCurrency; }
    public int getShipments() { return mnShipments; }

    public ArrayList<SRowShipmentDpsSupply> getRowShipmentDpsSupply() { return maRowsShipmentDpsSupply; }

    public void getRows(SGuiSession session, int nParamFkShipmentCobId, int mnParamFkCustomerId, int[] mnParamFkAddressId, int[] nParamFkDpsTypeId) throws SQLException {
        String sqlOrderByDoc = "";
        SRowShipmentDpsSupply row = null;

        ResultSet resultSet = null;
        Statement statement = null;

        sqlOrderByDoc += "num_ser, CAST(num AS UNSIGNED INTEGER), num, f_dt_code, dt, bp_key, bp, id_bp ";

        msSql = "SELECT " + SDbConsts.FIELD_ID + "1, " + SDbConsts.FIELD_ID + "2, " +
                "dt, num_ser, num, num_ref, tot_r, tot_cur_r, b_ship, cur_key, " + SDbConsts.FIELD_NAME + ", " +
                "'' AS " + SDbConsts.FIELD_CODE + ", f_dt_code, f_cb_code, id_bp, bp, bp_key, bpb, " +
                "SUM(f_orig_qty) AS f_orig_qty, " +

                (SLibUtils.compareKeys(SModSysConsts.TRNS_CL_DPS_SAL_ADJ, new int[] { nParamFkDpsTypeId[0], nParamFkDpsTypeId[1] }) ? "" :
                "COALESCE(SUM(f_adj_orig_qty), 0) AS f_adj_orig_qty, ") +

                "COALESCE(SUM(f_ship_qty), 0) AS f_ship_qty, " +
                SDbConsts.FIELD_USER_INS_ID + ", " + SDbConsts.FIELD_USER_INS_TS + ", " + SDbConsts.FIELD_USER_INS_NAME + ", " +
                SDbConsts.FIELD_USER_UPD_ID + ", " + SDbConsts.FIELD_USER_UPD_TS + ", " + SDbConsts.FIELD_USER_UPD_NAME + " " +
                "FROM (";

        msSql += "SELECT de.id_year AS " + SDbConsts.FIELD_ID + "1, de.id_doc AS " + SDbConsts.FIELD_ID + "2, de.id_ety, " +
                "d.dt, d.num_ser, d.num, d.num_ref, d.tot_r, d.tot_cur_r, d.b_ship, c.cur_key, " +
                "CONCAT(d.num_ser, IF(length(d.num_ser) = 0, '', '-'), d.num) AS " + SDbConsts.FIELD_NAME + ", " +
                "'' AS " + SDbConsts.FIELD_CODE + ", dt.code AS f_dt_code, cb.code AS f_cb_code, b.id_bp, b.bp, bc.bp_key, bb.bpb, " +
                "de.fid_item, de.fid_unit, de.fid_orig_unit, i.item_key, i.item, u.symbol AS f_unit, uo.symbol AS f_orig_unit, " +
                "de.orig_qty AS f_orig_qty, " +

                (SLibUtils.compareKeys(SModSysConsts.TRNS_CL_DPS_SAL_ADJ, new int[] { nParamFkDpsTypeId[0], nParamFkDpsTypeId[1] })  ? "" :
                "COALESCE((SELECT SUM(ddd.orig_qty) FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_DPS_ADJ) + " AS ddd, trn_dps_ety AS dae, trn_dps AS da WHERE " +
                "ddd.id_dps_year = de.id_year AND ddd.id_dps_doc = de.id_doc AND ddd.id_dps_ety = de.id_ety AND " +
                "ddd.id_adj_year = dae.id_year AND ddd.id_adj_doc = dae.id_doc AND ddd.id_adj_ety = dae.id_ety AND " +
                "dae.id_year = da.id_year AND dae.id_doc = da.id_doc AND " +
                "dae.b_del = 0 AND dae.fid_tp_dps_adj = " + SDataConstantsSys.TRNS_TP_DPS_ADJ_RET + " AND " +
                "da.b_del = 0 AND da.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + "), 0) AS f_adj_orig_qty, ") +

                "COALESCE((SELECT SUM(sde.qty) FROM " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP_DEST_ETY) + " AS sde, " +
                SModConsts.TablesMap.get(SModConsts.LOG_SHIP_DEST) + " AS sd, " +
                SModConsts.TablesMap.get(SModConsts.LOG_SHIP) + " AS s " +
                "WHERE sde.fk_dps_year_n = de.id_year AND sde.fk_dps_doc_n = de.id_doc AND sde.fk_dps_ety_n = de.id_ety AND " +
                "sde.id_ship = sd.id_ship AND sde.id_dest = sd.id_dest AND sd.id_ship = s.id_ship AND s.b_del = 0), 0) AS f_ship_qty, " +
                "0 AS " + SDbConsts.FIELD_USER_INS_ID + ", " +
                "d.ts_new AS " + SDbConsts.FIELD_USER_INS_TS + ", " +
                "'' AS " + SDbConsts.FIELD_USER_INS_NAME + ", "   +
                "0 AS " + SDbConsts.FIELD_USER_UPD_ID + ", " +
                "d.ts_edit AS " + SDbConsts.FIELD_USER_UPD_TS + ", " +
                "'' AS " + SDbConsts.FIELD_USER_UPD_NAME + " " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNU_TP_DPS) + " AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps AND " +
                "d.b_del = 0 AND d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " AND " +
                "d.fid_ct_dps = " + nParamFkDpsTypeId[0] + " AND d.fid_cl_dps = " + nParamFkDpsTypeId[1] + " " + // sqlFilter +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS c ON d.fid_cur = c.id_cur " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS cb ON d.fid_cob = cb.id_bpb " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b ON d.fid_bp_r = b.id_bp " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP_CT) + " AS bc ON d.fid_bp_r = bc.id_bp AND bc.id_ct_bp = " + SModSysConsts.BPSS_CT_BP_CUS + " " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS bb ON d.fid_bpb = bb.id_bpb " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc AND " +
                "de.b_del = 0 AND de.b_inv = 1 AND de.qty > 0 AND de.orig_qty > 0 " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON de.fid_item = i.id_item AND i.b_inv = 1 " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS u ON de.fid_unit = u.id_unit " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS uo ON de.fid_orig_unit = uo.id_unit " +
                "WHERE d.fid_cob = " + nParamFkShipmentCobId + (mnParamFkCustomerId > 0 ? " AND d.fid_bp_r = " + mnParamFkCustomerId +
                " AND d.fid_bpb = " + mnParamFkAddressId[0] + " AND d.fid_add = " + mnParamFkAddressId[1] : "") + " " +
                "GROUP BY " + SDbConsts.FIELD_ID + "1, " + SDbConsts.FIELD_ID + "2, de.id_ety, " +
                "d.dt, d.num_ser, d.num, d.num_ref, d.tot_r, d.tot_cur_r, d.b_ship, c.cur_key, " +
                "dt.code, cb.code, b.id_bp, b.bp, bc.bp_key, bb.bpb, " +
                "de.fid_item, de.fid_unit, de.fid_orig_unit, i.item_key, i.item, u.symbol, uo.symbol, " +
                "de.qty, de.orig_qty ";

        msSql += "HAVING (f_orig_qty " + (SLibUtils.compareKeys(SModSysConsts.TRNS_CL_DPS_SAL_ADJ, new int[] { nParamFkDpsTypeId[0], nParamFkDpsTypeId[1] }) ? "" :
                "- f_adj_orig_qty ") + " - f_ship_qty) <> 0 AND d.b_ship = 0 ";

        msSql += ") AS DPS_ETY_TMP " +  // derived table
                "GROUP BY " + SDbConsts.FIELD_ID + "1, " + SDbConsts.FIELD_ID + "2, " +
                "dt, num_ser, num, num_ref, tot_r, tot_cur_r, b_ship, cur_key, " + SDbConsts.FIELD_NAME + ", " +
                "f_dt_code, f_cb_code, id_bp, bp, bp_key, bpb " +
                "ORDER BY " + sqlOrderByDoc + "; ";

        statement = session.getDatabase().getConnection().createStatement();
        resultSet = statement.executeQuery(msSql);
        while (resultSet.next()) {

            row = new SRowShipmentDpsSupply();

            row.setPkYearId(resultSet.getInt(SDbConsts.FIELD_ID + "1"));
            row.setPkDocId(resultSet.getInt(SDbConsts.FIELD_ID + "2"));
            row.setDate(resultSet.getDate("dt"));
            row.setDocType(resultSet.getString("f_dt_code"));
            row.setNumber(resultSet.getString(SDbConsts.FIELD_NAME));
            row.setReference(resultSet.getString("num_ref"));
            row.setBizPartner(resultSet.getString("bp"));
            row.setBizPartnerCode(resultSet.getString("bp_key"));
            row.setTotal(resultSet.getDouble("tot_cur_r"));
            row.setCurrency(resultSet.getString("cur_key"));
            row.setShipments(0);

            maRowsShipmentDpsSupply.add(row);
        }
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkYearId = pk[0];
        mnPkDocId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkDocId };
    }

    @Override
    public void initRegistry() {
        mnPkYearId = 0;
        mnPkDocId = 0;
        mtDate = null;
        msDocType = "";
        msNumber = "";
        msReference = "";
        msBizPartner = "";
        msBizPartnerCode = "";
        mdTotal = 0;
        msCurrency = "";
        mnShipments = 0;

        maRowsShipmentDpsSupply = new ArrayList<SRowShipmentDpsSupply>();
    }

    @Override
    public String getSqlTable() {
        return "";
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

    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {

    }

    @Override
    public SRowShipmentDpsSupply clone() throws CloneNotSupportedException {
        SRowShipmentDpsSupply registry = new SRowShipmentDpsSupply();

        registry.setPkYearId(this.getPkYearId());
        registry.setPkDocId(this.getPkDocId());
        registry.setDate(this.getDate());
        registry.setDocType(this.getDocType());
        registry.setNumber(this.getNumber());
        registry.setReference(this.getReference());
        registry.setBizPartner(this.getBizPartner());
        registry.setBizPartnerCode(this.getBizPartnerCode());
        registry.setTotal(this.getTotal());
        registry.setCurrency(this.getCurrency());
        registry.setShipments(this.getShipments());

        return registry;
    }

    @Override
    public int[] getRowPrimaryKey() {
        return getPrimaryKey();
    }

    @Override
    public String getRowCode() {
        return "";
    }

    @Override
    public String getRowName() {
        return "";
    }

    @Override
    public boolean isRowSystem() {
        return false;
    }

    @Override
    public boolean isRowDeletable() {
        return false;
    }

    @Override
    public boolean isRowEdited() {
        return false;
    }

    @Override
    public void setRowEdited(boolean edited) {

    }

    @Override
    public Object getRowValueAt(int row) {
        Object value = null;

        switch(row) {
            case 0:
                value = msDocType;
                break;
            case 1:
                value = msNumber;
                break;
            case 2:
                value = msReference;
                break;
            case 3:
                value = mtDate;
                break;
            case 4:
                value = msBizPartner;
                break;
            case 5:
                value = msBizPartnerCode;
                break;
            case 6:
                value = mdTotal;
                break;
            case 7:
                value = msCurrency;
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int row) {
        switch(row) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            default:
        }
    }
}
