package wrapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import manager.Constants;
import matpiv.Core;
import pivLayer.Imagen;
import pivLayer.MapaVectores;

import com.mathworks.toolbox.javabuilder.MWArray;
import com.mathworks.toolbox.javabuilder.MWException;
import com.mathworks.toolbox.javabuilder.MWNumericArray;
import com.mathworks.toolbox.javabuilder.MWStructArray;

public class MatLabWrapper {

	public static MapaVectores CorrealcionCruzada(Imagen image1, Imagen image2, Object windowSize, Double overlap, String metodo, int numPass) {
		String path = Constants.TMP_RESOURCES_PATH;
		int rnd = (int) (Math.random() * 1000000);
		String input1 = path + "in" + rnd + ".png";
		String input2 = path + "in" + (int) (rnd + 1) + ".png";

		File fileInput1 = new File(input1);
		File fileInput2 = new File(input2);
		
		try {
			Core core = new Core();
			
			ImageIO.write(image1.getImage(), "png", fileInput1);
			ImageIO.write(image2.getImage(), "png", fileInput2);

			//Object[] in = { input1, input2, new Double[][] { new Double[] { 64.0, 64.0 }, new Double[] {32,32} }, (Double) 1.0, (Double) 0.5, "multin" };
			
			Object[] in = { input1, input2, windowSize, (Double) 1.0, overlap, metodo };
			
			Object[] out = new Object[4];
			out = core.matpiv(1, in);
			MWArray x = ((MWStructArray) out[0]).getField(1);
			MWArray y = ((MWStructArray) out[0]).getField(2);
			MWArray ux = ((MWStructArray) out[0]).getField(3);
			MWArray uy = ((MWStructArray) out[0]).getField(4);					

			return mWArraysMultiToMapaVectores(x, y, ux, uy, numPass);

		} catch (MWException | IOException e) {
			e.printStackTrace();
		} finally {
			fileInput1.delete();
			fileInput2.delete();
		}
		return null;
	}

	public static MapaVectores globalFilter(MapaVectores mapa) {
		try {
			Core core = new Core();
			List<double[][]> matlabArray = mapaVectoresToMWArrays(mapa);
			Object[] out = new Object[2];
			
			out = core.globfilt(2, new Object[] { matlabArray.get(0), matlabArray.get(1), matlabArray.get(2), matlabArray.get(3),(double) 3.0 });

			MWArray ux = ((MWNumericArray)out[0]);
			MWArray uy =((MWNumericArray) out[1]);

			return mWArraysMultiToMapaVectores(matlabArray.get(0), matlabArray.get(1), ux, uy, 1);

		} catch (MWException e) {
			e.printStackTrace();
		}
		return null;

	}
	
	public static MapaVectores localFilter(MapaVectores mapa) {
		try {
			Core core = new Core();
			List<double[][]> matlabArray = mapaVectoresToMWArrays(mapa);
			Object[] out = new Object[2];
			out = core.localfilt(2, new Object[] { matlabArray.get(0), matlabArray.get(1), matlabArray.get(2), matlabArray.get(3),(double) 3.0,"median",(double) 3.0 });

			MWArray ux = ((MWNumericArray)out[0]);
			MWArray uy =((MWNumericArray) out[1]);

			return mWArraysMultiToMapaVectores(matlabArray.get(0), matlabArray.get(1), ux, uy, 1);

		} catch (MWException e) {
			e.printStackTrace();
		}
		return null;

	}
	
	public static MapaVectores nanInterpolationFilter(MapaVectores mapa) {
		try {
			Core core = new Core();
			List<double[][]> matlabArray = mapaVectoresToMWArrays(mapa);
			Object[] out = new Object[2];
			out = core.naninterp(2, new Object[] {matlabArray.get(2), matlabArray.get(3),"linear", matlabArray.get(0), matlabArray.get(1)});

			MWArray ux = ((MWNumericArray)out[0]);
			MWArray uy =((MWNumericArray) out[1]);

			return mWArraysMultiToMapaVectores(matlabArray.get(0), matlabArray.get(1), ux, uy, 1);

		} catch (MWException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static void visualizarStreamlines(MapaVectores mapa) {
		try {
			Core core = new Core();
			List<double[][]> matlabArray = mapaVectoresToMWArrays(mapa);
			core.mstreamline(1, new Object[] { matlabArray.get(0), matlabArray.get(1), matlabArray.get(2), matlabArray.get(3), 2 });
		} catch (MWException e) {
			e.printStackTrace();
		}

	}

	public static MapaVectores mWArraysMultiToMapaVectores(double[][] x, double[][] y, MWArray ux, MWArray uy, int numMapas) {
		double[][] mapVec = new double[ux.numberOfElements() / numMapas][5];
		int columnIndex = ux.columnIndex()[ux.numberOfElements() - 1];
		int rowIndex = ux.rowIndex()[ux.numberOfElements() - 1];
		int k = 0;
		for (int i = 0; i < rowIndex; i++)
			for (int j = 0; j < columnIndex / numMapas; j++) {
				int[] index = new int[] { i + 1, j + 1 };
				mapVec[k] = new double[] { x[i][j], y[i][j], (double) ux.get(index), (double) uy.get(index), 1.0 };
				k++;
			}
		return new MapaVectores(mapVec);
	}

	public static MapaVectores mWArraysMultiToMapaVectores(MWArray x, MWArray y, MWArray ux, MWArray uy, int numMapas) {
		double[][] mapVec = new double[x.numberOfElements() / numMapas][5];
		int columnIndex = x.columnIndex()[x.numberOfElements() - 1];
		int rowIndex = x.rowIndex()[x.numberOfElements() - 1];
		int k = 0;
		for (int i = 0; i < rowIndex; i++)
			for (int j = 0; j < columnIndex / numMapas; j++) {
				int[] index = new int[] { i + 1, j + 1 };
				mapVec[k] = new double[] { (double) x.get(index), (double) y.get(index), (double) ux.get(index), (double) uy.get(index), 1.0 };
				k++;
			}
		return new MapaVectores(mapVec);
	}

	public static List<double[][]> mapaVectoresToMWArrays(MapaVectores mapaVectores) {

		int col = 0;
		double[][] vectorMap = mapaVectores.getMapaVectores();
		while (vectorMap[col][1] == vectorMap[0][1])
			col++;
		int fil = vectorMap.length / col;

		double[][] x = new double[fil][col];
		double[][] y = new double[fil][col];
		double[][] ux = new double[fil][col];
		double[][] uy = new double[fil][col];

		for (int i = 0; i < fil; i++)
			for (int j = 0; j < col; j++) {
				x[i][j] = vectorMap[(i * col) + j][0];
				y[i][j] = vectorMap[(i * col) + j][1];
				ux[i][j] = vectorMap[(i * col) + j][2];
				uy[i][j] = vectorMap[(i * col) + j][3];
			}

		List<double[][]> matlabArrays = new ArrayList<double[][]>();
		matlabArrays.add(x);
		matlabArrays.add(y);
		matlabArrays.add(ux);
		
		matlabArrays.add(uy);
		return matlabArrays;
	}

}
