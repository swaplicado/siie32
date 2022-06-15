/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.trn.db;

import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.mcfg.data.SDataParamsCompany;
import erp.mitm.data.SDataItem;
import erp.mmfg.data.SDataProductionOrder;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mtrn.data.SDataDiog;
import erp.mtrn.data.SDataDiogEntry;
import erp.mtrn.data.STrnStockMove;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Néstor Ávalos
 */
public class SDbInventoryValuationXXX extends SDbRegistryUser {

    protected int mnPkInventoryValuationId;
    //protected boolean mbDeleted;
    protected int mnFkYearYearId;
    protected int mnFkYearPeriodId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    protected boolean mbAuxIsConsistent;
    protected boolean mbAuxExistInventoryMoves;
    protected boolean mbAuxExistInventoryPurchasingExpenses;
    protected boolean mbAuxExistPurchasingExpenses;
    protected boolean mbAuxExistInventoryMfgExpenses;
    protected boolean mbAuxExistMfgExpenses;
    protected String msAuxAccountMask;
    protected String msAuxMfgCostUnitType;
    protected String msAuxMfgCostUnitTypeSql;
    protected String msAuxPeriodQuery;
    protected Date mtAuxDateStart;
    protected Date mtAuxDateEnd;
    protected int[] manAuxRawMaterialWarehouseKey;

    public SDbInventoryValuationXXX() {
        super(SModConsts.TRN_INV_VAL);
    }

    /*
     * Private methods
     */

    private SDataDiog createDiog(final SGuiSession session, final int[] keyDiogType, final int[] keyWarehouse, final int idLot, final int idItem, final int idUnit, final Date date, final int[] keyOrder, final double value) throws Exception {
        SDataDiog diog = null;
        SDataDiogEntry diogEntry = null;
        STrnStockMove stockMove = null;

        diog = new SDataDiog();
        diog.setPkYearId(mnFkYearYearId);
        diog.setDate(date);
        diog.setNumberSeries("");
        diog.setNumber("0");
        diog.setReference("");
        diog.setValue_r(SLibUtils.round(value, SLibUtils.DecimalFormatValue2D.getMaximumFractionDigits()));
        diog.setIsSystem(true);
        diog.setFkDiogCategoryId(keyDiogType[0]);
        diog.setFkDiogClassId(keyDiogType[1]);
        diog.setFkDiogTypeId(keyDiogType[2]);
        diog.setFkDiogAdjustmentTypeId(SModSysConsts.TRNU_TP_IOG_ADJ_NA);
        diog.setFkCompanyBranchId(keyWarehouse[0]);
        diog.setFkWarehouseId(keyWarehouse[1]);
        diog.setFkMfgYearId_n(keyOrder[0]);
        diog.setFkMfgOrderId_n(keyOrder[1]);
        diog.setFkMaintMovementTypeId(SModSysConsts.TRNS_TP_MAINT_MOV_NA);
        diog.setFkMaintUserId_n(SLibConsts.UNDEFINED);
        diog.setFkMaintUserSupervisorId(SModSysConsts.TRN_MAINT_USER_SUPV_NA);
        diog.setFkMaintReturnUserId_n(SLibConsts.UNDEFINED);
        diog.setFkMaintReturnUserSupervisorId(SModSysConsts.TRN_MAINT_USER_SUPV_NA);
        diog.setFkUserShippedId(SUtilConsts.USR_NA_ID);
        diog.setFkUserAuditedId(SUtilConsts.USR_NA_ID);
        diog.setFkUserAuthorizedId(SUtilConsts.USR_NA_ID);
        diog.setFkUserNewId(session.getUser().getPkUserId());
        diog.setFkUserEditId(SUtilConsts.USR_NA_ID);
        diog.setFkUserDeleteId(SUtilConsts.USR_NA_ID);

        diogEntry = new SDataDiogEntry();

        diogEntry.setQuantity(0d); // registry with quantity 0
        diogEntry.setIsInventoriable(true);
        diogEntry.setFkItemId(idItem);
        diogEntry.setFkUnitId(idUnit);
        diogEntry.setFkOriginalUnitId(idUnit);
        diogEntry.setFkUserNewId(session.getUser().getPkUserId());
        diogEntry.setFkUserEditId(SUtilConsts.USR_NA_ID);
        diogEntry.setFkUserDeleteId(SUtilConsts.USR_NA_ID);
        diogEntry.setValue(SLibUtils.round(value, SLibUtils.DecimalFormatValue2D.getMaximumFractionDigits()));
        diogEntry.setValueUnitary(SLibUtils.round(value, SLibUtils.DecimalFormatValue8D.getMaximumFractionDigits()));
        diogEntry.setOriginalValueUnitary(SLibUtils.round(value, SLibUtils.DecimalFormatValue8D.getMaximumFractionDigits()));

        stockMove = new STrnStockMove(new int[] { mnFkYearYearId, idItem, idUnit, SLibConstants.UNDEFINED, keyWarehouse[0], keyWarehouse[1] }, 0, diogEntry.getValue());
        diogEntry.getAuxStockMoves().add(stockMove);

        diog.getDbmsEntries().add(diogEntry);
        diog.save(session.getDatabase().getConnection());

        return diog;
    }

    private void deleteDiogMoves(final SGuiSession session, final int[] typeDiogIn) throws SQLException {
        String sql = "";
        Statement statement = session.getDatabase().getConnection().createStatement();

        sql = "UPDATE trn_stk SET b_del=1 "
                + "WHERE fid_ct_iog=" + typeDiogIn[0] + " AND fid_cl_iog=" + typeDiogIn[1] + " AND fid_tp_iog=" + typeDiogIn[2] + " AND "
                + "dt " + msAuxPeriodQuery + " AND b_del=0 ";
        statement.executeUpdate(sql);

        sql = "UPDATE trn_diog SET b_del=1, fid_usr_del=" + session.getUser().getPkUserId() + ", ts_del=NOW() "
                + "WHERE fid_ct_iog=" + typeDiogIn[0] + " AND fid_cl_iog=" + typeDiogIn[1] + " AND fid_tp_iog=" + typeDiogIn[2] + " AND "
                + "dt " + msAuxPeriodQuery + " AND b_del=0 ";
        statement.executeUpdate(sql);
    }

    private int readCostCenterByLinkType(final SGuiSession session, final int id_item) throws SQLException {
        ArrayList<Integer> idCc = new ArrayList<Integer>();
        ArrayList<int[]> aLinkType = new ArrayList<int[]>();

        Statement statement = session.getDatabase().getConnection().createStatement();
        Statement statementCc = session.getDatabase().getConnection().createStatement();
        ResultSet resultSet = null;
        ResultSet resultSetCc = null;
        HashMap<Integer, Integer> mapCc = new HashMap<Integer, Integer>();

        // Read configuration for item:

        msSql = "SELECT id_tp_link, id_ref " +
                "FROM mfgu_line_cfg_item " +
                "WHERE b_del = 0 " +
                "ORDER BY id_tp_link ASC ";
        resultSet = statement.executeQuery(msSql);
        while (resultSet.next()) {
            aLinkType.add(new int[] { resultSet.getInt(1), resultSet.getInt(2) });
        }

        // Read item by link type:

        for (int[] linkType : aLinkType) {
            switch(linkType[0]) {
                case SDataConstantsSys.TRNS_TP_LINK_ITEM:
                    msSql = "SELECT l.fid_cc, c.id_tp_link, c.id_ref, c.id_line " +
                        "FROM mfgu_line_cfg_item AS c " +
                        "INNER JOIN mfgu_line AS l ON " +
                        "c.id_line = l.id_line " +
                        "INNER JOIN erp.itmu_item AS i ON " +
                        "c.id_ref = i.id_item " +
                        "WHERE i.id_item = " + id_item + "; ";
                    break;

                case SDataConstantsSys.TRNS_TP_LINK_MFR:
                    msSql = "SELECT l.fid_cc, c.id_tp_link, c.id_ref, c.id_line " +
                    "FROM mfgu_line_cfg_item AS c, mfgu_line AS l, erp.itmu_mfr AS mfr " +
                    "INNER JOIN erp.itmu_item AS i ON " +
                    "mfr.id_mfr = i.fid_mfr " +
                    "INNER JOIN mfgu_line AS l ON " +
                    "c.id_line = l.id_line " +
                    "WHERE c.id_tp_link = " + linkType[0] + " AND c.id_ref = " + linkType[1] + " AND  i.id_item = " + id_item + " AND i.fid_mfr = " + linkType[1] + "; ";
                    break;

                case SDataConstantsSys.TRNS_TP_LINK_BRD:
                    msSql = "SELECT l.fid_cc, c.id_tp_link, c.id_ref, c.id_line " +
                    "FROM mfgu_line_cfg_item AS c, mfgu_line AS l, erp.itmu_brd AS brd " +
                    "INNER JOIN erp.itmu_item AS i ON " +
                    "brd.id_brd = i.fid_brd " +
                    "INNER JOIN mfgu_line AS l ON " +
                    "c.id_line = l.id_line " +
                    "WHERE c.id_tp_link = " + linkType[0] + " AND c.id_ref = " + linkType[1] + " AND  i.id_item = " + id_item + " AND i.fid_brd = " + linkType[1] + " ";
                    break;
                    
                case SDataConstantsSys.TRNS_TP_LINK_LINE:
                    msSql = "SELECT l.fid_cc, c.id_tp_link, c.id_ref, c.id_line " +
                    "FROM mfgu_line_cfg_item AS c, mfgu_line AS l, erp.itmu_line AS line " +
                    "INNER JOIN erp.itmu_item AS i ON " +
                    "line.id_line = i.fid_line_n " +
                    "INNER JOIN mfgu_line AS l ON " +
                    "c.id_line = l.id_line " +
                    "WHERE c.id_tp_link = " + linkType[0] + " AND c.id_ref = " + linkType[1] + " AND  i.id_item = " + id_item + " AND i.fid_line_n = " + linkType[1] + " ";
                    break;
                    
                case SDataConstantsSys.TRNS_TP_LINK_IGEN:
                    msSql = "SELECT l.fid_cc, c.id_tp_link, c.id_ref, c.id_line " +
                    "FROM mfgu_line_cfg_item AS c, mfgu_line AS l, erp.itmu_igen AS gen " +
                    "INNER JOIN erp.itmu_item AS i ON " +
                    "gen.id_igen = i.fid_igen " +
                    "WHERE c.id_line = l.id_line AND c.id_tp_link = " + linkType[0] + " AND c.id_ref = " + linkType[1] + " AND  i.id_item = " + id_item + " AND i.fid_igen = " + linkType[1] + " ";
                    break;
                    
                case SDataConstantsSys.TRNS_TP_LINK_IGRP:
                    msSql = "SELECT l.fid_cc, c.id_tp_link, c.id_ref, c.id_line " +
                    "FROM mfgu_line_cfg_item AS c, mfgu_line AS l, erp.itmu_igrp AS grp " +
                    "INNER JOIN erp.itmu_igen AS gen ON " +
                    "grp.id_igrp = gen.fid_igrp " +
                    "INNER JOIN erp.itmu_item AS i ON " +
                    "gen.id_igen = i.fid_igen " +
                    "INNER JOIN mfgu_line AS l ON " +
                    "c.id_line = l.id_line " +
                    "WHERE c.id_tp_link = " + linkType[0] + " AND c.id_ref = " + linkType[1] + " AND  i.id_item = " + id_item + " AND gen.fid_igrp = " + linkType[1] + " ";
                    break;
                    
                case SDataConstantsSys.TRNS_TP_LINK_IFAM:
                    msSql = "SELECT l.fid_cc, c.id_tp_link, c.id_ref, c.id_line " +
                    "FROM mfgu_line_cfg_item AS c, mfgu_line AS l, erp.itmu_ifam AS fam " +
                    "INNER JOIN erp.itmu_igrp AS grp ON " +
                    "fam.id_ifam = grp.fid_ifam " +
                    "INNER JOIN erp.itmu_igen AS gen ON " +
                    "gen.fid_igrp = grp.id_igrp " +
                    "INNER JOIN erp.itmu_item AS i ON " +
                    "gen.id_igen = i.fid_igen " +
                    "INNER JOIN mfgu_line AS l ON " +
                    "c.id_line = l.id_line " +
                    "WHERE c.id_tp_link = " + linkType[0] + " AND c.id_ref = " + linkType[1] + " AND  i.id_item = " + id_item + " AND grp.fid_ifam = " + linkType[1] + " ";
                    break;
                    
                case SDataConstantsSys.TRNS_TP_LINK_TP_ITEM:
                    msSql = "SELECT l.fid_cc, c.id_tp_link, c.id_ref, c.id_line " +
                    "FROM mfgu_line_cfg_item AS c, erp.itms_tp_item AS tp " +
                    "INNER JOIN erp.itmu_igen AS gen ON " +
                    "gen.fid_tp_item = tp.id_tp_item " +
                    "INNER JOIN erp.itmu_item AS i ON " +
                    "gen.id_igen = i.fid_igen " +
                    "INNER JOIN mfgu_line AS l ON " +
                    "c.id_line = l.id_line " +
                    "WHERE c.id_tp_link = " + linkType[0] + " AND c.id_ref = " + linkType[1] + " AND  i.id_item = " + id_item + " AND gen.fid_tp_item = " + linkType[1] + " ";
                    break;
                    
                case SDataConstantsSys.TRNS_TP_LINK_CL_ITEM:
                    msSql = "SELECT l.fid_cc, c.id_tp_link, c.id_ref, c.id_line " +
                    "FROM mfgu_line_cfg_item AS c, erp.itms_cl_item AS cl " +
                    "INNER JOIN erp.itmu_igen AS gen ON " +
                    "gen.fid_cl_item = cl.id_cl_item " +
                    "INNER JOIN erp.itmu_item AS i ON " +
                    "gen.id_igen = i.fid_igen " +
                    "INNER JOIN mfgu_line AS l ON " +
                    "c.id_line = l.id_line " +
                    "WHERE c.id_tp_link = " + linkType[0] + " AND c.id_ref = " + linkType[1] + " AND  i.id_item = " + id_item + " AND gen.fid_cl_item = " + linkType[1] + " ";
                    break;
                    
                case SDataConstantsSys.TRNS_TP_LINK_CT_ITEM:
                    msSql = "SELECT l.fid_cc, c.id_tp_link, c.id_ref, c.id_line " +
                    "FROM mfgu_line_cfg_item AS c, erp.itms_ct_item AS ct " +
                    "INNER JOIN erp.itmu_igen AS gen ON " +
                    "gen.fid_ct_item = ct.id_ct_item " +
                    "INNER JOIN erp.itmu_item AS i ON " +
                    "gen.id_igen = i.fid_igen " +
                    "INNER JOIN mfgu_line AS l ON " +
                    "c.id_line = l.id_line " +
                    "WHERE c.id_tp_link = " + linkType[0] + " AND c.id_ref = " + linkType[1] + " AND  i.id_item = " + id_item + " AND gen.fid_ct_item = " + linkType[1] + " " ;
                    break;

                case SDataConstantsSys.TRNS_TP_LINK_ALL:
                    msSql = "SELECT l.fid_cc, c.id_tp_link, c.id_ref, c.id_line " +
                    "FROM mfgu_line_cfg_item AS c " +
                    "INNER JOIN mfgu_line AS l ON " +
                    "c.id_line = l.id_line " +
                    "WHERE c.b_del = 0 AND c.id_ref = 0; ";
                    break;
                    
                default:
                    msSql = "";
            }

            resultSetCc = statementCc.executeQuery(msSql);
            while (resultSetCc.next()) {
                mapCc.put(resultSetCc.getInt("c.id_tp_link"), resultSetCc.getInt("l.fid_cc"));
            }
        }

        for (int i = SDataConstantsSys.TRNS_TP_LINK_ITEM; i >= SDataConstantsSys.TRNS_TP_LINK_ALL; i--) {
            if (mapCc.get(i) != null) {
                idCc.add(mapCc.get(i));
                break;
            }
        }

        return idCc.size() > SLibConsts.UNDEFINED ? idCc.get(idCc.size()-1) : SLibConsts.UNDEFINED;
    }

    @SuppressWarnings("unchecked")
    private void allocateMfgExpenses(final SGuiSession session) throws Exception {
        boolean ban = false;
        double dValue = 0;
        double dTotalAssigned = 0;
        double dTotalProductionExpense = 0;
        double dTotalItemUnitOrder = 0;
        double dTotalItemUnit = 0;
        String sProductionOrders = "";
        int numberOfPo = 0;

        SDataDiog diog = null;

        int idCc = 0;
        ArrayList<Integer> anIdCc = new ArrayList<>();
        ArrayList<SDataDiog> apportionDiogs = new ArrayList<>();
        ArrayList<SDataDiog> apportionDiogsAux = new ArrayList<>();
        ArrayList<SDataProductionOrder> aProductionOrders = new ArrayList<>();

        Statement statementCenterCostAmount = session.getDatabase().getConnection().createStatement();
        Statement statementCenterCostProduction = session.getDatabase().getConnection().createStatement();
        Statement statementItemUnitOrder = session.getDatabase().getConnection().createStatement();
        ResultSet resultSetItemUnit = null;
        ResultSet resultSetItemUnitOrder = null;
        ResultSet resultSetCenterCost = null;

        // 1. Delete Manufacturing Expenses for the msAuxPeriodQuery:

        deleteDiogMoves(session, SModSysConsts.TRNS_TP_IOG_IN_EXP_MFG);

        // Obtain productions orders:

        aProductionOrders.clear();
        aProductionOrders = obtainProductionOrders(session, mtAuxDateStart, false, true);
        sProductionOrders = aProductionOrders.size() > 0 ? "(" : "";
        for (SDataProductionOrder order : aProductionOrders) {
            ++numberOfPo;
            sProductionOrders += "(o.id_year = " + order.getPkYearId() + " AND o.id_ord = " + order.getPkOrdId() + ")";
            sProductionOrders += numberOfPo < aProductionOrders.size() ? " OR " : ")";
        }

        // Validate if there is manufacturing units per item-unit-warehouse-order for the msAuxPeriodQuery:

        msSql = composeSqlQueryMfgAmount(SLibConsts.UNDEFINED, SLibConsts.UNDEFINED, false, SLibConsts.UNDEFINED, true, sProductionOrders);
        resultSetItemUnit = statementCenterCostProduction.executeQuery(msSql);
        if (resultSetItemUnit.next()) {

            resultSetItemUnit.beforeFirst();
            while (resultSetItemUnit.next()) {

                // Obtain value for CC:

                idCc = readCostCenterByLinkType(session, resultSetItemUnit.getInt("s.id_item"));

                ban = true;
                for (int cc : anIdCc) {
                    if (idCc == cc) {
                        ban = false;
                        break;
                    }
                }

                if (ban) {

                    anIdCc.add(idCc);
                }
            }

            for (int cc : anIdCc) {

                // 3. Determine the total net GF by cost center in the current PC (ie, debits and credits in accounting policies by GF to the cost center in question).

                msSql = "SELECT SUM(re.debit - re.credit) AS f_bal " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS re ON " +
                    "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS acc ON " +
                    "re.fid_acc = acc.id_acc " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON " +
                    "re.fid_item_n = i.id_item " +
                    "WHERE r.b_del = 0 AND re.b_del = 0 AND " +
                    "(SELECT COUNT(*) " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " " +
                    "WHERE id_acc = CONCAT( " +
                    "LEFT(acc.id_acc, INSTR(acc.id_acc, '-') -1), " +
                    "RIGHT('" + msAuxAccountMask + "', LENGTH(acc.id_acc) - (INSTR(acc.id_acc, '-') -1))) AND " +
                    "fid_tp_acc_r = " + SDataConstantsSys.FINS_CLS_ACC_EXPEN_MFG[0] + " AND " +
                    "fid_cl_acc_r = " + SDataConstantsSys.FINS_CLS_ACC_EXPEN_MFG[1] + " AND " +
                    "fid_cls_acc_r = " + SDataConstantsSys.FINS_CLS_ACC_EXPEN_MFG[2] + ") > 0 AND " +
                    "re.fk_cc_n = " + cc + " AND " +
                    "r.dt " + msAuxPeriodQuery + " " +
                    "GROUP BY re.fk_cc_n " +
                    "ORDER BY re.fk_cc_n ";
                resultSetCenterCost = statementCenterCostAmount.executeQuery(msSql);
                if (resultSetCenterCost.next()) {

                    dTotalAssigned = 0;
                    dTotalProductionExpense = resultSetCenterCost.getDouble("f_bal");

                    resultSetCenterCost.beforeFirst();
                    while (resultSetCenterCost.next()) {

                        apportionDiogsAux.clear();

                        // 2. Determine the available inventory manufactured (PP and PT) in equivalent units per item-warehouse-OP in the current PC (ie, initial inventory + net PP and PT and PT PP deliveries).

                        msSql = composeSqlQueryMfgAmount(SLibConstants.UNDEFINED, SLibConstants.UNDEFINED, true, cc, true, sProductionOrders);
                        resultSetItemUnit = statementCenterCostProduction.executeQuery(msSql);
                        while (resultSetItemUnit.next()) {

                            // 4. GF apportion and allocate inventories (ie, create inputs System type INPUT PRODUCTION COST) for each item-unit-warehouse-OP, in warehouses of origin of manufacture (eg, PP).

                            msSql = composeSqlQueryMfgAmount(SLibConstants.UNDEFINED, SLibConstants.UNDEFINED, false, cc, true, sProductionOrders);
                            resultSetItemUnitOrder = statementItemUnitOrder.executeQuery(msSql);
                            while (resultSetItemUnitOrder.next()) {

                                //if (resultSetItemUnitOrder.getDouble("f_total") < SLibConsts.UNDEFINED) { // The product was generate in work process XXX jbarajas

                                dTotalItemUnitOrder = resultSetItemUnitOrder.getDouble("f_total");
                                dTotalItemUnit = resultSetItemUnit.getDouble("f_total") * (resultSetItemUnit.getDouble("f_total") < SLibConsts.UNDEFINED ? -1 : 1);
                                dValue = SLibUtils.round(resultSetCenterCost.getDouble("f_bal") * (dTotalItemUnitOrder / dTotalItemUnit), SLibUtils.DecimalFormatValue2D.getMaximumFractionDigits());

                                diog = createDiog(session, SModSysConsts.TRNS_TP_IOG_IN_EXP_MFG, 
                                        new int[] { resultSetItemUnitOrder.getInt("s.id_cob"), resultSetItemUnitOrder.getInt("s.id_wh") }, 
                                        SDataConstantsSys.TRNX_STK_LOT_DEF_ID, resultSetItemUnitOrder.getInt("s.id_item"), resultSetItemUnitOrder.getInt("s.id_unit"), 
                                        SLibTimeUtils.createDate(mnFkYearYearId, mnFkYearPeriodId, 1), 
                                        new int[] { resultSetItemUnitOrder.getInt("d.fid_mfg_year_n"), resultSetItemUnitOrder.getInt("d.fid_mfg_ord_n") }, 
                                        dValue);
                                
                                dTotalAssigned += dValue;
                                apportionDiogsAux.add(diog);
                                //}
                            }
                        }
                    }
                }

                if ((dTotalProductionExpense - dTotalAssigned) != 0) {

                    apportionDiogs(session, apportionDiogsAux, (dTotalProductionExpense - dTotalAssigned));
                }
            }

            apportionDiogs.addAll(apportionDiogsAux);
        }
    }

