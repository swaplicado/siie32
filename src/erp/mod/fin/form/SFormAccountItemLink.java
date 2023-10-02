/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.form;

import erp.gui.account.SAccount;
import erp.gui.account.SAccountConsts;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.fin.db.SDbAccountItemLink;
import java.awt.event.ItemEvent;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;

/**
 *
 * @author Juan Barajas
 */
public class SFormAccountItemLink extends sa.lib.gui.bean.SBeanForm {

    private SDbAccountItemLink moRegistry;

    /**
     * Creates new form SFormAccountItemLink
     */
    public SFormAccountItemLink(SGuiClient client, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.FIN_ABP_ITEM_LINK, SLibConstants.UNDEFINED, title);
        initComponents();
        initComponentsCustom();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        moAccountPanel = new erp.gui.account.SBeanPanelAccount();
        jPanel2 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jlLink = new javax.swing.JLabel();
        moKeyLink = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel24 = new javax.swing.JPanel();
        jlReference = new javax.swing.JLabel();
        moKeyReference = new sa.lib.gui.bean.SBeanFieldKey();

        jPanel1.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel23.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel23.setLayout(new java.awt.GridLayout(3, 1, 0, 5));
        jPanel23.add(moAccountPanel);

        jPanel2.setLayout(new java.awt.GridLayout(2, 1));

        jPanel22.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlLink.setForeground(new java.awt.Color(0, 0, 255));
        jlLink.setText("Tipo referencia: *");
        jlLink.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel22.add(jlLink);

