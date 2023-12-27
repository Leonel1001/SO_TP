import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Scheduler {
    private Queue<Task> taskQueue;

    public Scheduler() {
        taskQueue = new ConcurrentLinkedQueue<>();
    }

    // Método para adicionar uma tarefa à fila de tarefas
    public void addTask(Task task) {
        taskQueue.add(task);
    }

    // Método para obter a próxima tarefa da fila
    public Task getNextTask() {
        return taskQueue.poll();
    }

    // Outros métodos e lógica podem ser adicionados conforme necessário
}
