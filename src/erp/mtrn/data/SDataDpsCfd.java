/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import cfd.DElement;
import cfd.ver3.DCfdVer3Consts;
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
 * @author Juan Barajas, Sergio Flores, Isabel Servín
 */
public final class SDataDpsCfd extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkYearId;
    protected int mnPkDocId;
    protected String msVersion;
    protected String msCfdiType;
    protected String msPaymentWay;
    protected String msPaymentMethod;
    protected String msPaymentConditions;
    protected String msZipIssue;
    protected String msConfirmation;
    protected String msTaxRegime;
    protected String msCfdiUsage;
    protected String msRelatedType;
    protected String msRelatedUuid;
    protected String msXml;
    protected int mnFkRelatedDpsYearId_n;
    protected int mnFkRelatedDpsDocId_n;

    protected ArrayList<SDataDpsCfdEntry> maDbmsDpsCfdEntries;
    
    protected String msCfdiRelacionadosTipoRelacion;
    protected ArrayList<String> maCfdiRelacionados;
    
    protected String msCfdCceMotivoTraslado;
    protected String msCfdCceTipoOperacion;
    protected String msCfdCceClaveDePedimento;
    protected String msCfdCceCertificadoOrigen; 
    protected String msCfdCceNumCertificadoOrigen;
    protected String msCfdCceSubdivision;
    protected String msCfdCceTipoCambioUsd;
    protected String msCfdCceTotalUsd;
    protected String msCfdCceNumeroExportadorConfiable;
    protected String msCfdCceIncoterm;
    
    public SDataDpsCfd() {
        super(SDataConstants.TRN_DPS_CFD);
        maDbmsDpsCfdEntries = new ArrayList<>();
        maCfdiRelacionados = new ArrayList<>();
        reset();
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setVersion(String s) { msVersion = s; }
    public void setCfdiType(String s) { msCfdiType = s; }
    public void setPaymentWay(String s) { msPaymentWay = s; }
    public void setPaymentMethod(String s) { msPaymentMethod = s; }
    public void setPaymentConditions(String s) { msPaymentConditions = s; }
    public void setZipIssue(String s) { msZipIssue = s; }
    public void setConfirmation(String s) { msConfirmation = s; }
    public void setTaxRegime(String s) { msTaxRegime = s; }
    public void setCfdiUsage(String s) { msCfdiUsage = s; }
    public void setRelatedType(java.lang.String s) { msRelatedType = s; }
    public void setXml(java.lang.String s) { msXml = s; }
    public void setRelatedUuid(java.lang.String s) { msRelatedUuid = s; }
    public void setFkRelatedDpsYearId_n(int n) { mnFkRelatedDpsYearId_n = n; }
    public void setFkRelatedDpsDocId_n(int n) { mnFkRelatedDpsDocId_n = n; }

    public void setDbmsDpsCfdEntries(ArrayList<SDataDpsCfdEntry> o) { maDbmsDpsCfdEntries = o; }
    
    public void setCfdiRelacionadosTipoRelacion(String s) { msCfdiRelacionadosTipoRelacion = s; }
    
    public void setCfdCceMotivoTraslado(String s) { msCfdCceMotivoTraslado = s; }
    public void setCfdCceTipoOperacion(String s) { msCfdCceTipoOperacion = s; }
    public void setCfdCceClaveDePedimento(String s) { msCfdCceClaveDePedimento = s; }
    public void setCfdCceCertificadoOrigen(String s) { msCfdCceCertificadoOrigen = s; }
    public void setCfdCceNumCertificadoOrigen(String s) { msCfdCceNumCertificadoOrigen = s; }
    public void setCfdCceSubdivision(String s) { msCfdCceSubdivision = s; }
    public void setCfdCceTipoCambioUsd(String s) { msCfdCceTipoCambioUsd = s; }
    public void setCfdCceTotalUsd(String s) { msCfdCceTotalUsd = s; }
    public void setCfdCceNumeroExportadorConfiable(String s) { msCfdCceNumeroExportadorConfiable = s; }
    public void setCfdCceIncoterm(String s) { msCfdCceIncoterm = s; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public String getVersion() { return msVersion; }
    public String getCfdiType() { return msCfdiType; }
    public String getPaymentWay() { return msPaymentWay; }
    public String getPaymentMethod() { return msPaymentMethod; }
    public String getPaymentConditions() { return msPaymentConditions; }
    public String getZipIssue() { return msZipIssue; }
    public String getConfirmation() { return msConfirmation; }
    public String getTaxRegime() { return msTaxRegime; }
    public String getCfdiUsage() { return msCfdiUsage; }
    public String getRelatedType() { return msRelatedType; }
    public String getRelatedUuid() { return msRelatedUuid; }
    public String getXml() { return msXml; }
    public int getFkRelatedDpsYearId_n() { return mnFkRelatedDpsYearId_n; }
    public int getFkRelatedDpsDocId_n() { return mnFkRelatedDpsDocId_n; }
    
    public ArrayList<SDataDpsCfdEntry> getDbmsDpsCfdEntries() { return maDbmsDpsCfdEntries; }
    
    public String getCfdiRelacionadosTipoRelacion() { return msCfdiRelacionadosTipoRelacion; }
    public ArrayList<String> getCfdiRelacionados() { return maCfdiRelacionados; }
    
    public String getCfdCceMotivoTraslado() { return msCfdCceMotivoTraslado; }
    public String getCfdCceTipoOperacion() { return msCfdCceTipoOperacion; }
    public String getCfdCceClaveDePedimento() { return msCfdCceClaveDePedimento; }
    public String getCfdCceCertificadoOrigen() { return msCfdCceCertificadoOrigen; }
    public String getCfdCceNumCertificadoOrigen() { return msCfdCceNumCertificadoOrigen; }
    public String getCfdCceSubdivision() { return msCfdCceSubdivision; }
    public String getCfdCceTipoCambioUsd() { return msCfdCceTipoCambioUsd; }
    public String getCfdCceTotalUsd() { return msCfdCceTotalUsd; }
    public String getCfdCceNumeroExportadorConfiable() { return msCfdCceNumeroExportadorConfiable; }
    public String getCfdCceIncoterm() { return msCfdCceIncoterm; }
    
    public boolean hasInternationalCommerce() { return !msCfdCceTipoOperacion.isEmpty(); }
    
    /**
     * Adds UUID to array of CFDI Relacionados only if it has not been added yet previously.
     * @param uuid UUID of CFDI to add.
     * @return <code>true</code> if UUID was added, otherwise <code>false</code>.
     * @throws java.lang.Exception
     */
    public boolean addCfdiRelacionado(String uuid) throws Exception {
        if (uuid == null || uuid.length() != DCfdVer3Consts.LEN_UUID) {
            throw new Exception("La longitud del UUID " + (uuid == null ? "" : "'" + uuid + "'") + " debe ser de " + DCfdVer3Consts.LEN_UUID + " caracteres.");
        }
        
        boolean add = true;
        
        for (String cfdi : maCfdiRelacionados) {
            if (cfdi.equals(uuid)) {
                add = false;
                break;
            }
        }
        
        if (add) {
            maCfdiRelacionados.add(uuid);
        }
        
        if (msRelatedUuid.isEmpty()) {
            msRelatedUuid = uuid;
            msRelatedType = msCfdiRelacionadosTipoRelacion;
        }
        
        return add;
    }

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
        
        if (hasInternationalCommerce()) {
            // attributes for international commerce:
            SXmlDpsCfdCce cfdCce = new SXmlDpsCfdCce();
            cfdCce.getAttribute(SXmlDpsCfdCce.ATT_MOT_TRAS).setValue(msCfdCceMotivoTraslado);
            cfdCce.getAttribute(SXmlDpsCfdCce.ATT_TP_OPE).setValue(msCfdCceTipoOperacion);
            cfdCce.getAttribute(SXmlDpsCfdCce.ATT_CVE_PED).setValue(msCfdCceClaveDePedimento);
            cfdCce.getAttribute(SXmlDpsCfdCce.ATT_CERT_ORIG).setValue(msCfdCceCertificadoOrigen);
            cfdCce.getAttribute(SXmlDpsCfdCce.ATT_NUM_CERT_ORIG).setValue(msCfdCceNumCertificadoOrigen);
            cfdCce.getAttribute(SXmlDpsCfdCce.ATT_SUB).setValue(msCfdCceSubdivision);
            cfdCce.getAttribute(SXmlDpsCfdCce.ATT_TP_CAMB).setValue(msCfdCceTipoCambioUsd);
            cfdCce.getAttribute(SXmlDpsCfdCce.ATT_TOT_USD).setValue(msCfdCceTotalUsd);
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
        
        if (dpsCfd.isAvailableIntCommerce()) {
            for (SXmlElement element : dpsCfd.getXmlElements()) {
                if (element instanceof SXmlDpsCfdCce) {
                    SXmlDpsCfdCce dpsCfdCce = (SXmlDpsCfdCce) element;
                    msCfdCceMotivoTraslado = dpsCfdCce.getAttribute(SXmlDpsCfdCce.ATT_MOT_TRAS).getValue().toString();
                    msCfdCceTipoOperacion = dpsCfdCce.getAttribute(SXmlDpsCfdCce.ATT_TP_OPE).getValue().toString();
                    msCfdCceClaveDePedimento = dpsCfdCce.getAttribute(SXmlDpsCfdCce.ATT_CVE_PED).getValue().toString();
                    msCfdCceCertificadoOrigen = dpsCfdCce.getAttribute(SXmlDpsCfdCce.ATT_CERT_ORIG).getValue().toString();
                    msCfdCceNumCertificadoOrigen = dpsCfdCce.getAttribute(SXmlDpsCfdCce.ATT_NUM_CERT_ORIG).getValue().toString();
                    msCfdCceSubdivision = dpsCfdCce.getAttribute(SXmlDpsCfdCce.ATT_SUB).getValue().toString();
                    msCfdCceTipoCambioUsd = dpsCfdCce.getAttribute(SXmlDpsCfdCce.ATT_TP_CAMB).getValue().toString();
                    msCfdCceTotalUsd = dpsCfdCce.getAttribute(SXmlDpsCfdCce.ATT_TOT_USD).getValue().toString();
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
        msRelatedType = "";
        msRelatedUuid = "";
        msXml = "";
        mnFkRelatedDpsYearId_n = 0;
        mnFkRelatedDpsDocId_n = 0;
        
        msCfdiRelacionadosTipoRelacion = "";
        maCfdiRelacionados.clear();
        
        msCfdCceMotivoTraslado = "";
        msCfdCceTipoOperacion = "";
        msCfdCceClaveDePedimento = "";
        msCfdCceCertificadoOrigen = "";
        msCfdCceNumCertificadoOrigen = "";
        msCfdCceSubdivision = "";
        msCfdCceTipoCambioUsd = "";
        msCfdCceTotalUsd = "";
        msCfdCceNumeroExportadorConfiable = "";
        msCfdCceIncoterm = "";
    }

    @Override
    public int read(Object pk, Statement statement) {
        int[] key = (int[]) pk;
        String sql;
        ResultSet resultSet;
        
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
                msRelatedType = resultSet.getString("related_tp");
                msRelatedUuid = resultSet.getString("related_uuid");
                msXml = resultSet.getString("xml");
                mnFkRelatedDpsYearId_n = resultSet.getInt("fid_related_dps_year_n");
                mnFkRelatedDpsDocId_n = resultSet.getInt("fid_related_dps_doc_n");
                
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
        String sql;
        ResultSet resultSet;

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
                        "'" + msRelatedType + "', " +
                        "'" + msRelatedUuid + "', " + 
                        "'" + msXml + "', " +
                        (mnFkRelatedDpsYearId_n == 0 ? "NULL, " : mnFkRelatedDpsYearId_n + ", ") +
                        (mnFkRelatedDpsDocId_n == 0 ? "NULL " : mnFkRelatedDpsDocId_n + " ") +
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
                        "related_tp = '" + msRelatedType + "', " +
                        "related_uuid = '" + msRelatedUuid + "', " +
                        "xml = '" + msXml + "', " +
                        "fid_related_dps_year = " + (mnFkRelatedDpsYearId_n == 0 ? "NULL, " : mnFkRelatedDpsYearId_n + ", ") +
                        "fid_related_dps_doc = " + (mnFkRelatedDpsDocId_n == 0 ? "NULL " : mnFkRelatedDpsDocId_n + " ") +
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
