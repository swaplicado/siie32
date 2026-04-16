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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Partner {
    
    public static final String TYPE_ALL = "all";
    public static final String TYPE_PERSON = "person";
    public static final String TYPE_ORGANIZATION = "organization";
    
    public static final HashMap<String, String> Types = new HashMap<>();
    public static final HashMap<String, String> TypeCodes = new HashMap<>();
    
    static {
        Types.put(TYPE_PERSON, "Persona física");
        Types.put(TYPE_ORGANIZATION, "Persona moral");
        
        TypeCodes.put(TYPE_PERSON, "PF");
        TypeCodes.put(TYPE_ORGANIZATION, "PM");
    }
    
    private String partnerType;
    private List<Unit> units;
    private List<Case> cases;

    public String getPartnerType() { return partnerType; }
    public void setPartnerType(String partnerType) { this.partnerType = partnerType; }

    public List<Unit> getUnits() { return units; }
    public void setUnits(List<Unit> units) { this.units = units; }

    public List<Case> getCases() { return cases; }
    public void setCases(List<Case> cases) { this.cases = cases; }
    
    public Unit getUnit(final String key) {
        Unit unit = null;
        
        for (Unit u : units) {
            if (u.getKey().equals(key)) {
                unit = u;
                break;
            }
        }
        
        return unit;
    }
    
    public Case getCase(final String key, ArrayList<String> descriptions) {
        Case theCase = null;
        
        CASE:
        for (Case c : cases) {
            for (ProdServ ps : c.getProdServs()) {
                if (ps.getKey().equals(key)) {
                    int matches = 0;
                    
                    for (String description : descriptions) {
                        if (c.matchesKeyWords(description)) {
                            matches++;
                        }
                    }
                    
                    if (matches == descriptions.size()) {
                        theCase = c;
                        break CASE;
                    }
                }
            }
        }
        
        return theCase;
    }
}
