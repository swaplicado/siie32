/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import erp.client.SClientInterface;
import erp.lib.SLibConstants;
import erp.mtrn.form.uareu.SDialogUareUFingerprint;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Sergio Flores
 */
public abstract class STrnMaintUtilities {
    
    public static int getLastMaintDiogSignature(final Statement statement, final int[] diogKey) throws Exception {
        int id = SLibConstants.UNDEFINED;
        
        String sql = "SELECT MAX(id_maint_diog_sig) "
                + "FROM trn_maint_diog_sig "
                + "WHERE fk_diog_year = " + diogKey[0] + " AND fk_diog_doc = " + diogKey[1] + " ";
        ResultSet resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            id = resultSet.getInt(1);
        }
                
        return id;
    }
    
    public static byte[] enrollFingerprint(final SClientInterface client) {
        byte[] fingerprint = null;
        SDialogUareUFingerprint dialog = new SDialogUareUFingerprint(client, SDialogUareUFingerprint.MODE_ENROLLMENT);
        
        dialog.formReset();
        dialog.setVisible(true);
        if (dialog.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            fingerprint = (byte[]) dialog.getValue(SDialogUareUFingerprint.VALUE_FINGERPRINT);
        }
        
        return fingerprint;
    }
    
    public static boolean verifyFingerprint(final SClientInterface client, final byte[] fingerprint) {
        boolean verified = false;
        SDialogUareUFingerprint dialog = new SDialogUareUFingerprint(client, SDialogUareUFingerprint.MODE_VERIFICATION);
        
        dialog.formReset();
        dialog.setValue(SDialogUareUFingerprint.VALUE_FINGERPRINT, fingerprint);
        dialog.setVisible(true);
        if (dialog.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            verified = true;
        }
        
        return verified;
    }
}
