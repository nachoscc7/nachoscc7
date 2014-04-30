package nachos.test;

import nachos.machine.Machine;
import nachos.threads.KThread;
import nachos.threads.Lock;
import nachos.threads.ThreadedKernel;

public class TestPriority {

	public static void selfTest(){
		KThread t1 = new KThread(new PingTest(1)).setName("thread 1");		
		KThread t2 = new KThread(new PingTest(2)).setName("thread 2");
		KThread t3 = new KThread(new PingTest(3)).setName("thread 3");
		Machine.interrupt().disable();
		ThreadedKernel.scheduler.setPriority(t1, 7);
		ThreadedKernel.scheduler.setPriority(t2, 7);
		ThreadedKernel.scheduler.setPriority(t3, 3);
		Machine.interrupt().enable();
		t1.fork();		
		t2.fork();
		t3.fork();
		
	}
	
	public static void selfTestLock(){
		Lock lock = new Lock();
		KThread t1 = new KThread(new PingTestLock(1,lock)).setName("thread 1");		
		KThread t2 = new KThread(new PingTestLock(2,lock)).setName("thread 2");
		KThread t3 = new KThread(new PingTestLock(3,lock)).setName("thread 3");
		Machine.interrupt().disable();
		ThreadedKernel.scheduler.setPriority(t1, 1);
		ThreadedKernel.scheduler.setPriority(t2, 2);
		ThreadedKernel.scheduler.setPriority(t3, 3);
		Machine.interrupt().enable();
		t1.fork();		
		t2.fork();
		t3.fork();
		
	}
	
	private static class PingTest implements Runnable {
		private int which;

		PingTest(int which) {
		    this.which = which;
		}

		@Override
		public void run() {			
			for (int i=0; i<10; i++) {
				 
				System.out.println("*** thread " + which + " looped "
						   + i + " times");
				KThread.currentThread().yield();
			 }
		}
	}
	
	private static class PingTestLock implements Runnable {
		private int which;
		private Lock lock;
		PingTestLock(int which,Lock lock) {
			this.lock = lock;
		    this.which = which;
		}

		@Override
		public void run() {			
			for (int i=0; i<5; i++) {
				System.out.println("*** thread " + which + " intenta acceder al seccion critica");
				lock.acquire();
				Machine.interrupt().disable();
				ThreadedKernel.scheduler.setPriority(1);
				System.out.println("*** thread " + which + " accede al seccion critica PE:"+ThreadedKernel.scheduler.getEffectivePriority());
				Machine.interrupt().enable();
				System.out.println("*** thread " + which + " looped " + i + " times");
				lock.release();				
				KThread.currentThread().yield();
			 }
		}
	}

}
