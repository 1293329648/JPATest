package com.cjf;

import static org.junit.Assert.assertTrue;

import com.cjf.bean.Customer;
import com.cjf.utils.JPAUtil;
import org.junit.Test;

import javax.persistence.*;
import java.util.List;


public class AppTest {

    @Test
    public void Test01() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("myJpa");
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Customer customer = new Customer();
        customer.setCustName("陈建峰");
        entityManager.persist(customer);
        transaction.commit();
        entityManager.close();
        factory.close();
    }

    @Test
    public void Test02() {
        EntityManager entityManager = JPAUtil.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Customer customer = new Customer();
        customer.setCustName("王杰");
        customer.setCustIndustry("挖掘机技术工");
        entityManager.persist(customer);
        transaction.commit();
        entityManager.close();

    }

    @Test
    public void testAdd() {
        // 定义对象
        Customer c = new Customer();
        c.setCustName("刘超");
        c.setCustLevel("VIP客户");
        c.setCustSource("网络");
        c.setCustIndustry("IT教育");
        c.setCustAddress("昌平区北七家镇");
        c.setCustPhone("010-84389340");
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            // 获取实体管理对象
            em = JPAUtil.getEntityManager();
            // 获取事务对象
            tx = em.getTransaction();
            // 开启事务
            tx.begin();
            // 执行操作
            em.persist(c);
            // 提交事务
            tx.commit();
        } catch (Exception e) {
            // 回滚事务
            tx.rollback();
            e.printStackTrace();
        } finally {
            // 释放资源
            em.close();
        }
    }

    @Test
    public void testMerge() {
        //定义对象
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            //获取实体管理对象
            em = JPAUtil.getEntityManager();
            //获取事务对象
            tx = em.getTransaction();
            //开启事务
            tx.begin();
            //执行操作
            Customer c1 = em.find(Customer.class, 3L);
            c1.setCustName("陈建峰");
            em.clear();//把c1对象从缓存中清除出去
            em.merge(c1);
            //提交事务
            tx.commit();
        } catch (Exception e) {
            //回滚事务
            tx.rollback();
            e.printStackTrace();
        } finally {
            //释放资源
            em.close();
        }
    }

    /**
     * 删除
     */
    @Test
    public void testRemove() {
        // 定义对象
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            // 获取实体管理对象
            em = JPAUtil.getEntityManager();
            // 获取事务对象
            tx = em.getTransaction();
            // 开启事务
            tx.begin();
            // 执行操作
            Customer c1 = em.find(Customer.class, 3L);
            em.remove(c1);
            // 提交事务
            tx.commit();
        } catch (Exception e) {
            // 回滚事务
            tx.rollback();
            e.printStackTrace();
        } finally {
            // 释放资源
            em.close();
        }
    }

    @Test
    public void testGetOne() {
        // 定义对象
        EntityManager em = null;   //有缓存
        EntityTransaction tx = null;
        try {
            // 获取实体管理对象
            em = JPAUtil.getEntityManager();
            // 获取事务对象
            tx = em.getTransaction();
            // 开启事务
            tx.begin();
            // 执行操作
            Customer c1 = em.find(Customer.class, 1L);   //立即加载
            Customer c2 = em.getReference(Customer.class, 1L);  //延迟加载
            // 提交事务
            tx.commit();
            System.out.println(c1); // 输出查询对象
            System.out.println(c1 == c2);  //输出结果是true，说明c1和c2得到的是同一个对象 EntityManager也有缓存
        } catch (Exception e) {
            // 回滚事务
            tx.rollback();
            e.printStackTrace();
        } finally {
            // 释放资源
            em.close();
        }
    }

    //分页查询
    @Test
    public void findAll() {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            //获取实体管理对象
            em = JPAUtil.getEntityManager();
            //获取事务对象
            tx = em.getTransaction();
            tx.begin();
            // 创建query对象
            String jpql = "from Customer";
            Query query = em.createQuery(jpql);
            //设置分页条件
            query.setFirstResult(0);
            query.setMaxResults(2);
            // 查询并得到返回结果
            List list = query.getResultList(); // 得到集合返回类型
            for (Object object : list) {
                System.out.println(object);
            }
            tx.commit();
        } catch (Exception e) {
            // 回滚事务
            tx.rollback();
            e.printStackTrace();
        } finally {
            // 释放资源
            em.close();
        }
    }
    //模糊查询
    @Test
    public void findByName() {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            //获取实体管理对象
            em = JPAUtil.getEntityManager();
            //获取事务对象
            tx = em.getTransaction();
            tx.begin();
            // 创建query对象    asc是指定列按升序排列,desc则是指定列按降序排列
            String jpql = "from Customer where custName like ? order by custId asc";
            Query query = em.createQuery(jpql);
            query.setParameter(1,"陈%");
            // 查询并得到返回结果
            List list = query.getResultList(); // 得到集合返回类型
            for (Object object : list) {
                System.out.println(object);
            }
            tx.commit();
        } catch (Exception e) {
            // 回滚事务
            tx.rollback();
            e.printStackTrace();
        } finally {
            // 释放资源
            em.close();
        }
    }

    //统计
    @Test
    public void countTest() {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            //获取实体管理对象
            em = JPAUtil.getEntityManager();
            //获取事务对象
            tx = em.getTransaction();
            tx.begin();
            // 创建query对象    asc是指定列按升序排列,desc则是指定列按降序排列
            String jpql = " select  count(custId) from Customer ";
            Query query = em.createQuery(jpql);
            // 查询并得到返回结果
            Object count = query.getSingleResult();
            System.out.println(count);
            tx.commit();
        } catch (Exception e) {
            // 回滚事务
            tx.rollback();
            e.printStackTrace();
        } finally {
            // 释放资源
            em.close();
        }
    }


}
