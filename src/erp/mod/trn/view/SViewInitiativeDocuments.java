/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.gui.SModuleUtilities;
import erp.lib.SLibConstants;
import erp.mod.SModConsts;
import erp.mod.trn.db.SDbInitiative;
import erp.mod.view.SViewFilter;
import erp.mtrn.data.SDataDps;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibRpnArgument;
import sa.lib.SLibRpnArgumentType;
import sa.lib.SLibRpnOperator;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDatePeriod;
import sa.lib.grid.SGridFilterValue;
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
 * @author Isabel Servín, Sergio Flores
 */
public class SViewInitiativeDocuments extends SGridPaneView implements ActionListener {
    
    private SGridFilterDatePeriod moFilterDatePeriod;
    private SViewFilter moFilterFuncArea;
    private SViewFilter moFilterUser;

    private javax.swing.JButton mjbViewDps;
    private javax.swing.JButton mjbViewNotes;
    private javax.swing.JButton mjbViewLinks;
    
    /**
     * Creates view SViewInitiativeDocuments.
     * @param client GUI Client.
     * @param title Title.
     * @param subtype View subtype: SUtilConsts.PROC = Processed; SUtilConsts.PROC_PEND = Processing Pending.
     * @param params View mode: SUtilConsts.QRY_SUM = Summary; SUtilConsts.QRY_DET = Detail.
     */
    public SViewInitiativeDocuments(SGuiClient client, String title, int subtype, SGuiParams params) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.TRN_INIT_DPS, subtype, title, params);
        mnGridMode = params.getType();
        initComponentsCustom(); // SUtilConsts.QRY_PROC | SUtilConsts.QRY_PROC_PEND
    }
    
    private void initComponentsCustom() {
        int levelRightInitiatives = ((SClientInterface) miClient).getSessionXXX().getUser().hasRight(((SClientInterface) miClient), SDataConstantsSys.PRV_PUR_INIT).Level;
        
        setRowButtonsEnabled(false);
        jtbFilterDeleted.setEnabled(false);
        
        if (mnGridSubtype == SUtilConsts.PROC) {
            moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
            moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_YEAR, miClient.getSession().getCurrentDate().getTime()));
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        }
        
        moFilterFuncArea = new SViewFilter(miClient, this, SModConsts.CFGU_FUNC);
        moFilterFuncArea.initFilter(null);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterFuncArea);
        
        moFilterUser = new SViewFilter(miClient, this, SModConsts.USRU_USR);
        moFilterUser.initFilter(levelRightInitiatives <= SUtilConsts.LEV_AUTHOR ? SViewFilter.SUBTYPE_USER_SESSION : SViewFilter.SUBTYPE_USERS_ALL_INIT);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterUser);
        
        if (mnGridMode == SUtilConsts.QRY_DET) {
            mjbViewDps = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_LOOK), "Ver documento", this);
            mjbViewNotes = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_NOTES), "Ver notas del documento", this);
            mjbViewLinks = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_LINK), "Ver vínculos del documento", this);

            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbViewDps);
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbViewNotes);
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbViewLinks);
        }
    }

    private void actionViewDps() {
        if (mjbViewDps.isEnabled()) {
            if (getSelectedGridRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                int gui = SDataConstants.MOD_PUR;
                SDataDps dps = (SDataDps) SDataUtilities.readRegistry(((SClientInterface) miClient), SDataConstants.TRN_DPS, ((SGridRowView) getSelectedGridRow()).getRowRegistryTypeKey(), SLibConstants.EXEC_MODE_VERBOSE);

                if (dps != null) {
                    ((SClientInterface) miClient).getGuiModule(gui).setFormComplement(dps.getDpsTypeKey());
                    ((SClientInterface) miClient).getGuiModule(gui).showForm(SDataConstants.TRNX_DPS_RO, dps.getPrimaryKey());
                }
            }
        }
    }

    private void actionViewNotes() {
        if (mjbViewNotes.isEnabled()) {
            if (getSelectedGridRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                SModuleUtilities.showDocumentNotes((SClientInterface) miClient, SDataConstants.TRN_DPS, ((SGridRowView) getSelectedGridRow()).getRowRegistryTypeKey());
            }
        }
    }

    private void actionViewLinks() {
        if (mjbViewLinks.isEnabled()) {
            if (getSelectedGridRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                SModuleUtilities.showDocumentLinks((SClientInterface) miClient, ((SGridRowView) getSelectedGridRow()).getRowRegistryTypeKey());
            }
        }
    }
    
    @Override
    public void prepareSqlQuery() {
        String where = "";
        String join = mnGridMode == SUtilConsts.QRY_SUM ? "LEFT OUTER" : "INNER";
        Object filter;

        moPaneSettings = new SGridPaneSettings(1);
        
        if (mnGridMode == SUtilConsts.QRY_DET) {
            moPaneSettings.setTypeKeyLength(2);
        }

        if (mnGridSubtype == SUtilConsts.PROC) {
            filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
            where += (where.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("v.dt_sta_n", (SGuiDate) filter);
        }
        
        filter = ((SGridFilterValue) moFiltersMap.get(SModConsts.CFGU_FUNC)).getValue();
        if (filter != null && !((String) filter).isEmpty()) {
            where += (where.isEmpty() ? "" : "AND ") + "v.fk_func IN (" + filter + ") ";
        }
        
        filter = ((SGridFilterValue) moFiltersMap.get(SModConsts.USRU_USR)).getValue();
        if (filter != null && !((String) filter).isEmpty()) {
            where += (where.isEmpty() ? "" : "AND ") + "v.fk_usr_ins IN (" + filter + ") ";
        }

        msSql = "SELECT "
                + "v.id_init AS " + SDbConsts.FIELD_ID + "1, "
                + "v.code AS " + SDbConsts.FIELD_CODE + ", "
                + "v.name AS " + SDbConsts.FIELD_NAME + ", "
                + "CASE WHEN v.type = '" + SDbInitiative.TYPE_E + "' THEN '" + SDbInitiative.TYPE_PER_EVENT + "' WHEN v.type = '" + SDbInitiative.TYPE_R + "' THEN '" + SDbInitiative.TYPE_RECURRENT + "' ELSE '?' END AS _type, "
                + "v.budget, "
                + "v.dt_sta_n, "
                + "v.dt_end_n, "
                + "tp.name AS _periodicity_type, "
                + "f.name AS _func"
                + (mnGridMode == SUtilConsts.QRY_SUM ?
                    ", SUM(if(d.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_PUR_ORD[1] + ", d.stot_r, 0.0)) AS _comp_budget, SUM(if(d.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_PUR_DOC[1] + ", d.stot_r, 0.0)) AS _spent_budget" :
                    ", td.code, CONCAT(d.num_ser, IF(d.num_ser = '', '', '-'), d.num) AS _folio, CONCAT(d.num_ser, IF(d.num_ser = '', '', '-'), d.num) AS " + SDbConsts.FIELD_TYPE + ", d.dt, d.id_year AS " + SDbConsts.FIELD_TYPE_ID + "1, d.id_doc AS " + SDbConsts.FIELD_TYPE_ID + "2, d.stot_r, d.exc_rate, d.stot_cur_r, c.cur_key, b.bp, bc.bp_key") + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_INIT) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNS_TP_PERIOD) + " AS tp ON "
                + "v.fk_tp_period = tp.id_tp_period "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_FUNC) + " AS f ON "
                + "v.fk_func = f.id_func "
                + join + " JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_INIT_DPS) + " AS id ON id.id_init = v.id_init "
                + join + " JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d ON d.id_year = id.id_dps_year AND d.id_doc = id.id_dps_doc AND NOT d.b_del AND d.fid_st_dps <> " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + " AND d.fid_ct_dps = " + SDataConstantsSys.TRNS_CT_DPS_PUR + " "
                + join + " JOIN " + SModConsts.TablesMap.get(SModConsts.TRNU_TP_DPS) + " AS td ON td.id_ct_dps = d.fid_ct_dps AND td.id_cl_dps = d.fid_cl_dps AND td.id_tp_dps = d.fid_tp_dps "
                + join + " JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS c ON c.id_cur = d.fid_cur "
                + join + " JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b ON b.id_bp = d.fid_bp_r "
                + join + " JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP_CT) + " AS bc ON bc.id_bp = b.id_bp AND bc.id_ct_bp = " + SDataConstantsSys.BPSS_CT_BP_SUP + " "
                + "WHERE " + where + (where.isEmpty() ? "" : "AND ")
                + "NOT v.b_del "
                + "AND v.id_init IN ("
                    + "SELECT t.id_init "
                    + "FROM ("
                        + "SELECT xi.id_init, xi.budget, COALESCE(SUM(xd.stot_r), 0.0) AS _comp_budget "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_INIT) + " AS xi "
                        + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_INIT_DPS) + " AS xid ON xid.id_init = xi.id_init "
                        + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS xd ON xd.id_year = xid.id_dps_year AND xd.id_doc = xid.id_dps_doc AND "
                        + "NOT xd.b_del AND xd.fid_st_dps <> " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + " AND xd.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[0] + " AND xd.fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[1] + " "
                        + "WHERE NOT xi.b_del "
                        + "GROUP BY xi.id_init, xi.budget "
                        + "HAVING COALESCE(SUM(xd.stot_r), 0.0) " + (mnGridSubtype == SUtilConsts.PROC ? ">=" : "<") + " xi.budget "
                        + "ORDER BY xi.id_init, xi.budget "
                    + ") AS t "
                    + "ORDER BY t.id_init"
                + ") "
                + (mnGridMode == SUtilConsts.QRY_SUM ?
                    "GROUP BY v.code, v.name, v.id_init, v.type, v.budget, v.dt_sta_n, v.dt_end_n, tp.name, f.name " :
                    "")
                + "ORDER BY v.code, v.name, v.id_init, v.type, v.budget, v.dt_sta_n, v.dt_end_n, tp.name, f.name"
                + (mnGridMode == SUtilConsts.QRY_SUM ?
                    "" :
                    ",td.code, d.num_ser, d.num, d.dt, d.id_year, d.id_doc, b.bp, b.id_bp")
                + ";";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, SDbConsts.FIELD_CODE, SGridConsts.COL_TITLE_CODE, 125));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_L, SDbConsts.FIELD_NAME, SGridConsts.COL_TITLE_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "_type", "Tipo iniciativa"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "_periodicity_type", "Periodicidad"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "_func", "Área funcional"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt_sta_n", "Fecha inicial"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt_end_n", "Fecha final"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "budget", "Presup. estimado $"));
        
        if (mnGridMode == SUtilConsts.QRY_SUM) {
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_comp_budget", "Presup. comprometido $"));
            SGridColumnView columnView = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "", "Presup. por ejercer $");
            columnView.getRpnArguments().add(new SLibRpnArgument("budget", SLibRpnArgumentType.OPERAND));
            columnView.getRpnArguments().add(new SLibRpnArgument("_comp_budget", SLibRpnArgumentType.OPERAND));
            columnView.getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.SUBTRACTION, SLibRpnArgumentType.OPERATOR));
            gridColumnsViews.add(columnView);
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_spent_budget", "Presup. ejercido $"));
        }
        
        if (mnGridMode == SUtilConsts.QRY_DET) {
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "td.code", SGridConsts.COL_TITLE_TYPE + " doc."));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "_folio", "Folio doc."));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "d.dt", "Fecha doc."));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "d.stot_r", "Subtotal doc. $"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_EXC_RATE, "d.exc_rate", "Tipo cambio doc."));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "d.stot_cur_r", "Subtotal doc. mon $"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "c.cur_key", "Moneda doc."));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "b.bp", "Proveedor doc."));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_BPR, "bc.bp_key", "Clave proveedor doc."));
        }

        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.TRNS_TP_PERIOD);
        moSuscriptionsSet.add(SModConsts.CFGU_FUNC);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
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
            else if (button == mjbViewLinks) {
                actionViewLinks();
            }
        }
    }
}
