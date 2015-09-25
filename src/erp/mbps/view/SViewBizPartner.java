/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mbps.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.mbps.form.SDialogBizPartnerExport;
import erp.mod.SModConsts;
import erp.mod.hrs.db.SDbEmployee;
import erp.mod.hrs.form.SDialogEmployeeHireLog;
import erp.table.SFilterConstants;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiItem;

/**
 *
 * @author Alfonso Flores
 */
public class SViewBizPartner extends erp.lib.table.STableTab implements java.awt.event.ActionListener, java.awt.event.ItemListener {

    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;
    private javax.swing.JButton jbBizPartnerExport;
    private javax.swing.JButton jbStatusEmployee;
    private javax.swing.JComboBox jcbFilter;
    private javax.swing.JButton jbClearFilter;

    private int mnBizPartnerCategory;
    private java.lang.String msOrderKey;

    private erp.mbps.form.SDialogBizPartnerExport moDialogBizPartnerExport;
    private erp.mod.hrs.form.SDialogEmployeeHireLog moDialogEmployeeHireLog;
    private boolean mbHasRightEmpWage;
    
    private int mnPaymentTypeId;

    public SViewBizPartner(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType) {
        super(client, tabTitle, auxType);
        mnTabTypeAux01 = auxType;
        initComponents();
    }

    private void initComponents() {
        int i;
        int levelRightEdit = SDataConstantsSys.UNDEFINED;
        int levelRightEditCategory = SDataConstantsSys.UNDEFINED;
        mbHasRightEmpWage = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_HRS_CAT_EMP_WAGE).HasRight;
        STableColumn[] aoTableColumns = null;

        jbBizPartnerExport = new JButton(miClient.getImageIcon(SLibConstants.ICON_BP_EXPORT));
        jbBizPartnerExport.setPreferredSize(new Dimension(23, 23));
        jbBizPartnerExport.addActionListener(this);
        jbBizPartnerExport.setToolTipText("Exportar a otra categoría");
        jbBizPartnerExport.setEnabled(false);
        jbStatusEmployee = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_switch.gif")));
        jbStatusEmployee.setPreferredSize(new Dimension(23, 23));
        jbStatusEmployee.addActionListener(this);
        jbStatusEmployee.setToolTipText("Cambiar estatus alta/baja");
        jbStatusEmployee.setEnabled(false);

        moTabFilterDeleted = new STabFilterDeleted(this);
        jcbFilter = new javax.swing.JComboBox();
        jcbFilter.setToolTipText("Filtrar");
        jcbFilter.setPreferredSize(new java.awt.Dimension(150, 23));
        
