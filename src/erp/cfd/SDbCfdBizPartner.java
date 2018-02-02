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
import sa.lib.SLibConsts;
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
    
    public void setBizPartnerIds(final int idBizPartner, final int idBizPartnerBranch) {
        setBizPartnerIds(idBizPartner, idBizPartnerBranch, 0);
    }
    
    public void setBizPartnerIds(final int idBizPartner, final int idBizPartnerBranch, final int idBizPartnerBranchAddress) {
        mnBizPartnerId = idBizPartner;
        mnBizPartnerBranchId = idBizPartnerBranch;
        mnBizPartnerBranchAddressId = idBizPartnerBranchAddress;
    }
    
    public void setExpeditionBizPartnerIds(final int idBizPartner, final int idBizPartnerBranch) {
        mnExpeditionBizPartnerId = idBizPartner;
        mnExpeditionBizPartnerBranchId = idBizPartnerBranch;
    }
    
    public void setIsEmisor(final boolean isEmisor, final boolean isForIntCommerce) {
        mbIsEmisor = isEmisor;
        mbIsEmisorForIntCommerce = isForIntCommerce;
    }
    
    public SCfdDataBizPartner getBizPartner() throws Exception {
        SCfdDataBizPartner dataBizPartner = null;
        
        try {
            SDataBizPartner bizPartner = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, new int[] { mnBizPartnerId }, SLibConstants.EXEC_MODE_SILENT);
            
            if (bizPartner == null) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP + "\n" + SDataBizPartner.class.getName());
            }
            else {
                SDataBizPartnerBranch bizPartnerBranch = (SDataBizPartnerBranch) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB, new int[] { mnBizPartnerBranchId }, SLibConstants.EXEC_MODE_SILENT);
                
                if (bizPartnerBranch == null) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP + "\n" + SDataBizPartnerBranch.class.getName());
                }
                else {
                    SDataBizPartnerBranchAddress bizPartnerBranchAddress = null;
                    SCceEmisorAddressAux emisorAddress = null;
                    
                    if (mnBizPartnerBranchAddressId != 0) {
                        bizPartnerBranchAddress = bizPartnerBranch.getDbmsBizPartnerBranchAddress(new int[] { mnBizPartnerBranchId, mnBizPartnerBranchAddressId });
                    }
                    else if (!mbIsEmisor && bizPartnerBranch.getIsAddressPrintable()) {
                        bizPartnerBranchAddress = bizPartnerBranch.getDbmsBizPartnerBranchAddressOfficial();
                    }
                    else {
                        bizPartnerBranchAddress = bizPartner.getDbmsHqBranch().getDbmsBizPartnerBranchAddressOfficial();
                    }
                    
                    if (mbIsEmisorForIntCommerce) {
                        emisorAddress = miClient.getSessionXXX().getParamsCompany().getEmisorAddress(bizPartnerBranchAddress.getZipCode());
                        
                        if (emisorAddress == null) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP + "\n" + SCceEmisorAddressAux.class.getName());
                        }
                    }

                    dataBizPartner = new SCfdDataBizPartner();
                    dataBizPartner.setBizPartnerId(mnBizPartnerId);
                    dataBizPartner.setBizPartnerBranchId(mnBizPartnerBranchId);
                    dataBizPartner.setBizPartnerBranchHqId(bizPartner.getDbmsHqBranch().getPkBizPartnerBranchId());
                    dataBizPartner.setBizPartnerRfc(bizPartner.getFiscalId());
                    dataBizPartner.setBizPartnerCurp(bizPartner.getAlternativeId());
                    dataBizPartner.setBizPartnerFiscalForeing(bizPartner.getFiscalFrgId());
                    dataBizPartner.setBizPartnerName(bizPartner.getProperName());
                    dataBizPartner.setBizPartnerStreet(bizPartnerBranchAddress.getStreet());
                    dataBizPartner.setBizPartnerStreetNumberExt(bizPartnerBranchAddress.getStreetNumberExt());
                    dataBizPartner.setBizPartnerStreetNumberInt(bizPartnerBranchAddress.getStreetNumberInt());
                    dataBizPartner.setBizPartnerNeighborhood(!mbIsEmisorForIntCommerce ? bizPartnerBranchAddress.getNeighborhood() : emisorAddress.getCfdCceEmisorColonia());
                    dataBizPartner.setBizPartnerReference(bizPartnerBranchAddress.getReference());
                    dataBizPartner.setBizPartnerLocality(!mbIsEmisorForIntCommerce ? bizPartnerBranchAddress.getLocality() : emisorAddress.getCfdCceEmisorLocalidad());
                    dataBizPartner.setBizPartnerCounty(!mbIsEmisorForIntCommerce ? bizPartnerBranchAddress.getCounty() : emisorAddress.getCfdCceEmisorMunicipio());
                    dataBizPartner.setBizPartnerStateCode(bizPartnerBranchAddress.getDbmsDataState().getStateCode());
                    dataBizPartner.setBizPartnerStateName(bizPartnerBranchAddress.getState());
                    dataBizPartner.setBizPartnerZipCode(bizPartnerBranchAddress.getZipCode());
                    dataBizPartner.setBizPartnerPoBox(bizPartnerBranchAddress.getPoBox());
                    dataBizPartner.setBizPartnerCountryCode(bizPartnerBranchAddress.getDbmsDataCountry().getCountryCode());
                    dataBizPartner.setBizPartnerCountryName(bizPartnerBranchAddress.getDbmsDataCountry().getCountry());
                    dataBizPartner.setBizPartnerCountryId(bizPartnerBranchAddress.getDbmsDataCountry().getPkCountryId());
                    
                    if (mnExpeditionBizPartnerId != SLibConsts.UNDEFINED && mnExpeditionBizPartnerBranchId != SLibConsts.UNDEFINED) {
                        bizPartner = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, new int[] { mnExpeditionBizPartnerId }, SLibConstants.EXEC_MODE_SILENT);

                        if (bizPartner == null) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP + "\n" + SDataBizPartner.class.getName());
                        }
                        else {
                            bizPartnerBranch = (SDataBizPartnerBranch) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB, new int[] { mnExpeditionBizPartnerBranchId }, SLibConstants.EXEC_MODE_SILENT);
                            
                            if (bizPartnerBranch == null) {
                                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP + "\n" + SDataBizPartnerBranch.class.getName());
                            }
                            else {
                                if (bizPartnerBranch.getIsAddressPrintable()) {
                                    bizPartnerBranchAddress = bizPartnerBranch.getDbmsBizPartnerBranchAddressOfficial();
                                }
                                else {
                                    bizPartnerBranchAddress = bizPartner.getDbmsHqBranch().getDbmsBizPartnerBranchAddressOfficial();
                                }
                                
                                if (mbIsEmisorForIntCommerce) {
                                    emisorAddress = miClient.getSessionXXX().getParamsCompany().getEmisorAddress(bizPartnerBranchAddress.getZipCode());
                        
                                    if (emisorAddress == null) {
                                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP + "\n" + SCceEmisorAddressAux.class.getName());
                                    }
                                }
                                
                                dataBizPartner.setBizPartnerExpeditionStreet(bizPartnerBranchAddress.getStreet());
                                dataBizPartner.setBizPartnerExpeditionStreetNumberExt(bizPartnerBranchAddress.getStreetNumberExt());
                                dataBizPartner.setBizPartnerExpeditionStreetNumberInt(bizPartnerBranchAddress.getStreetNumberInt());
                                dataBizPartner.setBizPartnerExpeditionNeighborhood(!mbIsEmisorForIntCommerce ? bizPartnerBranchAddress.getNeighborhood() : emisorAddress.getCfdCceEmisorColonia());
                                dataBizPartner.setBizPartnerExpeditionReference(bizPartnerBranchAddress.getReference());
                                dataBizPartner.setBizPartnerExpeditionLocality(!mbIsEmisorForIntCommerce ? bizPartnerBranchAddress.getLocality() : emisorAddress.getCfdCceEmisorLocalidad());
                                dataBizPartner.setBizPartnerExpeditionCounty(!mbIsEmisorForIntCommerce ? bizPartnerBranchAddress.getCounty() : emisorAddress.getCfdCceEmisorMunicipio());
                                dataBizPartner.setBizPartnerExpeditionState(!mbIsEmisorForIntCommerce ? bizPartnerBranchAddress.getState() : bizPartnerBranchAddress.getDbmsDataState().getStateCode());
                                dataBizPartner.setBizPartnerExpeditionZipCode(bizPartnerBranchAddress.getZipCode());
                                dataBizPartner.setBizPartnerExpeditionPoBox(bizPartnerBranchAddress.getPoBox());
                                dataBizPartner.setBizPartnerExpeditionCountryName(!mbIsEmisorForIntCommerce ? bizPartnerBranchAddress.getDbmsDataCountry().getCountry() : bizPartnerBranchAddress.getDbmsDataCountry().getCountryCode());
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
        
        return dataBizPartner;
    }
}
