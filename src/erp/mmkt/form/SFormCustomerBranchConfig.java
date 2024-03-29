/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SFormCustomerBranchConfig.java
 *
 * Created on 10/11/2009
 */

package erp.mmkt.form;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormComponentItem;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.mmkt.data.SDataCustomerBranchConfig;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.AbstractAction;

/**
 *
 * @author Néstor Ávalos, Sergio Flores
 */
public class SFormCustomerBranchConfig extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener {

    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private boolean mbResetingForm;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private erp.client.SClientInterface miClient;

    private erp.mmkt.data.SDataCustomerBranchConfig moCustomerBranchConfig;
    private erp.lib.form.SFormField moFieldCustomerBranch;
    private erp.lib.form.SFormField moFieldFkSalesRouteId;
    private erp.lib.form.SFormField moFieldFkSalesAgentId_n;
    private erp.lib.form.SFormField moFieldFkSalesSupervisorId_n;

    private int mnCustomerId;

    /** Creates new form SFormCustomerBranchConfig */
    public SFormCustomerBranchConfig(erp.client.SClientInterface client) {
        super(client.getFrame(), true);
        miClient = client;
        mnFormType = SDataConstants.MKT_CFG_CUSB;

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

        jPanel1 = new javax.swing.JPanel();
        jbOK = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jlCustomerBranch = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jtfCustomerBranch = new javax.swing.JTextField();
        jlDummy = new javax.swing.JLabel();
        jlFkSalesRouteId = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jcbFkSalesRouteId = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkSalesRouteId = new javax.swing.JButton();
        jlFkSalesAgentId_n = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jcbFkSalesAgentId_n = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkSalesAgentId_n = new javax.swing.JButton();
        jlFkSalesSupervisorId_n = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jcbFkSalesSupervisorId_n = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkSalesSupervisorId_n = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Sucursal de cliente");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel1.setPreferredSize(new java.awt.Dimension(400, 33));
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbOK.setText("Aceptar");
        jbOK.setToolTipText("[Ctrl + Enter]");
        jbOK.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel1.add(jbOK);

        jbCancel.setText("Cancelar");
        jbCancel.setToolTipText("[Escape]");
        jPanel1.add(jbCancel);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new java.awt.GridLayout(5, 2, 5, 5));

        jlCustomerBranch.setForeground(java.awt.Color.blue);
        jlCustomerBranch.setText("Sucursal: *");
        jPanel4.add(jlCustomerBranch);

        jPanel7.setLayout(new java.awt.BorderLayout(5, 5));

        jtfCustomerBranch.setText("SUCURSAL");
        jtfCustomerBranch.setOpaque(false);
        jtfCustomerBranch.setPreferredSize(new java.awt.Dimension(56, 20));
        jPanel7.add(jtfCustomerBranch, java.awt.BorderLayout.CENTER);

        jlDummy.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel7.add(jlDummy, java.awt.BorderLayout.LINE_END);

        jPanel4.add(jPanel7);

        jlFkSalesRouteId.setText("Ruta de ventas de la sucursal: *");
        jPanel4.add(jlFkSalesRouteId);

        jPanel5.setLayout(new java.awt.BorderLayout(4, 4));

        jPanel5.add(jcbFkSalesRouteId, java.awt.BorderLayout.CENTER);

        jbFkSalesRouteId.setText("...");
        jbFkSalesRouteId.setToolTipText("Seleccionar ruta de ventas");
        jbFkSalesRouteId.setFocusable(false);
        jbFkSalesRouteId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel5.add(jbFkSalesRouteId, java.awt.BorderLayout.EAST);

        jPanel4.add(jPanel5);

        jlFkSalesAgentId_n.setText("Agente de ventas de la sucursal:");
        jPanel4.add(jlFkSalesAgentId_n);

        jPanel6.setLayout(new java.awt.BorderLayout(4, 4));

        jPanel6.add(jcbFkSalesAgentId_n, java.awt.BorderLayout.CENTER);

        jbFkSalesAgentId_n.setText("...");
        jbFkSalesAgentId_n.setToolTipText("Seleccionar agente de ventas");
        jbFkSalesAgentId_n.setFocusable(false);
        jbFkSalesAgentId_n.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel6.add(jbFkSalesAgentId_n, java.awt.BorderLayout.EAST);

        jPanel4.add(jPanel6);

        jlFkSalesSupervisorId_n.setText("Supervisor de ventas de la sucursal:");
        jPanel4.add(jlFkSalesSupervisorId_n);

        jPanel8.setLayout(new java.awt.BorderLayout(4, 4));

        jPanel8.add(jcbFkSalesSupervisorId_n, java.awt.BorderLayout.CENTER);

        jbFkSalesSupervisorId_n.setText("...");
        jbFkSalesSupervisorId_n.setToolTipText("Seleccionar agente de ventas");
        jbFkSalesSupervisorId_n.setFocusable(false);
        jbFkSalesSupervisorId_n.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel8.add(jbFkSalesSupervisorId_n, java.awt.BorderLayout.EAST);

        jPanel4.add(jPanel8);

