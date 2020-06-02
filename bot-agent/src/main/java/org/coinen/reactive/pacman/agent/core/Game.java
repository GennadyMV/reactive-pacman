/*
 * Implementation of "Ms Pac-Man" for the "Ms Pac-Man versus Ghost Team Competition", brought
 * to you by Philipp Rohlfshagen, David Robles and Simon Lucas of the University of Essex.
 * 
 * www.pacman-vs-ghosts.net
 * 
 * Code written by Philipp Rohlfshagen, based on earlier implementations of the game by
 * Simon Lucas and David Robles. 
 * 
 * You may use and distribute this code freely for non-commercial purposes. This notice 
 * needs to be included in all distributions. Deviations from the original should be 
 * clearly documented. We welcome any comments and suggestions regarding the code.
 */
package org.coinen.reactive.pacman.agent.core;

//import game.controllers.ghosts.GhostsActions;
import org.coinen.reactive.pacman.agent.controllers.pacman.PacManAction;
import org.coinen.reactive.pacman.agent.model.Decision;

import java.util.Set;

/*
 * This interface defines the contract between the game engine and the controllers. It provides all
 * the methods a controller may use to (a) query the game state, (b) compute game-related attributes
 * and (c) test moves by using a forward model (i.e., copy() followed by advanceGame()).
 */
public interface Game
{
	//These constants specify the exact nature of the game
	int UP=0;							//direction going up
	int RIGHT=1;						//direction going right
	int DOWN=2;							//direction going down
	int LEFT=3;							//direction going left
	int EMPTY=-1;						//value of an non-existing neighbour
	int PILL=10;						//points for a pill
	int POWER_PILL=50;					//points for a power pill
	int GHOST_EAT_SCORE=200;			//score for the first ghost eaten (doubles every time for the duration of a single power pill)
	int EDIBLE_TIME=200;				//initial time a ghost is edible for (decreases as level number increases)
	float EDIBLE_TIME_REDUCTION=0.9f;	//reduction factor by which edible time decreases as level number increases
	int[] LAIR_TIMES={40,60,80,100};	//time spend in the lair by each ghost at the start of a level
	int COMMON_LAIR_TIME=40;			//time spend in lair after being eaten
	float LAIR_REDUCTION=0.9f;			//reduction factor by which lair times decrease as level number increases
	int LEVEL_LIMIT=3000;				//time limit for a level
	float GHOST_REVERSAL=0.0015f;		//probability of a global ghost reversal event
	int MAX_LEVELS=16;					//maximum number of levels played before the end of the game
	int EXTRA_LIFE_SCORE=10000;			//extra life is awarded when this many points have been collected
	int EAT_DISTANCE=2;					//distance in the connected graph considered close enough for an eating event to take place
	int NUM_GHOSTS=4;					//number of ghosts in the game
	int NUM_MAZES=4;					//number of different mazes in the game
	int NUM_LIVES=3;					//total number of lives Ms Pac-Man has (current + NUM_LIVES-1 spares)
	int INITIAL_PAC_DIR=3;				//initial direction taken by Ms Pac-Man
	int[] INITIAL_GHOST_DIRS={3,3,3,3};	//initial directions for the ghosts (after leaving the lair)
	int GHOST_SPEED_REDUCTION=2;		//difference in speed when ghosts are edible (every GHOST_SPEED_REDUCTION, a ghost remains stationary)
	
	Game copy();														//returns an exact copy of the game (forward model)
	int[] advanceGame(PacManAction pacMan/*, GhostsActions ghosts*/);	//advances the game using the actions (directions) supplied; returns all directions played [PacMan, Ghost1, Ghost2, Ghost3, Ghost4]
	int advanceGame(Decision pacMan/*, GhostsActions ghosts*/);	//advances the game using the actions (directions) supplied; returns all directions played [PacMan, Ghost1, Ghost2, Ghost3, Ghost4]
	int getReverse(int direction);							//returns the reverse of the direction supplied
	boolean gameOver();										//returns true is Ms Pac-Man has lost all her lives or if MAX_LEVELS has been reached
	boolean checkPill(int pillIndex);						//checks if the pill specified is still available
	boolean checkPowerPill(int powerPillIndex);				//checks if the power pill specified is still available
	
