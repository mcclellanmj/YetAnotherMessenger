package after.hours.service.impl;

import after.hours.service.HelloService;

/**
 * @author Richard Wilkinson - richard.wilkinson@jweekend.com
 *
 */
public class BritishHelloService implements HelloService {

	/* (non-Javadoc)
	 * @see after.hours.service.HelloService#sayGoodbye(java.lang.String)
	 */
	public String sayGoodbye(String name) {
		return "Goodbye " + name;
	}

	/* (non-Javadoc)
	 * @see after.hours.service.HelloService#sayHello(java.lang.String)
	 */
	public String sayHello(String name) {
		return "Hello " + name;
	}

}
