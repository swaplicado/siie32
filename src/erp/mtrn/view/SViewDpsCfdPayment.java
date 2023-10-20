/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.gui.SModuleUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.data.SDataSqlUtilities;
import erp.lib.table.STabFilterDatePeriod;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.mtrn.data.SDataDpsCfdPayment;
import erp.mtrn.form.SDialogCfdiPaymentPicker;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import sa.lib.xml.SXmlUtils;

/**
 *
 * @author Isabel Servín
 */
public class SViewDpsCfdPayment extends erp.lib.table.STableTab implements java.awt.event.ActionListener {
    
    private javax.swing.JButton jbViewLinks;
    private javax.swing.JButton jbUploadXml;
    private javax.swing.JButton jbVinculeXml;
    private javax.swing.JButton jbRemoveXml;
    private javax.swing.JButton jbDownloadXml;
    private javax.swing.JButton jbMarkXml;
    private javax.swing.JButton jbDismarkXml;
    
    private erp.lib.table.STabFilterDatePeriod moTabFilterDatePeriod;
    
    public SViewDpsCfdPayment(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01) {
        super(client, tabTitle, SDataConstants.TRN_DPS_CFD_PAY, auxType01); // 217, 218
        initComponents();
        setEnabled(false);
    }

    private void initComponents() {
        
        jbViewLinks = new JButton(miClient.getImageIcon(SLibConstants.ICON_LINK));
        jbViewLinks.setPreferredSize(new Dimension(23, 23));
        jbViewLinks.addActionListener(this);
        jbViewLinks.setToolTipText("Ver vínculos del documento");
        
        jbUploadXml = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_ADD));
        jbUploadXml.setPreferredSize(new Dimension(23, 23));
        jbUploadXml.addActionListener(this);
        jbUploadXml.setToolTipText("Cargar XML de pagos");
        
        jbVinculeXml = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_ADD));
        jbVinculeXml.setPreferredSize(new Dimension(23, 23));
        jbVinculeXml.addActionListener(this);
        jbVinculeXml.setToolTipText("Vincular XML de pagos");
        
        jbRemoveXml = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_REM));
        jbRemoveXml.setPreferredSize(new Dimension(23, 23));
        jbRemoveXml.addActionListener(this);
        jbRemoveXml.setToolTipText("Eliminar XML de pagos");
        
        jbDownloadXml = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_XML));
        jbDownloadXml.setPreferredSize(new Dimension(23, 23));
        jbDownloadXml.addActionListener(this);
        jbDownloadXml.setToolTipText("Descargar XML de pagos");
        
        jbMarkXml = new JButton(miClient.getImageIcon(SLibConstants.ICON_EDIT));
        jbMarkXml.setPreferredSize(new Dimension(23, 23));
        jbMarkXml.addActionListener(this);
        jbMarkXml.setToolTipText("Marcar como concluido a complemento de pago");
        
        jbDismarkXml = new JButton(miClient.getImageIcon(SLibConstants.ICON_EDIT));
        jbDismarkXml.setPreferredSize(new Dimension(23, 23));
        jbDismarkXml.addActionListener(this);
        jbDismarkXml.setToolTipText("Desmarcar como concluido a complemento de pago");
        
        addTaskBarUpperComponent(jbViewLinks);
        addTaskBarUpperComponent(jbUploadXml);
        addTaskBarUpperComponent(jbVinculeXml);
        addTaskBarUpperComponent(jbRemoveXml);
        addTaskBarUpperComponent(jbDownloadXml);
        addTaskBarUpperComponent(jbMarkXml);
        addTaskBarUpperComponent(jbDismarkXml);
        
        jbViewLinks.setEnabled(true);
        jbUploadXml.setEnabled(true);
        jbVinculeXml.setEnabled(true);
        jbRemoveXml.setEnabled(true);
        jbDownloadXml.setEnabled(true);
        
        int i;
       
        moTabFilterDatePeriod = new STabFilterDatePeriod(miClient, this, SLibConstants.GUI_DATE_AS_YEAR_MONTH);
        
        if (getTabTypeAux01() == SDataConstantsSys.TRNX_DPS_CFD_PAY_DONE) {
            addTaskBarUpperSeparator();
            addTaskBarUpperComponent(moTabFilterDatePeriod);
        }
        
        STableField[] aoKeyFields = new STableField[2];
        erp.lib.table.STableColumn[] aoTableColumns = new STableColumn[18];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_year");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_doc");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", "Proveedor", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_key", "Clave proveedor", 50);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "suc_pro", "Sucursal proveedor", 75);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "code", "Tipo documento", STableConstants.WIDTH_CODE_DOC);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num", "Folio documento", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "num_ref", "Ref documento", STableConstants.WIDTH_DOC_NUM_REF);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "dt", "Fecha documento", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "tot_cur_r", "Total mon $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "_pay_cur", "Total mon pagado $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cur_key", "Moneda", STableConstants.WIDTH_CURRENCY_KEY);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "exc_rate", "T cambio", STableConstants.WIDTH_EXCHANGE_RATE);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererExchangeRate());
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "tot_r", "Total $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "_pay", "Total pagado $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cur_key_local", "Moneda local", STableConstants.WIDTH_CURRENCY_KEY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "_count_pay", "Pagos", 50);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "_count_cp", "XML de pagos", 50);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "done", "Marcado como terminado", 50);
        
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        jbNew.setEnabled(false);
        jbEdit.setEnabled(false);

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.USRU_USR);
        mvSuscriptors.add(SDataConstants.FIN_REC);
        mvSuscriptors.add(SDataConstants.FIN_REC_ETY);
        mvSuscriptors.add(SDataConstants.TRN_DPS);
        mvSuscriptors.add(SDataConstants.BPSU_BP);
        mvSuscriptors.add(SDataConstants.BPSU_BPB);
        mvSuscriptors.add(SDataConstants.TRN_DPS_CFD_PAY);

        populateTable();
    }
    
    private boolean isRowSelected() {        
        return !(moTablePane.getSelectedTableRow() == null || moTablePane.getSelectedTableRow().getIsSummary());
    }
    
    private void actionViewLinks() {
        if (jbViewLinks.isEnabled()) {
            if (isRowSelected()) {
                SModuleUtilities.showDocumentLinks(miClient, moTablePane.getSelectedTableRow());
            }
        }
    }
    
    private void actionUploadXml() {
        if (jbUploadXml.isEnabled()) {
            if (isRowSelected()) {
                FileFilter filter = new FileNameExtensionFilter("XML file", "xml");
                miClient.getFileChooser().repaint();
                miClient.getFileChooser().setAcceptAllFileFilterUsed(false);
                miClient.getFileChooser().setFileFilter(filter);

                try {
                    if (miClient.getFileChooser().showOpenDialog(miClient.getFrame()) == JFileChooser.APPROVE_OPTION ) {
                        if (miClient.getFileChooser().getSelectedFile().getName().toLowerCase().contains(".xml")) {
                            int bpId = 0;
                            String sql = "SELECT fid_bp_r FROM trn_dps WHERE id_year = " + ((int[]) moTablePane.getSelectedTableRow().getPrimaryKey())[0] + 
                                    " AND id_doc = " + ((int[]) moTablePane.getSelectedTableRow().getPrimaryKey())[1];
                            ResultSet resultSet = miClient.getSession().getStatement().executeQuery(sql);
                            if (resultSet.next()) {
                                bpId = resultSet.getInt(1);
                            }
                            String absolutePath = miClient.getFileChooser().getSelectedFile().getAbsolutePath();
                            String cfdi = SXmlUtils.readXml(absolutePath);
                            SDataDpsCfdPayment pay = new SDataDpsCfdPayment();
                            pay.setPkYearId(((int[]) moTablePane.getSelectedTableRow().getPrimaryKey())[0]);
                            pay.setPkDocId(((int[])moTablePane.getSelectedTableRow().getPrimaryKey())[1]);
                            pay.setFkUserId(miClient.getSession().getUser().getPkUserId());
                            pay.setAuxCfd(cfdi);
                            pay.setAuxCfdName(miClient.getFileChooser().getSelectedFile().getName());
                            pay.setAuxBizPartner(bpId);
                            if (pay.save(miClient.getSession().getDatabase().getConnection()) == SLibConstants.DB_ACTION_SAVE_OK) { 
                                miClient.showMsgBoxInformation("XML anexado.");
                                moTablePane.repaint();
                            }
                        }
                        else {
                            miClient.showMsgBoxInformation("El archivo sólo puede ser XML.");
                        }
                    }
                    miClient.getFileChooser().resetChoosableFileFilters();
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
    }
    
    private void actionVinculeXml() {
        if (jbVinculeXml.isEnabled()) {
            if (isRowSelected()) {
                try {
                    int bpId = 0;
                    String sql = "SELECT fid_bp_r FROM trn_dps WHERE id_year = " + ((int[]) moTablePane.getSelectedTableRow().getPrimaryKey())[0] + 
                            " AND id_doc = " + ((int[]) moTablePane.getSelectedTableRow().getPrimaryKey())[1];
                    ResultSet resultSet = miClient.getSession().getStatement().executeQuery(sql);
                    if (resultSet.next()) {
                        bpId = resultSet.getInt(1);
                    }
                    SDialogCfdiPaymentPicker pic = new SDialogCfdiPaymentPicker(miClient);
                    pic.setValue(bpId, ((int[]) moTablePane.getSelectedTableRow().getPrimaryKey())[0], ((int[]) moTablePane.getSelectedTableRow().getPrimaryKey())[1]);
                    pic.setVisible(true);
                }
                catch (Exception e) {
                    miClient.showMsgBoxWarning(e.getMessage());
                }
            }
        }
    }
    
    private void actionRemoveXml() {
        if (jbRemoveXml.isEnabled()) {
            if (isRowSelected()) {
                try {
                    if (miClient.showMsgBoxConfirm("Se eliminarán todos los XML de pagos relacionados a esta factura.\n¿Desea continuar?") == JOptionPane.OK_OPTION) {
                        String sql = "SELECT id_cfd_pay FROM trn_dps_cfd_pay "
                                + "WHERE id_year = " + ((int[]) moTablePane.getSelectedTableRow().getPrimaryKey())[0] + " "
                                + "AND id_doc = " + ((int[])moTablePane.getSelectedTableRow().getPrimaryKey())[1];
                        ResultSet resultSet = miClient.getSession().getDatabase().getConnection().createStatement().executeQuery(sql);
                        while (resultSet.next()) {
                            SDataDpsCfdPayment pay = new SDataDpsCfdPayment();
                            pay.read(new int[] {
                                ((int[]) moTablePane.getSelectedTableRow().getPrimaryKey())[0],
                                ((int[])moTablePane.getSelectedTableRow().getPrimaryKey())[1],
                                resultSet.getInt(1)
                            }, miClient.getSession().getStatement());
                            pay.setAuxToBeDeleted(true);
                            pay.save(miClient.getSession().getDatabase().getConnection());
                        }
                        miClient.showMsgBoxInformation("XML de pagos elimidados.");
                        moTablePane.renderTableRows();
                    }
                }
                catch (Exception e) {
                    miClient.showMsgBoxWarning(e.getMessage());
                }
            }
        }
    }
    
    private void actionDownloadXml() {
        if (jbDownloadXml.isEnabled()) {
            if (isRowSelected()) {
                try {
                    miClient.getFileChooser().setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    if (miClient.getFileChooser().showSaveDialog(miClient.getFrame()) == JFileChooser.APPROVE_OPTION) {
                        File fileAux = miClient.getFileChooser().getSelectedFile();
                        miClient.getFileChooser().setFileSelectionMode(JFileChooser.FILES_ONLY);
                        String sql = "SELECT id_cfd_pay FROM trn_dps_cfd_pay "
                                    + "WHERE id_year = " + ((int[]) moTablePane.getSelectedTableRow().getPrimaryKey())[0] + " "
                                    + "AND id_doc = " + ((int[])moTablePane.getSelectedTableRow().getPrimaryKey())[1];
                        ResultSet resultSet = miClient.getSession().getDatabase().getConnection().createStatement().executeQuery(sql);
                        while (resultSet.next()) {
                            SDataDpsCfdPayment pay = new SDataDpsCfdPayment();
                            pay.read(new int[] {
                                ((int[]) moTablePane.getSelectedTableRow().getPrimaryKey())[0],
                                ((int[])moTablePane.getSelectedTableRow().getPrimaryKey())[1],
                                resultSet.getInt(1)
                            }, miClient.getSession().getStatement());
                            miClient.getFileChooser().setSelectedFile(new File(fileAux, pay.getAuxCfdName()));
                            File file = new File(miClient.getFileChooser().getSelectedFile().getAbsolutePath());
                            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF8"))) {
                                bw.write(pay.getAuxCfd());
                            }
                        }
                        miClient.showMsgBoxInformation("XML de pagos descargados con éxito.");
                    }
                }
                catch (HeadlessException | SQLException | IOException e) {
                    miClient.showMsgBoxInformation(e.getMessage());
                }
            }
        }
    }
    
    private void actionMark(){
        if (isRowSelected()) {
            try {
                String sql = "INSERT INTO trn_dps_cfd_pay_done VALUES "
                            + "(" + ((int[]) moTablePane.getSelectedTableRow().getPrimaryKey())[0] + ", "
                            + ((int[])moTablePane.getSelectedTableRow().getPrimaryKey())[1] + ", " + miClient.getSession().getUser().getPkUserId() + ", NOW())";
                miClient.getSession().getDatabase().getConnection().createStatement().execute(sql);
                
            }
            catch (Exception e){
                System.out.println("error");
            }
        }
    }
    
    private void actionDismark(){
        if (isRowSelected()) {
            try {
                String sql = "DELETE FROM trn_dps_cfd_pay_done WHERE "
                            + "id_year = " + ((int[]) moTablePane.getSelectedTableRow().getPrimaryKey())[0] + " AND id_doc = "
                            + ((int[])moTablePane.getSelectedTableRow().getPrimaryKey())[1] + " ";
                miClient.getSession().getDatabase().getConnection().createStatement().execute(sql);
            }
            catch (Exception e){
                System.out.println("error");
            }
        }
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {

        }
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {

        }
    }

    @Override
    public void actionDelete() {
        if (jbDelete.isEnabled()) {

        }
    }

    @Override
    public void createSqlQuery() {
        java.lang.String sqlWhere = "";
        erp.lib.table.STableSetting setting;
        
        if (getTabTypeAux01() == SDataConstantsSys.TRNX_DPS_CFD_PAY_DONE) {
            for (int i = 0; i < mvTableSettings.size(); i++) {
                setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
                if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                    sqlWhere +=  SDataSqlUtilities.composePeriodFilter((int[]) setting.getSetting(), "dt");
                    sqlWhere += " AND (coalesce(_count_cp, 0) = _count_pay OR done IS NOT NULL) ";
                }
            }
        }
        
        if (getTabTypeAux01() == SDataConstantsSys.TRNX_DPS_CFD_PAY) {
            sqlWhere += " coalesce(_count_cp, 0) < _count_pay AND done IS NULL ";
        }

        msSql = "SELECT " 
                + "* " 
                + "FROM ( " 
                + "(SELECT " 
                + "d.id_year, d.id_doc, d.dt, d.num_ser, d.num, d.num_ref, " 
                + "d.tot_r, d.exc_rate, d.tot_cur_r, " 
                + "bp.bp, bp.id_bp, bpb.code AS bpb, bpb.id_bpb, c.cur_key, td.code, bpb_1.bpb AS suc_pro, ct.bp_key, "
                + "CONCAT(d.num_ser, IF(length(d.num_ser) = 0, '', '-'), d.num) AS f_num, " 
                + "SUM(re.debit) AS _pay, " 
                + "SUM(re.debit_cur) AS _pay_cur, " 
                + "COUNT(*) AS _count_pay, "
                + "'" + miClient.getSession().getSessionCustom().getLocalCurrencyCode() + "' AS f_cur_key_local " 
                + "FROM " 
                + "fin_rec AS r " 
                + "INNER JOIN fin_rec_ety AS re ON r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num " 
                + "INNER JOIN trn_dps AS d ON re.fid_dps_year_n = d.id_year AND re.fid_dps_doc_n = d.id_doc " 
                + "INNER JOIN erp.bpsu_bpb AS bpb ON d.fid_cob = bpb.id_bpb " 
                + "INNER JOIN erp.bpsu_bp AS bp ON d.fid_bp_r = bp.id_bp " 
                + "INNER JOIN erp.cfgu_cur AS c ON d.fid_cur = c.id_cur " 
                + "INNER JOIN erp.trnu_tp_dps AS td ON d.fid_ct_dps = td.id_ct_dps AND d.fid_cl_dps = td.id_cl_dps AND d.fid_tp_dps = td.id_tp_dps " 
                + "INNER JOIN erp.bpsu_bpb AS bpb_1 ON d.fid_bpb = bpb_1.id_bpb " 
                + "INNER JOIN erp.bpsu_bp_ct AS ct ON bp.id_bp = ct.id_bp AND ct.id_ct_bp = 2 " 
                + "WHERE NOT r.b_del AND NOT re.b_del " 
                + "AND re.fid_ct_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[0] + " AND re.fid_tp_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[1] + " " 
                + "AND re.fid_tp_acc_mov = " + SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_APP[0] + " "
                + "AND re.fid_cl_acc_mov = " + SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_APP[1] + " "
                + "AND re.fid_cls_acc_mov = " + SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_APP[2] + " "
                + "AND d.fid_ct_dps = " + SDataConstantsSys.TRNS_CT_DPS_PUR + " AND d.fid_tp_pay = " + SDataConstantsSys.TRNS_TP_PAY_CREDIT + " " 
                + "GROUP BY " 
                + "d.id_year, d.id_doc, d.dt, d.num_ser, d.num, d.num_ref, " 
                + "d.tot_r, d.exc_rate, d.tot_cur_r, " 
                + "bp.bp, bp.id_bp, bpb.bpb, bpb.id_bpb, c.cur_key, td.code, bpb_1.bpb, ct.bp_key " 
                + "ORDER BY " 
                + "d.id_year, d.id_doc " 
                + ") AS tpay " 
                + "LEFT OUTER JOIN " 
                + "(SELECT " 
                + "id_year AS id_year_aux, id_doc AS id_doc_aux, COUNT(*) AS _count_cp " 
                + "FROM " 
                + "trn_dps_cfd_pay " 
                + "GROUP BY " 
                + "id_year , id_doc " 
                + "ORDER BY " 
                + "id_year, id_doc) AS tcp ON " 
                + "tpay.id_year = tcp.id_year_aux AND tpay.id_doc = tcp.id_doc_aux " 
                + "LEFT OUTER JOIN " 
                + "(SELECT " 
                + "id_year AS id_year_aux, id_doc AS id_doc_aux, 1 AS done " 
                + "FROM " 
                + "trn_dps_cfd_pay_done " 
                + "ORDER BY " 
                + "id_year, id_doc) AS tcpd ON " 
                + "tpay.id_year = tcpd.id_year_aux AND tpay.id_doc = tcpd.id_doc_aux) " 
                + (sqlWhere.isEmpty() ? "" : "WHERE " + sqlWhere)
                + "ORDER BY " 
                + "bp, id_bp, bpb, id_bpb, num_ser, num " 
                + ";";
    }
    
    @Override
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        super.actionPerformed(evt);

        try {
            if (evt.getSource() instanceof javax.swing.JButton) {
                JButton button = (JButton) evt.getSource();

                if (button == jbViewLinks) {
                    actionViewLinks();
                }
                else if (button == jbUploadXml) {
                    actionUploadXml();
                }
                else if (button == jbVinculeXml) {
                    actionVinculeXml();
                }
                else if (button == jbRemoveXml) {
                    actionRemoveXml();
                }
                else if (button == jbDownloadXml) {
                    actionDownloadXml();
                }
                else if (button == jbMarkXml) {
                    actionMark();
                }
                else if (button == jbDismarkXml) {
                    actionDismark();
                }
            }
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
        finally {
            miClient.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
}