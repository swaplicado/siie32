/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mbps.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.gui.grid.SGridFilterPanelEmployee;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.mbps.form.SDialogBizPartnerExport;
import erp.mcfg.data.SCfgUtils;
import erp.mod.SModConsts;
import erp.mod.cfg.db.SSyncType;
import erp.mod.cfg.swap.utils.SExportUtils;
import erp.mod.cfg.swap.utils.SSwapConsts;
import erp.mod.hrs.db.SDbEmployee;
import erp.mod.hrs.db.SDbEmployeeHireLog;
import erp.mod.hrs.db.SHrsConsts;
import erp.mod.hrs.db.SHrsUtils;
import erp.mod.hrs.form.SDialogEmployeeHireLog;
import erp.mod.hrs.form.SDialogEmployerSubstitution;
import erp.table.SFilterConstants;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeConsts;
import sa.lib.SLibUtils;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiItem;

/**
 *
 * @author Alfonso Flores, Claudio Peña, Sergio Flores
 */
public class SViewBizPartner extends erp.lib.table.STableTab implements java.awt.event.ActionListener, java.awt.event.ItemListener {

    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;
    private javax.swing.JButton jbBizPartnerExport;
    private javax.swing.JButton jbStatusEmployeeChange;
    private javax.swing.JButton jbStatusEmployeeModify;
    private javax.swing.JButton jbStatusEmployeeRevert;
    private javax.swing.JToggleButton jtbViewEmployeeActive;
    private javax.swing.JToggleButton jtbViewEmployeeInactive;
    private javax.swing.JToggleButton jtbViewEmployeeAll;
    private javax.swing.ButtonGroup bgViewEmployee;
    private javax.swing.JComboBox jcbFilterDepartament;
    private javax.swing.JComboBox jcbFilterPaymentType;
    private javax.swing.JButton jbClearFilterDepartament;
    private javax.swing.JButton jbClearFilterPaymentType;
    private javax.swing.JButton jbExportDataToSwapServices;
    
    private int mnBizPartnerCategory;
    private java.lang.String msOrderKey;

    private erp.mbps.form.SDialogBizPartnerExport moDialogBizPartnerExport;
    private boolean mbIsViewEmployees;
    private boolean mbIsViewBizPartnersSimple;
    private boolean mbHasRightEmpWage;
    private boolean mbSwapServicesLinkUp;
    
    private int mnFilterPaymentTypeId;
    private int mnFilterDepartamentId;
    private int mnFilterStatusEmployee;

    public SViewBizPartner(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType) {
        super(client, tabTitle, auxType);
        mnTabTypeAux01 = auxType;
        initComponents();
    }

    private void initComponents() {
        int rightLevelBp = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BP).Level;
        int rightLevelBpCatCreate = 0;
        int rightLevelBpCatEdit = 0;
        boolean employeesCrudEnabled = false;
        
