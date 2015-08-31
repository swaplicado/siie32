/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.data.SProcConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.data.SDataSqlUtilities;
import erp.lib.form.SFormUtilities;
import erp.lib.table.STabFilterDatePeriod;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.*;

/**
 *
 * @author Alfonso Flores
 */
public class SViewCtr extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private javax.swing.JButton mjbAnnul;
    private javax.swing.JButton mjbPrint;
    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;
    private erp.lib.table.STabFilterDatePeriod moTabFilterDatePeriod;

    public SViewCtr(erp.client.SClientInterface client, java.lang.String tabTitle) {
        super(client, tabTitle, SDataConstants.TRN_CTR);
        initComponents();
    }

    private void initComponents() {
        int i;

        mjbAnnul = new JButton(miClient.getImageIcon(SLibConstants.ICON_ANNUL));
        mjbAnnul.setPreferredSize(new Dimension(23, 23));
        mjbAnnul.addActionListener(this);
        mjbAnnul.setToolTipText("Anular documento");

        mjbPrint = new JButton(miClient.getImageIcon(SLibConstants.ICON_PRINT));
        mjbPrint.setPreferredSize(new Dimension(23, 23));
        mjbPrint.addActionListener(this);
        mjbPrint.setToolTipText("Imprimir documento");

        moTabFilterDeleted = new STabFilterDeleted(this);
        moTabFilterDatePeriod = new STabFilterDatePeriod(miClient, this, SLibConstants.GUI_DATE_AS_YEAR_MONTH);

        addTaskBarUpperComponent(mjbAnnul);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDatePeriod);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(mjbPrint);

        jbNew.setEnabled(true);
        jbEdit.setEnabled(true);
        jbDelete.setEnabled(false);

        STableField[] aoKeyFields = new STableField[2];
        STableColumn[] aoTableColumns = new STableColumn[13];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "c.id_year");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "c.id_doc");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "c.dt", "Fecha doc.", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "c.dt_pay", "Fecha pago", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "c.num", "Folio doc.", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Asociado negocios", 200);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "c.tot_r", "Total $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "st.st_dps", "Estatus", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "c.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "c.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "c.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "c.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);

        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        SFormUtilities.createActionMap(this, this, "actionPrint", "print", KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK);

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }

    @Override
    public void createSqlQuery() {
        String sqlWhere = "";
        String sqlDatePeriod = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "c.b_del = FALSE ";
            }
            else if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                sqlDatePeriod += SDataSqlUtilities.composePeriodFilter((int[]) setting.getSetting(), "c.dt");
            }
        }

        msSql = "SELECT c.id_year, c.id_doc, c.dt, c.dt_pay, c.num, c.tot_r, c.b_del, b.bp, st.st_dps," +
                "c.ts_new, c.ts_edit, c.ts_del, un.usr, ue.usr, ud.usr " +
                "FROM trn_ctr AS c " +
                "INNER JOIN erp.bpsu_bp AS b ON c.fid_bp = b.id_bp " +
                "INNER JOIN erp.trns_st_dps AS st ON c.fid_st_dps = st.id_st_dps " +
                "INNER JOIN erp.usru_usr AS un ON c.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON c.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON c.fid_usr_del = ud.id_usr " +
                "WHERE " + sqlDatePeriod + " " +
                (sqlWhere.length() == 0 ? "" : " AND " + sqlWhere) +
                "ORDER BY c.dt, c.num, c.id_year, c.id_doc ";
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {
            if (miClient.getGuiModule(SDataConstants.MOD_FIN).showForm(SDataConstants.TRN_CTR , mnTabTypeAux01, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(SDataConstants.MOD_FIN).refreshCatalogues(mnTabType);
            }
        }
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (miClient.getGuiModule(SDataConstants.MOD_FIN).showForm(SDataConstants.TRN_CTR, mnTabTypeAux01, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(SDataConstants.MOD_FIN).refreshCatalogues(mnTabType);
                }
            }
        }
    }

    @Override
    public void actionDelete() {
        if (jbDelete.isEnabled()) {

        }
    }

    public void actionAnnul() {
        Vector<Object> params = new Vector<Object>();

        if (mjbAnnul.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (miClient.showMsgBoxConfirm(SLibConstants.MSG_CNF_REG_ANNUL) == JOptionPane.YES_OPTION) {
                    params.clear();
                    params.add(((int[])moTablePane.getSelectedTableRow().getPrimaryKey())[0]);
                    params.add(((int[])moTablePane.getSelectedTableRow().getPrimaryKey())[1]);
                    params.add(SDataConstantsSys.TRNS_ST_DPS_ANNULED);
                    params.add(miClient.getSession().getUser().getPkUserId());
                    params = SDataUtilities.callProcedure(miClient, SProcConstants.TRN_CTR_UPD_ST, params, SLibConstants.EXEC_MODE_SILENT);

                    miClient.getGuiModule(SDataConstants.MOD_FIN).refreshCatalogues(mnTabType);
                }
            }
        }
    }

    public void actionPrint() {
        Cursor cursor = getCursor();
        Map<String, Object> map = null;
        JasperPrint jasperPrint = null;
        JasperViewer jasperViewer = null;

        if (mjbPrint.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                try {
                    setCursor(new Cursor(Cursor.WAIT_CURSOR));

                    map = miClient.createReportParams();
                    map.put("nIdYear", ((int[])moTablePane.getSelectedTableRow().getPrimaryKey())[0]);
                    map.put("nIdDoc", ((int[])moTablePane.getSelectedTableRow().getPrimaryKey())[1]);

                    jasperPrint = SDataUtilities.fillReport(miClient, SDataConstantsSys.REP_TRN_CTR, map);
                    jasperViewer = new JasperViewer(jasperPrint, false);
                    jasperViewer.setTitle("Impresión de contrarrecibo");
                    jasperViewer.setVisible(true);
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
                finally {
                    setCursor(cursor);
                }
            }
        }
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();

            if (button == mjbAnnul) {
                actionAnnul();
            }
            if (button == mjbPrint) {
                actionPrint();
            }
        }
    }
}
