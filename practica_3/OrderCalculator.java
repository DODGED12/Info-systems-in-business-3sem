import java.util.Locale;
import java.util.Scanner;

public class OrderCalculator {

    private static final int MAX_QUANTITY = 10_000;
    private static final double MAX_PRICE = 5_000_000.0;
    private static final double MAX_DISCOUNT = 30.0;
    private static final double VAT_RATE = 10.0;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            System.out.print("Количество единиц: ");
            int quantity = Integer.parseInt(readValue(sc));
            System.out.print("Цена за единицу (руб.): ");
            double price = Double.parseDouble(readValue(sc).replace(',', '.'));
            System.out.print("Скидка (%): ");
            double discount = Double.parseDouble(readValue(sc).replace(',', '.'));

            String error = errorMessage(quantity, price, discount);
            if (!error.isEmpty()) {
                System.out.println("Ошибка: " + error);
                return;
            }
            System.out.println(buildReport(quantity, price, discount));
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: значение должно быть числом (количество - целым).");
        } catch (IllegalStateException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static String readValue(Scanner sc) {
        if (!sc.hasNextLine()) {
            throw new IllegalStateException("значение не введено");
        }
        String text = sc.nextLine().trim();
        if (text.isEmpty()) {
            throw new IllegalStateException("значение не введено");
        }
        return text;
    }

    static String errorMessage(int quantity, double price, double discount) {
        if (quantity <= 0 || quantity > MAX_QUANTITY) {
            return "количество должно быть от 1 до " + MAX_QUANTITY;
        }
        if (Double.isNaN(price) || price <= 0 || price > MAX_PRICE) {
            return "цена должна быть больше 0 и не больше 5000000 руб.";
        }
        if (Double.isNaN(discount) || discount < 0 || discount > MAX_DISCOUNT) {
            return "скидка должна быть от 0 до 30 %";
        }
        return "";
    }

    static boolean isValid(int quantity, double price, double discount) {
        return errorMessage(quantity, price, discount).isEmpty();
    }

    static double calculateBase(int quantity, double price) {
        return round2(quantity * price);
    }

    static double applyDiscount(double base, double discountPercent) {
        return round2(base - base * discountPercent / 100.0);
    }

    static double calculateVat(double amountAfterDiscount) {
        return round2(amountAfterDiscount * VAT_RATE / 100.0);
    }

    static String buildReport(int quantity, double price, double discount) {
        double base = calculateBase(quantity, price);
        double afterDiscount = applyDiscount(base, discount);
        double vat = calculateVat(afterDiscount);
        double total = round2(afterDiscount + vat);
        return String.format(Locale.US,
                "Базовая стоимость: %.2f руб.%n"
              + "Сумма после скидки: %.2f руб.%n"
              + "НДС (%.0f%%): %.2f руб.%n"
              + "Итого: %.2f руб.", base, afterDiscount, VAT_RATE, vat, total);
    }

    private static double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
