import java.util.*;

public class Formula {

    // Formula gave by the user which is transform into a standard form
    private String standardFormula;
    // DNF of the formula
    private String DNF;
    // Table which reference the constraint associated to an identifier
    private HashMap<String, String> reference;

    /**
     * Constructor of the class
     * @param formula Formula gave by the user
     */
    public Formula(String formula){
        reference = new HashMap<>();
        // We will make a standard formula(one space between word)
        standardFormula = setFormula(formula,true);
        String propositionnalFormula = PCLCToPropositionnal(standardFormula);
        DNF = makeDNF(propositionnalFormula);
        DNF = propositionnalToPCLC(DNF);
    }

    /**
     * Set the reference field to associated a constraint at an identifier
     * @param formula Formula at the PCLC form
     * @return String at the propositionnal form
     */
    public String PCLCToPropositionnal(String formula){
        ArrayList<String> litteraux = getWords(formula);
        String cur1,cur2,curLetter = "a",key;
        StringBuilder res = new StringBuilder("( ");
        int index = 1,cptLetterIdentifier = 1,cptLetter = 0;
        int tmp;
        boolean skip = false;
        // We look over the list of words of the formula
        while(index < litteraux.size()){
            cur1 = litteraux.get(index);
            // if we have a comparator operator, we are into a constraint, so we have to replace it
            if(cur1.equals("<") || cur1.equals("<=") || cur1.equals(">") ||cur1.equals(">=") ||cur1.equals("==") ||cur1.equals("!=")){
                cur2 = "";
                tmp = index;
                // we search for the end of the constraint
                while(!cur2.equals(")")){
                    cur2 = litteraux.get(tmp);
                    tmp++;
                }
                tmp--;
                // We take all of the constraint
                cur2 = litterauxToFormula(new ArrayList<>(litteraux.subList(getOpenParenthesis(formula,tmp)+1,tmp+1)));
                // If there isn't an identifier identified this constraint, we create it
                if(!reference.containsValue(cur2)){
                    if (curLetter.contains("z")) {
                        cptLetterIdentifier++;
                        curLetter = "a" + cptLetterIdentifier;
                        cptLetter = 0;
                    } else {
                        curLetter = new String((Character.toChars('a' + cptLetter)));
                        curLetter = curLetter + cptLetterIdentifier;
                        cptLetter++;
                    }
                    reference.put(curLetter,cur2);
                    res.append(curLetter + " ");
                    skip = true;
                // else, we replace the constraint by the identifier
                }else{
                    key = "";
                    for(Map.Entry<String,String> set : reference.entrySet()){
                        if(set.getValue().equals(cur2)){
                            key = set.getKey();
                            break;
                        }
                    }
                    // It's a Hashmap so we can have a lot of key, but here we have once
                    res.append(key + " ");
                    skip = true;
                }
                index = tmp-1;
            }
            // If it's not an constraint, we append the current word
            if(!skip && (cur1.equals("(") || cur1.equals(")") || cur1.equals("and") || cur1.equals("or") || cur1.equals("not"))){
                res.append(cur1 + " ");
            }
            skip = false;
            index ++;
        }
        String resultat = simplification(res.toString());
        return simplification(resultat);
    }

    /**
     * Set the reference field to associated a constraint at an identifier
     * @param formula Formula at the PCLC form
     * @return String at the propositionnal form
     */
    public String propositionnalToPCLC(String formula){
        ArrayList<String> litteraux = getWords(formula);
        int index = 0;
        StringBuilder res = new StringBuilder();
        String cur,key="";
        // We look over the list of words of the formula
        while(index < litteraux.size()){
            cur = litteraux.get(index);
            // If we encounter an indentifier, we replace it by the contraint referenced, else we append the current word
            if(cur.equals("(") || cur.equals(")") || cur.equals("and") || cur.equals("or") || cur.equals("not")){
                res.append(cur + " ");
            }else{
                for(Map.Entry<String,String> set : reference.entrySet()){
                    if(set.getKey().equals(cur)){
                        key = set.getValue();
                        break;
                    }
                }
                res.append(key + " ");
            }
            index++;
        }
        return res.toString();
    }

