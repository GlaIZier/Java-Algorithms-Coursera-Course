import java.util.HashMap;
import java.util.Map;


/*************************************************************************
 *  Third week coursera course Java Algorithms part 2
 *  Baseball Elimination
 *  @Date 09.02.14
 *  @Author Khokhlushin Mikhail
 *  
 *  Links:
 *  Specification: http://coursera.cs.princeton.edu/algs4/assignments/baseball.html
 *             http://coursera.cs.princeton.edu/algs4/checklists/baseball.html
 *             http://algs4.cs.princeton.edu/windows/
 *  Compilation:   javac-algs4 BaseballElimination.java
 *  Execution:     java-algs4 BaseballElimination <file_name_princeton_formatted_teams.txt> 
 *                     <(optional to test Flow network) name_of_team>  
 *                     with uncommented main method
 *  Execution only with installed algs4.jar and stdlib.jar
 *  
 *  Maxflow algorithm for mathematically elimination of teams, which can't win their division.
 *  At the beginning we have eliminatedTemas with all teams and null certificates.
 *  After checking the elimination of the team, we delete it from the map, if it is not eliminated
 *      or add certificate if it is eliminated. 
 *  
 *************************************************************************/
//immutable data type
public class BaseballElimination {

   private final int numTeams;
  
   // <teamName, positionNumber> for quick finding team position
   // used to find index in wins, loss, remain, games
   private final Map<String, Integer> teams;
   
   private final int[] wins;
   
   private final int[] loss;
   
   private final int[] remain;
   
   private final int[][] games;
   
   // Map cache for eliminated teams.
   private Map<String, Bag<String> > eliminatedTeams;
   
   // create a baseball division from given filename in format specified below
   public BaseballElimination(String filename) {
      In in = new In(filename);
      numTeams = in.readInt();
      teams = new HashMap<String, Integer>();
      wins = new int[numTeams];
      loss = new int[numTeams];
      remain = new int[numTeams];
      games = new int[numTeams][numTeams];
      
      // At first it has all teams with null bag
      // If team is eliminated, then we add certificate as bag
      // else we delete team from map
      // if we haven't found out about team, then it has null bag
      eliminatedTeams = new HashMap<String, Bag<String>>();
      
      int fileLine = 0; // equal to team position
      while (!in.isEmpty()) {
         int teamPosition = fileLine;
         String teamName = in.readString();
         teams.put(teamName, teamPosition);
         eliminatedTeams.put(teamName, null);
         wins[fileLine] = in.readInt();
         loss[fileLine] = in.readInt();
         remain[fileLine] = in.readInt();
         for (int numGames = 0; numGames < games[fileLine].length; numGames++) {
            games[fileLine][numGames] = in.readInt();
         }
         fileLine++;
      }
      
      in.close();
   }

   private void testParseInput() {
      System.out.println("Teams number: " + numberOfTeams());
      
      System.out.println("Teams: ");
      for (String team : teams.keySet()) {
         System.out.print(team + ", " + teams.get(team) + "; ");
      }
      System.out.println();
      
      System.out.println("Wins: ");
      for (int i = 0; i < wins.length; i++) {
         System.out.print(wins[i] + " ");
      }
      System.out.println();
      
      System.out.println("Losses: ");
      for (int i = 0; i < loss.length; i++) {
         System.out.print(loss[i] + " ");
      }
      System.out.println();
      
      System.out.println("Remains: ");
      for (int i = 0; i < remain.length; i++) {
         System.out.print(remain[i] + " ");
      }
      System.out.println();
      
      System.out.println("Games: ");
      for (int i = 0; i < games.length; i++) {
         for (int j = 0; j < games[i].length; j++) {
            System.out.print(games[i][j] + " "); 
         }
         System.out.println();
      }
      System.out.println();
   }

   // number of teams
   public int numberOfTeams() {
      return numTeams;
   }
   
   // all teams
   public Iterable<String> teams() {
      return teams.keySet();
   } 
   
   // number of wins for given team
   public int wins(String team) {
      if (!teams.containsKey(team)) 
         throw new java.lang.IllegalArgumentException("No such team!");
      return wins[teams.get(team)];
   }
   
   // number of losses for given team
   public int losses(String team) {
      if (!teams.containsKey(team)) 
         throw new java.lang.IllegalArgumentException("No such team!");
      return loss[teams.get(team)];
   } 
   
   // number of remaining games for given team
   public int remaining(String team) {
      if (!teams.containsKey(team)) 
         throw new java.lang.IllegalArgumentException("No such team!");
      return remain[teams.get(team)];
   }    
   
   // number of remaining games between team1 and team2 
   public int against(String team1, String team2) {
      if ( !teams.containsKey(team1) || !teams.containsKey(team2) )  
         throw new java.lang.IllegalArgumentException("No such team!");
      return games[teams.get(team1)][teams.get(team2)];
   } 
   
