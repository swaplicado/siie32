/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 * Calculation of identified costs of sales according to stock supplies of SIIE-Web.
 * @author Sergio Flores
 */
public class SDbIdentifiedCostCalculation extends SDbRegistryUser {

    protected int mnPkCalculationId;
    protected Date mtDateStart;
    protected Date mtDateEnd;
    protected boolean mbRecalculate;
    /*
    protected boolean mbDeleted;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected ArrayList<SDbIdentifiedCostLot> maChildrenIdentifiedCostLots;
    
    protected String msAuxCalculationLog;
    protected HashMap<String, SDbIdentifiedCostLot> moAuxMapIdentifiedCostLots; // key: lot key as String (0-0-0), value: identified lot for cost
    protected ArrayList<SRowIdentifiedCostDps> maAuxIdentifiedCostDpsRows;
    protected ArrayList<SRowIdentifiedCostDpsEntry> maAuxIdentifiedCostDpsEntryRows;
    protected ArrayList<SRowIdentifiedCostDpsEntrySupplyLot> maAuxIdentifiedCostDpsEntrySupplyLotRows;
    protected ArrayList<SRowIdentifiedCostLot> maAuxIdentifiedCostLotRows;

    public SDbIdentifiedCostCalculation() {
        super(SModConsts.TRN_COST_IDENT_CALC);
    }

    public void setPkCalculationId(int n) { mnPkCalculationId = n; }
    public void setDateStart(Date t) { mtDateStart = t; }
    public void setDateEnd(Date t) { mtDateEnd = t; }
    public void setRecalculate(boolean b) { mbRecalculate = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkCalculationId() { return mnPkCalculationId; }
    public Date getDateStart() { return mtDateStart; }
    public Date getDateEnd() { return mtDateEnd; }
    public boolean isRecalculate() { return mbRecalculate; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    public ArrayList<SDbIdentifiedCostLot> getChildrenIdentifiedCostLots() { return maChildrenIdentifiedCostLots; }
    
    public String getAuxCalculationLog() { return msAuxCalculationLog; }
    public HashMap<String, SDbIdentifiedCostLot> getAuxMapIdentifiedCostLots() { return moAuxMapIdentifiedCostLots; }
    public ArrayList<SRowIdentifiedCostDps> getAuxIdentifiedCostDpsRows() { return maAuxIdentifiedCostDpsRows; }
    public ArrayList<SRowIdentifiedCostDpsEntry> getAuxIdentifiedCostDpsEntryRows() { return maAuxIdentifiedCostDpsEntryRows; }
    public ArrayList<SRowIdentifiedCostDpsEntrySupplyLot> getAuxIdentifiedCostDpsEntrySupplyLotRows() { return maAuxIdentifiedCostDpsEntrySupplyLotRows; }
    public ArrayList<SRowIdentifiedCostLot> getAuxIdentifiedCostLotRows() { return maAuxIdentifiedCostLotRows; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkCalculationId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkCalculationId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkCalculationId = 0;
        mtDateStart = null;
        mtDateEnd = null;
        mbRecalculate = false;
        mbDeleted = false;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        maChildrenIdentifiedCostLots = new ArrayList<>();
        
        msAuxCalculationLog = "";
        moAuxMapIdentifiedCostLots = new HashMap<>();
        maAuxIdentifiedCostDpsRows = new ArrayList<>();
        maAuxIdentifiedCostDpsEntryRows = new ArrayList<>();
        maAuxIdentifiedCostDpsEntrySupplyLotRows = new ArrayList<>();
        maAuxIdentifiedCostLotRows = new ArrayList<>();
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_calc = " + mnPkCalculationId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_calc = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkCalculationId = 0;

        msSql = "SELECT COALESCE(MAX(id_calc), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkCalculationId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkCalculationId = resultSet.getInt("id_calc");
            mtDateStart = resultSet.getDate("dt_sta");
            mtDateEnd = resultSet.getDate("dt_end");
            mbRecalculate = resultSet.getBoolean("b_recalc");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            // read as well children lot unit costs:
            
            msSql = "SELECT id_item, id_unit, id_lot "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_COST_IDENT_LOT) + " "
                    + getSqlWhere() + ";";
            
            resultSet = session.getStatement().executeQuery(msSql);
            while (resultSet.next()) {
                SDbIdentifiedCostLot child = new SDbIdentifiedCostLot();
                child.read(session, new int[] { resultSet.getInt("id_item"), resultSet.getInt("id_unit"), resultSet.getInt("id_lot"), mnPkCalculationId });
                maChildrenIdentifiedCostLots.add(child);
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
            computePrimaryKey(session);
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkCalculationId + ", " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateEnd) + "', " + 
                    (mbRecalculate ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_calc = " + mnPkCalculationId + ", " +
                    "dt_sta = '" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " +
                    "dt_end = '" + SLibUtils.DbmsDateFormatDate.format(mtDateEnd) + "', " +
                    "b_recalc = " + (mbRecalculate ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        
        // save as well children lot unit costs:
        
        if (!mbRegistryNew) {
            // delete former children lot unit costs:
            
            msSql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.TRN_COST_IDENT_LOT) + " SET "
                    + "b_del = TRUE, "
                    + "fk_usr_upd = " + mnFkUserUpdateId + ", "
                    + "ts_usr_upd = " + "NOW()" + " "
                    + getSqlWhere() + " AND NOT b_del;";

            session.getStatement().execute(msSql);
        }
        
        for (SDbIdentifiedCostLot child : maChildrenIdentifiedCostLots) {
            child.setPkCalculationId(mnPkCalculationId);
            child.setDeleted(mbDeleted);
            child.save(session);
        }
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbIdentifiedCostCalculation clone() throws CloneNotSupportedException {
        SDbIdentifiedCostCalculation registry = new SDbIdentifiedCostCalculation();

        registry.setPkCalculationId(this.getPkCalculationId());
        registry.setDateStart(this.getDateStart());
        registry.setDateEnd(this.getDateEnd());
        registry.setRecalculate(this.isRecalculate());
        registry.setDeleted(this.isDeleted());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        
        for (SDbIdentifiedCostLot child : maChildrenIdentifiedCostLots) {
            registry.getChildrenIdentifiedCostLots().add(child.clone());
        }

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
    
    /**
     * Add calculation log entry and, if required, print it to system output.
     * @param entry
     * @param out 
     */
    private void addCalculationLogEntry(final String entry, final boolean print) {
        msAuxCalculationLog += (msAuxCalculationLog.isEmpty() ? "" : "\n") + entry;
        if (print) {
            System.out.println(entry);
        }
    }
    
