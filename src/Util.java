import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

class Util {
	// Utility method to read a byte sequence from InputStream line by line
	static String readLine(InputStream input) throws Exception {
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
	static void writeLine(OutputStream output, String str) throws Exception {
		for (char ch : str.toCharArray()) {
			output.write((int)ch);
		}

		output.write((int)'\r');
		output.write((int)'\n');
	}

	// From the current time, formatted according to the HTTP standard and return date string
	static String getDataStringUtc() {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.US);
		df.setTimeZone(cal.getTimeZone());
		return df.format(cal.getTime()) + " GMT";
	}

	// Extension and Content-Type Correspondence Table
	private static final HashMap<String, String> contentTypeMap =
			new HashMap<String, String>() {{
				put("html", "text/html");
				put("htm", "text/html");
				put("txt", "text/plain");
				put("css", "text/css");
				put("png", "image/png");
				put("jpg", "image/jpeg");
				put("jpeg", "image/jpeg");
				put("gif", "image/gif");
			}
	};

	// Accept extension and return Content-Type
	static String getContentType(String ext) {
		String ret = contentTypeMap.get(ext.toLowerCase());
		if (ret == null) {
			return "application/octet-stream";
		}else {
			return ret;
		}
	}
}
