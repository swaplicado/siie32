/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.gui.SModuleUtilities;
import erp.lib.SLibConstants;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.fin.db.SDbPayment;
import erp.mtrn.data.STrnFunctionalAreaUtils;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDatePeriod;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridRowView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;

/**
 *
 * @author Edwin Carmona
 */
public class SViewDpsAdvance extends SGridPaneView implements ActionListener, ItemListener {

    private SGridFilterDatePeriod moFilterDatePeriod;
    private JLabel jlFuncArea;
    private JTextField jtfFuncArea;
    
    private javax.swing.JButton mjbViewDps;
    private javax.swing.JButton mjbViewNotes;
    private javax.swing.JButton mjbViewLinks;
    
    public SViewDpsAdvance(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.TRNX_DPS_PUR_ADVANCE, SLibConstants.UNDEFINED, title);
        initComponentsCustom();
    }

    private void initComponentsCustom() {
        setRowButtonsEnabled(false);
        
        mjbViewDps = new JButton(miClient.getImageIcon(SLibConstants.ICON_LOOK));
        mjbViewNotes = new JButton(miClient.getImageIcon(SLibConstants.ICON_NOTES));
        mjbViewLinks = new JButton(miClient.getImageIcon(SLibConstants.ICON_LINK));
        
        mjbViewDps.setToolTipText("Ver documento");
        mjbViewNotes.setToolTipText("Ver notas del documento");
        mjbViewLinks.setToolTipText("Ver vínculos del documento");
        
        mjbViewDps.setPreferredSize(new java.awt.Dimension(23, 23));
        mjbViewNotes.setPreferredSize(new java.awt.Dimension(23, 23));
        mjbViewLinks.setPreferredSize(new java.awt.Dimension(23, 23));
        
        mjbViewDps.addActionListener(this);
        mjbViewNotes.addActionListener(this);
        mjbViewLinks.addActionListener(this);
        
        String areas[] = STrnFunctionalAreaUtils.getTextFilterOfFunctionalAreas((SClientInterface) miClient, SLibConsts.UNDEFINED);
        
        jlFuncArea = new JLabel("  Áreas funcionales:");
        
        jtfFuncArea = new JTextField(areas[1]);
        jtfFuncArea.setPreferredSize(new java.awt.Dimension(150, 23));
        jtfFuncArea.setEnabled(false);
        
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(new JPopupMenu.Separator());
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbViewDps);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbViewNotes);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbViewLinks);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(new JPopupMenu.Separator());
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jlFuncArea);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jtfFuncArea);
    }
    
    private void actionViewDps() {
        SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
        int[] type = SDataConstantsSys.TRNU_TP_DPS_PUR_INV;

        if (gridRow != null) {
            ((SClientInterface) miClient).getGuiModule(SDataConstants.MOD_PUR).setFormComplement(type);  // document type key
            ((SClientInterface) miClient).getGuiModule(SDataConstants.MOD_PUR).showForm(SDataConstants.TRN_DPS, gridRow.getRowPrimaryKey());
        }
    }

    private void actionViewNotes() {
        if (mjbViewNotes.isEnabled()) {
            SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
            SModuleUtilities.showDocumentNotes((SClientInterface) miClient, SDataConstants.TRN_DPS, gridRow.getRowPrimaryKey());
        }
    }

    private void actionViewLinks() {
        if (mjbViewLinks.isEnabled()) {
            SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
            SModuleUtilities.showDocumentLinks((SClientInterface) miClient, gridRow.getRowPrimaryKey());
        }
    }
    
    @Override
    public void prepareSqlQuery() {
        String where = "";
        Object filter;
        
        moPaneSettings = new SGridPaneSettings(2);
        moPaneSettings.setDisabledApplying(false);
        moPaneSettings.setDeletedApplying(false);
        moPaneSettings.setUserInsertApplying(false);
        moPaneSettings.setUserUpdateApplying(false);
        
        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
        if (filter != null) {
            where += "AND " + SGridUtils.getSqlFilterDate("d.dt", (SGuiDate) filter);
        }
        
        msSql = "SELECT "
                + "d.id_year AS " + SDbConsts.FIELD_ID + "1, "
                + "d.id_doc AS " + SDbConsts.FIELD_ID + "2, "
                + "dt.code AS " + SDbConsts.FIELD_CODE + ", "
                + "'' AS " + SDbConsts.FIELD_NAME + ", "
                + "IF(d.num_ser <> '', CONCAT(d.num_ser, '-', d.num), d.num) AS num, "
                + "d.num_ref, "
                + "d.dt, "
                + "cob.code AS f_cob_code, "
                + "de.concept, "
                + "f.code AS func, "
                + "bp.bp, "
                + "bpc.bp_key, "
                + "bpb.bpb, "
                + "d.tot_cur_r, "
                + "'" + miClient.getSession().getSessionCustom().getLocalCurrencyCode() + "' AS _cur, "
                + "d.acc_tag, "

                // === Campos de pagos (anticipos) ===
                + "d.tot_cur_r - COALESCE(p.payed_cur, 0) AS bal_cur, "
                + "COALESCE(p.payed_cur, 0) AS payed_cur, "
                + "COALESCE(p.payed_pend_cur, 0) AS payed_pend_cur, "
                + "(d.tot_cur_r - COALESCE(p.payed_cur, 0) - COALESCE(p.payed_pend_cur, 0)) AS f_bal_net_cur "

                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNU_TP_DPS) + " AS dt ON "
                + "d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_FUNC) + " AS f ON d.fid_func = f.id_func "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp ON d.fid_bp_r = bp.id_bp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP_CT) + " AS bpc ON "
                + "bp.id_bp = bpc.id_bp AND bpc.id_ct_bp = " + (mnGridSubtype == SModConsts.MOD_TRN_SAL_N ? 
                                                        SDataConstantsSys.BPSS_CT_BP_CUS : 
                                                        SDataConstantsSys.BPSS_CT_BP_SUP) + " "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS bpb ON d.fid_bpb = bpb.id_bpb "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS cob ON d.fid_cob = cob.id_bpb "

                // JOIN para concepto y partida tipo anticipo
                + "INNER JOIN (" +
                    "    SELECT  " +
                    "        id_year,  " +
                    "        id_doc,  " +
                    "        concept " +
                    "    FROM trn_dps_ety " +
                    "    WHERE NOT b_del " +
                    "    AND ops_type = 13 " +
                    "    GROUP BY id_year, id_doc " +
                    "    ORDER BY id_ety ASC " +
                    ") AS de ON de.id_year = d.id_year AND de.id_doc = d.id_doc "

                // === LEFT JOIN DE PAGOS (ANTICIPOS) ===
                + "LEFT JOIN ("
                    + "SELECT "
                        + "pre.fk_doc_year_n, "
                        + "pre.fk_doc_doc_n, "
                        + "SUM(CASE WHEN pr.fk_st_pay IN (" + 
                                                SModSysConsts.FINS_ST_PAY_SUBR + ", " + 
                                                SModSysConsts.FINS_ST_PAY_SUBR_P + 
                                            ") "
                            + "THEN ety_pay ELSE 0 END) AS payed, "
                        + "SUM(CASE WHEN pr.fk_st_pay IN (" + 
                                                SModSysConsts.FINS_ST_PAY_SUBR + ", " + 
                                                SModSysConsts.FINS_ST_PAY_SUBR_P + 
                                            ") "
                            + "THEN des_pay_app_ety_cur ELSE 0 END) AS payed_cur, "
                        + "SUM(CASE WHEN pr.fk_st_pay IN (" + 
                                                SModSysConsts.FINS_ST_PAY_NEW + ", " + 
                                                SModSysConsts.FINS_ST_PAY_IN_AUTH + ", " + 
                                                SModSysConsts.FINS_ST_PAY_SCHED + ", " + 
                                                SModSysConsts.FINS_ST_PAY_BLOC + ", " + 
                                                SModSysConsts.FINS_ST_PAY_SCHED_P + ", " + 
                                                SModSysConsts.FINS_ST_PAY_BLOC_P + 
                                            ") THEN ety_pay ELSE 0 END) AS payed_pend, "
                        + "SUM(CASE WHEN pr.fk_st_pay IN (" + 
                                                SModSysConsts.FINS_ST_PAY_NEW + ", " + 
                                                SModSysConsts.FINS_ST_PAY_IN_AUTH + ", " + 
                                                SModSysConsts.FINS_ST_PAY_SCHED + ", " + 
                                                SModSysConsts.FINS_ST_PAY_BLOC + ", " + 
                                                SModSysConsts.FINS_ST_PAY_SCHED_P + ", " + 
                                                SModSysConsts.FINS_ST_PAY_BLOC_P + 
                                            ") THEN des_pay_app_ety_cur ELSE 0 END) AS payed_pend_cur "
                    + "FROM fin_pay AS pr "
                    + "INNER JOIN fin_pay_ety AS pre ON pr.id_pay = pre.id_pay "
                    + "WHERE pr.b_del = 0 "
                    + "AND pr.pay_tp = '" + SDbPayment.TYPE_REQUEST + "' "
                    + "AND pr.pay_tp_op = " + SDataConstantsSys.TRNX_OPS_TYPE_OPS_PREPAY_DOC + " "
                    + "GROUP BY pre.fk_doc_year_n, pre.fk_doc_doc_n"
                + ") AS p ON d.id_year = p.fk_doc_year_n AND d.id_doc = p.fk_doc_doc_n "
                + where
                + "AND d.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_INV[0] 
                    + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_INV[1] 
                    + " AND d.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_INV[2] + " "
                + "AND NOT d.b_del ";

        // ORDER BY
        msSql += "ORDER BY d.acc_tag, d.num, bp.bp";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        int col = 0;
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();
        SGridColumnView[] columns = new SGridColumnView[16];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt", "Fecha documento");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "num", "Folio documento");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "bp", "Proveedor");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "bpc.bp_key", "Clave proveedor", 50);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "bpb.bpb", "Sucursal proveedor", 75);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, SDbConsts.FIELD_CODE, "Tipo documento");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "num_ref", "Referencia documento");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "f_cob_code", "Sucursal empresa", 35);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "tot_cur_r", "Total $");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "payed_cur", "Pagado $");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "bal_cur", "Saldo $");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "payed_pend_cur", "Pendiente $");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "f_bal_net_cur", "Saldo $");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "_cur", "Moneda");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "func", "Área funcional", 35);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "de.concept", "Concepto", 200);
        
        gridColumnsViews.addAll(Arrays.asList((SGridColumnView[]) columns));

        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.TRN_DPS);
        moSuscriptionsSet.add(SModConsts.TRN_DPS_ETY);
        moSuscriptionsSet.add(SModConsts.BPSU_BP);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        refreshGridWithReload();
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        
        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();

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
