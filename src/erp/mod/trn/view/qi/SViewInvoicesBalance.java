/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view.qi;

import erp.data.SDataConstantsSys;
import erp.gui.grid.SGridFilterPanelFunctionalArea;
import erp.lib.table.STableConstants;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.table.SFilterConstants;
import java.util.ArrayList;
import java.util.Date;
import sa.lib.SLibUtils;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiParams;

/**
 *
 * @author Claudio Peña
 */
public class SViewInvoicesBalance extends SGridPaneView {

    private int mnBizPartherId;
    private int mnYearId;
    private Date mtDateStart;
    private Date mtDateFinal;
    
    private SGridFilterPanelFunctionalArea moFilterFunctionalArea;
    
    public SViewInvoicesBalance(SGuiClient client, int gridType, int gridSubtype, String title, SGuiParams params) {
        super(client, SGridConsts.GRID_PANE_VIEW, gridType, gridSubtype, title, params);
        initComponentsCustom();
    }

    /*
     * Private methods
     */

   private void initComponentsCustom() {
        setRowButtonsEnabled(false);
        mtDateStart = null;
        mtDateFinal = null;
        mnBizPartherId = 0;
        createGridColumns();
        
        moFilterFunctionalArea = new SGridFilterPanelFunctionalArea(miClient, this, SFilterConstants.SETTING_FILTER_FUNC_AREA);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterFunctionalArea);
    }
    
     private void setParamsView(final Date dateStart, final Date dateFinal, final int idBizPartner, final int year ) {
        mtDateStart = dateStart;
        mtDateFinal = dateFinal;
        mnYearId = year;
        mnBizPartherId = idBizPartner;
    }
    
    private void renderView() {
        createGridColumns();
        populateGrid(SGridConsts.REFRESH_MODE_RELOAD);
    }

    /*
     * Public methods
     */
    
    public void initView(final Date dateStart, final Date DateEnd, final int idBizPartner, final int year) {
        setParamsView(dateStart, DateEnd, year, idBizPartner);
        renderView();
    }
    
    /*
     * Overriden methods
     */
    
    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;
        
        filter = (String) (moFiltersMap.get(SFilterConstants.SETTING_FILTER_FUNC_AREA) == null ? null : moFiltersMap.get(SFilterConstants.SETTING_FILTER_FUNC_AREA).getValue());
        if (filter != null) {
            sql += "AND d.fid_func IN ( " + filter + ") ";
        }
        
        moPaneSettings = new SGridPaneSettings(2);
        /* Query 1. Moves without document: */
        msSql = "SELECT '' AS f_id_1, '' AS f_id_2, '' AS f_code, '' AS f_name, '' AS f_cur_key, 'MXN' AS MXN, b.id_bp, b.bp, NULL AS id_year, NULL AS id_doc, NULL AS dt, NULL AS f_doc_code, " +
                "NULL AS f_num, NULL AS tot_r, NULL AS exc_rate, NULL AS tot_cur_r, c.id_cur, c.cur_key, NULL AS f_cob_code, " +
                "SUM(re.debit - re.credit) AS f_bal, " +
                "SUM(re.debit_cur - re.credit_cur) AS f_bal_cur, btp.id_tp_bp, btp.tp_bp " +
                "FROM fin_rec AS r INNER JOIN fin_rec_ety AS re ON " +
                "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num " +
                "AND r.id_year = " + mnYearId + " " +
                "AND r.dt <= '" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "' AND r.dt <= '" + SLibUtils.DbmsDateFormatDate.format(mtDateFinal) + "' AND NOT re.b_del " +
                "AND re.fid_ct_sys_mov_xxx = " + SModSysConsts.FINS_TP_TAX_CAL_AMT + " AND " ; 
                 if (mnGridMode == SDataConstantsSys.BPSS_CT_BP_CUS) {
                    msSql += "re.fid_tp_sys_mov_xxx = " + SDataConstantsSys.BPSS_CT_BP_CUS + " ";
                }
                else {
                    msSql += "re.fid_tp_sys_mov_xxx = " + SDataConstantsSys.BPSS_CT_BP_SUP  + " ";
                }
                msSql += "AND re.fid_bp_nr = " + mnBizPartherId + " " +
                "AND re.fid_dps_year_n IS NULL AND re.fid_dps_doc_n IS NULL " +
                "LEFT OUTER JOIN erp.bpsu_bp AS b ON re.fid_bp_nr = b.id_bp " +
                "LEFT OUTER JOIN erp.bpsu_bp_ct AS bct ON re.fid_bp_nr = bct.id_bp AND bct.id_ct_bp = re.fid_tp_sys_mov_xxx " +
                "LEFT OUTER JOIN erp.bpsu_tp_bp AS btp ON bct.fid_tp_bp = btp.id_tp_bp AND btp.id_ct_bp = re.fid_tp_sys_mov_xxx " +
                "LEFT OUTER JOIN erp.cfgu_cur AS c ON re.fid_cur = c.id_cur " +
                "GROUP BY btp.id_tp_bp, b.id_bp, b.bp, c.id_cur, c.cur_key " +
                "HAVING f_bal <> " + SModSysConsts.FINS_CFD_TAX_NA + " OR f_bal_cur <> " + SModSysConsts.FINS_CFD_TAX_NA + " " +

                /* Query 2. Moves with document: */
                "UNION " +
                "SELECT '' AS f_id_1, '' AS f_id_2, '' AS f_code, '' AS f_name, '' AS f_cur_key, 'MXN' AS MXN, b.id_bp, b.bp, d.id_year, d.id_doc, d.dt, dt.code AS f_doc_code, " +
                "CONCAT(d.num_ser, IF(length(d.num_ser) = " + SModSysConsts.FINS_CFD_TAX_NA + ", '', '-'), d.num) AS f_num, " +
                "d.tot_r, d.exc_rate, d.tot_cur_r, c.id_cur, c.cur_key, cob.code AS f_cob_code, " +
                "SUM(re.debit - re.credit) AS f_bal, " +
                "SUM(IF(d.fid_cur IS NULL OR d.fid_cur <> re.fid_cur, " + SModSysConsts.FINS_CFD_TAX_NA + ", re.debit_cur - re.credit_cur)) AS f_bal_cur, btp.id_tp_bp, btp.tp_bp " +
                "FROM fin_rec AS r INNER JOIN fin_rec_ety AS re ON " +
                "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num " +
                "AND r.id_year = " + mnYearId + " " +
                "AND r.dt >= '" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "' AND r.dt <= '" + SLibUtils.DbmsDateFormatDate.format(mtDateFinal) + "' AND NOT re.b_del " +
                "AND re.fid_ct_sys_mov_xxx = " + SModSysConsts.FINS_TP_TAX_CAL_AMT + " AND " ;             
                 if (mnGridMode == SDataConstantsSys.BPSS_CT_BP_CUS) {
                    msSql += "re.fid_tp_sys_mov_xxx = " + SDataConstantsSys.BPSS_CT_BP_CUS + " ";
                }
                else {
                    msSql += "re.fid_tp_sys_mov_xxx = " + SDataConstantsSys.BPSS_CT_BP_SUP  + " ";
                }
                msSql += "AND re.fid_bp_nr = " + mnBizPartherId + " " +
                "INNER JOIN erp.bpsu_bp AS b ON re.fid_bp_nr = b.id_bp " +
                "INNER JOIN erp.bpsu_bp_ct AS bct ON re.fid_bp_nr = bct.id_bp AND bct.id_ct_bp = re.fid_tp_sys_mov_xxx " +
                "INNER JOIN erp.bpsu_tp_bp AS btp ON bct.fid_tp_bp = btp.id_tp_bp AND btp.id_ct_bp = re.fid_tp_sys_mov_xxx " +
                "INNER JOIN trn_dps AS d ON re.fid_dps_year_n = d.id_year AND re.fid_dps_doc_n = d.id_doc AND d.b_del = " + SModSysConsts.FINS_CFD_TAX_NA + " AND d.fid_st_dps = 2 " + sql +
                "INNER JOIN erp.trnu_tp_dps AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps " +
                "INNER JOIN erp.cfgu_cur AS c ON d.fid_cur = c.id_cur " +
                "INNER JOIN erp.bpsu_bpb AS cob ON d.fid_cob = cob.id_bpb " +
                "GROUP BY btp.id_tp_bp, b.id_bp, b.bp, d.id_year, d.id_doc, d.dt, dt.code, d.num_ser, d.num, " +
                "d.tot_r, d.exc_rate, d.tot_cur_r, c.id_cur, c.cur_key, cob.code " +
                "HAVING f_bal <> " + SModSysConsts.FINS_CFD_TAX_NA + " OR f_bal_cur <> " + SModSysConsts.FINS_CFD_TAX_NA + " " +
                "ORDER BY tp_bp, id_tp_bp, bp, id_bp, f_num, dt, id_year, id_doc, id_cur " ;
    }
          

    @Override 
    
    public ArrayList<SGridColumnView> createGridColumns() {
        
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<SGridColumnView>();
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "f_num", "Folio doc", STableConstants.WIDTH_CODE_DOC));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "f_doc_code", "Tipo", STableConstants.WIDTH_DOC_NUM));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt", "Fecha", STableConstants.WIDTH_DATE));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "f_cob_code", "Sucursal", STableConstants.WIDTH_CODE_COB));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "tot_cur_r", "Total mon. $", STableConstants.WIDTH_VALUE_2X));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "cur_key", "Mon", STableConstants.WIDTH_CURRENCY_KEY));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "exc_rate", "T. Cambio", STableConstants.WIDTH_VALUE_2X));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "tot_r", "Total $", STableConstants.WIDTH_VALUE_2X));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "MXN", "ML", STableConstants.WIDTH_CURRENCY_KEY));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "tot_cur_r", "Saldo mon $", STableConstants.WIDTH_VALUE_2X));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "cur_key", "Mon $", STableConstants.WIDTH_CURRENCY_KEY));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "tot_r", "Saldo $", STableConstants.WIDTH_VALUE_2X));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "MXN", "ML", STableConstants.WIDTH_CURRENCY_KEY));

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