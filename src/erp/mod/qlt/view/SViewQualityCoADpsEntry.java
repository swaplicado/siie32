/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.qlt.view;


import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.mod.SModConsts;
import erp.mod.qlt.form.SDialogRepQltCoA;
import erp.mod.qlt.db.SDbCoAResult;
import erp.mod.qlt.utils.SQltUtils;
import erp.mod.qlt.form.SFormCoACapture;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
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
public class SViewQualityCoADpsEntry extends SGridPaneView implements ActionListener {

    private SGridFilterDatePeriod moFilterDatePeriod;
    SFormCoACapture miForm;
    
    private JButton jbCaptureCoA;
    private JButton jbPrintCoA;
    private JButton jbOpenCloseCoA;
    
    private boolean mbCanCaptureCoA;

    public SViewQualityCoADpsEntry(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.QLTX_QLT_DPS_ETY, SLibConsts.UNDEFINED, title);
        initComponents();
    }

    private void initComponents() {
        setRowButtonsEnabled(false, false, false, false, false);

        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
        jbCaptureCoA = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_edit.gif")), "Captura de resultados", this);
        jbPrintCoA = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_print.gif")), "Imprimir CoA", this);
        jbOpenCloseCoA = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_ok.gif")), "Abrir/Validar CoA", this);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbCaptureCoA);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPrintCoA);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbOpenCloseCoA);
        
        boolean canValidateCoA = ((SClientInterface) miClient).getSessionXXX().getUser().hasRight((SClientInterface) miClient, SDataConstantsSys.PRV_QLT_COA).Level == SUtilConsts.LEV_MANAGER;
        jbOpenCloseCoA.setEnabled(canValidateCoA);
        
        mbCanCaptureCoA = ((SClientInterface) miClient).getSessionXXX().getUser().hasRight((SClientInterface) miClient, SDataConstantsSys.PRV_QLT_COA).Level >= SUtilConsts.LEV_CAPTURE;
        jbCaptureCoA.setEnabled(mbCanCaptureCoA);
        
        miForm = new SFormCoACapture(miClient, "Captura de resultados para CoA");
    }
    
    private void actionCaptureCoA() {
        if (jtTable.getSelectedRowCount() != 1) {
            miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
        }
        else {
            SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

            if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
            }
            else if (gridRow.isRowSystem()) {
                miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_IS_SYSTEM);
            }
            else if (!gridRow.isUpdatable()) {
                miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_NON_UPDATABLE);
            }
            else if (moPaneSettings.isUserInsertApplying() && mnUserLevelAccess == SUtilConsts.LEV_AUTHOR && gridRow.getFkUserInsertId() != miClient.getSession().getUser().getPkUserId()) {
                miClient.showMsgBoxWarning(SDbConsts.MSG_REG_DENIED_RIGHT);
            }
            else {
                try {
                    boolean canCreate = true;
                    int[] pk = gridRow.getRowPrimaryKey();
                    SDbCoAResult oCoAResult = SQltUtils.getCoAResults(miClient.getSession(), pk[0], pk[1], pk[2], canCreate);
                    if (oCoAResult == null) {
                        miClient.showMsgBoxError("No se encontró configuración de CoA para el documento seleccionado.");
                        return;
                    }
                    if (oCoAResult.getCoAResultRows().isEmpty()) {
                        miClient.showMsgBoxError("No se encontró configuración de CoA para el documento seleccionado.");
                        return;
                    }
                    miForm.setRegistry(oCoAResult);
                    miForm.setFormVisible(true);
                    if (miForm.getFormResult() == SGuiConsts.FORM_RESULT_OK) {
                        SDbCoAResult oRegistry = (SDbCoAResult) miForm.getRegistry();
                        oRegistry.save(miClient.getSession());
                        if (oRegistry.getQueryResultId() == SDbConsts.SAVE_OK) {
                            miClient.showMsgBoxInformation("Resultados guardados con éxito.");
                            miClient.getSession().notifySuscriptors(SModConsts.QLTX_QLT_DPS_ETY);
                        }
                    }
                }
                catch (Exception e) {
                    Logger.getLogger(SViewQualityCoADpsEntry.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
                    miClient.showMsgBoxError(e.getMessage());
                }
            }
        }
    }
    
    private void actionPrintCoA() {
        SDialogRepQltCoA hrsReportsPayroll = null;
        if (jbPrintCoA.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else if (gridRow.isRowSystem()) {
                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_IS_SYSTEM);
                }
                else if (!gridRow.isUpdatable()) {
                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_NON_UPDATABLE);
                }
                else {
                    try {
                        hrsReportsPayroll = new SDialogRepQltCoA(miClient, "Impresión de CoA");
                        int[] pk = gridRow.getRowPrimaryKey();
                        boolean ok = hrsReportsPayroll.setReportsParams(pk[0], pk[1], pk[2], mbCanCaptureCoA);
                        if (ok) {
                            hrsReportsPayroll.setVisible(true);
                        }
                    }
                    catch (Exception e) {
                        SLibUtils.showException(this, e);
                    }
                }
            }
        }
    }

    private void actionOpenCloseCoA() {
        if (jbOpenCloseCoA.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else if (gridRow.isRowSystem()) {
                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_IS_SYSTEM);
                }
                else if (!gridRow.isUpdatable()) {
                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_NON_UPDATABLE);
                }
                else {
                    try {
                        boolean canCreate = false;
                        int[] pk = gridRow.getRowPrimaryKey();
                        SDbCoAResult oCoAResult = SQltUtils.getCoAResults(miClient.getSession(), pk[0], pk[1], pk[2], canCreate);
                        if (oCoAResult == null) {
                            miClient.showMsgBoxError("No se encontró configuración de CoA para el documento seleccionado.");
                            return;
                        }
                        if (oCoAResult.getCoAResultRows().isEmpty()) {
                            miClient.showMsgBoxError("No se encontró configuración de CoA para el documento seleccionado.");
                            return;
                        }
                        if ((oCoAResult.getExternalDocumentId() == null || oCoAResult.getExternalDocumentId().isEmpty()) && !oCoAResult.isClosed()) {
                            miClient.showMsgBoxError("No se han capturado resultados. No se puede validar el documento.");
                            return;
                        }
                        oCoAResult.setClosed(!oCoAResult.isClosed());
                        oCoAResult.save(miClient.getSession());
                        if (oCoAResult.getQueryResultId() == SDbConsts.SAVE_OK) {
                            miClient.showMsgBoxInformation("CoA " + (oCoAResult.isClosed() ? "validado" : "abierto") + " con éxito.");
                            miClient.getSession().notifySuscriptors(SModConsts.QLTX_QLT_DPS_ETY);
                        }
                    }
                    catch (Exception e) {
                        Logger.getLogger(SViewQualityCoADpsEntry.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
                        miClient.showMsgBoxError(e.getMessage());
                    }
                }
            }
        }
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(3);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "NOT d.b_del AND NOT de.b_del ";
        }

        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
        sql += (sql.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("d.dt", (SGuiDate) filter);

        msSql = "SELECT  de.id_year AS " + SDbConsts.FIELD_ID + "1, "
                + "    de.id_doc AS " + SDbConsts.FIELD_ID + "2, "
                + "    de.id_ety AS " + SDbConsts.FIELD_ID + "3, "
                + "    '' AS " + SDbConsts.FIELD_CODE + ", "
                + "    de.concept AS " + SDbConsts.FIELD_NAME + ", "
                + "    d.id_year, "
                + "    d.id_doc, "
                + "    ttd.code AS _tp_doc_code, "
                + "    ttd.tp_dps AS _tp_dps, "
                + "    d.dt, "
                + "    d.num_ser, "
                + "    d.num, "
                + "    d.num_ref, "
                + "    de.concept_key, "
                + "    de.concept, "
                + "    ii.item_key, "
                + "    ii.item, "
                + "    COALESCE(coa.capt_status, 'NA') AS res_st, "
                + "    COALESCE(qdt.template_name, 'NA') AS template_name, "
                + "    COALESCE(qdt.template_standard, 'NA') AS template_standard, "
                + "    bp.bp, "
                + "    d.b_del, "
                + "    coa.ts_coa_gen,"
                + "    coa.b_closed, "
                + "    d.fid_usr_new AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "    d.fid_usr_edit AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "    d.ts_new AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "    d.ts_edit AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "    ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "    uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM "
                + " " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d "
                + "        INNER JOIN "
                + " " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " AS de ON d.id_year = de.id_year "
                + "        AND d.id_doc = de.id_doc "
                + "        INNER JOIN "
                + " " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS ii ON de.fid_item = ii.id_item "
                + "        INNER JOIN "
                + " " + SModConsts.TablesMap.get(SModConsts.TRNU_TP_DPS) + " ttd ON d.fid_ct_dps = ttd.id_ct_dps "
                + "        AND d.fid_cl_dps = ttd.id_cl_dps "
                + "        AND d.fid_tp_dps = ttd.id_tp_dps "
                + "        INNER JOIN "
                + " " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp ON d.fid_bp_r = bp.id_bp "
                + "         LEFT JOIN "
                + " " + SModConsts.TablesMap.get(SModConsts.QLT_COA_RESULT) + " coa ON de.id_year = coa.fk_dps_year "
                + "        AND de.id_doc = coa.fk_dps_doc "
                + "        AND de.id_ety = coa.fk_dps_ety "
                + "         LEFT JOIN "
                + " " + SModConsts.TablesMap.get(SModConsts.QLT_DATASHEET_TEMPLATE) + " AS qdt ON coa.fk_datasheet_template_n = qdt.id_datasheet_template "
                + "        LEFT JOIN "
                + " " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
                + "         d.fid_usr_new = ui.id_usr "
                + "         LEFT JOIN "
                + " " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON "
                + "         d.fid_usr_edit = uu.id_usr "
                + "WHERE "
                + "        d.fid_ct_dps = 2 "
                + "        AND d.fid_cl_dps = 3 "
                + "        AND d.fid_tp_dps = 1 "
                + "        AND d.fid_func IN (1 , 2, 3, 4, 5, 6) "
                + (sql.isEmpty() ? "" : ("AND " + sql))
                + "        AND EXISTS (SELECT  "
                + "            1 "
                + "        FROM "
                + "            " + SModConsts.TablesMap.get(SModConsts.QLT_DATASHEET_TEMPLATE_LINK) + " AS qcr "
                + "        WHERE "
                + "            qcr.b_del = 0 "
                + "                AND ((qcr.fk_tp_link = 11 "
                + "                AND qcr.fk_ref = ii.id_item) "
                + "                OR (qcr.fk_tp_link = 10 "
                + "                AND qcr.fk_ref IN (SELECT  "
                + "                    i.fid_mfr "
                + "                FROM "
                + "                    " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i "
                + "                WHERE "
                + "                    i.id_item = ii.id_item)) "
                + "                OR (qcr.fk_tp_link = 9 "
                + "                AND qcr.fk_ref IN (SELECT  "
                + "                    i.fid_brd "
                + "                FROM "
                + "                    " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i "
                + "                WHERE "
                + "                    i.id_item = ii.id_item)) "
                + "                OR (qcr.fk_tp_link = 8 "
                + "                AND qcr.fk_ref IN (SELECT  "
                + "                    i.fid_line_n "
                + "                FROM "
                + "                    " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i "
                + "                WHERE "
                + "                    i.id_item = ii.id_item)) "
                + "                OR (qcr.fk_tp_link = 7 "
                + "                AND qcr.fk_ref IN (SELECT  "
                + "                    i.fid_igen "
                + "                FROM "
                + "                    " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i "
                + "                WHERE "
                + "                    i.id_item = ii.id_item)) "
                + "                OR (qcr.fk_tp_link = 6 "
                + "                AND qcr.fk_ref IN (SELECT  "
                + "                    igen.fid_igrp "
                + "                FROM "
                + "                    " + SModConsts.TablesMap.get(SModConsts.ITMU_IGEN) + " AS igen "
                + "                        INNER JOIN "
                + "                    " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON igen.id_igen = i.fid_igen "
                + "                WHERE "
                + "                    i.id_item = ii.id_item)) "
                + "                OR (qcr.fk_tp_link = 5 "
                + "                AND qcr.fk_ref IN (SELECT  "
                + "                    igrp.fid_ifam "
                + "                FROM "
                + "                    " + SModConsts.TablesMap.get(SModConsts.ITMU_IGRP) + " AS igrp "
                + "                        INNER JOIN "
                + "                    " + SModConsts.TablesMap.get(SModConsts.ITMU_IGEN) + " AS igen ON igrp.id_igrp = igen.fid_igrp "
                + "                        INNER JOIN "
                + "                    " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON igen.id_igen = i.fid_igen "
                + "                WHERE "
                + "                    i.id_item = ii.id_item)) "
                + "                OR (qcr.fk_tp_link = 4 "
                + "                AND qcr.fk_ref IN (SELECT  "
                + "                    itp.tp_idx "
                + "                FROM "
                + "                    " + SModConsts.TablesMap.get(SModConsts.ITMU_IGEN) + " AS igen "
                + "                        INNER JOIN "
                + "                    " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON igen.id_igen = i.fid_igen "
                + "                        INNER JOIN "
                + "                    " + SModConsts.TablesMap.get(SModConsts.ITMS_TP_ITEM) + " AS itp ON igen.fid_ct_item = itp.id_ct_item "
                + "                        AND igen.fid_cl_item = itp.id_cl_item "
                + "                        AND igen.fid_tp_item = itp.id_tp_item "
                + "                WHERE "
                + "                    i.id_item = ii.id_item)) "
                + "                OR (qcr.fk_tp_link = 3 "
                + "                AND qcr.fk_ref IN (SELECT  "
                + "                    icl.cl_idx "
                + "                FROM "
                + "                    " + SModConsts.TablesMap.get(SModConsts.ITMU_IGEN) + " AS igen "
                + "                        INNER JOIN "
                + "                    " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON igen.id_igen = i.fid_igen "
                + "                        INNER JOIN "
                + "                    " + SModConsts.TablesMap.get(SModConsts.ITMS_CL_ITEM) + " AS icl ON igen.fid_ct_item = icl.id_ct_item "
                + "                        AND igen.fid_cl_item = icl.id_cl_item "
                + "                WHERE "
                + "                    i.id_item = ii.id_item)) "
                + "                OR (qcr.fk_tp_link = 2 "
                + "                AND qcr.fk_ref IN (SELECT  "
                + "                    ict.ct_idx "
                + "                FROM "
                + "                    " + SModConsts.TablesMap.get(SModConsts.ITMU_IGEN) + " AS igen "
                + "                        INNER JOIN "
                + "                    " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON igen.id_igen = i.fid_igen "
                + "                        INNER JOIN "
                + "                    " + SModConsts.TablesMap.get(SModConsts.ITMS_CT_ITEM) + " AS ict ON igen.fid_ct_item = ict.id_ct_item "
                + "                WHERE "
                + "                    i.id_item = ii.id_item)) "
                + "                OR (qcr.fk_tp_link = 1 AND qcr.fk_ref = 0)) "
                + "        ORDER BY qcr.fk_tp_link DESC);";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "d.dt", "Fecha"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "_tp_doc_code", "Tipo doc."));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "_tp_dps", "Tipo doc."));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "d.num_ser", "Número"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "d.num", "Número"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "d.num_ref", "Referencia"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "de.concept_key", "Clave concepto"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "de.concept", "Concepto"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "ii.item_key", "Clave item"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "ii.item", "Item"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "bp.bp", "Cliente"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "res_st", "Estatus CoA"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "template_name", "Ficha técnica"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "template_standard", "Estándar"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "coa.b_closed", "Cerrado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "ts_coa_gen", "Generación CoA"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS));

        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == jbCaptureCoA) {
                actionCaptureCoA();
            }
            else if (button == jbPrintCoA) {
                actionPrintCoA();
            }
            else if (button == jbOpenCloseCoA) {
                actionOpenCloseCoA();
            }
        }
    }
}