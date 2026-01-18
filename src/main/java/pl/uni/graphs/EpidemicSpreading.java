package pl.uni.graphs;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import javax.swing.JFrame;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;



 // S - Susceptible (blue)
 //I - Infected (red)
 //R - Recovered/Immune (green)



public class EpidemicSpreading {

    private final int GRID_SIZE = 60;
    private double INFECTION_PROBABILITY = 0.15; //pi
    private double RECOVERY_PROBABILITY = 0.05; //pr
    private int IMMUNITY_DURATION = 30; //T
    private final int DELAY = 50;
    private final int MAX_ITERATIONS = 500;

    private enum NeighborhoodType { VON_NEUMANN, MOORE }

    private enum State {
        SUSCEPTIBLE(0), INFECTED(1), RECOVERED(2);
        public final int value;
        State(int value) { this.value = value; }
    }

    private Graph graph;
    private State[][] grid;
    private int[][] infectedTime;
    private int[][] recoveredTime;

    private List<Integer> susceptibleCount;
    private List<Integer> infectedCount;
    private List<Integer> recoveredCount;

    private int currentIteration;
    private org.graphstream.ui.view.Viewer viewer;

    // Stored results for chart display at the end
    private List<Integer> vonNeumannSusceptible;
    private List<Integer> vonNeumannInfected;
    private List<Integer> vonNeumannRecovered;

    private List<Integer> mooreSusceptible;
    private List<Integer> mooreInfected;
    private List<Integer> mooreRecovered;

    public EpidemicSpreading() {
        initializeGraph();
        initializeGrids();
        setupStyles();

        susceptibleCount = new ArrayList<Integer>();
        infectedCount = new ArrayList<Integer>();
        recoveredCount = new ArrayList<Integer>();
        currentIteration = 0;

        System.out.println("\n LAB 4 - EPIDEMIC SPREADING ON 2D GRID (60x60)\n");
        String presetName = choosePresetFromConsole();
        System.out.println("\nSelected preset: " + presetName +
                " | pi=" + INFECTION_PROBABILITY +
                ", pr=" + RECOVERY_PROBABILITY +
                ", T=" + IMMUNITY_DURATION);


        // Simulation 1: Von Neumann
        System.out.println("\nSIMULATION 1: VON NEUMANN NEIGHBORHOOD (4 neighbors)\n");
        runSimulation(NeighborhoodType.VON_NEUMANN);

        vonNeumannSusceptible = new ArrayList<Integer>(susceptibleCount);
        vonNeumannInfected = new ArrayList<Integer>(infectedCount);
        vonNeumannRecovered = new ArrayList<Integer>(recoveredCount);

        Tools.hitakey("Simulation 1 complete. Press enter to start simulation 2...");

        // Simulation 2: Moore
        resetSimulation();
        System.out.println("\nSIMULATION 2: MOORE NEIGHBORHOOD (8 neighbors)\n");
        runSimulation(NeighborhoodType.MOORE);

        mooreSusceptible = new ArrayList<Integer>(susceptibleCount);
        mooreInfected = new ArrayList<Integer>(infectedCount);
        mooreRecovered = new ArrayList<Integer>(recoveredCount);

        // Charts: sequential + user-controlled
        Tools.hitakey("Simulation 2 complete. Press Enter to display the Von Neumann chart...");
        JFrame vnFrame = EpidemicDataExporter.showChart(
                "VON_NEUMANN", vonNeumannSusceptible, vonNeumannInfected, vonNeumannRecovered
        );
        safeSleep(200);
        Tools.hitakey("Von Neumann chart opened. Press Enter to close it and continue...");
        if (vnFrame != null) vnFrame.dispose();

        Tools.hitakey("Press Enter to display the Moore chart...");
        JFrame mooreFrame = EpidemicDataExporter.showChart(
                "MOORE", mooreSusceptible, mooreInfected, mooreRecovered
        );
        safeSleep(200);
        Tools.hitakey("Moore chart opened. Press Enter to close it and exit...");
        if (mooreFrame != null) mooreFrame.dispose();

        System.exit(0);
    }

