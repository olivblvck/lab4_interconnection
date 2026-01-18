# Lab 4 — Epidemic Spreading on a 2D Torus (SIR Cellular Automaton)

This project implements an SIR-style epidemic spreading model on a 60×60 2D **torus** using a cellular automaton and GraphStream visualization. [file:45][file:42]  
At each time step, infected agents may infect neighbors with probability `pi`, recover with probability `pr` (cumulative recovery probability \(1-(1-pr)^k\) after `k` steps), and recovered agents stay immune for `T` steps before becoming susceptible again (reinfection is possible). [file:45][file:42]

## Features
- 2D torus grid (60×60) with two neighborhood types: **Von Neumann** (4 neighbors) and **Moore** (8 neighbors). [file:45][file:42]
- Live visualization (GraphStream): **blue** = Susceptible, **red** = Infected, **green** = Recovered. [file:45][file:42]
- CSV export + PNG plots of S(t), I(t), R(t) generated with XChart. [file:42][file:25]
- Automatic epidemic classification at the end of each run:
    - Type (i): infects all people and finally stops (S=0, I=0)
    - Type (ii): stops before infecting all (I=0, S>0)
    - Type (iii): never stops (endemic; I>0 at the end)
      [file:45][file:42]

## How to run
Run the main class:
- `pl.uni.graphs.EpidemicSpreading` [file:42]

The program asks you to pick a preset (pi, pr, T) from the console, then runs **two simulations** with the same parameters:
1) Von Neumann neighborhood
2) Moore neighborhood [file:42][file:45]

Outputs (saved in the project directory):
- CSV: `epidemic_vonNeumann.csv`, `epidemic_moore.csv` [file:42][file:25]
- PNG: `epidemic_vonNeumann.png`, `epidemic_moore.png` [file:42][file:25]

## Results (plots in-table)

| Preset | Parameters (pi, pr, T) | Von Neumann (classification + plot)                                                                                                    | Moore (classification + plot)                                                                                    |
|---|---:|----------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------|
| 2 — ModerateEpidemic | (0.12, 0.08, 35) | Type (ii) — stops with S>0. <br><img src="readme_files/epidemic_vonNeumann_preset2.png" width="420">                                   | Type (ii) — stops with S>0. <br><img src="readme_files/epidemic_moore_preset2.png" width="420">                  |
| 4 — EndemicEpidemic | (0.18, 0.03, 15) | Type (iii) — endemic (I>0). <br><img src="readme_files/epidemic_vonNeumann_preset4.png" width="420">                                   | Type (iii) — endemic (I>0). <br><img src="readme_files/epidemic_moore_preset4.png" width="420">                  |
| 9 — FullOutbreakThenStop | (0.25, 0.08, 600) | Type (ii) — often stops with a small leftover S (stochastic). <br><img src="readme_files/epidemic_vonNeumann_preset9.png" width="420"> | Type (i) — infects all and stops (S=0, I=0). <br><img src="readme_files/epidemic_moore_preset9.png" width="420"> |

Notes:
- Moore often spreads faster/stronger than Von Neumann because each cell has more contacts per step. [file:45]
- Results are stochastic (random “patient zero” and random transmission/recovery events), so rerunning a preset may occasionally change the final classification. [file:45][file:42]

## Implementation notes
- Von Neumann neighborhood is implemented with 4-neighbor edges (up/down/left/right) with torus wrapping, while Moore adds diagonals (8 neighbors). [file:42]
- The model records S/I/R counts each iteration, saves them to CSV, and generates the PNG charts via XChart. [file:42][file:25]