   // is given team eliminated?
   public boolean isEliminated(String team) {
      // check for bad input
      if (!teams.containsKey(team)) 
         throw new java.lang.IllegalArgumentException("No such team!");
      // check if already found out elimination of this team
      if (!eliminatedTeams.containsKey(team)) return false;
      if (eliminatedTeams.get(team) != null) return true;
      
      // check for trivial elimination
      // bugs in trivial elimination. Possible due to competition of trivial and 
      // if (isTrivialEliminated(team)) return true;
      
      // nontrivial elimination
      // create new  FlowNetwork
      FlowNetwork flowNetwork = createEliminationNetwork(team);
      // compute max flow. It changes flowNetwork by marking flows on weighted edges
      final int source = 0, target = flowNetwork.V() - 1;
      FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, source, target);
      // check elimination. Team is not eliminated iff flow from s to games vertices ==
      // capacity   
      for (FlowEdge e : flowNetwork.adj(source)) {
         if (e.flow() != e.capacity()) {
            // eliminated - add new elimination certificate
            addElimnationCertificate(flowNetwork, fordFulkerson, team);
            return true;
         }
      }
      // not eliminated - delete from aliminatedTeams
      eliminatedTeams.remove(team);
      return false;
   }

//   private boolean isTrivialEliminated(String team) {
//      int teamWins = wins[teams.get(team)];
//      int teamRemains = remain[teams.get(team)];
//      for (String curTeam : teams() ) {
//         int teamID = teams.get(curTeam);
//         if ( teamWins + teamRemains <= wins[teamID] && team != curTeam) {
//            Bag<String> trivialCert = new Bag<String>();
//            trivialCert.add(curTeam);
//            eliminatedTeams.put(team, trivialCert);
//            return true;
//         }
//      }
//      return false;
//   }

   // create flow network for specified team
   private FlowNetwork createEliminationNetwork(String team) {
      if (!teams.containsKey(team)) 
         throw new java.lang.IllegalArgumentException("No such team!");
      // calculate number of games
      int numGames = numberOfTeams() * (numberOfTeams() - 1) / 2;
      // number of vertices = number of games + number of teams + source + target
      int numVerticies = numGames + numberOfTeams() + 2;
      // source = first ID
      int source = 0; 
      // target = last ID
      int target = numVerticies - 1;
      FlowNetwork flowNetwork = new FlowNetwork(numVerticies);
      int teamID = teams.get(team);
      
      int gamesVertex = 1;
      for (int gamesRow = 0; gamesRow < games.length; gamesRow++) {
         for (int gamesCol = gamesRow + 1; gamesCol < games[gamesRow].length; gamesCol++) {
            // add edges from source to games vertices
            flowNetwork.addEdge(new FlowEdge(source, gamesVertex, games[gamesRow][gamesCol]));
            // add edges from games to teams vertices
            flowNetwork.addEdge(new FlowEdge(gamesVertex, numGames + gamesRow + 1, Double.POSITIVE_INFINITY));
            flowNetwork.addEdge(new FlowEdge(gamesVertex, numGames + gamesCol + 1, Double.POSITIVE_INFINITY));
            gamesVertex++;
         }
         // add edges from teams to target
         int teamVertex = numGames + gamesRow + 1;
         flowNetwork.addEdge(new FlowEdge(teamVertex, target, Math.max(0, wins[teamID] + remain[teamID]
               - wins[gamesRow] ) ) );
      }
      return flowNetwork;
   }
   
   private void addElimnationCertificate(FlowNetwork flowNetwork, FordFulkerson fordFulkerson, String team) {
      Bag<String> certificate = new Bag<String>();
      int numGames = numberOfTeams() * (numberOfTeams() - 1) / 2;
      
      for (String curTeam : teams() ) {
         int teamID = teams.get(curTeam);
         int teamVertexFlowNetwork = numGames + 1 + teamID;
         // in mincut
         if (fordFulkerson.inCut(teamVertexFlowNetwork) ) {
            certificate.add(curTeam);
         }
      }
      // add certificate
      eliminatedTeams.put(team, certificate);
   }
   
   // subset R of teams that eliminates given team; null if not eliminated
   public Iterable<String> certificateOfElimination(String team) {
      if (!teams.containsKey(team)) 
         throw new java.lang.IllegalArgumentException("No such team!");
      if (!eliminatedTeams.containsKey(team) ) return null;
      // if team wasn't checked
      if (eliminatedTeams.get(team) == null) { 
         isEliminated(team);
         return certificateOfElimination(team);
      }
      else return eliminatedTeams.get(team);
   } 

   // Test client
   public static void main(String[] args) {
      BaseballElimination division = new BaseballElimination(args[0]);
      // test flow network
      if (args.length != 1) {
         division.testParseInput();
         String team = args[1];
         FlowNetwork fn = division.createEliminationNetwork(team);
         System.out.println("FlowNetwork: ");
         System.out.println(fn.toString());
      }
      for (String team : division.teams()) {
          if (division.isEliminated(team)) {
              StdOut.print(team + " is eliminated by the subset R = { ");
              for (String t : division.certificateOfElimination(team))
                  StdOut.print(t + " ");
              StdOut.println("}");
          }
          else {
              StdOut.println(team + " is not eliminated");
          }
      }
  }
   
}
