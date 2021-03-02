import java.nio.file.Paths;

import com.rapidminer.RapidMiner;
import com.rapidminer.tools.PlatformUtilities;

class CliLauncher {
	public static void main(String args[]) throws Exception {
		System.setProperty(PlatformUtilities.PROPERTY_RAPIDMINER_HOME, Paths.get("").toAbsolutePath().toString());
		RapidMiner.setExecutionMode(RapidMiner.ExecutionMode.COMMAND_LINE);
		RapidMiner.init();
	}
}