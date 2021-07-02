/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mbps.form;

import erp.lib.SLibConstants;
import erp.lib.form.SFormUtilities;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;

/**
 *
 * @author Juan Barajas
 */
public class SDialogViewEmployeeImages extends javax.swing.JDialog implements java.awt.event.ActionListener {

    private erp.client.SClientInterface miClient;
    private ImageIcon moImage;

    /** Creates new form SDialogViewEmployeeImages */
    public SDialogViewEmployeeImages(erp.client.SClientInterface client, ImageIcon image, String title) {
        super(client.getFrame(), true);
        miClient = client;
        moImage = image;

        initComponents();
        initComponentsExtra();
        setTitle(title);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jbCancel = new javax.swing.JButton();
        jpImage = new javax.swing.JPanel();
        jlImage = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Fotografía");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel1.setPreferredSize(new java.awt.Dimension(492, 33));
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbCancel.setText("Cerrar");
        jbCancel.setToolTipText("[Cancelar]");
        jPanel1.add(jbCancel);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        jpImage.setLayout(new java.awt.BorderLayout());

        jlImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jpImage.add(jlImage, java.awt.BorderLayout.CENTER);

        getContentPane().add(jpImage, java.awt.BorderLayout.CENTER);

        setSize(new java.awt.Dimension(656, 439));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void initComponentsExtra() {
        jlImage.setIcon(moImage);
        jbCancel.addActionListener(this);
        SFormUtilities.createActionMap(getRootPane(), this, "actionCancel", "cancel", KeyEvent.VK_ESCAPE, SLibConstants.UNDEFINED);
    }

    private void windowActivated() {
    }

    public void actionCancel() {
        setVisible(false);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton jbCancel;
    private javax.swing.JLabel jlImage;
    private javax.swing.JPanel jpImage;
    // End of variables declaration//GEN-END:variables

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JButton) {
            javax.swing.JButton button = (javax.swing.JButton) e.getSource();

            if (button == jbCancel) {
                actionCancel();
            }
        }
    }
}