    /**
     * Get supplies from external database for sales.
     * @param connection Database connection.
     * @param externalDatabase External database squema name.
     * @return Supplies from external database for sales.
     * @throws Exception
     */
    private ArrayList<Supply> getSupplies(final Connection connection, final String externalDatabase) throws Exception {
        ArrayList<Supply> supplies = new ArrayList<>();
        
        String sql;
        
        // tables from current session's database:
        
        sql = "SELECT "
                + "td.id_ct_dps, td.id_cl_dps, td.id_tp_dps, td.code AS tp_dps_code, "
                + "de.id_year, de.id_doc, de.id_ety, "
                + "d.dt AS dps_dt, d.num_ser AS dps_series, d.num AS dps_number, "
                + "d.stot_r AS dps_stot, d.stot_cur_r AS dps_stot_cur, "
                + "c.id_cur, c.cur_key, b.id_bp, b.bp, "
                + "de.concept_key, de.concept, de.fid_item AS dps_ety_id_item, de.fid_unit AS dps_ety_id_unit, "
                + "de.qty AS dps_ety_qty, de.stot_r AS dps_ety_stot, de.stot_cur_r AS dps_ety_stot_cur, "
                + "t.*, l.* "
                + "\n"
                + "FROM trn_dps AS d "
                + "INNER JOIN erp.trnu_tp_dps AS td ON td.id_ct_dps = d.fid_ct_dps AND td.id_cl_dps = d.fid_cl_dps AND td.id_tp_dps = d.fid_tp_dps "
                + "INNER JOIN erp.cfgu_cur AS c ON c.id_cur = d.fid_cur "
                + "INNER JOIN erp.bpsu_bp AS b ON b.id_bp = d.fid_bp_r "
                + "INNER JOIN trn_dps_ety AS de ON de.id_year = d.id_year AND de.id_doc = d.id_doc "
                + "LEFT OUTER JOIN (";

        // tables from external database (3 SQL select segments):
        
        String[] sqlHints = new String[] { "invoices", "debit notes", "credit notes"};
        String[] sqlMvtRowFields = new String[] { "doc_invoice_row_id", "doc_debit_note_row_id", "doc_credit_note_row_id"};
        int[] sqlFactors = new int[] { 1, 1, -1}; // adds, adds, subtracts
        
        for (int segment = 0; segment < sqlHints.length; segment++) {
            sql += (segment == 0 ? "" : "\nUNION ALL ") + "\n/* " + sqlHints[segment] + " */ "
                    + "\n"
                    + "SELECT "
                    + "" + sqlFactors[segment] + " AS _factor, "
                    + "d.id_document AS _id_document, LEFT(d.external_id, 4) AS _ext_doc_year_id, CAST(RIGHT(d.external_id, LENGTH(d.external_id) - 5) AS UNSIGNED INTEGER) AS _ext_doc_doc_id, "
                    + "d.service_num AS _doc_series, d.num AS _doc_number, d.dt_date AS _doc_date, "
                    + "dr.id_document_row AS _id_document_row, dr.external_id AS _ext_doc_ety_id, dr.quantity AS _doc_row_qty, "
                    + "i.id_item AS _id_item, i.external_id AS _ext_item_id, i.name AS _item_name, i.code AS _item_code, "
                    + "u.id_unit AS _id_unit, u.external_id AS _ext_unit_id, u.name AS _unit_name, u.code AS _unit_code, "
                    + "m.id_mvt AS _id_mvt, m.dt_date AS _mvt_date, m.folio AS _mvt_folio, "
                    + "mr.id_mvt_row AS _id_mvt_row, mr.quantity AS _mvt_row_qty, "
                    + "mrl.id_mvt_row_lot AS _id_mvt_row_lot, mrl.quantity AS _mvt_row_lot_qty, "
                    + "l.id_lot AS _id_lot, l.lot AS _lot, l.dt_expiry AS _lot_exp "
                    + "\n"
                    + "FROM " + externalDatabase + ".wms_mvts AS m "
                    + "INNER JOIN " + externalDatabase + ".wms_mvt_rows AS mr ON mr.mvt_id = m.id_mvt "
                    + "INNER JOIN " + externalDatabase + ".wms_mvt_row_lots AS mrl ON mrl.mvt_row_id = mr.id_mvt_row "
                    + "INNER JOIN " + externalDatabase + ".wms_lots AS l ON l.id_lot = mrl.lot_id "
                    + "INNER JOIN " + externalDatabase + ".erpu_items AS i ON i.id_item = l.item_id "
                    + "INNER JOIN " + externalDatabase + ".erpu_units AS u ON u.id_unit = l.unit_id "
                    + "INNER JOIN " + externalDatabase + ".erpu_document_rows AS dr ON dr.id_document_row = mr." + sqlMvtRowFields[segment] + " "
                    + "INNER JOIN " + externalDatabase + ".erpu_documents AS d ON d.id_document = dr.document_id "
                    + "\n"
                    + "WHERE NOT m.is_deleted AND NOT mr.is_deleted AND NOT mrl.is_deleted "
                    + "AND d.dt_date BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtDateEnd) + "' "
                    + "AND mr." + sqlMvtRowFields[segment] + " <> 1 ";
        }
        
        sql += "\n"
                + "ORDER BY _id_document, _id_document_row, _id_mvt, _id_mvt_row, _id_mvt_row_lot, _id_lot "
                + "\n"
                + ") AS t ON t._ext_doc_year_id = de.id_year AND t._ext_doc_doc_id = de.id_doc AND t._ext_doc_ety_id = de.id_ety ";
        
        // tables from current session's database:
        
        sql += "\n"
                + "LEFT OUTER JOIN trn_lot AS l ON l.id_item = t._ext_item_id AND l.id_unit = t._ext_unit_id AND l.lot = t._lot AND NOT l.b_del "
                + "\n"
                + "WHERE NOT d.b_del AND NOT de.b_del "
                + "AND d.fid_ct_dps = " + SModSysConsts.TRNS_CT_DPS_SAL + " "
                + "AND d.fid_cl_dps IN (" + SModSysConsts.TRNS_CL_DPS_SAL_DOC[1] + ", " + SModSysConsts.TRNS_CL_DPS_SAL_ADJ[1] + ") "
                + "AND d.fid_st_dps = " + SModSysConsts.TRNS_ST_DPS_EMITED + " "
                + "AND d.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtDateEnd) + "' "
                + "\n"
                + "ORDER BY td.id_ct_dps, td.id_cl_dps, td.id_tp_dps, td.code, de.id_year, de.id_doc, de.id_ety, "
                + "t._id_mvt, t._id_mvt_row, t._id_mvt_row_lot;";
        
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            
            while (resultSet.next()) {
                Supply supply = new Supply();
                
                supply.DocYearId = resultSet.getInt("id_year");
                supply.DocDocId = resultSet.getInt("id_doc");
                supply.DocCategoryId = resultSet.getInt("id_ct_dps");
                supply.DocClassId = resultSet.getInt("id_cl_dps");
                supply.DocTypeId = resultSet.getInt("id_tp_dps");
                supply.DocTypeCode = resultSet.getString("tp_dps_code");
                supply.DocSeries = resultSet.getString("dps_series");
                supply.DocNumber = resultSet.getString("dps_number");
                supply.DocDate = resultSet.getDate("dps_dt");
                supply.DocSubtotal = resultSet.getDouble("dps_stot");
                supply.DocSubtotalCy = resultSet.getDouble("dps_stot_cur");
                supply.DocCurrencyId = resultSet.getInt("id_cur");
                supply.DocCurrencyCode = resultSet.getString("cur_key");
                supply.DocBizPartnerId = resultSet.getInt("id_bp");
                supply.DocBizPartnerName = resultSet.getString("bp");

                supply.DocEntryYearId = resultSet.getInt("id_year");
                supply.DocEntryDocId = resultSet.getInt("id_doc");
                supply.DocEntryEntryId = resultSet.getInt("id_ety");
                supply.DocEntryConceptKey = resultSet.getString("concept_key");
                supply.DocEntryConcept = resultSet.getString("concept");
                supply.DocEntryQuantity = resultSet.getDouble("dps_ety_qty");
                supply.DocEntrySubtotal = resultSet.getDouble("dps_ety_stot");
                supply.DocEntrySubtotalCy = resultSet.getDouble("dps_ety_stot_cur");
                supply.DocEntryItemId = resultSet.getInt("dps_ety_id_item");
                supply.DocEntryUnitId = resultSet.getInt("dps_ety_id_unit");

                supply.SupplyFactor = resultSet.getInt("_factor");
                
                supply.SupplyDocId = resultSet.getInt("_id_document");
                supply.SupplyDocSeries = resultSet.getString("_doc_series");
                supply.SupplyDocNumber = resultSet.getString("_doc_number");
                supply.SupplyDocDate = resultSet.getDate("_doc_date");

                supply.SupplyDocRowId = resultSet.getInt("_id_document_row");
                supply.SupplyDocRowQuantity = resultSet.getDouble("_doc_row_qty");
                supply.SupplyDocRowItemCode = resultSet.getString("_item_code");
                supply.SupplyDocRowItemName = resultSet.getString("_item_name");
                supply.SupplyDocRowUnitCode = resultSet.getString("_unit_code");
                supply.SupplyDocRowUnitName = resultSet.getString("_unit_name");
                
                supply.SupplyMovementId = resultSet.getInt("_id_mvt");
                supply.SupplyMovementFolio = resultSet.getString("_mvt_folio");
                supply.SupplyMovementDate = resultSet.getDate("_mvt_date");

                supply.SupplyMovementRowId = resultSet.getInt("_id_mvt_row");
                supply.SupplyMovementRowQuantity = resultSet.getDouble("_mvt_row_qty");

                supply.SupplyMovementRowLotId = resultSet.getInt("_id_mvt_row_lot");
                supply.SupplyMovementRowLotQuantity = resultSet.getDouble("_mvt_row_lot_qty");
                supply.SupplyLotId = resultSet.getInt("_id_lot");
                supply.SupplyLotLot = resultSet.getString("_lot");
                supply.SupplyLotExpiration = resultSet.getDate("_lot_exp");

                supply.LotItemId = resultSet.getInt("id_item");
                supply.LotUnitId = resultSet.getInt("id_unit");
                supply.LotLotId = resultSet.getInt("id_lot");
                supply.LotLot = resultSet.getString("lot");
                supply.LotExpiration = resultSet.getDate("dt_exp_n");
                supply.LotBlocked = resultSet.getBoolean("b_block");
                supply.LotDeleted = resultSet.getBoolean("b_del");
                
                supplies.add(supply);
            }
        }
        
