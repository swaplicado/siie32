/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormComponentItem;
import erp.mcfg.data.SDataCompanyBranchEntity;
import erp.mod.SModSysConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;

/**
 *
 * @author Sergio Flores
 */
public class SDataRawMaterialsConsume extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    public static final int MODE_CONSUME = 1;
    public static final int MODE_DELETE = 2;

    protected int mnPkYearId;
    protected int mnPkOrderId;
    protected java.util.Date mtDate;
    protected int mnFkUserNewId;

    protected int mnAuxMode;
    protected double mdAuxPercentage;
    protected java.util.Vector<erp.mtrn.data.SDataDiog> mvDbmsDiogs;

    public SDataRawMaterialsConsume() {
        super(SDataConstants.TRNX_DIOG_MFG_CON);
        mvDbmsDiogs = new Vector<erp.mtrn.data.SDataDiog>();
        reset();
    }

    /*
     * Private methods
     */

    /*
     * Public methods
     */

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkOrderId(int n) { mnPkOrderId = n; }
    public void setDate(java.util.Date t) { mtDate = t; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkOrderId() { return mnPkOrderId; }
    public java.util.Date getDate() { return mtDate; }
    public int getFkUserNewId() { return mnFkUserNewId; }

    public void setAuxMode(int n) { mnAuxMode = n; }
    public void setAuxPercentage(double d) { if (d >= 1d) mdAuxPercentage = 1d; else if (d <= 0d) mdAuxPercentage = 0d; else mdAuxPercentage = d; }

    public int getAuxMode() { return mnAuxMode; }
    public double getAuxPercentage() { return mdAuxPercentage; }
    public java.util.Vector<erp.mtrn.data.SDataDiog> getDbmsDiogs() { return mvDbmsDiogs; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkYearId = ((int[]) pk)[0];
        mnPkOrderId = ((int[]) pk)[1];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkOrderId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkYearId = 0;
        mnPkOrderId = 0;
        mtDate = null;
        mnFkUserNewId = 0;

        mnAuxMode = MODE_CONSUME;
        mdAuxPercentage = 1;
        mvDbmsDiogs.clear();
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        return mnLastDbActionResult;
    }

    @Override
    public int save(java.sql.Connection connection) {
        int iteration = 0;
        int idCompanyBranch = 0;
        int idWarehouse = 0;
        int sortingPosition = 0;
        int year = SLibTimeUtilities.digestYear(mtDate)[0];
        int[] keyDiogClass = null;
        int[] keyDiogType = null;
        double factor = 0;
        double percentage = 0;
        String sql = "";
        String sqlSign = "";
        Statement statement = null;
        Statement statementAux = null;
        ResultSet resultSet = null;
        SDataCompanyBranchEntity warehouse = null;
        SDataDiog iog = null;
        SDataDiogNotes diogNotes = null;
        SDataDiogEntry diogEntry = null;
        STrnStockMove stockMove = null;
        Vector<SFormComponentItem> dnsForDiog = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            //System.out.println("SDataRawMaterialsConsume 1...");
            statement = connection.createStatement();
            statementAux = connection.createStatement();

            //System.out.println("SDataRawMaterialsConsume 2...");
            sql = "UPDATE trn_diog, trn_stk SET trn_stk.b_del = 1 " +
                    "WHERE trn_stk.b_del = 0 AND trn_stk.fid_diog_year = trn_diog.id_year AND trn_stk.fid_diog_doc = trn_diog.id_doc AND " +
                    "trn_diog.fid_mfg_year_n = " + mnPkYearId + " AND trn_diog.fid_mfg_ord_n = " + mnPkOrderId + " AND (" +
                    "(trn_diog.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_CON[0] + " AND trn_diog.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_CON[1] + " AND trn_diog.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_CON[2] + ") OR " +
                    "(trn_diog.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_CON[0] + " AND trn_diog.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_CON[1] + " AND trn_diog.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_CON[2] + ")); ";
            //System.out.println("[" + sql + "]");
            statement.execute(sql);

            //System.out.println("SDataRawMaterialsConsume 3...");
            sql = "UPDATE trn_diog SET b_del = 1 " +
                    "WHERE b_del = 0 AND fid_mfg_year_n = " + mnPkYearId + " AND fid_mfg_ord_n = " + mnPkOrderId + " AND (" +
                    "(fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_CON[0] + " AND fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_CON[1] + " AND fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_CON[2] + ") OR " +
                    "(fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_CON[0] + " AND fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_CON[1] + " AND fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_CON[2] + ")); ";
            //System.out.println("[" + sql + "]");
            statement.execute(sql);

            if (mnAuxMode == MODE_CONSUME) {
                //System.out.println("SDataRawMaterialsConsume 4...");
                for (iteration = 1; iteration <= 2; iteration++) {
                    idCompanyBranch = 0;
                    idWarehouse = 0;

                    //System.out.println("SDataRawMaterialsConsume 4 (iteration " + iteration + ") 1...");
                    switch (iteration) {
                        case 1:
                            /* In order to "consume" finished goods taken out from work in progress,
                             * corresponding items need to be moved in.
                             */
                            factor = -1;
                            percentage = 1;
                            sqlSign = "<";
                            keyDiogClass = SDataConstantsSys.TRNS_CL_IOG_IN_MFG;
                            keyDiogType = SDataConstantsSys.TRNS_TP_IOG_IN_MFG_CON;
                            break;
                        case 2:
                            /* In order to "consume" raw materials taken into work in progress,
                             * corresponding items need to be moved out.
                             */
                            factor = 1;
                            percentage = mdAuxPercentage;
                            sqlSign = ">";
                            keyDiogClass = SDataConstantsSys.TRNS_CL_IOG_OUT_MFG;
                            keyDiogType = SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_CON;
                            break;
                        default:
                    }

                    //System.out.println("SDataRawMaterialsConsume 4 (iteration " + iteration + ") 2...");
                    sql = "SELECT s.id_cob, s.id_wh, s.id_item, s.id_unit, s.id_lot, " +
                            "i.item_key, i.item, u.symbol, l.lot, l.dt_exp_n, " +
                            "o.fid_item_r <> i.id_item AS f_is_rm, " +
                            "SUM(s.mov_in - s.mov_out) AS f_net " +
                            "FROM trn_diog AS g " +
                            "INNER JOIN trn_diog_ety AS ge ON g.fid_mfg_year_n = " + mnPkYearId + " AND g.fid_mfg_ord_n = " + mnPkOrderId + " AND " +
                            "g.id_year = ge.id_year AND g.id_doc = ge.id_doc AND g.b_del = 0 AND ge.b_del = 0 " +
                            "INNER JOIN trn_stk AS s ON ge.id_year = s.fid_diog_year AND ge.id_doc = s.fid_diog_doc AND ge.id_ety = s.fid_diog_ety AND s.b_del = 0 " +
                            "INNER JOIN erp.itmu_item AS i ON s.id_item = i.id_item " +
                            "INNER JOIN erp.itmu_unit AS u ON s.id_unit = u.id_unit " +
                            "INNER JOIN trn_lot AS l ON s.id_item = l.id_item AND s.id_unit = l.id_unit AND s.id_lot = l.id_lot " +
                            "INNER JOIN mfg_ord AS o ON g.fid_mfg_year_n = o.id_year AND g.fid_mfg_ord_n = o.id_ord " +
                            "WHERE (" +
                            "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_RM_ASD[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_RM_ASD[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_RM_ASD[2] + ") OR " +
                            "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_RM_RET[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_RM_RET[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_RM_RET[2] + ") OR " +
                            "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_ASD[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_ASD[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_ASD[2] + ") OR " +
                            "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_RET[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_RET[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_RET[2] + ") OR " +
                            "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_ASD[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_ASD[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_ASD[2] + ") OR " +
                            "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_RET[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_RET[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_RET[2] + ") OR " +
                            "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_ASD[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_ASD[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_ASD[2] + ") OR " +
                            "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_RET[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_RET[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_RET[2] + ")) " +
                            "GROUP BY s.id_cob, s.id_wh, s.id_item, s.id_unit, s.id_lot, " +
                            "i.item_key, i.item, u.symbol, l.lot, l.dt_exp_n " +
                            "HAVING f_net " + sqlSign + " 0 " +
                            "ORDER BY f_is_rm, i.item_key, i.item, u.symbol, l.lot, l.dt_exp_n, " +
                            "s.id_cob, s.id_wh, s.id_item, s.id_unit, s.id_lot; ";
                    resultSet = statement.executeQuery(sql);
                    while (resultSet.next()) {
                        //System.out.println("SDataRawMaterialsConsume 4 (iteration " + iteration + ") 2.1...");
                        if (idCompanyBranch != resultSet.getInt("s.id_cob") && idWarehouse != resultSet.getInt("s.id_wh")) {
                            idCompanyBranch = resultSet.getInt("s.id_cob");
                            idWarehouse = resultSet.getInt("s.id_wh");
                            sortingPosition = 1;

                            //System.out.println("SDataRawMaterialsConsume 4 (iteration " + iteration + ") 2.1.1...");
                            warehouse = new SDataCompanyBranchEntity();
                            if (warehouse.read(new int[] { idCompanyBranch, idWarehouse }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                            }

                            //System.out.println("SDataRawMaterialsConsume 4 (iteration " + iteration + ") 2.1.2...");
                            dnsForDiog = warehouse.getDnsForDiog(keyDiogClass);

                            //System.out.println("SDataRawMaterialsConsume 4 (iteration " + iteration + ") 2.1.3...");
                            if (dnsForDiog.isEmpty()) {
                                throw new Exception(SLibConstants.MSG_ERR_GUI_NO_DNS_ + "'" + warehouse.getEntity() + "'.");
                            }

                            //System.out.println("SDataRawMaterialsConsume 4 (iteration " + iteration + ") 2.1.4...");
                            iog = new SDataDiog();
                            iog.setPkYearId(year);
                            iog.setPkDocId(0);
                            iog.setDate(mtDate);
                            iog.setNumberSeries(dnsForDiog.get(0).toString());
                            iog.setNumber("");
                            iog.setReference("");
                            iog.setValue_r(0);
                            iog.setCostAsigned(0);
                            iog.setCostTransferred(0);
                            iog.setIsShipmentRequired(false);
                            iog.setIsShipped(false);
                            iog.setIsAudited(false);
                            iog.setIsAuthorized(false);
                            iog.setIsRecordAutomatic(false);
                            iog.setIsSystem(true);
                            iog.setIsDeleted(false);
                            iog.setFkDiogCategoryId(keyDiogType[0]);
                            iog.setFkDiogClassId(keyDiogType[1]);
                            iog.setFkDiogTypeId(keyDiogType[2]);
                            iog.setFkDiogAdjustmentTypeId(SDataConstantsSys.TRNU_TP_IOG_ADJ_NA);
                            iog.setFkCompanyBranchId(idCompanyBranch);
                            iog.setFkWarehouseId(idWarehouse);
                            iog.setFkDpsYearId_n(SLibConstants.UNDEFINED);
                            iog.setFkDpsDocId_n(SLibConstants.UNDEFINED);
                            iog.setFkDiogYearId_n(SLibConstants.UNDEFINED);
                            iog.setFkDiogDocId_n(SLibConstants.UNDEFINED);
                            iog.setFkMfgYearId_n(mnPkYearId);
                            iog.setFkMfgOrderId_n(mnPkOrderId);
                            iog.setFkBookkeepingYearId_n(SLibConstants.UNDEFINED);
                            iog.setFkBookkeepingNumberId_n(SLibConstants.UNDEFINED);
                            iog.setFkMaintMovementTypeId(SModSysConsts.TRNS_TP_MAINT_MOV_NA);
                            iog.setFkMaintUserId_n(SLibConsts.UNDEFINED);
                            iog.setFkMaintUserSupervisorId(SModSysConsts.TRN_MAINT_USER_SUPV_NA);
                            iog.setFkMaintReturnUserId_n(SLibConsts.UNDEFINED);
                            iog.setFkMaintReturnUserSupervisorId(SModSysConsts.TRN_MAINT_USER_SUPV_NA);
                            iog.setFkUserShippedId(SUtilConsts.USR_NA_ID);
                            iog.setFkUserAuditedId(SUtilConsts.USR_NA_ID);
                            iog.setFkUserAuthorizedId(SUtilConsts.USR_NA_ID);
                            iog.setFkUserNewId(mnFkUserNewId);
                            iog.setFkUserEditId(SUtilConsts.USR_NA_ID);
                            iog.setFkUserDeleteId(SUtilConsts.USR_NA_ID);

                            diogNotes = new SDataDiogNotes();
                            diogNotes.setNotes("AVANCE PROCESO: " + mdAuxPercentage + "%");
                            diogNotes.setIsPrintable(true);
                            diogNotes.setFkUserNewId(mnFkUserNewId);
                            diogNotes.setFkUserEditId(1);
                            diogNotes.setFkUserDeleteId(1);

                            iog.getDbmsNotes().add(diogNotes);

                            //System.out.println("SDataRawMaterialsConsume 4 (iteration " + iteration + ") 2.1.5...");
                            mvDbmsDiogs.add(iog);
                        }

                        //System.out.println("SDataRawMaterialsConsume 4 (iteration " + iteration + ") 2.2...");
                        diogEntry = new SDataDiogEntry();
                        diogEntry.setPkYearId(year);
                        diogEntry.setPkDocId(0);
                        diogEntry.setPkEntryId(0);
                        diogEntry.setQuantity(resultSet.getDouble("f_net") * factor * percentage);
                        diogEntry.setValueUnitary(0);
                        diogEntry.setValue(0);
                        diogEntry.setOriginalQuantity(resultSet.getDouble("f_net") * factor * percentage);
                        diogEntry.setOriginalValueUnitary(0);
                        diogEntry.setSortingPosition(sortingPosition++);
                        diogEntry.setIsInventoriable(true);
                        diogEntry.setIsDeleted(false);
                        diogEntry.setFkItemId(resultSet.getInt("s.id_item"));
                        diogEntry.setFkUnitId(resultSet.getInt("s.id_unit"));
                        diogEntry.setFkOriginalUnitId(resultSet.getInt("s.id_unit"));
                        diogEntry.setFkDpsYearId_n(SLibConstants.UNDEFINED);
                        diogEntry.setFkDpsDocId_n(SLibConstants.UNDEFINED);
                        diogEntry.setFkDpsEntryId_n(SLibConstants.UNDEFINED);
                        diogEntry.setFkDpsAdjustmentYearId_n(SLibConstants.UNDEFINED);
                        diogEntry.setFkDpsAdjustmentDocId_n(SLibConstants.UNDEFINED);
                        diogEntry.setFkDpsAdjustmentEntryId_n(SLibConstants.UNDEFINED);
                        diogEntry.setFkMfgYearId_n(SLibConstants.UNDEFINED);
                        diogEntry.setFkMfgOrderId_n(SLibConstants.UNDEFINED);
                        diogEntry.setFkMfgChargeId_n(SLibConstants.UNDEFINED);
                        diogEntry.setFkMaintAreaId(SModSysConsts.TRN_MAINT_AREA_NA);
                        diogEntry.setFkUserNewId(mnFkUserNewId);
                        diogEntry.setFkUserEditId(1);
                        diogEntry.setFkUserDeleteId(1);

                        //System.out.println("SDataRawMaterialsConsume 4 (iteration " + iteration + ") 2.3...");
                        stockMove = new STrnStockMove(new int[] { year, resultSet.getInt("s.id_item"), resultSet.getInt("s.id_unit"), resultSet.getInt("s.id_lot"), idCompanyBranch, idWarehouse }, resultSet.getDouble("f_net") * factor * percentage);
                        diogEntry.getAuxStockMoves().add(stockMove);

                        //System.out.println("SDataRawMaterialsConsume 4 (iteration " + iteration + ") 2.4...");
                        iog.getDbmsEntries().add(diogEntry);
                    }
                }

                //System.out.println("SDataRawMaterialsConsume 5...");
                for (SDataDiog myDiog : mvDbmsDiogs) {
                    if (myDiog.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }
            }

            //System.out.println("SDataRawMaterialsConsume 6...");
            mbIsRegistryNew = false;
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
        }
        catch (SQLException e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public java.util.Date getLastDbUpdate() {
        return null;
    }
}
