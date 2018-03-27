package xc.ssh.test;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import xc.ssh.domain.Car;
import xc.ssh.domain.Classes;
import xc.ssh.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)

@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class SpringTest {

	@Autowired
	private User user;
	@Autowired
	private Car car;
	@Resource(name="class1")
	private Classes class222;
	@Resource(name="aopTest")
	private AopTest aop;
	
	@Test
	public void fun1(){
		
	/*	ApplicationContext  ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		Object bean = ac.getBean("user");
		//设置了 lazy属性，会在使用的时候才去调用构造函数，赋值创建对象。 那么 date的类型转换错误，也就在使用user对象的时候才会触发。
		*/
		
		//System.out.println(user.getUsername());
/*	System.out.println(car);
		System.out.println(class222);
		System.out.println(user);*/
		
		aop.save();
		
		
	}


	
}