    /**
     * @param PCLC True if the formula given is in the PCLC form
     * @param formula Formula given by the user
     * @return the formula into a standard form
     */
    public String setFormula(String formula,boolean PCLC){
        StringBuilder res;
        res = new StringBuilder(formula.replaceAll(" {2,}"," "));
        // if the last and first character is a space, we erase it
        if(res.charAt(res.length()-1) == ' '){
            res.deleteCharAt(res.length()-1);
        }
        if(res.charAt(0) == ' '){
            res.deleteCharAt(0);
        }
        // if we encountered an open parenthesis we add a space after it
        if(res.charAt(0) == '(') {
            res.insert(0, " ");
        }

        // if we encountered an close parenthesis we add a space before it
        if(res.charAt(res.length()-1) == ')') {
            res.append(" ");
        }
        int index = 0;
        int d = 0;
        // For each character into the formula...
        for(char c : res.toString().toCharArray()){
            //... if the character is an open parenthesis...
            if(c == '(' ){
                // ... and if there is not a space after we add it
                if(res.toString().toCharArray()[d+1] !=' ') {
                    res.insert(d + 1,' ');
                    index++;
                }
                // ... and if there is not a space before we add it
                if(res.toString().toCharArray()[d-1]!=' ') {
                    res.insert(d,' ');
                    index++;
                    d++;
                }
            }
            //... if the character is a close parenthesis...
            if (c == ')'){
                // ... and if there is not a space after we add it
                if(res.toString().toCharArray()[d+1]!=' ') {
                    res.insert(d+1,' ');
                    index++;
                }
                //  and if there is not a space before we add it
                if (res.toString().toCharArray()[d - 1] != ' ') {
                    res.insert(d,' ');
                    index++;
                    d++;
                }
            }
            index++;
            d = index;
        }
        // if the last and first character is a space, we erase it
        if(res.charAt(res.length()-1) == ' '){
            res.deleteCharAt(res.length()-1);
        }
        if(res.charAt(0) == ' '){
            res.deleteCharAt(0);
        }
        if(res.charAt(0) != '(' || getCloseParenthesis(res.toString(),0)!= getWords(res.toString()).size()) {
            res.insert(0, "( ");
        }
        if(res.charAt(res.length()-1) != ')' || getOpenParenthesis(res.toString(), getWords(res.toString()).size()-1) != 0) {
            res.append(" )");
        }
        // If the formula is in the PCLC form we will add parenthesis all around the constraint
        if(PCLC){
            ArrayList<String> litteraux = getWords(res.toString());
            int index1=0,index2;
            String cur;
            while(index1 < litteraux.size()-1){
                cur = litteraux.get(index1);
                if(cur.equals("<") || cur.equals("<=") || cur.equals(">") ||cur.equals(">=") ||cur.equals("==") ||cur.equals("!=")){
                    index2 = index1-1;
                    while(index2 >= 0){
                        cur = litteraux.get(index2);
                        if(cur.equals(")") || cur.equals("(") ||cur.equals("and") ||cur.equals("or") ||cur.equals("not")){
                            litteraux.add(index2+1,"(");
                            break;
                        }
                        index2--;
                    }
                    // we add an open parenthesis and there is the constant b in the constraint: a1*x1 + a2*x2 < b
                    litteraux.add(index1+3,")");
                    index1++;
                }
                index1++;
            }
            res = new StringBuilder(litterauxToFormula(litteraux));
        }
        return simplification(res.toString());
    }

    /**
     * @param formula Formula which we want the words
     * @return List of words which compose the formula
     */
    private ArrayList<String> getWords(String formula){
        ArrayList<String> l = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(formula);
        while(st.hasMoreTokens()){
            l.add(st.nextToken());
        }
        return l;
    }

    /**
     * @return Standard Formula
     */
    public String getStandardFormula(){
        return standardFormula;
    }

    /**
     * @return the DNF form of the formula
     */
    public String getDNF(){
        return DNF;
    }