	int[] getPacManNeighbours();								//returns an array of size 4, indicating neighbouring nodes for the current position of Ms Pac-Man. E.g., [-1,12,-1,44] for neighbours 12 and 44 in direction RIGHT and LEFT
	int[] getGhostNeighbours(String whichGhost);				//returns an array of size 4, indicating neighbouring nodes for the current position of the ghost specified. Replaces the direction corresponding to the opposite previous direction with -1

	Set<String> getGhostsUuids();
	boolean isPowerUpEnabled();
	int getCurLevel();										//returns the current level
	int getCurMaze();										//returns the current maze
	int getCurPacManLoc();									//returns the node index Ms Pac-Man is at
	int getCurPacManDir();									//returns the last direction taken by Ms Pac-Man
	int getLivesRemaining();									//returns the number of lives remaining for Ms Pac-Man
	int getGhostCount();						//returns the node index for the ghost specified
	int getCurGhostLoc(String whichGhost);						//returns the node index for the ghost specified
	int getCurGhostDir(String whichGhost);						//returns the last direction taken by the ghost specified
	boolean isEdible(String whichGhost);						//returns true if the ghost is currently edible
	int getScore();											//returns the score of the game
	int getLevelTime();										//returns the time for which the CURRENT level has been played
	int getTotalTime();										//returns the time for which the game has been played (across all levels)
	int getNumberPills();									//returns the total number of pills in this maze (at the beginning of the level)
	int getNumberPowerPills();								//returns the total number of power pills in this maze (at the beginning of the level)
	String getName();										//returns the name of the maze
	int getInitialPacPosition();								//returns the position where Ms Pac-Man starts at the beginning of the level
	int getNumberOfNodes();									//returns the total number of nodes in the graph (pills, power pills and empty)
	int getX(int nodeIndex);									//returns the x-coordinate of the node specified
	int getY(int nodeIndex);									//returns the y-coordinate of the node specified
	int getPillIndex(int nodeIndex);							//returns the pill index of the node specified (can be used with the bitset for the pills)
	int getPowerPillIndex(int nodeIndex);					//returns the power pill index of the node specified (can be used with the bitset for the power pills)
	int getNeighbour(int nodeIndex, int direction);			//returns the neighbour of the node specified for the direction supplied
	int[] getPillIndices();									//returns indices to all nodes with pills
	int[] getPowerPillIndices();								//returns indices to all nodes with power pills
	int[] getJunctionIndices();								//returns indices to all nodes that are junctions
		
	int getNextEdibleGhostScore();							//returns the score awarded for the next ghost to be eaten
	int getNumActivePills();									//returns the number of pills still in the maze
	int getNumActivePowerPills();							//returns the number of power pills still in the maze
	int[] getPillIndicesActive();							//returns the indices of all active pills in the maze
	int[] getPowerPillIndicesActive();						//returns the indices of all active power pills in the maze
	
	boolean isCorner(int nodeIndex);							//returns true if node is a corner
	boolean isJunction(int nodeIndex);						//returns true if node is a junction (more than 2 neighbours)
	int getNumNeighbours(int nodeIndex);						//returns the number of neighbours of the node specified
	
	enum DM{PATH,EUCLID,MANHATTEN}                            //simple enumeration for use with the direction methods (below)
	int getNextPacManDir(int to, boolean closer, DM measure);	//returns the direction Ms Pac-Man should take to approach/retreat from the node specified, using the distance measure specified

	int getPathDistance(int from, int to);					//returns the shortest path distance (Dijkstra) from one node to another
	double getEuclideanDistance(int from, int to);			//returns the Euclidean distance between two nodes
	int getManhattenDistance(int from, int to);				//returns the Manhatten distance between two nodes
	
	int[] getPossiblePacManDirs();		//returns the set of possible directions for Ms Pac-Man, with or without the direction opposite to the last direction taken
	int[] getPossiblePacManDirs(Decision lastDecision);		//returns the set of possible directions for Ms Pac-Man, with or without the direction opposite to the last direction taken

	int[] getPath(int from, int to);										//returns the path from one node to another (e.g., [1,2,5,7,9] for 1 to 9)
	int[] getGhostPath(String whichGhost, int to);							//returns the path from one node to another, taking into account that reversals are not possible
	int getTarget(int from, int[] targets, boolean nearest, DM measure);	//selects a target from 'targets' given current position ('from'), a distance measure and whether it should be the point closest or farthest
	int getGhostPathDistance(String whichGhost, int to);						//returns the distance of a path for the ghost specified (accounts for the fact that ghosts may not reverse)
}