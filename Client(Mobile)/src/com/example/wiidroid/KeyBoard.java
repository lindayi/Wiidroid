package com.example.wiidroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class KeyBoard extends Activity {
	String connip;
	Button UpButton,SpaceButton,DownButton,EnterButton,LeftButton,RightButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.keyboard);
		Intent intent = getIntent();
		connip = intent.getStringExtra("socketip");
        UpButton = (Button)findViewById(R.id.button1); 
        DownButton = (Button)findViewById(R.id.button2);
        LeftButton = (Button)findViewById(R.id.button3); 
        RightButton = (Button)findViewById(R.id.button4); 
        SpaceButton = (Button)findViewById(R.id.button5);
        EnterButton = (Button)findViewById(R.id.button6);
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
        LeftButton.setOnTouchListener(
            	new OnTouchListener(){
            		public boolean onTouch(View view, MotionEvent event) {
            			switch(event.getAction()){
            				case MotionEvent.ACTION_DOWN:
            					connsendmsg("1 0 037",connip);
            					break;
            				case MotionEvent.ACTION_UP:
            					connsendmsg("1 1 037",connip);
            					break;
            			}
            			return false;
            		}
            	}
            );
        RightButton.setOnTouchListener(
                new OnTouchListener(){
                	public boolean onTouch(View view, MotionEvent event) {
                		switch(event.getAction()){
                			case MotionEvent.ACTION_DOWN:
                				connsendmsg("1 0 039",connip);
                				break;
                			case MotionEvent.ACTION_UP:
                				connsendmsg("1 1 039",connip);
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
	public void connsendmsg(String msg, String connip)
	{
		new Connect1(msg,connip,12345).start();
	}
}
