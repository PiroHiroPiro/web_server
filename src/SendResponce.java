import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

class SendResponce {
	static void sendOkResponce(OutputStream output, InputStream fis, String ext) throws Exception {
		Util.writeLine(output, "HTTP/1.1 200 OK");
		Util.writeLine(output, "Date: " + Util.getDataStringUtc());
		Util.writeLine(output, "Server: Modoki/0.2");
		Util.writeLine(output, "Connection: close");
		Util.writeLine(output, "Content-type: " + Util.getContentType(ext));
		Util.writeLine(output, "");

		int ch;
		while((ch = fis.read()) != -1) {
			output.write(ch);
		}
	}

	static void sendMovePermanentlyResponce(OutputStream output, String location) throws Exception {
		Util.writeLine(output, "HTTP/1.1 301 MovedPermanently");
		Util.writeLine(output, "Date: " + Util.getDataStringUtc());
		Util.writeLine(output, "Server: Modoki/0.2");
		Util.writeLine(output, "Location: " + location);
		Util.writeLine(output, "Connection: close");
		Util.writeLine(output, "");
	}

	static void sendNotFoundResponce(OutputStream output, String errorDocumentRoot) throws Exception {
		Util.writeLine(output, "HTTP/1.1 404 Not Found");
		Util.writeLine(output, "Date: " + Util.getDataStringUtc());
		Util.writeLine(output, "Server: Modoki/0.2");
		Util.writeLine(output, "Connection: close");
		Util.writeLine(output, "Content-type: text/html");
		Util.writeLine(output, "");

		try (InputStream fis = new BufferedInputStream(new FileInputStream(errorDocumentRoot + "/404.html"))) {
			int ch;
			while((ch = fis.read()) != -1) {
				output.write(ch);
			}
		}
	}
}
