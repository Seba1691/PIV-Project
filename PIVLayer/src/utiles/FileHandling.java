package utiles;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StreamTokenizer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;

public class FileHandling {

	public static void writeArrayToFile(double[][] array, String pathname, DecimalFormat df) throws IOException {
		BufferedWriter file = new BufferedWriter(new FileWriter(pathname));
		for (int i = 0; i < array.length; ++i) {
			for (int j = 0; j < array[0].length; ++j) {
				file.write(" " + df.format(array[i][j]));
			}
			file.newLine();
		}
		file.flush();
		file.close();
	}
	
	public static void writeArrayToFile(double[][] data, String pathname) throws IOException {
		DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance(Locale.US);
		df.applyPattern("+0.0000E00;-0.0000E00");
		writeArrayToFile(data, pathname + ".jvc", df);
	}

	public static double[][] readArrayFromFile(String pathname) throws FileHandlingException {
		try {
			int numOfRows = 0;
			int i = 0;
			int numOfCol = 0;
			int j = 0;
			double[] data = new double[25];
			// array list for conveniently appending the parsed rows
			// do not use a LinkedList here: bad performance
			ArrayList<double[]> al = new ArrayList<double[]>();
			// count lines that contain Tecplot key words
			BufferedReader br = new BufferedReader(new FileReader(pathname));
			int headerLines = -1;
			String line;
			do {
				line = br.readLine();
				headerLines += 1;
			} while (line.contains("TITLE") || line.contains("VARIABLES") || line.contains("ZONE"));
			br.close();
			br = new BufferedReader(new FileReader(pathname));
			// skip the header
			for (int h = 0; h < headerLines; h++)
				br.readLine();
			// configuring StreamTokenizer
			StreamTokenizer st = new StreamTokenizer(br);
			st.eolIsSignificant(true);
			st.resetSyntax();
			st.wordChars('0', '9');
			st.wordChars('-', '-');
			st.wordChars('+', '+');
			st.wordChars('e', 'e');
			st.wordChars('E', 'E');
			st.wordChars('.', '.');
			st.whitespaceChars(' ', ' ');
			st.whitespaceChars('\t', '\t');
			int type = -1;
			while ((type = st.nextToken()) != StreamTokenizer.TT_EOF) {
				switch (type) {
				case StreamTokenizer.TT_WORD:
					data[j] = Double.parseDouble(st.sval);
					j++;
					break;
				case StreamTokenizer.TT_EOL:
					// at the end of the line, the data is appended at the
					// ArrayList
					// use clone() to copy the object and not just its reference
					al.add(data.clone());
					numOfRows++;
					numOfCol = j;
					j = 0;
					break;
				default:
					break;
				}
			}
			br.close();
			// copying the data from the ArrayList into a double-array
			double[][] array = new double[numOfRows][numOfCol];
			for (i = 0; i < numOfRows; ++i) {
				data = (double[]) al.get(i);
				System.arraycopy(data, 0, array[i], 0, numOfCol);
			}
			al.clear();
			return (array);
		} catch (IOException e) {
			throw new FileHandlingException("Ha ocurrido un error al leer el mapa de vectores desde el archivo", e);
		}
	}

	public static String getExtension(File file) {
		return file.getName().contains(".") ? file.getName().substring(file.getName().lastIndexOf('.') + 1) : null;
	}

	public static BufferedImage getBufferedImage(File f) throws FileHandlingException {
		try {
			return ImageIO.read(f);
		} catch (IOException e) {
			throw new FileHandlingException("Ha ocurrido un error al obtener la imagen desde el archivo", e);
		}
	}

	public static void writeImageToFile(BufferedImage image, File file) throws FileHandlingException {
		try {
			ImageIO.write(image, getExtension(file), file);
		} catch (IOException e) {
			throw new FileHandlingException("Ha ocurrido un error al guardar la imagen en un archivo", e);
		}
	}
	
	public static InputStream getInputStreamFromZip(File zip, String entry) throws IOException {
		ZipInputStream zin = new ZipInputStream(new FileInputStream(zip));
		for (ZipEntry e; (e = zin.getNextEntry()) != null;) {
			if (e.getName().equals(entry)) {
				return zin;
			}
		}
		return null;
	}
	

}
