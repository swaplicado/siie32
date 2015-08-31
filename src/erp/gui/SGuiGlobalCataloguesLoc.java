/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.gui;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.form.SFormOptionPicker;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormOptionPickerInterface;
import erp.mloc.data.*;
import erp.mloc.form.*;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
/**
 *
 * @author Sergio Flores
 */
public class SGuiGlobalCataloguesLoc extends erp.lib.gui.SGuiModule implements java.awt.event.ActionListener {

    private javax.swing.JMenu jmMenuLocality;
    private javax.swing.JMenuItem jmiCountry;

    private erp.mloc.form.SFormCountry moFormCountry;

    private erp.form.SFormOptionPicker moPickerCountry;

    public SGuiGlobalCataloguesLoc(erp.client.SClientInterface client) {
        super(client, SDataConstants.MOD_CFG);
        initComponents();
    }

    private void initComponents() {
        boolean hasRightLocality;

        jmMenuLocality = new JMenu("Localidades");
        jmiCountry = new JMenuItem("Países");

        jmMenuLocality.add(jmiCountry);

        jmiCountry.addActionListener(this);

        moFormCountry = null;

        moPickerCountry = null;

        hasRightLocality = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_LOC).HasRight;

        jmiCountry.setEnabled(hasRightLocality);
        jmMenuLocality.setEnabled(hasRightLocality);
    }

    private int showForm(int formType, int auxType, java.lang.Object pk, boolean isCopy) {
        int result = SLibConstants.UNDEFINED;

        try {
            clearFormMembers();
            setFrameWaitCursor();

            switch (formType) {
                case SDataConstants.LOCU_CTY:
                    if (moFormCountry == null) {
                        moFormCountry = new SFormCountry(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataCountry();
                    }
                    miForm = moFormCountry;
                    break;
                default:
                    throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_FORM);
            }

            result = processForm(pk, isCopy);
            clearFormComplement();
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(this, e);
        }
        finally {
            restoreFrameCursor();
        }

        return result;
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
        java.lang.String sViewTitle = "";
        java.lang.Class oViewClass = null;

        try {
            setFrameWaitCursor();

            switch (viewType) {
                case SDataConstants.LOCU_CTY:
                    oViewClass = erp.mloc.view.SViewCountry.class;
                    sViewTitle = "Países";
                    break;
                default:
                    throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_VIEW);
            }

            processView(oViewClass, sViewTitle, viewType, auxType01, auxType02);
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(this, e);
        }
        finally {
            restoreFrameCursor();
        }
    }

    @Override
    public int showForm(int formType, java.lang.Object pk) {
        return showForm(formType, SDataConstants.UNDEFINED, pk, false);
    }

    @Override
    public int showForm(int formType, int auxType, java.lang.Object pk) {
        return showForm(formType, auxType, pk, false);
    }

    @Override
    public int showFormForCopy(int formType, java.lang.Object pk) {
        return showForm(formType, SDataConstants.UNDEFINED, pk, true);
    }

    @Override
    public erp.lib.form.SFormOptionPickerInterface getOptionPicker(int optionType) {
        SFormOptionPickerInterface picker = null;

        try {
            switch (optionType) {
                case SDataConstants.LOCU_CTY:
                    picker = moPickerCountry = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerCountry);
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
        javax.swing.JMenu[] menues = null;

        switch (moduleType) {
            case SDataConstants.MOD_CFG:
                menues = new JMenu[] { jmMenuLocality };
                break;
            case SDataConstants.GLOBAL_CAT_LOC:
                menues = new JMenu[] { jmMenuLocality };
                break;
            default:
        }

        return menues;
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JMenuItem) {
            javax.swing.JMenuItem item = (javax.swing.JMenuItem) e.getSource();

            if (item == jmiCountry) {
                showView(SDataConstants.LOCU_CTY);
            }
        }
    }
}
