/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.view;

import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.gui.grid.SGridFilterPanelEmployee;
import erp.lib.SLibConstants;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.cfg.db.SDbDocument;
import erp.mod.hrs.db.SDbDocAdminRecord;
import erp.mod.hrs.utils.SDocUtils;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterValue;
import sa.lib.grid.SGridFilterYear;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridRowView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;

/**
 *
 * @author Sergio Flores
 */
public class SViewDocAdminRecord extends SGridPaneView implements ActionListener {
    
    private int mnFilterAuthorId;
    private JFileChooser moFileChooserUpload;
    private JFileChooser moFileChooserDownload;
    private SGridFilterYear moFilterYear;
    private SGridFilterPanelEmployee moFilterEmployee;
    private JButton jbPrint;
    private JButton jbFileUpload;
    private JButton jbFileDownload;
    private JButton jbFileView;
    private JButton jbFileDelete;

    public SViewDocAdminRecord(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.HRS_DOC_ADM_REC, 0, title);
        initComponents();
    }
    
    private void initComponents() {
        int levelRightDocAdminRecord = ((SClientInterface) miClient).getSessionXXX().getUser().hasRight((SClientInterface) miClient, SDataConstantsSys.PRV_HRS_DOC_ADM_REC).Level;
        boolean enableNew = levelRightDocAdminRecord >= SUtilConsts.LEV_CAPTURE;
        boolean enableCopy = levelRightDocAdminRecord >= SUtilConsts.LEV_CAPTURE;
        boolean enableEdit = levelRightDocAdminRecord >= SUtilConsts.LEV_CAPTURE;
        boolean enableDelete = levelRightDocAdminRecord >= SUtilConsts.LEV_MANAGER;
        
        setRowButtonsEnabled(enableNew, enableCopy, enableEdit, false, enableDelete);
        
        if (levelRightDocAdminRecord == SUtilConsts.LEV_CAPTURE || levelRightDocAdminRecord == SUtilConsts.LEV_AUTHOR) {
            mnFilterAuthorId = miClient.getSession().getUser().getPkUserId();
            
            JTextField author = new JTextField(miClient.getSession().getUser().getName());
            author.setEditable(false);
            author.setFocusable(false);
            author.setPreferredSize(new Dimension(100, 23));
            author.setToolTipText("Autor");
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(author);
        }
        else {
            mnFilterAuthorId = 0;
        }
        
        moFileChooserUpload = new JFileChooser();
        moFileChooserUpload.setAcceptAllFileFilterUsed(false);
        moFileChooserUpload.addChoosableFileFilter(new FileNameExtensionFilter("Documento PDF (*.pdf)", "pdf"));
        moFileChooserUpload.addChoosableFileFilter(new FileNameExtensionFilter("Archivo de imagen (*.bpm, *.gif, *.png, *.jpg, *.jpeg)", "bmp", "gif", "png", "jpg", "jpeg"));
        moFileChooserUpload.setDialogTitle("Seleccionar archivo a cargar...");
        
        moFileChooserDownload = new JFileChooser();
        moFileChooserDownload.setAcceptAllFileFilterUsed(false);
        moFileChooserDownload.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        moFileChooserDownload.setDialogTitle("Seleccionar directorio para descargar archivo...");
        
        moFilterYear = new SGridFilterYear(miClient, this);
        moFilterYear.initFilter(null);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterYear);
        
        moFilterEmployee = new SGridFilterPanelEmployee(miClient, this);
        moFilterEmployee.initFilter(SGridFilterPanelEmployee.EMP_STATUS_ACT);
        getPanelCommandsCustom(SGuiConsts.PANEL_LEFT).add(moFilterEmployee);
        
        jbPrint = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_PRINT), "Imprimir documento", this);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPrint);
        
        jbFileUpload = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_doc_add.gif")), "Cargar archivo documento", this);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbFileUpload);
        
        jbFileDownload = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_save.gif")), "Descargar archivo documento", this);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbFileDownload);
        
        jbFileView = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_look.gif")), "Ver archivo documento", this);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbFileView);
        
        jbFileDelete = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_DELETE), "Eliminar archivo documento", this);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbFileDelete);
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "dar.b_del = 0 ";
        }

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_YEAR).getValue();
        if (filter != null && filter instanceof int[]) {
            sql += (sql.isEmpty() ? "" : "AND ") + "YEAR(dar.rec_dt_sta) = " + ((int[]) filter)[0] + " ";
        }

        filter = ((SGridFilterValue) moFiltersMap.get(SModConsts.HRSS_TP_PAY)).getValue();
        if (filter != null && ((int[]) filter).length == 1) {
            sql += (sql.isEmpty() ? "" : "AND ") + "emp.fk_tp_pay = " + ((int[]) filter)[0] + " ";
        }
        
        filter = ((SGridFilterValue) moFiltersMap.get(SModConsts.HRSU_DEP)).getValue();
        if (filter != null && ((int[]) filter).length == 1) {
            sql += (sql.isEmpty() ? "" : "AND ") + "dar.fk_offender_dep = " + ((int[]) filter)[0] + " ";
        }
        
        filter = ((SGridFilterValue) moFiltersMap.get(SGridFilterPanelEmployee.EMP_STATUS)).getValue();
        if (filter != null && ((int) filter) != SLibConsts.UNDEFINED) {
            if ((int)filter == SGridFilterPanelEmployee.EMP_STATUS_ACT) {
                sql += (sql.isEmpty() ? "" : "AND ") + "emp.b_act = 1 ";
            }
            else if ((int)filter == SGridFilterPanelEmployee.EMP_STATUS_INA) {
                sql += (sql.isEmpty() ? "" : "AND ") + "emp.b_act = 0 ";
            }
            else if ((int)filter == SGridFilterPanelEmployee.EMP_STATUS_ALL) {
                
            }
        }
        
        if (mnFilterAuthorId != 0) {
            sql += (sql.isEmpty() ? "" : "AND ") + "dar.fk_usr_ins = " + mnFilterAuthorId + " ";
        }
        
        msSql = "SELECT "
                + "dar.id_doc_adm_rec AS " + SDbConsts.FIELD_ID + "1, "
                + "dar.num, "
                + "dar.num AS " + SDbConsts.FIELD_CODE + ", "
                + "dar.num AS " + SDbConsts.FIELD_NAME + ", "
                + "dar.rec_dt_sta, "
                + "dar.rec_dt_end, "
                + "dar.breach_abstract, "
                + "dar.ending_notes, "
                + "dar.filevault_id, "
                + "dar.filevault_ts_n, "
                + "dar.file_type, "
                + "dar.b_offender_uni, "
                + "dar.b_offender_sign, "
                + "dar.b_offender_dis, "
                + "dar.b_hrs_witness_1, "
                + "dar.b_hrs_witness_2, "
                + "cob.code AS _cob_code, "
                + "bo.bp AS _emp_offender, "
                + "bb.bp AS _emp_boss, "
                + "bh.bp AS _emp_hrs, "
                + "bu.bp AS _emp_union_n, "
                + "bw1.bp AS _emp_witness_1, "
                + "bw2.bp AS _emp_witness_2, "
                + "emp.num, "
                + "emp.b_act, "
                + "dep.name AS _dep_name, "
                + "pos.name AS _pos_name, "
                + "dar.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "dar.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "dar.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "dar.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "dar.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_DOC_ADM_REC) + " AS dar "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS cob ON cob.id_bpb = dar.fk_cob "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bo ON bo.id_bp = dar.fk_emp_offender "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bb ON bb.id_bp = dar.fk_emp_boss "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bh ON bh.id_bp = dar.fk_emp_hrs "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bw1 ON bw1.id_bp = dar.fk_emp_witness_1 "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bw2 ON bw2.id_bp = dar.fk_emp_witness_2 "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS emp ON emp.id_emp = dar.fk_emp_offender "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_DEP) + " AS dep ON dep.id_dep = dar.fk_offender_dep "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_POS) + " AS pos ON pos.id_pos = dar.fk_offender_pos "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON dar.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON dar.fk_usr_upd = uu.id_usr "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bu ON bu.id_bp = dar.fk_emp_union_n "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY dar.num, dar.id_doc_adm_rec ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_RAW, "dar.num", "Folio"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "_cob_code", "Sucursal"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "dar.rec_dt_sta", "Fecha-hr inicial"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "_emp_offender", "Empleado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_BPR, "emp.num", "Clave"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "emp.b_act", "Activo empleado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "_dep_name", "Depto. empleado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "_pos_name", "Puesto empleado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "dar.b_offender_uni", "Sindicalizado empleado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "dar.b_offender_sign", "El empleado firmó"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "dar.b_offender_dis", "El empleado se desvinculó"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "dar.breach_abstract", "Falta cometida"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "dar.ending_notes", "Notas finales"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "_emp_boss", "Jefe inmediato"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "_emp_hrs", "Rep. patronal"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "_emp_union_n", "Rep. sindical"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "_emp_witness_1", "Testigo #1"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "dar.b_hrs_witness_1", "Testigo #1 es RRHH"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "_emp_witness_2", "Testigo #2"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "dar.b_hrs_witness_2", "Testigo #2 es RRHH"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "dar.filevault_id", "ID documento"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "dar.filevault_ts_n", "TS documento"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "dar.file_type", "Tipo archivo"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS));

        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.HRSU_EMP);
        moSuscriptionsSet.add(SModConsts.HRSU_DEP);
        moSuscriptionsSet.add(SModConsts.HRSU_POS);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }
    
    private void actionPerformedPrint() {
        if (jbPrint.isEnabled()) {
            SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
            
            if (gridRow == null) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                try {
                    SDbDocAdminRecord docAdminRecord = (SDbDocAdminRecord) miClient.getSession().readRegistry(SModConsts.HRS_DOC_ADM_REC, gridRow.getRowPrimaryKey());
                    SDbDocument document = (SDbDocument) miClient.getSession().readRegistry(SModConsts.CFGU_DOC, new int[] { docAdminRecord.getFkDocumentId() });

                    HashMap<String, Object> paramsMap = miClient.createReportParams();
                    paramsMap.put("docId", (long) docAdminRecord.getPkDocAdminRecordId());
                    paramsMap.put("docCode", document.getCode());
                    paramsMap.put("docName", document.getName());
                    paramsMap.put("docNotes", document.getNotes());
                    paramsMap.put("docPreceptSubsections", SDocUtils.composePreceptSubsections(miClient.getSession(), docAdminRecord.getPreceptSubsectionKeys(), " "));

                    String value;
                    HashMap<String, String> docDataValuesMap = SDocUtils.getDocDataValuesMap(miClient.getSession(), SModSysConsts.CFGS_TP_DOC_ADM_REC);
                    
                    value = docDataValuesMap.get(SDocUtils.VAL_DOC_ADM_REC_ORG);
                    if (value != null) {
                        paramsMap.put("termOrg", value);
                        paramsMap.put("termOrgU", SLibUtils.textFirstUpperCase(value));
                    }
                    
                    value = docDataValuesMap.get(SDocUtils.VAL_DOC_ADM_REC_PRON);
                    if (value != null) {
                        paramsMap.put("termPronoun", value);
                        paramsMap.put("termPronounU", SLibUtils.textFirstUpperCase(value));
                    }
                    
                    value = docDataValuesMap.get(SDocUtils.VAL_DOC_ADM_REC_EMP);
                    if (value != null) {
                        paramsMap.put("termEmployee", value);
                        paramsMap.put("termEmployeeU", SLibUtils.textFirstUpperCase(value));
                    }
                    
                    value = docDataValuesMap.get(SDocUtils.VAL_DOC_ADM_REC_BOSS);
                    if (value != null) {
                        paramsMap.put("termBoss", value);
                        paramsMap.put("termBossU", SLibUtils.textFirstUpperCase(value));
                    }
                    
                    value = docDataValuesMap.get(SDocUtils.VAL_DOC_ADM_REC_LIAB);
                    if (value != null) {
                        paramsMap.put("termLiable", value);
                        paramsMap.put("termLiableU", SLibUtils.textFirstUpperCase(value));
                    }
                    
                    value = docDataValuesMap.get(SDocUtils.VAL_DOC_ADM_REC_REP);
                    if (value != null) {
                        paramsMap.put("termRepresent", value);
                        paramsMap.put("termRepresentU", SLibUtils.textFirstUpperCase(value));
                    }
                    
                    value = docDataValuesMap.get(SDocUtils.VAL_DOC_ADM_REC_WIT);
                    if (value != null) {
                        paramsMap.put("termWitness", value);
                        paramsMap.put("termWitnessU", SLibUtils.textFirstUpperCase(value));
                    }
                    
                    miClient.getSession().printReport(SModConsts.HRSR_DOC_ADM_REC, 0, null, paramsMap);
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }
        }
    }

    private void actionPerformedFileUpload() {
        if (jbFileUpload.isEnabled()) {
            SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
            
            if (gridRow == null) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SDbDocAdminRecord docAdminRecord = (SDbDocAdminRecord) miClient.getSession().readRegistry(SModConsts.HRS_DOC_ADM_REC, gridRow.getRowPrimaryKey());
                
                if (!docAdminRecord.getFilevaultId().isEmpty()) {
                    miClient.showMsgBoxWarning("Este documento ya tiene un archivo (ID: '" + docAdminRecord.getFilevaultId() + "', TS: '" + SLibUtils.DateFormatDatetime.format(docAdminRecord.getFilevaultTs_n()) + "').");
                }
                else {
                    moFileChooserUpload.setSelectedFile(new File("")); // clear previous selected file
                    
                    if (moFileChooserUpload.showOpenDialog(miClient.getFrame()) == JFileChooser.APPROVE_OPTION) {
                        try {
                            File file = moFileChooserUpload.getSelectedFile();
                            String fileType = SDocUtils.isPdf(file) ? SDocUtils.FILE_TYPE_PDF : SDocUtils.FILE_TYPE_IMG;
                            String filevaultId = SDocUtils.uploadFile(miClient.getSession(), SDocUtils.BUCKET_DOC_ADM_REC, fileType, file);
                            docAdminRecord.setFilevaultId(filevaultId);
                            docAdminRecord.setFileType(fileType);
                            docAdminRecord.save(miClient.getSession());
                            miClient.getSession().notifySuscriptors(mnGridType);
                            
                            miClient.showMsgBoxInformation("El archivo del documento '" + file.getName() + "' (ID: '" + filevaultId + "') ha sido cargado.");
                        }
                        catch (Exception e) {
                            SLibUtils.showException(this, e);
                        }
                    }
                }
            }
        }
    }
    
    private void actionPerformedFileDownload() {
        if (jbFileDownload.isEnabled()) {
            SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
            
            if (gridRow == null) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SDbDocAdminRecord docAdminRecord = (SDbDocAdminRecord) miClient.getSession().readRegistry(SModConsts.HRS_DOC_ADM_REC, gridRow.getRowPrimaryKey());
                String filevaultId = docAdminRecord.getFilevaultId();
                
                if (filevaultId.isEmpty()) {
                    miClient.showMsgBoxWarning("Este documento no tiene archivo.");
                }
                else {
                    if (moFileChooserDownload.showSaveDialog(miClient.getFrame()) == JFileChooser.APPROVE_OPTION) {
                        try {
                            boolean returnPath = false;
                            String filename = SDocUtils.downloadFile(miClient.getSession(), SDocUtils.BUCKET_DOC_ADM_REC, filevaultId, moFileChooserDownload.getSelectedFile(), returnPath);
                            
                            miClient.showMsgBoxInformation("El archivo del documento '" + filename + "' (ID: '" + filevaultId + "') ha sido descargado.");
                        }
                        catch (Exception e) {
                            SLibUtils.showException(this, e);
                        }
                    }
                }
            }
        }
    }
    
    private void actionPerformedFileView() {
        if (jbFileView.isEnabled()) {
            SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
            
            if (gridRow == null) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                try {
                    SDbDocAdminRecord docAdminRecord = (SDbDocAdminRecord) miClient.getSession().readRegistry(SModConsts.HRS_DOC_ADM_REC, gridRow.getRowPrimaryKey());
                    SDocUtils.viewFile(miClient, SDocUtils.BUCKET_DOC_ADM_REC, docAdminRecord.getFilevaultId());
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }
        }
    }
    
    private void actionPerformedFileDelete() {
        if (jbFileDelete.isEnabled()) {
            SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
            
            if (gridRow == null) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SDbDocAdminRecord docAdminRecord = (SDbDocAdminRecord) miClient.getSession().readRegistry(SModConsts.HRS_DOC_ADM_REC, gridRow.getRowPrimaryKey());
                String filevaultId = docAdminRecord.getFilevaultId();
                
                if (filevaultId.isEmpty()) {
                    miClient.showMsgBoxWarning("Este documento no tiene archivo.");
                }
                else {
                    if (miClient.showMsgBoxConfirm("¿Está seguro que desea eliminar el archivo del documento (ID: '" + filevaultId + "')?") == JOptionPane.YES_OPTION) {
                        try {
                            String filename = SDocUtils.deleteFile(miClient.getSession(), SDocUtils.BUCKET_DOC_ADM_REC, filevaultId);
                            docAdminRecord.setFilevaultId("");
                            docAdminRecord.setFileType("");
                            docAdminRecord.save(miClient.getSession());
                            miClient.getSession().notifySuscriptors(mnGridType);
                            
                            miClient.showMsgBoxInformation("El archivo del documento '" + filename + "' (ID: '" + filevaultId + "') ha sido eliminado.");
                        }
                        catch (Exception e) {
                            SLibUtils.showException(this, e);
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == jbPrint) {
                actionPerformedPrint();
            }
            else if (button == jbFileUpload) {
                actionPerformedFileUpload();
            }
            else if (button == jbFileDownload) {
                actionPerformedFileDownload();
            }
            else if (button == jbFileView) {
                actionPerformedFileView();
            }
            else if (button == jbFileDelete) {
                actionPerformedFileDelete();
            }
        }
    }
}
