/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.view;

import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.gui.grid.SGridFilterPanelEmployee;
import erp.mod.SModConsts;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JTextField;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterValue;
import sa.lib.grid.SGridFilterYear;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;

/**
 *
 * @author Sergio Flores
 */
public class SViewDocAdminRecordSummary extends SGridPaneView {
    
    private int mnFilterAuthorId;
    private SGridFilterYear moFilterYear;
    private SGridFilterPanelEmployee moFilterEmployee;

    public SViewDocAdminRecordSummary(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.HRSX_DOC_ADM_REC_SUM, 0, title);
        initComponents();
    }
    
    private void initComponents() {
        int levelRightDocAdminRecord = ((SClientInterface) miClient).getSessionXXX().getUser().hasRight((SClientInterface) miClient, SDataConstantsSys.PRV_HRS_DOC_BREACH).Level;
        
        setRowButtonsEnabled(false);
        
        if (levelRightDocAdminRecord == SUtilConsts.LEV_CAPTURE || levelRightDocAdminRecord == SUtilConsts.LEV_AUTHOR) { // case hypothetically restricted (not possible)!
            mnFilterAuthorId = miClient.getSession().getUser().getPkUserId();
            
            JTextField author = new JTextField(miClient.getSession().getUser().getName());
            author.setEditable(false);
            author.setFocusable(false);
            author.setPreferredSize(new Dimension(100, 23));
            author.setToolTipText("Autor");
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(author);
        }
        else {
            mnFilterAuthorId = 0;
        }
        
        moFilterYear = new SGridFilterYear(miClient, this);
        moFilterYear.initFilter(null);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterYear);
        
        moFilterEmployee = new SGridFilterPanelEmployee(miClient, this);
        moFilterEmployee.initFilter(SGridFilterPanelEmployee.EMP_STATUS_ACT);
        getPanelCommandsCustom(SGuiConsts.PANEL_LEFT).add(moFilterEmployee);
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(1);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "dar.b_del = 0 ";
        }

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_YEAR).getValue();
        if (filter != null && filter instanceof int[]) {
            sql += (sql.isEmpty() ? "" : "AND ") + "YEAR(dar.rec_dt_sta) = " + ((int[]) filter)[0] + " ";
        }

        filter = ((SGridFilterValue) moFiltersMap.get(SModConsts.HRSS_TP_PAY)).getValue();
        if (filter != null && ((int[]) filter).length == 1) {
            sql += (sql.isEmpty() ? "" : "AND ") + "emp.fk_tp_pay = " + ((int[]) filter)[0] + " ";
        }
        
        filter = ((SGridFilterValue) moFiltersMap.get(SModConsts.HRSU_DEP)).getValue();
        if (filter != null && ((int[]) filter).length == 1) {
            sql += (sql.isEmpty() ? "" : "AND ") + "dar.fk_offender_dep = " + ((int[]) filter)[0] + " ";
        }
        
        filter = ((SGridFilterValue) moFiltersMap.get(SGridFilterPanelEmployee.EMP_STATUS)).getValue();
        if (filter != null && ((int) filter) != SLibConsts.UNDEFINED) {
            if ((int)filter == SGridFilterPanelEmployee.EMP_STATUS_ACT) {
                sql += (sql.isEmpty() ? "" : "AND ") + "emp.b_act = 1 ";
            }
            else if ((int)filter == SGridFilterPanelEmployee.EMP_STATUS_INA) {
                sql += (sql.isEmpty() ? "" : "AND ") + "emp.b_act = 0 ";
            }
            else if ((int)filter == SGridFilterPanelEmployee.EMP_STATUS_ALL) {
                
            }
        }
        
        if (mnFilterAuthorId != 0) {
            sql += (sql.isEmpty() ? "" : "AND ") + "dar.fk_usr_ins = " + mnFilterAuthorId + " ";
        }
        
        msSql = "SELECT "
                + "dar.fk_emp_offender AS " + SDbConsts.FIELD_ID + "1, "
                + "emp.num, "
                + "emp.num AS " + SDbConsts.FIELD_CODE + ", "
                + "bo.bp AS " + SDbConsts.FIELD_NAME + ", "
                + "dep.name AS _dep_name, "
                + "pos.name AS _pos_name, "
                + "COUNT(*) AS _count, "
                + "SUM(CASE WHEN dar.filevault_id <> '' THEN 1 ELSE 0 END) AS _count_done "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_DOC_ADM_REC) + " AS dar "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bo ON bo.id_bp = dar.fk_emp_offender "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS emp ON emp.id_emp = dar.fk_emp_offender "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_DEP) + " AS dep ON dep.id_dep = emp.fk_dep "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_POS) + " AS pos ON pos.id_pos = emp.fk_pos "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "GROUP BY dar.fk_emp_offender, bo.bp, emp.num, dep.name, pos.name "
                + "ORDER BY bo.bp, emp.num, dar.fk_emp_offender ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, SDbConsts.FIELD_NAME, "Empleado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_BPR, SDbConsts.FIELD_CODE, "Clave"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "_dep_name", "Depto. empleado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "_pos_name", "Puesto empleado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_RAW, "_count", "Actas administrativas"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_RAW, "_count_done", "Formalizadas"));

        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(SModConsts.HRS_DOC_ADM_REC);
        moSuscriptionsSet.add(SModConsts.HRSU_EMP);
        moSuscriptionsSet.add(SModConsts.HRSU_DEP);
        moSuscriptionsSet.add(SModConsts.HRSU_POS);
    }
}
