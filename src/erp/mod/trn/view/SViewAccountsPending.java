/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.trn.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.bps.db.SBpsUtils;
import java.util.ArrayList;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDateCutOff;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;

/**
 *
 * @author Sergio Flores
 */
public class SViewAccountsPending extends SGridPaneView {

    private SGridFilterDateCutOff moFilterDateCutOff;

    /**
     * Create view for pending accounts: receivable accounts & payable accounts.
     * @param client GUI client.
     * @param idBizPartnerCategory Desired business partner category.
     * @param title View's tab title.
     */
    public SViewAccountsPending(SGuiClient client, int idBizPartnerCategory, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.TRNX_ACC_PEND, idBizPartnerCategory, title, null);
        initComponentsCustom();
    }

    private void initComponentsCustom() {
        setRowButtonsEnabled(false, false, false, false, false);
        
        moFilterDateCutOff = new SGridFilterDateCutOff(miClient, this);
        moFilterDateCutOff.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_DATE, miClient.getSession().getCurrentDate().getTime()));
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDateCutOff);
    }

    @Override
    public void prepareSqlQuery() {
        int year = 0;
        Date date = null;
        int[] keySysMov = null;
        String sql = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(2);

        filter = moFiltersMap.get(SGridConsts.FILTER_DATE).getValue();
        if (filter instanceof SGuiDate) {
            date = (Date) filter;
            if (date == null) {
                date = SLibTimeUtils.getEndOfYear(miClient.getSession().getSystemDate());
            }
            year = SLibTimeUtils.digestYear(date)[0];
        }
        
        switch (mnGridSubtype) {
            case SModSysConsts.BPSS_CT_BP_SUP:
                keySysMov = SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP;
                break;
            case SModSysConsts.BPSS_CT_BP_CUS:
                keySysMov = SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS;
                break;
            default:
                // when no valid customer category is provided, null pointer exception will be thrown!
        }
        
        msSql = "SELECT "
                + "b.id_bp AS " + SDbConsts.FIELD_ID + "1, "
                + "d.id_year AS " + SDbConsts.FIELD_ID + "2, "
                + "d.id_doc AS " + SDbConsts.FIELD_ID + "3, "
                + "re.fid_cur AS " + SDbConsts.FIELD_ID + "4, "
                + "bc.bp_key AS " + SDbConsts.FIELD_CODE + ", "
                + "b.bp AS " + SDbConsts.FIELD_NAME + ", "
                + "IF(d.num_ser = '', d.num, CONCAT(d.num_ser, '-', num)) AS _doc_num, d.dt, "
                + "d.tot_cur_r, c.cur_key, d.tot_r, '" + miClient.getSession().getSessionCustom().getLocalCurrencyCode() + "' AS _cur_key_loc, "
                + "SUM(re.debit_cur - re.credit_cur) AS _doc_bal_cur, c.cur_key, SUM(re.debit - re.credit) AS _doc_bal, "
                + "d.dt_start_cred, d.days_cred, ADDDATE(d.dt_start_cred, d.days_cred) AS _due, "
                + "IF(DATEDIFF(CURDATE(), ADDDATE(d.dt, d.days_cred)) <= 0, -DATEDIFF(CURDATE(), ADDDATE(d.dt, d.days_cred)), 0) AS _doc_days_cred, "
                + "IF(DATEDIFF(CURDATE(), ADDDATE(d.dt, d.days_cred)) > 0, DATEDIFF(CURDATE(), ADDDATE(d.dt, d.days_cred)), 0) AS _doc_days_due, "
                + "IF(bc.b_cred_usr, bc.days_cred, bt.days_cred) AS _days_cred, "
                + "@cred := IF(bc.b_cred_usr, bc.cred_lim, bt.cred_lim) AS _cred_lim, bc.garnt, bc.insur, "
                + "@bal := COALESCE(t.bal, 0.0) AS _bal, "
                + "@bal > @cred AS _cred_lim_od, @bal > bc.garnt AS _garnt_od, @bal > bc.insur AS _insur_od, "   // overdrawn
                + "a.bp "
                + "FROM "
                + "" + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS re ON re.id_year = r.id_year AND re.id_per = r.id_per AND re.id_bkc = r.id_bkc AND re.id_tp_rec = r.id_tp_rec AND re.id_num = r.id_num "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b ON re.fid_bp_nr = b.id_bp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP_CT) + " AS bc ON b.id_bp = bc.id_bp AND bc.id_ct_bp = 3 "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_TP_BP) + " AS bt ON bc.fid_ct_bp = bt.id_ct_bp AND bc.fid_tp_bp = bt.id_tp_bp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS c ON re.fid_cur = c.id_cur "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d ON re.fid_dps_year_n = d.id_year AND re.fid_dps_doc_n = d.id_doc "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS a ON d.fid_sal_agt_n = a.id_bp "
                + "LEFT OUTER JOIN ("
                + "  SELECT re1.fid_bp_nr AS id_bp, SUM(re1.debit - re1.credit) AS bal "
                + "  FROM "
                + "  " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r1, " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS re1 "
                + "  WHERE "
                + "  r1.id_year = re1.id_year AND r1.id_per = re1.id_per AND r1.id_bkc = re1.id_bkc AND r1.id_tp_rec = re1.id_tp_rec AND r1.id_num = re1.id_num AND "
                + "  NOT r1.b_del AND NOT re1.b_del AND r1.id_year = " + year + " AND r1.dt <= '" + SLibUtils.DbmsDateFormatDate.format(date) + "' AND "
                + "  re1.fid_ct_sys_mov_xxx = " + keySysMov[0] + " AND re1.fid_tp_sys_mov_xxx = " + keySysMov[1] + " "
                + "  GROUP BY re1.fid_bp_nr "
                + "  ORDER BY re1.fid_bp_nr) AS t ON re.fid_bp_nr = t.id_bp "
                + "WHERE "
                + "NOT r.b_del AND NOT re.b_del AND r.id_year = " + year + " AND r.dt <= '" + SLibUtils.DbmsDateFormatDate.format(date) + "' AND "
                + "re.fid_ct_sys_mov_xxx = " + keySysMov[0] + " AND re.fid_tp_sys_mov_xxx = " + keySysMov[1] + " "
                + "GROUP BY "
                + "b.bp, b.id_bp, d.id_year, d.id_doc, re.fid_cur "
                + "HAVING "
                + "_doc_bal <> 0.0 AND _doc_bal_cur <> 0.0 "
                + "ORDER BY "
                + "b.bp, b.id_bp, d.num_ser, CAST(d.num AS UNSIGNED), d.id_year, d.id_doc, re.fid_cur; ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        String category = SBpsUtils.getBizPartnerCategoryName(mnGridSubtype, SUtilConsts.NUM_SNG).toLowerCase();
        SGridColumnView column = null;
        ArrayList<SGridColumnView> columns = new ArrayList<SGridColumnView>();

        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, SDbConsts.FIELD_NAME, SGridConsts.COL_TITLE_NAME + " " + category));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "_doc_num", "Folio doc"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "d.dt", "Emisión doc"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "d.tot_cur_r", "Total doc $ moneda doc"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "c.cur_key", "Moneda doc"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "d.tot_cur_r", "Total doc $ moneda loc"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "_cur_key_loc", "Moneda loc"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_doc_bal_cur", "Saldo doc $ moneda doc"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "c.cur_key", "Moneda doc"));
        column = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_doc_bal", "Saldo doc $ moneda loc");
        column.setSumApplying(true);
        columns.add(column);
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "_cur_key_loc", "Moneda loc"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "d.dt_start_cred", "Base crédito doc"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "d.days_cred", "Días crédito doc"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "_due", "Vencimiento doc"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "_doc_days_cred", "Días x vencer doc"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "_doc_days_due", "Días vencido doc"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "_days_cred", "Días crédito " + category));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_cred_lim", "Límite crédito " + category + " $"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "bc.garnt", "Garantía " + category + " $"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "bc.insur", "Seguro " + category + " $"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_bal", "Saldo " + category + " $"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "_cred_lim_od", "Límite crédito sobregiro"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "_garnt_od", "Garantía sobregiro"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "_insur_od", "Seguro sobregiro"));
        
        if (mnGridSubtype == SModSysConsts.BPSS_CT_BP_CUS) {
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "a.bp", SGridConsts.COL_TITLE_NAME + " agente"));
        }

        return columns;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.BPSU_BP);
        moSuscriptionsSet.add(SDataConstants.BPSU_BP);
        moSuscriptionsSet.add(SModConsts.TRN_DPS);
        moSuscriptionsSet.add(SDataConstants.TRN_DPS);
        moSuscriptionsSet.add(SModConsts.FIN_REC);
        moSuscriptionsSet.add(SDataConstants.FIN_REC);
        moSuscriptionsSet.add(SModConsts.CFGU_CUR);
        moSuscriptionsSet.add(SDataConstants.CFGU_CUR);
    }
}
