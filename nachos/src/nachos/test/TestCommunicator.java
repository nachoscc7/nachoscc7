package nachos.test;

import nachos.threads.Communicator;
import nachos.threads.KThread;

public class TestCommunicator {
	public TestCommunicator(){
		
	}
	
	private static class Speaker implements Runnable {
		Speaker(Communicator com, String name) {
		this.com = com;
		this.name = name;
		}

		public void run() {
		//two things to say
		for (int i = 0; i < 2; i++) {
		com.speak(i);
		System.out.println(name + " envia " + i);
		}
		System.out.println(name + " termina");
		}

		private Communicator com;
		private String name;
		    }

	private static class Listener implements Runnable {
		Listener(Communicator com, String name) {
		this.com = com;
		this.name = name;
		}

		public void run() {
		//two things to hear
		for (int i = 0; i < 2; i++) {
		int heard = com.listen();
		System.out.println(name + " recive " + heard);
		}
		System.out.println(name + " termina");
		}

		private Communicator com;
		private String name;
	}

	public static void selfTest() {
		Communicator com = new Communicator();

		KThread thread1 = new KThread(new Speaker(com, "Thread 1"));
		KThread thread2 = new KThread(new Listener(com, "Thread 2"));
		
		thread1.fork();
		thread2.fork();		
	}
}
