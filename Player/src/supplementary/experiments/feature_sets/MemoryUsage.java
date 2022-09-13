package supplementary.experiments.feature_sets;

import java.io.File;
import java.util.regex.Pattern;

import features.feature_sets.BaseFeatureSet;
import features.feature_sets.BaseFeatureSet.FeatureSetImplementations;
import features.feature_sets.network.JITSPatterNetFeatureSet;
import features.feature_sets.network.SPatterNetFeatureSet;
import function_approx.LinearFunction;
import game.Game;
import main.CommandLineArgParse;
import main.CommandLineArgParse.ArgOption;
import main.CommandLineArgParse.OptionTypes;
import main.StringRoutines;
import other.GameLoader;
import utils.ExperimentFileUtils;

/**
 * Script to check memory usage of different feature set implementations
 * 
 * @author Dennis Soemers
 */
public class MemoryUsage
{
	
	//-------------------------------------------------------------------------
	
	/** Games we have featuresets for */
	private static final String[] GAMES = 
			new String[]
			{
				"Alquerque.lud",
				"Amazons.lud",
				"ArdRi.lud",
				"Arimaa.lud",
				"Ataxx.lud",
				"Bao Ki Arabu (Zanzibar 1).lud",
				"Bizingo.lud",
				"Breakthrough.lud",
				"Chess.lud",
				"Chinese Checkers.lud",
				"English Draughts.lud",
				"Fanorona.lud",
				"Fox and Geese.lud",
				"Go.lud",
				"Gomoku.lud",
				"Gonnect.lud",
				"Havannah.lud",
				"Hex.lud",
				"Knightthrough.lud",
				"Konane.lud",
				"Level Chess.lud",
				"Lines of Action.lud",
				"Omega.lud",
				"Pentalath.lud",
				"Pretwa.lud",
				"Reversi.lud",
				"Royal Game of Ur.lud",
				"Surakarta.lud",
				"Shobu.lud",
				"Tablut.lud",
				"Triad.lud",
				"XII Scripta.lud",
				"Yavalath.lud"
			};
	
	//-------------------------------------------------------------------------
	
	/**
	 * Eval the memory usage of feature sets
	 * @param parsedArgs
	 */
	private static void evalMemoryUsage(final CommandLineArgParse parsedArgs)
	{
		String trainingOutDir = parsedArgs.getValueString("--training-out-dir");
		if (!trainingOutDir.endsWith("/"))
			trainingOutDir += "/";
		
		for (final String gameName : GAMES)
		{
			final Game game = GameLoader.loadGameFromName(gameName);
			final int numPlayers = game.players().count();
			
			final String cleanGameName = StringRoutines.cleanGameName(gameName.replaceAll(Pattern.quote(".lud"), ""));
			//final File gameTrainingDir = new File(trainingOutDir + cleanGameName + "/");
			
			final String[] policyWeightFilepathsPerPlayer = new String[numPlayers + 1];
			for (int p = 1; p <= numPlayers; ++p)
			{
				String policyWeightsFilepath = trainingOutDir + cleanGameName + "/PolicyWeightsCE_P" + p +  "_00201.txt";
				
				if (!new File(policyWeightsFilepath).exists())
				{
					final String parentDir = new File(policyWeightsFilepath).getParent();
					
					// Replace with whatever is the latest file we have
					if (policyWeightsFilepath.contains("Selection"))
					{
						policyWeightsFilepath = 
								ExperimentFileUtils.getLastFilepath(parentDir + "/PolicyWeightsSelection_P" + p, "txt");
					}
					else if (policyWeightsFilepath.contains("Playout"))
					{
						policyWeightsFilepath = 
								ExperimentFileUtils.getLastFilepath(parentDir + "/PolicyWeightsPlayout_P" + p, "txt");
					}
					else if (policyWeightsFilepath.contains("TSPG"))
					{
						policyWeightsFilepath = 
								ExperimentFileUtils.getLastFilepath(parentDir + "/PolicyWeightsTSPG_P" + p, "txt");
					}
					else if (policyWeightsFilepath.contains("PolicyWeightsCE"))
					{
						policyWeightsFilepath = 
								ExperimentFileUtils.getLastFilepath(parentDir + "/PolicyWeightsCE_P" + p, "txt");
					}
					else
					{
						policyWeightsFilepath = null;
					}
				}

				if (policyWeightsFilepath == null)
					System.err.println("Cannot resolve policy weights filepath: " + trainingOutDir + cleanGameName + "/PolicyWeightsCE_P" + p +  "_00201.txt");
				
				policyWeightFilepathsPerPlayer[p] = policyWeightsFilepath;
			}
			
			final LinearFunction[] linFuncs = new LinearFunction[numPlayers + 1];
			for (int p = 1; p <= numPlayers; ++p)
			{
				linFuncs[p] = LinearFunction.fromFile(policyWeightFilepathsPerPlayer[p]);
			}
			
			final BaseFeatureSet[] featureSets = new BaseFeatureSet[numPlayers + 1];
			
			for (final FeatureSetImplementations impl : new FeatureSetImplementations[] {FeatureSetImplementations.SPATTERNET, FeatureSetImplementations.JITSPATTERNET})
			{
				for (int p = 1; p <= numPlayers; ++p)
				{
					final String parentDir = new File(policyWeightFilepathsPerPlayer[p]).getParent();
					final String featureSetFilepath = parentDir + File.separator + linFuncs[p].featureSetFile();
					
					if (impl == FeatureSetImplementations.SPATTERNET)
						featureSets[p] = new SPatterNetFeatureSet(featureSetFilepath);
					else if (impl == FeatureSetImplementations.JITSPATTERNET)
						featureSets[p] = JITSPatterNetFeatureSet.construct(featureSetFilepath);
				}
				
				// TODO create policies from the feature sets
				
				// TODO create MCTS agents around the policies
				
				// TODO play a bit
				
				// TODO check memory usage
			}
		}
	}
	
	//-------------------------------------------------------------------------
	
	/**
	 * Main method
	 * @param args
	 */
	public static void main(final String[] args)
	{
		// Define options for arg parser
		final CommandLineArgParse argParse = 
				new CommandLineArgParse
				(
					true,
					"Eval memory usage of feature sets."
				);
		
		argParse.addOption(new ArgOption()
				.withNames("--training-out-dir")
				.help("Output directory for training results.")
				.withNumVals(1)
				.withType(OptionTypes.String)
				.setRequired());
		
		// Parse the args
		if (!argParse.parseArguments(args))
			return;
		
		evalMemoryUsage(argParse);
	}

}
