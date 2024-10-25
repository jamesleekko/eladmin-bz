package me.zhengjie.modules.dayufeng.rest;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.dayufeng.data.SysDeviceFreezer;
import me.zhengjie.modules.dayufeng.data.SysDeviceSmokeAlarm;
import me.zhengjie.modules.dayufeng.repository.SysDeviceFreezerRepository;
import me.zhengjie.modules.dayufeng.repository.SysDeviceSmokeRepository;
import me.zhengjie.modules.dayufeng.sevice.DayufengDeviceService;
import me.zhengjie.modules.dayufeng.sevice.DayufengTokenService;
import me.zhengjie.modules.system.service.DataService;
import me.zhengjie.modules.system.service.UserService;
import me.zhengjie.modules.system.service.dto.UserDto;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dayufeng")
@Slf4j
@RequiredArgsConstructor
@Api(tags = "大昱丰相关接口")
public class DayufengController {

    @Autowired
    private DayufengTokenService dayufengTokenService;

    @Autowired
    DataService dataService;

    @Autowired
    UserService userService;

    @Autowired
    private SysDeviceFreezerRepository sysDeviceFreezerRepository;

    @Autowired
    private SysDeviceSmokeRepository sysDeviceSmokeRepository;

    @GetMapping("/token")
    public ResponseEntity<String> getToken() {
        if (dayufengTokenService.isTokenValid()) {
            // Token is valid, return it
            String token = dayufengTokenService.getToken();
            log.info("Returning valid token: {}", token);
            return ResponseEntity.ok(token);
        } else {
            // Token is invalid, attempt to refresh
            try {
                dayufengTokenService.refreshToken();
                String refreshedToken = dayufengTokenService.getToken();
                log.info("Returning refreshed token: {}", refreshedToken);
                return ResponseEntity.ok(refreshedToken);
            } catch (RuntimeException e) {
                log.error("Failed to refresh token: {}", e.getMessage());
                return ResponseEntity.status(500).body("Failed to get token");
            }
        }
    }

    @ApiOperation("获取冰箱开关设备列表")
    @GetMapping("/freezer_list")
    public ResponseEntity<List<SysDeviceFreezer>> getFreezerList() {
        UserDto user = userService.findByName(SecurityUtils.getCurrentUsername());
        List<Long> deptIds = dataService.getDeptIds(user);

        List<SysDeviceFreezer> freezers = sysDeviceFreezerRepository.findByDeptIdIn(deptIds);
        return ResponseEntity.ok(freezers);
    }

    @ApiOperation("获取烟雾报警器设备列表")
    @GetMapping("/smoke_alarm_list")
    public ResponseEntity<List<SysDeviceSmokeAlarm>> getSmokeAlarmList() {
        UserDto user = userService.findByName(SecurityUtils.getCurrentUsername());
        List<Long> deptIds = dataService.getDeptIds(user);

        List<SysDeviceSmokeAlarm> smokeAlarms = sysDeviceSmokeRepository.findByDeptIdIn(deptIds);
        return ResponseEntity.ok(smokeAlarms);
    }
}