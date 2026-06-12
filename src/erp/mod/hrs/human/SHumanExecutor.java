/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.human;

import java.util.concurrent.*;

/**
 *
 * @author César Orozco
 */
public class SHumanExecutor {
    private static final ExecutorService executor =
        new ThreadPoolExecutor(
            5,
            5,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(100),
            new ThreadPoolExecutor.AbortPolicy() // manejo de saturación
        );

    public static void execute(Runnable task) {
        executor.execute(task);
    }
}
