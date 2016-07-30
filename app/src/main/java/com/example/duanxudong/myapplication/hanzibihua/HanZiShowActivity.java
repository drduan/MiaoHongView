package com.example.duanxudong.myapplication.hanzibihua;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.duanxudong.myapplication.R;
import com.example.duanxudong.myapplication.hanzibihua.data.BihuaPoint;
import com.example.duanxudong.myapplication.hanzibihua.data.OutlinePoint;
import com.example.duanxudong.myapplication.hanzibihua.mode.HanZiModel;
import com.example.duanxudong.myapplication.hanzibihua.utils.ToastUtil;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static com.example.duanxudong.myapplication.hanzibihua.utils.pinyin.cn2Spell;

public class HanZiShowActivity extends BaseActivity implements View.OnClickListener {
	private static String TAG = HanZiShowActivity.class.getSimpleName();
	 Cursor cursor;
	// private List<HanZiModel> listHanZi;
	 private HanZiModel hanZiModel;
	// private int id;
	 private int index=0;
	// 语音合成对象
	// private SpeechSynthesizer mTts;
	// 默认发音人
	private String voicer = "xiaoyan";
	//要显示的汉字
	private String HanZi,outline_path,fill_path,voice;
	 
	private final static int RESULT_HANZIOUTLINE_CODE=0X001;
	private final static int RESULT_HANZIFILL_CODE=0X002;
	private OutlinePoint outlinePoint;
	private BihuaPoint bihuaPoint;
	//HanZiShowAdapter adapter;
	 private MediaPlayer player = null;
	
	
	 private MiaoHongView mMiaoHongView = null;
	 private TextView txt_content,txt_pinyin;
	 private ImageView img_previous,img_next;
	 private Button btn_show,btn_voice;
	 private GridView grv_hanzi;
	 private ProgressBar pb_bar;
	private WebView webView;
	private TextView txt_hanzi;
	private LinearLayout lay_hanzi;
	 
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_hanzishow);
		webView=(WebView)findViewById(R.id.wv1);
		context=this;
    	initView();
    	initData();
    	onClick();


	}
	
	/***
	 * 获取汉字轮廓文件
	 * @param index
	 */
    private void getOutlinePotin(int index){
    	pb_bar.setVisibility(View.GONE);

        HanZi = "一";
		txt_hanzi.setVisibility(View.GONE);
				lay_hanzi.setVisibility(View.VISIBLE);

				try {
						new Thread(new Runnable() {
							@Override
							public void run() {
                                String result = getFromAssets("stroke/one_out.json");
								outlinePoint = new Gson().fromJson(result, OutlinePoint.class);
								Message msg = handler.obtainMessage();
								msg.what = RESULT_HANZIOUTLINE_CODE;
								handler.sendMessage(msg);
							}
						}).start();

				} catch (Exception e) {
					ToastUtil.show(context, "未找到汉字文件!");
					pb_bar.setVisibility(View.GONE);
				}

	}



	/***
	 * 获取汉字填充文件
	 * 
	 * @param index
	 */
	private void getFillPoint(int index) {
        pb_bar.setVisibility(View.GONE);

//

        lay_hanzi.setVisibility(View.VISIBLE);
        txt_hanzi.setVisibility(View.GONE);


        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String result = getFromAssets("stroke/one_fill.json");
                    bihuaPoint = new Gson().fromJson(result, BihuaPoint.class);

                    Message msg = handler.obtainMessage();
                    msg.what = RESULT_HANZIFILL_CODE;
                    handler.sendMessage(msg);
                }
            }).start();
        } catch (Exception e) {
            ToastUtil.show(context, "未找到汉字文件!");
            pb_bar.setVisibility(View.GONE);


        }
    }

    public String getFromAssets(String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

     
    private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==RESULT_HANZIOUTLINE_CODE){
		    	pb_bar.setVisibility(View.GONE);
				webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                webView.loadDataWithBaseURL(null,"11","text/html","utf-8","");
                txt_pinyin.setText("YI");
//				txt_pinyin.setText(listHanZi.get(index).getPinyin());
                txt_pinyin.setText(cn2Spell("一"));

				if(outlinePoint==null){ 
					mMiaoHongView.clearLine();
					return;
				} 
				mMiaoHongView.setOutlinePotin(outlinePoint); 
		    	mMiaoHongView.showOutLine();
                // TODO: 16/7/30  
            }else if(msg.what==RESULT_HANZIFILL_CODE){
		    	pb_bar.setVisibility(View.GONE);
				if(bihuaPoint==null){
					mMiaoHongView.clearMiaoHong();
					return;
				}
				mMiaoHongView.setBiHuaPotin(bihuaPoint);
				mMiaoHongView.startMiaohong();
			} 
		}
    	
    };
 


	@Override
	public void onClick(View v) {
		 switch (v.getId()) {
		case R.id.image_top_left:
			this.finish();
			break;
		case R.id.img_previous:

			break;
		case R.id.img_next:

			break;
		case R.id.btn_voice:
			break;
		case R.id.btn_show:
			if (bihuaPoint == null) {

                // TODO: 16/7/30
				getFillPoint(index);

			} else {
				mMiaoHongView.startMiaohong();
			}
			break;
		default:
			break;
		}
	}
	

	
	@Override
	protected void onDestroy() {

		super.onDestroy();
	}
	
	@Override
	protected void onResume() {
		//移动数据统计分析

		super.onResume();
	}
	@Override
	protected void onPause() {
		//移动数据统计分析

		super.onPause();
	}

	@Override
	public void initData() { 
		// 初始化合成对象

			getOutlinePotin(index);

	}

	@Override
	public void initView() {
		mMiaoHongView = (MiaoHongView)findViewById(R.id.img_hanzi);
    	txt_pinyin=(TextView)findViewById(R.id.txt_pinyin);
		txt_hanzi=(TextView)findViewById(R.id.txt_hanzi);
// TODO: 16/7/30  
		txt_hanzi.setVisibility(View.GONE);
    	img_previous=(ImageView)findViewById(R.id.img_previous);
    	img_next=(ImageView)findViewById(R.id.img_next);
    	btn_show=(Button)findViewById(R.id.btn_show);
    	btn_voice=(Button)findViewById(R.id.btn_voice);
    	grv_hanzi=(GridView)findViewById(R.id.grv_hanzi);
    	pb_bar=(ProgressBar)findViewById(R.id.pb_bar);
    	grv_hanzi.setSelector(new ColorDrawable(Color.TRANSPARENT));
		lay_hanzi=(LinearLayout)findViewById(R.id.lay_hanzi);
		super.setTitle();
		super.txtTitle.setText("生字学习");
		super.btn_right.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onClick() {
		super.imageLeft.setOnClickListener(this);

		img_previous.setOnClickListener(this);
		img_next.setOnClickListener(this);
		btn_voice.setOnClickListener(this);
		btn_show.setOnClickListener(this);
	} 
}
