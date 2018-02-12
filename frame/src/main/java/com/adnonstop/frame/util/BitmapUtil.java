package com.adnonstop.frame.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

import static android.graphics.BitmapFactory.decodeStream;

/**
 * Bitmap工具类
 * <p>
 * Created by ALiu on 2017/3/17.
 */

public class BitmapUtil {

    /**
     * 回收Bitmap
     *
     * @param bmp Bitmap
     */
    public static void recycle(Bitmap bmp) {
        if (bmp != null && !bmp.isRecycled()) {
            bmp.recycle();
        }
    }

    /**
     * 判断Bitmap可用
     *
     * @param bmp Bitmap
     * @return tf
     */
    public static boolean isCanUse(Bitmap bmp) {
        return bmp != null && !bmp.isRecycled();
    }

    /**
     * 保存图片
     *
     * @param context 上下文
     * @param bmp     Bitmap
     */
    /**
     * @param context  上下文
     * @param bmp      Bitmap
     * @param dirPath  文件夹路径
     * @param fileName 文件名
     * @param quality  质量
     */
    public static void saveImageToGallery(Context context, Bitmap bmp, String dirPath, String fileName, int quality) {
        saveBitmapFile(bmp, dirPath, fileName, quality);
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(dirPath + File.separator + fileName)));
    }

    /**
     * 插入到系统相册
     *
     * @param context  上下文
     * @param filePath 文件地址
     * @param fileName 文件名字
     */
    public static void insertToSystemAlbum(Context context, String filePath, String fileName) {
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    filePath, fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存图片
     *
     * @param bitmap   Bitmap
     * @param dirPath  文件夹路径
     * @param fileName 文件名
     * @param quality  质量
     */
    public static void saveBitmapFile(Bitmap bitmap, String dirPath, String fileName, int quality) {
        File file = new File(dirPath);//将要保存图片的路径
        if (!file.exists()) {
            file.mkdirs();
        }
        File newFile = new File(dirPath + File.separator + fileName);
        try {
            if (newFile.exists()) {
                boolean isDeleteSuccess = newFile.delete();
                if (isDeleteSuccess) {
                    boolean isCreateSuccess = newFile.createNewFile();
                    if (isCreateSuccess) {
                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(newFile));
                        if (fileName.endsWith("jpg")) {
                            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
                        } else if (fileName.endsWith("png")) {
                            bitmap.compress(Bitmap.CompressFormat.PNG, quality, bos);
                        }
                        bos.flush();
                        bos.close();
                    }
                }
            }
        } catch (IOException e) {
            Log.e("BitmapUtil", "saveBitmapFile:  " + "\t" + "缓存失败" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取宽高
     *
     * @param imgPath 图片路径
     * @return 宽高
     */
    public static int[] getImageWH(String imgPath) {
        int[] wh = {-1, -1};
        if (imgPath == null) {
            return wh;
        }
        File file = new File(imgPath);
        if (file.exists() && !file.isDirectory()) {
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                InputStream is = new FileInputStream(imgPath);
                decodeStream(is, null, options);
                wh[0] = options.outWidth;
                wh[1] = options.outHeight;
            } catch (Exception e) {
                Log.w("fuck", "getImageWH Exception.", e);
            }
        }
        return wh;
    }

    /**
     * 加载指定大小图片 - 硬压缩
     *
     * @param path  Bitmap地址
     * @param scale 缩放大小（整形）
     * @return Bitmap
     */
    public static Bitmap scaleBitmapHard(String path, int scale) {
        Bitmap bm = null;
        try {
            //读取图片
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            options.inSampleSize = scale;
            InputStream is = new FileInputStream(path);
            bm = decodeStream(is, null, options);
        } catch (Exception e) {
            Log.w("fuck", "scaleBitmapHard Exception.", e);
        } catch (OutOfMemoryError e) {
            Log.e("fuck", "scaleBitmapHard OutOfMemoryError.", e);
            bm = scaleBitmapHard(path, scale + 1);
            // out of memory deal..
        }
        return bm;
    }

    /**
     * 加载指定大小图片 - 软压缩
     *
     * @param temp      传入图片
     * @param scaleSize 缩放比例
     * @return Bitmap
     */
    public static Bitmap scaleBitmapSoft(Bitmap temp, float scaleSize) {
        if (!isCanUse(temp)) {
            return null;
        }
        Matrix m = new Matrix();
        m.postScale(scaleSize, scaleSize, 0, 0);
        return Bitmap.createBitmap(temp, 0, 0, temp.getWidth(), temp.getHeight(), m, true);
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 转换为圆形图片
     *
     * @param bitmap Bitmap
     * @return Bitmap
     */
    public static Bitmap circleToBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int min = width < height ? width : height;
            Bitmap output = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            canvas.drawCircle(min / 2, min / 2, min / 2, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, 0, 0, paint);
            return output;
        }
        return null;
    }

    /**
     * 获取圆角图片
     *
     * @param bitmap  Bitmap
     * @param roundPx 圆角半径
     * @return Bitmap
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        if (bitmap == null || bitmap.isRecycled())
            return null;
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * 压缩图片
     *
     * @param bitmap   源图片
     * @param width    想要的宽度
     * @param height   想要的高度
     * @param isAdjust 是否自动调整尺寸, true图片就不会拉伸，false严格按照你的尺寸压缩
     * @return Bitmap
     */
    public static Bitmap reduce(Bitmap bitmap, int width, int height, boolean isAdjust) {
        // 如果想要的宽度和高度都比源图片小，就不压缩了，直接返回原图
        if (bitmap.getWidth() < width && bitmap.getHeight() < height) {
            return bitmap;
        }
        // 根据想要的尺寸精确计算压缩比例, 方法详解：public BigDecimal divide(BigDecimal divisor, int scale, int roundingMode);
        // scale表示要保留的小数位, roundingMode表示如何处理多余的小数位，BigDecimal.ROUND_DOWN表示自动舍弃
        float sx = new BigDecimal(width).divide(new BigDecimal(bitmap.getWidth()), 4, BigDecimal.ROUND_DOWN).floatValue();
        float sy = new BigDecimal(height).divide(new BigDecimal(bitmap.getHeight()), 4, BigDecimal.ROUND_DOWN).floatValue();
        if (isAdjust) {// 如果想自动调整比例，不至于图片会拉伸
            sx = (sx < sy ? sx : sy);
            sy = sx;// 哪个比例小一点，就用哪个比例
        }
        Matrix matrix = new Matrix();
        matrix.postScale(sx, sy);// 调用api中的方法进行压缩，就大功告成了
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}
