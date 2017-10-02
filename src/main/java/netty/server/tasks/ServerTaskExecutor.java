package netty.server.tasks;

import io.netty.channel.Channel;
import netty.annotations.SingleChannelTask;
import netty.annotations.SingleTask;
import netty.Console;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.*;

public class ServerTaskExecutor {
    private final CustomThreadPool executor;

    /**
     * Stores channels and tasks for this channel, which are currently executed or queued for.
     */
    private volatile ConcurrentHashMap<Channel, ArrayList<ServerTask>> channels;

    public ServerTaskExecutor(int corePoolSize, int maxPoolSize, int queueCapacity) {
        executor = new CustomThreadPool(
                corePoolSize, maxPoolSize,
                1, TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(queueCapacity)
        );
        channels = new ConcurrentHashMap<>();
    }

    @SuppressWarnings("unchecked")
    public synchronized boolean execute(ServerTask task) {
        Console.println("<EXECUTOR> NEW TASK: " + task.getClass().getSimpleName());
        if (executor.getQueue().remainingCapacity() == 0) {
            task.reject("server is overloaded");
            return false;
        }

        Channel taskChannel = task.getChannel();
        Class   taskClass   = task.getClass();

        if (taskClass.isAnnotationPresent(SingleChannelTask.class)) {
            Console.println("<EXECUTOR> SingleChannelTask DETECT");
        }
        if (taskClass.isAnnotationPresent(SingleTask.class)) {
            Console.println("<EXECUTOR> SingleTask DETECT");
        }

        if (channels.containsKey(taskChannel)) {
            if (taskClass.isAnnotationPresent(SingleChannelTask.class)) {
                task.reject("concurrent request denied (SingleChannelTask rejected)");
                Console.println("<EXECUTOR> SingleChannelTask REJECTED: " + taskClass.getSimpleName());
                return false;
            }
            if (taskClass.isAnnotationPresent(SingleTask.class)) {
                for (ServerTask t : channels.get(taskChannel)) {
                    if (t.getClass().equals(taskClass)) {
                        task.reject("concurrent request denied (SingleTask rejected)");
                        Console.println("<EXECUTOR> SingleTask REJECTED: " + taskClass.getSimpleName());
                        return false;
                    }
                }
            }
            channels.get(taskChannel).add(task);
        } else {
            channels.put(taskChannel, new ArrayList<>(Arrays.asList(task)));
        }

        executor.execute(task);
        return true;
    }

    private class CustomThreadPool extends ThreadPoolExecutor {
        CustomThreadPool(int core, int max, long time, TimeUnit unit, BlockingQueue<Runnable> queue) {
            super(core, max, time, unit, queue);
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            ServerTask task = (ServerTask) r;
            Channel taskChannel = task.getChannel();
            ArrayList channelTaskList = channels.get(taskChannel);
            channelTaskList.remove(task);
            if (channelTaskList.isEmpty())
                channels.remove(taskChannel);
        }
    }
}
