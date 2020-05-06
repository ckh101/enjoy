package com.hbnet.fastsh.web.controller.pdd;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hbnet.fastsh.web.entity.PddFundStatement;
import com.hbnet.fastsh.web.entity.PddUserStore;
import com.hbnet.fastsh.web.entity.User;
import com.hbnet.fastsh.web.exception.ServiceException;
import com.hbnet.fastsh.utils.ApiResponse;
import com.hbnet.fastsh.utils.Num62;
import com.hbnet.fastsh.utils.SessionUtil;
import com.hbnet.fastsh.web.entity.PddStore;
import com.hbnet.fastsh.web.service.impl.PddFundStatementService;
import com.hbnet.fastsh.web.service.impl.PddStoreService;
import com.hbnet.fastsh.web.service.impl.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName: PingduoduoController
 * @Auther: zoulr@qq.com
 * @Date: 2019/9/24 10:35
 * @Description: 拼多多店铺相关
 */
@RequestMapping("admin/pdd/store")
@Controller
@Slf4j
public class StoreController extends PddController {
    @Autowired
    private PddStoreService pddStoreService;

    @Autowired
    private UserService userService;

    @Autowired
    private PddFundStatementService pddFundStatementService;

    /**
     * 查看我的店铺列表
     * @return
     */
    @RequestMapping("/my")
    public ModelAndView my() {
        ModelAndView view = new ModelAndView("/pdd/myStoreList");
        List<PddStore> storeList = pddStoreService.selectByUserId(SessionUtil.getCurUser().getId());
        view.addObject("list", storeList);

        return view;
    }


    /**
     * 查看店铺分页列表
     * @return
     */
    @RequestMapping("/list")
    public ModelAndView list(@RequestParam(defaultValue = "1") int pageNumber, @RequestParam(defaultValue = "20") int pageSize,
                             @RequestParam Map<String, String> params) {
        pageSize = Math.max(5, Math.min(pageSize, 100)); // 5-100条
        pageNumber = Math.max(1, pageNumber); // 页码最小为1

        ModelAndView view = new ModelAndView("/pdd/storeList");
        Page<PddStore> page = pddStoreService.page(pddStoreService.getSearchFilter(params), pageNumber, pageSize);

        view.addObject("page", page);

        return view;
    }

    /**
     * 获取店铺详情
     * @return
     */
    @RequiresPermissions("/admin/pdd/store/update")
    @GetMapping("/detail")
    @ResponseBody
    public ApiResponse detail(String storeId) {
        PddStore pddStore = pddStoreService.selectByStoreId(storeId);
        if (pddStore == null) {
            return ApiResponse.error("找不到指定店铺数据！");
        }

        return ApiResponse.ok(pddStore);
    }

    /**
     * 添加店铺
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public ApiResponse add(PddStore param) {
        param.setId(null);
        param.setStoreId(Num62.longToN62(System.currentTimeMillis()));
        param.setBalance(0L);
        param.setBlockedBalance(0L);
        param.setCreateTime(new Date());
        param.setUpdateTime(null);

        pddStoreService.save(param);

        return ApiResponse.ok();
    }

    /**
     * 修改店铺
     * @return
     */
    @PostMapping("/update")
    @ResponseBody
    public ApiResponse update(PddStore param) {
        PddStore db = pddStoreService.selectByStoreId(param.getStoreId());
        if (db == null) {
            return ApiResponse.error("找不到指定店铺信息！");
        }

        db.setName(param.getName());
        db.setDetail(param.getDetail());
        db.setUpdateTime(new Date());

        pddStoreService.save(db);

        return ApiResponse.ok();
    }

