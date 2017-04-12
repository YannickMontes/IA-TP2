package pacman.environnementRL;

import java.util.ArrayList;
import java.util.Arrays;

import pacman.elements.StateAgentPacman;
import pacman.elements.StateGamePacman;
import environnement.Etat;
/**
 * Classe pour définir un etat du MDP pour l'environnement pacman avec QLearning tabulaire

 */

class Position
{
	private int x;
	private int y;

	public Position(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public void setX(int x)
	{
		this.x = x;
	}

	public int getY()
	{
		return y;
	}

	public void setY(int y)
	{
		this.y = y;
	}

	public int getX()
	{
		return this.x;
	}
}

public class EtatPacmanMDPClassic implements Etat , Cloneable
{
	private Position pacmanPosition;
	private ArrayList<Position> ghostPositions;


	
	public EtatPacmanMDPClassic(StateGamePacman _stategamepacman)
	{
		pacmanPosition = new Position(_stategamepacman.getPacmanState(0).getX(),
										_stategamepacman.getPacmanState(0).getY());
		ghostPositions = new ArrayList<>();
		for(int i=0; i < _stategamepacman.getNumberOfGhosts(); i++)
		{
			Position ghostTmp = new Position(_stategamepacman.getGhostState(i).getX(),
										_stategamepacman.getGhostState(i).getY());
			ghostPositions.add(ghostTmp);
		}

		//_stategamepacman.get
	}
	
	@Override
	public String toString() {
		
		return "";
	}

	@Override
	public int hashCode()
	{
		return super.hashCode();
	}

	public Object clone() {
		EtatPacmanMDPClassic clone = null;
		try {
			// On recupere l'instance a renvoyer par l'appel de la 
			// methode super.clone()
			clone = (EtatPacmanMDPClassic)super.clone();
		} catch(CloneNotSupportedException cnse) {
			// Ne devrait jamais arriver car nous implementons 
			// l'interface Cloneable
			cnse.printStackTrace(System.err);
		}
		


		// on renvoie le clone
		return clone;
	}



	

}
