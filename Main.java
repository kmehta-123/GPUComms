public class Main
{
    public static void main(String[] args) throws Exception
    {
        Socket socket = new Socket("localhost", 13085);
        OutputStream outputStream = socket.getOutputStream();

        BufferedImage image = ImageIO.read(new File("test3.jpg"));
        
    }
}