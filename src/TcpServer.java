import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {
	public static void main(String[] argv) throws Exception {
		try (ServerSocket server = new ServerSocket(8001);
				FileOutputStream fos = new FileOutputStream("server_revc.txt");
				FileInputStream fis = new FileInputStream("server_send.txt")
				) {

			System.out.println("Listen access from client...");
			Socket socket = server.accept();
			System.out.println("Connect client.");

			int ch;
			// Output contents received from client to "server_recv.txt"
			InputStream input = socket.getInputStream();
			// The client sends 0 as the end mark
			while((ch = input.read()) != 0) {
				fos.write(ch);
			}

			// Send the contents of "server_send.txt" to the client
			OutputStream output = socket.getOutputStream();
			while((ch = fis.read()) != -1) {
				output.write(ch);
			}

			socket.close();
			System.out.println("Close connection.");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

}