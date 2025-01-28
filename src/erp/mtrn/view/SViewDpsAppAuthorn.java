/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.view;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.data.SDataSqlUtilities;
import erp.lib.table.STabFilterDatePeriod;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.lib.table.STableTab;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.cfg.utils.SAuthorizationUtils;
import erp.mod.hrs.utils.SDocUtils;
import erp.mod.trn.db.SDbSupplierFile;
import erp.mod.trn.db.SDbSupplierFileProcess;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiParams;

/**
 *
 * @author Isabel Servín
 */
public class SViewDpsAppAuthorn extends STableTab implements ActionListener {
    
    private STabFilterDatePeriod moTabFilterDatePeriod;
    private JFileChooser moFileChooserDownload;
    
    private JButton jbAnullAuth;
    private JButton jbAddFileSupp;
    private JButton jbDownFileSupp;
    private JButton jbAll;
    private JButton jbAuth;
    private JButton jbRej;
    private JTextField tfStatus;
    private int mnShow;

    public SViewDpsAppAuthorn(SClientInterface client, String tabTitle, int auxType01) {
        super(client, tabTitle, SDataConstants.TRNX_DPS_AUTH_APP, auxType01);
        initComponents();
    }
    
    private void initComponents() {
        int i;
        
        moFileChooserDownload = new JFileChooser();
        moFileChooserDownload.setAcceptAllFileFilterUsed(false);
        moFileChooserDownload.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        moFileChooserDownload.setDialogTitle("Seleccionar directorio para descargar archivo...");
        
        jbAnullAuth = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_return.gif")));
        jbAnullAuth.setPreferredSize(new Dimension(23, 23));
        jbAnullAuth.addActionListener(this);
        jbAnullAuth.setToolTipText("Anular autorización");
        
        jbAddFileSupp = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_doc_add_ora.gif")));
        jbAddFileSupp.setPreferredSize(new Dimension(23, 23));
        jbAddFileSupp.addActionListener(this);
        jbAddFileSupp.setToolTipText("Anexar archivos de soporte al documento");
        
        jbDownFileSupp = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_doc_down_ora.gif")));
        jbDownFileSupp.setPreferredSize(new Dimension(23, 23));
        jbDownFileSupp.addActionListener(this);
        jbDownFileSupp.setToolTipText("Descargar archivos de soporte anexados al documento");
        
        jbAll = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_detail.gif")));
        jbAll.setPreferredSize(new Dimension(23, 23));
        jbAll.addActionListener(this);
        jbAll.setToolTipText("Mostrar todos los concluidos");
        
        jbAuth = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_thumbs_up.gif")));
        jbAuth.setPreferredSize(new Dimension(23, 23));
        jbAuth.addActionListener(this);
        jbAuth.setToolTipText("Mostrar autorizados");
        
        jbRej = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_thumbs_down.gif")));
        jbRej.setPreferredSize(new Dimension(23, 23));
        jbRej.addActionListener(this);
        jbRej.setToolTipText("Mostrar rechazados");
        
        moTabFilterDatePeriod = new STabFilterDatePeriod(miClient, this, SLibConstants.GUI_DATE_AS_YEAR_MONTH);
        
        addTaskBarUpperComponent(jbAnullAuth);
        if (mnTabTypeAux01 == SModSysConsts.CFGS_ST_AUTHORN_AUTH) {
            addTaskBarUpperComponent(jbAddFileSupp);
        }
        addTaskBarUpperComponent(jbDownFileSupp);
        if (mnTabTypeAux01 == SModSysConsts.CFGS_ST_AUTHORN_AUTH) {
            addTaskBarUpperSeparator();
            addTaskBarUpperComponent(moTabFilterDatePeriod);
            addTaskBarUpperSeparator();
            tfStatus = new JTextField("TODOS");
            tfStatus.setEnabled(false);
            tfStatus.setPreferredSize(new java.awt.Dimension(100, 23));
            addTaskBarUpperComponent(tfStatus);
            addTaskBarUpperComponent(jbAll);
            addTaskBarUpperComponent(jbAuth);
            addTaskBarUpperComponent(jbRej); 
        }
        
        mnShow = SDataConstantsSys.CFGS_ST_AUTHORN_NA;
        
        jbNew.setEnabled(false);
        jbEdit.setEnabled(false);
        jbDelete.setEnabled(false);
        
        STableField[] aoKeyFields = new STableField[2];
        STableColumn[] aoTableColumns = new STableColumn[17];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "d.id_year");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "d.id_doc");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "fecha_sta", "Fecha estatus", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "doc_tp", "Tipo documento", STableConstants.WIDTH_CODE_DOC);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "folio", "Folio documento", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ref", "Ref documento", STableConstants.WIDTH_DOC_NUM_REF);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "fecha_doc", "Fecha documento", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "suc", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", "Proveedor", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_key", "Clave proveedor", 50);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb", "Sucursal proveedor", 75);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "files", "Soportes de pedidos", STableConstants.WIDTH_ICON);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererIcon());
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "auth_sta", "Autorización app web", 100);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "f_status", "Autorización", STableConstants.WIDTH_ICON);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererIcon());
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "tot_cur_r", "Total mon $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cur_key", "Moneda", STableConstants.WIDTH_CURRENCY_KEY);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "exc_rate", "T cambio", STableConstants.WIDTH_EXCHANGE_RATE);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererExchangeRate());
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "tot_r", "Total $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cur_key_local", "Moneda", STableConstants.WIDTH_CURRENCY_KEY);
        
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.TRN_DPS);
        mvSuscriptors.add(SDataConstants.TRN_DPS_AUTHORN);
        mvSuscriptors.add(SDataConstants.TRNU_TP_DPS);
        mvSuscriptors.add(SDataConstants.BPSU_BP);
        mvSuscriptors.add(SDataConstants.BPSU_BP_CT);
        mvSuscriptors.add(SDataConstants.BPSU_BPB);

        populateTable();
    }
    
    @Override
    public void createSqlQuery() {
        String where = "";
        String datePeriod = "";
        STableSetting setting;
        
        if (mnTabTypeAux01 == SDataConstantsSys.CFGS_ST_AUTHORN_AUTH) {
            for (int i = 0; i < mvTableSettings.size(); i++) {
                setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
                if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                    datePeriod += SDataSqlUtilities.composePeriodFilter((int[]) setting.getSetting(), "d.dt");
                }
            }
        }
        
        if (mnTabTypeAux01 == SDataConstantsSys.CFGS_ST_AUTHORN_PROC) {
            where += (where.isEmpty() ? "" : "AND ") + "a.fid_st_authorn IN(" + SModSysConsts.CFGS_ST_AUTHORN_PEND + ", " + SModSysConsts.CFGS_ST_AUTHORN_PROC + ") "
                    + "AND d.fid_st_dps_authorn NOT IN (" + SDataConstantsSys.TRNS_ST_DPS_AUTHORN_AUTHORN + ", " + SDataConstantsSys.TRNS_ST_DPS_AUTHORN_REJECT + ") ";
        }
        else {
            if (mnShow != SDataConstantsSys.CFGS_ST_AUTHORN_NA) {
                where += (where.isEmpty() ? "" : "AND ") + "a.fid_st_authorn = " + mnShow + " ";
            }
            else {
                where += (where.isEmpty() ? "" : "AND ") + "(a.fid_st_authorn IN(" + SModSysConsts.CFGS_ST_AUTHORN_AUTH + ", " + SModSysConsts.CFGS_ST_AUTHORN_REJ + ") OR "
                        + " d.fid_st_dps_authorn IN(" + SDataConstantsSys.TRNS_ST_DPS_AUTHORN_AUTHORN + ", " + SDataConstantsSys.TRNS_ST_DPS_AUTHORN_REJECT + ")) ";
            }
        }
        
        msSql = "SELECT DISTINCT "
                + "d.id_year, "
                + "d.id_doc, "
                + "a.ts_edit AS fecha_sta, " 
                + "dt.code AS doc_tp, " 
                + "d.num AS folio, " 
                + "d.num_ref AS ref, " 
                + "d.dt AS fecha_doc, " 
                + "(SELECT cob.code FROM erp.bpsu_bpb AS cob WHERE d.fid_cob = cob.id_bpb) AS suc, " 
                + "bp.bp, " 
                + "bpc.bp_key, " 
                + "bpb.bpb, " 
                + "CASE d.fid_st_dps_authorn " 
                + "WHEN " + SDataConstantsSys.TRNS_ST_DPS_AUTHORN_AUTHORN + " THEN 'AUTORIZADO' " 
                + "WHEN " + SDataConstantsSys.TRNS_ST_DPS_AUTHORN_REJECT + " THEN 'RECHAZADO' " 
                + "ELSE sah.name END AS auth_sta, " 
                + "IF(fl.id_sup_file IS NOT NULL, " + STableConstants.ICON_VIEW_FOLDER + ", 0) AS files, "
                + "CASE d.fid_st_dps_authorn "
                + "WHEN " + SDataConstantsSys.TRNS_ST_DPS_AUTHORN_PENDING + " THEN " + STableConstants.ICON_ST_WAIT + " "
                + "WHEN " + SDataConstantsSys.TRNS_ST_DPS_AUTHORN_AUTHORN + " THEN " + STableConstants.ICON_VIEW_OK_GREEN + " "
                + "WHEN " + SDataConstantsSys.TRNS_ST_DPS_AUTHORN_REJECT + " THEN " + STableConstants.ICON_VIEW_REJECTED + " "
                + "ELSE " + STableConstants.ICON_NULL + " END AS f_status, "
                + "d.tot_cur_r, " 
                + "(SELECT c.cur_key FROM erp.cfgu_cur AS c WHERE d.fid_cur = c.id_cur) AS f_cur_key, "
                + "d.exc_rate, "
                + "d.tot_r, "
                + "'MXN' AS f_cur_key_local " 
                + "FROM " + SDataConstants.TablesMap.get(SDataConstants.TRN_DPS) + " AS d "
                + "INNER JOIN " + SDataConstants.TablesMap.get(SDataConstants.TRN_DPS_AUTHORN) + " AS a "
                + "ON d.id_year = a.id_year AND d.id_doc = a.id_doc AND NOT a.b_del "
                + "INNER JOIN " + SDataConstants.TablesMap.get(SDataConstants.TRNU_TP_DPS) + " AS dt "
                + "ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps "
                + "INNER JOIN " + SDataConstants.TablesMap.get(SDataConstants.BPSU_BP) + " AS bp "
                + "ON d.fid_bp_r = bp.id_bp "
                + "INNER JOIN " + SDataConstants.TablesMap.get(SDataConstants.BPSU_BP_CT) + " AS bpc "
                + "ON bp.id_bp = bpc.id_bp AND bpc.id_ct_bp = 2 "
                + "INNER JOIN " + SDataConstants.TablesMap.get(SDataConstants.BPSU_BPB) + " AS bpb "
                + "ON d.fid_bpb = bpb.id_bpb "
                + "INNER JOIN erp.cfgs_st_authorn AS sah "
                + "ON a.fid_st_authorn = sah.id_st_authorn "
                + "LEFT OUTER JOIN trn_sup_file_dps AS fl "
                + "ON d.id_year = fl.id_year AND d.id_doc = fl.id_doc " 
                + "WHERE d.fid_ct_dps = " + SDataConstantsSys.TRNS_CL_DPS_PUR_ORD[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_PUR_ORD[1] + " "
                + (where.isEmpty() ? "" : "AND ") + where + " " 
                + (datePeriod.isEmpty() ? "" : "AND ") + datePeriod + " ";
    }

    @Override
    public void actionNew() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void actionEdit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void actionDelete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private boolean isRowSelected() {
        return !(moTablePane.getSelectedTableRow() == null || moTablePane.getSelectedTableRow().getIsSummary());
    }
    
    private void actionAddFileSupp() {
        try {
            if (jbAddFileSupp.isEnabled()) {
                if (isRowSelected()) {
                    SGuiParams params = new SGuiParams();
                    params.setKey((int[]) moTablePane.getSelectedTableRow().getPrimaryKey());
                    ((SGuiClient) miClient).getSession().getModule(SModConsts.MOD_TRN_N).showForm(SModConsts.TRNX_SUP_FILE_DPS_PROC, SLibConstants.UNDEFINED, params);
                    miClient.getGuiModule(SDataConstants.MOD_PUR).refreshCatalogues(mnTabType);
                }
            }
        }
        catch (Exception e) {
            miClient.showMsgBoxWarning(e.getMessage());
        }
    }
    
    private void actionRestartAuth() {
         try {
            if (jbAnullAuth.isEnabled()) {
                if (isRowSelected()) {
                    SDbSupplierFileProcess fileProcess = new SDbSupplierFileProcess();
                    fileProcess.read(miClient.getSession(), (int[]) moTablePane.getSelectedTableRow().getPrimaryKey());
                    if (fileProcess.getDps().getFkDpsAuthorizationStatusId() != SDataConstantsSys.TRNS_ST_DPS_AUTHORN_PENDING
                            && fileProcess.getDps().getFkDpsAuthorizationStatusId() != SDataConstantsSys.TRNS_ST_DPS_AUTHORN_REJECT) {
                        miClient.showMsgBoxInformation("No se puede anular la autorización porque el estatus del documento es " + fileProcess.getDpsStatus() + ".");
                    }
                    else {
                        if (miClient.showMsgBoxConfirm("El documento regresara a estatus de autorización \"N/A\".\n¿Desea continuar?") == JOptionPane.OK_OPTION) {
                            // Eliminar archivos de la nube
                            SDocUtils.deleteFilesToCloud(miClient.getSession(), fileProcess);
                            
                            // Eliminar pasos de autorización
                            SAuthorizationUtils.deleteStepsOfAuthorization(miClient.getSession(), SAuthorizationUtils.AUTH_TYPE_DPS, fileProcess.getPrimaryKey());
                            
                            // Actualizar estatus de autorización
                            String sql = "UPDATE trn_dps_authorn SET b_del = 1 WHERE id_year = " + fileProcess.getPkYearId() + " AND id_doc = " + fileProcess.getPkDocId();
                            miClient.getSession().getStatement().execute(sql);
                            
                            fileProcess.updateDpsStatus(miClient.getSession(), SDataConstantsSys.TRNS_ST_DPS_AUTHORN_NA);
                            
                            miClient.getGuiModule(SDataConstants.MOD_PUR).refreshCatalogues(mnTabType);
                            miClient.getGuiModule(SDataConstants.MOD_PUR).refreshCatalogues(SDataConstants.TRN_DPS);
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            miClient.showMsgBoxWarning(e.getMessage());
        }
    }
    
    private void actionDownFileSupp() {
        try {
            if (jbAddFileSupp.isEnabled()) {
                if (isRowSelected()) {
                    SDbSupplierFileProcess fileProcess = new SDbSupplierFileProcess();
                    fileProcess.read(miClient.getSession(), (int[]) moTablePane.getSelectedTableRow().getPrimaryKey());
                    if (fileProcess.getSuppFiles().isEmpty()) {
                        miClient.showMsgBoxWarning("Este documento no tiene archivos de soporte anexados.");
                    }
                    else{
                        if (moFileChooserDownload.showSaveDialog(miClient.getFrame()) == JFileChooser.APPROVE_OPTION) {
                            int cant = 0;
                            for (SDbSupplierFile file : fileProcess.getSuppFiles()) {
                                if (!file.getFilevaultId().isEmpty()) {
                                    boolean returnPath = false;
                                    SDocUtils.downloadFile(miClient.getSession(), SDocUtils.BUCKET_DOC_DPS_SUPPLIER, file.getFilevaultId(), moFileChooserDownload.getSelectedFile(), returnPath);
                                    cant++;
                                }
                            }
                            
                            miClient.showMsgBoxInformation("Se descargaron un total de " + cant + " archivos de soporte.");
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            miClient.showMsgBoxWarning(e.getMessage());
        }
    }
    
    private void actionAll() {
        tfStatus.setText("TODOS");
        mnShow = SDataConstantsSys.CFGS_ST_AUTHORN_NA;
        this.actionReload();
    }
    
    private void actionAuth() {
        tfStatus.setText("AUTORIZADO");
        mnShow = SDataConstantsSys.CFGS_ST_AUTHORN_AUTH;
        this.actionReload();
    }
    
    private void actionRej() {
        tfStatus.setText("RECHAZADO");
        mnShow = SDataConstantsSys.CFGS_ST_AUTHORN_REJ;
        this.actionReload();
    }
    
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            if (button == jbAddFileSupp) {
                actionAddFileSupp();
            }
            if (button == jbAnullAuth) {
                actionRestartAuth();
            }
            else if (button == jbDownFileSupp) {
                actionDownFileSupp();
            }
            else if (button == jbAll) {
                actionAll();
            }
            else if (button == jbAuth) {
                actionAuth();
            }
            else if (button == jbRej) {
                actionRej();
            }
        }
    }
}
