package com.example.wiidroid;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;

public class SteeringWheel extends Activity {
	SensorManager mySensorManager;//SensorManager对象引用	
	Sensor myAccelerometer; //加速度传感器
	/*TextView tvX;	//TextView对象引用	
	TextView tvY;	//TextView对象引用	
	TextView tvZ;	//TextView对象引用
	TextView info;*/
	TextView direc;
	int state=0;
	String connip;
	Button UpButton,SpaceButton,DownButton,EnterButton;
	//SocketClient client;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_swheel);
		Intent intent = getIntent();
		connip = intent.getStringExtra("socketip");
		//client = new SocketClient(connip,12345);
		
		//tvX = (TextView)findViewById(R.id.tvX);	//用于显示x轴方向加速度
        //tvY = (TextView)findViewById(R.id.tvY);	//用于显示y轴方向加速度	
        //tvZ = (TextView)findViewById(R.id.tvZ); //用于显示z轴方向加速度
        //info= (TextView)findViewById(R.id.info);//用于显示手机中加速度传感器的相关信息
		direc=(TextView)findViewById(R.id.fullscreen_content);
        mySensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);//获得SensorManager对象	
        myAccelerometer=mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);        
        /*String str="\n名字: "+myAccelerometer.getName()+"\n电池 :"+myAccelerometer.getPower()+
        "\n类型 :"+myAccelerometer.getType()+"\nVendor: "+myAccelerometer.getVendor()+
        "\n版本: "+myAccelerometer.getVersion()+"\n幅度: "+myAccelerometer.getMaximumRange()+
        "\nIP: "+connip;
        info.setText(str);//将信息字符串赋予名为info的TextView*/
        UpButton = (Button)findViewById(R.id.button2); 
        DownButton = (Button)findViewById(R.id.Button01);
        SpaceButton = (Button)findViewById(R.id.button1);
        EnterButton = (Button)findViewById(R.id.button3);
        UpButton.setOnTouchListener(
        	new OnTouchListener(){
        		public boolean onTouch(View view, MotionEvent event) {
        			switch(event.getAction()){
        				case MotionEvent.ACTION_DOWN:
        					connsendmsg("1 0 038",connip);
        					break;
        				case MotionEvent.ACTION_UP:
        					connsendmsg("1 1 038",connip);
        					break;
        			}
        			return false;
        		}
        	}
        );
        DownButton.setOnTouchListener(
            new OnTouchListener(){
            	public boolean onTouch(View view, MotionEvent event) {
            		switch(event.getAction()){
            			case MotionEvent.ACTION_DOWN:
            				connsendmsg("1 0 040",connip);
            				break;
            			case MotionEvent.ACTION_UP:
            				connsendmsg("1 1 040",connip);
            				break;
            		}
            		return false;
            	}
            }
        );
        SpaceButton.setOnTouchListener(
           	new OnTouchListener(){
           		public boolean onTouch(View view, MotionEvent event) {
           			switch(event.getAction()){
           				case MotionEvent.ACTION_DOWN:
           					connsendmsg("1 0 032",connip);
           					break;
           				case MotionEvent.ACTION_UP:
           					connsendmsg("1 1 032",connip);
           					break;
           			}
           			return false;
           		}
           	}
        );
        EnterButton.setOnTouchListener(
               	new OnTouchListener(){
               		public boolean onTouch(View view, MotionEvent event) {
               			switch(event.getAction()){
               				case MotionEvent.ACTION_DOWN:
               					connsendmsg("1 0 013",connip);
               					break;
               				case MotionEvent.ACTION_UP:
               					connsendmsg("1 1 013",connip);
               					break;
               			}
               			return false;
               		}
               	}
            );
	}
    @Override
	protected void onResume(){ //重写onResume方法
		super.onResume();
		mySensorManager.registerListener(mySensorListener, myAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}	
	@Override
	protected void onPause(){//重写onPause方法	
		super.onPause();
		mySensorManager.unregisterListener(mySensorListener);//取消注册监听器
	}
	private SensorEventListener mySensorListener = 
		new SensorEventListener(){//开发实现了SensorEventListener接口的传感器监听器
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy){}
		@Override
		public void onSensorChanged(SensorEvent event){
			float []values=event.values;//获取三个轴方向感上的加速度值
			//tvX.setText("x轴方向上的加速度为："+values[0]);		
			//tvY.setText("y轴方向上的加速度为："+values[1]);		
			//tvZ.setText("z轴方向上的加速度为："+values[2]);	
			if ((values[1] > 2)&&(state == 0)) {
				//client.sendMsg("1 0 39");
				connsendmsg("1 0 039",connip);
				state = 1;
				//tvY.setText("y轴方向上的加速度为："+values[1]+"右转");	
				direc.setText("→");
			}
			if ((values[1] > 2)&&(state == -1)) {
				connsendmsg("1 1 037",connip);
				connsendmsg("1 0 039",connip);
				state = 1;
				//tvY.setText("y轴方向上的加速度为："+values[1]+"右转");	
				direc.setText("→");
			}
			if ((values[1] < 2)&&(values[1] > -2)&&(state == 1)) {
				//client.sendMsg("1 1 39");
				connsendmsg("1 1 039",connip);
				state = 0;
				//tvY.setText("y轴方向上的加速度为："+values[1]+"右回中");	
				direc.setText("↑");
			}
			if ((values[1] < -2)&&(state == 0)) {
				//client.sendMsg("1 0 37");
				connsendmsg("1 0 037",connip);
				state = -1;
				//tvY.setText("y轴方向上的加速度为："+values[1]+"左转");	
				direc.setText("←");
			}
			if ((values[1] < -2)&&(state == 1)) {
				connsendmsg("1 1 039",connip);
				connsendmsg("1 0 037",connip);
				state = -1;
				//tvY.setText("y轴方向上的加速度为："+values[1]+"左转");	
				direc.setText("←");
			}
			if ((values[1] < 2)&&(values[1] > -2)&&(state == -1)) {
				//client.sendMsg("1 1 37");
				connsendmsg("1 1 037",connip);
				state = 0;
				//tvY.setText("y轴方向上的加速度为："+values[1]+"左回中");	
				direc.setText("↑");
			}
			/*if (values[1] > 3) {
				//client.sendMsg("1 0 39");
				connsendmsg("1 0 039",connip);
				direc.setText("方向盘模式\n→");
				//state = 1;
				//tvY.setText("y轴方向上的加速度为："+values[1]+"右转");	
			}
			if (values[1] < -3) {
				//client.sendMsg("1 0 37");
				connsendmsg("1 0 037",connip);
				direc.setText("方向盘模式\n←");
				//state = -1;
				//tvY.setText("y轴方向上的加速度为："+values[1]+"左转");	
			}*/
		}};
	public void connsendmsg(String msg, String connip)
	{
		new Connect1(msg,connip,12345).start();
	}
}
