/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.view;

import erp.lib.SLibUtilities;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.cfg.swap.SSwapConsts;
import erp.mod.cfg.swap.SSwapUtils;
import erp.mod.cfg.swap.SSyncType;
import erp.mod.cfg.swap.utils.SExportUtils;
import erp.mod.cfg.swap.utils.SResponses;
import erp.mod.fin.db.SDbPaymentEntry;
import erp.mod.view.SViewFilter;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDatePeriod;
import sa.lib.grid.SGridFilterValue;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;

/**
 *
 * @author Isabel Servín, Sergio Flores
 */
public class SViewPaymentStatus extends SGridPaneView implements ActionListener {
    
    private SGridFilterDatePeriod moFilterDatePeriod;
    private SViewFilter moFilterFunc;
    private SViewFilter moFilterCur;
    private JButton jbExportDataToSwapServices;
    
    private boolean mbIsDetail;
    
    public SViewPaymentStatus(SGuiClient client, int subType, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.FINX_PAY_ST, subType, title);
        initComponetsCustom();
    }
    
    private void initComponetsCustom() {
        setRowButtonsEnabled(false);
        
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
        moFilterFunc = new SViewFilter(miClient, this, SModConsts.CFGU_FUNC);
        moFilterFunc.initFilter(null);
        moFilterCur = new SViewFilter(miClient, this, SModConsts.CFGU_CUR);
        moFilterCur.initFilter(null);
        
        jbExportDataToSwapServices = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_move_up_ind.gif")),
            "Exportar registros '" + SSwapUtils.translateSyncType(SSyncType.PUR_PAYMENT, SLibConsts.LAN_ISO639_ES) + "' a " + SSwapConsts.SWAP_SERVICES, this);
        
        if (mnGridSubtype != SModSysConsts.FINS_ST_PAY_RCPT_P) {
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        }
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterFunc);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterCur);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbExportDataToSwapServices);
        
        mbIsDetail = mnGridSubtype == SModSysConsts.FINX_ST_PAY_EXEC_DET || mnGridSubtype == SModSysConsts.FINX_ST_PAY_RCPT_DET;
    }
    
    private void actionExportDataToSwapServices() {
        if (jbExportDataToSwapServices.isEnabled()) {
            try {
                miClient.getFrame().getRootPane().setCursor(new Cursor(Cursor.WAIT_CURSOR));
                SResponses responses = SExportUtils.exportData(miClient.getSession(), SSyncType.PUR_PAYMENT, true, SExportUtils.EXPORT_MODE_CONFIRM);
                SExportUtils.processResponses(miClient.getSession(), responses, 0, 0);
                miClient.getSession().notifySuscriptors(SModConsts.FIN_PAY);
            }
            catch (Exception e) {
                SLibUtilities.printOutException(this, e);
            }
            finally {
                miClient.getFrame().getRootPane().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        }
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter;

        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);
        moPaneSettings.setSystemApplying(false);
        
        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "NOT v.b_del ";
        }
        
        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
        if (filter != null) {
            sql += (sql.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("v.dt_app", (SGuiDate) filter);
        }
        
        filter = ((SGridFilterValue) moFiltersMap.get(SModConsts.CFGU_FUNC)).getValue();
        if (filter != null && !((String) filter).isEmpty()) {
            sql += (sql.isEmpty() ? "" : "AND ") + "v.fk_func IN (" + filter + ") ";
        }
        
        filter = ((SGridFilterValue) moFiltersMap.get(SModConsts.CFGU_CUR)).getValue();
        if (filter != null && !((String) filter).isEmpty()) {
            sql += (sql.isEmpty() ? "" : "AND ") + "v.fk_cur IN (" + filter + ") ";
        }
        setShowSums(filter != null && ((String) filter).split(",").length == 1);
        
        switch (mnGridSubtype) {
            case SModSysConsts.FINS_ST_PAY_RCPT_P:
                sql += (sql.isEmpty() ? "" : "AND ") + "v.fk_st_pay IN ("
                        + SModSysConsts.FINS_ST_PAY_EXEC + ", "
                        + SModSysConsts.FINS_ST_PAY_EXEC_P + ") /*AND b_rcpt_pay_req */";
                break;
            case SModSysConsts.FINS_ST_PAY_EXEC:
            case SModSysConsts.FINX_ST_PAY_EXEC_DET:
                sql += (sql.isEmpty() ? "" : "AND ") + "v.fk_st_pay IN ("
                        + SModSysConsts.FINS_ST_PAY_EXEC + ", "
                        + SModSysConsts.FINS_ST_PAY_EXEC_P + ")"
                        + "";
                break;
            case SModSysConsts.FINS_ST_PAY_RCPT:
            case SModSysConsts.FINX_ST_PAY_RCPT_DET:
                sql += (sql.isEmpty() ? "" : "AND ") + "v.fk_st_pay IN ("
                        + SModSysConsts.FINS_ST_PAY_RCPT + ", "
                        + SModSysConsts.FINS_ST_PAY_RCPT_P + ") ";
                break;
            default:
                sql += (sql.isEmpty() ? "" : "AND ") + "v.fk_st_pay = " + mnGridSubtype + " "; // just in case!
        }
        
        msSql = "SELECT "
                + "v.id_pay AS " + SDbConsts.FIELD_ID + "1, " 
                + "b.bp AS " + SDbConsts.FIELD_NAME + ", "  
                + "v.dt_app AS " + SDbConsts.FIELD_DATE + ", " 
                + "v.num AS " + SDbConsts.FIELD_CODE + ", "   
                + "v.dt_sched_n, "
                + "v.dt_exec_n, "
                + "v.pay_cur, "   
                + "c.cur_key, "   
                + "v.pay_exc_rate, "   
                + "v.pay, "   
                + "v.dt_req, " 
                + "v.nts, "   
                + "f.name AS func, "
                + "fs.name AS func_s, "
                + "sp.name AS status, "
                + (mbIsDetail 
                ? "ve.ety_pay_cur, "
                + "ve.ety_pay, "
                + "ve.conv_rate, "
                + "ve.des_pay_ety_cur, "
                + "ve.install, "
                + "ve.doc_bal_prev_cur, "
                + "ve.doc_bal_unpd_cur_r, "
                + "ce.cur_key AS ety_cur, "
                + "IF(ve.ety_tp = '" + SDbPaymentEntry.ENTRY_TYPE_ADVANCE + "' , 'PAGO ANTICIPO', 'PAGO A DOCUMENTO') AS ety_tp, "
                + "IF(d.num_ser = '', d.num, CONCAT(d.num_ser, '-', d.num)) AS dps, "
                : "")
                + "'MXN' AS mxn, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", " 
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "IF(fk_st_pay >= " + SModSysConsts.FINS_ST_PAY_REJC_P + ", " + SGridConsts.ICON_WAIT + ", " + SGridConsts.ICON_NULL + ")"
                + " AS proc, "
                + "v.ts_usr_sched, "
                + "v.ts_usr_exec, "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + ", "
                + "us.usr AS usr_sched, "
                + "us.usr AS usr_exec "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_PAY) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b ON " 
                + "v.fk_ben = b.id_bp " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS c ON " 
                + "v.fk_cur = c.id_cur " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_FUNC) + " AS f ON "
                + "v.fk_func = f.id_func "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_FUNC_SUB) + " AS fs ON "
                + "v.fk_func_sub = fs.id_func_sub "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FINS_ST_PAY) + " AS sp ON "
                + "v.fk_st_pay = sp.id_st_pay "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON " 
                + "v.fk_usr_ins = ui.id_usr " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON " 
                + "v.fk_usr_upd = uu.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS us ON " 
                + "v.fk_usr_sched = us.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ue ON " 
                + "v.fk_usr_exec = ue.id_usr "
                + (mbIsDetail 
                ? "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_PAY_ETY) + " AS ve ON "
                + "v.id_pay = ve.id_pay "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS ce ON " 
                + "ve.fk_ety_cur = ce.id_cur " 
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d ON " 
                + "ve.fk_doc_year_n = d.id_year AND ve.fk_doc_doc_n = d.id_doc " 
                : "")
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY v.num, v.dt_app, b.bp ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, SDbConsts.FIELD_CODE, "Folio"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, "Fecha"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, SDbConsts.FIELD_NAME, "Beneficiario"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt_sched_n", "Fecha programada"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt_exec_n", "Fecha ejecución"));
        SGridColumnView column = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "pay_cur", "Monto pago $");
        column.setSumApplying(true);
        gridColumnsViews.add(column);
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "cur_key", "Moneda"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_EXC_RATE, "pay_exc_rate", "Tipo cambio"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "pay", "Monto pago local $"));
        if (mbIsDetail) {
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "ety_tp", "Tipo pago"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "des_pay_ety_cur", "Monto a pagar $"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "ety_cur", "Moneda a pagar"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "dps", "Documento pago"));
        }
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "func", "Área funcional"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "func_s", "Subárea funcional"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "status", "Estatus"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_ICON, "proc", "En proceso"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, "Eliminado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "usr_sched", "Usr aut pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "ts_usr_sched", "Usr TS aut pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "usr_exec", "Usr efectua pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "ts_usr_exec", "Usr TS efectua pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS));
        
        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.FIN_PAY);
        moSuscriptionsSet.add(SModConsts.BPSU_BP);
        moSuscriptionsSet.add(SModConsts.CFGU_FUNC);
        moSuscriptionsSet.add(SModConsts.USRU_USR);     
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == jbExportDataToSwapServices) {
                actionExportDataToSwapServices();
            }
        }
    }
}
