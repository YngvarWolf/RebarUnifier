import java.util.ArrayList;
import java.util.List;

public class GOSTCalculator {
    int[] diameters = {3, 4, 5, 6, 8, 10, 12, 14, 16, 18, 20, 22, 25, 28, 32, 36, 40}; // диаметр арматуры Ø
    int[] steps = {1000, 500, 333, 250, 200, 167, 143, 125, 111, 100, 91, 83};
    double[] weightPlate = {0.0550, 0.0990, 0.1540, 0.2220, 0.3950, 0.6170, 0.8880, 1.2100,
            1.5800, 2.0000, 2.4700, 2.9800, 3.8500, 4.8300, 6.3100, 7.9900, 9.8700}; // масса 1 метра арматуры мм²/м Ø мм

    double[][] crossSectionalArea = {
            {0.071, 0.14, 0.21, 0.28, 0.35, 0.42, 0.49, 0.57, 0.64, 0.71, 0.78, 0.85},
            {0.126, 0.25, 0.38, 0.50, 0.63, 0.75, 0.88, 1.01, 1.13, 1.26, 1.38, 1.51},
            {0.196, 0.39, 0.59, 0.79, 0.98, 1.18, 1.37, 1.57, 1.77, 1.96, 2.16, 2.36},
            {0.283, 0.57, 0.85, 1.13, 1.41, 1.70, 1.98, 2.26, 2.54, 2.83, 3.11, 3.39},
            {0.503, 1.01, 1.51, 2.01, 2.51, 3.02, 3.52, 4.02, 4.52, 5.03, 5.53, 6.03},
            {0.785, 1.57, 2.36, 3.14, 3.93, 4.71, 5.50, 6.28, 7.07, 7.85, 8.64, 9.42},
            {1.131, 2.26, 3.39, 4.52, 5.65, 6.79, 7.92, 9.05, 10.18, 11.31, 12.44, 13.57},
            {1.539, 3.08, 4.62, 6.16, 7.7, 9.24, 10.78, 12.32, 13.85, 15.39, 16.93, 18.47},
            {2.011, 4.02, 6.03, 8.04, 10.05, 12.06, 14.07, 16.08, 18.10, 20.11, 22.12, 24.13},
            {2.545, 5.09, 7.63, 10.18, 12.72, 15.27, 17.81, 20.36, 22.90, 25.45, 27.99, 30.54},
            {3.142, 6.28, 9.42, 12.57, 15.71, 18.85, 21.99, 25.13, 28.27, 31.42, 34.56, 37.70},
            {3.801, 7.60, 11.40, 15.21, 19.01, 22.81, 26.61, 30.41, 34.21, 38.01, 41.81, 45.62},
            {4.909, 9.82, 14.73, 19.63, 24.54, 29.45, 34.36, 39.27, 44.18, 49.09, 54.00, 58.90},
            {6.158, 12.32, 18.47, 24.63, 30.79, 36.95, 43.10, 49.26, 55.42, 61.58, 67.73, 73.89},
            {8.042, 16.08, 24.13, 32.17, 40.21, 48.25, 56.30, 64.34, 72.38, 80.42, 88.47, 96.51},
            {10.179, 20.36, 30.54, 40.72, 50.89, 61.07, 71.25, 81.43, 91.61, 101.79, 111.97, 122.15},
            {12.566, 25.13, 37.70, 50.27, 62.83, 75.40, 87.96, 100.53, 113.10, 125.66, 138.23, 150.80}
    };

    public GOSTMatch findGreaterOrEqual(double requiredArea, boolean onlyStep100) {
        double nextLarger = Double.MAX_VALUE;
        GOSTMatch match = null;
        double EPSILON = 1e-4;

        for (int i = 0; i < crossSectionalArea.length; i++) {
            for (int j = 0; j < crossSectionalArea[i].length; j++) {
                if (onlyStep100 && steps[j] % 100 != 0) continue;

                double current = crossSectionalArea[i][j];

                if (Math.abs(current - requiredArea) < EPSILON) {
                    return new GOSTMatch(current, i, j);
                }

                if (current > requiredArea && current < nextLarger) {
                    nextLarger = current;
                    match = new GOSTMatch(current, i, j);
                }
            }
        }
        return match;
    }

    public int findStep(GOSTMatch match) {
        if (match == null || match.colIndex >= steps.length) return -1;
        return steps[match.colIndex];
    }

    public double findWeight(GOSTMatch match) {
        if (match == null || match.rowIndex >= weightPlate.length || match.colIndex >= steps.length)
            return -1;

        int stepsColl = steps[match.colIndex];
        double weightPerMeter = weightPlate[match.rowIndex];

        return stepsColl * weightPerMeter;
    }

    public int findDiameter(GOSTMatch match) {
        if (match == null || match.rowIndex >= diameters.length) return -1;
        return diameters[match.rowIndex];
    }

    public List<CalculationResult> unifyResultsByDiameter(List<CalculationResult> results, String mode) {
        List<CalculationResult> filtered = new ArrayList<>();
        for (CalculationResult r : results) {
            String zone = r.getZoneName().toLowerCase();
            if (mode.equals("всё") ||
                    (mode.equals("верх") && zone.contains("верхняя")) ||
                    (mode.equals("низ") && zone.contains("нижняя"))) {
                filtered.add(r);
            }
        }

        int maxDiameter = filtered.stream()
                .mapToInt(CalculationResult::getDiameter)
                .max()
                .orElse(-1);

        int maxRow = -1;
        for (int i = 0; i < diameters.length; i++) {
            if (diameters[i] == maxDiameter) {
                maxRow = i;
                break;
            }
        }

        List<CalculationResult> updatedResults = new ArrayList<>();
        for (CalculationResult r : results) {
            if (!filtered.contains(r)) {
                updatedResults.add(r);
                continue;
            }

            if (r.getDiameter() == maxDiameter) {
                updatedResults.add(r);
                continue;
            }

            double required = r.getRequiredArea();
            double EPSILON = 1e-4;
            GOSTMatch match = null;
            for (int j = 0; j < crossSectionalArea[maxRow].length; j++) {
                double area = crossSectionalArea[maxRow][j];
                if (area >= required - EPSILON) {
                    match = new GOSTMatch(area, maxRow, j);
                    break;
                }
            }

            if (match == null) {
                updatedResults.add(null);
            }
            else {
                int step = findStep(match);
                double weight = findWeight(match);
                double selectedArea = match.value;

                CalculationResult unified = new CalculationResult(
                        required,
                        selectedArea,
                        maxDiameter,
                        step,
                        weight,
                        r.getZoneName()
                );
                updatedResults.add(unified);
            }
        }
        return updatedResults;
    }

    public CalculationResult calculateResult(double requiredArea, String zoneName, boolean onlyStep100) {
        GOSTMatch match = findGreaterOrEqual(requiredArea, onlyStep100);
        if (match == null) return null;

        int diameter = findDiameter(match);
        int step = findStep(match);
        double weight = findWeight(match);
        double selectedArea = match.value;

        return new CalculationResult(requiredArea, selectedArea, diameter, step, weight, zoneName);
    }

    public static class GOSTMatch {
        public double value;
        public int rowIndex;
        public int colIndex;

        public GOSTMatch(double value, int rowIndex, int colIndex) {
            this.value = value;
            this.rowIndex = rowIndex;
            this.colIndex = colIndex;
        }
    }
}