package com.kkxx.diyls;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

/**
 * DATE：2017/10/22 on 12:11
 * Description:
 *
 * @author kkxx
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class DiyLSJobService extends JobService {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startJobScheduler();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        startService(new Intent(this, LockScreenService.class));
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    public void startJobScheduler() {
        Log.v("DaemonService", "start job");
        try {
            JobInfo.Builder builder = new JobInfo.Builder(1221, new ComponentName(getPackageName
                    (), DiyLSJobService.class.getName()));
            builder.setPeriodic(1000 * 2);
            builder.setPersisted(true);
            JobScheduler jobScheduler = (JobScheduler) this.getSystemService(Context
                    .JOB_SCHEDULER_SERVICE);
            jobScheduler.schedule(builder.build());
            // Android24版本才有scheduleAsPackage方法， 期待中
            //Class clz = Class.forName("android.app.job.JobScheduler");
            //Method[] methods = clz.getMethods();
            //Method method = clz.getMethod("scheduleAsPackage", JobInfo.class , String.class,
            // Integer.class, String.class);
            //Object obj = method.invoke(jobScheduler, builder.build(), "com.brycegao.autostart",
            // "brycegao", "test");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
