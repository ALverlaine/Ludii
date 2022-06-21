package search.minimax;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import game.Game;
import main.collections.FVector;
import main.collections.FastArrayList;
import other.context.Context;
import other.move.Move;
import other.state.State;
import policies.softmax.SoftmaxFromMetadataSelection;
import policies.softmax.SoftmaxPolicy;

/**
 * AI based on Unbounded Best-First Search, using trained action evaluations to complete the heuristic scores.
 * 
 * @author cyprien
 *
 */

public class LazyBFS extends BestFirstSearch
{
	
	/** Weight of the action evaluation when linearly combined with the heuristic score */
	private static float actionEvaluationWeight = 0.0f;
		
	/** Set to true to record analyticData */
	public boolean performAnalysis = true;

	protected String dataSaveAdress = "analytic_data/default.sav";
	
	//-------------------------------------------------------------------------
	
	/** An epsilon parameter to give to the selection policy which hopefully is not chaging anything*/
	private final float epsilon = 0f;

	/** A learned policy to use in Selection phase */
	protected SoftmaxPolicy learnedSelectionPolicy = null;
	
	//-------------------------------------------------------------------------

	/** Recording the values assosciated to the action encountered */
	protected List<Float> actionEvaluations;
	
	/** Recording the difference in the heuristic evaluation as consequence of the possible moves*/
	protected List<Float> scoreEvolutions;
	
	/** Recording the correlation coefficient between the ranking according to the action evaluations and the ranking according to the heuristic scores of the resulting states.*/
	protected List<Float> rankingCorrelations;
	
	/** Comparative datas regarding action evaluations and score evolutions that will be written in a file*/
	protected StringBuffer analyticData = new StringBuffer("[]");
	
	
	//-------------------------------------------------------------------------
	
	public static LazyBFS createLazyBFS ()
	{
		return new LazyBFS();
	}
	
	/**
	 * Constructor:
	 */
	
	public LazyBFS ()
	{
		super();
		setLearnedSelectionPolicy(new SoftmaxFromMetadataSelection(epsilon));
		friendlyName = "Lazy BFS";
		
		return;
	}
	
	//-------------------------------------------------------------------------

	@Override
	public Move selectAction
	(
			final Game game, 
			final Context context, 
			final double maxSeconds,
			final int maxIterations,
			final int maxDepth
	)
	{
		
		actionEvaluations = new ArrayList<Float>();
		scoreEvolutions = new ArrayList<Float>();
		rankingCorrelations = new ArrayList<Float>();
		
		final Move bestMove = super.selectAction(game,context,maxSeconds,maxIterations,maxDepth);
		
		if (performAnalysis)
		{
			System.out.println(analyticObservations());
			try
			{
			   FileWriter myWriter = new FileWriter("/home/cyprien/Documents/M1/Internship/"+dataSaveAdress);
			   myWriter.write(analyticData.toString());
			   myWriter.close();
			   System.out.println("Successfully wrote to the file.");
			} catch (IOException e)
			{
			   System.out.println("An error occurred.");
			   e.printStackTrace();
			}
		};
		
		return bestMove;
	}
	
