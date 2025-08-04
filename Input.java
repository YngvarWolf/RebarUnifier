import java.util.Scanner;

public class Input {
    private Scanner scanner = new Scanner(System.in);

    private double getPositiveDouble(String message) {
        double value;
        while (true) {
            System.out.print(message);
            try {
                value = Double.parseDouble(scanner.next());
                if (value > 0) {
                    return value;
                } else {
                    System.out.println("Ошибка: значение должно быть больше 0. Повторите ввод.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите числовое значение.");
            }
        }
    }

    public double getRebar(String message) {
        return getPositiveDouble(message);
    }

    public double getPlate(String message) {
        return getPositiveDouble(message);
    }

    public double getPercent(String message) {
        return getPositiveDouble(message);
    }

    public boolean getYesNo(String message) {
        System.out.print(message + " [да/нет]: ");
        String input = scanner.next().trim().toLowerCase();
        return input.equals("да");
    }
}