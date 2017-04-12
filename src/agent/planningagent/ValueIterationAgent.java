package agent.planningagent;

import java.util.*;

import environnement.*;
import util.HashMapUtil;


/**
 * Cet agent met a jour sa fonction de valeur avec value iteration 
 * et choisit ses actions selon la politique calculee.
 * @author laetitiamatignon
 *
 */
public class ValueIterationAgent extends PlanningValueAgent{
	/**
	 * discount facteur
	 */
	protected double gamma;

	private HashMap<Etat, List<Action>> actionList;

	/**
	 * fonction de valeur des etats
	 */
	protected HashMap<Etat,Double> V;
	
	/**
	 * 
	 * @param gamma
	 * @param mdp
	 */
	public ValueIterationAgent(double gamma,  MDP mdp) {
		super(mdp);
		this.gamma = gamma;
		V = new HashMap<Etat,Double>();
        this.actionList = new HashMap<>();
        for (Etat etat:this.mdp.getEtatsAccessibles()){
			V.put(etat, 0.0);
            this.actionList.put(etat, new ArrayList());
        }
		this.notifyObs();
		
	}
	
	
	
	
	public ValueIterationAgent(MDP mdp) {
		this(0.9,mdp);

	}
	
	/**
	 * 
	 * Mise a jour de V: effectue UNE iteration de value iteration (calcule V_k(s) en fonction de V_{k-1}(s'))
	 * et notifie ses observateurs.
	 * Ce n'est pas la version inplace (qui utilise nouvelle valeur de V pour mettre a jour ...)
	 */
	@Override
	public void updateV(){
        this.BellmanIteration();
        this.updateVminVmax();
		
		// mise a jour vmax et vmin pour affichage du gradient de couleur:
		//vmax est la valeur  max de V  pour tout s
		//vmin est la valeur min de V  pour tout s
		// ...

		//******************* laisser la notification a la fin de la methode	
		this.notifyObs();
	}
	

	/**
	 * renvoi l'action executee par l'agent dans l'etat e 
	 * Si aucune actions possibles, renvoi Action2D.NONE
	 */
	@Override
	public Action getAction(Etat e) {
		List<Action> actions = this.getPolitique(e);
		if (actions.size() == 0)
			return Action2D.NONE;
		else{//choix aleatoire
			return actions.get(rand.nextInt(actions.size()));
		}

		
	}
	@Override
	public double getValeur(Etat _e) {
		return  V.get(_e);
	}
	/**
	 * renvoi la (les) action(s) de plus forte(s) valeur(s) dans etat 
	 * (plusieurs actions sont renvoyees si valeurs identiques, liste vide si aucune action n'est possible)
	 */
	@Override
	public List<Action> getPolitique(Etat _e)
    {
		return this.actionList.get(_e);
		
	}
	
	@Override
	public void reset() {
		super.reset();

		
		this.V.clear();
		for (Etat etat:this.mdp.getEtatsAccessibles()){
			V.put(etat, 0.0);
		}
		this.notifyObs();
	}

	

	

	public HashMap<Etat,Double> getV() {
		return V;
	}
	public double getGamma() {
		return gamma;
	}
	@Override
	public void setGamma(double _g){
		System.out.println("gamma= "+gamma);
		this.gamma = _g;
	}

    private void updateValuesEndIteration(HashMap<Etat, Double> update)
    {
        Iterator<Etat> iterator = update.keySet().iterator();
        while(iterator.hasNext())
        {
            Etat t = iterator.next();
            this.getV().replace(t, update.get(t));
        }
    }


    private void BellmanIteration()
    {
        //delta est utilise pour detecter la convergence de l'algorithme
        //lorsque l'on planifie jusqu'a convergence, on arrete les iterations lorsque
        //delta < epsilon
        this.delta=0.0;
        List<Etat> states = this.mdp.getEtatsAccessibles();
        HashMap<Etat, Double> update = new HashMap();

        for(Etat currentState : states)
        {
            double max = Float.MIN_VALUE;
            List<Action> actions = this.mdp.getActionsPossibles(currentState);
            for(Action action : actions)
            {
                try
                {
                    Map<Etat, Double> possiblesNextState = this.mdp.getEtatTransitionProba(currentState, action);
                    double sum=0;
                    Iterator<Etat> iterator = possiblesNextState.keySet().iterator();
                    while(iterator.hasNext())
                    {
                        Etat nextState = iterator.next();
                        double recompense = this.mdp.getRecompense(currentState, action, nextState);
                        double probaAction = possiblesNextState.get(nextState);
                        double oldValue = this.getGamma() * this.getV().get(nextState);
                        sum += probaAction * (recompense+oldValue);
                    }
                    if(sum >= max)
                    {
                        max = sum;
                        if(!this.actionList.get(currentState).isEmpty())
                            this.actionList.get(currentState).remove(0);
                        this.actionList.get(currentState).add(action);
                    }


                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            update.put(currentState, max);
        }

        this.updateValuesEndIteration(update);
    }

    private void updateVminVmax()
    {
        Iterator<Etat> iterator = this.getV().keySet().iterator();
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        while(iterator.hasNext())
        {
            Etat etat = iterator.next();
            if(this.getV().get(etat) > max)
            {
                max = this.getV().get(etat);
            }
            if(this.getV().get(etat) < min)
            {
                min = this.getV().get(etat);
            }
        }

        this.vmin = min;
        this.vmax = max;
    }


	

	
}
