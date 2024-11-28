package me.zhengjie.modules.dayufeng.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.annotation.Log;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.dayufeng.data.*;
import me.zhengjie.modules.dayufeng.repository.SysDeviceDoorRepository;
import me.zhengjie.modules.dayufeng.repository.SysDeviceFreezerRepository;
import me.zhengjie.modules.dayufeng.repository.SysDeviceSmokeRepository;
import me.zhengjie.modules.dayufeng.repository.SysDeviceTempHumiRepository;
import me.zhengjie.modules.dayufeng.sevice.DayufengTokenService;
import me.zhengjie.modules.system.service.DataService;
import me.zhengjie.modules.system.service.UserService;
import me.zhengjie.modules.system.service.dto.UserDto;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Autowired
    private SysDeviceDoorRepository sysDeviceDoorRepository;

    @Autowired
    private SysDeviceTempHumiRepository sysDeviceTempHumiRepository;

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

    @ApiOperation("获取门禁设备列表")
    @GetMapping("/door_list")
    public ResponseEntity<List<MergedDeviceDoor>> getDoorList() {
        log.info("开始获取门禁设备列表");
        UserDto user = userService.findByName(SecurityUtils.getCurrentUsername());
        List<Long> deptIds = dataService.getDeptIds(user);

        List<SysDeviceDoor> localDoors = sysDeviceDoorRepository.findByDeptIdIn(deptIds);
        log.info("localDoors: {}", localDoors);
        Set<String> localGuids = localDoors.stream()
                .map(SysDeviceDoor::getDoorGuid)
                .collect(Collectors.toSet());
        log.info("door guids: {}", localGuids);

        List<MergedDeviceDoor> externalDoors = fetchDoorsFromDayufeng();

        List<MergedDeviceDoor> mergedDoors = externalDoors.stream()
                .filter(door -> localGuids.contains(door.getDoorGuid()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(mergedDoors);
    }

    private List<MergedDeviceDoor> fetchDoorsFromDayufeng() {
        String apiUrl = "https://api.dayufeng.cn/door_list?login_type=2";
        RestTemplate restTemplate = new RestTemplate();

        try {
            // 配置请求头（根据接口需求调整）
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + dayufengTokenService.getToken()); // 替换为实际的Token获取逻辑
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // 发起请求
            ResponseEntity<Map> response = restTemplate.exchange(
                    apiUrl, HttpMethod.GET, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
                List<Map<String, Object>> doors = (List<Map<String, Object>>) data.get("data");

                // 转换返回数据为 SysDeviceDoor 列表
                return doors.stream().map(this::convertToSysDeviceDoor).collect(Collectors.toList());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 如果调用失败，返回空列表
        return Collections.emptyList();
    }

    private MergedDeviceDoor convertToSysDeviceDoor(Map<String, Object> doorData) {
        MergedDeviceDoor door = new MergedDeviceDoor();
        door.setDoorGuid((String) doorData.get("door_guid"));
        door.setDoorName((String) doorData.get("door_name"));
        door.setDoorStatus((Integer) doorData.get("door_status"));
        door.setLineWay((Integer) doorData.get("line_way"));
        door.setDoorSN((String) doorData.get("door_sn"));
        return door;
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

    @ApiOperation("获取温湿度一体机设备列表")
    @GetMapping("/temp_humi_list")
    public ResponseEntity<List<SysDeviceTempAndHumi>> getTempHumiList(@RequestParam(required = false) String category) {
        UserDto user = userService.findByName(SecurityUtils.getCurrentUsername());
        List<Long> deptIds = dataService.getDeptIds(user);

        List<SysDeviceTempAndHumi> tempAndHumiDevices;
        if (category != null) {
            try {
                Category parsedCategory = Category.valueOf(category.toUpperCase());
                tempAndHumiDevices = sysDeviceTempHumiRepository.findByDeptIdInAndCategory(deptIds, parsedCategory);
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid category: " + category);
            }
        } else {
            tempAndHumiDevices = sysDeviceTempHumiRepository.findByDeptIdIn(deptIds);
        }

        // 返回设备列表
        return ResponseEntity.ok(tempAndHumiDevices);
    }
}