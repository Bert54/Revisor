package fr.loria.orpailleur.revisor.engine.revisorQA.qa;

/**
 @author Valmi Dufout-Lussier
 @date 13.02.2013
 */
public class QualitativeVariableSubstitution {
	
	// Fields :
	
	private String[] from;
	private String[] to;
	private int length;
	private int fromIter = 0;
	private int toIter = 0;
	
	// Constructors :
	
	public QualitativeVariableSubstitution(String[] from, String[] to) throws Exception {
		if(from.length != to.length) {
			throw new Exception("The lists of substituted and substitution variables must have the same length");
		}
		
		this.from = from;
		this.to = to;
		this.length = from.length;
	}
	
	public QualitativeVariableSubstitution(String from, String to) throws Exception {
		this(new String[] {from}, new String[] {to});
	}
	
	// Methods :
	
	public boolean hasNextFrom() {
		if(this.fromIter < this.length) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean hasNextTo() {
		if(this.toIter < this.length) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * getNextFromIndex() must be called before because the iteration occurs in the latter.
	 */
	public String getNextFrom() {
		return this.from[this.fromIter++];
	}
	
	public int getNextFromIndex() {
		return this.fromIter;
	}
	
	/**
	 * getNextToIndex() must be called before because the iteration occurs in the latter.
	 */
	public String getNextTo() {
		return this.to[this.toIter++];
	}
	
	public int getNextToIndex() {
		return this.toIter;
	}
	
}
