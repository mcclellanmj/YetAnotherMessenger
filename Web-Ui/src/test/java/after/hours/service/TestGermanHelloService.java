package after.hours.service;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import after.hours.module.GermanModule;

/**
 * @author Richard Wilkinson - richard.wilkinson@jweekend.com
 *
 */
public class TestGermanHelloService {
	
	@Inject HelloService helloService;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		Injector injector = Guice.createInjector(new GermanModule());
		injector.injectMembers(this);
	}

	/**
	 * Test method for {@link after.hours.service.impl.GermanHelloService#sayGoodbye(java.lang.String)}.
	 */
	@Test
	public void testSayGoodbye() {
		assertEquals("Hallo name", helloService.sayHello("name"));
	}

	/**
	 * Test method for {@link after.hours.service.impl.GermanHelloService#sayHello(java.lang.String)}.
	 */
	@Test
	public void testSayHello() {
		assertEquals("Auf Wiedersehen name", helloService.sayGoodbye("name"));
	}

}
