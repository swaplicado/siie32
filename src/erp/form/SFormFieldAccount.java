/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.form;

import erp.data.SDataConstants;
import erp.data.SDataUtilities;
import erp.data.SProcConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.form.*;
import erp.mfin.data.SDataAccount;
import erp.mfin.data.SDataCostCenter;
import javax.swing.JFormattedTextField;

/**
 *
 * @author Sergio Flores
 */
public abstract class SFormFieldAccount implements erp.lib.form.SFormFieldInterface, java.awt.event.FocusListener, javax.swing.event.CaretListener {

    private erp.client.SClientInterface miClient;
    private int mnAccountType;        // SDataConstants.FIN_ACC or SDataConstants.FIN_CC
    private int mnAccountLevels;
    private javax.swing.JFormattedTextField mjftAccount;
    private boolean mbIsForNewAccounts;

    private int mnProcValType;
    private int mnCurrentLevel;
    private boolean mbProcessingEvents;
    private java.lang.String msOriginalValue;
    private java.lang.String msMaskFormatter;
    private java.lang.String msEmptyAccountId;
    private java.util.Vector<java.lang.Integer> mvDotLevels;
    private erp.mfin.data.SDataAccount moAccountMajor;
    private erp.mfin.data.SDataCostCenter moCostCenterMajor;

    public SFormFieldAccount(erp.client.SClientInterface client, int accountType, int accountLevels, javax.swing.JFormattedTextField jFormattedTextField, boolean isForNewAccounts) {
        miClient = client;
        mnAccountType = accountType;
        mnAccountLevels = accountLevels;
        mjftAccount = jFormattedTextField;
        mbIsForNewAccounts = isForNewAccounts;

        mnCurrentLevel = 1;
        mbProcessingEvents = false;
        msOriginalValue = "";

        switch (mnAccountType) {
            case SDataConstants.FIN_ACC:
                mnProcValType = SProcConstants.FIN_ACC_VAL;
                msMaskFormatter = SDataUtilities.createMaskFormatterAccountId(miClient, mnAccountLevels);
                msEmptyAccountId = SDataUtilities.createNewFormattedAccountId(miClient, miClient.getSessionXXX().getParamsErp().getDeepAccounts());
                break;
            case SDataConstants.FIN_CC:
                mnProcValType = SProcConstants.FIN_CC_VAL;
                msMaskFormatter = SDataUtilities.createMaskFormatterCostCenterId(miClient, mnAccountLevels);
                msEmptyAccountId = SDataUtilities.createNewFormattedCostCenterId(miClient, miClient.getSessionXXX().getParamsErp().getDeepCostCenters());
                break;
            default:
                mnProcValType = SDataConstants.UNDEFINED;
                msMaskFormatter = "";
                msEmptyAccountId = "";
        }

        SFormUtilities.implementMaskFormatter(mjftAccount, msMaskFormatter);
        mvDotLevels = SDataUtilities.getAccountLevels(msMaskFormatter);
        resetMajorAccounts();

        mjftAccount.addFocusListener(this);
        mjftAccount.addCaretListener(this);
    }

    private void resetMajorAccounts() {
        moAccountMajor = null;
        moCostCenterMajor = null;
    }

    private void readMajorAccounts() {
        String accountId = "";

        mjftAccount.removeFocusListener(this);

        switch (mnAccountType) {
            case SDataConstants.FIN_ACC:
                if (moAccountMajor == null) {
                    accountId = SDataUtilities.obtainAccountIdByLevel(miClient, mjftAccount.getText(), 1);
                    if (accountId.compareTo(msEmptyAccountId) != 0) {
                        moAccountMajor = (SDataAccount) SDataUtilities.readRegistry(miClient, mnAccountType, new Object[] { accountId }, SLibConstants.EXEC_MODE_SILENT);
                    }
                }
                break;
            case SDataConstants.FIN_CC:
                if (moCostCenterMajor == null) {
                    accountId = SDataUtilities.obtainCostCenterIdByLevel(miClient, mjftAccount.getText(), 1);
                    if (accountId.compareTo(msEmptyAccountId) != 0) {
                        moCostCenterMajor = (SDataCostCenter) SDataUtilities.readRegistry(miClient, mnAccountType, new Object[] { accountId }, SLibConstants.EXEC_MODE_SILENT);
                    }
                }
                break;
            default:
        }

        mjftAccount.addFocusListener(this);
    }

    private int getMajorDeep() {
        int deep = 1;

        switch (mnAccountType) {
            case SDataConstants.FIN_ACC:
                if (moAccountMajor != null) {
                    deep = moAccountMajor.getDeep();
                }
                break;
            case SDataConstants.FIN_CC:
                if (moCostCenterMajor != null) {
                    deep = moCostCenterMajor.getDeep();
                }
                break;
            default:
                break;
        }

        return deep;
    }