    /**
     * Set the formula to the DNF form
     * @param formula formula
     * @return DNF form of the formula
     */
    public String makeDNF(String formula){
        String form = FNN(formula,0,false);
        form = form.replace(".","");
        form = simplification(form);
        String res = makeDNFBis(form);
        return res;
    }

    /**
     * Simplify the formula
     * @param formula Formula
     * @return The formula without parenthesis useless
     */
    public String simplification(String formula){
        ArrayList<String> litteraux = getWords(formula);
        String cur;
        String cur2;
        int index = 0;
        // We look for the form like "( a or a ) or a" or "a or ( a or a )", this is the symmetrical
        while(index < litteraux.size() - 2) {
            cur = litteraux.get(index);
            // If there is a binary operator before an open parenthesis
            if (litteraux.get(index + 1).equals("(")) {
                if (cur.equals("or") || cur.equals("and")) {
                    int cptParenthesis = 1;
                    int indexTmp = index + 2;
                    boolean simplify = false;
                    while (cptParenthesis >= 1) {
                        cur2 = litteraux.get(indexTmp);
                        if (cur2.equals("(")) {
                            cptParenthesis++;
                        }
                        if (cur2.equals(")")) {
                            cptParenthesis--;
                        }
                        // If in the same level of parenthesis we have the same binary operator, these parenthesis is useless
                        if (cptParenthesis == 1) {
                            if (cur.equals(cur2)) {
                                simplify = true;
                            }
                        }
                        indexTmp++;
                    }
                    indexTmp--;
                    if (simplify) {
                        // We remove the open parenthesis
                        litteraux.remove(index + 1);
                        indexTmp--;
                        // We remove the close parenthesis
                        litteraux.remove(indexTmp);
                        index = index-1;
                    }
                }
            }
            // we look for the symmetrical problem
            if (cur.equals(")")) {
                if (litteraux.get(index+1).equals("or") || litteraux.get(index+1).equals("and")) {
                    cur = litteraux.get(index+1);
                    int cptParenthesis = 1;
                    int indexTmp = index - 1;
                    boolean simplify = false;
                    while (cptParenthesis >= 1 && indexTmp > 0) {
                        cur2 = litteraux.get(indexTmp);
                        if (cur2.equals("(")) {
                            cptParenthesis--;
                        }
                        if (cur2.equals(")")) {
                            cptParenthesis++;
                        }
                        if (cptParenthesis == 1) {
                            if (cur.equals(cur2)) {
                                simplify = true;
                            }
                        }
                        indexTmp--;
                    }
                    indexTmp++;
                    if (simplify) {
                        // We remove the close parenthesis
                        litteraux.remove(index);
                        // We remove the open parenthesis
                        litteraux.remove(indexTmp);
                        index = index-2;
                    }
                }
            }
            index++;
        }
        // We delete the parenthesis useless: like ( ( a or b ) )
        int indexTmp;
        index = 0;
        while(index < litteraux.size()) {
            cur = litteraux.get(index);
            if (cur.equals("(")) {
                if(litteraux.get(index + 1).equals("(")) {
                    if (getCloseParenthesis(litterauxToFormula(litteraux), index) - 1 == getCloseParenthesis(litterauxToFormula(litteraux), index + 1)) {
                        indexTmp = getCloseParenthesis(litterauxToFormula(litteraux), index);
                        // We remove the open parenthesis
                        litteraux.remove(index);
                        indexTmp = indexTmp - 2;
                        // We remove the close parenthesis
                        litteraux.remove(indexTmp);
                        index = index - 1;
                    }
                }
                // If you have something like (a), you erase the parenthesis
                if(litteraux.get(index+2).equals(")")){
                    // We remove the open parenthesis
                    litteraux.remove(index);
                    // We remove the close parenthesis
                    litteraux.remove(index+1);
                    index = index - 1;
                }
            }
            index++;
        }
        if(litteraux.size() == 1){
            litteraux.add(0,"(");
            litteraux.add(litteraux.size(),")");
        }
        return litterauxToFormula(litteraux);
    }

