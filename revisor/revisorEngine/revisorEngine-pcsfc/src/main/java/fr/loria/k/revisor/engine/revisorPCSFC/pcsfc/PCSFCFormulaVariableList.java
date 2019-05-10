package fr.loria.k.revisor.engine.revisorPCSFC.pcsfc;

import java.util.HashMap;

public class PCSFCFormulaVariableList {

	private static PCSFCFormulaVariableList instance = new PCSFCFormulaVariableList();
	
	private HashMap<String, PCSFCFormula> formulas;
	
	private PCSFCFormulaVariableList() {
		this.formulas = new HashMap<>();
	}
	
	public static PCSFCFormulaVariableList getInstance() {
		return instance;
	}
	
	public void addNewFormulaVariable(String name) {
		this.formulas.put(name, new PCSFCTautology());
	}
	
	public void updateFormulaVariable(String name, PCSFCFormula formula) {
		this.formulas.remove(name);
		this.formulas.put(name, formula);
	}
	
	public PCSFCFormula getFormulaByIdentifier(String name) {
		return this.formulas.get(name);
	}

	public void clearVariables() {
		this.formulas.clear();
	}
	
}
