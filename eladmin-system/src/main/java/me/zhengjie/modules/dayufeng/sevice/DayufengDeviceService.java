package me.zhengjie.modules.dayufeng.sevice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DayufengDeviceService {

    @Autowired
    @Lazy  // 延迟加载DayufengApiClient，避免循环依赖
    private DayufengApiClient apiClient;


}
