package org.example.resource;

import org.example.dao.repo.UserInfoRepo;
import org.example.dto.UserInfoOutDto;
import org.example.util.Snowflake;
import org.example.vo.UserInfoVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author mr.ahai
 */
@RestController
public class UserInfoResource {
    @Resource
    private UserInfoRepo userInfoRepo;

    @GetMapping("/insertMsg")
    @Transactional
    public UserInfoOutDto insertMsg() {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Snowflake snowflake = new Snowflake(0,0);
        for (int i = 0; i < 1000; i++) {
            UserInfoVo userInfoVo = new UserInfoVo();
            userInfoVo.setUserName("yyds");
            userInfoVo.setAge(10);
            userInfoVo.setId(snowflake.nextId());
            userInfoRepo.insertUserInfo(userInfoVo);
        }

        stopWatch.stop();

        System.out.println(stopWatch.prettyPrint());
        System.out.println(stopWatch.getTotalTimeMillis()/1000000000);
        System.out.println(stopWatch.getLastTaskName());
        System.out.println(stopWatch.getLastTaskInfo());
        System.out.println(stopWatch.getTaskCount());

        return null;
    }
}
