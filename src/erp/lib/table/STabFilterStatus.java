/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.lib.table;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.lib.form.SFormUtilities;

/**
 *
 * @author swaplicado
 */
public class STabFilterStatus extends javax.swing.JPanel {
    
    private erp.client.SClientInterface miClient;
    private erp.lib.table.STableTabInterface miTableTab;

    private erp.lib.table.STableSetting moSetting;
    private String contenido;
    
    public STabFilterStatus(erp.lib.table.STableTabInterface tableTab, SClientInterface client) {
        miTableTab = tableTab;
        miClient = client;

        initComponents();
        initComponentsExtra();
    }

   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        moKeyFilter = new javax.swing.JComboBox();
        jbClearFilter = new javax.swing.JButton();

        setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 3, 0));

        moKeyFilter.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        add(moKeyFilter);

        jbClearFilter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sa/lib/img/cmd_std_delete_tmp.gif"))); // NOI18N
        jbClearFilter.setToolTipText("Quitar filtro");
        jbClearFilter.setPreferredSize(new java.awt.Dimension(23, 23));
        jbClearFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbClearFilterActionPerformed(evt);
            }
        });
        add(jbClearFilter);
    }// </editor-fold>//GEN-END:initComponents

    private void jbClearFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbClearFilterActionPerformed
        jbClearFilter.setEnabled(false);
        
        if (moKeyFilter.getSelectedIndex() > 0) {
            moKeyFilter.setSelectedIndex(0);
            moKeyFilter.requestFocus();
        }
    }//GEN-LAST:event_jbClearFilterActionPerformed
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jbClearFilter;
    private javax.swing.JComboBox moKeyFilter;
    // End of variables declaration//GEN-END:variables
    
    @SuppressWarnings("unchecked")
    private void initComponentsExtra() {
        SFormUtilities.populateComboBox(miClient, moKeyFilter, SDataConstants.ITMS_ST_ITEM);
        moSetting = new STableSetting(STableConstants.SETTING_FILTER_STATUS, STableConstants.STATUS_ON);
        moSetting.setSetting(" ");
        miTableTab.addSetting(moSetting);
        moKeyFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moKeyFilterActionPerformed(evt);
            }
        });
    }
    private void moKeyFilterActionPerformed(java.awt.event.ActionEvent evt){
         contenido = moKeyFilter.getSelectedItem().toString();
         moSetting.setSetting(contenido);
         miTableTab.updateSetting(moSetting);          
    }
}
