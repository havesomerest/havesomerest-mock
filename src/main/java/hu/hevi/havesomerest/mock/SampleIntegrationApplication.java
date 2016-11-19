package hu.hevi.havesomerest.mock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan("hu.hevi.havesomerest")
public class SampleIntegrationApplication {

	public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext ctx = SpringApplication.run(SampleIntegrationApplication.class, args);
        TcpConnectionService tcpConnectionService = ctx.getBean(TcpConnectionService.class);
        tcpConnectionService.run();
    }

}