    private void validateDotLevel(int dotLevel) {
        int maxLevel = getMajorDeep();
        String accountId = "";

        try {
            mnCurrentLevel = 1;     // first level is 1

            for (int i = 1; i <= dotLevel && i <= maxLevel; i++) {
                // Validate that current level does not have a zero value:

                if (i < mnAccountLevels) {
                    accountId = mjftAccount.getText().substring(mvDotLevels.get(i - 1), mvDotLevels.get(i) - 1);
                }
                else {
                    accountId = mjftAccount.getText().substring(mvDotLevels.get(i - 1));
                }

                if (SLibUtilities.parseLong(accountId) == 0L) {
                    break;  // zero value is not valid
                }
                else {
                    switch (mnAccountType) {
                        case SDataConstants.FIN_ACC:
                            accountId = SDataUtilities.obtainAccountIdByLevel(miClient, mjftAccount.getText(), i);
                            break;
                        case SDataConstants.FIN_CC:
                            accountId = SDataUtilities.obtainCostCenterIdByLevel(miClient, mjftAccount.getText(), i);
                            break;
                        default:
                            break;
                    }

                    if (SDataUtilities.callProcedureVal(miClient, mnProcValType, new Object[] { accountId }, SLibConstants.EXEC_MODE_SILENT) > 0) {
                        if (mnCurrentLevel < maxLevel) {
                            mnCurrentLevel++;
                        }
                        else {
                            break;
                        }
                    }
                    else {
                        break;  // accounting ID does not exist
                    }
                }
            }
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
        finally {
            if (mnCurrentLevel < dotLevel) {
                mjftAccount.removeCaretListener(this);
                mjftAccount.setCaretPosition(mvDotLevels.get(mnCurrentLevel - 1)); // caret position at beginning of last valid level
                mjftAccount.addCaretListener(this);
            }
        }
    }

    public int getAccountType() { return mnAccountType; }
    public int getAccountLevels() { return mnAccountLevels; }
    public boolean getIsForNewAccounts() { return mbIsForNewAccounts; }
    public java.lang.String getEmptyAccountId() { return msEmptyAccountId.substring(0, msMaskFormatter.length()); }
    public boolean isEmptyAccountId() { return msEmptyAccountId.substring(0, msMaskFormatter.length()).compareTo(mjftAccount.getText()) == 0; }

    @Override
    public void setComponent(javax.swing.JComponent component) {
        mjftAccount = (JFormattedTextField) component;
    }

    @Override
    public void setString(java.lang.String s) {
        mjftAccount.removeCaretListener(this);
        mjftAccount.setValue(s);
        mjftAccount.setCaretPosition(0);
        mjftAccount.addCaretListener(this);
    }

    @Override
    public javax.swing.JComponent getComponent() {
        return mjftAccount;
    }

    @Override
    public java.lang.String getString() {
        return mjftAccount.getText();
    }

    @Override
    public void setFieldValue(java.lang.Object o) {
        setString((String) o);
    }

    @Override
    public java.lang.Object getFieldValue() {
        return getString();
    }


    @Override
    public void setDataType(int type) {

    }

    @Override
    public void setKey(java.lang.Object key) {

    }

    @Override
    public int getDataType() {
        return SLibConstants.DATA_TYPE_STRING;
    }

    @Override
    public java.lang.Object getKey() {
        return new Object[] { mjftAccount.getText() };
    }

    public int getUsedLevelsCount() {
        return SDataUtilities.getAccountUsedLevelsCount(mjftAccount.getText(), mvDotLevels);
    }

    @Override
    public void focusGained(java.awt.event.FocusEvent evt) {
        if (!mbProcessingEvents) {
            mbProcessingEvents = true;
            mjftAccount.removeCaretListener(this);

            resetMajorAccounts();
            msOriginalValue = mjftAccount.getText();

            mjftAccount.setCaretPosition(0);
            mjftAccount.addCaretListener(this);
            mbProcessingEvents = false;
        }
    }

    @Override
    public void focusLost(java.awt.event.FocusEvent evt) {
        if (!mbProcessingEvents) {
            boolean reset = true;
            String accountIdByLevel = "";

            try {
                mbProcessingEvents = true;
                mjftAccount.removeCaretListener(this);

                mjftAccount.setText(mjftAccount.getText().replaceAll(" ", "0"));    // this prevents to loose input mask when user deletes it

                readMajorAccounts();

                for (int i = getMajorDeep(); i > 0; i--) {
                    reset = false;
                    switch (mnAccountType) {
                        case SDataConstants.FIN_ACC:
                            accountIdByLevel = SDataUtilities.obtainAccountIdByLevel(miClient, mjftAccount.getText(), i);
                            break;
                        case SDataConstants.FIN_CC:
                            accountIdByLevel = SDataUtilities.obtainCostCenterIdByLevel(miClient, mjftAccount.getText(), i);
                            break;
                        default:
                            break;
                    }

                    mjftAccount.setValue(accountIdByLevel);

                    if (mbIsForNewAccounts) {
                        break;
                    }
                    else if (SDataUtilities.callProcedureVal(miClient, mnProcValType, new Object[] { accountIdByLevel }, SLibConstants.EXEC_MODE_SILENT) > 0) {
                        break;
                    }
                }

                if (reset) {
                    mjftAccount.setValue(msOriginalValue);
                }
            }
            catch (Exception e) {
                SLibUtilities.printOutException(this, e);
            }
            finally {
                mjftAccount.setCaretPosition(0);
                mjftAccount.addCaretListener(this);
                mbProcessingEvents = false;
            }
        }
    }

    @Override
    public void caretUpdate(javax.swing.event.CaretEvent evt) {
        if (!mbProcessingEvents) {
            int dotLevel = 1;

            try {
                mbProcessingEvents = true;

                if (mvDotLevels.size() > 1 && evt.getDot() < mvDotLevels.get(1)) {
                    resetMajorAccounts();
                }

                for (int i = 1; i < mvDotLevels.size(); i++) {
                    if (evt.getDot() >= mvDotLevels.get(i)) {
                        if (i == 1) {
                            readMajorAccounts();
                        }

                        dotLevel++;
                    }
                    else {
                        break;
                    }
                }

                if (dotLevel > mnCurrentLevel) {
                    validateDotLevel(dotLevel);
                }
                else {
                    mnCurrentLevel = dotLevel;
                }
            }
            catch (Exception e) {
                SLibUtilities.printOutException(this, e);
            }
            finally {
                mbProcessingEvents = false;
            }
        }
    }
}
