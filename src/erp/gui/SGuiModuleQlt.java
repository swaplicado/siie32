/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.gui;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.form.SFormOptionPicker;
import erp.lib.SLibConstants;
import erp.mod.SModConsts;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import sa.lib.SLibConsts;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormOptionPickerInterface;
import erp.mqlt.data.*;
import erp.mqlt.form.*;

/**
 *
 * @author Uriel Castañeda, Edwin Carmona
 */
public class SGuiModuleQlt extends erp.lib.gui.SGuiModule implements java.awt.event.ActionListener {

    private javax.swing.JMenu jmQlt;
    private javax.swing.JMenuItem jmiQltLot;
    private javax.swing.JMenuItem jmiQltCoA;
    private javax.swing.JMenu jmQltLab;
    private javax.swing.JMenuItem jmQltLabAnalysis;
    private javax.swing.JMenuItem jmQltDatasheetTemplates;
    private javax.swing.JMenuItem jmQltDpsAnalysis;
    private javax.swing.JMenuItem jmQltDpsAnalysisDetail;
    private javax.swing.JMenu jmQltCfg;
//    private javax.swing.JMenuItem jmQltItemsVsAnalysisDetail;
//    private javax.swing.JMenuItem jmQltItemsVsAnalysis;
    private javax.swing.JMenuItem jmQltDatasheetTemplateLink;
    
    private SFormAnalysisItem moFormAnalysisItem;
    
    private erp.form.SFormOptionPicker moPickerAnalysis;
    
    public SGuiModuleQlt(erp.client.SClientInterface client) {
        super(client, SDataConstants.MOD_QLT);
        initComponents();
    }

