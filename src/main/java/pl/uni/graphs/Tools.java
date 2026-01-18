package pl.uni.graphs;

import java.io.IOException;
import org.graphstream.graph.implementations.SingleGraph;
import java.util.Scanner;

public class Tools {

    /**
     * Read the dgs file and create the SingleGraph and return it.
     * @param filename file path
     * @return graph
     */
    public static SingleGraph readGraph(String filename) {
        SingleGraph graph = new SingleGraph("" + filename);
        try {
            graph.read(filename);
        } catch (Exception e) {
            System.out.println("Error while reading savedgraph.dgs\n" + e);
        }
        return graph;
    }

    /**
     * Add a delay during the evolution of the graph (visualization).
     * @param delay milliseconds
     */
    public final static void pause(long delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Stop execution and wait for the user to hit ENTER.
     * (Kept for compatibility with the existing project code.)
     * @param message prompt text
     */
    public final static void hitakey(String message) {
        System.out.println("-----------------------");
        System.out.println(message);
        System.out.println("----- Press ENTER to continue -----");

        try {
            System.in.read();
        } catch (IOException ioe) { }
    }

    /**
     * Alternative ENTER-wait method (kept because it exists already).
     * @param message prompt text
     */
    public static void hitAKey(String message) {
        System.out.println("----- " + message + " -----");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }
}
