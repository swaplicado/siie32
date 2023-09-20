/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.utils;

import java.util.ArrayList;

/**
 *
 * @author Edwin Carmona
 */
public class SAuthorizationConfigJSON {

    private ArrayList<SCondition> conditions = new ArrayList<>();

    public ArrayList<SCondition> getConditions() {
        return conditions;
    }

    public void setConditions(ArrayList<SCondition> conditions) {
        this.conditions = conditions;
    }
}
