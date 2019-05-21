package fr.loria.orpailleur.revisor.engine.revisorPLAK.console;

import java.io.StringReader;

import fr.loria.orpailleur.revisor.engine.core.console.exception.ConsoleInitializationException;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.Instruction;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.InvalidInstruction;
import fr.loria.orpailleur.revisor.engine.core.utils.config.storage.ConfigFileStorage;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.RevisorEnginePLAK;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.config.PLAKConfig;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.parser.PLAKConsoleLexer;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.parser.PLAKConsoleParser;

/**
 * @author William Philbert
 */
public class RevisorConsolePLAK extends AbstractRevisorConsolePLAK<RevisorConsolePLAK, RevisorEnginePLAK, PLAKConfig, Instruction<RevisorConsolePLAK>> {
	
	// Constructors :
	
	public RevisorConsolePLAK() throws ConsoleInitializationException {
		super();
	}
	
	public RevisorConsolePLAK(final RevisorEnginePLAK engine) throws ConsoleInitializationException {
		super(engine);
	}
	
	public RevisorConsolePLAK(final PLAKConfig config) throws ConsoleInitializationException {
		super(config);
	}
	
	public RevisorConsolePLAK(final RevisorEnginePLAK engine, final PLAKConfig config) throws ConsoleInitializationException {
		super(engine, config);
	}
	
	// Methods :
	
	@Override
	public String getShortName() {
		return "PLAK";
	}
	
	@Override
	protected RevisorEnginePLAK newEngine() {
		return new RevisorEnginePLAK();
	}
	
	@Override
	protected PLAKConfig newConfig() {
		return new PLAKConfig(new ConfigFileStorage("./plak.properties"));
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Instruction<RevisorConsolePLAK> doParseCommand(final String command) throws Exception {
		try(StringReader reader = new StringReader(command)) {
			PLAKConsoleParser parser = new PLAKConsoleParser(this, command, new PLAKConsoleLexer(reader));
			return (Instruction<RevisorConsolePLAK>) parser.parse().value;
		}
	}
	
	@Override
	public Instruction<RevisorConsolePLAK> invalidInstruction(final String command, final String errorMessage) {
		return new InvalidInstruction<>(this, command, errorMessage);
	}
	
}
