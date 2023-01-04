package hsge.hsgeback.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories
public class RedisConfig {
    @Bean
    public GeoOperations<String, String> geoOperations(RedisTemplate<String, String> template){
        return template.opsForGeo();
    }

}
