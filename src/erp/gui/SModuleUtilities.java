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
import erp.mfin.data.SDataRecord;
import erp.mfin.data.SDataRecordEntry;
import erp.mfin.form.SDialogShowRecordCfd;
import erp.mtrn.data.SDataDiog;
import erp.mtrn.data.SDataDps;
import erp.mtrn.form.SDialogShowDocumentLinks;
import erp.mtrn.form.SDialogShowDocumentNotes;
import java.awt.Cursor;
import java.sql.ResultSet;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiConsts;

/**
 *
 * @author Sergio Flores, Uriel Castañeda, Claudio Peña, Isabel Servín
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
     * @param dpsKey Primary key of desired document.
     */
    public static void showDocumentLinks(final SClientInterface client, final Object dpsKey) {
        int links = 0;
        SDataDps dps = null;
        SDialogShowDocumentLinks dialog = null;

        if (dpsKey == null) {
            client.showMsgBoxInformation(SGuiConsts.ERR_MSG_UNDEF_REG);
        }
        else {
            dps = (SDataDps) SDataUtilities.readRegistry(client, SDataConstants.TRN_DPS, dpsKey, SLibConstants.EXEC_MODE_VERBOSE);

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
    
    /**
     * Shows document processing links.
     * @param client GUI Client.
     * @param tableRow View's selected table row.
     */
    public static void showDocumentLinks(final SClientInterface client, final STableRow tableRow) {
        if (tableRow == null) {
            client.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
        }
        else {
            showDocumentLinks(client, tableRow.getPrimaryKey());
        }
    }
    
    /**
     * Shows document processing links.
     * @param client GUI Client.
     * @param gridRow View's selected grid table row.
     */
    public static void showDocumentLinks(final SClientInterface client, final SGridRow gridRow) {
        if (gridRow == null) {
            client.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
        }
        else {
            showDocumentLinks(client, gridRow.getRowPrimaryKey());
        }
    }
    
    public static void showCfdi(final SClientInterface client, final Object pKey, final SDataRecordEntry ety, final int typeCfd) {
        SDialogShowRecordCfd dialog; 
        int cfds;
        String msg = "La póliza no tiene CFDI ";
        
        if (pKey == null) {
            client.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
        }
        else {
            client.getFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));
            SDataRecord rec = (SDataRecord) SDataUtilities.readRegistry(client, SDataConstants.FIN_REC, 
                    pKey, SLibConstants.EXEC_MODE_VERBOSE);
            client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            
            dialog = new SDialogShowRecordCfd(client, typeCfd);
            dialog.formReset();
            dialog.setValue(SDataConstants.FIN_REC, rec);
            if (ety != null) {
                dialog.setValue(SDataConstants.FIN_REC_ETY, ety);
                msg = "La partida de la póliza no tiene CFDI ";
            }
            cfds = dialog.readCfd();
            if (cfds == 0) {
                client.showMsgBoxInformation(msg + (typeCfd == SDataConstants.FINX_REC_CFD_DIRECT ? "directos" : "indirectos") + ".");
            }
            else {
                dialog.setFormVisible(true);
            }
        }
    }
    
    public static boolean customReportExists(final SClientInterface client, final String reportKey) {
        try {
            String sql = "SELECT * FROM cfg_custom_rep WHERE rep_key = '" + reportKey + "'";
            ResultSet resultSet = client.getSession().getStatement().executeQuery(sql);
            return resultSet.next();
        } catch (Exception e) {
            client.showMsgBoxWarning(e.getMessage());
        }
        return false;
    }
}
