/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.qlt.view;


import erp.mod.SModConsts;
import erp.mod.qlt.db.SDbCoAResult;
import erp.mod.qlt.db.SQltUtils;
import erp.mod.qlt.form.SFormCoACapture;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import sa.gui.util.SUtilConsts;
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
public class SViewQualityCoADpsEntry extends SGridPaneView implements ActionListener {

    private SGridFilterDatePeriod moFilterDatePeriod;
    SFormCoACapture miForm;
    
    private JButton jbCaptureCoA;
    private JButton jbPrintCoA;

    public SViewQualityCoADpsEntry(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.QLTX_QLT_DPS_ETY, SLibConsts.UNDEFINED, title);
        initComponents();
    }

    private void initComponents() {
        setRowButtonsEnabled(false, false, false, false, false);

        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod
                .initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
        jbCaptureCoA = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_link.gif")), "Captura de resultados", this);
        jbPrintCoA = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_link.gif")), "Imprimir CoA", this);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbCaptureCoA);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPrintCoA);
        
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
                    int[] pk = gridRow.getRowPrimaryKey();
                    SDbCoAResult oCoAResult = SQltUtils.getCoAResults(miClient.getSession(), pk[0], pk[1], pk[2]);
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
        miClient.showMsgBoxInformation("Imprimiendo...");
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
                + "    bp.bp, "
                + "    d.b_del, "
                + "    d.fid_usr_new AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "    d.fid_usr_edit AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "    d.ts_new AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "    d.ts_edit AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "    ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "    uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM "
                + "    trn_dps AS d "
                + "        INNER JOIN "
                + "    trn_dps_ety AS de ON d.id_year = de.id_year "
                + "        AND d.id_doc = de.id_doc "
                + "        INNER JOIN "
                + "    erp.itmu_item AS ii ON de.fid_item = ii.id_item "
                + "        INNER JOIN "
                + "    erp.trnu_tp_dps ttd ON d.fid_ct_dps = ttd.id_ct_dps "
                + "        AND d.fid_cl_dps = ttd.id_cl_dps "
                + "        AND d.fid_tp_dps = ttd.id_tp_dps "
                + "        INNER JOIN "
                + "    erp.bpsu_bp AS bp ON d.fid_bp_r = bp.id_bp "
                + "        INNER JOIN "
                + " " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
                + "         d.fid_usr_new = ui.id_usr "
                + "         INNER JOIN "
                + " " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON "
                + "         d.fid_usr_edit = uu.id_usr "
                + "WHERE "
                + "        d.fid_ct_dps = 2 "
                + "        AND d.fid_cl_dps = 3 "
                + "        AND d.fid_tp_dps = 1 "
                + "        AND d.fid_func IN (1 , 2, 3, 4, 5, 6) "
                + (sql.isEmpty() ? "" : ("AND " + sql))
                + "        AND EXISTS ( SELECT  "
                + "            1 "
                + "        FROM "
                + "            qlt_datasheet_template_link AS qcr "
                + "        WHERE "
                + "            qcr.b_del = 0 "
                + "                AND ((qcr.fk_tp_link = 11 "
                + "                AND qcr.fk_ref = ii.id_item) "
                + "                OR (qcr.fk_tp_link = 10 "
                + "                AND qcr.fk_ref IN (SELECT  "
                + "                    i.fid_mfr "
                + "                FROM "
                + "                    erp.itmu_item AS i "
                + "                WHERE "
                + "                    i.id_item = ii.id_item)) "
                + "                OR (qcr.fk_tp_link = 9 "
                + "                AND qcr.fk_ref IN (SELECT  "
                + "                    i.fid_brd "
                + "                FROM "
                + "                    erp.itmu_item AS i "
                + "                WHERE "
                + "                    i.id_item = ii.id_item)) "
                + "                OR (qcr.fk_tp_link = 8 "
                + "                AND qcr.fk_ref IN (SELECT  "
                + "                    i.fid_line_n "
                + "                FROM "
                + "                    erp.itmu_item AS i "
                + "                WHERE "
                + "                    i.id_item = ii.id_item)) "
                + "                OR (qcr.fk_tp_link = 7 "
                + "                AND qcr.fk_ref IN (SELECT  "
                + "                    i.fid_igen "
                + "                FROM "
                + "                    erp.itmu_item AS i "
                + "                WHERE "
                + "                    i.id_item = ii.id_item)) "
                + "                OR (qcr.fk_tp_link = 6 "
                + "                AND qcr.fk_ref IN (SELECT  "
                + "                    igen.fid_igrp "
                + "                FROM "
                + "                    erp.itmu_igen AS igen "
                + "                        INNER JOIN "
                + "                    erp.itmu_item AS i ON igen.id_igen = i.fid_igen "
                + "                WHERE "
                + "                    i.id_item = ii.id_item)) "
                + "                OR (qcr.fk_tp_link = 5 "
                + "                AND qcr.fk_ref IN (SELECT  "
                + "                    igrp.fid_ifam "
                + "                FROM "
                + "                    erp.itmu_igrp AS igrp "
                + "                        INNER JOIN "
                + "                    erp.itmu_igen AS igen ON igrp.id_igrp = igen.fid_igrp "
                + "                        INNER JOIN "
                + "                    erp.itmu_item AS i ON igen.id_igen = i.fid_igen "
                + "                WHERE "
                + "                    i.id_item = ii.id_item)) "
                + "                OR (qcr.fk_tp_link = 4 "
                + "                AND qcr.fk_ref IN (SELECT  "
                + "                    itp.tp_idx "
                + "                FROM "
                + "                    erp.itmu_igen AS igen "
                + "                        INNER JOIN "
                + "                    erp.itmu_item AS i ON igen.id_igen = i.fid_igen "
                + "                        INNER JOIN "
                + "                    erp.itms_tp_item AS itp ON igen.fid_ct_item = itp.id_ct_item "
                + "                        AND igen.fid_cl_item = itp.id_cl_item "
                + "                        AND igen.fid_tp_item = itp.id_tp_item "
                + "                WHERE "
                + "                    i.id_item = ii.id_item)) "
                + "                OR (qcr.fk_tp_link = 3 "
                + "                AND qcr.fk_ref IN (SELECT  "
                + "                    icl.cl_idx "
                + "                FROM "
                + "                    erp.itmu_igen AS igen "
                + "                        INNER JOIN "
                + "                    erp.itmu_item AS i ON igen.id_igen = i.fid_igen "
                + "                        INNER JOIN "
                + "                    erp.itms_cl_item AS icl ON igen.fid_ct_item = icl.id_ct_item "
                + "                        AND igen.fid_cl_item = icl.id_cl_item "
                + "                WHERE "
                + "                    i.id_item = ii.id_item)) "
                + "                OR (qcr.fk_tp_link = 2 "
                + "                AND qcr.fk_ref IN (SELECT  "
                + "                    ict.ct_idx "
                + "                FROM "
                + "                    erp.itmu_igen AS igen "
                + "                        INNER JOIN "
                + "                    erp.itmu_item AS i ON igen.id_igen = i.fid_igen "
                + "                        INNER JOIN "
                + "                    erp.itms_ct_item AS ict ON igen.fid_ct_item = ict.id_ct_item "
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
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL));
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
        }
    }

}