/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.gui.grid;

import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import sa.lib.SLibConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilter;
import sa.lib.grid.SGridFilterValue;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiFieldKeyGroup;
import sa.lib.gui.SGuiItem;
import sa.lib.gui.SGuiParams;
import sa.lib.gui.SGuiUtils;

/**
 *
 * @author Sergio Flores
 */
public class SGridFilterPanelCompound extends JPanel implements SGridFilter, ActionListener, ItemListener {

    public static final int PNL_TP_COB_ENT = 1;
    
    private SGuiClient miClient;
    private SGridPaneView moPaneView;
    private int mnTypePanel;
    private int mnFilterType1;
    private int mnFilterType2;
    private boolean mbJustUpdated;
    private SGuiFieldKeyGroup moFieldKeyGroup;
    
    /**
     * Creates new form SGridFilterProject
     */
    public SGridFilterPanelCompound(SGuiClient client, SGridPaneView paneView, int typePanel, int filterType1, int filterType2) {
        miClient = client;
        moPaneView = paneView;
        mnTypePanel = typePanel;
        mnFilterType1 = filterType1;
        mnFilterType2 = filterType2;
        mbJustUpdated = false;
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

        moKeyFilter1 = new sa.lib.gui.bean.SBeanFieldKey();
        jbClearFilter1 = new javax.swing.JButton();
        moKeyFilter2 = new sa.lib.gui.bean.SBeanFieldKey();
        jbClearFilter2 = new javax.swing.JButton();

        setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        moKeyFilter1.setMaximumRowCount(12);
        moKeyFilter1.setPreferredSize(new java.awt.Dimension(150, 23));
        add(moKeyFilter1);

        jbClearFilter1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sa/lib/img/cmd_std_clear.gif"))); // NOI18N
        jbClearFilter1.setToolTipText("Quitar filtro");
        jbClearFilter1.setPreferredSize(new java.awt.Dimension(23, 23));
        add(jbClearFilter1);

        moKeyFilter2.setMaximumRowCount(12);
        moKeyFilter2.setPreferredSize(new java.awt.Dimension(150, 23));
        add(moKeyFilter2);

        jbClearFilter2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sa/lib/img/cmd_std_clear.gif"))); // NOI18N
        jbClearFilter2.setToolTipText("Quitar filtro");
        jbClearFilter2.setPreferredSize(new java.awt.Dimension(23, 23));
        add(jbClearFilter2);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jbClearFilter1;
    private javax.swing.JButton jbClearFilter2;
    private sa.lib.gui.bean.SBeanFieldKey moKeyFilter1;
    private sa.lib.gui.bean.SBeanFieldKey moKeyFilter2;
    // End of variables declaration//GEN-END:variables

    /*
     * Private methods
     */
    
    private void initComponentsCustom() {
        moFieldKeyGroup = new SGuiFieldKeyGroup(miClient);
        jbClearFilter1.addActionListener(this);
        jbClearFilter2.addActionListener(this);
        updateOptions();
        initFilteKey();
    }
    
    private void initFilteKey() {
        int[] key = null;

        if (mnTypePanel == PNL_TP_COB_ENT) {
            key = ((SClientInterface) miClient).getSessionXXX().getCurrentCompanyBranchEntityKey(SDataConstantsSys.CFGS_CT_ENT_WH);

            if (key == null) {
                if (((SClientInterface) miClient).getSessionXXX().getCurrentCompanyBranchId() != SLibConstants.UNDEFINED) {
                    key = new int[] { ((SClientInterface) miClient).getSessionXXX().getCurrentCompanyBranchId(), SLibConstants.UNDEFINED };
                }
            }
        }
        initFilter(key);
    }
    
    private void actionClearFilter1() {
        jbClearFilter1.setEnabled(false);
        jbClearFilter2.setEnabled(false);
        
        if (moKeyFilter1.getSelectedIndex() > 0) {
            moKeyFilter1.setSelectedIndex(0);
            moKeyFilter1.requestFocus();
        }
    }
    
    private void actionClearFilter2() {
        jbClearFilter2.setEnabled(false);
        
        if (moKeyFilter2.getSelectedIndex() > 0) {
            moKeyFilter2.setSelectedIndex(0);
            moKeyFilter2.requestFocus();
        }
    }
    
    private void itemStateChangedFilter1() {
        moPaneView.putFilter(mnFilterType1, new SGridFilterValue(mnFilterType1, SGridConsts.FILTER_DATA_TYPE_INT_ARRAY, ((SGuiItem) moKeyFilter1.getSelectedItem()).getPrimaryKey()));
        jbClearFilter1.setEnabled(moKeyFilter1.getSelectedIndex() > 0);
        mbJustUpdated = true;
    }
    
    private void itemStateChangedFilter2() {
        if (!mbJustUpdated) {
            moPaneView.putFilter(mnFilterType2, new SGridFilterValue(mnFilterType2, SGridConsts.FILTER_DATA_TYPE_INT_ARRAY, ((SGuiItem) moKeyFilter2.getSelectedItem()).getPrimaryKey()));
            jbClearFilter2.setEnabled(moKeyFilter2.getSelectedIndex() > 0);
        }
        mbJustUpdated = false;
    }
    
    /*
     * Public methods
     */
    
    public void updateOptions() {
        moKeyFilter1.removeItemListener(this);
        moKeyFilter2.removeItemListener(this);
        
        moFieldKeyGroup.initGroup();
        moFieldKeyGroup.addFieldKey(moKeyFilter1, mnFilterType1, ((SClientInterface) miClient).getSessionXXX().getCurrentCompany().getPkCompanyId(), new SGuiParams(SDbRegistry.FIELD_CODE));
        moFieldKeyGroup.addFieldKey(moKeyFilter2, mnFilterType2, SLibConsts.UNDEFINED, new SGuiParams(SDbRegistry.FIELD_CODE));
        moFieldKeyGroup.populateCatalogues();
        moFieldKeyGroup.resetGroup();
        
        actionClearFilter1();
        
        moKeyFilter1.addItemListener(this);
        moKeyFilter2.addItemListener(this);
    }
    
    /*
     * Protected methods
     */
    
    @Override
    public void initFilter(Object value) {
        int[] key = null;
        
        moKeyFilter1.removeItemListener(this);
        moKeyFilter2.removeItemListener(this);
        
        key = value == null ? null : new int[] { ((int[]) value)[0] };
        SGuiUtils.locateItem(moKeyFilter1, key);
        jbClearFilter1.setEnabled(moKeyFilter1.getSelectedIndex() > 0);
        moPaneView.getFiltersMap().put(mnFilterType1, new SGridFilterValue(mnFilterType1, SGridConsts.FILTER_DATA_TYPE_INT_ARRAY, moKeyFilter1.getSelectedIndex() <= 0 ? null : key));
        
        key = value == null ? null : new int[] { ((int[]) value)[0], ((int[]) value)[1] };
        SGuiUtils.locateItem(moKeyFilter2, key);
        jbClearFilter2.setEnabled(moKeyFilter2.getSelectedIndex() > 0);
        moPaneView.getFiltersMap().put(mnFilterType2, new SGridFilterValue(mnFilterType2, SGridConsts.FILTER_DATA_TYPE_INT_ARRAY, moKeyFilter2.getSelectedIndex() <= 0 ? null : key));
        
        moKeyFilter1.addItemListener(this);
        moKeyFilter2.addItemListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == jbClearFilter1) {
                actionClearFilter1();
            }
            else if (button == jbClearFilter2) {
                actionClearFilter2();
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof JComboBox) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                JComboBox comboBox = (JComboBox) e.getSource();

                if (comboBox == moKeyFilter1) {
                    itemStateChangedFilter1();
                }
                else if (comboBox == moKeyFilter2) {
                    itemStateChangedFilter2();
                }
            }
        }
    }
}
