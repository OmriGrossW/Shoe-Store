package bgu.spl.mics;

public class RequestCompleted<T> implements Message {

    private Request<T> completed;
    private T result;
    /**
     * RequestCompleted Constructor
     * @param completed the request that was completed.
     * @param result the result of the specific request.
     */
    public RequestCompleted(Request<T> completed, T result) {
        this.completed = completed;
        this.result = result;
    }
    /**
     * 
     * @return the completed request.
     */
    public Request<T> getCompletedRequest() {
        return completed;
    }
    /**
     * 
     * @return the result of the specific request.
     */
    public T getResult() {
        return result;
    }

}
