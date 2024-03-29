/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * STabFilterCurrency.java
 */

package erp.table;

import erp.lib.SLibConstants;
import erp.lib.table.STableSetting;
import javax.swing.JToggleButton;

/**
 *
 * @author Edwin Carmona
 */
public class STabFilterCurrency extends javax.swing.JPanel implements java.awt.event.ActionListener {

    private static final String TXT_CUR_SYS = "MONEDA LOCAL";
    private static final String TXT_CUR_DPS = "MONEDA DOCTO.";

    private erp.client.SClientInterface miClient;
    private erp.lib.table.STableTab moTab;
    private erp.lib.table.STableSetting moSetting;

    private int mnDataType;
    
    public static final int TP_SYSTEM_CURRENCY = 1;
    
    /** Creates new form STabFilterCurrency */
    public STabFilterCurrency(erp.client.SClientInterface client, erp.lib.table.STableTab tableTab) {
        miClient = client;
        moTab = tableTab;

        initComponents();
        initComponentsExtra();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jtfFilterCurrency = new javax.swing.JTextField();
        jtbFilterCurrency = new javax.swing.JToggleButton();

        setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 3, 0));

        jtfFilterCurrency.setEditable(false);
        jtfFilterCurrency.setText("TEXT");
        jtfFilterCurrency.setToolTipText("Tipo moneda");
        jtfFilterCurrency.setPreferredSize(new java.awt.Dimension(100, 23));
        add(jtfFilterCurrency);

        jtbFilterCurrency.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/switch_money_off.gif"))); // NOI18N
        jtbFilterCurrency.setToolTipText("Cambiar tipo moneda");
        jtbFilterCurrency.setPreferredSize(new java.awt.Dimension(23, 23));
        jtbFilterCurrency.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/switch_money_on.gif"))); // NOI18N
        add(jtbFilterCurrency);
    }// </editor-fold>//GEN-END:initComponents

    private void initComponentsExtra() {
        moSetting = new STableSetting(SFilterConstants.SETTING_FILTER_CURRENCY, STabFilterCurrency.TP_SYSTEM_CURRENCY);
        
        jtbFilterCurrency.addActionListener(this);
        
        jtbFilterCurrency.setSelected(true);

        actionFilterCurrency();
    }
    
    private void actionFilterCurrency() {
        mnDataType = jtbFilterCurrency.isSelected() ? STabFilterCurrency.TP_SYSTEM_CURRENCY : SLibConstants.UNDEFINED;
        moSetting.setSetting(mnDataType);
        moTab.updateSetting(moSetting);
        
        renderText();
    }

    private void renderText() {
        jtfFilterCurrency.setText(mnDataType == STabFilterCurrency.TP_SYSTEM_CURRENCY ? TXT_CUR_SYS : TXT_CUR_DPS);
        jtfFilterCurrency.setCaretPosition(0);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton jtbFilterCurrency;
    private javax.swing.JTextField jtfFilterCurrency;
    // End of variables declaration//GEN-END:variables
 
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {                                                 
        if (e.getSource() instanceof javax.swing.JToggleButton) {
            JToggleButton toggleButton = (JToggleButton) e.getSource();

            if (toggleButton == jtbFilterCurrency) {
                actionFilterCurrency();
            }
        }
    }
}
