/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.hrs.form;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.hrs.db.SDbEarning;
import erp.mod.hrs.db.SDbEmployee;
import erp.mod.hrs.db.SDbMassiveSalarySscBase;
import erp.mod.hrs.db.ssc.SRowEmployeeSsc;
import erp.mod.hrs.db.ssc.SSscUtils;
import static erp.mod.hrs.db.ssc.SSscUtils.daysCalendarPeriod;
import erp.mod.hrs.utils.SAnniversary;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.grid.SGridColumnForm;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneForm;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;

/**
 *
 * @author Claudio Peña
 */
public class SFormMassiveUpdateSsc extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener, javax.swing.event.CellEditorListener {

    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    protected boolean mbIsSelectedAll;
    private boolean mbFirstTime;
    protected String msEmployeeSelectedsId;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private erp.lib.form.SFormField moFieldDateChangeSalary;
    private erp.client.SClientInterface miClient;
    public int mnYear;
    public int mnBimester;
    public int mnMonthStart = 0;
    public int mnMonthEnd = 0; 
    public double earningExtra = 0.0; 
    public Date mtDateStart = null;
    public Date mtDateEnd = null;
    
    protected SGridPaneForm moGridSbcEmployees;
    
    protected erp.lib.table.STablePane moTablePane;
    protected ArrayList<SDbEarning> maEarnings;
    protected double SDbEmployees[];
    protected JButton mjbSelectAll;
    protected JButton mjbDeselectAll;
    protected JButton mjbReset;
    
