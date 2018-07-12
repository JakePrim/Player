package com.prim.router.primrouter_annotation;

import javax.lang.model.element.Element;

/**
 * 存储的路由对象
 */
public class RouterMeta {

    /**
     * 路由的类型枚举
     */
    public enum Type {
        ACTIVITY, SERVICE
    }

    /**
     * 路由的类型
     */
    private Type type;

    /**
     * 节点 activity
     */
    private Element element;

    /**
     * 注解使用的类对象
     */
    private Class<?> destination;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 路由组
     */
    private String group;

    public static RouterMeta build(Type type, Class<?> destination, String path, String
            group) {
        return new RouterMeta(type, null, destination, path, group);
    }


    public RouterMeta() {
    }

    public RouterMeta(Type type, Router router, Element element) {
        this(type, element, null, router.path(), router.group());
    }

    public RouterMeta(Type type, Element element, Class<?> destination, String path, String group) {
        this.type = type;
        this.destination = destination;
        this.element = element;
        this.path = path;
        this.group = group;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public void setDestination(Class<?> destination) {
        this.destination = destination;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Type getType() {
        return type;
    }

    public Element getElement() {
        return element;
    }

    public Class<?> getDestination() {
        return destination;
    }

    public String getPath() {
        return path;
    }

    public String getGroup() {
        return group;
    }
}