    private static void safeSleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    //Apply preset values to simulation parameters (pi, pr, T)
    private void applyPreset(double pi, double pr, int t) {
        INFECTION_PROBABILITY = pi;
        RECOVERY_PROBABILITY = pr;
        IMMUNITY_DURATION = t;
    }

    //Console menu for selecting a preset by number
    private String choosePresetFromConsole() {
        java.util.Scanner sc = new java.util.Scanner(System.in);

        System.out.println("\n-----PRESET SELECTION----- ");
        System.out.println("1 - AggressiveEpidemic   (pi=0.25, pr=0.02, T=25)");
        System.out.println("2 - ModerateEpidemic     (pi=0.12, pr=0.08, T=35)");
        System.out.println("3 - WeakEpidemic         (pi=0.06, pr=0.15, T=60)");
        System.out.println("4 - EndemicEpidemic      (pi=0.18, pr=0.03, T=15)");
        System.out.println("5 - LabDefaults          (pi=0.15, pr=0.05, T=30)");
        System.out.println("6 - HighlyContagious     (pi=0.30, pr=0.04, T=20)");
        System.out.println("7 - IsolatedClusters     (pi=0.08, pr=0.12, T=50)");
        System.out.println("8 - RealisticFlu         (pi=0.20, pr=0.10, T=40)"); // uses REDUCED = 40
        System.out.println("9 - FullOutbreakThenStop (pi=0.35, pr=0.04, T=600)");

        System.out.print("Enter preset number [1-9]: ");

        int choice;
        try {
            choice = Integer.parseInt(sc.nextLine().trim());
        } catch (Exception e) {
            choice = 5; // default
        }

        switch (choice) {
            case 1:
                applyPreset(
                        EpidemicConfigPresets.AggressiveEpidemic.INFECTION_PROBABILITY,
                        EpidemicConfigPresets.AggressiveEpidemic.RECOVERY_PROBABILITY,
                        EpidemicConfigPresets.AggressiveEpidemic.IMMUNITY_DURATION
                );
                return "Aggressive";
            case 2:
                applyPreset(
                        EpidemicConfigPresets.ModerateEpidemic.INFECTION_PROBABILITY,
                        EpidemicConfigPresets.ModerateEpidemic.RECOVERY_PROBABILITY,
                        EpidemicConfigPresets.ModerateEpidemic.IMMUNITY_DURATION
                );
                return "Moderate";
            case 3:
                applyPreset(
                        EpidemicConfigPresets.WeakEpidemic.INFECTION_PROBABILITY,
                        EpidemicConfigPresets.WeakEpidemic.RECOVERY_PROBABILITY,
                        EpidemicConfigPresets.WeakEpidemic.IMMUNITY_DURATION
                );
                return "Weak";
            case 4:
                applyPreset(
                        EpidemicConfigPresets.EndemicEpidemic.INFECTION_PROBABILITY,
                        EpidemicConfigPresets.EndemicEpidemic.RECOVERY_PROBABILITY,
                        EpidemicConfigPresets.EndemicEpidemic.IMMUNITY_DURATION
                );
                return "Endemic";
            case 6:
                applyPreset(
                        EpidemicConfigPresets.HighlyContagious.INFECTION_PROBABILITY,
                        EpidemicConfigPresets.HighlyContagious.RECOVERY_PROBABILITY,
                        EpidemicConfigPresets.HighlyContagious.IMMUNITY_DURATION
                );
                return "HighlyContagious";
            case 7:
                applyPreset(
                        EpidemicConfigPresets.IsolatedClusters.INFECTION_PROBABILITY,
                        EpidemicConfigPresets.IsolatedClusters.RECOVERY_PROBABILITY,
                        EpidemicConfigPresets.IsolatedClusters.IMMUNITY_DURATION
                );
                return "IsolatedClusters";
            case 8:
                applyPreset(
                        EpidemicConfigPresets.RealisticFlu.INFECTION_PROBABILITY,
                        EpidemicConfigPresets.RealisticFlu.RECOVERY_PROBABILITY,
                        EpidemicConfigPresets.RealisticFlu.IMMUNITY_DURATION_REDUCED
                );
                return "RealisticFlu";
            case 9:
                applyPreset(
                        EpidemicConfigPresets.FullOutbreakThenStop.INFECTION_PROBABILITY,
                        EpidemicConfigPresets.FullOutbreakThenStop.RECOVERY_PROBABILITY,
                        EpidemicConfigPresets.FullOutbreakThenStop.IMMUNITY_DURATION
                );
                return "FullOutbreakThenStop";
            case 5:
            default:
                applyPreset(
                        EpidemicConfigPresets.LabDefaults.INFECTION_PROBABILITY,
                        EpidemicConfigPresets.LabDefaults.RECOVERY_PROBABILITY,
                        EpidemicConfigPresets.LabDefaults.IMMUNITY_DURATION
                );
                return "LabDefaults";
        }
    }


