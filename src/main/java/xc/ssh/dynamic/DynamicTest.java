package xc.ssh.dynamic;

import java.lang.reflect.Proxy;

public class DynamicTest {

	public static void main(String[] args) {
		//现在我想在car执行run的时候，都做一些操作
		
		Car car = new Car();
		
		Myhandler hd= new Myhandler(car);
		
		ICar p = (ICar) hd.getProxy();
		
		p.run();
	}
	
}
