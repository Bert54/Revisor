package fr.loria.orpailleur.revisor.engine.core.utils.string;

import java.util.Collections;
import java.util.Map;

/**
 * @author William Philbert
 */
public class LatexUtils {
	
	// Constants :
	
	public static final String ALPHA = "alpha";
	public static final String LATEX_ALPHA = "{\\alpha}";
	
	public static final String BETA = "beta";
	public static final String LATEX_BETA = "{\\beta}";
	
	public static final String GAMMA = "gamma";
	public static final String LATEX_GAMMA = "{\\gamma}";
	
	public static final String DELTA = "delta";
	public static final String LATEX_DELTA = "{\\delta}";
	
	public static final String EPSILON = "epsilon";
	public static final String LATEX_EPSILON = "{\\varepsilon}";
	
	public static final String ZETA = "zeta";
	public static final String LATEX_ZETA = "{\\zeta}";
	
	public static final String ETA = "eta";
	public static final String LATEX_ETA = "{\\eta}";
	
	public static final String THETA = "theta";
	public static final String LATEX_THETA = "{\\theta}";
	
	public static final String IOTA = "iota";
	public static final String LATEX_IOTA = "{\\iota}";
	
	public static final String KAPPA = "kappa";
	public static final String LATEX_KAPPA = "{\\kappa}";
	
	public static final String LAMBDA = "lambda";
	public static final String LATEX_LAMBDA = "{\\lambda}";
	
	public static final String MU = "mu";
	public static final String LATEX_MU = "{\\mu}";
	
	public static final String NU = "nu";
	public static final String LATEX_NU = "{\\nu}";
	
	public static final String XI = "xi";
	public static final String LATEX_XI = "{\\xi}";
	
	public static final String PI = "pi";
	public static final String LATEX_PI = "{\\pi}";
	
	public static final String RHO = "rho";
	public static final String LATEX_RHO = "{\\varrho}";
	
	public static final String SIGMA = "sigma";
	public static final String LATEX_SIGMA = "{\\sigma}";
	
	public static final String TAU = "tau";
	public static final String LATEX_TAU = "{\\tau}";
	
	public static final String UPSILON = "upsilon";
	public static final String LATEX_UPSILON = "{\\upsilon}";
	
	public static final String PHI = "phi";
	public static final String LATEX_PHI = "{\\varphi}";
	
	public static final String CHI = "chi";
	public static final String LATEX_CHI = "{\\chi}";
	
	public static final String PSI = "psi";
	public static final String LATEX_PSI = "{\\psi}";
	
	public static final String OMEGA = "omega";
	public static final String LATEX_OMEGA = "{\\omega}";
	
	public static final String BIG_GAMMA = "Gamma";
	public static final String LATEX_BIG_GAMMA = "{\\Gamma}";
	
	public static final String BIG_DELTA = "Delta";
	public static final String LATEX_BIG_DELTA = "{\\Delta}";
	
	public static final String BIG_LAMBDA = "Lambda";
	public static final String LATEX_BIG_LAMBDA = "{\\Lambda}";
	
	public static final String BIG_PHI = "Phi";
	public static final String LATEX_BIG_PHI = "{\\Phi}";
	
	public static final String BIG_PI = "Pi";
	public static final String LATEX_BIG_PI = "{\\Pi}";
	
	public static final String BIG_PSI = "Psi";
	public static final String LATEX_BIG_PSI = "{\\Psi}";
	
	public static final String BIG_SIGMA = "Sigma";
	public static final String LATEX_BIG_SIGMA = "{\\Sigma}";
	
	public static final String BIG_THETA = "Theta";
	public static final String LATEX_BIG_THETA = "{\\Theta}";
	
	public static final String BIG_UPSILON = "Upsilon";
	public static final String LATEX_BIG_UPSILON = "{\\Upsilon}";
	
	public static final String BIG_XI = "Xi";
	public static final String LATEX_BIG_XI = "{\\Xi}";
	
	public static final String BIG_OMEGA = "Omega";
	public static final String LATEX_BIG_OMEGA = "{\\Omega}";
	
	protected static final SymbolMap symbolMap = new SymbolMap();
	
	static {
		symbolMap.put(ALPHA, LATEX_ALPHA);
		symbolMap.put(BETA, LATEX_BETA);
		symbolMap.put(GAMMA, LATEX_GAMMA);
		symbolMap.put(DELTA, LATEX_DELTA);
		symbolMap.put(EPSILON, LATEX_EPSILON);
		symbolMap.put(ZETA, LATEX_ZETA);
		symbolMap.put(ETA, LATEX_ETA);
		symbolMap.put(THETA, LATEX_THETA);
		symbolMap.put(IOTA, LATEX_IOTA);
		symbolMap.put(KAPPA, LATEX_KAPPA);
		symbolMap.put(LAMBDA, LATEX_LAMBDA);
		symbolMap.put(MU, LATEX_MU);
		symbolMap.put(NU, LATEX_NU);
		symbolMap.put(XI, LATEX_XI);
		symbolMap.put(PI, LATEX_PI);
		symbolMap.put(RHO, LATEX_RHO);
		symbolMap.put(SIGMA, LATEX_SIGMA);
		symbolMap.put(TAU, LATEX_TAU);
		symbolMap.put(UPSILON, LATEX_UPSILON);
		symbolMap.put(PHI, LATEX_PHI);
		symbolMap.put(CHI, LATEX_CHI);
		symbolMap.put(PSI, LATEX_PSI);
		symbolMap.put(OMEGA, LATEX_OMEGA);
		symbolMap.put(BIG_GAMMA, LATEX_BIG_GAMMA);
		symbolMap.put(BIG_DELTA, LATEX_BIG_DELTA);
		symbolMap.put(BIG_LAMBDA, LATEX_BIG_LAMBDA);
		symbolMap.put(BIG_PHI, LATEX_BIG_PHI);
		symbolMap.put(BIG_PI, LATEX_BIG_PI);
		symbolMap.put(BIG_PSI, LATEX_BIG_PSI);
		symbolMap.put(BIG_SIGMA, LATEX_BIG_SIGMA);
		symbolMap.put(BIG_THETA, LATEX_BIG_THETA);
		symbolMap.put(BIG_UPSILON, LATEX_BIG_UPSILON);
		symbolMap.put(BIG_XI, LATEX_BIG_XI);
		symbolMap.put(BIG_OMEGA, LATEX_BIG_OMEGA);
	}
	
	// Methods :
	
	public static String symbol(String textSymbol) {
		return symbol(textSymbol, true);
	}
	
	public static String symbol(String textSymbol, boolean latex) {
		return symbolMap.symbol(textSymbol, latex);
	}
	
	public static Map<String, String> symbolMap() {
		return Collections.unmodifiableMap(symbolMap);
	}
	
}