    private String composeSqlQueryMfgAmount(final int idItem, final int idUnit, final boolean bItemUnit, final int idCc, final boolean bCostAssigned, final String sProductionOrders) {
        /*
        return "SELECT s.dt, s.id_item, s.id_unit, s.id_lot, s.id_cob, s.id_wh, d.fid_mfg_year_n, d.fid_mfg_ord_n, COALESCE((" +
            obtainQueryInitialInventoryManufacturing(mnFkYearYearId, SLibTimeUtils.createDate(mnFkYearYearId, mnFkYearPeriodId, 1), idItem, idUnit, bItemUnit) + "),0) + " +
            "(SUM(((s.mov_in - s.mov_out) * " +
            "(IF ((d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_RET[0] + " AND " +
            "d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_RET[1] + " AND " +
            "d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_RET[2] + ") OR (" +
            "d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_RET[0] + " AND " +
            "d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_RET[1] + " AND " +
            "d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_RET[2] + "), -1, 1)))" +
            (msAuxMfgCostUnitTypeSql.isEmpty() ? "" : "* ") +  msAuxMfgCostUnitTypeSql + ")) AS f_total " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS de ON " +
            "d.id_year = de.id_year AND d.id_doc = de.id_doc " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s ON " +
            "de.id_year = s.fid_diog_year AND de.id_doc = s.fid_diog_doc AND de.id_ety = s.fid_diog_ety " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON " +
            "i.id_item = de.fid_item " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS u ON " +
            "u.id_unit = de.fid_unit " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MFG_ORD) + " AS o ON " +
            "d.fid_mfg_year_n = o.id_year AND d.fid_mfg_ord_n = o.id_ord " +
            (idItem == SLibConsts.UNDEFINED && idUnit == SLibConsts.UNDEFINED ? "" :
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MFGU_LINE) + " AS cl ON " +
            "cl.fid_cc = " + idCc + " ") +
            "WHERE d.b_del = 0 AND de.b_del = 0 AND  s.b_del = 0 AND o.b_del = 0 AND " +
            "((d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_ASD[0] + " AND " +
            "d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_ASD[1] + " AND " +
            "d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_ASD[2] + ") OR " +
            "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_RET[0] + " AND " +
            "d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_RET[1] + " AND " +
            "d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_RET[2] + ") OR " +
            "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_ASD[0] + " AND " +
            "d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_ASD[1] + " AND " +
            "d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_ASD[2] + ") OR " +
            "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_RET[0] + " AND " +
            "d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_RET[1] + " AND " +
            "d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_RET[2] + ")) " +
            // (bCostAssigned ? "AND o.fid_inv_val_n IS NOT NULL " : "") + " " +
            (idItem == SLibConsts.UNDEFINED && idUnit == SLibConsts.UNDEFINED ? "" :
                "AND s.id_item = " + idItem + " AND s.id_unit = " + idUnit + " ") +
            (sProductionOrders.isEmpty() ? "" : "AND " + sProductionOrders) + " " +
            (bItemUnit ? "" : "GROUP BY s.id_item, s.id_unit, s.id_cob, s.id_wh, o.id_year, o.id_ord ") + " " +
            "ORDER BY " +
            (idItem == SLibConsts.UNDEFINED && idUnit == SLibConsts.UNDEFINED ?
                "s.id_item, s.id_unit, s.id_cob, s.id_wh, o.id_year, o.id_ord " :
                "s.id_item, s.id_unit, o.id_year, o.id_ord ");
        */
        return "SELECT s.dt, s.id_item, s.id_unit, s.id_lot, s.id_cob, s.id_wh, d.fid_mfg_year_n, d.fid_mfg_ord_n, COALESCE((" +
            composeSqlQueryMfgStartingInventory(mnFkYearYearId, SLibTimeUtils.createDate(mnFkYearYearId, mnFkYearPeriodId, 1), idItem, idUnit, bItemUnit) + "),0) + " +
            "SUM(((s.mov_in - s.mov_out) * -1) * " +  msAuxMfgCostUnitTypeSql + ") AS f_total " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS de ON " +
            "d.id_year = de.id_year AND d.id_doc = de.id_doc " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s ON " +
            "de.id_year = s.fid_diog_year AND de.id_doc = s.fid_diog_doc AND de.id_ety = s.fid_diog_ety " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON " +
            "i.id_item = de.fid_item " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS u ON " +
            "u.id_unit = de.fid_unit " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MFG_ORD) + " AS o ON " +
            "d.fid_mfg_year_n = o.id_year AND d.fid_mfg_ord_n = o.id_ord " +
            (idItem == SLibConsts.UNDEFINED && idUnit == SLibConsts.UNDEFINED ? "" :
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MFGU_LINE) + " AS cl ON " +
            "cl.fid_cc = " + idCc + " ") +
            "WHERE d.b_del = 0 AND de.b_del = 0 AND  s.b_del = 0 AND o.b_del = 0 AND " +
            "((d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_ASD[0] + " AND " +
            "d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_ASD[1] + " AND " +
            "d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_ASD[2] + ") OR " +
            "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_ASD[0] + " AND " +
            "d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_ASD[1] + " AND " +
            "d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_ASD[2] + ") OR " +
            "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_RET[0] + " AND " +
            "d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_RET[1] + " AND " +
            "d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_RET[2] + ") OR " +
            "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_FG_RET[0] + " AND " +
            "d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_FG_RET[1] + " AND " +
            "d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_FG_RET[2] + ")) " +
            (idItem == SLibConsts.UNDEFINED && idUnit == SLibConsts.UNDEFINED ? "" :
                "AND s.id_item = " + idItem + " AND s.id_unit = " + idUnit + " ") +
            (sProductionOrders.isEmpty() ? "" : "AND " + sProductionOrders) + " " +
            (bItemUnit ? "" : "GROUP BY s.id_item, s.id_unit, s.id_cob, s.id_wh, o.id_year, o.id_ord ") + " " +
            "ORDER BY " +
            (idItem == SLibConsts.UNDEFINED && idUnit == SLibConsts.UNDEFINED ?
                "s.id_item, s.id_unit, s.id_cob, s.id_wh, o.id_year, o.id_ord " :
                "s.id_item, s.id_unit, o.id_year, o.id_ord ");
    }

    private String composeSqlQueryMfgStartingInventory(final int year, final Date dateStart, final int idItem, final int idUnit, final boolean bItemUnit) {
        return "SELECT SUM((ss.mov_in - ss.mov_out) * " + msAuxMfgCostUnitTypeSql + ") AS f_total " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS sd " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS sde ON " +
            "sd.id_year = sde.id_year AND sd.id_doc = sde.id_doc " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS ss ON " +
            "sde.id_year = ss.fid_diog_year AND sde.id_doc = ss.fid_diog_doc AND sde.id_ety = ss.fid_diog_ety " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON " +
            "i.id_item = sde.fid_item " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS su ON " +
            "su.id_unit = sde.fid_unit " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_IGEN) + " AS ig ON " +
            "i.fid_igen = ig.id_igen " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMS_TP_ITEM) + " AS t ON " +
            "ig.fid_ct_item = t.id_ct_item AND ig.fid_cl_item = t.id_cl_item AND ig.fid_tp_item = t.id_tp_item " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MFG_ORD) + " AS so ON " +
            "sd.fid_mfg_year_n = so.id_year AND sd.fid_mfg_ord_n = so.id_ord " +
            "WHERE sd.b_del = 0 AND sde.b_del = 0 AND  ss.b_del = 0 AND so.b_del = 0 AND " +
            "((ig.fid_ct_item = " + SModSysConsts.ITMS_TP_ITEM_SAL_PRO_FG[0] + " AND ig.fid_cl_item = " + SModSysConsts.ITMS_TP_ITEM_SAL_PRO_FG[1] + " AND ig.fid_tp_item = " + SModSysConsts.ITMS_TP_ITEM_SAL_PRO_FG[2] + ") OR " +
            "(ig.fid_ct_item = " + SModSysConsts.ITMS_TP_ITEM_SAL_PRO_WP[0] + " AND ig.fid_cl_item = " + SModSysConsts.ITMS_TP_ITEM_SAL_PRO_WP[1] + " AND ig.fid_tp_item = " + SModSysConsts.ITMS_TP_ITEM_SAL_PRO_WP[2] + ")) AND " +
            "YEAR(sd.dt) = " + year + " AND " +
            "sd.dt < '" +  SLibUtils.DbmsDateFormatDate.format(dateStart) + "' AND " +
            "ss.id_item = s.id_item AND " +
            "ss.id_unit = s.id_unit AND " +
            "ss.id_cob = s.id_cob AND " +
            "ss.id_wh = s.id_wh AND " +
            "so.id_year = o.id_year AND " +
            "so.id_ord = o.id_ord " +
            (idItem == SLibConsts.UNDEFINED && idUnit == SLibConsts.UNDEFINED ? "" :
                " AND ss.id_item = " + idItem + " AND ss.id_unit = " + idUnit + " ") +
            (bItemUnit ? "" : "GROUP BY s.id_item, s.id_unit, s.id_cob, s.id_wh, o.id_year, o.id_ord ") +
            "ORDER BY " +
            (idItem == SLibConsts.UNDEFINED && idUnit == SLibConsts.UNDEFINED ?
                "ss.id_item, ss.id_unit, ss.id_cob, ss.id_wh, so.id_year, so.id_ord " :
                "ss.id_item, ss.id_unit ");
    }

    @SuppressWarnings("unchecked")
    private void computeInventoryValuation(final SGuiSession session) throws Exception {
        boolean bInventoryMovesPending = false;
        boolean simpleDiog = false;
        int nPkInventoryValuationPreviousId = 0;
        int[] anProductionOrderId = null;

        SDataProductionOrder productionOrderAux1 = null;
        SDataDiog diog = null;
        SDataDiog diogAux = null;
        SDataDiog diogInput = null;
        SDataDiog diogOutput = null;

        SDbInventoryValuationXXX inventoryValuationAux = null;

        Statement statement = session.getDatabase().getConnection().createStatement();
        ResultSet resultSet = null;

        ArrayList<SDataProductionOrder> aProductionOrders = new ArrayList<>();
        ArrayList<SDataDiog> aDiogs = new ArrayList<>();

        // 1. Define current calculation msAuxPeriodQuery (year/month):

        // 2. Delete calculation of the current calculation msAuxPeriodQuery:
        //  Obtain previous id of CVI:

        msSql = "SELECT COALESCE(MAX(id_inv_val), 0) " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_INV_VAL) + " " +
                "WHERE b_del = 0 AND id_inv_val <> " + mnPkInventoryValuationId + " AND fk_year_year = " + mnFkYearYearId + " AND fk_year_per = " + mnFkYearPeriodId + "; ";
        resultSet = statement.executeQuery(msSql);
        if (resultSet.next()) {

            nPkInventoryValuationPreviousId = resultSet.getInt(1);
        }

        if (nPkInventoryValuationPreviousId != SLibConsts.UNDEFINED) {

            // Preserve ID of the current PC, will be used later:
            // Delete CVI of the current PC:

            inventoryValuationAux = (SDbInventoryValuationXXX) session.readRegistry(SModConsts.TRN_INV_VAL, new int[] { nPkInventoryValuationPreviousId });
            inventoryValuationAux.delete(session);
        }

        // 3. Validate whether you can make the calculation of inventory valuation:
        // (This validation is in function canSave)

        // 4. Detach all PO associated with the former CVI current PC, if available:

        detachAllProductionOrderFromValuationInventory(session, nPkInventoryValuationPreviousId);

        // 5. Allocate  purchasing costs:

        allocateExpensesPurchasing(session);

        // 6. Allocate manufacturing costs:

        allocateMfgExpenses(session);

        // 7. Obtain information for CIV:
        // 7.1. Obtain all current WM of CP ordened as follows: date, warehouse type, simple input, composed input, compused output, simple output, diog type, number serie

        aDiogs.addAll(obtainDiogs(session));

        // 7.2. Create a list of PO with WM in the current CP:

        aProductionOrders.clear();
        aProductionOrders.addAll(obtainProductionOrders(session, mtAuxDateStart, true, false));

        // 8. Perform CIV, regardless of the MCIV configured for the company:

        for (int i=0; i<aDiogs.size(); i++) {
            diog = aDiogs.get(i);

            // 8.1. Each WM Input change status:

            simpleDiog = isSimpleDiog(new int[] { diog.getFkDiogCategoryId(), diog.getFkDiogClassId(), diog.getFkDiogTypeId() });
            if (diog.getFkDiogCategoryId() == SModSysConsts.TRNS_CT_IOG_IN) {

                // Initialize with zero cost:

                if (!isSimpleDiogByUser(new int[] { diog.getFkDiogCategoryId(), diog.getFkDiogClassId(), diog.getFkDiogTypeId() }) &&
                    !SLibUtils.compareKeys(new int[] { diog.getFkDiogCategoryId(), diog.getFkDiogClassId(), diog.getFkDiogTypeId() }, SModSysConsts.TRNS_TP_IOG_IN_EXP_PUR) &&
                    !SLibUtils.compareKeys(new int[] { diog.getFkDiogCategoryId(), diog.getFkDiogClassId(), diog.getFkDiogTypeId() }, SModSysConsts.TRNS_TP_IOG_IN_EXP_MFG)) {

                    diogAux = assignDiogOutputCosts(session, diog, diog.getDate(), true);
                    diog = diogAux.clone();
                }

                diog.setCostAsigned(SModSysConsts.TRNX_DIOG_CST_ASIG_NO);
                diog.setCostTransferred(SModSysConsts.TRNX_DIOG_CST_TRAN_NA);
            }

            // 8.2. Each WM Output change status:

            if (diog.getFkDiogCategoryId() == SModSysConsts.TRNS_CT_IOG_OUT) {

                // Obtain production order if is possible:

                productionOrderAux1 = null;
                if (diog.getFkMfgYearId_n() != SLibConsts.UNDEFINED &&
                        diog.getFkMfgOrderId_n() != SLibConsts.UNDEFINED) {

                    productionOrderAux1 = new SDataProductionOrder();
                    productionOrderAux1.read(new int[] { diog.getFkMfgYearId_n(), diog.getFkMfgOrderId_n() }, session.getStatement());
                }

                // Validate if the inventory movement if not assigned the cost in previous periods:

                if (simpleDiog ||
                    (diog.getFkMfgYearId_n() == SLibConsts.UNDEFINED &&
                        diog.getFkMfgOrderId_n() == SLibConsts.UNDEFINED) ||
                    (productionOrderAux1 != null &&
                        productionOrderAux1.getIsCostDone() ==  SModSysConsts.MFGX_ORD_CST_DONE_NO)) {

                    // Initialize with zero cost:

                    diogAux = assignDiogOutputCosts(session, diog, diog.getDate(), true);
                    diog = diogAux.clone();
                    diog.setCostAsigned(SModSysConsts.TRNX_DIOG_CST_ASIG_NO);
                }
                else {

                    diog.setCostAsigned(SModSysConsts.TRNX_DIOG_CST_ASIG_YES);
                }

                if (simpleDiog) {
                    diog.setCostTransferred(SModSysConsts.TRNX_DIOG_CST_TRAN_NA);
                }
                else {
                    diog.setCostTransferred(SModSysConsts.TRNX_DIOG_CST_TRAN_NO);
                }
            }

            diog.save(session.getDatabase().getConnection());
            aDiogs.set(i, diog);
        }

        // 8.4. WM process the current CP, beginning to end date DÍA_MÍNIMO current CP:

        for (int i=0; i<aDiogs.size(); i++) {
            diogOutput = aDiogs.get(i);

            if (diogOutput.getFkDiogCategoryId() == SModSysConsts.TRNS_CT_IOG_OUT) {

                // Read the lastest version of the database (diogOutput):

                diogOutput.read((int[]) diogOutput.getPrimaryKey(), statement);

                diogInput = null;
                diogAux = null;
                productionOrderAux1 = null;

                // Inventory move simple:

                if (isSimpleDiog(new int[] { diogOutput.getFkDiogCategoryId(), diogOutput.getFkDiogClassId(), diogOutput.getFkDiogTypeId() })) {
                    if (diogOutput.getCostAsigned() == SModSysConsts.TRNX_DIOG_CST_ASIG_NO) {

                        diogAux = assignDiogOutputCosts(session, diogOutput, diogOutput.getDate(), false);
                        diogOutput = diogAux.clone();
                        diogOutput.setCostAsigned(SModSysConsts.TRNX_DIOG_CST_ASIG_YES);
                        diogOutput.setCostTransferred(SModSysConsts.TRNX_DIOG_CST_TRAN_YES);
                        diogOutput.save(session.getDatabase().getConnection());
                    }
                }
                else {

                    // Raw Material inventory move:

                    if (SLibUtils.compareKeys(new int[] { diogOutput.getFkDiogCategoryId(), diogOutput.getFkDiogClassId(), diogOutput.getFkDiogTypeId() }, SModSysConsts.TRNS_TP_IOG_OUT_MFG_RM_ASD) ||
                            SLibUtils.compareKeys(new int[] { diogOutput.getFkDiogCategoryId(), diogOutput.getFkDiogClassId(), diogOutput.getFkDiogTypeId() }, SModSysConsts.TRNS_TP_IOG_OUT_MFG_RM_RET))  {

                        // For diog output:

                        if (diogOutput.getCostAsigned() == SModSysConsts.TRNX_DIOG_CST_ASIG_NO) {

                            diogAux = assignDiogOutputCosts(session, diogOutput, diogOutput.getDate(), false);
                            diogOutput = diogAux.clone();
                            diogOutput.setCostAsigned(SModSysConsts.TRNX_DIOG_CST_ASIG_YES);
                            diogOutput.save(session.getDatabase().getConnection());
                        }

                        // For diog input (mirror):

                        diogInput = new SDataDiog();
                        diogInput.read(new int[] { diogOutput.getFkDiogYearId_n(), diogOutput.getFkDiogDocId_n() }, session.getStatement());
                        diogAux = assignDiogInputMirrorCosts(diogOutput, diogInput);
                        diogInput = diogAux.clone();
                        diogInput.setCostAsigned(SModSysConsts.TRNX_DIOG_CST_ASIG_YES);
                        diogOutput.setDbmsDataCounterpartDiog(diogInput);
                        diogOutput.setCostTransferred(SModSysConsts.TRNX_DIOG_CST_TRAN_YES);
                        diogOutput.save(session.getDatabase().getConnection());

                        anProductionOrderId = null;
                        if (SLibUtils.compareKeys(new int[] { diogOutput.getFkDiogCategoryId(), diogOutput.getFkDiogClassId(), diogOutput.getFkDiogTypeId() }, SModSysConsts.TRNS_TP_IOG_OUT_MFG_RM_ASD)) {

                            // Obtain production order destiny ID:

                            if (diogInput.getFkMfgYearId_n() > SLibConsts.UNDEFINED &&
                                diogInput.getFkMfgOrderId_n() > SLibConsts.UNDEFINED) {

                                anProductionOrderId = new int[] { diogInput.getFkMfgYearId_n(), diogInput.getFkMfgOrderId_n() };
                            }
                        }
                        else {
                            // Obtain production order source ID:

                            if (diogOutput.getFkMfgYearId_n() > SLibConsts.UNDEFINED &&
                                diogOutput.getFkMfgOrderId_n() > SLibConsts.UNDEFINED) {

                                anProductionOrderId = new int[] { diogOutput.getFkMfgYearId_n(), diogOutput.getFkMfgOrderId_n() };
                            }
                        }

                        // Read production order:

                        if (anProductionOrderId != null) {

                            verifyMfgCostsProductionOrder(
                                    session,
                                    anProductionOrderId,
                                    new int[] { diogInput.getPkYearId(), diogInput.getPkDocId() },
                                    new int[] { diogOutput.getPkYearId(), diogOutput.getPkDocId() },
                                    "BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(diogOutput.getDate()) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtAuxDateEnd) + "' ",
                                    mnFkYearYearId,
                                    diogOutput.getDate());
                        }
                    }
                    else if (SLibUtils.compareKeys(new int[] { diogOutput.getFkDiogCategoryId(), diogOutput.getFkDiogClassId(), diogOutput.getFkDiogTypeId() }, SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_ASD) ||
                            SLibUtils.compareKeys(new int[] { diogOutput.getFkDiogCategoryId(), diogOutput.getFkDiogClassId(), diogOutput.getFkDiogTypeId() }, SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_RET)) {

                        // Work Process inventory move:

                        if (SLibUtils.compareKeys(new int[] { diogOutput.getFkDiogCategoryId(), diogOutput.getFkDiogClassId(), diogOutput.getFkDiogTypeId() }, SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_RET)) {

                            // Assign cost 0 by default:

                            diogAux = assignDiogOutputCosts(session, diogOutput, diogOutput.getDate(), true);
                            diogOutput = diogAux.clone();
                            diogOutput.setCostAsigned(SModSysConsts.TRNX_DIOG_CST_ASIG_YES);
                            diogOutput.save(session.getDatabase().getConnection());
                        }

                        // Read diogInput:

                        diogInput = new SDataDiog();
                        diogInput.read(new int[] { diogOutput.getFkDiogYearId_n(), diogOutput.getFkDiogDocId_n() }, session.getStatement());

                        anProductionOrderId = null;
                        if (SLibUtils.compareKeys(new int[] { diogOutput.getFkDiogCategoryId(), diogOutput.getFkDiogClassId(), diogOutput.getFkDiogTypeId() }, SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_ASD)) {

                            // Obtain production order source ID:

                            if (diogOutput.getFkMfgYearId_n() > SLibConsts.UNDEFINED &&
                                diogOutput.getFkMfgOrderId_n() > SLibConsts.UNDEFINED) {

                                anProductionOrderId = new int[] { diogOutput.getFkMfgYearId_n(), diogOutput.getFkMfgOrderId_n() };
                            }
                        }
                        else {
                            // Obtain production order destiny ID:

                            if (diogInput.getFkMfgYearId_n() > SLibConsts.UNDEFINED &&
                                diogInput.getFkMfgOrderId_n() > SLibConsts.UNDEFINED) {

                                anProductionOrderId = new int[] { diogInput.getFkMfgYearId_n(), diogInput.getFkMfgOrderId_n() };
                            }
                        }

                        // Read production order:

                        if (anProductionOrderId != null) {

                            verifyMfgCostsProductionOrder(session,
                                    anProductionOrderId,
                                    new int[] { diogInput.getPkYearId(), diogInput.getPkDocId() },
                                    new int[] { diogOutput.getPkYearId(), diogOutput.getPkDocId() },
                                    "BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(diogOutput.getDate()) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtAuxDateEnd) + "' ",
                                    mnFkYearYearId,
                                    diogOutput.getDate());

                            // Read productionOrder, diogInput and diogOut if update data in before process:

                            diogInput = new SDataDiog();
                            diogInput.read(new int[] { diogOutput.getFkDiogYearId_n(), diogOutput.getFkDiogDocId_n() }, session.getStatement());

                            diogOutput.read(new int[] { diogOutput.getPkYearId(), diogOutput.getPkDocId() }, session.getStatement());
                        }

                        // For diog output:

                        if (diogOutput.getCostAsigned() == SModSysConsts.TRNX_DIOG_CST_ASIG_YES &&
                                diogOutput.getCostTransferred() == SModSysConsts.TRNX_DIOG_CST_TRAN_NO) {

                             // For diog input (mirror):

                            if (diogInput == null) {

                                diogInput = new SDataDiog();
                                diogInput.read(new int[] { diogOutput.getFkDiogYearId_n(), diogOutput.getFkDiogDocId_n() }, session.getStatement());
                            }

                            diogAux = assignDiogInputMirrorCosts(diogOutput, diogInput);
                            diogInput = diogAux.clone();
                            diogInput.setCostAsigned(SModSysConsts.TRNX_DIOG_CST_ASIG_YES);
                            diogOutput.setDbmsDataCounterpartDiog(diogInput);
                            diogOutput.setCostTransferred(SModSysConsts.TRNX_DIOG_CST_TRAN_YES);
                            diogOutput.save(session.getDatabase().getConnection());
                        }

                        anProductionOrderId = null;
                        if (SLibUtils.compareKeys(new int[] { diogOutput.getFkDiogCategoryId(), diogOutput.getFkDiogClassId(), diogOutput.getFkDiogTypeId() }, SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_ASD)) {

                            // Obtain production order destiny ID:

                            if (diogInput.getFkMfgYearId_n() > SLibConsts.UNDEFINED &&
                                diogInput.getFkMfgOrderId_n() > SLibConsts.UNDEFINED) {

                                anProductionOrderId = new int[] { diogInput.getFkMfgYearId_n(), diogInput.getFkMfgOrderId_n() };
                            }
                        }
                        else {
                            // Obtain production order source ID:

                            if (diogOutput.getFkMfgYearId_n() > SLibConsts.UNDEFINED &&
                                diogOutput.getFkMfgOrderId_n() > SLibConsts.UNDEFINED) {

                                anProductionOrderId = new int[] { diogOutput.getFkMfgYearId_n(), diogOutput.getFkMfgOrderId_n() };
                            }
                        }

                        // Read production order destiny:

                        if (anProductionOrderId != null) {

                            verifyMfgCostsProductionOrder(session,
                                    anProductionOrderId,
                                    new int[] { diogInput.getPkYearId(), diogInput.getPkDocId() },
                                    new int[] { diogOutput.getPkYearId(), diogOutput.getPkDocId() },
                                    "BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(diogOutput.getDate()) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtAuxDateEnd) + "' ",
                                    mnFkYearYearId,
                                    diogOutput.getDate());
                        }
                    }
                    else if (SLibUtils.compareKeys(new int[] { diogOutput.getFkDiogCategoryId(), diogOutput.getFkDiogClassId(), diogOutput.getFkDiogTypeId() }, SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_ASD) ||
                            SLibUtils.compareKeys(new int[] { diogOutput.getFkDiogCategoryId(), diogOutput.getFkDiogClassId(), diogOutput.getFkDiogTypeId() }, SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_RET)) {

                        // Finished good:

                        if (SLibUtils.compareKeys(new int[] { diogOutput.getFkDiogCategoryId(), diogOutput.getFkDiogClassId(), diogOutput.getFkDiogTypeId() }, SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_RET)) {

                            // Assign cost 0 by default:

                            diogAux = assignDiogOutputCosts(session, diogOutput, diogOutput.getDate(), true);
                            diogOutput = diogAux.clone();
                            diogOutput.setCostAsigned(SModSysConsts.TRNX_DIOG_CST_ASIG_YES);
                            diogOutput.save(session.getDatabase().getConnection());
                        }

                        // Read diogInput:

                        diogInput = new SDataDiog();
                        diogInput.read(new int[] { diogOutput.getFkDiogYearId_n(), diogOutput.getFkDiogDocId_n() }, session.getStatement());

                        anProductionOrderId = null;
                        if (SLibUtils.compareKeys(new int[] { diogOutput.getFkDiogCategoryId(), diogOutput.getFkDiogClassId(), diogOutput.getFkDiogTypeId() }, SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_ASD)) {

                            // Obtain production order source ID:

                            if (diogOutput.getFkMfgYearId_n() > SLibConsts.UNDEFINED &&
                                diogOutput.getFkMfgOrderId_n() > SLibConsts.UNDEFINED) {

                                anProductionOrderId = new int[] { diogOutput.getFkMfgYearId_n(), diogOutput.getFkMfgOrderId_n() };
                            }
                        }
                        else {
                            // Obtain production order destiny ID:

                            if (diogInput.getFkMfgYearId_n() > SLibConsts.UNDEFINED &&
                                diogInput.getFkMfgOrderId_n() > SLibConsts.UNDEFINED) {

                                anProductionOrderId = new int[] { diogInput.getFkMfgYearId_n(), diogInput.getFkMfgOrderId_n() };
                            }
                        }

                        // Read production order:

                        if (anProductionOrderId != null) {

                            verifyMfgCostsProductionOrder(session,
                                    anProductionOrderId,
                                    new int[] { diogInput.getPkYearId(), diogInput.getPkDocId() },
                                    new int[] { diogOutput.getPkYearId(), diogOutput.getPkDocId() },
                                    "BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(diogOutput.getDate()) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtAuxDateEnd) + "' ",
                                    mnFkYearYearId,
                                    diogOutput.getDate());

                            diogInput = new SDataDiog();
                            diogInput.read(new int[] { diogOutput.getFkDiogYearId_n(), diogOutput.getFkDiogDocId_n() }, session.getStatement());

                            diogOutput.read(new int[] { diogOutput.getPkYearId(), diogOutput.getPkDocId() }, session.getStatement());
                        }

                        // For diog output:

                        if (diogOutput.getCostAsigned() == SModSysConsts.TRNX_DIOG_CST_ASIG_YES &&
                                diogOutput.getCostTransferred() == SModSysConsts.TRNX_DIOG_CST_TRAN_NO) {

                             // For diog input (mirror):

                            if (diogInput == null) {

                                diogInput = new SDataDiog();
                                diogInput.read(new int[] { diogOutput.getFkDiogYearId_n(), diogOutput.getFkDiogDocId_n() }, session.getStatement());
                            }

                            diogAux = assignDiogInputMirrorCosts(diogOutput, diogInput);
                            diogInput = diogAux.clone();
                            diogInput.setCostAsigned(SModSysConsts.TRNX_DIOG_CST_ASIG_YES);
                            diogOutput.setDbmsDataCounterpartDiog(diogInput);
                            diogOutput.setCostTransferred(SModSysConsts.TRNX_DIOG_CST_TRAN_YES);
                            diogOutput.save(session.getDatabase().getConnection());
                        }
                    }
                    else {
                        // Composite outputs (transfers, conversion...):

                        if (diogOutput.getCostAsigned() == SModSysConsts.TRNX_DIOG_CST_ASIG_NO) {

                            diogAux = assignDiogOutputCosts(session, diogOutput, diogOutput.getDate(), false);
                            diogOutput = diogAux.clone();
                            diogOutput.setCostAsigned(SModSysConsts.TRNX_DIOG_CST_ASIG_YES);
                            diogOutput.save(session.getDatabase().getConnection());

                            // For diog input (mirror):

                            diogInput = new SDataDiog();
                            diogInput.read(new int[] { diogOutput.getFkDiogYearId_n(), diogOutput.getFkDiogDocId_n() }, session.getStatement());
                            diogAux = assignDiogInputMirrorCosts(diogOutput, diogInput);
                            diogInput = diogAux.clone();
                            diogInput.setCostAsigned(SModSysConsts.TRNX_DIOG_CST_ASIG_YES);
                            diogOutput.setDbmsDataCounterpartDiog(diogInput);
                            diogOutput.setCostTransferred(SModSysConsts.TRNX_DIOG_CST_TRAN_YES);
                            diogOutput.save(session.getDatabase().getConnection());
                        }
                    }
                }
            }
        }

        // 8.5. Initialize variable control InventoryMovesPending with FALSE:

        bInventoryMovesPending = false;

        // 8.6. Check the WM of current CP:

        aDiogs.clear();
        aDiogs.addAll(obtainDiogs(session));
        for (SDataDiog diogObject : aDiogs) {

            if ((isSimpleDiog(new int[] { diogObject.getFkDiogCategoryId(), diogObject.getFkDiogClassId(), diogObject.getFkDiogTypeId() }) &&
                    diogObject.getFkDiogCategoryId() == SModSysConsts.TRNS_CT_IOG_OUT &&
                    diogObject.getCostAsigned() == SModSysConsts.TRNX_DIOG_CST_ASIG_NO) ||
                (!isSimpleDiog(new int[] { diogObject.getFkDiogCategoryId(), diogObject.getFkDiogClassId(), diogObject.getFkDiogTypeId() }) &&
                    diogObject.getFkDiogCategoryId() == SModSysConsts.TRNS_CT_IOG_IN &&
                    diogObject.getCostAsigned() == SModSysConsts.TRNX_DIOG_CST_ASIG_NO) ||
                (!isSimpleDiog(new int[] { diogObject.getFkDiogCategoryId(), diogObject.getFkDiogClassId(), diogObject.getFkDiogTypeId() }) &&
                    diogObject.getFkDiogCategoryId() == SModSysConsts.TRNS_CT_IOG_OUT &&
                    (diogObject.getCostAsigned() == SModSysConsts.TRNX_DIOG_CST_ASIG_NO ||
                    diogObject.getCostTransferred() == SModSysConsts.TRNX_DIOG_CST_TRAN_NO))) {

                    bInventoryMovesPending = true;
                    break;
            }
        }

        // 8.7. Verify if control variable bInventoryMovesPending is FALSE:

        if (bInventoryMovesPending) {

            msQueryResult = "Error: no se pudieron procesar todos los movimientos de inventarios.";
            throw new Exception(msQueryResult);
        }

        // After finished valuation process, update timeStamp for registry:

        statement.executeUpdate("UPDATE " + SModConsts.TablesMap.get(SModConsts.TRN_INV_VAL) +
            " SET "+ (mbRegistryNew ?  "ts_usr_ins = NOW(), " : "") + "ts_usr_upd = NOW() WHERE id_inv_val = " + mnPkInventoryValuationId + "; ");
    }

    private ArrayList<SDataDiog> obtainDiogs(final SGuiSession session) throws Exception {
        SDataDiog diog = null;
        ArrayList<SDataDiog> aDiogs = new ArrayList<SDataDiog>();

        ResultSet resultSet = null;
        Statement statement = session.getDatabase().getConnection().createStatement();

        msSql = "SELECT DISTINCT d.id_year, d.id_doc, " +
            "COALESCE(CASE d.fid_ct_iog " +
            "WHEN " + SModSysConsts.TRNS_CT_IOG_IN + " THEN " +
            "CASE d.fid_cl_iog " +
            "WHEN " + SModSysConsts.TRNS_CL_IOG_IN_PUR[1] + " THEN 1 " +
            "WHEN " + SModSysConsts.TRNS_CL_IOG_IN_SAL[1] + " THEN 1 " +
            "WHEN " + SModSysConsts.TRNS_CL_IOG_IN_ADJ[1] + " THEN 1 " +
            "WHEN " + SModSysConsts.TRNS_CL_IOG_IN_EXT[1] + " THEN 1 " +
            "WHEN " + SModSysConsts.TRNS_CL_IOG_IN_INT[1] + " THEN 2 " +
            "WHEN " + SModSysConsts.TRNS_CL_IOG_IN_MFG[1] + " THEN " +
            "CASE d.fid_tp_iog " +
            "WHEN " + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_ASD[2] + " THEN 2 " +
            "WHEN " + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_RET[2] + " THEN 2 " +
            "WHEN " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_ASD[2] + " THEN 2 " +
            "WHEN " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_RET[2] + " THEN 2 " +
            "WHEN " + SModSysConsts.TRNS_TP_IOG_IN_MFG_FG_ASD[2] + " THEN 2 " +
            "WHEN " + SModSysConsts.TRNS_TP_IOG_IN_MFG_FG_RET[2] + " THEN 2 " +
            "WHEN " + SModSysConsts.TRNS_TP_IOG_IN_MFG_CON[2] + " THEN 1 " +
            "END " +
            "END " +
            "WHEN " + SModSysConsts.TRNS_CT_IOG_OUT + " THEN " +
            "CASE d.fid_cl_iog " +
            "WHEN " + SModSysConsts.TRNS_CL_IOG_OUT_PUR[1] + " THEN 4 " +
            "WHEN " + SModSysConsts.TRNS_CL_IOG_OUT_SAL[1] + " THEN 4 " +
            "WHEN " + SModSysConsts.TRNS_CL_IOG_OUT_ADJ[1] + " THEN 4 " +
            "WHEN " + SModSysConsts.TRNS_CL_IOG_OUT_EXT[1] + " THEN 4 " +
            "WHEN " + SModSysConsts.TRNS_CL_IOG_OUT_INT[1] + " THEN 3 " +
            "WHEN " + SModSysConsts.TRNS_CL_IOG_OUT_MFG[1] + " THEN " +
            "CASE d.fid_tp_iog " +
            "WHEN " + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_ASD[2] + " THEN 3 " +
            "WHEN " + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_RET[2] + " THEN 3 " +
            "WHEN " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_ASD[2] + " THEN 3 " +
            "WHEN " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_RET[2] + " THEN 3 " +
            "WHEN " + SModSysConsts.TRNS_TP_IOG_IN_MFG_FG_ASD[2] + " THEN 3 " +
            "WHEN " + SModSysConsts.TRNS_TP_IOG_IN_MFG_FG_RET[2] + " THEN 3 " +
            "WHEN " + SModSysConsts.TRNS_TP_IOG_IN_MFG_CON[2] + " THEN 4 " +
            "END " +
            "END " +
            "END, 1) AS f_val " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS de ON " +
            "d.id_year = de.id_year AND d.id_doc = de.id_doc " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNS_TP_IOG) + " AS t ON " +
            "d.fid_ct_iog = t.id_ct_iog AND d.fid_cl_iog = t.id_cl_iog AND d.fid_tp_iog = t.id_tp_iog " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s ON " +
            "de.id_year = s.fid_diog_year AND de.id_doc = s.fid_diog_doc AND de.id_ety = s.fid_diog_ety " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_COB_ENT) + " AS c ON " +
            "s.id_cob = c.id_cob AND s.id_wh = c.id_ent " +
            "WHERE d.b_del = 0 AND de.b_del = 0 AND s.b_del = 0 AND d.dt " + msAuxPeriodQuery + " AND (" +
            "d.fid_tp_iog <> " + SModSysConsts.TRNS_TP_IOG_IN_MFG_CON[2] + " OR " +
            "d.fid_tp_iog <> " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_CON[2] + ") " +
            "ORDER BY d.dt, c.fid_ct_ent, c.fid_tp_ent, f_val, d.fid_ct_iog, d.fid_cl_iog, d.fid_tp_iog, d.num_ser, d.num ";
        resultSet = statement.executeQuery(msSql);
        while (resultSet.next()) {

            diog = new SDataDiog();
            diog.read(new int[] { resultSet.getInt("d.id_year"), resultSet.getInt("d.id_doc") }, session.getStatement());
            aDiogs.add(diog);
        }

        return aDiogs;
    }

    private void detachAllProductionOrderFromValuationInventory(final SGuiSession session, final int nPkInventoryStockPrevious) throws Exception {
        Statement statement = session.getDatabase().getConnection().createStatement();

        msSql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.MFG_ORD) + " AS o " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d ON " +
            "o.id_year = d.fid_mfg_year_n AND o.id_ord = d.fid_mfg_ord_n " +
            "INNER JOIN trn_inv_val AS v ON " +
            "o.fid_inv_val_n = v.id_inv_val " +
            "SET fid_inv_val_n = NULL " +
            "WHERE o.b_del = 0 AND d.b_del = 0 AND v.id_inv_val = " +  nPkInventoryStockPrevious + " AND v.fk_year_year = " + mnFkYearYearId + " AND v.fk_year_per = " + mnFkYearPeriodId + "; ";
        statement.executeUpdate(msSql);
    }

    private ArrayList obtainProductionOrders(final SGuiSession session, final Date dateStart, final boolean validateProductionOrder, final boolean bDiogTypeRawMaterial) throws Exception {
        boolean bAux = false;
        ArrayList<SDataProductionOrder> aProductionOrders = new ArrayList<SDataProductionOrder>();

        SDataProductionOrder productionOrder = null;
        SDataProductionOrder productionOrderAux2 = null;

        ResultSet resultSet = null;
        Statement statement = session.getDatabase().getConnection().createStatement();

         msSql = "SELECT d.fid_mfg_year_n, d.fid_mfg_ord_n, SUM(s.mov_in - s.mov_out) AS f_stock " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS de ON " +
                "d.id_year = de.id_year AND d.id_doc = de.id_doc " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s ON " +
                "de.id_year = s.fid_diog_year AND de.id_doc = s.fid_diog_doc AND de.id_ety = s.fid_diog_ety " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MFG_ORD) + " AS o ON " +
                "d.fid_mfg_year_n = o.id_year AND d.fid_mfg_ord_n = o.id_ord " +
                "WHERE d.b_del = 0 AND de.b_del = 0 AND s.b_del = 0 AND o.b_del = 0 AND " +
                (!bDiogTypeRawMaterial ? "" :
                 "((d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_ASD[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_ASD[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_ASD[2] + ") OR " +
                "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_ASD[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_ASD[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_ASD[2] + ") OR " +
                "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_RET[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_RET[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_RET[2] + ") OR " +
                "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_RET[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_RET[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_RET[2] + ")) AND ") + " " +
                "d.dt " + msAuxPeriodQuery + " " +
                "GROUP BY d.fid_mfg_year_n, d.fid_mfg_ord_n " +
                "ORDER BY d.fid_mfg_year_n, d.fid_mfg_ord_n ";
        resultSet = statement.executeQuery(msSql);
        while (resultSet.next()) {

            productionOrder = new SDataProductionOrder();
            productionOrder.read(new int[] { resultSet.getInt(1), resultSet.getInt(2) }, session.getStatement());

            // 7.2.1. Update status MCC for OP if is necessary:

            if (validateProductionOrder) {

                bAux = false;
                switch (productionOrder.getIsCostDone()) {
                    case SModSysConsts.MFGX_ORD_CST_DONE_NA:

                        bAux = true;
                        break;
                    case SModSysConsts.MFGX_ORD_CST_DONE_YES:

                        // Validate if is cost done for the previous period:

                        productionOrderAux2 = obtainProductionOrderPreviousStatusCost(session, productionOrder.getPkYearId(), productionOrder.getPkOrdId(), dateStart, null);
                        if (productionOrderAux2 != null) {
                            if (productionOrderAux2.getIsCostDone() != SModSysConsts.MFGX_ORD_CST_DONE_YES) {
                                bAux = true;
                            }
                        }
                        else {
                            bAux = true;
                        }

                        break;

                }
                if (bAux) {

                    // Change status MCC of production order to no-made:

                    productionOrder.setIsCostDone(SModSysConsts.MFGX_ORD_CST_DONE_NO);
                }

                // 7.2.2. Integrate arrayList with the relation with others production orders:

                productionOrder.getAuxProductionOrdersRelations().addAll(productionOrderObtainRelations(session, productionOrder.getPkYearId(), productionOrder.getPkOrdId()));

                // 7.2.3. Validate isn't circular referent between production orders:

                for (SDataProductionOrder orderRelation : productionOrder.getAuxProductionOrdersRelations()) {

                    if (SLibUtils.compareKeys(productionOrder.getPrimaryKey(), orderRelation.getPrimaryKey())) {

                        msQueryResult = "Error: existe una relación circular con la orden de producción #'" + productionOrder.getNumber() + "'.";
                        throw new Exception(msQueryResult);
                    }
                }

                // Save productionOrder:

                productionOrder.save(session.getDatabase().getConnection());
            }

            aProductionOrders.add(productionOrder);
        }

        return aProductionOrders;
    }

    private double diogEntriesSumValues(final SDataDiog diog) {
        double dValue_r = 0;

        for (SDataDiogEntry entry : diog.getDbmsEntries()) {

            if (!entry.getIsDeleted()) {
                dValue_r += entry.getValue();
            }
        }

        return SLibUtils.round(dValue_r, SLibUtils.DecimalFormatValue2D.getMaximumFractionDigits());
    }

    private void verifyMfgCostsProductionOrder(final SGuiSession session, final int[] pkProductionOrderId, final int[] pkParamDiogInputId, final int[] pkParamDiogOutputId, final String period, final int year, final Date dateStart) throws Exception {
        SDataDiog diogInput = new SDataDiog();
        SDataDiog diogOutput = new SDataDiog();
        SDataProductionOrder productionOrder = new SDataProductionOrder();
        SDataProductionOrder productionOrderAux = null;

        productionOrder.read(new int[] { pkProductionOrderId[0], pkProductionOrderId[1] }, session.getStatement());
        diogInput.read(new int[] { pkParamDiogInputId[0], pkParamDiogInputId[1] }, session.getStatement());
        diogOutput.read(new int[] { pkParamDiogOutputId[0], pkParamDiogOutputId[1] }, session.getStatement());

        if (productionOrder.getIsCostDone() == SModSysConsts.MFGX_ORD_CST_DONE_NO) {
            if (productionOrder.getIsCostDone() == SModSysConsts.MFGX_ORD_CST_DONE_YES) { /* Exit */ }
            else {
                if (productionOrderVerifyCostsInventoryInputMoves(session, pkProductionOrderId)) { /* Exit */ }
                else {

                    realizeMccProductionOrder(session, year, dateStart, pkProductionOrderId);
                    diogOutput.read(new int[] { pkParamDiogOutputId[0], pkParamDiogOutputId[1] }, session.getStatement());
                    if (diogOutput.getCostAsigned() == SModSysConsts.TRNX_DIOG_CST_ASIG_YES) {

                        for (int i=0; i<productionOrder.getAuxProductionOrdersRelations().size(); i++) {

                             productionOrderAux = productionOrder.getAuxProductionOrdersRelations().get(i);
                             verifyMfgCostsProductionOrder(session, new int[] { productionOrderAux.getPkYearId(), productionOrderAux.getPkOrdId() }, pkParamDiogInputId, pkParamDiogOutputId, period, year, dateStart);
                        }
                    }
                }
            }
        }
    }

    private boolean productionOrderVerifyCostsInventoryInputMoves(final SGuiSession session, final int[] pkProductionOrderId) throws Exception {
        boolean can = false;
        ResultSet resultSet = null;
        Statement statement = session.getDatabase().getConnection().createStatement();

        msSql = "SELECT COALESCE(COUNT(*), 0) " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS de ON " +
                "d.id_year = de.id_year AND d.id_doc = de.id_doc " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MFG_ORD) + " AS o ON " +
                "d.fid_mfg_year_n = o.id_year AND d.fid_mfg_ord_n = o.id_ord " +
                "WHERE d.b_del = 0 AND de.b_del = 0 AND ( " +
                "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_ASD[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_ASD[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_ASD[2] + ") OR " +
                "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_ASD[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_ASD[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_ASD[2] + ") OR " +
                "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_RM_RET[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_RM_RET[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_RM_RET[2] + ") OR " +
                "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_RET[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_RET[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_RET[2] + ")) AND " +
                "d.fid_mfg_year_n = " + pkProductionOrderId[0] + " AND d.fid_mfg_ord_n = " + pkProductionOrderId[1] + " AND " +
                "d.cst_asig = " + SModSysConsts.TRNX_DIOG_CST_ASIG_NO + " " +
                "GROUP BY d.fid_mfg_year_n, d.fid_mfg_ord_n;";
        resultSet = statement.executeQuery(msSql);
        if (resultSet.next()) {
            if (resultSet.getInt(1) > SLibConsts.UNDEFINED) {
                can = true;
            }
        }

        return can;
    }

    private void realizeMccProductionOrder(final SGuiSession session, final int year, final Date dateStart, final int[] pkProductionOrderId) throws Exception {
        double dDiogTotal = 0;
        double dTotalAssigned = 0;
        double dValueUnitaryEquivalent = 0;

        SDataProductionOrder productionOrder = null;
        SDataDiog diogOutput = null;
        SDataDiogEntry diogOutputEntry = null;

        Statement statementMfgTotal = session.getDatabase().getConnection().createStatement();
        Statement statementCostNet = session.getDatabase().getConnection().createStatement();
        Statement statementOutputProducts = session.getDatabase().getConnection().createStatement();

        ResultSet resultSetMfgTotal = null;
        ResultSet resultSetCostNet = null;
        ResultSet resultSetOutputProducts = null;

        ArrayList<SDataDiog> apportionDiogs = new ArrayList<SDataDiog>();

        // Determine the total net quantity manufactured MASS equivalent units (Kg) (i.e. E/D Products):

        msSql = "SELECT s.dt, s.id_item, s.id_unit, s.id_lot, s.id_cob, s.id_wh, COALESCE((" +
            composeSqlQueryStartingInventoryProductionOrderQuantity(year, pkProductionOrderId, dateStart) + "), 0) + " +
            "ABS((SUM((s.mov_in - s.mov_out) * i.mass))) AS f_total, COALESCE((" +
            composeSqlQueryStartingInventoryProductionOrderQuantity(year, pkProductionOrderId, dateStart) + "), 0) + " +
            "ABS((SUM((s.mov_in - s.mov_out)))) AS f_total_item " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS de ON " +
            "d.id_year = de.id_year AND d.id_doc = de.id_doc " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s ON " +
            "de.id_year = s.fid_diog_year AND de.id_doc = s.fid_diog_doc AND de.id_ety = s.fid_diog_ety " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON " +
            "i.id_item = de.fid_item " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS u ON " +
            "u.id_unit = de.fid_unit " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MFG_ORD) + " AS o ON " +
            "d.fid_mfg_year_n = o.id_year AND d.fid_mfg_ord_n = o.id_ord " +
            "WHERE d.b_del = 0 AND de.b_del = 0 AND  s.b_del = 0 AND o.b_del = 0 AND " +
            "d.fid_mfg_year_n = " + pkProductionOrderId[0] + " AND d.fid_mfg_ord_n = " + pkProductionOrderId[1] + " AND " +
            "((d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_ASD[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_ASD[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_ASD[2] + ") OR " +
            "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_ASD[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_ASD[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_ASD[2] + ")) " +
            "GROUP BY d.fid_mfg_year_n, d.fid_mfg_ord_n " +
            "ORDER BY d.fid_mfg_year_n, d.fid_mfg_ord_n ";
        resultSetMfgTotal = statementMfgTotal.executeQuery(msSql);
        if (resultSetMfgTotal.next()) {

            // Determine the cost of MPD (CMPD) Total net (ie, $E E/D Inputs):

            msSql = "SELECT s.dt, s.id_item, s.id_unit, s.id_lot, s.id_cob, s.id_wh, " +
                /*"COALESCE((" +
                (obtainQueryInitialInventoryProductionOrderRMCosts(year, pkProductionOrderId, dateStart)) + "), 0) + "*/ // XXX jbarajas
                "(SUM((s.debit - s.credit) * " +
                "(IF ((d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_RET[0] + " AND " +
                "d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_RET[1] + " AND " +
                "d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_RET[2] + ") OR (" +
                "d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_RET[0] + " AND " +
                "d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_RET[1] + " AND " +
                "d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_RET[2] + "), -1, 1)))) AS f_balance " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS de ON " +
                "d.id_year = de.id_year AND d.id_doc = de.id_doc " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s ON " +
                "de.id_year = s.fid_diog_year AND de.id_doc = s.fid_diog_doc AND de.id_ety = s.fid_diog_ety " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_COB_ENT) + " AS ent ON " +
                "d.fid_cob = ent.id_cob AND d.fid_wh = ent.id_ent " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON " +
                "i.id_item = de.fid_item " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS u ON " +
                "u.id_unit = de.fid_unit " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MFG_ORD) + " AS o ON " +
                "d.fid_mfg_year_n = o.id_year AND d.fid_mfg_ord_n = o.id_ord " +
                "WHERE d.b_del = 0 AND de.b_del = 0 AND  s.b_del = 0 AND o.b_del = 0 AND " +
                "d.fid_mfg_year_n = " + pkProductionOrderId[0] + " AND d.fid_mfg_ord_n = " + pkProductionOrderId[1] + " AND " +
                "((d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_ASD[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_ASD[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_ASD[2] + ") OR " +
                "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_RET[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_RET[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_RET[2] + ") OR " +
                "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_ASD[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_ASD[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_ASD[2] + ") OR " +
                "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_RET[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_RET[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_RET[2] + ") OR " +
                "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_EXP_MFG[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_EXP_MFG[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_EXP_MFG[2] + ")) " +
                "GROUP BY d.fid_mfg_year_n, d.fid_mfg_ord_n " +
                "ORDER BY d.fid_mfg_year_n, d.fid_mfg_ord_n ";
            resultSetCostNet = statementCostNet.executeQuery(msSql);
            if (resultSetCostNet.next()) {

                // Apportion the cost of MPD and assigned to the outputs Products (PP, PT, SP and SD) (ie, determining the $S of each output OP):

                apportionDiogs.clear();
                msSql = "SELECT DISTINCT d.id_year, d.id_doc " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS de ON " +
                    "d.id_year = de.id_year AND d.id_doc = de.id_doc " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s ON " +
                    "de.id_year = s.fid_diog_year AND de.id_doc = s.fid_diog_doc AND de.id_ety = s.fid_diog_ety " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON " +
                    "i.id_item = de.fid_item " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS u ON " +
                    "u.id_unit = de.fid_unit " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MFG_ORD) + " AS o ON " +
                    "d.fid_mfg_year_n = o.id_year AND d.fid_mfg_ord_n = o.id_ord " +
                    "WHERE d.b_del = 0 AND de.b_del = 0 AND  s.b_del = 0 AND o.b_del = 0 AND " +
                    "d.fid_mfg_year_n = " + pkProductionOrderId[0] + " AND d.fid_mfg_ord_n = " + pkProductionOrderId[1] + " AND " +
                    "((d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_ASD[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_ASD[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_ASD[2] + ") OR " +
                    "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_RET[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_RET[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_RET[2] + ") OR " +
                    "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_ASD[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_ASD[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_ASD[2] + ") OR " +
                    "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_RET[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_RET[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_RET[2] + ")) " +
                    "ORDER BY s.id_cob, s.id_wh, s.id_item, s.id_unit ";
                resultSetOutputProducts = statementOutputProducts.executeQuery(msSql);
                while (resultSetOutputProducts.next()) {

                    diogOutput = new SDataDiog();
                    diogOutput.read(new int[] { resultSetOutputProducts.getInt("d.id_year"), resultSetOutputProducts.getInt("d.id_doc") }, session.getStatement());
                    diogOutput.setIsRegistryEdited(true);
                    dDiogTotal = 0d;
                    for (int i=0; i<diogOutput.getDbmsEntries().size(); i++) {

                        diogOutputEntry = diogOutput.getDbmsEntries().get(i);
                        diogOutputEntry.setIsRegistryEdited(true);
                        if (!SLibUtils.belongsTo(new int[] { diogOutput.getFkDiogCategoryId(), diogOutput.getFkDiogClassId(), diogOutput.getFkDiogTypeId() },
                                new int[][] { SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_RET, SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_RET })) {

                            dValueUnitaryEquivalent = SLibUtils.round(
                                    (resultSetMfgTotal.getDouble("f_total") > SLibConsts.UNDEFINED ?
                                        (resultSetCostNet.getDouble("f_balance") / resultSetMfgTotal.getDouble("f_total")) : SLibConsts.UNDEFINED),
                                    SLibUtils.DecimalFormatValue8D.getMaximumFractionDigits());

                            diogOutputEntry.setValueUnitary(SLibUtils.round(
                                    dValueUnitaryEquivalent *
                                    (resultSetMfgTotal.getDouble("f_total_item") > SLibConsts.UNDEFINED ?
                                    resultSetMfgTotal.getDouble("f_total") / resultSetMfgTotal.getDouble("f_total_item") : SLibConsts.UNDEFINED),
                                    SLibUtils.DecimalFormatValue8D.getMaximumFractionDigits()));

                            diogOutputEntry.setOriginalValueUnitary(diogOutputEntry.getValueUnitary());

                            diogOutputEntry.setValue(SLibUtils.round(
                                    diogOutputEntry.getValueUnitary() * diogOutputEntry.getQuantity(),
                                    SLibUtils.DecimalFormatValue2D.getMaximumFractionDigits()));
                        }
                        else {

                            diogOutputEntry.setValueUnitary(SLibConsts.UNDEFINED);
                            diogOutputEntry.setOriginalValueUnitary(SLibConsts.UNDEFINED);
                        }

                        dDiogTotal += diogOutputEntry.getValue();
                        diogOutput.getDbmsEntries().set(i, diogOutputEntry);
                    }

                    diogOutput.setValue_r(SLibUtils.round(dDiogTotal, SLibUtils.DecimalFormatValue2D.getMaximumFractionDigits()));
                    diogOutput.setCostAsigned(SModSysConsts.TRNX_DIOG_CST_ASIG_YES);
                    dTotalAssigned += dDiogTotal;
                    diogOutput.save(session.getDatabase().getConnection());

                    apportionDiogs.add(diogOutput);
                }

                // Validate apportion the purchasing expenses and assign to inventory for each item-unit-lot-warehouse in the warehouses destiny:

                if ((resultSetCostNet.getDouble("f_balance") - dTotalAssigned) != SLibConsts.UNDEFINED) {

                    apportionDiogs(session, apportionDiogs, (resultSetCostNet.getDouble("f_balance") - dTotalAssigned));
                }

                productionOrder = new SDataProductionOrder();
                productionOrder.read(new int[] { pkProductionOrderId[0], pkProductionOrderId[1] }, session.getStatement());
                productionOrder.setIsCostDone(SModSysConsts.MFGX_ORD_CST_DONE_YES);
                productionOrder.setFkInventoryValuationId_n(mnPkInventoryValuationId);
                productionOrder.save(session.getDatabase().getConnection());
            }
        }
    }

    private SDataDiog assignDiogOutputCosts(final SGuiSession session, final SDataDiog diogValuation, final Date dateMinimumDay, final boolean assignZero) throws CloneNotSupportedException, Exception {
        double[] adStockBalance = new double[] { 0d, 0d };
        double[] adStockBalanceInputs = new double[] { 0d, 0d };

        SDataDiog diog = diogValuation.clone();
        SDataDiogEntry diogEntry = null;
        STrnStockMove stockMove = null;

        for (int j=0; j<diog.getDbmsEntries().size(); j++) {

            diogEntry = diog.getDbmsEntries().get(j);
            for (int k=0; k<diogEntry.getAuxStockMoves().size(); k++) {

                stockMove = diogEntry.getAuxStockMoves().get(k);

                if (!assignZero) {

                    adStockBalance = obtainStockBalance(session,
                        stockMove.getPkYearId(), stockMove.getPkItemId(), diogEntry.getFkUnitId(),
                        stockMove.getPkCompanyBranchId(), stockMove.getPkWarehouseId(),
                        dateMinimumDay);

                    adStockBalanceInputs = obtainStockBalanceInputs(session,
                        stockMove.getPkYearId(), stockMove.getPkItemId(), diogEntry.getFkUnitId(),
                        stockMove.getPkCompanyBranchId(), stockMove.getPkWarehouseId(),
                        dateMinimumDay);
                }

                diogEntry.setValueUnitary(
                        SLibUtils.round((adStockBalance[0] + adStockBalanceInputs[0]) > SLibConsts.UNDEFINED ?
                        (adStockBalance[1] + adStockBalanceInputs[1])/(adStockBalance[0] + adStockBalanceInputs[0]) :
                        SLibConsts.UNDEFINED,
                        SLibUtils.DecimalFormatValue8D.getMaximumFractionDigits()));
                diogEntry.setOriginalValueUnitary(diogEntry.getValueUnitary());

                diogEntry.setValue(
                        SLibUtils.round(diogEntry.getValueUnitary() * diogEntry.getQuantity(), SLibUtils.DecimalFormatValue2D.getMaximumFractionDigits()));
            }

            diog.getDbmsEntries().set(j, diogEntry);
        }

        diog.setValue_r(diogEntriesSumValues(diog));
        return diog;
    }

    private SDataDiog assignDiogInputMirrorCosts(final SDataDiog diogValuationOutput, final SDataDiog diogValuationInput) throws CloneNotSupportedException, Exception {
        SDataDiog diogOutput = diogValuationOutput.clone();
        SDataDiog diogInput = diogValuationInput.clone();
        SDataDiogEntry diogOutputEntry = null;
        SDataDiogEntry diogInputEntry = null;
        STrnStockMove stockMoveOutput = null;
        STrnStockMove stockMoveInput = null;

        for (int j=0; j<diogOutput.getDbmsEntries().size(); j++) {

            diogOutputEntry = diogOutput.getDbmsEntries().get(j);
            for (int k=0; k<diogOutputEntry.getAuxStockMoves().size(); k++) {

                stockMoveOutput = diogOutputEntry.getAuxStockMoves().get(k);
                for (int l=0; l<diogInput.getDbmsEntries().size(); l++) {

                    diogInputEntry = diogInput.getDbmsEntries().get(l);
                    for (int m=0; m<diogInputEntry.getAuxStockMoves().size(); m++) {

                        stockMoveInput = diogInputEntry.getAuxStockMoves().get(m);
                        if (stockMoveOutput.getPkYearId() == stockMoveInput.getPkYearId() &&
                                stockMoveOutput.getPkItemId() == stockMoveInput.getPkItemId() &&
                                stockMoveOutput.getPkUnitId() == stockMoveInput.getPkUnitId() &&
                                stockMoveOutput.getPkLotId() == stockMoveInput.getPkLotId() &&
                                stockMoveOutput.getQuantity() == stockMoveInput.getQuantity()) {

                            diogInputEntry.setValueUnitary(diogOutputEntry.getValueUnitary());
                            diogInputEntry.setOriginalValueUnitary(diogOutputEntry.getValueUnitary());
                            diogInputEntry.setValue(diogOutputEntry.getValue());

                            diogInputEntry.getAuxStockMoves().set(m, stockMoveInput);
                        }
                    }
                }
            }
        }

        diogInput.setValue_r(diogEntriesSumValues(diogInput));
        return diogInput;
    }

    private String productionOrderValidateOrderMoves(final SGuiSession session, final SDataProductionOrder order) throws Exception {
        String  msg = "";

        SDataDiog diogIn = null;
        SDataDiog diogOut = null;

        // Validate that all moves input of raw material come first that all moves out:

        msg = productionOrderValidateRawMaterialDelivery(session, (int[]) order.getPrimaryKey());
        if (msg.isEmpty()) {

            // Validate with return raw material:

            diogIn = productionOrderObtainInventoryMoves(session, SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_RET, order.getPkYearId(), order.getPkOrdId());
            if (diogIn != null) {

                diogOut = productionOrderObtainInventoryMoves(session, SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_ASD, order.getPkYearId(), order.getPkOrdId());
                if (diogOut == null) {
                    msg = "La orden de producción #'" + order.getNumber() + "' no es consistente en sus movimientos de entrada devolución de MP.\n" +
                            "Existe el movimiento de entrada devolución de MP, pero no existe el movimiento de entrada entrega de MP.";
                }
                else {
                    if (diogOut.getDate().compareTo(diogIn.getDate()) > SLibConsts.UNDEFINED) {
                        msg = "La orden de producción #'" + order.getNumber() + "' no es consistente en sus movimientos de entrada devolución de MP.\n" +
                            "La fecha '" + SLibUtils.DateFormatDate.format(diogIn.getDate()) + "' de entrada devolución de MP es posterior a la fecha '" +
                                SLibUtils.DateFormatDate.format(diogOut.getDate()) + "' de entrada entrega de MP.";
                    }
                }
            }
        }
        else {

            msg += "\n Orden de producción #'" + order.getNumber() + "'.";
        }

        // Validate with delivery production process:

        if (msg.isEmpty()) {

            diogIn = productionOrderObtainInventoryMoves(session, SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_ASD, order.getPkYearId(), order.getPkOrdId());
            if (diogIn != null) {

                diogOut = productionOrderObtainInventoryMoves(session, SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_ASD, order.getPkYearId(), order.getPkOrdId());
                if (diogOut == null) {
                    msg = "La orden de producción #'" + order.getNumber() + "' no es consistente en sus movimientos de entrada entrega de PP.\n" +
                            "Existe el movimiento de entrada entrega PP, pero no existe el movimiento de entrada entrega de MP.";
                }
                else {
                    if (diogOut.getDate().compareTo(diogIn.getDate()) > SLibConsts.UNDEFINED) {
                        msg = "La orden de producción #'" + order.getNumber() + "' no es consistente en sus movimientos de entrada entrega de PP.\n" +
                            "La fecha '" + SLibUtils.DateFormatDate.format(diogIn.getDate()) + "' de entrada entrega de PP es posterior a la fecha '" +
                                SLibUtils.DateFormatDate.format(diogOut.getDate()) + "' de entrada entrega de MP.";
                    }
                }
            }
        }

        // Validate with return production process:

        if (msg.isEmpty()) {
            diogIn = productionOrderObtainInventoryMoves(session, SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_RET, order.getPkYearId(), order.getPkOrdId());
            if (diogIn != null) {

                diogOut = productionOrderObtainInventoryMoves(session, SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_ASD, order.getPkYearId(), order.getPkOrdId());
                if (diogOut == null) {
                    msg = "La orden de producción #'" + order.getNumber() + "' no es consistente en sus movimientos de entrada devolución de PP.\n" +
                            "Existe el movimiento de entrada devolución de PP, pero no existe el movimiento de entrada entrega de PP.";
                }
                else {
                    if (diogOut.getDate().compareTo(diogIn.getDate()) > SLibConsts.UNDEFINED) {
                        msg = "La orden de producción #'" + order.getNumber() + "' no es consistente en sus movimientos de entrada devolución de PP.\n" +
                            "La fecha '" + SLibUtils.DateFormatDate.format(diogIn.getDate()) + "' de entrada devolución de PP es posterior a la fecha '" +
                                SLibUtils.DateFormatDate.format(diogOut.getDate()) + "' de entrada entrega de PP.";
                    }
                }
            }
        }

        // Validate with delivery finished good:

        if (msg.isEmpty()) {

            diogIn = productionOrderObtainInventoryMoves(session, SModSysConsts.TRNS_TP_IOG_IN_MFG_FG_ASD, order.getPkYearId(), order.getPkOrdId());
            if (diogIn != null) {

                if (order.getFkOrdYearId_n() > SLibConsts.UNDEFINED &&
                        order.getFkOrdId_n() > SLibConsts.UNDEFINED) {

                    diogOut = productionOrderObtainInventoryMoves(session, SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_ASD, order.getPkYearId(), order.getPkOrdId());
                    if (diogOut == null) {
                        msg = "La orden de producción #'" + order.getNumber() + "' no es consistente en sus movimientos de entrada entrega de PT.\n" +
                                "Existe el movimiento de entrada entrega PT, pero no existe el movimiento de entrada entrega de PP.";
                    }
                    else {
                        if (diogOut.getDate().compareTo(diogIn.getDate()) > SLibConsts.UNDEFINED) {
                            msg = "La orden de producción #'" + order.getNumber() + "' no es consistente en sus movimientos de entrada entrega de PT.\n" +
                                "La fecha '" + SLibUtils.DateFormatDate.format(diogIn.getDate()) + "' de entrada entrega de PT es posterior a la fecha '" +
                                    SLibUtils.DateFormatDate.format(diogOut.getDate()) + "' de entrada entrega de PP.";
                        }
                    }
                }
            }
        }

        // Validate with return finished good:

        if (msg.isEmpty()) {

            diogIn = productionOrderObtainInventoryMoves(session, SModSysConsts.TRNS_TP_IOG_IN_MFG_FG_RET, order.getPkYearId(), order.getPkOrdId());
            if (diogIn != null) {

                diogOut = productionOrderObtainInventoryMoves(session, SModSysConsts.TRNS_TP_IOG_IN_MFG_FG_ASD, order.getPkYearId(), order.getPkOrdId());
                if (diogOut == null) {
                    msg = "La orden de producción #'" + order.getNumber() + "' no es consistente en sus movimientos de entrada entrega de PT.\n" +
                            "Existe el movimiento de entrada devolución de PT, pero no existe el movimiento de entrada entrega de PT.";
                }
                else {
                    if (diogOut.getDate().compareTo(diogIn.getDate()) > SLibConsts.UNDEFINED) {
                        msg = "La orden de producción #'" + order.getNumber() + "' no es consistente en sus movimientos de entrada devolución de PT.\n" +
                            "La fecha '" + SLibUtils.DateFormatDate.format(diogIn.getDate()) + "' de entrada devolución de PT es posterior a la fecha '" +
                                SLibUtils.DateFormatDate.format(diogOut.getDate()) + "' de entrada entrega de PT.";
                    }
                }
            }
        }

        return msg;
    }

    private String productionOrderObtainInventoryInputsMoves(final SGuiSession session, final int[] productionOrderId, final String productionOrderNumber) throws Exception {
        String msg = "";
        Date dateStart = null;
        Date dateEnd = null;

        Statement statement = session.getDatabase().getConnection().createStatement();
        ResultSet resultSet = null;

        // Obtain the period of the first movement of input:

        msSql = "SELECT MIN(d.dt) " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS de ON " +
                "d.id_year = de.id_year AND d.id_doc = de.id_doc " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s ON " +
                "de.id_year = s.fid_diog_year AND de.id_doc = s.fid_diog_doc AND de.id_ety = s.fid_diog_ety " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MFG_ORD) + " AS o ON " +
                "d.fid_mfg_year_n = o.id_year AND d.fid_mfg_ord_n = o.id_ord " +
                "WHERE d.b_del = 0 AND de.b_del = 0 AND " +
                "d.fid_mfg_year_n = " + productionOrderId[0] + " AND d.fid_mfg_ord_n = " + productionOrderId[1] + " AND ( " +
                "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_ASD[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_ASD[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_ASD[2] + ") OR " +
                "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_ASD[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_ASD[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_ASD[2] + ") OR " +
                "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_RET[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_RET[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_RET[2] + ") OR " +
                "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_RET[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_RET[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_RET[2] + "))" +
                "GROUP BY d.fid_mfg_year_n, d.fid_mfg_ord_n; ";
        resultSet = statement.executeQuery(msSql);
        if (resultSet.next()) {

            dateStart = SLibTimeUtils.getBeginOfMonth(resultSet.getDate(1));
            dateEnd = SLibTimeUtils.getEndOfMonth(resultSet.getDate(1));
            msSql = "SELECT d.id_year, d.id_doc " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS de ON " +
                "d.id_year = de.id_year AND d.id_doc = de.id_doc " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s ON " +
                "de.id_year = s.fid_diog_year AND de.id_doc = s.fid_diog_doc AND de.id_ety = s.fid_diog_ety " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MFG_ORD) + " AS o ON " +
                "d.fid_mfg_year_n = o.id_year AND d.fid_mfg_ord_n = o.id_ord " +
                "WHERE d.b_del = 0 AND de.b_del = 0 AND " +
                "d.fid_mfg_year_n = " + productionOrderId[0] + " AND d.fid_mfg_ord_n = " + productionOrderId[1] + " AND ( " +
                "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_ASD[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_ASD[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_ASD[2] + ") OR " +
                "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_ASD[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_ASD[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_ASD[2] + ") OR " +
                "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_RET[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_RET[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_RET[2] + ") OR " +
                "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_RET[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_RET[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_RET[2] + ")) AND " +
                "(d.dt < '" + SLibUtils.DbmsDateFormatDate.format(dateStart) + "' OR d.dt > '" + SLibUtils.DbmsDateFormatDate.format(dateEnd) + "') " +
                "GROUP BY d.fid_mfg_year_n, d.fid_mfg_ord_n; ";
            resultSet = statement.executeQuery(msSql);
            if (resultSet.next()) {

                msg = "La valuación de inventarios no puede continuar:\nexisten movimientos de la orden de producción #'" + productionOrderNumber + "' de Entrega/Devolución de Insumos fuera de su periodo de valuación (" +
                        SLibTimeUtils.digestMonth(dateStart)[0] + "-" + SLibUtils.DecimalFormatCalendarMonth.format(SLibTimeUtils.digestMonth(dateStart)[1]) + ").";
            }
        }

        return msg;
    }

    private String productionOrderObtainInventoryProductsMoves(final SGuiSession session, final int[] productionOrderId, final String productionOrderNumber) throws Exception {
        String msg = "";
        Statement statement = session.getDatabase().getConnection().createStatement();
        ResultSet resultSet = null;

        msSql = "SELECT COUNT(*) " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS de ON " +
                "d.id_year = de.id_year AND d.id_doc = de.id_doc " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s ON " +
                "de.id_year = s.fid_diog_year AND de.id_doc = s.fid_diog_doc AND de.id_ety = s.fid_diog_ety " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MFG_ORD) + " AS o ON " +
                "d.fid_mfg_year_n = o.id_year AND d.fid_mfg_ord_n = o.id_ord " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_INV_VAL) + " AS iv ON " +
                "o.fid_inv_val_n = iv.id_inv_val " +
                "WHERE d.b_del = 0 AND de.b_del = 0 AND iv.b_del = 0 AND iv.id_inv_val <> " + mnPkInventoryValuationId + " AND " +
                "d.fid_mfg_year_n = " + productionOrderId[0] + " AND d.fid_mfg_ord_n = " + productionOrderId[1] + " AND ( " +
                "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_ASD[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_ASD[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_ASD[2] + ") OR " +
                "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_ASD[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_ASD[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_ASD[2] + ") OR " +
                "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_RET[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_RET[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_RET[2] + ") OR " +
                "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_FG_RET[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_FG_RET[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_FG_RET[2] + ")) AND " +
                "(de.ts_edit > iv.ts_usr_upd OR de.ts_del > iv.ts_usr_upd) " +
                "GROUP BY d.id_year, d.id_doc; ";
        resultSet = statement.executeQuery(msSql);
        if (resultSet.next()) {

            msg = "La valuación de inventarios no puede continuar:\nexisten movimientos de inventario de la orden de producción #'" + productionOrderNumber + "' modificados después de su valuación de inventarios.";
        }

        return msg;
    }

    private String productionOrderValidateRawMaterialDelivery(final SGuiSession session, final int[] productionOrderId) throws Exception {
        String msg = "";
        ResultSet resultSet = null;
        Statement statement = session.getDatabase().getConnection().createStatement();

        msSql = "SELECT IF(MAX(d.dt) <= ( " +
            "SELECT COALESCE((SELECT MIN(d.dt) " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS de ON " +
            "d.id_year = de.id_year AND d.id_doc = de.id_doc " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MFG_ORD) + " AS o ON " +
            "d.fid_mfg_year_n = o.id_year AND d.fid_mfg_ord_n = o.id_ord " +
            "WHERE d.b_del = 0 AND de.b_del = 0 AND " +
            "d.fid_mfg_year_n = " + productionOrderId[0] + " AND d.fid_mfg_ord_n = " + productionOrderId[1] + " AND ( " +
            "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_ASD[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_ASD[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_ASD[2] + ") OR " +
            "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_ASD[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_ASD[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_ASD[2] + ") OR " +
            "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_RET[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_RET[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_RET[2] + ") OR " +
            "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_FG_RET[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_FG_RET[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_FG_RET[2] + ")) " +
            "GROUP BY d.fid_mfg_year_n, d.fid_mfg_ord_n), '" + SLibUtils.DbmsDateFormatDate.format(mtAuxDateEnd) + "')), 1, 0) " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS de ON " +
            "d.id_year = de.id_year AND d.id_doc = de.id_doc " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MFG_ORD) + " AS o ON " +
            "d.fid_mfg_year_n = o.id_year AND d.fid_mfg_ord_n = o.id_ord " +
            "WHERE d.b_del = 0 AND de.b_del = 0 AND " +
            "d.fid_mfg_year_n = " + productionOrderId[0] + " AND d.fid_mfg_ord_n = " + productionOrderId[1] + " AND ( " +
            "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_ASD[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_ASD[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_ASD[2] + ") OR " +
            "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_ASD[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_ASD[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_ASD[2] + ") OR " +
            "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_RM_RET[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_RM_RET[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_RM_RET[2] + ") OR " +
            "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_RET[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_RET[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_RET[2] + ")) " +
            "GROUP BY d.fid_mfg_year_n, d.fid_mfg_ord_n; ";
        resultSet = statement.executeQuery(msSql);
        if (resultSet.next() &&
                resultSet.getInt(1) == 0) {

            msg = "Todos los movimientos de entrada de insumos (MP, PP) deben estar registrados antes que las salidas de productos (PP, PT).";
        }

        return msg;
    }

    private SDataDiog productionOrderObtainInventoryMoves(final SGuiSession session, final int[] moveInventory, final int orderYearId, final int orderDocId) throws Exception {
        SDataDiog diog = null;

        Statement statement = session.getDatabase().getConnection().createStatement();
        ResultSet resultSet = null;

        msSql = "SELECT d.id_year, d.id_doc, d.fid_ct_iog, d.fid_cl_iog, d.fid_tp_iog " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS de ON " +
                "d.id_year = de.id_year AND d.id_doc = de.id_doc " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s ON " +
                "de.id_year = s.fid_diog_year AND de.id_doc = s.fid_diog_doc AND de.id_ety = s.fid_diog_ety " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MFG_ORD) + " AS o ON " +
                "d.fid_mfg_year_n = o.id_year AND d.fid_mfg_ord_n = o.id_ord " +
                "WHERE d.b_del = 0 AND de.b_del = 0 AND  s.b_del = 0 AND o.b_del = 0 AND " +
                "d.fid_ct_iog = " + moveInventory[0] + " AND d.fid_cl_iog = " + moveInventory[1] + " AND d.fid_tp_iog = " + moveInventory[2] + " AND " +
                "d.fid_mfg_year_n = " + orderYearId + " AND d.fid_mfg_ord_n = " + orderDocId + " AND d.dt " + msAuxPeriodQuery + " " +
                "ORDER BY d.dt, d.ts_edit, d.fid_ct_iog, d.fid_cl_iog, d.fid_tp_iog, d.id_year, d.id_doc ";
        resultSet = statement.executeQuery(msSql);
        if (resultSet.next()) {

            diog = new SDataDiog();
            diog.read(new int[] { resultSet.getInt("d.id_year"), resultSet.getInt("d.id_doc") }, statement);
        }

        return diog;
    }

    private ArrayList<SDataProductionOrder> productionOrderObtainRelations(final SGuiSession session, final int orderYearId, final int orderDocId) throws Exception {
        SDataProductionOrder productionOrder = null;
        ArrayList<SDataProductionOrder> aProductionOrdersRelations = new ArrayList<SDataProductionOrder>();

        Statement statement = session.getDatabase().getConnection().createStatement();
        Statement statementAux1 = session.getDatabase().getConnection().createStatement();
        Statement statementAux2 = session.getDatabase().getConnection().createStatement();
        ResultSet resultSet = null;
        ResultSet resultSetAux1 = null;
        ResultSet resultSetAux2 = null;

        // Obtain all input inventory moves to delivery WP:

        msSql = "SELECT d.id_year, d.id_doc, d.fid_mfg_year_n, d.fid_mfg_ord_n, de.fid_item, de.fid_unit " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS de ON " +
                "d.id_year = de.id_year AND d.id_doc = de.id_doc " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s ON " +
                "de.id_year = s.fid_diog_year AND de.id_doc = s.fid_diog_doc AND de.id_ety = s.fid_diog_ety " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MFG_ORD) + " AS o ON " +
                "d.fid_mfg_year_n = o.id_year AND d.fid_mfg_ord_n = o.id_ord " +
                "WHERE d.b_del = 0 AND de.b_del = 0 AND  s.b_del = 0 AND o.b_del = 0 AND " +
                "d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_ASD[0] + " AND " +
                "d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_ASD[1] + " AND " +
                "d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_ASD[2] + " AND " +
                "d.fid_mfg_year_n = " + orderYearId + " AND d.fid_mfg_ord_n = " + orderDocId + " AND d.dt " + msAuxPeriodQuery + " " +
                "ORDER BY d.dt, d.ts_edit, d.fid_ct_iog, d.fid_cl_iog, d.fid_tp_iog, d.id_year, d.id_doc ";
        resultSet = statement.executeQuery(msSql);
        while (resultSet.next()) {

            // Obtain output inventory move delivery WP:

            msSql = "SELECT d.id_year, d.id_doc, d.fid_mfg_year_n, d.fid_mfg_ord_n, de.fid_item, de.fid_unit " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS de ON " +
                "d.id_year = de.id_year AND d.id_doc = de.id_doc " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s ON " +
                "de.id_year = s.fid_diog_year AND de.id_doc = s.fid_diog_doc AND de.id_ety = s.fid_diog_ety " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MFG_ORD) + " AS o ON " +
                "d.fid_mfg_year_n = o.id_year AND d.fid_mfg_ord_n = o.id_ord " +
                "WHERE d.b_del = 0 AND de.b_del = 0 AND  s.b_del = 0 AND o.b_del = 0 AND " +
                "d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_ASD[0] + " AND " +
                "d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_ASD[1] + " AND " +
                "d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_ASD[2] + " AND " +
                "d.fid_diog_year_n = " + resultSet.getInt("d.id_year") + " AND d.fid_diog_doc_n = " + resultSet.getInt("d.id_doc") + " AND d.dt " + msAuxPeriodQuery + " " +
                "ORDER BY d.dt, d.ts_edit, d.fid_ct_iog, d.fid_cl_iog, d.fid_tp_iog, d.id_year, d.id_doc ";
            resultSetAux1 = statementAux1.executeQuery(msSql);
            while (resultSetAux1.next()) {

                // Validate that item and unit from production order of output move is ingredient for first production order:

                msSql = "SELECT d.id_year, d.id_doc, d.fid_mfg_year_n, d.fid_mfg_ord_n, de.fid_item, de.fid_unit " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS de ON " +
                    "d.id_year = de.id_year AND d.id_doc = de.id_doc " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s ON " +
                    "de.id_year = s.fid_diog_year AND de.id_doc = s.fid_diog_doc AND de.id_ety = s.fid_diog_ety " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MFG_ORD) + " AS o ON " +
                    "d.fid_mfg_year_n = o.id_year AND d.fid_mfg_ord_n = o.id_ord " +
                    "WHERE d.b_del = 0 AND de.b_del = 0 AND  s.b_del = 0 AND o.b_del = 0 AND " +
                    "d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_ASD[0] + " AND " +
                    "d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_ASD[1] + " AND " +
                    "d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_ASD[2] + " AND " +
                    "d.fid_mfg_year_n = " + orderYearId + " AND d.fid_mfg_ord_n = " + orderDocId + " AND " +
                    "s.id_item = " + resultSetAux1.getInt("de.fid_item") + " AND s.id_unit = " + resultSetAux1.getInt("de.fid_unit") + " AND " +
                    "d.dt " + msAuxPeriodQuery + " " +
                    "ORDER BY d.dt, d.ts_edit, d.fid_ct_iog, d.fid_cl_iog, d.fid_tp_iog, d.id_year, d.id_doc ";
                resultSetAux2 = statementAux2.executeQuery(msSql);
                if (resultSetAux2.next()) {

                    productionOrder = new SDataProductionOrder();
                    productionOrder.read(new int[] { resultSetAux1.getInt("d.fid_mfg_year_n"), resultSetAux1.getInt("d.fid_mfg_ord_n") }, session.getStatement());
                    aProductionOrdersRelations.add(productionOrder);
                }
            }
        }

        return aProductionOrdersRelations;
    }

    private String validateMovesForProductionOrderInputs(final SGuiSession session, final SDataProductionOrder order) throws Exception {
        int rows = 0;
        String  msg = "";
        String periodSystem = "'" + SLibUtils.DateFormatDate.format(mtAuxDateStart) + " - " + SLibUtils.DateFormatDate.format(mtAuxDateEnd) + "'";

        // Validate with delivery raw material:

        rows = obtainInventoryMovesForProductionOrderInputs(session, SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_ASD, order.getPkYearId(), order.getPkOrdId());
        if (rows > SLibConsts.UNDEFINED) {

            msg = "La orden de producción #'" + order.getNumber() + "' no es consistente en sus movimientos de entrada entrega de MP.\n" +
                    "Existen insumos que se entregaron fuera del periodo actual " + periodSystem + ".";
        }

        // Validate with return raw material:

        if (msg.isEmpty()) {
            rows = obtainInventoryMovesForProductionOrderInputs(session, SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_RET, order.getPkYearId(), order.getPkOrdId());
            if (rows > SLibConsts.UNDEFINED) {

                msg = "La orden de producción #'" + order.getNumber() + "' no es consistente en sus movimientos de entrada devolución de MP.\n" +
                        "Existen insumos que se devolvieron fuera del periodo actual " + periodSystem + ".";
            }
        }

        // Validate with delivery work process:

        if (msg.isEmpty()) {
            rows = obtainInventoryMovesForProductionOrderInputs(session, SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_ASD, order.getPkYearId(), order.getPkOrdId());
            if (rows > SLibConsts.UNDEFINED) {

                msg = "La orden de producción #'" + order.getNumber() + "' no es consistente en sus movimientos de entrada entrega de PP.\n" +
                        "Existen insumos que se entregaron fuera del periodo actual " + periodSystem + ".";
            }
        }

        // Validate with return work process:

        if (msg.isEmpty()) {
            rows = obtainInventoryMovesForProductionOrderInputs(session, SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_RET, order.getPkYearId(), order.getPkOrdId());
            if (rows > SLibConsts.UNDEFINED) {

                msg = "La orden de producción #'" + order.getNumber() + "' no es consistente en sus movimientos de entrada devolución de PP.\n" +
                        "Existen insumos que se devolvieron fuera del periodo actual " + periodSystem + ".";
            }
        }

        return msg;
    }

    private int obtainInventoryMovesForProductionOrderInputs(final SGuiSession session, final int[] moveInventory, final int orderYearId, final int orderDocId) throws Exception {
        int rows = 0;

        Statement statement = session.getDatabase().getConnection().createStatement();
        ResultSet resultSet = null;

        msSql = "SELECT COUNT(*) " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS de ON " +
                "d.id_year = de.id_year AND d.id_doc = de.id_doc " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s ON " +
                "de.id_year = s.fid_diog_year AND de.id_doc = s.fid_diog_doc AND de.id_ety = s.fid_diog_ety " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MFG_ORD) + " AS o ON " +
                "d.fid_mfg_year_n = o.id_year AND d.fid_mfg_ord_n = o.id_ord " +
                "WHERE d.b_del = 0 AND de.b_del = 0 AND  s.b_del = 0 AND o.b_del = 0 AND " +
                "d.fid_ct_iog = " + moveInventory[0] + " AND d.fid_cl_iog = " + moveInventory[1] + " AND d.fid_tp_iog = " + moveInventory[2] + " AND " +
                "d.fid_mfg_year_n = " + orderYearId + " AND d.fid_mfg_ord_n = " + orderDocId + " AND " +
                "(d.dt < '" + SLibUtils.DbmsDateFormatDate.format(mtAuxDateStart) + "' OR d.dt > '" + SLibUtils.DbmsDateFormatDate.format(mtAuxDateEnd) + "') " +
                "GROUP BY de.id_year, de.id_doc, de.id_ety " +
                "ORDER BY d.dt, d.ts_edit, d.fid_ct_iog, d.fid_cl_iog, d.fid_tp_iog, d.id_year, d.id_doc ";
        resultSet = statement.executeQuery(msSql);
        if (resultSet.next()) {
            rows = resultSet.getInt(1);
        }

        return rows;
    }

    private boolean validateProductionOrderInputsPreviousOuputMoves(final SGuiSession session, final int[] productionOrderId) throws Exception {
        boolean valid = true;

        Statement statement = session.getDatabase().getConnection().createStatement();
        ResultSet resultSet = null;

        // Obtain the period of the first movement of input:

        msSql = "SELECT MIN(d.dt) " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS de ON " +
                "d.id_year = de.id_year AND d.id_doc = de.id_doc " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s ON " +
                "de.id_year = s.fid_diog_year AND de.id_doc = s.fid_diog_doc AND de.id_ety = s.fid_diog_ety " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MFG_ORD) + " AS o ON " +
                "d.fid_mfg_year_n = o.id_year AND d.fid_mfg_ord_n = o.id_ord " +
                "WHERE d.b_del = 0 AND de.b_del = 0 AND " +
                "d.fid_mfg_year_n = " + productionOrderId[0] + " AND d.fid_mfg_ord_n = " + productionOrderId[1] + " AND ( " +
                "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_ASD[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_ASD[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_ASD[2] + ") OR " +
                "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_ASD[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_ASD[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_ASD[2] + ") OR " +
                "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_RET[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_RET[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_RET[2] + ") OR " +
                "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_RET[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_RET[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_RET[2] + "))" +
                "GROUP BY d.fid_mfg_year_n, d.fid_mfg_ord_n; ";

        resultSet = statement.executeQuery(msSql);
        if (resultSet.next()) {
            msSql = "SELECT d.id_year, d.id_doc " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS de ON " +
                "d.id_year = de.id_year AND d.id_doc = de.id_doc " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s ON " +
                "de.id_year = s.fid_diog_year AND de.id_doc = s.fid_diog_doc AND de.id_ety = s.fid_diog_ety " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MFG_ORD) + " AS o ON " +
                "d.fid_mfg_year_n = o.id_year AND d.fid_mfg_ord_n = o.id_ord " +
                "WHERE d.b_del = 0 AND de.b_del = 0 AND " +
                "d.fid_mfg_year_n = " + productionOrderId[0] + " AND d.fid_mfg_ord_n = " + productionOrderId[1] + " AND ( " +
                "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_ASD[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_ASD[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_ASD[2] + ") OR " +
                "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_ASD[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_ASD[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_ASD[2] + ") OR " +
                "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_RET[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_RET[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_RET[2] + ") OR " +
                "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_RET[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_RET[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_RET[2] + ")) AND " +
                "d.dt > '" + SLibUtils.DbmsDateFormatDate.format(resultSet.getDate(1)) + "' " +
                "GROUP BY d.fid_mfg_year_n, d.fid_mfg_ord_n; ";

            resultSet = statement.executeQuery(msSql);
            if (resultSet.next()) {
                valid = false;
            }
        }

        return valid;
    }

    private SDataProductionOrder obtainProductionOrderPreviousStatusCost(final SGuiSession session, final int orderYearId, final int orderDocId, final Date dateStart, final int[] moveInventory)  throws Exception {
        SDataProductionOrder productionOrder = null;

        Statement statement = session.getDatabase().getConnection().createStatement();
        ResultSet resultSet = null;

        msSql = "SELECT d.fid_mfg_year_n, d.fid_mfg_ord_n " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS de ON " +
                "d.id_year = de.id_year AND d.id_doc = de.id_doc " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s ON " +
                "de.id_year = s.fid_diog_year AND de.id_doc = s.fid_diog_doc AND de.id_ety = s.fid_diog_ety " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MFG_ORD) + " AS o ON " +
                "d.fid_mfg_year_n = o.id_year AND d.fid_mfg_ord_n = o.id_ord " +
                "WHERE d.b_del = 0 AND de.b_del = 0 AND  s.b_del = 0 AND o.b_del = 0 AND " +
                "d.fid_mfg_year_n = " + orderYearId + " AND d.fid_mfg_ord_n = " + orderDocId + " AND " +
                "d.dt < '" + SLibUtils.DbmsDateFormatDate.format(dateStart) + "' " +
                "ORDER BY d.dt ASC, d.id_year, d.id_doc ";
        resultSet = statement.executeQuery(msSql);
        if (resultSet.next()) {

            productionOrder = new SDataProductionOrder();
            productionOrder.read(new int[] { resultSet.getInt("d.fid_mfg_year_n"), resultSet.getInt("d.fid_mfg_ord_n") }, statement);
        }

        return productionOrder;
    }

    private String validatePurchasesForPhysicalMeasurementItems(final SGuiSession session) throws Exception {
        String  msg = "";

        Statement statement = session.getDatabase().getConnection().createStatement();
        ResultSet resultSet = null;

        msSql = "SELECT i.item, i.item_key " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS de ON " +
                "d.id_year = de.id_year AND d.id_doc = de.id_doc " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s ON " +
                "de.id_year = s.fid_diog_year AND de.id_doc = s.fid_diog_doc AND de.id_ety = s.fid_diog_ety " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON " +
                "i.id_item = de.fid_item " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS u ON " +
                "u.id_unit = de.fid_unit " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_IGEN) + " AS ig ON " +
                "i.fid_igen = ig.id_igen " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_TP_UNIT) + " AS tpu ON " +
                "ig.fid_tp_unit = tpu.id_tp_unit " +
                "WHERE d.b_del = 0 AND de.b_del = 0 AND  s.b_del = 0 AND " +
                "((d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_PUR_PUR[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_PUR_PUR[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_PUR_PUR[2] + ") OR " +
                "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_PUR_PUR[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_PUR_PUR[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_PUR_PUR[2] + ") OR " +
                "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_EXT_CHG_PUR[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_EXT_CHG_PUR[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_EXT_CHG_PUR[2] + ") OR " +
                "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_EXT_CHG_PUR[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_EXT_CHG_PUR[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_EXT_CHG_PUR[2] + ") OR " +
                "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_EXT_WAR_PUR[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_EXT_WAR_PUR[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_EXT_WAR_PUR[2] + ") OR " +
                "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_EXT_WAR_PUR[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_EXT_WAR_PUR[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_EXT_WAR_PUR[2] + ") OR " +
                "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_EXT_CSG_PUR[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_EXT_CSG_PUR[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_EXT_CSG_PUR[2] + ") OR " +
                "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_EXT_CSG_PUR[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_EXT_CSG_PUR[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_EXT_CSG_PUR[2] + ")) AND " +
                "ig.fid_ct_item = " + SModSysConsts.ITMS_CL_ITEM_PUR_CON[0] + " AND ig.fid_cl_item = " + SModSysConsts.ITMS_CL_ITEM_PUR_CON[1] + " AND " +
                "i.mass <= 0 AND " +
                "d.dt " + msAuxPeriodQuery + " " +
                "GROUP BY de.id_year, de.id_doc, de.id_ety " +
                "ORDER BY d.dt, d.ts_edit, d.fid_ct_iog, d.fid_cl_iog, d.fid_tp_iog, d.id_year, d.id_doc ";
        resultSet = statement.executeQuery(msSql);
        if (resultSet.next()) {
            msg = "La unidad 'MASA' del ítem '" + resultSet.getString("i.item") + " (" + resultSet.getString("i.item_key") + ")" + "' debe ser mayor a cero.";
        }

        return msg;
    }

    private String validateMovesForProductionOrderPhysicalMeasurement(final SGuiSession session, final SDataProductionOrder order) throws Exception {
        String  msg = "";

        Statement statement = session.getDatabase().getConnection().createStatement();
        ResultSet resultSet = null;

        msSql = "SELECT o.num, i.item, i.item_key, tpu.tp_unit " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS de ON " +
                "d.id_year = de.id_year AND d.id_doc = de.id_doc " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s ON " +
                "de.id_year = s.fid_diog_year AND de.id_doc = s.fid_diog_doc AND de.id_ety = s.fid_diog_ety " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MFG_ORD) + " AS o ON " +
                "d.fid_mfg_year_n = o.id_year AND d.fid_mfg_ord_n = o.id_ord " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON " +
                "i.id_item = de.fid_item " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS u ON " +
                "u.id_unit = de.fid_unit " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_IGEN) + " AS ig ON " +
                "i.fid_igen = ig.id_igen " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_TP_UNIT) + " AS tpu ON " +
                "ig.fid_tp_unit = tpu.id_tp_unit " +
                "WHERE d.b_del = 0 AND de.b_del = 0 AND  s.b_del = 0 AND o.b_del = 0 AND " +
                "((ig.fid_ct_item = " + SDataConstantsSys.ITMS_TP_ITEM_SAL_PRO_WP[0] + " AND " +
                "ig.fid_cl_item = " + SDataConstantsSys.ITMS_TP_ITEM_SAL_PRO_WP[1] + " AND " +
                "ig.fid_tp_item = " + SDataConstantsSys.ITMS_TP_ITEM_SAL_PRO_WP[2] + ") OR " +
                "(ig.fid_ct_item = " + SDataConstantsSys.ITMS_TP_ITEM_SAL_PRO_FG[0] + " AND " +
                "ig.fid_cl_item = " + SDataConstantsSys.ITMS_TP_ITEM_SAL_PRO_FG[1] + " AND " +
                "ig.fid_tp_item = " + SDataConstantsSys.ITMS_TP_ITEM_SAL_PRO_FG[2] + ")) AND " +
                "d.fid_mfg_year_n = " + order.getPkYearId() + " AND d.fid_mfg_ord_n = " + order.getPkOrdId() + " AND " +
                msAuxMfgCostUnitTypeSql + " <= 0 AND " +
                "d.dt " + msAuxPeriodQuery + " " +
                "GROUP BY de.id_year, de.id_doc, de.id_ety " +
                "ORDER BY d.dt, d.ts_edit, d.fid_ct_iog, d.fid_cl_iog, d.fid_tp_iog, d.id_year, d.id_doc ";
        resultSet = statement.executeQuery(msSql);
        if (resultSet.next()) {
            msg = "La unidad '" + msAuxMfgCostUnitType + "' del ítem '" + resultSet.getString("i.item") + " (" + resultSet.getString("i.item_key") + ")" + "' de la orden de producción #' " + resultSet.getString("o.num") + "' debe ser mayor a cero.";
        }

        return msg;
    }

    private void allocateExpensesPurchasing(final SGuiSession session) throws Exception {
        double dValue = 0;
        double dTotalAssigned = 0;

        ArrayList<SDataDiog> apportionDiogs = new ArrayList<SDataDiog>();

        SDataDiog diog = null;
        SDataItem item = null;

        Statement statementItemUnitWarehouse = session.getDatabase().getConnection().createStatement(); // XXX jbarajas statementItemMass
        Statement statementPurchasingExpenses = session.getDatabase().getConnection().createStatement();
        Statement statementItem = session.getDatabase().getConnection().createStatement(); // XXX jbarajas statementItemUnitWarehouse
        ResultSet resultSetItemUnitWarehouse = null; // XXX jbarajas resultSetItemMass
        ResultSet resultSetPurchasingExpenses = null;
        ResultSet resultSetItem = null; // XXX jbarajas resultSetItemUnitWarehouse

        // 1. Delete Purchases Expense for the msAuxPeriodQuery:

        deleteDiogMoves(session, SModSysConsts.TRNS_TP_IOG_IN_EXP_PUR);

        // 2. Determine the total of purchasing expenses by item-unit for the msAuxPeriodQuery:

        msSql = composeSqlQueryForPurchasingExpenses(SLibConsts.UNDEFINED);
        resultSetPurchasingExpenses = statementPurchasingExpenses.executeQuery(msSql);
        while (resultSetPurchasingExpenses.next()) {

            // 2.1 Determine the available inventory MP, mass (kg), for item-unit-store the current PC (ie, initial inventory purchases MP + net income):

            // XXX jbarajas warehouseType
            msSql = composeSqlQueryStartingInventoryAvailableForPurchasingExpenses(resultSetPurchasingExpenses.getInt("re.fid_item_n"), false);
            resultSetItemUnitWarehouse = statementItemUnitWarehouse.executeQuery(msSql);

            // Validate if there is purchased by units per item-unit-lot-warehouse for the msAuxPeriodQuery:

            if (resultSetItemUnitWarehouse.next()) {

                dTotalAssigned = 0;
                resultSetItemUnitWarehouse.beforeFirst();
                while (resultSetItemUnitWarehouse.next()) {

                    // 3. GC apportion and allocate inventories (ie, create inputs System type ENTRY COST PURCHASE) for each item-unit-warehouse, stores shopping destination (eg, MP).

                    msSql = composeSqlQueryStartingInventoryAvailableForPurchasingExpenses(resultSetItemUnitWarehouse.getInt("s.id_item"), true); // XXX jbarajas mnFkYearYearId, mtAuxDateEnd
                    //msSql = obtainQueryForApportionPurchasingExpensesByWareHouseType(resultSetItemUnitWarehouse.getInt("s.id_item"), mnFkYearYearId, mtAuxDateEnd, true, SDataConstantsSys.CFGS_TP_ENT_WH_MFG_RM); // XXX jbarajas mnFkYearYearId, mtAuxDateEnd
                    resultSetItem = statementItem.executeQuery(msSql);
                    while (resultSetItem.next()) {
                        dValue = SLibUtils.round(resultSetPurchasingExpenses.getDouble("f_bal") * (resultSetItemUnitWarehouse.getDouble("f_total") / resultSetItem.getDouble("f_total")), SLibUtils.DecimalFormatValue2D.getMaximumFractionDigits());
                        diog = createDiog(session, SModSysConsts.TRNS_TP_IOG_IN_EXP_PUR, 
                                new int[] { resultSetItemUnitWarehouse.getInt("s.id_cob"), resultSetItemUnitWarehouse.getInt("s.id_wh") }, 
                                SDataConstantsSys.TRNX_STK_LOT_DEF_ID, resultSetItem.getInt("s.id_item"), resultSetItem.getInt("s.id_unit"), 
                                SLibTimeUtils.createDate(mnFkYearYearId, mnFkYearPeriodId, 1), 
                                new int[] { SLibConsts.UNDEFINED, SLibConsts.UNDEFINED }, 
                                dValue);
                        
                        dTotalAssigned += dValue;
                        apportionDiogs.add(diog);
                    }
                }

                // Validate apportion the purchasing expenses and assign to inventory for each item-unit-lot-warehouse in the warehouses destiny:

                if ((resultSetPurchasingExpenses.getDouble("f_bal") - dTotalAssigned) != 0) {

                    apportionDiogs(session, apportionDiogs, (resultSetPurchasingExpenses.getDouble("f_bal") - dTotalAssigned));
                }
            }
            else {
                // XXX jbarajas

                msSql = composeSqlQueryForPurchasingExpensesApportionByWarehouseType(resultSetPurchasingExpenses.getInt("re.fid_item_n"), mnFkYearYearId, mtAuxDateEnd, false, SDataConstantsSys.CFGS_TP_ENT_WH_MFG_RM);
                resultSetItemUnitWarehouse = statementItemUnitWarehouse.executeQuery(msSql);

                if (resultSetItemUnitWarehouse.next() && resultSetItemUnitWarehouse.getDouble("f_total") > 0) {

                    dTotalAssigned = 0;
                    resultSetItemUnitWarehouse.beforeFirst();
                    while (resultSetItemUnitWarehouse.next()) {

                        msSql = composeSqlQueryForPurchasingExpensesApportionByWarehouseType(resultSetItemUnitWarehouse.getInt("s.id_item"), mnFkYearYearId, mtAuxDateEnd, true, SDataConstantsSys.CFGS_TP_ENT_WH_MFG_RM);
                        resultSetItem = statementItem.executeQuery(msSql);
                        while (resultSetItem.next()) {
                            dValue = SLibUtils.round(resultSetPurchasingExpenses.getDouble("f_bal") * (resultSetItemUnitWarehouse.getDouble("f_total") / resultSetItem.getDouble("f_total")), SLibUtils.DecimalFormatValue2D.getMaximumFractionDigits());
                            diog = createDiog(session, SModSysConsts.TRNS_TP_IOG_IN_EXP_PUR, 
                                    new int[] { resultSetItemUnitWarehouse.getInt("s.id_cob"), resultSetItemUnitWarehouse.getInt("s.id_wh") }, 
                                    SDataConstantsSys.TRNX_STK_LOT_DEF_ID, resultSetItem.getInt("s.id_item"), resultSetItem.getInt("s.id_unit"), 
                                    SLibTimeUtils.createDate(mnFkYearYearId, mnFkYearPeriodId, 1), 
                                    new int[] { SLibConsts.UNDEFINED, SLibConsts.UNDEFINED }, 
                                    dValue);
                            
                            dTotalAssigned += dValue;
                            apportionDiogs.add(diog);
                        }
                    }

                    // Validate apportion the purchasing expenses and assign to inventory for each item-unit-lot-warehouse in the warehouses destiny:

                    if ((resultSetPurchasingExpenses.getDouble("f_bal") - dTotalAssigned) != 0) {
                        apportionDiogs(session, apportionDiogs, (resultSetPurchasingExpenses.getDouble("f_bal") - dTotalAssigned));
                    }
                }
                else { // XXX jbarajas, XXX sflores (2016-02-12, jbarajas comment what for?!)
                    // Read item:

                    item = new SDataItem();
                    item.read(new int[] { resultSetPurchasingExpenses.getInt("re.fid_item_n") }, statementItemUnitWarehouse);
                    createDiog(session, SModSysConsts.TRNS_TP_IOG_IN_EXP_PUR, 
                            new int[] { manAuxRawMaterialWarehouseKey[0], manAuxRawMaterialWarehouseKey[1] }, 
                            SDataConstantsSys.TRNX_STK_LOT_DEF_ID, item.getPkItemId(), item.getFkUnitId(), 
                            resultSetPurchasingExpenses.getDate("r.dt"), 
                            new int[] { SLibConsts.UNDEFINED, SLibConsts.UNDEFINED},
                            SLibUtils.round(resultSetPurchasingExpenses.getDouble("f_bal"), SLibUtils.DecimalFormatValue2D.getMaximumFractionDigits()));
                }
            }
        }
    }

    private String composeSqlQueryStartingInventoryAvailableForPurchasingExpenses(final int idItem, final boolean isByItem) {
        return msSql = "SELECT s.dt, s.id_item, s.id_unit, s.id_cob, s.id_wh, (COALESCE((" +
                composeSqlQueryStartingInventoryPurchasing(mnFkYearYearId, SLibTimeUtils.createDate(mnFkYearYearId, mnFkYearPeriodId, 1)) + "), 0) + " +
                "(SUM((s.mov_in - s.mov_out) * i.mass))) AS f_total " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS de ON " +
                "d.id_year = de.id_year AND d.id_doc = de.id_doc " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s ON " +
                "de.id_year = s.fid_diog_year AND de.id_doc = s.fid_diog_doc AND de.id_ety = s.fid_diog_ety " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON " +
                "i.id_item = de.fid_item " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS u ON " +
                "u.id_unit = de.fid_unit " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS bpb ON s.id_cob = bpb.id_bpb " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_COB_ENT) + " AS ent ON s.id_cob = ent.id_cob AND s.id_wh = ent.id_ent " +
                "WHERE d.b_del = 0 AND de.b_del = 0 AND  s.b_del = 0 AND " +
                "((d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_PUR_PUR[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_PUR_PUR[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_PUR_PUR[2] + ") OR " +
                "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_PUR_PUR[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_PUR_PUR[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_PUR_PUR[2] + ") OR " +
                "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_EXT_CHG_PUR[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_EXT_CHG_PUR[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_EXT_CHG_PUR[2] + ") OR " +
                "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_EXT_CHG_PUR[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_EXT_CHG_PUR[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_EXT_CHG_PUR[2] + ") OR " +
                "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_EXT_WAR_PUR[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_EXT_WAR_PUR[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_EXT_WAR_PUR[2] + ") OR " +
                "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_EXT_WAR_PUR[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_EXT_WAR_PUR[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_EXT_WAR_PUR[2] + ") OR " +
                "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_EXT_CSG_PUR[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_EXT_CSG_PUR[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_EXT_CSG_PUR[2] + ") OR " +
                "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_EXT_CSG_PUR[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_EXT_CSG_PUR[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_EXT_CSG_PUR[2] + ")) AND " +
                "s.id_item = " + idItem + " AND " +
                "ent.fid_ct_ent = " + SDataConstantsSys.CFGS_TP_ENT_WH_MFG_RM[0] + " AND ent.fid_tp_ent = " + SDataConstantsSys.CFGS_TP_ENT_WH_MFG_RM[1] + " AND " +
                "d.dt " + msAuxPeriodQuery + " " +
                "GROUP BY s.id_item " + (isByItem ? "" : ", s.id_unit, s.id_cob, s.id_wh ") +
                "HAVING f_total > 0 " +
                "ORDER BY s.id_item " + (isByItem ? "" : ", s.id_unit, s.id_cob, s.id_wh ");
    }

    private String composeSqlQueryStartingInventoryPurchasing(final int year, final Date dateStart) {
        return "SELECT (SUM((ss.mov_in - ss.mov_out) * i.mass)) AS f_total " + // XXX jbarajas  * i.mass))
            "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS sd " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS sde ON " +
            "sd.id_year = sde.id_year AND sd.id_doc = sde.id_doc " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS ss ON " +
            "sde.id_year = ss.fid_diog_year AND sde.id_doc = ss.fid_diog_doc AND sde.id_ety = ss.fid_diog_ety " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS si ON " +
            "si.id_item = sde.fid_item " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS su ON " +
            "su.id_unit = sde.fid_unit " +
            "WHERE sd.b_del = 0 AND sde.b_del = 0 AND  ss.b_del = 0 AND " +
            "YEAR(sd.dt) = " + year + " AND " +
            "sd.dt < '" +  SLibUtils.DbmsDateFormatDate.format(dateStart) + "' AND " +
            "ss.id_item = s.id_item AND " +
            "ss.id_unit = s.id_unit AND " +
            "ss.id_cob = s.id_cob AND " +
            "ss.id_wh = s.id_wh " +
            "GROUP BY s.id_item, s.id_unit " +
            "ORDER BY s.id_item, s.id_unit ";
    }

    private String composeSqlQueryStartingInventoryProductionOrderQuantity(final int year, final int[] pkProductionOrderId, final Date dateStart) {
        return "SELECT (SUM((s.mov_in - s.mov_out) * i.mass)) AS f_total " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS de ON " +
            "d.id_year = de.id_year AND d.id_doc = de.id_doc " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s ON " +
            "de.id_year = s.fid_diog_year AND de.id_doc = s.fid_diog_doc AND de.id_ety = s.fid_diog_ety " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON " +
            "i.id_item = de.fid_item " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS u ON " +
            "u.id_unit = de.fid_unit " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_IGEN) + " AS ig ON " +
            "i.fid_igen = ig.id_igen " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMS_TP_ITEM) + " AS t ON " +
            "ig.fid_ct_item = t.id_ct_item AND ig.fid_cl_item = t.id_cl_item AND ig.fid_tp_item = t.id_tp_item " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MFG_ORD) + " AS o ON " +
            "d.fid_mfg_year_n = o.id_year AND d.fid_mfg_ord_n = o.id_ord " +
            "WHERE d.b_del = 0 AND de.b_del = 0 AND  s.b_del = 0 AND o.b_del = 0 AND d.id_year = " + year + " AND " +
            "((ig.fid_ct_item = " + SModSysConsts.ITMS_TP_ITEM_SAL_PRO_WP[0] + " AND ig.fid_cl_item = " + SModSysConsts.ITMS_TP_ITEM_SAL_PRO_WP[1] + " AND ig.fid_tp_item = " + SModSysConsts.ITMS_TP_ITEM_SAL_PRO_WP[2] + ") OR " +
            "(ig.fid_ct_item = " + SModSysConsts.ITMS_TP_ITEM_SAL_PRO_FG[0] + " AND ig.fid_cl_item = " + SModSysConsts.ITMS_TP_ITEM_SAL_PRO_FG[1] + " AND ig.fid_tp_item = " + SModSysConsts.ITMS_TP_ITEM_SAL_PRO_FG[2] + ")) AND " +
            "d.fid_mfg_year_n = " + pkProductionOrderId[0] + " AND d.fid_mfg_ord_n = " + pkProductionOrderId[1] + " AND " +
            "d.dt < " + SLibUtils.DbmsDateFormatDate.format(dateStart) + " " +
            "GROUP BY d.fid_mfg_year_n, d.fid_mfg_ord_n " +
            "ORDER BY d.fid_mfg_year_n, d.fid_mfg_ord_n ";
    }

    private String composeSqlQueryStartingInventoryProductionOrderRmCosts(final int year, final int[] pkProductionOrderId, final Date dateStart) {
        return "SELECT SUM(s.debit - s.credit) AS f_balance " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS de ON " +
            "d.id_year = de.id_year AND d.id_doc = de.id_doc " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s ON " +
            "de.id_year = s.fid_diog_year AND de.id_doc = s.fid_diog_doc AND de.id_ety = s.fid_diog_ety " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON " +
            "i.id_item = de.fid_item " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS u ON " +
            "u.id_unit = de.fid_unit " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_IGEN) + " AS ig ON " +
            "i.fid_igen = ig.id_igen " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMS_TP_ITEM) + " AS t ON " +
            "ig.fid_ct_item = t.id_ct_item AND ig.fid_cl_item = t.id_cl_item AND ig.fid_tp_item = t.id_tp_item " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MFG_ORD) + " AS o ON " +
            "d.fid_mfg_year_n = o.id_year AND d.fid_mfg_ord_n = o.id_ord " +
            "WHERE d.b_del = 0 AND de.b_del = 0 AND  s.b_del = 0 AND o.b_del = 0 AND d.id_year = " + year + " AND (" +
            "(ig.fid_ct_item = " + SModSysConsts.ITMS_TP_ITEM_PUR_CON_RMD[0] + " AND ig.fid_cl_item = " + SModSysConsts.ITMS_TP_ITEM_PUR_CON_RMD[1] + " AND ig.fid_tp_item = " + SModSysConsts.ITMS_TP_ITEM_PUR_CON_RMD[2] + ") OR " +
            "(ig.fid_ct_item = " + SModSysConsts.ITMS_TP_ITEM_PUR_CON_RMP[0] + " AND ig.fid_cl_item = " + SModSysConsts.ITMS_TP_ITEM_PUR_CON_RMP[1] + " AND ig.fid_tp_item = " + SModSysConsts.ITMS_TP_ITEM_PUR_CON_RMP[2] + ") OR " +
            "(ig.fid_ct_item = " + SModSysConsts.ITMS_TP_ITEM_SAL_PRO_WP[0] + " AND ig.fid_cl_item = " + SModSysConsts.ITMS_TP_ITEM_SAL_PRO_WP[1] + " AND ig.fid_tp_item = " + SModSysConsts.ITMS_TP_ITEM_SAL_PRO_WP[2] + ")) AND " +
            "d.fid_mfg_year_n = " + pkProductionOrderId[0] + " AND d.fid_mfg_ord_n = " + pkProductionOrderId[1] + " AND " +
            "d.dt < '" + SLibUtils.DbmsDateFormatDate.format(dateStart) + "' " +
            "GROUP BY d.fid_mfg_year_n, d.fid_mfg_ord_n " +
            "ORDER BY d.fid_mfg_year_n, d.fid_mfg_ord_n ";
    }

    private void apportionDiogs(final SGuiSession session, final ArrayList<SDataDiog> paramApportionDiogs, final double difference) {
        int[] anPkDiogId = null;
        double dGreaterValue = 0;

        SDataDiogEntry diogEntry = null;
        SDataDiogEntry diogEntryAux = null;

        ArrayList<SDataDiog> apportionDiogs = new ArrayList<SDataDiog>();
        apportionDiogs.addAll(paramApportionDiogs);

        // Seeek greater value:

        for (SDataDiog oDiog : apportionDiogs) {

            if (oDiog.getValue_r() > dGreaterValue &&
                    oDiog.getIsSystem()) {
                dGreaterValue = oDiog.getValue_r();
                anPkDiogId = new int[] { oDiog.getPkYearId(), oDiog.getPkDocId() };
            }
        }

        // Assign difference to diog:

        for (SDataDiog oDiog : apportionDiogs) {

            if (SLibUtils.compareKeys(new int[] { oDiog.getPkYearId(), oDiog.getPkDocId() }, anPkDiogId)) {

                oDiog.setValue_r(SLibUtils.round(oDiog.getValue_r() + (difference), SLibUtils.DecimalFormatValue2D.getMaximumFractionDigits()));

                // Assign difference to diogEntry:

                for (SDataDiogEntry entry : oDiog.getDbmsEntries()) {

                    if (!entry.getIsDeleted() &&
                            (entry.getValue()) > (difference) &&
                            ((entry.getValue()) + (difference)) > 0) {

                        diogEntry = entry;
                        diogEntry.setValue(SLibUtils.round(diogEntry.getValue() + (difference), SLibUtils.DecimalFormatValue2D.getMaximumFractionDigits()));
                        diogEntry.setValueUnitary(SLibUtils.round(diogEntry.getValueUnitary() + (difference), SLibUtils.DecimalFormatValue8D.getMaximumFractionDigits()));
                        diogEntry.setOriginalValueUnitary(SLibUtils.round(diogEntry.getOriginalValueUnitary() + (difference), SLibUtils.DecimalFormatValue8D.getMaximumFractionDigits()));
                        diogEntry.save(session.getDatabase().getConnection());
                        break;
                    }
                }

                if (diogEntry != null) {
                    for (int i=0; i<oDiog.getDbmsEntries().size(); i++) {

                        diogEntryAux = oDiog.getDbmsEntries().get(i);
                        if (SLibUtils.compareKeys(diogEntry.getPrimaryKey(), diogEntryAux.getPrimaryKey())) {

                            oDiog.getDbmsEntries().set(i, diogEntry);
                            break;
                        }
                    }
                }

                oDiog.save(session.getDatabase().getConnection());
                break;
            }
        }
    }

    private String composeSqlQueryForPurchasingExpenses(final int idItem) {
        return "SELECT r.dt, re.fid_item_n, SUM(re.debit - re.credit) AS f_bal " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS re ON " +
                "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS acc ON " +
                "re.fid_acc = acc.id_acc " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON " +
                "re.fid_item_n = i.id_item " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_IGEN) + " AS ig ON " +
                "i.fid_igen = ig.id_igen " +
                "WHERE r.b_del = 0 AND re.b_del = 0 AND " +
                "(SELECT COUNT(*) " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " " +
                "WHERE id_acc = CONCAT( " +
                "LEFT(acc.id_acc, INSTR(acc.id_acc, '-') -1), " +
                "RIGHT('" + msAuxAccountMask + "', LENGTH(acc.id_acc) - (INSTR(acc.id_acc, '-') -1))) AND " +
                "((fid_tp_acc_r = " + SDataConstantsSys.FINS_CLS_ACC_PUR[0] + " AND " +
                "fid_cl_acc_r = " + SDataConstantsSys.FINS_CLS_ACC_PUR[1] + " AND " +
                "fid_cls_acc_r = " + SDataConstantsSys.FINS_CLS_ACC_PUR[2] + ") OR (" +
                "fid_tp_acc_r = " + SDataConstantsSys.FINS_CLS_ACC_EXPEN_PUR[0] + " AND " +
                "fid_cl_acc_r = " + SDataConstantsSys.FINS_CLS_ACC_EXPEN_PUR[1] + " AND " +
                "fid_cls_acc_r = " + SDataConstantsSys.FINS_CLS_ACC_EXPEN_PUR[2] + "))) > 0 AND " +
                "r.dt " + msAuxPeriodQuery + " AND " +
                (idItem == SLibConsts.UNDEFINED ? "" : "re.fid_item_n = " + idItem + " AND ") + " " +
                "re.fid_item_aux_n IS NOT NULL " +
                "GROUP BY re.fid_item_n " +
                "ORDER BY re.fid_item_n ";
    }

    private String composeSqlQueryForPurchasingExpensesApportion(final int idItem, final int year, final Date date, final boolean isByItem) { // XXX jbarajas final int year, final Date date, final boolean isByItem
        return "SELECT s.id_cob, s.id_wh, s.id_item, s.id_unit, s.dt, (SUM(s.mov_in - s.mov_out) * i.mass) AS f_total " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS de ON " +
            "d.id_year = de.id_year AND d.id_doc = de.id_doc " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s ON " +
            "de.id_year = s.fid_diog_year AND de.id_doc = s.fid_diog_doc AND de.id_ety = s.fid_diog_ety " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON " +
            "i.id_item = de.fid_item " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS u ON " +
            "u.id_unit = de.fid_unit " +
            "WHERE d.b_del = 0 AND de.b_del = 0 AND s.b_del = 0 " +
            /*"AND " + // XXX jbarajas
            "((d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_PUR_PUR[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_PUR_PUR[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_PUR_PUR[2] + ") OR " +
            "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_PUR_PUR[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_PUR_PUR[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_PUR_PUR[2] + ") OR " +
            "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_EXT_CHG_PUR[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_EXT_CHG_PUR[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_EXT_CHG_PUR[2] + ") OR " +
            "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_EXT_CHG_PUR[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_EXT_CHG_PUR[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_EXT_CHG_PUR[2] + ") OR " +
            "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_EXT_WAR_PUR[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_EXT_WAR_PUR[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_EXT_WAR_PUR[2] + ") OR " +
            "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_EXT_WAR_PUR[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_EXT_WAR_PUR[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_EXT_WAR_PUR[2] + ") OR " +
            "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_EXT_CSG_PUR[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_EXT_CSG_PUR[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_EXT_CSG_PUR[2] + ") OR " +
            "(d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_EXT_CSG_PUR[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_EXT_CSG_PUR[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_EXT_CSG_PUR[2] + ")) " +*/
            (idItem == SLibConsts.UNDEFINED ? "" : "AND s.id_item = " + idItem) + " AND " +
            " d.id_year = " + year + " AND d.dt <= '" + SLibUtils.DbmsDateFormatDate.format(date) + "' " +
            "GROUP BY s.id_item " + (isByItem ? "" : ", s.id_unit, s.id_cob, s.id_wh ") +
            "ORDER BY s.id_item " + (isByItem ? "" : ", s.id_unit, s.id_cob, s.id_wh ");
    }

    private String composeSqlQueryForPurchasingExpensesApportionByWarehouseType(final int idItem, final int year, final Date date, final boolean isByItem, final int[] warehouseType) {
        // XXX jbarajas new method
        return "SELECT s.id_cob, s.id_wh, s.id_item, s.id_unit, s.dt, (SUM(s.mov_in - s.mov_out) * i.mass) AS f_total " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS de ON " +
            "d.id_year = de.id_year AND d.id_doc = de.id_doc " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s ON " +
            "de.id_year = s.fid_diog_year AND de.id_doc = s.fid_diog_doc AND de.id_ety = s.fid_diog_ety " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON " +
            "i.id_item = de.fid_item " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS u ON " +
            "u.id_unit = de.fid_unit " +
            (warehouseType == null ? "" :
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS bpb ON s.id_cob = bpb.id_bpb " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_COB_ENT) + " AS ent ON s.id_cob = ent.id_cob AND s.id_wh = ent.id_ent ") +
            "WHERE d.b_del = 0 AND de.b_del = 0 AND s.b_del = 0 " +
            (idItem == SLibConsts.UNDEFINED ? "" : "AND s.id_item = " + idItem) + " AND " +
            (warehouseType == null ? "" : "ent.fid_ct_ent = " + warehouseType[0] + " AND ent.fid_tp_ent = " + warehouseType[1] + " AND ") +
            "d.id_year = " + year + " AND d.dt <= '" + SLibUtils.DbmsDateFormatDate.format(date) + "' " +
            "GROUP BY s.id_item " + (isByItem ? "" : ", s.id_unit, s.id_cob, s.id_wh ") +
            "ORDER BY s.id_item " + (isByItem ? "" : ", s.id_unit, s.id_cob, s.id_wh ");
    }

    private boolean isSimpleDiog(final int[] diogType) {
        boolean isSimple = true;

        if (SLibUtils.compareKeys(diogType, SModSysConsts.TRNS_TP_IOG_IN_INT_TRA) ||
                SLibUtils.compareKeys(diogType, SModSysConsts.TRNS_TP_IOG_OUT_INT_TRA) ||
                SLibUtils.compareKeys(diogType, SModSysConsts.TRNS_TP_IOG_IN_INT_CNV) ||
                SLibUtils.compareKeys(diogType, SModSysConsts.TRNS_TP_IOG_OUT_INT_CNV) ||
                SLibUtils.compareKeys(diogType, SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_ASD) ||
                SLibUtils.compareKeys(diogType, SModSysConsts.TRNS_TP_IOG_OUT_MFG_RM_ASD) ||
                SLibUtils.compareKeys(diogType, SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_RET) ||
                SLibUtils.compareKeys(diogType, SModSysConsts.TRNS_TP_IOG_OUT_MFG_RM_RET) ||
                SLibUtils.compareKeys(diogType, SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_ASD) ||
                SLibUtils.compareKeys(diogType, SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_ASD) ||
                SLibUtils.compareKeys(diogType, SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_RET) ||
                SLibUtils.compareKeys(diogType, SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_RET) ||
                SLibUtils.compareKeys(diogType, SModSysConsts.TRNS_TP_IOG_IN_MFG_FG_ASD) ||
                SLibUtils.compareKeys(diogType, SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_ASD) ||
                SLibUtils.compareKeys(diogType, SModSysConsts.TRNS_TP_IOG_IN_MFG_FG_RET) ||
                SLibUtils.compareKeys(diogType, SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_RET)) {
            isSimple = false;
        }

        return isSimple;
    }

    private boolean isSimpleDiogByUser(final int[] diogType) {
        boolean isSimple = false;

        if (SLibUtils.compareKeys(diogType, SModSysConsts.TRNS_TP_IOG_IN_PUR_PUR) ||
                SLibUtils.compareKeys(diogType, SModSysConsts.TRNS_TP_IOG_IN_SAL_SAL) ||
                SLibUtils.compareKeys(diogType, SModSysConsts.TRNS_TP_IOG_IN_ADJ_INV) ||
                SLibUtils.compareKeys(diogType, SModSysConsts.TRNS_TP_IOG_IN_ADJ_ADJ) ||
                SLibUtils.compareKeys(diogType, SModSysConsts.TRNS_TP_IOG_IN_EXT_CHG_PUR) ||
                SLibUtils.compareKeys(diogType, SModSysConsts.TRNS_TP_IOG_IN_EXT_CHG_SAL) ||
                SLibUtils.compareKeys(diogType, SModSysConsts.TRNS_TP_IOG_IN_EXT_WAR_PUR) ||
                SLibUtils.compareKeys(diogType, SModSysConsts.TRNS_TP_IOG_IN_EXT_WAR_SAL) ||
                SLibUtils.compareKeys(diogType, SModSysConsts.TRNS_TP_IOG_IN_EXT_CSG_PUR) ||
                SLibUtils.compareKeys(diogType, SModSysConsts.TRNS_TP_IOG_IN_EXT_CSG_SAL)) {
            isSimple = true;
        }

        return isSimple;
    }

    private double[] obtainStockBalance(final SGuiSession session, final int pkYearId, final int pkItemId, final int pkUnitId,
        final int pkCompanyBranchId, final int pkWarehouseId, final Date date) throws Exception {

        double stock = 0;
        double balance = 0;

        Statement statement = session.getDatabase().getConnection().createStatement();
        ResultSet resultSet = null;

        msSql = "SELECT SUM(s.mov_in - s.mov_out) AS f_stock, SUM(s.debit - s.credit) AS f_balance " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS de ON " +
                "d.id_year = de.id_year AND d.id_doc = de.id_doc " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s ON " +
                "s.fid_diog_year = de.id_year AND s.fid_diog_doc = de.id_doc AND s.fid_diog_ety = de.id_ety " +
                "WHERE d.b_del = 0 AND de.b_del = 0 AND s.b_del = 0 AND " +
                "s.id_year = " + pkYearId + " AND " +
                "s.id_item = " + pkItemId + " AND " +
                "s.id_unit = " + pkUnitId + " AND " +
                "s.id_cob = " + pkCompanyBranchId + " AND " +
                "s.id_wh = " + pkWarehouseId + " AND " +
                "d.dt < '" + SLibUtils.DbmsDateFormatDate.format(date) + "' " +
                "GROUP BY s.id_year, s.id_item, s.id_unit, s.id_cob, s.id_wh; ";
        resultSet = statement.executeQuery(msSql);
        if (resultSet.next()) {
            stock = resultSet.getDouble("f_stock");
            balance = resultSet.getDouble("f_balance");
        }

        return new double[] { stock, balance };
    }

    private double[] obtainStockBalanceInputs(final SGuiSession session, final int pkYearId, final int pkItemId, final int pkUnitId,
        final int pkCompanyBranchId, final int pkWarehouseId, final Date date) throws Exception {

        double stock = 0;
        double balance = 0;

        Statement statement = session.getDatabase().getConnection().createStatement();
        ResultSet resultSet = null;

        msSql = "SELECT SUM(s.mov_in - s.mov_out) AS f_stock, SUM(s.debit - s.credit) AS f_balance " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS de ON " +
                "d.id_year = de.id_year AND d.id_doc = de.id_doc " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s ON " +
                "s.fid_diog_year = de.id_year AND s.fid_diog_doc = de.id_doc AND s.fid_diog_ety = de.id_ety " +
                "WHERE d.b_del = 0 AND de.b_del = 0 AND s.b_del = 0 AND " +
                "d.fid_ct_iog = " + SModSysConsts.TRNS_CT_IOG_IN  + " AND " +
                "s.id_year = " + pkYearId + " AND " +
                "s.id_item = " + pkItemId + " AND " +
                "s.id_unit = " + pkUnitId + " AND " +
                "s.id_cob = " + pkCompanyBranchId + " AND " +
                "s.id_wh = " + pkWarehouseId + " AND " +
                "d.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(date) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(date) + "' " +
                "GROUP BY s.id_year, s.id_item, s.id_unit, s.id_cob, s.id_wh; ";
        resultSet = statement.executeQuery(msSql);
        if (resultSet.next()) {
            stock = resultSet.getDouble("f_stock");
            balance = resultSet.getDouble("f_balance");
        }

        return new double[] { stock, balance };
    }

    private void validateAllPeriods(final SGuiSession session, final int maxYear, final int maxPeriod) throws Exception {
        int minYear = 0;
        int minPeriod = 0;
        ResultSet resultSet = null;

        // Get first inventory valuation period:

        msSql = "SELECT fk_year_year, fk_year_per "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_INV_VAL) + " "
                + "WHERE id_inv_val = (SELECT COALESCE(MIN(id_inv_val), 0) FROM " + SModConsts.TablesMap.get(SModConsts.TRN_INV_VAL) + " WHERE b_del=0) ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception("No existen aún valuaciones de inventarios en esta empresa.");
        }
        else {
            minYear = resultSet.getInt("fk_year_year");
            minPeriod = resultSet.getInt("fk_year_per");
            
            msSql = "SELECT fk_year_year, fk_year_per "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_INV_VAL) + " "
                    + "WHERE (fk_year_year<" + maxYear + " OR (fk_year_year=" + maxYear + " AND fk_year_per<=" + maxPeriod + ")) AND "
                    + "b_del=0 "
                    + "ORDER BY fk_year_year, fk_year_per ";
            resultSet = session.getStatement().executeQuery(msSql);
            while (resultSet.next()) {
                if (resultSet.getInt(1) != minYear || resultSet.getInt(2) != minPeriod) {
                    throw new Exception("No existe la valuación de inventarios del periodo: " + minYear + "-" + SLibUtils.DecimalFormatCalendarMonth.format(minPeriod) + ".");
                }
                
                if (minPeriod < SLibTimeConsts.MONTH_MAX) {
                    minPeriod++;
                }
                else {
                    minYear++;
                    minPeriod = 1;
                }
            }
        }
    }

    /*
     * Public methods
     */

    public void setPkInventoryValuationId(int n) { mnPkInventoryValuationId = n; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkYearYearId(int n) { mnFkYearYearId = n; }
    public void setFkYearPeriodId(int n) { mnFkYearPeriodId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkInventoryValuationId() { return mnPkInventoryValuationId; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkYearYearId() { return mnFkYearYearId; }
    public int getFkYearPeriodId() { return mnFkYearPeriodId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public void setAuxIsConsistent(boolean b) { mbAuxIsConsistent = b; }
    public void setAuxExistInventoryMoves(boolean b) { mbAuxExistInventoryMoves = b; }
    public void setAuxExistInventoryPurchasingExpenses(boolean b) { mbAuxExistInventoryPurchasingExpenses = b; }
    public void setAuxExistPurchasingExpenses(boolean b) { mbAuxExistPurchasingExpenses = b; }
    public void setAuxExistInventoryMfgExpenses(boolean b) { mbAuxExistInventoryMfgExpenses = b; }
    public void setAuxExistMfgExpenses(boolean b) { mbAuxExistMfgExpenses = b; }
    public void setAuxAccountMask(String s) { msAuxAccountMask = s; }
    public void setAuxMfgCostUnitType(String s) { msAuxMfgCostUnitType = s; }
    public void setAuxMfgCostUnitTypeSql(String s) { msAuxMfgCostUnitTypeSql = s; }
    public void setAuxPeriodQuery(String s) { msAuxPeriodQuery = s; }
    public void setAuxDateStart(Date t) { mtAuxDateStart = t; }
    public void setAuxDateEnd(Date t) { mtAuxDateEnd = t; }
    public void setAuxRawMaterialWarehouseKey(int[] n) { manAuxRawMaterialWarehouseKey = n; };

    public boolean getAuxIsConsistent() { return mbAuxIsConsistent; }
    public boolean getAuxExistInventoryMoves() { return mbAuxExistInventoryMoves; }
    public boolean getAuxExistInventoryPurchasingExpenses() { return mbAuxExistInventoryPurchasingExpenses; }
    public boolean getAuxExistPurchasingExpenses() { return mbAuxExistPurchasingExpenses; }
    public boolean getAuxExistInventoryMfgExpenses() { return mbAuxExistInventoryMfgExpenses; }
    public boolean getAuxExistMfgExpenses() { return mbAuxExistMfgExpenses; }
    public String getAuxAccountMask() { return msAuxAccountMask; }
    public String getAuxMfgCostUnitType() { return msAuxMfgCostUnitType; }
    public String getAuxMfgCostUnitTypeSql() { return msAuxMfgCostUnitTypeSql; }
    public String getAuxPeriodQuery() { return msAuxPeriodQuery; }
    public Date getAuxDateStart() { return mtAuxDateStart; }
    public Date getAuxDateEnd() { return mtAuxDateEnd; }
    public int[] getAuxRawMaterialWarehouseKey() { return manAuxRawMaterialWarehouseKey; };

    public void validatePurchasingExpensesInventory(SGuiSession session) throws Exception {
        mbAuxExistInventoryMoves = false;
        mbAuxExistInventoryPurchasingExpenses = false;
        mbAuxExistPurchasingExpenses = false;

        Statement statement = session.getDatabase().getConnection().createStatement();
        ResultSet resultSet = null;

        // Obtain inventory moves in general:

        msSql = "SELECT SUM(s.mov_in), SUM(s.mov_out) " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS de ON " +
                "d.id_year = de.id_year AND d.id_doc = de.id_doc " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s ON " +
                "de.id_year = s.fid_diog_year AND de.id_doc = s.fid_diog_doc AND de.id_ety = s.fid_diog_ety " +
                "WHERE d.b_del = 0 AND de.b_del = 0 AND s.b_del = 0 AND d.dt " + msAuxPeriodQuery + " " +
                "GROUP BY d.id_year ";
        resultSet = statement.executeQuery(msSql);
        if (resultSet.next()) {
            if (resultSet.getDouble(1) > 0d ||
                    resultSet.getDouble(2) > 0d) {

                mbAuxExistInventoryMoves  = true;
            }
        }

        if (mbAuxExistInventoryMoves) {

            // Obtain inventory for purchasing expenses:

            msSql = composeSqlQueryForPurchasingExpensesApportion(SLibConsts.UNDEFINED, mnFkYearYearId, mtAuxDateEnd, false); // XXX jbarajas mnFkYearYearId, mtAuxDateEnd
            resultSet = statement.executeQuery(msSql);
            if (resultSet.next()) {

                mbAuxExistInventoryPurchasingExpenses = true;
            }

            // Obtain purshasing expenses:

            msSql = composeSqlQueryForPurchasingExpenses(SLibConsts.UNDEFINED);
            resultSet = statement.executeQuery(msSql);
            if (resultSet.next()) {
                mbAuxExistPurchasingExpenses = true;
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void validateMfgExpensesInventory(SGuiSession session) throws Exception {
        mbAuxExistInventoryMfgExpenses = false;
        mbAuxExistMfgExpenses = false;

        String sProductionOrders = "";
        ArrayList<SDataProductionOrder> aProductionOrders = new ArrayList<>();

        Statement statement = session.getDatabase().getConnection().createStatement();
        ResultSet resultSet = null;

        // Obtain productions orders:

        aProductionOrders.clear();
        aProductionOrders = obtainProductionOrders(session, mtAuxDateStart, false, true);
        sProductionOrders = aProductionOrders.size() > 0 ? "(" : "";
        for (SDataProductionOrder order : aProductionOrders) {
            sProductionOrders += "(o.id_year = " + order.getPkYearId() + " AND o.id_ord = " + order.getPkOrdId() + ") OR ";
        }
        sProductionOrders = sProductionOrders.isEmpty() ? "" : sProductionOrders.substring(0, sProductionOrders.length() - 3);
        sProductionOrders += aProductionOrders.size() > 0 ? ")" : "";

        // Obtain inventory for manufacturing expenses:

        if (!sProductionOrders.isEmpty()) {

            msSql = composeSqlQueryMfgAmount(SLibConsts.UNDEFINED, SLibConsts.UNDEFINED, false, SLibConsts.UNDEFINED, false, sProductionOrders);
            resultSet = statement.executeQuery(msSql);
            if (resultSet.next()) {
                mbAuxExistInventoryMfgExpenses = true;
            }
        }

        // Obtain manufacturing expenses:

        msSql = "SELECT re.fid_item_n, SUM(re.debit - re.credit) AS f_bal " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS re ON " +
            "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS acc ON " +
            "re.fid_acc = acc.id_acc " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON " +
            "re.fid_item_n = i.id_item " +
            "WHERE r.b_del = 0 AND re.b_del = 0 AND " +
            "(SELECT COUNT(*) " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " " +
            "WHERE id_acc = CONCAT( " +
            "LEFT(acc.id_acc, INSTR(acc.id_acc, '-') -1), " +
            "RIGHT('" + msAuxAccountMask + "', LENGTH(acc.id_acc) - (INSTR(acc.id_acc, '-') -1))) AND " +
            "(fid_tp_acc_r = " + SDataConstantsSys.FINS_CLS_ACC_EXPEN_MFG[0] + " AND " +
            "fid_cl_acc_r = " + SDataConstantsSys.FINS_CLS_ACC_EXPEN_MFG[1] + " AND " +
            "fid_cls_acc_r = " + SDataConstantsSys.FINS_CLS_ACC_EXPEN_MFG[2] + ")) > 0 AND " +
            "r.dt " + msAuxPeriodQuery + " " +
            "GROUP BY re.fid_item_n " +
            "ORDER BY re.fid_item_n ";

        resultSet = statement.executeQuery(msSql);
        if (resultSet.next()) {
            mbAuxExistMfgExpenses = true;
        }
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnFkYearYearId = pk[0];
        mnFkYearPeriodId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnFkYearYearId, mnFkYearPeriodId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkInventoryValuationId = 0;
        mbDeleted = false;
        mnFkYearYearId = 0;
        mnFkYearPeriodId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;

        mbAuxIsConsistent = false;
        mbAuxExistInventoryMoves = false;
        mbAuxExistInventoryPurchasingExpenses = false;
        mbAuxExistPurchasingExpenses = false;
        mbAuxExistInventoryMfgExpenses = false;
        mbAuxExistMfgExpenses = false;
        msAuxAccountMask = "";
        msAuxMfgCostUnitType = "";
        msAuxMfgCostUnitTypeSql = "";
        msAuxPeriodQuery = "";
        mtAuxDateStart = null;
        mtAuxDateEnd = null;
        manAuxRawMaterialWarehouseKey = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_inv_val = " + mnPkInventoryValuationId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_inv_val = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkInventoryValuationId = 0;

        msSql = "SELECT COALESCE(MAX(id_inv_val), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkInventoryValuationId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet = null;
        Statement statement = session.getStatement();

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkInventoryValuationId = resultSet.getInt("id_inv_val");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkYearYearId = resultSet.getInt("fk_year_year");
            mnFkYearPeriodId = resultSet.getInt("fk_year_per");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            statement.execute("SET @estatus := 1;");

            msSql = "SELECT " +
                "IF(@estatus = 1, @estatus := IF((" +
                "SELECT MAX(ts_edit) " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " " +
                "WHERE id_year = v.fk_year_year AND (MONTH(dt)) = v.fk_year_per) > v.ts_usr_upd, 0, 1), 0) AS f_estatus " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_INV_VAL) + " AS v " +
                "WHERE v.b_del = 0 AND v.fk_year_year = " + mnFkYearYearId + " AND v.fk_year_per = " + (mnFkYearPeriodId - 1) + "; ";
            resultSet = statement.executeQuery(msSql);

            if (!resultSet.next()) {
                mbAuxIsConsistent = true;
            }
            else {
                mbAuxIsConsistent = resultSet.getBoolean(1);
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

        if (msQueryResult.isEmpty()) {
            if (mbRegistryNew) {
                computePrimaryKey(session);
                mbDeleted = false;
                mnFkUserInsertId = session.getUser().getPkUserId();
                mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

                msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkInventoryValuationId + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    mnFkYearYearId + ", " +
                    mnFkYearPeriodId + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
            }
            else {
                mnFkUserUpdateId = session.getUser().getPkUserId();

                msSql = "UPDATE " + getSqlTable() + " SET " +
                    // "id_inv_val = " + mnPkInventoryValuation + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_year_year = " + mnFkYearYearId + ", " +
                    "fk_year_per = " + mnFkYearPeriodId + ", " +
                    // "fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    // "ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
            }
            session.getStatement().execute(msSql);

            // Compute inventory valuation:

            computeInventoryValuation(session);

            mbRegistryNew = false;
            mnQueryResultId = SDbConsts.SAVE_OK;
        }
        else {
            //throws new Exception;
        }
    }

    @Override
    public boolean canDelete(SGuiSession session) throws SQLException, Exception {
        boolean can = super.canDelete(session);

        if (can) {

        }

        return can;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean canSave(SGuiSession session) throws SQLException, Exception {
        boolean can = super.canSave(session);
        int[] prevPeriodKey = null;
        ResultSet resultSet = null;
        ArrayList<SDataDiog> diogs = null;
        ArrayList<SDataProductionOrder> productionOrders = null;
        SDbInventoryValuationXXX prevInventoryValuation = null;

        if (can) {
            msAuxMfgCostUnitType = "";
            msAuxMfgCostUnitTypeSql = "";
            manAuxRawMaterialWarehouseKey = null;
            
            diogs = new ArrayList<>();
            productionOrders = new ArrayList<>();

            // 3. Validate whether it is possible to compute inventory valuation:

            // 3.1. Validate company configuration for Inventory Valuation Calculation Method (MCVI):

            if (((SDataParamsCompany) session.getConfigCompany()).getInventoryValuationMethod() != SModSysConsts.CFGX_IVM_AVG) {
                can = false;
                msQueryResult = "No se ha configurado en esta empresa un Método de Cálculo de Valuación de Inventarios (MCVI) soportado por el sistema.\n1 MCVI soportados: costo promedio.";
            }

            // 3.2. Validate company configuration for Unit Type for Manufacturing Cost Calculation (CCF):

            if (can) {
                msAuxMfgCostUnitType = (String) session.readField(SModConsts.ITMU_TP_UNIT, new int[] { ((SDataParamsCompany) session.getConfigCompany()).getFkMfgCostUnitTypeId() }, SDbRegistry.FIELD_NAME);
                
                switch (((SDataParamsCompany) session.getConfigCompany()).getFkMfgCostUnitTypeId()) {
                    case SModSysConsts.ITMU_TP_UNIT_LEN:
                        msAuxMfgCostUnitTypeSql = "i.len";
                        break;
                    case SModSysConsts.ITMU_TP_UNIT_SURF:
                        msAuxMfgCostUnitTypeSql = "i.surf";
                        break;
                    case SModSysConsts.ITMU_TP_UNIT_VOL:
                        msAuxMfgCostUnitTypeSql = "i.vol";
                        break;
                    case SModSysConsts.ITMU_TP_UNIT_MASS:
                        msAuxMfgCostUnitTypeSql = "i.mass";
                        break;
                    case SModSysConsts.ITMU_TP_UNIT_TIME:
                        msAuxMfgCostUnitTypeSql = "i.prod_time";
                        break;
                    default:
                        can = false;
                        msQueryResult = "No se ha configurado en esta empresa el Tipo de Unidad base para el Cálculo de Costos de Fabricación (CCF).";
                }
            }

            // 3.3. Validate that there is one and only one default warehouse for Raw Materials:

            if (can) {
                msSql = "SELECT COUNT(*) "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_COB_ENT) + " "
                        + "WHERE fid_ct_ent = " + SModSysConsts.CFGS_TP_ENT_WH_MFG_RM[0] + " AND fid_tp_ent = " + SModSysConsts.CFGS_TP_ENT_WH_MFG_RM[1] + " AND "
                        + "b_del = 0 AND b_def = 1 ";
                resultSet = session.getStatement().executeQuery(msSql);
                if (resultSet.next()) {
                    if (resultSet.getInt(1) == 0) {
                        can = false;
                        msQueryResult = "No existen en esta empresa un almacenes predeterminados de tipo Materia Prima,\nes necesario solamente uno para la asignación por defecto de gastos de compra.";
                    }
                    else if (resultSet.getInt(1) > 1) {
                        can = false;
                        msQueryResult = "Existen en esta empresa " + resultSet.getInt(1) + " almacenes 'predeterminados' de tipo Materia Prima,\nes necesario solamente uno para la asignación por defecto de gastos de compra.";
                    }
                    else {
                        msSql = "SELECT id_cob, id_ent "
                                + "FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_COB_ENT) + " "
                                + "WHERE fid_ct_ent = " + SModSysConsts.CFGS_TP_ENT_WH_MFG_RM[0] + " AND fid_tp_ent = " + SModSysConsts.CFGS_TP_ENT_WH_MFG_RM[1] + " AND "
                                + "b_del = 0 AND b_def = 1 ";
                        resultSet = session.getStatement().executeQuery(msSql);
                        if (resultSet.next()) {
                            manAuxRawMaterialWarehouseKey = new int[] { resultSet.getInt(1), resultSet.getInt(2) };
                        }
                    }
                }
            }

            // 3.4. Validate that all accounting periods preceding the current one inclusive are closed:

            if (can) {
                msSql = "SELECT "
                        + "(SELECT COUNT(*) FROM fin_year_per WHERE id_year<" + mnFkYearYearId + " AND b_closed=0) "
                        + "(SELECT COUNT(*) FROM fin_year_per WHERE id_year=" + mnFkYearYearId + " AND id_per<=" + mnFkYearPeriodId + " AND b_closed=0) ";
                if (resultSet.next()) {
                    if (resultSet.getInt(1) > 0) {
                        can = false;
                        msQueryResult = (resultSet.getInt(1) == 1 ? "Existe 1 periodo contable abierto " : "Existen " + resultSet.getInt(1) + " periodos contables abiertos ")
                                + "antes del periodo " + mnFkYearYearId + "-" + SLibUtils.DecimalFormatCalendarMonth.format(mnFkYearPeriodId) + " inclusive.";
                    }
                }
            }

            // 3.5. Validate that the Calculation Period (PC) inmediately preceding current Inventory Valuation Calculation (CVI) is consistent:

            // (a) 3.5.1. There must exist all preceding PC up to current CVI:

            if (can) {
                prevPeriodKey = SLibTimeUtils.digestMonth(SLibTimeUtils.addDate(SLibTimeUtils.createDate(mnFkYearYearId, mnFkYearPeriodId), 0, -1, 0));
                validateAllPeriods(session, prevPeriodKey[0], prevPeriodKey[1]);
                
                // Get previous period's CVI, if any:

                msSql = "SELECT id_inv_val " +
                        "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_INV_VAL) + " " +
                        "WHERE fk_year_year = " + prevPeriodKey[0] + " AND fk_year_per = " + prevPeriodKey[1] + " AND b_del = 0 ";
                resultSet = session.getStatement().executeQuery(msSql);
                if (resultSet.next()) {
                    prevInventoryValuation = (SDbInventoryValuationXXX) session.readRegistry(SModConsts.TRN_INV_VAL, new int[] { resultSet.getInt(1) });
                }
            }

            // (b) 3.5.2. All previous purchases costs (GC) must have been inputted before the immediately previous PC:

            if (can && prevInventoryValuation != null) {
                msSql = "SELECT COUNT(*) " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS re ON " +
                    "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS acc ON " +
                    "re.fid_acc = acc.id_acc " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON " +
                    "re.fid_item_n = i.id_item " +
                    "WHERE r.b_del = 0 AND re.b_del = 0 AND " +
                    "(SELECT COUNT(*) " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " " +
                    "WHERE id_acc = CONCAT( " +
                    "LEFT(acc.id_acc, INSTR(acc.id_acc, '-') -1), " +
                    "RIGHT('" + msAuxAccountMask + "', LENGTH(acc.id_acc) - (INSTR(acc.id_acc, '-') -1))) AND " +
                    "(fid_tp_acc_r = " + SDataConstantsSys.FINS_CLS_ACC_EXPEN_PUR[0] + " AND " +
                    "fid_cl_acc_r = " + SDataConstantsSys.FINS_CLS_ACC_EXPEN_PUR[1] + " AND " +
                    "fid_cls_acc_r = " + SDataConstantsSys.FINS_CLS_ACC_EXPEN_PUR[2] + ")) > 0 AND " +
                    "re.fid_item_aux_n IS NOT NULL AND " +
                    "YEAR(r.dt) = " + prevPeriodKey[0] + " AND " +
                    "MONTH(r.dt) = " + prevPeriodKey[1] + " AND " +
                    "(re.ts_edit > '" + SLibUtils.DbmsDateFormatDatetime.format(prevInventoryValuation.getTsUserUpdate()) + "' OR " +
                    "re.ts_del > '" + SLibUtils.DbmsDateFormatDatetime.format(prevInventoryValuation.getTsUserUpdate()) + "') " +
                    "GROUP BY re.id_year, re.id_per, re.id_bkc, re.id_tp_rec, re.id_num, re.id_ety " +
                    "ORDER BY re.id_year, re.id_per, re.id_bkc, re.id_tp_rec, re.id_num, re.id_ety ";
                resultSet = session.getStatement().executeQuery(msSql);
                if (resultSet.next()) {
                    if (resultSet.getInt(1) > SLibConsts.UNDEFINED) {
                        can = false;
                        msQueryResult = "Existen gastos de compra capturados o modificados después de la valuación de inventarios "
                                + "del periodo: " + prevPeriodKey[0] + "-" + SLibUtils.DecimalFormatCalendarMonth.format(prevPeriodKey[1]) + ".";
                    }
                }
            }

            // (c) 3.5.3. All previous manufacturing costs (GF) must have been inputted before the immediately previous PC:

            if (can && prevInventoryValuation != null) {
                msSql = "SELECT COUNT(*) " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS re ON " +
                    "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS acc ON " +
                    "re.fid_acc = acc.id_acc " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON " +
                    "re.fid_item_n = i.id_item " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_IGEN) + " AS ig ON " +
                    "i.fid_igen = ig.id_igen " +
                    "WHERE r.b_del = 0 AND re.b_del = 0 AND " +
                    "(SELECT COUNT(*) " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " " +
                    "WHERE id_acc = CONCAT( " +
                    "LEFT(acc.id_acc, INSTR(acc.id_acc, '-') -1), " +
                    "RIGHT('" + msAuxAccountMask + "', LENGTH(acc.id_acc) - (INSTR(acc.id_acc, '-') -1))) AND " +
                    "((fid_tp_acc_r = " + SDataConstantsSys.FINS_CLS_ACC_EXPEN_MFG[0] + " AND " +
                    "fid_cl_acc_r = " + SDataConstantsSys.FINS_CLS_ACC_EXPEN_MFG[1] + " AND " +
                    "fid_cls_acc_r = " + SDataConstantsSys.FINS_CLS_ACC_EXPEN_MFG[2] + "))) > 0 AND " +
                    "YEAR(r.dt) = " + prevPeriodKey[0] + " AND " +
                    "MONTH(r.dt) = " + prevPeriodKey[1] + " AND " +
                    "(re.ts_edit > '" + SLibUtils.DbmsDateFormatDatetime.format(prevInventoryValuation.getTsUserUpdate()) + "' OR " +
                    "re.ts_del > '" + SLibUtils.DbmsDateFormatDatetime.format(prevInventoryValuation.getTsUserUpdate()) + "') " +
                    "GROUP BY re.id_year, re.id_per, re.id_bkc, re.id_tp_rec, re.id_num, re.id_ety " +
                    "ORDER BY re.id_year, re.id_per, re.id_bkc, re.id_tp_rec, re.id_num, re.id_ety ";
                resultSet = session.getStatement().executeQuery(msSql);
                if (resultSet.next()) {
                    if (resultSet.getInt(1) > SLibConsts.UNDEFINED) {
                        can = false;
                        msQueryResult = "Existen gastos de producción capturados o modificados después de la valuación de inventarios "
                                + "del periodo: " + prevPeriodKey[0] + "-" + SLibUtils.DecimalFormatCalendarMonth.format(prevPeriodKey[1]) + ".";
                    }
                }
            }

            // (d) 3.5.4. All inventory moves (MA) must have been inputted before the immediately previous PC:

            if (can && prevInventoryValuation != null) {
                msSql = "SELECT COUNT(*) " +
                        "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d " +
                        "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS de ON " +
                        "d.id_year = de.id_year AND d.id_doc = de.id_doc " +
                        "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s ON " +
                        "de.id_year = s.fid_diog_year AND de.id_doc = s.fid_diog_doc AND de.id_ety = s.fid_diog_ety " +
                        "WHERE d.b_del = 0 AND de.b_del = 0 AND  s.b_del = 0 AND " +
                        "YEAR(d.dt) = " + prevPeriodKey[0] + " AND " +
                        "MONTH(d.dt) = " + prevPeriodKey[1] + " AND " +
                        "(de.ts_edit > '" + SLibUtils.DbmsDateFormatDatetime.format(prevInventoryValuation.getTsUserUpdate()) + "' OR " +
                        "de.ts_del > '" + SLibUtils.DbmsDateFormatDatetime.format(prevInventoryValuation.getTsUserUpdate()) + "') " +
                        "GROUP BY d.id_year, d.id_doc " +
                        "ORDER BY d.id_year, d.id_doc ";
                resultSet = session.getStatement().executeQuery(msSql);
                if (resultSet.next()) {
                    if (resultSet.getInt(1) > SLibConsts.UNDEFINED) {
                        can = false;
                        msQueryResult = "Existen movimientos de inventario capturados o modificados después de la valuación de inventarios "
                                + "del periodo: " + prevPeriodKey[0] + "-" + SLibUtils.DecimalFormatCalendarMonth.format(prevPeriodKey[1]) + ".";
                    }
                }
            }

            // (e) 3.5.5. The CVI PC immediately preceding the current must have been generated after any modification (eg, opening/closing) and the corresponding previous accounting msAuxPeriodQuery, which must also be closed:

            if (can) {
                msSql = "SELECT COUNT(*) " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_YEAR) + " AS y " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_YEAR_PER) + " AS p ON " +
                    "y.id_year = p.id_year " +
                    "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_INV_VAL) + " AS v ON " +
                    "p.id_year = v.fk_year_year AND p.id_per = v.fk_year_per " +
                    "WHERE y.b_del = 0 AND p.b_del = 0 AND v.b_del = 0 AND " +
                    "v.fk_year_year <= " + prevPeriodKey[0] + " AND v.fk_year_per <= " + prevPeriodKey[1] + " AND p.ts_edit > v.ts_usr_upd AND " +
                    "p.b_closed = 1 ";
                resultSet = session.getStatement().executeQuery(msSql);
                if (resultSet.next()) {
                    if (resultSet.getInt(1) > SLibConsts.UNDEFINED) {
                        can = false;
                        msQueryResult = "Existen periodos contables abiertos o modificados después de la valuación de inventarios "
                                + "del periodo: " + prevPeriodKey[0] + "-" + SLibUtils.DecimalFormatCalendarMonth.format(prevPeriodKey[1]) + ".";
                    }
                }
            }

            // 3.6 Validate that all items purchased in the RM CP have defined corresponding to MASA (kg) (required Assignment GC) physical measurement (ie,> 0):

            if (can) {
                msQueryResult = validatePurchasesForPhysicalMeasurementItems(session);
                if (!msQueryResult.isEmpty()) {
                    can = false;
                }
            }

            // 3.7. Validate that the Manufacturing Order (MO) with Warehouse Moves (WM) in the CP are consistens:

            if (can) {

                // 3.7.1. Obtain list with production orders

                productionOrders.clear();
                productionOrders.addAll(obtainProductionOrders(session, mtAuxDateStart, false, false));
                for (SDataProductionOrder productionOrder : productionOrders) {

                    // 3.7.4. Validate with unit type of manufacturing costs calculation (MCC):

                    if (can) {
                        if (!validateProductionOrderInputsPreviousOuputMoves(session, (int[]) productionOrder.getPrimaryKey())) {
                            can = false;
                            msQueryResult = "La valuación de inventarios no puede continuar, existen movimientos de la orden de producción '" + productionOrder.getNumber() + "' de Entrega/Devolución de Insumos posteriores a la primer entrega de PP/PT.";
                            break;
                        }
                    }

                    // 3.7.2. Validated for each OP with CCF Status = "CCF No-Made" that:
                    // Validate that all MA Inputs have been made in the current PC, ie, the date of each MA must belong to the current PC.

                    if (productionOrder.getIsCostDone() == SModSysConsts.MFGX_ORD_CST_DONE_NO) {
                        msQueryResult = productionOrderObtainInventoryInputsMoves(session, (int[]) productionOrder.getPrimaryKey(), productionOrder.getNumber());
                        if (!msQueryResult.isEmpty()) {
                            can = false;
                            break;
                        }

                        // Validate that moves of production order are consistent (first inputs the output):

                        if (can) {
                            msQueryResult = validateMovesForProductionOrderInputs(session, productionOrder);
                            if (!msQueryResult.isEmpty()) {
                                can = false;
                                break;
                            }
                        }

                        if (can) {
                            msQueryResult = productionOrderValidateOrderMoves(session, productionOrder);
                            if (!msQueryResult.isEmpty()) {
                                can = false;
                                break;
                            }
                        }
                    }

                    // 3.7.3. Validated for each OP with CCF Status = "CCF Made" that:
                    // Validate that all MA Products Inputs, regardless of PC to which they belong, placed before the corresponding CVI, ie, the TS of each MA must be earlier than the corresponding TS CVI.

                    if (can) {
                        if (productionOrder.getIsCostDone() == SModSysConsts.MFGX_ORD_CST_DONE_YES) {
                            msQueryResult = productionOrderObtainInventoryInputsMoves(session, (int[]) productionOrder.getPrimaryKey(), productionOrder.getNumber());
                            if (!msQueryResult.isEmpty()) {
                                can = false;
                                break;
                            }

                            msQueryResult = productionOrderObtainInventoryProductsMoves(session, (int[]) productionOrder.getPrimaryKey(), productionOrder.getNumber());
                            if (!msQueryResult.isEmpty()) {
                                can = false;
                                break;
                            }
                        }
                    }

                    // 3.7.4. Validate with unit type of manufacturing costs calculation (MCC):

                    if (can) {
                        msQueryResult = validateMovesForProductionOrderPhysicalMeasurement(session, productionOrder);
                        if (!msQueryResult.isEmpty()) {
                            can = false;
                            break;
                        }
                    }
                }
            }

            // Validate that all output that corresponds to a compound MA has its corresponding entry 'mirror':

            if (can) {
                diogs.addAll(obtainDiogs(session));
                for (SDataDiog diog : diogs) {
                    if (diog.getFkDiogCategoryId() == SModSysConsts.TRNS_CT_IOG_OUT &&
                            !isSimpleDiog(new int[] { diog.getFkDiogCategoryId(), diog.getFkDiogClassId(), diog.getFkDiogTypeId() })&&
                            diog.getDbmsDataCounterpartDiog() == null) {
                                can = false;
                                msQueryResult = "El movimiento de inventario '" + (diog.getNumberSeries().isEmpty() ? "" : "-") + diog.getNumber() + "' no tiene su contraparte de entrada.";
                                break;
                    }
                }
            }

            if (can) {
                validatePurchasingExpensesInventory(session); // Update class members: mbAuxExistInventoryMoves, mbAuxExistInventoryPurchasingExpenses, mbAuxExistPurchasingExpenses
                validateMfgExpensesInventory(session); // Update class members: mbAuxExistInventoryMfgExpenses, mbAuxExistMfgExpenses

                if (!mbAuxExistInventoryMfgExpenses &&
                        mbAuxExistMfgExpenses) {
                        can = false;
                        msQueryResult = "No hay inventario disponible de producción para asignar los gastos de fabricación.";
                }
            }
        }

        if (!can) {
            mnQueryResultId = SDbConsts.SAVE_ERROR;
        }

        return can;
    }

    @Override
    public SDbInventoryValuationXXX clone() throws CloneNotSupportedException {
        SDbInventoryValuationXXX registry = new SDbInventoryValuationXXX();

        registry.setPkInventoryValuationId(this.getPkInventoryValuationId());
        registry.setDeleted(this.isDeleted());
        registry.setFkYearYearId(this.getFkYearYearId());
        registry.setFkYearPeriodId(this.getFkYearPeriodId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setAuxIsConsistent(this.getAuxIsConsistent());
        registry.setAuxExistInventoryMoves(this.getAuxExistInventoryMoves());
        registry.setAuxExistInventoryPurchasingExpenses(this.getAuxExistInventoryPurchasingExpenses());
        registry.setAuxExistPurchasingExpenses(this.getAuxExistPurchasingExpenses());
        registry.setAuxExistInventoryMfgExpenses(this.getAuxExistInventoryMfgExpenses());
        registry.setAuxExistMfgExpenses(this.getAuxExistMfgExpenses());
        registry.setAuxAccountMask(this.getAuxAccountMask());
        registry.setAuxMfgCostUnitType(this.getAuxMfgCostUnitType());
        registry.setAuxMfgCostUnitTypeSql(this.getAuxMfgCostUnitTypeSql());
        registry.setAuxPeriodQuery(this.getAuxPeriodQuery());
        registry.setAuxDateStart(this.getAuxDateStart());
        registry.setAuxDateEnd(this.getAuxDateEnd());
        registry.setAuxRawMaterialWarehouseKey(SLibUtils.cloneKey(this.getAuxRawMaterialWarehouseKey()));
                
        registry.setRegistryNew(mbRegistryNew);
        
        return registry;
    }
}
