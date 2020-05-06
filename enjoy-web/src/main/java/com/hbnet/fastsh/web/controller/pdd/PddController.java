package com.hbnet.fastsh.web.controller.pdd;

import com.hbnet.fastsh.utils.SessionUtil;
import com.hbnet.fastsh.web.service.impl.PddStoreService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ClassName: PddController
 * @Auther: zoulr@qq.com
 * @Date: 2019/9/26 18:18
 */
public class PddController {
    @Autowired
    private PddStoreService pddStoreService;

    /**
     * 检查是否有权限操作该店铺。管理员或店铺用户可操作
     * @param storeId
     * @return
     */
    protected boolean checkStoreId(String storeId) {
        if (SessionUtil.isAdmin()) {
            return true;
        }

        if (StringUtils.isBlank(storeId)) {
            return false;
        }

        return pddStoreService.selectByUserIdAndStoreId(SessionUtil.getCurUser().getId(), storeId) != null;
    }
}
