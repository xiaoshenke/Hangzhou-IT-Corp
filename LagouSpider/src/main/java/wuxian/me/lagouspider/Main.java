package wuxian.me.lagouspider;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import wuxian.me.lagouspider.util.FileUtil;

/**
 * Created by wuxian on 29/3/2017.
 */
public class Main {

    public static void main(String[] args){
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource res = resolver.getResource("classpath:spider.xml");  //p75
        BeanFactory bf = new XmlBeanFactory(res);
        //bf.getBean("sqlSessionFactory");

        LagouSpider spider = new LagouSpider();
        spider.beginSpider();
    }

}
