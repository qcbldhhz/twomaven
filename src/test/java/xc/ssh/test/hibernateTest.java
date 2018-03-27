package xc.ssh.test;

import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

import xc.ssh.domain.Classes;
import xc.ssh.domain.User;

public class hibernateTest {

	
	/**
	 * 用于测试学习hibernate的各种关系
	 * */
	
	//测试用于hibernate缓存，然后属性改变，是改变快照，还是改变一级缓存
	//结论：改变的是一级缓存， 1.我把user持久化，一级缓存和快照区各一份。 2. 修改属性值。 3.get方法取user对象、因为get方式是从缓存中取的，然后发现取出来的user，是已经改变属性的user、
	// 所以持久化对象，改变属性，改变的是缓存区
	@Test
	public   void  testAdd(){
		 Configuration configure = new Configuration().configure();
		 SessionFactory sessionFactory = configure.buildSessionFactory();
		 Session session = sessionFactory.openSession();
		 
		 Transaction tx = session.beginTransaction();
		 User  user = new User();
		 user.setId(15);
		 session.update(user);
		 user.setUsername("张三111");
		 user.setPassword("123569222111");
		 User user1 = (User) session.get(User.class, 15);
		 System.out.println(user1);
		 
		 tx.commit();
		 session.close();
		
	}
	
	//此方法用来测试主键自增长问题， 其中 increment 是由hibernate来管理，  native，identity是由数据库来管理。
	//问题：断点调试，session.save的insert语句是否是用于获得主键。
	//首先我配置文件设置为 increment, 发现执行save时会发送select语句用于查询主键，然后再commit提交的时候才发送insert语句
	//然后设置为 identity ,  执行save时直接发送insert语句，commit提交的时候没有发送语句。
	//而且无论设置increment还是identity，我为user设置了id，也是无用的，数据库中的数据主键，不会按我设置的id保存。
	//结论： session.save 一定会想办法获取到 主键，然后发送insert语句保存
	
	@Test
	public   void  testin(){
		 Configuration configure = new Configuration().configure();
		 SessionFactory sessionFactory = configure.buildSessionFactory();
		 Session session = sessionFactory.openSession();
		 
		 Transaction tx = session.beginTransaction();
		 User  user = new User();
		 user.setId(15);
		 user.setUsername("王五");
		 user.setPassword("88888");
		 session.update(user);

		 
		 tx.commit();
		 session.close();
		
	}
	
	//此方法用于测试updata,如果我update之前没有设置主键则会报错。
	//如果设置了主键，update之后的语句后，会把user变为持久化状态。那么以后在commit提交的时候，发现user属性与快照的不同会发送updata语句
	// 对于持久化状态的对象， save并不会发送insert语句。
	@Test
	public   void  testupdate(){
		 Configuration configure = new Configuration().configure();
		 SessionFactory sessionFactory = configure.buildSessionFactory();
		 Session session = sessionFactory.openSession();
		 
		 Transaction tx = session.beginTransaction();
		 User  user = new User();
		 User  user2 = (User) session.get(User.class, 14);
		 user.setId(15);
		 session.update(user);
		 user.setUsername("王五");
		 user.setPassword("7777");
		 session.save(user2);
		 
		 tx.commit();
		 session.close();
		
	}
	//再来测试一波save,看看对游离状态的对象会不会发送insert语句
	//证明数据库中会多存入一条数据，并且变成持久化状态后，还会发送update语句改变数据库中的属性
	@Test
	public   void  testsave(){
		 Configuration configure = new Configuration().configure();
		 SessionFactory sessionFactory = configure.buildSessionFactory();
		 Session session = sessionFactory.openSession();
		 
		 Transaction tx = session.beginTransaction();
		 User  user = (User) session.get(User.class, 14);
		 session.evict(user);
		 session.save(user);
		 user.setUsername("方清平");
		 user.setPassword("7777");
		 
		 tx.commit();
		 session.close();
		
	}
	
