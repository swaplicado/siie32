/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.view;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.util.ArrayList;
import sa.lib.SLibConsts;
import sa.lib.SLibRpnArgument;
import sa.lib.SLibRpnArgumentType;
import sa.lib.SLibRpnOperator;
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
 * @author Edwin Carmona
 */
public class SViewBankPayDispersion extends SGridPaneView {

    private SGridFilterDatePeriod moFilterDatePeriod;
    
    public SViewBankPayDispersion(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.HRSX_BANK_PAY_DISP, SLibConsts.UNDEFINED, title);
        initComponentsCustom();
    }

    /*
     * Private methods
     */
    private void initComponentsCustom() {
        setRowButtonsEnabled(false);
        
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
    }

    /*
     * Public methods
     */

    /*
     * Overriden methods
     */

    @Override
    public void actionMouseClicked() {
        
    }
    
    @Override
    public void prepareSqlQuery() {
        String sql = "";
        String where = "";
        Object filter = null;
        
        moPaneSettings = new SGridPaneSettings(1);
        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
        where += (where.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("hp.dt_end", (SGuiDate) filter);
       
        msSql = "SELECT " +
            "    hp.id_pay AS " + SDbConsts.FIELD_ID + "1, " +
            "    hp.num AS " + SDbConsts.FIELD_CODE + ", " +
            "    hp.num AS " + SDbConsts.FIELD_NAME + ", " +
            "    IF(hp.fk_tp_pay = " + SModSysConsts.HRSS_TP_PAY_FOR + ", " +
            "        'QUINCENA', " +
            "        'SEMANA') AS tp_pay, " +
            "    hp.num, " +
            "    hp.dt_sta, " +
            "    hp.dt_end, " +
            "    r.concept, " +
            "    r.fid_cob_n, " +
            "    r.fid_acc_cash_n, " +
            "    bba.fid_bank, " +
            "    bp.bp, " +
            "    (SELECT  " +
            "            COUNT(*) " +
            "        FROM " +
            "            " + SModConsts.TablesMap.get(SModConsts.HRS_ACC_PAY) + " AS hap, " +
            "            " + SModConsts.TablesMap.get(SModConsts.HRS_ACC_PAY_RCP) + " AS hapr " +
            "        WHERE " +
            "            hap.id_pay = hapr.id_pay " +
            "                AND hap.id_acc = hapr.id_acc " +
            "                AND NOT hap.b_del " +
            "                AND hap.id_pay = hp.id_pay " +
            "                AND hapr.fid_rec_year = r.id_year " +
            "                AND hapr.fid_rec_per = r.id_per " +
            "                AND hapr.fid_rec_bkc = r.id_bkc " +
            "                AND hapr.fid_rec_tp_rec = r.id_tp_rec " +
            "                AND hapr.fid_rec_num = r.id_num) AS _count_bank_rcp, " +
            "    hp.nts, " +
            "    SUM(re.debit) AS deb, " +
            "    SUM(re.credit) AS cred " +
            "FROM " +
            "    " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " r " +
            "        INNER JOIN " +
            "    " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " re ON r.id_year = re.id_year " +
            "        AND r.id_per = re.id_per " +
            "        AND r.id_bkc = re.id_bkc " +
            "        AND r.id_tp_rec = re.id_tp_rec " +
            "        AND r.id_num = re.id_num " +
            "        INNER JOIN " +
            "    " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS hp ON re.fid_pay_n = hp.id_pay " +
            "        INNER JOIN " +
            "    " + SModConsts.TablesMap.get(SModConsts.FIN_ACC_CASH) + " fac ON r.fid_cob_n = fac.id_cob " +
            "        AND r.fid_acc_cash_n = fac.id_acc_cash " +
            "        INNER JOIN " +
            "    " + SModConsts.TablesMap.get(SModConsts.BPSU_BANK_ACC) + " bba ON fac.fid_bpb_n = bba.id_bpb " +
            "        AND fac.fid_bank_acc_n = bba.id_bank_acc " +
            "        INNER JOIN " +
            "	" + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " bp ON bba.fid_bank = bp.id_bp " +
            "WHERE " +
            "    NOT r.b_del AND NOT re.b_del " +
            "        AND " + where + " " +
            "        AND r.id_tp_rec = '" + SModSysConsts.FINU_TP_REC_CASH_BANK + "' " +
            "GROUP BY r.id_year , r.id_per , r.id_bkc , r.id_tp_rec , r.id_num " +
            "ORDER BY tp_pay , num ASC , bp, concept ASC , nts ASC;";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();
        SGridColumnView column = null;
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "tp_pay", "Tipo de pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "hp.num", "Núm nómina"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "hp.dt_sta", "F inicial nómina"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "hp.dt_end", "F final nómina"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "bp.bp", "Banco", 100));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "_count_bank_rcp", "Empleados"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "hp.nts", "Notas nómina", 200));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "r.concept", "Concepto póliza", 200));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "deb", "Percepciones $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "cred", "Deducciones $"));
        
        column = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "", "Total neto $");
        column.getRpnArguments().add(new SLibRpnArgument("deb", SLibRpnArgumentType.OPERAND));
        column.getRpnArguments().add(new SLibRpnArgument("cred", SLibRpnArgumentType.OPERAND));
        column.getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.SUBTRACTION, SLibRpnArgumentType.OPERATOR));
        gridColumnsViews.add(column);
        
        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
    }
}