    private SDbEmployee moCurrentEmployee;
    protected SAnniversary moAnniversary;
    /**
     * 
     * @param client
     * @param year
     * @param bimester 
     */
    public SFormMassiveUpdateSsc(erp.client.SClientInterface client, int year, int bimester) throws Exception {
        super(client.getFrame(), true);
        miClient = client;
        mnFormType = SDataConstants.FINU_TAX_BAS;
        
        mnYear = year;
        mnBimester = bimester;
        mnMonthEnd = mnBimester * 2;
        mnMonthStart = mnMonthEnd - 1;
        
        initComponents();
        initComponentsExtra();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     * 
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jlSbcEmployeesLabel = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jbSave = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jpEmployeeSBC = new javax.swing.JPanel();
        jpFilter = new javax.swing.JPanel();
        jlYear = new javax.swing.JLabel();
        jtfYear = new javax.swing.JTextField();
        jlBimestre = new javax.swing.JLabel();
        jtfBimestre = new javax.swing.JTextField();
        jlDateCut = new javax.swing.JLabel();
        jftDateCut = new javax.swing.JFormattedTextField();
        jlDateChangeSsc = new javax.swing.JLabel();
        jftDateChangeSsc = new javax.swing.JFormattedTextField();
        jbDateChangeSscPicker = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Actualización de SBC de empelados");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel1.setPreferredSize(new java.awt.Dimension(392, 33));
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jPanel3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel3.setPreferredSize(new java.awt.Dimension(500, 33));
        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        jlSbcEmployeesLabel.setText("Empleados: 0; Seleccionados: 0");
        jlSbcEmployeesLabel.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel3.add(jlSbcEmployeesLabel);

        jPanel1.add(jPanel3);

        jPanel4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel4.setPreferredSize(new java.awt.Dimension(500, 33));
        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 1, 0));

        jbSave.setText("Guardar");
        jbSave.setToolTipText("[Ctrl + Enter]");
        jbSave.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel4.add(jbSave);

        jbCancel.setText("Cancelar");
        jbCancel.setToolTipText("[Escape]");
        jPanel4.add(jbCancel);

        jPanel1.add(jPanel4);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        jPanel2.setLayout(new java.awt.BorderLayout(0, 5));

        jpEmployeeSBC.setBorder(javax.swing.BorderFactory.createTitledBorder("Empleados SBC:"));
        jpEmployeeSBC.setLayout(new java.awt.BorderLayout());

        jpFilter.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlYear.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jlYear.setText("Ejercicio:");
        jlYear.setToolTipText("");
        jlYear.setPreferredSize(new java.awt.Dimension(75, 23));
        jpFilter.add(jlYear);

        jtfYear.setEditable(false);
        jtfYear.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfYear.setText("0");
        jtfYear.setFocusable(false);
        jtfYear.setPreferredSize(new java.awt.Dimension(75, 23));
        jpFilter.add(jtfYear);

        jlBimestre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlBimestre.setText("Bimestre:");
        jlBimestre.setPreferredSize(new java.awt.Dimension(75, 23));
        jpFilter.add(jlBimestre);

        jtfBimestre.setEditable(false);
        jtfBimestre.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfBimestre.setText("0");
        jtfBimestre.setFocusable(false);
        jtfBimestre.setPreferredSize(new java.awt.Dimension(75, 23));
        jpFilter.add(jtfBimestre);

        jlDateCut.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlDateCut.setText("Fecha de corte:");
        jlDateCut.setPreferredSize(new java.awt.Dimension(100, 23));
        jpFilter.add(jlDateCut);

        jftDateCut.setEditable(false);
        jftDateCut.setText("dd/mm/yyyy");
        jftDateCut.setFocusable(false);
        jftDateCut.setPreferredSize(new java.awt.Dimension(75, 23));
        jpFilter.add(jftDateCut);

        jlDateChangeSsc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlDateChangeSsc.setText("Fecha de actualización:");
        jlDateChangeSsc.setToolTipText("");
        jlDateChangeSsc.setPreferredSize(new java.awt.Dimension(150, 23));
        jpFilter.add(jlDateChangeSsc);

        jftDateChangeSsc.setText("yyyy/mm/dd");
        jftDateChangeSsc.setPreferredSize(new java.awt.Dimension(75, 23));
        jpFilter.add(jftDateChangeSsc);

        jbDateChangeSscPicker.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbDateChangeSscPicker.setToolTipText("Seleccionar fecha");
        jbDateChangeSscPicker.setFocusable(false);
        jbDateChangeSscPicker.setPreferredSize(new java.awt.Dimension(23, 23));
        jpFilter.add(jbDateChangeSscPicker);

        jpEmployeeSBC.add(jpFilter, java.awt.BorderLayout.NORTH);

        jPanel2.add(jpEmployeeSBC, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        setSize(new java.awt.Dimension(1040, 720));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void initComponentsExtra() throws Exception {
        mtDateEnd = SLibTimeUtils.getEndOfMonth(SLibTimeUtils.createDate(mnYear, mnMonthEnd));

        mvFields = new Vector<>();
        mjbSelectAll = new JButton("Seleccionar todos");
        mjbDeselectAll = new JButton("Deseleccionar todos");
        mjbReset = new JButton("Reiniciar");

        mjbSelectAll.addActionListener(this);
        mjbDeselectAll.addActionListener(this);
        mjbReset.addActionListener(this);

        mbIsSelectedAll = false;
        msEmployeeSelectedsId = "";
        jtfYear.setText("" + mnYear + "");
        jtfBimestre.setText("" + mnBimester + "");
        jftDateCut.setText(SLibUtils.DateFormatDate.format(mtDateEnd));

        try {
            maEarnings = SSscUtils.getEarningPaidInPeriod(miClient.getSession(), mnYear, mnMonthStart, mnMonthEnd);
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
        
        moGridSbcEmployees = new SGridPaneForm((SGuiClient) miClient, SModConsts.HRSX_BEN_DET, SLibConsts.UNDEFINED, "Tabla de empleados") {
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false);
            }

            @Override
            public ArrayList<SGridColumnForm> createGridColumns() {
                SGridColumnForm column = null;
                ArrayList<SGridColumnForm> columns = new ArrayList<>();
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "Nombre empleado", 250));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_BPR, "Número empleado"));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_DATE, "Inicio prestaciones", 75));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_INT_1B, "Antigüedad final periodo", 50));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_ACC, "Período pago", 60));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_INT_1B, "Días vacaciones", 50));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_PER_2D, "Prima vacacional %", 50));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_INT_1B, "Días aguinaldo", 50));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_8D, "Factor de integración", 100));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_AMT, "Ingreso diario $", 75));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_AMT, "SBC actual $", 75));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_DATE, "SBC últ. cambio", 75));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_AMT, "SBC con factor $", 75));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_AMT, "Otras percepciones $", 75));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_INT_1B, "Días bimestre ", 50));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_INT_2B, "Días no trabajados sugeridos", 75));
                column = new SGridColumnForm(SGridConsts.COL_TYPE_INT_2B, "Días no trabajados efectivos", moGridSbcEmployees.getTable().getDefaultEditor(Integer.class));
                column.setEditable(true);
                columns.add(column);
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_AMT, "SBC sugerido $"));
                column = new SGridColumnForm(SGridConsts.COL_TYPE_DEC_AMT, "SBC nuevo $", moGridSbcEmployees.getTable().getDefaultEditor(Double.class));
                column.setEditable(true);
                columns.add(column);
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_AMT, "Diferencia SBC $"));
                column = new SGridColumnForm(SGridConsts.COL_TYPE_BOOL_S, "Seleccionado", moGridSbcEmployees.getTable().getDefaultEditor(Boolean.class));
                column.setEditable(true);
                columns.add(column);
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_AMT, "Pagos extras: ", 1));
                for (SDbEarning earning : maEarnings) {
                    columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_AMT, SLibUtils.textProperCase(earning.getName()) + " Exento", 75));
                    columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_AMT, SLibUtils.textProperCase(earning.getName()) + " Gravado", 75));
                }

                moGridSbcEmployees.getTable().getDefaultEditor(Integer.class).addCellEditorListener(SFormMassiveUpdateSsc.this);
                moGridSbcEmployees.getTable().getDefaultEditor(Double.class).addCellEditorListener(SFormMassiveUpdateSsc.this);
                moGridSbcEmployees.getTable().getDefaultEditor(Boolean.class).addCellEditorListener(SFormMassiveUpdateSsc.this);
                
                return columns;
                
            }
        };
        
        moGridSbcEmployees.getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbSelectAll);
        moGridSbcEmployees.getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbDeselectAll);
        moGridSbcEmployees.getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbReset);
        
        moGridSbcEmployees.setForm(null);
        moGridSbcEmployees.setPaneFormOwner(null);
        
        moFieldDateChangeSalary = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jftDateChangeSsc, jlDateCut);
        moFieldDateChangeSalary.setPickerButton(jbDateChangeSscPicker);

        mtDateStart = SLibTimeUtils.getBeginOfMonth(SLibTimeUtils.createDate(mnYear, mnMonthStart));
        mtDateEnd = SLibTimeUtils.getEndOfMonth(SLibTimeUtils.createDate(mnYear, mnMonthEnd));
        
        dataEmployeeDetail(mtDateStart, mtDateEnd, mnYear);
        jpEmployeeSBC.add(moGridSbcEmployees, BorderLayout.CENTER);

        employeeSelectAll(true);

        jbSave.addActionListener(this);
        jbCancel.addActionListener(this);
        jbDateChangeSscPicker.addActionListener(this);
        
        AbstractAction actionOk = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { 
                try {
                actionSave();
                    } catch (ParseException ex) {
                        Logger.getLogger(SFormMassiveUpdateSsc.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
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
            jftDateChangeSsc.requestFocusInWindow();
        }
    }

    private void actionSave() throws ParseException {
        SFormValidation validation = formValidate();

        if (validation.getIsError()) {
            if (validation.getComponent() != null) {
                validation.getComponent().requestFocus();
            }
            if (!validation.getMessage().isEmpty()) {
                miClient.showMsgBoxWarning(validation.getMessage());
            }
        }
        else {
            boolean canClose = true;
            JOptionPane.showMessageDialog(this, "Antes de actualizar sugerimos exportar los datos como CSV");
            if (JOptionPane.showConfirmDialog(this, "Esta seguro que desea actualizar los SBC", "Confirmar", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
                canClose = true;
            } 
            else {
                save(jftDateChangeSsc.getText());

                mnFormResult = SLibConstants.FORM_RESULT_OK;
                setVisible(false);
            }
        }
    }
    
    private void actionCancel() {
        mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
        setVisible(false);
    }

    private void dataEmployeeDetail(Date dateLayoudStart, Date dateLayoutEnd, int layoutYear) {
        String dateStart = SLibUtils.DbmsDateFormatDate.format(dateLayoudStart); 
        String dateEnd = SLibUtils.DbmsDateFormatDate.format(dateLayoutEnd); 
        String pDateBoyCurr = SLibUtils.DbmsDateFormatDate.format(SLibTimeUtils.getBeginOfYear(dateLayoutEnd));
        String sql = "";
        ResultSet resultSet = null;
        int auxEmployee = 0;
        
        String dateEndAnt = SLibUtils.DbmsDateFormatDate.format(SLibTimeUtils.addDate(dateLayoutEnd, 0, -2, 0));

        try {
           ArrayList<SRowEmployeeSsc> rows = SSscUtils.createEmployeeSbcRows(miClient.getSession(), layoutYear, mnMonthStart, mnMonthEnd);
           
           for (SRowEmployeeSsc row : rows) {

           moCurrentEmployee = (SDbEmployee) miClient.getSession().readRegistry(SModConsts.HRSU_EMP, new int[] { row.getEmployee().getPkEmployeeId() });

           Date cutoffYearStart = SLibTimeUtils.getBeginOfYear(mtDateEnd);

           if (moCurrentEmployee.getDateBenefits().after(cutoffYearStart)) {
               cutoffYearStart = moCurrentEmployee.getDateBenefits().after(mtDateEnd) ? mtDateEnd : moCurrentEmployee.getDateBenefits();
           }

           sql = "SELECT bp.id_bp AS _IdEmp, bp.bp AS _emp_name, e.num AS _emp_num, tp.name AS _pay_tp_name, e.dt_ben AS _emp_dt_ben, e.dt_sal_ssc AS SscLastUpdate, " +
                "e.sal_ssc AS SscCurrent, '" + dateEnd + "' AS _p_dt_cutoff, @sen_raw:=CEILING(ROUND(DATEDIFF('" + dateEnd + "', e.dt_ben) / 365,4)) AS _sen_raw, " +
                "@curr_sal_wage:=(SELECT IF((SELECT COUNT(*) FROM hrs_emp_log_wage WHERE id_emp = " + row.getEmployee().getPkEmployeeId() + " AND dt " +
                "BETWEEN ' " + dateStart + " ' AND ' " + dateEnd + " ') >= 1, 0, v.wage) AS wage FROM hrs_emp_log_wage AS v INNER JOIN erp.hrsu_emp AS emp " +
                "ON v.id_emp = emp.id_emp WHERE v.b_del = 0 AND emp.b_act = 1  AND v.id_emp = " + row.getEmployee().getPkEmployeeId() + " AND v.dt <= ' " + dateEndAnt + " ' " +
                "ORDER BY v.dt DESC LIMIT 1) as WageI, " +
                "@curr_sal_day:=ROUND(IF(e.fk_tp_pay = 1, e.sal, ((e.wage * 12) / 365)), 2) AS salarioDiario, " +
                "@curr_dt_base:=IF(" + SModSysConsts.HRSS_TP_BEN_VAC + " = " + SModSysConsts.HRSS_TP_BEN_ANN_BON + ", " +
                "IF(e.dt_ben < ' " + pDateBoyCurr + " ', ' " + pDateBoyCurr + " ', e.dt_ben), " +
                "ADDDATE(e.dt_ben, INTERVAL @sen_as_years YEAR)) AS _curr_dt_base, " +
                "@VacationsDays:=(SELECT vac_day FROM HRS_EMP_WAGE_FAC_ANN where id_emp = " + row.getEmployee().getPkEmployeeId() + " and id_ann = @sen_raw) AS VacationDays, " +
                "@annualBonusDays:=(SELECT ann_bon_day FROM HRS_EMP_WAGE_FAC_ANN where id_emp = " + row.getEmployee().getPkEmployeeId() + " and id_ann = @sen_raw) AS AnnualBonusDays, " +
                "@primaVacacional:=(SELECT vac_bon_per FROM HRS_EMP_WAGE_FAC_ANN where id_emp = " + row.getEmployee().getPkEmployeeId() + " and id_ann = @sen_raw) AS primavacaional, " +
                "@factorI:=(SELECT wage_fac FROM HRS_EMP_WAGE_FAC_ANN where id_emp = " + row.getEmployee().getPkEmployeeId() + " and id_ann = @sen_raw) AS SscFactor, " +
                "@SbcFac:=(@curr_sal_day * @factorI) AS SbcFac, " +
                "(@Factori * @curr_sal_day) AS SscRaw " +
                "FROM erp.bpsu_bp AS bp INNER JOIN erp.hrsu_emp AS e ON e.id_emp = bp.id_bp " +
                "INNER JOIN erp.hrss_tp_pay AS tp ON e.fk_tp_pay = tp.id_tp_pay " +
                "LEFT OUTER JOIN (SELECT t.id_emp, t.ben_year, t.ben_ann, SUM(t.ben_unt) AS ben_unt, SUM(t.ben_amt) AS ben_amt " +
                "FROM (SELECT pre.id_emp, pre.ben_year, pre.ben_ann, SUM(pre.unt) AS ben_unt, SUM(pre.amt_r) AS ben_amt " +
                "FROM hrs_pay AS p " +
                "INNER JOIN hrs_pay_rcp AS pr ON pr.id_pay = p.id_pay " +
                "INNER JOIN hrs_pay_rcp_ear AS pre ON pre.id_pay = pr.id_pay AND pre.id_emp = pr.id_emp " +
                "INNER JOIN erp.hrsu_emp AS e ON pr.id_emp = e.id_emp " +
                "WHERE pre.fk_tp_ben = 21 AND e.b_act AND NOT p.b_del AND NOT pr.b_del AND NOT pre.b_del AND pre.ben_year = YEAR('" + dateEnd + "') " +
                "GROUP BY pre.id_emp , pre.ben_year , pre.ben_ann UNION SELECT prd.id_emp, prd.ben_year, prd.ben_ann, - SUM(prd.unt) AS ben_unt, - SUM(prd.amt_r) AS ben_amt " +
                "FROM hrs_pay AS p " +
                "INNER JOIN hrs_pay_rcp AS pr ON pr.id_pay = p.id_pay " +
                "INNER JOIN hrs_pay_rcp_ded AS prd ON prd.id_pay = pr.id_pay AND prd.id_emp = pr.id_emp " +
                "INNER JOIN erp.hrsu_emp AS e ON pr.id_emp = e.id_emp " +
                "WHERE prd.fk_tp_ben = 21 AND e.b_act AND NOT p.b_del AND NOT pr.b_del AND NOT prd.b_del AND prd.ben_year = YEAR('" + dateEnd + "') " +
                "GROUP BY prd.id_emp , prd.ben_year , prd.ben_ann " +
                "ORDER BY id_emp , ben_year , ben_ann) AS t " +
                "GROUP BY id_emp , ben_year , ben_ann " +
                "ORDER BY id_emp , ben_year , ben_ann) AS tcur ON tcur.id_emp = bp.id_bp " +
                "LEFT OUTER JOIN (SELECT t.id_emp, t.ben_year, t.ben_ann, SUM(t.ben_unt) AS ben_unt, SUM(t.ben_amt) AS ben_amt FROM " +
                "(SELECT  pre.id_emp, pre.ben_year, pre.ben_ann, SUM(pre.unt) AS ben_unt, SUM(pre.amt_r) AS ben_amt " +
                "FROM hrs_pay AS p " +
                "INNER JOIN hrs_pay_rcp AS pr ON pr.id_pay = p.id_pay " +
                "INNER JOIN hrs_pay_rcp_ear AS pre ON pre.id_pay = pr.id_pay AND pre.id_emp = pr.id_emp " +
                "INNER JOIN erp.hrsu_emp AS e ON pr.id_emp = e.id_emp " +
                "WHERE pre.fk_tp_ben = 21 AND e.b_act AND NOT p.b_del AND NOT pr.b_del AND NOT pre.b_del AND pre.ben_year = YEAR('" + dateEnd + "') - 1 " +
                "GROUP BY pre.id_emp , pre.ben_year , pre.ben_ann " +
                "UNION SELECT prd.id_emp, prd.ben_year, prd.ben_ann, - SUM(prd.unt) AS ben_unt, - SUM(prd.amt_r) AS ben_amt " +
                "FROM hrs_pay AS p " +
                "INNER JOIN hrs_pay_rcp AS pr ON pr.id_pay = p.id_pay " +
                "INNER JOIN hrs_pay_rcp_ded AS prd ON prd.id_pay = pr.id_pay AND prd.id_emp = pr.id_emp " +
                "INNER JOIN erp.hrsu_emp AS e ON pr.id_emp = e.id_emp " +
                "INNER JOIN hrs_emp_log_sal_ssc AS va ON va.id_emp = e.id_emp " +
                "INNER JOIN erp.bpsu_bp AS bp ON va.id_emp = bp.id_bp " +
                "INNER JOIN erp.usru_usr AS ui ON va.fk_usr_ins = ui.id_usr " +
                "INNER JOIN erp.usru_usr AS uu ON va.fk_usr_upd = uu.id_usr " +
                "WHERE prd.fk_tp_ben = 21 AND e.b_act AND NOT p.b_del AND NOT pr.b_del AND NOT prd.b_del AND prd.ben_year = YEAR('" + dateEnd + "') - 1 " +
                "GROUP BY prd.id_emp , prd.ben_year , prd.ben_ann " +
                "ORDER BY id_emp , ben_year , ben_ann) AS t " +
                "GROUP BY id_emp , ben_year , ben_ann " +
                "ORDER BY id_emp , ben_year , ben_ann) AS tprev ON tprev.id_emp = bp.id_bp " +
                "WHERE e.b_act AND NOT e.b_del AND e.b_act AND bp.id_bp = " + row.getEmployee().getPkEmployeeId() + " AND e.dt_sal_ssc <= '" + dateStart + "' " +
                "ORDER BY bp.bp , bp.id_bp " +
                "LIMIT 1";
            
            Statement statement = miClient.getSession().getStatement().getConnection().createStatement();
            resultSet = statement.executeQuery(sql);
            earningExtra = 0;
            int auxEarning = 0;

            if (resultSet.next()) {
                for( auxEarning = 0; auxEarning <= rows.get(auxEmployee).getSbcEarnings().size()-1; auxEarning++) {
                earningExtra = earningExtra + rows.get(auxEmployee).getSbcEarnings().get(auxEarning).AmountTaxed;
                }

                auxEmployee++;
                auxEarning++;

                row.getEmployee().getXtaEmployeeName();
                row.getEmployee().getNumber();
                row.getEmployee().getDateBenefits();
                row.getEmployee().getPkEmployeeId();
                row.getEmployee().getFkPaymentTypeId();
                row.setVacationsDays(resultSet.getInt("VacationDays"));
                row.setVacationsBonus(resultSet.getDouble("primavacaional"));
                row.setAnnualBonusDays(resultSet.getDouble("AnnualBonusDays"));
                row.setSscFactor(resultSet.getDouble("SscFactor"));
                row.setDailyIncome(resultSet.getDouble("salarioDiario"));
                row.setSscCurrent(resultSet.getDouble("SscCurrent")); 
                row.setSscLastUpdate(resultSet.getDate("SscLastUpdate"));
                row.setSscRaw(resultSet.getDouble("SscRaw"));
                row.setSscRaw(resultSet.getDouble("SscRaw"));
                row.setSalaryDifferent(resultSet.getDouble("SscRaw")-resultSet.getDouble("SscCurrent"));
                row.setPeriodDays(SLibTimeUtils.countPeriodDays(dateLayoudStart, dateLayoutEnd));
                row.setVariableIncome(earningExtra);
                row.computeSbc();
            }
        }
            
        moGridSbcEmployees.populateGrid(new Vector<>(rows));
        moGridSbcEmployees.setSelectedGridRow(0);
        refreshSbcEmployeesLabel();
        
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
    
    private void reset() {        
        for (SGridRow row : moGridSbcEmployees.getModel().getGridRows()) {
            SRowEmployeeSsc rowEmployeeSsc = (SRowEmployeeSsc) row;
            rowEmployeeSsc.setAbsenceEffectiveDaysFinal(rowEmployeeSsc.getAbsenceEffectiveDaysSuggested());
            rowEmployeeSsc.setSscSuggested(rowEmployeeSsc.getSscFinal());
            rowEmployeeSsc.setRowSelected(false);
        }
        
        moGridSbcEmployees.renderGridRows();
        moGridSbcEmployees.setSelectedGridRow(0);
        refreshSbcEmployeesLabel();
    }
    
    private void employeeSelectAll(boolean selected) {
        for (SGridRow row : moGridSbcEmployees.getModel().getGridRows()) {
            SRowEmployeeSsc rowEmployeeSsc = (SRowEmployeeSsc) row;
            rowEmployeeSsc.setRowSelected(selected);
        }
        
        moGridSbcEmployees.renderGridRows();
        moGridSbcEmployees.setSelectedGridRow(0);
        refreshSbcEmployeesLabel();
    }
    
    public void refreshSbcEmployeesLabel() {
        int selected = 0;
        
        for (SGridRow row : moGridSbcEmployees.getModel().getGridRows()) {
            SRowEmployeeSsc rowEmployeeSsc = (SRowEmployeeSsc) row;
            if (rowEmployeeSsc.isRowSelected()) {
                selected++;
            }
        }
        
        jlSbcEmployeesLabel.setText("Empleados: " + moGridSbcEmployees.getModel().getRowCount() + "; Seleccionados: " + selected);
    }
    
    private void editingStopedSbcEmployees() {
        int selectedRow = moGridSbcEmployees.getTable().getSelectedRow();
        
        moGridSbcEmployees.renderGridRows();
        
        if (selectedRow != -1) {
            moGridSbcEmployees.getTable().setRowSelectionInterval(selectedRow, selectedRow);
        }
        
        refreshSbcEmployeesLabel();
    }
    
    private void actionDateSalaryChange() {
        miClient.getGuiDatePickerXXX().pickDate(moFieldDateChangeSalary.getDate(), moFieldDateChangeSalary);
    }
    
    public void save(String Date) throws ParseException {
        SDbMassiveSalarySscBase massiveSalarySscBase = new SDbMassiveSalarySscBase();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaDate = format.parse(jftDateChangeSsc.getText());
        
        for (SGridRow row : moGridSbcEmployees.getModel().getGridRows()) {
            SRowEmployeeSsc rowEmployeeSbc = (SRowEmployeeSsc) row;
            
            if (rowEmployeeSbc.isRowSelected()) {
                massiveSalarySscBase.getSalarySscBases().add(rowEmployeeSbc.createSalarySscBase(miClient.getSession(), fechaDate));
            }
        }
        
        miClient.getSession().saveRegistry(massiveSalarySscBase);
        miClient.showMsgBoxInformation(SLibConsts.MSG_PROCESS_FINISHED);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbDateChangeSscPicker;
    private javax.swing.JButton jbSave;
    private javax.swing.JFormattedTextField jftDateChangeSsc;
    private javax.swing.JFormattedTextField jftDateCut;
    private javax.swing.JLabel jlBimestre;
    private javax.swing.JLabel jlDateChangeSsc;
    private javax.swing.JLabel jlDateCut;
    private javax.swing.JLabel jlSbcEmployeesLabel;
    private javax.swing.JLabel jlYear;
    private javax.swing.JPanel jpEmployeeSBC;
    private javax.swing.JPanel jpFilter;
    private javax.swing.JTextField jtfBimestre;
    private javax.swing.JTextField jtfYear;
    // End of variables declaration//GEN-END:variables

    @Override
    public void formClearRegistry() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void formReset() {
        moFieldDateChangeSalary.setDate(miClient.getSession().getCurrentDate());
    }

    @Override
    public void formRefreshCatalogues() {
    }

    @Override
    public erp.lib.form.SFormValidation formValidate() {
        SFormValidation validation = new SFormValidation();
        boolean save = true;
        Cursor cursor = null;
        SRowEmployeeSsc row = null;
        ArrayList<SRowEmployeeSsc> SRowEmployeeSbc = null;

        try {
            SRowEmployeeSbc = new ArrayList<>();
            cursor = getCursor();
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
            SRowEmployeeSbc = new ArrayList<SRowEmployeeSsc>();

            for (SGridRow rowAux : moGridSbcEmployees.getModel().getGridRows()) {
                row = (SRowEmployeeSsc) rowAux;
                if (row.isRowSelected()) {
                    if (row.getSscFinal() <= 0) {
                        miClient.showMsgBoxWarning("El valor del SBC debe ser mayor a 0.");
                        validation.setIsError(true);
                        save = false;
                        break;
                    }
                    else {
                        row.setSscLastUpdate(miClient.getSession().getSystemDate());
                        SRowEmployeeSbc.add(row);
                    }
                }
            }
            if (validation.getIsError() == false ) {
                if (SRowEmployeeSbc.isEmpty()) {
                    miClient.showMsgBoxWarning("No se ha seleccionado ningún empleado para actualizar.");
                    validation.setIsError(true);

                }
            }
            if ("  /  /    ".equals(jftDateChangeSsc.getText())) {
                miClient.showMsgBoxWarning("No se ha seleccionado un fecha de actualización");
                validation.setIsError(true);
            }
        }
        catch (Exception e) {
            setCursor(cursor);
            SLibUtils.showException(this, e);
        }
        finally {
            setCursor(cursor);
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
        int i = 0;        
    }

    @Override
    public erp.lib.data.SDataRegistry getRegistry() {
        return null;
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

            if (button == jbSave) {
                try {
                    actionSave();
                } catch (ParseException ex) {
                    Logger.getLogger(SFormMassiveUpdateSsc.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if (button == jbCancel) {
                actionCancel();
            }
            else if (button == mjbSelectAll) {
                employeeSelectAll(true);
            }
            else if (button == mjbDeselectAll) {
                employeeSelectAll(false);
            }
            else if (button == mjbReset) {
                reset();
            }
             else if (button == jbDateChangeSscPicker) {
                actionDateSalaryChange();
            }
        }
    }

    @Override
    public void editingStopped(ChangeEvent ce) {
        editingStopedSbcEmployees();
    }

    @Override
    public void editingCanceled(ChangeEvent ce) {
        
    }
}
