package com.example.stock.facade;

import com.example.stock.service.StockService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Lock 획득 재시도를 기본으로 제공한다.
 * pub-sub 방식으로 구현이 되어있기 때문에 Lettuce와 비교했을 때 Redis에 부하가 덜 간다.
 * 별도의 라이브러리를 사용해야 한다.
 * Lock을 라이브러리 차원에서 제공해주기 때문에 사용법을 공부해야 한다.
 * 실무에서는 재시도가 필요한 Lock의 경우에는 Redission을 활용하고, 그렇지 않을 경우에는 Lettuce을 활용한다.
 * 재시도가 필요한 경우?: 선착순 100명 까지 물품을 구매할 수 있을 경우
 * 재시도가 필요하지 않은 경우?: 선착순 한명만 가능, Lock 획득 재시도 할 필요가 없음
 */
@Component
public class RedissonLockStockFacade {

    private RedissonClient redissonClient;

    private StockService stockService;

    public RedissonLockStockFacade(RedissonClient redissonClient, StockService stockService) {
        this.redissonClient = redissonClient;
        this.stockService = stockService;
    }

    public void decrease(Long key, Long quantity) {
        RLock lock = redissonClient.getLock(key.toString());

        try {
            boolean available = lock.tryLock(20, 1, TimeUnit.SECONDS);

            if (!available) {
                System.out.println("lock 획득 실패");
                return;
            }

            stockService.decrease(key, quantity);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
