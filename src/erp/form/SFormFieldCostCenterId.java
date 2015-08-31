/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.form;

import erp.data.SDataConstants;

/**
 *
 * @author Sergio Flores
 */
public class SFormFieldCostCenterId extends SFormFieldAccount {

    public SFormFieldCostCenterId(erp.client.SClientInterface client, int level, javax.swing.JFormattedTextField jft, boolean isForNewAccounts) {
        super(client, SDataConstants.FIN_CC, level, jft, isForNewAccounts);
    }
}
