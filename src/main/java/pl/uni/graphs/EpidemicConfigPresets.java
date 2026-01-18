package pl.uni.graphs;

//Configuration presets for different epidemic scenarios
public class EpidemicConfigPresets {

    /**
     * PRESET 1: Aggressive Epidemic - Infects all people (Category i)
     * High transmission, slow recovery
     * Epidemic burns out after infecting most/all of population
     */
    public static class AggressiveEpidemic {
        public static final double INFECTION_PROBABILITY = 0.25;   // 25% chance to infect each neighbor
        public static final double RECOVERY_PROBABILITY = 0.02;    // Very slow recovery
        public static final int IMMUNITY_DURATION = 25;            // Moderate immunity

        // Expected outcome: Epidemic curve rises to peak, then falls to zero
        // Final: All or almost all infected then recovered
        // Duration: ~150-200 iterations
    }

    /**
     * PRESET 2: Moderate Epidemic - Partial infection (Category ii)
     * Medium transmission, faster recovery
     * Epidemic dies out before infecting entire population
     */
    public static class ModerateEpidemic {
        public static final double INFECTION_PROBABILITY = 0.12;   // 12% chance to infect
        public static final double RECOVERY_PROBABILITY = 0.08;    // Moderate recovery
        public static final int IMMUNITY_DURATION = 35;            // Longer immunity

        // Expected outcome: Epidemic peaks early then declines
        // Final: Some susceptible remain, no infected
        // Duration: ~80-120 iterations
    }

    /**
     * PRESET 3: Weak Epidemic - Dies out quickly (Category ii)
     * Low transmission, very fast recovery, strong immunity
     * Epidemic cannot sustain itself
     */
    public static class WeakEpidemic {
        public static final double INFECTION_PROBABILITY = 0.06;   // 6% chance to infect
        public static final double RECOVERY_PROBABILITY = 0.15;    // Very fast recovery
        public static final int IMMUNITY_DURATION = 60;            // Very long immunity

        // Expected outcome: Quick spike then immediate decline
        // Final: Many susceptible remain, few recovered
        // Duration: ~30-50 iterations
    }

    /**
     * PRESET 4: Endemic Disease - Never stops (Category iii)
     * Moderate transmission, very slow recovery, short immunity
     * Disease becomes persistent in population
     * Susceptible regain status quickly after immunity expires
     */
    public static class EndemicEpidemic {
        public static final double INFECTION_PROBABILITY = 0.18;   // 18% chance to infect
        public static final double RECOVERY_PROBABILITY = 0.03;    // Very slow recovery
        public static final int IMMUNITY_DURATION = 15;            // Very short immunity (reinfection quickly)

        // Expected outcome: Oscillating pattern
        // Susceptible -> Infected -> Recovered -> Susceptible again
        // Final: Persistent infection, never reaches zero infected
        // Duration: Runs for full 500 iterations with waves
    }

    /**
     * PRESET 5: Laboratory Defaults (from PDF example)
     * Parameters similar to example: pr=0.05
     * This is the "recommended starting point" mentioned in PDF
     */
    public static class LabDefaults {
        public static final double INFECTION_PROBABILITY = 0.15;
        public static final double RECOVERY_PROBABILITY = 0.05;    // As mentioned in PDF
        public static final int IMMUNITY_DURATION = 30;

        // From PDF: after 50 steps with pr=0.05, recovery prob = 0.92
        // This preset should show mixed results
    }

    /**
     * PRESET 6: Highly Contagious - COVID-like behavior
     * Very high transmission, variable recovery based on immunity waning
     * Models reinfection possibility
     */
    public static class HighlyContagious {
        public static final double INFECTION_PROBABILITY = 0.30;   // Very contagious
        public static final double RECOVERY_PROBABILITY = 0.04;    // Slow recovery
        public static final int IMMUNITY_DURATION = 20;            // Immunity wanes quickly

        // Expected outcome: Multiple waves of infection
        // Disease never fully eliminated
        // High proportion always susceptible due to waning immunity
    }

    /**
     * PRESET 7: Isolated Clusters - Localized spread
     * Low transmission, high recovery, long immunity
     * Epidemic remains localized, doesn't spread far
     * Good for von Neumann neighborhood where spread is slower
     */
    public static class IsolatedClusters {
        public static final double INFECTION_PROBABILITY = 0.08;   // Low transmission
        public static final double RECOVERY_PROBABILITY = 0.12;    // Quick recovery
        public static final int IMMUNITY_DURATION = 50;            // Long immunity

        // Expected outcome: Small clusters of infection
        // Many uninfected regions remain
        // Multiple separate outbreak waves in different areas
    }

    /**
     * PRESET 8: Realistic SIR Model
     * Parameters based on typical flu/measles dynamics
     * Balanced transmission and recovery
     */
    public static class RealisticFlu {
        public static final double INFECTION_PROBABILITY = 0.20;   // 20% per contact
        public static final double RECOVERY_PROBABILITY = 0.10;    // Average 10 days to recover
        public static final int IMMUNITY_DURATION = 365;           // 1 year immunity (in time units)

        // Note: IMMUNITY_DURATION should be scaled appropriately
        // If each step = 1 day, then 365 = 1 year
        // For CA simulation, reduced to reasonable value:
        public static final int IMMUNITY_DURATION_REDUCED = 40;    // ~1-2 months equivalent
    }


}