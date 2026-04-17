/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.model.account;

/**
 *
 * @author Sergio Flores
 */
public class SAccountSettings {
    
    protected Group moGroup;
    protected Partner moPartner;
    protected Unit moPartnerUnit;
    protected Case moPartnerCase;
    
    public SAccountSettings(final Group group, final Partner partner, final Unit partnerUnit, final Case partnerCase) {
        moGroup = group;
        moPartner = partner;
        moPartnerUnit = partnerUnit;
        moPartnerCase = partnerCase;
    }
    
    public Group getGroup() { return moGroup; }
    public Partner getPartner() { return moPartner; }
    public Unit getPartnerUnit() { return moPartnerUnit; }
    public Case getPartnerCase() { return moPartnerCase; }
}
