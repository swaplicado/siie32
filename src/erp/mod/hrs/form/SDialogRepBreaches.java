/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.form;

import com.fasterxml.jackson.databind.ObjectMapper;
import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.lib.SLibUtilities;
import erp.mcfg.data.SCfgUtils;
import erp.mcfg.data.SDataCompany;
import erp.mod.SModSysConsts;
import erp.mod.cfg.db.SDbMms;
import erp.mod.hrs.db.SRepBreachesTo;
import erp.mtrn.data.STrnUtilities;
import static erp.mtrn.data.STrnUtilities.getMms;
import erp.musr.data.SDataAccessCompany;
import erp.musr.data.SDataUser;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JDialog;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiFields;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.mail.SMail;
import sa.lib.mail.SMailConsts;
import sa.lib.mail.SMailSender;

/**
 *
 * @author Isabel Servín, Sergio Flores
 */
public class SDialogRepBreaches extends JDialog implements ActionListener {

    private static final SimpleDateFormat moDateFormatDate = new SimpleDateFormat("dd-MMM-yy");
    
    private final SGuiClient miClient;
    private Vector<SDataCompany> moVecCompanies;
    private XSSFWorkbook moWorkbook;
    private Row moExcelRow;
    private Cell moExcelCell;
    private SGuiFields moFields;
    private ObjectMapper moMapper;
    private SMailSender moMailSender;
    
