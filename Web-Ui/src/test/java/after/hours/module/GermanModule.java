package after.hours.module;

import com.google.inject.AbstractModule;
import after.hours.service.HelloService;
import after.hours.service.impl.GermanHelloService;

/**
 * @author Richard Wilkinson - richard.wilkinson@jweekend.com
 *
 */
public class GermanModule extends AbstractModule {

	/* (non-Javadoc)
	 * @see com.google.inject.AbstractModule#configure()
	 */
	@Override
	protected void configure() {
		bind(HelloService.class).to(GermanHelloService.class);

	}

}
