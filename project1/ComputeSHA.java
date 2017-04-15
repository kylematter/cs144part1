import java.io.File;
import java.security.*;
import java.io.FileInputStream;

public class ComputeSHA {

	public static StringBuffer makeHash (byte[] bytes) {
		StringBuffer hex = new StringBuffer();
		try {
			MessageDigest sum = MessageDigest.getInstance("SHA");
			byte[] arr = sum.digest(bytes);
			for (byte x: arr) {
				if ((x & 0xFF) < 0x10) {
					hex.append('0');
				}
				hex.append(Integer.toHexString(0xFF & x));
			}
		} catch (NoSuchAlgorithmException e) {
			System.out.println("The algorithm does not exist.");
			System.exit(1);
		}
		return hex;
	}

	public static void main (String[] args) {
		File input = new File(args[0]);
		byte[] b = new byte[(int)input.length()];
		try {
			FileInputStream stream = new FileInputStream(input);
			stream.read(b);
			stream.close();
			System.out.println(makeHash(b));
		}
		catch (Exception e) {
			System.out.println("Something went wrong");
			System.exit(1);
		}
	}
}
