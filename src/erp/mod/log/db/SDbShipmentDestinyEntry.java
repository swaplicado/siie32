/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.log.db;

import erp.data.SDataConstantsSys;
import erp.mitm.data.SDataItem;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mtrn.data.SDataDiogEntry;
import erp.mtrn.data.SDataDpsEntry;
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
public class SDbShipmentDestinyEntry extends SDbRegistry implements SGridRow {

    protected int mnPkShipmentId;
    protected int mnPkDestinyId;
    protected int mnPkEntryId;
    protected double mdQuantity;
    protected double mdVolume;
    protected double mdMass;
    protected double mdWeightGross;
    protected double mdWeightDelivery;
    protected int mnFkItemId_r;
    protected int mnFkUnitId_r;
    protected int mnFkDpsYearId_n;
    protected int mnFkDpsDocId_n;
    protected int mnFkDpsEntryId_n;
    protected int mnFkDiogYearId_n;
    protected int mnFkDiogDocId_n;
    protected int mnFkDiogEntryId_n;

    protected Date mtXtaDoctoDate;
    protected String msXtaDoctoType;
    protected String msXtaDoctoNumber;
    protected String msXtaItem;
    protected String msXtaItemCode;
    protected String msXtaItemUnit;
    protected double mdXtaDoctoQuantity;
    protected double mdXtaDoctoQuantityAdjustment;
    protected double mdXtaShipmentQuantity;
    protected double mdXtaShipmentQuantityPending;
    protected double mdXtaSubtotal;

    protected ArrayList<SDbShipmentDestinyEntry> maShipmentDestinyEntries;

    protected SDataItem moItem;
    protected SDataDpsEntry moDpsEntry;
    protected SDataDiogEntry moDiogEntry;

    public SDbShipmentDestinyEntry() {
        super(SModConsts.LOG_SHIP_DEST_ETY);
    }

