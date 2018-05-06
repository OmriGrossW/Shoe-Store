package bgu.spl.app.json;

import bgu.spl.app.ShoeStorageInfo;

/**
 * The Class JsonObject.
 */
public class JsonObject {
	
	private ShoeStorageInfo[] initialStorage;
	private Services services;
	
	/**
	 * JsonObject Copy Constructor.
	 *
	 * @param jo 
	 */
	public JsonObject(JsonObject jo){
		initialStorage = jo.initialStorage;
		services = new Services(jo.services);
	}
	
	/**
	 * Gets the initial storage array.
	 *
	 * @return the initial storage
	 */
	public ShoeStorageInfo[] getInitialStorage(){
		return initialStorage;
	}
	
	/**
	 * Gets the services.
	 *
	 * @return the services
	 */
	public Services getServices(){
		return services;
	}

}
