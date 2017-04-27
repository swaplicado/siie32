/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view;

import erp.data.SDataConstantsSys;
import erp.mod.SModConsts;
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
public class SViewInvoiceToSend extends SGridPaneView {
   
    private Date mtDateStart;
    private Date mtDateFinal;
    private int mnYearId;
    private int mnBizPartberId;
    
    public SViewInvoiceToSend(SGuiClient client, int gridType, int gridSubtype, String title, SGuiParams params) {
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
    }

    /*
     * Public methods
     */

    public void renderView(final Date dateStart, final Date dateFinal, final int year, final int idBizPartner ) {
        mtDateStart = dateStart;
        mtDateFinal = dateFinal;
        mnYearId = year;
        mnBizPartberId = idBizPartner;
        createGridColumns();
        populateGrid(SGridConsts.REFRESH_MODE_RELOAD);
    }
    
    /*
     * Overriden methods
     */
    
    @Override
    public void prepareSqlQuery() {
        String sqlDate = "'" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "' ";
        String sqlDateF = "'" + SLibUtils.DbmsDateFormatDate.format(mtDateFinal) + "' ";

        
        moPaneSettings = new SGridPaneSettings(2);
        msSql= "SELECT id_year " + SDbConsts.FIELD_ID + "1, " +
                "id_doc " + SDbConsts.FIELD_ID + "2, " + 
                "'' AS " + SDbConsts.FIELD_CODE + ", " +
                "'' AS " + SDbConsts.FIELD_NAME + ", " + 
                "dt, dt_doc_delivery_n, b_close, b_del, ts_close, num_ser, num, num_ref, CONCAT(num_ser, " +
                "IF(length(num_ser) = 0, '', '-'), num) AS f_num, ts_audit, tot_cur_r, code, cur_key, id_bp, bp, bp_key, id_bpb, " +
                "bpb, code_bpb, usr, f_count_snd FROM( " +
                "(SELECT d.id_year, d.id_doc, d.dt, d.dt_doc_delivery_n, d.b_close, d.b_del, d.ts_close, d.num_ser, d.num, d.num_ref, CONCAT(d.num_ser, " +
                "IF(length(d.num_ser) = 0, '', '-'), d.num) AS f_num, d.ts_audit, d.tot_cur_r, dt.code, c.cur_key, b.id_bp, b.bp, bc.bp_key, " +
                "bb.id_bpb, bb.bpb, cb.code AS code_bpb, ua.usr, " +
                "(SELECT COUNT(*) FROM " + SModConsts.TablesMap.get(SModConsts.TRN_CFD_SND_LOG) + "  WHERE id_cfd = x.id_cfd  ) AS f_count_snd FROM trn_dps AS d " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNU_TP_DPS) + "  AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps ";
                if (mnGridMode == SDataConstantsSys.BPSS_CT_BP_CUS) {
                    msSql += "AND d.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_INV[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_INV[1] + " AND d.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_INV[2] + " ";
                }
                else {
                    msSql += "AND d.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_INV[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_INV[1] + " AND d.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_INV[2] + " ";
                }
                
                msSql += "AND d.id_year = " + mnYearId + " AND d.dt >= " + sqlDate + " AND d.dt <= " + sqlDateF + " AND d.fid_cob = " + SDataConstantsSys.BPSS_TP_BPB_HQ + " AND d.fid_bp_r = " + mnBizPartberId +  
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
       
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "f_num", "Folio doc"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "num_ref", "Referencia"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt", "Fecha doc"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt_doc_delivery_n", "Entrega programada"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "tot_cur_r", "Total mon $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "cur_key", "Moneda "));
        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
    }
}