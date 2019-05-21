package fr.loria.k.revisor.engine.revisorPCSFC;

public class RevisorPCSFC {

	private static final RevisorEnginePCSFC engine = new RevisorEnginePCSFC();
	
	public static final String formatNameToLatex(final String name) {
		return engine.formatNameToLatex(name);
	}
	
}
