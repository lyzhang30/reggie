import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
//import redis.clients.jedis.Jedis;

/**
 * Redis测试类
 * @author zhanglianyong
 * 2022/8/2311:36
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisTest {

    //@Test
    //public void test() {
    //    Jedis jedis = new Jedis("localhost", 6379);
    //    jedis.set("zhanglianyong", "21");
    //    System.out.println(jedis.get("zhanglianyong"));
    //}
}
