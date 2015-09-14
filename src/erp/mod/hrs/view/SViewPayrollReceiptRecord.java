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
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;

/**
 *
 * @author Juan Barajas
 */
public class SViewPayrollReceiptRecord extends SGridPaneView {

    public SViewPayrollReceiptRecord(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.HRSX_PAY_REC, SLibConsts.UNDEFINED, title);
        setRowButtonsEnabled(false);
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
            sql += "pe.b_del = 0 ";
        }

        msSql = "SELECT pe.id_pay, pe.id_emp, p.id_pay, r.id_year, r.id_per, r.id_bkc, r.id_tp_rec, r.id_num, "
                + "p.per_year, p.per, p.num, p.fk_tp_pay, p.dt_sta, p.dt_end, " 
                + "p.b_nor, p.b_clo, p.b_del, r.dt, bkc.code, cob.code, "
                + "CONCAT(r.id_year, '-', erp.lib_fix_int(r.id_per, 2)) as f_per, "
                + "(SELECT t.name FROM " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_PAY) + " AS t WHERE p.fk_tp_pay = t.id_tp_pay) AS f_pay_name, "
                + "(SELECT emp.num FROM " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS emp WHERE emp.id_emp = pe.id_emp) AS " + SDbConsts.FIELD_CODE + ", "
                + "(SELECT bp.bp FROM " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp WHERE bp.id_bp = pe.id_emp) AS " + SDbConsts.FIELD_NAME + ", "
                + "CONCAT(r.id_tp_rec, '-', erp.lib_fix_int(r.id_num, " + SDataConstantsSys.NUM_LEN_FIN_REC + ")) as f_num, "
                + "SUM(pe.ear_r) AS f_debit, SUM(pe.ded_r) AS f_credit, SUM(pe.pay_r) AS f_balance, "
                + "uc.usr AS f_usr_close, "
                + "p.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "p.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "p.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "p.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pe ON "
                + "p.id_pay = pe.id_pay "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS re ON re.fid_pay_n = pe.id_pay "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r ON r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_BKC) + " AS bkc ON r.id_bkc = bkc.id_bkc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS cob ON r.fid_cob = cob.id_bpb "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uc ON "
                + "p.fk_usr_clo = uc.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
                + "p.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON "
                + "p.fk_usr_upd = uu.id_usr "
                + "WHERE p.b_del = 0 AND re.b_del = 0 " + (sql.isEmpty() ? "" : " AND " + sql)
                + "GROUP BY pe.id_pay, pe.id_emp, p.per_year, p.per, f_pay_name, p.num, "
                + "p.dt_sta, p.dt_end, p.b_nor, p.b_clo, p.b_del, r.dt, bkc.code, cob.code, r.id_year, r.id_per, r.id_bkc, r.id_tp_rec, r.id_num "
                + "ORDER BY p.per_year, p.per, f_pay_name, p.num, p.dt_sta, p.dt_end, " + SDbConsts.FIELD_NAME + ", pe.id_pay, pe.id_emp, "
                + "p.b_nor, p.b_clo, p.b_del, r.dt, bkc.code, cob.code, r.id_year, r.id_per, r.id_bkc, r.id_tp_rec, r.id_num ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<SGridColumnView>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_CAL_YEAR, "p.per_year", "Ejercicio"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_CAL_MONTH, "p.per", "Período"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "f_pay_name", "Tipo nómina"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "p.num", "No. nómina"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "p.dt_sta", "F. inicial"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "p.dt_end", "F. final"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, SDbConsts.FIELD_NAME, SGridConsts.COL_TITLE_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_BPR, SDbConsts.FIELD_CODE, SGridConsts.COL_TITLE_CODE));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "p.b_nor", "Es normal"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "p.b_clo", "Cerrada"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "p.b_del", "Eliminado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "f_per", "Período póliza"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "bkc.code", "Centro contable póliza"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "cob.code", "Sucursal empresa póliza"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "f_num", "Folio póliza"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "r.dt", "Fecha póliza"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "f_debit", "Percepciones $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "f_credit", "Deducciones $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "f_balance", "Alcance neto $"));
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
