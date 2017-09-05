package info.getbus.servebus.dao;

import info.getbus.servebus.model.tmp.B;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collection;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

@ContextConfiguration(locations = {
		"classpath:persistence-config.xml" })
//@ActiveProfiles("test")
//@Transactional

@RunWith(SpringJUnit4ClassRunner.class)
public class BMapperTest {

	@Autowired
	private BMapper mapper;

	@Test
	public void notNull(){
		assertNotNull(mapper);
	}

	@Test
	public void selectAll() throws Exception {
		Collection<B> l = mapper.selectAll();
		assertNotEquals(0, l.size());
		System.out.println(l);

	}

	@Test
	public void test1() throws Exception {
		B b = new B();
		b.setA("aaaaaa");
		b.setB("qqqqq");
		System.out.println(b);
		mapper.insert(b);
		System.out.println(b);
	}
}