/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.gui;

import erp.data.SDataConstants;
import erp.form.SFormOptionPicker;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormOptionPickerInterface;
import redis.clients.jedis.Jedis;

/**
 *
 * @author Sergio Flores
 */
public class SGuiGlobalCataloguesTrn extends erp.lib.gui.SGuiModule implements java.awt.event.ActionListener {

    private erp.form.SFormOptionPicker moPickerDpsCategory;
    private erp.form.SFormOptionPicker moPickerDpsClass;
    private erp.form.SFormOptionPicker moPickerDpsType;
    private erp.form.SFormOptionPicker moPickerDiogType;
    private erp.form.SFormOptionPicker moPickerTypeLink;
    private erp.form.SFormOptionPicker moPickerDpsNature;
    private erp.form.SFormOptionPicker moPickerSystemNotes;
    private erp.form.SFormOptionPicker moPickerDps;
    private erp.form.SFormOptionPicker moPickerDpsPendPay;
    private erp.form.SFormOptionPicker moPickerStockConfigDns;

    public SGuiGlobalCataloguesTrn(erp.client.SClientInterface client) {
        super(client, SDataConstants.MOD_CFG);
        initComponents();
    }

    private void initComponents() {
        moPickerDpsCategory = null;
        moPickerDpsClass = null;
        moPickerDpsType = null;
        moPickerDiogType = null;
        moPickerTypeLink = null;
        moPickerDpsNature = null;
        moPickerSystemNotes = null;
        moPickerDps = null;
        moPickerDpsPendPay = null;
        moPickerStockConfigDns = null;
    }

    private int showForm(int formType, int auxType, java.lang.Object pk, boolean isCopy) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void showView(int viewType) {
        showView(viewType, 0, 0);
    }

    @Override
    public void showView(int viewType, int auxType) {
        showView(viewType, auxType, 0);
    }

    @Override
    public void showView(int viewType, int auxType01, int auxType02) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int showForm(int formType, java.lang.Object pk) {
        return showForm(formType, SDataConstants.UNDEFINED, pk, false);
    }

    @Override
    public int showForm(int formType, int auxType, Object pk) {
        return showForm(formType, auxType, pk, false);
    }

    @Override
    public int showFormForCopy(int formType, Object pk) {
        return showForm(formType, SDataConstants.UNDEFINED, pk, true);
    }

    @Override
    public erp.lib.form.SFormOptionPickerInterface getOptionPicker(int optionType) {
        SFormOptionPickerInterface picker = null;

        try {
            switch (optionType) {
                case SDataConstants.TRNS_CT_DPS:
                    picker = moPickerDpsCategory = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerDpsCategory);
                    break;
                case SDataConstants.TRNS_CL_DPS:
                    picker = moPickerDpsClass= SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerDpsClass);
                    break;
                case SDataConstants.TRNU_TP_DPS:
                    picker = moPickerDpsType = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerDpsType);
                    break;
                case SDataConstants.TRNS_TP_IOG:
                    picker = moPickerDiogType = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerDiogType);
                    break;
                case SDataConstants.TRNS_TP_LINK:
                    picker = moPickerTypeLink = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerTypeLink);
                    break;
                case SDataConstants.TRNU_DPS_NAT:
                    picker = moPickerDpsNature = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerDpsNature);
                    break;
                case SDataConstants.TRN_SYS_NTS:
                    picker = moPickerSystemNotes = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerSystemNotes);
                    break;
                 case SDataConstants.TRN_DPS:
                    picker = moPickerDps = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerDps);
                    break;
                case SDataConstants.TRNX_DPS_PAY_PEND:
                    picker = moPickerDpsPendPay = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerDpsPendPay);
                    break;
                 case SDataConstants.TRN_DNS_DPS:
                    picker = moPickerStockConfigDns = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerStockConfigDns);
                    break;
                default:
                    throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_FORM_PICK);
            }
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(this, e);
        }

        return picker;
    }

    @Override
    public int annulRegistry(int registryType, java.lang.Object pk, sa.lib.gui.SGuiParams params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int deleteRegistry(int registryType, java.lang.Object pk) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public javax.swing.JMenu[] getMenues() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public javax.swing.JMenu[] getMenuesForModule(int moduleType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
