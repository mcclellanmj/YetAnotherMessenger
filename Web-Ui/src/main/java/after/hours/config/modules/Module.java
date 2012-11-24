package after.hours.config.modules;

import com.google.inject.AbstractModule;
import after.hours.service.HelloService;
import after.hours.service.impl.BritishHelloService;

/**
 * @author Richard Wilkinson - richard.wilkinson@jweekend.com
 *
 */
public class Module extends AbstractModule {

	/* (non-Javadoc)
	 * @see com.google.inject.AbstractModule#configure()
	 */
	@Override
	protected void configure() {
		
//		bind(HelloService.class).to(GermanHelloService.class);
		bind(HelloService.class).to(BritishHelloService.class);
		
	}
}