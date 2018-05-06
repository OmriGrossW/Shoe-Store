package bgu.spl.app;


import bgu.spl.app.json.JsonObject;
import bgu.spl.app.services.ManagementService;
import bgu.spl.app.services.SellingService;
import bgu.spl.app.services.ShoeFactoryService;
import bgu.spl.app.services.TimeService;
import bgu.spl.app.services.WebsiteClientService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.*;
import java.util.logging.*;

/**
 * The Class ShoeStoreRunner.
 */
public class ShoeStoreRunner {
	
	private static CountDownLatch startSignal;
	public final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	/**
	 * decrease by one the start signal CountDownLatch
	 */
	public static void countDown(){
		startSignal.countDown();
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments. used to load the JSON file.
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws InterruptedException the interrupted exception
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		
		int numOfThredsToCountDown;
		int numOfFactories;
		int numOfSellers;
		int numOfClients;
		Reader reader = null;
		FileHandler fileTxt = null;
		System.setProperty("java.util.logging.SimpleFormatter.format","%4$s: %5$s %n");
		
		 if (args[0].equals("shutdown") || args[0].equals("Shutdown") || args[0].equals("quit") || args[0].equals("Quit")){
		    	System.exit(0);
		    }
		
		try {
			fileTxt = new FileHandler("MainLog.txt");
		} catch (SecurityException | IOException e1) {
			LOGGER.severe(e1.getMessage());
		}
		LOGGER.setLevel(Level.INFO);
		SimpleFormatter formatterTxt = new SimpleFormatter();
	    fileTxt.setFormatter(formatterTxt);
	    LOGGER.addHandler(fileTxt);
	    
	   
		
		
		Gson gson = new GsonBuilder().create();
		reader = new BufferedReader(new FileReader("/users/studs/bsc/2016/omerbenc/new-workspace/Ass2/src/main/java/bgu/spl/app/"+args[0]+".json"));
		JsonObject jsonObj= new JsonObject(gson.fromJson(reader, JsonObject.class));		
		LOGGER.info("File was successfully read!\n");
		Store.getStore().load(jsonObj.getInitialStorage());	//Loading the storage	
		numOfFactories = jsonObj.getServices().getFactories();
		numOfSellers = jsonObj.getServices().getSellers();
		numOfClients = jsonObj.getServices().getClients().length;
		numOfThredsToCountDown = numOfFactories + numOfSellers + numOfClients+1;
		startSignal = new CountDownLatch(numOfThredsToCountDown);
		Store.getStore().setPrintRecieptSignal(numOfThredsToCountDown);
		
		for (int i=1 ; i<=numOfFactories ; i++){ //Factories Creation
			new Thread(new ShoeFactoryService("Factory "+i)).start();
		}
		if(numOfFactories==0) LOGGER.warning("NO FACTORIES TO CREATE!\n");
			
		for (int i=1 ; i<=numOfSellers ; i++){ //Sellers Creation
			new Thread(new SellingService("Seller "+i)).start();
		}
		if(numOfSellers==0) LOGGER.warning("NO SELLERS TO CREATE!\n");
		
		for (int i=0 ; i<numOfClients ; i++){ //Customers Creation
			new Thread(new WebsiteClientService(jsonObj.getServices().getClients()[i].getName(), jsonObj.getServices().getClients()[i].getPurchaseScheduleList(), jsonObj.getServices().getClients()[i].getWishList())).start();
		}
		if(numOfClients==0) LOGGER.warning("NO CLIENTS TO CREATE!\n");

		try{ //Manager Creation
			new Thread(new ManagementService(jsonObj.getServices().getManager().getDiscountScheduleList())).start();;
		}
		catch (Exception e){
			LOGGER.warning("MANAGER WAS NOT CREATED!\n");
		}
		
		startSignal.await();
		try{ //TimeService Creation
			new Thread(new TimeService(jsonObj.getServices().getTime().getSpeed(), jsonObj.getServices().getTime().getDuration())).start();	
		}
		catch (Exception e){
			LOGGER.warning("TIME-SERVICE WAS NOT CREATED!\n");
		}			
		
	}
}
