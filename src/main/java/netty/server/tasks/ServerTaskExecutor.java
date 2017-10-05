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
    private String name;
    
    private String logPrefix;

    /**
     * Stores channels and tasks for this channel, which are currently executed or queued for.
     */
    private volatile ConcurrentHashMap<Channel, ArrayList<ServerTask>> channels;
    
    public ServerTaskExecutor(int corePoolSize, int maxPoolSize, int queueCapacity) {
        this(corePoolSize, maxPoolSize, queueCapacity, null);
    }

    public ServerTaskExecutor(int corePoolSize, int maxPoolSize, int queueCapacity, String name) {
        executor = new CustomThreadPool(
                corePoolSize, maxPoolSize,
                1, TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(queueCapacity)
        );
        channels = new ConcurrentHashMap<>();
        this.name = name;
        logPrefix = "<EXECUTOR" + (name != null ? ":" + name : "") + "> ";
    }

    @SuppressWarnings("unchecked")
    public synchronized boolean execute(ServerTask task) {
        Console.println(logPrefix + "NEW TASK: " + task.getClass().getSimpleName());
        if (executor.getQueue().remainingCapacity() == 0) {
            task.reject("server is overloaded");
            return false;
        }

        Channel taskChannel = task.getChannel();
        Class   taskClass   = task.getClass();

        if (taskClass.isAnnotationPresent(SingleChannelTask.class)) {
            Console.println(logPrefix + "@SingleChannelTask DETECT: " + taskClass.getSimpleName());
        }
        if (taskClass.isAnnotationPresent(SingleTask.class)) {
            Console.println(logPrefix + "@SingleTask DETECT: " + taskClass.getSimpleName());
        }

        if (channels.containsKey(taskChannel)) {
            if (taskClass.isAnnotationPresent(SingleChannelTask.class)) {
                task.reject("concurrent response denied (SingleChannelTask rejected)");
                Console.println(logPrefix + "@SingleChannelTask REJECTED: " + taskClass.getSimpleName());
                return false;
            }
            if (taskClass.isAnnotationPresent(SingleTask.class)) {
                for (ServerTask t : channels.get(taskChannel)) {
                    if (t.getClass().equals(taskClass)) {
                        task.reject("concurrent response denied (SingleTask rejected)");
                        Console.println(logPrefix + "@SingleTask REJECTED: " + taskClass.getSimpleName());
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

    public String getName() {
        return name;
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
            Console.println(logPrefix + "TASK RELEASED: " + task.getClass().getSimpleName());
            if (channelTaskList.isEmpty())
                channels.remove(taskChannel);
        }
    }
}
