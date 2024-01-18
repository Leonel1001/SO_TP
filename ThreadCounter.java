import java.util.concurrent.atomic.AtomicInteger;

public class ThreadCounter extends Thread {
    private final Cpu cpu;
    final AtomicInteger activeThreadCount;
    private final Bomber bomber;
    private final AutomaticMessage automaticMessage;

    // Construtor da classe ThreadCounter, que recebe instâncias das classes Cpu,
    // Bomber e AutomaticMessage.
    public ThreadCounter(Cpu cpu, Bomber bomber, AutomaticMessage automaticMessage) {
        this.cpu = cpu;
        this.bomber = bomber;
        this.automaticMessage = automaticMessage;
        this.activeThreadCount = new AtomicInteger(0);
    }

    @Override
    public void run() {
        // Loop infinito para ver continuamente o número de threads ativas.
        while (true) {
            int activeThreadCountCpu = (cpu != null) ? cpu.getActiveThreadCount() : 0;
            int activeThreadCountBomber = (bomber != null) ? bomber.getActiveThreadCount() : 0;
            int activeThreadCountAutomatic = (automaticMessage != null) ? automaticMessage.getActiveThreadCount() : 0;
            int totalActiveThreadCount = activeThreadCountCpu + activeThreadCountBomber + activeThreadCountAutomatic;
            this.activeThreadCount.set(totalActiveThreadCount);

            try {
                Thread.sleep(1000); // Agora dorme por 1 segundo para evitar uma iteração muito rápida
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    // Método sincronizado para obter o número total de threads ativas.

    public synchronized int getActiveThreadCount() {
        return activeThreadCount.get();
    }

}
