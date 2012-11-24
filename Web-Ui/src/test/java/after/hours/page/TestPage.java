package after.hours.page;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import after.hours.HomePage;
import after.hours.WicketApplication;
import after.hours.service.HelloService;
import after.hours.service.impl.BritishHelloService;

/**
 * @author Richard Wilkinson - richard.wilkinson@jweekend.com
 *
 */
public class TestPage {
	
	private WicketTester tester;
	
	@Before
	public void setUp()
	{
		final HelloService service = mock(BritishHelloService.class);
		when(service.sayHello(anyString())).thenReturn("sayHello");
		when(service.sayGoodbye(anyString())).thenCallRealMethod();
		
		tester = new WicketTester(new WicketApplication(){
			protected Injector getGuiceInjector()
			{
				return Guice.createInjector(new AbstractModule(){
					@Override
					protected void configure() {
						bind(HelloService.class).toInstance(service);
					}
				});
			}
		});
	}
	
	@Test
	public void testPage()
	{
		tester.startPage(HomePage.class);
		
		tester.assertRenderedPage(HomePage.class);
		
		FormTester formTester = tester.newFormTester("form");
		
		formTester.setValue("name", "Bob");
		
		formTester.submit();
		
		//clicking ajax links like this does not submit the form so thats why we need the explicit submit
		tester.clickLink("helloSubmit", true);
		
		tester.assertComponentOnAjaxResponse("text");
		
		tester.assertModelValue("text", "sayHello");
		
		tester.clickLink("goodbyeSubmit", true);
		
		tester.assertComponentOnAjaxResponse("text");
		
		tester.assertModelValue("text", "Goodbye Bob");
	}

}