    public void setPkShipmentId(int n) { mnPkShipmentId = n; }
    public void setPkDestinyId(int n) { mnPkDestinyId = n; }
    public void setPkEntryId(int n) { mnPkEntryId = n; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setVolume(double d) { mdVolume = d; }
    public void setMass(double d) { mdMass = d; }
    public void setWeightGross(double d) { mdWeightGross = d; }
    public void setWeightDelivery(double d) { mdWeightDelivery = d; }
    public void setFkItemId_r(int n) { mnFkItemId_r = n; }
    public void setFkUnitId_r(int n) { mnFkUnitId_r = n; }
    public void setFkDpsYearId_n(int n) { mnFkDpsYearId_n = n; }
    public void setFkDpsDocId_n(int n) { mnFkDpsDocId_n = n; }
    public void setFkDpsEntryId_n(int n) { mnFkDpsEntryId_n = n; }
    public void setFkDiogYearId_n(int n) { mnFkDiogYearId_n = n; }
    public void setFkDiogDocId_n(int n) { mnFkDiogDocId_n = n; }
    public void setFkDiogEntryId_n(int n) { mnFkDiogEntryId_n = n; }

    public int getPkShipmentId() { return mnPkShipmentId; }
    public int getPkDestinyId() { return mnPkDestinyId; }
    public int getPkEntryId() { return mnPkEntryId; }
    public double getQuantity() { return mdQuantity; }
    public double getVolume() { return mdVolume; }
    public double getMass() { return mdMass; }
    public double getWeightGross() { return mdWeightGross; }
    public double getWeightDelivery() { return mdWeightDelivery; }
    public int getFkItemId_r() { return mnFkItemId_r; }
    public int getFkUnitId_r() { return mnFkUnitId_r; }
    public int getFkDpsYearId_n() { return mnFkDpsYearId_n; }
    public int getFkDpsDocId_n() { return mnFkDpsDocId_n; }
    public int getFkDpsEntryId_n() { return mnFkDpsEntryId_n; }
    public int getFkDiogYearId_n() { return mnFkDiogYearId_n; }
    public int getFkDiogDocId_n() { return mnFkDiogDocId_n; }
    public int getFkDiogEntryId_n() { return mnFkDiogEntryId_n; }

    public void setXtaDoctoDate(Date t) { mtXtaDoctoDate = t; }
    public void setXtaDoctoType(String s) { msXtaDoctoType = s; };
    public void setXtaDoctoNumber(String s) { msXtaDoctoNumber = s; }
    public void setXtaItem(String s) { msXtaItem = s; }
    public void setXtaItemCode(String s) { msXtaItemCode = s; }
    public void setXtaItemUnit(String s) { msXtaItemUnit = s; }
    public void setXtaDoctoQuantity(double d) { mdXtaDoctoQuantity = d; }
    public void setXtaDoctoQuantityAdjustment(double d) { mdXtaDoctoQuantityAdjustment = d; }
    public void setXtaShipmentQuantity(double d) { mdXtaShipmentQuantity = d; }
    public void setXtaShipmentQuantityPending(double d) { mdXtaShipmentQuantityPending = d; }
    public void setXtaDpsSubtotal(double d) { mdXtaSubtotal = d; }

    public Date getXtaDoctoDate() { return mtXtaDoctoDate; }
    public String getXtaDoctoType() { return msXtaDoctoType; };
    public String getXtaDoctoNumber() { return msXtaDoctoNumber; }
    public String getXtaItem() { return msXtaItem; }
    public String getXtaItemCode() { return msXtaItemCode; }
    public String getXtaItemUnit() { return msXtaItemUnit; }
    public double getXtaDoctoQuantity() { return mdXtaDoctoQuantity; }
    public double getXtaDoctoQuantityAdjustment() { return mdXtaDoctoQuantityAdjustment; }
    public double getXtaShipmentQuantity() { return mdXtaShipmentQuantity; }
    public double getXtaShipmentQuantityPending() { return mdXtaShipmentQuantityPending; }
    public double getXtaDpsSubtotal() { return mdXtaSubtotal; }

    public void setShipmentDestinyEntries(ArrayList<SDbShipmentDestinyEntry> v) { maShipmentDestinyEntries = v; }
    public void setItem(SDataItem o) { moItem = o; }
    public void setDpsEntry(SDataDpsEntry o) { moDpsEntry = o; }
    public void setDiogEntry(SDataDiogEntry o) { moDiogEntry = o; }

    public ArrayList<SDbShipmentDestinyEntry> getShipmentDestinyEntries() { return maShipmentDestinyEntries; }
    public SDataItem getItem() { return moItem; }
    public SDataDpsEntry getDpsEntry() { return moDpsEntry; }
    public SDataDiogEntry getDiogEntry() { return moDiogEntry; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkShipmentId = pk[0];
        mnPkDestinyId = pk[1];
        mnPkEntryId = pk[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkShipmentId, mnPkDestinyId, mnPkEntryId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkShipmentId = 0;
        mnPkDestinyId = 0;
        mnPkEntryId = 0;
        mdQuantity = 0;
        mdVolume = 0;
        mdMass = 0;
        mdWeightGross = 0;
        mdWeightDelivery = 0;
        mnFkItemId_r = 0;
        mnFkUnitId_r = 0;
        mnFkDpsYearId_n = 0;
        mnFkDpsDocId_n = 0;
        mnFkDpsEntryId_n = 0;
        mnFkDiogYearId_n = 0;
        mnFkDiogDocId_n = 0;
        mnFkDiogEntryId_n = 0;

        mtXtaDoctoDate = null;
        msXtaDoctoType = "";
        msXtaDoctoNumber = "";
        msXtaItem = "";
        msXtaItemCode = "";
        msXtaItemUnit = "";
        mdXtaDoctoQuantity = 0;
        mdXtaDoctoQuantityAdjustment = 0;
        mdXtaShipmentQuantity = 0;
        mdXtaShipmentQuantityPending = 0;
        mdXtaSubtotal = 0;

        moItem = null;
        moDpsEntry = null;
        moDiogEntry = null;

        maShipmentDestinyEntries = new ArrayList<SDbShipmentDestinyEntry>();
    }

    /*
     * Public methods:
     */

    public double computeSubtotal(SGuiSession session) {

        if (moDpsEntry == null) {

            moDpsEntry = new SDataDpsEntry();
            moDpsEntry.read(new int[] { mnFkDpsYearId_n, mnFkDpsDocId_n, mnFkDpsEntryId_n } , session.getStatement());
        }

        if (moDpsEntry != null) {

            mdXtaSubtotal = mdQuantity * moDpsEntry.getPriceUnitaryRealCy_r();
        }
        else {

            mdXtaSubtotal = 0;
        }

        return mdXtaSubtotal;
    }

    public void computeDpsEntry(SGuiSession session, final int[] anPrimaryKeyDps) throws SQLException, Exception {
        SDbShipmentDestinyEntry entry = null;

        String sqlOrderByDoc = "";
        SRowShipmentDpsSupply row = null;

        ResultSet resultSet = null;
        Statement statement = null;

        sqlOrderByDoc += "num_ser, CAST(num AS UNSIGNED INTEGER), num, f_dt_code, dt, bp_key, bp, id_bp ";

        msSql = "SELECT de.id_year, de.id_doc, de.id_ety, " +
                "d.dt, d.num_ser, d.num, d.num_ref, d.tot_r, d.tot_cur_r, d.b_ship, c.cur_key, " +
                "CONCAT(d.num_ser, IF(length(d.num_ser) = 0, '', '-'), d.num) AS f_num, " +
                "'' AS " + SDbConsts.FIELD_CODE + ", dt.code AS f_dt_code, cb.code AS f_cb_code, b.id_bp, b.bp, bc.bp_key, bb.bpb, " +
                "de.fid_item, de.fid_unit, de.fid_orig_unit, i.id_item, i.item_key, i.item, u.symbol AS f_unit, uo.symbol AS f_orig_unit, " +
                "i.vol, i.mass, i.weight_gross, i.weight_delivery, i.fid_unit, " +
                "de.orig_qty AS f_orig_qty, " +
                "COALESCE((SELECT SUM(ddd.orig_qty) FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_DPS_ADJ) + " AS ddd, trn_dps_ety AS dae, trn_dps AS da WHERE " +
                "ddd.id_dps_year = de.id_year AND ddd.id_dps_doc = de.id_doc AND ddd.id_dps_ety = de.id_ety AND " +
                "ddd.id_adj_year = dae.id_year AND ddd.id_adj_doc = dae.id_doc AND ddd.id_adj_ety = dae.id_ety AND " +
                "dae.id_year = da.id_year AND dae.id_doc = da.id_doc AND " +
                "dae.b_del = 0 AND dae.fid_tp_dps_adj = " + SDataConstantsSys.TRNS_TP_DPS_ADJ_RET + " AND " +
                "da.b_del = 0 AND da.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + "), 0) AS f_adj_orig_qty, " +
                "COALESCE((SELECT SUM(sde.qty) FROM " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP_DEST_ETY) + " AS sde, " +
                SModConsts.TablesMap.get(SModConsts.LOG_SHIP_DEST) + " AS sd, " +
                SModConsts.TablesMap.get(SModConsts.LOG_SHIP) + " AS s " +
                "WHERE sde.fk_dps_year_n = de.id_year AND sde.fk_dps_doc_n = de.id_doc AND sde.fk_dps_ety_n = de.id_ety AND " +
                "sde.id_ship = sd.id_ship AND sde.id_dest = sd.id_dest AND sd.id_ship = s.id_ship AND s.b_del = 0), 0) AS f_ship_qty " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNU_TP_DPS) + " AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps AND " +
                "d.b_del = 0 AND d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " " + // sqlFilter +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS c ON d.fid_cur = c.id_cur " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS cb ON d.fid_cob = cb.id_bpb " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b ON d.fid_bp_r = b.id_bp " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP_CT) + " AS bc ON d.fid_bp_r = bc.id_bp AND bc.id_ct_bp = " + SModSysConsts.BPSS_CT_BP_CUS + " " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS bb ON d.fid_bpb = bb.id_bpb " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc AND " +
                "de.b_del = 0 AND de.b_inv = 1 AND de.qty > 0 AND de.orig_qty > 0 " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON de.fid_item = i.id_item " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS u ON de.fid_unit = u.id_unit " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS uo ON de.fid_orig_unit = uo.id_unit " +
                "WHERE de.id_year = " + anPrimaryKeyDps[0] + " AND de.id_doc = " + anPrimaryKeyDps[1] + " " +
                (anPrimaryKeyDps.length > 2 ? "AND de.id_ety = " + anPrimaryKeyDps[2] : "") + " " +
                "GROUP BY de.id_year, de.id_doc, de.id_ety, " +
                "d.dt, d.num_ser, d.num, d.num_ref, d.tot_r, d.tot_cur_r, d.b_ship, c.cur_key, " +
                "dt.code, cb.code, b.id_bp, b.bp, bc.bp_key, bb.bpb, " +
                "de.fid_item, de.fid_unit, de.fid_orig_unit, i.item_key, i.item, u.symbol, uo.symbol, " +
                "de.qty, de.orig_qty ";

        msSql += "HAVING (f_orig_qty - f_adj_orig_qty - f_ship_qty) <> 0 OR d.b_ship = 0 " +
                "ORDER BY " + sqlOrderByDoc + "; ";

        /*
        ResultSet resultSet = null;
        Statement statement = null;

        msSql = "SELECT de.id_year, de.id_doc, de.id_ety, d.dt, tp.code, de.fid_item, CONCAT(d.num_ser, IF(LENGTH(d.num_ser), '-', ''), d.num) AS f_num, i.item, i.item_key, "
                + "u.symbol, de.qty, i.vol, i.mass, i.weight_gross, i.weight_delivery, i.id_item, i.fid_unit, "

                + "COALESCE((SELECT SUM(sde.qty) FROM " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP_DEST_ETY) + " AS sde "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP_DEST) + " AS sd ON sde.id_ship = sd.id_ship AND sde.id_dest = sd.id_dest "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP) + " AS s ON sd.id_ship = s.id_ship "
                + "WHERE s.b_del = 0 AND sde.fk_dps_year_n = de.id_year AND sde.fk_dps_doc_n = de.id_doc AND sde.fk_dps_ety_n = de.id_ety), 0) AS f_ship_qty, "

                + "(de.qty - COALESCE((SELECT SUM(sde.qty) FROM " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP_DEST_ETY) + " AS sde "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP_DEST) + " AS sd ON sde.id_ship = sd.id_ship AND sde.id_dest = sd.id_dest "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP) + " AS s ON sd.id_ship = s.id_ship "
                + "WHERE s.b_del = 0 AND sde.fk_dps_year_n = de.id_year AND sde.fk_dps_doc_n = de.id_doc AND sde.fk_dps_ety_n = de.id_ety), 0)) AS f_ship_qty_pend "

                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNU_TP_DPS) + " AS tp ON d.fid_ct_dps = tp.id_ct_dps AND d.fid_cl_dps = tp.id_cl_dps AND "
                + "d.fid_tp_dps = tp.id_tp_dps "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON de.fid_item = i.id_item "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS u ON i.fid_unit = u.id_unit "
                + "WHERE d.b_del = 0 AND de.b_del = 0 AND de.id_year = " + anPrimaryKeyDps[0] + " AND de.id_doc = " + anPrimaryKeyDps[1] + " "
                + (anPrimaryKeyDps.length > 2 ? "AND de.id_ety = " + anPrimaryKeyDps[2] : "") + " ";
        */

        statement = session.getDatabase().getConnection().createStatement();
        resultSet = statement.executeQuery(msSql);
        maShipmentDestinyEntries.clear();
        while (resultSet.next()) {

            entry = new SDbShipmentDestinyEntry();

            entry.setVolume(resultSet.getDouble("i.vol"));
            entry.setMass(resultSet.getDouble("i.mass"));
            entry.setWeightGross(resultSet.getDouble("i.weight_gross"));
            entry.setWeightDelivery(resultSet.getDouble("i.weight_delivery"));
            entry.setFkItemId_r(resultSet.getInt("i.id_item"));
            entry.setFkUnitId_r(resultSet.getInt("i.fid_unit"));
            entry.setFkDpsYearId_n(resultSet.getInt("de.id_year"));
            entry.setFkDpsDocId_n(resultSet.getInt("de.id_doc"));
            entry.setFkDpsEntryId_n(resultSet.getInt("de.id_ety"));

            entry.setXtaDoctoDate(resultSet.getDate("d.dt"));
            entry.setXtaDoctoType(resultSet.getString("f_dt_code"));
            entry.setXtaDoctoNumber(resultSet.getString("f_num"));
            entry.setXtaItem(resultSet.getString("i.item"));
            entry.setXtaItemCode(resultSet.getString("i.item_key"));
            entry.setXtaItemUnit(resultSet.getString("f_unit"));
            entry.setXtaDoctoQuantity(resultSet.getDouble("f_orig_qty"));
            entry.setXtaDoctoQuantityAdjustment(resultSet.getDouble("f_adj_orig_qty"));
            entry.setXtaShipmentQuantity(resultSet.getDouble("f_ship_qty"));
            entry.setXtaShipmentQuantityPending(resultSet.getDouble("f_orig_qty") - resultSet.getDouble("f_adj_orig_qty") - resultSet.getDouble("f_ship_qty"));
            entry.setQuantity(resultSet.getDouble("f_orig_qty") - resultSet.getDouble("f_adj_orig_qty") - resultSet.getDouble("f_ship_qty"));

            moDpsEntry = new SDataDpsEntry();
            moDpsEntry.read(new int[] { resultSet.getInt("de.id_year"), resultSet.getInt("de.id_doc"), resultSet.getInt("de.id_ety") } ,
                    session.getDatabase().getConnection().createStatement());
            entry.setDpsEntry(moDpsEntry);

            moItem = new SDataItem();
            moItem.read(new int[] { resultSet.getInt("de.fid_item") },
                    session.getDatabase().getConnection().createStatement());
            entry.setItem(moItem);

            maShipmentDestinyEntries.add(entry);
        }
    }

    public void computeDiogEntry(SGuiSession session, final int[] anPrimaryKeyDiog) throws SQLException, Exception {
        SDbShipmentDestinyEntry entry = null;

        ResultSet resultSet = null;
        Statement statement = null;

        msSql = "SELECT ge.id_year, ge.id_doc, ge.id_ety, ge.fid_item, "
            + "CONCAT(g.num_ser, IF(LENGTH(g.num_ser) = 0, '', '-'), erp.lib_fix_int(g.num, " + SDataConstantsSys.NUM_LEN_IOG + ")) AS f_num, "
            + "g.dt, tpg.code, bpb.bpb AS f_bpb, bpb.code AS f_bpb_code, ent.ent AS f_ent, ent.code AS f_ent_code, i.item, i.item_key, "
            + "u.symbol, i.vol, i.mass, i.weight_gross, i.weight_delivery, i.id_item, i.fid_unit, "
            + "g.b_ship AS f_ship, ge.qty AS f_qty, ge.orig_qty AS f_orig_qty, "

            + "COALESCE((SELECT SUM(sde.qty) FROM " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP_DEST_ETY) + " AS sde "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP_DEST) + " AS sd ON sde.id_ship = sd.id_ship AND sde.id_dest = sd.id_dest "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP) + " AS s ON sd.id_ship = s.id_ship "
            + "WHERE s.b_del = 0 AND sde.fk_diog_year_n = ge.id_year AND sde.fk_diog_doc_n = ge.id_doc AND sde.fk_diog_ety_n = ge.id_ety), 0) AS f_ship_qty, "

            + "(ge.qty - COALESCE((SELECT SUM(sde.qty) FROM " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP_DEST_ETY) + " AS sde "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP_DEST) + " AS sd ON sde.id_ship = sd.id_ship AND sde.id_dest = sd.id_dest "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP) + " AS s ON sd.id_ship = s.id_ship "
            + "WHERE s.b_del = 0 AND sde.fk_diog_year_n = ge.id_year AND sde.fk_diog_doc_n = ge.id_doc AND sde.fk_diog_ety_n = ge.id_ety), 0)) AS f_ship_qty_pend, "

            //+ "COALESCE(SUM(de.qty), 0) AS f_ship_qty, "
            + "g.fid_usr_ship AS " + SDbConsts.FIELD_USER_INS_ID + ", "
            + "g.ts_ship AS " + SDbConsts.FIELD_USER_INS_TS + ", "
            + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + " "
            + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS g "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNS_TP_IOG) + " AS tpg ON g.fid_ct_iog = tpg.id_ct_iog AND g.fid_cl_iog = tpg.id_cl_iog AND g.fid_tp_iog = tpg.id_tp_iog "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS ge ON g.id_year = ge.id_year AND g.id_doc = ge.id_doc "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS bpb ON g.fid_cob = bpb.id_bpb "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_COB_ENT) + " AS ent ON g.fid_cob = ent.id_cob AND g.fid_wh = ent.id_ent "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON ge.fid_item = i.id_item "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS u ON ge.fid_unit = u.id_unit "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON g.fid_usr_ship = ui.id_usr "
            //+ "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP_DEST_ETY) + " AS de ON ge.id_year = de.fk_diog_year_n AND ge.id_doc = de.fk_diog_doc_n AND ge.id_ety = de.fk_diog_ety_n "
            //+ "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP_DEST) + " AS d ON d.id_ship = de.id_ship AND d.id_dest = de.id_dest "
            //+ "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP) + " AS s ON s.id_ship = d.id_ship "
            + "WHERE g.b_del = 0 AND ge.b_del = 0 AND g.b_ship_req = 1 AND "
            + "g.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_INT_TRA[0] + " AND g.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_INT_TRA[1] + " AND g.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_INT_TRA[2] + " AND "
            + "ge.id_year = " + anPrimaryKeyDiog[0] + " AND ge.id_doc = " + anPrimaryKeyDiog[1] + " "
            + (anPrimaryKeyDiog.length > 2 ? "AND ge.id_ety = " + anPrimaryKeyDiog[2] : "") + " ";

        statement = session.getDatabase().getConnection().createStatement();
        resultSet = statement.executeQuery(msSql);
        maShipmentDestinyEntries.clear();
        while (resultSet.next()) {

            entry = new SDbShipmentDestinyEntry();

            entry.setVolume(resultSet.getDouble("i.vol"));
            entry.setMass(resultSet.getDouble("i.mass"));
            entry.setWeightGross(resultSet.getDouble("i.weight_gross"));
            entry.setWeightDelivery(resultSet.getDouble("i.weight_delivery"));
            entry.setFkItemId_r(resultSet.getInt("i.id_item"));
            entry.setFkUnitId_r(resultSet.getInt("i.fid_unit"));
            entry.setFkDiogYearId_n(resultSet.getInt("ge.id_year"));
            entry.setFkDiogDocId_n(resultSet.getInt("ge.id_doc"));
            entry.setFkDiogEntryId_n(resultSet.getInt("ge.id_ety"));

            entry.setXtaDoctoDate(resultSet.getDate("g.dt"));
            entry.setXtaDoctoType(resultSet.getString("tpg.code"));
            entry.setXtaDoctoNumber(resultSet.getString("f_num"));
            entry.setXtaItem(resultSet.getString("i.item"));
            entry.setXtaItemCode(resultSet.getString("i.item_key"));
            entry.setXtaItemUnit(resultSet.getString("u.symbol"));
            entry.setXtaDoctoQuantity(resultSet.getDouble("f_qty"));
            entry.setXtaShipmentQuantity(resultSet.getDouble("f_ship_qty"));
            entry.setXtaShipmentQuantityPending(resultSet.getDouble("f_ship_qty_pend"));
            entry.setQuantity(resultSet.getDouble("f_ship_qty_pend"));

            moDiogEntry = new SDataDiogEntry();
            moDiogEntry.read(new int[] { resultSet.getInt("ge.id_year"), resultSet.getInt("ge.id_doc"), resultSet.getInt("ge.id_ety") } ,
                    session.getDatabase().getConnection().createStatement());
            entry.setDiogEntry(moDiogEntry);

            moItem = new SDataItem();
            moItem.read(new int[] { resultSet.getInt("ge.fid_item") }, session.getDatabase().getConnection().createStatement());
            entry.setItem(moItem);

            maShipmentDestinyEntries.add(entry);
        }
    }

    public void computeRowValues() {
        mdVolume = mdQuantity * moItem.getVolume();
        mdMass = mdQuantity * moItem.getMass();
        mdWeightGross = mdQuantity * moItem.getWeightGross();
        mdWeightDelivery = mdQuantity * moItem.getWeightDelivery();
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_ship = " + mnPkShipmentId + " AND id_dest = " + mnPkDestinyId + " AND id_ety = " + mnPkEntryId;
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_ship = " + pk[0] + " AND id_dest = " + pk[1] + " AND id_ety = " + pk[2];
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkEntryId = 0;

        msSql = "SELECT COALESCE(MAX(id_ety), 0) + 1 FROM " + getSqlTable() + " WHERE id_ship = " + mnPkShipmentId + " AND id_dest = " + mnPkDestinyId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkEntryId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet = null;
        Statement statement = null;
        SDbShipmentDestinyEntry entry = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        statement = session.getDatabase().getConnection().createStatement();
        resultSet = statement.executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            entry = new SDbShipmentDestinyEntry();

            mnPkShipmentId = resultSet.getInt("id_ship");
            mnPkDestinyId = resultSet.getInt("id_dest");
            mnPkEntryId = resultSet.getInt("id_ety");
            mdQuantity = resultSet.getDouble("qty");
            mdVolume = resultSet.getDouble("vol");
            mdMass = resultSet.getDouble("mass");
            mdWeightGross = resultSet.getDouble("weight_gross");
            mdWeightDelivery = resultSet.getDouble("weight_delivery");
            mnFkItemId_r = resultSet.getInt("fk_item_r");
            mnFkUnitId_r = resultSet.getInt("fk_unit_r");
            mnFkDpsYearId_n = resultSet.getInt("fk_dps_year_n");
            mnFkDpsYearId_n = resultSet.wasNull() ? 0 : mnFkDpsYearId_n;
            mnFkDpsDocId_n = resultSet.getInt("fk_dps_doc_n");
            mnFkDpsDocId_n = resultSet.wasNull() ? 0 : mnFkDpsDocId_n;
            mnFkDpsEntryId_n = resultSet.getInt("fk_dps_ety_n");
            mnFkDpsEntryId_n = resultSet.wasNull() ? 0 : mnFkDpsEntryId_n;
            mnFkDiogYearId_n = resultSet.getInt("fk_diog_year_n");
            mnFkDiogYearId_n = resultSet.wasNull() ? 0 : mnFkDiogYearId_n;
            mnFkDiogDocId_n = resultSet.getInt("fk_diog_doc_n");
            mnFkDiogDocId_n = resultSet.wasNull() ? 0 : mnFkDiogDocId_n;
            mnFkDiogEntryId_n = resultSet.getInt("fk_diog_ety_n");
            mnFkDiogEntryId_n = resultSet.wasNull() ? 0 : mnFkDiogEntryId_n;

            if (mnFkDpsYearId_n > 0 && mnFkDpsDocId_n > 0 && mnFkDpsEntryId_n > 0) {

                computeDpsEntry(session, new int[] { mnFkDpsYearId_n, mnFkDpsDocId_n, mnFkDpsEntryId_n });
            }

            if (mnFkDiogYearId_n > 0 && mnFkDiogDocId_n > 0 && mnFkDiogEntryId_n > 0) {

                computeDiogEntry(session, new int[] { mnFkDiogYearId_n, mnFkDiogDocId_n, mnFkDiogEntryId_n });
            }

            if (maShipmentDestinyEntries.size() > 0) {

                entry = maShipmentDestinyEntries.get(0);

                mtXtaDoctoDate = entry.getXtaDoctoDate();
                msXtaDoctoType = entry.getXtaDoctoType();
                msXtaDoctoNumber = entry.getXtaDoctoNumber();
                msXtaItem = entry.getXtaItem();
                msXtaItemCode = entry.getXtaItemCode();
                msXtaItemUnit = entry.getXtaItemUnit();
                mdXtaDoctoQuantity = entry.getXtaDoctoQuantity();
                mdXtaShipmentQuantity = entry.getXtaShipmentQuantity();
                mdXtaShipmentQuantityPending = entry.getXtaShipmentQuantityPending();
            }

            mbRegistryNew = false;
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        if (mbRegistryNew) {
            verifyRegistryNew(session);
        }

        if (mbRegistryNew) {
            computePrimaryKey(session);

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkShipmentId + ", " +
                    mnPkDestinyId + ", " +
                    mnPkEntryId + ", " +
                    mdQuantity + ", " +
                    mdVolume + ", " +
                    mdMass + ", " +
                    mdWeightGross + ", " +
                    mdWeightDelivery + ", " +
                    mnFkItemId_r + ", " +
                    mnFkUnitId_r + ", " +
                    (mnFkDpsYearId_n > 0 ? mnFkDpsYearId_n : "NULL") + ", " +
                    (mnFkDpsDocId_n > 0 ? mnFkDpsDocId_n : "NULL") + ", " +
                    (mnFkDpsEntryId_n > 0 ? mnFkDpsEntryId_n : "NULL") + ", " +
                    (mnFkDiogYearId_n > 0 ? mnFkDiogYearId_n : "NULL") + ", " +
                    (mnFkDiogDocId_n > 0 ? mnFkDiogDocId_n : "NULL") + ", " +
                    (mnFkDiogEntryId_n > 0 ? mnFkDiogEntryId_n : "NULL") + " " +
                    ")";
        }
        else {

            msSql = "UPDATE " + getSqlTable() + " SET " +
                "id_ship = " + mnPkShipmentId + ", " +
                "id_dest = " + mnPkDestinyId + ", " +
                "id_ety = " + mnPkEntryId + ", " +
                "qty = " + mdQuantity + ", " +
                "vol = " + mdVolume + ", " +
                "mass = " + mdMass + ", " +
                "weight_gross = " + mdWeightGross + ", " +
                "weight_delivery = " + mdWeightDelivery + ", " +
                "fk_item_r = " + mnFkItemId_r + ", " +
                "fk_unit_r = " + mnFkUnitId_r + ", " +
                "fk_dps_year_n = " + (mnFkDpsYearId_n > 0 ? mnFkDpsYearId_n : "NULL") + ", " +
                "fk_dps_doc_n = " + (mnFkDpsDocId_n >  0 ? mnFkDpsDocId_n : "NULL") + ", " +
                "fk_dps_ety_n = " + (mnFkDpsEntryId_n > 0 ? mnFkDpsEntryId_n : "NULL") + ", " +
                "fk_diog_year_n = " + (mnFkDiogYearId_n > 0 ? mnFkDiogYearId_n : "NULL") + ", " +
                "fk_diog_doc_n = " + (mnFkDiogDocId_n > 0 ? mnFkDiogDocId_n : "NULL") + ", " +
                "fk_diog_ety_n = " + (mnFkDiogEntryId_n > 0 ? mnFkDiogEntryId_n : "NULL") + " " +
                getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbShipmentDestinyEntry clone() throws CloneNotSupportedException {
        SDbShipmentDestinyEntry registry = new SDbShipmentDestinyEntry();

        registry.setPkShipmentId(this.getPkShipmentId());
        registry.setPkDestinyId(this.getPkDestinyId());
        registry.setPkEntryId(this.getPkEntryId());
        registry.setQuantity(this.getQuantity());
        registry.setVolume(this.getVolume());
        registry.setMass(this.getMass());
        registry.setWeightGross(this.getWeightGross());
        registry.setWeightDelivery(this.getWeightDelivery());
        registry.setFkItemId_r(this.getFkItemId_r());
        registry.setFkUnitId_r(this.getFkUnitId_r());
        registry.setFkDpsYearId_n(this.getFkDpsYearId_n());
        registry.setFkDpsDocId_n(this.getFkDpsDocId_n());
        registry.setFkDpsEntryId_n(this.getFkDpsEntryId_n());
        registry.setFkDiogYearId_n(this.getFkDiogYearId_n());
        registry.setFkDiogDocId_n(this.getFkDiogDocId_n());
        registry.setFkDiogEntryId_n(this.getFkDiogEntryId_n());

        registry.setXtaDoctoDate(this.getXtaDoctoDate());
        registry.setXtaDoctoType(this.getXtaDoctoType());
        registry.setXtaDoctoNumber(this.getXtaDoctoNumber());
        registry.setXtaItem(this.getXtaItem ());
        registry.setXtaItemCode(this.getXtaItemCode());
        registry.setXtaItemUnit(this.getXtaItemUnit());
        registry.setXtaDoctoQuantity(this.getXtaDoctoQuantity());
        registry.setXtaDoctoQuantityAdjustment(this.getXtaDoctoQuantityAdjustment());
        registry.setXtaShipmentQuantity(this.getXtaShipmentQuantity());
        registry.setXtaShipmentQuantityPending(this.getXtaShipmentQuantityPending());
        registry.setXtaDpsSubtotal(this.getXtaDpsSubtotal());

        registry.setShipmentDestinyEntries(this.getShipmentDestinyEntries());
        registry.setItem(this.getItem());
        registry.setDpsEntry(this.getDpsEntry());
        registry.setDiogEntry(this.getDiogEntry());

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
        return true;
    }

    @Override
    public boolean isRowEdited() {
        return isRowEdited();
    }

    @Override
    public void setRowEdited(final boolean edited) {
        setRowEdited(edited);
    }

    @Override
    public Object getRowValueAt(int row) {
        Object value = null;

        switch(row) {
            case 0:
                value = msXtaDoctoType;
                break;
            case 1:
                value = msXtaDoctoNumber;
                break;
            case 2:
                value = mtXtaDoctoDate;
                break;
            case 3:
                value = msXtaItem;
                break;
            case 4:
                value = msXtaItemCode;
                break;
            case 5:
                value = mdXtaDoctoQuantity;
                break;
            case 6:
                value = mdXtaDoctoQuantityAdjustment;
                break;
            case 7:
                value = mdXtaShipmentQuantity;
                break;
            case 8:
                value = mdXtaShipmentQuantityPending;
                break;
            case 9:
                value = mdQuantity;
                break;
            case 10:
                value = msXtaItemUnit;
                break;
            case 11:
                mdVolume = mdQuantity * moItem.getVolume();
                value = mdVolume;
                break;
            case 12:
                mdMass = mdQuantity * moItem.getMass();
                value = mdMass;
                break;
            case 13:
                mdWeightGross = mdQuantity * moItem.getWeightGross();
                value = mdWeightGross;
                break;
            case 14:
                mdWeightDelivery = mdQuantity * moItem.getWeightDelivery();
                value = mdWeightDelivery;
                break;
            case 15:
                value = msXtaItemUnit;
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
            case 8:
            case 9:
                mdQuantity = (Double) value;
                computeRowValues();
                break;
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            default:
        }
    }
}
