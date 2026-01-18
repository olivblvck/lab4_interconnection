package pl.uni.graphs;

//Configuration presets for different epidemic scenarios
public class EpidemicConfigPresets {

    //PRESET 1: Aggressive Epidemic - Infects all people
    // High transmission, slow recovery
    public static class AggressiveEpidemic {
        public static final double INFECTION_PROBABILITY = 0.25;   // 25% chance to infect each neighbor
        public static final double RECOVERY_PROBABILITY = 0.02;
        public static final int IMMUNITY_DURATION = 25;

    }

    // PRESET 2: Moderate Epidemic - Partial infection
    // Medium transmission, faster recovery
    public static class ModerateEpidemic {
        public static final double INFECTION_PROBABILITY = 0.12;   // 12% chance to infect
        public static final double RECOVERY_PROBABILITY = 0.08;
        public static final int IMMUNITY_DURATION = 35;
    }

    // PRESET 3: Weak Epidemic - Dies out quickly
    // Low transmission, very fast recovery, strong immunity
    public static class WeakEpidemic {
        public static final double INFECTION_PROBABILITY = 0.06;   // 6% chance to infect
        public static final double RECOVERY_PROBABILITY = 0.15;
        public static final int IMMUNITY_DURATION = 60;
    }

    // PRESET 4: Endemic Disease - Never stops (Category iii)
    // Moderate transmission, very slow recovery, short immunity. Disease becomes persistent in population
    public static class EndemicEpidemic {
        public static final double INFECTION_PROBABILITY = 0.18;   // 18% chance to infect
        public static final double RECOVERY_PROBABILITY = 0.03;
        public static final int IMMUNITY_DURATION = 15;
    }

    // PRESET 5: Laboratory Defaults
    // Parameters similar to example: pr=0.05
    public static class LabDefaults {
        public static final double INFECTION_PROBABILITY = 0.15;
        public static final double RECOVERY_PROBABILITY = 0.05;
        public static final int IMMUNITY_DURATION = 30;
    }

    // PRESET 6: Highly Contagious - COVID-like behavior
    // Very high transmission, variable recovery based on immunity waning. Models reinfection possibility
    public static class HighlyContagious {
        public static final double INFECTION_PROBABILITY = 0.30;
        public static final double RECOVERY_PROBABILITY = 0.04;
        public static final int IMMUNITY_DURATION = 20;
    }

    // PRESET 7: Isolated Clusters - Localized spread
    // Low transmission, high recovery,  Good for von Neumann neighborhood where spread is slower
    public static class IsolatedClusters {
        public static final double INFECTION_PROBABILITY = 0.08;
        public static final double RECOVERY_PROBABILITY = 0.12;
        public static final int IMMUNITY_DURATION = 50;
    }

    // PRESET 8: Realistic SIR Model
    // Parameters based on typical flu/measles dynamics
    public static class RealisticFlu {
        public static final double INFECTION_PROBABILITY = 0.20;   // 20% per contact
        public static final double RECOVERY_PROBABILITY = 0.10;
        public static final int IMMUNITY_DURATION = 365;
        public static final int IMMUNITY_DURATION_REDUCED = 40;
    }

    // PRESET 9: FullOutbreakThenStop - Type (i)
// High transmission + long immunity (no reinfections during the run)
    public static class FullOutbreakThenStop {
        public static final double INFECTION_PROBABILITY = 0.25; // pi
        public static final double RECOVERY_PROBABILITY = 0.08;  // pr
        public static final int IMMUNITY_DURATION = 600;         // T > MAX_ITERATIONS (500)
    }



}