package wrapper;

import java.awt.Rectangle;

import fiji.threshold.Auto_Local_Threshold;
import ij.ImagePlus;
import ij.gui.PolygonRoi;
import ij.plugin.ImageCalculator;
import ij.plugin.filter.MaximumFinder;
import ij.process.ByteProcessor;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;
import pivLayer.ElementoProcesable;
import pivLayer.Imagen;

public class ImageJWrapper {

	public static Imagen substractFilter(Imagen imagen1, Imagen imagen2) {
		ImagePlus imp1 = new ImagePlus("", imagen1.getImage());
		ImagePlus imp2 = new ImagePlus("", imagen2.getImage());
		ImageCalculator ic = new ImageCalculator();
		ImagePlus imp3 = ic.run("Subtract create", imp1, imp2);
		return new Imagen(imp3.getBufferedImage());
	}

	public static Imagen autoLocalThreshold(Imagen imagen1, String metodo, int radio, double parametro1, double parametro2, boolean fondoBlanco) {
		// Clases y metodos correspondientes a la herramienta extendida
		ImagePlus imp1 = new ImagePlus("", imagen1.getImage()).duplicate();
		ImageConverter c = new ImageConverter(imp1);
		c.convertToGray8();
		Auto_Local_Threshold a = new Auto_Local_Threshold();
		a.exec(imp1, metodo, radio, parametro1, parametro2, fondoBlanco);
		return new Imagen(imp1.getBufferedImage());
	}

	public static Imagen findMaxima(Imagen imagen1, int noiseTolerance, boolean excludeEdges) {
		ImagePlus imp1 = new ImagePlus("", imagen1.getImage());
		ImageProcessor ip = imp1.getProcessor();
		MaximumFinder mf = new MaximumFinder();
		ImagePlus imp2 = new ImagePlus("", mf.findMaxima(ip, noiseTolerance, 1, excludeEdges));
		return new Imagen(imp2.getBufferedImage());
	}

	public static ElementoProcesable crop(Imagen imagen, Integer[][] integers) {
		ImagePlus imp1 = new ImagePlus("", imagen.getImage()).duplicate();
		float[] pointx = new float[integers[0].length];
		float[] pointy = new float[integers[1].length];
		for (int i = 0; i < integers[0].length; i++) {
			pointx[i] = (float) integers[0][i];
			pointy[i] = (float) integers[1][i];
		}
		PolygonRoi polygonROI = new PolygonRoi(pointx, pointy, PolygonRoi.POLYGON);
		imp1.setRoi(polygonROI);
		ImageProcessor ip2 = imp1.getProcessor().crop();
		ImagePlus imp2 = new ImagePlus("", ip2);
		return new Imagen(imp2.getBufferedImage());
	}

