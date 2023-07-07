/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mfin.view;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.table.STabFilterDatePeriod;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.mfin.form.SFormCopyBankNbDays;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.grid.SGridUtils;

/**
 *
 * @author SW
 */
public class SViewBankNbDay extends erp.lib.table.STableTab implements java.awt.event.ActionListener{

    private JButton jbCopyNbDays;
    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;
    private erp.lib.table.STabFilterDatePeriod moTabFilterDatePeriod;
    
    private SFormCopyBankNbDays moFormCopyBankNbDays;
    
    public SViewBankNbDay(SClientInterface client, String tabTitle) {
        super(client, tabTitle, SDataConstants.FINU_BANK_NB_DAY);
        initComponents();
    }
    
    private void initComponents() {
        int i;
        int levelRightEdit = SDataConstantsSys.UNDEFINED;

        moTabFilterDeleted = new STabFilterDeleted(this);
        moTabFilterDatePeriod = new STabFilterDatePeriod(miClient, this, SLibConstants.GUI_DATE_AS_YEAR);

        jbCopyNbDays = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_dps_link_rev.gif")), "Copiar días inhábiles bancarios de años anteriores", this);
        
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDatePeriod);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(jbCopyNbDays);

        STableField[] aoKeyFields = new STableField[1];
        STableColumn[] aoTableColumns = new STableColumn[7];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "b.id_bank_nb_day");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "b.nb_day", "Fecha", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.name", "Nombre", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "b.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "b.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "b.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        levelRightEdit = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_FIN_EXC_RATE).Level;

        jbNew.setEnabled(levelRightEdit >= SUtilConsts.LEV_AUTHOR);
        jbEdit.setEnabled(levelRightEdit >= SUtilConsts.LEV_AUTHOR);
        //jbDelete.setEnabled(false);

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.CFGU_CUR);
        mvSuscriptors.add(SDataConstants.USRU_USR);
        
        populateTable();
    }
    
    private void actionCopyNbDays() {
       if (jbCopyNbDays.isEnabled()) {
            try {
                moFormCopyBankNbDays = new SFormCopyBankNbDays(miClient);
                moFormCopyBankNbDays.setVisible(true);
                createSqlQuery();
                populateTable();
                
            }
            catch (Exception e) {
                SLibUtils.showException(this, e);
            }
        }    
    }

    @Override
    public void createSqlQuery() {
        int year;
        String sqlWhere = "";
        STableSetting setting = null;
        
        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "b.b_del = FALSE ";
            }
            if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                year = ((int[]) setting.getSetting())[0];
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND " ) + "YEAR(b.nb_day) = " + year + " ";
            }
        }
        
        msSql = "SELECT b.id_bank_nb_day, b.nb_day, b.name, b.b_del, b.ts_new, b.ts_edit, un.usr, ue.usr " +
                "FROM erp.finu_bank_nb_day AS b " +
                "INNER JOIN erp.usru_usr AS un ON " +
                "b.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON " +
                "b.fid_usr_edit = ue.id_usr " +
                (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere);
    }
    
    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {
            if (miClient.getGuiModule(SDataConstants.MOD_FIN).showForm(mnTabType, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(SDataConstants.MOD_FIN).refreshCatalogues(mnTabType);
            }
        }
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (miClient.getGuiModule(SDataConstants.MOD_FIN).showForm(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(SDataConstants.MOD_FIN).refreshCatalogues(mnTabType);
                }
            }
        }
    }

    @Override
    public void actionDelete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbNew) {
                actionNew();
            }
            else if (button == jbEdit) {
                actionEdit();
            }
            else if (button == jbCopyNbDays) {
                actionCopyNbDays();
            }
        }
    }
}
