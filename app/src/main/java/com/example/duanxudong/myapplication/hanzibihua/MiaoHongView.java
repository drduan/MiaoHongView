package com.example.duanxudong.myapplication.hanzibihua;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.duanxudong.myapplication.hanzibihua.data.BihuaPoint;
import com.example.duanxudong.myapplication.hanzibihua.data.OutlinePoint;
import com.example.duanxudong.myapplication.hanzibihua.data.Point;
import com.example.duanxudong.myapplication.hanzibihua.data.Stroke;

import java.util.List;

import static com.example.duanxudong.myapplication.hanzibihua.utils.DensityUtil.px2dip;


/**
 * 描红自定义View，包括：显示生字轮廓、描红演示和手写三个功能
 * Demo:
	// 初始化
	private MiaoHongView mMiaoHongView = null;
	mMiaoHongView = (MiaoHongView)findViewById(R.id.miaohongViewId);
	mMiaoHongView.setOnMiaoHongViewListener(new OnMiaoHongViewListener{
		// 回调
	});
	// 延时一会再显示轮廓，或者在上层传view的宽高进来就可以在初始化的时候立即显示了
	mMiaoHongView.showOutLine();
	
	// 开始描红
	mMiaoHongView.startMiaohong();
	// 开始手写
	mMiaoHongView.startWrite();
 * */
