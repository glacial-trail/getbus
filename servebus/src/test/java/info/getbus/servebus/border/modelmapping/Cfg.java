package info.getbus.servebus.border.modelmapping;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

@TestConfiguration
@ImportResource("classpath:conversion-config.xml")
@ComponentScan(basePackages = "info.getbus.servebus.border.modelmapping")
public class Cfg { }
