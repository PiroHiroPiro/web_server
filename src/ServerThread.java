import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

public class ServerThread implements Runnable {

	private static final String DOCUMENT_ROOT = "C:\\Apache24\\htdocs";
	private static final String SERVER_NAME = "localhost:8001";
	private Socket socket;

	@Override
	public void run() {
		OutputStream output = null;
		try {
			InputStream input = socket.getInputStream();

			String line;
			String path = null;
			String ext = null;
			String host = null;

			while((line = Util.readLine(input)) != null) {
				if (line == "") {
					break;
				}
				if(line.startsWith("GET")) {
					path = URLDecoder.decode(line.split(" ")[1], "UTF-8");
					String[] tmp = path.split("\\.");
					ext = tmp[tmp.length - 1];
				}else if(line.startsWith("Host:")) {
					host = line.substring("Host: ".length());
				}
			}

			if(path == null) {
				return;
			}

			if(path.endsWith("/")) {
				path += "index.html";
				ext = "html";
			}

			output = new BufferedOutputStream(socket.getOutputStream());

			FileSystem fs = FileSystems.getDefault();
			Path pathObj = fs.getPath(DOCUMENT_ROOT + path);
			Path realPath;

			try {
				realPath = pathObj.toRealPath();
			} catch (NoSuchFileException ex) {
				SendResponce.sendNotFoundResponce(output, DOCUMENT_ROOT);
				return;
			}
			if(!realPath.startsWith(DOCUMENT_ROOT)) {
				SendResponce.sendNotFoundResponce(output, DOCUMENT_ROOT);
				return;
			}else if(Files.isDirectory(realPath)) {
				String location = "http://" + ((host != null) ? host : SERVER_NAME) + path + "/";
				SendResponce.sendMovePermanentlyResponce(output, location);
				return;
			}

			try (InputStream fis = new BufferedInputStream(Files.newInputStream(realPath))) {
				SendResponce.sendOkResponce(output, fis, ext);
			}catch(FileNotFoundException ex) {
				SendResponce.sendNotFoundResponce(output, DOCUMENT_ROOT);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	ServerThread(Socket socket) {
		this.socket = socket;
	}

}
