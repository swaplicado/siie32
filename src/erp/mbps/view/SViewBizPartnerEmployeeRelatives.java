/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mbps.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.gui.grid.SGridFilterPanelEmployee;
import erp.lib.SLibConstants;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.mbps.data.SDataEmployee;
import erp.mbps.form.SFormBizPartnerEmployeeRelativesPhoto;
import erp.mcfg.data.SCfgUtils;
import erp.mod.SModConsts;
import erp.table.SFilterConstants;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JToggleButton;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiItem;

/**
 *
 * @author Sergio Flores, Isabel Servín, Claudio Peña, Sergio Flores
 */
public class SViewBizPartnerEmployeeRelatives extends erp.lib.table.STableTab implements java.awt.event.ActionListener, java.awt.event.ItemListener {

    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;
    private javax.swing.JToggleButton jtbViewEmployeeActive;
    private javax.swing.JToggleButton jtbViewEmployeeInactive;
    private javax.swing.JToggleButton jtbViewEmployeeAll;
    private javax.swing.ButtonGroup bgViewEmployee;
    private javax.swing.JComboBox jcbFilterDepartament;
    private javax.swing.JComboBox jcbFilterPaymentType;
    private javax.swing.JButton jbClearFilterDepartament;
    private javax.swing.JButton jbClearFilterPaymentType;
    private javax.swing.JButton jbShowPhoto;
    
    private java.lang.String msOrderKey;

    private int mnFilterPaymentTypeId;
    private int mnFilterDepartamentId;
    private int mnFilterStatusEmployee;

    public SViewBizPartnerEmployeeRelatives(erp.client.SClientInterface client, java.lang.String tabTitle) {
        super(client, tabTitle, SDataConstants.BPSX_BP_EMP_REL);
        initComponents();
    }

    private void initComponents() {
        int levelRightEdit = SDataConstantsSys.UNDEFINED;
        int levelRightEditCategory = SDataConstantsSys.UNDEFINED;
        boolean employeesCrudEnabled = false;

        try {
            employeesCrudEnabled = SLibUtils.parseInt(SCfgUtils.getParamValue(miClient.getSession().getStatement(), SDataConstantsSys.CFG_PARAM_HRS_EMPLOYEES_CRUD)) == 1;
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
        
        moTabFilterDeleted = new STabFilterDeleted(this);
        bgViewEmployee = new ButtonGroup();
        
        jtbViewEmployeeActive = new JToggleButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_po_act_off.gif")));
        jtbViewEmployeeActive.setPreferredSize(new Dimension(23, 23));
        jtbViewEmployeeActive.addActionListener(this);
        jtbViewEmployeeActive.setToolTipText("Ver activos");
        bgViewEmployee.add(jtbViewEmployeeActive);
        
        jtbViewEmployeeActive.setSelected(true);
        
        jtbViewEmployeeInactive = new JToggleButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_po_ina_off.gif")));
        jtbViewEmployeeInactive.setPreferredSize(new Dimension(23, 23));
        jtbViewEmployeeInactive.addActionListener(this);
        jtbViewEmployeeInactive.setToolTipText("Ver inactivos");
        bgViewEmployee.add(jtbViewEmployeeInactive);
        
        jtbViewEmployeeAll = new JToggleButton(new ImageIcon(getClass().getResource("/erp/img/switch_filter_off.gif")));
        jtbViewEmployeeAll.setPreferredSize(new Dimension(23, 23));
        jtbViewEmployeeAll.addActionListener(this);
        jtbViewEmployeeAll.setToolTipText("Ver todos");
        bgViewEmployee.add(jtbViewEmployeeAll);
        
        jcbFilterDepartament= new javax.swing.JComboBox();
        jcbFilterDepartament.setToolTipText("Filtrar departamento");
        jcbFilterDepartament.setPreferredSize(new java.awt.Dimension(250, 23));
        
        jbClearFilterDepartament = SGridUtils.createButton(new ImageIcon(getClass().getResource("/sa/lib/img/cmd_std_delete_tmp.gif")), "Quitar filtro departamento", this);
        
        jcbFilterPaymentType = new javax.swing.JComboBox();
        jcbFilterPaymentType.setToolTipText("Filtrar periodo pago");
        jcbFilterPaymentType.setPreferredSize(new java.awt.Dimension(125, 23));
        
        jbClearFilterPaymentType = SGridUtils.createButton(new ImageIcon(getClass().getResource("/sa/lib/img/cmd_std_delete_tmp.gif")), "Quitar filtro periodo pago", this);
        
        jbShowPhoto = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_print_photo.gif")), "Ver foto", this);
        
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);
        
        addTaskBarUpperComponent(jcbFilterDepartament);
        addTaskBarUpperComponent(jbClearFilterDepartament);

        addTaskBarLowerComponent(jtbViewEmployeeActive);
        addTaskBarLowerComponent(jtbViewEmployeeInactive);
        addTaskBarLowerComponent(jtbViewEmployeeAll);
        addTaskBarLowerComponent(jcbFilterPaymentType);
        addTaskBarLowerComponent(jbClearFilterPaymentType);
        
        addTaskBarLowerSeparator();
        addTaskBarLowerComponent(jbShowPhoto);

        msOrderKey = "bp.bp, bp.id_bp ";

        int field = 0;
        STableField[] aoKeyFields = new STableField[1];
        aoKeyFields[field++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "bp.id_bp");
        
        for (field = 0; field < aoKeyFields.length; field++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[field]);
        }