    // initialize 2D grid graph (60x60 torus)
    private void initializeGraph() {
        graph = new SingleGraph("Epidemic Spreading Torus");
        graph.setAutoCreate(false);

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                String nodeId = i + "_" + j;
                Node node = graph.addNode(nodeId);

                // Fixed 2D coordinates
                node.addAttribute("x", j * 1.0);
                node.addAttribute("y", i * 1.0);
                node.addAttribute("ui.label", "");
            }
        }

        addVonNeumannEdges();
    }

    // Add Von Neumann neighborhood edges (4 neighbors)
    private void addVonNeumannEdges() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                String nodeId = i + "_" + j;

                // Right neighbor
                int right = (j + 1) % GRID_SIZE;
                String rightId = i + "_" + right;
                String edgeId = nodeId + "_" + rightId;
                if (!graph.getNode(nodeId).hasEdgeBetween(rightId)) {
                    graph.addEdge(edgeId, nodeId, rightId);
                }

                // Down neighbor
                int down = (i + 1) % GRID_SIZE;
                String downId = down + "_" + j;
                String edgeId2 = nodeId + "_" + downId;
                if (!graph.getNode(nodeId).hasEdgeBetween(downId)) {
                    graph.addEdge(edgeId2, nodeId, downId);
                }
            }
        }
    }

    //Switch from Von Neumann to Moore neighborhood
    private void switchToMooreNeighborhood() {
        removeAllEdges();
        addMooreEdges();
    }

    //Remove all edges from graph
    private void removeAllEdges() {
        List<String> edgesToRemove = new ArrayList<String>();
        for (Object edgeObj : graph.getEdgeSet()) {
            org.graphstream.graph.Edge edge = (org.graphstream.graph.Edge) edgeObj;
            edgesToRemove.add(edge.getId());
        }
        for (String edgeId : edgesToRemove) {
            graph.removeEdge(edgeId);
        }
    }

    // Add Moore neighborhood edges (8 neighbors including diagonals)
    private void addMooreEdges() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                String nodeId = i + "_" + j;

                for (int di = -1; di <= 1; di++) {
                    for (int dj = -1; dj <= 1; dj++) {
                        if (di == 0 && dj == 0) continue;

                        int ni = (i + di + GRID_SIZE) % GRID_SIZE;
                        int nj = (j + dj + GRID_SIZE) % GRID_SIZE;
                        String neighborId = ni + "_" + nj;

                        if (!graph.getNode(nodeId).hasEdgeBetween(neighborId)) {
                            String edgeId = nodeId + "_" + neighborId;
                            graph.addEdge(edgeId, nodeId, neighborId);
                        }
                    }
                }
            }
        }
    }

    // Initialize state grids
    private void initializeGrids() {
        grid = new State[GRID_SIZE][GRID_SIZE];
        infectedTime = new int[GRID_SIZE][GRID_SIZE];
        recoveredTime = new int[GRID_SIZE][GRID_SIZE];

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                grid[i][j] = State.SUSCEPTIBLE;
                infectedTime[i][j] = -1;
                recoveredTime[i][j] = -1;
            }
        }

        // Infect one random person
        Random rand = new Random();
        int startI = rand.nextInt(GRID_SIZE);
        int startJ = rand.nextInt(GRID_SIZE);
        grid[startI][startJ] = State.INFECTED;
        infectedTime[startI][startJ] = 0;

        System.out.println("\nInitial infected person at position: (" + startI + ", " + startJ + ")");
    }

    // Reset for next simulation
    private void resetSimulation() {
        switchToMooreNeighborhood();
        initializeGrids();
        susceptibleCount.clear();
        infectedCount.clear();
        recoveredCount.clear();
        currentIteration = 0;
    }

    // Setup graph visualization styles
    private void setupStyles() {
        String styleSheet =
                "graph { fill-color: white; }" +
                        "node { size: 8px; stroke-mode: none; text-alignment: under; }" +
                        "edge { stroke-width: 0.3px; stroke-color: #e0e0e0; }" +
                        "node.susceptible { fill-color: blue; }" +
                        "node.infected { fill-color: red; }" +
                        "node.recovered { fill-color: green; }";

        graph.addAttribute("ui.stylesheet", styleSheet);
        graph.addAttribute("ui.antialias", true);
    }

    // update node visualization color based on state
    private void updateNodeVisualization(int i, int j) {
        String nodeId = i + "_" + j;
        Node node = graph.getNode(nodeId);
        if (node == null) return;

        switch (grid[i][j]) {
            case SUSCEPTIBLE:
                node.addAttribute("ui.class", "susceptible");
                break;
            case INFECTED:
                node.addAttribute("ui.class", "infected");
                break;
            case RECOVERED:
                node.addAttribute("ui.class", "recovered");
                break;
            default:
                break;
        }
    }

    //get neighbors based on neighborhood type
    private List<int[]> getNeighbors(int i, int j, NeighborhoodType neighborhood) {
        List<int[]> neighbors = new ArrayList<int[]>();

        if (neighborhood == NeighborhoodType.VON_NEUMANN) {
            int[][] directions = new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
            for (int d = 0; d < directions.length; d++) {
                int ni = (i + directions[d][0] + GRID_SIZE) % GRID_SIZE;
                int nj = (j + directions[d][1] + GRID_SIZE) % GRID_SIZE;
                neighbors.add(new int[]{ni, nj});
            }
        } else {
            for (int di = -1; di <= 1; di++) {
                for (int dj = -1; dj <= 1; dj++) {
                    if (di == 0 && dj == 0) continue;
                    int ni = (i + di + GRID_SIZE) % GRID_SIZE;
                    int nj = (j + dj + GRID_SIZE) % GRID_SIZE;
                    neighbors.add(new int[]{ni, nj});
                }
            }
        }
        return neighbors;
    }

    // Perform one cellular automaton step
    private void simulationStep(NeighborhoodType neighborhood) {
        State[][] newGrid = new State[GRID_SIZE][GRID_SIZE];
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                newGrid[i][j] = grid[i][j];
            }
        }

        Random rand = new Random();

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (grid[i][j] == State.SUSCEPTIBLE) {
                    List<int[]> neighbors = getNeighbors(i, j, neighborhood);
                    for (int n = 0; n < neighbors.size(); n++) {
                        int[] nb = neighbors.get(n);
                        int ni = nb[0];
                        int nj = nb[1];

                        if (grid[ni][nj] == State.INFECTED) {
                            if (rand.nextDouble() < INFECTION_PROBABILITY) {
                                newGrid[i][j] = State.INFECTED;
                                infectedTime[i][j] = currentIteration;
                                break;
                            }
                        }
                    }
                } else if (grid[i][j] == State.INFECTED) {
                    int timeInfected = currentIteration - infectedTime[i][j];
                    double recoveryChance = 1.0 - Math.pow(1.0 - RECOVERY_PROBABILITY, timeInfected);
                    if (rand.nextDouble() < recoveryChance) {
                        newGrid[i][j] = State.RECOVERED;
                        recoveredTime[i][j] = currentIteration;
                    }
                } else if (grid[i][j] == State.RECOVERED) {
                    int timeSinceRecovery = currentIteration - recoveredTime[i][j];
                    if (timeSinceRecovery >= IMMUNITY_DURATION) {
                        newGrid[i][j] = State.SUSCEPTIBLE;
                        recoveredTime[i][j] = -1;
                    }
                }
            }
        }

        grid = newGrid;
        currentIteration++;
    }

    // Update statistics and visualization
    private void updateStatistics() {
        int s = 0, i = 0, r = 0;

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                updateNodeVisualization(row, col);

                switch (grid[row][col]) {
                    case SUSCEPTIBLE:
                        s++;
                        break;
                    case INFECTED:
                        i++;
                        break;
                    case RECOVERED:
                        r++;
                        break;
                    default:
                        break;
                }
            }
        }

        susceptibleCount.add(s);
        infectedCount.add(i);
        recoveredCount.add(r);
    }

    // check if epidemic has ended (no infected remain)
    private boolean epidemicEnded() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (grid[i][j] == State.INFECTED) return false;
            }
        }
        return true;
    }

    // run a complete simulation
    private void runSimulation(NeighborhoodType neighborhood) {
        System.out.println("\nStarting simulation with neighborhood: " + neighborhood);
        System.out.println("Parameters: pi=" + INFECTION_PROBABILITY +
                ", pr=" + RECOVERY_PROBABILITY +
                ", T=" + IMMUNITY_DURATION);

        System.out.println("\n---SIMULATION WINDOW OPENED ---");
        System.out.println("--- BLUE = Susceptible ; RED = Infected ; GREEN = Recovered ---\n");

        // show graph
        viewer = graph.display(false);

        int iteration = 0;
        while (iteration < MAX_ITERATIONS && !epidemicEnded()) {
            simulationStep(neighborhood);
            updateStatistics();
            Tools.pause(DELAY);

            if (iteration % 50 == 0) {
                int ss = susceptibleCount.get(susceptibleCount.size() - 1);
                int ii = infectedCount.get(infectedCount.size() - 1);
                int rr = recoveredCount.get(recoveredCount.size() - 1);
                System.out.println("  Iteration " + iteration + ": S=" + ss + " | I=" + ii + " | R=" + rr);
            }

            iteration++;
        }

        printStatistics(neighborhood);
    }

    // print detailed statistics and save csv/png
    private void printStatistics(NeighborhoodType neighborhood) {
        System.out.println("\n---SIMULATION COMPLETE---\n");
        System.out.println("Neighborhood type: " + neighborhood);
        System.out.println("Total iterations: " + currentIteration);

        if (!susceptibleCount.isEmpty()) {
            System.out.println("\n--- FINAL STATE ---");
            System.out.println("Susceptible (S): " + susceptibleCount.get(susceptibleCount.size() - 1));
            System.out.println("Infected (I):    " + infectedCount.get(infectedCount.size() - 1));
            System.out.println("Recovered (R):   " + recoveredCount.get(recoveredCount.size() - 1));

            int maxInfected = Collections.max(infectedCount);
            System.out.println("\n--- PEAK INFECTION ---");
            System.out.println("Maximum infected: " + maxInfected);
            System.out.println("At iteration: " + infectedCount.indexOf(maxInfected));

            System.out.println("\n--- EPIDEMIC CLASSIFICATION ---");
            if (susceptibleCount.get(susceptibleCount.size() - 1) == 0) {
                System.out.println("Type (i): Infected all, stopped");
            } else if (infectedCount.get(infectedCount.size() - 1) == 0) {
                System.out.println("Type (ii): Stopped, didn't infect all");
            } else {
                System.out.println("Type (iii): Never stops (endemic)");
            }

            String filename;
            String pngFilename;

            if (neighborhood == NeighborhoodType.VON_NEUMANN) {
                filename = "epidemic_vonNeumann.csv";
                pngFilename = "epidemic_vonNeumann.png";
            } else {
                filename = "epidemic_moore.csv";
                pngFilename = "epidemic_moore.png";
            }

            try {
                EpidemicDataExporter.saveToCsv(filename, susceptibleCount, infectedCount, recoveredCount);
                EpidemicDataExporter.saveChartAsPNG(pngFilename, neighborhood.toString(),
                        susceptibleCount, infectedCount, recoveredCount);
            } catch (IOException e) {
                System.err.println("Error while saving output files: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new EpidemicSpreading();
    }
}
