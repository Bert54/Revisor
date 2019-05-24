package fr.loria.k.revisor.engine.revisorPCSFC.pcsfc;

import java.util.ArrayList;
import java.util.Collection;

import fr.loria.k.revisor.engine.revisorPCSFC.RevisorPCSFC;

public class PCSFCEnumeration extends PCSFCFormula {

	private String name;
	private String modality;
	private ArrayList<String> unusedModalities;
	
	public PCSFCEnumeration(String n, String mod, Collection<String> um) {
		this.name = n;
		this.modality = mod;
		this.unusedModalities = new ArrayList<>(um);
		this.unusedModalities.remove(this.modality);
	}

	@Override
	public String toString(boolean latex) {
		if (latex) {
			return " \\:" + RevisorPCSFC.formatNameToLatex(this.name) + "\\: = \\:" + this.modality;
		}
		return " " + this.name + " = " + this.modality;
	}

	@Override
	public boolean canRevise() {
		return false;
	}

	@Override
	public PCSFCFormula toPCLC() {
		PCSFCFormula pclc;
		String modalityWithoutQuotes = this.modality.replace("\"", "");
		pclc = new PCSFCBoolean("boolean_encoding_" + modalityWithoutQuotes).toPCLC();
		for (String s: this.unusedModalities) {
			modalityWithoutQuotes = s.replace("\"", "");
			pclc = new PCSFCAnd(pclc, new PCSFCNot(new PCSFCBoolean("boolean_encoding_" + modalityWithoutQuotes).toPCLC()));
		}
		return pclc;
	}
	
	@Override
	public PCSFCFormula toPCSFC() {
		return new PCSFCEnumeration(this.name, this.modality, this.unusedModalities);
	}

}
