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
import erp.mod.cfg.db.SDbDocument;
import erp.mod.hrs.db.SDbDocBreach;
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
public class SViewDocBreach extends SGridPaneView implements ActionListener {
    
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

    public SViewDocBreach(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.HRS_DOC_BREACH, 0, title);
        initComponents();
    }
    
    private void initComponents() {
        int levelRightDocBreach = ((SClientInterface) miClient).getSessionXXX().getUser().hasRight((SClientInterface) miClient, SDataConstantsSys.PRV_HRS_DOC_BREACH).Level;
        boolean enableNew = levelRightDocBreach >= SUtilConsts.LEV_CAPTURE;
        boolean enableCopy = levelRightDocBreach >= SUtilConsts.LEV_CAPTURE;
        boolean enableEdit = levelRightDocBreach >= SUtilConsts.LEV_CAPTURE;
        boolean enableDelete = levelRightDocBreach >= SUtilConsts.LEV_MANAGER;
        
        setRowButtonsEnabled(enableNew, enableCopy, enableEdit, false, enableDelete);
        
        if (levelRightDocBreach == SUtilConsts.LEV_CAPTURE || levelRightDocBreach == SUtilConsts.LEV_AUTHOR) {
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
            sql += (sql.isEmpty() ? "" : "AND ") + "db.b_del = 0 ";
        }

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_YEAR).getValue();
        if (filter != null && filter instanceof int[]) {
            sql += (sql.isEmpty() ? "" : "AND ") + "YEAR(db.breach_ts) = " + ((int[]) filter)[0] + " ";
        }

        filter = ((SGridFilterValue) moFiltersMap.get(SModConsts.HRSS_TP_PAY)).getValue();
        if (filter != null && ((int[]) filter).length == 1) {
            sql += (sql.isEmpty() ? "" : "AND ") + "emp.fk_tp_pay = " + ((int[]) filter)[0] + " ";
        }
        
        filter = ((SGridFilterValue) moFiltersMap.get(SModConsts.HRSU_DEP)).getValue();
        if (filter != null && ((int[]) filter).length == 1) {
            sql += (sql.isEmpty() ? "" : "AND ") + "db.fk_offender_dep = " + ((int[]) filter)[0] + " ";
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
            sql += (sql.isEmpty() ? "" : "AND ") + "db.fk_usr_ins = " + mnFilterAuthorId + " ";
        }
        
        msSql = "SELECT "
                + "db.id_doc_breach AS " + SDbConsts.FIELD_ID + "1, "
                + "db.num, "
                + "db.num AS " + SDbConsts.FIELD_CODE + ", "
                + "db.num AS " + SDbConsts.FIELD_NAME + ", "
                + "db.breach_ts, "
                + "db.breach_abstract, "
                + "db.ending_notes, "
                + "db.filevault_id, "
                + "db.filevault_ts_n, "
                + "db.file_type, "
                + "db.b_offender_uni, "
                + "db.b_offender_sign, "
                + "cob.code AS _cob_code, "
                + "ba.bp AS _emp_author, "
                + "bo.bp AS _emp_offender, "
                + "bb.bp AS _emp_boss, "
                + "bu.bp AS _emp_union_n, "
                + "emp.num, "
                + "emp.b_act, "
                + "dep.name AS _dep_name, "
                + "pos.name AS _pos_name, "
                + "db.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "db.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "db.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "db.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "db.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_DOC_BREACH) + " AS db "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS cob ON cob.id_bpb = db.fk_cob "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS ba ON ba.id_bp = db.fk_emp_author "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bo ON bo.id_bp = db.fk_emp_offender "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bb ON bb.id_bp = db.fk_emp_boss "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS emp ON emp.id_emp = db.fk_emp_offender "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_DEP) + " AS dep ON dep.id_dep = db.fk_offender_dep "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_POS) + " AS pos ON pos.id_pos = db.fk_offender_pos "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON db.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON db.fk_usr_upd = uu.id_usr "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bu ON bu.id_bp = db.fk_emp_union_n "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY db.num, db.id_doc_breach ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_RAW, "db.num", "Folio"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "_cob_code", "Sucursal"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "db.breach_ts", "Fecha-hr"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "_emp_offender", "Empleado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_BPR, "emp.num", "Clave"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "emp.b_act", "Activo empleado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "_dep_name", "Depto. empleado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "_pos_name", "Puesto empleado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "db.b_offender_uni", "Sindicalizado empleado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "db.b_offender_sign", "El empleado firmó"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "db.breach_abstract", "Falta cometida"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "db.ending_notes", "Notas finales"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "_emp_author", "Reporta"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "_emp_boss", "Jefe inmediato"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "_emp_union_n", "Rep. sindical"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "db.filevault_id", "ID documento"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "db.filevault_ts_n", "TS documento"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "db.file_type", "Tipo archivo"));
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
                SDbDocBreach docBreach = (SDbDocBreach) miClient.getSession().readRegistry(SModConsts.HRS_DOC_BREACH, gridRow.getRowPrimaryKey());
                SDbDocument document = (SDbDocument) miClient.getSession().readRegistry(SModConsts.CFGU_DOC, new int[] { docBreach.getFkDocumentId() });
                
                HashMap<String, Object> paramsMap = miClient.createReportParams();
                paramsMap.put("docId", (long) docBreach.getPkDocBreachId());
                paramsMap.put("docCode", document.getCode());
                paramsMap.put("docName", document.getName());
                paramsMap.put("docNotes", document.getNotes());
                paramsMap.put("docPreceptSubsections", SDocUtils.composePreceptSubsections(miClient.getSession(), docBreach.getPreceptSubsectionKeys(), " "));
                
                miClient.getSession().printReport(SModConsts.HRSR_DOC_BREACH, 0, null, paramsMap);
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
                SDbDocBreach docBreach = (SDbDocBreach) miClient.getSession().readRegistry(SModConsts.HRS_DOC_BREACH, gridRow.getRowPrimaryKey());
                
                if (!docBreach.getFilevaultId().isEmpty()) {
                    miClient.showMsgBoxWarning("Este documento ya tiene un archivo (ID: '" + docBreach.getFilevaultId() + "', TS: '" + SLibUtils.DateFormatDatetime.format(docBreach.getFilevaultTs_n()) + "').");
                }
                else {
                    moFileChooserUpload.setSelectedFile(new File("")); // clear previous selected file
                    
                    if (moFileChooserUpload.showOpenDialog(miClient.getFrame()) == JFileChooser.APPROVE_OPTION) {
                        try {
                            File file = moFileChooserUpload.getSelectedFile();
                            String fileType = SDocUtils.isPdf(file) ? SDocUtils.FILE_TYPE_PDF : SDocUtils.FILE_TYPE_IMG;
                            String filevaultId = SDocUtils.uploadFile(miClient.getSession(), SDocUtils.BUCKET_DOC_BREACH, fileType, file);
                            docBreach.setFilevaultId(filevaultId);
                            docBreach.setFileType(fileType);
                            docBreach.save(miClient.getSession());
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
                SDbDocBreach docBreach = (SDbDocBreach) miClient.getSession().readRegistry(SModConsts.HRS_DOC_BREACH, gridRow.getRowPrimaryKey());
                String filevaultId = docBreach.getFilevaultId();
                
                if (filevaultId.isEmpty()) {
                    miClient.showMsgBoxWarning("Este documento no tiene archivo.");
                }
                else {
                    if (moFileChooserDownload.showSaveDialog(miClient.getFrame()) == JFileChooser.APPROVE_OPTION) {
                        try {
                            String filename = SDocUtils.downloadFile(miClient.getSession(), SDocUtils.BUCKET_DOC_BREACH, filevaultId, moFileChooserDownload.getSelectedFile());
                            
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
                    SDbDocBreach docBreach = (SDbDocBreach) miClient.getSession().readRegistry(SModConsts.HRS_DOC_BREACH, gridRow.getRowPrimaryKey());
                    SDocUtils.viewFile(miClient, SDocUtils.BUCKET_DOC_BREACH, docBreach.getFilevaultId());
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
                SDbDocBreach docBreach = (SDbDocBreach) miClient.getSession().readRegistry(SModConsts.HRS_DOC_BREACH, gridRow.getRowPrimaryKey());
                String filevaultId = docBreach.getFilevaultId();
                
                if (filevaultId.isEmpty()) {
                    miClient.showMsgBoxWarning("Este documento no tiene archivo.");
                }
                else {
                    if (miClient.showMsgBoxConfirm("¿Está seguro que desea eliminar el archivo del documento (ID: '" + filevaultId + "')?") == JOptionPane.YES_OPTION) {
                        try {
                            String filename = SDocUtils.deleteFile(miClient.getSession(), SDocUtils.BUCKET_DOC_BREACH, filevaultId);
                            docBreach.setFilevaultId("");
                            docBreach.setFileType("");
                            docBreach.save(miClient.getSession());
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
