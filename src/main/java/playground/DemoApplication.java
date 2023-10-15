package playground;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.util.List;
import java.util.function.Function;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class DemoApplication {

	WebClient webClient;

	Function<UriBuilder, UriBuilder> uriFunction = uriBuilder -> uriBuilder.path("/xxx");

	Function<UriBuilder,URI> uriQueryFunction = uriBuilder -> uriBuilder.replaceQueryParam("parm", 1).build();

	{


		this.webClient.get().uri(uriFunction.andThen(uriQueryFunction)).retrieve()
					  .bodyToFlux(test.class)
		.flatMap(o -> Flux.fromIterable(o.list))
		.subscribe();
	}

	public static void main(String[] args) {


		SpringApplication.run(DemoApplication.class, args);
	}

	public static class test {
		public List<Integer> list;
	}
}
