import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.EOFException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12345); // Port number
            System.out.println("Waiting for client...");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected");
            while (true) {
                DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
                
                if(dis.available() == 0)
                    break;

                String fileName = dis.readUTF();
                long fileSize = dis.readLong();

                System.out.println("Receiving file: " + fileName);

                FileOutputStream fos = new FileOutputStream(fileName);
                byte[] buffer = new byte[4096];
                int bytesRead;
                while (fileSize > 0 && (bytesRead = dis.read(buffer, 0, (int) Math.min(buffer.length, fileSize))) != -1) {
                    fos.write(buffer, 0, bytesRead);
                    fileSize -= bytesRead;
                }
                System.out.println("File received: " + fileName);
                fos.close();
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
