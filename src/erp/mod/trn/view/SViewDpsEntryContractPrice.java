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
        String sql = "";
        Object filter = null;
        
        moPaneSettings = new SGridPaneSettings(3);

        if (mnGridMode != SModConsts.VIEW_ST_PEND) {
            filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
            sql += (sql.isEmpty() ? "AND " : "") + SGridUtils.getSqlFilterDate("d.dt", (SGuiDate) filter);
        }
        else {
            filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE).getValue();
            if (filter != null) {
                sql += (sql.isEmpty() ? "AND " : "") + "d.dt <= '" + SLibUtils.DbmsDateFormatDate.format((SGuiDate) filter) + "' ";
            }
        }
        
        
        msSql = "SELECT de.id_year AS " + SDbConsts.FIELD_ID + "1, "
                + "de.id_doc AS " + SDbConsts.FIELD_ID + "2, "
                + "de.id_ety AS " + SDbConsts.FIELD_ID + "3, "
                + "'' AS " + SDbConsts.FIELD_CODE + ", "
                + "'' AS " + SDbConsts.FIELD_NAME + ", "
                + "(SELECT cob.code FROM erp.bpsu_bpb AS cob WHERE d.fid_cob = cob.id_bpb) AS f_cob_code, "
                + "d.b_close, d.dt, d.num_ref, CONCAT(d.num_ser, IF(length(d.num_ser) = 0, '', '-'), d.num) AS f_num, dt.code, "
                + "COALESCE(SUM(dps_sup.orig_qty), 0) AS f_orig_qty_supply, de.orig_qty AS f_orig_qty_prc, "
                + "it.item, de.concept_key, u.symbol, bp.bp, bpc.bp_key "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " AS de ON de.id_year = d.id_year AND de.id_doc = d.id_doc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNU_TP_DPS) + " AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp ON bp.id_bp = d.fid_bp_r "
                + "INNER JOIN erp.bpsu_bp_ct AS bpc ON bp.id_bp = bpc.id_bp AND bpc.id_ct_bp = "
                + (isViewForCategoryPur() ? SDataConstantsSys.BPSS_CT_BP_SUP : SDataConstantsSys.BPSS_CT_BP_CUS) + " "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS it ON de.fid_item = it.id_item "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS u ON u.id_unit = de.fid_unit "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_DPS_SUPPLY) + " AS dps_sup ON dps_sup.id_src_year = de.id_year AND dps_sup.id_src_doc = de.id_doc AND dps_sup.id_src_ety = de.id_ety "
                + "WHERE EXISTS (SELECT * FROM trn_dps_ety_prc AS dep WHERE dep.id_year = de.id_year AND dep.id_doc = de.id_doc AND dep.id_ety = de.id_ety) AND "
                + (isViewForCategoryPur() ? "d.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_CON[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_CON[1] + " AND d.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_CON[2] : 
                "d.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_CON[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_CON[1] + " AND d.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_CON[2]) + " " + sql
                + "GROUP BY de.id_year, de.id_doc, de.id_ety "
                + (mnGridMode == SModConsts.VIEW_ST_PEND ? "HAVING f_orig_qty_prc <> f_orig_qty_supply AND d.b_close = 0 " : "HAVING f_orig_qty_prc = f_orig_qty_supply OR d.b_close = 1 ")
                + "ORDER BY de.id_year, de.id_doc, de.id_ety; ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        SGridColumnView column = null;
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<SGridColumnView>();
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "dt.code", "Tipo doc"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "f_num", "Folio doc"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "num_ref", "Referencia doc"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "d.dt", "Fecha doc"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CO, "f_cob_code", "Sucursal empresa"));
        if (isViewForCategoryPur()) {
            if (((SDataParamsErp) miClient.getSession().getConfigSystem()).getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_BPR, "bpc.bp_key", "Clave proveedor"));
                gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "bp.bp", "Proveedor"));
            }
            else {
                gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "bp.bp", "Proveedor"));
                gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_BPR, "bpc.bp_key", "Clave proveedor"));
            }
        }
        else {
            if (((SDataParamsErp) miClient.getSession().getConfigSystem()).getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_BPR, "bpc.bp_key", "Clave cliente"));
                gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "bp.bp", "Cliente"));
            }
            else {
                gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "bp.bp", "Cliente"));
                gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_BPR, "bpc.bp_key", "Clave cliente"));
            }
        }
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "de.concept_key", "Clave ítem"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "it.item", "Ítem"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "f_orig_qty_prc", "Cantidad"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "f_orig_qty_supply", "Cant procesada"));
        column = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "", "Cant pendiente");
        column.getRpnArguments().add(new SLibRpnArgument("f_orig_qty_prc", SLibRpnArgumentType.OPERAND));
        column.getRpnArguments().add(new SLibRpnArgument("f_orig_qty_supply", SLibRpnArgumentType.OPERAND));
        column.getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.SUBTRACTION, SLibRpnArgumentType.OPERATOR));
        gridColumnsViews.add(column);
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "u.symbol", "Unidad"));

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
