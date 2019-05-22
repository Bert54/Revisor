package fr.loria.orpailleur.revisor.engine.revisorQA.qa;

import java.util.Arrays;
import java.util.List;

/**
 @author Valmi Dufout-Lussier
 @date 07.02.2013
 */
public class QualitativeAlgebra {
	
	// TODO - CORE - Note from William to Valmi - This class should be converted to an enum, like this:
	//
	//	// Enum :
	//	
	//	ALLEN("allen"),
	//	RCC8("rcc8"),
	//	INDU("indu");
	//	
	//	// Fields :
	//	
	//	private String label;
	//	
	//	// Constructors :
	//	
	//	private QualitativeAlgebra(String label) {
	//		this.label = label;
	//	}
	//	
	//	// Getters :
	//	
	//	public String getLabel() {
	//		return this.label;
	//	}
	
	// Constants :
	
	public static final List<String> RECOGNISED = Arrays.asList("allen", "rcc8", "indu");
	
	// Fields :
	
	private String label;
	
	// Constructors :
	
	public QualitativeAlgebra(QAPBackendRequest qap, String label) throws Exception {
		if(!RECOGNISED.contains(label)) {
			throw new Exception("Trying to load algebra label " + label + " which is not recognised");
		}
		else {
			this.label = label;
		}
	}
	
	// Getters :
	
	public String getLabel() {
		return this.label;
	}
	
}
