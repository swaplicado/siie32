/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mbps.form;

import erp.data.SDataConstants;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.form.SFormComponentItem;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.lib.table.STableColumnForm;
import erp.lib.table.STablePane;
import erp.mbps.data.SDataBizPartner;
import erp.mbps.data.SDataScaleBizPartner;
import erp.mbps.data.SDataScaleBizPartnerMap;
import erp.mbps.data.SDataScaleBizPartnerMapRow;
import erp.mbps.data.SDataScaleBizPartnerRow;
import erp.mcfg.data.SDataScale;
import erp.mcfg.data.SDataScaleUtils;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Isabel Servín
 */
public class SFormScaleBizPartner extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener, ListSelectionListener, ItemListener {

    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private int mnFormSubtype;
    private boolean mbFirstTime;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private erp.lib.form.SFormField moFieldFkBizPartnerId;
    private erp.client.SClientInterface miClient;
    
    private SDataScale moScale;

    private ArrayList<SDataScaleBizPartnerRow> maScaleBizPartnerRows;
    //El HashMap principal representa la empresa de la báscula y el arrayList dentro representa los bizPartner mapeados a esa empresa
    private HashMap<int[], ArrayList<SDataScaleBizPartnerMapRow>> maHashOfScaleBizPartnerMapRows; 
    
    private erp.lib.table.STablePane moScaleBizPartnerPane;
    private erp.lib.table.STablePane moScaleBizPartnerMapPane;

    private javax.swing.JComboBox<SFormComponentItem> jcbFkBizPartnerId;
    
    private final String msSqlTpMap;
    