        jbClearFilter = new javax.swing.JButton();
        jbClearFilter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sa/lib/img/cmd_std_clear.gif")));
        jbClearFilter.setToolTipText("Quitar filtro");
        jbClearFilter.setPreferredSize(new java.awt.Dimension(23, 23));
        jbClearFilter.addActionListener(this);
        
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);
        addTaskBarUpperComponent(jbBizPartnerExport);
        
        if (mnTabTypeAux01 == SDataConstants.BPSX_BP_EMP) {
            addTaskBarUpperComponent(jcbFilter);
            addTaskBarUpperComponent(jbClearFilter);
        }
        addTaskBarUpperComponent(jbStatusEmployee);

        STableField[] aoKeyFields = new STableField[1];

        switch(mnTabTypeAux01) {
            case SDataConstants.BPSX_BP_CO:
                aoTableColumns = new STableColumn[33];
                break;
            case SDataConstants.BPSX_BP_SUP:
            case SDataConstants.BPSX_BP_CUS:
                aoTableColumns = new STableColumn[34];
                break;
            case SDataConstants.BPSX_BP_CDR:
            case SDataConstants.BPSX_BP_DBR:
                aoTableColumns = new STableColumn[29];
                break;
            case SDataConstants.BPSU_BP:
            case SDataConstants.BPSX_BP_ATT_CARR:
            case SDataConstants.BPSX_BP_ATT_SAL_AGT:
                aoTableColumns = new STableColumn[24];
                break;
            case SDataConstants.BPSX_BP_ATT_BANK:
                aoTableColumns = new STableColumn[26];
                break;
            case SDataConstants.BPSX_BP_EMP:
                aoTableColumns = new STableColumn[50];
                break;
            default:
                break;
        }

        i = 0;
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

                levelRightEditCategory = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BP_SUP).Level;
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

                levelRightEditCategory = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BP_CUS).Level;
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

                levelRightEditCategory = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BP_CDR).Level;
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

                levelRightEditCategory = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BP_DBR).Level;
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
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 250);
                msOrderKey = "bp.bp, bp.id_bp ";
                break;

            default:
        }

        if (mnTabTypeAux01 == SDataConstants.BPSX_BP_EMP) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "e.num", "Clave", 100);
        }
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.fiscal_id", "RFC", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.alt_id", "CURP", 100);

        if (mnTabTypeAux01 == SDataConstants.BPSX_BP_ATT_BANK) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.code_bank_san", "Código Santander", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.code_bank_baj", "Código BanBajío", 100);
        }

        if (mnTabTypeAux01 == SDataConstants.BPSX_BP_EMP) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "e.ssn", "NSS", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "e.b_mfg_ope", "Operador", STableConstants.WIDTH_BOOLEAN_2X);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "e.b_act", "Activo", STableConstants.WIDTH_BOOLEAN_2X);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "e.dt_bir", "Nacimiento", STableConstants.WIDTH_DATE);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "e.dt_ben", "Ini. benef.", STableConstants.WIDTH_DATE);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "e.dt_hire", "Últ. alta", STableConstants.WIDTH_DATE);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "e.dt_dis_n", "Últ. baja", STableConstants.WIDTH_DATE);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_sal", "Salario $", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "f_dt_sal", "Salario", STableConstants.WIDTH_DATE);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_wage", "Sueldo $", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "f_dt_wage", "Sueldo", STableConstants.WIDTH_DATE);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_sal_ssc", "SBC $", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "f_dt_sal_ssc", "SBC", STableConstants.WIDTH_DATE);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "e.wrk_hrs_day", "Hrs. jornada", 75);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "pay.name", "Periodo pago", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "sal.name", "Tipo salario", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "emp.name", "Tipo empleado", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "wrk.name", "Tipo obrero", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dep.name", "Departamento", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "pos.name", "Puesto", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "sht.name", "Turno", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "rshe.name", "Régimen contratación", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "risk.name", "Riesgo trabajo", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "mwz.name", "Área geográfica", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bank.name", "Banco", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "e.bank_acc", "Cuenta bancaria", 100);
        }

        if (mnTabTypeAux01 != SDataConstants.BPSU_BP && mnTabTypeAux01 != SDataConstants.BPSX_BP_ATT_SAL_AGT && mnTabTypeAux01 != SDataConstants.BPSX_BP_EMP &&
                mnTabTypeAux01 != SDataConstants.BPSX_BP_ATT_BANK && mnTabTypeAux01 != SDataConstants.BPSX_BP_ATT_CARR) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ct.ct_bp", "Cat. asoc. neg.", 150);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp.tp_bp", "Tipo asoc. neg.", 150);
        }
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp_bp.tp_bp_idy", "Tipo identidad AN", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tax_tp.tax_idy", "Tipo identidad impuestos", 150);

        if (mnTabTypeAux01 != SDataConstants.BPSU_BP && mnTabTypeAux01 != SDataConstants.BPSX_BP_ATT_SAL_AGT && mnTabTypeAux01 != SDataConstants.BPSX_BP_EMP &&
                mnTabTypeAux01 != SDataConstants.BPSX_BP_ATT_BANK && mnTabTypeAux01 != SDataConstants.BPSX_BP_ATT_CARR) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "bp_ct.dt_start", "Ini. oper.", STableConstants.WIDTH_DATE);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "bp_ct.dt_end_n", "Fin oper.", STableConstants.WIDTH_DATE);
        }

        if (mnTabTypeAux01 == SDataConstants.BPSX_BP_SUP || mnTabTypeAux01 == SDataConstants.BPSX_BP_CUS) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "f_last_trans", "Últ. trans.", STableConstants.WIDTH_DATE);
        }

        if (mnTabTypeAux01 == SDataConstants.BPSX_BP_CO || mnTabTypeAux01 == SDataConstants.BPSX_BP_SUP || mnTabTypeAux01 == SDataConstants.BPSX_BP_CUS) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "c.cur_key", "Moneda", STableConstants.WIDTH_CURRENCY_KEY);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "l.lan_key", "Idioma", 50);
        }

        if (mnTabTypeAux01 != SDataConstants.BPSU_BP && mnTabTypeAux01 != SDataConstants.BPSX_BP_ATT_SAL_AGT && mnTabTypeAux01 != SDataConstants.BPSX_BP_EMP &&
                mnTabTypeAux01 != SDataConstants.BPSX_BP_ATT_BANK && mnTabTypeAux01 != SDataConstants.BPSX_BP_ATT_CARR) {
        }

        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bp.b_cus", "Cliente", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bp.b_sup", "Proveedor", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bp.b_dbr", "Deudor div.", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bp.b_cdr", "Acreedor div.", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bp.b_att_bank", "Banco", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bp.b_att_car", "Transportista", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bp.b_att_sal_agt", "Agente ventas", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bp.b_att_emp", "Empleado", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bp.b_att_par_shh", "Socio/accionista", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bp.b_att_rel_pty", "Parte relacionada", STableConstants.WIDTH_BOOLEAN_2X);

        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.web", "Sitio web", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bp.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);

        if (mnTabTypeAux01 != SDataConstants.BPSU_BP && mnTabTypeAux01 != SDataConstants.BPSX_BP_ATT_SAL_AGT && mnTabTypeAux01 != SDataConstants.BPSX_BP_EMP &&
                mnTabTypeAux01 != SDataConstants.BPSX_BP_ATT_BANK && mnTabTypeAux01 != SDataConstants.BPSX_BP_ATT_CARR) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bp_ct.b_del", "Categoría eliminada", STableConstants.WIDTH_BOOLEAN);
        }

        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "bp.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "bp.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "bp.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        levelRightEdit = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BP).Level;

        jbNew.setEnabled((levelRightEditCategory >=  SUtilConsts.LEV_AUTHOR || levelRightEdit >=  SUtilConsts.LEV_AUTHOR) && mnTabTypeAux01 != SDataConstants.BPSU_BP);
        jbEdit.setEnabled((levelRightEditCategory >=  SUtilConsts.LEV_AUTHOR || levelRightEdit >=  SUtilConsts.LEV_AUTHOR) && mnTabTypeAux01 != SDataConstants.BPSU_BP);
        jbDelete.setEnabled(false);
        jbBizPartnerExport.setEnabled(levelRightEditCategory >=  SUtilConsts.LEV_AUTHOR && mnTabTypeAux01 != SDataConstants.BPSU_BP && mnTabTypeAux01 != SDataConstants.BPSX_BP_ATT_SAL_AGT && mnTabTypeAux01 != SDataConstants.BPSX_BP_EMP &&
                mnTabTypeAux01 != SDataConstants.BPSX_BP_ATT_BANK && mnTabTypeAux01 != SDataConstants.BPSX_BP_ATT_CARR);
        jbStatusEmployee.setEnabled(mnTabTypeAux01 == SDataConstants.BPSX_BP_EMP);

        moDialogBizPartnerExport = new SDialogBizPartnerExport(miClient);

        mvSuscriptors.add(mnTabTypeAux01);
        mvSuscriptors.add(SDataConstants.USRU_USR);
        mvSuscriptors.add(SDataConstants.BPSU_BP);
        mvSuscriptors.add(SDataConstants.BPSX_BP_CO);
        mvSuscriptors.add(SDataConstants.BPSX_BP_CUS);
        mvSuscriptors.add(SDataConstants.BPSX_BP_SUP);
        mvSuscriptors.add(SDataConstants.BPSX_BP_DBR);
        mvSuscriptors.add(SDataConstants.BPSX_BP_CDR);
        mvSuscriptors.add(SDataConstants.BPSX_BP_ATT_BANK);
        mvSuscriptors.add(SDataConstants.BPSX_BP_ATT_CARR);
        mvSuscriptors.add(SDataConstants.BPSX_BP_EMP);
        mvSuscriptors.add(SDataConstants.BPSX_BP_ATT_SAL_AGT);
        mvSuscriptors.add(SModConsts.HRS_EMP_LOG_HIRE);
        mvSuscriptors.add(SModConsts.HRSU_TP_EMP);
        mvSuscriptors.add(SModConsts.HRSU_TP_WRK);
        mvSuscriptors.add(SModConsts.HRSU_TP_MWZ);
        mvSuscriptors.add(SModConsts.HRSU_DEP);
        mvSuscriptors.add(SModConsts.HRSU_POS);
        mvSuscriptors.add(SModConsts.HRSU_SHT);

        populateTable();
        updateOptions();
    }
    
    private void actionClearFilter() {
        jbClearFilter.setEnabled(false);
        jcbFilter.setSelectedIndex(0);
        jcbFilter.requestFocus();
        mnPaymentTypeId = SLibConsts.UNDEFINED;
        
        try {
            populateTable();
        }
        catch (Exception e) {
            SLibUtils.printException(this, e);
        }
    }
    
    private void itemStateChangedFilter() {
        try {
            mnPaymentTypeId = (((SGuiItem) jcbFilter.getSelectedItem()).getPrimaryKey().length == 0 ? SLibConsts.UNDEFINED : ((SGuiItem) jcbFilter.getSelectedItem()).getPrimaryKey()[0]);
            jbClearFilter.setEnabled(jcbFilter.getSelectedIndex() > 0);
            populateTable();
        }
        catch (Exception e) {
            SLibUtils.printException(this, e);
        }
    }
    
    private void updateOptions() {
        jcbFilter.removeItemListener(this);
        
        miClient.getSession().populateCatalogue(jcbFilter, SModConsts.HRSS_TP_PAY, SLibConsts.UNDEFINED, null);
        
        actionClearFilter();
        
        jcbFilter.addItemListener(this);
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

    public void actionBizPartnerExport() {
        int nBizPartnerCategoryId = 0;

        if (jbBizPartnerExport.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                moDialogBizPartnerExport.formReset();
                moDialogBizPartnerExport.formRefreshCatalogues();
                moDialogBizPartnerExport.setValue(mnBizPartnerCategory, moTablePane.getSelectedTableRow().getPrimaryKey());
                moDialogBizPartnerExport.setFormVisible(true);

                if (moDialogBizPartnerExport.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                    nBizPartnerCategoryId = ((Integer)  moDialogBizPartnerExport.getValue(mnBizPartnerCategory)).intValue();
                    if (miClient.getGuiModule(SDataConstants.GLOBAL_CAT_BPS).showFormForCopy(nBizPartnerCategoryId, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                        miClient.getGuiModule(SDataConstants.GLOBAL_CAT_BPS).refreshCatalogues(mnTabTypeAux01);
                    }
                }
            }
        }
    }

    public void actionStatusChange() {
        SDbEmployee employee = null;

        if (jbStatusEmployee.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                moDialogEmployeeHireLog = new SDialogEmployeeHireLog((SGuiClient) miClient, "Cambio de estatus del empleado");

                moDialogEmployeeHireLog.setValue(SLibConsts.DATA_TYPE_KEY, moTablePane.getSelectedTableRow().getPrimaryKey());
                moDialogEmployeeHireLog.setFormVisible(true);

                if (moDialogEmployeeHireLog.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                    employee = (SDbEmployee)  moDialogEmployeeHireLog.getValue(SLibConsts.UNDEFINED);

                    try {
                        employee.save(miClient.getSession());
                    }
                    catch (Exception e) {
                        SLibUtilities.renderException(this, e);
                    }

                    miClient.getGuiModule(SDataConstants.GLOBAL_CAT_BPS).refreshCatalogues(mnTabTypeAux01);
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
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "bp.b_del = 0 ";
                sqlCategoryWhere = " AND bp_ct.b_del = 0 ";
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_BP) {
                if ((Integer) setting.getSetting() != SLibConstants.UNDEFINED) {
                    sqlBizPartner += " AND e.id_emp = " + (Integer) setting.getSetting() + " ";
                }
            }
        }

        if (mnTabTypeAux01 == SDataConstants.BPSX_BP_ATT_BANK) {
            sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "bp.b_att_bank = 1 ";
        }
        else if (mnTabTypeAux01 == SDataConstants.BPSX_BP_ATT_CARR) {
            sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "bp.b_att_car = 1 ";
        }
        else if (mnTabTypeAux01 == SDataConstants.BPSX_BP_EMP) {
            sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "bp.b_att_emp = 1 ";
        }
        else if (mnTabTypeAux01 == SDataConstants.BPSX_BP_ATT_SAL_AGT) {
            sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "bp.b_att_sal_agt = 1 ";
        }

        msSql = "SELECT bp.id_bp, bp.bp, bp.bp_comm, bp.fiscal_id, bp.alt_id, bp.web, bp.code_bank_san, bp.code_bank_baj, bp.b_sup, bp.b_cus, bp.b_cdr, bp.b_dbr, bp.b_att_emp, bp.b_del, " +
                "tp_bp.tp_bp_idy, tax_tp.tax_idy, bp.b_att_bank, bp.b_att_car, bp.b_att_sal_agt, bp.b_att_emp, bp.b_att_par_shh, bp.b_att_rel_pty, " +
                (mnTabTypeAux01 == SDataConstants.BPSU_BP || mnTabTypeAux01 == SDataConstants.BPSX_BP_ATT_SAL_AGT || mnTabTypeAux01 == SDataConstants.BPSX_BP_EMP ||
                mnTabTypeAux01 == SDataConstants.BPSX_BP_ATT_BANK || mnTabTypeAux01 == SDataConstants.BPSX_BP_ATT_CARR ? "" : "bp_ct.b_del, bp_ct.bp_key, bp_ct.co_key, bp_ct.dt_start, bp_ct.dt_end_n, " +
                "ct.ct_bp, tp.tp_bp, c.cur_key, l.lan_key, ") +
                "(SELECT MAX(dt) FROM trn_dps WHERE b_del = 0 AND fid_bp_r = bp.id_bp AND fid_ct_dps IN(" + SDataConstantsSys.TRNS_CT_DPS_PUR + ", " + SDataConstantsSys.TRNS_CT_DPS_SAL + ") AND " +
                "fid_cl_dps IN(" + SDataConstantsSys.TRNS_CL_DPS_PUR_DOC[1] + ", " + SDataConstantsSys.TRNS_CL_DPS_PUR_ADJ[1] + ")) AS f_last_trans, " +
                (mnTabTypeAux01 != SDataConstants.BPSX_BP_EMP ? "" :
                "e.num, e.ssn, e.dt_bir, e.dt_ben, e.dt_hire, e.dt_dis_n, IF(" + mbHasRightEmpWage + ", e.sal, 0) AS f_sal, IF(" + mbHasRightEmpWage + ", e.dt_sal, NULL) AS f_dt_sal, IF(" + mbHasRightEmpWage + ", e.wage, 0) AS f_wage, "
                + "IF(" + mbHasRightEmpWage + ", e.dt_wage, NULL) AS f_dt_wage, IF(" + mbHasRightEmpWage + ", e.sal_ssc, 0) AS f_sal_ssc, IF(" + mbHasRightEmpWage + ", e.dt_sal_ssc, NULL) AS f_dt_sal_ssc, e.wrk_hrs_day, e.bank_acc, e.b_mfg_ope, e.b_act, " +
                "pay.name, sal.name, emp.name, wrk.name, mwz.name, dep.name, pos.name, sht.name, rshe.name, risk.name, bank.name, ") +
                "bp.fid_usr_new, bp.fid_usr_edit, bp.fid_usr_del, bp.ts_new, bp.ts_edit, bp.ts_del, un.usr, ue.usr, ud.usr " +
                "FROM erp.bpsu_bp AS bp " +
                "INNER JOIN erp.bpss_tp_bp_idy AS tp_bp ON " +
                "bp.fid_tp_bp_idy = tp_bp.id_tp_bp_idy " +
                "INNER JOIN erp.finu_tax_idy AS tax_tp ON " +
                "bp.fid_tax_idy = tax_tp.id_tax_idy " +
                "INNER JOIN erp.usru_usr AS un ON " +
                "bp.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON " +
                "bp.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON " +
                "bp.fid_usr_del = ud.id_usr " +
                (mnTabTypeAux01 == SDataConstants.BPSU_BP || mnTabTypeAux01 == SDataConstants.BPSX_BP_ATT_SAL_AGT || mnTabTypeAux01 == SDataConstants.BPSX_BP_EMP ||
                mnTabTypeAux01 == SDataConstants.BPSX_BP_ATT_BANK || mnTabTypeAux01 == SDataConstants.BPSX_BP_ATT_CARR ?
                /*(mnTabTypeAux01 == SDataConstants.BPSX_BP_ATT_SAL_AGT ?
                "INNER JOIN erp.bpsu_bp_ct AS bp_ct ON bp.id_bp = bp_ct.id_bp AND bp.b_att_sal_agt = 1 " + (sqlCategoryWhere.length() == 0 ? "" : sqlCategoryWhere) : "")*/"" :
                "INNER JOIN erp.bpsu_bp_ct AS bp_ct ON bp.id_bp = bp_ct.id_bp AND bp_ct.fid_ct_bp = " + mnBizPartnerCategory + " " + (sqlCategoryWhere.length() == 0 ? "" : sqlCategoryWhere) +
                "INNER JOIN erp.bpss_ct_bp AS ct ON bp_ct.fid_ct_bp = ct.id_ct_bp " +
                "INNER JOIN erp.bpsu_tp_bp AS tp ON bp_ct.fid_ct_bp = tp.id_ct_bp AND bp_ct.fid_tp_bp = tp.id_tp_bp " +
                "LEFT OUTER JOIN erp.cfgu_cur AS c ON bp_ct.fid_cur_n = c.id_cur " +
                "LEFT OUTER JOIN erp.cfgu_lan AS l ON bp_ct.fid_lan_n = l.id_lan ") +
                (mnTabTypeAux01 != SDataConstants.BPSX_BP_EMP ? "" :
                "LEFT OUTER JOIN erp.hrsu_emp AS e ON bp.id_bp = e.id_emp " +
                "LEFT OUTER JOIN erp.hrss_tp_pay AS pay ON e.fk_tp_pay = pay.id_tp_pay " +
                "LEFT OUTER JOIN erp.hrss_tp_sal AS sal ON e.fk_tp_sal = sal.id_tp_sal " +
                "LEFT OUTER JOIN erp.hrsu_tp_emp AS emp ON e.fk_tp_emp = emp.id_tp_emp " +
                "LEFT OUTER JOIN erp.hrsu_tp_wrk AS wrk ON e.fk_tp_wrk = wrk.id_tp_wrk " +
                "LEFT OUTER JOIN erp.hrsu_tp_mwz AS mwz ON e.fk_tp_mwz = mwz.id_tp_mwz " +
                "LEFT OUTER JOIN erp.hrsu_dep AS dep ON e.fk_dep = dep.id_dep " +
                "LEFT OUTER JOIN erp.hrsu_pos AS pos ON e.fk_pos = pos.id_pos " +
                "LEFT OUTER JOIN erp.hrsu_sht AS sht ON e.fk_sht = sht.id_sht " +
                "LEFT OUTER JOIN erp.hrss_tp_rec_sche AS rshe ON e.fk_tp_rec_sche = rshe.id_tp_rec_sche " +
                "LEFT OUTER JOIN erp.hrss_tp_pos_risk AS risk ON e.fk_tp_pos_risk = risk.id_tp_pos_risk " +
                "LEFT OUTER JOIN erp.hrss_bank AS bank ON e.fk_bank_n = bank.id_bank ") +
                (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) + (sqlBizPartner.length() == 0 ? "" : sqlBizPartner) + (mnPaymentTypeId != SLibConsts.UNDEFINED ? " AND e.fk_tp_pay = " + mnPaymentTypeId : "") + " " +
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
            else if (button == jbStatusEmployee) {
                actionStatusChange();
            }
            else if (button == jbClearFilter) {
                    actionClearFilter();
                }
            }
        }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof JComboBox) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                JComboBox comboBox = (JComboBox) e.getSource();

                if (comboBox == jcbFilter) {
                    itemStateChangedFilter();
                }
            }
        }
    }
}
