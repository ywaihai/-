//package com.waihai.usercenter.once;
//
//import com.waihai.usercenter.mapper.UserMapper;
//import com.waihai.usercenter.model.domin.User;
//import jakarta.annotation.Resource;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StopWatch;
//
//@Component
//public class InsertUsers {
//    @Resource
//    private UserMapper userMapper;
//
//    /**
//     * 循环插入用户
//     */
////    @Scheduled(initialDelay = 5000,fixedRate = Long.MAX_VALUE )
//    public void doInsertUser() {
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//        final int INSERT_NUM = 1000;
//        for (int i = 0; i < INSERT_NUM; i++) {
//            User user = new User();
//            user.setUsername("假外害");
//            user.setUserAccount("waihai");
//            user.setAvatarUrl("https://ts1.cn.mm.bing.net/th/id/R-C.e4fe1a1677f72143432de42d44e85f9f?rik=QNhDoEtIS2Ve3A&riu=http%3a%2f%2finews.gtimg.com%2fnewsapp_match%2f0%2f15103659616%2f0&ehk=gvcKqCFPB5ADv7JtilqqKL%2b1U87peA41C0HCNdeflKc%3d&risl=&pid=ImgRaw&r=0");
//            user.setProfile("二十岁的草木无可奈何，等待着三十五岁的颂歌。");
//            user.setGender(0);
//            user.setUserPassword("12345678");
//            user.setPhone("123456789108");
//            user.setEmail("waihai@qq.com");
//            user.setUserStatus(0);
//            user.setUserRole(0);
//            user.setPlanetCode("666");
//            user.setTags("[]");
//            userMapper.insert(user);
//        }
//        stopWatch.stop();
//        System.out.println( stopWatch.getLastTaskTimeMillis());
//
//    }
//}
