/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.gui.grid;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import sa.lib.SLibConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilter;
import sa.lib.grid.SGridFilterValue;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiItem;
import sa.lib.gui.SGuiParams;
import sa.lib.gui.SGuiUtils;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SGridFilterPanelEmployee extends JPanel implements SGridFilter, ActionListener, ItemListener {

    public static int EMP_STATUS = 200;
    public static int EMP_STATUS_ACT = 2;
    public static int EMP_STATUS_INA = 3;
    public static int EMP_STATUS_ALL = 4;
    
    private SGuiClient miClient;
    private SGridPaneView moPaneView;
    private int mnFilterType1;
    private int mnFilterType2;
    
    /**
     * Creates new form SGridFilterPanelEmployee
     */
    public SGridFilterPanelEmployee(SGuiClient client, SGridPaneView paneView, int type1, int type2) {
        miClient = client;
        moPaneView = paneView;
        mnFilterType1 = type1;
        mnFilterType2 = type2;
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

        bgFilter = new javax.swing.ButtonGroup();
        jtbActive = new javax.swing.JToggleButton();
        jtbInactive = new javax.swing.JToggleButton();
        jtbAll = new javax.swing.JToggleButton();
        moKeyFilter1 = new sa.lib.gui.bean.SBeanFieldKey();
        jbClearFilter1 = new javax.swing.JButton();
        moKeyFilter2 = new sa.lib.gui.bean.SBeanFieldKey();
        jbClearFilter2 = new javax.swing.JButton();

        setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        bgFilter.add(jtbActive);
        jtbActive.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_po_act_off.gif"))); // NOI18N
        jtbActive.setSelected(true);
        jtbActive.setToolTipText("Ver activos");
        jtbActive.setPreferredSize(new java.awt.Dimension(23, 23));
        jtbActive.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_po_act_on.gif"))); // NOI18N
        add(jtbActive);

        bgFilter.add(jtbInactive);
        jtbInactive.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_po_ina_off.gif"))); // NOI18N
        jtbInactive.setToolTipText("Ver inactivos");
        jtbInactive.setPreferredSize(new java.awt.Dimension(23, 23));
        jtbInactive.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_po_ina_on.gif"))); // NOI18N
        add(jtbInactive);

        bgFilter.add(jtbAll);
        jtbAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/switch_filter_off.gif"))); // NOI18N
        jtbAll.setToolTipText("Ver todos");
        jtbAll.setPreferredSize(new java.awt.Dimension(23, 23));
        jtbAll.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/switch_filter_on.gif"))); // NOI18N
        add(jtbAll);

        moKeyFilter1.setPreferredSize(new java.awt.Dimension(125, 23));
        add(moKeyFilter1);

        jbClearFilter1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sa/lib/img/cmd_std_delete_tmp.gif"))); // NOI18N
        jbClearFilter1.setToolTipText("Quitar filtro");
        jbClearFilter1.setPreferredSize(new java.awt.Dimension(23, 23));
        add(jbClearFilter1);

        moKeyFilter2.setPreferredSize(new java.awt.Dimension(250, 23));
        add(moKeyFilter2);

        jbClearFilter2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sa/lib/img/cmd_std_delete_tmp.gif"))); // NOI18N
        jbClearFilter2.setToolTipText("Quitar filtro");
        jbClearFilter2.setPreferredSize(new java.awt.Dimension(23, 23));
        add(jbClearFilter2);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgFilter;
    private javax.swing.JButton jbClearFilter1;
    private javax.swing.JButton jbClearFilter2;
    private javax.swing.JToggleButton jtbActive;
    private javax.swing.JToggleButton jtbAll;
    private javax.swing.JToggleButton jtbInactive;
    private sa.lib.gui.bean.SBeanFieldKey moKeyFilter1;
    private sa.lib.gui.bean.SBeanFieldKey moKeyFilter2;
    // End of variables declaration//GEN-END:variables

    /*
     * Private methods
     */
    
    private void actionEmpStatusStateChange() {
        int nFilterEmpStatus = SLibConsts.UNDEFINED;
        
        if (jtbActive.isSelected()) {
            nFilterEmpStatus = EMP_STATUS_ACT;
        }
        else if (jtbInactive.isSelected()) {
            nFilterEmpStatus = EMP_STATUS_INA;
        }
        else if (jtbAll.isSelected()) {
            nFilterEmpStatus = EMP_STATUS_ALL;
        }
        moPaneView.putFilter(EMP_STATUS, new SGridFilterValue(EMP_STATUS, SGridConsts.FILTER_DATA_TYPE_INT, nFilterEmpStatus));
        jbClearFilter1.setEnabled(moKeyFilter1.getSelectedIndex() > 0);
    }
    
    private void initComponentsCustom() {
        jbClearFilter1.addActionListener(this);
        jbClearFilter2.addActionListener(this);
        updateOptions();
    }
    
    private void actionClearFilter1() {
        jbClearFilter1.setEnabled(false);
        
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
    }
    
    private void itemStateChangedFilter2() {
        moPaneView.putFilter(mnFilterType2, new SGridFilterValue(mnFilterType2, SGridConsts.FILTER_DATA_TYPE_INT_ARRAY, ((SGuiItem) moKeyFilter2.getSelectedItem()).getPrimaryKey()));
        jbClearFilter2.setEnabled(moKeyFilter2.getSelectedIndex() > 0);
    }
    
    /*
     * Public methods
     */
    
    public void updateOptions() {
        moKeyFilter1.removeItemListener(this);
        moKeyFilter2.removeItemListener(this);
        jtbActive.removeItemListener(this);
        jtbInactive.removeItemListener(this);
        jtbAll.removeItemListener(this);
        
        miClient.getSession().populateCatalogue(moKeyFilter1, mnFilterType1, SLibConsts.UNDEFINED, new SGuiParams(SDbRegistry.FIELD_CODE));
        miClient.getSession().populateCatalogue(moKeyFilter2, mnFilterType2, SLibConsts.UNDEFINED, new SGuiParams(SDbRegistry.FIELD_CODE));
        
        actionClearFilter1();
        actionClearFilter2();
        
        moKeyFilter1.addItemListener(this);
        moKeyFilter2.addItemListener(this);
        jtbActive.addItemListener(this);
        jtbInactive.addItemListener(this);
        jtbAll.addItemListener(this);
    }
    
    /*
     * Protected methods
     */
    
    @Override
    public void initFilter(Object value) {
        int[] key = null;
        
        moKeyFilter1.removeItemListener(this);
        moKeyFilter2.removeItemListener(this);
        jtbActive.removeItemListener(this);
        jtbInactive.removeItemListener(this);
        jtbAll.removeItemListener(this);
        
        key = value == null ? null : new int[] { ((int[]) value)[0] };
        SGuiUtils.locateItem(moKeyFilter1, key);
        jbClearFilter1.setEnabled(moKeyFilter1.getSelectedIndex() > 0);
        moPaneView.getFiltersMap().put(mnFilterType1, new SGridFilterValue(mnFilterType1, SGridConsts.FILTER_DATA_TYPE_INT_ARRAY, moKeyFilter1.getSelectedIndex() <= 0 ? null : key));
        
        key = value == null ? null : new int[] { ((int[]) value)[0], ((int[]) value)[1] };
        SGuiUtils.locateItem(moKeyFilter2, key);
        jbClearFilter2.setEnabled(moKeyFilter2.getSelectedIndex() > 0);
        moPaneView.getFiltersMap().put(mnFilterType2, new SGridFilterValue(mnFilterType2, SGridConsts.FILTER_DATA_TYPE_INT_ARRAY, moKeyFilter2.getSelectedIndex() <= 0 ? null : key));
        
        jtbActive.setSelected(true); // by default, allways show first active registries
        moPaneView.getFiltersMap().put(EMP_STATUS, new SGridFilterValue(EMP_STATUS, SGridConsts.FILTER_DATA_TYPE_INT, EMP_STATUS_ACT));
        
        moKeyFilter1.addItemListener(this);
        moKeyFilter2.addItemListener(this);
        jtbActive.addItemListener(this);
        jtbInactive.addItemListener(this);
        jtbAll.addItemListener(this);
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
        else if (e.getSource() instanceof JToggleButton) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                JToggleButton toggleButton = (JToggleButton) e.getSource();

                if (toggleButton == jtbActive || toggleButton == jtbInactive || toggleButton == jtbAll) {
                    actionEmpStatusStateChange();
                }
            }
        }
    }
}
