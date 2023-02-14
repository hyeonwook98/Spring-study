package com.example.stock.facade;

import com.example.stock.repository.LockRepository;
import com.example.stock.service.StockService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Named Lock은 주로 분산 락을 구현할 때 사용한다.
 * Pessimistic Lock은 타임아웃을 구현하기 굉장히 까다롭지만, Named Lock은 손쉽게 구현할 수 있다.
 * 이외에도 데이터 삽입시에 정합성을 맞춰야 하는 경우에도 사용할 수 있다.
 * 하지만 이 방법은 트랜잭션 종료시에 락 해제와 세션 관리를 직접 잘 해줘야하므로 주의해서 사용해야하고, 사용할 때는 구현방법이 복잡할 수 있다.
 */
@Component
public class NamedLockStockFacade {

    private final LockRepository lockRepository;
    private final StockService stockService;

    public NamedLockStockFacade(LockRepository lockRepository, StockService stockService) {
        this.lockRepository = lockRepository;
        this.stockService = stockService;
    }

    @Transactional
    public void decrease(Long id, Long quantity) {
        try {
            lockRepository.getLock(id.toString());
            stockService.decreaseWithNamedLock(id, quantity); // Naned Lock사용시 부모의 트랜잭션과 별도로 실행되어야한다.
        } finally {
            lockRepository.releaseLock(id.toString());
        }
    }
}
