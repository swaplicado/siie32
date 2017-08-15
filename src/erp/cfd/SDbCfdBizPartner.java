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
    protected int mnBizPartnerExpeditionId;
    protected int mnBizPartnerBranchExpeditionId;
    protected boolean mbIsEmisor;
    protected boolean mbIsEmisorForIntCommerce;
    
    public SDbCfdBizPartner(SClientInterface client) {
        miClient = client;
        
        mnBizPartnerId = 0;
        mnBizPartnerBranchId = 0;
        mnBizPartnerExpeditionId = 0;
        mnBizPartnerBranchExpeditionId = 0;
        mbIsEmisor = false;
        mbIsEmisorForIntCommerce = false;
    }
    
    public void setBizPartnerId(int n) { mnBizPartnerId = n; }
    public void setBizPartnerBranchId(int n) { mnBizPartnerBranchId = n; }
    public void setBizPartnerExpeditionId(int n) { mnBizPartnerExpeditionId = n; }
    public void setBizPartnerBranchExpeditionId(int n) { mnBizPartnerBranchExpeditionId = n; }
    public void setIsEmisor(boolean b) { mbIsEmisor = b; }
    public void setIsEmisorForIntCommerce(boolean b) { mbIsEmisorForIntCommerce = b; }
    
    public int getBizPartnerId() { return mnBizPartnerId; }
    public int getBizPartnerBranchId() { return mnBizPartnerBranchId; }
    public int getBizPartnerExpeditionId() { return mnBizPartnerExpeditionId; }
    public int getBizPartnerBranchExpeditionId() { return mnBizPartnerBranchExpeditionId; }
    public boolean isEmisor() { return mbIsEmisor; }
    public boolean isEmisorForIntCommerce() { return mbIsEmisorForIntCommerce; }
    
    public SCfdDataBizPartner getBizPartner() throws Exception {
        SCfdDataBizPartner dataBizPartner = null;
        
        try {
            SDataBizPartner moBizPartner = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, new int[] { mnBizPartnerId }, SLibConstants.EXEC_MODE_SILENT);
            
            if (moBizPartner == null) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP + "\n" + SDataBizPartner.class.getName());
            }
            else {
                SDataBizPartnerBranch moBizPartnerBranch = (SDataBizPartnerBranch) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB, new int[] { mnBizPartnerBranchId }, SLibConstants.EXEC_MODE_SILENT);
                
                if (moBizPartnerBranch == null) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP + "\n" + SDataBizPartnerBranch.class.getName());
                }
                else {
                    SDataBizPartnerBranchAddress moBizPartnerBranchAddress = null;
                    SCceEmisorAddressAux emisorAddress = null;
                    
                    if (!mbIsEmisor && moBizPartnerBranch.getIsAddressPrintable()) {
                        moBizPartnerBranchAddress = moBizPartnerBranch.getDbmsBizPartnerBranchAddressOfficial();
                    }
                    else {
                        moBizPartnerBranchAddress = moBizPartner.getDbmsHqBranch().getDbmsBizPartnerBranchAddressOfficial();
                    }
                    
                    if (mbIsEmisorForIntCommerce) {
                        emisorAddress = miClient.getSessionXXX().getParamsCompany().getEmisorAddress(moBizPartnerBranchAddress.getZipCode());
                        
                        if (emisorAddress == null) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP + "\n" + SCceEmisorAddressAux.class.getName());
                        }
                    }

                    dataBizPartner = new SCfdDataBizPartner();
                    dataBizPartner.setBizPartnerId(mnBizPartnerId);
                    dataBizPartner.setBizPartnerBranchId(mnBizPartnerBranchId);
                    dataBizPartner.setBizPartnerBranchHqId(moBizPartner.getDbmsHqBranch().getPkBizPartnerBranchId());
                    dataBizPartner.setBizPartnerRfc(moBizPartner.getFiscalId());
                    dataBizPartner.setBizPartnerCurp(moBizPartner.getAlternativeId());
                    dataBizPartner.setBizPartnerFiscalForeing(moBizPartner.getFiscalFrgId());
                    dataBizPartner.setBizPartnerName(moBizPartner.getProperName());
                    dataBizPartner.setBizPartnerStreet(moBizPartnerBranchAddress.getStreet());
                    dataBizPartner.setBizPartnerStreetNumberExt(moBizPartnerBranchAddress.getStreetNumberExt());
                    dataBizPartner.setBizPartnerStreetNumberInt(moBizPartnerBranchAddress.getStreetNumberInt());
                    dataBizPartner.setBizPartnerNeighborhood(!mbIsEmisorForIntCommerce ? moBizPartnerBranchAddress.getNeighborhood() : emisorAddress.getCfdCceEmisorColonia());
                    dataBizPartner.setBizPartnerReference(moBizPartnerBranchAddress.getReference());
                    dataBizPartner.setBizPartnerLocality(!mbIsEmisorForIntCommerce ? moBizPartnerBranchAddress.getLocality() : emisorAddress.getCfdCceEmisorLocalidad());
                    dataBizPartner.setBizPartnerCounty(!mbIsEmisorForIntCommerce ? moBizPartnerBranchAddress.getCounty() : emisorAddress.getCfdCceEmisorMunicipio());
                    dataBizPartner.setBizPartnerStateCode(moBizPartnerBranchAddress.getDbmsDataState().getStateCode());
                    dataBizPartner.setBizPartnerStateName(moBizPartnerBranchAddress.getState());
                    dataBizPartner.setBizPartnerZipCode(moBizPartnerBranchAddress.getZipCode());
                    dataBizPartner.setBizPartnerPoBox(moBizPartnerBranchAddress.getPoBox());
                    dataBizPartner.setBizPartnerCountryCode(moBizPartnerBranchAddress.getDbmsDataCountry().getCountryCode());
                    dataBizPartner.setBizPartnerCountryName(moBizPartnerBranchAddress.getDbmsDataCountry().getCountry());
                    dataBizPartner.setBizPartnerCountryId(moBizPartnerBranchAddress.getDbmsDataCountry().getPkCountryId());
                    
                    if (mnBizPartnerExpeditionId != SLibConsts.UNDEFINED && mnBizPartnerBranchExpeditionId != SLibConsts.UNDEFINED) {
                        moBizPartner = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, new int[] { mnBizPartnerExpeditionId }, SLibConstants.EXEC_MODE_SILENT);

                        if (moBizPartner == null) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP + "\n" + SDataBizPartner.class.getName());
                        }
                        else {
                            moBizPartnerBranch = (SDataBizPartnerBranch) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB, new int[] { mnBizPartnerBranchExpeditionId }, SLibConstants.EXEC_MODE_SILENT);
                            
                            if (moBizPartnerBranch == null) {
                                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP + "\n" + SDataBizPartnerBranch.class.getName());
                            }
                            else {
                                if (moBizPartnerBranch.getIsAddressPrintable()) {
                                    moBizPartnerBranchAddress = moBizPartnerBranch.getDbmsBizPartnerBranchAddressOfficial();
                                }
                                else {
                                    moBizPartnerBranchAddress = moBizPartner.getDbmsHqBranch().getDbmsBizPartnerBranchAddressOfficial();
                                }
                                
                                if (mbIsEmisorForIntCommerce) {
                                    emisorAddress = miClient.getSessionXXX().getParamsCompany().getEmisorAddress(moBizPartnerBranchAddress.getZipCode());
                        
                                    if (emisorAddress == null) {
                                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP + "\n" + SCceEmisorAddressAux.class.getName());
                                    }
                                }
                                
                                dataBizPartner.setBizPartnerExpeditionStreet(moBizPartnerBranchAddress.getStreet());
                                dataBizPartner.setBizPartnerExpeditionStreetNumberExt(moBizPartnerBranchAddress.getStreetNumberExt());
                                dataBizPartner.setBizPartnerExpeditionStreetNumberInt(moBizPartnerBranchAddress.getStreetNumberInt());
                                dataBizPartner.setBizPartnerExpeditionNeighborhood(!mbIsEmisorForIntCommerce ? moBizPartnerBranchAddress.getNeighborhood() : emisorAddress.getCfdCceEmisorColonia());
                                dataBizPartner.setBizPartnerExpeditionReference(moBizPartnerBranchAddress.getReference());
                                dataBizPartner.setBizPartnerExpeditionLocality(!mbIsEmisorForIntCommerce ? moBizPartnerBranchAddress.getLocality() : emisorAddress.getCfdCceEmisorLocalidad());
                                dataBizPartner.setBizPartnerExpeditionCounty(!mbIsEmisorForIntCommerce ? moBizPartnerBranchAddress.getCounty() : emisorAddress.getCfdCceEmisorMunicipio());
                                dataBizPartner.setBizPartnerExpeditionState(!mbIsEmisorForIntCommerce ? moBizPartnerBranchAddress.getState() : moBizPartnerBranchAddress.getDbmsDataState().getStateCode());
                                dataBizPartner.setBizPartnerExpeditionZipCode(moBizPartnerBranchAddress.getZipCode());
                                dataBizPartner.setBizPartnerExpeditionPoBox(moBizPartnerBranchAddress.getPoBox());
                                dataBizPartner.setBizPartnerExpeditionCountryName(!mbIsEmisorForIntCommerce ? moBizPartnerBranchAddress.getDbmsDataCountry().getCountry() : moBizPartnerBranchAddress.getDbmsDataCountry().getCountryCode());
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
