package com.primrouter_core;

import com.prim.router.primrouter_annotation.RouterMeta;
import com.primrouter_core.interfaces.IRouteGroup;

import java.util.HashMap;
import java.util.Map;

/**
 * @author prim
 * @version 1.0.0
 * @desc 存储路由表的本地仓库
 * @time 2018/7/10 - 下午10:55
 */
public class Depository {

    /**
     * 存储路由表的分组信息
     */
    public static final Map<String, Class<? extends IRouteGroup>> rootMap = new HashMap<>(10);

    /**
     * 存储路由表的具体组的信息
     */
    public static final Map<String, RouterMeta> groupMap = new HashMap<>(20);

}
