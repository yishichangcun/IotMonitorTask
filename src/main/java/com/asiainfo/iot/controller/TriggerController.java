//package com.asiainfo.iot.controller;
//
//import com.asiainfo.iot.schedule.QuartzManager;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.validation.Valid;
//
//@RestController
//@RequestMapping("/trigger")
//public class TriggerController {
//
//    @Autowired
//    private QuartzManager manager;
//
//    /**
//     * @desc 添加SimpleTrigger
//     */
//    @PostMapping("/addSimple")
//    public ResultBody addSimpleTrigger(@Valid @RequestBody TriggerParam triggerParam) {
//        manager.addSimppleTrigger(triggerParam.getTriggerName(),triggerParam.getStartDate(),triggerParam.getRepeatInterval(), triggerParam.getRepeatCount());
//        return new ResultBody<String>();
//    }
//
//    /**
//     * @desc 添加CronTrigger
//     */
//    @PostMapping("/addCron")
//    public ResultBody addCronTrigger(@Valid @RequestBody TriggerParam triggerParam {
//        manager.addCronTrigger(triggerParam.getTriggerName(), triggerParam.getCronExpression());
//        return new ResultBody<String>();
//    }
//
//    /**
//     * @desc 激活Trigger
//     */
//    @PostMapping("/resume")
//    public ResultBody resume(@Valid @RequestBody TriggerParam triggerParam) {
//        String triggerName = triggerParam.getTriggerName();
//        manager.resumeTrigger(triggerName);
//        return new ResultBody<String>();
//    }
//
//    /**
//     * @desc 暂停Trigger
//     */
//    @PostMapping("/pause")
//    public ResultBody pause(@Valid @RequestBody TriggerParam triggerParam) {
//        String triggerName = triggerParam.getTriggerName();
//        manager.pauseTrigger(triggerName);
//        return new ResultBody<String>();
//    }
//
//    /**
//     * @desc 删除Trigger
//     */
//    @PostMapping("/delete")
//    public ResultBody delete(@Valid @RequestBody TriggerParam triggerParam) {
//        String triggerName = triggerParam.getTriggerName();
//        manager.removeTrigger(triggerName);
//        return new ResultBody<String>();
//    }
//}
