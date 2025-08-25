import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Objects;

public class CalculationServer {
    private String placeInScript;
    private String input;
    private int firstNum;
    private int secondNum;
    private BigInteger bigIntResult;
    private PrintWriter out;
    private BufferedReader in;
    private boolean isFirstRun;

    public CalculationServer(PrintWriter out, BufferedReader in) {
        this.out = out;
        this.in = in;
        this.placeInScript = null;
        this.input = null;
        this.firstNum = 0;
        this.secondNum = 0;
        this.bigIntResult = null;
        this.isFirstRun = true;
    }

    private String readInput() throws IOException {
        return in.readLine();
    }

    private void sendOutput(String message) {
        out.println(message);
        out.flush();
    }

    private void setAndGetConsoleText() throws IOException {
        boolean condition = false;

        do {
            switch (placeInScript) {
                case "start" -> {
                    sendOutput("Чтобы начать, введите: s");
                    sendOutput("Чтобы выйти, введите: q\n");
                    input = readInput();
                    if (Objects.equals(input, "q")) {
                        exitOnQ();
                    } else if (Objects.equals(input, "s")) {
                        condition = true;
                    } else {
                        sendOutput("Неверный ввод. Пожалуйста, введите 's' или 'q'\n");
                    }
                }
                case "numbers" -> {
                    sendOutput("Это должно быть целое число, в диапазоне от 0 до 1000:");
                    sendOutput("Чтобы завершить работу, введите: q\n");
                    input = readInput();
                    if (Objects.equals(input, "q")) {
                        exitOnQ();
                    } else if (input.matches("^(?:0|[1-9]|[1-9][0-9]{1,2}|1000)$")) {
                        condition = true;
                    } else {
                        sendOutput("Неверный ввод. Пожалуйста, введите целое число от 0 до 1000.");
                    }
                }
                case "operations" -> {
                    sendOutput("Чтобы вывести сумму всех чисел между выбранными, введите: +");
                    sendOutput("Чтобы вывести суммы всех чисел от 0 до каждого из выбранных, введите: +0");
                    sendOutput("Чтобы вывести произведение всех чисел между выбранными, введите: *");
                    sendOutput("Чтобы вывести суммы всех чисел от 0 до каждого из выбранных, сумму и произведение всех чисел между выбранными, введите: +*");
                    sendOutput("Чтобы завершить работу, введите: q\n");
                    input = readInput();
                    switch (input) {
                        case "+0", "+" -> {
                            printSumResult();
                            condition = true;
                        }
                        case "*" -> {
                            getMultiplicationResult();
                            condition = true;
                        }
                        case "+*" -> {
                            printSumResult();
                            getMultiplicationResult();
                            condition = true;
                        }
                        case "q" -> exitOnQ();
                        default -> sendOutput("Неверная операция. Пожалуйста, выберите одну из доступных операций.");
                    }
                }
            }
        } while (!condition);
    }

    private int getSumResult(int number1, int number2) {
        if (number1 == number2) {
            return number1 * 2;
        }
        if (number2 > number1) {
            return number1 + getSumResult(number1 + 1, number2);
        }
        return 0;
    }

    private void printSumResult() {
        String message;
        if (Objects.equals(input, "+0") || Objects.equals(input, "+*")) {
            message = "Сумма всех чисел от 0 до";
            sendOutput(String.format("%s меньшего: %d", message, (getSumResult(0, firstNum) - firstNum)));
            sendOutput(String.format("%s большего: %d", message, (getSumResult(0, secondNum) - secondNum)));

        }
        if (Objects.equals(input, "+") || Objects.equals(input, "+*")) {
            message = "Сумма всех чисел от меньшего до большего включительно: ";
            if (firstNum == secondNum) {
                sendOutput(message + firstNum * 2);
            } else {
                sendOutput(message + (getSumResult(firstNum, secondNum) - secondNum));
            }
        }
    }

