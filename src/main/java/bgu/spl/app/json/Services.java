package bgu.spl.app.json;


/**
 * The Class Services.
 * Json Class
 */
public class Services {

	private Time time;
	private Manager manager;
	private int factories;
	private int sellers;
	private Client[] customers;
	
	
	/**
	 * Services Copy Constructor.
	 *
	 * @param srv 
	 */
	public Services(Services srv){
		this.time=new Time(srv.time);
		this.manager=new Manager(srv.manager);
		this.factories=srv.factories;
		this.sellers=srv.sellers;
		this.customers = new Client[srv.customers.length];
		for (int i=0 ; i<srv.customers.length ; i++){
			this.customers[i] = srv.customers[i];
		}
	}

	/**
	 * Gets the time.
	 *
	 * @return the time
	 */
	public Time getTime() {
		return time;
	}

	/**
	 * Gets the manager.
	 *
	 * @return the manager
	 */
	public Manager getManager() {
		return manager;
	}

	/**
	 * Gets the factories.
	 *
	 * @return the factories
	 */
	public int getFactories() {
		return factories;
	}

	/**
	 * Gets the sellers.
	 *
	 * @return the sellers
	 */
	public int getSellers() {
		return sellers;
	}
	
	/**
	 * Gets the clients.
	 *
	 * @return the clients
	 */
	public Client[] getClients() {
		return customers;
	}




}


