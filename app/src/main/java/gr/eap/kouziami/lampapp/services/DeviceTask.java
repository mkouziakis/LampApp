package gr.eap.kouziami.lampapp.services;

/**
 * Simple Command design pattern
 * Extend in order to define the steps
 * of a service execution and UI updates
 *
 * Created by KouziaMi on 22/11/2015.
 */

public abstract class DeviceTask<E> {
    /**
     * Templated execution result, depends which service you call, you decide the type ;)
     */
    public E result;

    /**
    * Override and code service execution
    */
    public abstract void execute();

    /**
     * Code here what will happen after a successfull invocation
     */
    public void postSuccess(){};

    /**
     * Code here what will happen after an error
     */
    public void postError(){};

    /**
     * Code here what will happen anyway
     */
    public void postDefault(){};
}
