/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.swap.model.account;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import sa.lib.SLibUtils;

/**
 *
 * @author Sergio Flores
 */
public class Case {

    private String caseName;
    private String caseCode;
    private List<ProdServ> prodServs;
    /** Lower-case key words. */
    private List<String> keyWords;
    private int item;
    private String itemDesc;
    private int unit;
    private String unitDesc;
    private String account;
    private String costCenter;
    private int docNature;
    private int funcSubArea;
    private int funcSubAreaBol;

    public String getCaseName() { return caseName; }
    public void setCaseName(String caseName) { this.caseName = caseName; }

    public String getCaseCode() { return caseCode; }
    public void setCaseCode(String caseCode) { this.caseCode = caseCode; }

    public List<ProdServ> getProdServs() { return prodServs; }
    public void setProdServs(List<ProdServ> prodServs) { this.prodServs = prodServs; }

    /** Get lower-case key words. */
    public List<String> getKeyWords() { return keyWords; }
    /** Set lower-case key words. All passed key words will be converted to lowercased. */
    public void setKeyWords(List<String> keyWords) { this.keyWords = keyWords.stream().map(s -> s == null ? null : s.toLowerCase()).collect(Collectors.toList()); }

    public int getItem() { return item; }
    public void setItem(int item) { this.item = item; }

    public String getItemDesc() { return itemDesc; }
    public void setItemDesc(String itemDesc) { this.itemDesc = itemDesc; }

    public int getUnit() { return unit; }
    public void setUnit(int unit) { this.unit = unit; }

    public String getUnitDesc() { return unitDesc; }
    public void setUnitDesc(String unitDesc) { this.unitDesc = unitDesc; }

    public String getAccount() { return normalize(account); }
    public void setAccount(String account) { this.account = account; }

    public String getCostCenter() { return normalize(costCenter); }
    public void setCostCenter(String costCenter) { this.costCenter = costCenter; }

    public int getDocNature() { return docNature; }
    public void setDocNature(int docNature) { this.docNature = docNature; }

    public int getFuncSubArea() { return funcSubArea; }
    public void setFuncSubArea(int funcSubArea) { this.funcSubArea = funcSubArea; }

    public int getFuncSubAreaBol() { return funcSubAreaBol; }
    public void setFuncSubAreaBol(int funcSubAreaBol) { this.funcSubAreaBol = funcSubAreaBol; }

    private String normalize(String value) {
        return (value == null || "null".equals(value)) ? null : value;
    }

    /**
     * Get ProdServ by key.
     * @param key Key of desired ProdServ.
     * @return ProdServ, if found, otherwise <code>null</code>.
     */
    public ProdServ getProdServ(final String key) {
        ProdServ prodServ = null;
        
        for (ProdServ ps : prodServs) {
            if (ps.getKey().equals(key)) {
                prodServ = ps;
                break;
            }
        }
        
        return prodServ;
    }

    /**
     * Check if any set of lower-case key words matches the given text to be evaluated also in lower case, using only ASCII characters.
     * Format of set of every key words: word1+word2+word3
     * @param textToEvaluate Text to evaluate for matching.
     * @return Returns <code>true</code> when key words are unavailable or the text to be evaluated matches any set of key words, that is, all words of the set in the defined order; otherwise <code>false</code>.
     */
    public boolean matchesKeyWords(String textToEvaluate) {
        boolean matches = keyWords.isEmpty(); // matches when key words are unavailable!
        
        if (!matches) {
            String asciiTextToEvaluateInLowerCase = SLibUtils.textToAscii(textToEvaluate).toLowerCase();
            
            for (String keyWord : keyWords) {
                if (keyWord.isEmpty()) {
                    matches = true; // matches when there is an empty key word!
                    break;
                }
                else {
                    int index = 0;
                    boolean perfectMatch = true;
                    String[] asciiWords = SLibUtils.textToAscii(keyWord).split(Pattern.quote("+"));

                    for (String asciiWord : asciiWords) {
                        index = asciiTextToEvaluateInLowerCase.indexOf(asciiWord, index);

                        if (index == -1) {
                            perfectMatch = false;
                            break;
                        }
                    }

                    if (perfectMatch) {
                        matches = true;
                        break;
                    }
                }
            }
        }
        
        return matches;
    }
}