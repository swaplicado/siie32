/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.table.STabFilterDate;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.mtrn.form.SDialogStampDetail;
import javax.swing.JButton;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SViewStampAvailable extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STableColumn[] aoTableColumns;
    private erp.lib.table.STableField[] aoKeyFields;
    private erp.lib.table.STabFilterDate moTabFilterDate;
    private JButton jbDetail;
    private SDialogStampDetail moDialogStampDetail;

    protected java.util.Date mtDateEnd;

    public SViewStampAvailable(erp.client.SClientInterface client, java.lang.String tabTitle) {
        super(client, tabTitle, SDataConstants.TRNX_STAMP_AVL);
        initComponents();
    }

    private void initComponents() {
        int i;

        moTabFilterDate = new STabFilterDate(miClient, this);
        moDialogStampDetail = new SDialogStampDetail(miClient);

        jbDetail = new javax.swing.JButton();
        jbDetail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_kardex.gif"))); // NOI18N
        jbDetail.setToolTipText("Ver movimientos de timbrado [Ctrl+K]");
        jbDetail.setPreferredSize(new java.awt.Dimension(23, 23));

        removeTaskBarUpperComponent(jbNew);
        removeTaskBarUpperComponent(jbEdit);
        removeTaskBarUpperComponent(jbDelete);
        addTaskBarUpperComponent(moTabFilterDate);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(jbDetail);

        jbDetail.addActionListener(this);

        aoKeyFields = new STableField[2];
        aoTableColumns = new STableColumn[2];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "v.id_year");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "v.id_pac");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }
        
        i = 0;

        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "vt.pac", "PAC", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "stock", "Timbres disponibles", 110);

        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        setIsSummaryApplying(true);

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.TRN_DPS);
        mvSuscriptors.add(SDataConstants.TRN_SIGN);

        populateTable();

        moTablePane.setDoubleClickAction(this, "actionDetails");
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {
            if (miClient.getGuiModule(SDataConstants.MOD_SAL).showForm(mnTabType, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(mnTabType);
            }
        }
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (miClient.getGuiModule(SDataConstants.MOD_SAL).showForm(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(mnTabType);
                }
            }
        }
    }

    @Override
    public void actionDelete() {
        if (jbDelete.isEnabled()) {

	}
    }

    public void actionDetails() {
        if (jbDetail.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                moDialogStampDetail.refreshStampDetail();
                moDialogStampDetail.showStampDetail(((int[]) moTablePane.getSelectedTableRow().getPrimaryKey())[0], ((int[]) moTablePane.getSelectedTableRow().getPrimaryKey())[1], mtDateEnd);
            }
	}
    }
    @Override
    public void createSqlQuery() {
        STableSetting setting = null;
        String sqlWhereDate = "";

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
               sqlWhereDate += "AND id_year = " + SLibTimeUtilities.digestYear(new java.sql.Date(((java.util.Date) setting.getSetting()).getTime()))[0] +
               " AND dt <= '" + (new java.sql.Date(((java.util.Date) setting.getSetting()).getTime())) + "' ";
               mtDateEnd = (new java.sql.Date(((java.util.Date) setting.getSetting()).getTime()));
            }
        }

        msSql = "SELECT v.id_year, v.id_pac, vt.pac, (SUM(v.mov_in) - SUM(v.mov_out)) AS stock " +
                "FROM trn_sign AS v " +
                "INNER JOIN trn_pac AS vt ON v.id_pac = vt.id_pac " +
                "WHERE v.b_del = 0 AND vt.b_pre_pay = 1 " + sqlWhereDate + " " +
                "GROUP BY v.id_pac " +
                "ORDER BY v.id_pac, vt.pac ";
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();

            if (button == jbDetail) {
                actionDetails();
            }
        }
    }
}
