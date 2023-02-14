package com.example.stock.facade;

import com.example.stock.repository.RedisLockRepository;
import com.example.stock.service.StockService;
import org.springframework.stereotype.Component;

/**
 * 구현이 간단하다.
 * Spring Data Redis를 이용하면 Lettuce가 기본이기 때문에 별도의 라이브러리를 사용하지 않아도 된다.
 * Spin Lock 방식이기 때문에 동시에 많은 스레드가 Lock 획득 대기 상태라면 Redis에 부하를 줄 수 있다.
 * -> 그래서 락획득 실패시 Thread.sleep(100)을 통해 재시도에 대한 시간적 텀을 두게 하였다!!
 * 실무에서는 재시도가 필요한 Lock의 경우에는 Redission을 활용하고, 그렇지 않을 경우에는 Lettuce을 활용한다.
 * 재시도가 필요한 경우?: 선착순 100명 까지 물품을 구매할 수 있을 경우
 * 재시도가 필요하지 않은 경우?: 선착순 한명만 가능, Lock 획득 재시도 할 필요가 없음
 */
@Component
public class LettuceLockStockFacade {

    private RedisLockRepository redisLockRepository;

    private StockService stockService;

    public LettuceLockStockFacade(RedisLockRepository redisLockRepository, StockService stockService) {
        this.redisLockRepository = redisLockRepository;
        this.stockService = stockService;
    }

    public void decrease(Long key, Long quantity) throws InterruptedException {
        while (!redisLockRepository.lock(key)) {
            Thread.sleep(100);
        }

        try {
            stockService.decrease(key, quantity);
        } finally {
            redisLockRepository.unlock(key);
        }
    }
}