        int column = 0;
        STableColumn[] aoTableColumns = new STableColumn[77];
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Empleado", 250);
        aoTableColumns[column] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "_emp_num", "Clave", 50);
        aoTableColumns[column++].setCellRenderer(SGridUtils.CellRendererIntegerRaw);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "e.b_act", "Activo", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "pay.name", "Período pago", 100);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.fiscal_id", "RFC", 100);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.alt_id", "CURP", 150);
        aoTableColumns[column] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "e.ssn", "NSS", 75);
        aoTableColumns[column++].setApostropheOnCsvRequired(true);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "e.dt_bir", "Nacimiento empleado", STableConstants.WIDTH_DATE);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_FLOAT, "_e_age", "Edad empleado", STableConstants.WIDTH_NUM_SMALLINT);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "e.dt_ben", "Inicio beneficios", STableConstants.WIDTH_DATE);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_FLOAT, "_e_set", "Antigüedad", STableConstants.WIDTH_NUM_SMALLINT);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "e.dt_hire", "Última alta", STableConstants.WIDTH_DATE);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "e.dt_dis_n", "Última baja", STableConstants.WIDTH_DATE);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dep.name", "Departamento", 100);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "pos.name", "Puesto", 100);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "emp.name", "Tipo empleado", 100);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "wrk.name", "Tipo obrero", 100);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "addr.street", "Calle", 150);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "addr.street_num_ext", "No. exterior", 50);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "addr.street_num_int", "No. interior", 50);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "addr.neighborhood", "Colonia", 150);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "addr.zip_code", "CP", 50);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "addr.reference", "Referencia (teléfono)", 100);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "addr.locality", "Localidad", 150);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "addr.county", "Municipio", 100);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "addr.state", "Estado", 100);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_cty_abbr", "País", 50);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "e.b_mfg_ope", "Operador", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "e.b_uni", "Sindicalizado", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bp.b_att_par_shh", "Socio/accionista", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bp.b_att_rel_pty", "Parte relacionada", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "csex.name", "Sexo empleado", 75);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cblo.name", "Grupo sanguíneo empleado", 75);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cedu.name", "Escolaridad empleado", 75);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cmar.name", "Estado civil empleado", 75);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "_with_img_pho", "Foto", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "_with_img_sig", "Firma", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "e.place_bir", "Lugar nacimiento", 150);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "e.umf", "UMF", 50);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "er.mate", "Cónyuge", 200);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "er.mate_dt_bir_n", "Cónyuge nacimiento", STableConstants.WIDTH_DATE);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_FLOAT, "_mate_age", "Cónyuge edad", STableConstants.WIDTH_NUM_SMALLINT);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cmate.name", "Cónyuge sexo", 75);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "er.b_mate_dec", "Cónyuge finado", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "_parent", "Es mamá/papá", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "er.son_1", "Hijo 1", 200);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "er.son_dt_bir_1_n", "Hijo 1 nacimiento", STableConstants.WIDTH_DATE);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_FLOAT, "_son1_age", "Hijo 1 edad", STableConstants.WIDTH_NUM_SMALLINT);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cson1.name", "Hijo 1 sexo", 75);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "er.b_son_dec_1", "Hijo 1 finado", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "er.son_2", "Hijo 2", 200);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "er.son_dt_bir_2_n", "Hijo 2 nacimiento", STableConstants.WIDTH_DATE);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_FLOAT, "_son2_age", "Hijo 2 edad", STableConstants.WIDTH_NUM_SMALLINT);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cson2.name", "Hijo 2 sexo", 75);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "er.b_son_dec_2", "Hijo 2 finado", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "er.son_3", "Hijo 3", 200);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "er.son_dt_bir_3_n", "Hijo 3 nacimiento", STableConstants.WIDTH_DATE);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_FLOAT, "_son3_age", "Hijo 3 edad", STableConstants.WIDTH_NUM_SMALLINT);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cson3.name", "Hijo 3 sexo", 75);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "er.b_son_dec_3", "Hijo 3 finado", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "er.son_4", "Hijo 4", 200);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "er.son_dt_bir_4_n", "Hijo 4 nacimiento", STableConstants.WIDTH_DATE);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_FLOAT, "_son4_age", "Hijo 4 edad", STableConstants.WIDTH_NUM_SMALLINT);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cson4.name", "Hijo 4 sexo", 75);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "er.b_son_dec_4", "Hijo 4 finado", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "er.son_5", "Hijo 5", 200);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "er.son_dt_bir_5_n", "Hijo 5 nacimiento", STableConstants.WIDTH_DATE);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_FLOAT, "_son5_age", "Hijo 5 edad", STableConstants.WIDTH_NUM_SMALLINT);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cson5.name", "Hijo 5 sexo", 50);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "er.b_son_dec_5", "Hijo 5 finado", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bp.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "bp.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "bp.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[column++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "bp.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);
        
        for (column = 0; column < aoTableColumns.length; column++) {
            moTablePane.addTableColumn(aoTableColumns[column]);
        }

        levelRightEdit = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BP).Level;
        boolean isPermissionEdit = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_EMP_PERS_DATA).HasRight;

        jbNew.setEnabled(false);
        jbEdit.setEnabled(((levelRightEditCategory >= SUtilConsts.LEV_AUTHOR || levelRightEdit >= SUtilConsts.LEV_AUTHOR) && employeesCrudEnabled) || !isPermissionEdit);
        jbDelete.setEnabled(false);
        mvSuscriptors.add(SDataConstants.USRU_USR);
        mvSuscriptors.add(SDataConstants.BPSX_BP_EMP);
        mvSuscriptors.add(SModConsts.HRS_EMP_LOG_HIRE);
        mvSuscriptors.add(SModConsts.HRSU_TP_EMP);
        mvSuscriptors.add(SModConsts.HRSU_TP_WRK);
        mvSuscriptors.add(SModConsts.HRSU_TP_MWZ);
        mvSuscriptors.add(SModConsts.HRSU_DEP);
        mvSuscriptors.add(SModConsts.HRSU_POS);
        mvSuscriptors.add(SModConsts.HRSU_SHT);

        populateTable();
        updateOptions();
        itemStateChangedViewEmployee();
    }
    
    private void actionClearFilterPaymentType() {
        jbClearFilterPaymentType.setEnabled(false);
        jcbFilterPaymentType.setSelectedIndex(0);
        jcbFilterPaymentType.requestFocus();
        mnFilterPaymentTypeId = SLibConsts.UNDEFINED;
        
        try {
            populateTable();
        }
        catch (Exception e) {
            SLibUtils.printException(this, e);
        }
    }
    
    private void actionClearFilterDepartament() {
        jbClearFilterDepartament.setEnabled(false);
        jcbFilterDepartament.setSelectedIndex(0);
        jcbFilterDepartament.requestFocus();
        mnFilterDepartamentId = SLibConsts.UNDEFINED;
        
        try {
            populateTable();
        }
        catch (Exception e) {
            SLibUtils.printException(this, e);
        }
    }
    
    private void actionShowPhoto() {
        if (jbShowPhoto.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                SDataEmployee employee = (SDataEmployee) SDataUtilities.readRegistry(miClient, SModConsts.HRSU_EMP, (int[]) moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);
                SFormBizPartnerEmployeeRelativesPhoto photo = new SFormBizPartnerEmployeeRelativesPhoto(employee, (String)moTablePane.getSelectedTableRow().getValues().firstElement());
                photo.setVisible(true);
            }
        }
    }
    
    private void itemStateChangedViewEmployee() {
        if (jtbViewEmployeeActive.isSelected()) {
            mnFilterStatusEmployee = SGridFilterPanelEmployee.EMP_STATUS_ACT;
            jtbViewEmployeeActive.setSelectedIcon(new ImageIcon(getClass().getResource("/erp/img/icon_std_po_act_on.gif")));
        }
        else if (jtbViewEmployeeInactive.isSelected()) {
            mnFilterStatusEmployee = SGridFilterPanelEmployee.EMP_STATUS_INA;
            jtbViewEmployeeInactive.setSelectedIcon(new ImageIcon(getClass().getResource("/erp/img/icon_std_po_ina_on.gif")));
        }
        else if (jtbViewEmployeeAll.isSelected()) {
            mnFilterStatusEmployee = SGridFilterPanelEmployee.EMP_STATUS_ALL;
            jtbViewEmployeeAll.setSelectedIcon(new ImageIcon(getClass().getResource("/erp/img/switch_filter_on.gif")));
        }
        populateTable();
    }
    
    private void itemStateChangedFilterPaymentType() {
        try {
            mnFilterPaymentTypeId = (((SGuiItem) jcbFilterPaymentType.getSelectedItem()).getPrimaryKey().length == 0 ? SLibConsts.UNDEFINED : ((SGuiItem) jcbFilterPaymentType.getSelectedItem()).getPrimaryKey()[0]);
            jbClearFilterPaymentType.setEnabled(jcbFilterPaymentType.getSelectedIndex() > 0);
            populateTable();
        }
        catch (Exception e) {
            SLibUtils.printException(this, e);
        }
    }
    
    private void itemStateChangedFilterDepartament() {
        try {
            mnFilterDepartamentId = (((SGuiItem) jcbFilterDepartament.getSelectedItem()).getPrimaryKey().length == 0 ? SLibConsts.UNDEFINED : ((SGuiItem) jcbFilterDepartament.getSelectedItem()).getPrimaryKey()[0]);
            jbClearFilterDepartament.setEnabled(jcbFilterDepartament.getSelectedIndex() > 0);
            populateTable();
        }
        catch (Exception e) {
            SLibUtils.printException(this, e);
        }
    }
    
    private void updateOptions() {
        jcbFilterPaymentType.removeItemListener(this);
        jcbFilterDepartament.removeItemListener(this);
        jtbViewEmployeeActive.removeItemListener(this);
        jtbViewEmployeeInactive.removeItemListener(this);
        jtbViewEmployeeAll.removeItemListener(this);
        
        miClient.getSession().populateCatalogue(jcbFilterPaymentType, SModConsts.HRSS_TP_PAY, SLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(jcbFilterDepartament, SModConsts.HRSU_DEP, SLibConsts.UNDEFINED, null);
        
        actionClearFilterPaymentType();
        actionClearFilterDepartament();
        
        jcbFilterPaymentType.addItemListener(this);
        jcbFilterDepartament.addItemListener(this);
        jtbViewEmployeeActive.addItemListener(this);
        jtbViewEmployeeInactive.addItemListener(this);
        jtbViewEmployeeAll.addItemListener(this);
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {

        }
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (miClient.getGuiModule(SDataConstants.GLOBAL_CAT_BPS).showForm(SDataConstants.BPSX_BP_EMP, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(SDataConstants.GLOBAL_CAT_BPS).refreshCatalogues(SDataConstants.BPSX_BP_EMP);
                }
            }
        }
    }

    @Override
    public void actionDelete() {
        if (jbDelete.isEnabled()) {

        }
    }

    @Override
    public void createSqlQuery() {
        String sqlWhere = "";
        String sqlBizPartner = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "bp.b_del = 0 ";
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_BP) {
                if ((Integer) setting.getSetting() != SLibConstants.UNDEFINED) {
                    sqlBizPartner += " AND e.id_emp = " + (Integer) setting.getSetting() + " ";
                }
            }
        }

        sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "bp.b_att_emp = 1 ";

        msSql = "SELECT bp.id_bp, bp.bp, bp.fiscal_id, bp.alt_id, bp.b_att_par_shh, bp.b_att_rel_pty, bp.b_del, " +
                "CAST(e.num AS UNSIGNED INTEGER) AS _emp_num, e.ssn, e.dt_bir, e.dt_ben, e.dt_hire, e.dt_dis_n, e.b_act, e.b_mfg_ope, e.b_uni, e.place_bir, e.umf, " +
                "addr.street, addr.street_num_ext, addr.street_num_int, addr.neighborhood, addr.locality, addr.county, addr.state, addr.zip_code, addr.reference, " + 
                "img_pho_n IS NOT NULL AS _with_img_pho, img_sig_n IS NOT NULL AS _with_img_sig, " +
                "bp.fid_usr_new, bp.fid_usr_edit, bp.fid_usr_del, bp.ts_new, bp.ts_edit, bp.ts_del, un.usr, ue.usr, ud.usr, " +
                "pay.name, emp.name, wrk.name, dep.name, pos.name, " +
                "COALESCE(cty.cty_abbr, '" + miClient.getSession().getSessionCustom().getLocalCountryCode() + "') AS _cty_abbr, " +
                "csex.name, cblo.name, cmar.name, cedu.name, " +
                "er.mate, er.mate_dt_bir_n, er.b_mate_dec, cmate.name, " +
                "er.son_1, er.son_dt_bir_1_n, er.b_son_dec_1, cson1.name, " +
                "er.son_2, er.son_dt_bir_2_n, er.b_son_dec_2, cson2.name, " +
                "er.son_3, er.son_dt_bir_3_n, er.b_son_dec_3, cson3.name, " +
                "er.son_4, er.son_dt_bir_4_n, er.b_son_dec_4, cson4.name, " +
                "er.son_5, er.son_dt_bir_5_n, er.b_son_dec_5, cson5.name, " +
                "PERIOD_DIFF(DATE_FORMAT(NOW(), '%Y%m'), DATE_FORMAT(e.dt_bir, '%Y%m')) / 12 AS _e_age, " +
                "PERIOD_DIFF(DATE_FORMAT(NOW(), '%Y%m'), DATE_FORMAT(e.dt_ben, '%Y%m')) / 12 AS _e_set, " +
                "PERIOD_DIFF(DATE_FORMAT(NOW(), '%Y%m'), DATE_FORMAT(er.mate_dt_bir_n, '%Y%m')) / 12 AS _mate_age, " +
                "PERIOD_DIFF(DATE_FORMAT(NOW(), '%Y%m'), DATE_FORMAT(er.son_dt_bir_1_n, '%Y%m')) / 12 AS _son1_age, " +
                "PERIOD_DIFF(DATE_FORMAT(NOW(), '%Y%m'), DATE_FORMAT(er.son_dt_bir_2_n, '%Y%m')) / 12 AS _son2_age, " +
                "PERIOD_DIFF(DATE_FORMAT(NOW(), '%Y%m'), DATE_FORMAT(er.son_dt_bir_3_n, '%Y%m')) / 12 AS _son3_age, " +
                "PERIOD_DIFF(DATE_FORMAT(NOW(), '%Y%m'), DATE_FORMAT(er.son_dt_bir_4_n, '%Y%m')) / 12 AS _son4_age, " +
                "PERIOD_DIFF(DATE_FORMAT(NOW(), '%Y%m'), DATE_FORMAT(er.son_dt_bir_5_n, '%Y%m')) / 12 AS _son5_age, " +
                "(er.son_1 <> '' OR er.son_2 <> '' OR er.son_3 <> '' OR er.son_4 <> '' OR er.son_5 <> '') _parent " +
                "FROM erp.bpsu_bp AS bp " +
                "INNER JOIN hrs_emp_member AS em ON bp.id_bp = em.id_emp " +
                "INNER JOIN erp.usru_usr AS un ON bp.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON bp.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON bp.fid_usr_del = ud.id_usr " +
                "INNER JOIN erp.bpsu_bpb AS bpb ON bpb.fid_bp = bp.id_bp " +
                "INNER JOIN erp.bpsu_bpb_add AS addr ON bpb.id_bpb = addr.id_bpb AND addr.id_add = 1 " +
                "LEFT OUTER JOIN erp.hrsu_emp AS e ON bp.id_bp = e.id_emp " +
                "LEFT OUTER JOIN erp.hrss_tp_pay AS pay ON e.fk_tp_pay = pay.id_tp_pay " +
                "LEFT OUTER JOIN erp.hrsu_tp_emp AS emp ON e.fk_tp_emp = emp.id_tp_emp " +
                "LEFT OUTER JOIN erp.hrsu_tp_wrk AS wrk ON e.fk_tp_wrk = wrk.id_tp_wrk " +
                "LEFT OUTER JOIN erp.hrsu_dep AS dep ON e.fk_dep = dep.id_dep " +
                "LEFT OUTER JOIN erp.hrsu_pos AS pos ON e.fk_pos = pos.id_pos " +
                "LEFT OUTER JOIN erp.locu_cty AS cty ON addr.fid_cty_n = cty.id_cty " +
                "LEFT OUTER JOIN erp.hrsu_emp_rel AS er ON e.id_emp = er.id_emp " +
                "LEFT OUTER JOIN erp.hrss_tp_hrs_cat AS csex ON e.fk_cl_cat_sex = csex.id_cl_hrs_cat AND e.fk_tp_cat_sex = csex.id_tp_hrs_cat " +
                "LEFT OUTER JOIN erp.hrss_tp_hrs_cat AS cblo ON e.fk_cl_cat_blo = cblo.id_cl_hrs_cat AND e.fk_tp_cat_blo = cblo.id_tp_hrs_cat " +
                "LEFT OUTER JOIN erp.hrss_tp_hrs_cat AS cmar ON e.fk_cl_cat_mar = cmar.id_cl_hrs_cat AND e.fk_tp_cat_mar = cmar.id_tp_hrs_cat " +
                "LEFT OUTER JOIN erp.hrss_tp_hrs_cat AS cedu ON e.fk_cl_cat_edu = cedu.id_cl_hrs_cat AND e.fk_tp_cat_edu = cedu.id_tp_hrs_cat " +
                "LEFT OUTER JOIN erp.hrss_tp_hrs_cat AS cmate ON er.fk_cl_cat_sex_mate = cmate.id_cl_hrs_cat AND er.fk_tp_cat_sex_mate = cmate.id_tp_hrs_cat " +
                "LEFT OUTER JOIN erp.hrss_tp_hrs_cat AS cson1 ON er.fk_cl_cat_sex_son_1 = cson1.id_cl_hrs_cat AND er.fk_tp_cat_sex_son_1 = cson1.id_tp_hrs_cat " +
                "LEFT OUTER JOIN erp.hrss_tp_hrs_cat AS cson2 ON er.fk_cl_cat_sex_son_2 = cson2.id_cl_hrs_cat AND er.fk_tp_cat_sex_son_2 = cson2.id_tp_hrs_cat " +
                "LEFT OUTER JOIN erp.hrss_tp_hrs_cat AS cson3 ON er.fk_cl_cat_sex_son_3 = cson3.id_cl_hrs_cat AND er.fk_tp_cat_sex_son_3 = cson3.id_tp_hrs_cat " +
                "LEFT OUTER JOIN erp.hrss_tp_hrs_cat AS cson4 ON er.fk_cl_cat_sex_son_4 = cson4.id_cl_hrs_cat AND er.fk_tp_cat_sex_son_4 = cson4.id_tp_hrs_cat " +
                "LEFT OUTER JOIN erp.hrss_tp_hrs_cat AS cson5 ON er.fk_cl_cat_sex_son_5 = cson5.id_cl_hrs_cat AND er.fk_tp_cat_sex_son_5 = cson5.id_tp_hrs_cat " +
                (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) + sqlBizPartner + 
                (mnFilterPaymentTypeId == SLibConsts.UNDEFINED ? "" : "AND e.fk_tp_pay = " + mnFilterPaymentTypeId + " ") +
                (mnFilterDepartamentId == SLibConsts.UNDEFINED ? "" : "AND e.fk_dep = " + mnFilterDepartamentId + " ") +
                (mnFilterStatusEmployee == SLibConsts.UNDEFINED ? "" : (mnFilterStatusEmployee == SGridFilterPanelEmployee.EMP_STATUS_ACT ? "AND e.b_act = 1 " : (mnFilterStatusEmployee == SGridFilterPanelEmployee.EMP_STATUS_INA ? "AND e.b_act = 0 " : ""))) +
                "ORDER BY " + msOrderKey;
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbClearFilterPaymentType) {
                actionClearFilterPaymentType();
            }
            else if (button == jbClearFilterDepartament) {
                actionClearFilterDepartament();
            }
            else if (button == jbShowPhoto) {
                actionShowPhoto();
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof JComboBox) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                JComboBox comboBox = (JComboBox) e.getSource();

                if (comboBox == jcbFilterPaymentType) {
                    itemStateChangedFilterPaymentType();
                }
                else if (comboBox == jcbFilterDepartament) {
                    itemStateChangedFilterDepartament();
                }
            }
        }
        else if (e.getSource() instanceof JToggleButton) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                JToggleButton toggleButton = (JToggleButton) e.getSource();
                
                if (toggleButton == jtbViewEmployeeActive) {
                    itemStateChangedViewEmployee();
                }
                else if (toggleButton == jtbViewEmployeeInactive) {
                    itemStateChangedViewEmployee();
                }
                else if (toggleButton == jtbViewEmployeeAll) {
                    itemStateChangedViewEmployee();
                }
            }
        }
    }
}
