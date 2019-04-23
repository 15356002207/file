package tset;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimingTrigger {

	private String date;
	public TimingTrigger(String date) {
		this.date=date;
	}
	public void start() {
		//设置定时器
		Timer time=new Timer();
	    Date da;
		try {
			da = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
			 MyTask myTask = new MyTask();		    
			 time.schedule(myTask, da);
		} catch (ParseException e) {
		 
			e.printStackTrace();
		}
	}
}

class MyTask extends TimerTask{
    public void run() {    	
    	// TODO 在这里添加动作
    	System.out.println("工作");
		Date da = null;
		try {
			da = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2018-08-20 23:41:25");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(da);
 			
 	}
}
