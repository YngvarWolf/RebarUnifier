import java.util.List;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Input input = new Input();
        Output output = new Output();
        GOSTCalculator calculator = new GOSTCalculator();

        double lengthPlate = input.getPlate("Введите длину плиты (м): ");
        double widthPlate = input.getPlate("Введите ширину плиты (м): ");

        double inPutTopX = input.getRebar("Верхняя арматура по X: ");
        double inPutBottomX = input.getRebar("Нижняя арматура по X: ");
        double inPutTopY = input.getRebar("Верхняя арматура по Y: ");
        double inPutBottomY = input.getRebar("Нижняя арматура по Y: ");

        boolean useStock = input.getYesNo("Требуется процент запаса арматуры?: ");
        double percent = useStock ? input.getPercent("Введите процент запаса: ") : 0;

        boolean useStepRounding = input.getYesNo("Требуется ли кратность шага?: ");

        double topX = inPutTopX * (1 + percent / 100);
        double bottomX = inPutBottomX * (1 + percent / 100);
        double topY = inPutTopY * (1 + percent / 100);
        double bottomY = inPutBottomY * (1 + percent / 100);

        CalculationResult resultTopX = calculator.calculateResult(topX, "Верхняя арматура по X", useStepRounding);
        CalculationResult resultBottomX = calculator.calculateResult(bottomX, "Нижняя арматура по X", useStepRounding);
        CalculationResult resultTopY = calculator.calculateResult(topY, "Верхняя арматура по Y", useStepRounding);
        CalculationResult resultBottomY = calculator.calculateResult(bottomY, "Нижняя арматура по Y", useStepRounding);

        output.printAllResults(resultTopX, resultBottomX, resultTopY, resultBottomY);

        boolean unificationRebar = input.getYesNo("Требуется ли унификация?");
        boolean unificationByDiameter = false;
        String unificationZoneChoice = "всё";
        List<CalculationResult> allResults = List.of(resultTopX, resultBottomX, resultTopY, resultBottomY);

        if (unificationRebar) {
            unificationByDiameter = input.getYesNo("Унификация по диаметру?");
//            boolean unificationByStep = input.getYesNo("Унификация по шагу? (не реализовано)");

            if (unificationByDiameter) {
                System.out.print("Унификация по арматуре: введите 'верх', 'низ' или 'всё': ");
                unificationZoneChoice = scanner.next().trim().toLowerCase();

                List<CalculationResult> unifiedResults =
                        calculator.unifyResultsByDiameter(allResults, unificationZoneChoice);

                System.out.println("===== Результаты после унификации =====");
                output.printAllResults(unifiedResults.toArray(new CalculationResult[0]));

                if (unifiedResults.size() == 4) {
                    resultTopX = unifiedResults.get(0);
                    resultBottomX = unifiedResults.get(1);
                    resultTopY = unifiedResults.get(2);
                    resultBottomY = unifiedResults.get(3);
                }
            }

//            boolean useReinforcement = input.getYesNo("Требуется ли усиление?: ");
//            double reinforcementPercent = useReinforcement ? input.getPercent("Процент усиления: ") : 0;
        }
    }
}