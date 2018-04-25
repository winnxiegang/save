package git.dzc.downloadmanagerlib.download.service;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'a', 'b', 'c', 'd', 'e', 'f' };

	public final static String MD5(String s) {
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
            for (byte byte0 : md) {
                str[k++] = HEX_DIGITS[byte0 >>> 4 & 0xf];
                str[k++] = HEX_DIGITS[byte0 & 0xf];
            }
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String toHexString(byte[] b) {
		StringBuilder sb = new StringBuilder(b.length * 2);
        for (byte aB : b) {
            sb.append(HEX_DIGITS[(aB & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[aB & 0x0f]);
        }
		return sb.toString();
	}


	/**
	 * 对文件进行md5
	 * @param file
	 * @return
	 */
	public static String md5sum(File file) {
		InputStream fis;
		byte[] buffer = new byte[1024];
		int numRead = 0;
		MessageDigest md5;
		try{
			fis = new FileInputStream(file);
			md5 = MessageDigest.getInstance("MD5");
			while((numRead=fis.read(buffer)) > 0) {
				md5.update(buffer,0,numRead);
			}
			fis.close();
			return toHexString(md5.digest());
		} catch (Exception e) {
			System.out.println("error");
			return null;
		}
	}

	/**
	 * 对文件进行md5校验
	 */
	public static boolean md5Check(String md5,File file){
		boolean isCompleted = false;

		String localMD5 = md5sum(file);

		if (md5.equals(localMD5)) {

			return true;
		}
		return isCompleted;
	}

	public static String md5ForFile(File file){
		int buffersize = 1024;
		FileInputStream fis = null;
		DigestInputStream dis = null;

		try {
			//创建MD5转换器和文件流
			MessageDigest messageDigest =MessageDigest.getInstance("MD5");
			fis = new FileInputStream(file);
			dis = new DigestInputStream(fis,messageDigest);

			byte[] buffer = new byte[buffersize];
			//DigestInputStream实际上在流处理文件时就在内部就进行了一定的处理
			while (dis.read(buffer) > 0);

			//通过DigestInputStream对象得到一个最终的MessageDigest对象。
			messageDigest = dis.getMessageDigest();

			// 通过messageDigest拿到结果，也是字节数组，包含16个元素
			byte[] array = messageDigest.digest();
			// 同样，把字节数组转换成字符串
			StringBuilder hex = new StringBuilder(array.length * 2);
			for (byte b : array) {
				if ((b & 0xFF) < 0x10){
					hex.append("0");
				}
				hex.append(Integer.toHexString(b & 0xFF));
			}
			return hex.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

}
