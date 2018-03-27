package xc.ssh.domain;

import org.aspectj.lang.ProceedingJoinPoint;

public class Advice {

	public void  before(){
		System.out.println("我在你之前执行！！！！");
	}
	
	public void  after(){
		System.out.println("我在你之后执行！！！！");
	}
	
	
	public void  afterFinalException(){
		System.out.println("之后执行！ 无论是否异常，我都执行");
	}
	
	public void afterException(){
		System.out.println("之后执行！ 我只在异常的时候才执行");
	}
	
	
	public void  around(ProceedingJoinPoint pjp) throws Throwable{
		System.out.println("环绕前");
		pjp.proceed();
		System.out.println("环绕后");
	}
}
