package nachos.test;

import nachos.threads.KThread;

public class TestJoin {
	public TestJoin(){
		
	}
	public static void selfTest(){
		new KThread(new PingTest(1)).setName("forked thread").fork();        
    	new KThread(new Test(3)).setName("forked thread").fork();
	}
	
	private static class PingTest implements Runnable {
		PingTest(int which) {
		    this.which = which;
		}
		
		public void run() {
		    for (int i=0; i<5; i++) {
			System.out.println("*** thread " + which + " looped "
					   + i + " times");
			KThread.currentThread().yield();
		    }
		}

		private int which;
	}
	private static class Test implements Runnable {
	    Test(int which) {
	        this.which = which;
	    }
	    
	    public void run() {
	        KThread th = new KThread(new PingTest(2));
	        th.fork();
	        th.join();
	        for (int i=0; i<5; i++) {
	        System.out.println("*** thread " + which + " looped "
	                   + i + " times");
	        KThread.currentThread().yield();
	        }
	    }
	    private int which;
	   }
}