    /**
     * Creates new form SDialogRepBreaches
     * @param client
     */
    public SDialogRepBreaches(SGuiClient client) {
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jlDate = new javax.swing.JLabel();
        moDate = new sa.lib.gui.bean.SBeanFieldDate();
        jPanel6 = new javax.swing.JPanel();
        jlTo = new javax.swing.JLabel();
        moTextTo = new sa.lib.gui.bean.SBeanFieldText();
        jlAddingMultipleMailHelp = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jlCc = new javax.swing.JLabel();
        moTextCc = new sa.lib.gui.bean.SBeanFieldText();
        jlAddingMultipleMailHelp1 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jlBcc = new javax.swing.JLabel();
        moTextBcc = new sa.lib.gui.bean.SBeanFieldText();
        jlAddingMultipleMailHelp2 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        moBooleanSaveEmails = new sa.lib.gui.bean.SBeanFieldBoolean();
        jPanel10 = new javax.swing.JPanel();
        jlReportCompanies = new javax.swing.JLabel();
        moRadThisCompany = new sa.lib.gui.bean.SBeanFieldRadio();
        moRadAccesCompany = new sa.lib.gui.bean.SBeanFieldRadio();
        moRadCustom = new sa.lib.gui.bean.SBeanFieldRadio();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jListCompanies = new javax.swing.JList();
        jPanel11 = new javax.swing.JPanel();
        jlNote = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jbSend = new javax.swing.JButton();
        jbClose = new javax.swing.JButton();

        setTitle("Reporte de consecuencias");
        setIconImage(null);
        setIconImages(null);
        setModal(true);
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Parámetros del reporte:"));
        jPanel1.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel2.setLayout(new java.awt.GridLayout(6, 0, 0, 5));

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDate.setText("Fecha de corte:*");
        jlDate.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel3.add(jlDate);
        jPanel3.add(moDate);

        jPanel2.add(jPanel3);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTo.setForeground(new java.awt.Color(0, 102, 102));
        jlTo.setText("Para:*");
        jlTo.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel6.add(jlTo);

        moTextTo.setPreferredSize(new java.awt.Dimension(450, 23));
        jPanel6.add(moTextTo);

        jlAddingMultipleMailHelp.setForeground(new java.awt.Color(109, 109, 109));
        jlAddingMultipleMailHelp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlAddingMultipleMailHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_view_help.png"))); // NOI18N
        jlAddingMultipleMailHelp.setToolTipText("Separar varias cuentas con \";\", sin espacios, p. ej., \"cuenta1@mail.com;cuenta2@mail.com\"");
        jlAddingMultipleMailHelp.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel6.add(jlAddingMultipleMailHelp);

        jPanel2.add(jPanel6);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCc.setForeground(new java.awt.Color(0, 102, 102));
        jlCc.setText("CC:");
        jlCc.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel7.add(jlCc);

        moTextCc.setPreferredSize(new java.awt.Dimension(450, 23));
        jPanel7.add(moTextCc);

        jlAddingMultipleMailHelp1.setForeground(new java.awt.Color(109, 109, 109));
        jlAddingMultipleMailHelp1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlAddingMultipleMailHelp1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_view_help.png"))); // NOI18N
        jlAddingMultipleMailHelp1.setToolTipText("Separar varias cuentas con \";\", sin espacios, p. ej., \"cuenta1@mail.com;cuenta2@mail.com\"");
        jlAddingMultipleMailHelp1.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel7.add(jlAddingMultipleMailHelp1);

        jPanel2.add(jPanel7);

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBcc.setForeground(new java.awt.Color(0, 102, 102));
        jlBcc.setText("BCC:");
        jlBcc.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel8.add(jlBcc);

        moTextBcc.setPreferredSize(new java.awt.Dimension(450, 23));
        jPanel8.add(moTextBcc);

        jlAddingMultipleMailHelp2.setForeground(new java.awt.Color(109, 109, 109));
        jlAddingMultipleMailHelp2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlAddingMultipleMailHelp2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_view_help.png"))); // NOI18N
        jlAddingMultipleMailHelp2.setToolTipText("Separar varias cuentas con \";\", sin espacios, p. ej., \"cuenta1@mail.com;cuenta2@mail.com\"");
        jlAddingMultipleMailHelp2.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel8.add(jlAddingMultipleMailHelp2);

        jPanel2.add(jPanel8);

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        moBooleanSaveEmails.setSelected(true);
        moBooleanSaveEmails.setText("Guardar destinatarios en la configuración del reporte");
        moBooleanSaveEmails.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel9.add(moBooleanSaveEmails);

        jPanel2.add(jPanel9);

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlReportCompanies.setText("Reporte de:");
        jPanel10.add(jlReportCompanies);

        buttonGroup1.add(moRadThisCompany);
        moRadThisCompany.setSelected(true);
        moRadThisCompany.setText("Empresa actual");
        moRadThisCompany.setToolTipText("");
        moRadThisCompany.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel10.add(moRadThisCompany);

        buttonGroup1.add(moRadAccesCompany);
        moRadAccesCompany.setText("Mis empresas con acceso");
        moRadAccesCompany.setToolTipText("");
        moRadAccesCompany.setPreferredSize(new java.awt.Dimension(190, 23));
        jPanel10.add(moRadAccesCompany);

        buttonGroup1.add(moRadCustom);
        moRadCustom.setText("Selección personalizada");
        moRadCustom.setToolTipText("");
        moRadCustom.setPreferredSize(new java.awt.Dimension(190, 23));
        jPanel10.add(moRadCustom);

        jPanel2.add(jPanel10);

        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Seleccionar empresas con Módulo Recursos Humanos:"));
        jPanel5.setLayout(new java.awt.BorderLayout());

        jScrollPane2.setViewportView(jListCompanies);

        jPanel5.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jlNote.setText("Para seleccionar varias opciones mantenga presionada la tecla Ctrl");
        jlNote.setEnabled(false);
        jPanel11.add(jlNote);

        jPanel5.add(jPanel11, java.awt.BorderLayout.NORTH);

        jPanel1.add(jPanel5, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbSend.setText("Enviar");
        jbSend.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel4.add(jbSend);

        jbClose.setText("Cerrar");
        jbClose.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel4.add(jbClose);

        getContentPane().add(jPanel4, java.awt.BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JList jListCompanies;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton jbClose;
    private javax.swing.JButton jbSend;
    private javax.swing.JLabel jlAddingMultipleMailHelp;
    private javax.swing.JLabel jlAddingMultipleMailHelp1;
    private javax.swing.JLabel jlAddingMultipleMailHelp2;
    private javax.swing.JLabel jlBcc;
    private javax.swing.JLabel jlCc;
    private javax.swing.JLabel jlDate;
    private javax.swing.JLabel jlNote;
    private javax.swing.JLabel jlReportCompanies;
    private javax.swing.JLabel jlTo;
    private sa.lib.gui.bean.SBeanFieldBoolean moBooleanSaveEmails;
    private sa.lib.gui.bean.SBeanFieldDate moDate;
    private sa.lib.gui.bean.SBeanFieldRadio moRadAccesCompany;
    private sa.lib.gui.bean.SBeanFieldRadio moRadCustom;
    private sa.lib.gui.bean.SBeanFieldRadio moRadThisCompany;
    private sa.lib.gui.bean.SBeanFieldText moTextBcc;
    private sa.lib.gui.bean.SBeanFieldText moTextCc;
    private sa.lib.gui.bean.SBeanFieldText moTextTo;
    // End of variables declaration//GEN-END:variables

    @SuppressWarnings("unchecked")
    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 640, 400);
        
        moFields = new SGuiFields();
        moMapper = new ObjectMapper();
        
        moDate.setDateSettings(miClient, SGuiUtils.getLabelName(jlDate.getText()), true);
        moTextTo.setTextSettings(SGuiUtils.getLabelName(jlTo), 500, 1);
        moTextTo.setTextCaseType(0);
        moTextCc.setTextSettings(SGuiUtils.getLabelName(jlCc), 500, 0);
        moTextCc.setTextCaseType(0);
        moTextBcc.setTextSettings(SGuiUtils.getLabelName(jlBcc), 500, 0);
        moTextBcc.setTextCaseType(0);
        moBooleanSaveEmails.setBooleanSettings(moBooleanSaveEmails.getText(), true);
        
        moFields.addField(moDate);
        moFields.addField(moTextTo);
        moFields.addField(moTextCc);
        moFields.addField(moTextBcc);
        moFields.addField(moBooleanSaveEmails);
        
        try {
            String to = SCfgUtils.getParamValue(miClient.getSession().getStatement(), SDataConstantsSys.CFG_PARAM_DOC_HRS_MAIL_BREACH_TO);
            SRepBreachesTo oTo = moMapper.readValue(to, SRepBreachesTo.class);
            moTextTo.setValue(oTo.getTo());
            moTextCc.setValue(oTo.getCc());
            moTextBcc.setValue(oTo.getBcc());
        }
        catch (Exception e) {
            miClient.showMsgBoxError(e.getMessage());
        }
            
        moDate.setValue(miClient.getSession().getCurrentDate());
        
        moVecCompanies = new Vector<>();
        Vector<String> modeloLista = new Vector<>();
        SDataUser user = new SDataUser();
        user.read(new int[] { miClient.getSession().getUser().getPkUserId() }, miClient.getSession().getStatement());
        if (user.isSupervisor()) {
            try {    
                String sql = "SELECT id_co FROM erp.cfgu_co WHERE NOT b_del AND b_mod_hrs";
                ResultSet resultSet = miClient.getSession().getDatabase().getConnection().createStatement().executeQuery(sql);
                while (resultSet.next()) {
                    SDataCompany c = new SDataCompany();
                    c.read(new int[] { resultSet.getInt(1) }, miClient.getSession().getStatement());
                    moVecCompanies.addElement(c);
                    modeloLista.add(c.getCompany());
                }
            }
            catch (Exception e) {
                miClient.showMsgBoxError(e.getMessage());
            }
        } 
        else {
            for (SDataAccessCompany accCom : user.getDbmsAccessCompanies()) {
                SDataCompany c = new SDataCompany();
                c.read(new int[] {accCom.getPkCompanyId()}, miClient.getSession().getStatement());
                if (c.getIsModuleHrs() && !c.getIsDeleted()) {
                    moVecCompanies.addElement(c);
                    modeloLista.add(c.getCompany());
                }
            }
        }
        
        jListCompanies.setListData(modeloLista);
        
        SGuiUtils.createActionMap(rootPane, this, "actionClose", "close", KeyEvent.VK_ESCAPE);
        
        jbSend.addActionListener(this);
        jbClose.addActionListener(this);
    }
    
    private void sendMail() {
        try {
            String companiesIds = "";
            if (moRadThisCompany.isSelected()) {
                companiesIds += miClient.getSession().getConfigCompany().getCompanyId();
            }
            else if (moRadAccesCompany.isSelected()) {
                for (SDataCompany company : moVecCompanies) {
                    companiesIds += (companiesIds.isEmpty() ? "" : ";") + company.getPkCompanyId();
                }
            } 
            else if (moRadCustom.isSelected()) {
                List list = jListCompanies.getSelectedValuesList();
                for (Object list1 : list) {
                    for (SDataCompany company : moVecCompanies) {
                        String companyName = list1.toString();
                        if (company.getCompany().equals(companyName)) {
                            companiesIds += (companiesIds.isEmpty() ? "" : ";") + company.getPkCompanyId();
                            break;
                        }
                    }
                }
            }
            
            createExcelReport(new ArrayList<>(Arrays.asList(SLibUtilities.textExplode(companiesIds, ";"))));
            
            SDbMms mms = getMms((SClientInterface) miClient, SModSysConsts.CFGS_TP_MMS_MAIL_REPS);
            String subject = "[SIIE] Reporte de sanciones GH ";
            String body = composeMailBody(moDate.getValue());
            
            moMailSender = new SMailSender(mms.getHost(), mms.getPort(), mms.getProtocol(), mms.isStartTls(), mms.isAuth(), mms.getUser(), mms.getUserPassword(), mms.getUser());
            moMailSender.setMailReplyTo(mms.getXtaMailReplyTo());
            
            ArrayList<String> toRecipients = new ArrayList<>(Arrays.asList(SLibUtils.textExplode(moTextTo.getValue().toLowerCase(), ";")));
            ArrayList<String> toCcRecipients = moTextCc.getValue().isEmpty() ? new ArrayList<>() : new ArrayList<>(Arrays.asList(SLibUtils.textExplode(moTextCc.getValue().toLowerCase(), ";")));
            ArrayList<String> toBccRecipients = moTextBcc.getValue().isEmpty() ? new ArrayList<>() : new ArrayList<>(Arrays.asList(SLibUtils.textExplode(moTextBcc.getValue().toLowerCase(), ";")));
            
            // Guardar excel en disco
            
            String tempDir = System.getProperty("java.io.tmpdir") + "sanciones.xlsx";
            
            try (FileOutputStream outputStream = new FileOutputStream(tempDir)) {
                moWorkbook.write(outputStream);
            }
            
            File file = new File(tempDir);
            
            SMail mail = new SMail(moMailSender, subject, body, toRecipients, toCcRecipients, toBccRecipients);
            mail.getAttachments().add(file);
            mail.setContentType(SMailConsts.CONT_TP_TEXT_HTML);
            mail.send();
            file.delete();
            miClient.showMsgBoxInformation("El correo-e ha sido enviado.");
        }
        catch (Exception e) {
            miClient.showMsgBoxError(e.getMessage());
        }
    }
    
    private void createExcelReport(ArrayList<String> ids) throws Exception {
        String sql;
        String breachesSql = "SELECT * FROM(";
        ResultSet resultSet;
        moWorkbook = new XSSFWorkbook();
        Sheet sheet = moWorkbook.createSheet("Hoja 1");
        
        CellStyle header = getCellStyle(true, true, IndexedColors.LIGHT_BLUE.getIndex());
        CellStyle data = getCellStyle(false, true, IndexedColors.WHITE.getIndex());
        
        int rowNum = 0;
        int cellNum = 0;
        
        moExcelRow = sheet.createRow(rowNum++);
        createExcelCell("COLABORADOR", header, cellNum);
        sheet.setColumnWidth(cellNum++, 11058);
        createExcelCell("FECHA", header, cellNum);
        sheet.setColumnWidth(cellNum++, 4234);
        createExcelCell("PUESTO", header, cellNum);
        sheet.setColumnWidth(cellNum++, 11058);
        createExcelCell("FALTA", header, cellNum);
        sheet.setColumnWidth(cellNum++, 6350);
        createExcelCell("ACONTECIMIENTO", header, cellNum);
        sheet.setColumnWidth(cellNum++, 19088);
        createExcelCell("APARTADO", header, cellNum);
        sheet.setColumnWidth(cellNum++, 6861);
        createExcelCell("FIRMA", header, cellNum);
        sheet.setColumnWidth(cellNum++, 3175);
        createExcelCell("COMENTARIOS", header, cellNum);
        sheet.setColumnWidth(cellNum++, 16861);
        
        for (int i = 0; i < ids.size(); i++) {
            sql = "SELECT bd FROM erp.cfgu_co WHERE id_co = " + ids.get(i);
            resultSet = miClient.getSession().getStatement().executeQuery(sql); 
            if (resultSet.next()) {
                breachesSql += (i > 0 ? "UNION " : "") + getCompanySql(resultSet.getString(1));
            }
        }
        breachesSql += ") AS a ORDER BY fecha;";
        
        resultSet = miClient.getSession().getStatement().executeQuery(breachesSql);
        while (resultSet.next()) {
            cellNum = 0;
            moExcelRow = sheet.createRow(rowNum++);
            createExcelCell(resultSet.getString("colaborador"), data, cellNum++);
            createExcelCell(moDateFormatDate.format(resultSet.getDate("fecha")), data, cellNum++);
            createExcelCell(resultSet.getString("puesto"), data, cellNum++);
            createExcelCell(resultSet.getString("falta").toLowerCase(), data, cellNum++);
            createExcelCell(resultSet.getString("acontecimiento").toLowerCase(), data, cellNum++);
            createExcelCell(resultSet.getString("apartado"), data, cellNum++);
            createExcelCell(resultSet.getString("firma"), data, cellNum++);
            createExcelCell(resultSet.getString("comentarios"), data, cellNum++);
        }
    }
    
    private String getCompanySql(String bd) {
        return "SELECT bp.bp colaborador, ar.rec_dt_sta fecha, dep.name puesto, s.name falta, ar.breach_descrip acontecimiento, ss.code apartado, IF(ar.b_offender_sign = 1, 'SI', 'NO') firma, ar.ending_notes comentarios " +
                "FROM hrs_doc_adm_rec AS ar " +
                "INNER JOIN " + bd + ".hrs_doc_adm_rec_prec_subsec AS pss ON ar.id_doc_adm_rec = pss.id_doc_adm_rec " +
                "INNER JOIN " + bd + ".hrs_prec_subsec AS ss ON pss.id_prec = ss.id_prec AND pss.id_sec = ss.id_sec AND pss.id_subsec = ss.id_subsec " +
                "INNER JOIN " + bd + ".hrs_prec_sec AS s ON ss.id_prec = s.id_prec AND ss.id_sec = s.id_sec " +
                "INNER JOIN erp.hrsu_emp AS emp ON ar.fk_emp_offender = emp.id_emp " +
                "INNER JOIN erp.bpsu_bp AS bp ON emp.id_emp = bp.id_bp " +
                "INNER JOIN erp.hrsu_dep AS dep ON emp.fk_dep = dep.id_dep " +
                "WHERE NOT ar.b_del " +
                "UNION " +
                "SELECT bp.bp colaborador, db.breach_ts fecha, dep.name puesto, s.name falta, db.breach_descrip acontecimiento, ss.code apartado, IF(db.b_offender_sign = 1, 'SI', 'NO') firma, db.ending_notes comentarios " +
                "FROM hrs_doc_breach AS db " +
                "INNER JOIN " + bd + ".hrs_doc_breach_prec_subsec AS pss ON db.id_doc_breach = pss.id_doc_breach " +
                "INNER JOIN " + bd + ".hrs_prec_subsec AS ss ON pss.id_prec = ss.id_prec AND pss.id_sec = ss.id_sec AND pss.id_subsec = ss.id_subsec " +
                "INNER JOIN " + bd + ".hrs_prec_sec AS s ON ss.id_prec = s.id_prec AND ss.id_sec = s.id_sec " +
                "INNER JOIN erp.hrsu_emp AS emp ON db.fk_emp_offender = emp.id_emp " +
                "INNER JOIN erp.bpsu_bp AS bp ON emp.id_emp = bp.id_bp " +
                "INNER JOIN erp.hrsu_dep AS dep ON emp.fk_dep = dep.id_dep " +
                "WHERE NOT db.b_del ";
    }
    
    private void createExcelCell(String content, CellStyle style, int cellNum) {
        moExcelCell = moExcelRow.createCell(cellNum);
        moExcelCell.setCellValue(content);
        moExcelCell.setCellStyle(style);
    }
    
    private CellStyle getCellStyle (boolean bold, boolean border, short color){
        XSSFFont font;
        CellStyle cellStyle = moWorkbook.createCellStyle();
        font = moWorkbook.createFont();
        font.setBold(bold);
        font.setFontName("Aptos Narrow");
        font.setFontHeight(11);
        cellStyle.setFont(font);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        
        cellStyle.setFillForegroundColor(color);
        cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setWrapText(true);
        
        if (border) {
            cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
            cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
            cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
            cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        }
        
        return cellStyle;
    }
    
    private String composeMailBody(Date cutOff) throws Exception {
        String html = "<html>\n";
        html += "<head>\n";
        html += "<style>\n"
                + "body {"
                + " font-size: 100%;"
                + "} "
                + "h1 {"
                + " font-size: 2.00em;"
                + " font-family: sans-serif;"
                + "} "
                + "h2 {"
                + " font-size: 1.75em;"
                + " font-family: sans-serif;"
                + "} "
                + "h3 {"
                + " font-size: 1.50em;"
                + " font-family: sans-serif;"
                + "} "
                + "h4 {"
                + " font-size: 1.25em;"
                + " font-family: sans-serif;"
                + "} "
                + "</style>\n";
        html += "</head>\n";
        html += "<body>\n";
        html += "<h2>" + SLibUtils.textToHtml("Reporte de sanciones GH.") + "</h2>\n";
        html += "<p>Fecha de corte del archivo Excel: " + SLibUtils.textToHtml(SLibUtils.DateFormatDate.format(cutOff)) + "</p>\n";
        
        html += STrnUtilities.composeMailFooter("warning");
        
        html += "</body>\n";
        
        html += "</html>";
 
        return html;
    }
    
    private SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();
        
        if (validation.isValid()) {
            if (!SLibUtilities.validateEmails(moTextTo.getValue())){
                validation.setMessage("Uno o más correos en el campo 'Para' no son válidos.");
                validateForm().setComponent(moTextTo);
            }

            if (!moTextCc.getValue().isEmpty()) {
                if (!SLibUtilities.validateEmails(moTextCc.getValue())){
                    validation.setMessage("Uno o más correos en el campo 'CC' no son válidos.");
                    validateForm().setComponent(moTextCc);
                }
            }

            if (!moTextBcc.getValue().isEmpty()) {
                if (!SLibUtilities.validateEmails(moTextBcc.getValue())){
                    validation.setMessage("Uno o más correos en el campo 'BCC' no son válidos.");
                    validateForm().setComponent(moTextBcc);
                }
            }
            
            if (validation.isValid() && moBooleanSaveEmails.isSelected()) {
                if (moTextTo.getValue().length() + moTextCc.getValue().length() + moTextBcc.getValue().length() > 500) {
                    validation.setMessage("El número de caracteres en conjunto para los tres campos de correo no debe pasar de los 500.\n"
                            + "Borre destinatarios o deseleccione la casilla de \"Guardar destinatarios en la configuración del reporte\"");
                }
            }
            
            if (validation.isValid() && moRadCustom.isSelected()) {
                if (jListCompanies.getSelectedIndices().length == 0) {
                    validation.setMessage("Debe seleccionar al menos una empresa de la lista.");
                }
            }
        }
        return validation;
    }
    
