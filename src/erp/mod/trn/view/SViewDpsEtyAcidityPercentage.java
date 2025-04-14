/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view;

import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.mod.SModConsts;
import erp.mtrn.data.STrnFunctionalAreaUtils;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JLabel;
import javax.swing.JTextField;
import sa.lib.SLibConsts;
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
public class SViewDpsEtyAcidityPercentage extends SGridPaneView {

    private SGridFilterDatePeriod moFilterDatePeriod;
    
    private JLabel jlFuncArea;
    private JTextField jtfFuncArea;
    
    public SViewDpsEtyAcidityPercentage(SGuiClient client, int subType, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.TRNX_DPS_ETY_ACI_PER, subType, title);
        initComponentsCustom();
    }

    private void initComponentsCustom() {
        setRowButtonsEnabled(false);
        
        String areas[] = STrnFunctionalAreaUtils.getTextFilterOfFunctionalAreas((SClientInterface) miClient, SLibConsts.UNDEFINED);
        
        jlFuncArea = new JLabel("Áreas funcionales:");
        jtfFuncArea = new JTextField(areas[1]);
        jtfFuncArea.setPreferredSize(new java.awt.Dimension(100, 23));
        jtfFuncArea.setEnabled(false);
        
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jlFuncArea);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jtfFuncArea);
    }
    
    @Override
    public void prepareSqlQuery() {
        String where = "";
        Object filter;
        
        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
        if (filter != null) {
            where += (where.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("d.dt", (SGuiDate) filter);
        }
        
        moPaneSettings = new SGridPaneSettings(3);
        moPaneSettings.setDisabledApplying(false);
        moPaneSettings.setDeletedApplying(false);
        moPaneSettings.setUserInsertApplying(false);
        moPaneSettings.setUserUpdateApplying(false);
        
        msSql = "SELECT "
                + "de.id_year AS " + SDbConsts.FIELD_ID + "1, "
                + "de.id_doc AS " + SDbConsts.FIELD_ID + "2, "
                + "de.id_ety AS " + SDbConsts.FIELD_ID + "3, "
                + "dt.code AS " + SDbConsts.FIELD_CODE + ", "
                + "'' AS " + SDbConsts.FIELD_NAME + ", "
                + "IF(d.num_ser <> '', CONCAT(d.num_ser, '-', d.num), d.num) AS num, "
                + "d.num_ref, "
                + "d.dt, "
                + "(SELECT cob.code FROM erp.bpsu_bpb AS cob WHERE d.fid_cob = cob.id_bpb) AS f_cob_code, "
                + "bp.bp, "
                + "bpc.bp_key, "
                + "bpb.bpb, "
                + "i.item, "
                + "de.qty, "
                + "u.symbol, "
                + "de.acidity_per_n "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " AS de ON "
                + "d.id_year = de.id_year AND d.id_doc = de.id_doc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNU_TP_DPS) + " AS dt ON "
                + "d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp ON "
                + "d.fid_bp_r = bp.id_bp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP_CT) + " AS bpc ON "
                + "bp.id_bp = bpc.id_bp AND bpc.id_ct_bp = " + (mnGridSubtype == SModConsts.MOD_TRN_SAL_N ?  SDataConstantsSys.BPSS_CT_BP_CUS : SDataConstantsSys.BPSS_CT_BP_SUP) + " " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS bpb ON "
                + "d.fid_bpb = bpb.id_bpb "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON "
                + "de.fid_item = i.id_item "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS u ON "
                + "de.fid_unit = u.id_unit "
                + "WHERE de.acidity_per_n IS NOT NULL "
                + (mnGridSubtype == SModConsts.MOD_TRN_SAL_N ?
                "AND d.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_INV[0] + " AND fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_INV[1] + " AND fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_INV[2] + " " :
                "AND d.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_INV[0] + " AND fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_INV[1] + " AND fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_INV[2] + " ")
                + "AND NOT d.b_del AND NOT de.b_del AND d.fid_st_dps <> " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + " "
                + (where.isEmpty() ? "" : "AND " + where)
                + (mnGridSubtype == SModConsts.MOD_TRN_SAL_N ? "ORDER BY d.num, bp" : "ORDER BY bp, d.num");
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        int col = 0;
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();
        SGridColumnView[] columns = new SGridColumnView[12];

        if (mnGridSubtype == SModConsts.MOD_TRN_SAL_N) {
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, SDbConsts.FIELD_CODE, "Tipo documento");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "num", "Folio documento");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "num_ref", "Referencia documento");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt", "Fecha documento");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "f_cob_code", "Sucursal empresa", 35);
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "bp", "Cliente");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "bpc.bp_key", "Clave cliente", 50);
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "bpb.bpb", "Sucursal cliente", 75);
        }
        else {
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "bp", "Proveedor");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "bpc.bp_key", "Clave proveedor", 50);
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "bpb.bpb", "Sucursal proveedor", 75);
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, SDbConsts.FIELD_CODE, "Tipo documento");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "num", "Folio documento");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "num_ref", "Referencia documento");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt", "Fecha documento");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "f_cob_code", "Sucursal empresa", 35);
        }
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "item", "Ítem");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT_UNIT, "qty", "Cantidad");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "symbol", "Unidad");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_4D, "acidity_per_n", "Acidez");
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
}
