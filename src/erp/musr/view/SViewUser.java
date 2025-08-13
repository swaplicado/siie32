/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.musr.view;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.mcfg.data.SCfgUtils;
import erp.mod.cfg.db.SSyncType;
import erp.mod.cfg.swap.SHttpConsts;
import erp.mod.cfg.swap.SSwapConsts;
import erp.mod.cfg.swap.utils.SExportUtils;
import erp.musr.form.SFormExportUser;
import erp.siieapp.SUserExportUtils;
import java.awt.Dimension;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import sa.gui.util.SUtilConsts;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;

/**
 *
 * @author Sergio Flores, Edwin Carmona, Claudio Peña, Sergio Flores
 */
public class SViewUser extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private javax.swing.JButton jbCopy;
    private javax.swing.JButton jbSiieAppExport;
    private javax.swing.JButton jbSiieAppSync;
    private javax.swing.JButton jbExportDataToSwapServices;

    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;

    private boolean mbSwapServicesLinkUp;
    
    public SViewUser(erp.client.SClientInterface client, java.lang.String tabTitle) {
        super(client, tabTitle, SDataConstants.USRU_USR);
        initComponents();
    }

    private void initComponents() {
        // Initialize commands:
        
        jbCopy = new JButton(miClient.getImageIcon(SLibConstants.ICON_COPY));
        jbCopy.setPreferredSize(new Dimension(23, 23));
        jbCopy.addActionListener(this);
        jbCopy.setToolTipText("Copiar usuario");

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(jbCopy);

        moTabFilterDeleted = new STabFilterDeleted(this);
        
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);
        
        try {
           String siieAppUrls = SCfgUtils.getParamValue(miClient.getSession().getStatement(), SDataConstantsSys.CFG_PARAM_SIIE_APP_URLS);
           
            if (!siieAppUrls.isEmpty()) {
                addTaskBarUpperSeparator();
        
                jbSiieAppExport = new JButton(miClient.getImageIcon(SLibConstants.ICON_ARROW_UP));
                jbSiieAppExport.setPreferredSize(new Dimension(23, 23));
                jbSiieAppExport.addActionListener(this);
                jbSiieAppExport.setToolTipText("Exportar usuario a SIIE App");

                addTaskBarUpperComponent(jbSiieAppExport);
                
                jbSiieAppSync = new JButton(miClient.getImageIcon(SLibConstants.ICON_LINK));
                jbSiieAppSync.setPreferredSize(new Dimension(23, 23));
                jbSiieAppSync.addActionListener(this);
                jbSiieAppSync.setToolTipText("Sincronizar usuarios con SIIE App");

                addTaskBarUpperComponent(jbSiieAppSync);
            }
        }
        catch (Exception e) {
            Logger.getLogger(SViewUser.class.getName()).log(Level.SEVERE, null, e);
        }

        // Enable SWAP Services:
        mbSwapServicesLinkUp = (boolean) miClient.getSwapServicesSetting(SSwapConsts.CFG_NVP_LINK_UP);
        if (mbSwapServicesLinkUp) {
            jbExportDataToSwapServices = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_move_up_ind.gif")), "Exportar usuarios a " + SSwapConsts.SWAP_SERVICES, this);

            addTaskBarUpperSeparator();
            addTaskBarUpperComponent(jbExportDataToSwapServices);
        }

        // Initialize table:

        erp.lib.table.STableField[] aoKeyFields = new STableField[1];
        erp.lib.table.STableColumn[] aoTableColumns = new STableColumn[mbSwapServicesLinkUp ? 21 : 18];

        int i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "u.id_usr");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "u.usr", "Usuario", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "u.email", "Correo-e usuario", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Empleado", 250);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bbc.email_02", "Correo-e empresa empleado", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "e.b_act", "Activo empleado", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "u.b_univ", "Acceso universal", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "u.b_can_edit", "Modificable", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "u.b_can_del", "Eliminable", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "u.b_act", "Cuenta activa", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "u.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "u.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "u.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "u.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_siie_app_usr_last_sync", "SIIE App usr. últ. sincronización", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "_siie_app_ts_last_sync", "SIIE App últ. sincronización", STableConstants.WIDTH_DATE_TIME);
        
        if (mbSwapServicesLinkUp) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "_ss_is_exp", SSwapConsts.SWAP_SERVICES + " exportado",  STableConstants.WIDTH_BOOLEAN_2X);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tss._ss_usr", SSwapConsts.SWAP_SERVICES + " usr. últ. exportación", STableConstants.WIDTH_USER);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "tss._ss_resp_ts", SSwapConsts.SWAP_SERVICES + " últ. exportación", STableConstants.WIDTH_DATE_TIME);
        }
        
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        int levelRightEdit = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_USR).Level;

        jbNew.setEnabled(levelRightEdit >= SUtilConsts.LEV_AUTHOR);
        jbEdit.setEnabled(levelRightEdit >= SUtilConsts.LEV_AUTHOR);
        //jbDelete.setEnabled(false);

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.TRN_USR_CFG);
        mvSuscriptors.add(SDataConstants.USRX_RIGHT);

        populateTable();
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

    private void actionSiieAppExport() {
        if (jbSiieAppExport != null && jbSiieAppExport.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null || moTablePane.getSelectedTableRow().getIsSummary()) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                int[] pkUser = (int[]) moTablePane.getSelectedTableRow().getPrimaryKey();
                SFormExportUser moSFormExportUser = new SFormExportUser((SClientInterface) miClient);
                moSFormExportUser.setValue(pkUser[0], null);
                moSFormExportUser.setVisible(true);
                if (moSFormExportUser.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                    miClient.getGuiModule(SDataConstants.GLOBAL_CAT_USR).refreshCatalogues(mnTabType);
                }
            }
        }
    }

    private void actionSiieAppSync() {
        if (jbSiieAppSync != null && jbSiieAppSync.isEnabled()) {
            SUserExportUtils oExport = new SUserExportUtils((SGuiClient) miClient);
            oExport.SynchronizeExternal();
            miClient.getGuiModule(SDataConstants.GLOBAL_CAT_USR).refreshCatalogues(mnTabType);
        }
    }

    private void actionExportDataToSwapServices() {
        if (jbExportDataToSwapServices != null && jbExportDataToSwapServices.isEnabled()) {
            try {
                String response = SExportUtils.exportData(miClient.getSession(), SSyncType.USER);
                
                if (response.isEmpty()) {
                    miClient.showMsgBoxInformation("Los usuarios fueron exportados correctamente a " + SSwapConsts.SWAP_SERVICES + ".");
                    miClient.getGuiModule(SDataConstants.GLOBAL_CAT_USR).refreshCatalogues(mnTabType);
                }
                else {
                    miClient.showMsgBoxInformation("Ocurrió un problema al exportar los usuarios a " + SSwapConsts.SWAP_SERVICES + ":\n" + response);
                }
            }
            catch (Exception e) {
                SLibUtilities.printOutException(this, e);
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
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "NOT u.b_del ";
            }
        }

        msSql = "SELECT u.id_usr, u.email, u.usr, u.b_univ, u.b_can_edit, u.b_can_del, u.b_act, u.b_del, u.b_can_edit AS " + STableConstants.FIELD_IS_EDITABLE + ", "
                + "u.ts_new, u.ts_edit, u.ts_del, un.usr, ue.usr, ud.usr, "
                + "b.bp, bbc.email_02, e.b_act, "
                + "uls.usr AS _siie_app_usr_last_sync, u.ts_last_sync_n AS _siie_app_ts_last_sync"
                + (!mbSwapServicesLinkUp ? "" : ", tss.reference_id IS NOT NULL AS _ss_is_exp, tss._ss_usr, tss._ss_resp_ts") + " "
                + "FROM erp.usru_usr AS u "
                + "INNER JOIN erp.usru_usr AS un ON u.fid_usr_new = un.id_usr "
                + "INNER JOIN erp.usru_usr AS ue ON u.fid_usr_edit = ue.id_usr "
                + "INNER JOIN erp.usru_usr AS ud ON u.fid_usr_del =  ud.id_usr "
                + "LEFT OUTER JOIN erp.bpsu_bp AS b ON b.id_bp = u.fid_bp_n AND b.b_att_emp "
                + "LEFT OUTER JOIN erp.bpsu_bpb AS bb ON bb.fid_bp = b.id_bp AND bb.fid_tp_bpb = " + SDataConstantsSys.BPSS_TP_BPB_HQ + " "
                + "LEFT OUTER JOIN erp.bpsu_bpb_con AS bbc ON bbc.id_bpb = bb.id_bpb AND bbc.id_con = " + SUtilConsts.BRA_CON_ID + " "
                + "LEFT OUTER JOIN erp.hrsu_emp AS e ON e.id_emp = b.id_bp "
                + "/* SIIE App Sync Log: */ "
                + "LEFT OUTER JOIN erp.usru_usr AS uls ON uls.id_usr = u.fid_usr_last_sync_n "
                + (!mbSwapServicesLinkUp ? "" : "/* SWAP Services Sync Log: */ "
                + "LEFT OUTER JOIN (SELECT sle.reference_id, u.usr AS _ss_usr, MAX(sl.response_timestamp) AS _ss_resp_ts "
                + "FROM erp.cfg_sync_log AS sl "
                + "INNER JOIN erp.cfg_sync_log_ety AS sle ON sle.id_sync_log = sl.id_sync_log "
                + "INNER JOIN erp.usru_usr AS u ON u.id_usr = sl.fk_usr "
                + "WHERE sl.sync_type = '" + SSyncType.USER + "' "
                + "AND (sle.response_code = '" + SHttpConsts.RSC_SUCC_OK + "' OR sle.response_code = '" + SHttpConsts.RSC_SUCC_CREATED + "') "
                + "GROUP BY sle.reference_id, u.usr "
                + "ORDER BY CONVERT(sle.reference_id, UNSIGNED)) AS tss ON tss.reference_id = CONVERT(u.id_usr, CHAR) ")
                + (sqlWhere.isEmpty() ? "" : "WHERE " + sqlWhere)
                + "ORDER BY u.usr, u.id_usr ";
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

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbCopy) {
                actionCopy();
            }
            else if (button == jbSiieAppExport) {
                actionSiieAppExport();
            }
            else if (button == jbSiieAppSync) {
                actionSiieAppSync();
            }
            else if (button == jbExportDataToSwapServices) {
                actionExportDataToSwapServices();
            }
        }
    }
}
