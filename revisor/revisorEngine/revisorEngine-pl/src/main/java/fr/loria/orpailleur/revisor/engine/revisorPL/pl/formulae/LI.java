package fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class LI {
	
	// Constants :
	
	public static final int TRUE = 1;
	public static final int FALSE = -1;
	
	// Fields :
	
	private HashMap<String, Integer> identifiers;
	private HashMap<Integer, String> names;
	private int current_index;
	
	// Constructors :
	
	public LI() {
		this.init();
	}
	
	// Methods :
	
	protected void init() {
		this.identifiers = new HashMap<>();
		this.names = new HashMap<>();
		
		this.add("true", TRUE);
		this.add("false", FALSE);
		
		this.current_index = 2;
	}
	
	protected void add(String name, Integer id) {
		this.identifiers.put(name, id);
		this.names.put(id, name);
	}
	
	public void add(String name) {
		while(name.charAt(0) == '!' && name.length() > 1) {
			name = name.substring(1);
		}
		if(!this.identifiers.containsKey(name)) {
			this.add(name, this.current_index++);
		}
	}
	
	public Integer getOrCreateID(final String name) {
		this.add(name);
		return getID(name);
	}
	
	public Integer getID(String name) {
		Integer id = this.identifiers.get(name);
		int polarity = 1;
		
		while(id == null && name.charAt(0) == '!' && name.length() > 1) {
			polarity = -polarity;
			name = name.substring(1);
			id = this.identifiers.get(name);
		}
		
		return id * polarity;
	}
	
	public ArrayList<String> sortedNames(final HashSet<Integer> literalSet) {
		ArrayList<Integer> literals = new ArrayList<>();
		
		if(literalSet != null) {
			literals.addAll(literalSet);
		}
		
		Collections.sort(literals, new Comparator<Integer>() {
			
			@Override
			public int compare(final Integer i1, final Integer i2) {
				return LI.this.getName(Math.abs(i1)).compareTo(LI.this.getName(Math.abs(i2)));
			}
			
		});
		
		ArrayList<String> latexNames = new ArrayList<>();
		
		for(Integer i : literals) {
			latexNames.add(this.getName(i));
		}
		
		return latexNames;
	}
	
	public Iterable<Integer> getIDs() {
		return new Iterable<Integer>() {
			
			private final int maxIndex = LI.this.current_index;
			
			@Override
			public Iterator<Integer> iterator() {
				return new Iterator<Integer>() {
					
					private int index = 2;
					
					@Override
					public boolean hasNext() {
						return this.index <= maxIndex;
					}
					
					@Override
					public Integer next() {
						return this.index++;
					}
					
					@Override
					public void remove() throws UnsupportedOperationException {
						throw new UnsupportedOperationException();
					}
					
				};
			}
			
		};
	}
	
	public void printVars(final Iterable<Integer> iterable) {
		if(iterable == null) {
			return;
		}
		
		for(Integer i : iterable) {
			System.out.print(this.getName(i) + " ");
		}
		
		System.out.println();
	}
	
	public void printAll() {
		System.out.println("#PL VARIABLES#");
		
		for(Integer i : this.names.keySet()) {
			System.out.println(i + " : " + this.names.get(i));
		}
	}
	
	public String getSimpleName(final Integer id) {
		String name = this.names.get(id);
		
		if(name == null) {
			name = this.names.get(-id);
			name = ((name == null) ? "<NO-NAME!>" : name);
		}
		
		return name;
	}
	
	public String getName(final Integer id) {
		String name = this.names.get(id);
		
		if(name == null) {
			name = this.names.get(-id);
			name = "!" + ((name == null) ? "<NO-NAME>" : name);
		}
		
		return name;
	}
	
	public String getDotName(final Integer id) {
		String name = this.names.get(id);
		
		if(name == null) {
			name = this.names.get(-id);
			name = "&not;" + ((name == null) ? "<NO-NAME>" : name);
			return name;
		}
		else {
			return name;
		}
	}
	
	public double getVarCount() {
		return this.names.size();
	}
	
	public PLLiteral[] litsetToArray(final HashSet<Integer> literals) {
		PLLiteral lits[] = new PLLiteral[literals.size()];
		int i = 0;
		
		for(Integer literal : literals) {
			lits[i++] = new PLLiteral(this, literal);
		}
		
		return lits;
	}
	
}
