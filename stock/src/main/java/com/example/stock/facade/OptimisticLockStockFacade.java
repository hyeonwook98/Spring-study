package com.example.stock.facade;

import com.example.stock.service.OptimisticLockStockService;
import org.springframework.stereotype.Service;

/**
 * Lock을 잡지 않으므로, Pessimistic Lock(낙관적 락)보다 성능상 이점이 있을 수 있다.
 * 하지만, 업데이트가 실패했을 경우 재시도 로직이 개발자가 직접 작성을 해주어야 한다.
 * 또한 충돌이 빈번하게 일어난다면, Pessimistic Lock이 성능상 이점이 더 있을 수 있다.
 */
@Service
public class OptimisticLockStockFacade {

    private OptimisticLockStockService optimisticLockStockService;

    public OptimisticLockStockFacade(OptimisticLockStockService optimisticLockStockService) {
        this.optimisticLockStockService = optimisticLockStockService;
    }

    public void decrease(Long id, Long quantity) throws InterruptedException{
        while (true) {
            try {
                optimisticLockStockService.decrease(id, quantity);

                break;
            } catch (Exception e) {
                Thread.sleep(50);
            }
        }
    }
}
