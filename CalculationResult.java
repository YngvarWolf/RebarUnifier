public class CalculationResult {
    private double requiredArea;
    private double selectedArea;
    private int diameter;
    private int step;
    private double weight;
    private String zoneName;

    public CalculationResult(double requiredArea, double selectedArea, int diameter,
                             int step, double weight, String zoneName) {
        this.requiredArea = requiredArea;
        this.selectedArea = selectedArea;
        this.diameter = diameter;
        this.step = step;
        this.weight = weight;
        this.zoneName = zoneName;
    }

    public double getRequiredArea() { return requiredArea; }
    public double getSelectedArea() { return selectedArea; }
    public int getDiameter() { return diameter; }
    public int getStep() { return step; }
    public double getWeight() { return weight; }
    public String getZoneName() { return zoneName; }

    @Override
    public String toString() {
        return String.format(
                "Зона: %s\n" +
                        "Необходимая площадь: %.2f см²\n" +
                        "Подобранная площадь: %.2f см²\n" +
                        "Диаметр арматуры: Ø%d мм\n" +
                        "Шаг укладки: %d мм\n" +
                        "Общий вес: %.3f кг\n",
                zoneName, requiredArea, selectedArea, diameter, step, weight
        );
    }
}