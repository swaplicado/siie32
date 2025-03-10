/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.view;

import erp.gui.account.SAccountUtils;
import erp.mcfg.data.SDataParamsCompany;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import sa.lib.SLibUtils;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilter;
import sa.lib.grid.SGridFilterValue;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;

/**
 *
 * @author Sergio Flores
 */
public class SPaneCostCenterLevel extends javax.swing.JPanel implements SGridFilter, ActionListener {
    
    public static final int FILTER_LEVEL = 1001;

    protected SGuiClient miClient;
    protected SGridPaneView moPaneView;
    protected int mnLevelCur;
    protected int mnLevelMax;
    protected String msCostCenterCode;
    protected int[] manCostCenterLevelsLengths;
    
    /**
     * Creates new form SFinPanelCostCenterLevel
     * @param client
     * @param paneView
     */
    public SPaneCostCenterLevel(SGuiClient client, SGridPaneView paneView) {
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

        jtfLevel = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jbIncrement = new javax.swing.JButton();
        jbDecrement = new javax.swing.JButton();

        setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfLevel.setEditable(false);
        jtfLevel.setText("CC Level: 1 of 4");
        jtfLevel.setFocusable(false);
        jtfLevel.setPreferredSize(new java.awt.Dimension(100, 23));
        add(jtfLevel);

        jPanel1.setLayout(new java.awt.GridLayout(2, 1, 0, 1));

        jbIncrement.setText("+");
        jbIncrement.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jbIncrement.setPreferredSize(new java.awt.Dimension(20, 11));
        jPanel1.add(jbIncrement);

        jbDecrement.setText("-");
        jbDecrement.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jbDecrement.setPreferredSize(new java.awt.Dimension(20, 11));
        jPanel1.add(jbDecrement);

        add(jPanel1);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton jbDecrement;
    private javax.swing.JButton jbIncrement;
    private javax.swing.JTextField jtfLevel;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        jbIncrement.addActionListener(this);
        jbDecrement.addActionListener(this);
        
        int mask = ((SDataParamsCompany) miClient.getSession().getConfigCompany()).getMaskCostCenter();
        int levels = SAccountUtils.getLevels(mask);
        
        mnLevelCur = 1;
        mnLevelMax = SAccountUtils.getLevels(mask);
        renderLevel();
        
        msCostCenterCode = SAccountUtils.composeCodeUsrZeros(mask);
        manCostCenterLevelsLengths = new int[levels];
        
        for (int i = 0; i < levels; i++) {
            manCostCenterLevelsLengths[i] = SAccountUtils.getLengthCodeUsr(mask, i + 1);
        }
    }
    
    private void renderLevel() {
        jtfLevel.setText("Nivel CC: " + mnLevelCur + " de " + mnLevelMax);
        jtfLevel.setCaretPosition(0);
    }
    
    private void updateLevel(final boolean broadcastEvent) {
        renderLevel();
        
        // compose & broadcast filter:
        
        int leftChars = 0;
        String rightMask = "";
        
        if (mnLevelCur < mnLevelMax) {
            leftChars = manCostCenterLevelsLengths[mnLevelCur - 1];
            rightMask = SLibUtils.textRight(msCostCenterCode, msCostCenterCode.length() - leftChars);
        }
        
        SGridFilterValue value = new SGridFilterValue(SGridConsts.FILTER_CUSTOM, SGridConsts.FILTER_DATA_TYPE_OBJ, new Filter(leftChars, rightMask));
        
        if (broadcastEvent) {
            moPaneView.putFilter(FILTER_LEVEL, value);
        }
        else {
            moPaneView.getFiltersMap().put(FILTER_LEVEL, value);
        }
    }
    
    private void actionPerformedAdjust(final boolean increment) {
        boolean adjusted = false;
        
        if (increment) {
            if (mnLevelCur < mnLevelMax) {
                mnLevelCur++;
                adjusted = true;
            }
        }
        else {
            if (mnLevelCur > 1) {
                mnLevelCur--;
                adjusted = true;
            }
        }
        
        if (adjusted) {
            updateLevel(true);
        }
    }
    
    public void setPaneEnabled(final boolean enable) {
        jbIncrement.setEnabled(enable);
        jbDecrement.setEnabled(enable);
    }
    
    @Override
    public void initFilter(final Object value) {
        mnLevelCur = (Integer) value;
        updateLevel(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == jbIncrement) {
                actionPerformedAdjust(true);
            }
            else if (button == jbDecrement) {
                actionPerformedAdjust(false);
            }
        }
    }
    
    public class Filter {
        int LeftChars;
        String RightMask;
        
        public Filter(final int leftChars, String rightMask) {
            LeftChars = leftChars;
            RightMask = rightMask;
        }
    }
}
