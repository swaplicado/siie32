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
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Néstor Ávalos
 */
public class SRowShipmentDiogSupply extends SDbRegistry implements SGridRow {

    protected int mnPkYearId;
    protected int mnPkDocId;
    protected Date mtDate;
    protected String msNumber;
    protected String msWarehouseBranch;
    protected String msWarehouseBranchCode;
    protected String msWarehouse;
    protected String msWarehouseCode;

    protected ArrayList<SRowShipmentDiogSupply> maRowsShipmentDiogSupply;

    public SRowShipmentDiogSupply() {
        super(SModConsts.LOGX_SHIP_DIOG);
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setNumber(String s) { msNumber = s; }
    public void setWarehouseBranch(String s) { msWarehouseBranch = s; }
    public void setWarehouseBranchCode(String s) { msWarehouseBranchCode = s; }
    public void setWarehouse(String s) { msWarehouse = s; }
    public void setWarehouseCode(String s) { msWarehouseCode = s; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public Date getDate() { return mtDate; }
    public String getNumber() { return msNumber; }
    public String getWarehouseBranch() { return msWarehouseBranch; }
    public String getWarehouseBranchCode() { return msWarehouseBranchCode; }
    public String getWarehouse() { return msWarehouse; }
    public String getWarehouseCode() { return msWarehouseCode; }

    public ArrayList<SRowShipmentDiogSupply> getRowShipmentDiogSupply() { return maRowsShipmentDiogSupply; }

    public void getRows(SGuiSession session, int nParamFkShipmentCobId, int[] nParamFkEntityId) throws SQLException {
        SRowShipmentDiogSupply row = null;

        ResultSet resultSet = null;
        Statement statement = null;

        /*
         msSql = "SELECT "
            + "ge.id_year, "
            + "ge.id_doc, "
            + "ge.id_ety, "
            + "CONCAT(g.num_ser, IF(LENGTH(g.num_ser) = 0, '', '-'), erp.lib_fix_int(g.num, " + SDataConstantsSys.NUM_LEN_IOG + ")) AS " + SDbConsts.FIELD_NAME + ", "
            + "g.dt, "
            + "bpb.bpb AS f_bpb, "
            + "bpb.code AS f_bpb_code, "
            + "ent.ent AS f_ent, "
            + "ent.code AS f_ent_code, "
            + "i.item_key, "
            + "i.item, "
            + "u.symbol AS f_unit, "
            + "g.b_ship AS f_ship, "
            + "ge.qty AS f_qty, "
            + "ge.orig_qty AS f_orig_qty, "
            + "COALESCE(SUM(de.qty), 0) AS f_ship_qty, "
            + "g.fid_usr_ship AS " + SDbConsts.FIELD_USER_INS_ID + ", "
            + "g.ts_ship AS " + SDbConsts.FIELD_USER_INS_TS + ", "
            + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + " "
            + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS g "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS ge ON g.id_year = ge.id_year AND g.id_doc = ge.id_doc "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS bpb ON g.fid_cob = bpb.id_bpb "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_COB_ENT) + " AS ent ON g.fid_cob = ent.id_cob AND g.fid_wh = ent.id_ent "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON ge.fid_item = i.id_item "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS u ON ge.fid_unit = u.id_unit "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON g.fid_usr_ship = ui.id_usr "
            + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP_DEST_ETY) + " AS de ON ge.id_year = de.fk_diog_year_n AND ge.id_doc = de.fk_diog_doc_n AND ge.id_ety = de.fk_diog_ety_n "
            + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP_DEST) + " AS d ON d.id_ship = de.id_ship AND d.id_dest = de.id_dest "
            + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP) + " AS s ON s.id_ship = d.id_ship "
            + "WHERE g.b_del = 0 AND ge.b_del = 0 AND g.b_ship_req = 1 AND "
            + "g.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_INT_TRA[0] + " AND g.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_INT_TRA[1] + " AND g.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_INT_TRA[2] + " AND "
            + "g.fid_cob = " + nParamFkShipmentCobId + " "
            + "GROUP BY ge.id_year, ge.id_doc ";
         */

         msSql = "SELECT " + SDbConsts.FIELD_ID + "1, " + SDbConsts.FIELD_ID + "2, '', "
                    + SDbConsts.FIELD_NAME + ", " + SDbConsts.FIELD_DATE + ", "
                    + "'' AS " + SDbConsts.FIELD_CODE + ", "
                    + "f_bpb, "
                    + "f_bpb_code, "
                    + "f_ent, "
                    + "f_ent_code, "
                    + "f_comp1, " + SDbConsts.FIELD_USER_INS_ID + ", " + SDbConsts.FIELD_USER_INS_TS + ", " + SDbConsts.FIELD_USER_INS_NAME + ", "
                    + "SUM(f_qty) AS f_qty, "
                    + "SUM(f_orig_qty) AS f_orig_qty, "
                    + "COALESCE(SUM(f_ship_qty), 0) AS f_ship_qty "
                    + "FROM (";

         msSql += "SELECT "
            + "ge.id_year AS " + SDbConsts.FIELD_ID + "1, "
            + "ge.id_doc AS " + SDbConsts.FIELD_ID + "2, "
            + "ge.id_ety, "
            + "'' AS " + SDbConsts.FIELD_CODE + ", "
            + "CONCAT(g.num_ser, IF(LENGTH(g.num_ser) = 0, '', '-'), erp.lib_fix_int(g.num, " + SDataConstantsSys.NUM_LEN_IOG + ")) AS " + SDbConsts.FIELD_NAME + ", "
            + "g.dt AS " + SDbConsts.FIELD_DATE + ", "
            + "bpb.bpb AS f_bpb, "
            + "bpb.code AS f_bpb_code, "
            + "ent.ent AS f_ent, "
            + "ent.code AS f_ent_code, "
            + "i.item_key, "
            + "i.item, "
            + "u.symbol AS f_unit, "
            + "g.b_ship AS " + SDbConsts.FIELD_COMP + "1, "
            + "ge.qty AS f_qty, "
            + "ge.orig_qty AS f_orig_qty, "
            + "COALESCE(SUM(de.qty), 0) AS f_ship_qty, "
            + "g.fid_usr_ship AS " + SDbConsts.FIELD_USER_INS_ID + ", "
            + "g.ts_ship AS " + SDbConsts.FIELD_USER_INS_TS + ", "
            + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + " "
            + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS g "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS ge ON g.id_year = ge.id_year AND g.id_doc = ge.id_doc "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS bpb ON g.fid_cob = bpb.id_bpb "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_COB_ENT) + " AS ent ON g.fid_cob = ent.id_cob AND g.fid_wh = ent.id_ent "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON ge.fid_item = i.id_item "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS u ON ge.fid_unit = u.id_unit "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON g.fid_usr_ship = ui.id_usr "
            + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP_DEST_ETY) + " AS de ON ge.id_year = de.fk_diog_year_n AND ge.id_doc = de.fk_diog_doc_n AND ge.id_ety = de.fk_diog_ety_n AND de.id_ship = (SELECT id_ship FROM " +
                 SModConsts.TablesMap.get(SModConsts.LOG_SHIP) + " WHERE de.id_ship = id_ship AND b_del = 0) "
            + "WHERE g.b_del = 0 AND ge.b_del = 0 AND g.b_ship_req = 1 AND "
            + "g.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_INT_TRA[0] + " AND g.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_INT_TRA[1] + " AND g.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_INT_TRA[2] + " AND "
            + "g.fid_cob = " + nParamFkShipmentCobId + (nParamFkEntityId != null ? " AND g.fid_cob = " + nParamFkEntityId[0] +
                 " AND g.fid_wh = " + nParamFkEntityId[1] : "")  + " "
            //+ (sql.isEmpty() ? "" : "AND " + sql)
            + "GROUP BY " + SDbConsts.FIELD_ID + "1, " + SDbConsts.FIELD_ID + "2, ge.id_ety, g.dt, g.num_ser, g.num, ge.fid_item, ge.fid_unit, i.item_key, i.item, u.symbol, ge.qty, ge.orig_qty ";

        msSql += ") AS DIOG_ETY_TMP " +  // derived table
                    "GROUP BY " + SDbConsts.FIELD_ID + "1, " + SDbConsts.FIELD_ID + "2, " + SDbConsts.FIELD_NAME + ", " + SDbConsts.FIELD_DATE + " ";

        msSql += "HAVING (f_orig_qty - f_ship_qty) <> 0 AND f_comp1 = 0 ";

        msSql += "ORDER BY " + SDbConsts.FIELD_NAME + ", " + SDbConsts.FIELD_DATE + ", " + SDbConsts.FIELD_ID + "1, " + SDbConsts.FIELD_ID + "2 ";

        statement = session.getDatabase().getConnection().createStatement();
        resultSet = statement.executeQuery(msSql);

        while (resultSet.next()) {

            row = new SRowShipmentDiogSupply();

            row.setPkYearId(resultSet.getInt(SDbConsts.FIELD_ID + "1"));
            row.setPkDocId(resultSet.getInt(SDbConsts.FIELD_ID + "2"));
            row.setDate(resultSet.getDate(SDbConsts.FIELD_DATE));
            row.setNumber(resultSet.getString(SDbConsts.FIELD_NAME));
            row.setWarehouseBranch(resultSet.getString("f_bpb"));
            row.setWarehouseBranchCode(resultSet.getString("f_bpb_code"));
            row.setWarehouse(resultSet.getString("f_ent"));
            row.setWarehouseCode(resultSet.getString("f_ent_code"));

            maRowsShipmentDiogSupply.add(row);
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
        msNumber = "";
        msWarehouseBranch = "";
        msWarehouseBranchCode = "";
        msWarehouse = "";
        msWarehouseCode = "";

        maRowsShipmentDiogSupply = new ArrayList<SRowShipmentDiogSupply>();
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
    public SRowShipmentDiogSupply clone() throws CloneNotSupportedException {
        SRowShipmentDiogSupply registry = new SRowShipmentDiogSupply();

        registry.setPkYearId(this.getPkYearId());
        registry.setPkDocId(this.getPkDocId());
        registry.setDate(this.getDate());
        registry.setNumber(this.getNumber());
        registry.setWarehouseBranch(this.getWarehouseBranch());
        registry.setWarehouseBranchCode(this.getWarehouseBranchCode());
        registry.setWarehouse(this.getWarehouse());
        registry.setWarehouseCode(this.getWarehouseCode());

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
                value = msNumber;
                break;
            case 1:
                value = mtDate;
                break;
            case 2:
                value = msWarehouseBranch;
                break;
            case 3:
                value = msWarehouseBranchCode;
                break;
            case 4:
                value = msWarehouse;
                break;
            case 5:
                value = msWarehouseCode;
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
            default:
        }
    }
}
