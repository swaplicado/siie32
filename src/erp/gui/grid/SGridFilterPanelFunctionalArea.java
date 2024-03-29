/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.gui.grid;

import erp.form.SFormOptionPickerFunctionalArea;
import erp.client.SClientInterface;
import erp.mtrn.data.STrnFunctionalAreaUtils;
import erp.table.SFilterConstants;
import javax.swing.JPanel;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilter;
import sa.lib.grid.SGridFilterValue;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;

/**
 *
 * @author Edwin Carmona, Sergio Flores
 */
public class SGridFilterPanelFunctionalArea extends JPanel implements SGridFilter {

    private static final int TAB_SETTING_ADD = 1;
    private static final int TAB_SETTING_UPDATE = 2;

    private SGuiClient miClient;
    private SGridPaneView moPaneView;
    private SFormOptionPickerFunctionalArea moFunctionalAreaPicker;
    private int mnCurrentFunctionalAreaId;
    
    /**
     * Creates new pane view SGridFilterPanelFunctionalArea.
     * By default, functional areas asigned to current user are set.
     * @param client GUI client.
     * @param paneView Pane view.
     */
    public SGridFilterPanelFunctionalArea(SGuiClient client, SGridPaneView paneView) {
        miClient = client;
        moPaneView = paneView;
        
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

        jtfFunctionalArea = new sa.lib.gui.bean.SBeanFieldText();
        jbFunctionalArea = new javax.swing.JButton();

        setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfFunctionalArea.setEditable(false);
        jtfFunctionalArea.setText("TEXT");
        jtfFunctionalArea.setToolTipText("Área funcional");
        jtfFunctionalArea.setPreferredSize(new java.awt.Dimension(65, 23));
        add(jtfFunctionalArea);

        jbFunctionalArea.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_filter_func.gif"))); // NOI18N
        jbFunctionalArea.setToolTipText("Seleccionar área funcional");
        jbFunctionalArea.setPreferredSize(new java.awt.Dimension(23, 23));
        jbFunctionalArea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbFunctionalAreaActionPerformed(evt);
            }
        });
        add(jbFunctionalArea);
    }// </editor-fold>//GEN-END:initComponents

    private void jbFunctionalAreaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbFunctionalAreaActionPerformed
        actionPerformedFunctionalArea();
    }//GEN-LAST:event_jbFunctionalAreaActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jbFunctionalArea;
    private sa.lib.gui.bean.SBeanFieldText jtfFunctionalArea;
    // End of variables declaration//GEN-END:variables

    /*
     * Private methods
     */
    
    private void initComponentsCustom() {
        moFunctionalAreaPicker = new SFormOptionPickerFunctionalArea((SClientInterface) miClient);
        
        updateAndRenderFunctionalArea(0, TAB_SETTING_ADD);
        jbFunctionalArea.setEnabled(((SClientInterface) miClient).getSessionXXX().getParamsCompany().getIsFunctionalAreas());
    }
    
    private void updateAndRenderFunctionalArea(int functionalAreaId, int tabSettingAction) {
        mnCurrentFunctionalAreaId = functionalAreaId;
        
        String texts[] = STrnFunctionalAreaUtils.getTextFilterOfFunctionalAreas((SClientInterface) miClient, mnCurrentFunctionalAreaId);
        SGridFilterValue filterValue = new SGridFilterValue(SFilterConstants.SETTING_FILTER_FUNC_AREA, SGridConsts.FILTER_DATA_TYPE_TEXT, texts[0]);

        if (tabSettingAction == TAB_SETTING_ADD) {
            moPaneView.getFiltersMap().put(SFilterConstants.SETTING_FILTER_FUNC_AREA, filterValue);
        }
        else {
            moPaneView.putFilter(SFilterConstants.SETTING_FILTER_FUNC_AREA, filterValue);
        }
        
        jtfFunctionalArea.setText(texts[1]);
        jtfFunctionalArea.setCaretPosition(0);
    }
    
    private void actionPerformedFunctionalArea() {
        moFunctionalAreaPicker.formRefreshOptionPane();
        moFunctionalAreaPicker.formReset();
        moFunctionalAreaPicker.setSelectedPrimaryKey(new int[] { mnCurrentFunctionalAreaId });
        moFunctionalAreaPicker.setVisible(true);
        
        if (moFunctionalAreaPicker.getFormResult() == erp.lib.SLibConstants.FORM_RESULT_OK) {
            updateAndRenderFunctionalArea(moFunctionalAreaPicker.getSelectedPrimaryKey() == null ? 0 : ((int[]) moFunctionalAreaPicker.getSelectedPrimaryKey())[0], TAB_SETTING_UPDATE);
        }
    }
    
    /*
     * Public methods
     */
    
    /*
     * Protected methods
     */
    
    @Override
    public void initFilter(Object value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
