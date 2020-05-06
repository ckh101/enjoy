package com.hbnet.fastsh.schedule.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hbnet.fastsh.constants.WebConstants;
import com.hbnet.fastsh.redis.service.RedisService;
import com.hbnet.fastsh.schedule.executor.TaskExecutor;
import com.hbnet.fastsh.web.vo.task.AdTaskParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: BatchTaskConsumer
 * @Auther: zoulr@qq.com
 * @Date: 2019/8/23 10:34
 * @Description: 批量广告任务消费者
 */
@Slf4j
@Component
public class BatchTaskConsumer implements CommandLineRunner {
    private static final int REDIS_EXCEPTION_WAITING_TIME = 1 * 1000; // second

    /**
     * 最大休眠，单位：毫秒
     */
    private static final int MAX_SLEEP = 100;

    /**
     * 休眠时长加长/缩短的步长，单位：毫秒
     */
    private static final int SLEEP_STEP = 10;

    /**
     * 空值计数器<br>
     * taskId 为空时，+1，否则 -1<br>
     * 最大值为 MAX_NULL_COUNTER
     * 最小值为 MIN_NULL_COUNTER
     */
    private int nullCounter = 0;

    /**
     * 最大空值计数
     */
    private static final int MAX_NULL_COUNTER = 100;

    /**
     * 最小空值计数
     */
    private static final int MIN_NULL_COUNTER = 0;

    /**
     * 计数步长
     */
    private static final int COUNT_STEP = 1;

    @Autowired
    private RedisService redisService;

    @Autowired
    private BeanFactory beanFactory;

    @Override
    public void run(String... args) {
        log.info("准备启动批量任务处理器...", getThreadName());

        Thread payThread = new Thread(() -> {
            for (; ; ) {
                String taskId = null;
                try {
                    taskId = redisService.queuePop(WebConstants.REDIS_KEY_BATCH_TASK_QUEUE);
                } catch (Exception e) {
                    log.error("从Redis队列获取任务ID失败:", e);
                    try {
                        TimeUnit.MILLISECONDS.sleep(REDIS_EXCEPTION_WAITING_TIME);
                    } catch (InterruptedException e1) {
                        log.error("[严重异常]任务消费者休眠异常，停止所有消费！", e);
                        break;
                    }
                }

                if (StringUtils.isNotBlank(taskId)) {
                    log.debug("准备处理任务{}", taskId);

                    String taskDetail = redisService.get("smartad-task-" + taskId);
                    if (StringUtils.isBlank(taskDetail)) {
                        log.warn("批量任务{}无法获取执行内容!", taskId);
                        continue;
                    }

                    JSONObject json = JSON.parseObject(taskDetail);
                    if (!json.containsKey("succedCnt")) { // 包含此字段表示已经完成
                        AdTaskParam taskParam = json.toJavaObject(AdTaskParam.class);
                        TaskExecutor executor = beanFactory.getBean(taskParam.getType().concat("TaskExecutor"), TaskExecutor.class);
                        executor.submit(taskParam);
                    }
                }

                // 计数
                count(taskId);

                // 休眠
                try {
                    letCpuRest();
                } catch (InterruptedException e) {
                    log.error("[严重异常]任务消费者休眠异常-letCpuRest，停止所有消费！", e);
                    break;
                }
            }
        }, getThreadName());

        // 将此线程设置为守护线程
        payThread.setDaemon(true);
        // 启动线程
        payThread.start();

        log.info("批量任务处理器启动成功!", getThreadName());
    }

    /**
     * 获取线程名称
     */
    private String getThreadName() {
        return this.getClass().getSimpleName();
    }

    /**
     * 计数
     */
    private void count(String taskId) {
        if (StringUtils.isBlank(taskId)) {
            int temp = this.nullCounter + COUNT_STEP;
            this.nullCounter = temp > MAX_NULL_COUNTER ? MAX_NULL_COUNTER : temp;
        } else {
            int temp = this.nullCounter - COUNT_STEP;
            this.nullCounter = temp < MIN_NULL_COUNTER ? MIN_NULL_COUNTER : temp;
        }
    }

    /**
     * 让CPU休息一下<br>
     */
    private void letCpuRest() throws InterruptedException {
        int amount = this.nullCounter / SLEEP_STEP;

        if (amount > 0) {
            int duration = (amount * SLEEP_STEP) > MAX_SLEEP ? MAX_SLEEP : (amount * SLEEP_STEP);

            TimeUnit.MILLISECONDS.sleep(duration);
        }
    }
}