	@Override
	protected FVector estimateMoveValues
	(
		final FastArrayList<Move> legalMoves,
		final Context context,
		final int maximisingPlayer,
		final float inAlpha,
		final float inBeta,
		final List<Long> nodeLabel
	)
	{

		final State state = context.state();
		final int mover = state.playerToAgent(state.mover());
		final Game game = context.game();
		
		final float heuristicScore = getContextValue(context,maximisingPlayer,mover,inAlpha,inBeta);
		
		if (savingSearchTreeDescription)
		{
			searchTreeOutput.append("("+stringOfNodeLabel(nodeLabel)+","+Float.toString(heuristicScore)+","+((mover==maximisingPlayer)? 1:2)+"),\n");
		}
		
		final int numLegalMoves = legalMoves.size();
		final FVector moveScores = new FVector(numLegalMoves);
	
		for (int i = 0; i < numLegalMoves; ++i)
		{
			final Move m = legalMoves.get(i);
			
			final float actionValue = (float) learnedSelectionPolicy.computeLogit(context,m);
			
			moveScores.set(i,actionValue);
		};
		
		if (performAnalysis)
		{
			final FVector moveHeuristicScores = new FVector(numLegalMoves);
			
			for (int i = 0; i < numLegalMoves; ++i)
			{
				final Move m = legalMoves.get(i);
				
				final Context contextCopy = copyContext(context);
				game.apply(contextCopy, m);
				final float nextHeuristicScore = getContextValue(contextCopy,maximisingPlayer,mover,inAlpha,inBeta);
				
				moveHeuristicScores.set(i,nextHeuristicScore);
				
				scoreEvolutions.add(normalise(nextHeuristicScore)-normalise(heuristicScore));
				actionEvaluations.add((mover==maximisingPlayer)? moveScores.get(i): -moveScores.get(i));
			};
			
			if (numLegalMoves > 1)
			{
				final FastArrayList<Integer> tempMovesListIndices = new FastArrayList<Integer>(legalMoves.size());
				for (int i=0; i<numLegalMoves; i++)
				{
					tempMovesListIndices.add(i);
				}
				final int[] sortedMoveIndices = new int[numLegalMoves];
				for (int i=0; i<numLegalMoves; i++)
				{
					sortedMoveIndices[i] = tempMovesListIndices.removeSwap(ThreadLocalRandom.current().nextInt(tempMovesListIndices.size()));
				}
				
				final List<ScoredMoveIndex> evaluatedMoveIndices = new ArrayList<ScoredMoveIndex>(numLegalMoves);
				final List<ScoredMoveIndex> scoredMoveIndices = new ArrayList<ScoredMoveIndex>(numLegalMoves);
				
				for (int i=0; i<numLegalMoves; i++)
				{
					evaluatedMoveIndices.add(new ScoredMoveIndex(i,moveScores.get(i)));
					scoredMoveIndices.add(new ScoredMoveIndex(i,moveHeuristicScores.get(i)));
				}
				
				if (mover==maximisingPlayer)
					Collections.sort(scoredMoveIndices);
				else
					Collections.sort(scoredMoveIndices,Collections.reverseOrder());
				
				Collections.sort(evaluatedMoveIndices);
				
				float covarianceSum = 0f;
				float varianceSum1 = 0f;
				float varianceSum2 = 0f;
				
				for (float rank1=0; rank1<numLegalMoves; rank1++)
				{
					// rank variables must be float for the calculations
					
					int i = scoredMoveIndices.get((int) rank1).moveIndex;
					float rank2 = -1;
					for (int j=0; j<numLegalMoves; j++)
					{
						if (evaluatedMoveIndices.get(j).moveIndex == i)
							rank2 = j;
					}
					assert rank2 != -1f;
					
					covarianceSum += (rank1/(numLegalMoves-1)-0.5)*(rank2/(numLegalMoves-1)-0.5);
					
					varianceSum1 += Math.pow((rank1/(numLegalMoves-1)-0.5), 2);
					varianceSum2 += Math.pow((rank2/(numLegalMoves-1)-0.5), 2);
					
					//System.out.println(covarianceSum);
				}
				
				float correlation = (float) (covarianceSum / Math.sqrt(varianceSum1*varianceSum2));
				
				rankingCorrelations.add(correlation);
			}
			
		}
		
		int sign = (maximisingPlayer == mover)? 1 : -1 ;
			
		for (int i = 0; i < numLegalMoves; ++i)
		{
			moveScores.set(i,moveScores.get(i)*actionEvaluationWeight*sign+heuristicScore);
		}
		
		return moveScores;
	}
	
	private float normalise(float heuristicScore)
	{
		
		float maxValue = 5f ; //FIXME
		float minValue = -5f ;
		
		if (heuristicScore>maxValue)
			return maxValue;
		else if (heuristicScore<minValue)
			return minValue;
		else
			return heuristicScore;
	}

