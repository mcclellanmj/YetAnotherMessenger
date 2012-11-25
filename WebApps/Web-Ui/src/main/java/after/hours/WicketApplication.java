package after.hours;

import org.apache.wicket.Page;
import org.apache.wicket.guice.GuiceComponentInjector;
import org.apache.wicket.protocol.http.WebApplication;

import com.google.inject.Guice;
import com.google.inject.Injector;
import after.hours.config.modules.Module;

/**
 * Application object for your web application. If you want to run this application without deploying, run the Start class.
 * 
 * @see wicket.myproject.Start#main(String[])
 * 
 * @author Richard Wilkinson - richard.wilkinson@jweekend.com
 *
 */
public class WicketApplication extends WebApplication
{    
	/**
	 * Constructor
	 */
	public WicketApplication()
	{
	}
	
	@Override
	protected void init() {
		super.init();
		addComponentInstantiationListener(new GuiceComponentInjector(this, getGuiceInjector()));
	}
	
	protected Injector getGuiceInjector()
	{
		return Guice.createInjector(new Module());
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends Page> getHomePage() {
		return HomePage.class;
	}

}
