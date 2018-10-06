import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class Modoki01 {

	private static final String DOCUMENT_ROOT = "C:\\Apache24\\htdocs";

	// Utility method to read a byte sequence from InputStream line by line
	private static String readLine(InputStream input) throws Exception {
		int ch;
		String ret = "";
		while((ch = input.read()) != -1) {
			if(ch == '\r') {
			}else if(ch == '\n') {
				break;
			} else {
				ret += (char)ch;
			}
		}

		if (ch == -1) {
			return null;
		} else {
			return ret;
		}
	}

	// Utility method that writes a single character string to OutputStream as a byte sequence
	private static void writeLine(OutputStream output, String str) throws Exception {
		for (char ch : str.toCharArray()) {
			output.write((int)ch);
		}

		output.write((int)'\r');
		output.write((int)'\n');
	}

	// From the current time, formatted according to the HTTP standard and return date string
	private static String getDataStringUtc() {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.US);
		df.setTimeZone(cal.getTimeZone());
		return df.format(cal.getTime()) + " GMT";
	}

	public static void main(String[] argv) throws Exception {
		try (ServerSocket server = new ServerSocket(8001)) {
			Socket socket = server.accept();

			InputStream input = socket.getInputStream();

			String line;
			String path = null;
			while((line = readLine(input)) != null) {
				if (line == "") {
					break;
				}
				if(line.startsWith("GET")) {
					path = line.split(" ")[1];
				}
			}

			OutputStream output = socket.getOutputStream();
			// Return response header
			writeLine(output, "HTTP/1.1 200 OK");
			writeLine(output, "Date: " + getDataStringUtc());
			writeLine(output, "Server: Modoki/0.1");
			writeLine(output, "Connection: close");
			writeLine(output, "Content-type: text/html");
			writeLine(output, "");

			// Return responce body
			try (FileInputStream fis = new FileInputStream(DOCUMENT_ROOT + path);) {
				int ch;
				while((ch = fis.read()) != -1) {
					output.write(ch);
				}
			}

			socket.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

}