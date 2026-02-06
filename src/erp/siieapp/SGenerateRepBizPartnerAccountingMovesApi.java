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
import java.time.LocalDate;
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
 *
 * @author Adrian Aviles
 */
public class SGenerateRepBizPartnerAccountingMovesApi {

    private SGuiSession oSession;

    public SGenerateRepBizPartnerAccountingMovesApi(SGuiSession session) {
        oSession = session;
    }

    public SGenerateRepBizPartnerAccountingMovesApi() {

    }

    public byte[] generateReportPdf(int pkBizPartner, String sBd) throws Exception {
        try {
            int year = LocalDate.now().getYear();
            Map<String, Object> map = null;
            JasperPrint jasperPrint = null;
            SDataBizPartnerCategory bizPartnerCategory = null;
            int[] moFieldBizPartner = new int[]{pkBizPartner};
            int[] paramErpPk = new int[]{1};
            int[] manSysMoveTypeKey = SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP;
            int mnBizPartnerCategoryId = SDataConstantsSys.BPSS_CT_BP_SUP;
            java.lang.String msBizPartnerCatSng = SBpsUtils.getBizPartnerCategoryName(mnBizPartnerCategoryId, SUtilConsts.NUM_SNG);
            java.lang.String msBizPartnerCatPlr = SBpsUtils.getBizPartnerCategoryName(mnBizPartnerCategoryId, SUtilConsts.NUM_PLR);

            String host = "192.168.1.233";
            String port = "3306";
            String db = sBd;
            SSwapClient client = new SSwapClient(host, SLibUtils.parseInt(port), db, false, SDataConstantsSys.USRX_USER_ADMIN);
            SClientApi apiClient = new SClientApi(client.getSession(), client.getSession().getUser().getPkUserId());

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
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }
    }
}
