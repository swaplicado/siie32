/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import cfd.DElement;
import cfd.ver33.DElementCfdiRelacionado;
import cfd.ver33.DElementCfdiRelacionados;
import erp.cfd.SXmlDpsCfd;
import erp.cfd.SXmlDpsCfdCce;
import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import sa.lib.xml.SXmlElement;

/**
 * Handling additional information for CFDI.
 * @author Juan Barajas, Sergio Flores
 */
public class SDataDpsCfd extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkYearId;
    protected int mnPkDocId;
    protected java.lang.String msVersion;
    protected java.lang.String msCfdiType;
    protected java.lang.String msPaymentWay;
    protected java.lang.String msPaymentMethod;
    protected java.lang.String msPaymentConditions;
    protected java.lang.String msZipIssue;
    protected java.lang.String msConfirmation;
    protected java.lang.String msTaxRegime;
    protected java.lang.String msCfdiUsage;
    protected String msXml;
    
    protected String msCfdiRelacionadosTipoRelacion;
    protected ArrayList<String> maCfdiRelacionados;
    
    protected String msCfdCceMotivoTraslado;
    protected String msCfdCceTipoOperacion;
    protected String msCfdCceClaveDePedimento;
    protected String msCfdCceCertificadoOrigen; 
    protected String msCfdCceNumCertificadoOrigen;
    protected String msCfdCceSubdivision;
    protected String msCfdCceTipoCambioUSD;
    protected String msCfdCceTotalUSD;
    protected String msCfdCceNumeroExportadorConfiable;
    protected String msCfdCceIncoterm;
    
    public SDataDpsCfd() {
        super(SDataConstants.TRN_DPS_CFD);
        maCfdiRelacionados = new ArrayList<>();
        reset();
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setVersion(java.lang.String s) { msVersion = s; }
    public void setCfdiType(java.lang.String s) { msCfdiType = s; }
    public void setPaymentWay(java.lang.String s) { msPaymentWay = s; }
    public void setPaymentMethod(java.lang.String s) { msPaymentMethod = s; }
    public void setPaymentConditions(java.lang.String s) { msPaymentConditions = s; }
    public void setZipIssue(java.lang.String s) { msZipIssue = s; }
    public void setConfirmation(java.lang.String s) { msConfirmation = s; }
    public void setTaxRegime(java.lang.String s) { msTaxRegime = s; }
    public void setCfdiUsage(java.lang.String s) { msCfdiUsage = s; }
    
    public void setCfdiRelacionadosTipoRelacion(java.lang.String s) { msCfdiRelacionadosTipoRelacion = s; }
    
    public void setCfdCceMotivoTraslado(String s) { msCfdCceMotivoTraslado = s; }
    public void setCfdCceTipoOperacion(String s) { msCfdCceTipoOperacion = s; }
    public void setCfdCceClaveDePedimento(String s) { msCfdCceClaveDePedimento = s; }
    public void setCfdCceCertificadoOrigen(String s) { msCfdCceCertificadoOrigen = s; }
    public void setCfdCceNumCertificadoOrigen(String s) { msCfdCceNumCertificadoOrigen = s; }
    public void setCfdCceSubdivision(String s) { msCfdCceSubdivision = s; }
    public void setCfdCceTipoCambioUSD(String s) { msCfdCceTipoCambioUSD = s; }
    public void setCfdCceTotalUSD(String s) { msCfdCceTotalUSD = s; }
    public void setCfdCceNumeroExportadorConfiable(String s) { msCfdCceNumeroExportadorConfiable = s; }
    public void setCfdCceIncoterm(String s) { msCfdCceIncoterm = s; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public java.lang.String getVersion() { return msVersion; }
    public java.lang.String getCfdiType() { return msCfdiType; }
    public java.lang.String getPaymentWay() { return msPaymentWay; }
    public java.lang.String getPaymentMethod() { return msPaymentMethod; }
    public java.lang.String getPaymentConditions() { return msPaymentConditions; }
    public java.lang.String getZipIssue() { return msZipIssue; }
    public java.lang.String getConfirmation() { return msConfirmation; }
    public java.lang.String getTaxRegime() { return msTaxRegime; }
    public java.lang.String getCfdiUsage() { return msCfdiUsage; }
    public String getXml() { return msXml; }
    
    public String getCfdiRelacionadosTipoRelacion() { return msCfdiRelacionadosTipoRelacion; }
    public ArrayList<String> getCfdiRelacionados() { return maCfdiRelacionados; }
    
    public String getCfdCceMotivoTraslado() { return msCfdCceMotivoTraslado; }
    public String getCfdCceTipoOperacion() { return msCfdCceTipoOperacion; }
    public String getCfdCceClaveDePedimento() { return msCfdCceClaveDePedimento; }
    public String getCfdCceCertificadoOrigen() { return msCfdCceCertificadoOrigen; }
    public String getCfdCceNumCertificadoOrigen() { return msCfdCceNumCertificadoOrigen; }
    public String getCfdCceSubdivision() { return msCfdCceSubdivision; }
    public String getCfdCceTipoCambioUSD() { return msCfdCceTipoCambioUSD; }
    public String getCfdCceTotalUSD() { return msCfdCceTotalUSD; }
    public String getCfdCceNumeroExportadorConfiable() { return msCfdCceNumeroExportadorConfiable; }
    public String getCfdCceIncoterm() { return msCfdCceIncoterm; }
    
    public boolean hasInternationalCommerce() { return !msCfdCceMotivoTraslado.isEmpty(); }

    /**
     * Generate the XML with infromation aditional for CFDI.
     * @throws Exception 
     */
    private void computeXml() throws Exception {
        SXmlDpsCfd dpsCfd = new SXmlDpsCfd();
        
        if (!maCfdiRelacionados.isEmpty()) {
            DElementCfdiRelacionados cfdiRelacionados = new DElementCfdiRelacionados();
            cfdiRelacionados.getAttTipoRelacion().setString(msCfdiRelacionadosTipoRelacion);
            
            for (String uuid : maCfdiRelacionados) {
                DElementCfdiRelacionado cfdiRelacionado = new DElementCfdiRelacionado();
                cfdiRelacionado.getAttUuid().setString(uuid);
                cfdiRelacionados.getEltCfdiRelacionados().add(cfdiRelacionado);
            }
            
            dpsCfd.getElements().add(cfdiRelacionados);
        }
        
        if (!msCfdCceMotivoTraslado.isEmpty()) {
            // attributes for international commerce:
            SXmlDpsCfdCce cfdCce = new SXmlDpsCfdCce();
            cfdCce.getAttribute(SXmlDpsCfdCce.ATT_MOT_TRAS).setValue(msCfdCceMotivoTraslado);
            cfdCce.getAttribute(SXmlDpsCfdCce.ATT_TP_OPE).setValue(msCfdCceTipoOperacion);
            cfdCce.getAttribute(SXmlDpsCfdCce.ATT_CVE_PED).setValue(msCfdCceClaveDePedimento);
            cfdCce.getAttribute(SXmlDpsCfdCce.ATT_CERT_ORIG).setValue(msCfdCceCertificadoOrigen);
            cfdCce.getAttribute(SXmlDpsCfdCce.ATT_NUM_CERT_ORIG).setValue(msCfdCceNumCertificadoOrigen);
            cfdCce.getAttribute(SXmlDpsCfdCce.ATT_SUB).setValue(msCfdCceSubdivision);
            cfdCce.getAttribute(SXmlDpsCfdCce.ATT_TP_CAMB).setValue(msCfdCceTipoCambioUSD);
            cfdCce.getAttribute(SXmlDpsCfdCce.ATT_TOT_USD).setValue(msCfdCceTotalUSD);
            cfdCce.getAttribute(SXmlDpsCfdCce.ATT_NUM_EXP_CONF).setValue(msCfdCceNumeroExportadorConfiable);
            cfdCce.getAttribute(SXmlDpsCfdCce.ATT_INCOTERM).setValue(msCfdCceIncoterm);

            dpsCfd.getXmlElements().add(cfdCce);
        }
        
        msXml = dpsCfd.getXmlString();
    }
    
    /**
     * Load values in fields from XML.
     * @throws Exception 
     */
    private void processXml(final String xml) throws Exception {
        SXmlDpsCfd dpsCfd = new SXmlDpsCfd();
        dpsCfd.processXml(xml);
        
        if (dpsCfd.isAvailableCfdiRelacionados()) {
            for (DElement element : dpsCfd.getElements()) {
                if (element instanceof DElementCfdiRelacionados) {
                    DElementCfdiRelacionados cfdiRelacionados = (DElementCfdiRelacionados) element;
                    msCfdiRelacionadosTipoRelacion = cfdiRelacionados.getAttTipoRelacion().getString();
                    for (DElementCfdiRelacionado cfdiRelacionado : cfdiRelacionados.getEltCfdiRelacionados()) {
                        maCfdiRelacionados.add(cfdiRelacionado.getAttUuid().getString());
                    }
                    break;
                }
            }
        }
        
        if (dpsCfd.isAvailableCce()) {
            for (SXmlElement element : dpsCfd.getXmlElements()) {
                if (element instanceof SXmlDpsCfdCce) {
                    SXmlDpsCfdCce dpsCfdCce = (SXmlDpsCfdCce) element;
                    msCfdCceMotivoTraslado = dpsCfdCce.getAttribute(SXmlDpsCfdCce.ATT_MOT_TRAS).getValue().toString();
                    msCfdCceTipoOperacion = dpsCfdCce.getAttribute(SXmlDpsCfdCce.ATT_TP_OPE).getValue().toString();
                    msCfdCceClaveDePedimento = dpsCfdCce.getAttribute(SXmlDpsCfdCce.ATT_CVE_PED).getValue().toString();
                    msCfdCceCertificadoOrigen = dpsCfdCce.getAttribute(SXmlDpsCfdCce.ATT_CERT_ORIG).getValue().toString();
                    msCfdCceNumCertificadoOrigen = dpsCfdCce.getAttribute(SXmlDpsCfdCce.ATT_NUM_CERT_ORIG).getValue().toString();
                    msCfdCceSubdivision = dpsCfdCce.getAttribute(SXmlDpsCfdCce.ATT_SUB).getValue().toString();
                    msCfdCceTipoCambioUSD = dpsCfdCce.getAttribute(SXmlDpsCfdCce.ATT_TP_CAMB).getValue().toString();
                    msCfdCceTotalUSD = dpsCfdCce.getAttribute(SXmlDpsCfdCce.ATT_TOT_USD).getValue().toString();
                    msCfdCceNumeroExportadorConfiable = dpsCfdCce.getAttribute(SXmlDpsCfdCce.ATT_NUM_EXP_CONF).getValue().toString();
                    msCfdCceIncoterm = dpsCfdCce.getAttribute(SXmlDpsCfdCce.ATT_INCOTERM).getValue().toString();
                    break;
                }
            }
        }
    }
    
    @Override
    public void setPrimaryKey(Object pk) {
        mnPkYearId = ((int[]) pk)[0];
        mnPkDocId = ((int[]) pk)[1];
    }

    @Override
    public Object getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkDocId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkYearId = 0;
        mnPkDocId = 0;
        msVersion = "";
        msCfdiType = "";
        msPaymentWay = "";
        msPaymentMethod = "";
        msPaymentConditions = "";
        msZipIssue = "";
        msConfirmation = "";
        msTaxRegime = "";
        msCfdiUsage = "";
        msXml = "";
        
        msCfdiRelacionadosTipoRelacion = "";
        maCfdiRelacionados.clear();
        
        msCfdCceMotivoTraslado = "";
        msCfdCceTipoOperacion = "";
        msCfdCceClaveDePedimento = "";
        msCfdCceCertificadoOrigen = "";
        msCfdCceNumCertificadoOrigen = "";
        msCfdCceSubdivision = "";
        msCfdCceTipoCambioUSD = "";
        msCfdCceTotalUSD = "";
        msCfdCceNumeroExportadorConfiable = "";
        msCfdCceIncoterm = "";
    }

    @Override
    public int read(Object pk, Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM trn_dps_cfd WHERE id_year = " + key[0] + " AND id_doc = " + key[1] + " ";
            
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkYearId = resultSet.getInt("id_year");
                mnPkDocId = resultSet.getInt("id_doc");
                msVersion = resultSet.getString("ver");
                msCfdiType = resultSet.getString("cfd_tp");
                msPaymentWay = resultSet.getString("pay_way");
                msPaymentMethod = resultSet.getString("pay_met");
                msPaymentConditions = resultSet.getString("pay_cond");
                msZipIssue = resultSet.getString("zip_iss");
                msConfirmation = resultSet.getString("conf");
                msTaxRegime = resultSet.getString("tax_reg");
                msCfdiUsage = resultSet.getString("cfd_use");
                msXml = resultSet.getString("xml");
                
                processXml(msXml);
                
                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
            }
        }
        catch (java.sql.SQLException e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        catch (Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public int save(java.sql.Connection connection) {
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            if (mbIsRegistryNew) {
                sql = "SELECT COUNT(*) FROM trn_dps_cfd WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " ";
                resultSet = connection.createStatement().executeQuery(sql);

                if (resultSet.next()) {
                    mbIsRegistryNew = resultSet.getInt(1) == 0;
                }
            }
            
            computeXml();

            if (mbIsRegistryNew) {
                sql = "INSERT INTO trn_dps_cfd VALUES (" +
                        mnPkYearId + ", " +
                        mnPkDocId + ", " +
                        "'" + msVersion + "', " +
                        "'" + msCfdiType + "', " +
                        "'" + msPaymentWay + "', " +
                        "'" + msPaymentMethod + "', " +
                        "'" + msPaymentConditions + "', " +
                        "'" + msZipIssue + "', " +
                        "'" + msConfirmation + "', " +
                        "'" + msTaxRegime + "', " +
                        "'" + msCfdiUsage + "', " +
                        "'" + msXml + "' " +
                        ")";
            }
            else {
                sql = "UPDATE trn_dps_cfd SET " +
                        //"id_year = " + mnPkYearId + ", " +
                        //"id_doc = " + mnPkDocId + ", " +
                        "ver = '" + msVersion + "', " +
                        "cfd_tp = '" + msCfdiType + "', " +
                        "pay_way = '" + msPaymentWay + "', " +
                        "pay_met = '" + msPaymentMethod + "', " +
                        "pay_cond = '" + msPaymentConditions + "', " +
                        "zip_iss = '" + msZipIssue + "', " +
                        "conf = '" + msConfirmation + "', " +
                        "tax_reg = '" + msTaxRegime + "', " +
                        "cfd_use = '" + msCfdiUsage + "', " +
                        "xml = '" + msXml + "' " +
                        "WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " ";
            }

            connection.createStatement().execute(sql);
            
            mbIsRegistryNew = false;
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
        }
        catch (java.sql.SQLException e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        catch (Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        
        return mnLastDbActionResult;
    }

    @Override
    public java.util.Date getLastDbUpdate() {
        return new Date();  // XXX check this! (Sergio Flores, 2017-08-08)
    }
}
