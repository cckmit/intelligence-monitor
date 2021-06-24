package com.zhikuntech.intellimonitor.demo.domain.controller;

import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.core.commons.dto.AuthDTO;
import com.zhikuntech.intellimonitor.core.commons.dto.AuthHeaderDTO;
import com.zhikuntech.intellimonitor.core.commons.dto.HeaderParamDTO;
import com.zhikuntech.intellimonitor.core.commons.dto.UpdateAuthDTO;
import com.zhikuntech.intellimonitor.core.commons.utils.AuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @Author 杨锦程
 * @Date 2021/6/22 14:47
 * @Description 权限测试
 * @Version 1.0
 */
@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    @Autowired
    private AuthUtil authUtil;

    /**
     * 新增权限
     *
     * @param code
     * @return
     */
    @PostMapping("/createAuth/{code}")
    public BaseResponse createAuth(@PathVariable("code") String code,
                                   @RequestBody AuthHeaderDTO authHeaderDTO) {
        return authUtil.createAuth(code, authHeaderDTO.getHeaderParamDTO(),authHeaderDTO.getAuthDTO());
    }

    private AuthDTO mockAddAuthDTO() {
        AuthDTO authDTO = new AuthDTO();
        authDTO.setAuthCode("examSystem");
        authDTO.setAuthName("考试系统");
        authDTO.setDescription("考试系统Demo");
        authDTO.setSubType(0);

        AuthDTO authDTO1 = new AuthDTO();
        authDTO1.setAuthCode("exams");
        authDTO1.setAuthName("考试列表");
        authDTO1.setDescription("考试列表");
        authDTO1.setSubType(1);

        AuthDTO authDTO11 = new AuthDTO();
        authDTO11.setAuthCode("view");
        authDTO11.setAuthName("查看");
        authDTO11.setDescription("查看考试按钮");
        authDTO11.setSubType(2);

        AuthDTO authDTO12 = new AuthDTO();
        authDTO12.setAuthCode("resit");
        authDTO12.setAuthName("重考");
        authDTO12.setDescription("重考按钮，最多只能考3次（含）");
        authDTO12.setSubType(2);

        AuthDTO[] authDTOList1 = new AuthDTO[2];
        authDTOList1[0] = authDTO11;
        authDTOList1[1] = authDTO12;
        authDTO1.setChildren(authDTOList1);

        AuthDTO authDTO2 = new AuthDTO();
        authDTO2.setAuthCode("questions");
        authDTO2.setAuthName("考题列表");
        authDTO2.setDescription("考题列表");
        authDTO2.setSubType(1);

        AuthDTO[] authDTOList2 = new AuthDTO[2];
        authDTOList2[0] = authDTO1;
        authDTOList2[1] = authDTO2;

        authDTO.setChildren(authDTOList2);
        System.out.println(authDTO);
        return authDTO;
    }

    private AuthDTO mockUpdateAuthDTO() {
        AuthDTO authDTO = new AuthDTO();
        //修改权限时authCode要和待修改的权限authCode相同
        authDTO.setAuthCode("examSystem");
        authDTO.setAuthName("考试系统1");
        authDTO.setDescription("考试系统Demo1");
        authDTO.setSubType(0);

        AuthDTO authDTO1 = new AuthDTO();
        authDTO1.setAuthCode("exams");
        authDTO1.setAuthName("考试列表1");
        authDTO1.setDescription("考试列表1");
        authDTO1.setSubType(1);

        AuthDTO authDTO11 = new AuthDTO();
        authDTO11.setAuthCode("view");
        authDTO11.setAuthName("查看1");
        authDTO11.setDescription("查看考试按钮1");
        authDTO11.setSubType(2);

        AuthDTO authDTO12 = new AuthDTO();
        authDTO12.setAuthCode("resit");
        authDTO12.setAuthName("重考1");
        authDTO12.setDescription("重考按钮，最多只能考3次（含）1");
        authDTO12.setSubType(2);

        AuthDTO[] authDTOList1 = new AuthDTO[2];
        authDTOList1[0] = authDTO11;
        authDTOList1[1] = authDTO12;
        authDTO1.setChildren(authDTOList1);

        AuthDTO authDTO2 = new AuthDTO();
        authDTO2.setAuthCode("questions");
        authDTO2.setAuthName("考题列表1");
        authDTO2.setDescription("考题列表1");
        authDTO2.setSubType(1);

        AuthDTO[] authDTOList2 = new AuthDTO[2];
        authDTOList2[0] = authDTO1;
        authDTOList2[1] = authDTO2;

        authDTO.setChildren(authDTOList2);
        System.out.println(authDTO);
        return authDTO;
    }

    /**
     * 删除权限
     *
     * @param code
     * @return
     */
    @DeleteMapping("/deleteAuth/{code}")
    public BaseResponse deleteAuth(@PathVariable("code") String code,@RequestBody HeaderParamDTO headerParamDTO) {
        return authUtil.deleteAuth(code,headerParamDTO);
    }

    /**
     * 查询权限
     *
     * @param code
     * @return
     */
    @GetMapping("/selectAuth/{code}")
    public BaseResponse selectAuth(@PathVariable("code") String code,@RequestBody HeaderParamDTO headerParamDTO) {
        BaseResponse baseResponse = authUtil.selectAuth(code,headerParamDTO);
        Object data = baseResponse.getData();
        if (!ObjectUtils.isEmpty(data)) {
            AuthDTO authDTO = (AuthDTO) data;
            log.info("controller selectAuth authDTO->{}", authDTO);
        }
        return baseResponse;
    }

    /**
     * 修改权限
     *
     * @param code
     * @return
     */
    @PutMapping("/updateAuth/{code}")
    public BaseResponse updateAuth(@PathVariable("code") String code,
                                   @RequestBody AuthHeaderDTO authHeaderDTO) {
        BaseResponse baseResponse = authUtil.updateAuth(code,authHeaderDTO.getHeaderParamDTO(), authHeaderDTO.getAuthDTO());
        Object data = baseResponse.getData();
        UpdateAuthDTO updateAuthDTO = (UpdateAuthDTO) data;
        log.info("转换成UpdateAuthDTO实例->{}", updateAuthDTO);
        return baseResponse;
    }

    /**
     * 回滚权限
     *
     * @param rollbackId
     * @return
     */
    @PutMapping("/rollbacksAuth/{rollbackId}")
    public BaseResponse rollbacksAuth(@PathVariable("rollbackId") String rollbackId,@RequestBody HeaderParamDTO headerParamDTO) {
        return authUtil.rollbacksAuth(rollbackId,headerParamDTO);
    }

}
