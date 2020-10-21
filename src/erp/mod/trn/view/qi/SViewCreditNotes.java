/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view.qi;

import erp.SClientUtils;
import erp.client.SClientInterface;
import erp.lib.table.STableConstants;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.util.ArrayList;
import java.util.Date;
import sa.lib.SLibUtils;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiParams;

/**
 *
 * @author Claudio Peña, Isabel Servín
 */
public class SViewCreditNotes extends SGridPaneView {

    private Date mtDateStart;
    private Date mtDateFinal;
    private int mnYearId;
    private int mnBizPartherId;
    
    public SViewCreditNotes(SGuiClient client, int gridType, int gridSubtype, String title, SGuiParams params) {
        super(client, SGridConsts.GRID_PANE_VIEW, gridType, gridSubtype, title, params);
        initComponentsCustom();
    }

    /*
     * Private methods
     */

    private void initComponentsCustom() {
        setRowButtonsEnabled(false);
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
        String complementaryDbName = "";
        
        try {
            complementaryDbName = SClientUtils.getComplementaryDdName((SClientInterface) miClient);
        }
        catch (Exception e) {
            SLibUtils.printException(this, e);
        }
            
        moPaneSettings = new SGridPaneSettings(2);
        msSql = "SELECT d.id_year AS f_id_1, d.id_doc AS f_id_2, d.num_ref AS f_code, bp AS f_name, d.id_year, d.id_doc, d.dt, d.dt_doc_delivery_n, d.num_ref, d.comms_ref, d.exc_rate, d.stot_r, d.tax_charged_r, d.tax_retained_r, d.tot_r, d.stot_cur_r, d.tax_charged_cur_r, " +
        "d.tax_retained_cur_r, d.tot_cur_r, d.b_copy, d.b_link, d.b_close, d.b_audit, d.b_del, d.ts_link, d.ts_close, d.ts_new, d.ts_edit, d.ts_del, dt.code, " +
        "(SELECT fa.code FROM cfgu_func AS fa WHERE d.fid_func = fa.id_func) AS f_fa_code, " +
        "(SELECT dn.code FROM erp.trnu_dps_nat AS dn WHERE d.fid_dps_nat = dn.id_dps_nat) AS f_dn_code, CONCAT(d.num_ser, IF(length(d.num_ser) = " + SModSysConsts.FINS_CFD_TAX_NA + ", '', '-'), d.num) AS f_num, " +
        "(SELECT CONCAT(src.num_ser, IF(length(src.num_ser) = " + SModSysConsts.FINS_CFD_TAX_NA + ", '', '-'), src.num) AS id_ped " +
        "FROM trn_dps AS src " +
        "INNER JOIN trn_dps_dps_supply AS spl ON src.id_doc = spl.id_src_doc AND src.id_year = spl.id_src_year " +
        "WHERE spl.id_des_doc = d.id_doc AND src.id_year = d.id_year AND src.b_del = " + SModSysConsts.FINS_CFD_TAX_NA + " LIMIT " + SModSysConsts.FINS_TP_ACC_NA + ") AS f_ord_num, " +
        "(SELECT CONCAT(ord.id_year, '-', ord.num) FROM mfg_ord AS ord WHERE d.fid_mfg_year_n = ord.id_year AND d.fid_mfg_ord_n = ord.id_ord) AS num_ord, " +
        "IF(d.fid_st_dps = " + SModSysConsts.FINS_TP_ACC_RES + ", " + STableConstants.ICON_ST_ANNUL + ", " + STableConstants.ICON_NULL + ") AS f_ico, IF(x.ts IS NULL OR doc_xml = '', " + STableConstants.ICON_NULL + ", IF(x.fid_tp_xml = " + SModSysConsts.FINS_TP_ACC_BAL + " OR x.fid_tp_xml = " + SModSysConsts.FINS_TP_ACC_NA + ", " + STableConstants.ICON_XML + ", " +
        "IF(x.fid_st_xml = " + SModSysConsts.FINS_TP_ACC_NA + " OR LENGTH(uuid) = 0, " + STableConstants.ICON_XML_PEND + ", IF(LENGTH(xc.ack_can_xml) = " + SModSysConsts.FINS_CFD_TAX_NA + " AND xc.ack_can_pdf_n IS NULL, " + STableConstants.ICON_XML_SIGN + ", " +
        "IF(LENGTH(xc.ack_can_xml) != " + SModSysConsts.FINS_CFD_TAX_NA + ", " + STableConstants.ICON_XML_CANC_XML + ", IF(xc.ack_can_pdf_n IS NOT NULL, " + STableConstants.ICON_XML_CANC_PDF + ", " + STableConstants.ICON_XML_SIGN + " )))))) AS f_ico_xml, bp.id_bp, bp.bp, bpc.bp_key, bpb.id_bpb, bpb.bpb, " +
        "(SELECT c.cur_key FROM erp.cfgu_cur AS c WHERE d.fid_cur = c.id_cur) AS f_cur_key, 'MXN' AS f_cur_key_local, " +
        "(SELECT cob.code FROM erp.bpsu_bpb AS cob WHERE d.fid_cob = cob.id_bpb) AS f_cob_code, ul.usr, uc.usr, un.usr, ue.usr, ud.usr , " +
        "(SELECT rbc.code FROM fin_bkc AS rbc WHERE r.id_bkc = rbc.id_bkc) AS f_rbc_code, " +
        "(SELECT rcb.code FROM erp.bpsu_bpb AS rcb WHERE r.fid_cob = rcb.id_bpb) AS f_rcb_code, CONCAT(r.id_year, '-', erp.lib_fix_int(r.id_per, " + SModSysConsts.FINS_TP_ACC_BAL + ")) as f_rper, " +
        "CONCAT(r.id_tp_rec, '-', erp.lib_fix_int(r.id_num, " + SModSysConsts.ITMU_TP_UNIT_MASS + ")) as f_rnum FROM trn_dps AS d " +
        "INNER JOIN erp.bpsu_bp AS bp ON d.fid_bp_r = bp.id_bp " +
        "INNER JOIN erp.bpsu_bp_ct AS bpc ON bp.id_bp = bpc.id_bp AND bpc.id_ct_bp = " + SModSysConsts.FINS_TP_ACC_RES + " " +
        "INNER JOIN erp.bpsu_bpb AS bpb ON d.fid_bpb = bpb.id_bpb " +
        "INNER JOIN erp.trnu_tp_dps AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps " +
        "AND d.fid_tp_dps = dt.id_tp_dps AND d.fid_ct_dps = " + SModSysConsts.FINS_TP_ACC_BAL + " AND d.fid_cl_dps = " + SModSysConsts.ITMU_TP_UNIT_VOL + " " +
        "INNER JOIN erp.usru_usr AS ul ON d.fid_usr_link = ul.id_usr " +
        "INNER JOIN erp.usru_usr AS uc ON d.fid_usr_close = uc.id_usr " +
        "INNER JOIN erp.usru_usr AS un ON d.fid_usr_new = un.id_usr " +
        "INNER JOIN erp.usru_usr AS ue ON d.fid_usr_edit = ue.id_usr " +
        "INNER JOIN erp.usru_usr AS ud ON d.fid_usr_del = ud.id_usr " +
        "LEFT OUTER JOIN trn_cfd AS x ON d.id_year = x.fid_dps_year_n AND d.id_doc = x.fid_dps_doc_n " +
        "LEFT OUTER JOIN " + complementaryDbName + ".trn_cfd AS xc ON x.id_cfd = xc.id_cfd " +
        "LEFT OUTER JOIN trn_dps_rec AS dr ON d.id_year = dr.id_dps_year AND d.id_doc = dr.id_dps_doc " +
        "LEFT OUTER JOIN fin_rec AS r ON dr.fid_rec_year = r.id_year AND dr.fid_rec_per = r.id_per AND dr.fid_rec_bkc = r.id_bkc AND dr.fid_rec_tp_rec = r.id_tp_rec AND dr.fid_rec_num = r.id_num " +
        "WHERE bp.id_bp = " + mnBizPartherId + " AND d.b_del = " + SModSysConsts.FINS_CFD_TAX_NA + " AND YEAR(d.dt) = " + mnYearId + " AND d.dt >= '" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "' AND d.dt <= '" + SLibUtils.DbmsDateFormatDate.format(mtDateFinal) + "'";
        
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
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "tax_charged_r", "Imp tras mon $", STableConstants.WIDTH_VALUE));
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