    private void initComponents() {
        jmQlt = new JMenu("Calidad");
        jmiQltLot = new JMenuItem("Lotes aprobados");
        jmiQltCoA = new JMenuItem("Generar certificado");
        jmQltLab = new JMenu("Laboratorio");
        jmQltLabAnalysis = new JMenuItem("Análisis de laboratorio");
        jmQltDatasheetTemplates = new JMenuItem("Fichas técnicas");
        jmQltDpsAnalysis = new JMenuItem("Documentos vs análisis");
        jmQltDpsAnalysisDetail = new JMenuItem("Documentos vs análisis a detalle");
        jmQltCfg = new JMenu("Configuración");
        jmQltDatasheetTemplateLink = new JMenuItem("Fichas técnicas e ítems");
//        jmQltItemsVsAnalysis = new JMenuItem("Items vs análisis");
//        jmQltItemsVsAnalysisDetail = new JMenuItem("Ítems vs análisis a detalle");
        
        jmQlt.add(jmiQltLot);
        jmQlt.add(jmiQltCoA);
        jmQltLab.add(jmQltLabAnalysis);
        jmQltLab.add(jmQltDatasheetTemplates);
        jmQltLab.add(jmQltDpsAnalysis);
        jmQltLab.add(jmQltDpsAnalysisDetail);
//        jmQltCfg.add(jmQltItemsVsAnalysis);
//        jmQltCfg.add(jmQltItemsVsAnalysisDetail);
        jmQltCfg.add(jmQltDatasheetTemplateLink);

        jmiQltLot.setEnabled(true);
        jmiQltCoA.setEnabled(true);
        jmQltLabAnalysis.setEnabled(true);
        jmQltDatasheetTemplates.setEnabled(true);
        jmQltDpsAnalysis.setEnabled(true);
        jmQltDpsAnalysisDetail.setEnabled(true);
//        jmQltItemsVsAnalysis.setEnabled(true);
//        jmQltItemsVsAnalysisDetail.setEnabled(true);
        jmQltDatasheetTemplateLink.setEnabled(true);

        jmiQltLot.addActionListener(this);
        jmiQltCoA.addActionListener(this);
        jmQltLabAnalysis.addActionListener(this);
        jmQltDatasheetTemplates.addActionListener(this);
        jmQltDpsAnalysis.addActionListener(this);
        jmQltDpsAnalysisDetail.addActionListener(this);
//        jmQltItemsVsAnalysis.addActionListener(this);
//        jmQltItemsVsAnalysisDetail.addActionListener(this);
        jmQltDatasheetTemplateLink.addActionListener(this);
        
        jmQlt.setEnabled(miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_QLT_LOT_APR).HasRight);
        jmQltLab.setEnabled(miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_QLT_LOT_APR).HasRight);
        jmQltCfg.setEnabled(miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_QLT_LOT_APR).HasRight);
    }

    private int showForm(int formType, int auxType, java.lang.Object pk, boolean isCopy) {
        int result = SLibConstants.UNDEFINED;

        try {
            clearFormMembers();
            setFrameWaitCursor();

            switch (formType) {
                case SDataConstants.QLT_ANALYSIS_ITEM:
                    if (moFormAnalysisItem == null) {
                        String title = "Análisis vs ítem";
                        moFormAnalysisItem = new SFormAnalysisItem(miClient, title, formType);
                    }
                    if (pk != null) {
                        moRegistry = new SDataAnalysisItem();
                    }
                    miForm = moFormAnalysisItem;
                    break;
                
                default:
                    throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_FORM);
            }

            result = processForm(pk, isCopy);

            if (result == SLibConstants.DB_ACTION_SAVE_OK && moRegistry != null) {
                switch (formType) {
                    default:
                }
            }
            
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

                case SDataConstants.QLT_ANALYSIS_ITEM:
                    oViewClass = erp.mqlt.view.SViewAnalysisItem.class;
                    sViewTitle = "Fichas técnicas de ítems a detalle";
                    break;
                    
                case SDataConstants.QLTX_DPS_ETY_ANALYSIS:
                    oViewClass = erp.mqlt.view.SViewDpsEntryAnalysisQlt.class;
                    sViewTitle = "Documentos vs análisis";
                    break;
                    
                case SDataConstants.QLTX_ITEM_ANALYSIS:
                    oViewClass = erp.mqlt.view.SViewItemAnalysis.class;
                    sViewTitle = "Fichas técnicas de ítems";
                    break;
                    
                case SDataConstants.QLTX_DOCUMENT_ANALYSIS:
                    oViewClass = erp.mqlt.view.SViewDocumentAnalysis.class;
                    sViewTitle = "Documentos vs análisis";
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
                case SDataConstants.QLT_ANALYSIS:
                    picker = moPickerAnalysis = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerAnalysis);
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
        int result = SLibConstants.UNDEFINED;

        try {
            switch (registryType) {
                case SDataConstants.QLT_ANALYSIS_ITEM:
                    moRegistry = new SDataAnalysisItem();
                    moRegistry.read(pk, miClient.getSession().getDatabase().getConnection().createStatement());
                    break;
                default:
                    throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
            }
            
            boolean delete = ! moRegistry.getIsDeleted();
            result = processActionDelete(pk, delete);
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }

        return result;
    }

    @Override
    public javax.swing.JMenu[] getMenues() {
        return new JMenu[] { jmQlt, jmQltLab, jmQltCfg };
    }

    @Override
    public javax.swing.JMenu[] getMenuesForModule(int moduleType) {
        return null;
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JMenuItem) {
            javax.swing.JMenuItem item = (javax.swing.JMenuItem) e.getSource();

            if (item == jmiQltLot) {
                miClient.getSession().showView(SModConsts.QLT_LOT_APR, SLibConsts.UNDEFINED, null);
            }
            if (item == jmiQltCoA) {
                miClient.getSession().showView(SModConsts.QLTX_QLT_DPS_ETY, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmQltLabAnalysis) {
                miClient.getSession().showView(SModConsts.QLT_ANALYSIS, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmQltDatasheetTemplates) {
                miClient.getSession().showView(SModConsts.QLT_DATASHEET_TEMPLATE, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmQltDatasheetTemplateLink) {
                miClient.getSession().showView(SModConsts.QLT_DATASHEET_TEMPLATE_LINK, SLibConsts.UNDEFINED, null);
            }
//            else if (item == jmQltItemsVsAnalysis) {
//                showView(SDataConstants.QLTX_ITEM_ANALYSIS, SDataConstants.UNDEFINED, SDataConstants.UNDEFINED);
//            }
//            else if (item == jmQltItemsVsAnalysisDetail) {
//                showView(SDataConstants.QLT_ANALYSIS_ITEM, SDataConstants.UNDEFINED, SDataConstants.UNDEFINED);
//            }
            else if (item == jmQltDpsAnalysis) {
                showView(SDataConstants.QLTX_DOCUMENT_ANALYSIS, SDataConstants.UNDEFINED, SDataConstants.UNDEFINED);
            }
            else if (item == jmQltDpsAnalysisDetail) {
                showView(SDataConstants.QLTX_DPS_ETY_ANALYSIS, SDataConstants.UNDEFINED, SDataConstants.UNDEFINED);
            }
        }
    }
}
