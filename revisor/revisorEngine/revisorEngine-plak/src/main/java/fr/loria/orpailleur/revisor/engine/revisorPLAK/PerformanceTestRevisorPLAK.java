package fr.loria.orpailleur.revisor.engine.revisorPLAK;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import fr.loria.orpailleur.revisor.engine.revisorPL.pl.PL;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.AND;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.LI;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.OR;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLConstant;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLFormula;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLLiteral;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.util.LitSet;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.plak.adaptops.AKAdaptOp;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.plak.adaptops.ak.RuleSet;

/**
 * @author Gabin PERSONENI
 */
public class PerformanceTestRevisorPLAK {
	
	static int VAR_COUNT = 50; // number of variables
	static int RULE_COUNT = 40; // number of adaptation rules
	static double RULE_COSTS = 1f; // ]0; 1]
	static int TIMEOUT_THRESHOLD = 10_000; // milliseconds
	static int SAMPLE_SIZE = 250;
	static int DSTAR_MAX = 4;
	
	static final LI li = new LI();
	static final PL pl = new PL(li);
	
	/**
	 * Thread performing adaptation
	 */
	static class ADPT_Thread extends Thread {
		
		PLFormula psi, mu;
		PLFormula result = null;
		RuleSet rs;
		long time = -1;
		
		ADPT_Thread(final PLFormula psi, final PLFormula mu, final RuleSet rs) {
			this.psi = psi;
			this.mu = mu;
			this.rs = rs;
		}
		
		@Override
		public void run() {
			long t1 = System.nanoTime();
			this.result = RevisorPLAK.adaptAK(this.psi, this.mu, this.rs);
			long t2 = System.nanoTime();
			this.time = t2 - t1;
		}
		
		public void clear() {
			this.rs = null;
			this.result = null;
			this.psi = null;
			this.mu = null;
		}
	}
	
	public static class PerfData {
		
		public ArrayList<Double> distances = new ArrayList<>();
		public ArrayList<Float> times = new ArrayList<>();
		
		public float averageTime = 0f;
		public float stdDev = 0f;
		public float succesPercentage = 0f;
		
		public PerfData() {
			
		}
		
		@Override
		public String toString() {
			return "AVG=" + this.averageTime + " STDDEV=" + this.stdDev + " SUCCESS=" + this.succesPercentage;
		}
		
		public void computeAVG_and_STDDEV() {
			
			this.averageTime = 0f;
			int size = 0;
			
			for(Float v : this.times) {
				if(v != null) {
					this.averageTime += v;
					size++;
				}
			}
			
			this.succesPercentage = size / this.times.size();
			this.averageTime /= size;
			
			float sumOfSquaredDifferences = 0f;
			
			for(Float v : this.times) {
				if(v != null) {
					sumOfSquaredDifferences += (v - this.averageTime) * (v - this.averageTime);
				}
			}
			
			this.stdDev = (float) Math.sqrt(sumOfSquaredDifferences / size);
		}
	}
	
