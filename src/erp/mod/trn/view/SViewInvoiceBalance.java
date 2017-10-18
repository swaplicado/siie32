/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JButton;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiParams;

/**
 *
 * @author Claudio Peña
 */
public class SViewInvoiceBalance extends SGridPaneView {

    private JButton mjDpsView;
    private JButton mjDpsNotes;
    private JButton mjDpsLinks;
    private int mnYearId;
    private int mnBizPartberId;
    
    public SViewInvoiceBalance(SGuiClient client, int gridType, int gridSubtype, String title, SGuiParams params) {
        super(client, SGridConsts.GRID_PANE_VIEW, gridType, gridSubtype, title, params);
        initComponentsCustom();
    }

    /*
     * Private methods
     */

    private void initComponentsCustom() {
        setRowButtonsEnabled(false);
        mnBizPartberId = 0;
        createGridColumns();
    }
    
    private void setParamsView(final int year, final int idBizPartner ) {
        mnYearId = year;
        mnBizPartberId = idBizPartner;
    }
    
    private void renderView() {
        createGridColumns();
        populateGrid(SGridConsts.REFRESH_MODE_RELOAD);
    }

    /*
     * Public methods
     */
    
    public void initView(final int year, final int idBizPartner) {
        setParamsView(year, idBizPartner);
        renderView();
    }
    
    /*
     * Overriden methods
     */
    
    @Override
    public void prepareSqlQuery() {
        
        moPaneSettings = new SGridPaneSettings(2);
        
         msSql = "SELECT b.id_bp, b.bp, d.id_year, d.id_doc, d.num_ser, d.num, re.ref, " +
                "d.dt, d.dt_start_cred, d.days_cred, " +
                "(select name from erp.bpss_risk where id_risk = bpc.fid_risk_n) AS risk, " +
                "bpc.garnt, bpc.insur, bpc.b_garnt_prc, bpc.b_insur_prc, bpc.cred_lim, COALESCE(bpc.days_cred,0) AS days_credit, " +
                "ADDDATE(d.dt_start_cred, d.days_cred) AS _due_dt, " +
                "DATEDIFF( '" +mnYearId+ "-12-31" + "', ADDDATE(d.dt_start_cred, d.days_cred)) AS _days_past_due, " +
                "SUM(re.debit - re.credit) AS _bal, SUM(re.debit_cur - re.credit_cur) AS _bal_cur, c.id_cur, c.cur_key " +
                "FROM fin_rec AS r " +
                "INNER JOIN fin_rec_ety AS re ON r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num " +
                "INNER JOIN erp.cfgu_cur AS c ON re.fid_cur = c.id_cur " +
                "INNER JOIN erp.bpsu_bp AS b ON re.fid_bp_nr = b.id_bp " +
                "INNER JOIN erp.bpsu_bp_ct AS bpc ON b.id_bp = bpc.id_bp " +
                "INNER JOIN fin_acc AS a ON re.fk_acc = a.pk_acc     " +
                "INNER JOIN fin_acc AS al ON f_acc_std_ldg(a.code) = al.code " +
                "LEFT OUTER JOIN trn_dps AS d ON re.fid_dps_year_n = d.id_year AND re.fid_dps_doc_n = d.id_doc " +
                "LEFT OUTER JOIN erp.usru_usr AS us ON (bpc.fid_usr_ana_n = us.id_usr) " +
                "LEFT OUTER JOIN mkt_cfg_cus AS mcc ON (re.fid_bp_nr = mcc.id_cus) " +
                "WHERE " +
                "bpc.fid_risk_n IS NOT NULL AND bpc.id_ct_bp = " + SModSysConsts.FINS_TP_ACC_BAL + " AND " + // checar esa SModSysConsts.FINS_TP_ACC_BAL es el BIz PArther de negocios
                "r.b_del = " + SModSysConsts.TRNX_DIOG_CST_TRAN_NA + " AND re.b_del = " + SModSysConsts.TRNX_DIOG_CST_TRAN_NA + " AND r.id_year = '" + mnYearId + "' AND r.dt <=  '" +mnYearId+ "-12-31" + "' AND " +
                "re.fid_cl_sys_acc = " + SModSysConsts.FINS_CL_SYS_MOV_GI + " AND al.fid_tp_acc_r = " +SModSysConsts.FINS_TP_ACC_BAL + " " +//$P{nSysAccountClassId}/*21*/ 
                "AND (d.fid_cob = 1 OR (re.fid_dps_year_n IS NULL AND r.fid_cob = 1)) " +
                "AND re.fid_bp_nr = " + mnBizPartberId + " " +
                "GROUP BY b.id_bp, b.bp, d.id_year, d.id_doc, d.num_ser, d.num, re.ref, " +
                "d.dt, d.dt_start_cred, d.days_cred, " +
                "c.id_cur, c.cur_key " +
                "HAVING SUM(re.debit - re.credit) <> 0 OR SUM(re.debit_cur - re.credit_cur) <> 0 " +
                "ORDER BY b.bp, b.id_bp, _due_dt, d.num_ser, d.num, d.id_year, d.id_doc, re.ref "; 
    }
          

    @Override 
    
    public ArrayList<SGridColumnView> createGridColumns() {
        
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<SGridColumnView>();
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "d.num", "prueba5"));

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "d.num", "Factura"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "num_ser", "Factura"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "ref", "Factura"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_TIME, "d.dt", "Base CR."));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "_due_dt", "Vencimiento"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "d.days_cred", "Dias crédito"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "_days_past_due", "Mora"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "_bal", "Saldo Total"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, " c.cur_key", "Moneda"));

        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.TRN_DPS);
        moSuscriptionsSet.add(SModConsts.BPSU_BP);
        moSuscriptionsSet.add(SModConsts.BPSU_BP_CT);
        moSuscriptionsSet.add(SModConsts.BPSU_BPB);
        moSuscriptionsSet.add(SModConsts.TRNU_TP_DPS);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }
}