    /**
     * @param formula formula given
     * @return the DNF form of the formula
     */
    public String makeDNFBis(String formula){
        if(isDNF(formula)){
            return formula;
        }
        return "";
    }

    /**
     * @param formula to check
     * @return True if the formula is in the DNF form, else false
     */
    public boolean isDNF(String formula){
        boolean dnf = true;
        boolean toChange = false;
        String cur;
        ArrayList<String> litteraux = getWords(formula);
        int cptParenthesis = 1;
        // We begin at 1 because the first character is a (
        int indexTmp = 1;
        while(cptParenthesis >= 1 && !toChange){
            cur = litteraux.get(indexTmp);
            if(cur.equals("(")){
                cptParenthesis++;
            }
            if(cur.equals(")")){
                cptParenthesis--;
            }
            // if there is a level of parenthesis superior to 2, it's not a DNF
            if(cptParenthesis > 2){
                dnf = false;
                toChange = true;
            }
            indexTmp++;
        }
        indexTmp--;
        if(toChange) {
            changeDNF(formula, indexTmp);
        }
        return dnf;
    }

    /**
     * Make change on the formula to make it as an DNF formula
     * @param formula formula given
     * @param index index of the problem
     * @return formula changed
     */
    public String changeDNF(String formula, int index){
        ArrayList<String> litteraux = getWords(formula);
        String cur = litteraux.get(index-1), formula1="", operator="", formula2="",before="",after="";
        int cptParenthesis=1,indexTmp=1,goodIndex=0;
        // If we have something like "a or ( a and d )"
        if(cur.equals("or") || cur.equals("and")){
            // we take what is before the variable
            before = litterauxToFormula(new ArrayList<>(litteraux.subList(0,index -2)));
            // we take the left variable
            formula1 = litteraux.get(index - 2);
            // The operator
            operator = cur;
            // We take the right variable
            formula2 = litterauxToFormula(new ArrayList<>(litteraux.subList(index,getCloseParenthesis(formula,index))));
            // We take what is after the right variable
            after = litterauxToFormula(new ArrayList<>(litteraux.subList(getCloseParenthesis(formula,index),litteraux.size())));
        }else {
            cur = litteraux.get(getCloseParenthesis(formula,index));
            // The symmetrical problem
            if(cur.equals("or") || cur.equals("and")){
                before = litterauxToFormula(new ArrayList<>(litteraux.subList(0,index)));
                formula1 = litteraux.get(getCloseParenthesis(formula,index)+1);
                if(formula1.equals("(")){
                    indexTmp = getCloseParenthesis(formula,index)+1;
                    formula1 = litterauxToFormula(new ArrayList<>(litteraux.subList(indexTmp,getCloseParenthesis(formula,indexTmp))));
                }
                operator = cur;
                formula2 = litterauxToFormula(new ArrayList<>(litteraux.subList(index,getCloseParenthesis(formula,index))));
                after = litterauxToFormula(new ArrayList<>(litteraux.subList(getCloseParenthesis(formula,index)+2,litteraux.size())));
            }else {
                while (cptParenthesis >= 1 && indexTmp < index) {
                    cur = litteraux.get(indexTmp);
                    if (cur.equals("(")) {
                        cptParenthesis++;
                        goodIndex = indexTmp;
                    }
                    if (cur.equals(")")) {
                        cptParenthesis--;
                    }
                    indexTmp++;
                }
                before = litterauxToFormula(new ArrayList<>(litteraux.subList(0, goodIndex - 2)));
                formula1 = litteraux.get(goodIndex - 2);
                if (litteraux.get(goodIndex - 2).equals(")")) {
                    formula1 = litterauxToFormula(new ArrayList<>(litteraux.subList(getOpenParenthesis(formula, goodIndex - 2) + 1, goodIndex - 1)));
                }
                operator = litteraux.get(goodIndex - 1);
                formula2 = litterauxToFormula(new ArrayList<>(litteraux.subList(goodIndex, getCloseParenthesis(formula, goodIndex))));
                after = litterauxToFormula(new ArrayList<>(litteraux.subList(getCloseParenthesis(formula, goodIndex), litteraux.size())));
            }
        }
        String newFormula = distribution(formula1, formula2, operator);
        newFormula = simplification(before + " " + newFormula + " " + after);
        newFormula = simplification(newFormula);
        // Do the recursive here
        if (isDNF(newFormula)) {
            return newFormula;
        }
        return "";
    }

