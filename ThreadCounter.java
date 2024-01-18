import java.util.concurrent.atomic.AtomicInteger;

public class ThreadCounter extends Thread {
    private final Cpu cpu;
    final AtomicInteger activeThreadCount;
    private final Bomber bomber;
    private final AutomaticMessage automaticMessage;

    public ThreadCounter(Cpu cpu, Bomber bomber, AutomaticMessage automaticMessage) {
        this.cpu = cpu;
        this.bomber = bomber;
        this.automaticMessage = automaticMessage;
        this.activeThreadCount = new AtomicInteger(0);
    }

    @Override
    public void run() {
        while (true) {
            int activeThreadCountCpu = (cpu != null) ? cpu.getActiveThreadCount() : 0;
            int activeThreadCountBomber = (bomber != null) ? bomber.getActiveThreadCount() : 0;
            int activeThreadCountAutomatic = (automaticMessage != null) ? automaticMessage.getActiveThreadCount() : 0;
            int totalActiveThreadCount = activeThreadCountCpu + activeThreadCountBomber + activeThreadCountAutomatic;
            this.activeThreadCount.set(totalActiveThreadCount);

            // Imprime a contagem de threads
            System.out.println("Total Active Threads: " + totalActiveThreadCount);
            System.out.println("  CPU Threads: " + activeThreadCountCpu);
            System.out.println("  Bomber Threads: " + activeThreadCountBomber);
            System.out.println("  AutomaticMessage Threads: " + activeThreadCountAutomatic);

            try {
                Thread.sleep(1000); // Agora dorme por 1 segundo para evitar uma iteração muito rápida
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized int getActiveThreadCount() {
        return activeThreadCount.get();
    }

}
