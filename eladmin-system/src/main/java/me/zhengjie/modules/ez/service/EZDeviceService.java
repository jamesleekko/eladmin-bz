package me.zhengjie.modules.ez.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.ez.config.bean.SysDeviceCamera;
import me.zhengjie.modules.ez.repository.SysDeviceCameraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Service
//@RequiredArgsConstructor
@Slf4j
public class EZDeviceService {

    private final RestTemplate restTemplate;
    private final SysDeviceCameraRepository sysDeviceCameraRepository;

    private static final String BASE_URL = "https://open.ys7.com/api/lapp";

    @Autowired
    public EZDeviceService(@Qualifier("EZRestTemplate") RestTemplate restTemplate, SysDeviceCameraRepository sysDeviceCameraRepository) {
        this.restTemplate = restTemplate;
        this.sysDeviceCameraRepository = sysDeviceCameraRepository;
    }

    // 获取设备列表的接口
    public void fetchAndSaveDeviceList(String token) {
        String url = BASE_URL + "/device/list";
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("accessToken", token)
                .queryParam("pageStart", 0)
                .queryParam("pageSize", 50);

        try {
            //清除现有记录
            sysDeviceCameraRepository.deleteAll();

            // 发送请求
            ResponseEntity<String> response = restTemplate.postForEntity(uriBuilder.toUriString(), null, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("设备列表请求成功: {}", response.getBody());

                // 解析返回数据
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(response.getBody());

                if (jsonNode.get("code").asText().equals("200")) {
                    // 解析 data 中的设备列表
                    JsonNode devices = jsonNode.get("data");

                    for (JsonNode device : devices) {
                        String deviceSerial = device.has("deviceSerial") ? device.get("deviceSerial").asText(null) : null;
                        String deviceType = device.has("deviceType") ? device.get("deviceType").asText(null) : null;
                        String deviceName = device.has("deviceName") ? device.get("deviceName").asText(null) : null;
                        String deviceIp = device.has("deviceIp") ? device.get("deviceIp").asText(null) : null;
                        Integer cameraNo = device.has("cameraNo") ? device.get("cameraNo").asInt() : 1;
                        Integer deviceStatus = device.has("status") ? (device.get("status").isInt() ? device.get("status").asInt() : null) : null;
                        Long deptId = 18L;
                        Integer zbType = 1;


                        // 判断 device_type 是否包含 "h6c"
                        if (deviceType.toLowerCase().contains("h6c")) {
                            log.info("设备符合摄像头type, deviceSerial: {}", deviceSerial);

                            // 构建设备实体并保存
                            SysDeviceCamera deviceEntity = new SysDeviceCamera();
                            deviceEntity.setDeviceSerial(deviceSerial);
                            deviceEntity.setDeviceType(deviceType);
                            deviceEntity.setDeviceName(deviceName);
                            deviceEntity.setDeviceIp(deviceIp);
                            deviceEntity.setCameraNo(cameraNo);
                            deviceEntity.setDeviceStatus(deviceStatus);
                            deviceEntity.setDeptId(deptId);
                            deviceEntity.setZBType(zbType);

                            sysDeviceCameraRepository.save(deviceEntity);
                        }
                    }
                } else {
                    log.error("设备列表请求失败，错误信息: {}", jsonNode.get("msg").asText());
                }
            } else {
                log.error("设备列表请求失败，HTTP状态码: {}", response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("请求设备列表时发生异常", e);
        }
    }

    // 保存或更新设备到数据库
    private void saveOrUpdateDevice(SysDeviceCamera device) {
        Optional<SysDeviceCamera> existingDevice = sysDeviceCameraRepository.findByDeviceSerial(device.getDeviceSerial());

        if (existingDevice.isPresent()) {
            SysDeviceCamera existing = existingDevice.get();
            // 更新现有设备记录
            existing.setDeviceName(device.getDeviceName());
            existing.setDeviceType(device.getDeviceType());
            sysDeviceCameraRepository.save(existing);
            log.info("更新设备: {}", existing.getDeviceSerial());
        } else {
            // 插入新设备
            sysDeviceCameraRepository.save(device);
            log.info("保存新设备: {}", device.getDeviceSerial());
        }
    }
}
