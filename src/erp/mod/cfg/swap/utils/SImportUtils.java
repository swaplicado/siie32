/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.utils;

import erp.SFileUtilities;
import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.mod.SModConsts;
import erp.mtrn.data.SDataDps;
import erp.mtrn.data.cfd.SCfdRenderer;
import erp.mtrn.form.SDialogDpsFinder;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public abstract class SImportUtils {
    
    /**
     * Create prepared statement to count imports:
     * Index:   Parameter:
     *      1   Sync type.
     *      2   Response code.
     *      3   User ID.
     *      4   Entry response code.
     *      5   Reference ID.
     * @param session
     * @return Prepared statement.
     * @throws Exception 
     */
    public static PreparedStatement createPreparedStatementToCountImports(final SGuiSession session) throws Exception {
        String sql = "SELECT COUNT(*) "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.CFG_COM_IMP_LOG) + " AS il "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFG_COM_IMP_LOG_ETY) + " AS ile ON ile.id_sync_log = il.id_sync_log "
                + "WHERE il.sync_type = ? AND il.response_code = ? AND il.fk_usr = ? AND ile.response_code = ? AND ile.reference_id = ?;";
        
        return session.getStatement().getConnection().prepareStatement(sql);
    }
    
    /**
     * Count imports.
     * @param preparedStatement
     * @param syncType
     * @param responseCode
     * @param userId
     * @param referenceId
     * @throws Exception 
     */
    public static int countImports(final PreparedStatement preparedStatement, final String syncType, final String responseCode, final int userId, final String referenceId) throws Exception {
        int count = 0;
        
        preparedStatement.setString(1, syncType);
        preparedStatement.setString(2, responseCode);
        preparedStatement.setInt(3, userId);
        preparedStatement.setString(4, responseCode);
        preparedStatement.setString(5, referenceId);
        
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        }
        
        return count;
    }
    
    /**
     * Import and create a new invoice.
     * @param client GUI client.
     * @param isPurchase Is-purchase flag.
     * @param dialogDpsFinder DPS Finder dialog.
     * @param cfdiXml XML CFDI file. Can be <code>null</code>. When it is <code>null</code>, then a file is required in an open dialog.
     * @param linkToOrder Ask-for-order flag.
     * @param orderRequired Required order. Can be <code>null</code>. When it is <code>null</code> and an order must to be linked, then an order is required in DPS Finder dialog.
     * @throws java.lang.Exception
     */
    public static void importCfdi(final SClientInterface client, final boolean isPurchase, final SDialogDpsFinder dialogDpsFinder, final File cfdiXml, final boolean linkToOrder, final SDataDps orderRequired) throws Exception {
        SDataDps order = null; 

        if (linkToOrder) {
            if (orderRequired != null) {
                order = orderRequired;
            }
            else {
                int[] orderTypeKey = isPurchase ? SDataConstantsSys.TRNS_CL_DPS_PUR_ORD : SDataConstantsSys.TRNS_CL_DPS_SAL_ORD;

                dialogDpsFinder.formReset();
                dialogDpsFinder.setValue(SLibConstants.VALUE_FILTER_KEY, orderTypeKey);
                dialogDpsFinder.setVisible(true);

                if (dialogDpsFinder.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                    order = (SDataDps) dialogDpsFinder.getValue(SDataConstants.TRN_DPS);
                }
            }
        }

        FileFilter filter = SFileUtilities.createFileNameExtensionFilter(SFileUtilities.XML);
        client.getFileChooser().repaint();
        client.getFileChooser().setAcceptAllFileFilterUsed(false);
        client.getFileChooser().setFileFilter(filter);
        
        if (!linkToOrder || (linkToOrder && order != null)) {
            if (client.getFileChooser().showOpenDialog(client.getFrame()) == JFileChooser.APPROVE_OPTION) {
                File file = client.getFileChooser().getSelectedFile();

                if (file.getName().toLowerCase().contains("." + SFileUtilities.XML)) {
                    SCfdRenderer renderer = new SCfdRenderer(client);
                    SDataDps dpsRendered = renderer.renderCfdi(file, order, isPurchase ? SDataConstantsSys.BPSS_CT_BP_SUP : SDataConstantsSys.BPSS_CT_BP_CUS);
                    
                    int module = isPurchase ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL;
                    int[] invoiceTypeKey = isPurchase ? SDataConstantsSys.TRNU_TP_DPS_PUR_INV : SDataConstantsSys.TRNU_TP_DPS_SAL_INV;

                    if (dpsRendered != null) {
                        client.getGuiModule(module).setFormComplement(new Object[] { invoiceTypeKey, false }); // document type key, is imported
                        client.getGuiModule(module).setAuxRegistry(dpsRendered);

                        if (client.getGuiModule(module).showForm(SDataConstants.TRN_DPS, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                            client.getGuiModule(module).refreshCatalogues(SDataConstants.TRN_DPS);
                            SDataUtilities.showDpsRecord(client, (SDataDps) client.getGuiModule(module).getRegistry());
                        }
                    }
                }
                else {
                    client.showMsgBoxInformation("El archivo proporcionado debe ser XML.\n"
                            + "(Archivo proporcionado: '" + file.getName() + "')");
                }
            }

            client.getFileChooser().resetChoosableFileFilters();
        }
    }
}
