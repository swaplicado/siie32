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
import sa.lib.SLibUtils;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SDbCfdBizPartner {

    private SClientInterface miClient;
    
    protected int mnBizPartnerId;
    protected int mnBizPartnerBranchId;
    protected int mnBizPartnerBranchAddressId;
    protected int mnExpeditionBizPartnerId;
    protected int mnExpeditionBizPartnerBranchId;
    protected boolean mbIsEmisor;
    protected boolean mbIsEmisorForIntCommerce;
    
    public SDbCfdBizPartner(SClientInterface client) {
        miClient = client;
        
        mnBizPartnerId = 0;
        mnBizPartnerBranchId = 0;
        mnExpeditionBizPartnerId = 0;
        mnExpeditionBizPartnerBranchId = 0;
        mbIsEmisor = false;
        mbIsEmisorForIntCommerce = false;
    }
    
    public void setBizPartnerIds(final int bizPartnerId, final int bizPartnerBranchId) {
        setBizPartnerIds(bizPartnerId, bizPartnerBranchId, 0);
    }
    
    public void setBizPartnerIds(final int bizPartnerId, final int bizPartnerBranchId, final int bizPartnerBranchAddressId) {
        mnBizPartnerId = bizPartnerId;
        mnBizPartnerBranchId = bizPartnerBranchId;
        mnBizPartnerBranchAddressId = bizPartnerBranchAddressId;
    }
    
    public void setExpeditionBizPartnerIds(final int expeditionBizPartnerId, final int expeditionBizPartnerBranchId) {
        mnExpeditionBizPartnerId = expeditionBizPartnerId;
        mnExpeditionBizPartnerBranchId = expeditionBizPartnerBranchId;
    }
    
    public void setIsEmisor(final boolean isEmisor, final boolean isEmisonForIntCommerce) {
        mbIsEmisor = isEmisor;
        mbIsEmisorForIntCommerce = isEmisonForIntCommerce;
    }
    
    public SCfdDataBizPartner getCfdDataBizPartner() throws Exception {
        SCfdDataBizPartner cfdBizPartner = null;
        
        try {
            // business partner information:
            
            SDataBizPartner bizPartner = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, new int[] { mnBizPartnerId }, SLibConstants.EXEC_MODE_SILENT);
            
            if (bizPartner == null) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP + "\n" + SDataBizPartner.class.getName() + ".");
            }
            else {
                SDataBizPartnerBranch bizPartnerBranch = (SDataBizPartnerBranch) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB, new int[] { mnBizPartnerBranchId }, SLibConstants.EXEC_MODE_SILENT);
                
                if (bizPartnerBranch == null) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP + "\n" + SDataBizPartnerBranch.class.getName() + ".");
                }
                else {
                    SDataBizPartnerBranchAddress bizPartnerBranchAddress = null;
                    
                    if (mnBizPartnerBranchAddressId != 0) {
                        bizPartnerBranchAddress = bizPartnerBranch.getDbmsBizPartnerBranchAddress(new int[] { mnBizPartnerBranchId, mnBizPartnerBranchAddressId });
                    }
                    else if (!mbIsEmisor && bizPartnerBranch.getIsAddressPrintable()) {
                        bizPartnerBranchAddress = bizPartnerBranch.getDbmsBizPartnerBranchAddressOfficial();
                    }
                    else {
                        bizPartnerBranchAddress = bizPartner.getDbmsHqBranch().getDbmsBizPartnerBranchAddressOfficial();
                    }
                    
                    if (bizPartnerBranchAddress == null) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP + "\n" + SDataBizPartnerBranchAddress.class.getName() + ".");
                    }
                    else  {
                        SCceEmisorAddressAux cceEmisorAddress = null;

                        if (mbIsEmisorForIntCommerce) {
                            cceEmisorAddress = miClient.getSessionXXX().getParamsCompany().getEmisorAddress(bizPartnerBranchAddress.getZipCode());

                            if (cceEmisorAddress == null) {
                                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP + "\nConfiguración de emisor para el CCE, para el CP " + bizPartnerBranchAddress.getZipCode() + ".");
                            }
                        }

                        cfdBizPartner = new SCfdDataBizPartner();
                        cfdBizPartner.setBizPartnerId(mnBizPartnerId);
                        cfdBizPartner.setBizPartnerBranchId(mnBizPartnerBranchId);
                        cfdBizPartner.setBizPartnerBranchHqId(bizPartner.getDbmsHqBranch().getPkBizPartnerBranchId());
                        cfdBizPartner.setBizPartnerRfc(bizPartner.getFiscalId());
                        cfdBizPartner.setBizPartnerCurp(bizPartner.getAlternativeId());
                        cfdBizPartner.setBizPartnerFiscalForeing(bizPartner.getFiscalFrgId());
                        cfdBizPartner.setBizPartnerName(bizPartner.getProperName());
                        cfdBizPartner.setBizPartnerStreet(bizPartnerBranchAddress.getStreet());
                        cfdBizPartner.setBizPartnerStreetNumberExt(bizPartnerBranchAddress.getStreetNumberExt());
                        cfdBizPartner.setBizPartnerStreetNumberInt(bizPartnerBranchAddress.getStreetNumberInt());
                        cfdBizPartner.setBizPartnerNeighborhood(!mbIsEmisorForIntCommerce ? bizPartnerBranchAddress.getNeighborhood() : cceEmisorAddress.getCfdCceEmisorColonia());
                        cfdBizPartner.setBizPartnerReference(bizPartnerBranchAddress.getReference());
                        cfdBizPartner.setBizPartnerLocality(!mbIsEmisorForIntCommerce ? bizPartnerBranchAddress.getLocality() : cceEmisorAddress.getCfdCceEmisorLocalidad());
                        cfdBizPartner.setBizPartnerCounty(!mbIsEmisorForIntCommerce ? bizPartnerBranchAddress.getCounty() : cceEmisorAddress.getCfdCceEmisorMunicipio());
                        cfdBizPartner.setBizPartnerStateCode(bizPartnerBranchAddress.getDbmsDataState().getStateCode());
                        cfdBizPartner.setBizPartnerStateName(bizPartnerBranchAddress.getState());
                        cfdBizPartner.setBizPartnerZipCode(bizPartnerBranchAddress.getZipCode());
                        cfdBizPartner.setBizPartnerPoBox(bizPartnerBranchAddress.getPoBox());
                        cfdBizPartner.setBizPartnerCountryCode(bizPartnerBranchAddress.getDbmsDataCountry().getCountryCode());
                        cfdBizPartner.setBizPartnerCountryName(bizPartnerBranchAddress.getDbmsDataCountry().getCountry());
                        cfdBizPartner.setBizPartnerCountryId(bizPartnerBranchAddress.getDbmsDataCountry().getPkCountryId());

                        // expedition business partner information:

                        if (mnExpeditionBizPartnerId != 0 && mnExpeditionBizPartnerBranchId != 0) {
                            SDataBizPartner expeditionBizPartner = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, new int[] { mnExpeditionBizPartnerId }, SLibConstants.EXEC_MODE_SILENT);

                            if (expeditionBizPartner == null) {
                                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP + "\n" + SDataBizPartner.class.getName() + " de expedición.");
                            }
                            else {
                                SDataBizPartnerBranch expeditionBizPartnerBranch = (SDataBizPartnerBranch) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB, new int[] { mnExpeditionBizPartnerBranchId }, SLibConstants.EXEC_MODE_SILENT);

                                if (expeditionBizPartnerBranch == null) {
                                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP + "\n" + SDataBizPartnerBranch.class.getName() + " de expedición.");
                                }
                                else {
                                    SDataBizPartnerBranchAddress expeditionBizPartnerBranchAddress = null;

                                    if (expeditionBizPartnerBranch.getIsAddressPrintable()) {
                                        expeditionBizPartnerBranchAddress = expeditionBizPartnerBranch.getDbmsBizPartnerBranchAddressOfficial();
                                    }
                                    else {
                                        expeditionBizPartnerBranchAddress = expeditionBizPartner.getDbmsHqBranch().getDbmsBizPartnerBranchAddressOfficial();
                                    }
                                    
                                    if (expeditionBizPartnerBranchAddress == null) {
                                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP + "\n" + SDataBizPartnerBranchAddress.class.getName() + ".");
                                    }
                                    else  {
                                        SCceEmisorAddressAux expeditionCceEmisorAddress = null;

                                        if (mbIsEmisorForIntCommerce) {
                                            expeditionCceEmisorAddress = miClient.getSessionXXX().getParamsCompany().getEmisorAddress(expeditionBizPartnerBranchAddress.getZipCode());

                                            if (expeditionCceEmisorAddress == null) {
                                                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP + "\nConfiguración de emisor para el CCE, para el CP " + expeditionBizPartnerBranchAddress.getZipCode() + ".");
                                            }
                                        }

                                        cfdBizPartner.setBizPartnerExpeditionStreet(expeditionBizPartnerBranchAddress.getStreet());
                                        cfdBizPartner.setBizPartnerExpeditionStreetNumberExt(expeditionBizPartnerBranchAddress.getStreetNumberExt());
                                        cfdBizPartner.setBizPartnerExpeditionStreetNumberInt(expeditionBizPartnerBranchAddress.getStreetNumberInt());
                                        cfdBizPartner.setBizPartnerExpeditionNeighborhood(!mbIsEmisorForIntCommerce ? expeditionBizPartnerBranchAddress.getNeighborhood() : expeditionCceEmisorAddress.getCfdCceEmisorColonia());
                                        cfdBizPartner.setBizPartnerExpeditionReference(expeditionBizPartnerBranchAddress.getReference());
                                        cfdBizPartner.setBizPartnerExpeditionLocality(!mbIsEmisorForIntCommerce ? expeditionBizPartnerBranchAddress.getLocality() : expeditionCceEmisorAddress.getCfdCceEmisorLocalidad());
                                        cfdBizPartner.setBizPartnerExpeditionCounty(!mbIsEmisorForIntCommerce ? expeditionBizPartnerBranchAddress.getCounty() : expeditionCceEmisorAddress.getCfdCceEmisorMunicipio());
                                        cfdBizPartner.setBizPartnerExpeditionState(!mbIsEmisorForIntCommerce ? expeditionBizPartnerBranchAddress.getState() : expeditionBizPartnerBranchAddress.getDbmsDataState().getStateCode());
                                        cfdBizPartner.setBizPartnerExpeditionZipCode(expeditionBizPartnerBranchAddress.getZipCode());
                                        cfdBizPartner.setBizPartnerExpeditionPoBox(expeditionBizPartnerBranchAddress.getPoBox());
                                        cfdBizPartner.setBizPartnerExpeditionCountryName(!mbIsEmisorForIntCommerce ? expeditionBizPartnerBranchAddress.getDbmsDataCountry().getCountry() : expeditionBizPartnerBranchAddress.getDbmsDataCountry().getCountryCode());
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
