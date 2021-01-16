package lava.core.clip;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.IOException;

import lava.core.R;


/**
 * 博客地址：http://blog.csdn.net/gdutxiaoxu
 * wuxi: 暂时不要转换为kotlin，会丢失最小缩放比例的特性，导致缩放可以小于裁剪框
 */
public class ClipViewLayout extends RelativeLayout {

    private static final String TAG = "ClipViewLayout";

    //裁剪原图
    private ImageView mImageView;
    //裁剪框
    private ClipView mClipView;
    //裁剪框水平方向间距，xml布局文件中指定
    private float mHorizontalPadding;
    //裁剪框垂直方向间距，计算得出
    private float mVerticalPadding;
    //图片缩放、移动操作矩阵
    private Matrix mMatrix = new Matrix();
    //图片原来已经缩放、移动过的操作矩阵
    private Matrix mSavedMatrix = new Matrix();
    //动作标志：无
    private static final int NONE = 0;
    //动作标志：拖动
    private static final int DRAG = 1;
    //动作标志：缩放
    private static final int ZOOM = 2;
    //初始化动作标志
    private int mode = NONE;
    //记录起始坐标
    private PointF mStart = new PointF();
    //记录缩放时两指中间点坐标
    private PointF mMid = new PointF();
    private float mOldDist = 1f;
    //用于存放矩阵的9个值
    private final float[] mMatrixValues = new float[9];
    //最小缩放比例
    private float mMinScale;
    //最大缩放比例
    private float mMaxScale = 4;

    private int mPreViewW;
    private int mPreViewH;
    private int mWidthPixels;
    private int mHeightPixels;
    private Bitmap sourceBitmap = null;


    public ClipViewLayout(Context context) {
        this(context, null);
    }

