package netty.server;

import io.netty.channel.Channel;
import netty.annotations.SingleChannelTask;
import netty.annotations.SingleTask;
import netty.server.tasks.ServerTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.*;

public class ServerExecutor<T extends ServerTask> {
    private final CustomThreadPool executor;

    /**
     * Stores channels and tasks for this channel, which are currently executed or queued for.
     */
    private final HashMap<Channel, ArrayList<T>> channels;

    public ServerExecutor(int corePoolSize, int maxPoolSize, int queueCapacity) {
        executor = new CustomThreadPool(
                corePoolSize, maxPoolSize,
                1, TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(queueCapacity)
        );
        channels = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    public synchronized boolean execute(T task) {
        if (executor.getQueue().remainingCapacity() == 0) {
            task.reject("server is overloaded");
            return false;
        }

        Channel channel = task.getContext().channel();
        if (channels.containsKey(channel)) {
            if (task.getClass().isAnnotationPresent(SingleChannelTask.class)) {
                task.reject("concurrent request denied (SingleChannelTask rejected)");
                return false;
            }
            if (task.getClass().isAnnotationPresent(SingleTask.class)) {
                for (T t : channels.get(channel)) {
                    if (t.getClass().isAnnotationPresent(SingleTask.class)) {
                        task.reject("concurrent request denied (SingleTask rejected)");
                        return false;
                    }
                }
            }
            channels.get(channel).add(task);
        } else {
            channels.put(channel, new ArrayList<>(Arrays.asList(task)));
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
            Channel channel = task.getContext().channel();
            ArrayList taskList = channels.get(channel);
            taskList.remove(task);
            if (taskList.isEmpty())
                channels.remove(channel);
        }
    }
}
