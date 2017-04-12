package agent.strategy;

import java.util.List;
import java.util.Random;

import agent.rlagent.RLAgent;
import environnement.Action;
import environnement.Etat;
/**
 * Strategie qui renvoit un choix aleatoire avec proba epsilon, un choix glouton (suit la politique de l'agent) sinon
 * @author lmatignon
 *
 */
public class StrategyGreedy extends StrategyExploration{
	/**
	 * parametre pour probabilite d'exploration
	 */
	protected double epsilon;
	private Random rand=new Random();
	
	
	
	public StrategyGreedy(RLAgent agent,double epsilon) {
		super(agent);
		this.epsilon = epsilon;
	}

	@Override
	public Action getAction(Etat _e) {//renvoi null si _e absorbant
		double randNumber = rand.nextDouble();
		List<Action> actions;
		List<Action> possiblesActions = this.agent.getActionsLegales(_e);
		if (possiblesActions.isEmpty())
		{
			return null;
		}
		Action returnedAction = null;
		if(randNumber > this.epsilon)
		{
			randNumber = (int)(rand.nextDouble() * (possiblesActions.size()));
			returnedAction = possiblesActions.get((int)randNumber);
		}
		else
		{
			List<Action> bestActions = this.agent.getPolitique(_e);
			if(bestActions.size() == 1)
			{
				randNumber = (int)(rand.nextDouble() * (bestActions.size()));
				returnedAction = bestActions.get((int)randNumber);
			}
			else if(!bestActions.isEmpty())
			{
				returnedAction = bestActions.get(0);
			}
		}
		
		return returnedAction;
	}

	public double getEpsilon() {
		return epsilon;
	}

	public void setEpsilon(double epsilon) {
		this.epsilon = epsilon;
		System.out.println("epsilon:"+epsilon);
	}

/*	@Override
	public void setAction(Action _a) {
		// TODO Auto-generated method stub
		
	}*/

}
