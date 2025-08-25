import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Сервер запущен на localhost:8080");
            System.out.println("Ожидание подключений...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Новое подключение: " + clientSocket.getInetAddress());

                // Обрабатываем каждого клиента в отдельном потоке
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            System.out.println("Не удалось запустить сервер: " + e.getMessage());
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            CalculationServer calculation = new CalculationServer(out, in);
            calculation.flow(); // Запускаем основной поток

        } catch (IOException e) {
            System.out.println("Ошибка при обработке клиента: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Общая ошибка: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                System.out.println("Соединение с клиентом закрыто");
            } catch (IOException e) {
                System.out.println("Ошибка при закрытии сокета: " + e.getMessage());
            }
        }
    }
}