	public static void main(final String[] args) {
		
		try {
			
			File f = new File("./_perfdata_DSTAR_.csv");
			
			f.createNewFile();
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			
			int timeouts = 0;
			int timeoutsC = 0;
			int harderThan_DSTAR_MAX_problems = 0;
			int totalProblems = 0;
			
			HashMap<Double, PerfData[]> scores = new HashMap<>();
			boolean classDone[] = new boolean[DSTAR_MAX + 1];
			classDone[0] = true;
			
			for(int dstar = 1; dstar <= DSTAR_MAX; dstar++) {
				scores.put((double) dstar, new PerfData[] {new PerfData(), new PerfData()});
				classDone[dstar] = false;
			}
			
			for(int i = 0; i < 10000; i++) {
				AKAdaptOp.maxSolutionCost = (double) DSTAR_MAX;
				
				PerfData[] perfData = test(1);
				
				totalProblems++;
				
				Double dstar = perfData[0].distances.get(0);
				Float time = perfData[0].times.get(0);
				
				Double dstarC = perfData[1].distances.get(0);
				Float timeC = perfData[1].times.get(0);
				
				if((dstar != null && dstar == Double.POSITIVE_INFINITY) || (dstarC != null && dstarC == Double.POSITIVE_INFINITY)) {
					harderThan_DSTAR_MAX_problems++;
				}
				else {
					if(dstar == null) {
						timeouts++;
					}
					
					if(dstarC == null) {
						timeoutsC++;
					}
				}
				
				if(dstarC == null) {
					dstar = null;
				}
				
				if(dstar != null) {
					dstar = (double) ((int) (0 + dstar));
				}
				
				if(dstarC != null) {
					dstarC = (double) ((int) (0 + dstarC));
				}
				
				PerfData[] timesCollection = scores.get(dstar);
				
				if(timesCollection != null && !classDone[dstar.intValue()]) {
					timesCollection[0].times.add(time);
					timesCollection[1].times.add(timeC);
					
					if(timesCollection[0].times.size() >= SAMPLE_SIZE) {
						classDone[dstar.intValue()] = true;
					}
					
					float avgCompletion = 0;
					String msg = "";
					
					for(int d = 1; d <= DSTAR_MAX; d++) {
						float percentage = percentage(((float) scores.get((double) d)[0].times.size()) / SAMPLE_SIZE);
						avgCompletion += percentage;
						msg += (" | d*=" + d + ", " + percentage + "% done");
					}
					
					avgCompletion /= DSTAR_MAX;
					
					System.out.println("[GLOBAL : " + avgCompletion + "% done, " + timeouts + "|" + timeoutsC + " timeouts] | d*>" + DSTAR_MAX + ", " + harderThan_DSTAR_MAX_problems + " problems avoided." + msg);
					
					boolean finished = true;
					
					for(boolean b : classDone) {
						if(!b) {
							finished = false;
							break;
						}
					}
					
					if(finished) {
						break;
					}
				}
			}
			
			bw.write(" \tPLAK V1\t\tPLAK V2\t\tSOLVED\n");
			bw.write("d*\tAVERAGE\tSTD DEV\tAVERAGE\tSTD DEV\t\n");
			
			for(int dstar = 1; dstar <= DSTAR_MAX; dstar++) {
				PerfData[] perfData = scores.get((double) dstar);
				perfData[0].computeAVG_and_STDDEV();
				perfData[1].computeAVG_and_STDDEV();
				
				bw.write(dstar + "\t" + perfData[0].averageTime + "\t" + perfData[0].stdDev + "\t" + perfData[1].averageTime + "\t" + perfData[1].stdDev + "\t" + perfData[0].times.size() + "\n");
			}
			
			bw.write("\n");
			bw.write("TIMEOUTS V1\t" + timeouts + "\n");
			bw.write("TIMEOUTS V2\t" + timeoutsC + "\n");
			bw.write("d*>" + DSTAR_MAX + " NOT SOLVED\t" + harderThan_DSTAR_MAX_problems + "\n");
			bw.write("TOTAL PROBLEMS GENERATED\t" + totalProblems + "\n");
			
			bw.flush();
			bw.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static float percentage(final float f) {
		return((int) (f * 10000) / 100f);
	}
	
	@SuppressWarnings("deprecation")
	public static PerfData[] test(final int k) {
		PerfData perfData = new PerfData();
		PerfData perfDataC = new PerfData();
		
		// main loop
		for(int i = 0; i < k; i++) {
			// adaptation problem generation
			PLFormula psi = PLConstant.TRUE, mu = PLConstant.TRUE;
			psi = new OR(li, gen(1, VAR_COUNT, VAR_COUNT, 0.5f));
			mu = gen(2, VAR_COUNT, (int) (VAR_COUNT * 0.75), 0.5f);
			// generate a new problem if (psi & mu) != false
			
			while(pl.AND(psi, mu).toDNF(0x0FFF_FFFF) != PLConstant.FALSE) {
				psi = new OR(li, gen(1, VAR_COUNT, VAR_COUNT, 0.5f));
				mu = gen(2, VAR_COUNT, (int) (VAR_COUNT * 0.75), 0.5f);
			}
			
			// rule generation
			RuleSet rules = new RuleSet(li);
			
			for(int j = 0; j < RULE_COUNT; j++) {
				genRule(psi, rules);
			}
			
			// garbage collection (so every execution of the algorithm is done in a clean VM)
			System.gc();
			
			try {
				ADPT_Thread t = new ADPT_Thread(psi, mu, rules);
				t.start();
				
				boolean finished = false;
				
				for(int timer = 0; timer < TIMEOUT_THRESHOLD; timer += 10) {
					Thread.sleep(10);
					
					// time != -1 if algorithm has finished
					if(t.time != -1) {
						perfData.times.add(t.time / 1_000_000f);
						perfData.distances.add(AKAdaptOp.getLastAKDistance());
						finished = true;
						break;
					}
				}
				
				t.clear();
				t.stop();
				
				if(!finished) {
					perfData.times.add(null);
					perfData.distances.add(null);
				}
			}
			// should not happen
			catch(InterruptedException e) {
				e.printStackTrace();
				System.exit(0);
			}
			
			// optimize the rule set to take advantage of commutativity
			rules.computeCommutables();
			
			// garbage collection (so every execution of the algorithm is done in a clean VM)
			System.gc();
			
			try {
				ADPT_Thread t = new ADPT_Thread(psi, mu, rules);
				t.start();
				
				boolean finished = false;
				
				for(int timer = 0; timer < TIMEOUT_THRESHOLD; timer += 10) {
					Thread.sleep(10);
					
					// time != -1 if algorithm has finished
					if(t.time != -1) {
						perfDataC.times.add(t.time / 1_000_000f);
						perfDataC.distances.add(AKAdaptOp.getLastAKDistance());
						finished = true;
						break;
					}
				}
				
				t.clear();
				t.stop();
				
				if(!finished) {
					perfDataC.times.add(null);
					perfDataC.distances.add(null);
				}
				
			}
			// should not happen
			catch(InterruptedException e) {
				e.printStackTrace();
				System.exit(0);
			}
			
		}
		
		perfData.computeAVG_and_STDDEV();
		perfDataC.computeAVG_and_STDDEV();
		
		return new PerfData[] {perfData, perfDataC};
	}
	
	/**
	 * Generate a rule that could be applied on psi.
	 * @param psi - A PLFormula.
	 * @param rules - The set of rule to which the generated rule will be added.
	 */
	public static void genRule(final PLFormula psi, final RuleSet rules) {
		PLFormula[] children = ((OR) psi).getChildren();
		AND and = (AND) children[(int) (Math.random() * children.length)];
		ArrayList<Integer> left = new ArrayList<>();
		left.addAll(and.asLitSet());
		double changes = (left.size() / 2) - 4 + Math.random() * 3;
		
		for(int c = 0; c < changes || left.size() > 4; c++) {
			if(left.size() > 1)
				left.remove((int) (Math.random() * left.size()));
		}
		
		LitSet right = new LitSet(li);
		right.addAll(left);
		changes = 1 + Math.random() * (left.size() * 0.9);
		Integer definitive_removal = 0;
		
		for(int c = 0; c < changes; c++) {
			if(right.size() > 0) {
				definitive_removal = left.get((int) (Math.random() * left.size()));
				right.remove(definitive_removal);
			}
		}
		
		changes = 1 + Math.random() * (right.size() / 2);
		
		for(int c = 0; c < changes; c++) {
			Integer literal = (int) (Math.random() * li.getVarCount());
			
			while(literal == definitive_removal || literal == 0 || literal == 1 || literal == -1) {
				literal = (int) (Math.random() * li.getVarCount());
			}
			
			if(!right.contains(literal)) {
				right.add(literal);
			}
		}
		
		LitSet ls_left = new LitSet(li);
		LitSet ls_right = new LitSet(li);
		LitSet ls_context = new LitSet(li);
		ls_left.addAll(left);
		ls_right.addAll(right);
		
		for(Integer l : ls_right) {
			if(ls_left.contains(l)) {
				ls_context.add(l);
			}
		}
		
		for(Integer l : ls_context) {
			ls_left.remove(l);
			ls_right.remove(l);
		}
		
		rules.addRule(new AND(li, ls_context), new AND(li, ls_left), new AND(li, ls_right), RULE_COSTS);
	}
	
	public static PLFormula gen(final int depth, final int varCount, int size, final double negPercent) {
		// init
		if(size < 2) {
			size = 2;
		}
		
		int var_count = varCount;
		
		if(var_count < 2) {
			var_count = 2;
		}
		
		boolean ANDs = true;
		
		// instantiating literals
		ArrayList<PLFormula> formules = new ArrayList<>();
		
		for(int i = 0; i < var_count; i++) {
			String var = (char) ('a' + i % 26) + "_" + i / 26;
			formules.add((int) (Math.random() * formules.size()), new PLLiteral(li, (Math.random() < negPercent) ? "!" + var : var));
		}
		
		for(int i = 0; i < size - var_count; i++) {
			formules.add((int) (Math.random() * formules.size()), formules.get((int) (Math.random() * formules.size())));
		}
		
		// building the formula tree
		for(int p = depth; p > 1; p--) {
			ArrayList<PLFormula> formules_tmp = new ArrayList<>();
			int delta = formules.size() - p - 1;
			int nb_groupes = (int) (p + delta * Math.random());
			int[] tailles_groupes = new int[nb_groupes];
			
			for(int i = 0; i < tailles_groupes.length; i++) {
				tailles_groupes[i] = 1;
				if(i == 0) {
					tailles_groupes[i]++;
				}
			}
			
			for(int i = 0; i < formules.size() - tailles_groupes.length - 1; i++) {
				tailles_groupes[(int) (Math.random() * tailles_groupes.length)]++;
			}
			int index_global = 0;
			for(int i = 0; i < tailles_groupes.length; i++) {
				PLFormula[] fp_s = new PLFormula[tailles_groupes[i]];
				
				for(int j = 0; j < fp_s.length; j++) {
					fp_s[j] = formules.get(index_global++);
				}
				
				formules_tmp.add(ANDs ? new AND(li, fp_s) : new OR(li, fp_s));
			}
			
			formules = formules_tmp;
			ANDs = !ANDs;
		}
		
		// depth 1
		{
			PLFormula[] fp_s = new PLFormula[formules.size()];
			
			for(int i = 0; i < formules.size(); i++) {
				fp_s[i] = formules.get(i);
			}
			
			PLFormula f = ANDs ? new AND(li, fp_s) : new OR(li, fp_s);
			return f;
		}
	}
	
}
