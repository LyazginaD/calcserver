import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CalculationServerTest {

    private CalculationServer server;
    private StringWriter stringWriter;
    public PrintWriter out;
    public BufferedReader in;

    @BeforeEach
    void setUp() {

        stringWriter = new StringWriter();
        out = new PrintWriter(stringWriter);
        in = mock(BufferedReader.class);
        server = new CalculationServer(out, in);
    }

    // ==================== ТЕСТЫ ДЛЯ СУММ ОТ НУЛЯ ДО ВЫБРАННЫХ ЧИСЕЛ ====================

    @Test
    @DisplayName("Сумма от 0 до чисел в диапазоне (4 и 7) - позитивный")
    void testSumFromZeroToNumbersInRange(){
        // Arrange
        server.firstNum = 4;
        server.secondNum = 7;
        server.input = "+0";
        server.getMinAndMax(); // Сортируем числа

        // Act
        server.printSumResult();
        String output = stringWriter.toString();

        // Assert
        assertTrue(output.contains("Сумма всех чисел от 0 до меньшего: 10")); // 0+1+2+3+4 = 10
        assertTrue(output.contains("Сумма всех чисел от 0 до большего: 28")); // 0+1+2+3+4+5+6+7 = 28
    }

    @Test
    @DisplayName("Сумма от 0 до чисел идущих по порядку (2 и 3) - позитивный")
    void testSumFromZeroToConsecutiveNumbers(){
        // Arrange
        server.firstNum = 2;
        server.secondNum = 3;
        server.input = "+0";
        server.getMinAndMax();

        // Act
        server.printSumResult();
        String output = stringWriter.toString();

        // Assert
        assertTrue(output.contains("Сумма всех чисел от 0 до меньшего: 3")); // 0+1+2 = 3
        assertTrue(output.contains("Сумма всех чисел от 0 до большего: 6")); // 0+1+2+3= 6
    }

    @Test
    @DisplayName("Сумма от 0 до граничных значений (0 и 1000) - позитивный")
    void testSumFromZeroToBoundaryValues(){
        // Arrange
        server.firstNum = 0;
        server.secondNum = 1000;
        server.input = "+0";
        server.getMinAndMax();

        // Act
        server.printSumResult();
        String output = stringWriter.toString();

        // Assert
        assertTrue(output.contains("Сумма всех чисел от 0 до меньшего: 0")); // 0
        assertTrue(output.contains("Сумма всех чисел от 0 до большего: 500500")); // 0+1+2+...+1000 = 500500
    }

    @Test
    @DisplayName("Сумма от 0 до чисел (1 и 1000) - позитивный")
    void testSumFromZeroToOneAndThousand(){
        // Arrange
        server.firstNum = 1;
        server.secondNum = 1000;
        server.input = "+0";
        server.getMinAndMax();

        // Act
        server.printSumResult();
        String output = stringWriter.toString();

        // Assert
        assertTrue(output.contains("Сумма всех чисел от 0 до меньшего: 1")); // 1
        assertTrue(output.contains("Сумма всех чисел от 0 до большего: 500500")); // 0+1+2+...+1000 = 500500
    }

    @Test
    @DisplayName("Сумма от 0 до одинаковых чисел (5 и 5) - позитивный")
    void testSumFromZeroToEqualNumbers(){
        // Arrange
        server.firstNum = 5;
        server.secondNum = 5;
        server.input = "+0";
        server.getMinAndMax();

        // Act
        server.printSumResult();
        String output = stringWriter.toString();

        // Assert
        assertTrue(output.contains("Сумма всех чисел от 0 до меньшего: 15")); // 0+1+2+3+4+5 = 15
        assertTrue(output.contains("Сумма всех чисел от 0 до большего: 15")); // 0+1+2+3+4+5 = 15
    }

    // ==================== ТЕСТЫ ДЛЯ СУММ МЕЖДУ ВЫБРАННЫМИ ЧИСЛАМИ ====================

    @Test
    @DisplayName("Сумма между числами в диапазоне (4 и 7) - позитивный")
    void testSumBetweenNumbersInRange(){
        // Arrange
        server.firstNum = 4;
        server.secondNum = 7;
        server.input = "+";
        server.getMinAndMax();

        // Act
        server.printSumResult();
        String output = stringWriter.toString();

        // Assert
        assertTrue(output.contains("Сумма всех чисел от меньшего до большего включительно: 22")); // 4+5+6+7 = 22
    }

    @Test
    @DisplayName("Сумма между числами идущими по порядку (2 и 3) - позитивный")
    void testSumBetweenConsecutiveNumbers(){
        // Arrange
        server.firstNum = 2;
        server.secondNum = 3;
        server.input = "+";
        server.getMinAndMax();

        // Act
        server.printSumResult();
        String output = stringWriter.toString();

        // Assert
        assertTrue(output.contains("Сумма всех чисел от меньшего до большего включительно: 5")); // 2+3 = 5
    }

    @Test
    @DisplayName("Сумма между граничными значениями (0 и 1000) - позитивный")
    void testSumBetweenBoundaryValues(){
        // Arrange
        server.firstNum = 0;
        server.secondNum = 1000;
        server.input = "+";
        server.getMinAndMax();

        // Act
        server.printSumResult();
        String output = stringWriter.toString();

        // Assert
        assertTrue(output.contains("Сумма всех чисел от меньшего до большего включительно: 500500")); // 0+1+2+...+1000 = 500500
    }

    @Test
    @DisplayName("Сумма между одинаковыми числами (4 и 4) - позитивный")
    void testSumBetweenEqualNumbers(){
        // Arrange
        server.firstNum = 4;
        server.secondNum = 4;
        server.input = "+";
        server.getMinAndMax();

        // Act
        server.printSumResult();
        String output = stringWriter.toString();

        // Assert
        assertTrue(output.contains("Сумма всех чисел от меньшего до большего включительно: 8")); // 4*2 = 8
    }

    // ==================== ТЕСТЫ ДЛЯ ПРОИЗВЕДЕНИЙ МЕЖДУ ВЫБРАННЫМИ ЧИСЛАМИ ====================

    @Test
    @DisplayName("Произведение между числами в диапазоне (4 и 7) - позитивный")
    void testProductBetweenNumbersInRange(){
        // Arrange
        server.firstNum = 2;
        server.secondNum = 4;
        server.input = "*";
        server.getMinAndMax();

        // Act
        server.getMultiplicationResult();
        String output = stringWriter.toString();

        // Assert
        assertTrue(output.contains("Произведение всех чисел от меньшего до большего включительно: 24")); // 2*3*4 = 24
    }

    @Test
    @DisplayName("Произведение между числами (1 и 1000) - позитивный, научная нотация")
    void testProductBetweenOneAndThousand(){
        // Arrange
        server.firstNum = 1;
        server.secondNum = 1000;
        server.input = "*";
        server.getMinAndMax();

        // Act
        server.getMultiplicationResult();
        String output = stringWriter.toString();

        // Assert
        assertTrue(output.contains("Произведение всех чисел от меньшего до большего включительно приблизительно равно:"));
        assertTrue(output.contains("4.0 * 10^2567") || output.contains("4 * 10^2567"));
    }

    @Test
    @DisplayName("Произведение между граничными значениями (0 и 1000) - позитивный")
    void testProductBetweenBoundaryValues(){
        // Arrange
        server.firstNum = 0;
        server.secondNum = 1000;
        server.input = "*";
        server.getMinAndMax();

        // Act
        server.getMultiplicationResult();
        String output = stringWriter.toString();

        // Assert
        assertTrue(output.contains("Произведение всех чисел от меньшего до большего включительно: 0")); // 0 * ... = 0
    }

    @Test
    @DisplayName("Произведение между числами идущими по порядку (2 и 3) - позитивный")
    void testProductBetweenConsecutiveNumbers(){
        // Arrange
        server.firstNum = 2;
        server.secondNum = 3;
        server.input = "*";
        server.getMinAndMax();

        // Act
        server.getMultiplicationResult();
        String output = stringWriter.toString();

        // Assert
        assertTrue(output.contains("Произведение всех чисел от меньшего до большего включительно: 6")); // 2*3 = 6
    }

    @Test
    @DisplayName("Произведение между одинаковыми числами (4 и 4) - позитивный")
    void testProductBetweenEqualNumbers(){
        // Arrange
        server.firstNum = 4;
        server.secondNum = 4;
        server.input = "*";
        server.getMinAndMax();

        // Act
        server.getMultiplicationResult();
        String output = stringWriter.toString();

        // Assert
        assertTrue(output.contains("Произведение всех чисел от меньшего до большего включительно: 16")); // 4*4 = 16
    }

    @Test
    @DisplayName("Произведение маленьких чисел - обычный формат")
    void testProductSmallNumbersRegularFormat(){
        // Arrange
        server.firstNum = 2;
        server.secondNum = 4;
        server.input = "*";
        server.getMinAndMax();

        // Act
        server.getMultiplicationResult();
        String output = stringWriter.toString();

        // Assert
        assertTrue(output.contains("Произведение всех чисел от меньшего до большего включительно: 24")); // 2*3*4*5=24
        assertFalse(output.contains("приблизительно равно"));
    }

    @Test
    @DisplayName("Проверка операции +* (сумма и произведение)")
    void testBothSumAndProductOperations(){
        // Arrange
        server.firstNum = 2;
        server.secondNum = 3;
        server.input = "+*";
        server.getMinAndMax();

        // Act
        server.printSumResult();
        server.getMultiplicationResult();
        String output = stringWriter.toString();

        // Assert
        assertTrue(output.contains("Сумма всех чисел от 0 до меньшего: 3")); // 0+1+2 = 3
        assertTrue(output.contains("Сумма всех чисел от 0 до большего: 6")); // 0+1+2+3 = 6
        assertTrue(output.contains("Сумма всех чисел от меньшего до большего включительно: 5")); // 2+3 = 5
        assertTrue(output.contains("Произведение всех чисел от меньшего до большего включительно: 6")); // 2*3 = 6
    }

    // ==================== ДОПОЛНИТЕЛЬНЫЕ ТЕСТЫ ====================

    @Test
    @DisplayName("Проверка сортировки чисел")
    void testGetMinAndMax() {
        // Arrange
        server.firstNum = 10;
        server.secondNum = 5;

        // Act
        server.getMinAndMax();

        // Assert
        assertEquals(5, server.firstNum);
        assertEquals(10, server.secondNum);
    }

}