    /**
     * 指派店铺给用户
     * @return
     */
    @PostMapping("/authorize")
    @ResponseBody
    public ApiResponse assign(String storeId, String userIds) {
        PddStore store = pddStoreService.selectByStoreId(storeId);
        if (store == null) {
            return ApiResponse.error("店铺不存在！");
        }

        String[] userIdArr = StringUtils.split(userIds, ",");
        if (userIdArr.length > 20) {
            return ApiResponse.error("一个店铺最多授权20个用户！");
        }

        List<Long> userIdList = Arrays.asList(userIdArr).stream().map(str->Long.parseLong(str)).collect(Collectors.toList());

        try {
            pddStoreService.assignStore(store, new HashSet<>(userIdList));
        } catch (Exception ex) {
            if (ex instanceof ServiceException) {
                return ApiResponse.error(ex.getMessage());
            } else {
                log.error("指派店铺出错!storeId={},userIds={}", storeId, userIds, ex);
                return ApiResponse.error("指派店铺时出错！");
            }
        }

        return ApiResponse.ok();
    }

    /**
     * 获取授权信息列表
     * @return
     */
    @RequiresPermissions("/admin/pdd/store/authorize")
    @GetMapping("/getAuthorizedUsers")
    @ResponseBody
    public ApiResponse getAuthorizedUsers(String storeId) {
        PddStore store = pddStoreService.selectByStoreId(storeId);
        if (store == null) {
            return ApiResponse.error("找不到指定店铺信息！");
        }

        try {
            List<PddUserStore> list = pddStoreService.listUserStoreByStoreId(store);
            List<Long> userList = list.stream().map(a -> a.getUserId()).collect(Collectors.toList());
            List<User> allUser = userService.findAll();

            JSONArray array = new JSONArray();
            for (User user : allUser) {
                JSONObject json = new JSONObject();
                json.put("id", user.getId());
                json.put("text", user.getUserName());
                json.put("selected", userList.contains(user.getId()));

                array.add(json);
            }

            return ApiResponse.ok(array);
        } catch (Exception ex) {
            if (ex instanceof ServiceException) {
                return ApiResponse.error(ex.getMessage());
            } else {
                log.error("获取店铺授权用户出错!storeId={}", storeId, ex);
                return ApiResponse.error("获取店铺授权用户时出错！");
            }
        }
    }


    /**
     * 充值
     * <br/>只分配给管理员操作
     * @param storeId
     * @return
     */
    @PostMapping("/deposit")
    @ResponseBody
    public ApiResponse deposit(String storeId, long amount, @RequestParam(required = false) String remark) {
        PddStore store = pddStoreService.selectByStoreId(storeId);
        if (store == null) {
            return ApiResponse.error("找不到指定店铺信息！");
        }

        if (StringUtils.length(remark) > 100) {
            return ApiResponse.error("备注最多100个字！");
        }

        pddStoreService.deposit(store, amount, remark, SessionUtil.getCurUser());

        return ApiResponse.ok();
    }


    /**
     * 查看资金流水分页列表
     * @return
     */
    @RequestMapping("/fundStatement/list")
    public ModelAndView fundStatementList(String storeId, @RequestParam(defaultValue = "1") int pageNumber, @RequestParam(defaultValue = "20") int pageSize,
                             @RequestParam Map<String, String> params) {
        pageSize = Math.max(5, Math.min(pageSize, 100)); // 5-100条
        pageNumber = Math.max(1, pageNumber); // 页码最小为1

        ModelAndView view = new ModelAndView("/pdd/fundStatementList");

        Page<PddFundStatement> page;
        if (!checkStoreId(storeId)) { // 非管理员只能看指定店铺
            page = new PageImpl(Collections.emptyList(), PageRequest.of(pageNumber - 1, pageSize), 0);
        } else {
            if (StringUtils.isNotBlank(storeId)) {
                params.put("search_EQ_storeId", storeId);
                view.addObject("store", pddStoreService.selectByStoreId(storeId));
            }

            page = pddFundStatementService.page(pddFundStatementService.getSearchFilter(params), pageNumber, pageSize);
        }

        view.addObject("page", page);

        return view;
    }
}