    public ClipViewLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    //初始化控件自定义的属性
    public void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ClipViewLayout);

        //获取剪切框距离左右的边距, 默认为50dp
        mHorizontalPadding = array.getDimensionPixelSize(R.styleable.ClipViewLayout_horizontalPadding,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics()));
        //获取裁剪框边框宽度，默认1dp
        int clipBorderWidth = array.getDimensionPixelSize(R.styleable.ClipViewLayout_clipBorderWidth,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()));
        //裁剪框类型(圆或者矩形)
        int clipType = array.getInt(R.styleable.ClipViewLayout_clipType, 2);

        //回收
        array.recycle();


        mClipView = new ClipView(context);
        mClipView.setClipType(clipType, 1);

        //设置剪切框边框
        mClipView.setClipBorderWidth(clipBorderWidth);
        //设置剪切框水平间距
        mClipView.setmHorizontalPadding(mHorizontalPadding);
        mImageView = new ImageView(context);
        //相对布局布局参数
        android.view.ViewGroup.LayoutParams lp = new LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        this.addView(mImageView, lp);
        this.addView(mClipView, lp);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        mWidthPixels = displayMetrics.widthPixels;
        mHeightPixels = displayMetrics.heightPixels;
        mPreViewW = (int) (mWidthPixels - mHorizontalPadding * 2);
    }


    /**
     * 初始化图片
     */
    public void setImageSrc(final Bitmap bitmap) {
        initSrcPic(bitmap);

        //需要等到imageView绘制完毕再初始化原图
//        ViewTreeObserver observer = mImageView.getViewTreeObserver();
//        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            public void onGlobalLayout() {
//                initSrcPic(path);
//                mImageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//            }
//        });
    }

    /**
     * 初始化图片
     * step 1: decode 出 720*1280 左右的照片  因为原图可能比较大 直接加载出来会OOM
     * step 2: 将图片缩放 移动到imageView 中间
     */
    public void initSrcPic(Bitmap bitmap) {
//        if (uri == null) {
//            return;
//        }
//
//        String path = FileUtil.getRealFilePathFromUri(getContext(), uri);
//        Log.i("evan", "**********clip_view path*******  " + path);
//        if (TextUtils.isEmpty(path)) {
//            return;
//        }

//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(path, options); // 此时返回的bitmap为null

//        int w = options.outWidth;
//        int h = options.outHeight;

//        Bitmap bitmap = decodeSampledBitmap(path, w > mWidthPixels ? mWidthPixels : w, h > mHeightPixels ? mHeightPixels : h);
//        Bitmap bitmap = BitmapFactory.decodeFile(path);
//        Bitmap bitmap = null;

//        try {
//            bitmap = Glide.with(getContext()).asBitmap().load(path).submit().get();
//        } catch (Exception e){
//            e.printStackTrace();
//        }

        if (bitmap == null) {
            return;
        }
        sourceBitmap = bitmap;

        //竖屏拍照的照片，直接使用的话，会旋转90度，下面代码把角度旋转过来
//        int rotation = getExifOrientation(path); //查询旋转角度
//        if (rotation != 90) {
//            Matrix m = new Matrix();
//            m.setRotate(rotation);
//            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
//        }

        //图片的缩放比
        float scaleX;
        float scaleY;
        if (bitmap.getWidth() >= bitmap.getHeight()) {//宽图
            scaleX = (float) mImageView.getWidth() / bitmap.getWidth();
            //如果高缩放后小于裁剪区域 则将裁剪区域与高的缩放比作为最终的缩放比
            Rect rect = mClipView.getClipRect();
            //高的最小缩放比
            mMinScale = Math.max(rect.height() / (float) bitmap.getHeight(), rect.width() / (float) bitmap.getWidth());
            if (scaleX < mMinScale) {
                scaleX = mMinScale + 0.05f;
            }
        } else {//高图
            //高的缩放比
            scaleX = (float) mImageView.getHeight() / bitmap.getHeight();
            //如果宽缩放后小于裁剪区域 则将裁剪区域与宽的缩放比作为最终的缩放比
            Rect rect = mClipView.getClipRect();
            //宽的最小缩放比
            mMinScale = Math.max(rect.height() / (float) bitmap.getHeight(), rect.width() / (float) bitmap.getWidth());
            if (scaleX < mMinScale) {
                scaleX = mMinScale + 0.05f;
            }
        }
        scaleY = scaleX;
        // 缩放
        mMatrix.postScale(scaleX, scaleY);
        // 平移,将缩放后的图片平移到imageview的中心


        //imageView的中心x
        int midX = mImageView.getWidth() / 2;
        //imageView的中心y
        int midY = mImageView.getHeight() / 2;
        //bitmap的中心x
        int imageMidX = (int) (bitmap.getWidth() * scaleX / 2);
        //bitmap的中心y
        int imageMidY = (int) (bitmap.getHeight() * scaleY / 2);
        mMatrix.postTranslate(midX - imageMidX, midY - imageMidY);
        mImageView.setScaleType(ImageView.ScaleType.MATRIX);
        mImageView.setImageMatrix(mMatrix);
        mImageView.setImageBitmap(bitmap);
    }

    public Bitmap decodeSampledBitmap(String filePath, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        int inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        Log.i(TAG, "decodeSampledBitmap: inSampleSize = " + inSampleSize);
        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            int ratio = heightRatio < widthRatio ? heightRatio : widthRatio;
            // inSampleSize只能是2的次幂  将ratio就近取2的次幂的值
            if (ratio < 3)
                inSampleSize = ratio;
            else if (ratio < 6.5)
                inSampleSize = 4;
            else if (ratio < 8)
                inSampleSize = 8;
            else
                inSampleSize = ratio;
        }

        return inSampleSize;
    }

    /**
     * 查询图片旋转角度
     */
    public static int getExifOrientation(String filepath) {// YOUR MEDIA PATH AS STRING
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
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

            }
        }
        return degree;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "onTouchEvent: ACTION_DOWN");
                mSavedMatrix.set(mMatrix);
                //设置开始点位置
                mStart.set(event.getX(), event.getY());
                mode = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                //开始放下时候两手指间的距离
                mOldDist = spacing(event);
                if (mOldDist > 10f) {
                    mSavedMatrix.set(mMatrix);
                    midPoint(mMid, event);
                    mode = ZOOM;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "onTouchEvent: mode =" + mode);
                if (mode == DRAG) { //拖动
                    mMatrix.set(mSavedMatrix);
                    float dx = event.getX() - mStart.x;
                    float dy = event.getY() - mStart.y;

                    mVerticalPadding = mClipView.getClipRect().top;
                    mMatrix.postTranslate(dx, dy);
                    //检查边界
                    checkBorder();
                } else if (mode == ZOOM) { //缩放
                    //缩放后两手指间的距离
                    float newDist = spacing(event);
                    if (newDist > 10f) {
                        //手势缩放比例
                        float scale = newDist / mOldDist;
                        if (scale < 1) { //缩小
                            if (getScale() > mMinScale) {
                                mMatrix.set(mSavedMatrix);
                                mVerticalPadding = mClipView.getClipRect().top;
                                mMatrix.postScale(scale, scale, mMid.x, mMid.y);
                                //缩放到最小范围下面去了，则返回到最小范围大小
                                while (getScale() < mMinScale) {
                                    //返回到最小范围的放大比例
                                    scale = 1 + 0.01F;
                                    mMatrix.postScale(scale, scale, mMid.x, mMid.y);
                                }
                            }
                            //边界检查
                            checkBorder();
                        } else { //放大
                            if (getScale() <= mMaxScale) {
                                mMatrix.set(mSavedMatrix);
                                mVerticalPadding = mClipView.getClipRect().top;
                                mMatrix.postScale(scale, scale, mMid.x, mMid.y);
                            }
                        }
                    }
                }
                mImageView.setImageMatrix(mMatrix);
                break;
        }
        return true;
    }

    /**
     * 根据当前图片的Matrix获得图片的范围
     */
    private RectF getMatrixRectF(Matrix matrix) {
        RectF rect = new RectF();
        Drawable d = mImageView.getDrawable();
        if (null != d) {
            rect.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            matrix.mapRect(rect);
        }
        return rect;
    }

    /**
     * 边界检测
     */
    private void checkBorder() {
        RectF rect = getMatrixRectF(mMatrix);
        float deltaX = 0;
        float deltaY = 0;
        int width = mImageView.getWidth();
        int height = mImageView.getHeight();
        // 如果宽或高大于屏幕，则控制范围 ; 这里的0.001是因为精度丢失会产生问题，但是误差一般很小，所以我们直接加了一个0.01
        if (rect.width() + 0.01 >= width - 2 * mHorizontalPadding) {
            // 图片矩阵的最左边 > 裁剪边框的左边
            if (rect.left > mHorizontalPadding) {
                deltaX = -rect.left + mHorizontalPadding;
            }
            // 图片矩阵的最右边 < 裁剪边框的右边
            if (rect.right < width - mHorizontalPadding) {
                deltaX = width - mHorizontalPadding - rect.right;
            }
        }
        if (rect.height() + 0.01 >= height - 2 * mVerticalPadding) {
            // 图片矩阵的 top > 裁剪边框的 top
            if (rect.top > mVerticalPadding) {
                deltaY = -rect.top + mVerticalPadding;
            }
            // 图片矩阵的 bottom < 裁剪边框的 bottom
            if (rect.bottom < height - mVerticalPadding) {
                deltaY = height - mVerticalPadding - rect.bottom;
            }
        }

        Log.i(TAG, "checkBorder: deltaX=" + deltaX + " deltaY = " + deltaY);

        mMatrix.postTranslate(deltaX, deltaY);
    }

    /**
     * 获得当前的缩放比例
     */
    public final float getScale() {
        mMatrix.getValues(mMatrixValues);
        return mMatrixValues[Matrix.MSCALE_X];
    }

    /**
     *
     * @param clipType
     * @param ratio 高/宽 比例
     */
    public void setClipType(int clipType, float ratio) {
        if (mClipView != null) {
            mClipView.setClipType(clipType, ratio);
            mPreViewH = (int) (mPreViewW * ratio);
        }
    }


    /**
     * 多点触控时，计算最先放下的两指距离
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * 多点触控时，计算最先放下的两指中心坐标
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }


    /**
     * 获取剪切图
     */
    public Bitmap clip() {
//        mImageView.setDrawingCacheEnabled(true);
//        mImageView.buildDrawingCache();
        Rect rect = mClipView.getClipRect();
        Bitmap cropBitmap = null;
        Bitmap zoomedCropBitmap = null;
        try {
            mMatrix.getValues(mMatrixValues);
//            float transX = mMatrixValues[Matrix.MTRANS_X];
//            float transY = mMatrixValues[Matrix.MTRANS_Y];
            float scaleX = mMatrixValues[Matrix.MSCALE_X];
            float scaleY = mMatrixValues[Matrix.MSCALE_Y];

            int width = mImageView.getWidth();
            int height = mImageView.getHeight();

            RectF imageRect = getMatrixRectF(mMatrix);
            int left = (int) (Math.abs(mHorizontalPadding - imageRect.left) / scaleY);
            int right = (int) (Math.abs(Math.min(imageRect.right - imageRect.left, width - mHorizontalPadding - imageRect.left)) / scaleY);
            int top = (int) (Math.abs(mVerticalPadding - imageRect.top) / scaleY);
            int bottom = (int) (Math.abs(Math.min(imageRect.bottom - imageRect.top, height - mVerticalPadding - imageRect.top)) / scaleY);

            BitmapDrawable bitmapDrawable = (BitmapDrawable)mImageView.getDrawable();
            sourceBitmap = sourceBitmap == null ? sourceBitmap : bitmapDrawable.getBitmap();

//            PLog.i("23333333333", "left: " + left + " right: " + right + " top: " + top + " bottom: " + bottom + " width: " + width + " height: " + height);
//            PLog.i("23333333333 imageRect", "left: " + imageRect.left + " right: " + imageRect.right + " top: " + imageRect.top + " bottom: " + imageRect.bottom);
//            PLog.i("23333333333 sourceBitmap", "width: " + sourceBitmap.getWidth() + " height: " + sourceBitmap.getHeight() + " scaleX: " + scaleX + " scaleY: " + scaleY);
//            PLog.i("23333333333", "transX: " + transX + " transY: " + transY + " scaleX: " + scaleX + " scaleY: " + scaleY + "minScale: " + mMinScale);

//            I/23333333333: left: 10 right: 469 top: 0 bottom: 692 width: 480 height: 692
//            I/23333333333 imageRect: left: 84.73837 right: 388.83762 top: 105.0 bottom: 589.65814
//            I/23333333333 sourceBitmap: width: 320 height: 510 scaleX: 0.9503101 scaleY: 0.9503101

//            int scaleWidth = right - left;
//            if (scaleWidth > sourceBitmap.getWidth()) {
//                left = sourceBitmap.getWidth();
//            }

            Matrix matrix = new Matrix();
            float scaleWidth = ((float) width / (right - left) );
            float scaleHeight = ((float) (bottom - top) / height);
            Log.i(TAG, "zoomBitmap: scaleWidth =" + scaleWidth + " scaleHeight = " + scaleHeight);
//            matrix.postScale(scaleWidth, scaleHeight);
            matrix.postScale(scaleWidth, scaleWidth);

            zoomedCropBitmap = Bitmap.createBitmap(4000, 4000, Bitmap.Config.RGB_565);
//            zoomedCropBitmap = Bitmap.createBitmap(sourceBitmap, left, top, right - left, bottom - top, matrix, true);
////            zoomedCropBitmap = zoomBitmap(cropBitmap, right - left, bottom - top);


//            cropBitmap = Bitmap.createBitmap(mImageView.getDrawingCache(), rect.left, rect.top, rect.width(), rect.height());
//            // 对图片进行压缩，这里因为 mPreViewW 与宽高是相等的，所以压缩比例是 1:1，可以根据需要自己调整cd
//            zoomedCropBitmap = zoomBitmap(cropBitmap, mPreViewW, mPreViewH);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        }
        if (cropBitmap != null) {
            cropBitmap.recycle();
        }
        // 释放资源
//        mImageView.destroyDrawingCache();
        return zoomedCropBitmap;
    }

    public Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);
        Log.i(TAG, "zoomBitmap: scaleWidth =" + scaleWidth + " scaleHeight = " + scaleHeight);
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
    }

    /**
     * 用于开启子进程进行裁剪，避开单进程内存耗尽问题
     * @return 裁剪相关参数
     */
    public Intent getClipParams() {
        float scaleX = mMatrixValues[Matrix.MSCALE_X];
        float scaleY = mMatrixValues[Matrix.MSCALE_Y];

        int width = mImageView.getWidth();
        int height = mImageView.getHeight();

        RectF imageRect = getMatrixRectF(mMatrix);
        int left = (int) (Math.abs(mHorizontalPadding - imageRect.left) / scaleY);
        int right = (int) (Math.abs(Math.min(imageRect.right - imageRect.left, width - mHorizontalPadding - imageRect.left)) / scaleY);
        int top = (int) (Math.abs(mVerticalPadding - imageRect.top) / scaleY);
        int bottom = (int) (Math.abs(Math.min(imageRect.bottom - imageRect.top, height - mVerticalPadding - imageRect.top)) / scaleY);

        Intent intent = new Intent();
        intent.putExtra("left", left);
        intent.putExtra("right", right);
        intent.putExtra("top", top);
        intent.putExtra("bottom", bottom);
        intent.putExtra("width", width);
        intent.putExtra("height", height);
        return intent;
    }

}