        moKeyLink.setPreferredSize(new java.awt.Dimension(300, 23));
        moKeyLink.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                moKeyLinkItemStateChanged(evt);
            }
        });
        jPanel22.add(moKeyLink);

        jPanel2.add(jPanel22);

        jPanel24.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlReference.setForeground(new java.awt.Color(0, 0, 255));
        jlReference.setText("Referencia: *");
        jlReference.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel24.add(jlReference);

        moKeyReference.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel24.add(moKeyReference);

        jPanel2.add(jPanel24);

        jPanel23.add(jPanel2);

        jPanel1.add(jPanel23, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void moKeyLinkItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_moKeyLinkItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            itemStateChangedLinkType();
        }
    }//GEN-LAST:event_moKeyLinkItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel jlLink;
    private javax.swing.JLabel jlReference;
    private erp.gui.account.SBeanPanelAccount moAccountPanel;
    private sa.lib.gui.bean.SBeanFieldKey moKeyLink;
    private sa.lib.gui.bean.SBeanFieldKey moKeyReference;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 480, 300);

        moAccountPanel.setPanelSettings((SGuiClient) miClient, SAccountConsts.TYPE_ACCOUNT, true, true, true);
        moAccountPanel.setAccountNameWidth(300);
        moKeyLink.setKeySettings(miClient, SGuiUtils.getLabelName(jlLink.getText()), true);
        moKeyReference.setKeySettings(miClient, SGuiUtils.getLabelName(jlReference.getText()), true);

        moFields.addField(moKeyLink);
        moFields.addField(moKeyReference);

        moFields.setFormButton(jbSave);

        moAccountPanel.initPanel();
    }

    private void itemStateChangedLinkType() {
        boolean enable = true;

        moKeyReference.removeAllItems();
        moKeyReference.setEnabled(false);
        moKeyReference.setEnabled(false);

        try {
            if (moKeyLink.getSelectedIndex() > 0) {
                switch (moKeyLink.getValue()[0]) {
                    case SModSysConsts.ITMS_LINK_ALL:
                        enable = false;
                        break;
                    case SModSysConsts.ITMS_LINK_CT_ITEM:
                        miClient.getSession().populateCatalogue(moKeyReference, SModConsts.ITMS_CT_ITEM, SLibConsts.UNDEFINED, null);
                        break;
                    case SModSysConsts.ITMS_LINK_CL_ITEM:
                        miClient.getSession().populateCatalogue(moKeyReference, SModConsts.ITMS_CL_ITEM, SLibConsts.UNDEFINED, null);
                        break;
                    case SModSysConsts.ITMS_LINK_TP_ITEM:
                        miClient.getSession().populateCatalogue(moKeyReference, SModConsts.ITMS_TP_ITEM, SLibConsts.UNDEFINED, null);
                        break;
                    case SModSysConsts.ITMS_LINK_IFAM:
                        miClient.getSession().populateCatalogue(moKeyReference, SModConsts.ITMU_IFAM, SLibConsts.UNDEFINED, null);
                        break;
                    case SModSysConsts.ITMS_LINK_IGRP:
                        miClient.getSession().populateCatalogue(moKeyReference, SModConsts.ITMU_IGRP, SLibConsts.UNDEFINED, null);
                        break;
                    case SModSysConsts.ITMS_LINK_IGEN:
                        miClient.getSession().populateCatalogue(moKeyReference, SModConsts.ITMU_IGEN, SLibConsts.UNDEFINED, null);
                        break;
                    case SModSysConsts.ITMS_LINK_LINE:
                        miClient.getSession().populateCatalogue(moKeyReference, SModConsts.ITMU_LINE, SLibConsts.UNDEFINED, null);
                        break;
                    case SModSysConsts.ITMS_LINK_BRD:
                        miClient.getSession().populateCatalogue(moKeyReference, SModConsts.ITMU_BRD, SLibConsts.UNDEFINED, null);
                        break;
                    case SModSysConsts.ITMS_LINK_MFR:
                        miClient.getSession().populateCatalogue(moKeyReference, SModConsts.ITMU_MFR, SLibConsts.UNDEFINED, null);
                        break;
                    case SModSysConsts.ITMS_LINK_ITEM:
                        miClient.getSession().populateCatalogue(moKeyReference, SModConsts.ITMU_ITEM, SLibConsts.UNDEFINED, null);
                        break;
                    default:
                        enable = false;
                        throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_FORM_PICK);
                }

                if (enable) {
                    moKeyReference.setSelectedIndex(0);
                    moKeyReference.setEnabled(true);
                    moKeyReference.setEnabled(true);
                }
            }
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
    }

    @Override
    public void addAllListeners() {

    }

    @Override
    public void removeAllListeners() {

    }

    @Override
    public void reloadCatalogues() {
        miClient.getSession().populateCatalogue(moKeyLink, SModConsts.ITMS_LINK, SLibConsts.UNDEFINED, null);
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        int[] key = null;
        moRegistry = (SDbAccountItemLink) registry;

        mnFormResult = SLibConsts.UNDEFINED;
        mbFirstActivation = true;

        removeAllListeners();
        reloadCatalogues();

        key = moRegistry.getPrimaryKey();

        if (moRegistry.isRegistryNew()) {
            moRegistry.initPrimaryKey();
            jtfRegistryKey.setText("");
        }
        else {
            jtfRegistryKey.setText(SLibUtils.textKey(moRegistry.getPrimaryKey()));
        }

        moAccountPanel.setSelectedAccount(new SAccount(key[0], (String) miClient.getSession().readField(SModConsts.FIN_ACC, new int[] { key[0] }, SDbRegistry.FIELD_CODE), "", false, SLibConstants.UNDEFINED, SLibConstants.UNDEFINED));
        moKeyLink.setValue(new int[] { key[1] });
        moKeyReference.setValue(new int[] { key[2] });

        setFormEditable(true);

        if (moRegistry.isRegistryNew()) {
            itemStateChangedLinkType();
            moAccountPanel.setPanelEditable(true);
        }
        else {
            moAccountPanel.setPanelEditable(false);
            moKeyLink.setEnabled(false);
            moKeyReference.setEnabled(false);
        }

        addAllListeners();
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        SDbAccountItemLink registry = moRegistry.clone();

        if (registry.isRegistryNew()) {}

        moRegistry.setPkAccountId(moAccountPanel.getSelectedAccount() == null ? 1 : moAccountPanel.getSelectedAccount().getAccountId());
        moRegistry.setPkLinkId(moKeyLink.getValue()[0]);

        switch (moKeyLink.getValue()[0]) {
            case SModSysConsts.ITMS_LINK_ALL:
                moRegistry.setPkReferenceId(SLibConstants.UNDEFINED);
                break;
            default:
                moRegistry.setPkReferenceId(moKeyReference.getValue()[0]);
        }

        return registry;
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();

        if (validation.isValid()) {
            validation = moAccountPanel.validatePanel();
        }

        return validation;
    }
}
