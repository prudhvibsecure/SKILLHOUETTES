package com.sharmastech.skillhouettes.imageloader;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.sharmastech.skillhouettes.R;
import com.sharmastech.skillhouettes.utils.TraceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ImageLoader {

	MemoryCache memoryCache = new MemoryCache();
	FileCache fileCache;
	private Map<ImageView, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());
	ExecutorService executorService;

	private Context context = null;

	private ImageTable table = null;

	private int stub_id = R.mipmap.ic_launcher;

	public ImageLoader(Context context, boolean save) {
		this.context = context;
		fileCache = new FileCache(context, save);
		executorService = Executors.newFixedThreadPool(4);

		table = new ImageTable(context);
	}

	public void setDefaultDrawbale(int drawable) {
		stub_id = drawable;
	}

	public void DisplayImage(String url, ImageView imageView, int defaultSize) {//0 = actual size, 1 = default size, 2 = custom size
		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null) {

			imageView.setImageBitmap(bitmap);
		} else {
			queuePhoto(url, imageView, defaultSize);
			imageView.setImageResource(stub_id);
		}
	}

	public void displayImageDrwable(String url, ImageView imageView, int defaultSize) {
		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null) {

			Resources resources = context.getResources();
			Drawable drawable = new BitmapDrawable(resources, bitmap);

			imageView.setImageDrawable(drawable);
		} else {
			queuePhoto(url, imageView, defaultSize);
			imageView.setImageResource(stub_id);
		}
	}

	private void queuePhoto(String url, ImageView imageView, int actualSize) {
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		p.setActualSize(actualSize);
		executorService.submit(new PhotosLoader(p));
	}

	private Bitmap getBitmap(String url, int actualSize) {
		File f = fileCache.getFile(url);

		Bitmap b = decodeFile(f, actualSize);
		if (b != null)
			return b;

		try {
			Bitmap bitmap;
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl
					.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);

			conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();
			OutputStream os = new FileOutputStream(f);
			CopyStream(is, os);
			os.close();
			bitmap = decodeFile(f, actualSize);

			return bitmap;
		} catch (Exception ex) {
			TraceUtils.logException(ex);
			return null;
		}
	}

	private Bitmap decodeFile(File f, int actualSize) {
		try {
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);
			
			int REQUIRED_SIZE = 70;
			if(actualSize == 1)
				REQUIRED_SIZE = 70;
			if(actualSize == 2)
				REQUIRED_SIZE = 300;

			if(actualSize == 3)
				REQUIRED_SIZE = 500;
			
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			
			if(actualSize != 0)
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);

		} catch (FileNotFoundException e) {
			TraceUtils.logException(e);
		}
		return null;
	}

	private class PhotoToLoad {
		public String url;
		public ImageView imageView;
		public int actualSize  = 0;

		public PhotoToLoad(String u, ImageView i) {
			url = u;
			imageView = i;
		}		
		
		private void setActualSize(int actualSize) {
			this.actualSize = actualSize;						
		}
		
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		public void run() {

			if (imageViewReused(photoToLoad))
				return;

			Bitmap bmp = getBitmap(photoToLoad.url, photoToLoad.actualSize);

			if(photoToLoad.actualSize == 6)
				bmp = getRoundedShape(bmp);

			if (bmp != null) {
				saveImageInfo(photoToLoad.url);
			}

			memoryCache.put(photoToLoad.url, bmp);

			if (imageViewReused(photoToLoad))
				return;

			BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);

			Activity a = photoToLoad.imageView.getContext() instanceof Activity ? (Activity) photoToLoad.imageView.getContext() : (Activity) context;
			a.runOnUiThread(bd);

			/*Activity a = (Activity) photoToLoad.imageView.getContext();
			a.runOnUiThread(bd);*/
		}
	}

	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	private void saveImageInfo(final String image) {
		try {
			new Thread(new Runnable() {

				@Override
				public void run() {
					String link = String.valueOf(image.hashCode());
					if (table != null)
						table.insertORUpdate(link);
				}
			}).start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null)
				photoToLoad.imageView.setImageBitmap(bitmap);
			else
				photoToLoad.imageView.setImageResource(stub_id);
		}
	}

	public void clearCache() {
		memoryCache.clear();
		clearThreads();
	}

	public void clearThreads() {

		try {

		if (!executorService.isShutdown()) {
			List<Runnable> list = executorService.shutdownNow();
			for (int i = 0; i < list.size(); i++) {
				BitmapDisplayer runnable = (BitmapDisplayer) list.get(i);
				imageViews.remove(runnable.photoToLoad.imageView);
			}
		}

		} catch (Exception e) {
			TraceUtils.logException(e);
		}

		if(table != null)
			table.close();
		table = null;
		if(executorService != null)
			executorService.shutdown();
		executorService = null;

	}

	public void clearOnlyThreads() {

		try {

			if (!executorService.isShutdown()) {
				List<Runnable> list = executorService.shutdownNow();
				for (int i = 0; i < list.size(); i++) {
					BitmapDisplayer runnable = (BitmapDisplayer) list.get(i);
					imageViews.remove(runnable.photoToLoad.imageView);
				}
			}
			executorService = Executors.newFixedThreadPool(4);

		} catch (Exception e) {
			TraceUtils.logException(e);
		}

	}

	private void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
			TraceUtils.logException(ex);
		}
	}

	private Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
		int targetWidth = 200;
		int targetHeight = 200;
		Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
				targetHeight, Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(targetBitmap);
		Path path = new Path();
		path.addCircle(((float) targetWidth - 1) / 2,
				((float) targetHeight - 1) / 2,
				(Math.min(((float) targetWidth),
						((float) targetHeight)) / 2),
				Path.Direction.CCW);

		canvas.clipPath(path);
		Bitmap sourceBitmap = scaleBitmapImage;
		canvas.drawBitmap(sourceBitmap,
				new Rect(0, 0, sourceBitmap.getWidth(),
						sourceBitmap.getHeight()),
				new Rect(0, 0, targetWidth, targetHeight), null);
		return targetBitmap;
	}

}