import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TcpClient {
	public static void main(String[] argv) throws Exception {
		try (Socket socket = new Socket("localhost", 80);
				FileInputStream fis = new FileInputStream("client_send.txt");
				FileOutputStream fos = new FileOutputStream("client_revc.txt")
				) {

			int ch;
			// Send the contents of "client_send.txt" to the server
			OutputStream output = socket.getOutputStream();
			while((ch = fis.read()) != -1) {
				output.write(ch);
			}
			// Send 0 to indicate termination
			// output.write(0);

			// Output reply from server to "client_recv.txt"
			InputStream input = socket.getInputStream();
			while((ch = input.read()) != 0) {
				fos.write(ch);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
