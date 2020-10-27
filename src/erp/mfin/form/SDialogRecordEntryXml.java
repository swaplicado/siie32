/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mfin.form;

import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.table.STableColumnForm;
import erp.lib.table.STablePane;
import erp.mfin.data.SDataCfdRecordRow;
import erp.mtrn.data.SCfdUtils;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Irving Sánchez
 */
public class SDialogRecordEntryXml extends javax.swing.JDialog implements ActionListener {

    private int mnFormResult;
    private erp.client.SClientInterface miClient;
    private ArrayList<SDataCfdRecordRow> maRows;
    private erp.lib.table.STablePane moPaneCfdRecordRows;
    
    /**
     * Creates new form SDialogRecordEntryDps
     */
    public SDialogRecordEntryXml(erp.client.SClientInterface client, ArrayList<SDataCfdRecordRow> cfdRows) {
        super(client.getFrame(), true);
        miClient = client;
        maRows = (ArrayList<SDataCfdRecordRow>) cfdRows.clone();
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

        jPanel11 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jbOk = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();
        jpPanel33 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jbRowAdd = new javax.swing.JButton();
        jbRowRemove = new javax.swing.JButton();
        jpGrid = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Agregar archivos XML");
        setModal(true);
        setPreferredSize(new java.awt.Dimension(400, 250));
        setResizable(false);

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder("Documentos XML de la partida"));
        jPanel11.setLayout(new java.awt.BorderLayout());

        jPanel2.setPreferredSize(new java.awt.Dimension(468, 33));
        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbOk.setText("Aceptar");
        jbOk.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel2.add(jbOk);

        jbCancel.setText("Cancelar");
        jPanel2.add(jbCancel);

        jPanel11.add(jPanel2, java.awt.BorderLayout.PAGE_END);

        jpPanel33.setLayout(new java.awt.BorderLayout());

        jPanel4.setPreferredSize(new java.awt.Dimension(110, 33));
        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jbRowAdd.setText("Agregar XML");
        jbRowAdd.setToolTipText("Seleccionar archivo XML...");
        jbRowAdd.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel4.add(jbRowAdd);

        jbRowRemove.setText("Eliminar XML");
        jbRowRemove.setToolTipText("Eliminar archivo XML");
        jbRowRemove.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel4.add(jbRowRemove);

        jpPanel33.add(jPanel4, java.awt.BorderLayout.NORTH);

        jpGrid.setLayout(new java.awt.BorderLayout());
        jpPanel33.add(jpGrid, java.awt.BorderLayout.CENTER);

        jPanel11.add(jpPanel33, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel11, java.awt.BorderLayout.CENTER);

        setSize(new java.awt.Dimension(416, 289));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void initComponentsExtra () {
        int i = 0;
        STableColumnForm tableColumnsEntry[];
        
        moPaneCfdRecordRows = new STablePane(miClient);
        jpGrid.add(moPaneCfdRecordRows, BorderLayout.CENTER);

        tableColumnsEntry = new STableColumnForm[1];
        tableColumnsEntry[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "XML", 250);

        for (i = 0; i < tableColumnsEntry.length; i++) {
            moPaneCfdRecordRows.addTableColumn(tableColumnsEntry[i]);
        }
        moPaneCfdRecordRows.createTable();
        
        // Form listeners:

        jbRowAdd.addActionListener(this);
        jbRowRemove.addActionListener(this);
        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);
        
        populateGridRows();
    }
    
    private void actionRowAdd () {
        FileFilter filter = new FileNameExtensionFilter("XML file", "xml");
        
        miClient.getFileChooser().repaint();
        miClient.getFileChooser().setAcceptAllFileFilterUsed(false);
        miClient.getFileChooser().setFileFilter(filter);

        try {
            if (miClient.getFileChooser().showOpenDialog(miClient.getFrame()) == JFileChooser.APPROVE_OPTION) {
                if (SCfdUtils.validateCfdiReceptor(miClient, miClient.getFileChooser().getSelectedFile().getAbsolutePath())) {
                    maRows.add(new SDataCfdRecordRow(maRows.size() + 1, 0, miClient.getFileChooser().getSelectedFile().getName(), miClient.getFileChooser().getSelectedFile().getAbsolutePath()));
                    populateGridRows();
                }
            }
            miClient.getFileChooser().resetChoosableFileFilters();
            miClient.getFileChooser().setAcceptAllFileFilterUsed(true);
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
    }
    
    private void actionRowRemove () {
        int index = 0;
        SDataCfdRecordRow recordRow = null;
        ArrayList<SDataCfdRecordRow> deleteRows = null;
        
        index = moPaneCfdRecordRows.getTable().getSelectedRow();
        deleteRows = new ArrayList<SDataCfdRecordRow>();
        if (index != -1) {
            if (miClient.showMsgBoxConfirm(SLibConstants.MSG_CNF_REG_DELETE) == JOptionPane.YES_OPTION) {
                recordRow = (SDataCfdRecordRow) moPaneCfdRecordRows.getTableRow(index);
                
                for (SDataCfdRecordRow row : maRows) {
                    if (row.getMoveId() == recordRow.getMoveId()) {
                        if (row.getCfdId() == SLibConstants.UNDEFINED) {
                            deleteRows.add(row);
                        }
                        else {
                            row.setNameXml("");
                            row.setPathXml("");
                        }
                        break;
                    }
                }
                for (SDataCfdRecordRow row : deleteRows) {
                    maRows.remove(row);
                }
                
                populateGridRows();
            }
        }
    }
    
    private void actionOk() {
        mnFormResult = SLibConstants.FORM_RESULT_OK;
        setVisible(false);
    }
    
    private void actionCancel() {
        mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
        setVisible(false);
    }
    
    private void populateGridRows () {
        moPaneCfdRecordRows.clearTableRows();
        for (SDataCfdRecordRow row : maRows) {
            if (!row.getNameXml().isEmpty()) {
                moPaneCfdRecordRows.addTableRow(row);
            }
        }
        moPaneCfdRecordRows.renderTableRows();
        moPaneCfdRecordRows.setTableRowSelection(0);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbOk;
    private javax.swing.JButton jbRowAdd;
    private javax.swing.JButton jbRowRemove;
    private javax.swing.JPanel jpGrid;
    private javax.swing.JPanel jpPanel33;
    // End of variables declaration//GEN-END:variables

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (JButton) e.getSource();
            if (button == jbRowAdd) {
                actionRowAdd();
            } 
            else if (button == jbRowRemove) {
                actionRowRemove ();
            } 
            else if (button == jbOk) {
                actionOk();
            }  
            else if (button == jbCancel) {
                actionCancel();
            }  
         }
    }
    
    public int getFormResult() {
        return mnFormResult;
    }
    
    public ArrayList<SDataCfdRecordRow> getGridRows() {
        return maRows;
    }
}
