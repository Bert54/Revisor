package fr.loria.orpailleur.revisor.engine.revisorPLAK.plak.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class DotIt {
	
	public static interface DotNode {
		
		public String dotNodeLabel();
		
	}
	
	public static interface DotEdge {
		
		public String dotEdgeLabel();
		
	}
	
	private static class Edge {
		
		private int n1, n2;
		private String label;
		
		private Edge(final String label, final int n1, final int n2) {
			this.label = label;
			this.n1 = n1;
			this.n2 = n2;
		}
		
	}
	
	// Fields :
	
	private HashSet<Integer> newLevels = new HashSet<>();
	private HashMap<DotNode, Integer> nodes = new HashMap<>();
	private ArrayList<DotNode> nodelist = new ArrayList<>();
	private ArrayList<Edge> edges = new ArrayList<>();
	private int exportId = 0;
	private String outputFolder = null;
	private int start = 0, end = 0;
	
	// Constructors :
	
	public DotIt(final String outputFolder) {
		this.outputFolder = outputFolder;
	}
	
	// Methods :
	
	public void dotInit() {
		this.nodes.clear();
		this.nodelist.clear();
		this.edges.clear();
	}
	
	public String dotCode() {
		String code = "";
		code += "digraph G {\n" + "graph [fontname = \"Cambria Math\"];\n" + "node [fontname = \"Cambria Math\", shape = \"none\"];\n" + "edge [fontname = \"Cambria Math\"];\n" + "charset=\"Latin-1\"\n\n";
		
		for(int i = 0; i < this.nodelist.size(); i++) {
			DotNode node = this.nodelist.get(i);
			code += "n" + i + "[label = <" + node.dotNodeLabel() + ">]\n";
		}
		
		for(int i = 0; i < this.edges.size(); i++) {
			Edge edge = this.edges.get(i);
			code += "n" + edge.n1 + " -> n" + edge.n2 + " [label= <" + edge.label + ">]\n";
		}
		
		code += "}";
		return code;
	}
	
	public void setStart(final int start) {
		this.start = start;
	}
	
	public void setEnd(final int end) {
		this.end = end;
	}
	
	public void dotWrite() {
		this.exportId++;
		
		if(this.exportId < this.start) {
			return;
		}
		
		if(this.end != 0 && this.exportId > this.end) {
			return;
		}
		
		this.newLevels.add(this.nodes.size());
		
		try {
			DecimalFormat df = new DecimalFormat("000000000");
			String path1 = this.outputFolder + df.format(this.exportId) + ".dot";
			String path2 = this.outputFolder + df.format(this.exportId) + ".png";
			File tmpFile = new File(path1);
			new File(this.outputFolder).mkdirs();
			tmpFile.createNewFile();
			FileOutputStream fileOutput = (new FileOutputStream(tmpFile));
			fileOutput.write(this.dotCode().getBytes());
			fileOutput.close();
			String cmd = "dot -Tpng \"" + path1 + "\" -o \"" + path2 + "\"";
			System.out.println(cmd);
			Runtime.getRuntime().exec(cmd);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public int addNode(final DotNode n) {
		if(this.nodes.containsKey(n)) {
			return this.nodes.get(n);
		}
		else {
			int id = this.nodelist.size();
			this.nodes.put(n, id);
			this.nodelist.add(n);
			return id;
		}
	}
	
	public void addEdge(final DotNode n1, final String str, final DotNode n2) {
		int i1 = this.addNode(n1);
		int i2 = this.addNode(n2);
		this.edges.add(new Edge(str, i1, i2));
	}
	
	public void addEdge(final DotNode n1, final DotEdge e, final DotNode n2) {
		int i1 = this.addNode(n1);
		int i2 = this.addNode(n2);
		this.edges.add(new Edge(e.dotEdgeLabel(), i1, i2));
	}
	
}
