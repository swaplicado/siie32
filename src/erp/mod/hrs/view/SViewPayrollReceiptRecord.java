/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.view;

import erp.data.SDataConstantsSys;
import erp.mod.SModConsts;
import java.util.ArrayList;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDatePeriod;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SViewPayrollReceiptRecord extends SGridPaneView {

    private SGridFilterDatePeriod moFilterDatePeriod;

    public SViewPayrollReceiptRecord(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.HRSX_PAY_REC, SLibConsts.UNDEFINED, title);
        initComponentCustom();
    }

    /*
     * Private methods
     */

    private void initComponentCustom() {
        setRowButtonsEnabled(false);
        jtbFilterDeleted.setEnabled(false);
        
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(0);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            sql += "NOT pr.b_del ";
        }
        
        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
        sql += (sql.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("p.dt_end", (SGuiDate) filter);

        msSql = "SELECT pr.id_pay, pr.id_emp, r.id_year, r.id_per, r.id_bkc, r.id_tp_rec, r.id_num, "
                + "p.per_year, p.per, p.num, p.fk_tp_pay, p.dt_sta, p.dt_end, p.nts, p.b_clo, p.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "tp.name, tp.code, tpsc.name, tpsc.code, tps.name, tps.code, e.num AS " + SDbConsts.FIELD_CODE + ", bp.bp AS " + SDbConsts.FIELD_NAME + ", r.dt, bkc.code, cob.code, "
                + "CONCAT(r.id_year, '-', erp.lib_fix_int(r.id_per, 2)) as f_per, "
                + "CONCAT(r.id_tp_rec, '-', erp.lib_fix_int(r.id_num, " + SDataConstantsSys.NUM_LEN_FIN_REC + ")) as f_num, "
                + "pr.ear_r AS f_debit, pr.ded_r AS f_credit, pr.pay_r AS f_balance, "
                + "uc.usr AS f_usr_close, "
                + "p.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "p.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "p.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "p.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_PAY) + " AS tp ON p.fk_tp_pay = tp.id_tp_pay "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_TP_PAY_SHT_CUS) + " AS tpsc ON p.fk_tp_pay_sht_cus = tpsc.id_tp_pay_sht_cus "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_PAY_SHT) + " AS tps ON p.fk_tp_pay_sht = tps.id_tp_pay_sht "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON p.id_pay = pr.id_pay "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS e ON pr.id_emp = e.id_emp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp ON pr.id_emp = bp.id_bp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_ACC_PAY) + " AS pa ON pa.id_pay = p.id_pay AND pa.b_del = 0 "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_ACC_PAY_RCP) + " AS pea ON pea.id_pay = pa.id_pay AND pea.id_acc = pa.id_acc AND pea.id_emp = pr.id_emp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r ON r.id_year = pea.fid_rec_year AND r.id_per = pea.fid_rec_per AND r.id_bkc = pea.fid_rec_bkc AND r.id_tp_rec = pea.fid_rec_tp_rec AND r.id_num = pea.fid_rec_num "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS re ON re.id_year = r.id_year AND re.id_per = r.id_per AND re.id_bkc = r.id_bkc AND re.id_tp_rec = r.id_tp_rec AND re.id_num = r.id_num "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_BKC) + " AS bkc ON r.id_bkc = bkc.id_bkc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS cob ON r.fid_cob = cob.id_bpb "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uc ON p.fk_usr_clo = uc.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON p.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON p.fk_usr_upd = uu.id_usr "
                + "WHERE p.b_del = 0 AND re.b_del = 0 " + (sql.isEmpty() ? "" : " AND " + sql)
                + "GROUP BY pr.id_pay, pr.id_emp, r.id_year, r.id_per, r.id_bkc, r.id_tp_rec, r.id_num, "
                + "p.per_year, p.per, p.num, p.fk_tp_pay, p.dt_sta, p.dt_end, p.nts, p.b_clo, p.b_del, "
                + "tp.name, tp.code, tpsc.name, tpsc.code, tps.name, tps.code, e.num, bp.bp, r.dt, bkc.code, cob.code "
                + "ORDER BY p.per_year, p.per, tp.name, p.num, tpsc.code, tps.name, bp.bp, pr.id_pay, pr.id_emp, "
                + "r.dt, bkc.code, cob.code, r.id_year, r.id_per, r.id_bkc, r.id_tp_rec, r.id_num ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_CAL_YEAR, "p.per_year", "Ejercicio"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_CAL_MONTH, "p.per", "Período"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "tp.name", "Período pago nómina"));
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "p.num", "Núm nómina"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "p.dt_sta", "F inicial nómina"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "p.dt_end", "F final nómina"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "tpsc.code", "Tipo nómina empresa"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "tps.name", "Tipo nómina"));
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "p.nts", "Notas nómina", 200));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "p.b_clo", "Cerrada"));
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, SDbConsts.FIELD_NAME, "Empleado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_BPR, SDbConsts.FIELD_CODE, "Clave"));
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "f_per", "Período póliza"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "bkc.code", "Centro contable póliza"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "cob.code", "Sucursal empresa póliza"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "f_num", "Folio póliza"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "r.dt", "Fecha póliza"));
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "f_debit", "Percepciones $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "f_credit", "Deducciones $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "f_balance", "Total neto $"));
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS));
        
        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.HRS_PAY);
        moSuscriptionsSet.add(SModConsts.FIN_REC);
        moSuscriptionsSet.add(SModConsts.BPSU_BPB);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }
}
