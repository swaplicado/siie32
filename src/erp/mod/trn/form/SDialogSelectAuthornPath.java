/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.form;

import erp.data.SDataConstantsSys;
import erp.mod.cfg.db.SDbAuthorizationPath;
import erp.mod.cfg.utils.SAuthorizationUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JDialog;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;

/**
 *
 * @author Isabel Servín
 */
public class SDialogSelectAuthornPath extends JDialog implements ActionListener {

    private final SGuiClient miClient;
    private ArrayList<SDbAuthorizationPath> maAllAuthPaths;
    private ArrayList<SDbAuthorizationPath> maSelectedAuthPaths;
    private int mnFormResult;
    
    /**
     * Creates new form SDialogSelectAuthornPath
     * @param client
     */
    public SDialogSelectAuthornPath(SGuiClient client) {
        miClient = client;
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

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListAuthornPath = new javax.swing.JList();
        jPanel3 = new javax.swing.JPanel();
        jbSelect = new javax.swing.JButton();
        jbClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Seleccionar ruta de autorización");
        setIconImages(null);
        setModal(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setText("Para seleccionar varias opciones mantener presionada la tecla Ctrl");
        jLabel1.setEnabled(false);
        jLabel1.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel4.add(jLabel1);

        jPanel2.add(jPanel4, java.awt.BorderLayout.NORTH);

        jScrollPane1.setViewportView(jListAuthornPath);

        jPanel2.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbSelect.setText("Seleccionar");
        jbSelect.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel3.add(jbSelect);

        jbClose.setText("Cancelar");
        jbClose.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel3.add(jbClose);

        jPanel1.add(jPanel3, java.awt.BorderLayout.SOUTH);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        actionClose();
    }//GEN-LAST:event_formWindowClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList jListAuthornPath;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbClose;
    private javax.swing.JButton jbSelect;
    // End of variables declaration//GEN-END:variables

    @SuppressWarnings("unchecked")
    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 640, 400);
        
        Vector<String> modeloLista = new Vector<>();
        try {
            HashMap<String, Integer> map = new HashMap<>();
            map.put("fid_ct_dps", SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[0]);
            map.put("fid_cl_dps", SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[1]);
            map.put("fid_tp_dps", SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[2]);
            maAllAuthPaths = SAuthorizationUtils.getConfigurationsDps(miClient.getSession(), map);
            maAllAuthPaths.stream().forEach((path) -> {
                modeloLista.add(path.getName());
            });
        }
        catch (Exception e) {
            miClient.showMsgBoxError(e.getMessage());
        }
        
        jListAuthornPath.setListData(modeloLista);
        
        SGuiUtils.createActionMap(rootPane, this, "actionClose", "close", KeyEvent.VK_ESCAPE);
        
        jbSelect.addActionListener(this);
        jbClose.addActionListener(this);
    }
    
    private SGuiValidation validateForm() {
        SGuiValidation validation = new SGuiValidation();
        
        if (jListAuthornPath.getSelectedIndex() <= -1) {
            validation.setMessage("Debe seleccionar un esquema de autorización.");
            validation.setComponent(jListAuthornPath);
        }
        
        return validation;
    }
    
    private void actionSelect() {
        SGuiValidation validation = validateForm();
        if (validation.isValid()) {
            maSelectedAuthPaths = new ArrayList<>();
            List list = jListAuthornPath.getSelectedValuesList();
            for (Object list1 : list) {
                for (SDbAuthorizationPath path : maAllAuthPaths) {
                    String selectedValue = list1.toString();
                    if (path.getName().equals(selectedValue)) {
                        maSelectedAuthPaths.add(path);
                        break;
                    }
                }
            }
            mnFormResult = SGuiConsts.FORM_RESULT_OK;
            this.setVisible(false);
        }
        else {
            miClient.showMsgBoxWarning(validation.getMessage());
            if (validation.getComponent() != null) {
                validation.getComponent().requestFocus();
            }
        }
    }

    public void actionClose() {
        mnFormResult = SGuiConsts.FORM_RESULT_CANCEL;
        this.setVisible(false);
    }
    
    public int getFormResult() {
        return mnFormResult;
    }
    
    public ArrayList<SDbAuthorizationPath> getSelectedAuthPaths() {
        return maSelectedAuthPaths;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == jbSelect) {
                actionSelect();
            }
            else if (button == jbClose) {
                actionClose();
            }
        }
    }
}
