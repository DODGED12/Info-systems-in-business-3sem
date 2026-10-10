public class SelfCheck {
    private static int failed = 0;

    public static void main(String[] args) {
        // 1. типовое значение: 10 * 1000 = 10000; скидка 10 % -> 9000; НДС 10 % -> 900; итого 9900
        checkEq("база", 10000.00, OrderCalculator.calculateBase(10, 1000));
        checkEq("скидка 10%", 9000.00, OrderCalculator.applyDiscount(10000, 10));
        checkEq("НДС", 900.00, OrderCalculator.calculateVat(9000));
        // 2. нижняя граница
        check("количество 1 допустимо", OrderCalculator.isValid(1, 0.01, 0));
        check("количество 0 недопустимо", !OrderCalculator.isValid(0, 100, 0));
        check("цена 0 недопустима", !OrderCalculator.isValid(1, 0, 0));
        check("скидка 0 допустима", OrderCalculator.isValid(1, 100, 0));
        check("скидка -1 недопустима", !OrderCalculator.isValid(1, 100, -1));
        // 3. верхняя граница
        check("количество 10000 допустимо", OrderCalculator.isValid(10000, 100, 0));
        check("количество 10001 недопустимо", !OrderCalculator.isValid(10001, 100, 0));
        check("цена 5000000 допустима", OrderCalculator.isValid(1, 5_000_000, 0));
        check("цена 5000000.01 недопустима", !OrderCalculator.isValid(1, 5_000_000.01, 0));
        check("скидка 30 допустима", OrderCalculator.isValid(1, 100, 30));
        check("скидка 30.01 недопустима", !OrderCalculator.isValid(1, 100, 30.01));
        // 4. округление: 3 * 0.1 в double не равно ровно 0.3
        checkEq("округление", 0.30, OrderCalculator.calculateBase(3, 0.1));
        // 5. НДС после скидки, а не до неё
        checkEq("НДС после скидки", 8.00, OrderCalculator.calculateVat(OrderCalculator.applyDiscount(100, 20)));

        System.out.println(failed == 0 ? "SelfCheck: все проверки пройдены" : "SelfCheck: ошибок " + failed);
    }

    private static void check(String name, boolean ok) {
        System.out.println((ok ? "OK   " : "FAIL ") + name);
        if (!ok) failed++;
    }

    private static void checkEq(String name, double expected, double actual) {
        check(name + " (ожидалось " + expected + ", получено " + actual + ")",
                Math.abs(expected - actual) < 0.0001);
    }
}
