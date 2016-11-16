package webApp;

public class Main {


	public static void main(String[] args) throws InterruptedException {	
		Thread thread = new Thread(new ParserThread(3000));
		thread.start();
		
		thread.join();	
	}

}
