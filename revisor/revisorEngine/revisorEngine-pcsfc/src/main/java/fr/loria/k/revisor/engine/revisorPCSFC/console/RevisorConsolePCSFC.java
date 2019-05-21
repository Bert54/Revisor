package fr.loria.k.revisor.engine.revisorPCSFC.console;

import java.io.StringReader;

import fr.loria.k.revisor.engine.revisorPCSFC.RevisorEnginePCSFC;
import fr.loria.k.revisor.engine.revisorPCSFC.console.config.PCSFCConfig;
import fr.loria.k.revisor.engine.revisorPCSFC.console.parser.PCSFCConsoleLexer;
import fr.loria.k.revisor.engine.revisorPCSFC.console.parser.PCSFCConsoleParser;
import fr.loria.orpailleur.revisor.engine.core.console.exception.ConsoleInitializationException;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.Instruction;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.InvalidInstruction;
import fr.loria.orpailleur.revisor.engine.core.utils.config.storage.ConfigFileStorage;

public class RevisorConsolePCSFC extends AbstractRevisorConsolePCSFC<RevisorConsolePCSFC, RevisorEnginePCSFC, PCSFCConfig, Instruction<RevisorConsolePCSFC>> {

	public RevisorConsolePCSFC () throws ConsoleInitializationException {
		super();
	}
	
	public RevisorConsolePCSFC (final RevisorEnginePCSFC engine) throws ConsoleInitializationException {
		super(engine);
	}
	
	public RevisorConsolePCSFC (final PCSFCConfig config) throws ConsoleInitializationException {
		super(config);
	}
	
	public RevisorConsolePCSFC (final RevisorEnginePCSFC engine, final PCSFCConfig config) throws ConsoleInitializationException {
		super(engine, config);
	}

	@Override
	public String getShortName() {
		return "PCSFC";
	}
	
	@Override
	protected RevisorEnginePCSFC newEngine() {
		return new RevisorEnginePCSFC();
	}
	
	@Override
	protected PCSFCConfig newConfig() {
		return new PCSFCConfig(new ConfigFileStorage("./pcsfc.properties"));
	}

	@Override
	public Instruction<RevisorConsolePCSFC> invalidInstruction(String command, String errorMessage) {
		return new InvalidInstruction<>(this, command, errorMessage);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Instruction<RevisorConsolePCSFC> doParseCommand(final String command) throws Exception {
		try(StringReader reader = new StringReader(command)) {
			PCSFCConsoleParser parser = new PCSFCConsoleParser(this, command, new PCSFCConsoleLexer(reader));
			return (Instruction<RevisorConsolePCSFC>) parser.parse().value;
		}
	}
	
}
