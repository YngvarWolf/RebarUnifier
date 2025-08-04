public class Output {

    public void printResult(CalculationResult result) {
        if (result == null) {
            System.out.println("Не удалось подобрать арматуру для указанной зоны.");
            return;
        }

        System.out.println("Результаты расчета:");
        System.out.println("----------------------------");
        System.out.println(result);
        System.out.println("----------------------------");
    }

    public void printAllResults(CalculationResult... results) {
        System.out.println("===== Итоговый отчет =====");
        for (CalculationResult result : results) {
            printResult(result);
        }
    }
}