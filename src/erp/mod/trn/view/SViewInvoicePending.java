/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view;

import erp.data.SDataConstantsSys;
import erp.gui.grid.SGridFilterPanelFunctionalArea;
import erp.mod.SModConsts;
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
public class SViewInvoicePending extends SGridPaneView {

    private Date mtDateStart;
    private Date mtDateFinal;
    private int mnYearId;
    private int mnBizPartberId;
    
    private SGridFilterPanelFunctionalArea moFilterFunctionalArea;
    
    public SViewInvoicePending(SGuiClient client, int gridType, int gridSubtype, String title, SGuiParams params) {
        super(client, SGridConsts.GRID_PANE_VIEW, gridType, gridSubtype, title, params);
        initComponentsCustom();
    }

    /*
     * Private methods
     */

    private void initComponentsCustom() {
        setRowButtonsEnabled(false);
        mtDateStart = null;
        mnBizPartberId = 0;
        createGridColumns();
        
        moFilterFunctionalArea = new SGridFilterPanelFunctionalArea(miClient, this, SFilterConstants.SETTING_FILTER_FUNC_AREA);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterFunctionalArea);
    }

    private void setParamsView(final Date dateStart, final Date dateFinal, final int year, final int idBizPartner ) {
        mtDateStart = dateStart;
        mtDateFinal = dateFinal;
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
        msSql= "SELECT d.id_year " + SDbConsts.FIELD_ID + "1, " +
                "d.id_doc " + SDbConsts.FIELD_ID + "2, " + 
                "'' AS " + SDbConsts.FIELD_CODE + ", " +
                "'' AS " + SDbConsts.FIELD_NAME + ", " + 
                "d.dt, d.dt_doc_delivery_n, d.num_ref, d.b_close, d.b_del, d.ts_close, CONCAT(d.num_ser, IF(length(d.num_ser) = 0, '', '-'), d.num) AS f_num, " +
                "IF(d.aut_authorn_rej = " + SDataConstantsSys.TRNS_ST_DPS_NEW + ", 'Tope evento usuario', IF(d.aut_authorn_rej = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + ", 'Tope mensual usuario', IF(d.aut_authorn_rej = " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + ", 'Tope mensual área funcional', 'No aplica'))) " +
                "AS _aut_auth_rej, d.ts_authorn, d.tot_cur_r, dt.code, c.cur_key, b.id_bp, b.bp, bc.bp_key, bb.id_bpb, bb.bpb, cb.code, sa.st_dps_authorn, ua.usr " +
                "FROM  " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d " +
                "INNER JOIN  " + SModConsts.TablesMap.get(SModConsts.TRNU_TP_DPS) + " AS dt ON d.fid_ct_dps = dt.id_ct_dps " + sql ;
                 if (mnGridMode == SDataConstantsSys.BPSS_CT_BP_CUS) {
                    msSql += "AND d.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_INV[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_INV[1] + " AND d.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_INV[2] + " ";
                }
                else {
                    msSql += "AND d.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_INV[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_INV[1] + " AND d.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_INV[2] + " ";
                }
                
                msSql +="INNER JOIN  " + SModConsts.TablesMap.get(SModConsts.TRNS_ST_DPS_AUTHORN) + " AS sa ON d.fid_st_dps_authorn = sa.id_st_dps_authorn " +
                "INNER JOIN  " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS c ON d.fid_cur = c.id_cur " +
                "INNER JOIN  " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b ON d.fid_bp_r = b.id_bp " +
                "INNER JOIN  " + SModConsts.TablesMap.get(SModConsts.BPSU_BP_CT) + " AS bc ON b.id_bp = bc.id_bp AND bc.id_ct_bp = " + mnGridMode + " " +
                "INNER JOIN  " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS cb ON d.fid_cob = cb.id_bpb " +
                "INNER JOIN  " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS bb ON d.fid_bpb = bb.id_bpb " +
                "INNER JOIN  " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ua ON d.fid_usr_authorn = ua.id_usr " +
                "INNER JOIN  " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS un ON d.fid_usr_new = un.id_usr WHERE d.b_del = 0 AND d.fid_st_dps_authorn IN (1, 2) AND d.id_year = " + mnYearId + " AND d.dt >= '" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "' AND d.dt <= '" + SLibUtils.DbmsDateFormatDate.format(mtDateFinal) + "'  AND d.fid_cob = + " + SDataConstantsSys.BPSS_TP_BPB_HQ + " AND d.fid_bp_r = " + mnBizPartberId + 
                " ORDER BY dt.code, d.num_ser, d.num, d.dt, b.bp, b.id_bp, bb.bpb, bb.id_bpb ";  
        }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        SGridColumnView column = null;
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<SGridColumnView>();
       
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "f_num", "Folio doc"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "num_ref", "Referencia"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt", "Fecha doc"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt_doc_delivery_n", "Entrega programada"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "tot_cur_r", "Total mon $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "st_dps_authorn", "Estatus"));
        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.CFGU_CUR);
        moSuscriptionsSet.add(SModConsts.BPSU_BP);
        moSuscriptionsSet.add(SModConsts.BPSU_BP_CT);
        moSuscriptionsSet.add(SModConsts.BPSU_BPB);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }
}