/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view;

import erp.data.SDataConstantsSys;
import erp.mcfg.data.SCfgUtils;
import erp.mod.SModConsts;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDatePeriod;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;

/**
 *
 * @author Isabel Servín
 */
public class SViewDpsAccountTag extends SGridPaneView implements ItemListener {

    private SGridFilterDatePeriod moFilterDatePeriod;
    private JLabel jlAccTag;
    private JComboBox<String> jcbAccTag;
    
    public SViewDpsAccountTag(SGuiClient client, int subType, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.TRNX_DPS_ACC_TAG, subType, title);
        initComponentsCustom();
    }

    private void initComponentsCustom() {
        setRowButtonsEnabled(false);
        
        jlAccTag = new JLabel("Etiqueta contable:");
        jcbAccTag = new JComboBox<>(); 
        jcbAccTag.addItemListener(this);
        
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        
        try {
            String sAccTags[] = SCfgUtils.getParamValue(miClient.getSession().getStatement(), SDataConstantsSys.CFG_PARAM_TRN_ACC_TAGS).replaceAll(" ", "").split(",");
            jcbAccTag.removeAllItems();
            jcbAccTag.addItem("TODAS");
            for (String tag : sAccTags) {
                jcbAccTag.addItem(tag);
            }
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jlAccTag);
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jcbAccTag);
        } 
        catch (Exception e) {
            SLibUtils.printException(this, e);
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
        
        if (((String)jcbAccTag.getSelectedItem()).equals("TODAS")) {
            where = "WHERE d.acc_tag <> '' ";
        }
        else {
            where = "WHERE d.acc_tag = '" + (String)jcbAccTag.getSelectedItem() + "' ";
        }
        
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
                + "bp.bp, "
                + "d.acc_tag "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNU_TP_DPS) + " AS dt ON "
                + "d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp ON "
                + "d.fid_bp_r = bp.id_bp "
                + where
                + (mnGridSubtype == SModConsts.MOD_TRN_SAL_N ?
                "AND d.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_INV[0] + " AND fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_INV[1] + " AND fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_INV[2] + " " :
                "AND d.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_INV[0] + " AND fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_INV[1] + " AND fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_INV[2] + " ")
                + (mnGridSubtype == SModConsts.MOD_TRN_SAL_N ? "ORDER BY acc_tag, d.num, bp" : "ORDER BY acc_tag, bp, d.num");
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        int col = 0;
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();
        SGridColumnView[] columns = new SGridColumnView[6];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ACC, "acc_tag", "Etiqueta contable", 100);
        if (mnGridSubtype == SModConsts.MOD_TRN_SAL_N) {
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, SDbConsts.FIELD_CODE, "Tipo documento");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "num", "Folio documento");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "num_ref", "Referencia documento");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt", "Fecha documento");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "bp", "Asociado negocios");
        }
        else {
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "bp", "Asociado negocios");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, SDbConsts.FIELD_CODE, "Tipo documento");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "num", "Folio documento");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "num_ref", "Referencia documento");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt", "Fecha documento");
        }
        
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
        actionGridReload();
    }
}
