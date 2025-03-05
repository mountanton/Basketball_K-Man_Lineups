/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lattice_basketball;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lattice_euroleague;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeSet;

/**
 *
 * @author Michalis Mountantonakis
 * ICS FORTH and CSD UOC
 */
public class Lattice_Basketball {

    public static HashMap<String, Double> allPlayers = new HashMap<String, Double>();
    public static ArrayList<String> toPrint = new ArrayList<String>();
    public static HashMap<String, Double> top10 = new HashMap<>();
    public static ArrayList<Lineup> globalLineups = new ArrayList<Lineup>();
    public static int nodes = 0, loops = 0, OverZero = 0;
    public static double maximization = 0;
    public static TreeSet<String> maximization_roster = new TreeSet<String>();

    static BufferedWriter writer;

    /**
     * Sort the players of a team
     *
     * @param map contains for each player in how many lineups he has
     * participated.
     */
    public static HashMap<String, Double> sortPlayers(HashMap<String, Double> map) {

        List<Map.Entry<String, Double>> list = new ArrayList<>(map.entrySet());

        list.sort(Map.Entry.comparingByValue());

        LinkedHashMap<String, Double> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Double> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        TreeSet<Lineup> treeset = new TreeSet<Lineup>();
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Select the Dataset:A for Euroleague 2022-23, B for Euroleague 2023-24, C for Euroleague 2024-25, D for NBA 2023-24");
        String dataset = keyboard.next();
        String[] allTeams = {};
        String currentFile = "";
        if (dataset.equals("A")) {
            String[] seasonTeams = {"ASV", "BAY", "BER", "BSK", "CZV", "EFS", "FBD", "FCB", "MCO", "MIL", "MTA", "OLY", "PAO", "PAR", "RMB", "VBC", "VIR", "ZAL"};
            allTeams = Arrays.copyOf(seasonTeams, seasonTeams.length);
            currentFile = "euroleague_lineups_2023_all_play_by_play.txt";
        } else if (dataset.equals("B")) {
            String[] seasonTeams = {"ASM", "ASV", "BAS", "BAY", "BER", "CZB", "EFE", "FCB", "FEN", "MIL", "MTA", "OLY", "PAN", "PAR", "RMB", "VAL", "VIR", "ZAL"};
            allTeams = Arrays.copyOf(seasonTeams, seasonTeams.length);
            currentFile = "euroleague_lineups_2024_all_play_by_play.txt";
        } else if (dataset.equals("C")) {
            String[] seasonTeams = {"ASM", "ASV", "BAS", "BAY", "BER", "CZV", "EFE", "FCB", "FEN", "MIL", "MTA", "OLY", "PAB", "PAN", "PAR", "RMB", "VIR", "ZAL"};
            allTeams = Arrays.copyOf(seasonTeams, seasonTeams.length);
            currentFile = "euroleague_lineups_2025_all_play_by_play.txt";
        } else if (dataset.equals("D")) {
            String[] seasonTeams = {"ATL", "BOS", "BKN", "CHA", "CHI", "CLE", "DAL", "DEN", "DET", "GSW", "HOU", "IND", "LAC", "LAL", "MEM", "MIA", "MIL", "MIN", "NOP", "NYK", "OKC", "ORL", "PHI", "PHX", "POR", "SAC", "SAS", "TOR", "UTA", "WAS"};
            allTeams = Arrays.copyOf(seasonTeams, seasonTeams.length);
            currentFile = "nba_lineups_2024_all_play_by_play.txt";
        }

        Scanner keyboard2 = new Scanner(System.in);
        System.out.println("Select Method: Baseline, Semilattice_asc, Semilattice_desc, Semilattice_rand");
        String method = keyboard2.next();

        Scanner keyboard3 = new Scanner(System.in);
        System.out.println("Select one of this: All, Maximization");
        String example = keyboard3.next();

        String printValue = "No";
        if (example.equals("All") && !method.equals("Baseline")) {
            Scanner keyboard4 = new Scanner(System.in);
            System.out.println("To Print in File: No, All, 1-Man, 2-Man, 3-Man, 4-Man, 5-Man");
            printValue = keyboard4.next();
        }
        String printing = "No";
        if (!printValue.equals("No")) {
            printing = printValue;
        }
        long toRemove = 0;
        long start = System.currentTimeMillis();
        System.out.println(currentFile);
        if (!printing.equals("No")) {
            writer = new BufferedWriter(new FileWriter("output_" + currentFile));
            writer.write("LINEUP\tTM\tMIN\tPTS\t2PTM\t2PTA\t2PT%\t3PTM\t3PTA\t3PT%\tFGM\tFGA\tFG%\tFTM\tFTA\tFT%\tOR\tDR\tTR\tAST\tPF\tDF\tST\tTO\tBLK\tBLKA\tVAL\t+ / -\tAST_TO\tPTS_MIN\tOPPPTS_MIN\tTR_MIN\tDR_MIN\tOR_MIN\tAST_MIN\tTO_MIN\tST_MIN\tVAL_MIN\tBLK_MIN\tPLMIN_MIN");
        }
        for (String team : allTeams) {
            allPlayers = new HashMap<String, Double>();
            toPrint = new ArrayList<String>();
            maximization = 0;
            maximization_roster = new TreeSet<String>();
            long start2 = System.currentTimeMillis();
            ArrayList<Lineup> lineups = readFile(currentFile, team);
            long finish2 = System.currentTimeMillis();
            long timeElapsed2 = finish2 - start2;
            toRemove += timeElapsed2;

            HashMap<String, Double> sortedMap = sortPlayers(allPlayers);
            ArrayList<String> teamPlayers = new ArrayList<String>();
            for (String k : sortedMap.keySet()) {
                teamPlayers.add(k);
            }

            if (method.equals("Baseline")) {
                globalLineups = lineups;
                baseline(teamPlayers, new ArrayList<String>(), 0, 1);
            } else if (method.equals("Semilattice_asc")) {
                recursiveSemiLattice(teamPlayers, new ArrayList<String>(), 0, 1, lineups, printing);
            } else if (method.equals("Semilattice_desc")) {
                Collections.reverse(teamPlayers);
                recursiveSemiLattice(teamPlayers, new ArrayList<String>(), 0, 1, lineups, printing);
            } else if (method.equals("Semilattice_rand")) {
                Collections.shuffle(teamPlayers);
                recursiveSemiLattice(teamPlayers, new ArrayList<String>(), 0, 1, lineups, printing);
            }
            if (example.equals("Maximization")) {
                System.out.println("Best K-Man with most points per minute for " + team + "\t" + maximization_roster + "\t" + maximization);
            }
        }
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("Computation Time:" + (timeElapsed - toRemove));
        System.out.println("Computation Statistics Visited K-Man:" + nodes + " Iterations:" + loops + " Avg Iteration:" + (double) loops / (double) nodes);
        if (!printing.equals("No")) {
            writer.close();
        }
    }

