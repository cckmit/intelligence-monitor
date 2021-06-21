package com.zhikuntech.intellimonitor.windpowerforecast.domain.controller.assessresult;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 考核结果
 *
 * @author liukai
 */
@Api(tags = "考核结果")
@RestController
@RequestMapping("/assess-result")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AssessResultController {


    // TODO 月考核结果 列表模式

    // TODO 日考核结果 列表模式

    // TODO 曲线模式（日 + 月）

    // TODO 修改数据


}
