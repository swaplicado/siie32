/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import erp.client.SClientInterface;
import erp.lib.SLibConstants;
import erp.mtrn.form.uareu.SDialogUareUFingerPassword;
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
        
        String sql = "SELECT MAX(mds.id_maint_diog_sig) "
                + "FROM trn_maint_diog_sig AS mds "
                + "INNER JOIN trn_diog AS d ON mds.fk_diog_year = d.id_year AND mds.fk_diog_doc = d.id_doc AND mds.ts_usr_ins >= d.ts_edit "
                + "WHERE mds.fk_diog_year = " + diogKey[0] + " AND mds.fk_diog_doc = " + diogKey[1] + " ";
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
    
    public static int enrollFingerPassword(final SClientInterface client) {
        int fingerPassword = 0;
        SDialogUareUFingerPassword dialog = new SDialogUareUFingerPassword(client,  SDialogUareUFingerprint.MODE_ENROLLMENT);
        
        dialog.formReset();
        dialog.setVisible(true);
        if (dialog.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            fingerPassword = (int) Integer.parseInt(dialog.getValue(SDialogUareUFingerPassword.VALUE_FINGERPASSWORD));
        }
        
        return fingerPassword;
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
    
    public static boolean verifyFingerPassword(final SClientInterface client, final int fingerPassword) {
        boolean verified = false;
        SDialogUareUFingerPassword dialog = new SDialogUareUFingerPassword(client, SDialogUareUFingerprint.MODE_VERIFICATION);
        
        dialog.formReset();
        dialog.setValue(SDialogUareUFingerPassword.VALUE_FINGERPASSWORD, fingerPassword);
        dialog.setVisible(true);
        if (dialog.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            verified = true;
        }
        
        return verified;
    }
    
    public static int getFingerprintUserPk(final SClientInterface client) {
        int pk = 0;
        SDialogUareUFingerprint dialog = new SDialogUareUFingerprint(client, SDialogUareUFingerprint.MODE_GET_FINGERPRINT_USR);
        
        dialog.formReset();
        dialog.setVisible(true);
        if (dialog.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            pk = (int) dialog.getValue(SDialogUareUFingerprint.VALUE_FINGERPRINT_USR);
        }
        
        return pk;
    }
}
