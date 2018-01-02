package esia.timewatcher.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.ContextMenu;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;

import esia.timewatcher.R;

public class BitmapUtils {
	private static LinkedList<Bitmap> icons = null;

	public static LinkedList<Bitmap> loadTypeIcons(Context context) {
		if (icons == null) {
			icons = new LinkedList<>();
			TypedArray typedArray = context.getResources().obtainTypedArray(R.array.type_icons);
			for (int i = 0; i < typedArray.length(); ++i) {
				icons.add(drawableToBitmap(context, typedArray.getDrawable(i)));
			}
			typedArray.recycle();
		}
		return new LinkedList<>(icons);
	}

	public static byte[] bitmapToBytes(Bitmap bitmap) {
		assert(bitmap != null);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
		return stream.toByteArray();
	}

	// convert from byte array to bitmap
	public static Bitmap bytesToBitmap(byte[] image) {
		return BitmapFactory.decodeByteArray(image, 0, image.length);
	}

	public static Bitmap drawableToBitmap(Context context, Drawable drawable) {
		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);

		return bitmap;
	}
}