    /**
     * Do the distribution of the formula1 to the formula2 by the operator
     * @param formula1 left variable
     * @param formula2 right variable
     * @param operator operator of the distribution
     * @return the distribution
     */
    public String distribution(String formula1, String formula2,String operator) {
        String res = "";
        String formula2_2,formula2_1,operator2;
        int indexOperator = 2, index;
        ArrayList<String> litteraux = getWords(formula2);
        if (litteraux.get(1).equals("(")) {
            indexOperator = getCloseParenthesis(formula2, 1);
        }
        // The first character is obligatory an open parenthesis, so we don't take it
        formula2_1 = litterauxToFormula(new ArrayList<>(litteraux.subList(1, indexOperator)));
        operator2 = litteraux.get(indexOperator);
        res = res + " ( " + formula1 + " " + operator + formula2_1 + " ) " + operator2;
        index = indexOperator+2;
        if(litteraux.get(indexOperator+1).equals("(")){
            index = getCloseParenthesis(formula2,indexOperator+1);
        }
        if(litteraux.get(index).equals("and") || litteraux.get(index).equals("or")){
            formula2_2 = distributionContinue(formula1,litterauxToFormula(new ArrayList<>(litteraux.subList(indexOperator + 1, litteraux.size()-1))),operator,operator2);
            res = res + " " + formula2_2;
        }else{
            formula2_2 = litterauxToFormula(new ArrayList<>(litteraux.subList(indexOperator+1,litteraux.size())));
            res = res + " ( "+ formula1+ " "  + operator + formula2_2;
        }
        return " ( "+simplification(res)+" ) ";
    }

    private String distributionContinue(String formula1, String formula2, String operator1,String operator2) {
        String res = "";
        String formula2_2,formula2_1;
        int indexOperator = 1, index;
        ArrayList<String> litteraux = getWords(formula2);
        if (litteraux.get(0).equals("(")) {
            indexOperator = getCloseParenthesis(formula2, 1);
        }
        formula2_1 = litterauxToFormula(new ArrayList<>(litteraux.subList(0, indexOperator)));
        res = res + " ( " + formula1+ " "  + operator1 + formula2_1 + " ) ";
        if(indexOperator >= litteraux.size()-1){
            return res;
        }
        index = indexOperator;
        res = res + operator2;
        if(litteraux.get(indexOperator+1).equals("(")){
            index = getCloseParenthesis(formula2,indexOperator+1);
        }
        if(litteraux.get(index).equals("and") || litteraux.get(index).equals("or")){
            formula2_2 = distributionContinue(formula1,litterauxToFormula(new ArrayList<>(litteraux.subList(indexOperator + 1, litteraux.size()))),operator1,operator2);
            res = res + " " + formula2_2;
        }else{
            formula2_2 = litterauxToFormula(new ArrayList<>(litteraux.subList(indexOperator+1,litteraux.size())));
            res = res + " ( "+ formula1+ " "  + operator1 + formula2_2 + " ) ";
        }
        return simplification(res);
    }

