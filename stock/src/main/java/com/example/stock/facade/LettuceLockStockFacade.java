package com.example.stock.facade;

import com.example.stock.repository.RedisLockRepository;
import com.example.stock.service.StockService;
import org.springframework.stereotype.Component;

/*
* Lettuce를 활용한 방법은 구현이 간단하다는 장점이 있다.
* 단점으로는 스핀락 방식으로 진행하기에 동시에 많은 쓰레드가 lock 획득 대기 상태라면 레디스에 부하를 줄 수 있다.
* -> 그래서 락획득 실패시 Thread.sleep(100)을 통해 재시도에 대한 시간적 텀을 두게 하였다!!
* */
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
