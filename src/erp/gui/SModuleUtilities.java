/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.gui;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.table.STableRow;
import erp.mtrn.data.SDataDiog;
import erp.mtrn.data.SDataDps;
import erp.mtrn.form.SDialogShowDocumentLinks;
import erp.mtrn.form.SDialogShowDocumentNotes;

/**
 *
 * @author Sergio Flores, Uriel Castañeda
 */
public abstract class SModuleUtilities {
    
    /**
     * Shows document notes.
     * @param client GUI Client.
     * @param documentType Document type, constans defined in <code>SDataConstants</code>.
     * @param tableRow View's selected table row.
     */
    public static void showDocumentNotes(final SClientInterface client, final int documentType, final STableRow tableRow) {
        if (tableRow == null) {
            client.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
        }
        else {
            showDocumentNotes(client, documentType, tableRow.getPrimaryKey());
        }
    }
    
    /**
     * Shows document notes.
     * @param client GUI Client.
     * @param documentType Document type, constans defined in <code>SDataConstants</code>.
     * @param documentKey Document's primary key.
     */
    public static void showDocumentNotes(final SClientInterface client, final int documentType, final Object documentKey) {
        int notes = 0;
        SDataDps dps = null;
        SDataDiog diog = null;
        SDialogShowDocumentNotes dialog = null;
        
        switch (documentType) {
            case SDataConstants.TRN_DPS:
                dps = (SDataDps) SDataUtilities.readRegistry(client, SDataConstants.TRN_DPS, documentKey, SLibConstants.EXEC_MODE_VERBOSE);
                notes = dps.getDbmsDpsNotes().size();
                break;
            case SDataConstants.TRN_DIOG:
                diog = (SDataDiog) SDataUtilities.readRegistry(client, SDataConstants.TRN_DIOG, documentKey, SLibConstants.EXEC_MODE_VERBOSE);
                notes = diog.getDbmsNotes().size();
                break;
            default:
        }

        if (notes == 0) {
            client.showMsgBoxInformation(SLibConstants.MSG_INF_NO_NOTES_DPS);
        }
        else {
            dialog = new SDialogShowDocumentNotes(client, documentType);
            dialog.formReset();
            dialog.setParamDocumentKey(documentKey);
            dialog.setFormVisible(true);
        }
    }
    
    /**
     * Shows document processing links.
     * @param client GUI Client.
     * @param tableRow View's selected table row.
     */
    public static void showDocumentLinks(final SClientInterface client, final STableRow tableRow) {
        int links = 0;
        SDataDps dps = null;
        SDialogShowDocumentLinks dialog = null;

        if (tableRow == null) {
            client.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
        }
        else {
            dps = (SDataDps) SDataUtilities.readRegistry(client, SDataConstants.TRN_DPS, tableRow.getPrimaryKey(), SLibConstants.EXEC_MODE_VERBOSE);

            dialog = new SDialogShowDocumentLinks(client);
            dialog.formReset();
            dialog.setValue(SDataConstants.TRN_DPS, dps);
            links = dialog.readLinks();

            if (links == 0) {
                client.showMsgBoxInformation(SLibConstants.MSG_INF_NO_LINK_DPS);
            }
            else {
                dialog.setFormVisible(true);
            }
        }
    }
}