    /**
     * @param formula Formula
     * @param index Currrent index
     * @param distrib True if a negation have to be distribute, false else
     * @return the formula into the FNN form
     */
    public String FNN(String formula, int index, boolean distrib){
        // List of words that compose the formula
        ArrayList<String> litteraux = getWords(formula);
        if(litteraux.isEmpty()){
            return "";
        }
        // Current word
        String cur = litteraux.get(index);
        // Result
        String res = "";
        // True if we don't look the second condition, false else
        boolean passe = false;
        // if we reach the last word into the list, we return the current word
        if ( index >= litteraux.size()-1 ) {
            return res + " " + cur;
        }
        // If we have a not before an open parenthesis...
        if (cur.equals("not") && litteraux.get(index + 1).equals("(")) {
            // and there is not a distribution in progress we make it, otherwise we don't need to do a distribution
            if (!distrib) {
                // We get the index of close parenthesis linked to the open parenthesis meet
                int indexTmp = getCloseParenthesis(formula, index + 1);
                return res + " " + FNN(litterauxToFormula(new ArrayList<>(litteraux.subList(index + 1, indexTmp))), 0, true)
                        + " " + FNN(formula, indexTmp, distrib);
            }
            passe = true;
        }
        // If we have a distribution in progress and the next word is an open parenthesis, we need to do a recursion
        if (!passe && distrib && litteraux.get(index + 1).equals("(")) {
            // We get the index of close parenthesis linked to the open parenthesis meet
            int indexTmp = getCloseParenthesis(formula, index + 1);
            return res + FNN(cur + " .", 0, true)
                    + " " + FNN(litterauxToFormula(new ArrayList<>(litteraux.subList(index + 1, indexTmp))), 0, true)
                    + " " + FNN(formula, indexTmp, distrib);
        }
        // If we have a distribution to do...
        if (distrib) {
            // We look the operator and change it
            if (cur.equals("and")) {
                return res + "or" + FNN(formula, index + 1, true);
            } else if (cur.equals("or")) {
                return res + "and" + FNN(formula, index + 1, true);
            } else if (cur.equals("not")) {
                if (litteraux.get(index + 1).equals("(")) {
                    int indexTmp = getCloseParenthesis(formula, index + 1);
                    return res + " " + FNN(litterauxToFormula(new ArrayList<>(litteraux.subList(index + 1, indexTmp))), 0, false)
                            + " " + FNN(formula, indexTmp, true);
                }
                res = res + FNN(litteraux.get(index + 1) + " .", 0, false);
                if (index + 2 <= litteraux.size() - 1) {
                    res = res + FNN(formula, index + 2, true);
                }
                return res;
            } else if (!cur.equals("(")) {
                // If there is not an operator, it's a variable, so we add a negation to it
                res = res + " not";
            }
            return res + " " + cur + " " + FNN(formula, index + 1, true);
        }
        return res + " " + cur + " " + FNN(formula, index + 1, false);
    }

    /**
     * @param formula Formula
     * @param index Current index of reading of the formula
     * @return Index of the close parenthesis of the litteral + 1
     */
    public int getCloseParenthesis(String formula,int index){
        ArrayList<String> litteraux = getWords(formula);
        int cptParenthesis = 1;
        int indexTmp = index+1;
        String cur2;
        while(cptParenthesis >= 1 && indexTmp < litteraux.size()){
            cur2 = litteraux.get(indexTmp);
            if(cur2.equals("(")){
                cptParenthesis++;
            }
            if(cur2.equals(")")){
                cptParenthesis--;
            }
            indexTmp++;
        }
        if(indexTmp > litteraux.size()){
            return index;
        }
        return indexTmp;
    }

    /**
     * @param formula Formula
     * @param index Current index of reading of the formula
     * @return Index of the open parenthesis of the litteral - 1
     */
    public int getOpenParenthesis(String formula,int index){
        ArrayList<String> litteraux = getWords(formula);
        int cptParenthesis = 1;
        int indexTmp = index-1;
        String cur2;
        while(cptParenthesis >= 1 && indexTmp > 0){
            cur2 = litteraux.get(indexTmp);
            if(cur2.equals("(")){
                cptParenthesis--;
            }
            if(cur2.equals(")")){
                cptParenthesis++;
            }
            indexTmp--;
        }
        if(indexTmp < 0){
            return index;
        }
        return indexTmp;
    }

    /**
     * @param litteraux list of words
     * @return A new formula based of the list of words
     */
    public String litterauxToFormula(ArrayList<String> litteraux){
        StringBuilder res = new StringBuilder();
        for(String s:litteraux){
            res.append(" "+s+" ");
        }
        return res.toString();
    }

}
