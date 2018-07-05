package com.primrouter_core.interfaces;

import com.prim.router.primrouter_annotation.RouterMeta;

import java.util.Map;

/**
 *一组路由表的接口
 */
public interface IRouteGroup {
    void loadInto(Map<String, RouterMeta> routerMetaMap);
}
