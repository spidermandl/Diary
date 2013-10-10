package com.diary.goal.setting.tools;

import java.io.InputStream;

import com.diary.goal.setting.DiaryApplication;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class BitmapCustomize {

	static public Bitmap customizePicture(Context con, int resid,int required_width,int required_height,boolean zoom) {
		Bitmap temp = DiaryApplication.getInstance().getBitmap(resid);
		if (temp == null) {
			InputStream is = con.getResources().openRawResource(resid);
			
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			// options.inSampleSize = 10; // width，hight设为原来的十分一
			/**
			 * The function of decodeStream directly call JNI >> nativeDecodeAsset().
			 * Save the memory consumed by Java Dalvik. 
			 */
			BitmapFactory.decodeStream(is, null, options);
			//BitmapFactory.decodeResource(con.getResources(), resid,options);
			//System.gc();
			
			options.inSampleSize=calculateInSampleSize(options, required_width, required_height);
			options.inJustDecodeBounds=false;
			temp=BitmapFactory.decodeStream(is,null,options);
			if(zoom)
				temp=zoomBitmap(temp, required_width, required_height);
			DiaryApplication.getInstance().setBitmap(resid, temp);
		}
		return temp;
	}
	
	public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) width / w);
		float scaleHeight = ((float) height / h);
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		return newbmp;
	}
	
	public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth,int reqHeight) {
		if (reqWidth==0)
			reqWidth=options.outWidth;
		if(reqHeight==0)
			reqHeight=options.outHeight;
		
		final int height=options.outHeight;
		final int width=options.outWidth;
		int inSampleSize=1;
		
		if(height>reqHeight || width>reqWidth){
			final int heightRatio =Math.round((float)height/(float) reqHeight);
			final int widthRatio =Math.round((float)width/(float) reqWidth);
			
			inSampleSize=heightRatio<widthRatio?heightRatio:widthRatio;
			
			final float totalPixels = width * height;
			
			final float totalReqPixelsCap = reqWidth*reqHeight*2;
			
			while(totalPixels / (inSampleSize*inSampleSize)>totalReqPixelsCap){
				inSampleSize++;
			}
		}
		return inSampleSize;
	}
//    /** 
//     * 最大返回maxNumOfPixels = 1280*1280像素的图片 
//     *  
//     */  
//    public static Bitmap getSuitableBitmap(ContentResolver resolver, Uri uri) throws FileNotFoundException {  
//        int maxNumOfPixels = 1280*1280;  
//        BitmapFactory.Options opts = new BitmapFactory.Options();  
//        opts.inJustDecodeBounds = true;  
//        BitmapFactory.decodeStream(resolver.openInputStream(uri), null,opts);  
//        opts.inSampleSize = computeSampleSize(opts, -1, maxNumOfPixels);  
//        opts.inJustDecodeBounds = false;  
//        try {  
//            return BitmapFactory.decodeStream(resolver.openInputStream(uri), null,opts);              
//        } catch (OutOfMemoryError err) {  
//            KdweiboLogger.e("", "", err);  
//        }  
//        return null;  
//    }  
//      
//    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {  
//        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);  
//        int roundedSize;  
//        if (initialSize <= 8) {  
//            roundedSize = 1;  
//            while (roundedSize < initialSize) {  
//                roundedSize <<= 1;  
//            }  
//        } else {  
//            roundedSize = (initialSize + 7) / 8 * 8;  
//        }  
//        return roundedSize;  
//    }  
//      
//    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {  
//        double w = options.outWidth;  
//        double h = options.outHeight;   
//        int lowerBound = (maxNumOfPixels == -1) ? 1 :  (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));  
//        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));  
//      
//        if (upperBound < lowerBound) {  
//            // return the larger one when there is no overlapping zone.  
//            return lowerBound;  
//        }  
//        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {  
//            return 1;  
//        } else if (minSideLength == -1) {  
//            return lowerBound;  
//        } else {  
//            return upperBound;  
//        }  
//      
//    }   
//		private void FreeBitmap(HashMap<Integer, Bitmap> cache){
//		if(cache.isEmpty()){
//		    return;
//		}
//		for(Bitmap bitmap:cache.values()){
//		    if(bitmap != null && !bitmap.isRecycled()){
//		        bitmap.recycle();
//		        System.out.println("=============recycle bitmap=======");
//		        }
//		    }
//		    cache.clear();
//		}
}
