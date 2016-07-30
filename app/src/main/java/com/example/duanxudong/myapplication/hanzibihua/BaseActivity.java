package com.example.duanxudong.myapplication.hanzibihua;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.duanxudong.myapplication.R;


public abstract class BaseActivity extends Activity implements IBaseActivity{
	protected Context context;
	protected Intent intent;
	protected View view;
	protected ImageView imageLeft;
	protected TextView txtTitle;
	protected Button btn_right;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	protected void setTitle(){
		view=(View)findViewById(R.id.relative_top_title);
		imageLeft=(ImageView)view.findViewById(R.id.image_top_left);
		btn_right=(Button)view.findViewById(R.id.btn_top_right);
		txtTitle=(TextView)view.findViewById(R.id.txt_top_title);
	}
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onClick() {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	
}
