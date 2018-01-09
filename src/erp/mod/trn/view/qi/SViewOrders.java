/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view.qi;

import erp.data.SDataConstantsSys;
import erp.lib.table.STableConstants;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.util.ArrayList;
import java.util.Date;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
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
public class SViewOrders extends SGridPaneView {

    private Date mtDateStart;
    private Date mtDateFinal;
    private int mnYearId;
    private int mnBizPartherId;
    
    public SViewOrders(SGuiClient client, int gridType, int gridSubtype, String title, SGuiParams params) {
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
        
        moPaneSettings = new SGridPaneSettings(2);
        
         msSql = "SELECT d.id_year " + SDbConsts.FIELD_ID + "1, " +
                "d.id_doc " + SDbConsts.FIELD_ID + "2, " + 
                "'' AS " + SDbConsts.FIELD_CODE + ", " +
                "'' AS " + SDbConsts.FIELD_NAME + ", " + 
                "d.dt, d.exc_rate, d.stot_r, d.tax_charged_r, d.tax_retained_r, d.tot_r, d.stot_cur_r, d.tax_charged_cur_r, d.tax_retained_cur_r, d.tot_cur_r, d.ts_close, " +
                "CONCAT(d.num_ser, IF(length(d.num_ser) = " + SModSysConsts.FINS_CFD_TAX_NA + ", '', '-'), d.num) AS f_num, " +
                "(SELECT c.cur_key FROM erp.cfgu_cur AS c WHERE d.fid_cur = c.id_cur) AS f_cur_key, '" + miClient.getSession().getSessionCustom().getLocalCurrencyCode() + "' AS f_cur_key_local, " +
                "(SELECT cob.code FROM erp.bpsu_bpb AS cob WHERE d.fid_cob = cob.id_bpb) AS f_cob_code " + 
                "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d " + 
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp ON d.fid_bp_r = bp.id_bp " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP_CT) + " AS bpc ON bp.id_bp = bpc.id_bp AND bpc.id_ct_bp = " + mnGridMode  +  " " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS bpb ON d.fid_bpb = bpb.id_bpb " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNU_TP_DPS) + " AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps ";
                if (mnGridMode == SDataConstantsSys.BPSS_CT_BP_CUS) {
                    msSql += "AND d.fid_ct_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_ORD[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_ORD[1] + " ";
                }
                else {
                    msSql += "AND d.fid_ct_dps = " + SDataConstantsSys.TRNS_CL_DPS_PUR_ORD[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_PUR_ORD[1] + " ";
                }
                msSql += "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ul ON d.fid_usr_link = ul.id_usr " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uc ON d.fid_usr_close = uc.id_usr " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS un ON d.fid_usr_new = un.id_usr " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ue ON d.fid_usr_edit = ue.id_usr " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ud ON d.fid_usr_del = ud.id_usr " +
                "LEFT OUTER JOIN trn_cfd AS x ON d.id_year = x.fid_dps_year_n AND d.id_doc = x.fid_dps_doc_n WHERE NOT d.b_del " +
                "AND d.id_year = " + mnYearId + " AND d.dt >= '" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "' AND d.dt <= '" + SLibUtils.DbmsDateFormatDate.format(mtDateFinal) + "' AND  bp.id_bp = " + mnBizPartherId;
    }
          

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<SGridColumnView>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "code", "Tipo doc", STableConstants.WIDTH_CODE_DOC));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "f_num", "Folio doc", STableConstants.WIDTH_DOC_NUM));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "num_ref", "Refencia", STableConstants.WIDTH_DOC_NUM_REF));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt", "Fecha doc", STableConstants.WIDTH_DATE));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "f_rcb_code", "Sucursal", STableConstants.WIDTH_CODE_COB));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt_doc_delivery_n", "Entrega programada", STableConstants.WIDTH_DATE));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "stot_r", "Subtotal mon $", STableConstants.WIDTH_VALUE_2X));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "tax_charged_r", "Imp tras mon $", STableConstants.WIDTH_VALUE_2X));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "tot_r", "Total mon $", STableConstants.WIDTH_VALUE_2X));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "f_cur_key", "Moneda", STableConstants.WIDTH_CURRENCY_KEY));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "tot_cur_r", "Total", STableConstants.WIDTH_VALUE_2X));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "f_cur_key_local", "Moneda", STableConstants.WIDTH_CURRENCY_KEY));
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