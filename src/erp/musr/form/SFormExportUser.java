/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.musr.form;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.data.SDataRegistry;
import erp.lib.form.SFormValidation;
import erp.lib.table.STableColumnForm;
import erp.lib.table.STablePane;
import erp.lib.table.STableRow;
import erp.mbps.data.SDataBizPartner;
import erp.musr.data.SDataUser;
import erp.musr.data.SDataUserAppRow;
import erp.musr.data.SUserUtils;
import erp.siieapp.SUserExportUtils;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import org.json.simple.parser.ParseException;
import sa.lib.gui.SGuiClient;

/**
 *
 * @author SW, Edwin Carmona
 */
public class SFormExportUser extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener {

    private erp.client.SClientInterface miClient;
    private erp.lib.form.SFormField moFieldEmail;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private ArrayList<SDataUserAppRow> lApps;
    private int mnUserId;
    private STablePane moUserPane;
    private SDataUser user;
    private SDataBizPartner moBizPartner;
    private int mnFormResult;

    /**
     * Creates new form SFormRedisSessionLocks
     */
    public SFormExportUser(erp.client.SClientInterface client) {
        super(client.getFrame(), true);
        miClient = client;
        initComponents();
        try {
            initComponentsExtra();
        } catch (SQLException ex) {
            Logger.getLogger(SFormExportUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(SFormExportUser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initComponentsExtra() throws SQLException, ParseException {
        mvFields = new Vector<>();
        mvFields.add(moFieldEmail);

        erp.lib.table.STableColumnForm tableColumnsUserRoles[];

        moUserPane = new STablePane(miClient);
        jpUserApps.add(moUserPane, BorderLayout.CENTER);

        int i = 0;
        tableColumnsUserRoles = new STableColumnForm[3];
        tableColumnsUserRoles[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Nombre", 200);
        tableColumnsUserRoles[i] = new STableColumnForm(SLibConstants.DATA_TYPE_BOOLEAN, "Acceso", 50);
        tableColumnsUserRoles[i++].setEditable(true);
        tableColumnsUserRoles[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_INTEGER, "Tipo", 100);

        for (i = 0; i < tableColumnsUserRoles.length; i++) {
            moUserPane.addTableColumn(tableColumnsUserRoles[i]);
        }

        jbCancel.addActionListener(this);
        jbExport.addActionListener(this);
    }

    private void initCustom() throws SQLException, ParseException {
        user = (SDataUser) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.USRU_USR, new int[]{mnUserId}, SLibConstants.EXEC_MODE_SILENT);
        moBizPartner = (SDataBizPartner) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.BPSU_BP, new int[]{user.getFkBizPartnerId_n()}, SLibConstants.EXEC_MODE_SILENT);
        if (user.getFkBizPartnerId_n() == 0) {
            if (miClient.showMsgBoxConfirm("Este usuario no tiene un asociado de negocios, ¿Deseas continuar?") != JOptionPane.YES_OPTION) {
                return;
            }
        }

        SUserExportUtils oExport = new SUserExportUtils((SGuiClient) miClient);
        String token = oExport.loginSiieApp();
        if (token.isEmpty()) {
            return;
        }
        lApps = oExport.getUserApps(mnUserId, token);
        if (lApps == null) {
            return;
        }
        jtfUser.setText(user.getUser());
        jtfEmail.setText(user.getEmail());
        moFieldEmail = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfEmail, jlEmail);
        moFieldEmail.setAutoCaseType(SLibConstants.CASE_LOWER);

        moUserPane.createTable();
        moUserPane.clearTableRows();

        for (SDataUserAppRow oAppRow : lApps) {
            moUserPane.addTableRow(oAppRow);
        }
        moUserPane.renderTableRows();
    }

    @SuppressWarnings("unchecked")
    private void actionExport() {
        int i;
        ArrayList<Vector> VecApps = new ArrayList<>();
        Vector<Object> vec = null;
        i = 0;
        for (STableRow row : moUserPane.getTableModel().getTableRows()) {
            SDataUserAppRow role = (SDataUserAppRow) moUserPane.getTableRow(i);
            vec = (Vector<Object>) row.getValues().clone();
            vec.add(0, role.getAppId());
            VecApps.add(vec);
            i++;
        }

        Statement statement = miClient.getSession().getStatement();

        String sql = "UPDATE erp.usru_usr as u"
                + " SET u.email = " + "'" + moFieldEmail.getString() + "'"
                + " WHERE u.id_usr = " + "'" + mnUserId + "'" + ";";

        try {
            statement.executeUpdate(sql);
            user.setEmail(moFieldEmail.getString());
        } catch (Exception e) {
            Logger.getLogger(SFormExportUser.class.getName()).log(Level.SEVERE, null, e);
        }

        try {
            SUserExportUtils oExport = new SUserExportUtils((SGuiClient) miClient);
            boolean res = oExport.exportUser(user, moBizPartner, VecApps);
            if (res) {
                miClient.showMsgBoxInformation("Usuario exportado con éxito");
                SUserUtils.updateLastSyncDate(miClient.getSession(), mnUserId);
                mnFormResult = SLibConstants.FORM_RESULT_OK;
                this.setVisible(false);
            }
        } catch (Exception e) {
            Logger.getLogger(SFormExportUser.class.getName()).log(Level.SEVERE, null, e);
        }
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
        jPanel17 = new javax.swing.JPanel();
        jbExport = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jbAddUserRole = new javax.swing.JButton();
        jbModifyUserRole = new javax.swing.JButton();
        jbDeleteUserRole = new javax.swing.JButton();
        jpUserApps = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jlUser = new javax.swing.JLabel();
        jtfUser = new javax.swing.JTextField();
        jlAux = new javax.swing.JLabel();
        jlEmail = new javax.swing.JLabel();
        jtfEmail = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Exportación de usuario");

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel17.setPreferredSize(new java.awt.Dimension(792, 33));
        jPanel17.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbExport.setText("Exportar");
        jbExport.setToolTipText("[Ctrl + Enter]");
        jPanel17.add(jbExport);

        jbCancel.setText("Cancelar");
        jbCancel.setToolTipText("[Escape]");
        jPanel17.add(jbCancel);

        jPanel1.add(jPanel17, java.awt.BorderLayout.SOUTH);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel7.setLayout(new java.awt.BorderLayout());

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Aplicaciones"));
        jPanel8.setPreferredSize(new java.awt.Dimension(771, 220));
        jPanel8.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel14.setPreferredSize(new java.awt.Dimension(755, 23));
        jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 1, 0));

        jbAddUserRole.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_new.gif"))); // NOI18N
        jbAddUserRole.setToolTipText("Crear [Ctrl+N]");
        jbAddUserRole.setEnabled(false);
        jbAddUserRole.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel14.add(jbAddUserRole);

        jbModifyUserRole.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_edit.gif"))); // NOI18N
        jbModifyUserRole.setToolTipText("Modificar [Ctrl+M]");
        jbModifyUserRole.setEnabled(false);
        jbModifyUserRole.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel14.add(jbModifyUserRole);

        jbDeleteUserRole.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_delete.gif"))); // NOI18N
        jbDeleteUserRole.setToolTipText("Eliminar [Ctrl+D]");
        jbDeleteUserRole.setEnabled(false);
        jbDeleteUserRole.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel14.add(jbDeleteUserRole);

        jPanel8.add(jPanel14, java.awt.BorderLayout.NORTH);

        jpUserApps.setLayout(new java.awt.BorderLayout());
        jPanel8.add(jpUserApps, java.awt.BorderLayout.CENTER);

        jPanel7.add(jPanel8, java.awt.BorderLayout.NORTH);

        jPanel4.add(jPanel7, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel4, java.awt.BorderLayout.CENTER);

        jPanel5.setPreferredSize(new java.awt.Dimension(776, 33));
        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jlUser.setText("Usuario:");
        jlUser.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel5.add(jlUser);

        jtfUser.setText("USER");
        jtfUser.setEnabled(false);
        jtfUser.setFocusable(false);
        jtfUser.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel5.add(jtfUser);

        jlAux.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel5.add(jlAux);

        jlEmail.setText("Email:");
        jlEmail.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel5.add(jlEmail);

        jtfEmail.setPreferredSize(new java.awt.Dimension(200, 23));
        jtfEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfEmailActionPerformed(evt);
            }
        });
        jPanel5.add(jtfEmail);

        jPanel2.add(jPanel5, java.awt.BorderLayout.NORTH);

        jPanel1.add(jPanel2, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        setSize(new java.awt.Dimension(656, 439));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jtfEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfEmailActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JButton jbAddUserRole;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbDeleteUserRole;
    private javax.swing.JButton jbExport;
    private javax.swing.JButton jbModifyUserRole;
    private javax.swing.JLabel jlAux;
    private javax.swing.JLabel jlEmail;
    private javax.swing.JLabel jlUser;
    private javax.swing.JPanel jpUserApps;
    private javax.swing.JTextField jtfEmail;
    private javax.swing.JTextField jtfUser;
    // End of variables declaration//GEN-END:variables

    @Override
    public void formClearRegistry() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void formReset() {
        moUserPane.createTable(null);
        moUserPane.clearTableRows();
        mnFormResult = SLibConstants.UNDEFINED;
    }

    @Override
    public void formRefreshCatalogues() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    public void setValue(int id, Object value) {
        this.mnUserId = id;

        try {
            initCustom();
        } catch (SQLException ex) {
            Logger.getLogger(SFormExportUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(SFormExportUser.class.getName()).log(Level.SEVERE, null, ex);
        }
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

            if (button == jbCancel) {
                setVisible(false);
            }
            else if (button == jbExport) {
                actionExport();
            }
        }
    }
}
