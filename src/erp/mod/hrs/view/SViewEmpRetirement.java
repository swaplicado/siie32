package erp.mod.hrs.view;

import erp.mod.hrs.utils.SReportEmpRetiremet;
import erp.mod.SModConsts;
import erp.mod.hrs.db.SHrsConsts;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDateRange;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;

/**
 *
 * @author Claudio Peña
 */
public class SViewEmpRetirement extends SGridPaneView implements ActionListener {

    private SGridFilterDateRange moFilterDateRange;
    private int mnPeriodDays;
    private JButton jbEmployeesRetirement;
    Date[] dateRange = null;
    Date dateStart = null;
    Date dateEnd = null;

    public SViewEmpRetirement(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.HRSX_BEN_ANN_BON, SLibConsts.UNDEFINED, title);
        initComponentsCustom();
    }

    /*
     * Private methods
     */
    private void initComponentsCustom() {
        setRowButtonsEnabled(false);

        Date currentDate = miClient.getSession().getCurrentDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        int currentMonth = calendar.get(Calendar.MONTH);
        int bimestreStartMonth = (currentMonth / 2) * 2;
        calendar.set(Calendar.MONTH, bimestreStartMonth);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startOfBimestre = calendar.getTime();

        calendar.add(Calendar.MONTH, 1); 
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date endOfBimestre = calendar.getTime();

        Date[] bimestreRange = new Date[] { startOfBimestre, endOfBimestre };

        moFilterDateRange = new SGridFilterDateRange(miClient, this);
        moFilterDateRange.initFilter(bimestreRange);
        jbEmployeesRetirement = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_wizard.gif")), "Reporte cuotas patronales", this);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbEmployeesRetirement);
    }
    
    @Override
    public void actionMouseClicked() {
    }
    
    @Override
    public void prepareSqlQuery() {
        String sql = "";
        String where = "";
        String cutoff = "";
        Object filter = null;
        Date dtYear = new Date();

        moPaneSettings = new SGridPaneSettings(1);
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = miClient.getSession().getCurrentDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        dateStart = calendar.getTime();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        dateEnd = calendar.getTime();
        String formattedDateStart = dateFormat.format(dateStart);
        String formattedDateEnd = dateFormat.format(dateEnd);

        int[] aDt = SLibTimeUtils.digestDate(dateStart);
        dtYear = SLibTimeUtils.createDate(aDt[0], aDt[1], aDt[2]);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            where += (where.isEmpty() ? "" : " ") + "NOT e.b_del AND  e.b_act = 1 ";
        }

        cutoff = "'" + SLibUtils.DbmsDateFormatDate.format(SLibTimeUtils.getEndOfYear(dtYear)) + "'";
        
        mnPeriodDays = SLibTimeUtils.countPeriodDays(dateStart, dateEnd);

        msSql = "SELECT " +
                "fc.id_cc AS " + SDbConsts.FIELD_ID + "1, " +
                "fc.cc AS " + SDbConsts.FIELD_CODE + ", " +
                "fc.cc AS " + SDbConsts.FIELD_NAME + ", " +
                "e.id_emp, " +
                "e.num, " +
                "e.b_act, " +
                "b.bp, " +
                "e.sal_ssc, " +
                "d.name, " +
                "fc.cc, " +
                "fc.id_cc, " +
                "(SELECT wage FROM HRS_MWZ_WAGE ORDER BY id_wage DESC LIMIT 1 ) AS _SM, " +  // Salario mínimo (_SM)
                "(SELECT amt FROM HRS_UMA ORDER BY id_uma DESC LIMIT 1) AS _UMA, " +       // Monto de UMA más reciente
                "(e.sal_ssc / (SELECT amt FROM HRS_UMA ORDER BY id_uma DESC LIMIT 1)) AS cos, " +  // Cálculo de cos (salario / UMA)
                "CASE " +  // Determinar el id_row
                "    WHEN e.sal_ssc < (SELECT wage FROM HRS_MWZ_WAGE WHERE dt_sta >= '" + formattedDateStart + "' AND dt_sta <= '" + formattedDateEnd + "') " +
                "    THEN 1 " + // Salario menor a _SM
                "    ELSE COALESCE( " +
                "        (SELECT r.id_row " +
                "         FROM HRS_EMPR_SSC s " +
                "         INNER JOIN HRS_EMPR_SSC_ROW r ON s.id_empr_ssc = r.id_empr_ssc " +
                "         WHERE s.tbl_year = " + aDt[0] + " " +
                "           AND (e.sal_ssc / (SELECT amt FROM HRS_UMA ORDER BY id_uma DESC LIMIT 1)) " +
                "               BETWEEN r.low_lim AND COALESCE( " +
                "                   (SELECT MIN(low_lim) FROM HRS_EMPR_SSC_ROW WHERE low_lim > r.low_lim), " +
                "                   999999 " + // Valor grande para el límite superior si no hay siguiente
                "               ) " +
                "         ORDER BY r.low_lim ASC LIMIT 1), " +
                "        (SELECT r.id_row " +
                "         FROM HRS_EMPR_SSC s " +
                "         INNER JOIN HRS_EMPR_SSC_ROW r ON s.id_empr_ssc = r.id_empr_ssc " +
                "         WHERE s.tbl_year = " + aDt[0] + " " +
                "         ORDER BY r.low_lim DESC LIMIT 1) " +
                "    ) " +
                "END AS id_row, " +
                "CASE " +  // low_lim_type
                "    WHEN e.sal_ssc < (SELECT wage FROM HRS_MWZ_WAGE WHERE dt_sta >= '" + formattedDateStart + "' AND dt_sta <= '" + formattedDateEnd + "') " +
                "    THEN 'SM' " +
                "    ELSE COALESCE( " +
                "        (SELECT r.low_lim_type " +
                "         FROM HRS_EMPR_SSC s " +
                "         INNER JOIN HRS_EMPR_SSC_ROW r ON s.id_empr_ssc = r.id_empr_ssc " +
                "         WHERE s.tbl_year = " + aDt[0] + " " +
                "           AND (e.sal_ssc / (SELECT amt FROM HRS_UMA ORDER BY id_uma DESC LIMIT 1)) " +
                "               BETWEEN r.low_lim AND COALESCE( " +
                "                   (SELECT MIN(low_lim) FROM HRS_EMPR_SSC_ROW WHERE low_lim > r.low_lim), " +
                "                   999999 " +
                "               ) " +
                "         ORDER BY r.low_lim ASC LIMIT 1), " +
                "        (SELECT r.low_lim_type " +
                "         FROM HRS_EMPR_SSC s " +
                "         INNER JOIN HRS_EMPR_SSC_ROW r ON s.id_empr_ssc = r.id_empr_ssc " +
                "         WHERE s.tbl_year = " + aDt[0] + " " +
                "         ORDER BY r.low_lim DESC LIMIT 1) " +
                "    ) " +
                "END AS low_lim_type, " +
                "CASE " +  // low_lim
                "    WHEN e.sal_ssc < (SELECT wage FROM HRS_MWZ_WAGE WHERE dt_sta >= '" + formattedDateStart + "' AND dt_sta <= '" + formattedDateEnd + "') " +
                "    THEN 1.0 " +
                "    ELSE COALESCE( " +
                "        (SELECT r.low_lim " +
                "         FROM HRS_EMPR_SSC s " +
                "         INNER JOIN HRS_EMPR_SSC_ROW r ON s.id_empr_ssc = r.id_empr_ssc " +
                "         WHERE s.tbl_year = " + aDt[0] + " " +
                "           AND (e.sal_ssc / (SELECT amt FROM HRS_UMA ORDER BY id_uma DESC LIMIT 1)) " +
                "               BETWEEN r.low_lim AND COALESCE( " +
                "                   (SELECT MIN(low_lim) FROM HRS_EMPR_SSC_ROW WHERE low_lim > r.low_lim), " +
                "                   999999 " +
                "               ) " +
                "         ORDER BY r.low_lim ASC LIMIT 1), " +
                "        (SELECT r.low_lim " +
                "         FROM HRS_EMPR_SSC s " +
                "         INNER JOIN HRS_EMPR_SSC_ROW r ON s.id_empr_ssc = r.id_empr_ssc " +
                "         WHERE s.tbl_year = " + aDt[0] + " " +
                "         ORDER BY r.low_lim DESC LIMIT 1) " +
                "    ) " +
                "END AS low_lim, " +
                " @empr_rate:= CASE " +  // empr_rate
                "     WHEN e.sal_ssc < (SELECT wage FROM HRS_MWZ_WAGE WHERE dt_sta >= '" + formattedDateStart + "' AND dt_sta <= '" + formattedDateEnd + "') " +
                "    THEN (SELECT r.empr_rate " +
                "          FROM HRS_EMPR_SSC s " +
                "          INNER JOIN HRS_EMPR_SSC_ROW r ON s.id_empr_ssc = r.id_empr_ssc " +
                "          WHERE s.tbl_year = " + aDt[0] + " AND r.id_row IN (1, 2) " +
                "          ORDER BY r.id_row ASC LIMIT 1) " +
                "    ELSE COALESCE( " +
                "        (SELECT r.empr_rate " +
                "         FROM HRS_EMPR_SSC s " +
                "         INNER JOIN HRS_EMPR_SSC_ROW r ON s.id_empr_ssc = r.id_empr_ssc " +
                "         WHERE s.tbl_year = " + aDt[0] + " " +
                "           AND (e.sal_ssc / (SELECT amt FROM HRS_UMA ORDER BY id_uma DESC LIMIT 1)) " +
                "               BETWEEN r.low_lim AND COALESCE( " +
                "                   (SELECT MIN(low_lim) FROM HRS_EMPR_SSC_ROW WHERE low_lim > r.low_lim), " +
                "                   999999 " +
                "               ) " +
                "         ORDER BY r.low_lim ASC LIMIT 1), " +
                "        (SELECT r.empr_rate " +
                "         FROM HRS_EMPR_SSC s " +
                "         INNER JOIN HRS_EMPR_SSC_ROW r ON s.id_empr_ssc = r.id_empr_ssc " +
                "         WHERE s.tbl_year = " + aDt[0] + " " +
                "         ORDER BY r.low_lim DESC LIMIT 1) " +
                "    ) " +
                "END AS empr_rate, " +
                mnPeriodDays + " AS period_days, " +
                SHrsConsts.IMMS_RETIREMET + " AS f_retirement, " +
                SHrsConsts.IMMS_INFONAVIT + " AS f_infonavit, " +
                "((e.sal_ssc * " + SHrsConsts.IMMS_RETIREMET + ") * " + mnPeriodDays + " ) AS t_retirement, " +
                "((e.sal_ssc * @empr_rate) * " + mnPeriodDays + " ) AS t_unemployment_old_age, " +
                "((e.sal_ssc * " + SHrsConsts.IMMS_INFONAVIT + ") * " + mnPeriodDays + " ) AS t_infonavit " +
                "FROM erp.hrsu_emp AS e " +
                "INNER JOIN erp.bpsu_bp AS b ON b.id_bp = e.id_emp " +
                "INNER JOIN erp.hrsu_dep AS d ON d.id_dep = e.fk_dep " +
                "INNER JOIN HRS_CFG_ACC_DEP_PACK_CC AS c ON c.id_dep = d.id_dep " +
                "INNER JOIN HRS_PACK_CC_CC AS cc ON cc.id_pack_cc = c.fk_pack_cc " +
                "INNER JOIN FIN_CC AS fc ON fc.pk_cc = cc.id_cc " +
                "INNER JOIN HRS_EMP_MEMBER AS mem ON mem.id_emp = e.id_emp " +
                "WHERE " +
                (where.isEmpty() ? " e.b_act = 0 AND NOT e.b_del " : " ") + where +
                "GROUP BY e.id_emp " +
                "ORDER BY e.sal_ssc, e.id_emp, id_row ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "b.bp", "Nombre empleado", 250));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_RAW, "e.num", "Número empleado", 50));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "e.b_act", "Activo", 50));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "e.sal_ssc", "Salario SBC $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "d.name", "Departamento", 150));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_L, "fc.cc", "Centro de costo"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "fc.id_cc", "No. centro de costo"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_SM", "Salario minimo $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_UMA", "UMA"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "low_lim_type", "Unidad medida", 60));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_TAX, "f_retirement", "Factor retiro"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_TAX, "empr_rate", "Factor cesantia"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_TAX, "f_infonavit", "Factor Infonavit"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_RAW, "period_days", "Días del periodo"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "t_retirement", "Retiro"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "t_unemployment_old_age", "Cesantia y vejez"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "t_infonavit", "Infonavit"));

        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
    }

    @Override
    @SuppressWarnings("empty-statement")
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();

        if (button == jbEmployeesRetirement) {
            try {
                 SReportEmpRetiremet.generateCcTotalsReportStatic(miClient.getSession().getStatement().getConnection(), dateStart, dateEnd );
            } catch (SQLException ex) {
                Logger.getLogger(SViewEmpRetirement.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(SViewEmpRetirement.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}