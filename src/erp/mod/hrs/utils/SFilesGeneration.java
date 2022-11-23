/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.utils;

import erp.cfd.SCfdConsts;
import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.mtrn.data.SCfdUtils;
import erp.mtrn.data.SDataCfd;
import erp.print.SDataConstantsPrint;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import sa.lib.SLibConsts;
import sa.lib.gui.SGuiClient;

/**
 * Clase de utilidades para la generación de archivos XML y PDF
 *
 * @author Edwin Carmona
 */
public class SFilesGeneration {

    /**
     *
     * @param client
     * @param year
     */
    public void readCfds(SGuiClient client, int year) {
        try {
            String sql = "SELECT "
                    + " cfd.id_cfd"
                    + " FROM "
                    + " trn_cfd cfd "
                    + " WHERE"
                    + " YEAR(ts) = " + year
                    + " AND xml_rfc_emi = '" + ((SClientInterface) client).getSessionXXX().getCompany().getDbmsDataCompany().getFiscalId() + "' "
                    + " ORDER BY ts ASC;";

            ResultSet resulReceipts = client.getSession().getStatement().
                    getConnection().createStatement().
                    executeQuery(sql);

            while (resulReceipts.next()) {
                SDataCfd cfdForPrinting = (SDataCfd) SDataUtilities.readRegistry((SClientInterface) client, SDataConstants.TRN_CFD, new int[]{resulReceipts.getInt("id_cfd")}, SLibConstants.EXEC_MODE_SILENT);
                
                int cfdiVersion = 0;
                
                switch (cfdForPrinting.getFkXmlTypeId()) {
                    case SDataConstantsSys.TRNS_TP_XML_CFD:
                    case SDataConstantsSys.TRNS_TP_XML_CFDI_32:
                        cfdiVersion = SCfdConsts.CFDI_PAYROLL_VER_OLD;
                        break;
                    case SDataConstantsSys.TRNS_TP_XML_CFDI_33:
                    case SDataConstantsSys.TRNS_TP_XML_CFDI_40:
                        cfdiVersion = SCfdConsts.CFDI_PAYROLL_VER_CUR;
                        break;
                    default:
                        throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                
                switch (cfdForPrinting.getFkCfdTypeId()) {
                    case SDataConstantsSys.TRNS_TP_CFD_INV:
                    case SDataConstantsSys.TRNS_TP_CFD_PAY_REC:
                    case SDataConstantsSys.TRNS_TP_CFD_BOL:
                        this.writeXml(cfdForPrinting.getDocXmlName(), cfdForPrinting.getDocXml(), (SClientInterface) client);
                        if (cfdForPrinting.getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_ANNULED || cfdForPrinting.getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_EMITED) {
                            SCfdUtils.printCfd((SClientInterface) client, cfdForPrinting, cfdiVersion, SDataConstantsPrint.PRINT_MODE_PDF_FILE, 1, false);
                        }
                        break;
                        
                    case SDataConstantsSys.TRNS_TP_CFD_PAYROLL:
                        if (cfdForPrinting.getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_ANNULED || cfdForPrinting.getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_EMITED) {
                            this.writeXml(cfdForPrinting.getDocXmlName(), cfdForPrinting.getDocXml(), (SClientInterface) client);
                            SCfdUtils.printCfd((SClientInterface) client, cfdForPrinting, cfdiVersion, SDataConstantsPrint.PRINT_MODE_PDF_FILE, 1, false);
                        }
                        break;
                    default:
                        // do nothing
                }
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(SFilesGeneration.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            Logger.getLogger(SFilesGeneration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     *
     * @param name
     * @param xml
     * @param client
     * @throws IOException
     */
    public void writeXml(String name, String xml, SClientInterface client) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(client.getSessionXXX().getParamsCompany().getXmlBaseDirectory() + name))) {
            writer.write(xml);
        }
    }
}
