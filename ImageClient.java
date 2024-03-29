import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayDeque;

public class ImageClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 5555;

    public static void main(String[] args) {
        ArrayDeque<String> imageQueue = new ArrayDeque<>();
        imageQueue.add("test_images/test.jpg");
        imageQueue.add("test_images/test2.jpg");
        // Add more images to the queue as needed

        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {

            while (!imageQueue.isEmpty()) {
                String imagePath = imageQueue.poll();
                sendImage(dos, imagePath);
            }

            // Send the end marker to indicate that there are no more files to send
            dos.writeUTF("END_OF_TRANSMISSION");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendImage(DataOutputStream dos, String imagePath) {
        try (FileInputStream fis = new FileInputStream(imagePath)) {
            dos.writeUTF(new File(imagePath).getName());
            dos.writeLong(new File(imagePath).length());

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, bytesRead);
            }

            System.out.println("File sent successfully: " + imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
