/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.view;

import erp.data.SDataConstantsSys;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.view.SViewFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
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
 * @author Claudio Pela
 */
public class SViewReportPayment extends SGridPaneView implements ActionListener, ItemListener {
        
    private JLabel jlDate;
    private JRadioButton jrbDateApp;
    private JRadioButton jrbDateReq;
    private ButtonGroup bgDate;
    
    private boolean mbAppliesFilterDatePeriod;
    private SGridFilterDatePeriod moFilterDatePeriod;
    private SViewFilter moFilterFuncArea;
    private SViewFilter moFilterCurrency;
    
    private boolean mbAppliesDateRequired;
    
    public SViewReportPayment(SGuiClient client, int subType, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.FIN_PAY_REP, subType, title);
        initComponetsCustom();
    }
    
    private void initComponetsCustom() {
        if (mnGridSubtype == SLibConsts.UNDEFINED) {
            setRowButtonsEnabled(false, false, false, false, false);
            jtbFilterDeleted.setEnabled(true);
        }
        else {
            setRowButtonsEnabled(false);
            jtbFilterDeleted.setEnabled(false);
        }
 
        jlDate = new JLabel("Fecha:");
        jrbDateApp = new JRadioButton("Solicitud");
        jrbDateApp.setToolTipText("Filtrar por la fecha de la solicitud de pago");

        mbAppliesDateRequired = mnGridSubtype == SLibConsts.UNDEFINED || mnGridSubtype == SModSysConsts.FINS_ST_PAY_REJC;

        if (mbAppliesDateRequired) {
            jrbDateReq = new JRadioButton("Requerida");
            jrbDateReq.setToolTipText("Filtrar por la fecha requerida de pago");
        }
        else {
            jrbDateReq = new JRadioButton("Programada");
            jrbDateReq.setToolTipText("Filtrar por la fecha programada de pago");
        }

        bgDate = new ButtonGroup();
        bgDate.add(jrbDateApp);
        bgDate.add(jrbDateReq);

        jrbDateReq.setSelected(true);

        jrbDateApp.setEnabled(false);
        jrbDateReq.setEnabled(false);

        jrbDateApp.addItemListener(this);
        jrbDateReq.addItemListener(this);
        
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
        moFilterFuncArea = new SViewFilter(miClient, this, SModConsts.CFGU_FUNC);
        moFilterFuncArea.initFilter(null);
        moFilterCurrency = new SViewFilter(miClient, this, SModConsts.CFGU_CUR);
        moFilterCurrency.initFilter(null);
        
        if (SLibUtils.belongsTo(mnGridSubtype, new int[] { SLibConsts.UNDEFINED, SModSysConsts.FINS_ST_PAY_REJC, SModSysConsts.FINS_ST_PAY_SCHED, SModSysConsts.FINS_ST_PAY_CANC })) {
            mbAppliesFilterDatePeriod = true;
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jlDate);
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jrbDateApp);
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jrbDateReq);
        }
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterFuncArea);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterCurrency);
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(new JLabel());
        
    }
    
    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter;

        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setUserInsertApplying(false);
        moPaneSettings.setUserUpdateApplying(false);
        moPaneSettings.setSystemApplying(false);
        
        if (mnGridSubtype == SLibConsts.UNDEFINED) {
            filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
            if ((Boolean) filter) {
                sql += (sql.isEmpty() ? "" : "AND ") + "NOT v.b_del ";
            }
        }
        else {
            sql += (sql.isEmpty() ? "" : "AND ") + "NOT v.b_del ";
        }
        
        if (mbAppliesFilterDatePeriod) {
            filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
            if (jrbDateApp.isSelected()) {
                sql += (sql.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("v.dt_app", (SGuiDate) filter);
            }
            else {
                if (mbAppliesDateRequired) {
                    sql += (sql.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("v.dt_req", (SGuiDate) filter);
                }
                else {
                    sql += (sql.isEmpty() ? "" : "AND ") + "(v.dt_sched_n IS NOT NULL AND " + SGridUtils.getSqlFilterDate("v.dt_sched_n", (SGuiDate) filter) + ") ";
                }
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
        setShowSums(filter != null && ((String) filter).split(",").length == 1); // only if a single currency is selected!
        
        switch (mnGridSubtype) {
            case SLibConsts.UNDEFINED: // payments management
                sql += (sql.isEmpty() ? "" : "AND ") + "v.fk_st_pay IN ("
                + SModSysConsts.FINS_ST_PAY_NEW + ", " 
                + SModSysConsts.FINS_ST_PAY_IN_AUTH + ", " 
                + SModSysConsts.FINS_ST_PAY_REJC + ", " 
                + SModSysConsts.FINS_ST_PAY_REJC_P + ", "
                + SModSysConsts.FINS_ST_PAY_SCHED + ", " 
                + SModSysConsts.FINS_ST_PAY_SCHED_P + ", " 
                + SModSysConsts.FINS_ST_PAY_SUBR + ", "
                + SModSysConsts.FINS_ST_PAY_SUBR_P + ", "
                + SModSysConsts.FINS_ST_PAY_BLOC + ", "
                + SModSysConsts.FINS_ST_PAY_BLOC_P + ", "
                + SModSysConsts.FINS_ST_PAY_CANC + ", "
                + SModSysConsts.FINS_ST_PAY_CANC_P + ") ";
                break;
                
            case SModSysConsts.FINS_ST_PAY_IN_AUTH:
                sql += (sql.isEmpty() ? "" : "AND ") + "v.fk_st_pay IN ("
                        + SModSysConsts.FINS_ST_PAY_IN_AUTH + ") ";
                break;
                
            case SModSysConsts.FINS_ST_PAY_REJC:
                sql += (sql.isEmpty() ? "" : "AND ") + "v.fk_st_pay IN ("
                        + SModSysConsts.FINS_ST_PAY_REJC + ", "
                        + SModSysConsts.FINS_ST_PAY_REJC_P + ") ";
                break;
                
            case SModSysConsts.FINS_ST_PAY_SCHED:
                sql += (sql.isEmpty() ? "" : "AND ") + "v.fk_st_pay IN ("
                        + SModSysConsts.FINS_ST_PAY_SCHED + ", "
                        + SModSysConsts.FINS_ST_PAY_SCHED_P + ") ";
                break;
                
            case SModSysConsts.FINS_ST_PAY_BLOC:
                sql += (sql.isEmpty() ? "" : "AND ") + "v.fk_st_pay IN ("
                        + SModSysConsts.FINS_ST_PAY_BLOC + ", "
                        + SModSysConsts.FINS_ST_PAY_BLOC_P + ") ";
                break;
                
            case SModSysConsts.FINS_ST_PAY_CANC:
                sql += (sql.isEmpty() ? "" : "AND ") + "v.fk_st_pay IN ("
                        + SModSysConsts.FINS_ST_PAY_CANC + ", "
                        + SModSysConsts.FINS_ST_PAY_CANC_P + ") ";
                break;
                
            default:
                // nothing
        }
                
        msSql = "SELECT "
            + "v.id_pay AS " + SDbConsts.FIELD_ID + "1, "
            + "b.bp AS " + SDbConsts.FIELD_NAME + ", "
            + "v.dt_req AS FechaRequeridaPago, "
            + "ve.ety_tp, "
            + "v.b_sys, v.b_del, v.fk_usr_ins, v.fk_usr_upd, "
            + "v.fk_usr_ins AS f_usr_ins, v.fk_usr_upd AS f_usr_upd, "
            + "CASE "
            + "WHEN ve.ety_tp = 'P' THEN 'Pago a documento' "
            + "ELSE 'Anticipo' "
            + "END AS tipoPago, "
            + "ce.cur_key AS Moneda, "
            + "SUM(ve.des_pay_app_ety_cur) AS montoTotal, "
            + "COUNT(*) AS numeroPagos, "
            + "CASE "
            + "WHEN ve.ety_tp = 'A' THEN 'ANTICIPO' "
            + "ELSE COALESCE( ( "
            + "SELECT SUBSTRING_INDEX( "
            + "GROUP_CONCAT(e.concept ORDER BY ve.des_pay_app_ety_cur DESC), ',', 1 ) "
            + "FROM trn_dps_ety e "
            + "WHERE e.id_year = ve.fk_doc_year_n "
            + "AND e.id_doc = ve.fk_doc_doc_n "
            + "), 'ANTICIPO') "
            + "END AS conceptoPrincipal, "
            + "GROUP_CONCAT(DISTINCT CONCAT(v.ser, IF(v.ser = '', '', '-'), v.num) "
            + "ORDER BY v.ser, v.num SEPARATOR ', ') AS f_code, "
            + "COALESCE(MAX(nat.dps_nat), 'S/DOC') AS nat "
            + "FROM fin_pay AS v "
            + "INNER JOIN fin_pay_ety AS ve ON v.id_pay = ve.id_pay "
            + "INNER JOIN erp.bpsu_bp AS b ON v.fk_ben = b.id_bp "
            + "INNER JOIN erp.cfgu_cur AS ce ON ve.fk_ety_cur = ce.id_cur "
            + "LEFT JOIN trn_dps AS d ON d.id_doc = ve.fk_doc_doc_n AND d.id_year = ve.fk_doc_year_n "
            + "LEFT JOIN erp.TRNU_DPS_NAT nat ON d.fid_dps_nat = nat.id_dps_nat "
            + "LEFT JOIN trn_dps_ety AS e ON e.id_year = ve.fk_doc_year_n AND e.id_doc = ve.fk_doc_doc_n "
            + (sql.isEmpty() ? "" : "WHERE " + sql)
            + "GROUP BY "
            + "b.bp, v.dt_req, ve.ety_tp, ce.cur_key "
            + "ORDER BY "
            + "b.bp, ce.cur_key, "
            + "CASE "
            + "WHEN ve.ety_tp = 'P' THEN 1 "
            + "WHEN ve.ety_tp = 'A' THEN 2 "
            + "ELSE 3 "
            + "END";
        
    }

        
    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "tipoPago", "Tipo pago", 100));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, SDbConsts.FIELD_NAME, "Beneficiario pago", 250));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "numeroPagos", "No. de pagos", 50));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "f_code", "Solicitud(des) de pago", 150));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "montoTotal", "Monto a pagar $", 100));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "Moneda", "Moneda a pagar", 50));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "FechaRequeridaPago", "Fecha requerida pago", 70));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "conceptoPrincipal", "Concepto mayor", 500));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "nat", "Naturaleza doc", 100));
       

        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.FIN_PAY_ETY);
        moSuscriptionsSet.add(SModConsts.BPSU_BP);
        moSuscriptionsSet.add(SModConsts.CFGU_FUNC);
        moSuscriptionsSet.add(SModConsts.USRU_USR);     
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
         
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof JRadioButton && e.getStateChange() == ItemEvent.SELECTED) {
            JRadioButton radioButton = (JRadioButton) e.getSource();
            
            if (radioButton == jrbDateApp || radioButton == jrbDateReq) {
                refreshGridWithRefresh();
            }
        }
    }
}
