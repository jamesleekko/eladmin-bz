package me.zhengjie.modules.system.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.system.domain.data.*;
import me.zhengjie.modules.system.repository.*;
import me.zhengjie.modules.system.service.DataService;
import me.zhengjie.modules.system.service.UserService;
import me.zhengjie.modules.system.service.dto.UserDto;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor
@Api(tags = "库存相关接口")
public class StockController {

    @Autowired
    DataService dataService;

    @Autowired
    UserService userService;

    @Autowired
    private StockItemRepository stockItemRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockInboundRecordRepository stockInboundRecordRepository;

    @Autowired
    private StockOutboundRecordRepository stockOutboundRecordRepository;

    @Autowired
    private StockOperationLogRepository stockOperationLogRepository;

    /**
     * 获取所有库存物品列表
     */
    @GetMapping("/items")
    @ApiOperation("获取所有库存物品列表")
    public ResponseEntity<List<SysStockItem>> getAllStockItems() {
        List<SysStockItem> items = stockItemRepository.findAll();
        return ResponseEntity.ok(items);
    }

    /**
     * 获取某个部门的库存列表
     */
    @GetMapping("/stocks")
    @ApiOperation("获取某个部门的库存列表")
    public ResponseEntity<List<SysStock>> getStockListByDept() {
        UserDto user = userService.findByName(SecurityUtils.getCurrentUsername());
        List<Long> deptIds = dataService.getDeptIds(user);

        List<SysStock> stocks = stockRepository.findByDeptIdIn(deptIds);
        return ResponseEntity.ok(stocks);
    }

    /**
     * 入库操作
     */
    @PostMapping("/inbound")
    @ApiOperation("新增入库记录")
    public ResponseEntity<String> addInboundRecord(@RequestBody SysStockInboundRecord inboundRecord) {
        UserDto user = userService.findByName(SecurityUtils.getCurrentUsername());
        List<Long> deptIds = dataService.getDeptIds(user);

        SysStock stock = stockRepository
                .findByItem_ItemIdAndDeptIdIn(inboundRecord.getItem().getItemId(), deptIds)
                .stream().findFirst()
                .orElse(new SysStock());
        stock.setItem(inboundRecord.getItem());
        stock.setDeptId(deptIds.get(0));       //TODO:此处强制取第一个元素，待优化
        stock.setCurrentQuantity(stock.getCurrentQuantity() == null ? inboundRecord.getQuantity() :
                stock.getCurrentQuantity() + inboundRecord.getQuantity());
        stock.setLastUpdated(LocalDateTime.now());
        stockRepository.save(stock);

        inboundRecord.setInboundTime(LocalDateTime.now());
        stockInboundRecordRepository.save(inboundRecord);

        logOperationLog(inboundRecord.getItem(), "inbound", inboundRecord.getQuantity(),
                inboundRecord.getOperator(), "入库");

        return ResponseEntity.ok("入库成功");
    }

    /**
     * 出库操作
     */
    @PostMapping("/outbound")
    @ApiOperation("新增出库记录")
    public ResponseEntity<String> addOutboundRecord(@RequestBody SysStockOutboundRecord outboundRecord) {
        UserDto user = userService.findByName(SecurityUtils.getCurrentUsername());
        List<Long> deptIds = dataService.getDeptIds(user);

        SysStock stock = stockRepository
                .findByItem_ItemIdAndDeptIdIn(outboundRecord.getItem().getItemId(), deptIds)
                .stream().findFirst()
                .orElseThrow(() -> new RuntimeException("库存不足或未找到相关库存"));

        if (stock.getCurrentQuantity() < outboundRecord.getQuantity()) {
            throw new RuntimeException("出库数量超出库存量");
        }
        stock.setCurrentQuantity(stock.getCurrentQuantity() - outboundRecord.getQuantity());
        stock.setLastUpdated(LocalDateTime.now());
        stockRepository.save(stock);

        outboundRecord.setOutboundTime(LocalDateTime.now());
        stockOutboundRecordRepository.save(outboundRecord);

        logOperationLog(outboundRecord.getItem(), "outbound", outboundRecord.getQuantity(),
                outboundRecord.getOperator(), "出库");

        return ResponseEntity.ok("出库成功");
    }

    /**
     * 查询操作日志
     */
    @GetMapping("/logs")
    @ApiOperation("查询操作日志")
    public ResponseEntity<List<SysStockOperationLog>> getOperationLogs(@RequestParam Long deptId,
                                                                       @RequestParam(required = false) LocalDateTime startTime,
                                                                       @RequestParam(required = false) LocalDateTime endTime) {
        List<SysStockOperationLog> logs;
        if (startTime != null && endTime != null) {
            logs = stockOperationLogRepository.findByDeptIdAndOperationTimeBetween(deptId, startTime, endTime);
        } else {
            logs = stockOperationLogRepository.findByDeptId(deptId);
        }
        return ResponseEntity.ok(logs);
    }

    /**
     * 日志记录工具方法
     */
    private void logOperationLog(SysStockItem item, String operationType, Integer quantity,
                                 String operator, String note) {
        SysStockOperationLog log = new SysStockOperationLog();
        log.setItem(item);
        log.setOperationType(SysStockOperationLog.OperationType.valueOf(operationType.toUpperCase()));
        log.setQuantity(quantity);
        log.setOperator(operator);
        log.setOperationTime(LocalDateTime.now());
        log.setNote(note);
        stockOperationLogRepository.save(log);
    }
}
