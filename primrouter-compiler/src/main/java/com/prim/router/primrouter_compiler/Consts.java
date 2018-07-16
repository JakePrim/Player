package com.prim.router.primrouter_compiler;

import com.squareup.javapoet.ClassName;

public class Consts {
    public static final String EXTRA_NAME = "com.prim.router.primrouter_annotation.Extra";

    public static final String ARGUMENTS_NAME = "moduleName";

    public static final String Activity = "android.app.Activity";

    public static final String Fragment = "android.app.Fragment";

    public static final String V4Fragment = "android.support.v4.app.Fragment";

    public static final String Service = "com.primrouter_core.interfaces.IService";

    public static final String ROUTEGROUP = "com.primrouter_core.interfaces.IRouteGroup";

    public static final String ROUTEROOT = "com.primrouter_core.interfaces.IRouteRoot";

    public static final String Extra = "com.primrouter_core.interfaces.IExtra";

    public static final String PAGENAME = "com.prim.router.generated";

    public static final String GROUP_CLASS_NAME = "PrimRouter$$Group$$";

    public static final String ROOT_CLASS_NAME = "PrimRouter$$Root$$";

    public static final String GROUP_PARAM_NAME = "atlas";

    public static final String ROOT_PARAM_NAME = "routers";

    public static final String GROUP_METHOD_NAME = "loadInto";

    public static final String ROOT_METHOD_NAME = "loadInto";

    public static final String EXTRA_METHOD_NAME = "loadExtra";


    private static final String LANG = "java.lang";
    public static final String BYTE = LANG + ".Byte";
    public static final String SHORT = LANG + ".Short";
    public static final String INTEGER = LANG + ".Integer";
    public static final String LONG = LANG + ".Long";
    public static final String FLOAT = LANG + ".Float";
    public static final String DOUBEL = LANG + ".Double";
    public static final String BOOLEAN = LANG + ".Boolean";
    public static final String STRING = LANG + ".String";
    public static final String ARRAY = "ARRAY";

    public static final String BYTEARRAY = "byte[]";
    public static final String SHORTARRAY = "short[]";
    public static final String BOOLEANARRAY = "boolean[]";
    public static final String CHARARRAY = "char[]";
    public static final String DOUBLEARRAY = "double[]";
    public static final String FLOATARRAY = "float[]";
    public static final String INTARRAY = "int[]";
    public static final String LONGARRAY = "long[]";
    public static final String STRINGARRAY = "java.lang.String[]";

    public static final String ARRAYLIST = "java.util.ArrayList";
    public static final String LIST = "java.util.List";


    public static final String PARCELABLE = "android.os.Parcelable";


    public static final ClassName ROUTER = ClassName.get("com.primrouter_core.core", "PrimRouter");
}