	/**
	 * 单项多对一 ，  多个user，对应一个class
	 * 1.首先多对一，能通过 多 查 到  一。  通过  多  保存一 ，通过多修改一 
	 *	例如，拿到这个user，就能够知道此user对应的classes
	 *
	 * 2.其实都是通过外键这条路径，查询到对应的值、
	 * 3. 配置，   既然是多对一 ，那么得在多的一方配置。   many-to-one   name="属性名"  clounm="外键字段"
	 * 
	 * */
	@Test
	public void test1(){
		//读取配置文件
		Configuration configure = new Configuration().configure();
		//创建session工厂
		SessionFactory sessionFactory = configure.buildSessionFactory();
		//打开一个session会话连接
		Session session = sessionFactory.openSession();
		//开启事务
		Transaction transaction = session.beginTransaction();
		//创建 一 (班级)
		Classes class1= new Classes();
		class1.setName("啦啦啦啦班！！！");
		
		 User  user = new User();
		 user.setUsername("我老赵来上学咯");
		 user.setPassword("988888");
		 user.setClasses(class1);
		 //我只保存user，看是否把classes也保存 ------报错了,提示说classes是瞬时状态，没有进入到持久化，所以不能保存，所以得要一个save加入到持久化状态
		 session.save(class1);  //class1保存在前，会少发送一条update语句。  
		 session.save(user);    //user保存在前，会发送updata语句，因为保存user的时候，还不知道外键应该如何存储，所以会再修改user表，添加外键
		 
		 //通过 user一放来维护外键，那么user可能会有外键，此时不用发送update语句
		 
		transaction.commit();
		session.close();
		
	}
	//单向 多对一的查询 。   通过查到的user， get方法得到classes ， 发送两条select语句
	//结论 ，可以通过多查询到一。 
	//注意，我在配置文件中， <many-to-one  ... lazy="false"> .不然会报错。代理对象类型转换错误，好像是javassist.jar包的问题,换了一个版本
	@Test
	public void testSel(){
		//读取配置文件
		Configuration configure = new Configuration().configure();
		//创建session工厂
		SessionFactory sessionFactory = configure.buildSessionFactory();
		//打开一个session会话连接 
		Session session = sessionFactory.openSession();
		//开启事务
		Transaction transaction = session.beginTransaction();
		User  user=(User) session.get(User.class, 21);
		System.out.println(user);
		Classes classes = user.getClasses();
		System.out.println(classes);
		
		
		transaction.commit();
		session.close();
		
	}
	
	/**
	 * 单项多对一
	 * 通过one对many进行维护 (查询，修改，添加) ,配置   <set name="users" >  
	 * 											<key   clounm="外键字段">  
	 * 											<ont-to-many  class="" 外键对应的类>
	 *  										<set>
	 * 其实配置思想是一样的， (1)外键字段，(2)对应类中的属性。(3)对方的类
	 * 1.其实还是通过外键，连接两方
	 * 先看添加
	 * */
	@Test
	public void testone(){
		//读取配置文件
		Configuration configure = new Configuration().configure();
		//创建session工厂  
		SessionFactory sessionFactory = configure.buildSessionFactory();
		//打开一个session会话连接
		Session session = sessionFactory.openSession();
		//开启事务
		Transaction transaction = session.beginTransaction();
		//创建 一 (班级)
		Classes class1= new Classes(); 
		class1.setName("多对一的班！！！");
		
		 User  user = new User();
		 user.setUsername("老钱，多多多");
		 user.setPassword("89898989");
		 
		 //classes里面setuser
		 class1.getUsers().add(user);
		 
		 
		 //我只保存classes，看是否把user也保存 ------报错了，瞬时状态不能保存
		 session.save(user);   //无论哪个在前，都会有update语句。   分析：暂时想不通，只知道多的一方维护关系，肯定发送update语句
		 session.save(class1); 		 
		 
		transaction.commit();
		session.close();
		
	}
	
	//看看查询m,没有问题，可以查询
	@Test
	public void testoneSel(){
		//读取配置文件
		Configuration configure = new Configuration().configure();
		//创建session工厂  
		SessionFactory sessionFactory = configure.buildSessionFactory();
		//打开一个session会话连接
		Session session = sessionFactory.openSession();
		//开启事务
		Transaction transaction = session.beginTransaction();
		 
		Classes classes = (Classes) session.get(Classes.class, 1);
		 System.out.println(classes.getName());
		Set<User> s = classes.getUsers();
		System.out.println(s);
		transaction.commit();
		session.close();
		
	}
	