        mbIsViewEmployees = SLibUtils.belongsTo(mnTabTypeAux01, new int[] { SDataConstants.BPSX_BP_EMP, SDataConstants.BPSX_BP_EMP_CON_EXP });
        mbIsViewBizPartnersSimple = mbIsViewEmployees || SLibUtils.belongsTo(mnTabTypeAux01, new int[] { SDataConstants.BPSU_BP, SDataConstants.BPSX_BP_ATT_SAL_AGT, SDataConstants.BPSX_BP_ATT_BANK, SDataConstants.BPSX_BP_ATT_CARR });
        mbHasRightEmpWage = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_CAT_EMP_WAGE).HasRight;
        
        moTabFilterDeleted = new STabFilterDeleted(this);
        
        jbBizPartnerExport = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_BP_EXPORT), "Exportar a otra " + (mbIsViewEmployees ? "empresa" : "categoría"), this);
        
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(jbBizPartnerExport);
        
        if (mbIsViewEmployees) {
            try {
                employeesCrudEnabled = SLibUtils.parseInt(SCfgUtils.getParamValue(miClient.getSession().getStatement(), SDataConstantsSys.CFG_PARAM_HRS_EMPLOYEES_CRUD)) == 1;
            }
            catch (Exception e) {
                SLibUtils.showException(this, e);
            }
            
            jcbFilterDepartament = new javax.swing.JComboBox();
            jcbFilterDepartament.setToolTipText("Filtrar departamento");
            jcbFilterDepartament.setPreferredSize(new java.awt.Dimension(250, 23));
            jcbFilterDepartament.setMaximumRowCount(16);
            
            jbClearFilterDepartament = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_delete.gif")), "Quitar filtro departamento", this);
            
            jbStatusEmployeeChange = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_switch.gif")), "Cambiar estatus alta-baja", this);
            jbStatusEmployeeModify = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_edit.gif")), "Modificar última alta o baja", this);
            jbStatusEmployeeRevert = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_move_left.gif")), "Revertir última alta o baja", this);
            
            addTaskBarUpperSeparator();
            addTaskBarUpperComponent(jcbFilterDepartament);
            addTaskBarUpperComponent(jbClearFilterDepartament);
            addTaskBarUpperSeparator();
            addTaskBarUpperComponent(jbStatusEmployeeChange);
            addTaskBarUpperComponent(jbStatusEmployeeModify);
            addTaskBarUpperComponent(jbStatusEmployeeRevert);
            
            if (mnTabTypeAux01 == SDataConstants.BPSX_BP_EMP_CON_EXP) {
                // render cutoff date:
                
                JLabel cutoffLabel = new JLabel("Fecha corte:", SwingConstants.CENTER);
                cutoffLabel.setPreferredSize(new Dimension(75, 23));
                
                JTextField cutoffTextField = new JTextField(SLibUtils.DateFormatDate.format(miClient.getSession().getSystemDate()));
                cutoffTextField.setEditable(false);
                cutoffTextField.setFocusable(false);
                cutoffTextField.setHorizontalAlignment(JTextField.CENTER);
                cutoffTextField.setPreferredSize(new Dimension(75, 23));
                
                addTaskBarUpperSeparator();
                addTaskBarUpperComponent(cutoffLabel);
                addTaskBarUpperComponent(cutoffTextField);
            }
            
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

            jcbFilterPaymentType = new javax.swing.JComboBox();
            jcbFilterPaymentType.setToolTipText("Filtrar periodo pago");
            jcbFilterPaymentType.setPreferredSize(new java.awt.Dimension(125, 23));

            jbClearFilterPaymentType = SGridUtils.createButton(new ImageIcon(getClass().getResource("/sa/lib/img/cmd_std_delete_tmp.gif")), "Quitar filtro periodo pago", this);
            
            addTaskBarLowerComponent(jtbViewEmployeeActive);
            addTaskBarLowerComponent(jtbViewEmployeeInactive);
            addTaskBarLowerComponent(jtbViewEmployeeAll);
            addTaskBarLowerComponent(jcbFilterPaymentType);
            addTaskBarLowerComponent(jbClearFilterPaymentType);
        }

        // Initialize table:

        STableField[] aoKeyFields = new STableField[1];
        STableColumn[] aoTableColumns = null;

        switch(mnTabTypeAux01) {
            case SDataConstants.BPSX_BP_CO:
                aoTableColumns = new STableColumn[35];
                break;
                
            case SDataConstants.BPSX_BP_SUP:
                aoTableColumns = new STableColumn[39];
                break;
                
            case SDataConstants.BPSX_BP_CUS:
                aoTableColumns = new STableColumn[40];
                break;
                
            case SDataConstants.BPSX_BP_CDR:
            case SDataConstants.BPSX_BP_DBR:
                aoTableColumns = new STableColumn[30];
                break;
                
            case SDataConstants.BPSU_BP:
            case SDataConstants.BPSX_BP_ATT_CARR:
            case SDataConstants.BPSX_BP_ATT_SAL_AGT:
                aoTableColumns = new STableColumn[25];
                break;
                
            case SDataConstants.BPSX_BP_ATT_BANK:
                aoTableColumns = new STableColumn[27];
                break;
                
            case SDataConstants.BPSX_BP_EMP:
                aoTableColumns = new STableColumn[65];
                break;
                
            case SDataConstants.BPSX_BP_EMP_CON_EXP:
                aoTableColumns = new STableColumn[24];
                break;
                
            default:
                // nothing
        }
        
        int i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "bp.id_bp");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        switch(mnTabTypeAux01) {
            case SDataConstants.BPSX_BP_CO:
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 250);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_ct.bp_key", "Clave", 100);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp_comm", "Nombre comercial", 200);
                msOrderKey = "bp.bp, bp_ct.bp_key, bp.bp_comm, bp.id_bp ";
                mnBizPartnerCategory = SDataConstantsSys.BPSS_CT_BP_CO;
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_ct.co_key", "Clave empresa", 100);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_ct.num_exporter", "No. exportador conf.", 50);
                break;

            case SDataConstants.BPSX_BP_SUP:
                if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_ct.bp_key", "Clave", 100);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 250);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp_comm", "Nombre comercial", 200);
                    msOrderKey = "bp_ct.bp_key, bp.bp_comm, bp.bp, bp.id_bp ";
                }
                else if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_NAME_KEY) {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 250);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_ct.bp_key", "Clave", 100);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp_comm", "Nombre comercial", 200);
                    msOrderKey = "bp.bp, bp_ct.bp_key, bp.bp_comm, bp.id_bp ";
                }
                else {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp_comm", "Nombre comercial", 200);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_ct.bp_key", "Clave", 100);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 250);
                    msOrderKey = "bp.bp_comm, bp_ct.bp_key, bp.bp, bp.id_bp ";
                }
                mnBizPartnerCategory = SDataConstantsSys.BPSS_CT_BP_SUP;
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_ct.co_key", "Clave empresa", 100);

                rightLevelBpCatCreate = rightLevelBpCatEdit = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BP_SUP).Level;

                // Enable SWAP Services:
                mbSwapServicesLinkUp = (boolean) miClient.getSwapServicesSetting(SSwapConsts.CFG_NVP_LINK_UP);
                if (mbSwapServicesLinkUp) {
                    jbExportDataToSwapServices = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_move_up_mag.gif")), "Exportar proveedores a " + SSwapConsts.SWAP_SERVICES, this);

                    addTaskBarUpperSeparator();
                    addTaskBarUpperComponent(jbExportDataToSwapServices);
                }
                break;

            case SDataConstants.BPSX_BP_CUS:
                if (miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_ct.bp_key", "Clave", 100);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 250);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp_comm", "Nombre comercial", 200);
                    msOrderKey = "bp_ct.bp_key, bp.bp_comm, bp.bp, bp.id_bp ";
                }
                else if (miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_NAME_KEY) {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 250);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_ct.bp_key", "Clave", 100);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp_comm", "Nombre comercial", 200);
                    msOrderKey = "bp.bp, bp_ct.bp_key, bp.bp_comm, bp.id_bp ";
                }
                else {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp_comm", "Nombre comercial", 200);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_ct.bp_key", "Clave", 100);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 250);
                    msOrderKey = "bp.bp_comm, bp_ct.bp_key, bp.bp, bp.id_bp ";
                }
                mnBizPartnerCategory = SDataConstantsSys.BPSS_CT_BP_CUS;
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_ct.co_key", "Clave empresa", 100);

                rightLevelBpCatCreate = rightLevelBpCatEdit = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BP_CUS).Level;
                break;

            case SDataConstants.BPSX_BP_CDR:
                if (miClient.getSessionXXX().getParamsErp().getFkSortingCreditorTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_ct.bp_key", "Clave", 100);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 250);
                    msOrderKey = "bp_ct.bp_key, bp.bp, bp.id_bp ";
                }
                else {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 250);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_ct.bp_key", "Clave", 100);
                    msOrderKey = "bp.bp, bp_ct.bp_key, bp.id_bp ";
                }
                mnBizPartnerCategory = SDataConstantsSys.BPSS_CT_BP_CDR;

                rightLevelBpCatCreate = rightLevelBpCatEdit = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BP_CDR).Level;
                break;

            case SDataConstants.BPSX_BP_DBR:
                if (miClient.getSessionXXX().getParamsErp().getFkSortingDebtorTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_ct.bp_key", "Clave", 100);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 250);
                    msOrderKey = "bp_ct.bp_key, bp.bp, bp.id_bp ";
                }
                else {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 250);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_ct.bp_key", "Clave", 100);
                    msOrderKey = "bp.bp, bp_ct.bp_key, bp.id_bp ";
                }
                mnBizPartnerCategory = SDataConstantsSys.BPSS_CT_BP_DBR;

                rightLevelBpCatCreate = rightLevelBpCatEdit = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BP_DBR).Level;
                break;

            case SDataConstants.BPSU_BP:
            case SDataConstants.BPSX_BP_ATT_BANK:
            case SDataConstants.BPSX_BP_ATT_CARR:
            case SDataConstants.BPSX_BP_ATT_SAL_AGT:
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 250);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp_comm", "Nombre comercial", 150);
                msOrderKey = "bp.bp, bp.id_bp ";
                break;

            case SDataConstants.BPSX_BP_EMP:
            case SDataConstants.BPSX_BP_EMP_CON_EXP:
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Nombre empleado", 250);
                aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "_emp_num", "Número empleado", 50);
                aoTableColumns[i++].setCellRenderer(SGridUtils.CellRendererIntegerRaw);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "e.b_act", "Activo empleado", STableConstants.WIDTH_BOOLEAN_2X);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "pay.name", "Período pago", 100);
                msOrderKey = "bp.bp, bp.id_bp ";
                
                if (miClient.getSessionXXX().getUser().hasPrivilege(SDataConstantsSys.PRV_HRS_AUX_HRS)) {
                    rightLevelBpCatCreate = rightLevelBpCatEdit = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_AUX_HRS).Level;
                }
                else {
                    rightLevelBpCatCreate = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_CAT_EMP).Level;
                    
                    if (mbHasRightEmpWage) {
                        rightLevelBpCatEdit = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_CAT_EMP_WAGE).Level;
                    }
                    else {
                        rightLevelBpCatEdit = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_CAT_EMP).Level;
                    }
                }
                break;

            default:
        }

        // fiscal IDs:
        
        if (mnTabTypeAux01 == SDataConstants.BPSX_BP_EMP) {
            // for employees:
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.fiscal_id", "RFC", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.alt_id", "CURP", 150);
        }
        else if (mnTabTypeAux01 != SDataConstants.BPSX_BP_EMP_CON_EXP) {
            // for all, except employees with expirable contracts:
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.fiscal_id", "RFC", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.fiscal_frg_id", "ID fiscal", 75);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.alt_id", "CURP", 150);
        }
        
        // for banks:
        
        if (mnTabTypeAux01 == SDataConstants.BPSX_BP_ATT_BANK) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.code_bank_san", "Código Santander", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.code_bank_baj", "Código BanBajío", 100);
        }

        // for employees:
        
        if (mnTabTypeAux01 == SDataConstants.BPSX_BP_EMP) {
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "e.ssn", "NSS", 75);
            aoTableColumns[i++].setApostropheOnCsvRequired(true);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "e.dt_bir", "Nacimiento empleado", STableConstants.WIDTH_DATE);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_FLOAT, "_e_age", "Edad empleado", STableConstants.WIDTH_NUM_SMALLINT);
        }
        if (mbIsViewEmployees) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "sc.co_key", "Membresía", 35);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "e.dt_ben", "Inicio prestaciones", STableConstants.WIDTH_DATE);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_FLOAT, "_e_sen", "Antigüedad", STableConstants.WIDTH_NUM_SMALLINT);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "e.dt_hire", "Última alta", STableConstants.WIDTH_DATE);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "e.dt_dis_n", "Última baja", STableConstants.WIDTH_DATE);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dep.name", "Departamento", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "pos.name", "Puesto", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "f_dt_pos", "Último cambio puesto",  STableConstants.WIDTH_DATE);
        }
        if (mnTabTypeAux01 == SDataConstants.BPSX_BP_EMP) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_sal", "Salario diario $", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "f_dt_sal", "Salario diario últ. cambio", STableConstants.WIDTH_DATE);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_wage", "Sueldo mensual $", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "f_dt_wage", "Sueldo mensual últ. cambio", STableConstants.WIDTH_DATE);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_sal_ssc", "SBC $", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "f_dt_sal_ssc", "SBC últ. cambio", STableConstants.WIDTH_DATE);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "sal.name", "Tipo salario", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "emp.name", "Tipo empleado", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "wrk.name", "Tipo obrero", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "e.b_uni", "Sindicalizado", STableConstants.WIDTH_BOOLEAN_2X);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "e.b_mfg_ope", "Operador", STableConstants.WIDTH_BOOLEAN_2X);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "sht.name", "Turno", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "wrktp.name", "Jornada", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "e.wrk_hrs_day", "Horas jornada", 50);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_overtime", "Tiempo extra", 100);
        }
        if (mbIsViewEmployees) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "con.name", "Tipo contrato", 200);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "e.con_exp_n", "Terminación contrato", STableConstants.WIDTH_DATE);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "_con_val", "Días terminación contrato", 75);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "rshe.name", "Tipo régimen (empleado)", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "emrshe.name", "Tipo régimen (empresa)", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "risk.name", "Riesgo trabajo", 100);
        }
        if (mnTabTypeAux01 == SDataConstants.BPSX_BP_EMP) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "e.zip_code", "Código postal fiscal", 50);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "mwz.name", "Área geográfica", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bank.name", "Banco", 100);
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "e.bank_acc", "Cuenta bancaria", 100);
            aoTableColumns[i++].setApostropheOnCsvRequired(true);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "gsrv.name", "Prov. despensa", 100);
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "e.grocery_srv_acc", "Cuenta despensa", 100);
            aoTableColumns[i++].setApostropheOnCsvRequired(true);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "_with_img_pho", "Foto", STableConstants.WIDTH_BOOLEAN);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "_with_img_sig", "Firma", STableConstants.WIDTH_BOOLEAN);
        }
        
        // for standard views of business partners:

        if (!mbIsViewBizPartnersSimple) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ct.ct_bp", "Cat. asoc. neg.", 150);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp.tp_bp", "Tipo asoc. neg.", 150);
        }
        
        // for all, except employees with expirable contracts:
        
        if (mnTabTypeAux01 != SDataConstants.BPSX_BP_EMP_CON_EXP) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp_bp.tp_bp_idy", "Tipo identidad AN", 150);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tax_tp.tax_idy", "Tipo identidad impuestos", 150);
        }

        // for standard views of business partners:

        if (!mbIsViewBizPartnersSimple) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "bp_ct.dt_start", "Ini. oper.", STableConstants.WIDTH_DATE);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "bp_ct.dt_end_n", "Fin oper.", STableConstants.WIDTH_DATE);
        }

        // for suppliers and customers:

        if (mnTabTypeAux01 == SDataConstants.BPSX_BP_SUP || mnTabTypeAux01 == SDataConstants.BPSX_BP_CUS) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "f_last_trans", "Últ. trans.", STableConstants.WIDTH_DATE);
        }

        // for companies, suppliers and customers:

        if (mnTabTypeAux01 == SDataConstants.BPSX_BP_CO || mnTabTypeAux01 == SDataConstants.BPSX_BP_SUP || mnTabTypeAux01 == SDataConstants.BPSX_BP_CUS) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "c.cur_key", "Moneda", STableConstants.WIDTH_CURRENCY_KEY);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "l.lan_key", "Idioma", 50);
        }
        
        // for suppliers:

        if (mnTabTypeAux01 == SDataConstants.BPSX_BP_SUP) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_ct.diot_oper", "Tipo operación DIOT", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_ct.cfdi_pay_way", "Forma pago predeterminada", 50);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_ct.cfdi_cfd_use", "Uso CFDI predeterminado", 50);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_ct.tax_regime", "Regimen fiscal CFDI", 50);
        }

        // for customers:

        if (mnTabTypeAux01 == SDataConstants.BPSX_BP_CUS) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_ct.cfdi_pay_way", "Forma pago predeterminada", 50);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_ct.cfdi_cfd_use", "Uso CFDI predeterminado", 50);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_ct.tax_regime", "Regimen fiscal CFDI", 50);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tca.tp_cfd_add", "Tipo addenda CFDI", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "us.usr", "Analista AN", 100);
        }

        // for all, except employees with expirable contracts:
        
        if (mnTabTypeAux01 != SDataConstants.BPSX_BP_EMP_CON_EXP) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bp.b_cus", "Cliente", STableConstants.WIDTH_BOOLEAN_2X);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bp.b_sup", "Proveedor", STableConstants.WIDTH_BOOLEAN_2X);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bp.b_dbr", "Deudor div.", STableConstants.WIDTH_BOOLEAN_2X);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bp.b_cdr", "Acreedor div.", STableConstants.WIDTH_BOOLEAN_2X);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bp.b_att_bank", "Banco", STableConstants.WIDTH_BOOLEAN_2X);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bp.b_att_car", "Transportista", STableConstants.WIDTH_BOOLEAN_2X);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bp.b_att_sal_agt", "Agente ventas", STableConstants.WIDTH_BOOLEAN_2X);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bp.b_att_emp", "Empleado", STableConstants.WIDTH_BOOLEAN_2X);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bp.b_att_par_shh", "Socio/accionista", STableConstants.WIDTH_BOOLEAN_2X);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bp.b_att_rel_pty", "Parte relacionada", STableConstants.WIDTH_BOOLEAN_2X);
        }

        // for all, except employees:

        if (!mbIsViewEmployees) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.web", "Sitio web", 150);
        }
        
        // for standard views of business partners:

        if (!mbIsViewBizPartnersSimple) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bp_ct.b_del", "Categoría eliminada", STableConstants.WIDTH_BOOLEAN);
        }
        
        // for all:

        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bp.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "bp.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "bp.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "bp.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);
        
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        if (mnTabTypeAux01 == SDataConstants.BPSU_BP || (mbIsViewEmployees && !employeesCrudEnabled)) {
            jbNew.setEnabled(false);
            jbEdit.setEnabled(false);
        }
        else {
            jbNew.setEnabled(rightLevelBp >= SUtilConsts.LEV_CAPTURE || rightLevelBpCatCreate >= SUtilConsts.LEV_CAPTURE);
            jbEdit.setEnabled(rightLevelBp >= SUtilConsts.LEV_EDITOR || rightLevelBpCatEdit >= SUtilConsts.LEV_EDITOR);
        }
        
        jbDelete.setEnabled(false); // allways disabled, business partners are disabled only within form
        
        if (mbIsViewEmployees) {
            jbBizPartnerExport.setEnabled(employeesCrudEnabled);
        }
        else if (mbIsViewBizPartnersSimple) {
            jbBizPartnerExport.setEnabled(false);
        }
        else {
            jbBizPartnerExport.setEnabled(rightLevelBp >= SUtilConsts.LEV_MANAGER || rightLevelBpCatEdit >= SUtilConsts.LEV_MANAGER);
        }
        
        moDialogBizPartnerExport = new SDialogBizPartnerExport(miClient);
        
        if (mbIsViewEmployees) {
            jbStatusEmployeeChange.setEnabled(jbEdit.isEnabled());
            jbStatusEmployeeModify.setEnabled(jbEdit.isEnabled());
            jbStatusEmployeeRevert.setEnabled(jbEdit.isEnabled());
        }

        mvSuscriptors.add(mnTabTypeAux01);
        mvSuscriptors.add(SDataConstants.USRU_USR);
        mvSuscriptors.add(SDataConstants.BPSU_BP);
        mvSuscriptors.add(SDataConstants.BPSX_BP_CO);
        mvSuscriptors.add(SDataConstants.BPSX_BP_CUS);
        mvSuscriptors.add(SDataConstants.BPSX_BP_SUP);
        mvSuscriptors.add(SDataConstants.BPSX_BP_DBR);
        mvSuscriptors.add(SDataConstants.BPSX_BP_CDR);
        mvSuscriptors.add(SDataConstants.BPSX_BP_UPD);
        mvSuscriptors.add(SDataConstants.BPSX_BP_ATT_BANK);
        mvSuscriptors.add(SDataConstants.BPSX_BP_ATT_CARR);
        mvSuscriptors.add(SDataConstants.BPSX_BP_ATT_SAL_AGT);
        mvSuscriptors.add(SDataConstants.BPSX_BP_EMP);
        mvSuscriptors.add(SDataConstants.BPSX_BP_EMP_CON_EXP);
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

    private void actionExportDataToSwapServices() {
        if (jbExportDataToSwapServices != null && jbExportDataToSwapServices.isEnabled()) {
            try {
                String response = SExportUtils.exportData(miClient.getSession(), SSyncType.PARTNER_SUPPLIER, false);
                
                if (response.isEmpty()) {
                    miClient.showMsgBoxInformation("Los proveedores fueron exportados correctamente a " + SSwapConsts.SWAP_SERVICES + ".");
                }
                else {
                    miClient.showMsgBoxInformation("Ocurrió un problema al exportar los provedores a " + SSwapConsts.SWAP_SERVICES + ":\n" + response);
                }
            }
            catch (Exception e) {
                SLibUtilities.printOutException(this, e);
            }
        }
    }
    
    private void itemStateChangedViewEmployee() {
        if (!mbIsViewEmployees) {
            mnFilterStatusEmployee = SLibConstants.UNDEFINED;
        }
        else {
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
        if (mbIsViewEmployees) {
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
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {
            if (miClient.getGuiModule(SDataConstants.GLOBAL_CAT_BPS).showForm(mnTabTypeAux01, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(SDataConstants.GLOBAL_CAT_BPS).refreshCatalogues(mnTabTypeAux01);
            }
        }
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (miClient.getGuiModule(SDataConstants.GLOBAL_CAT_BPS).showForm(mnTabTypeAux01, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(SDataConstants.GLOBAL_CAT_BPS).refreshCatalogues(mnTabTypeAux01);
                }
            }
        }
    }

    @Override
    public void actionDelete() {
        if (jbDelete.isEnabled()) {

        }
    }

    /**
     * Exportación de asociados de negocios. Dependiendo del modo de uso de la vista:
     * a) empleados: sustitución patronal de empleados dados de baja.
     * b) asociados de negocios: creación de otras categorías de asociados de negocios.
     */
    public void actionBizPartnerExport() {
        if (jbBizPartnerExport.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (mbIsViewEmployees) {
                    // sustitución patronal del empleado dado de baja:
                    
                    SDialogEmployerSubstitution dialog = new SDialogEmployerSubstitution(miClient.getSession().getClient(), "Sustitución patronal");
                    dialog.setValue(SModConsts.HRSU_EMP, (int[]) moTablePane.getSelectedTableRow().getPrimaryKey());
                    dialog.resetForm();
                    dialog.setVisible(true);
                    if (dialog.getFormResult() == SGuiConsts.FORM_RESULT_OK) {
                        SDbEmployee employee = (SDbEmployee) dialog.getValue(SModConsts.HRSU_EMP);
                        miClient.getSession().saveRegistry(employee);
                        miClient.getGuiModule(SDataConstants.GLOBAL_CAT_BPS).refreshCatalogues(mnTabTypeAux01);
                    }
                }
                else {
                    // creación de otra categoría del asociado de negocios:
                    
                    moDialogBizPartnerExport.formReset();
                    moDialogBizPartnerExport.formRefreshCatalogues();
                    moDialogBizPartnerExport.setValue(mnBizPartnerCategory, moTablePane.getSelectedTableRow().getPrimaryKey());
                    moDialogBizPartnerExport.setFormVisible(true);

                    if (moDialogBizPartnerExport.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                        int bizPartnerCategoryId = (Integer)  moDialogBizPartnerExport.getValue(mnBizPartnerCategory);
                        if (miClient.getGuiModule(SDataConstants.GLOBAL_CAT_BPS).showFormForCopy(bizPartnerCategoryId, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                            miClient.getGuiModule(SDataConstants.GLOBAL_CAT_BPS).refreshCatalogues(mnTabTypeAux01);
                        }
                    }
                }
            }
        }
    }

    public void actionStatusEmployeeChange() {
        if (jbStatusEmployeeChange.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                SDialogEmployeeHireLog dialog = new SDialogEmployeeHireLog((SGuiClient) miClient, "Cambiar estatus activo del empleado", SDialogEmployeeHireLog.MODE_SWITCH);
                dialog.setValue(SModConsts.HRSU_EMP, moTablePane.getSelectedTableRow().getPrimaryKey());
                dialog.setFormVisible(true);

                if (dialog.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                    try {
                        SDbEmployee employee = (SDbEmployee) dialog.getValue(SModConsts.HRSU_EMP);
                        employee.save(miClient.getSession());
                    }
                    catch (Exception e) {
                        SLibUtilities.renderException(this, e);
                    }

                    miClient.getGuiModule(SDataConstants.GLOBAL_CAT_BPS).refreshCatalogues(mnTabTypeAux01);
                    miClient.getSession().notifySuscriptors(mnTabTypeAux01);
                }
            }
        }
    }
    
    public void actionStatusEmployeeModify() {
        if (jbStatusEmployeeChange.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                SDialogEmployeeHireLog dialog = new SDialogEmployeeHireLog((SGuiClient) miClient, "Modificar última alta o baja del empleado", SDialogEmployeeHireLog.MODE_MODIFY);
                dialog.setValue(SModConsts.HRSU_EMP, moTablePane.getSelectedTableRow().getPrimaryKey());
                dialog.setFormVisible(true);

                if (dialog.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                    try {
                        SDbEmployeeHireLog employeeHireLog = (SDbEmployeeHireLog) dialog.getValue(SModConsts.HRS_EMP_LOG_HIRE);
                        
                        if (SHrsUtils.modifyHireLog(miClient.getSession(), employeeHireLog)) {
                            miClient.getGuiModule(SDataConstants.GLOBAL_CAT_BPS).refreshCatalogues(mnTabTypeAux01);
                            miClient.getSession().notifySuscriptors(mnTabTypeAux01);
                        }
                    }
                    catch (Exception e) {
                        SLibUtilities.renderException(this, e);
                    }
                }
            }
        }
    }
    
    public void actionStatusEmployeeRevert() {
        if (jbStatusEmployeeRevert.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                try {
                    SDbEmployee employee = (SDbEmployee) miClient.getSession().readRegistry(SModConsts.HRSU_EMP, (int[]) moTablePane.getSelectedTableRow().getPrimaryKey());
                    
                    if (miClient.showMsgBoxConfirm("¿Está seguro que desea revertir la última " + (employee.isActive() ? "ALTA" : "BAJA") + " del empleado '" + employee.getXtaEmployeeName() + "'?\n"
                            + "IMPORTANTE: ¡Esta acción no se puede deshacer!") == JOptionPane.YES_OPTION) {
                        if (SHrsUtils.revertLastHireLogEntry(miClient.getSession(), employee)) {
                            miClient.getGuiModule(SDataConstants.GLOBAL_CAT_BPS).refreshCatalogues(mnTabTypeAux01);
                            miClient.getSession().notifySuscriptors(mnTabTypeAux01);
                        }
                    }
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
    }

    @Override
    public void createSqlQuery() {
        String sqlWhere = "";
        String sqlCategoryWhere = "";
        String sqlBizPartner = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere += (sqlWhere.isEmpty() ? "" : "AND ") + "bp.b_del = 0 " + (mbIsViewEmployees ? "AND e.b_del = 0 " : "");
                sqlCategoryWhere = " AND bp_ct.b_del = 0 ";
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_BP) {
                if ((Integer) setting.getSetting() != SLibConstants.UNDEFINED) {
                    sqlBizPartner += " AND e.id_emp = " + (Integer) setting.getSetting() + " ";
                }
            }
        }

        switch(mnTabTypeAux01) {
            case SDataConstants.BPSX_BP_CO:
                sqlWhere += (sqlWhere.isEmpty() ? "" : "AND ") + "bp.b_co ";
                break;
            case SDataConstants.BPSX_BP_SUP:
                sqlWhere += (sqlWhere.isEmpty() ? "" : "AND ") + "bp.b_sup ";
                break;
            case SDataConstants.BPSX_BP_CUS:
                sqlWhere += (sqlWhere.isEmpty() ? "" : "AND ") + "bp.b_cus ";
                break;
            case SDataConstants.BPSX_BP_CDR:
                sqlWhere += (sqlWhere.isEmpty() ? "" : "AND ") + "bp.b_cdr ";
                break;
            case SDataConstants.BPSX_BP_DBR:
                sqlWhere += (sqlWhere.isEmpty() ? "" : "AND ") + "bp.b_dbr ";
                break;
            case SDataConstants.BPSU_BP:
                break;
            case SDataConstants.BPSX_BP_ATT_CARR:
                sqlWhere += (sqlWhere.isEmpty() ? "" : "AND ") + "bp.b_att_car ";
                break;
            case SDataConstants.BPSX_BP_ATT_SAL_AGT:
                sqlWhere += (sqlWhere.isEmpty() ? "" : "AND ") + "bp.b_att_sal_agt ";
                break;
            case SDataConstants.BPSX_BP_ATT_BANK:
                sqlWhere += (sqlWhere.isEmpty() ? "" : "AND ") + "bp.b_att_bank ";
                break;
            case SDataConstants.BPSX_BP_EMP:
            case SDataConstants.BPSX_BP_EMP_CON_EXP:
                sqlWhere += (sqlWhere.isEmpty() ? "" : "AND ") + "bp.b_att_emp ";
                break;
            default:
        }
        
        msSql = "SELECT bp.id_bp, bp.bp, bp.bp_comm, bp.fiscal_id, bp.fiscal_frg_id, bp.alt_id, bp.web, bp.code_bank_san, bp.code_bank_baj, bp.b_sup, bp.b_cus, bp.b_cdr, bp.b_dbr, bp.b_att_emp, bp.b_del, " +
                "tp_bp.tp_bp_idy, tax_tp.tax_idy, bp.b_att_bank, bp.b_att_car, bp.b_att_sal_agt, bp.b_att_emp, bp.b_att_par_shh, bp.b_att_rel_pty, " +
                (mbIsViewBizPartnersSimple ? "" : "bp_ct.b_del, bp_ct.bp_key, bp_ct.co_key, bp_ct.num_exporter, bp_ct.diot_oper, bp_ct.cfdi_pay_way, bp_ct.cfdi_cfd_use, bp_ct.tax_regime, bp_ct.dt_start, bp_ct.dt_end_n, " +
                "ct.ct_bp, tp.tp_bp, c.cur_key, l.lan_key, us.usr, tpy.tp_pay_sys, bp_ct.pay_account, tca.tp_cfd_add, ") +
                "(SELECT MAX(dt) FROM trn_dps WHERE b_del = 0 AND fid_bp_r = bp.id_bp AND fid_ct_dps IN(" + SDataConstantsSys.TRNS_CT_DPS_PUR + ", " + SDataConstantsSys.TRNS_CT_DPS_SAL + ") AND " +
                "fid_cl_dps IN(" + SDataConstantsSys.TRNS_CL_DPS_PUR_DOC[1] + ", " + SDataConstantsSys.TRNS_CL_DPS_PUR_ADJ[1] + ")) AS f_last_trans, " +
                (!mbIsViewEmployees ? "" :
                "e.num AS _emp_num, e.ssn, e.zip_code, e.dt_bir, e.dt_ben, e.dt_hire, e.dt_dis_n, IF(" + mbHasRightEmpWage + ", e.sal, 0) AS f_sal, IF(" + mbHasRightEmpWage + ", e.dt_sal, NULL) AS f_dt_sal, IF(" + mbHasRightEmpWage + ", e.wage, 0) AS f_wage, " +
                "IF(" + mbHasRightEmpWage + ", e.dt_wage, NULL) AS f_dt_wage, IF(" + mbHasRightEmpWage + ", e.sal_ssc, 0) AS f_sal_ssc, IF(" + mbHasRightEmpWage + ", e.dt_sal_ssc, NULL) AS f_dt_sal_ssc, e.dt_pos as f_dt_pos, e.wrk_hrs_day, e.con_exp_n, e.bank_acc, e.grocery_srv_acc, e.b_mfg_ope, e.b_act, e.b_uni, " +
                "CASE e.overtime WHEN " + SHrsConsts.OVERTIME_NEVER + " THEN '" + SHrsConsts.TXT_OVERTIME_NEVER + "' WHEN " + SHrsConsts.OVERTIME_ALLWAYS + " THEN '" + SHrsConsts.TXT_OVERTIME_ALLWAYS + "' WHEN " + SHrsConsts.OVERTIME_SOMETIMES + " THEN '" + SHrsConsts.TXT_OVERTIME_SOMETIMES + "' END AS _overtime, " +
                "sc.co_key, pay.name, sal.name, emp.name, wrk.name, wrktp.name, mwz.name, dep.name, pos.name, sht.name, con.name, rshe.name, emrshe.name, risk.name, bank.name, gsrv.name, " +
                "PERIOD_DIFF(DATE_FORMAT(NOW(), '%Y%m'), DATE_FORMAT(e.dt_bir, '%Y%m')) / " + SLibTimeConsts.MONTHS + " AS _e_age, " +
                "PERIOD_DIFF(DATE_FORMAT(NOW(), '%Y%m'), DATE_FORMAT(e.dt_ben, '%Y%m')) / " + SLibTimeConsts.MONTHS + " AS _e_sen, " +
                "DATEDIFF(e.con_exp_n, CURDATE()) AS _con_val, " +
                "img_pho_n IS NOT NULL AS _with_img_pho, img_sig_n IS NOT NULL AS _with_img_sig, ") +
                "bp.fid_usr_new, bp.fid_usr_edit, bp.fid_usr_del, bp.ts_new, bp.ts_edit, bp.ts_del, un.usr, ue.usr, ud.usr " +
                "FROM erp.bpsu_bp AS bp " +
                (!mbIsViewEmployees ? "" : "INNER JOIN hrs_emp_member AS em ON bp.id_bp = em.id_emp ") +
                "INNER JOIN erp.bpss_tp_bp_idy AS tp_bp ON bp.fid_tp_bp_idy = tp_bp.id_tp_bp_idy " +
                "INNER JOIN erp.finu_tax_idy AS tax_tp ON bp.fid_tax_idy = tax_tp.id_tax_idy " +
                "INNER JOIN erp.usru_usr AS un ON bp.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON bp.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON bp.fid_usr_del = ud.id_usr " + 
                (mbIsViewBizPartnersSimple ? "" :
                "INNER JOIN erp.bpsu_bp_ct AS bp_ct ON bp.id_bp = bp_ct.id_bp AND bp_ct.fid_ct_bp = " + mnBizPartnerCategory + " " + sqlCategoryWhere +
                "INNER JOIN erp.bpss_ct_bp AS ct ON bp_ct.fid_ct_bp = ct.id_ct_bp " +
                "INNER JOIN erp.bpsu_tp_bp AS tp ON bp_ct.fid_ct_bp = tp.id_ct_bp AND bp_ct.fid_tp_bp = tp.id_tp_bp " +
                "INNER JOIN erp.bpss_tp_cfd_add AS tca ON bp_ct.fid_tp_cfd_add = tca.id_tp_cfd_add " +
                "LEFT OUTER JOIN erp.usru_usr AS us ON bp_ct.fid_usr_ana_n = us.id_usr " +
                "LEFT OUTER JOIN erp.trnu_tp_pay_sys AS tpy ON bp_ct.fid_tp_pay_sys_n = tpy.id_tp_pay_sys " +
                "LEFT OUTER JOIN erp.cfgu_cur AS c ON bp_ct.fid_cur_n = c.id_cur " +
                "LEFT OUTER JOIN erp.cfgu_lan AS l ON bp_ct.fid_lan_n = l.id_lan ") +
                (!mbIsViewEmployees ? "" :
                "LEFT OUTER JOIN erp.hrsu_emp AS e ON bp.id_bp = e.id_emp " +
                "LEFT OUTER JOIN erp.cfgu_co AS sc ON e.fk_src_com = sc.id_co " +
                "LEFT OUTER JOIN erp.hrss_tp_pay AS pay ON e.fk_tp_pay = pay.id_tp_pay " +
                "LEFT OUTER JOIN erp.hrss_tp_sal AS sal ON e.fk_tp_sal = sal.id_tp_sal " +
                "LEFT OUTER JOIN erp.hrsu_tp_emp AS emp ON e.fk_tp_emp = emp.id_tp_emp " +
                "LEFT OUTER JOIN erp.hrsu_tp_wrk AS wrk ON e.fk_tp_wrk = wrk.id_tp_wrk " +
                "LEFT OUTER JOIN erp.hrsu_tp_mwz AS mwz ON e.fk_tp_mwz = mwz.id_tp_mwz " +
                "LEFT OUTER JOIN erp.hrsu_dep AS dep ON e.fk_dep = dep.id_dep " +
                "LEFT OUTER JOIN erp.hrsu_pos AS pos ON e.fk_pos = pos.id_pos " +
                "LEFT OUTER JOIN erp.hrsu_sht AS sht ON e.fk_sht = sht.id_sht " +
                "LEFT OUTER JOIN erp.hrss_tp_con AS con ON e.fk_tp_con = con.id_tp_con " +
                "LEFT OUTER JOIN erp.hrss_tp_rec_sche AS rshe ON e.fk_tp_rec_sche = rshe.id_tp_rec_sche " +
                "LEFT OUTER JOIN erp.hrss_tp_rec_sche AS emrshe ON em.fk_tp_rec_sche_n = emrshe.id_tp_rec_sche " +
                "LEFT OUTER JOIN erp.hrss_tp_pos_risk AS risk ON e.fk_tp_pos_risk = risk.id_tp_pos_risk " +
                "LEFT OUTER JOIN erp.hrss_tp_work_day AS wrktp ON e.fk_tp_work_day = wrktp.id_tp_work_day " +
                "LEFT OUTER JOIN erp.hrss_bank AS bank ON e.fk_bank_n = bank.id_bank " +
                "LEFT OUTER JOIN erp.hrss_grocery_srv AS gsrv ON e.fk_grocery_srv = gsrv.id_grocery_srv ") +
                (sqlWhere.isEmpty() ? "" : "WHERE " + sqlWhere) + sqlBizPartner + 
                (mnFilterPaymentTypeId == 0 ? "" : "AND e.fk_tp_pay = " + mnFilterPaymentTypeId + " ") +
                (mnFilterDepartamentId == 0 ? "" : "AND e.fk_dep = " + mnFilterDepartamentId + " ") +
                (mnFilterStatusEmployee == 0 ? "" : (mnFilterStatusEmployee == SGridFilterPanelEmployee.EMP_STATUS_ACT ? "AND e.b_act = 1 " : (mnFilterStatusEmployee == SGridFilterPanelEmployee.EMP_STATUS_INA ? "AND e.b_act = 0 " : ""))) +
                (mnTabTypeAux01 != SDataConstants.BPSX_BP_EMP_CON_EXP ? "" : "AND e.con_exp_n IS NOT NULL ") +
                "ORDER BY " + msOrderKey;
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbBizPartnerExport) {
                actionBizPartnerExport();
            }
            else if (button == jbStatusEmployeeChange) {
                actionStatusEmployeeChange();
            }
            else if (button == jbStatusEmployeeModify) {
                actionStatusEmployeeModify();
            }
            else if (button == jbStatusEmployeeRevert) {
                actionStatusEmployeeRevert();
            }
            else if (button == jbClearFilterPaymentType) {
                actionClearFilterPaymentType();
            }
            else if (button == jbClearFilterDepartament) {
                actionClearFilterDepartament();
            }
            else if (button == jbExportDataToSwapServices) {
                actionExportDataToSwapServices();
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
