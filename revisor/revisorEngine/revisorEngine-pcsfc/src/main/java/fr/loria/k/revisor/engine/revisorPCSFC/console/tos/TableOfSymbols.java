package fr.loria.k.revisor.engine.revisorPCSFC.console.tos;

import java.util.ArrayList;
import java.util.HashMap;

import fr.loria.k.revisor.engine.revisorPCSFC.console.exceptions.DoubleDeclareException;
import fr.loria.k.revisor.engine.revisorPCSFC.console.exceptions.VariableNotDeclaredException;

public class TableOfSymbols {

	private static TableOfSymbols instance = new TableOfSymbols();
	
	private HashMap<Entry, Symbol> table;
	
	public static TableOfSymbols getInstance() {
		return instance;
	}
	
	private TableOfSymbols() {
		this.table = new HashMap<>();
	}

	public void addEntry(Entry e, Symbol s) throws DoubleDeclareException {
		for (Entry en : this.table.keySet()) {
			if (en.getEntryName().equals(e.getEntryName())) {
				throw new DoubleDeclareException("Cannot declare a variable more than once", null, true, false);
			}
		}
		this.table.put(e, s);
	}
	
	public Symbol identify(Entry e) throws VariableNotDeclaredException {
		for (Entry en : this.table.keySet()) {
			if (en.getEntryName().equals(e.getEntryName())) {
				return this.table.get(en);
			}
		}
		throw new VariableNotDeclaredException("Variable "+ e.getEntryName() +" has not been declared.", null, true, false);
	}
	
	public boolean hasEntryByName(Entry e) {
		for (Entry en : this.table.keySet()) {
			if (en.getEntryName().equals(e.getEntryName())) {
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<Symbol> getAllSymbols() {
		ArrayList<Symbol> symbList = new ArrayList<>();
		for(Entry e: this.table.keySet()) {
			symbList.add(this.table.get(e));
		}
		return symbList;
	}
	
	public void resetTable() {
		this.table.clear();
	}
	
}
