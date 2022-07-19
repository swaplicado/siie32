/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.form;

import erp.client.SClientInterface;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.hrs.db.SHrsConsts;
import erp.mod.hrs.db.SHrsUtils;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanFormDialog;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SDialogRepVacationsFileCsv extends SBeanFormDialog {
    
    protected SPanelHrsDepartments moPanelHrsDepartments;
   
    /**
     * Creates new form SDialogVacationsFileCsv
     * @param client
     * @param title
     */
    public SDialogRepVacationsFileCsv(SGuiClient client, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT,  SModConsts.HRSR_VAC_CSV, SLibConsts.UNDEFINED, title);
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

        moRadGroupFilterType = new javax.swing.ButtonGroup();
        jbGrpOrderBy = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jlDateCut = new javax.swing.JLabel();
        moDateDateCut = new sa.lib.gui.bean.SBeanFieldDate();
        jPanel14 = new javax.swing.JPanel();
        jlPaymentType = new javax.swing.JLabel();
        moKeyPaymentType = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel6 = new javax.swing.JPanel();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Parámetros del reporte:"));
        jPanel1.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel2.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateCut.setText("Fecha corte:*");
        jlDateCut.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel11.add(jlDateCut);
        jPanel11.add(moDateDateCut);

        jPanel2.add(jPanel11);

        jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPaymentType.setText("Período pago:");
        jlPaymentType.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel14.add(jlPaymentType);

        moKeyPaymentType.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel14.add(moKeyPaymentType);

        jPanel2.add(jPanel14);

        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Seleccionar departamentos:"));
        jPanel6.setLayout(new java.awt.BorderLayout());
        jPanel1.add(jPanel6, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel6;
    private javax.swing.ButtonGroup jbGrpOrderBy;
    private javax.swing.JLabel jlDateCut;
    private javax.swing.JLabel jlPaymentType;
    private sa.lib.gui.bean.SBeanFieldDate moDateDateCut;
    private sa.lib.gui.bean.SBeanFieldKey moKeyPaymentType;
    private javax.swing.ButtonGroup moRadGroupFilterType;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 560, 350);
        
        moPanelHrsDepartments = new SPanelHrsDepartments(miClient);
        
        jbSave.setText("Guardar");

        moDateDateCut.setDateSettings(miClient, SGuiUtils.getLabelName(jlDateCut.getText()), true);
        moKeyPaymentType.setKeySettings(miClient, SGuiUtils.getLabelName(jlPaymentType.getText()), false);

        jPanel6.add(moPanelHrsDepartments, BorderLayout.CENTER);
        
        moFields.addField(moDateDateCut);
        moFields.addField(moKeyPaymentType);
        
        moFields.setFormButton(jbSave);
        
        moDateDateCut.setValue(miClient.getSession().getCurrentDate());
        
        reloadCatalogues();
    }

    private void computeReport() {
        String sql = "";
        ResultSet resulSetEmployee = null;
        String buffer = "";
        String dateCut = "";
        int nEmployeeAnniversary = 0;
        int nVacationsDaysAll = 0;
        int nVacationsDaysPayed = 0;
        Cursor cursor = getCursor();
        String sDepartamentsId = "";
        String sDepartamentsName = "";

        try {
            miClient.getFileChooser().setSelectedFile(new File(getTitle() + " " + ((SClientInterface) miClient).getSessionXXX().getFormatters().getFileNameDatetimeFormat().format(new java.util.Date()) + ".csv"));
            if (miClient.getFileChooser().showSaveDialog(miClient.getFrame()) == JFileChooser.APPROVE_OPTION) {
                setCursor(new Cursor(Cursor.WAIT_CURSOR));

                File file = new File(miClient.getFileChooser().getSelectedFile().getAbsolutePath());
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
        
                sDepartamentsId = (String) moPanelHrsDepartments.getValue(SGuiConsts.PARAM_KEY);
                sDepartamentsName = (String) moPanelHrsDepartments.getValue(SGuiConsts.PARAM_ITEM);
                dateCut = "'" + SLibUtils.DbmsDateFormatDate.format(moDateDateCut.getValue()) + "' ";
                
                buffer = ((SClientInterface)miClient).getSessionXXX().getCompany().getCompany() + "\n";
                buffer += "REPORTE VACACIONES PENDIENTES\n";
                buffer += "FECHA CORTE: " + SLibUtils.DateFormatDate.format(moDateDateCut.getValue()) + "\n" ;
                buffer += "PERÍODO DE PAGO: " + (moKeyPaymentType.getSelectedIndex() > 0 ? moKeyPaymentType.getSelectedItem() : "(TODOS)") + "\n\n";
                buffer += "Usuario: " + ((SClientInterface)miClient).getSessionXXX().getUser().getUser() + "\n";
                buffer += "Emisión: " + ((SClientInterface) miClient).getSessionXXX().getFormatters().getDatetimeFormat().format(new java.util.Date()) + "\n";
                
                bw.write(SLibUtilities.textToAscii(buffer));
                bw.write("\n\n");
                
                bw.write("\n");
                buffer = "\"EMPLEADO\",\"INICIO BENEFICIOS\",\"FECHA BASE\",\"ANTIGÜEDAD AÑOS\",\"ANTIGÜEDAD DÍAS\",\"ANIVERSARIO\",\"ANIVERSARIO AÑO\",\"DÍAS\",\"PAGADOS\",\"POR PAGAR\",\"ADELANTADOS\",\"UNIDAD\"";

                bw.write(SLibUtilities.textToAscii(buffer));
                
                //moParamsMap.put("sDepartaments", sDepartamentsName.isEmpty() || (boolean) moPanelHrsDepartments.getValue(SGuiConsts.PARAM_ROWS) ? "(TODOS)" : sDepartamentsName + " ");
                
                sql = "SELECT bp.id_bp AS f_id_1, " +
                        "IF(!emp.b_act, 0, TIMESTAMPDIFF(YEAR,emp.dt_ben, " + dateCut + ")) AS f_id_2, " +
                        "IF(!emp.b_act, 0, TIMESTAMPDIFF(DAY,ADDDATE(emp.dt_ben, INTERVAL TIMESTAMPDIFF(YEAR,emp.dt_ben," + dateCut + " ) YEAR)," + dateCut + " )) AS f_id_3, " +
                        "v.id_ben AS f_id_4, " +
                        "bp.bp AS f_code, " +
                        "bp.bp AS f_name, " +
                        "emp.dt_ben, " +
                        "emp.b_act, " +
                        "(ADDDATE(emp.dt_ben, INTERVAL TIMESTAMPDIFF(YEAR,emp.dt_ben," + dateCut + " ) YEAR)) AS f_dt_base, " +
                        "IF(!emp.b_act, 0, TIMESTAMPDIFF(YEAR,emp.dt_ben," + dateCut + " )) AS f_sen, " +
                        "IF(!emp.b_act, 0, TIMESTAMPDIFF(DAY,ADDDATE(emp.dt_ben, INTERVAL TIMESTAMPDIFF(YEAR,emp.dt_ben," + dateCut + " ) YEAR)," + dateCut + " )) AS f_sen_day, " +
                        "IF(!emp.b_act OR TIMESTAMPDIFF(YEAR,emp.dt_ben," + dateCut + " ) = 0, 0, TIMESTAMPDIFF(YEAR,emp.dt_ben," + dateCut + " )) AS f_ann, " +
                        "IF(!emp.b_act OR TIMESTAMPDIFF(YEAR,emp.dt_ben," + dateCut + " ) = 0, 0, (IF(YEAR(ADDDATE(emp.dt_ben, INTERVAL TIMESTAMPDIFF(YEAR,emp.dt_ben," + dateCut + " ) YEAR)) > YEAR(emp.dt_ben), YEAR(ADDDATE(emp.dt_ben, INTERVAL TIMESTAMPDIFF(YEAR,emp.dt_ben," + dateCut + " ) YEAR)) - 1, YEAR(emp.dt_ben)))) AS f_ann_ano, " +
                        "IF(!emp.b_act OR TIMESTAMPDIFF(YEAR,emp.dt_ben," + dateCut + " ) = 0, 0, IF(emp.b_act = 0, 0, (SELECT SUM(ben_day) FROM hrs_ben_row WHERE id_ben = vr.id_ben AND id_row <= vr.id_row))) AS f_ben_day_2, " +
                        "IF(v.fk_tp_ben = " + SModSysConsts.HRSS_TP_BEN_VAC + ", 'día', '$') AS f_unit, " +
                        "IF(!emp.b_act OR TIMESTAMPDIFF(YEAR,emp.dt_ben," + dateCut + " ) = 0, 0, (SELECT br.ben_day FROM hrs_ben AS b INNER JOIN hrs_ben_row AS br ON b.id_ben = br.id_ben WHERE fk_tp_ben = " + SModSysConsts.HRSS_TP_BEN_VAC + " AND dt_sta <= " + dateCut + " " +
                        "AND br.id_row = (SELECT br.id_row FROM hrs_ben AS b INNER JOIN hrs_ben_row AS br ON b.id_ben = br.id_ben WHERE fk_tp_ben = " + SModSysConsts.HRSS_TP_BEN_VAC + " AND dt_sta <= " + dateCut + "  AND mon >= TIMESTAMPDIFF(YEAR,emp.dt_ben," + dateCut + " ) * " + SHrsConsts.YEAR_MONTHS + " ORDER BY dt_sta DESC, b.id_ben LIMIT 1) ORDER BY dt_sta DESC, b.id_ben LIMIT 1)) AS f_day_bon, " +
                        "IF(!emp.b_act OR TIMESTAMPDIFF(YEAR,emp.dt_ben," + dateCut + " ) = 0, 0, IF(vr.ben_bon_per = 0, 1, vr.ben_bon_per)) AS f_bon_per, " +
                        "IF(!emp.b_act, 0, COALESCE(IF(emp.fk_tp_pay = " + SModSysConsts.HRSS_TP_PAY_FOR + ", emp.wage * " + SHrsConsts.YEAR_MONTHS + " / " + SHrsConsts.YEAR_DAYS + ", emp.sal), 0)) AS f_pay_day, " +
                        "IF(!emp.b_act, 0, COALESCE((IF(emp.fk_tp_pay = " + SModSysConsts.HRSS_TP_PAY_FOR + ", emp.wage * " + SHrsConsts.YEAR_MONTHS + " / " + SHrsConsts.YEAR_DAYS + ", emp.sal) / emp.wrk_hrs_day), 0)) AS f_pay_hr, " +
                        "IF(!emp.b_act, 0, (SELECT COALESCE(SUM(unt_all), 0) FROM hrs_pay_rcp_ear WHERE b_del = 0 AND ben_ann <> 0 AND ben_ann <= TIMESTAMPDIFF(YEAR,emp.dt_ben," + dateCut + ") AND fk_tp_ben = " + SModSysConsts.HRSS_TP_BEN_VAC + " AND id_emp = emp.id_emp)) AS f_payed_unt, " +
                        "IF(!emp.b_act OR TIMESTAMPDIFF(YEAR,emp.dt_ben," + dateCut + " ) = 0, 0, IF(ear.ben_ann = TIMESTAMPDIFF(YEAR,emp.dt_ben," + dateCut + " ), SUM(ear.amt_r), 0))  AS f_payed_amt, " +
                        "IF(!emp.b_act, 0, (SELECT COALESCE(SUM(unt_all), 0) FROM hrs_pay_rcp_ear WHERE b_del = 0 AND ben_ann <> 0 AND ben_ann > TIMESTAMPDIFF(YEAR,emp.dt_ben," + dateCut + ") AND fk_tp_ben = " + SModSysConsts.HRSS_TP_BEN_VAC + " AND id_emp = emp.id_emp)) AS f_payed_unt_oth, " +
                        "IF(!emp.b_act OR TIMESTAMPDIFF(YEAR,emp.dt_ben," + dateCut + " ) = 0, 0, IF(ear.ben_ann <> TIMESTAMPDIFF(YEAR,emp.dt_ben," + dateCut + " ), SUM(ear.amt_r), 0))  AS f_payed_amt_oth " +
                        "FROM erp.hrsu_emp AS emp " +
                        "INNER JOIN hrs_emp_member AS empm ON empm.id_emp = emp.id_emp " +
                        "INNER JOIN erp.bpsu_bp AS bp ON bp.id_bp = emp.id_emp " +
                        "INNER JOIN hrs_ben AS v " +
                        "INNER JOIN hrs_ben_row AS vr ON v.id_ben = vr.id_ben " +
                        "LEFT OUTER JOIN hrs_pay_rcp AS rcp ON rcp.id_emp = emp.id_emp AND rcp.b_del = 0 " +
                        "LEFT OUTER JOIN hrs_pay_rcp_ear AS ear ON ear.id_pay = rcp.id_pay AND ear.id_emp = rcp.id_emp AND ear.fk_tp_ben = v.fk_tp_ben AND ear.b_del = 0 " +
                        "WHERE v.id_ben = ( SELECT id_ben FROM hrs_ben WHERE fk_tp_ben = " + SModSysConsts.HRSS_TP_BEN_VAC + " AND dt_sta <= " + dateCut + " ORDER BY dt_sta DESC, id_ben LIMIT 1) AND " +
                        "vr.id_row = ( SELECT br.id_row FROM hrs_ben AS b INNER JOIN hrs_ben_row AS br ON b.id_ben = br.id_ben WHERE fk_tp_ben = " + SModSysConsts.HRSS_TP_BEN_VAC + " AND dt_sta <= " + dateCut + "  AND mon >= TIMESTAMPDIFF(YEAR,emp.dt_ben," + dateCut + " ) * " + SHrsConsts.YEAR_MONTHS + " " +
                        (moKeyPaymentType.getSelectedIndex() > 0 ? " AND emp.fk_tp_pay = " + moKeyPaymentType.getValue()[0] : "") + " " +
                        (sDepartamentsId.isEmpty() ? "" : " AND emp.fk_dep IN(" + sDepartamentsId + ")") + " " +
                        "ORDER BY dt_sta DESC, b.id_ben LIMIT 1) AND emp.b_act = 1 " +
                        "GROUP BY emp.id_emp ORDER BY bp.bp, bp.id_bp; ";

                resulSetEmployee = miClient.getSession().getStatement().getConnection().createStatement().executeQuery(sql);
                while (resulSetEmployee.next()) {
                    buffer = "";
                    nEmployeeAnniversary = resulSetEmployee.getInt("f_ann");
                    nVacationsDaysAll = SHrsUtils.getDaysVacationsAll(miClient.getSession(), nEmployeeAnniversary, moDateDateCut.getValue());
                    nVacationsDaysPayed = resulSetEmployee.getInt("f_payed_unt");
                    
                    buffer += "\"" + resulSetEmployee.getString("f_name").replace("\"", "'") + "\",";
                    buffer += "\"" + (resulSetEmployee.getDate("emp.dt_ben")) + "\",";
                    buffer += "\"" + (resulSetEmployee.getDate("f_dt_base")) + "\",";
                    buffer += "\"" + (resulSetEmployee.getInt("f_sen")) + "\",";
                    buffer += "\"" + (resulSetEmployee.getInt("f_sen_day")) + "\",";
                    buffer += "\"" + (nEmployeeAnniversary) + "\",";
                    buffer += "\"" + (resulSetEmployee.getInt("f_ann_ano")) + "\",";
                    buffer += "\"" + (nVacationsDaysAll) + "\",";
                    buffer += "\"" + (nVacationsDaysPayed) + "\",";
                    buffer += "\"" + (nVacationsDaysAll - nVacationsDaysPayed) + "\",";
                    buffer += "\"" + (resulSetEmployee.getInt("f_payed_unt_oth")) + "\",";
                    buffer += "\"" + (resulSetEmployee.getString("f_unit")).replace("\"", "'") + "\",";
                    
                    bw.write("\n");
                    bw.write(SLibUtilities.textToAscii(buffer));
                }
                bw.flush();
                bw.close();
                
                if (miClient.showMsgBoxConfirm(SLibConstants.MSG_INF_FILE_CREATE + file.getPath() + "\n" + SLibConstants.MSG_CNF_FILE_OPEN) == JOptionPane.YES_OPTION) {
                    SLibUtilities.launchFile(file.getPath());
                }
            }
        }
        catch(Exception e) {
            SLibUtilities.renderException(this, e);
        }
        finally {
            setCursor(cursor);
        }
        
    }
    
    @Override
    public void addAllListeners() {
    }

    @Override
    public void removeAllListeners() {
    }
    
    @Override
    public void reloadCatalogues() {
        miClient.getSession().populateCatalogue(moKeyPaymentType, SModConsts.HRSS_TP_PAY, SLibConsts.UNDEFINED, null);
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();

        if (validation.isValid()) {
            validation = moPanelHrsDepartments.validatePanel();
        }
                
        
        return validation;
    }
    
    @Override
    public void actionSave() {
        if (jbSave.isEnabled()) {
            if (SGuiUtils.computeValidation(miClient, validateForm())) {
                computeReport();
            }
        }
    }
}
