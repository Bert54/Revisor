package fr.loria.k.revisor.engine.revisorPCSFC.console.config;

import fr.loria.orpailleur.revisor.engine.core.utils.config.EngineConfig;
import fr.loria.orpailleur.revisor.engine.core.utils.config.item.BooleanConfig;
import fr.loria.orpailleur.revisor.engine.core.utils.config.storage.ConfigStorage;

public class PCSFCConfig extends EngineConfig {

	public final BooleanConfig displayVariablesContent = new BooleanConfig("Display_variables_content", true, "If true, the content of variables will be printed as well when printing variable names; Else, only variable types will be printed.");
	public final BooleanConfig displayPCLCFormulaRevision = new BooleanConfig("Display_formulas_converted_to_PCLC_before_revision", false, "If true, the formulas used for the revision will be printed in their PCLC form before executing the revision algorithm; Else, nothing will be printed.");
	
	public PCSFCConfig(ConfigStorage storage) {
		super(storage);
	}

	@Override
	public String description() {
		return "This tab contains the settings used by the console mode of Revisor/PCSFC";
	}

}