    private void getMultiplicationResult() {
        if (firstNum == secondNum) {
            bigIntResult = BigInteger.valueOf(firstNum).multiply(BigInteger.valueOf(secondNum));
            sendOutput("Произведение всех чисел от меньшего до большего включительно: " + bigIntResult);
        } else {
            bigIntResult = BigInteger.valueOf(1);
            for (int i = firstNum; i <= secondNum; i++) {
                bigIntResult = bigIntResult.multiply(BigInteger.valueOf(i));
            }

            int comparisonResult = bigIntResult.compareTo(BigInteger.valueOf(999_999_999));
            if (comparisonResult < 0) {
                sendOutput("Произведение всех чисел от меньшего до большего включительно: " + bigIntResult);
            } else {
                int numDigits = bigIntResult.toString().length();
                int exponent = numDigits - 1;

                BigDecimal bigDecimal = new BigDecimal(bigIntResult);
                BigDecimal divisor = new BigDecimal("1E" + exponent);
                BigDecimal roundedDecimal = bigDecimal.divide(divisor, 1, RoundingMode.HALF_UP);

                sendOutput(String.format("Произведение всех чисел от меньшего до большего включительно приблизительно равно: %s * 10^%d",
                        roundedDecimal.stripTrailingZeros().toPlainString(), exponent));
            }
        }
    }

    public void flow() throws IOException {
        // Основной цикл программы
        while (true) {
            if (isFirstRun) {
                printWelcomeText();
                placeInScript = "start";
                setAndGetConsoleText();
                isFirstRun = false;
            }

            placeInScript = "numbers";
            sendOutput("Пожалуйста, введите первое число: \n");
            setAndGetConsoleText();
            firstNum = Integer.parseInt(input);

            sendOutput("Пожалуйста, введите второе число: \n");
            setAndGetConsoleText();
            secondNum = Integer.parseInt(input);

            getMinAndMax();
            sendOutput(String.format("Выбранные числа: %s и %s", firstNum, secondNum));

            placeInScript = "operations";
            setAndGetConsoleText();

            // После завершения расчетов предлагаем начать заново
            if (!offerRestart()) {
                break;
            }

            reset();
        }
    }

    public boolean offerRestart() throws IOException {
        sendOutput("============================================");
        sendOutput("Чтобы начать заново, введите: s");
        sendOutput("Чтобы выйти, введите: q\n");

        while (true) {
            input = readInput();
            if ("q".equals(input)) {
                sendOutput("Завершение работы. До свидания!");
                return false;
            } else if ("s".equals(input)) {
                sendOutput("Начинаем заново...");
                sendOutput("============================================");
                return true;
            } else {
                sendOutput("Неверный ввод. Пожалуйста, введите 's' или 'q'\n");
            }
        }
    }

    public void reset() {
        this.placeInScript = "numbers";
        this.input = null;
        this.firstNum = 0;
        this.secondNum = 0;
        this.bigIntResult = null;
    }

    public void printWelcomeText() {
        sendOutput("=== ДОБРО ПОЖАЛОВАТЬ В ПРОГРАММУ РАСЧЕТОВ ===");
        sendOutput("Это программа, которая считает сумму и произведение всех целых чисел");
        sendOutput("между любыми двумя, включая выбранные, в диапазоне от 0 до 1000.");
        sendOutput("Также она считает суммы чисел от нуля до выбранных чисел (включительно).");
        sendOutput("Пожалуйста, выберите целые числа от 0 до 1000");
        sendOutput("============================================");
    }

    private void getMinAndMax() {
        int[] array = {firstNum, secondNum};
        Arrays.sort(array);
        firstNum = array[0];
        secondNum = array[1];
    }

    public boolean isInputEqualQ() {
        return Objects.equals(input, "q");
    }

    private void exitOnQ() {
        if (isInputEqualQ()) {
            sendOutput("Завершение работы...");
            System.exit(0);
        }
    }

}