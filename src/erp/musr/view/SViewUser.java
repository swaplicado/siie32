/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.musr.view;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.musr.form.SFormExportUser;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import sa.gui.util.SUtilConsts;
import sa.lib.gui.SGuiConsts;
import java.util.Vector;

/**
 *
 * @author Sergio Flores
 */
public class SViewUser extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private javax.swing.JButton jbCopy;
    private javax.swing.JButton jbExport;
    
    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;

    public SViewUser(erp.client.SClientInterface client, java.lang.String tabTitle) {
        super(client, tabTitle, SDataConstants.USRU_USR);
        initComponents();
    }

    private void initComponents() {
        int i;
        int levelRightEdit = SDataConstantsSys.UNDEFINED;

        moTabFilterDeleted = new STabFilterDeleted(this);

        jbCopy = new JButton(miClient.getImageIcon(SLibConstants.ICON_COPY));
        jbCopy.setPreferredSize(new Dimension(23, 23));
        jbCopy.addActionListener(this);
        jbCopy.setToolTipText("Copiar usuario");

        addTaskBarUpperComponent(jbCopy);
        
        jbExport = new JButton(miClient.getImageIcon(SLibConstants.ICON_ARROW_UP));
        jbExport.setPreferredSize(new Dimension(23, 23));
        jbExport.addActionListener(this);
        jbExport.setToolTipText("Exportar usuario");

        addTaskBarUpperComponent(jbExport);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);

        //jbDelete.setEnabled(false);

        erp.lib.table.STableField[] aoKeyFields = new STableField[1];
        erp.lib.table.STableColumn[] aoTableColumns = new STableColumn[13];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "u.id_usr");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "u.usr", "Usuario", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "u.email", "Email", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "u.b_univ", "Acceso universal", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "u.b_can_edit", "Modificable", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "u.b_can_del", "Eliminable", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "u.b_act", "Activo", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "u.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "u.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "u.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "u.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        levelRightEdit = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_USR).Level;

        jbNew.setEnabled(levelRightEdit >=  SUtilConsts.LEV_AUTHOR);
        jbEdit.setEnabled(levelRightEdit >=  SUtilConsts.LEV_AUTHOR);

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.USRX_RIGHT);

        populateTable();
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {
            if (miClient.getGuiModule(SDataConstants.GLOBAL_CAT_USR).showForm(mnTabType, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(SDataConstants.GLOBAL_CAT_USR).refreshCatalogues(mnTabType);
            }
        }
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (!moTablePane.getSelectedTableRow().getIsEditable()) {
                    miClient.showMsgBoxWarning(STableConstants.MSG_WAR_REGISTRY_NO_EDITABLE);
                }
                else {
                    if (miClient.getGuiModule(SDataConstants.GLOBAL_CAT_USR).showForm(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                        miClient.getGuiModule(SDataConstants.GLOBAL_CAT_USR).refreshCatalogues(mnTabType);
                    }
                }
            }
        }
    }

    @Override
    public void actionDelete() {
        if (jbDelete.isEnabled()) {

        }
    }

    private void actionCopy() {
        if (jbCopy.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null || moTablePane.getSelectedTableRow().getIsSummary()) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                if (miClient.getGuiModule(SDataConstants.GLOBAL_CAT_USR).showFormForCopy(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(SDataConstants.GLOBAL_CAT_USR).refreshCatalogues(mnTabType);
                }
            }
        }
    }
    
    public int showMsgBoxConfirm(String msg) {
        return JOptionPane.showConfirmDialog(this, msg, SGuiConsts.MSG_BOX_CONFIRM, JOptionPane.YES_NO_OPTION);
    }
    
    private void actionExport() {
        if (jbExport.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null || moTablePane.getSelectedTableRow().getIsSummary()) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                int[] oUser = (int[]) moTablePane.getSelectedTableRow().getPrimaryKey();
                SFormExportUser moSFormExportUser = new SFormExportUser((SClientInterface) miClient);
                moSFormExportUser.setValue(oUser[0], null);
                moSFormExportUser.show();
            }
        }
    }

    @Override
    public void createSqlQuery() {
        java.lang.String sqlWhere = "";
        erp.lib.table.STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "u.b_del = FALSE ";
            }
        }

        msSql = "SELECT u.id_usr, u.email, u.usr, u.b_univ, u.b_can_edit, u.b_can_del, u.b_act, u.b_del, u.b_can_edit AS " + STableConstants.FIELD_IS_EDITABLE + ", " +
                "u.ts_new, u.ts_edit, u.ts_del, un.usr, ue.usr, ud.usr " +
                "FROM erp.usru_usr AS u " +
                "INNER JOIN erp.usru_usr AS un ON " +
                "u.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON " +
                "u.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON " +
                "u.fid_usr_del =  ud.id_usr " +
                (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) +
                "ORDER BY u.usr, u.id_usr ";
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == jbCopy) {
                actionCopy();
            }
            else if (button == jbExport) {
                actionExport();
            }
        }
    }
}
