package fr.loria.k.revisor.engine.revisorPCSFC.console.tos;

import java.util.HashMap;

import fr.loria.k.revisor.engine.revisorPCSFC.console.exceptions.DoubleDeclareException;

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
				throw new DoubleDeclareException("Cannot declare a variable more than once");
			}
		}
		this.table.put(e, s);
	}
	
	public void removeEntry(Entry e) {
		Entry entryToRemove = null;
		for (Entry en : this.table.keySet()) {
			if (en.getEntryName().equals(e.getEntryName())) {
				entryToRemove = en;
			}
		}
		this.table.remove(entryToRemove);
	}
	
}
