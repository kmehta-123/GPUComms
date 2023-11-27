import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayDeque;

public class Client {
    public static void main(String[] args) {
        try {
            // Temporary, q should be constantly updating and reading images from GPU
            ArrayDeque<FileInputStream> q = new ArrayDeque<FileInputStream>();

            // Add images to q here
            q.add(new FileInputStream("test_images/test.jpg"));
            q.add(new FileInputStream("test_images/test2.jpg"));
            q.add(new FileInputStream("test_images/test3.jpg"));

            Socket socket = new Socket("localhost", 12345); // Replace with the server's IP and port

            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            int imageID = 0;

            while(!q.isEmpty()) {
                FileInputStream fis = q.remove();
                String fileName = "received_images/received_image_" + imageID + ".jpg";

                dos.writeUTF(fileName);

                long fileSize = fis.available();
                dos.writeLong(fileSize);

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    dos.write(buffer, 0, bytesRead);
                }

                System.out.println("File sent: " + fileName);
                imageID ++;
                fis.close();
            }

            dos.close();
            socket.close();

            System.out.println("All files sent!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
