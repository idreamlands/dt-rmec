
package org.idreamlands.dt.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.router.ExpressionEvaluatingRouter;
import org.springframework.messaging.MessageChannel;

@EnableBinding
public class SourceWithDynamicDestination {

	@Autowired
	private BinderAwareChannelResolver resolver;


	@Bean(name = "sourceChannel")
	public MessageChannel localChannel() {
		return new DirectChannel();
	}

	@Bean
	@ServiceActivator(inputChannel = "sourceChannel")
	public ExpressionEvaluatingRouter router() {
		ExpressionEvaluatingRouter router = new ExpressionEvaluatingRouter(new SpelExpressionParser().parseExpression("payload.consumerQueue"));
		router.setDefaultOutputChannelName("default-output");
		router.setChannelResolver(resolver);
		return router;
	}
}
