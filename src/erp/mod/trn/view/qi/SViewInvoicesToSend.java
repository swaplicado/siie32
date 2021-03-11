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
import sa.lib.db.SDbConsts;
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
public class SViewInvoicesToSend extends SGridPaneView {
   
    private Date mtDateStart;
    private Date mtDateFinal;
    private int mnYearId;
    private int mnBizPartherId;
    
    private SGridFilterPanelFunctionalArea moFilterFunctionalArea;
    
    public SViewInvoicesToSend(SGuiClient client, int gridType, int gridSubtype, String title, SGuiParams params) {
        super(client, SGridConsts.GRID_PANE_VIEW, gridType, gridSubtype, title, params);
        initComponentsCustom();
    }

    /*
     * Private methods
     */

    private void initComponentsCustom() {
        setRowButtonsEnabled(false);
        mtDateStart = null;
        mnBizPartherId = 0;
        createGridColumns();
        
        moFilterFunctionalArea = new SGridFilterPanelFunctionalArea(miClient, this, SFilterConstants.SETTING_FILTER_FUNC_AREA);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterFunctionalArea);
    }

     private void setParamsView(final Date dateStart, final Date dateFinal, final int year, final int idBizPartner ) {
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
    
    public void initView(final Date dateStart, final Date dateFinal, final int year, final int idBizPartner) {
        setParamsView(dateStart, dateFinal, year, idBizPartner);
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
        msSql= "SELECT id_year " + SDbConsts.FIELD_ID + "1, " +
                "id_doc " + SDbConsts.FIELD_ID + "2, " + 
                "'' AS " + SDbConsts.FIELD_CODE + ", " +
                "'' AS " + SDbConsts.FIELD_NAME + ", " + 
                "dt, dt_doc_delivery_n, b_close, b_del, ts_close, num_ser, num, num_ref, CONCAT(num_ser, " +
                "IF(length(num_ser) = " + SModSysConsts.FINS_CFD_TAX_NA + ", '', '-'), num) AS f_num, ts_audit, tot_cur_r, code, cur_key, id_bp, bp, bp_key, id_bpb, " +
                "bpb, code_bpb, usr, f_count_snd FROM( " +
                "(SELECT d.id_year, d.id_doc, d.dt, d.dt_doc_delivery_n, d.b_close, d.b_del, d.ts_close, d.num_ser, d.num, d.num_ref, CONCAT(d.num_ser, " +
                "IF(length(d.num_ser) = " + SModSysConsts.FINS_CFD_TAX_NA + ", '', '-'), d.num) AS f_num, d.ts_audit, d.tot_cur_r, dt.code, c.cur_key, b.id_bp, b.bp, bc.bp_key, " +
                "bb.id_bpb, bb.bpb, cb.code AS code_bpb, ua.usr, " +
                "(SELECT COUNT(*) FROM " + SModConsts.TablesMap.get(SModConsts.TRN_CFD_SND_LOG) + "  WHERE id_cfd = x.id_cfd  ) AS f_count_snd FROM trn_dps AS d " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNU_TP_DPS) + "  AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps ";
                if (mnGridMode == SDataConstantsSys.BPSS_CT_BP_CUS) {
                    msSql += "AND d.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_INV[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_INV[1] + " AND d.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_INV[2] + " ";
                }
                else {
                    msSql += "AND d.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_INV[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_INV[1] + " AND d.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_INV[2] + " ";
                }
                
                msSql += "AND d.id_year = " + mnYearId + " AND d.dt >= '" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "' AND d.dt <= '" + SLibUtils.DbmsDateFormatDate.format(mtDateFinal) + "' " +
                "AND d.fid_cob = " + SDataConstantsSys.BPSS_TP_BPB_HQ + " AND d.fid_bp_r = " + mnBizPartherId + sql +
                " INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS c ON d.fid_cur = c.id_cur INNER JOIN erp.bpsu_bp AS b ON d.fid_bp_r = b.id_bp " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP_CT) + " AS bc ON b.id_bp = bc.id_bp AND bc.id_ct_bp = " + mnGridMode + " " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS cb ON d.fid_cob = cb.id_bpb " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS bb ON d.fid_bpb = bb.id_bpb " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ua ON d.fid_usr_audit = ua.id_usr " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_CFD) + "  AS x ON d.id_year = x.fid_dps_year_n AND d.id_doc = x.fid_dps_doc_n AND x.fid_st_xml = 2 WHERE d.b_del = 0 " +
                    "AND d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " HAVING f_count_snd = 0 " +
                "ORDER BY dt.code, d.num_ser, CAST(d.num AS UNSIGNED INTEGER), d.num, d.dt, b.bp, bc.bp_key, b.id_bp, bb.bpb, bb.id_bpb) ) AS t ";  
        }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        SGridColumnView column = null;
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<SGridColumnView>();
       
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "f_num", "Folio doc", STableConstants.WIDTH_CODE_DOC));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "num_ref", "Referencia", STableConstants.WIDTH_DOC_NUM));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt", "Fecha doc", STableConstants.WIDTH_DATE));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt_doc_delivery_n", "Entrega programada", STableConstants.WIDTH_DATE));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "tot_cur_r", "Total mon $", STableConstants.WIDTH_VALUE_2X));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "cur_key", "Moneda ", STableConstants.WIDTH_CURRENCY_KEY));
        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.CFGU_CUR);
        moSuscriptionsSet.add(SModConsts.BPSU_BP_CT);
        moSuscriptionsSet.add(SModConsts.BPSU_BPB);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
        moSuscriptionsSet.add(SModConsts.TRN_CFD);
    }
}