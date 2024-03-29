import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ImageServer {
    private static final int PORT = 5555;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server listening on port " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket.getInetAddress());

                handleClient(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket socket) {
        try (DataInputStream dis = new DataInputStream(socket.getInputStream())) {
            while (true) {
                String fileName = dis.readUTF();
                if (fileName.equals("END_OF_TRANSMISSION")) {
                    System.out.println("Client disconnected: " + socket.getInetAddress());
                    break;
                }

                long fileSize = dis.readLong();
                System.out.println("Receiving file: " + fileName + " (" + fileSize + " bytes)");

                try (FileOutputStream fos = new FileOutputStream(fileName)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while (fileSize > 0 && (bytesRead = dis.read(buffer, 0, (int) Math.min(buffer.length, fileSize))) != -1) {
                        fos.write(buffer, 0, bytesRead);
                        fileSize -= bytesRead;
                    }

                    System.out.println("File received successfully.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
