package nachos.test;

import nachos.threads.Alarm;
import nachos.threads.KThread;



public class TestAlarm {
	public TestAlarm(){
		
	}
	
	public static void selfTest(){
		Alarm alarma1 = new Alarm();
    	KThread thread = new KThread(new PingTestAlarm(1,alarma1)).setName("Thread 1");
    	thread.fork();
    	thread.join();
	}
	
	private static class PingTestAlarm2 implements Runnable {
        PingTestAlarm2(int which,Alarm alarma) {
            this.which = which;
            this.alarma = alarma;
        }
        
        public void run() {
         alarma.waitUntil(10000000);
          for (int i=0; i<50; i++) {
            System.out.println("*** thread " + which + " looped "
                       + i + " times");
            KThread.currentThread().yield();
            }
        }

    private int which;
    private Alarm alarma;
    }
	private static class PingTestAlarm implements Runnable {
        PingTestAlarm(int which,Alarm alarma) {
            this.which = which;
            this.alarma = alarma;
        }
        
        public void run() {
        	KThread thread = new KThread(new PingTestAlarm2(2,alarma)).setName("Forked Alarm");
        	thread.fork();
        	thread.join();
          for (int i=0; i<50; i++) {
            System.out.println("*** thread " + which + " looped "
                       + i + " times");
            KThread.currentThread().yield();
            }
        }

    private int which;
    private Alarm alarma;
    }

}
