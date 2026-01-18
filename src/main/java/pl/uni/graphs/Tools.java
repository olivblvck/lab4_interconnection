package pl.uni.graphs;

import java.io.IOException;
import org.graphstream.graph.implementations.SingleGraph;
import java.util.Scanner;

public class Tools {

    public static SingleGraph readGraph(String filename) {
        SingleGraph graph = new SingleGraph("" + filename);
        try {
            graph.read(filename);
        } catch (Exception e) {
            System.out.println("Error while reading savedgraph.dgs\n" + e);
        }
        return graph;
    }


    public final static void pause(long delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }


    public final static void hitakey(String message) {
        System.out.println("-----------------------");
        System.out.println(message);
        System.out.println("----- Press Enter to continue -----");

        try {
            System.in.read();
        } catch (IOException ioe) { }
    }



}