    /** Creates new form SFormScaleItem
     * @param client
     * @param subType */
    public SFormScaleBizPartner(erp.client.SClientInterface client, int subType) {
        super(client.getFrame(), true);
        miClient = client;
        mnFormType = SDataConstants.BPSU_SCA_BP;
        mnFormSubtype = subType;
        this.setTitle(mnFormSubtype == SDataConstants.MOD_SAL ? "Mapeo de clientes de báscula" : "Mapeo de proveedores de báscula");
        msSqlTpMap = "tp_map = " + (mnFormSubtype == SDataConstants.MOD_SAL ? 2 : 1);
        
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jpData = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jpControls = new javax.swing.JPanel();
        jlScale = new javax.swing.JLabel();
        jtfScale = new javax.swing.JTextField();
        jbFkBizPartnerId = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jrbSup = new javax.swing.JRadioButton();
        jrbCus = new javax.swing.JRadioButton();
        jrbAll = new javax.swing.JRadioButton();
        jbImport = new javax.swing.JButton();
        jpScaleBizPartner = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jpScaleBizPartnerMap = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jbDefault = new javax.swing.JButton();
        jbDelete = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel = new javax.swing.JPanel();
        jbOk = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jpData.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jpData.setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        jpControls.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jlScale.setText("Báscula:");
        jlScale.setPreferredSize(new java.awt.Dimension(45, 23));
        jpControls.add(jlScale);

        jtfScale.setEditable(false);
        jtfScale.setPreferredSize(new java.awt.Dimension(195, 23));
        jpControls.add(jtfScale);

        jbFkBizPartnerId.setText("Agregar asociado negocios");
        jbFkBizPartnerId.setToolTipText("Agregar asociado de negocio a la empresa de báscula");
        jbFkBizPartnerId.setFocusable(false);
        jbFkBizPartnerId.setPreferredSize(new java.awt.Dimension(190, 23));
        jpControls.add(jbFkBizPartnerId);

        jLabel1.setText("Mostrar:");
        jLabel1.setPreferredSize(new java.awt.Dimension(50, 23));
        jpControls.add(jLabel1);

        buttonGroup1.add(jrbSup);
        jrbSup.setSelected(true);
        jrbSup.setText("Proveedores");
        jrbSup.setPreferredSize(new java.awt.Dimension(90, 25));
        jpControls.add(jrbSup);

        buttonGroup1.add(jrbCus);
        jrbCus.setText("Clientes");
        jrbCus.setPreferredSize(new java.awt.Dimension(75, 25));
        jpControls.add(jrbCus);

        buttonGroup1.add(jrbAll);
        jrbAll.setText("Todas");
        jrbAll.setPreferredSize(new java.awt.Dimension(65, 25));
        jpControls.add(jrbAll);

        jbImport.setText("Importar empresas báscula");
        jbImport.setPreferredSize(new java.awt.Dimension(185, 23));
        jpControls.add(jbImport);

        jPanel1.add(jpControls, java.awt.BorderLayout.NORTH);

        jpScaleBizPartner.setPreferredSize(new java.awt.Dimension(300, 250));
        jpScaleBizPartner.setLayout(new java.awt.BorderLayout());
        jPanel1.add(jpScaleBizPartner, java.awt.BorderLayout.CENTER);

        jpData.add(jPanel1, java.awt.BorderLayout.NORTH);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jpScaleBizPartnerMap.setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 1, 0));

        jbDefault.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_ok.gif"))); // NOI18N
        jbDefault.setToolTipText("Fijar predeterminado");
        jbDefault.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel3.add(jbDefault);

        jbDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_delete.gif"))); // NOI18N
        jbDelete.setToolTipText("Eliminar");
        jbDelete.setFocusable(false);
        jbDelete.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel3.add(jbDelete);

        jpScaleBizPartnerMap.add(jPanel3, java.awt.BorderLayout.NORTH);

        jPanel2.add(jpScaleBizPartnerMap, java.awt.BorderLayout.CENTER);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jLabel2.setText("Asociados de negocios mapeados a la empresa de básula:");
        jLabel2.setPreferredSize(new java.awt.Dimension(500, 23));
        jPanel4.add(jLabel2);

        jPanel2.add(jPanel4, java.awt.BorderLayout.NORTH);

        jpData.add(jPanel2, java.awt.BorderLayout.CENTER);

        getContentPane().add(jpData, java.awt.BorderLayout.CENTER);

        jPanel.setPreferredSize(new java.awt.Dimension(792, 33));
        jPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbOk.setText("Aceptar");
        jbOk.setToolTipText("[Ctrl + Enter]");
        jbOk.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel.add(jbOk);

        jbCancel.setText("Cancelar");
        jbCancel.setToolTipText("[Escape]");
        jPanel.add(jbCancel);

        getContentPane().add(jPanel, java.awt.BorderLayout.SOUTH);

        setSize(new java.awt.Dimension(976, 638));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        actionCancel();
    }//GEN-LAST:event_formWindowClosing

    private void initComponentsExtra() {
        int i;

        jcbFkBizPartnerId = new javax.swing.JComboBox<>();
        
        moFieldFkBizPartnerId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkBizPartnerId, new JLabel("Asoc. de negocios"));
        moFieldFkBizPartnerId.setPickerButton(jbFkBizPartnerId);
        
        moScaleBizPartnerPane = new STablePane(miClient);
        jpScaleBizPartner.add(moScaleBizPartnerPane, BorderLayout.CENTER);
        
        moScaleBizPartnerMapPane = new STablePane(miClient);
        jpScaleBizPartnerMap.add(moScaleBizPartnerMapPane, BorderLayout.CENTER);

        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);

        // Tabla con los elementos de la báscula
        
        erp.lib.table.STableColumnForm tableColumnsScaleBizPartner[];
        
        i = 0;
        tableColumnsScaleBizPartner = new STableColumnForm[3];
        tableColumnsScaleBizPartner[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Empresa báscula", 400);
        tableColumnsScaleBizPartner[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_INTEGER, "Cantidad mapeada", 100);
        tableColumnsScaleBizPartner[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Asociado negocios predeterminado", 400);
        
        for (i = 0; i < tableColumnsScaleBizPartner.length; i++) {
            moScaleBizPartnerPane.addTableColumn(tableColumnsScaleBizPartner[i]);
        }

        moScaleBizPartnerPane.createTable(this);
        moScaleBizPartnerPane.getTable().getTableHeader().setReorderingAllowed(false);
        
        // Tabla con los elementos vinculados al ítem de la báscula
        
        erp.lib.table.STableColumnForm tableColumnsScaleBizPartnerMap[];

        i = 0;
        tableColumnsScaleBizPartnerMap = new STableColumnForm[2];
        tableColumnsScaleBizPartnerMap[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Asociado negocios", 400);
        tableColumnsScaleBizPartnerMap[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_BOOLEAN, "Predeterminado", 100);
        
        for (i = 0; i < tableColumnsScaleBizPartnerMap.length; i++) {
            moScaleBizPartnerMapPane.addTableColumn(tableColumnsScaleBizPartnerMap[i]);
        }

        moScaleBizPartnerMapPane.createTable(null);
        moScaleBizPartnerMapPane.getTable().getTableHeader().setReorderingAllowed(false);
        
        jbDefault.addActionListener(this);
        jbFkBizPartnerId.addActionListener(this);
        jbImport.addActionListener(this);
        jbDelete.addActionListener(this);
        jrbSup.addItemListener(this);
        jrbCus.addItemListener(this);
        jrbAll.addItemListener(this);

        AbstractAction actionOk = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionOk(); }
        };

        SFormUtilities.putActionMap(getRootPane(), actionOk, "ok", KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK);

        AbstractAction action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionCancel(); }
        };

        SFormUtilities.putActionMap(getRootPane(), action, "cancel", KeyEvent.VK_ESCAPE, 0);
    }

    private void windowActivated() {
        if (mbFirstTime) {
            mbFirstTime = false;
            //moPanelBizPartnerBranch.requestFocusFirstTime();
        }
    }
    
    private void populateScaleBizPartnerTable() {
        maScaleBizPartnerRows = new ArrayList<>();
        
        try {
            String sql = "SELECT sp.id_sca, sp.id_sca_bp, sp.name bp, sp.b_sup, sp.b_cus, " +
                    "(SELECT COUNT(*) FROM erp.bpsu_sca_bp_map WHERE id_sca = sp.id_sca AND id_sca_bp = sp.id_sca_bp AND " + msSqlTpMap + ") cant, bp.bp def " +
                    "FROM erp.bpsu_sca_bp AS sp " +
                    "LEFT JOIN erp.bpsu_sca_bp_map AS spm ON sp.id_sca = spm.id_sca AND sp.id_sca_bp = spm.id_sca_bp AND spm.b_def AND " + msSqlTpMap + " " +
                    "LEFT JOIN erp.bpsu_bp AS bp ON spm.id_bp = bp.id_bp " +
                    "WHERE sp.id_sca = " + moScale.getPkScaleId() + " AND NOT sp.b_del ORDER BY sp.name";
            ResultSet resultSet = miClient.getSession().getStatement().executeQuery(sql);
            
            while (resultSet.next()) {
                SDataScaleBizPartnerRow row = new SDataScaleBizPartnerRow(
                        new int[] { resultSet.getInt("id_sca"), resultSet.getInt("id_sca_bp") }, 
                        resultSet.getString("bp"), resultSet.getInt("cant"), resultSet.getString("def"), 
                        resultSet.getBoolean("b_sup"), resultSet.getBoolean("b_cus"));
                maScaleBizPartnerRows.add(row);
                
                if (resultSet.getInt("cant") > 0) {
                    createScaleBizPartnerMapRow(resultSet.getInt("id_sca"), resultSet.getInt("id_sca_bp"));
                }
            }
            renderBizPartnerTable();
        }
        catch (Exception e) { }
    }
    
    private void renderBizPartnerTable() {
        moScaleBizPartnerPane.clearTableRows();
        if (maScaleBizPartnerRows != null && !maScaleBizPartnerRows.isEmpty()) {
            for (SDataScaleBizPartnerRow row : maScaleBizPartnerRows) {
                if (jrbSup.isSelected() && row.mbSup) {
                    moScaleBizPartnerPane.addTableRow(row);
                }
                else if (jrbCus.isSelected() && row.mbCus) {
                    moScaleBizPartnerPane.addTableRow(row);
                }
                else if (jrbAll.isSelected()) {
                    moScaleBizPartnerPane.addTableRow(row);
                }
            }
        }
        
        moScaleBizPartnerPane.renderTableRows();
        moScaleBizPartnerPane.setTableRowSelection(0);
    }
    
    private void populateScaleBizPartnerMapTable(SDataScaleBizPartnerRow scaRow) {
        try {
            boolean found = false;
            for (ArrayList<SDataScaleBizPartnerMapRow> arrMap : maHashOfScaleBizPartnerMapRows.values()) {
                for (SDataScaleBizPartnerMapRow row : arrMap) {
                    if (row.moScaId[0] == scaRow.moId[0] && row.moScaId[1] == scaRow.moId[1]) {
                        found = true;
                        renderBizPartnerMapTable(arrMap);
                        break;
                    }
                }
            }
            
            if (!found) {
                ArrayList<SDataScaleBizPartnerMapRow> arr = createScaleBizPartnerMapRow(scaRow.moId[0], scaRow.moId[1]);
                renderBizPartnerMapTable(arr);
            }
        }
        catch (Exception e) { }
    }
    
    private ArrayList<SDataScaleBizPartnerMapRow> createScaleBizPartnerMapRow(int idSca, int idScaBp) throws Exception {
        ArrayList<SDataScaleBizPartnerMapRow> arr = new ArrayList<>();
        String sql = "SELECT spm.id_sca, spm.id_sca_bp, spm.id_bp, spm.b_def, bp.bp FROM erp.bpsu_sca_bp_map AS spm " +
                "INNER JOIN erp.bpsu_bp AS bp ON spm.id_bp = bp.id_bp " +
                "WHERE spm.id_sca = " + idSca + " AND spm.id_sca_bp = '" + idScaBp + "' ORDER BY bp.bp, b_def;";
        ResultSet resultSet = miClient.getSession().getDatabase().getConnection().createStatement().executeQuery(sql);

        while (resultSet.next()) {
            SDataScaleBizPartnerMapRow row = new SDataScaleBizPartnerMapRow(
                    new int[] { resultSet.getInt("id_sca"), resultSet.getInt("id_sca_bp") }, 
                    resultSet.getInt("id_bp"), resultSet.getString("bp"), resultSet.getBoolean("b_def"), false);
            row.prepareTableRow();
            arr.add(row);
        }
        if (!arr.isEmpty()) {
            maHashOfScaleBizPartnerMapRows.put(arr.get(0).moScaId, arr);
        }
        return arr;
    }
    
    private void renderBizPartnerMapTable(ArrayList<SDataScaleBizPartnerMapRow> arr) {
        moScaleBizPartnerMapPane.clearTableRows();
        if (!arr.isEmpty()) {
            arr.stream().forEach((row) -> {
                moScaleBizPartnerMapPane.addTableRow(row);
            });
        }    
        moScaleBizPartnerMapPane.renderTableRows();
        moScaleBizPartnerMapPane.setTableRowSelection(0);
    }

    private void actionOk() {
        mnFormResult = SLibConstants.FORM_RESULT_OK;
        setVisible(false);
    }

    private void actionCancel() {
        if (miClient.showMsgBoxConfirm("¿Está seguro de cerrar sin guardar?") == JOptionPane.OK_OPTION) {
            mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
            setVisible(false);
        }
    }
    
    private void actionFkBizPartnerId() {
        if (miClient.pickOption(mnFormSubtype == SDataConstants.MOD_SAL ? SDataConstants.BPSX_BP_CUS : SDataConstants.BPSX_BP_SUP, moFieldFkBizPartnerId, null) == SLibConstants.FORM_RESULT_OK) {
            SDataBizPartner bp = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, moFieldFkBizPartnerId.getKeyAsIntArray(), SLibConstants.EXEC_MODE_SILENT);
            if (bp != null) {
                boolean found = false;
                boolean foundBp = false;
                int selectedRow = moScaleBizPartnerPane.getTable().getSelectedRow();
                int[] pkScaleBp = ((SDataScaleBizPartnerRow) moScaleBizPartnerPane.getSelectedTableRow()).moId;
                for (ArrayList<SDataScaleBizPartnerMapRow> arrRow : maHashOfScaleBizPartnerMapRows.values()) {
                    int i = 0;
                    for (SDataScaleBizPartnerMapRow row : arrRow) {
                        if (pkScaleBp[0] == row.moScaId[0] && pkScaleBp[1] == row.moScaId[1]) {
                            if (bp.getPkBizPartnerId() == row.mnBizPartnerId) {
                                foundBp = true;
                            }
                            found = true;
                            i++;
                        }
                    }
                    if (i > 0 && !foundBp) {
                        SDataScaleBizPartnerMapRow newRow = new SDataScaleBizPartnerMapRow(pkScaleBp, bp.getPkBizPartnerId(), bp.getBizPartner(), false, true);
                        newRow.prepareTableRow();
                        arrRow.add(newRow);
                        renderBizPartnerMapTable(arrRow);
                        ((SDataScaleBizPartnerRow)moScaleBizPartnerPane.getSelectedTableRow()).mnCant = i + 1;
                        ((SDataScaleBizPartnerRow)moScaleBizPartnerPane.getSelectedTableRow()).prepareTableRow();
                        break;
                    }
                }
                if (!found) {
                    ArrayList<SDataScaleBizPartnerMapRow> newArrRow = new ArrayList<>();
                    SDataScaleBizPartnerMapRow newRow = new SDataScaleBizPartnerMapRow(pkScaleBp, bp.getPkBizPartnerId(), bp.getBizPartner(), true, true);
                    newRow.prepareTableRow();
                    newArrRow.add(newRow);
                    renderBizPartnerMapTable(newArrRow);
                    maHashOfScaleBizPartnerMapRows.put(newArrRow.get(0).moScaId, newArrRow);
                    ((SDataScaleBizPartnerRow)moScaleBizPartnerPane.getSelectedTableRow()).mnCant = 1;
                    ((SDataScaleBizPartnerRow)moScaleBizPartnerPane.getSelectedTableRow()).msDefault = bp.getBizPartner();
                    ((SDataScaleBizPartnerRow)moScaleBizPartnerPane.getSelectedTableRow()).prepareTableRow();
                }
                moScaleBizPartnerPane.renderTableRows();
                moScaleBizPartnerPane.setTableRowSelection(selectedRow);
            }
        }
    }
    
    private void actionDefault() {
        int selectedRow = moScaleBizPartnerPane.getTable().getSelectedRow();
        int[] pk = ((SDataScaleBizPartnerMapRow) moScaleBizPartnerMapPane.getSelectedTableRow()).moScaId;
        int item = ((SDataScaleBizPartnerMapRow) moScaleBizPartnerMapPane.getSelectedTableRow()).mnBizPartnerId;
        for (ArrayList<SDataScaleBizPartnerMapRow> arr : maHashOfScaleBizPartnerMapRows.values()) {
            boolean found = false;
            for (SDataScaleBizPartnerMapRow row : arr)  {
                if (row.moScaId[0] == pk[0] && row.moScaId[1] == pk[1]) {
                    row.mbDefault = row.mnBizPartnerId == item;
                    row.mbIsEdit = true;
                    if (row.mnBizPartnerId == item) {
                        ((SDataScaleBizPartnerRow)moScaleBizPartnerPane.getSelectedTableRow()).msDefault = row.msName;
                        ((SDataScaleBizPartnerRow)moScaleBizPartnerPane.getSelectedTableRow()).prepareTableRow();
                    }
                    row.prepareTableRow();
                    found = true;
                }
            }
            if (found) {
                renderBizPartnerMapTable(arr);
                break;
            }
        }
        moScaleBizPartnerPane.renderTableRows();
        moScaleBizPartnerPane.setTableRowSelection(selectedRow);
    }
    
    private void actionDelete() {
        int index = moScaleBizPartnerMapPane.getTable().getSelectedRow();
        int selectedRow = moScaleBizPartnerPane.getTable().getSelectedRow();
        
        if (index != -1) {
            int[] pk = ((SDataScaleBizPartnerMapRow) moScaleBizPartnerMapPane.getSelectedTableRow()).moScaId;
            int item = ((SDataScaleBizPartnerMapRow) moScaleBizPartnerMapPane.getSelectedTableRow()).mnBizPartnerId;
            for (ArrayList<SDataScaleBizPartnerMapRow> arr : maHashOfScaleBizPartnerMapRows.values()) {
                for (SDataScaleBizPartnerMapRow row : arr)  {
                    if (row.moScaId[0] == pk[0] && row.moScaId[1] == pk[1]) {
                        if (row.mnBizPartnerId == item) {
                            if (row.mbDefault) {
                                miClient.showMsgBoxWarning("No se puede eliminar el renglón debido a que es el predeterminado, seleccione otro antes de eliminar.");
                                break;
                            }
                            else {
                                arr.remove(row);
                                ((SDataScaleBizPartnerRow)moScaleBizPartnerPane.getSelectedTableRow()).mnCant --;
                                ((SDataScaleBizPartnerRow)moScaleBizPartnerPane.getSelectedTableRow()).prepareTableRow();
                                renderBizPartnerMapTable(arr);
                                break;
                            }
                        }
                    }
                }
            }
        }
        moScaleBizPartnerPane.renderTableRows();
        moScaleBizPartnerPane.setTableRowSelection(selectedRow);
    }
    
    private void actionImport() {
        if (miClient.showMsgBoxConfirm("Se guardarán los cambios realizados hasta el momento.\n¿Desea continuar?") == JOptionPane.OK_OPTION) {
            saveConf();
            Connection connection = SDataScaleUtils.getScaleConnection(miClient.getSession(), moScale.getPkScaleId());

            try {
                ArrayList<SDataScaleBizPartner> aux = new ArrayList<>();
                String sql = "SELECT DISTINCT emp.Emp_ID, emp.Emp_Nombre, "
                        + "(IF pes.Pes_PesoPri > pes.Pes_PesoSeg THEN 1 ELSE 2 END IF) AS type "
                        + "FROM dba.Empresas AS emp "
                        + "INNER JOIN dba.Pesadas AS pes ON emp.Emp_ID = pes.Emp_ID "
                        + "WHERE pes.Usb_ID = 'ACTH' AND pes.Pes_Completo = 1 "
                        + "ORDER BY emp.Emp_Nombre ";

                ResultSet resultSet = connection.createStatement().executeQuery(sql);

                while (resultSet.next()) {
                    SDataScaleBizPartner scaleBizPartner = new SDataScaleBizPartner();
                    scaleBizPartner.setPkScaleId(moScale.getPkScaleId());
                    scaleBizPartner.setCode(resultSet.getString("Emp_ID"));
                    scaleBizPartner.setName(resultSet.getString("Emp_Nombre"));
                    scaleBizPartner.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
                    scaleBizPartner.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
                    switch (resultSet.getString("type")) {
                        case "1" : scaleBizPartner.setIsSupplier(true); break;
                        case "2" : scaleBizPartner.setIsCustomer(true); break;
                    }
                    scaleBizPartner.save(miClient.getSession().getDatabase().getConnection());
                    aux.add(scaleBizPartner);
                }
                
                sql = "SELECT Emp_ID, Emp_Nombre FROM dba.Empresas ";
                resultSet = connection.createStatement().executeQuery(sql);
                while (resultSet.next()) {
                    for (SDataScaleBizPartner scaleBizPartner : aux) {
                        if (scaleBizPartner.getCode().equals(resultSet.getString(1))) {
                            scaleBizPartner.setAuxInScale(true);
                        }
                    }
                }
                
                for (SDataScaleBizPartner scaleBizPartner : aux) {
                    scaleBizPartner.setFkUserDeleteId(miClient.getSession().getUser().getPkUserId());
                    scaleBizPartner.saveInScale(miClient.getSession().getDatabase().getConnection());
                }
                
                populateScaleBizPartnerTable();
            }
            catch (Exception e) {
                miClient.showMsgBoxInformation(e.getMessage());
            }
        }
    }
    
    private void saveConf() {
        try {
            //Primer barrido: eliminación.
            String sql = "SELECT * FROM erp.bpsu_sca_bp_map WHERE id_sca = " + moScale.getPkScaleId() + " AND " + msSqlTpMap + " ORDER BY id_sca_bp";
            
            try (ResultSet resultSet = miClient.getSession().getStatement().executeQuery(sql)) {
                while (resultSet.next()) {
                    boolean found = false;
                    for (ArrayList<SDataScaleBizPartnerMapRow> arr : maHashOfScaleBizPartnerMapRows.values()) {
                        for (SDataScaleBizPartnerMapRow row : arr)  {
                            if (row.moScaId[0] == resultSet.getInt("id_sca") && row.moScaId[1] == resultSet.getInt("id_sca_bp") &&
                                    row.mnBizPartnerId == resultSet.getInt("id_bp")) {
                                found = true;
                            }
                        }
                    }
                    if (!found) {
                        sql = "DELETE FROM erp.bpsu_sca_bp_map "
                                + "WHERE id_sca = " + resultSet.getInt("id_sca") + " "
                                + "AND id_sca_bp = " + resultSet.getInt("id_sca_bp") + " "
                                + "AND id_bp = " + resultSet.getInt("id_bp");
                        miClient.getSession().getDatabase().getConnection().createStatement().execute(sql);
                    }                  
                }
            }
            
            // Segundo barrido: actualización e inserción
            for (ArrayList<SDataScaleBizPartnerMapRow> arr : maHashOfScaleBizPartnerMapRows.values()) {
                for (SDataScaleBizPartnerMapRow row : arr)  {
                    SDataScaleBizPartnerMap bpMap = new SDataScaleBizPartnerMap();
                    if (row.mbIsEdit) {
                        bpMap.read(new int[] {row.moScaId[0], row.moScaId[1], row.mnBizPartnerId}, miClient.getSession().getStatement());
                        bpMap.setIsDefault(row.mbDefault);
                        bpMap.setFkUserId(miClient.getSession().getUser().getPkUserId());
                        bpMap.save(miClient.getSession().getDatabase().getConnection());
                    }
                    else if (row.mbIsNew) {
                        bpMap.setPkScaleId(moScale.getPkScaleId());
                        bpMap.setPkScaleBizPartnerId(row.moScaId[1]);
                        bpMap.setPkBizPartnerId(row.mnBizPartnerId);
                        bpMap.setTypeMap(mnFormSubtype == SDataConstants.MOD_SAL ? 2 : 1);
                        bpMap.setIsDefault(row.mbDefault);
                        bpMap.setFkUserId(miClient.getSession().getUser().getPkUserId());
                        bpMap.save(miClient.getSession().getDatabase().getConnection());
                    }
                }
            }
        }
        catch (Exception e) {}
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbDefault;
    private javax.swing.JButton jbDelete;
    private javax.swing.JButton jbFkBizPartnerId;
    private javax.swing.JButton jbImport;
    private javax.swing.JButton jbOk;
    private javax.swing.JLabel jlScale;
    private javax.swing.JPanel jpControls;
    private javax.swing.JPanel jpData;
    private javax.swing.JPanel jpScaleBizPartner;
    private javax.swing.JPanel jpScaleBizPartnerMap;
    private javax.swing.JRadioButton jrbAll;
    private javax.swing.JRadioButton jrbCus;
    private javax.swing.JRadioButton jrbSup;
    private javax.swing.JTextField jtfScale;
    // End of variables declaration//GEN-END:variables

    @Override
    public void formClearRegistry() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void formReset() {
        mbFirstTime = true;
        maHashOfScaleBizPartnerMapRows = new HashMap<>();
        moScaleBizPartnerPane.clearTableRows();
        moScaleBizPartnerMapPane.clearTableRows();
        SFormUtilities.populateComboBox(miClient, jcbFkBizPartnerId, SDataConstants.BPSU_BP);
        jrbCus.setSelected(mnFormSubtype == SDataConstants.MOD_SAL);
        
//        moPanelBizPartnerBranch.formReset();
//        moPanelBizPartnerBranch.setParamBizPartnerDescription(msParamBizPartnerDescription);
    }

    @Override
    public void formRefreshCatalogues() {
//        moPanelBizPartnerBranch.formRefreshCatalogues();
    }

    @Override
    public erp.lib.form.SFormValidation formValidate() {
        return null;
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
        moScale = (SDataScale) registry;
        
        jtfScale.setText(moScale.getScale());
        // leer los items de la báscula
        populateScaleBizPartnerTable();
    }

    @Override
    public erp.lib.data.SDataRegistry getRegistry() {
        if (mnFormResult == SLibConstants.FORM_RESULT_OK) {
            saveConf();
        }
        return moScale;
    }

    @Override
    public void setValue(int type, java.lang.Object value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public java.lang.Object getValue(int type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public javax.swing.JLabel getTimeoutLabel() {
        return null;
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JButton) {
            javax.swing.JButton button = (javax.swing.JButton) e.getSource();

            if (button == jbOk) {
                actionOk();
            }
            else if (button == jbCancel) {
                actionCancel();
            }
            else if (button == jbFkBizPartnerId) {
                actionFkBizPartnerId();
            }
            else if (button == jbDefault) {
                actionDefault();
            }
            else if (button == jbDelete) {
                actionDelete();
            }
            else if (button == jbImport) {
                actionImport();
            }
        }
    }
    
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            if (moScaleBizPartnerPane.getSelectedTableRow() != null) {
                populateScaleBizPartnerMapTable((SDataScaleBizPartnerRow) moScaleBizPartnerPane.getSelectedTableRow());
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        renderBizPartnerTable();
    }
}
