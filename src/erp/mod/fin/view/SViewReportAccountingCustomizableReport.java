/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.view;

import erp.gui.account.SAccountConsts;
import erp.mod.SModConsts;
import erp.mod.fin.form.SDialogReportAccountingAcc;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import sa.lib.SLibTimeUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDateRange;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;

/**
 *
 * @author Isabel Servín
 */
public class SViewReportAccountingCustomizableReport extends SGridPaneView implements ActionListener {

    SGridFilterDateRange moFilterDateRange;
    
    JButton jbAcc;
    JButton jbCc;
    
    SDialogReportAccountingAcc moDialogReportAccountingAcc;
    
    public SViewReportAccountingCustomizableReport(SGuiClient client, int subType, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.FINX_REP_CUS_ACC, subType, title);
        setRowButtonsEnabled(false);
        initComponentsCustom();
    }
    
    private void initComponentsCustom() {
        moFilterDateRange = new SGridFilterDateRange(miClient, this);
        moFilterDateRange.initFilter(new Date[] { SLibTimeUtils.getBeginOfMonth(miClient.getSession().getCurrentDate()), SLibTimeUtils.getEndOfMonth(miClient.getSession().getCurrentDate()) }); 
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDateRange);
        getPanelCommandsSys(SGuiConsts.PANEL_LEFT).setVisible(false);
        jtbFilterDeleted.setVisible(false);
        
        jbAcc = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_kardex.gif")), "Ver cuentas contables de la vista", this);
        jbCc = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_detail.gif")), "Ver centros de costo de la vista", this);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbAcc);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbCc);
        
        moDialogReportAccountingAcc = new SDialogReportAccountingAcc(miClient, mnGridSubtype);
    }
    
    private void actionAcc() {
        moDialogReportAccountingAcc.setValue(1, SAccountConsts.TYPE_ACCOUNT);
        moDialogReportAccountingAcc.setValue(2, "Cuentas contables");
        moDialogReportAccountingAcc.populateArea();
        moDialogReportAccountingAcc.setVisible(true);
    }

    private void actionCc() {
        moDialogReportAccountingAcc.setValue(1, SAccountConsts.TYPE_COST_CENTER);
        moDialogReportAccountingAcc.setValue(2, "Centros de costo");
        moDialogReportAccountingAcc.populateArea();
        moDialogReportAccountingAcc.setVisible(true);
    }
    
    private String getSqlAcc() {
        String acc = "";
        
        try {
            String sql = "SELECT ccs.id_acc _sta, cce.id_acc _end FROM fin_rep_cus_acc_acc AS r " +
                    "INNER JOIN fin_acc AS ccs ON r.id_acc_start = ccs.pk_acc " +
                    "INNER JOIN fin_acc AS cce ON r.id_acc_end = cce.pk_acc " +
                    "WHERE r.id_rep_cus_acc = " + mnGridSubtype;
            ResultSet resultSet = miClient.getSession().getStatement().executeQuery(sql);
            while (resultSet.next()) {
                acc += (acc.isEmpty() ? "(" : "OR ") + "re.fid_acc BETWEEN '" + resultSet.getString(1) + "' AND '" + resultSet.getString(2) + "' ";
            }
            acc += acc.isEmpty() ? "" : ") ";
        }
        catch (Exception e) {}
        
        return acc;
    }
    
    private String getSqlCc() {
        String cc = "";
        
        try {
            String sql = "SELECT ccs.id_cc _sta, cce.id_cc _end FROM fin_rep_cus_acc_cc AS r " +
                    "INNER JOIN fin_cc AS ccs ON r.id_cc_start = ccs.pk_cc " +
                    "INNER JOIN fin_cc AS cce ON r.id_cc_end = cce.pk_cc " +
                    "WHERE r.id_rep_cus_acc = " + mnGridSubtype;
            ResultSet resultSet = miClient.getSession().getStatement().executeQuery(sql);
            while (resultSet.next()) {
                cc += (cc.isEmpty() ? "(" : "OR ") + "re.fid_cc_n BETWEEN '" + resultSet.getString(1) + "' AND '" + resultSet.getString(2) + "' ";
            }
            cc += cc.isEmpty() ? "" : ") ";
        }
        catch (Exception e) {}
        
        return cc;
    }

    @Override
    public void prepareSqlQuery() {
        String where = "";
        Object filter;
        
        String sqlAcc = getSqlAcc();
        String sqlCc = getSqlCc();
        
        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setUserInsertApplying(false);
        moPaneSettings.setUserUpdateApplying(false);
        moPaneSettings.setDeletedApplying(false);
        
        filter = (Date[]) moFiltersMap.get(SGridConsts.FILTER_DATE_RANGE).getValue();
        where += (where.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDateRange("r.dt", (Date[]) filter);
        
        msSql = "SELECT DISTINCT " 
                + "r.id_year AS " + SDbConsts.FIELD_ID + "1, "
                + "CONCAT(r.id_year, '-', LPAD(r.id_per, 2, 0)) per, "
                + "CONCAT(r.id_tp_rec, '-', LPAD(r.id_num, 6, 0)) AS " + SDbConsts.FIELD_NAME + ", " 
                + "' ' AS " + SDbConsts.FIELD_CODE + ", " 
                + "r.dt, " 
                + "re.fid_acc, " 
                + "acc, " 
                + "fid_cc_n, " 
                + "cc, " 
                + "re.concept, " 
                + "debit, credit, " 
                + "fid_item_n, " 
                + "i.name, " 
                + "i.item_key "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS re ON "
                + "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS acc ON "
                + "re.fk_acc = acc.pk_acc "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_CC) + " AS cc ON "
                + "re.fk_cc_n = cc.pk_cc "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON "
                + "re.fid_item_n = i.id_item "
                + (where.length() == 0 ? "" : "WHERE " + where) + " " 
                + (sqlAcc.length() == 0 ? "" : "AND " + sqlAcc) + " " 
                + (sqlCc.length() == 0 ? "" : "AND " + sqlCc) + " " 
                + "AND NOT r.b_del AND NOT re.b_del "
                + "ORDER BY fid_cc_n, item_key, dt, r.id_num ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_PER, "per", "Periodo póliza"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, SDbConsts.FIELD_NAME, "Folio póliza"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "r.dt", SGridConsts.COL_TITLE_DATE));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ACC, "fid_acc", "No. cuenta contable"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ACC, "acc", "Cuenta contable"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ACC, "fid_cc_n", "No. centro costo"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ACC, "cc", "Centro costo"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "item_key", "Clave ítem/Gasto"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "i.name", "Ítem/Gasto"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "re.concept", "Concepto"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "re.debit", "Debe $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "re.credit", "Haber $"));
        
        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.FIN_REC);
        moSuscriptionsSet.add(SModConsts.FIN_REC_ETY);
        moSuscriptionsSet.add(SModConsts.FIN_ACC);
        moSuscriptionsSet.add(SModConsts.FIN_CC);
        moSuscriptionsSet.add(SModConsts.ITMU_ITEM);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbAcc) {
                actionAcc();
            }
            else if (button == jbCc) {
                actionCc();
            }
        }
    }
}
