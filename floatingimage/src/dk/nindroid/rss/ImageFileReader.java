package dk.nindroid.rss;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import dk.nindroid.rss.data.Progress;

public class ImageFileReader{

	public static Bitmap readImage(File f, int size, Progress progress){
		String path = f.getAbsolutePath();
		Options opts = new Options();
		setProgress(progress, 10);
		// Get bitmap dimensions before reading...
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, opts);
		int width = opts.outWidth;
		int height = opts.outHeight;
		int largerSide = Math.max(width, height);
		setProgress(progress, 20);
		opts.inJustDecodeBounds = false;
		if(largerSide > size * 2){
			int sampleSize = getSampleSize(size, largerSide);
			opts.inSampleSize = sampleSize;
		}
		Bitmap bmp = BitmapFactory.decodeFile(path, opts);
		setProgress(progress, 60);
		if(bmp == null) return null;
		width = bmp.getWidth();
		height = bmp.getHeight();
		largerSide = Math.max(width, height);
		setProgress(progress, 80);
		if(largerSide > size){
			float scale = (float)size / largerSide;
			Bitmap tmp = Bitmap.createScaledBitmap(bmp, (int)(width * scale), (int)(height * scale), true);
			bmp.recycle();
			bmp = tmp;
		}
		setProgress(progress, 90);
		return bmp;
	}
	
	public static void setProgress(Progress progress, int percent){
		if(progress != null){
			progress.setPercentDone(percent);
		}
	}
	
	private static int getSampleSize(int target, int source){
		int fraction = source / target;
		if(fraction > 32){
			return 32;
		}if(fraction > 16){
			return 16;
		}if(fraction > 8){
			return 8;
		}if(fraction > 4){
			return 4;
		}if(fraction > 2){
			return 2;
		}
		return 1;
	}
}
