package after.hours;

import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;

import com.google.inject.Inject;
import after.hours.service.HelloService;

/**
 * @author Richard Wilkinson - richard.wilkinson@jweekend.com
 *
 */
public class HomePage extends WebPage {

	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private String text;
	private String name;
	
	@Inject
	private HelloService helloService;

    /**
	 * Constructor that is invoked when page is invoked without a em.
	 * 
	 * @param parameters
	 *            Page parameters
	 */
    public HomePage(final PageParameters parameters) {

		final Label label = new Label("text", new PropertyModel<String>(this, "text"));
		label.setOutputMarkupId(true);
		
		add(label);

		Form<Void> form = new Form<Void>("form");
		form.add(new TextField<String>("name", new PropertyModel<String>(this, "name")));
		
		add(form);
		
		add(new AjaxSubmitLink("helloSubmit", form){
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				HomePage.this.text = helloService.sayHello(name);
				target.addComponent(label);
			}
		});
		
		add(new AjaxSubmitLink("goodbyeSubmit", form){
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				HomePage.this.text = helloService.sayGoodbye(name);
				target.addComponent(label);
			}
		});
        
    }
}
