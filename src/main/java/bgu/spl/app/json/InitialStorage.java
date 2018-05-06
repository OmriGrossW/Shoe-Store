package bgu.spl.app.json;


import bgu.spl.app.ShoeStorageInfo;

/**
 * The Class InitialStorage.
 * Json Class
 */
public class InitialStorage {
	
	private ShoeStorageInfo[] initialStorage;
	
	/**
	 * InitialStorage Copy Costructor.
	 *
	 * @param is 
	 */
	public InitialStorage(InitialStorage is){
		initialStorage = is.initialStorage;
	}

	/**
	 * Gets the initial storage array.
	 *
	 * @return the initial storage
	 */
	public ShoeStorageInfo[] getInitialStorage() {
		return initialStorage;
	}


}
