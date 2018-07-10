package com.prim.router.primrouter_compiler.processor;

import com.google.auto.service.AutoService;
import com.prim.router.primrouter_compiler.Consts;
import com.prim.router.primrouter_compiler.Log;
import com.prim.router.primrouter_compiler.Utils;
import com.prim.router.primrouter_annotation.Router;
import com.prim.router.primrouter_annotation.RouterMeta;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;


import static javax.lang.model.SourceVersion.RELEASE_7;
import static javax.lang.model.element.Modifier.PUBLIC;

//AnnotationProcessor register
@AutoService(Processor.class)

/**
 * compiled java version {@link AbstractProcessor#getSupportedSourceVersion()}
 */
@SupportedSourceVersion(RELEASE_7)

/**
 * Allow AnnotationProcessor process annotation，{@link AbstractProcessor#getSupportedAnnotationTypes()}
 */
@SupportedAnnotationTypes({"com.prim.router.primrouter_annotation.Router"})

/**
 * 注解处理器接收的参数 {@link AbstractProcessor#getSupportedOptions()}
 */
@SupportedOptions({Consts.ARGUMENTS_NAME})

/**
 * Router 注解处理器
 */
public class RouterProcessor extends AbstractProcessor {

    private Messager messager;

    /**
     * 文件生成器 类/资源
     */
    private Filer filer;

    /**
     *
     */
    private Locale locale;

    /**
     * 获取传递的参数
     */
    private Map<String, String> options;

    /**
     * compiled java version
     */
    private SourceVersion sourceVersion;

    /**
     * 类型工具类
     */
    private Types typeUtils;

    /**
     * 节点工具类
     */
    private Elements elementUtils;

    /**
     * 模块的名称
     */
    private String moduleName;

    /**
     * key 组名  value 类名
     */
    private Map<String, String> rootMap = new TreeMap<>();

    /**
     * 分组 key 组名  value 对应组的路由信息
     */
    private Map<String, List<RouterMeta>> groupMap = new HashMap<>();

    private Log log;

    /**
     * init process environment utils
     *
     * @param processingEnvironment
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        messager = processingEnvironment.getMessager();
        log = Log.newLog(messager);
        filer = processingEnvironment.getFiler();
        locale = processingEnvironment.getLocale();
        options = processingEnvironment.getOptions();
        sourceVersion = processingEnvironment.getSourceVersion();
        typeUtils = processingEnvironment.getTypeUtils();
        elementUtils = processingEnvironment.getElementUtils();

        // 获取传递的参数
        if (!options.isEmpty()) {
            moduleName = options.get(Consts.ARGUMENTS_NAME);
            log.i("module:" + moduleName);
        }

        if (Utils.isEmpty(moduleName)) {
            throw new NullPointerException("Not set Processor Parmaters.");
        }

    }

    /**
     * process annotation
     *
     * @param set              The set of nodes that support processing annotations
     * @param roundEnvironment Current or previous operating environment，annotation that can be found by this object
     * @return true already processed ，follow-up will not be dealt with
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (!set.isEmpty()) {
            //Set nodes annotated by Router
            Set<? extends Element> annotatedWith = roundEnvironment.getElementsAnnotatedWith(Router.class);
            if (annotatedWith != null) {
                processRouter(annotatedWith);
            }
            return true;
        }
        return false;
    }

    /**
     * 处理被注解的节点集合
     *
     * @param annotatedWith
     */
    private void processRouter(Set<? extends Element> annotatedWith) {
        RouterMeta routerMeta = null;
        //获得Activity类的节点信息
        TypeElement activity = elementUtils.getTypeElement(Consts.Activity);
        //单个的节点
        for (Element element : annotatedWith) {
            // 获取类信息 如Activity类
            TypeMirror typeMirror = element.asType();
            // 获取节点的注解信息
            Router annotation = element.getAnnotation(Router.class);
            //只能指定的类上面使用
            if (typeUtils.isSubtype(typeMirror, activity.asType())) {
                //存储路由相关的信息
                routerMeta = new RouterMeta(RouterMeta.Type.ACTIVITY, annotation, element);
            } else if (typeUtils.isSubtype(typeMirror, activity.asType())) {

            } else {
                throw new RuntimeException("Just Support Activity Router!");
            }

            //检查是否配置group如果没有配置 则从path中截取组名
            checkRouterGroup(routerMeta);
        }

        //获取 primrouter-core IRouterGroup 类节点
        TypeElement routeGroupElement = elementUtils.getTypeElement(Consts.ROUTEGROUP);
        //获取 primrouter-core IRouterRoot 类节点
        TypeElement routeRootElement = elementUtils.getTypeElement(Consts.ROUTEROOT);

        //生成 $$Group$$ 记录分组表
        generatedGroupTable(routeGroupElement);

        //生成 $$Root$$ 记录路由表
        generatedRootTable(routeRootElement, routeGroupElement);
    }

