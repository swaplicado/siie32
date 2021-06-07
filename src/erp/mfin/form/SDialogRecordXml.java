/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mfin.form;

import erp.data.SDataConstants;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormUtilities;
import erp.lib.table.STableColumnForm;
import erp.lib.table.STableConstants;
import erp.lib.table.STablePane;
import erp.mfin.data.SDataCfdRecordRow;
import erp.mfin.data.SDataRecordEntry;
import erp.mfin.data.SDataRecordEntryRow;
import erp.mtrn.data.SDataCfd;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Isabel Servín
 */
public class SDialogRecordXml extends javax.swing.JDialog implements ActionListener, ListSelectionListener {

    private int mnFormResult;
    private final erp.client.SClientInterface miClient;
    private final ArrayList<SDataCfdRecordRow> maXmlRecordRows;
    private final Vector<SDataRecordEntry> maRecordEntries;
    private ArrayList<SDataCfdRecordRow> maXmlRecordEntryRows;
    private erp.lib.table.STablePane moPaneXmlRecordRows;
    private erp.lib.table.STablePane moPaneRecordEntryRows;
    private erp.lib.table.STablePane moPaneXmlRecordEntryRows;
    
    /**
     * Creates new form SDialogRecordXml
     * @param client
     * @param entries
     * @param cfdRows
     */
    public SDialogRecordXml(erp.client.SClientInterface client, Vector<SDataRecordEntry> entries, ArrayList<SDataCfdRecordRow> cfdRows) {
        super(client.getFrame(), true);
        miClient = client;
        maXmlRecordRows = (ArrayList<SDataCfdRecordRow>) cfdRows.clone();
        maRecordEntries = entries;
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

        jpContols = new javax.swing.JPanel();
        jpGridXmlRecord = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jpGridRecordEntryRow = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jbAdd = new javax.swing.JButton();
        jbAddAll = new javax.swing.JButton();
        jpGridXmlRecordEntryRow = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jbRemove = new javax.swing.JButton();
        jbRemoveAll = new javax.swing.JButton();
        jbRemoveAllAll = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jbOk = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Agregar archivos XML a un renglón");
        setModal(true);
        setPreferredSize(new java.awt.Dimension(400, 250));

        jpContols.setBorder(javax.swing.BorderFactory.createTitledBorder("CFDI de la póliza contable"));
        jpContols.setLayout(new java.awt.BorderLayout());

        jpGridXmlRecord.setBorder(javax.swing.BorderFactory.createTitledBorder("CFDI del encabezado de la póliza contable"));
        jpGridXmlRecord.setPreferredSize(new java.awt.Dimension(300, 1));
        jpGridXmlRecord.setLayout(new java.awt.BorderLayout());
        jpContols.add(jpGridXmlRecord, java.awt.BorderLayout.WEST);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jpGridRecordEntryRow.setBorder(javax.swing.BorderFactory.createTitledBorder("Renglones de la póliza contable:"));
        jpGridRecordEntryRow.setPreferredSize(new java.awt.Dimension(100, 200));
        jpGridRecordEntryRow.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel18.setLayout(new java.awt.BorderLayout());

        jPanel19.setLayout(new java.awt.GridLayout(4, 0, 0, 5));

        jbAdd.setText(">");
        jbAdd.setToolTipText("Mover XML a la partida seleccionada");
        jbAdd.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel19.add(jbAdd);

        jbAddAll.setText(">>");
        jbAddAll.setToolTipText("Mover todos los XML a la partida seleccionada");
        jbAddAll.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel19.add(jbAddAll);

        jPanel18.add(jPanel19, java.awt.BorderLayout.NORTH);

        jpGridRecordEntryRow.add(jPanel18, java.awt.BorderLayout.WEST);

        jPanel3.add(jpGridRecordEntryRow, java.awt.BorderLayout.NORTH);

        jpGridXmlRecordEntryRow.setBorder(javax.swing.BorderFactory.createTitledBorder("CFDI de los renglones de la póliza contable:"));
        jpGridXmlRecordEntryRow.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel20.setLayout(new java.awt.BorderLayout());

        jPanel21.setLayout(new java.awt.GridLayout(4, 0, 0, 5));

        jbRemove.setText("<");
        jbRemove.setToolTipText("Mover el XML seleccionado al encabezado de la póliza");
        jbRemove.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel21.add(jbRemove);

        jbRemoveAll.setText("<<");
        jbRemoveAll.setToolTipText("Mover todos los XML de la partida al encabezado de la póliza");
        jbRemoveAll.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel21.add(jbRemoveAll);

        jbRemoveAllAll.setText("<<<");
        jbRemoveAllAll.setToolTipText("Mover los XML de todas las partidas al encabezado de la póliza");
        jbRemoveAllAll.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel21.add(jbRemoveAllAll);

        jPanel20.add(jPanel21, java.awt.BorderLayout.NORTH);

        jpGridXmlRecordEntryRow.add(jPanel20, java.awt.BorderLayout.WEST);

        jPanel3.add(jpGridXmlRecordEntryRow, java.awt.BorderLayout.CENTER);

        jpContols.add(jPanel3, java.awt.BorderLayout.CENTER);

        getContentPane().add(jpContols, java.awt.BorderLayout.CENTER);

        jPanel2.setPreferredSize(new java.awt.Dimension(468, 33));
        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbOk.setText("Aceptar");
        jbOk.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel2.add(jbOk);

        jbCancel.setText("Cancelar");
        jbCancel.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel2.add(jbCancel);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        setSize(new java.awt.Dimension(769, 537));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void initComponentsExtra () {
        int i = 0;
        STableColumnForm tableColumnsXml[];
        
        moPaneXmlRecordRows = new STablePane(miClient);
        jpGridXmlRecord.add(moPaneXmlRecordRows, BorderLayout.CENTER);

        tableColumnsXml = new STableColumnForm[1];
        tableColumnsXml[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "XML", 250);

        for (i = 0; i < tableColumnsXml.length; i++) {
            moPaneXmlRecordRows.addTableColumn(tableColumnsXml[i]);
        }
        moPaneXmlRecordRows.createTable();
        
        i = 0;
        
        moPaneRecordEntryRows = new STablePane(miClient);
        jpGridRecordEntryRow.add(moPaneRecordEntryRows, BorderLayout.CENTER);

        STableColumnForm tableColumnsEntries[] = new STableColumnForm[10];
        tableColumnsEntries[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_INTEGER, "#", STableConstants.WIDTH_NUM_TINYINT);
        tableColumnsEntries[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "No. cuenta contable", STableConstants.WIDTH_ACCOUNT_ID);
        tableColumnsEntries[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Cuenta contable", STableConstants.WIDTH_ACCOUNT);
        tableColumnsEntries[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Concepto", 175);
        tableColumnsEntries[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Debe $", STableConstants.WIDTH_VALUE);
        tableColumnsEntries[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Haber $", STableConstants.WIDTH_VALUE);
        tableColumnsEntries[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "T. cambio", STableConstants.WIDTH_EXCHANGE_RATE);
        tableColumnsEntries[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererExchangeRate());
        tableColumnsEntries[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Debe mon $", STableConstants.WIDTH_VALUE);
        tableColumnsEntries[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Haber mon $", STableConstants.WIDTH_VALUE);
        tableColumnsEntries[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Moneda", STableConstants.WIDTH_CURRENCY_KEY);

        for (i = 0; i < tableColumnsEntries.length; i++) {
            moPaneRecordEntryRows.addTableColumn(tableColumnsEntries[i]);
        }
        
        i = 0;
        STableColumnForm tableColumnsEntriesXml[]; 
        
        moPaneXmlRecordEntryRows = new STablePane(miClient);
        jpGridXmlRecordEntryRow.add(moPaneXmlRecordEntryRows, BorderLayout.CENTER);

        tableColumnsEntriesXml = new STableColumnForm[1];
        tableColumnsEntriesXml[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "XML", 250);

        for (i = 0; i < tableColumnsEntriesXml.length; i++) {
            moPaneXmlRecordEntryRows.addTableColumn(tableColumnsEntriesXml[i]);
        }
        moPaneXmlRecordEntryRows.createTable();
        
        // Form listeners:
 
        jbAdd.addActionListener(this);
        jbAddAll.addActionListener(this);
        jbRemove.addActionListener(this);
        jbRemoveAll.addActionListener(this);
        jbRemoveAllAll.addActionListener(this);
        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);
        
        AbstractAction actionCancel = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionCancel(); }
        };
        
        SFormUtilities.putActionMap(getRootPane(), actionCancel, "cancel", KeyEvent.VK_ESCAPE, 0);
        
        populateGridXmlRecordRows();
        populateGridRecordEntryRows();
    }
    
    private void actionOk() {
        mnFormResult = SLibConstants.FORM_RESULT_OK;
        setVisible(false);
    }
    
    private void actionCancel() {
        mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
        setVisible(false);
    }
    
    private void populateGridXmlRecordRows() {
        moPaneXmlRecordRows.clearTableRows();
        for (SDataCfdRecordRow row : maXmlRecordRows) {
            if (!row.getNameXml().isEmpty()) {
                moPaneXmlRecordRows.addTableRow(row);
            }
        }
        moPaneXmlRecordRows.renderTableRows();
        moPaneXmlRecordRows.setTableRowSelection(0);
    }
    
    private void populateGridRecordEntryRows() {
        moPaneRecordEntryRows.createTable(this);
        for (SDataRecordEntry entry : maRecordEntries){
            moPaneRecordEntryRows.addTableRow(new SDataRecordEntryRow(entry));
        }
        moPaneRecordEntryRows.renderTableRows();
        moPaneRecordEntryRows.setTableRowSelection(0);
    }
    
    private void populateGridXmlRecordEntryRows(SDataRecordEntryRow sDataRecordEntryRow) {
        maXmlRecordEntryRows = new ArrayList<>();
        if (sDataRecordEntryRow.getCfds().size() > 0) {
            for (SDataCfd cfd : sDataRecordEntryRow.getCfds()) {
                maXmlRecordEntryRows.add(new SDataCfdRecordRow(maXmlRecordEntryRows.size() + 1, cfd.getPkCfdId(), cfd.getDocXmlName(), ""));
            }
        }
        
        moPaneXmlRecordEntryRows.clearTableRows();
        for (SDataCfdRecordRow row : maXmlRecordEntryRows) {
            if (!row.getNameXml().isEmpty()) {
                moPaneXmlRecordEntryRows.addTableRow(row);
            }
        }
        moPaneXmlRecordEntryRows.renderTableRows();
        moPaneXmlRecordEntryRows.setTableRowSelection(0);
    }
    
    private void actionAdd() {
        int index;
        SDataCfdRecordRow recordRow;
        ArrayList<SDataCfdRecordRow> cfdRecordRows;
        ArrayList<SDataCfd> auxCfdToDel = new ArrayList<>();
        SDataRecordEntryRow recordEntryRow = (SDataRecordEntryRow) moPaneRecordEntryRows.getSelectedTableRow();
        
        index = moPaneXmlRecordRows.getTable().getSelectedRow();
        cfdRecordRows = new ArrayList<>();
        if (index != -1) {
            
            recordRow = (SDataCfdRecordRow) moPaneXmlRecordRows.getTableRow(index);

            for (SDataCfdRecordRow row : maXmlRecordRows) {
                if (row.getMoveId() == recordRow.getMoveId()) {
                    cfdRecordRows.add(row);
                    break;
                }
            }
            
            for (SDataCfdRecordRow row : cfdRecordRows) {
                maXmlRecordRows.remove(row);
                SDataCfd cfd = (SDataCfd) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_CFD, new int[] { row.getCfdId() }, SLibConstants.EXEC_MODE_SILENT);
                recordEntryRow.getCfds().add(cfd);
                for (SDataCfd cfdToDel : recordEntryRow.getCfdsToDel()) {
                    if(SLibUtilities.compareKeys(cfdToDel.getPrimaryKey(), cfd.getPrimaryKey())) {
                        auxCfdToDel.add(cfdToDel);
                    }
                }
            }
            for (SDataCfd cfdToDel : auxCfdToDel) {
                recordEntryRow.getCfdsToDel().remove(cfdToDel);
            }

            populateGridXmlRecordRows();
            populateGridXmlRecordEntryRows(recordEntryRow);
        }
    }
    
    private void actionAddAll() {
        ArrayList<SDataCfdRecordRow> deleteRows = new ArrayList<>();
        SDataRecordEntryRow recordEntryRow = (SDataRecordEntryRow) moPaneRecordEntryRows.getSelectedTableRow();
        ArrayList<SDataCfd> auxCfdToDel = new ArrayList<>();
        
        for (SDataCfdRecordRow row : maXmlRecordRows) {
            deleteRows.add(row);
        }
        
        for (SDataCfdRecordRow row : deleteRows) {
            maXmlRecordRows.remove(row);
            SDataCfd cfd = (SDataCfd) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_CFD, new int[] { row.getCfdId() }, SLibConstants.EXEC_MODE_SILENT);
            recordEntryRow.getCfds().add(cfd);
            for (SDataCfd cfdToDel : recordEntryRow.getCfdsToDel()) {
                if(SLibUtilities.compareKeys(cfdToDel.getPrimaryKey(), cfd.getPrimaryKey())) {
                    auxCfdToDel.add(cfdToDel);
                }
            }
        }
        for (SDataCfd cfdToDel : auxCfdToDel) {
            recordEntryRow.getCfdsToDel().remove(cfdToDel);
        }

        populateGridXmlRecordRows();
        populateGridXmlRecordEntryRows(recordEntryRow);
    }
    
    private void actionRemove() {
        int index;
        SDataCfdRecordRow recordRow;
        ArrayList<SDataCfdRecordRow> auxCfdRecordRow;
        SDataRecordEntryRow recordEntryRow = (SDataRecordEntryRow) moPaneRecordEntryRows.getSelectedTableRow();
        
        index = moPaneXmlRecordEntryRows.getTable().getSelectedRow();
        auxCfdRecordRow = new ArrayList<>();
        if (index != -1) {
            
            recordRow = (SDataCfdRecordRow) moPaneXmlRecordEntryRows.getTableRow(index);
            for (SDataCfdRecordRow row : maXmlRecordEntryRows) {
                if (row.getMoveId() == recordRow.getMoveId()) {
                    auxCfdRecordRow.add(row);
                    break;
                }
            }
            
            for (SDataCfdRecordRow row : auxCfdRecordRow) {
                maXmlRecordRows.add(row);
                HashSet<SDataCfd> cfds = recordEntryRow.getCfds();
                for(SDataCfd cfd : cfds) {
                    if (SLibUtilities.compareKeys(new int[] { row.getCfdId() }, new int[] { cfd.getPkCfdId() })) {
                        recordEntryRow.getCfds().remove(cfd);
                        recordEntryRow.getCfdsToDel().add(cfd);
                        break;
                    }
                }
            }

            populateGridXmlRecordRows();
            populateGridXmlRecordEntryRows(recordEntryRow);
            moPaneXmlRecordEntryRows.renderTableRows();
            moPaneXmlRecordEntryRows.setTableRowSelection(0);
        }
    }
    
    private void actionRemoveAll() {
        SDataRecordEntryRow recordEntryRow = (SDataRecordEntryRow) moPaneRecordEntryRows.getSelectedTableRow();
        
        for (SDataCfdRecordRow row : maXmlRecordEntryRows) {
            maXmlRecordRows.add(row);
        }

        for (SDataCfd cfd : recordEntryRow.getCfds()) {
            recordEntryRow.getCfdsToDel().add(cfd);
        }
        recordEntryRow.getCfds().clear();
        populateGridXmlRecordRows();
        populateGridXmlRecordEntryRows(recordEntryRow);
        moPaneXmlRecordEntryRows.renderTableRows();
        moPaneXmlRecordEntryRows.setTableRowSelection(0);
    }
    
    private void actionRemoveAllAll() {
        for (int i  = 0; i < moPaneRecordEntryRows.getTable().getRowCount(); i++) {
            SDataRecordEntryRow recordEntryRow = (SDataRecordEntryRow) moPaneRecordEntryRows.getTableRow(i);
            
            maXmlRecordEntryRows = new ArrayList<>();
            if (recordEntryRow.getCfds().size() > 0) {
                for (SDataCfd cfd : recordEntryRow.getCfds()) {
                    maXmlRecordEntryRows.add(new SDataCfdRecordRow(maXmlRecordEntryRows.size() + 1, cfd.getPkCfdId(), cfd.getDocXmlName(), ""));
                }
            }
            for (SDataCfdRecordRow row : maXmlRecordEntryRows) {
                maXmlRecordRows.add(row);
            }
            for (SDataCfd cfd : recordEntryRow.getCfds()) {
                recordEntryRow.getCfdsToDel().add(cfd);
            }
            recordEntryRow.getCfds().clear();
            populateGridXmlRecordEntryRows(recordEntryRow);
        }

        populateGridXmlRecordRows();
        moPaneRecordEntryRows.setTableRowSelection(0);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JButton jbAdd;
    private javax.swing.JButton jbAddAll;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbOk;
    private javax.swing.JButton jbRemove;
    private javax.swing.JButton jbRemoveAll;
    private javax.swing.JButton jbRemoveAllAll;
    private javax.swing.JPanel jpContols;
    private javax.swing.JPanel jpGridRecordEntryRow;
    private javax.swing.JPanel jpGridXmlRecord;
    private javax.swing.JPanel jpGridXmlRecordEntryRow;
    // End of variables declaration//GEN-END:variables

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            if (moPaneRecordEntryRows.getTable().getSelectedRow() > -1) {
                populateGridXmlRecordEntryRows((SDataRecordEntryRow)moPaneRecordEntryRows.getSelectedTableRow());
            }
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == jbAdd) {
                actionAdd();
            }
            if (button == jbAddAll) {
                actionAddAll();
            }
            if (button == jbRemove) {
                actionRemove();
            }
            if (button == jbRemoveAll) {
                actionRemoveAll();
            }
            if (button == jbRemoveAllAll) {
                actionRemoveAllAll();
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
    
    public ArrayList<SDataCfdRecordRow> getGridRecordRows() {
        return maXmlRecordRows;
    }
    
    public ArrayList<SDataRecordEntryRow> getGridRecordEntryRows() {
        ArrayList<SDataRecordEntryRow> recordEntryRows = new ArrayList<>();
        for (int i = 0; i< moPaneRecordEntryRows.getTable().getRowCount(); i++) {
            recordEntryRows.add((SDataRecordEntryRow) moPaneRecordEntryRows.getTableRow(i));
        }
        return recordEntryRows;
    }
}
