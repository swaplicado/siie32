/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.form;

import erp.mcfg.data.SDataParamsErp;
import erp.mod.SModConsts;
import java.awt.BorderLayout;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanDialogReport;

/**
 * Genera el reporte de posiciones y vacantes.
 * @author Isabel Servín
 */
public class SDialogRepHrsPos extends SBeanDialogReport {
   
    private HashSet<String> dbCompaniesWithEmpCrud;
    private SPanelHrsDepartments moPanelHrsDepartments;
    
    /**
     * Creates new form SDialogRepHrsPos
     * @param client
     * @param title
     */
    public SDialogRepHrsPos(SGuiClient client, String title) {
        setFormSettings(client, SModConsts.HRSR_POS, SLibConsts.UNDEFINED, title);
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

        moGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        moRadDetail = new sa.lib.gui.bean.SBeanFieldRadio();
        moRadSummary = new sa.lib.gui.bean.SBeanFieldRadio();
        jPanel19 = new javax.swing.JPanel();
        jpDepartments = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jlDate = new javax.swing.JLabel();
        jtfDate = new javax.swing.JTextField();

        setPreferredSize(new java.awt.Dimension(800, 450));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Parámetros del reporte:"));
        jPanel1.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel4.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Modalidad:"));
        jPanel8.setLayout(new java.awt.GridLayout(2, 0, 0, 5));

        moGroup1.add(moRadDetail);
        moRadDetail.setText("Detalle");
        jPanel8.add(moRadDetail);

        moGroup1.add(moRadSummary);
        moRadSummary.setText("Resumen");
        jPanel8.add(moRadSummary);

        jPanel4.add(jPanel8, java.awt.BorderLayout.NORTH);

        jPanel19.setLayout(new java.awt.BorderLayout());

        jpDepartments.setBorder(javax.swing.BorderFactory.createTitledBorder("Departamentos:"));
        jpDepartments.setLayout(new java.awt.BorderLayout());
        jPanel19.add(jpDepartments, java.awt.BorderLayout.CENTER);

        jPanel4.add(jPanel19, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel4, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDate.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jlDate.setText("Fecha de corte: ");
        jPanel2.add(jlDate);

        jtfDate.setEditable(false);
        jtfDate.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel2.add(jtfDate);

        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JLabel jlDate;
    private javax.swing.JPanel jpDepartments;
    private javax.swing.JTextField jtfDate;
    private javax.swing.ButtonGroup moGroup1;
    private sa.lib.gui.bean.SBeanFieldRadio moRadDetail;
    private sa.lib.gui.bean.SBeanFieldRadio moRadSummary;
    // End of variables declaration//GEN-END:variables
    
    
    
    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 720, 450);
        moPanelHrsDepartments = new SPanelHrsDepartments(miClient);
        
        jtfDate.setText(SLibUtils.DateFormatDate.format(miClient.getSession().getSystemDate()));
        
        moFields.addField(moRadDetail);
        moFields.addField(moRadSummary);
        
        moFields.setFormButton(jbPrint);
        
        jpDepartments.add(moPanelHrsDepartments, BorderLayout.CENTER);
        
        moRadDetail.setSelected(true);
    }
    
    private void obtainCompanies() {
        try {
            Statement statement = miClient.getSession().getStatement();
            HashSet<String> dbCompaniesWithModHrs = new HashSet<>();
            String sql = "SELECT bd FROM erp.cfgu_co WHERE b_mod_hrs AND NOT b_del;";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                dbCompaniesWithModHrs.add(resultSet.getString(1));
            }
            for (String db : dbCompaniesWithModHrs) {
                sql = "SELECT param_value FROM " + db + ".cfg_param WHERE id_param = 11;";
                resultSet = statement.executeQuery(sql);
                if (resultSet.next()) {
                    if (resultSet.getInt(1) == 1) {
                        dbCompaniesWithEmpCrud.add(db);
                    }
                }
            }
        }
        catch (Exception e) {
            miClient.showMsgBoxError(e.getMessage());
        }
    }
    
    private String obtainQuery() {
        int compCant = dbCompaniesWithEmpCrud.size();
        String departamentsId = (String) moPanelHrsDepartments.getValue(SGuiConsts.PARAM_KEY);
        String sqlWhere = departamentsId.isEmpty() ? "" : " AND d.id_dep IN(" + departamentsId + ") ";
        String sql = compCant > 1 ? "SELECT com, id_dep, departamento, id_pos, puesto, pos, ocupado FROM (" : ""; 
        int i = 0;
        for (String db : dbCompaniesWithEmpCrud) {
            i++;
            sql += "SELECT (SELECT c.co FROM " + db + ".cfg_param_co AS com INNER JOIN erp.cfgu_co AS c ON com.id_co = c.id_co) AS com, "
                + "d.id_dep, d.name AS departamento, p.id_pos, p.name AS puesto, p.pos, COUNT(*) AS ocupado " +
                "FROM erp.hrsu_emp AS e " +
                "INNER JOIN " + db + ".hrs_emp_member AS em ON e.id_emp = em.id_emp " +
                "INNER JOIN erp.hrsu_pos AS p ON e.fk_pos = p.id_pos " +
                "INNER JOIN erp.hrsu_dep AS d ON p.fk_dep = d.id_dep " +
                "WHERE e.b_act AND NOT e.b_del AND NOT p.b_del AND NOT em.b_del " +
                sqlWhere +
                "GROUP BY p.id_pos ";
            sql += i < compCant ? "UNION " : "";
        }
        sql += compCant > 1 ? ") AS a ORDER BY a.id_dep, a.id_pos" : "";
        return sql;
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
    public void createParamsMap() {
        dbCompaniesWithEmpCrud = new HashSet<>();
        obtainCompanies();
        String departamentsName = (String) moPanelHrsDepartments.getValue(SGuiConsts.PARAM_ITEM);
        String query = obtainQuery();
        boolean isSummary = moRadSummary.isSelected();
        
        moParamsMap = miClient.createReportParams();
        
        moParamsMap.put("sEmpresa", ((SDataParamsErp) miClient.getSession().getConfigSystem()).getErp());
        moParamsMap.put("sDepartamentos", departamentsName.isEmpty() || (boolean) moPanelHrsDepartments.getValue(SGuiConsts.PARAM_ROWS) ? "(TODOS)" : departamentsName + " ");
        moParamsMap.put("sConsulta", query);
        moParamsMap.put("bResumen", isSummary);
        moParamsMap.put("tFecha", miClient.getSession().getSystemDate());
    }
}
