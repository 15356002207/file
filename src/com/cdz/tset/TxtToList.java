/**
 * 
 */
package tset;

/**
 * @author Administrator
 *
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TxtToList {
	public static String str2[] = new String[226];
	public static List<String[]> list = new ArrayList<String[]>();

	public static List<String[]> jiequ(String filepath) {


		File file = new File(filepath);// 我的txt文本存放目录，根据自己的路径修改即可
		txt2String(file);
		/* System.out.println(txt2String(file)); */
		return list;


	}

	public static void txt2String(File file) {
		String str1[] = new String[226];


		StringBuilder result = new StringBuilder();
		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
			BufferedReader br = new BufferedReader(isr);




			String s = null;
			String s1 = null;


			int i = 0;
			while ((s = br.readLine()) != null) {// 使用readLine方法，一次读一行
				if (!s.equals("")) {// 判断非空行
					if (Character.isDigit(s.charAt(0))) {

						/* result.append(System.lineSeparator() + s); */

						/*
						 * s2 = s1.substring(s1.length() - 1, s1.length()); if (s2.equals("域") ||
						 * s2.equals("户")) { // s2 = s2.replace(s1.length() - 1, s1.length() - 2, "");
						 */
						/*
						 * }
						 */
						s1 = s.replace(" ", "");
						s1 = s1.substring(0, s1.length() - 2);
						str1[i] = s1.substring(0, 3);
						str2[i] = s1.substring(3);
					i++;
						result.append(System.lineSeparator() + s1);

				}
				
				}
			}
			br.close();


		} catch (Exception e) {
			e.printStackTrace();
		}

		list.add(str1);
		list.add(str2);

	}



	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
}