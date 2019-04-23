package tset;

/**
 * 
 * @auther 周科迪
 * @date 2018年8月17日
 * @description 定时触发器，将设定时间传入TimingTrigger, 将执行的工作内容写在MyTask的run方法里
 */
public class Usage {

	public static void main(String[] args) {		 
	// 传入设定时间
		String time = "2018-08-20 23:41:25";
	TimingTrigger timingTrigger=new TimingTrigger(time);
	timingTrigger.start();	
	}
}
 