	public static Imagen clahe(Imagen imagen, int blockRadius, int bins, float slope, Rectangle roiBox, ByteProcessor mask) {
		ImagePlus imp = new ImagePlus("", imagen.getImage()).duplicate();
		/* initialize box if necessary */
		final Rectangle box;
		if (roiBox == null) {
			if (mask == null)
				box = new Rectangle(0, 0, imp.getWidth(), imp.getHeight());
			else
				box = new Rectangle(0, 0, Math.min(imp.getWidth(), mask.getWidth()), Math.min(imp.getHeight(), mask.getHeight()));
		} else
			box = roiBox;

		/* make sure that the box is not larger than the mask */
		if (mask != null) {
			box.width = Math.min(mask.getWidth(), box.width);
			box.height = Math.min(mask.getHeight(), box.height);
		}

		/* make sure that the box is not larger than the image */
		box.width = Math.min(imp.getWidth() - box.x, box.width);
		box.height = Math.min(imp.getHeight() - box.y, box.height);

		final int boxXMax = box.x + box.width;
		final int boxYMax = box.y + box.height;

		/* convert 8bit processors with a LUT to RGB and create Undo-step */
		final ImageProcessor ip;
		if (imp.getType() == ImagePlus.COLOR_256) {
			ip = imp.getProcessor().convertToRGB();
			imp.setProcessor(imp.getTitle(), ip);
		} else
			ip = imp.getProcessor();

		/* work on ByteProcessors that reflect the user defined intensity range */
		final ByteProcessor src;
		if (imp.getType() == ImagePlus.GRAY8)
			src = (ByteProcessor) ip.convertToByte(true).duplicate();
		else
			src = (ByteProcessor) ip.convertToByte(true);
		final ByteProcessor dst = (ByteProcessor) src.duplicate();

		for (int y = box.y; y < boxYMax; ++y) {
			final int yMin = Math.max(0, y - blockRadius);
			final int yMax = Math.min(imp.getHeight(), y + blockRadius + 1);
			final int h = yMax - yMin;

			final int xMin0 = Math.max(0, box.x - blockRadius);
			final int xMax0 = Math.min(imp.getWidth() - 1, box.x + blockRadius);

			/* initially fill histogram */
			final int[] hist = new int[bins + 1];
			final int[] clippedHist = new int[bins + 1];
			for (int yi = yMin; yi < yMax; ++yi)
				for (int xi = xMin0; xi < xMax0; ++xi)
					++hist[roundPositive(src.get(xi, yi) / 255.0f * bins)];

			for (int x = box.x; x < boxXMax; ++x) {
				final int v = roundPositive(src.get(x, y) / 255.0f * bins);

				final int xMin = Math.max(0, x - blockRadius);
				final int xMax = x + blockRadius + 1;
				final int w = Math.min(imp.getWidth(), xMax) - xMin;
				final int n = h * w;

				final int limit;
				if (mask == null)
					limit = (int) (slope * n / bins + 0.5f);
				else
					limit = (int) ((1 + mask.get(x - box.x, y - box.y) / 255.0f * (slope - 1)) * n / bins + 0.5f);

				/* remove left behind values from histogram */
				if (xMin > 0) {
					final int xMin1 = xMin - 1;
					for (int yi = yMin; yi < yMax; ++yi)
						--hist[roundPositive(src.get(xMin1, yi) / 255.0f * bins)];
				}

				/* add newly included values to histogram */
				if (xMax <= imp.getWidth()) {
					final int xMax1 = xMax - 1;
					for (int yi = yMin; yi < yMax; ++yi)
						++hist[roundPositive(src.get(xMax1, yi) / 255.0f * bins)];
				}

				/* clip histogram and redistribute clipped entries */
				System.arraycopy(hist, 0, clippedHist, 0, hist.length);
				int clippedEntries = 0, clippedEntriesBefore;
				do {
					clippedEntriesBefore = clippedEntries;
					clippedEntries = 0;
					for (int i = 0; i <= bins; ++i) {
						final int d = clippedHist[i] - limit;
						if (d > 0) {
							clippedEntries += d;
							clippedHist[i] = limit;
						}
					}

					final int d = clippedEntries / (bins + 1);
					final int m = clippedEntries % (bins + 1);
					for (int i = 0; i <= bins; ++i)
						clippedHist[i] += d;

					if (m != 0) {
						final int s = bins / m;
						for (int i = 0; i <= bins; i += s)
							++clippedHist[i];
					}
				} while (clippedEntries != clippedEntriesBefore);

				/* build cdf of clipped histogram */
				int hMin = bins;
				for (int i = 0; i < hMin; ++i)
					if (clippedHist[i] != 0)
						hMin = i;

				int cdf = 0;
				for (int i = hMin; i <= v; ++i)
					cdf += clippedHist[i];

				int cdfMax = cdf;
				for (int i = v + 1; i <= bins; ++i)
					cdfMax += clippedHist[i];

				final int cdfMin = clippedHist[hMin];

				dst.set(x, y, roundPositive((cdf - cdfMin) / (float) (cdfMax - cdfMin) * 255.0f));
			}

			/* multiply the current row into ip */
			final int t = y * imp.getWidth();
			if (imp.getType() == ImagePlus.GRAY8) {
				for (int x = box.x; x < boxXMax; ++x) {
					final int i = t + x;
					ip.set(i, dst.get(i));
				}
			} else if (imp.getType() == ImagePlus.GRAY16) {
				final int min = (int) ip.getMin();
				for (int x = box.x; x < boxXMax; ++x) {
					final int i = t + x;
					final int v = ip.get(i);
					final float a = (float) dst.get(i) / src.get(i);
					ip.set(i, Math.max(0, Math.min(65535, roundPositive(a * (v - min) + min))));
				}
			} else if (imp.getType() == ImagePlus.GRAY32) {
				final float min = (float) ip.getMin();
				for (int x = box.x; x < boxXMax; ++x) {
					final int i = t + x;
					final float v = ip.getf(i);
					final float a = (float) dst.get(i) / src.get(i);
					ip.setf(i, a * (v - min) + min);
				}
			} else if (imp.getType() == ImagePlus.COLOR_RGB) {
				for (int x = box.x; x < boxXMax; ++x) {
					final int i = t + x;
					final int argb = ip.get(i);
					final float a = (float) dst.get(i) / src.get(i);
					final int r = Math.max(0, Math.min(255, roundPositive(a * ((argb >> 16) & 0xff))));
					final int g = Math.max(0, Math.min(255, roundPositive(a * ((argb >> 8) & 0xff))));
					final int b = Math.max(0, Math.min(255, roundPositive(a * (argb & 0xff))));
					ip.set(i, (r << 16) | (g << 8) | b);
				}
			}
			imp.updateAndDraw();
		}
		return new Imagen(imp.getBufferedImage());
	}

	final static private int roundPositive(float a) {
		return (int) (a + 0.5f);
	}

}
