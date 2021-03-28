package playground;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.atomic.LongAdder;

@Slf4j
public class Reactor {

    static LongAdder globalCounter = new LongAdder();

    Flux<Long> checkPressure(Flux<Long> i) {
        log.info("i value: {}", i);

        return Flux.interval(Duration.ofSeconds(1), Schedulers.elastic())
                   .doOnNext(o -> globalCounter.increment())
                   .filter(v -> {
                       if (globalCounter.intValue() > 5)
                           return true;
                       else
                           return false;
                   });
//        return Flux.interval(Duration.ofSeconds(1), Schedulers.single())
//                   .filter(v -> v % 5 == 0);
    }

    @Test
    public void oneRepeatBackpressured() {

        Mono<String> source = Mono.just("X");

        LongAdder count = new LongAdder();

        Mono<Integer> src = Mono.defer(() -> Mono.just(count.intValue()));

        src.repeatWhen(i -> checkPressure(i))
           .takeUntil(u -> u == 10)
           .log()
           .doOnNext(s -> {
               log.info("Get {}", s);
               count.increment();
           })
           .blockLast();
    }
}