	//-------------------------------------------------------------------------
	
	public void initAI(final Game game, final int playerID)
	{
		super.initAI(game,  playerID);
		
		// Instantiate feature sets for selection policy
		if (learnedSelectionPolicy != null)
		{
			learnedSelectionPolicy.initAI(game, playerID);
		}
		
		return;
	}
	
	public String analyticObservations()
	{
		final StringBuffer res = new StringBuffer();
		
		final int nbEntries = scoreEvolutions.size();
		
		assert nbEntries == actionEvaluations.size();
		
		float actionEvaluationsSum = 0f;
		float actionEvaluationsAbsSum = 0f;
		float scoreEvolutionsSum = 0f;
		float scoreEvolutionsAbsSum = 0f;
		float rankingCorrelationSum = 0f;
		
		for (int i=0; i<nbEntries; i++)
		{
			actionEvaluationsSum += actionEvaluations.get(i);
			scoreEvolutionsSum += scoreEvolutions.get(i);
			actionEvaluationsAbsSum += Math.abs(actionEvaluations.get(i));
			scoreEvolutionsAbsSum += Math.abs(scoreEvolutions.get(i));
		};
		for (int i=0; i<rankingCorrelations.size(); i++)
		{
			rankingCorrelationSum += rankingCorrelations.get(i);
		}
		
		float actionEvaluationsMean = actionEvaluationsSum/nbEntries;
		float scoreEvolutionsMean = scoreEvolutionsSum/nbEntries;
		
		float rankingCorrelationMean = rankingCorrelationSum / rankingCorrelations.size();
		
		float covarianceSum = 0f;
		float actionEvaluationsVarianceSum = 0f;
		float scoreEvolutionsVarianceSum = 0f;

		for (int i=0; i<nbEntries; i++)
		{
			covarianceSum += (actionEvaluations.get(i)-actionEvaluationsMean)*(scoreEvolutions.get(i)-scoreEvolutionsMean);
			actionEvaluationsVarianceSum += Math.pow((actionEvaluations.get(i)-actionEvaluationsMean),2);
			scoreEvolutionsVarianceSum += Math.pow((scoreEvolutions.get(i)-scoreEvolutionsMean),2);
		};
		
		float covariance = covarianceSum / nbEntries;
		float actionEvaluationsVariance = actionEvaluationsVarianceSum / nbEntries;
		float scoreEvolutionsVariance = scoreEvolutionsVarianceSum / nbEntries;
		float correlationCoeficient = (float) (covariance / Math.sqrt(actionEvaluationsVariance*scoreEvolutionsVariance));
		
		res.append("\nNumber of actionEvaluations entries: ");
		res.append(Integer.toString(nbEntries));
		res.append("\nAverage abs action evaluation: ");
		res.append(Float.toString(actionEvaluationsAbsSum/nbEntries));
		res.append("\nAverage abs score evolution: ");
		res.append(Float.toString(scoreEvolutionsAbsSum/nbEntries));
		res.append("\nCorrelation coeficient: ");
		res.append(Float.toString(correlationCoeficient));
		res.append("\nRatio: ");
		res.append(Float.toString(scoreEvolutionsAbsSum/actionEvaluationsAbsSum));
		res.append("\nRanking correlation: ");
		res.append(Float.toString(rankingCorrelationMean));
		
		analyticData.deleteCharAt(analyticData.length()-1);
		analyticData.append("["+Float.toString(actionEvaluationsMean)+","+Float.toString(scoreEvolutionsMean)+","+Float.toString(correlationCoeficient)+","+Float.toString(rankingCorrelationMean)+"],\n]");
		
		return res.toString();
	}
	
	
	//-------------------------------------------------------------------------
	
	/**
	 * Sets the learned policy to use in Selection phase
	 * @param policy The policy.
	 */
	public void setLearnedSelectionPolicy(final SoftmaxPolicy policy)
	{
		learnedSelectionPolicy = policy;
	}
}
