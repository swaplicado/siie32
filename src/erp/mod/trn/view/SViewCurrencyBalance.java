/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view;

import erp.data.SDataConstantsSys;
import erp.mod.SModConsts;
import erp.mod.trn.db.STrnConsts;
import java.util.ArrayList;
import java.util.Date;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDate;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;
import sa.lib.gui.SGuiParams;

/**
 *
 * @author Edwin Carmona
 */
public class SViewCurrencyBalance extends SGridPaneView {

    private SGridFilterDate moFilterDate;
    private Date mtDateCut;
    
    public SViewCurrencyBalance(SGuiClient client, int gridSubtype, String title, SGuiParams params) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.TRNX_BP_BAL_CUR, gridSubtype, title, params);
        initComponentsCustom();
    }

    /*
     * Private methods
     */

    private void initComponentsCustom() {
        setRowButtonsEnabled(false);
        moFilterDate = new SGridFilterDate(miClient, this);
        mtDateCut = null;
        
        moFilterDate.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_YEAR, miClient.getSession().getCurrentDate().getTime()));
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDate);
    }

    /*
     * Public methods
     */

    /*
     * Overriden methods
     */
    
    @Override
    public void prepareSqlQuery() {
        String dateCut = SLibUtils.DbmsDateFormatDate.format(miClient.getSession().getCurrentDate());
        Object filter = null;
        
        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE).getValue();
        if (filter != null) {
            mtDateCut = (SGuiDate) filter;
            dateCut = "'" + SLibUtils.DbmsDateFormatDate.format(mtDateCut) + "' ";
        }
        
        moPaneSettings = new SGridPaneSettings(1);

        msSql = "SELECT " +
                "re.fid_cur AS " + SDbConsts.FIELD_ID + "1, " +
                "'' AS " + SDbConsts.FIELD_CODE + ", " +
                "'' AS " + SDbConsts.FIELD_NAME + ", " +
                "c.cur AS _currency, " +
                "(IF(ba.fid_cty_n = " + miClient.getSession().getSessionCustom().getLocalCountryKey()[0] + " OR ba.fid_cty_n IS NULL, " +
                "'" + STrnConsts.TXT_TRN_DOM + "', '" + STrnConsts.TXT_TRN_INT + "')) AS _oper_type, " +
                "SUM(re.debit - re.credit) AS _bal, " +
                "SUM(re.debit_cur - re.credit_cur) AS _bal_cur, " +
                "c.cur_key AS _currency_key, " +
                "b.bp, ba.fid_cty_n " +
                "FROM fin_rec AS r " +
                "INNER JOIN fin_rec_ety AS re ON r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num " +
                "INNER JOIN erp.cfgu_cur AS c ON re.fid_cur = c.id_cur " +
                "INNER JOIN fin_acc AS a ON re.fid_acc = a.id_acc " +
                "INNER JOIN erp.bpsu_bp AS b ON re.fid_bp_nr = b.id_bp " +
                "INNER JOIN erp.bpsu_bpb AS bpb ON b.id_bp = bpb.fid_bp AND bpb.fid_tp_bpb = " + SDataConstantsSys.BPSS_TP_BPB_HQ + " " +
                "INNER JOIN erp.bpsu_bpb_add ba ON bpb.id_bpb = ba.id_bpb AND ba.fid_tp_add = " + SDataConstantsSys.BPSS_TP_ADD_OFF + " " +
                "WHERE r.id_year = " + SLibTimeUtils.digestYear(mtDateCut)[0] + " AND " +
                "r.dt <= " + dateCut + " AND NOT r.b_del " +
                "AND NOT re.b_del AND re.fid_ct_sys_mov_xxx = " + (mnGridSubtype == SDataConstantsSys.TRNS_CT_DPS_SAL ? SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS[0] : SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[0]) + " " +
                "AND re.fid_tp_sys_mov_xxx = " + (mnGridSubtype == SDataConstantsSys.TRNS_CT_DPS_SAL ? SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS[1] : SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[1]) + " " +
                "GROUP BY _currency, _oper_type" + (mnGridMode == SGuiConsts.PARAM_BPR ? ", b.bp" : "" ) + " " +
                "HAVING _bal <> 0 OR _bal_cur <> 0 " +
                "ORDER BY _currency, _oper_type" + (mnGridMode == SGuiConsts.PARAM_BPR ? ", b.bp" : "");
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        SGridColumnView column = null;
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<SGridColumnView>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "_currency", "Nombre moneda"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "_oper_type", "Tipo operación"));
        if (mnGridMode == SGuiConsts.PARAM_BPR) {
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "bp", mnGridSubtype == SDataConstantsSys.TRNS_CT_DPS_SAL ? "Cliente" : "Proveedor"));
        }
        
        column = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_bal", "Monto $");
        column.setSumApplying(true);
        gridColumnsViews.add(column);
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_bal_cur", "Monto mon $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "_currency_key", "Moneda"));

        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
    }
}
