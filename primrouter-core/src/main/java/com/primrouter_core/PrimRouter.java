package com.primrouter_core;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;

import com.prim.router.primrouter_annotation.RouterMeta;
import com.primrouter_core.interfaces.IRouteGroup;
import com.primrouter_core.interfaces.IRouteRoot;
import com.primrouter_core.utils.Utils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author prim
 * @version 1.0.0
 * @desc router 的入口
 * @time 2018/7/10 - 下午6:43
 */
public class PrimRouter {

    private static PrimRouter primRouter;
    public static final String PAGENAME = "com.prim.router.generated";

    public static final String GROUP_CLASS_NAME = "PrimRouter$$Group$$";

    public static final String ROOT_CLASS_NAME = "PrimRouter$$Root$$";

    private static final String TAG = "PrimRouter";

    private Application application;

    private Handler mHandler;

    public static PrimRouter getInstance() {
        if (primRouter == null) {
            synchronized (PrimRouter.class) {
                if (primRouter == null) {
                    primRouter = new PrimRouter();
                }
            }
        }
        return primRouter;
    }

    public PrimRouter() {
        mHandler = new Handler();
    }

    /**
     * 初始化-收集路由表，必须在Application中初始化
     *
     * @param application
     */
    public void initRouter(Application application) {
        this.application = application;
        try {
            loadRouteTable();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 收集加载路由表
     */
    private void loadRouteTable() throws InterruptedException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (application == null) {
            throw new IllegalArgumentException("initRouter(Application application) must Application init");
        }
        //获取所有APT 生成的路由类的全类名
        Set<String> routerMap = Utils.getFileNameByPackageName(application, PAGENAME);
        for (String className : routerMap) {
            //获取root中注册的路由分组信息，存储到本地仓库中。
            if (className.startsWith(PAGENAME + "." + ROOT_CLASS_NAME)) {
                Object instance = Class.forName(className).getConstructor().newInstance();
                if (instance instanceof IRouteRoot) {
                    IRouteRoot routeRoot = (IRouteRoot) instance;
                    routeRoot.loadInto(Depository.rootMap);
                }
            }

            Log.e(TAG, "路由表分组信息 「");
            for (Map.Entry<String, Class<? extends IRouteGroup>> entry : Depository.rootMap.entrySet()) {
                Log.e(TAG, "【key --> " + entry.getKey() + ": value --> " + entry.getValue() + "]");
            }
            Log.e(TAG, " 」");
        }
    }

    public JumpCard jump(String path) {
        if (TextUtils.isEmpty(path)) {
            throw new RuntimeException("路由地址无效");
        } else {
            return new JumpCard(path, getGroupName(path));
        }
    }

    public JumpCard jump(String path, String group) {
        if (TextUtils.isEmpty(path) || TextUtils.isEmpty(group)) {
            throw new RuntimeException("路由地址无效");
        } else {
            return new JumpCard(path, group);
        }
    }

    public JumpCard jump(String path, String group, Bundle bundle) {
        if (TextUtils.isEmpty(path) || TextUtils.isEmpty(group)) {
            throw new RuntimeException("路由地址无效");
        } else {
            return new JumpCard(path, group, bundle);
        }
    }


    public Object navigation(Context context, final JumpCard jumpCard, final int requestCode, Object o1) {
        if (context == null) {
            return null;
        }
        produceJumpCard(jumpCard);
        switch (jumpCard.getType()) {
            case ACTIVITY:
                Log.e(TAG, "navigation: " + jumpCard.getDestination());
                if (context == null) {
                    context = this.application;
                }
                final Intent intent = new Intent(context, jumpCard.getDestination());
                intent.putExtras(jumpCard.getExtras());
                if (jumpCard.getFlags() != -1) {
                    intent.setFlags(jumpCard.getFlags());
                } else if (!(context instanceof Activity)) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                final Context finalContext = context;
                mHandler.post(new Runnable() {//在主线程中跳转
                    @Override
                    public void run() {
                        //可能需要返回码
                        if (requestCode > 0) {
                            ActivityCompat.startActivityForResult((Activity) finalContext, intent,
                                    requestCode, jumpCard.getOptionsBundle());
                        } else {
                            Log.e(TAG, "run: " + jumpCard.getDestination());
                            ActivityCompat.startActivity(finalContext, intent, jumpCard
                                    .getOptionsBundle());
                        }

                        if ((0 != jumpCard.getEnterAnim() || 0 != jumpCard.getExitAnim()) &&
                                finalContext instanceof Activity) {
                            //老版本
                            ((Activity) finalContext).overridePendingTransition(jumpCard
                                            .getEnterAnim()
                                    , jumpCard.getExitAnim());
                        }
                    }
                });
                break;
        }
        return null;
    }

    /**
     * 准备跳卡
     */
    private void produceJumpCard(JumpCard card) {
        //获取仓库中存储的 具体每个组的信息
        RouterMeta routerMeta = Depository.groupMap.get(card.getPath());
        if (routerMeta == null) {//没有记录在仓库中，从路由表的分组信息中查找
            Class<? extends IRouteGroup> groupClass = Depository.rootMap.get(card.getGroup());
            if (groupClass == null) {
                throw new RuntimeException("没有找到对应的路由表信息：" + card.getGroup() + ":" + card.getPath());
            }
            IRouteGroup routeGroup = null;
            try {
                routeGroup = groupClass.getConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("路由映射表信息记录失败");
            }
            routeGroup.loadInto(Depository.groupMap);
            produceJumpCard(card);//再次进入
        } else {
            //设置要跳转的类
            card.setDestination(routerMeta.getDestination());
            //设置要跳转的类型
            card.setType(routerMeta.getType());
        }

    }

    /**
     * 通过路由地址获取分组名
     *
     * @param path
     * @return
     */
    private String getGroupName(String path) {
        if (!path.startsWith("/")) {
            throw new RuntimeException(path + ": 不能有效的提取group");
        }
        try {
            String group = path.substring(1, path.indexOf("/", 1));
            if (TextUtils.isEmpty(group)) {
                throw new RuntimeException("不能有效的提取group");
            }
            return group;
        } catch (Exception e) {
            return null;
        }
    }
}
