package nachos.threads;


import nachos.ag.BoatGrader;

public class Boat
{
    static BoatGrader bg;
    static int child,childBeachA,adult,adultsBeachA;
    static Lock lockBeachA,lockBeachB,lockAdults;
    static int countChildren;
    static Condition2 conditionBeachA,conditionBeachB,conditionAdults;
    static boolean blockAdults;
    static boolean blockChild;
    static Runnable threadAdults,threadChildren;
    static KThread threadPersonAdults;
    static Lock lockSimulation = new Lock();
	static Condition2 simulation = new Condition2(lockSimulation);
	static Lock lockBegin = new Lock();
	static Condition2 simulationBegin = new Condition2(lockBegin);
    public static void selfTest()
    {
	BoatGrader b = new BoatGrader();
	
	System.out.println("\n ***Testing Boats with only 2 children***");
	begin(4, 5, b);

	//System.out.println("\n ***Testing Boats with 2 children, 1 adult***");
  	//begin(1, 2, b);

//  	System.out.println("\n ***Testing Boats with 3 children, 3 adults***");
//  	begin(3, 3, b);
    }

    
    
    public static void begin( int adults, int children, BoatGrader b )
    {
    	bg = b;
    	child = childBeachA = children;
    	adult = adultsBeachA = adults;
    	lockAdults= lockBeachB= lockBeachA = new Lock();
    	
    	conditionBeachA= new Condition2(lockBeachA);
    	conditionBeachB = new Condition2(lockBeachB);
    	conditionAdults = new Condition2(lockAdults);
    	blockChild = blockAdults=true;
    	threadAdults = threadChildren = null;
   
    	
    	for(int i=0; i < adults; i++){
    		KThread t = new KThread(new Runnable(){
    			public void run(){
    				AdultItinerary();
    			}
    			
    		});
    		
    		t.setName("Adult:" + i);
    		t.fork();
    	}
    	for(int i=0; i < children; i++){
    		KThread t = new KThread(new Runnable(){
    			public void run(){
    				ChildItinerary();
    			}
    			
    		});

    		t.setName("Child:" + i);
    		t.fork();
    	}
    	
    	lockSimulation.acquire();
    		simulation.sleep();
    	lockSimulation.release();
    	 
    	 
    }
    
    
    

    static void AdultItinerary()
    {
    	
    	
    	while(blockAdults){
    		lockAdults.acquire();
    		conditionAdults.sleep();
    		lockAdults.release(); 
    	}
    	
    	adultsBeachA--;
	 	bg.AdultRowToMolokai(); 
	 	lockBeachA.acquire();
	 		conditionBeachA.wake();
	 	lockBeachA.release();
    }

    static void ChildItinerary()
    {
    	
    	while(childBeachA!=0){
    		lockBegin.acquire();
    		countChildren++;
    		blockAdults=true;
    		while(countChildren>=3){	
    			simulationBegin.sleep();
    		}
    		lockBegin.release();
    		while(countChildren==1){   			
    		//1
    			childBeachA--;
    			bg.ChildRowToMolokai();
    			
    			lockBeachB.acquire();
    			conditionBeachB.sleep();
    			lockBeachB.release();
    			
    		//2	
    			if(childBeachA!=0 || adultsBeachA!=0){
	    			childBeachA++;
	    			bg.ChildRowToOahu();
	    			
	    			lockBeachA.acquire();
	    			conditionBeachA.wake();
	    			lockBeachA.release();
	    			
	    			lockBeachB.acquire();
	    			conditionBeachB.sleep();
	    			lockBeachB.release();
	    			
	    			lockBeachA.acquire();
	    			conditionBeachA.wake();
	    			lockBeachA.release();
    			}
    		}
    		while(countChildren==2){
    		//1
    			childBeachA--;
    			bg.ChildRideToMolokai();
    			
    			lockBeachB.acquire();
    			conditionBeachB.wake();
    			lockBeachB.release();
    		//2
    			if(childBeachA!=0 || adultsBeachA!=0 ){
	    			lockBeachA.acquire();
	    			conditionBeachA.sleep();
	    			lockBeachA.release();
	    			if(childBeachA==1){
		    			blockAdults=false;
		    			lockAdults.acquire();
		    			conditionAdults.wake();
		    			lockAdults.release();
	    			}
	    			while(childBeachA==1){
	    				lockBeachA.acquire();
	    				conditionBeachA.sleep();
	    				childBeachA++;
	        			bg.ChildRowToOahu();
	    				lockBeachA.release();
	    			}
	    			
    			}
    			countChildren=0;
    			lockBeachB.acquire();
				conditionBeachB.wake();
				lockBeachB.release();
				
				lockBeachA.acquire();
    			conditionBeachA.sleep();
    			lockBeachA.release();
    		}
    		
    	}
    	lockSimulation.acquire();
		simulation.wake();
		lockSimulation.release();	
    		
    }
    	

    static void SampleItinerary()
    {
	// Please note that this isn't a valid solution (you can't fit
	// all of them on the boat). Please also note that you may not
	// have a single thread calculate a solution and then just play
	// it back at the autograder -- you will be caught.
	System.out.println("\n ***Everyone piles on the boat and goes to Molokai***");
	bg.AdultRowToMolokai();
	bg.ChildRideToMolokai();
	bg.AdultRideToMolokai();
	bg.ChildRideToMolokai();
    }
    
}