    private void actionSend() {
        SGuiValidation validation = validateForm();
        if (validation.isValid()) {
            sendMail();
            saveMails();
            this.setVisible(false);
        }
        else {
            miClient.showMsgBoxWarning(validation.getMessage());
            if (validation.getComponent() != null) {
                validation.getComponent().requestFocus();
            }
        }
    }
    
    private void saveMails() {
        try {
            if (moBooleanSaveEmails.isSelected()) {
                String json = "{ \"to\":\"" + moTextTo.getValue().toLowerCase() + "\", \"cc\":\"" + 
                        moTextCc.getValue().toLowerCase() + "\", \"bcc\":\"" + moTextBcc.getValue().toLowerCase() + "\"}";
                String sql = "UPDATE cfg_param SET param_value = '" + json + "' WHERE param_key = '" + SDataConstantsSys.CFG_PARAM_DOC_HRS_MAIL_BREACH_TO + "'";
                miClient.getSession().getStatement().execute(sql);
            }
        }
        catch (Exception e) {
            miClient.showMsgBoxError("Error al actualizar la lista de emails.");
        }
    }
    
    public void actionClose() {
        this.setVisible(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == jbSend) {
                actionSend();
            }
            else if (button == jbClose) {
                actionClose();
            }
        }
    }
}
