package com.meeting.utils;

import com.ruoyi.common.utils.uuid.UUID;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * Redis分布式锁实现
 */
@Component
public class RedisLock {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final String KEY_PREFIX = "lock:";

    private static final String ID_PREFIX = UUID.fastUUID().toString(true) + "-";

    private static final DefaultRedisScript<Long> UNLOCK_SCRIPT;

    static {
        UNLOCK_SCRIPT = new DefaultRedisScript<>();
        UNLOCK_SCRIPT.setLocation(new ClassPathResource("unlock.lua"));
        UNLOCK_SCRIPT.setResultType(Long.class);
    }

    /**
     * 尝试获取锁
     * @param lockName 锁名称
     * @param timeoutSec 锁持有的超时时间(毫秒)，过期后自动释放
     * @return true代表获取成功，false代表获取失败
     */
    public boolean tryLock(String lockName, long timeoutSec) {
        String threadId = ID_PREFIX + Thread.currentThread().getId();
        Boolean success = stringRedisTemplate.opsForValue().setIfAbsent(
                KEY_PREFIX + lockName, threadId, timeoutSec, TimeUnit.MILLISECONDS);
        return Boolean.TRUE.equals(success);
    }

    /**
     * 释放锁
     * @param lockName 锁名称
     */
    public void unlock(String lockName) {
        String threadId = ID_PREFIX + Thread.currentThread().getId();
        stringRedisTemplate.execute(UNLOCK_SCRIPT,
                Collections.singletonList(KEY_PREFIX + lockName), threadId);
    }
}