    /**
     * Read the players and statistics of a team
     *
     * @param filePath the path containing the lineups
     * @param team the team for the lineups
     */
    public static ArrayList<Lineup> readFile(String filePath, String team) throws IOException {
        StringBuilder content = new StringBuilder();
        ArrayList<Lineup> allLineups = new ArrayList<Lineup>();
        // Use try-with-resources to automatically close the BufferedReader
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int i = 0;
            String[] namesStats = null;
            while ((line = br.readLine()) != null) {
                Lineup l = new Lineup();
                String[] split = line.split("\t");
                if (i == 0) {
                    namesStats = split;
                    for (int j = 0; j <= 27; j++) {
                        if (!split[j].equals("RNK") && !split[j].equals("LINEUP") && !split[j].equals("GP") && !split[j].equals("W") && !split[j].equals("L") && !split[j].equals("TM")) {
                            toPrint.add(split[j]);
                        }
                    }
                    toPrint.add("AST_TO");
                    toPrint.add("PTS_MIN");
                    toPrint.add("OPPPTS_MIN");
                    toPrint.add("TR_MIN");
                    toPrint.add("DR_MIN");
                    toPrint.add("OR_MIN");
                    toPrint.add("AST_MIN");
                    toPrint.add("TO_MIN");
                    toPrint.add("ST_MIN");
                    toPrint.add("VAL_MIN");
                    toPrint.add("BLK_MIN");
                    toPrint.add("PLMIN_MIN");

                    i++;
                    continue;
                }
                if (!split[1].equals(team)) {
                    continue;
                } else if (i == 1) {
                    l.setTeam(split[1]);
                    i++;
                }
                String names = split[0];
                for (String str : names.split(",")) {
                    l.lineup.add(str.trim());
                    if (!allPlayers.containsKey(str.trim())) {
                        allPlayers.put(str.trim(), 1.0);
                    } else {
                        allPlayers.put(str.trim(), allPlayers.get(str.trim()) + 1.0);
                    }
                }
                l.setStats(namesStats, split);
                l.setTeam(team);
                allLineups.add(l);
                content.append(line).append(System.lineSeparator());
            }
        }

