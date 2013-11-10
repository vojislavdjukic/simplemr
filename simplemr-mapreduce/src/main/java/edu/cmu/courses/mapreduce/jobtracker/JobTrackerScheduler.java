package edu.cmu.courses.mapreduce.jobtracker;

import edu.cmu.courses.mapreduce.common.Constants;
import edu.cmu.courses.mapreduce.task.Task;
import edu.cmu.courses.mapreduce.task.TaskStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JobTrackerScheduler implements Runnable{
    private static Logger LOG = LoggerFactory.getLogger(JobTrackerScheduler.class);

    private JobTracker jobTracker;
    private ExecutorService pool;

    public JobTrackerScheduler(JobTracker jobTracker, int poolSize){
        this.jobTracker = jobTracker;
        if(poolSize <= 0){
            this.pool = Executors.newFixedThreadPool(Constants.DEFAULT_SCHEDULED_THREAD_POOL_SIZE);
        } else {
            this.pool = Executors.newFixedThreadPool(poolSize);
        }
    }

    @Override
    public void run() {
        while(true){
            try {
                Task task = jobTracker.takeTask();
                task.setStatus(TaskStatus.PENDING);
                JobTrackerDispatcher dispatcher = new JobTrackerDispatcher(jobTracker, task);
                pool.execute(dispatcher);
            } catch (InterruptedException e) {
                LOG.error("Job scheduler is interrupted!", e);
                break;
            }
        }
    }
}
