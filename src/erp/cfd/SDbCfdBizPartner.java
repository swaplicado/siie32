/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.cfd;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.mbps.data.SDataBizPartner;
import erp.mbps.data.SDataBizPartnerBranch;
import erp.mbps.data.SDataBizPartnerBranchAddress;
import erp.mod.SModConsts;
import erp.mod.hrs.db.SDbEmployee;
import erp.mtrn.data.SCfdUtils;
import sa.lib.SLibUtils;

/**
 *
 * @author Juan Barajas, Sergio Flores, Isabel Servín
 */
public class SDbCfdBizPartner {

    protected SClientInterface miClient;
    
    protected int mnBizPartnerId;
    protected int mnBizPartnerBranchId;
    protected int mnBizPartnerBranchAddressId;
    protected int mnIssuingBizPartnerId;
    protected int mnIssuingBizPartnerBranchId;
    protected String msZipCodeHq;
    protected String msZipCodeEmployee;
    protected boolean mbIsIssuer;
    protected boolean mbIsIssueForIntCommerce;
    protected boolean mbIsIssueForPayroll;
    
    public SDbCfdBizPartner(SClientInterface client) {
        miClient = client;
        
        mnBizPartnerId = 0;
        mnBizPartnerBranchId = 0;
        mnIssuingBizPartnerId = 0;
        mnIssuingBizPartnerBranchId = 0;
        msZipCodeHq = "";
        msZipCodeEmployee = "";
        mbIsIssuer = false;
        mbIsIssueForIntCommerce = false;
        mbIsIssueForPayroll = false;
    }
    
    public void setBizPartnerIds(final int bizPartnerId, final int bizPartnerBranchId) {
        setBizPartnerIds(bizPartnerId, bizPartnerBranchId, 0);
    }
    
    public void setBizPartnerIds(final int bizPartnerId, final int bizPartnerBranchId, final int bizPartnerBranchAddressId) {
        mnBizPartnerId = bizPartnerId;
        mnBizPartnerBranchId = bizPartnerBranchId;
        mnBizPartnerBranchAddressId = bizPartnerBranchAddressId;
    }
    
    public void setIssuingBizPartnerIds(final int expeditionBizPartnerId, final int expeditionBizPartnerBranchId) {
        mnIssuingBizPartnerId = expeditionBizPartnerId;
        mnIssuingBizPartnerBranchId = expeditionBizPartnerBranchId;
    }
    
    public void setIssuer(final boolean isIssuer, final boolean isIssueForIntCommerce) {
        mbIsIssuer = isIssuer;
        mbIsIssueForIntCommerce = isIssueForIntCommerce;
    }
    
    public void setIssueForPayroll(final boolean isIssueForPayroll) {
        mbIsIssueForPayroll = isIssueForPayroll;
    }
    
    public SCfdDataBizPartner createCfdDataBizPartner() throws Exception {
        SCfdDataBizPartner cfdBizPartner = null;
        
        try {
            // business partner information:
            
            SDataBizPartner bizPartner = null;
            
            if (mbIsIssuer) {
                bizPartner = (SDataBizPartner) SCfdUtils.DataSet.get(SCfdUtils.KEY_CFD_ISSUER);
                
                if (bizPartner == null) {
                    bizPartner = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, new int[] { mnBizPartnerId }, SLibConstants.EXEC_MODE_SILENT);
                    SCfdUtils.DataSet.put(SCfdUtils.KEY_CFD_ISSUER, bizPartner);
                }
            }
            else {
                bizPartner = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, new int[] { mnBizPartnerId }, SLibConstants.EXEC_MODE_SILENT);
            }
            
