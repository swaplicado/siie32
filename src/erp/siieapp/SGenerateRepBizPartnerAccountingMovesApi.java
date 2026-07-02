/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.siieapp;

import erp.SClientApi;
import erp.cli.swap.SSwapClient;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadDescriptions;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.mbps.data.SDataBizPartner;
import erp.mbps.data.SDataBizPartnerCategory;
import erp.mcfg.data.SDataParamsErp;
import erp.mod.bps.db.SBpsUtils;
import java.io.File;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;

/**
 * Generador de reportes PDF de movimientos contables de asociados de negocios.
 * 
 * Utiliza JasperReports para generar reportes en PDF mostrando los movimientos
 * contables de un asociado de negocios (proveedor) para un período específico.
 * Integra datos del ERP incluyendo información de crédito, moneda y parámetros
 * configurables del sistema.
 * 
 * @author Adrián Aviles
 * @version 1.0
 */
public class SGenerateRepBizPartnerAccountingMovesApi {

    /** Sesión del usuario para acceso a datos */
    private SGuiSession oSession;

    /**
     * Constructor que inicializa el generador con una sesión específica.
     * @param session sesión del usuario
     */
    public SGenerateRepBizPartnerAccountingMovesApi(SGuiSession session) {
        oSession = session;
    }

    /**
     * Constructor por defecto. Sesión será nula hasta que se configure.
     */
    public SGenerateRepBizPartnerAccountingMovesApi() {

    }

    /**
     * Genera un reporte PDF con los movimientos contables de un asociado de negocios.
     * 
     * Obtiene información del asociado de negocios (proveedor) desde la base de datos,
     * junto con parámetros del sistema y detalles de crédito. Llena un template
     * JasperReports con estos datos y exporta el resultado como PDF.
     * 
     * Nota: Actualmente utiliza una conexión hardcodeada a IP 192.168.1.233 para
     * un usuario y empresa específicos. Considerar parametrizar en futuras versiones.
     * 
     * @param pkBizPartner identificador del asociado de negocios (proveedor)
     * @param sBd nombre de la base de datos
     * @param year año fiscal para el cual generar el reporte
     * @return contenido del PDF como arreglo de bytes
     * @throws Exception si hay error en generación de reporte o acceso a datos
     */
    public byte[] generateReportPdf(int pkBizPartner, String sBd, int year) throws Exception {
        try {
            Map<String, Object> map = null;
            JasperPrint jasperPrint = null;
            SDataBizPartnerCategory bizPartnerCategory = null;
            int[] moFieldBizPartner = new int[]{pkBizPartner};
            int[] paramErpPk = new int[]{1};
            int[] manSysMoveTypeKey = SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP;
            int mnBizPartnerCategoryId = SDataConstantsSys.BPSS_CT_BP_SUP;
            java.lang.String msBizPartnerCatSng = SBpsUtils.getBizPartnerCategoryName(mnBizPartnerCategoryId, SUtilConsts.NUM_SNG);
            java.lang.String msBizPartnerCatPlr = SBpsUtils.getBizPartnerCategoryName(mnBizPartnerCategoryId, SUtilConsts.NUM_PLR);

            String host = "192.168.1.233"; // XXX 2026-07-02, Sergio Flores: parametrizar este valor en duro.
            String port = "3306"; // XXX 2026-07-02, Sergio Flores: parametrizar este valor en duro.
            String db = sBd;
            int idDefaultCompany = 2852; // XXX 2026-07-02, Sergio Flores: parametrizar este valor en duro.
            SSwapClient client = new SSwapClient(host, SLibUtils.parseInt(port), db, false, SDataConstantsSys.USRX_USER_ADMIN, idDefaultCompany);
            SClientApi apiClient = new SClientApi(client.getSession(), client.getSession().getUser().getPkUserId(), true);

            SDataBizPartner oBizPartner = new SDataBizPartner();
            int result = oBizPartner.read((Object) moFieldBizPartner, client.getSession().getStatement());
            SDataParamsErp paramsErp = new SDataParamsErp();
            paramsErp.read((Object) paramErpPk, client.getSession().getStatement());

            map = apiClient.createReportParams();
            map.put("nSysMoveCatId", manSysMoveTypeKey[0]);
            map.put("nSysMoveTypeId", manSysMoveTypeKey[1]);
            map.put("sBizPartnerCat", msBizPartnerCatSng.toUpperCase());
            map.put("sBizPartnerCatPlural", msBizPartnerCatPlr.toUpperCase());
            map.put("nLocalCurrencyId", paramsErp.getDbmsDataCurrency().getPkCurrencyId());
            map.put("sLocalCurrency", paramsErp.getDbmsDataCurrency().getCurrency());
            map.put("nYear", year);
            map.put("tDate", SLibTimeUtilities.createDate(year, 12, 31));
            map.put("nBizPartnerId", oBizPartner.getPkBizPartnerId());
            map.put("sBizPartner", oBizPartner.getBizPartner());

            switch (mnBizPartnerCategoryId) {
                case SDataConstantsSys.BPSS_CT_BP_SUP:
                    bizPartnerCategory = oBizPartner.getDbmsCategorySettingsSup();
                    break;
                case SDataConstantsSys.BPSS_CT_BP_CUS:
                    bizPartnerCategory = oBizPartner.getDbmsCategorySettingsCus();
                    break;
                default:
                    // nothing
            }

            String sql = SDataReadDescriptions.createQueryForCatalogue(SDataConstants.BPSS_TP_CRED, new int[]{bizPartnerCategory.getEffectiveCreditTypeId()}, 0);
            Statement statement = client.getSession().getStatement();
            ResultSet res = statement.executeQuery(sql);
            if (!res.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            String desc = res.getString("descrip");

            map.put("dCreditLimit", bizPartnerCategory.getEffectiveCreditLimit());
            map.put("nDaysCredit", bizPartnerCategory.getEffectiveDaysOfCredit());
            map.put("nDaysGrace", bizPartnerCategory.getEffectiveDaysOfGrace());
            map.put("sCreditType", desc);
            map.put("sFuncText", "");
            map.put("sFilterFunctionalArea", "");

            String reportFileName = SDataUtilities.getReportFileName(SDataConstantsSys.REP_FIN_BPS_ACC_MOV);

            if (!reportFileName.isEmpty()) {
                jasperPrint = JasperFillManager.fillReport((JasperReport) JRLoader.loadObject(new File(reportFileName)), map, client.getSession().getDatabase().getConnection());
            }

            return JasperExportManager.exportReportToPdf(jasperPrint);
        }
        catch (Exception e) {
            System.out.println(e);
            throw e;
        }
    }
}