public class MiaoHongView extends View {
	private String TAG="MiaoHongView";
	private OnMiaoHongViewListener mOnMiaoHongViewListener = null;
	private OutlinePoint outlinePoint;
	private BihuaPoint bihuaPoint;
	// 视图的宽高
	private int mViewWidth = 0, mViewHeight = 0;
	// 轮廓的绘制信息
	private DrawInfo mOutlineDrawInfo = new DrawInfo();
	// 描红的绘制信息
	private DrawInfo mMiaohongDrawInfo = new DrawInfo();
	// 手写的绘制信息
	private DrawInfo mWriteDrawInfo = new DrawInfo();
	// 视图状态，分：普通、描红和手写三种状态，视图的显示可以根据状态来控制
	private static final int STATE_NORMAL = 0x01;
	private static final int STATE_MIAOHONG = 0x02;
	private static final int STATE_WRITING = 0x03;
	private int mState = STATE_NORMAL;
	private Context context;
	private float mSize;
	//汉字控件宽度，用于缩放字体大小 
	private int width=200;
	
	
	public MiaoHongView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context=context;
	}

	public MiaoHongView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
	}

	public MiaoHongView(Context context) {
		super(context);
		this.context=context;
	}
	
	/**
	 * 获取View的宽高、初始化各个视图
	 * 
	 * */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		//mViewWidth = widthMeasureSpec;
		//mViewHeight = heightMeasureSpec;

		int mViewWidth= Math.min(getMeasuredWidth(), getMeasuredHeight());
		
		mOutlineDrawInfo.init(mViewWidth, mViewWidth);
		mMiaohongDrawInfo.init(mViewWidth, mViewWidth);
		mWriteDrawInfo.init(mViewWidth, mViewWidth);
	}
	
	/**
	 * 根据不同状态显示不同的bitmap
	 * 
	 * */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		switch(mState){
		case STATE_NORMAL:{
			// 只显示轮廓
			if(null != mOutlineDrawInfo.bitmap && !mOutlineDrawInfo.bitmap.isRecycled()){
				canvas.drawBitmap(mOutlineDrawInfo.bitmap, 0, 0, null);
			}
		}
		break;
		case STATE_MIAOHONG:{
			// 显示轮廓和描红笔迹
			if(null != mOutlineDrawInfo.bitmap && !mOutlineDrawInfo.bitmap.isRecycled()){
				canvas.drawBitmap(mOutlineDrawInfo.bitmap, 0, 0, null);
			}
			
			if(null != mMiaohongDrawInfo.bitmap && !mMiaohongDrawInfo.bitmap.isRecycled()){
				canvas.drawBitmap(mMiaohongDrawInfo.bitmap, 0, 0, null);
			}
		}
		break;
		case STATE_WRITING:{
			// 显示轮廓和手写笔迹，允许手写
			if(null != mOutlineDrawInfo.bitmap && !mOutlineDrawInfo.bitmap.isRecycled()){
				canvas.drawBitmap(mOutlineDrawInfo.bitmap, 0, 0, null);
			}
			
			if(null != mWriteDrawInfo.bitmap && !mWriteDrawInfo.bitmap.isRecycled()){
				canvas.drawBitmap(mWriteDrawInfo.bitmap, 0, 0, null);
			}
		}
		break;
		default:{
			
		}
		break;
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(mState != STATE_WRITING){
			return true;
		}
		
		// 这里做手写功能
		return super.onTouchEvent(event);
	} 
	
	
	/**
	 * 设置描红回调
	 * 
	 * */ 
	public void setOnMiaoHongViewListener(OnMiaoHongViewListener onMiaoHongViewListener){
		mOnMiaoHongViewListener = onMiaoHongViewListener;
	}
	 
	/**
	 * 设置轮廓数据
	 * @param outlinePoint
	 */
	public void setOutlinePotin(OutlinePoint outlinePoint){
		this.outlinePoint=outlinePoint;
	}
	
	/**
	 * 笔画填充
	 * @param bihuaPoint
	 */
	public void setBiHuaPotin(BihuaPoint bihuaPoint){
		this.bihuaPoint=bihuaPoint;
	} 
	
	
	/**
	 * 开始描红
	 * 
	 * */
	public void startMiaohong(){
		// 停止所有正在进行的工作，以便开始执行描红
		stopAll();
		// 发送开始描红的回调
		if(null != mOnMiaoHongViewListener){
			mOnMiaoHongViewListener.onStartMiaohong();
		}
		// 得到描红信息
		mState = STATE_MIAOHONG; 
		// 开始描红
		startMiaohong(bihuaPoint);
	}
	
	/**
	 * 停止描红
	 * 
	 * */
	public void stopMiaoHong(){
		if(null != mOnMiaoHongViewListener){
			mOnMiaoHongViewListener.onStopMiaohong();
		}
		
		mState = STATE_NORMAL;
	}
	
	/**
	 * 开始手写
	 * 
	 * */
	public void startWriting(){
		// 停止所有正在进行的工作，以便开始执行手写
		stopAll();
		// 发送开始手写的回调
		if(null != mOnMiaoHongViewListener){
			mOnMiaoHongViewListener.onStartWrite();
		}
		
		// 开始手写
		mState = STATE_WRITING;
		mWriteDrawInfo.canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
	}
	
	/**
	 * 停止手写
	 * 
	 * */
	public void stopWriting(){
		if(null != mOnMiaoHongViewListener){
			mOnMiaoHongViewListener.onStopWrite();
		}
		
		mState = STATE_WRITING;
	}
	
	/**
	 * 停止所有正在进行的工作
	 * 
	 * */
	private void stopAll(){
		mState = STATE_NORMAL;
	}
	
	public void clearLine(){
		mOutlineDrawInfo.canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
	}
	
	public void clearMiaoHong(){
		mMiaohongDrawInfo.canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
	}
	
	/**
	 * 显示汉字轮廓
	 * 
	 * */
	public void showOutLine(){ 
		if (outlinePoint == null) {
			postInvalidate();
			return;
		}
		try {
			mState = STATE_NORMAL;
			mSize =width/ px2dip(context, outlinePoint.getmWordWidth());
			new Thread(new Runnable() {
				@Override
				public void run() {
					if(mOutlineDrawInfo!=null){
						// TODO: 16/7/30  
						mOutlineDrawInfo.canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
						List<Point> outLinePointList =outlinePoint.getOutLinePointList();
						for(Point point : outLinePointList){
							mOutlineDrawInfo.canvas.drawPoint(point.getX()*mSize, point.getY()*mSize, mOutlineDrawInfo.paint);
						} 
					}
					postInvalidate();
				}
			}).start();
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	} 
	
	private void startMiaohong(final BihuaPoint bihuaPoint){
		// 这里可以用线程绘制，调用postInvalidate来刷新，为了便于停止描红，可以考虑使用Timer和TimerTask，而不是thread
		new Thread(new Runnable() {
			@Override
			public void run() { 
				// for循环开始，描红每一笔
				mMiaohongDrawInfo.canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
				mMiaohongDrawInfo.setStrokeWidth(2);
				for(Stroke storke : bihuaPoint.getStorkeList()){
					// 描红每一笔
					drawBihua(storke);
				}
			}
		}).start();
	}
	
	private void drawBihua(Stroke stroke){
		// 每隔200个点刷新一次，刷新时休眠50毫秒，这里可以根据描红汉字的宽高、笔画点信息的多少自行调节，通过这两个参数可以控制演示的快慢
		final int DELAY_POINT = 200;
		final int DELAY_TIME =12;
		
		// 字不可能是直接显示在视图的左上角的，想让字在视图中居中或者是显示在其它位置，可以在drawPoint中加上位置的偏移offsetX、offsetY
		List<Point> bihuaPointList = stroke.getBihuaPointList();
		for(int index = 0; index < bihuaPointList.size(); index++){
			//mMiaohongDrawInfo.canvas.drawPoint(bihuaPointList.get(index).getX(), bihuaPointList.get(index).getY(), mMiaohongDrawInfo.paint);
			mMiaohongDrawInfo.canvas.drawPoint(bihuaPointList.get(index).getX()*mSize, bihuaPointList.get(index).getY()*mSize, mMiaohongDrawInfo.paint); 
			if(index%DELAY_POINT == 0){
				try {
					Thread.currentThread();
					Thread.sleep(DELAY_TIME);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				postInvalidate();
			}
		}
		
		// bihuaPointList的size不可能刚好可以对DELAY_POINT取模为0，所以这里还需要再刷新一次
		postInvalidate();
	}
 
	
	/**
	 * 停止语音播报
	 * 
	 * */ 
	private void stopVoice(){
		
	} 
	
	/**
	 * 执行View的销毁工作
	 * 
	 * */
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		stopVoice();
		if(!mOutlineDrawInfo.bitmap.isRecycled()){
			mOutlineDrawInfo.bitmap.recycle();
		}
		
		if(!mWriteDrawInfo.bitmap.isRecycled()){
			mWriteDrawInfo.bitmap.recycle();
		}
		
		if(!mMiaohongDrawInfo.bitmap.isRecycled()){
			mMiaohongDrawInfo.bitmap.recycle();
		}
	}
	
	/**
	 * 绘制信息
	 * 
	 * */
	class DrawInfo{
		Bitmap bitmap = null;
		Canvas canvas = null;
		Paint paint = null;
		
		public void init(int width, int height){
			bitmap = Bitmap.createBitmap(width, height, Config.ARGB_4444);
			canvas = new Canvas();
			paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			//设置画笔的颜色
			paint.setColor(Color.RED);
			//设置画笔风格
			paint.setStyle(Paint.Style.FILL);
			// 设置外边缘
			paint.setStrokeJoin(Paint.Join.ROUND);
			// 形状
			paint.setStrokeCap(Paint.Cap.ROUND);
			canvas.setBitmap(bitmap);
		}
		
		public void setColor(int color){
			paint.setColor(color);
		}
		
		public void setStrokeWidth(int strokeWidth){
			paint.setStrokeWidth(strokeWidth);
		}
	}
}