            if (bizPartner == null) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP + "\n" + SDataBizPartner.class.getName() + ".");
            }
            else {
                SDataBizPartnerBranch bizPartnerBranch = bizPartner.getDbmsBizPartnerBranch(new int[] { mnBizPartnerBranchId });
                
                if (bizPartnerBranch == null) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP + "\n" + SDataBizPartnerBranch.class.getName() + ".");
                }
                else {
                    SDataBizPartnerBranchAddress bizPartnerBranchAddress = null;
                    
                    if (mnBizPartnerBranchAddressId != 0) {
                        bizPartnerBranchAddress = bizPartnerBranch.getDbmsBizPartnerBranchAddress(new int[] { mnBizPartnerBranchId, mnBizPartnerBranchAddressId });
                    }
                    else if (!mbIsIssuer && bizPartnerBranch.getIsAddressPrintable()) {
                        bizPartnerBranchAddress = bizPartnerBranch.getDbmsBizPartnerBranchAddressOfficial();
                        msZipCodeHq = bizPartner.getDbmsBizPartnerBranchHq().getDbmsBizPartnerBranchAddressOfficial().getZipCode();
                    }
                    else {
                        bizPartnerBranchAddress = bizPartner.getDbmsBizPartnerBranchHq().getDbmsBizPartnerBranchAddressOfficial();
                        msZipCodeHq = bizPartnerBranchAddress.getZipCode();
                        
                    }
                    
                    if (mbIsIssueForPayroll) {
                        msZipCodeEmployee = (String) miClient.getSession().readField(SModConsts.HRSU_EMP, new int[] { mnBizPartnerId }, SDbEmployee.FIELD_ZIP_CODE);
                    }
                    
                    if (bizPartnerBranchAddress == null) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP + "\n" + SDataBizPartnerBranchAddress.class.getName() + ".");
                    }
                    else  {
                        SCceEmisorAddressAux cceEmisorAddress = null;

                        if (mbIsIssueForIntCommerce) {
                            cceEmisorAddress = miClient.getSessionXXX().getParamsCompany().getEmisorAddress(bizPartnerBranchAddress.getZipCode());

                            if (cceEmisorAddress == null) {
                                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP + "\nConfiguración de emisor para el CCE, para el CP " + bizPartnerBranchAddress.getZipCode() + ".");
                            }
                        }

                        cfdBizPartner = new SCfdDataBizPartner();
                        cfdBizPartner.setBizPartnerId(mnBizPartnerId);
                        cfdBizPartner.setBizPartnerBranchId(mnBizPartnerBranchId);
                        cfdBizPartner.setBizPartnerBranchHqId(bizPartner.getDbmsBizPartnerBranchHq().getPkBizPartnerBranchId());
                        cfdBizPartner.setBizPartnerRfc(bizPartner.getFiscalId());
                        cfdBizPartner.setBizPartnerCurp(bizPartner.getAlternativeId());
                        cfdBizPartner.setBizPartnerFiscalForeing(bizPartner.getFiscalFrgId());
                        String name = !bizPartner.getBizPartnerFiscal().isEmpty() ? bizPartner.getBizPartnerFiscal() : bizPartner.getProperName();
                        cfdBizPartner.setBizPartnerName(name);
                        cfdBizPartner.setBizPartnerStreet(bizPartnerBranchAddress.getStreet());
                        cfdBizPartner.setBizPartnerStreetNumberExt(bizPartnerBranchAddress.getStreetNumberExt());
                        cfdBizPartner.setBizPartnerStreetNumberInt(bizPartnerBranchAddress.getStreetNumberInt());
                        cfdBizPartner.setBizPartnerNeighborhood(!mbIsIssueForIntCommerce ? bizPartnerBranchAddress.getNeighborhood() : cceEmisorAddress.getCfdCceEmisorColonia());
                        cfdBizPartner.setBizPartnerReference(bizPartnerBranchAddress.getReference());
                        cfdBizPartner.setBizPartnerLocality(!mbIsIssueForIntCommerce ? bizPartnerBranchAddress.getLocality() : cceEmisorAddress.getCfdCceEmisorLocalidad());
                        cfdBizPartner.setBizPartnerCounty(!mbIsIssueForIntCommerce ? bizPartnerBranchAddress.getCounty() : cceEmisorAddress.getCfdCceEmisorMunicipio());
                        cfdBizPartner.setBizPartnerStateCode(bizPartnerBranchAddress.getDbmsDataState().getStateCode());
                        cfdBizPartner.setBizPartnerStateName(bizPartnerBranchAddress.getState());
                        cfdBizPartner.setBizPartnerZipCode(bizPartnerBranchAddress.getZipCode());
                        cfdBizPartner.setBizPartnerZipCodeHq(msZipCodeHq);
                        cfdBizPartner.setBizPartnerZipCodeEmployee(msZipCodeEmployee);
                        cfdBizPartner.setBizPartnerPoBox(bizPartnerBranchAddress.getPoBox());
                        cfdBizPartner.setBizPartnerCountryCode(bizPartnerBranchAddress.getDbmsDataCountry().getCountryCode());
                        cfdBizPartner.setBizPartnerCountryName(bizPartnerBranchAddress.getDbmsDataCountry().getCountry());
                        cfdBizPartner.setBizPartnerCountryId(bizPartnerBranchAddress.getDbmsDataCountry().getPkCountryId());

                        // expedition business partner information:

                        if (mnIssuingBizPartnerId != 0 && mnIssuingBizPartnerBranchId != 0) {
                            SDataBizPartner expeditionBizPartner = null;
                            
                            if (mnBizPartnerId == mnIssuingBizPartnerId) {
                                expeditionBizPartner = bizPartner;
                            }
                            else {
                                expeditionBizPartner = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, new int[] { mnIssuingBizPartnerId }, SLibConstants.EXEC_MODE_SILENT);
                            }

                            if (expeditionBizPartner == null) {
                                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP + "\n" + SDataBizPartner.class.getName() + " de expedición.");
                            }
                            else {
                                SDataBizPartnerBranch expeditionBizPartnerBranch = expeditionBizPartner.getDbmsBizPartnerBranch(new int[] { mnIssuingBizPartnerBranchId });

                                if (expeditionBizPartnerBranch == null) {
                                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP + "\n" + SDataBizPartnerBranch.class.getName() + " de expedición.");
                                }
                                else {
                                    SDataBizPartnerBranchAddress expeditionBizPartnerBranchAddress = null;

                                    if (expeditionBizPartnerBranch.getIsAddressPrintable()) {
                                        expeditionBizPartnerBranchAddress = expeditionBizPartnerBranch.getDbmsBizPartnerBranchAddressOfficial();
                                    }
                                    else {
                                        expeditionBizPartnerBranchAddress = expeditionBizPartner.getDbmsBizPartnerBranchHq().getDbmsBizPartnerBranchAddressOfficial();
                                    }
                                    
                                    if (expeditionBizPartnerBranchAddress == null) {
                                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP + "\n" + SDataBizPartnerBranchAddress.class.getName() + ".");
                                    }
                                    else  {
                                        SCceEmisorAddressAux expeditionCceEmisorAddress = null;

                                        if (mbIsIssueForIntCommerce) {
                                            expeditionCceEmisorAddress = miClient.getSessionXXX().getParamsCompany().getEmisorAddress(expeditionBizPartnerBranchAddress.getZipCode());

                                            if (expeditionCceEmisorAddress == null) {
                                                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP + "\nConfiguración de emisor para el CCE, para el CP " + expeditionBizPartnerBranchAddress.getZipCode() + ".");
                                            }
                                        }

                                        cfdBizPartner.setBizPartnerExpeditionStreet(expeditionBizPartnerBranchAddress.getStreet());
                                        cfdBizPartner.setBizPartnerExpeditionStreetNumberExt(expeditionBizPartnerBranchAddress.getStreetNumberExt());
                                        cfdBizPartner.setBizPartnerExpeditionStreetNumberInt(expeditionBizPartnerBranchAddress.getStreetNumberInt());
                                        cfdBizPartner.setBizPartnerExpeditionNeighborhood(!mbIsIssueForIntCommerce ? expeditionBizPartnerBranchAddress.getNeighborhood() : expeditionCceEmisorAddress.getCfdCceEmisorColonia());
                                        cfdBizPartner.setBizPartnerExpeditionReference(expeditionBizPartnerBranchAddress.getReference());
                                        cfdBizPartner.setBizPartnerExpeditionLocality(!mbIsIssueForIntCommerce ? expeditionBizPartnerBranchAddress.getLocality() : expeditionCceEmisorAddress.getCfdCceEmisorLocalidad());
                                        cfdBizPartner.setBizPartnerExpeditionCounty(!mbIsIssueForIntCommerce ? expeditionBizPartnerBranchAddress.getCounty() : expeditionCceEmisorAddress.getCfdCceEmisorMunicipio());
                                        cfdBizPartner.setBizPartnerExpeditionState(!mbIsIssueForIntCommerce ? expeditionBizPartnerBranchAddress.getState() : expeditionBizPartnerBranchAddress.getDbmsDataState().getStateCode());
                                        cfdBizPartner.setBizPartnerExpeditionZipCode(expeditionBizPartnerBranchAddress.getZipCode());
                                        cfdBizPartner.setBizPartnerExpeditionPoBox(expeditionBizPartnerBranchAddress.getPoBox());
                                        cfdBizPartner.setBizPartnerExpeditionCountryName(!mbIsIssueForIntCommerce ? expeditionBizPartnerBranchAddress.getDbmsDataCountry().getCountry() : expeditionBizPartnerBranchAddress.getDbmsDataCountry().getCountryCode());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
        
        return cfdBizPartner;
    }
}
