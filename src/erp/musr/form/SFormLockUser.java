/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.musr.form;

import erp.lib.SLibConstants;
import erp.lib.data.SDataRegistry;
import erp.lib.form.SFormValidation;
import erp.redis.SRedisLockUtils;
import java.awt.event.ActionEvent;
import java.util.Set;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;
import redis.clients.jedis.Jedis;

/**
 *
 * @author SW
 */
public class SFormLockUser extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener {

    private erp.client.SClientInterface miClient;
    private Jedis jedis;
    DefaultTableModel modelo = new DefaultTableModel();
    
    /**
     * Creates new form SFormUserSession
     * @param client
     */
    public SFormLockUser(erp.client.SClientInterface client) {
        super(client.getFrame(), true);
        miClient = client;
        //jedis = miClient.getJedis();
        initComponents();
        initComponentsExtra();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTLocks = new javax.swing.JTable();
        jPanel26 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jtType = new javax.swing.JTextField();
        jPanel28 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jtId = new javax.swing.JTextField();
        jPanel29 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jtCompanyId = new javax.swing.JTextField();
        jPanel30 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jtRegistryType = new javax.swing.JTextField();
        jPanel31 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jtPK = new javax.swing.JTextField();
        jPanel32 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jtSessionId = new javax.swing.JTextField();
        jPanel33 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jtUserId = new javax.swing.JTextField();
        jPanel24 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jbReload = new javax.swing.JButton();
        jPanel34 = new javax.swing.JPanel();
        jbErase = new javax.swing.JButton();
        jbClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Candados Activos");
        setMinimumSize(new java.awt.Dimension(246, 193));
        setPreferredSize(new java.awt.Dimension(700, 217));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel1.setMinimumSize(new java.awt.Dimension(246, 160));
        jPanel1.setPreferredSize(new java.awt.Dimension(699, 400));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel25.setPreferredSize(new java.awt.Dimension(480, 399));
        jPanel25.setLayout(new java.awt.BorderLayout());

        jPanel3.setPreferredSize(new java.awt.Dimension(479, 298));
        jPanel3.setLayout(new java.awt.GridLayout(1, 1));

        jPanel5.setPreferredSize(new java.awt.Dimension(478, 297));
        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jScrollPane1.setPreferredSize(new java.awt.Dimension(476, 296));

        jTLocks.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTLocks.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTLocksMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTLocks);

        jPanel5.add(jScrollPane1);

        jPanel3.add(jPanel5);

        jPanel25.add(jPanel3, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel25, java.awt.BorderLayout.WEST);

        jPanel26.setPreferredSize(new java.awt.Dimension(200, 150));
        jPanel26.setLayout(new java.awt.BorderLayout());

        jPanel2.setPreferredSize(new java.awt.Dimension(199, 200));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel4.setMinimumSize(new java.awt.Dimension(60, 100));
        jPanel4.setPreferredSize(new java.awt.Dimension(199, 150));
        jPanel4.setLayout(new java.awt.GridLayout(8, 1));

        jPanel27.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setText("Tipo");
        jLabel1.setMaximumSize(new java.awt.Dimension(60, 14));
        jLabel1.setMinimumSize(new java.awt.Dimension(60, 14));
        jLabel1.setPreferredSize(new java.awt.Dimension(60, 23));
        jPanel27.add(jLabel1);

