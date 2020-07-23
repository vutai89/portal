package com.mcredit.scheduler;

import java.util.Observable;

import com.mcredit.common.MCLogger;

import sun.misc.Signal;

public class SignalHandler extends Observable implements sun.misc.SignalHandler {

	/**
	   * Tells the object to handle the given signal.
	   * @param signalName The name of the signal, such as "SEGV", "ILL",
	   * "FPE", "ABRT", "INT", "TERM", "HUP", etc. Not all platforms
	   * support all signals. Microsoft Windows may not support HUP, for
	   * example, whereas that is a widely use and supported signal under
	   * Unix (and its variants); additionally, the JVM may be using some
	   * signals (the use of -Xrs will reduce or disable them at the cost
	   * of losing what the JVM wanted them for).
	   * @exception IllegalArgumentException is thrown when the named
	   * signal is not available for some reason. Watch out: the original
	   * cause (missing class or method) may be wrapped inside the exception!
	   **/
	  public void handleSignal( final String signalName )
	    throws IllegalArgumentException
	  {
	    try
	      {
	    	MCLogger.debug(this, "SignalHandler.handleSignal " + signalName);
	    		Signal.handle(new sun.misc.Signal	(signalName), this );
	      }
	    catch( IllegalArgumentException x )
	      {
	// Most likely this is a signal that's not supported on this
	        // platform or with the JVM as it is currently configured
	        throw x;
	      }
	    catch( Throwable x )
	      {
		 // We may have a serious problem, including missing classes
	        // or changed APIs
	        throw new IllegalArgumentException( "Signal unsupported: "+signalName, x );
	      }
	  }
	 /**
	   * Called by Sun Microsystems' signal trapping routines in the JVM.
	   * @param signal The {@link sun.misc.Signal} that we received
	   **/
	  public void handle( final Signal signal )
	  {
	 // setChanged ensures that notifyObservers actually calls someone. In
	    // simple cases this seems like extra work but in asynchronous designs,
	    // setChanged might be called on one thread, and notifyObservers, on
	    // another or only when multiple changes may have been completed (to
	    // wrap up multiple changes in a single notifcation).
		  setChanged();
		  notifyObservers( signal );
	  }

}
