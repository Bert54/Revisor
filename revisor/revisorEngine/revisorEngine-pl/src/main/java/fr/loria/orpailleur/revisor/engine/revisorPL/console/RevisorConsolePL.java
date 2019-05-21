package fr.loria.orpailleur.revisor.engine.revisorPL.console;

import java.io.StringReader;

import fr.loria.orpailleur.revisor.engine.core.console.exception.ConsoleInitializationException;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.Instruction;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.InvalidInstruction;
import fr.loria.orpailleur.revisor.engine.core.utils.config.storage.ConfigFileStorage;
import fr.loria.orpailleur.revisor.engine.revisorPL.RevisorEnginePL;
import fr.loria.orpailleur.revisor.engine.revisorPL.console.config.PLConfig;
import fr.loria.orpailleur.revisor.engine.revisorPL.console.parser.PLConsoleLexer;
import fr.loria.orpailleur.revisor.engine.revisorPL.console.parser.PLConsoleParser;

/**
 * @author William Philbert
 */
public class RevisorConsolePL extends AbstractRevisorConsolePL<RevisorConsolePL, RevisorEnginePL, PLConfig, Instruction<RevisorConsolePL>> {
	
	// Constructors :
	
	public RevisorConsolePL() throws ConsoleInitializationException {
		super();
	}
	
	public RevisorConsolePL(final RevisorEnginePL engine) throws ConsoleInitializationException {
		super(engine);
	}
	
	public RevisorConsolePL(final PLConfig config) throws ConsoleInitializationException {
		super(config);
	}
	
	public RevisorConsolePL(final RevisorEnginePL engine, final PLConfig config) throws ConsoleInitializationException {
		super(engine, config);
	}
	
	// Methods :
	
	@Override
	public String getShortName() {
		return "PL";
	}
	
	@Override
	protected RevisorEnginePL newEngine() {
		return new RevisorEnginePL();
	}
	
	@Override
	protected PLConfig newConfig() {
		return new PLConfig(new ConfigFileStorage("./pl.properties"));
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Instruction<RevisorConsolePL> doParseCommand(final String command) throws Exception {
		try(StringReader reader = new StringReader(command)) {
			PLConsoleParser parser = new PLConsoleParser(this, command, new PLConsoleLexer(reader));
			return (Instruction<RevisorConsolePL>) parser.parse().value;
		}
	}
	
	@Override
	public Instruction<RevisorConsolePL> invalidInstruction(final String command, final String errorMessage) {
		return new InvalidInstruction<>(this, command, errorMessage);
	}
	
}
