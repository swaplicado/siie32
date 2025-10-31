/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.view;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.fin.db.SDbPaymentEntry;
import erp.mod.view.SViewFilter;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
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
import sa.lib.gui.SGuiParams;

/**
 *
 * @author Isabel Servín, Sergio Flores
 */
public class SViewPaymentStatus extends SGridPaneView implements ItemListener {
    
    public static final int DETAILED = 1;
    public static final int PENDING = 2;
    
    private JLabel jlDate;
    private JRadioButton jrbDateApp;
    private JRadioButton jrbDateExec;
    private ButtonGroup bgDate;
    
    private boolean mbAppliesFilterDatePeriod;
    private SGridFilterDatePeriod moFilterDatePeriod;
    private SViewFilter moFilterFuncArea;
    private SViewFilter moFilterCurrency;
    
    private boolean mbIsDetailed;
    private boolean mbIsPending;
    
    public SViewPaymentStatus(SGuiClient client, int subType, String title, SGuiParams params) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.FINX_PAY_ST, subType, title, params);
        initComponetsCustom();
    }
    
    private void initComponetsCustom() {
        mbIsDetailed = mnGridMode == DETAILED; // value processed by framework through SGuiParams in superclass
        mbIsPending = mnGridSubmode == PENDING; // value processed by framework through SGuiParams in superclass
        
        setRowButtonsEnabled(false);
        jtbFilterDeleted.setEnabled(false);
        
        jlDate = new JLabel("Fecha:");
        jrbDateApp = new JRadioButton("Solicitud");
        jrbDateApp.setToolTipText("Filtrar por la fecha de la solicitud de pago");
        jrbDateExec = new JRadioButton("Operación");
        jrbDateExec.setToolTipText("Filtrar por la fecha de operación del pago");
        
        bgDate = new ButtonGroup();
        bgDate.add(jrbDateApp);
        bgDate.add(jrbDateExec);
        bgDate.setSelected(jrbDateApp.getModel(), true);
        
        jrbDateApp.addItemListener(this);
        jrbDateExec.addItemListener(this);
        
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
        moFilterFuncArea = new SViewFilter(miClient, this, SModConsts.CFGU_FUNC);
        moFilterFuncArea.initFilter(null);
        moFilterCurrency = new SViewFilter(miClient, this, SModConsts.CFGU_CUR);
        moFilterCurrency.initFilter(null);
        
        if (mnGridSubtype == SModSysConsts.FINS_ST_PAY_EXEC || (mnGridSubtype == SModSysConsts.FINS_ST_PAY_EXEC && !mbIsPending)) {
            mbAppliesFilterDatePeriod = true;
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jlDate);
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jrbDateApp);
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jrbDateExec);
        }
        
        switch (mnGridSubtype) {
            case SModSysConsts.FINS_ST_PAY_IN_TREAS:
            case SModSysConsts.FINS_ST_PAY_EXEC:
            case SModSysConsts.FINS_ST_PAY_RCPT:
                break;
            default:
                throw new UnsupportedOperationException(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterFuncArea);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterCurrency);
    }
    
    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter;

        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);
        moPaneSettings.setSystemApplying(false);
        
        /* Code snippet preserved just for consistence, but it is never used in this view:
        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "NOT v.b_del ";
        }
        */
        sql += (sql.isEmpty() ? "" : "AND ") + "NOT v.b_del ";
        
        if (mbAppliesFilterDatePeriod) {
            filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
            if (jrbDateApp.isSelected()) {
                sql += (sql.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("v.dt_app", (SGuiDate) filter);
            }
            else {
                sql += (sql.isEmpty() ? "" : "AND ") + "((v.dt_exec_n IS NOT NULL AND " + SGridUtils.getSqlFilterDate("v.dt_exec_n", (SGuiDate) filter) + ") "
                        + "OR (v.dt_sched_n IS NOT NULL AND " + SGridUtils.getSqlFilterDate("v.dt_sched_n", (SGuiDate) filter) + ") "
                        + "OR " + SGridUtils.getSqlFilterDate("v.dt_req", (SGuiDate) filter) + ") ";
            }
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
            case SModSysConsts.FINS_ST_PAY_IN_TREAS:
                sql += (sql.isEmpty() ? "" : "AND ") + "v.fk_st_pay IN ("
                        + SModSysConsts.FINS_ST_PAY_IN_TREAS + ") ";
                break;
                
            case SModSysConsts.FINS_ST_PAY_EXEC:
                sql += (sql.isEmpty() ? "" : "AND ") + "v.fk_st_pay IN ("
                        + SModSysConsts.FINS_ST_PAY_EXEC + ", "
                        + SModSysConsts.FINS_ST_PAY_EXEC_P + ", "
                        + SModSysConsts.FINS_ST_PAY_RCPT + ", "
                        + SModSysConsts.FINS_ST_PAY_RCPT_P + ") ";
                break;
                
            case SModSysConsts.FINS_ST_PAY_RCPT:
                if (mbIsPending) {
                    sql += (sql.isEmpty() ? "" : "AND ") + "v.fk_st_pay IN ("
                            + SModSysConsts.FINS_ST_PAY_EXEC + ", "
                            + SModSysConsts.FINS_ST_PAY_EXEC_P + ") AND v.b_rcpt_pay_req ";
                }
                else {
                    sql += (sql.isEmpty() ? "" : "AND ") + "v.fk_st_pay IN ("
                            + SModSysConsts.FINS_ST_PAY_RCPT + ", "
                            + SModSysConsts.FINS_ST_PAY_RCPT_P + ") ";
                }
                break;
                
            default:
                sql += (sql.isEmpty() ? "" : "AND ") + "v.fk_st_pay = " + mnGridSubtype + " "; // just in case!
        }
        
        msSql = "SELECT "
                + "v.id_pay AS " + SDbConsts.FIELD_ID + "1, " 
                + "b.bp AS " + SDbConsts.FIELD_NAME + ", "  
                + "v.dt_app AS " + SDbConsts.FIELD_DATE + ", " 
                + "CONCAT(v.ser, IF(v.ser = '', '', '-'), v.num) AS " + SDbConsts.FIELD_CODE + ", "
                + "v.dt_sched_n, "
                + "v.dt_exec_n, "
                + "v.pay_cur, "   
                + "c.cur_key, "   
                + "v.pay_exc_rate, "   
                + "v.pay, "   
                + "v.dt_req, " 
                + "v.nts, "   
                + "f.name AS _func, "
                + "fs.name AS _func_sub, "
                + "sp.name AS _status, "
                + (mbIsDetailed 
                ? "ve.ety_pay_cur, "
                + "ve.ety_pay, "
                + "ve.conv_rate, "
                + "ve.des_pay_ety_cur, "
                + "ve.install, "
                + "ve.doc_bal_prev_cur, "
                + "ve.doc_bal_unpd_cur_r, "
                + "ce.cur_key AS _ety_cur_key, "
                + "IF(ve.ety_tp = '" + SDbPaymentEntry.ENTRY_TYPE_ADVANCE + "', '" + SDbPaymentEntry.DESC_ENTRY_TYPE_ADVANCE + "', '" + SDbPaymentEntry.DESC_ENTRY_TYPE_PAYMENT + "') AS _ety_tp, "
                + "CONCAT(d.num_ser, IF(d.num_ser = '', '', '-'), d.num) AS _dps, "
                : "")
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", " 
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "IF(fk_st_pay >= " + SModSysConsts.FINS_ST_PAY_REJC_P + ", " + SGridConsts.ICON_WAIT + ", " + SGridConsts.ICON_NULL + ") AS _ico_proc, "
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
                + (mbIsDetailed 
                ? "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_PAY_ETY) + " AS ve ON "
                + "v.id_pay = ve.id_pay "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS ce ON " 
                + "ve.fk_ety_cur = ce.id_cur " 
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d ON " 
                + "ve.fk_doc_year_n = d.id_year AND ve.fk_doc_doc_n = d.id_doc " 
                : "")
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY v.ser, LPAD(v.num, 9, '0'), " + (jrbDateApp.isSelected() ? "v.dt_app" : "COALESCE(dt_exec_n, COALESCE(dt_sched_n, dt_req))") + ", b.bp ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, SDbConsts.FIELD_CODE, "Folio solicitud"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, "Fecha solicitud"));
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, SDbConsts.FIELD_NAME, "Beneficiario pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt_sched_n", "Fecha programada pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt_exec_n", "Fecha operación pago"));
        SGridColumnView column = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "pay_cur", "Monto pago $");
        column.setSumApplying(true);
        gridColumnsViews.add(column);
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "cur_key", "Moneda pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_EXC_RATE, "pay_exc_rate", "TC pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "pay", "Monto pago moneda local $"));
        if (mbIsDetailed) {
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "_ety_tp", "Tipo pago"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "des_pay_ety_cur", "Monto a pagar $"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "_ety_cur_key", "Moneda a pagar"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "_dps", "Documento pago"));
        }
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "_func", "Área funcional"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "_func_sub", "Subárea funcional"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "_status", "Estatus"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_ICON, "_ico_proc", "En proceso"));
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
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof JRadioButton && e.getStateChange() == ItemEvent.SELECTED) {
            JRadioButton radioButton = (JRadioButton) e.getSource();
            
            if (radioButton == jrbDateApp || radioButton == jrbDateExec) {
                refreshGridWithRefresh();
            }
        }
    }
}
