/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.utils;

import java.util.ArrayList;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanFormDialog;

/**
 *
 * @author Sergio Flores
 */
public class SDialogMessages extends SBeanFormDialog {

    private int mnMessagesCount;
    
    /**
     * Creates new form SDialogMessages
     * @param client GUI client.
     * @param title Dialog's title.
     * @param label Messages' label.
     */
    public SDialogMessages(SGuiClient client, String title, String label) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, 0, 0, title);
        initComponents();
        initComponentsCustom();
        jlLabel.setText(label);
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
        jlLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jspMessages = new javax.swing.JScrollPane();
        jtaMessages = new javax.swing.JTextArea();

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jlLabel.setText("<label>");
        jlLabel.setPreferredSize(new java.awt.Dimension(600, 23));
        jPanel1.add(jlLabel);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jspMessages.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jtaMessages.setEditable(false);
        jtaMessages.setBackground(java.awt.SystemColor.control);
        jtaMessages.setColumns(20);
        jtaMessages.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        jtaMessages.setLineWrap(true);
        jtaMessages.setRows(5);
        jtaMessages.setText("1. Message A\n2. Message B\n3. Message C");
        jtaMessages.setWrapStyleWord(true);
        jspMessages.setViewportView(jtaMessages);

        jPanel2.add(jspMessages, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel jlLabel;
    private javax.swing.JScrollPane jspMessages;
    private javax.swing.JTextArea jtaMessages;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 640, 400);
        jbSave.setEnabled(false);
        jbCancel.setText(SUtilConsts.TXT_CLOSE);
        initForm();
    }
    
    /**
     * Append message, and increment messages count by one.
     * @param message Message to append.
     */
    public void appendMessage(final String message) {
        if (message != null && !message.isEmpty()) {
            mnMessagesCount++;
            jtaMessages.setText(jtaMessages.getText() + ((jtaMessages.getText().isEmpty() ? "" : "\n\n") + mnMessagesCount + ". " + message));
            jtaMessages.setCaretPosition(0);
        }
    }
    
    /**
     * Append messages, and increment messages count by number of messages.
     * @param messages Messages to append.
     */
    public void appendMessages(final ArrayList<String> messages) {
        for (String message : messages) {
            appendMessage(message);
        }
    }
    
    public int getMessagesCount() {
        return mnMessagesCount;
    }
    
    public String getMessages() {
        return jtaMessages.getText();
    }
    
    @Override
    public void initForm() {
        resetForm();
    }
    
    @Override
    public void resetForm() {
        mnMessagesCount = 0;
        jtaMessages.setText("");
    }
    
    @Override
    public void addAllListeners() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeAllListeners() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void reloadCatalogues() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SGuiValidation validateForm() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