        return supplies;
    }
    
    /**
     * Get manufacturing order key for given lot. Method invoked in recursive context.
     * @param connection Database connection.
     * @param lotKey Lot key: item ID, unit ID, lot ID.
     * @return Manufacturing order key.
     */
    private int[] getOrderKey(final Connection connection, final int[] lotKey) throws Exception {
        int[] orderKey = null;
        int itemId = lotKey[0];
        int unitId = lotKey[1];
        int lotId = lotKey[2];
        
        String sql = "SELECT id_year, id_ord "
                + "FROM mfg_ord "
                + "WHERE NOT b_del AND NOT b_for "
                + "AND fid_lot_item_nr = " + itemId + " AND fid_lot_unit_nr = " + unitId + " AND fid_lot_n = " + lotId + " "
                + "ORDER BY id_year, id_ord LIMIT 1;";
        
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            
            if (resultSet.next()) {
                orderKey = new int[] { resultSet.getInt("id_year"), resultSet.getInt("id_ord") };
            }
        }
        
        return orderKey;
    }
    
    /**
     * Compute unit cost of lot from manufacturing for given manufacturing order. Method invoked in recursive context.
     * Adds a new instance of identified cost for lot to member map of its own kink.
     * @param session GUI session.
     * @param orderKey Manufacturing order key: year ID, order ID.
     * @return New instance of identified cost for lot.
     * @throws Exception
     */
    private SDbIdentifiedCostLot computeLotCostUnitFromMfg(final SGuiSession session, final int[] orderKey) throws Exception {
        SDbIdentifiedCostLot identifiedCostLotMfg = null;
        int yearId = orderKey[0];
        int orderId = orderKey[1];
        
        // get manufactured lot and parent order, if any, of given manufacturing order:

        String sql = "SELECT num, ref, dt, qty, fid_lot_item_nr, fid_lot_unit_nr, fid_lot_n, fid_ord_year_n, fid_ord_n "
                + "FROM mfg_ord "
                + "WHERE NOT b_del AND NOT b_for "
                + "AND id_year = " + yearId + " AND id_ord = " + orderId + ";";

        try (Statement statement = session.getStatement().getConnection().createStatement()) { // allow a valid database statement in recursion
            ResultSet resultSet = statement.executeQuery(sql);
            
            if (resultSet.next()) {
                addCalculationLogEntry("- OP #" + resultSet.getString("num") + " " + resultSet.getDate("dt") + " PK=" + SLibUtils.textKey(orderKey) + " Ref. '" + resultSet.getString("ref") + "'...", true);
                
                int[] orderLotKey = new int[] { resultSet.getInt("fid_lot_item_nr"), resultSet.getInt("fid_lot_unit_nr"), resultSet.getInt("fid_lot_n") };
                
                identifiedCostLotMfg = moAuxMapIdentifiedCostLots.get(SLibUtils.textKey(orderLotKey)); // lot may be processed already
                
                if (identifiedCostLotMfg == null) {
                    double mfgCost = 0.0;
                    double mfgQuantity = 0.0;
                    double orderQuantity = resultSet.getDouble("qty");
                    ArrayList<int[]> jointMfgLotKeys = new ArrayList<>();
                    
                    if (resultSet.getInt("fid_ord_year_n") != 0 && resultSet.getInt("fid_ord_n") != 0) {
                        // recursively compute parent manufacturing order:
                        computeLotCostUnitFromMfg(session, new int[] { resultSet.getInt("fid_ord_year_n"), resultSet.getInt("fid_ord_n") }); // returned instance is discarted, it is useless!
                    }

                    // calculate cost of manufacturing order from stock movements for consumptions:

                    sql = "SELECT s.fid_ct_iog, s.fid_diog_year, s.fid_diog_doc, s.fid_diog_ety, s.id_year, s.id_item, s.id_unit, s.id_lot, s.id_cob, s.id_wh, s.id_mov, s.mov_in, s.mov_out "
                            + "FROM trn_stk AS s "
                            + "INNER JOIN trn_diog_ety AS ioe ON ioe.id_year = s.fid_diog_year AND ioe.id_doc = s.fid_diog_doc AND ioe.id_ety = s.fid_diog_ety "
                            + "INNER JOIN trn_diog AS io ON io.id_year = ioe.id_year AND io.id_doc = ioe.id_doc "
                            + "WHERE NOT s.b_del AND NOT io.b_del AND NOT ioe.b_del "
                            + "AND s.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_CON[1] + " AND s.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_CON[2] + " "
                            + "AND io.fid_mfg_year_n = " + yearId + " AND io.fid_mfg_ord_n = " + orderId + " "
                            + "ORDER BY s.fid_ct_iog, s.fid_diog_year, s.fid_diog_doc, s.fid_diog_ety, s.id_year, s.id_item, s.id_unit, s.id_lot, s.id_cob, s.id_wh, s.id_mov, s.mov_in, s.mov_out;";

                    resultSet = statement.executeQuery(sql);

                    while (resultSet.next()) {
                        int[] stockLotKey = new int[] { resultSet.getInt("s.id_item"), resultSet.getInt("s.id_unit"), resultSet.getInt("s.id_lot") };
                        boolean isJointMfgLotKey = orderLotKey[0] == stockLotKey[0] && orderLotKey[1] == stockLotKey[1] && orderLotKey[2] != stockLotKey[2];

                        if (SLibUtils.compareKeys(orderLotKey, stockLotKey) || isJointMfgLotKey) {
                            // stock move is for manufactured lot:

                            switch (resultSet.getInt("s.fid_ct_iog")) {
                                case SModSysConsts.TRNS_CT_IOG_IN:
                                    // inputs add:
                                    mfgQuantity += resultSet.getDouble("s.mov_in");
                                    break;
                                case SModSysConsts.TRNS_CT_IOG_OUT:
                                    // outputs subtract:
                                    mfgQuantity -= resultSet.getDouble("s.mov_out");
                                    break;
                                default:
                                    // do nothing
                            }

                            if (isJointMfgLotKey) {
                                jointMfgLotKeys.add(stockLotKey);
                            }
                        }
                        else {
                            // stock move is for consumed lot:

                            SDbIdentifiedCostLot identifiedCostLot = getIdentifiedCostLot(session, stockLotKey);

                            if (identifiedCostLot != null) {
                                switch (resultSet.getInt("s.fid_ct_iog")) {
                                    case SModSysConsts.TRNS_CT_IOG_IN:
                                        // inputs subtract:
                                        mfgCost = SLibUtils.roundAmount(mfgCost - SLibUtils.roundAmount(identifiedCostLot.getCostUnit() * resultSet.getDouble("s.mov_in")));
                                        break;
                                    case SModSysConsts.TRNS_CT_IOG_OUT:
                                        // outputs add:
                                        mfgCost = SLibUtils.roundAmount(mfgCost + SLibUtils.roundAmount(identifiedCostLot.getCostUnit() * resultSet.getDouble("s.mov_out")));
                                        break;
                                    default:
                                        // do nothing
                                }
                            }
                        }
                    }

                    // add identified cost for lot from manufacturing:

                    Double lotCostUnit = null;

                    if (mfgQuantity != 0.0) {
                        lotCostUnit = mfgCost / mfgQuantity; // actual quantity
                    }
                    else if (orderQuantity != 0.0) {
                        lotCostUnit = mfgCost / orderQuantity; // planned quantity
                    }

                    identifiedCostLotMfg = addIdentifiedCostLot(orderLotKey, lotCostUnit, SDbIdentifiedCostLot.COST_UNIT_TP_MFG, null);

                    // add the same identified cost for all joint manufacturing lots from manufacturing:

                    for (int[] jointMfgLotKey : jointMfgLotKeys) {
                        addIdentifiedCostLot(jointMfgLotKey, lotCostUnit, SDbIdentifiedCostLot.COST_UNIT_TP_MFG, orderLotKey);
                    }
                }
            }
        }
        
        return identifiedCostLotMfg;
    }
    
    /**
     * Compute unit cost of lot from stock for given lot. Method invoked in recursive context.
     * Adds a new instance of identified cost for lot to member map of its own kink.
     * @param session GUI session.
     * @param lotKey Lot key: item ID, unit ID, lot ID.
     * @return New instance of identified cost for lot.
     * @throws Exception
     */
    private SDbIdentifiedCostLot computeLotCostUnitFromStk(final SGuiSession session, final int[] lotKey) throws Exception {
        checkIdentifiedCostLot(lotKey); // throws exception when an instance of identified cost for lot exists
        
        SDbIdentifiedCostLot identifiedCostLotStk = null;
        int itemId = lotKey[0];
        int unitId = lotKey[1];
        int lotId = lotKey[2];
        
        String sql = "SELECT id_year, id_cob, id_wh, id_mov, dt, mov_in, mov_out, cost_u, debit, credit, fid_ct_iog "
                + "FROM trn_stk "
                + "WHERE NOT b_del "
                + "AND (fid_cl_iog = " + SModSysConsts.TRNS_CL_IOG_IN_PUR[1] + " OR fid_cl_iog = " + SModSysConsts.TRNS_CL_IOG_IN_ADJ[1] + ") "
                + "AND NOT (fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_ADJ_INV[1] + " AND fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_ADJ_INV[2] + " AND MONTH(dt) = 1 AND DAY(dt) = 1) "
                + "AND id_item = " + itemId + " AND id_unit = " + unitId + " AND id_lot = " + lotId + " "
                + "ORDER BY dt, id_cob, id_wh, id_mov;";
        
        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            double acumQuantity = 0.0;
            double acumCost = 0.0;
            ResultSet resultSet = statement.executeQuery(sql);
            
            while (resultSet.next()) {
                int iogCategory = resultSet.getInt("fid_ct_iog");
                double costUnit = resultSet.getDouble("cost_u");
                double quantity = 0.0;
                
                switch (iogCategory) {
                    case SModSysConsts.TRNS_CT_IOG_IN:
                        quantity = resultSet.getDouble("mov_in");
                        if (costUnit == 0.0 && quantity != 0) {
                            costUnit = resultSet.getDouble("debit") / quantity;
                        }
                        acumQuantity += quantity;
                        acumCost = SLibUtils.roundAmount(acumCost + SLibUtils.roundAmount(quantity * costUnit));
                        break;
                        
                    case SModSysConsts.TRNS_CT_IOG_OUT:
                        quantity = resultSet.getDouble("mov_out");
                        if (costUnit == 0.0 && quantity != 0) {
                            costUnit = resultSet.getDouble("credit") / quantity;
                        }
                        acumQuantity -= quantity;
                        acumCost = SLibUtils.roundAmount(acumCost - SLibUtils.roundAmount(quantity * costUnit));
                        break;
                        
                    default:
                        // do nothing
                }
            }
            
            // add identified cost for lot from stock:
                
            Double lotCostUnit = null;
            
            if (acumQuantity != 0) {
                lotCostUnit = acumCost / acumQuantity;
            }
            
            identifiedCostLotStk = addIdentifiedCostLot(lotKey, lotCostUnit, SDbIdentifiedCostLot.COST_UNIT_TP_STK, null);
        }
        
        return identifiedCostLotStk;
    }
    
    /**
     * Check that an instance of identified cost for lot has not been created already.
     * Throw exception when an instance of identified cost for lot exists.
     * @param lotKey Lot key: item ID, unit ID, lot ID.
     */
    private void checkIdentifiedCostLot(final int[] lotKey) throws Exception {
        String lotKeyAsString = SLibUtils.textKey(lotKey);
        
        if (moAuxMapIdentifiedCostLots.containsKey(lotKeyAsString)) {
            throw new Exception("¡El lote PK=" + lotKeyAsString + " ya existe en la colección de costos identificados de lotes!");
        }
    }
    
    /**
     * Create a new instance of identified cost for lot.
     * @param lotKey Lot key: item ID, unit ID, lot ID.
     * @param costUnit Unit cost for lot.
     * @param costUnitType Unit cost type. Available options: constants COST_UNIT_TP_..., i.e., "STK" = from stock; "MFG" = from manufacturing.
     * @param jointMfgLotKey_n Joint manufacturing lot key: item ID, unit ID, lot ID. Can be <code>null</code>.
     * @return New instance of identified cost for lot.
     */
    private SDbIdentifiedCostLot addIdentifiedCostLot(final int[] lotKey, final Double costUnit, final String costUnitType, final int[] jointMfgLotKey_n) throws Exception {
        checkIdentifiedCostLot(lotKey);
        
        SDbIdentifiedCostLot identifiedCostLot = SDbIdentifiedCostLot.createIdentifiedCostLot(lotKey, costUnit, costUnitType, jointMfgLotKey_n);
        moAuxMapIdentifiedCostLots.put(SLibUtils.textKey(lotKey), identifiedCostLot);
        
        return identifiedCostLot;
    }
    
    /**
     * Get an instance of identified cost for lot for given lot. Method invoked in recursive context.
     * @param session GUI session.
     * @param lotKey Lot key: item ID, unit ID, lot ID.
     * @return Instance of identified cost for lot.
     * @throws Exception
     */
    private SDbIdentifiedCostLot getIdentifiedCostLot(final SGuiSession session, final int[] lotKey) throws Exception {
        String lotKeyAsString = SLibUtils.textKey(lotKey); // convenience variable
        SDbIdentifiedCostLot identifiedCostLot = moAuxMapIdentifiedCostLots.get(lotKeyAsString);

        if (identifiedCostLot == null) {
            // get former costs:

            ArrayList<SDbIdentifiedCostLot> identifiedCostLots = SDbIdentifiedCostLot.getIdentifiedCostLots(session, lotKey);

            if (!identifiedCostLots.isEmpty() && mbRecalculate) {
                // delete former costs:

                for (SDbIdentifiedCostLot icl : identifiedCostLots) {
                    icl.delete(session);
                }

                identifiedCostLots.clear();
            }

            if (!identifiedCostLots.isEmpty()) {
                // get more recent former cost:

                identifiedCostLot = identifiedCostLots.get(0);
            }
            else {
                // check if lot was manufactured:

                int[] orderKey = getOrderKey(session.getStatement().getConnection(), lotKey);

                if (orderKey != null) {
                    identifiedCostLot = computeLotCostUnitFromMfg(session, orderKey); // unit cost from manufacturing
                }
                else {
                    identifiedCostLot = computeLotCostUnitFromStk(session, lotKey); // unit cost from stock
                }
            }
        }
        
        return identifiedCostLot;
    }
    
    /**
     * Calculate identified costs for lots of sales according to stock supplies of SIIE-Web.
     * Update as well children identified cost for lots.
     * @param session GUI session.
     * @param start Start of period.
     * @param end End of period.
     * @param recalculate Recalculate all previous unit costs.
     * @param externalDatabase External database squema name.
     * @throws Exception
     */
    public void calculate(final SGuiSession session, final Date start, final Date end, final boolean recalculate, final String externalDatabase) throws Exception {
        mtDateStart = start;
        mtDateEnd = end;
        mbRecalculate = recalculate;
        
        msAuxCalculationLog = "";
        moAuxMapIdentifiedCostLots.clear();
        maAuxIdentifiedCostDpsRows.clear();
        maAuxIdentifiedCostDpsEntryRows.clear();
        maAuxIdentifiedCostDpsEntrySupplyLotRows.clear();
        maAuxIdentifiedCostLotRows.clear();
        
        addCalculationLogEntry("Calculando costos identificados de ventas "
                + "del " + SLibUtils.DateFormatDate.format(mtDateStart) + (SLibTimeUtils.isSameDate(mtDateStart, mtDateEnd) ? "" : " al " + SLibUtils.DateFormatDate.format(mtDateEnd)) + "...", true);
        
        // calculate unit costs:
        
        addCalculationLogEntry("Obteniendo surtidos externos...", true);
        ArrayList<Supply> supplies = getSupplies(session.getStatement().getConnection(), externalDatabase);
        addCalculationLogEntry("Surtidos externos obtenidos: " + SLibUtils.DecimalFormatInteger.format(supplies.size()) + ".", true);
        
        int count = 0;
        for (Supply supply : supplies) {
            addCalculationLogEntry((++count) + ". " + supply.DocTypeCode + " #" + supply.getDpsNumber() + " " + SLibUtils.DateFormatDate.format(supply.DocDate) + " PK=" + supply.getDpsKeyAsString() + "; "
                    + "MVT #" + supply.SupplyDocNumber + " PK=" + supply.SupplyMovementId + "; MVT Row PK=" + supply.SupplyMovementRowId + "; MVT Row Lot PK=" + supply.SupplyMovementRowLotId + "; "
                    + "Ítem '" + supply.SupplyDocRowItemName + "' Unit '" + supply.SupplyDocRowUnitCode + "' Lot '" + supply.SupplyLotLot + "'...", true);
            
            if (supply.SupplyMovementId == 0) {
                // non-existing supply:
                supply.LotCostUnit = 0;
                supply.CostUnitType = SDbIdentifiedCostLot.COST_UNIT_TP_NA;
                supply.CalculationIssue = SDbIdentifiedCostLot.CALC_ISSUE_SUP;
            }
            else if (supply.getLotKey() == null) {
                // non-existing lot:
                supply.LotCostUnit = 0;
                supply.CostUnitType = SDbIdentifiedCostLot.COST_UNIT_TP_NA;
                supply.CalculationIssue = SDbIdentifiedCostLot.CALC_ISSUE_LOT;
            }
            else {
                SDbIdentifiedCostLot identifiedCostLot = getIdentifiedCostLot(session, supply.getLotKey());
                
                supply.LotCostUnit = identifiedCostLot.getCostUnit();
                supply.CostUnitType = identifiedCostLot.getCostUnitType();
                supply.CalculationIssue = identifiedCostLot.getCalculationIssue();
            }
        }
        
        // prepare unit costs for rendering:
        
        addCalculationLogEntry("Preparando costos identificados para su exhibición...", true);
        HashMap<String, SRowIdentifiedCostDps> dpsRows = new HashMap<>(); // key: DPS key as String (0-0), value: row object
        HashMap<String, SRowIdentifiedCostDpsEntry> dpsEntryRows = new HashMap<>(); // key: DPS entry key as String (0-0-0), value: row object
        HashMap<String, SRowIdentifiedCostLot> lotRows = new HashMap<>(); // key: lot key as String (0-0-0), value: row object

        for (Supply supply : supplies) {
            // documents:

            SRowIdentifiedCostDps dpsRow = dpsRows.get(supply.getDpsKeyAsString());

            if (dpsRow == null) {
                dpsRow = new SRowIdentifiedCostDps(supply);
                dpsRows.put(supply.getDpsKeyAsString(), dpsRow);
            }

            dpsRow.addDocCost(supply.getSupplyMovementRowLotCost());

            // document entries:

            SRowIdentifiedCostDpsEntry dpsEntryRow = dpsEntryRows.get(supply.getDpsEntryKeyAsString());

            if (dpsEntryRow == null) {
                dpsEntryRow = new SRowIdentifiedCostDpsEntry(supply);
                dpsEntryRows.put(supply.getDpsEntryKeyAsString(), dpsEntryRow);
            }

            dpsEntryRow.addDocEntryCost(supply.getSupplyMovementRowLotCost());

            // document entry supply row lots:

            maAuxIdentifiedCostDpsEntrySupplyLotRows.add(new SRowIdentifiedCostDpsEntrySupplyLot(supply));

            // lots:

            if (supply.getLotKey() != null) {
                SRowIdentifiedCostLot lotRow = lotRows.get(supply.getLotKeyAsString());

                if (lotRow == null) {
                    lotRow = new SRowIdentifiedCostLot(supply);
                    lotRows.put(supply.getLotKeyAsString(), lotRow);
                }
            }
        }
        
        maAuxIdentifiedCostDpsRows.addAll(dpsRows.values());
        maAuxIdentifiedCostDpsEntryRows.addAll(dpsEntryRows.values());
        maAuxIdentifiedCostLotRows.addAll(lotRows.values());
        
        // update as well children identified cost for lots:
        
        maChildrenIdentifiedCostLots.clear();
        maChildrenIdentifiedCostLots.addAll(moAuxMapIdentifiedCostLots.values());
        
        addCalculationLogEntry("El cálculo ha finalizado.", true);
    }
    
    public class Supply {
        
        // document:
        
        public int DocYearId;
        public int DocDocId;
        public int DocCategoryId;
        public int DocClassId;
        public int DocTypeId;
        public String DocTypeCode;
        public String DocSeries;
        public String DocNumber;
        public Date DocDate;
        public double DocSubtotal;
        public double DocSubtotalCy;
        public int DocCurrencyId;
        public String DocCurrencyCode;
        public int DocBizPartnerId;
        public String DocBizPartnerName;
        
        // document entry:
        
        public int DocEntryYearId;
        public int DocEntryDocId;
        public int DocEntryEntryId;
        public String DocEntryConceptKey;
        public String DocEntryConcept;
        public double DocEntryQuantity;
        public double DocEntrySubtotal;
        public double DocEntrySubtotalCy;
        public int DocEntryItemId;
        public int DocEntryUnitId;
        
        // external supply:
        
        public int SupplyFactor;
        
        // external supplied document:
        
        public int SupplyDocId;
        public String SupplyDocSeries;
        public String SupplyDocNumber;
        public Date SupplyDocDate;
        
        // external supplied document entry:
        
        public int SupplyDocRowId;
        public double SupplyDocRowQuantity;
        public String SupplyDocRowItemCode;
        public String SupplyDocRowItemName;
        public String SupplyDocRowUnitCode;
        public String SupplyDocRowUnitName;
        
        // external supply movement:
        
        public int SupplyMovementId;
        public String SupplyMovementFolio;
        public Date SupplyMovementDate;
        
        // external supply movement row:
        
        public int SupplyMovementRowId;
        public double SupplyMovementRowQuantity;
        
        // external supply movement row lot:
        
        public int SupplyMovementRowLotId;
        public double SupplyMovementRowLotQuantity;
        public int SupplyLotId;
        public String SupplyLotLot;
        public Date SupplyLotExpiration;
        
        // lot:
        
        public int LotItemId;
        public int LotUnitId;
        public int LotLotId;
        public String LotLot;
        public Date LotExpiration;
        public boolean LotBlocked;
        public boolean LotDeleted;
        
        // lot unit cost:
        
        public double LotCostUnit;
        public String CostUnitType;
        public String CalculationIssue;
        
        public Supply() {
            reset();
        }
        
        public void reset() {
            DocYearId = 0;
            DocDocId = 0;
            DocCategoryId = 0;
            DocClassId = 0;
            DocTypeId = 0;
            DocTypeCode = "";
            DocSeries = "";
            DocNumber = "";
            DocDate = null;
            DocSubtotal = 0;
            DocSubtotalCy = 0;
            DocCurrencyId = 0;
            DocCurrencyCode = "";
            DocBizPartnerId = 0;
            DocBizPartnerName = "";

            DocEntryYearId = 0;
            DocEntryDocId = 0;
            DocEntryEntryId = 0;
            DocEntryConceptKey = "";
            DocEntryConcept = "";
            DocEntryQuantity = 0;
            DocEntrySubtotal = 0;
            DocEntrySubtotalCy = 0;
            DocEntryItemId = 0;
            DocEntryUnitId = 0;

            SupplyFactor = 0;

            SupplyDocId = 0;
            SupplyDocSeries = "";
            SupplyDocNumber = "";
            SupplyDocDate = null;

            SupplyDocRowId = 0;
            SupplyDocRowQuantity = 0;
            SupplyDocRowItemCode = "";
            SupplyDocRowItemName = "";
            SupplyDocRowUnitCode = "";
            SupplyDocRowUnitName = "";

            SupplyMovementId = 0;
            SupplyMovementFolio = "";
            SupplyMovementDate = null;

            SupplyMovementRowId = 0;
            SupplyMovementRowQuantity = 0;

            SupplyMovementRowLotId = 0;
            SupplyMovementRowLotQuantity = 0;
            SupplyLotId = 0;
            SupplyLotLot = "";
            SupplyLotExpiration = null;

            LotItemId = 0;
            LotUnitId = 0;
            LotLotId = 0;
            LotLot = "";
            LotExpiration = null;
            LotBlocked = false;
            LotDeleted = false;
            
            LotCostUnit = 0;
            CostUnitType = "";
            CalculationIssue = "";
        }
        
        public int[] getDpsKey() {
            return new int[] { DocYearId, DocDocId };
        }
        
        public String getDpsKeyAsString() {
            return SLibUtils.textKey(getDpsKey());
        }
        
        public String getDpsNumber() {
            return STrnUtils.formatDocNumber(DocSeries, DocNumber);
        }
        
        public int[] getDpsEntryKey() {
            return new int[] { DocEntryYearId, DocEntryDocId, DocEntryEntryId };
        }
        
        public String getDpsEntryKeyAsString() {
            return SLibUtils.textKey(getDpsEntryKey());
        }
        
        /**
         * Get lot key. Can be <code>null</code>.
         * @return 
         */
        public int[] getLotKey() {
            if (LotItemId == 0 || LotItemId == 0 || LotLotId == 0) {
                LotItemId = 0;
                LotUnitId = 0;
                LotLotId = 0;
                return null;
            }
            else {
                return new int[] { LotItemId, LotUnitId, LotLotId };
            }
        }
        
        /**
         * Get lot key as <code>String</code>. Can be empty if lot is <code>null</code>.
         * @return 
         */
        public String getLotKeyAsString() {
            int[] lotKey = getLotKey();
            return lotKey == null ? "" : SLibUtils.textKey(lotKey);
        }
        
        public double getSupplyMovementRowLotCost() {
            return SLibUtils.roundAmount(LotCostUnit * SupplyMovementRowLotQuantity);
        }
    }
}