	/**
	 * 双向多对一 ，  多个user，与一个class互相对应， 
	 * 
	 * 我们先看从一到多
	 * */
	@Test
	public void test2(){
		//创建session工厂
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		//打开一个session会话连接
		Session session = sessionFactory.openSession();
		//开启事务
		Transaction transaction = session.beginTransaction();	
		//创建user
		User  user = new User();
		user.setUsername("老孙");
		user.setPassword("988888");

		User  user2 = new User();
		user2.setUsername("老包");
		user2.setPassword("666888");
		
		Classes class1= new Classes();
		class1.setName("三班！！！");
		
		/**遵循 多 来维护关系的原则，而且先保存一 (如果先保存多，会发送update语句)。  因为如果一 来维护关系，肯定会多发送update语句。
		如何确定谁来维护关系，此处需要在 user中添加class。 也就是多需要填好“一”的属性
		*/
		
		//class1.getUsers().add(user);
		//class1.getUsers().add(user2);
		
		user.setClasses(class1); // 首先user中是没有calss的， 然后class是一个完整的。
		user2.setClasses(class1); // 后面肯定是要修改user的， 那么user中的class对应表的外键，  所以就必须要修改。
		/*
		 * user先入库，此时user是没有class_id的。   然后class入库，此时会根据class.hbm.xml进行添加外键值。此时会修改user。
		 *然后class在前 .  先保存完整的class,然后会保存user，user2. 此时user和user2还是不完整的。
		 *
		 * 
		 * */
		
		session.save(class1);
		session.save(user); 
		session.save(user2);
		
		transaction.commit();
		session.close();
		
	}
	
	
	
	
	/**
	 * sava 操作会保存到缓存区吗？
	 * 1.此处我先保存save。通过看数据库知道接下来保存的主键是9 
	 * 2.然后我 session.get()查找到主键为9的user，
	 * 
	 * 现象： 发现没有打印select语句。
	 *  结论 :因为get是先从缓存里面拿的，说明save操作会保存到缓存里面。
	 * */
	@Test
	public void fun4(){
		//读取配置文件
		Configuration configure = new Configuration().configure();
		//创建session工厂
		SessionFactory sessionFactory = configure.buildSessionFactory();
		//打开一个session会话连接
		Session session = sessionFactory.openSession();
		//开启事务
		Transaction transaction = session.beginTransaction();
		
		 User user = new User();
		 //user.setId(1);
		 user.setUsername("哈哈哈哈哦哦哦哦");
		session.save(user);  //此处发送 insert 语句 只是为了获取id
		user.setUsername("品牌扫大派送的爱仕达");  //这里证明有不有快照，对比

		 //通过学习知道，session.get() 是先从缓存里面找的，然后找不到再去数据库查。那么如果缓存有的话，是不是就不去数据库查了呢？
		 
		 Object object = session.get(User.class,9);
		 System.out.println(object);
		transaction.commit();
		session.close();
		
	}
	
	//session.delete()会不会删除session里面的数据
	// 1.首先session.get(User.class, 1);
	//2.然后session.delete()
	//3. 然后再session.get(User.class, 1); 看是否打印 select语句 （结果：这里直接返回null。不打印select语句）
	//结果：会删除数据库中的数据，但不会从数据库里面查询，而是从缓存中直接得到数据库里面已经没有数据的了
	@Test
	public void fun5(){
		 //读配置文件 
		 Configuration configure = new Configuration().configure();
		//创session工厂
		   SessionFactory sessionFactory = configure.buildSessionFactory();
		   Session session = sessionFactory.openSession();
		   
		   Transaction transaction = session.beginTransaction();
		   
		   User user = (User) session.get(User.class, 1);
		   
		   System.out.println(user+"!11111");
		   
		   //session.delete(user);  //对象从持久变成瞬时。
		   
		  // User user2 = (User) session.get(User.class, 1);
		  // System.out.println(user2);
		  
		   //session.update(user); // 对象从瞬时，变成持久。    没有什么好改变的。
		   //session.save(user); // 每次都需要增加，所以有insert。
		   
		   transaction.commit();
		   session.close();
	}
	
}