        return allLineups;
    }

    /**
     * The baseline model for computing the experiments
     *
     * @param allPlayers all the players of the team
     * @param players the current subset of players
     * @param size the current order
     * @param level the current level (K)
     */
    public static void baseline(ArrayList<String> allPlayers, ArrayList<String> players, int size, int level) {
        if (level == 1) {
            int i = 0;
            for (; i < allPlayers.size(); i++) {
                nodes++;
                ArrayList<String> newPlayers = new ArrayList<String>();
                newPlayers.add(allPlayers.get(i));
                double points = 0;
                Lineup newLineup = new Lineup();
                newLineup.lineup.addAll(newPlayers);

                for (Lineup l : globalLineups) {
                    loops++;
                    TreeSet<String> check = new TreeSet<>(l.lineup);
                    check.retainAll(newPlayers);
                    if (check.size() == newPlayers.size()) {
                        newLineup.increaseStats(l.allStats);

                    }
                }

                newLineup.setPercentages();
                //newLineup.printStats(toPrint);
                size = i + 1;
                baseline(allPlayers, newPlayers, size, 2);
            }
        }

        for (int i = size; i < allPlayers.size(); i++) {
            nodes++;
            ArrayList<String> newPlayers = new ArrayList(players);
            newPlayers.add(allPlayers.get(i));
            double points = 0;
            Lineup newLineup = new Lineup();
            newLineup.lineup.addAll(newPlayers);
            for (Lineup l : globalLineups) {
                loops++;
                TreeSet<String> check = new TreeSet<>(l.lineup);
                check.retainAll(newPlayers);
                if (check.size() == newPlayers.size()) {
                    newLineup.increaseStats(l.allStats);
                }
            }
            if (newLineup.allStats.get("MIN") != null && maximization < (newLineup.allStats.get("PTS") / newLineup.allStats.get("MIN")) && newLineup.allStats.get("MIN") >= 80 && newPlayers.size() > 1) {
                maximization = newLineup.allStats.get("PTS") / newLineup.allStats.get("MIN"); // newLineup.allStats.get("TO");
                maximization_roster = newLineup.lineup;
            }
            size++;
            level++;
            if (newPlayers.size() < 5) {
                baseline(allPlayers, newPlayers, size, level);
            }

        }

    }

    /**
     * The baseline model for computing the experiments
     *
     * @param allPlayers all the players of the team
     * @param players the current subset of players
     * @param size the current order
     * @param level the current level (K)
     * @param lineups the lineups that are transferred to the next level
     * @param printing if we need to print in file and for which K or all
     */
    public static void recursiveSemiLattice(ArrayList<String> allPlayers, ArrayList<String> players, int size, int level, ArrayList<Lineup> lineups, String printing) throws IOException {

        for (int i = size; i < allPlayers.size(); i++) {
            nodes++;
            ArrayList<String> newPlayers = new ArrayList(players);
            newPlayers.add(allPlayers.get(i));
            Lineup newLineup = new Lineup();
            newLineup.lineup.addAll(newPlayers);
            ArrayList<Lineup> newLineups = new ArrayList<Lineup>();

            for (Lineup l : lineups) {
                loops++;
                TreeSet<String> check = new TreeSet<>(l.lineup);
                check.retainAll(newPlayers);
                if (check.size() == newPlayers.size()) {
                    newLineup.increaseStats(l.allStats);
                    newLineup.setTeam(l.team);
                    newLineups.add(l);
                }
            }
            if (newLineup.allStats.get("MIN") != null) {
                newLineup.setPercentages();
            }
            if (newLineups.size() > 0 && maximization < (newLineup.allStats.get("PTS") / newLineup.allStats.get("MIN")) && newLineup.allStats.get("MIN") >= 80 && newPlayers.size() > 1) {
                maximization = newLineup.allStats.get("PTS") / newLineup.allStats.get("MIN"); // newLineup.allStats.get("TO");
                maximization_roster = newLineup.lineup;
            }
            if (newLineups.size() > 0 && !printing.equals("No")) {
                if (printing.equals("All") || (printing.equals("1-Man") && newPlayers.size() == 1) || (printing.equals("2-Man") && newPlayers.size() == 2) || (printing.equals("3-Man") && newPlayers.size() == 3 || (printing.equals("4-Man") && newPlayers.size() == 4) || (printing.equals("5-Man") && newPlayers.size() == 5))) {
                    writer.write(newLineup.returnStats(toPrint));
                }

            }
            size++;
            level++;
            if (newPlayers.size() < 5 && newLineups.size() > 0) {
                recursiveSemiLattice(allPlayers, newPlayers, size, level, newLineups, printing);
            }

        }

    }

}
