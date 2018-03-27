package xc.ssh.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Myhandler implements InvocationHandler {

	private Object target;
	
	public Myhandler(Object target) {
		this.target=target;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		
		System.out.println("想不到吧，我运行在你的前面");
			Object result = method.invoke(target, args);
		System.out.println("其实你早就已经被包围了");
		
		return result;
	}

	public Object getProxy(){
		
		
		Object object = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), target.getClass().getInterfaces(), this);
		return object;
		
	}
	
}