        jPanel3.add(jPanel4, java.awt.BorderLayout.NORTH);

        jPanel2.add(jPanel3, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        setSize(new java.awt.Dimension(546, 235));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void initComponentsExtra() {
        mvFields = new Vector<>();

        moFieldCustomerBranch = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfCustomerBranch, jlCustomerBranch);
        moFieldFkSalesRouteId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkSalesRouteId, jlFkSalesRouteId);
        moFieldFkSalesAgentId_n = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFkSalesAgentId_n, jlFkSalesAgentId_n);
        moFieldFkSalesSupervisorId_n = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFkSalesSupervisorId_n, jlFkSalesSupervisorId_n);

        mvFields.add(moFieldCustomerBranch);
        mvFields.add(moFieldFkSalesRouteId);
        mvFields.add(moFieldFkSalesAgentId_n);

        jbOK.addActionListener(this);
        jbCancel.addActionListener(this);
        jbFkSalesRouteId.addActionListener(this);
        jbFkSalesAgentId_n.addActionListener(this);
        jbFkSalesSupervisorId_n.addActionListener(this);

        AbstractAction actionOk = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionOk(); }
        };

        SFormUtilities.putActionMap(getRootPane(), actionOk, "ok", KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK);

        AbstractAction actionCancel = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionCancel(); }
        };

        SFormUtilities.putActionMap(getRootPane(), actionCancel, "cancel", KeyEvent.VK_ESCAPE, 0);
    }

    private void windowActivated() {
        if (mbFirstTime) {
            mbFirstTime = false;
        }
    }

    private void actionOk() {
        SFormValidation validation = formValidate();

        if (validation.getIsError()) {
            if (validation.getComponent() != null) {
                validation.getComponent().requestFocus();
            }
            if (validation.getMessage().length() > 0) {
                miClient.showMsgBoxWarning(validation.getMessage());
            }
        }
        else {
            mnFormResult = SLibConstants.FORM_RESULT_OK;
            setVisible(false);
        }
    }

    private void actionCancel() {
        mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
        setVisible(false);
    }

    private void actionFkSalesRouteId() {
        miClient.pickOption(SDataConstants.MKTU_SAL_ROUTE, moFieldFkSalesRouteId, null);
    }

    private void actionFkSalesAgentId_n() {
        miClient.pickOption(SDataConstants.BPSX_BP_ATT_SAL_AGT, moFieldFkSalesAgentId_n, null);
    }

    private void actionFkSalesSupervisorId_n() {
        miClient.pickOption(SDataConstants.BPSX_BP_ATT_SAL_AGT, moFieldFkSalesSupervisorId_n, null);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbFkSalesAgentId_n;
    private javax.swing.JButton jbFkSalesRouteId;
    private javax.swing.JButton jbFkSalesSupervisorId_n;
    private javax.swing.JButton jbOK;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkSalesAgentId_n;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkSalesRouteId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkSalesSupervisorId_n;
    private javax.swing.JLabel jlCustomerBranch;
    private javax.swing.JLabel jlDummy;
    private javax.swing.JLabel jlFkSalesAgentId_n;
    private javax.swing.JLabel jlFkSalesRouteId;
    private javax.swing.JLabel jlFkSalesSupervisorId_n;
    private javax.swing.JTextField jtfCustomerBranch;
    // End of variables declaration//GEN-END:variables

    @Override
    public void formClearRegistry() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void formReset() {
        mnFormResult = SLibConstants.UNDEFINED;
        mnFormStatus = SLibConstants.UNDEFINED;
        mbFirstTime = true;
        mnCustomerId = 0;

        moCustomerBranchConfig = null;

        for (int i = 0; i < mvFields.size(); i++) {
            ((erp.lib.form.SFormField) mvFields.get(i)).resetField();
        }

        jtfCustomerBranch.setEnabled(false);
        jtfCustomerBranch.setFocusable(false);

        moFieldFkSalesRouteId.setKey(new int[] { miClient.getSessionXXX().getParamsCompany().getFkDefaultSalesRouteId_n() });
    }

    @Override
    public void formRefreshCatalogues() {
        SFormUtilities.populateComboBox(miClient, jcbFkSalesRouteId, SDataConstants.MKTU_SAL_ROUTE);
        SFormUtilities.populateComboBox(miClient, jcbFkSalesAgentId_n, SDataConstants.BPSX_BP_ATT_SAL_AGT);
        SFormUtilities.populateComboBox(miClient, jcbFkSalesSupervisorId_n, SDataConstants.BPSX_BP_ATT_SAL_AGT);
    }

    @Override
    public erp.lib.form.SFormValidation formValidate() {
        SFormValidation validation = new SFormValidation();

        for (int i = 0; i < mvFields.size(); i++) {
            if (!((erp.lib.form.SFormField) mvFields.get(i)).validateField()) {
                validation.setIsError(true);
                validation.setComponent(((erp.lib.form.SFormField) mvFields.get(i)).getComponent());
                break;
            }
        }

        if (!validation.getIsError() && moFieldFkSalesSupervisorId_n.getKeyAsIntArray()[0] != 0 && moFieldFkSalesAgentId_n.getKeyAsIntArray()[0] == 0) {
            validation.setMessage("Se debe ingresar un valor para el campo '" + jlFkSalesAgentId_n.getText() + "'.");
            validation.setComponent(jcbFkSalesAgentId_n);
        }

        if (!validation.getIsError() && moFieldFkSalesSupervisorId_n.getKeyAsIntArray()[0] != 0 && moFieldFkSalesSupervisorId_n.getKeyAsIntArray()[0] == moFieldFkSalesAgentId_n.getKeyAsIntArray()[0]) {
            validation.setMessage("Se debe ingresar un valor diferente para el campo '" + jlFkSalesAgentId_n.getText() + "'.");
            validation.setComponent(jcbFkSalesAgentId_n);
        }

        return validation;
    }

    @Override
    public void setFormStatus(int status) {
        mnFormStatus = status;
    }

    @Override
    public void setFormVisible(boolean visible) {
        setVisible(visible);
    }

    @Override
    public int getFormStatus() {
        return mnFormStatus;
    }

    @Override
    public int getFormResult() {
        return mnFormResult;
    }

    @Override
    public void setRegistry(erp.lib.data.SDataRegistry registry) {
        moCustomerBranchConfig = (SDataCustomerBranchConfig) registry;

        moFieldCustomerBranch.setFieldValue( moCustomerBranchConfig.getDbmsCustomerBranch());
        moFieldFkSalesRouteId.setKey(new int[] { moCustomerBranchConfig.getFkSalesRouteId() });
        if (jcbFkSalesAgentId_n.getItemCount() > 0) {
            moFieldFkSalesAgentId_n.setKey(new int[] { moCustomerBranchConfig.getFkSalesAgentId_n() });
        }
        if (jcbFkSalesSupervisorId_n.getItemCount() > 0) {
            moFieldFkSalesSupervisorId_n.setKey(new int[] { moCustomerBranchConfig.getFkSalesSupervisorId_n() });
        }
        jtfCustomerBranch.setEnabled(false);
        jtfCustomerBranch.setFocusable(false);
    }

    @Override
    public erp.lib.data.SDataRegistry getRegistry() {
        if (moCustomerBranchConfig == null) {
            moCustomerBranchConfig = new SDataCustomerBranchConfig();
            moCustomerBranchConfig.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
            moCustomerBranchConfig.setUserNewTs(miClient.getSessionXXX().getWorkingDate());
        }
        else {
            moCustomerBranchConfig.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
            moCustomerBranchConfig.setDbmsUserEdit(miClient.getSessionXXX().getUser().getUser());
            moCustomerBranchConfig.setUserEditTs(miClient.getSessionXXX().getWorkingDate());
        }

        moCustomerBranchConfig.setPkCustomerBranchId(moCustomerBranchConfig.getPkCustomerBranchId());
        moCustomerBranchConfig.setFkSalesRouteId(moFieldFkSalesRouteId.getKeyAsIntArray()[0]);
        moCustomerBranchConfig.setFkSalesAgentId_n(moFieldFkSalesAgentId_n.getKeyAsIntArray()[0]);
        moCustomerBranchConfig.setFkSalesSupervisorId_n(moFieldFkSalesSupervisorId_n.getKeyAsIntArray()[0]);
        moCustomerBranchConfig.setDbmsCustomerBranch(moFieldCustomerBranch.getString());
        moCustomerBranchConfig.setDbmsSalesRoute(jcbFkSalesRouteId.getSelectedItem().toString());
        if (jcbFkSalesAgentId_n.getSelectedIndex() > 0) {
            moCustomerBranchConfig.setDbmsSalesAgent(jcbFkSalesAgentId_n.getSelectedItem().toString());
        }
        else {
            moCustomerBranchConfig.setDbmsSalesAgent("");
        }

        if (jcbFkSalesSupervisorId_n.getSelectedIndex() > 0) {
            moCustomerBranchConfig.setDbmsSalesSupervisor(jcbFkSalesSupervisorId_n.getSelectedItem().toString());
        }
        else {
            moCustomerBranchConfig.setDbmsSalesSupervisor("");
        }
        
        moCustomerBranchConfig.setIsRegistryEdited(true);

        return moCustomerBranchConfig;
    }

    @Override
    public void setValue(int type, Object value) {
        mnCustomerId = SLibUtilities.parseInt(value.toString());
    }

    @Override
    public Object getValue(int type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public javax.swing.JLabel getTimeoutLabel() {
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JButton) {
            javax.swing.JButton button = (javax.swing.JButton) e.getSource();

            if (button == jbOK) {
                actionOk();
            }
            else if (button == jbCancel) {
                actionCancel();
            }
            else if (button == jbFkSalesRouteId) {
                actionFkSalesRouteId();
            }
            else if (button == jbFkSalesAgentId_n) {
                actionFkSalesAgentId_n();
            }
            else if (button == jbFkSalesSupervisorId_n) {
                actionFkSalesSupervisorId_n();
            }
        }
    }
}
