package me.zhengjie.modules.ez.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.ez.config.bean.SysDeviceCamera;
import me.zhengjie.modules.ez.config.bean.SysDeviceCategory;
import me.zhengjie.modules.ez.repository.SysDeviceCameraRepository;
import me.zhengjie.modules.ez.repository.SysDeviceCategoryRepository;
import me.zhengjie.modules.ez.service.EZDeviceService;
import me.zhengjie.modules.ez.service.EZTokenService;
import me.zhengjie.modules.system.service.DataService;
import me.zhengjie.modules.system.service.UserService;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static me.zhengjie.utils.SecurityUtils.getCurrentUser;

@Slf4j
@RestController
@RequestMapping("/zb")
@RequiredArgsConstructor
@Api(tags = "萤石云相关接口")
public class EZController {

    @Autowired
    private EZDeviceService ezDeviceService;

    @Autowired
    private EZTokenService ezTokenService;

    @Autowired
    DataService dataService;

    @Autowired
    UserService userService;

    @Autowired
    private SysDeviceCameraRepository sysDeviceCameraRepository;

    @Autowired
    private SysDeviceCategoryRepository sysDeviceCategoryRepository;

    @PostMapping("/token")
    public String getToken() {
        if (ezTokenService.isTokenExpired()) {
            ezTokenService.fetchToken();  // 重新获取 token
        }
        return ezTokenService.getAccessToken();
    }

    @ApiOperation("获取设备类型列表")
    @GetMapping(value = "/device_category_list")
    public ResponseEntity<List<SysDeviceCategory>> getDeviceCategoryList() {

        // 根据 deptIds 查询设备列表
        List<SysDeviceCategory> categories = sysDeviceCategoryRepository.findAll();

        return ResponseEntity.ok(categories);
    }

    @ApiOperation("获取摄像头列表")
    @GetMapping(value = "/camera_list")
    public ResponseEntity<List<SysDeviceCamera>> getCameraList() {
        List<Long> deptIds = dataService.getDeptIds(userService.findByName(SecurityUtils.getCurrentUsername()));

        // 根据 deptIds 查询设备列表
        List<SysDeviceCamera> devices = sysDeviceCameraRepository.findByDeptIdIn(deptIds);

        return ResponseEntity.ok(devices);
    }
}
