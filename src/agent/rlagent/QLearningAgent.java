package agent.rlagent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javafx.util.Pair;
import environnement.Action;
import environnement.Environnement;
import environnement.Etat;
/**
 * Renvoi 0 pour valeurs initiales de Q
 * @author laetitiamatignon
 *
 */
public class QLearningAgent extends RLAgent {
	/**
	 *  format de memorisation des Q valeurs: utiliser partout setQValeur car cette methode notifie la vue
	 */
	protected HashMap<Etat,HashMap<Action,Double>> qvaleurs;
	
	//AU CHOIX: vous pouvez utiliser une Map avec des Pair pour les clés si vous préférez
	//protected HashMap<Pair<Etat,Action>,Double> qvaleurs;

	
	/**
	 * 
	 * @param alpha
	 * @param gamma
	 * @param _env
	 */
	public QLearningAgent(double alpha, double gamma, Environnement _env)
	{
		super(alpha, gamma,_env);
		qvaleurs = new HashMap<Etat,HashMap<Action,Double>>();
	}


	
	
	/**
	 * renvoi la (les) action(s) de plus forte(s) valeur(s) dans l'etat e
	 *  (plusieurs actions sont renvoyees si valeurs identiques)
	 *  renvoi liste vide si aucunes actions possibles dans l'etat (par ex. etat absorbant)
	 */
	@Override
	public List<Action> getPolitique(Etat e)
	{
		// retourne action de meilleures valeurs dans _e selon Q : utiliser getQValeur()
		// retourne liste vide si aucune action legale (etat terminal)
		List<Action> returnactions = new ArrayList<Action>();
		if (this.getActionsLegales(e).size() == 0)
		{//etat  absorbant; impossible de le verifier via environnement
			System.out.println("aucune action legale");
			return new ArrayList<Action>();
		}
		
		List<Action> possiblesActions = this.getActionsLegales(e);
		double max = 0.0;
		for(Action a : possiblesActions)
		{
			if(this.getQValeur(e, a) > max)
			{
				returnactions = new ArrayList<>();
				returnactions.add(a);
				max = this.getQValeur(e, a);
			}
			else if(this.getQValeur(e, a) == max)
			{
				returnactions.add(a);
			}
		}
		return returnactions;
		
		
	}
	
	@Override
	public double getValeur(Etat e)
	{
		if(!this.qvaleurs.containsKey(e))
			return 0.0;

		double max = Double.MIN_VALUE;
		List<Action> actions = this.getActionsLegales(e);
		for(Action a : actions)
		{
			if (this.getQValeur(e, a) > max)
				max = this.getQValeur(e, a);
		}
		return max;
	}

	@Override
	public double getQValeur(Etat e, Action a)
	{
		if(this.qvaleurs.containsKey(e) && this.qvaleurs.get(e).containsKey(a))
		{
			return this.qvaleurs.get(e).get(a);
		}

		return 0.0;
	}
	
	
	
	@Override
	public void setQValeur(Etat e, Action a, double d)
	{
		if(!this.qvaleurs.containsKey(e))
		{
			this.qvaleurs.put(e, new HashMap<>());
		}
		if(!this.qvaleurs.get(e).containsKey(a))
		{
			this.qvaleurs.get(e).put(a, d);
		} else {
			this.qvaleurs.get(e).replace(a, d);
		}

		if(this.vmax < d)
			this.vmax = d;
		
		this.notifyObs();
		
	}
	
	
	/**
	 * mise a jour du couple etat-valeur (e,a) apres chaque interaction <etat e,action a, etatsuivant esuivant, recompense reward>
	 * la mise a jour s'effectue lorsque l'agent est notifie par l'environnement apres avoir realise une action.
	 * @param e
	 * @param a
	 * @param esuivant
	 * @param reward
	 */
	@Override
	public void endStep(Etat e, Action a, Etat esuivant, double reward)
	{
		if (RLAgent.DISPRL)
			System.out.println("QL mise a jour etat "+e+" action "+a+" etat' "+esuivant+ " r "+reward);

		double nextStep = (1 - this.alpha) * this.getQValeur(e, a) + this.alpha * (reward + (this.gamma * this.getValeur(esuivant)));

		this.setQValeur(e, a , nextStep);
	}

	@Override
	public Action getAction(Etat e) {
		this.actionChoisie = this.stratExplorationCourante.getAction(e);
		return this.actionChoisie;
	}

	@Override
	public void reset() {
		super.reset();
		//*** VOTRE CODE
		
		this.episodeNb =0;
		this.notifyObs();
	}









	


}