        jtType.setPreferredSize(new java.awt.Dimension(125, 23));
        jtType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtTypeActionPerformed(evt);
            }
        });
        jPanel27.add(jtType);

        jPanel4.add(jPanel27);

        jPanel28.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel2.setText("Id");
        jLabel2.setPreferredSize(new java.awt.Dimension(60, 23));
        jPanel28.add(jLabel2);

        jtId.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel28.add(jtId);

        jPanel4.add(jPanel28);

        jPanel29.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel3.setText("compañia Id");
        jLabel3.setPreferredSize(new java.awt.Dimension(60, 23));
        jPanel29.add(jLabel3);

        jtCompanyId.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel29.add(jtCompanyId);

        jPanel4.add(jPanel29);

        jPanel30.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel4.setText("Registro");
        jLabel4.setPreferredSize(new java.awt.Dimension(60, 23));
        jPanel30.add(jLabel4);

        jtRegistryType.setPreferredSize(new java.awt.Dimension(125, 23));
        jtRegistryType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtRegistryTypeActionPerformed(evt);
            }
        });
        jPanel30.add(jtRegistryType);

        jPanel4.add(jPanel30);

        jPanel31.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel5.setText("PK");
        jLabel5.setPreferredSize(new java.awt.Dimension(60, 23));
        jPanel31.add(jLabel5);

        jtPK.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel31.add(jtPK);

        jPanel4.add(jPanel31);

        jPanel32.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel6.setText("sesion Id");
        jLabel6.setPreferredSize(new java.awt.Dimension(60, 23));
        jPanel32.add(jLabel6);

        jtSessionId.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel32.add(jtSessionId);

        jPanel4.add(jPanel32);

        jPanel33.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel7.setText("usuario Id");
        jLabel7.setPreferredSize(new java.awt.Dimension(60, 23));
        jPanel33.add(jLabel7);

        jtUserId.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel33.add(jtUserId);

        jPanel4.add(jPanel33);

        jPanel2.add(jPanel4, java.awt.BorderLayout.CENTER);

        jPanel26.add(jPanel2, java.awt.BorderLayout.NORTH);

        jPanel1.add(jPanel26, java.awt.BorderLayout.EAST);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel24.setLayout(new java.awt.BorderLayout());

        jPanel6.setMinimumSize(new java.awt.Dimension(50, 10));
        jPanel6.setPreferredSize(new java.awt.Dimension(100, 10));
        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jbReload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_reload.gif"))); // NOI18N
        jbReload.setToolTipText("Modificar datos");
        jbReload.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel6.add(jbReload);

        jPanel24.add(jPanel6, java.awt.BorderLayout.WEST);

        jPanel34.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbErase.setText("Eliminar");
        jPanel34.add(jbErase);

        jbClose.setText("Cerrar");
        jPanel34.add(jbClose);

        jPanel24.add(jPanel34, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel24, java.awt.BorderLayout.PAGE_END);

        setSize(new java.awt.Dimension(720, 450));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void jtRegistryTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtRegistryTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtRegistryTypeActionPerformed

    private void jTLocksMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTLocksMouseClicked
        int Fila = jTLocks.getSelectedRow();
        String type = jTLocks.getValueAt(Fila, 0).toString();
        String id = jTLocks.getValueAt(Fila, 1).toString();
        String companyId = jTLocks.getValueAt(Fila, 2).toString();
        String RegistryType = jTLocks.getValueAt(Fila, 3).toString();
        String PK = jTLocks.getValueAt(Fila, 4).toString();
        String sessionId = jTLocks.getValueAt(Fila, 5).toString();
        String userId = jTLocks.getValueAt(Fila, 6).toString();
        
        jtType.setText(type);
        jtId.setText(id);
        jtCompanyId.setText(companyId);
        jtRegistryType.setText(RegistryType);
        jtPK.setText(PK);
        jtSessionId.setText(sessionId);
        jtUserId.setText(userId);
    }//GEN-LAST:event_jTLocksMouseClicked

    private void jtTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtTypeActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTLocks;
    private javax.swing.JButton jbClose;
    private javax.swing.JButton jbErase;
    private javax.swing.JButton jbReload;
    private javax.swing.JTextField jtCompanyId;
    private javax.swing.JTextField jtId;
    private javax.swing.JTextField jtPK;
    private javax.swing.JTextField jtRegistryType;
    private javax.swing.JTextField jtSessionId;
    private javax.swing.JTextField jtType;
    private javax.swing.JTextField jtUserId;
    // End of variables declaration//GEN-END:variables

    private void windowActivated() {
        
    }
    
    private void initComponentsExtra(){
        
        jtType.setEditable(false);
        jtId.setEditable(false);
        jtCompanyId.setEditable(false);
        jtRegistryType.setEditable(false);
        jtPK.setEditable(false);
        jtSessionId.setEditable(false);
        jtUserId.setEditable(false);
        
        jbErase.addActionListener(this);
        jbClose.addActionListener(this);
        jbReload.addActionListener(this);
        
        jTLocks.setModel(modelo);
        
        modelo.addColumn("Type");
        modelo.addColumn("id");
        modelo.addColumn("companyId");
        modelo.addColumn("RegistryType");
        modelo.addColumn("pk");
        modelo.addColumn("sessionId");
        modelo.addColumn("userId");

        int[] anchos = {50, 50, 100, 50, 100, 60, 50};
        for (int i = 0; i < jTLocks.getColumnCount(); i++) {
            jTLocks.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }
        
        //Set<String> Keys = jedis.keys("Lock+*");
        Vector keys = SRedisLockUtils.getLocksList(jedis);
                
        for(int i=0; i<keys.size(); i++){
            String stringKey = keys.get(i).toString();
            String[] splitKey = stringKey.split("\\+");
            modelo.addRow(splitKey);
        }
        
    }
    
    private void actionEraseLock(){
        /*String KeyLock = jtType.getText() + "+" +
                        jtId.getText() + "+" +
                        jtCompanyId.getText() + "+" +
                        jtRegistryType.getText() + "+" +
                        jtPK.getText() + "+" +
                        jtSessionId.getText() + "+" +
                        jtUserId.getText();
        if(jtType.getText().equals("Lock") && !jtId.getText().equals("") && !jtCompanyId.getText().equals("") && !jtRegistryType.getText().equals("")
                && !jtPK.getText().equals("") && !jtSessionId.getText().equals("") && !jtUserId.getText().equals("")){
            moDialogEraseLock.formReset();
            moDialogEraseLock.setLock(KeyLock);
            moDialogEraseLock.setVisible(true);

            if(moDialogEraseLock.getFormResult() == SLibConstants.FORM_RESULT_OK){
                if(jedis.del(KeyLock) == 1){
                    refreshTable();
                }else{
                    JOptionPane.showMessageDialog(this, "No se encontro el candado a borrar");
                }
            }
        }else{
            JOptionPane.showMessageDialog(this, "No se selecciono ningun candado");
        }
            */
    }
    
    private void refreshTable(){
        DefaultTableModel modelo = new DefaultTableModel();
        
        jTLocks.setModel(modelo);
        
        modelo.addColumn("Type");
        modelo.addColumn("id");
        modelo.addColumn("companyId");
        modelo.addColumn("RegistryType");
        modelo.addColumn("pk");
        modelo.addColumn("sessionId");
        modelo.addColumn("userId");

        int[] anchos = {50, 50, 100, 50, 100, 60, 50};
        for (int i = 0; i < jTLocks.getColumnCount(); i++) {
            jTLocks.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }
        
        Set<String> Keys = jedis.keys("Lock+*");
        
        for(int i=0; i<Keys.size(); i++){
            String sKey = Keys.toArray()[i].toString();
            String[] splitKey = sKey.split("\\+");
            modelo.addRow(splitKey);
        }
        
        jtType.setText("");
        jtId.setText("");
        jtCompanyId.setText("");
        jtRegistryType.setText("");
        jtPK.setText("");
        jtSessionId.setText("");
        jtUserId.setText("");
    }
    
    @Override
    public void formClearRegistry() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void formReset() {
        
    }

    @Override
    public void formRefreshCatalogues() {
        
    }

    @Override
    public SFormValidation formValidate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setFormStatus(int status) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setFormVisible(boolean visible) {
        setVisible(visible);
    }

    @Override
    public int getFormStatus() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getFormResult() {
        return SLibConstants.FORM_RESULT_CANCEL;
    }

    @Override
    public void setRegistry(SDataRegistry registry) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SDataRegistry getRegistry() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setValue(int type, Object value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getValue(int type) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JLabel getTimeoutLabel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JButton) {
            javax.swing.JButton button = (javax.swing.JButton) e.getSource();

            if (button == jbErase) {
                actionEraseLock();
            }else if (button == jbClose) {
                setVisible(false);
            }else if (button == jbReload) {
                refreshTable();
            }
        }
    }
}
