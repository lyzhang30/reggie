import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;
//import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * 1. 使用Jedis操作redis
 * 2. Spring-data
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class JedisTest {

    //public void testString(){
    //    redisTemplate.opsForCluster().set("city","beijing");
    //}

    @Test
    public void testRedis() {
        //连接
        Jedis jedis = new Jedis("localhost", 6379);

        //执行具体的操作
        jedis.set("username","zhanglianyong");

        String name = jedis.get("username");
        System.out.println(name);

        //删除
        jedis.del("username");
        jedis.set("username","niuren");

        jedis.hset("myhash","name","张连勇");
        String myName = jedis.hget("myhash", "name");
        System.out.println(myName);

        //获取所有的key
        Set<String> keys = jedis.keys("*");
        for(String key : keys){
            System.out.println(key);
        }
        //关闭连接
        jedis.close();



    }

}