    /**
     * 生成路由表class 类
     *
     * @param routeRootElement
     */
    private void generatedRootTable(TypeElement routeRootElement, TypeElement routeGroupElement) {
        ////类型 Map<String,Class<? extends IRouteGroup>> routes>
        ParameterizedTypeName atlas = ParameterizedTypeName.get(ClassName.get(Map.class),
                ClassName.get(String.class),
                ParameterizedTypeName.get(
                        ClassName.get(Class.class),
                        WildcardTypeName.subtypeOf(ClassName.get(routeGroupElement))));

        //创建参数  Map<String,Class<? extends IRouteGroup>>> routes
        ParameterSpec altlas = ParameterSpec
                .builder(atlas, Consts.ROOT_PARAM_NAME)//参数名
                .build();//创建参数

        //public void loadInfo(Map<String,Class<? extends IRouteGroup>> routes> routes)
        //函数 public void loadInfo(Map<String,Class<? extends IRouteGroup>> routes> routes)
        MethodSpec.Builder loadIntoMethodOfRootBuilder = MethodSpec.methodBuilder
                (Consts.ROOT_METHOD_NAME)
                .addAnnotation(Override.class)
                .addModifiers(PUBLIC)
                .addParameter(altlas);

        //函数体
        for (Map.Entry<String, String> entry : rootMap.entrySet()) {
            loadIntoMethodOfRootBuilder.addStatement(Consts.ROOT_PARAM_NAME + ".put($S, $T.class)", entry
                    .getKey(), ClassName.get(Consts.PAGENAME, entry.getValue
                    ()));
        }
        //生成 $Root$类
        String rootClassName = Consts.ROOT_CLASS_NAME + moduleName;
        try {
            JavaFile.builder(Consts.PAGENAME,
                    TypeSpec.classBuilder(rootClassName)
                            .addSuperinterface(ClassName.get(routeRootElement))
                            .addModifiers(PUBLIC)
                            .addMethod(loadIntoMethodOfRootBuilder.build())
                            .build()
            ).build().writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 生成分组表class 类
     *
     * @param routeGroupElement primrouter-core IRouterGroup 类节点
     */
    private void generatedGroupTable(TypeElement routeGroupElement) {
        //创建参数类型 Map<String,RouterMeta>
        ParameterizedTypeName atlas = ParameterizedTypeName.get(ClassName.get(Map.class),
                ClassName.get(String.class),
                ClassName.get(RouterMeta.class));

        //创建参数 Map<String,RouterMeta> atlas
        ParameterSpec altlas = ParameterSpec
                .builder(atlas, Consts.GROUP_PARAM_NAME)//参数名
                .build();//创建参数

        //遍历分组 每一个分组 创建一个 $$Group$$类
        for (Map.Entry<String, List<RouterMeta>> entry : groupMap.entrySet()) {
            MethodSpec.Builder builder = MethodSpec.methodBuilder(Consts.GROUP_METHOD_NAME)//函数名
                    .addModifiers(PUBLIC)//作用域
                    .addAnnotation(Override.class)//添加一个注解
                    .addParameter(altlas);//添加参数

            //Group组中
            List<RouterMeta> groupData = entry.getValue();

            //遍历 生成函数体
            for (RouterMeta meta : groupData) {
                //$S = String
                //$T = class
                //添加函数体
                builder.addStatement(Consts.GROUP_PARAM_NAME+".put($S,$T.build($T.$L,$T.class,$S,$S))",
                        meta.getPath(),
                        ClassName.get(RouterMeta.class),
                        ClassName.get(RouterMeta.Type.class),
                        meta.getType(),
                        ClassName.get((TypeElement) meta.getElement()),
                        meta.getPath(),
                        meta.getGroup());
            }

            MethodSpec loadInto = builder.build();//函数创建完成loadInto();

            String groupClassName = Consts.GROUP_CLASS_NAME + entry.getKey();

            TypeSpec typeSpec = TypeSpec.classBuilder(groupClassName)//类名
                    .addSuperinterface(ClassName.get(routeGroupElement))//实现接口IRouteGroup
                    .addModifiers(PUBLIC)//作用域
                    .addMethod(loadInto)//添加方法
                    .build();//类创建完成

            //生成Java文件
            JavaFile javaFile = JavaFile
                    .builder(Consts.PAGENAME, typeSpec)//包名和类
                    .build();
            try {
                javaFile.writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            rootMap.put(entry.getKey(), groupClassName);
        }

    }

    /**
     * 检查设置路由组
     *
     * @param routerMeta
     */
    private void checkRouterGroup(RouterMeta routerMeta) {
        if (routerVerify(routerMeta)) {
            List<RouterMeta> routerMetas = groupMap.get(routerMeta.getGroup());
            if (Utils.isEmpty(routerMetas)) {
                routerMetas = new ArrayList<>();
                routerMetas.add(routerMeta);
                groupMap.put(routerMeta.getGroup(), routerMetas);
            } else {
                routerMetas.add(routerMeta);
            }
        } else {
            log.i("router path no verify,please check");
        }
    }

    /**
     * 验证路由地址配置是否正确合法性
     *
     * @param routerMeta 存储的路由bean对象
     * @return true 路由地址配置正确  false 路由地址配置不正确
     */
    private boolean routerVerify(RouterMeta routerMeta) {
        String path = routerMeta.getPath();
        if (Utils.isEmpty(path)) {
            throw new NullPointerException("@Router path not to be null or to length() == 0");
        }
        if (!path.startsWith("/")) {//路由地址必须以/开头
            throw new IllegalArgumentException("@Router path must / first");
        }
        String group = routerMeta.getGroup();
        if (Utils.isEmpty(group)) {
            String defaultGroup = path.substring(1, path.indexOf("/", 1));
            //截取的还是为空
            if (Utils.isEmpty(defaultGroup)) {
                return false;
            }
            //设置group
            routerMeta.setGroup(defaultGroup);
        }
        return true;
    }
}
