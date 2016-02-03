/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.gui.SModuleUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mcfg.data.SDataParamsErp;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.trn.form.SDialogContractPriceCardex;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import sa.lib.SLibRpnArgument;
import sa.lib.SLibRpnArgumentType;
import sa.lib.SLibRpnOperator;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDateCutOff;
import sa.lib.grid.SGridFilterDatePeriod;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridRowView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;
import sa.lib.gui.SGuiParams;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SViewDpsEntryContractPrice extends SGridPaneView implements ActionListener {

    private JButton mjbViewDps;
    private JButton mjbViewNotes;
    private JButton jbCardex;
    private SGridFilterDatePeriod moFilterDatePeriod;
    private SGridFilterDateCutOff moFilterDateCutOff;
    private SDialogContractPriceCardex moDialogContractPriceCardex;
    
    public SViewDpsEntryContractPrice(SGuiClient client, int gridSubtype, String title, SGuiParams params) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.TRN_DPS_ETY_PRC, gridSubtype, title, params);
        initComponents();
        setRowButtonsEnabled(false);
        jtbFilterDeleted.setEnabled(false);
    }
    
    private void initComponents() {
        if (mnGridMode != SModConsts.VIEW_ST_PEND) {
            moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
            moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        }
        else {
            moFilterDateCutOff = new SGridFilterDateCutOff(miClient, this);
            moFilterDateCutOff.initFilter(null);
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDateCutOff);
        }
        
        mjbViewDps = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_LOOK), "Ver documento", this);
        mjbViewNotes = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_NOTES), "Ver notas", this);
        jbCardex = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_kardex.gif")), "Ver entregas mensuales", this);
        
        moDialogContractPriceCardex = new SDialogContractPriceCardex(miClient, "Entregas mensuales partida del contrato");
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbViewDps);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbViewNotes);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbCardex);
    }
    
    private boolean isViewForCategoryPur() {
        return mnGridSubtype == SModConsts.MOD_TRN_PUR_N;
    }

    private void actionViewDps() {
        int gui = isViewForCategoryPur() ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL;

        if (jtTable.getSelectedRowCount() != 1) {
            miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
        }
        else {
            SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

            if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
            }
            else {
                ((SClientInterface) miClient).getGuiModule(gui).setFormComplement(SDataConstantsSys.TRNU_TP_DPS_SAL_INV);
                ((SClientInterface) miClient).getGuiModule(gui).showForm(SDataConstants.TRNX_DPS_RO, gridRow.getRowPrimaryKey());
            }
        }
    }

    private void actionViewNotes() {
        if (jtTable.getSelectedRowCount() != 1) {
            miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
        }
        else {
            SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

            if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
            }
            else {
                SModuleUtilities.showDocumentNotes((SClientInterface) miClient, SDataConstants.TRN_DPS, gridRow.getRowPrimaryKey());
            }
        }
    }

    private void actionCardex() {
        int[] key = null;
        
        if (jbCardex.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                try {
                    SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                    if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                        miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                    }
                    else {
                        key = (int[]) gridRow.getRowPrimaryKey();
                    
                        moDialogContractPriceCardex.setFormParams(key, isViewForCategoryPur() ? "Proveedor:" : "Cliente:");
                        moDialogContractPriceCardex.setVisible(true);
                    }
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
    }

    @Override
    public void prepareSqlQuery() {
        int idCategory = isViewForCategoryPur() ? SModSysConsts.BPSS_CT_BP_SUP : SModSysConsts.BPSS_CT_BP_CUS;
        int[] keyDpsType = isViewForCategoryPur() ? SModSysConsts.TRNU_TP_DPS_PUR_CON : SModSysConsts.TRNU_TP_DPS_SAL_CON;
        String sql = "";
        Object filter = null;
        
        moPaneSettings = new SGridPaneSettings(3);

        if (mnGridMode != SModConsts.VIEW_ST_PEND) {
            filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
            sql += (sql.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("d.dt", (SGuiDate) filter) + " ";
        }
        else {
            filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE).getValue();
            if (filter != null) {
                sql += (sql.isEmpty() ? "" : "AND ") + "d.dt <= '" + SLibUtils.DbmsDateFormatDate.format((SGuiDate) filter) + "' ";
            }
        }
        
        msSql = "SELECT "
                + "de.id_year AS " + SDbConsts.FIELD_ID + "1, "
                + "de.id_doc AS " + SDbConsts.FIELD_ID + "2, "
                + "de.id_ety AS " + SDbConsts.FIELD_ID + "3, "
                + "'' AS " + SDbConsts.FIELD_CODE + ", "
                + "'' AS " + SDbConsts.FIELD_NAME + ", "
                + "CONCAT(d.num_ser, IF(length(d.num_ser) = 0, '', '-'), d.num) AS f_num, "
                + "d.dt, d.num_ref, d.b_link, d.fid_cob, d.fid_bpb, d.fid_bp_r, "
                + "dt.code, dt.tp_dps, cob.code, cob.bpb, b.bp, bc.bp_key, bb.bpb, "
                + "i.item, de.concept_key, u.symbol, uo.symbol, "
                + "de.orig_qty AS f_orig_qty, "
                + "COALESCE(SUM(ds.orig_qty), 0) AS f_orig_qty_supply "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " AS de ON "
                + "d.id_year = de.id_year AND d.id_doc = de.id_doc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNU_TP_DPS) + " AS dt ON "
                + "d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS cob ON "
                + "d.fid_cob = cob.id_bpb "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b ON "
                + "d.fid_bp_r = b.id_bp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP_CT) + " AS bc ON "
                + "d.fid_bp_r = bc.id_bp AND bc.id_ct_bp = " + idCategory + " "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS bb ON "
                + "d.fid_bpb = bb.id_bpb "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON "
                + "de.fid_item = i.id_item "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS u ON "
                + "de.fid_unit = u.id_unit "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS uo ON "
                + "de.fid_orig_unit = uo.id_unit "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_DPS_SUPPLY) + " AS ds ON "
                + "de.id_year = ds.id_src_year AND de.id_doc = ds.id_src_doc AND de.id_ety = ds.id_src_ety "
                + "WHERE EXISTS (SELECT * FROM trn_dps_ety_prc AS dep WHERE dep.id_year = de.id_year AND dep.id_doc = de.id_doc AND dep.id_ety = de.id_ety) AND "
                + "d.fid_ct_dps = " + keyDpsType[0] + " AND d.fid_cl_dps = " + keyDpsType[1] + " AND d.fid_tp_dps = " + keyDpsType[2] + " " + (sql.isEmpty() ? "" : "AND " + sql)
                + "GROUP BY de.id_year, de.id_doc, de.id_ety, d.num_ser, d.num, "
                + "d.dt, d.num_ref, d.b_link, d.fid_cob, d.fid_bpb, d.fid_bp_r, "
                + "dt.code, dt.tp_dps, cob.code, cob.bpb, b.bp, bc.bp_key, bb.bpb, "
                + "i.item, de.concept_key, u.symbol, uo.symbol, "
                + "de.orig_qty "
                + (mnGridMode == SModConsts.VIEW_ST_PEND ? "HAVING f_orig_qty_supply < f_orig_qty AND d.b_link = 0 " : "HAVING f_orig_qty_supply >= f_orig_qty OR d.b_link = 1 ") + " ";
        
            msSql += "ORDER BY dt.code, d.num_ser, CAST(d.num AS UNSIGNED INTEGER), d.num, d.dt, de.id_year, de.id_doc, ";

            if (isViewForCategoryPur()) {
                msSql += ((SDataParamsErp) miClient.getSession().getConfigSystem()).getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ?
                    "bc.bp_key, b.bp, " : "b.bp, bc.bp_key, ";
            }
            else {
                msSql += ((SDataParamsErp) miClient.getSession().getConfigSystem()).getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ?
                    "bc.bp_key, b.bp, " : "b.bp, bc.bp_key, ";
            }

            msSql += "d.fid_bp_r, bb.bpb, d.fid_bpb, ";

            if (((SDataParamsErp) miClient.getSession().getConfigSystem()).getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                msSql += "i.item_key, i.item, ";
            }
            else {
                msSql += "i.item, i.item_key, ";
            }

            msSql += "de.fid_item, uo.symbol, de.fid_orig_unit ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        SGridColumnView column = null;
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<SGridColumnView>();
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "dt.code", "Tipo doc"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "f_num", "Folio doc"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "num_ref", "Referencia doc"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "d.dt", "Fecha doc"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CO, "cob.code", "Sucursal empresa"));
        
        if (isViewForCategoryPur()) {
            if (((SDataParamsErp) miClient.getSession().getConfigSystem()).getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_BPR, "bc.bp_key", "Clave proveedor"));
                gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "b.bp", "Proveedor"));
            }
            else {
                gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "b.bp", "Proveedor"));
                gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_BPR, "bc.bp_key", "Clave proveedor"));
            }
        }
        else {
            if (((SDataParamsErp) miClient.getSession().getConfigSystem()).getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_BPR, "bc.bp_key", "Clave cliente"));
                gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "b.bp", "Cliente"));
            }
            else {
                gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "b.bp", "Cliente"));
                gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_BPR, "bc.bp_key", "Clave cliente"));
            }
        }
        
        if (((SDataParamsErp) miClient.getSession().getConfigSystem()).getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "de.concept_key", "Clave ítem"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "i.item", "Ítem"));
        }
        else {
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "i.item", "Ítem"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "de.concept_key", "Clave ítem"));
        }
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "f_orig_qty", "Cantidad"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "f_orig_qty_supply", "Cant procesada"));
        column = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_2D, "", "Avance %");
        column.getRpnArguments().add(new SLibRpnArgument("f_orig_qty_supply", SLibRpnArgumentType.OPERAND));
        column.getRpnArguments().add(new SLibRpnArgument("f_orig_qty", SLibRpnArgumentType.OPERAND));
        column.getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.DIVISION, SLibRpnArgumentType.OPERATOR));
        gridColumnsViews.add(column);
        column = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "", "Cant pendiente");
        column.getRpnArguments().add(new SLibRpnArgument("f_orig_qty", SLibRpnArgumentType.OPERAND));
        column.getRpnArguments().add(new SLibRpnArgument("f_orig_qty_supply", SLibRpnArgumentType.OPERAND));
        column.getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.SUBTRACTION, SLibRpnArgumentType.OPERATOR));
        gridColumnsViews.add(column);
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "u.symbol", "Unidad"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "d.b_link", "Cerrado"));

        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SDataConstants.TRN_DPS);
        moSuscriptionsSet.add(SDataConstants.TRNU_TP_DPS);
        moSuscriptionsSet.add(SDataConstants.CFGU_CUR);
        moSuscriptionsSet.add(SDataConstants.BPSU_BP);
        moSuscriptionsSet.add(SDataConstants.BPSU_BP_CT);
        moSuscriptionsSet.add(SDataConstants.BPSU_BPB);
        moSuscriptionsSet.add(SDataConstants.USRU_USR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == mjbViewDps) {
                actionViewDps();
            }
            else if (button == mjbViewNotes) {
                actionViewNotes();
            }
            else if (button == jbCardex) {
                actionCardex();
            }
        }
    }
}
