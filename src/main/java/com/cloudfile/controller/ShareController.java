package com.cloudfile.controller;

import com.cloudfile.annotation.GlobalInterceptor;
import com.cloudfile.annotation.VerifyParam;
import com.cloudfile.entity.dto.SessionWebUserDto;
import com.cloudfile.entity.po.FileShare;
import com.cloudfile.entity.query.FileShareQuery;
import com.cloudfile.entity.vo.PaginationResultVO;
import com.cloudfile.entity.vo.ResponseVO;
import com.cloudfile.service.FileShareService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;

/**
 * 分享
 */
@RestController("shareController")
@RequestMapping("/share")
public class ShareController extends ABaseController {
    @Resource
    private FileShareService fileShareService;

    /**
     * 根据条件获取分享文件列表
     * @param session
     * @param query
     * @return
     */
    @RequestMapping("/loadShareList")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO loadShareList(HttpSession session, FileShareQuery query) {
        query.setOrderBy("share_time desc");
        SessionWebUserDto userDto = getUserInfoFromSession(session);
        query.setUserId(userDto.getUserId());
        query.setQueryFileName(true);
        PaginationResultVO resultVO = this.fileShareService.findListByPage(query);
        return getSuccessResponseVO(resultVO);
    }

    /**
     * 分享文件
     * @param session
     * @param fileId
     * @param validType 有效期类型 0:1天 1:7天 2:30天 3:永久有效
     * @param code 提取码
     * @return
     */
    @RequestMapping("/shareFile")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO shareFile(HttpSession session,
                                @VerifyParam(required = true) String fileId,
                                @VerifyParam(required = true) Integer validType,
                                String code) {
        SessionWebUserDto userDto = getUserInfoFromSession(session);
        FileShare share = new FileShare();
        share.setFileId(fileId);
        share.setValidType(validType);
        share.setCode(code);
        share.setUserId(userDto.getUserId());
        fileShareService.saveShare(share);
        return getSuccessResponseVO(share);
    }

    /**
     * 取消分享
     * @param session
     * @param shareIds
     * @return
     */
    @RequestMapping("/cancelShare")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO cancelShare(HttpSession session, @VerifyParam(required = true) String shareIds) {
        SessionWebUserDto userDto = getUserInfoFromSession(session);
        fileShareService.deleteFileShareBatch(shareIds.split(","), userDto.getUserId());
        return getSuccessResponseVO(null);
    }
}
