/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view;

import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.mcfg.data.SCfgUtils;
import erp.mod.SModConsts;
import erp.mtrn.data.STrnFunctionalAreaUtils;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import sa.lib.SLibConsts;
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
public class SViewDpsAccountTag extends SGridPaneView implements ActionListener, ItemListener {

    private SGridFilterDatePeriod moFilterDatePeriod;
    private JLabel jlAccTag;
    private JLabel jlFuncArea;
    private JComboBox<String> jcbAccTag;
    private JTextField jtfFuncArea;
    
    private javax.swing.JButton mjbViewDps;
    private javax.swing.JButton mjbViewNotes;
    private javax.swing.JButton mjbViewLinks;
    
    public SViewDpsAccountTag(SGuiClient client, int subType, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.TRNX_DPS_ACC_TAG, subType, title);
        initComponentsCustom();
    }

    private void initComponentsCustom() {
        setRowButtonsEnabled(false);
        
        mjbViewDps = new JButton(miClient.getImageIcon(SLibConstants.ICON_LOOK));
        mjbViewNotes = new JButton(miClient.getImageIcon(SLibConstants.ICON_NOTES));
        mjbViewLinks = new JButton(miClient.getImageIcon(SLibConstants.ICON_LINK));
        
        mjbViewDps.setToolTipText("Ver documento");
        mjbViewNotes.setToolTipText("Ver notas");
        mjbViewLinks.setToolTipText("Ver vínculos del documento");
        
        mjbViewDps.addActionListener(this);
        mjbViewNotes.addActionListener(this);
        mjbViewLinks.addActionListener(this);
        
        String areas[] = STrnFunctionalAreaUtils.getTextFilterOfFunctionalAreas((SClientInterface) miClient, SLibConsts.UNDEFINED);
        
        jlAccTag = new JLabel("Etiqueta contable:");
        jlFuncArea = new JLabel("  Áreas funcionales:");
        
        jcbAccTag = new JComboBox<>(); 
        jcbAccTag.setPreferredSize(new java.awt.Dimension(100, 23));
        jcbAccTag.addItemListener(this);
        
        jtfFuncArea = new JTextField(areas[1]);
        jtfFuncArea.setPreferredSize(new java.awt.Dimension(100, 23));
        jtfFuncArea.setEnabled(false);
        
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        
        try {
            String sAccTags[] = SCfgUtils.getParamValue(miClient.getSession().getStatement(), SDataConstantsSys.CFG_PARAM_TRN_ACC_TAGS).replaceAll(" ", "").split(",");
            jcbAccTag.removeAllItems();
            jcbAccTag.addItem("(TODAS)");
            for (String tag : sAccTags) {
                jcbAccTag.addItem(tag);
            }
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jlAccTag);
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jcbAccTag);
        } 
        catch (Exception e) {
            SLibUtils.printException(this, e);
        }
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jlFuncArea);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jtfFuncArea);
    }
    
    @Override
    public void prepareSqlQuery() {
        String where;
        Object filter;
        
        moPaneSettings = new SGridPaneSettings(2);
        moPaneSettings.setDisabledApplying(false);
        moPaneSettings.setDeletedApplying(false);
        moPaneSettings.setUserInsertApplying(false);
        moPaneSettings.setUserUpdateApplying(false);
        
        if (((String)jcbAccTag.getSelectedItem()).equals("(TODAS)")) {
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
                + "(SELECT cob.code FROM erp.bpsu_bpb AS cob WHERE d.fid_cob = cob.id_bpb) AS f_cob_code, "
                + "(SELECT de.concept FROM trn_dps_ety AS de WHERE de.id_doc = d.id_doc AND de.id_year = d.id_year AND NOT de.b_del ORDER BY de.id_ety LIMIT 1) AS concept, "
                + "f.code AS func, "
                + "bp.bp, "
                + "bpc.bp_key, "
                + "bpb.bpb, "
                + "IF(d.fid_cl_dps = 5, COALESCE(d.stot_r, 0.0) * -1, COALESCE(d.stot_r, 0.0)) AS stot_r, "
                + "'" + miClient.getSession().getSessionCustom().getLocalCurrencyCode() + "' AS _cur, "
                + "d.acc_tag "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNU_TP_DPS) + " AS dt ON "
                + "d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_FUNC) + " AS f ON "
                + "d.fid_func = f.id_func "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp ON "
                + "d.fid_bp_r = bp.id_bp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP_CT) + " AS bpc ON "
                + "bp.id_bp = bpc.id_bp AND bpc.id_ct_bp = " + (mnGridSubtype == SModConsts.MOD_TRN_SAL_N ?  SDataConstantsSys.BPSS_CT_BP_CUS : SDataConstantsSys.BPSS_CT_BP_SUP) + " " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS bpb ON "
                + "d.fid_bpb = bpb.id_bpb "
                + where
                + (mnGridSubtype == SModConsts.MOD_TRN_SAL_N ?
                "AND d.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_INV[0] + " AND (fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_INV[1] + " OR fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_CN[1] + ") AND fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_INV[2] + " " :
                "AND d.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_INV[0] + " AND (fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_INV[1] + " OR fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_CN[1] + ") AND fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_INV[2] + " ")
                + "AND NOT d.b_del "
                + (mnGridSubtype == SModConsts.MOD_TRN_SAL_N ? "ORDER BY acc_tag, d.num, bp" : "ORDER BY acc_tag, bp, d.num");
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        int col = 0;
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();
        SGridColumnView[] columns = new SGridColumnView[13];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ACC, "acc_tag", "Etiqueta contable", 100);
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
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "stot_r", "Subtotal $");
        columns[col++].setSumApplying(true);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "_cur", "Moneda");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "func", "Área funcional", 35);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "concept", "Concepto", 200);
        
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

//            if (button == mjbViewDps) {
//                actionViewDps();
//            }
//            else if (button == mjbViewNotes) {
//                actionViewNotes();
//            }
//            else if (button == mjbViewLinks) {
//                actionViewLinks();
//            }
        }
    }
}
