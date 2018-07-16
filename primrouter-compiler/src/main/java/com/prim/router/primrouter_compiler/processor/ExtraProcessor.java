package com.prim.router.primrouter_compiler.processor;

import com.google.auto.service.AutoService;
import com.prim.router.primrouter_annotation.Extra;
import com.prim.router.primrouter_compiler.Consts;
import com.prim.router.primrouter_compiler.Log;
import com.prim.router.primrouter_compiler.Utils;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * @author prim
 * @version 1.0.0
 * @desc
 * @time 2018/7/15 - 下午7:41
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes({Consts.EXTRA_NAME})
@SupportedOptions({Consts.ARGUMENTS_NAME})
public class ExtraProcessor extends AbstractProcessor {

    /**
     * key:类节点 value:需要注入的属性节点集合
     */
    private Map<TypeElement, List<Element>> extraMap = new HashMap<>();

    private Elements elementUtils;

    private Types typeUtils;

    private Filer filer;

    private Log log;

    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtils = processingEnvironment.getElementUtils();
        typeUtils = processingEnvironment.getTypeUtils();
        filer = processingEnvironment.getFiler();
        messager = processingEnvironment.getMessager();
        log = Log.newLog(messager);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (null != set && !set.isEmpty()) {//注意这里必须要判断set.isEmpty()  否则会走两遍
            log.i("process");
            //拿到注有Extra 的节点集合
            Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(Extra.class);
            if (null != elementsAnnotatedWith) {
                try {
                    saveExtras(elementsAnnotatedWith);
                    generateExtras();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 保存节点的信息
     *
     * @param elementsAnnotatedWith
     */
    private void saveExtras(Set<? extends Element> elementsAnnotatedWith) {
        for (Element element : elementsAnnotatedWith) {
            //获取注解父类的节点类型
            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
            if (extraMap.containsKey(enclosingElement)) {
                extraMap.get(enclosingElement).add(element);
            } else {
                List<Element> list = new ArrayList<>();
                list.add(element);
                extraMap.put(enclosingElement, list);
            }
        }
    }

    private MethodSpec.Builder builder;

    private TypeMirror parcelableType;

    private TypeMirror iServiceType;

    private TypeElement extraElement;

    private TypeMirror activity;

    /**
     * 生成表
     */
    private void generateExtras() {
        //支持Activity和Fragment
        activity = elementUtils.getTypeElement(Consts.Activity).asType();

        extraElement = elementUtils.getTypeElement(Consts.Extra);

        parcelableType = elementUtils.getTypeElement(Consts.PARCELABLE).asType();

        iServiceType = elementUtils.getTypeElement(Consts.Service).asType();

        log.i("generateExtras");

        //参数
        ParameterSpec target = ParameterSpec.builder(TypeName.OBJECT, "target").build();
        if (!Utils.isEmpty(extraMap)) {
            //遍历被Extra注解属性的类
            for (Map.Entry<TypeElement, List<Element>> entry : extraMap.entrySet()) {

                TypeElement classType = entry.getKey();

                log.i("isSubtype:" + typeUtils.isSubtype(classType.asType(), activity) + " | " + classType.asType());
                if (typeUtils.isSubtype(classType.asType(), activity)) {
                    generateClass(target, entry, classType);
                } else {
                    throw new RuntimeException("[Just Support Activity Field] : " + classType + " --> " + typeUtils.isSubtype(classType.asType(), activity));
                }


            }
        }
    }

    public void generateClass(ParameterSpec target, Map.Entry<TypeElement, List<Element>> entry, TypeElement classType) {

        builder = MethodSpec.methodBuilder(Consts.EXTRA_METHOD_NAME)//方法名
                .addAnnotation(Override.class)//注解
                .addModifiers(Modifier.PUBLIC)//作用域
                .addParameter(target);//添加参数
        log.i("generate method");
        //设置函数体
        addFuncationBody(entry, classType);

        log.i("generate type");
        //生成Java类
        TypeSpec typeSpec = TypeSpec.classBuilder(classType.getSimpleName() + "$$Extra")
                .addSuperinterface(ClassName.get(extraElement))
                .addModifiers(Modifier.PUBLIC)
                .addMethod(builder.build())
                .build();
        try {
            //生成Java类文件
            log.i("package name:" + ClassName.get(classType).packageName());
            JavaFile.builder(ClassName.get(classType).packageName(), typeSpec).build().writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
            log.i("package name error");
        }
    }

    private void addFuncationBody(Map.Entry<TypeElement, List<Element>> entry, TypeElement classType) {
        for (Element element : entry.getValue()) {
            //$T t = ($T)target
            builder.addStatement("$T t = ($T)target", classType, classType);
            //遍历注解节点 生成函数体
            TypeMirror typeMirror = element.asType();//获取注解节点的类型
            //获取TypeKind 枚举类型的序列号
            int type = typeMirror.getKind().ordinal();
            //获取属性名
            String fieldName = element.getSimpleName().toString();
            //获取注解的值
            String extraName = element.getAnnotation(Extra.class).name();
            //判断直接的值为空的情况下的处理
            extraName = Utils.isEmpty(extraName) ? fieldName : extraName;
            String defaultValue = "t." + fieldName;
            String statement = defaultValue + " = t.getIntent().";
            if (type == TypeKind.BOOLEAN.ordinal()) {
                statement += "getBooleanExtra($S, " + defaultValue + ")";
            } else if (type == TypeKind.BYTE.ordinal()) {
                statement += "getByteExtra($S, " + defaultValue + ")";
            } else if (type == TypeKind.SHORT.ordinal()) {
                statement += "getShortExtra($S, " + defaultValue + ")";
            } else if (type == TypeKind.INT.ordinal()) {
                statement += "getIntExtra($S, " + defaultValue + ")";
            } else if (type == TypeKind.LONG.ordinal()) {
                statement += "getLongExtra($S, " + defaultValue + ")";
            } else if (type == TypeKind.CHAR.ordinal()) {
                statement += "getCharExtra($S, " + defaultValue + ")";
            } else if (type == TypeKind.FLOAT.ordinal()) {
                statement += "getFloatExtra($S, " + defaultValue + ")";
            } else if (type == TypeKind.DOUBLE.ordinal()) {
                statement += "getDoubleExtra($S, " + defaultValue + ")";
            } else {
                //数组类型
                if (type == TypeKind.ARRAY.ordinal()) {
                    addArrayStatement(statement, fieldName, extraName, typeMirror, element);
                } else {
                    //Object
                    addObjectStatement(statement, fieldName, extraName, typeMirror, element);
                }
                return;
            }
            log.i("generate statement:" + statement);
            builder.addStatement(statement, extraName);
        }
    }

    /**
     * 添加对象 String/List/Parcelable
     *
     * @param statement
     * @param extraName
     * @param typeMirror
     * @param element
     */
    private void addObjectStatement(String statement, String fieldName, String extraName,
                                    TypeMirror typeMirror,
                                    Element element) {
        //Parcelable
        if (typeUtils.isSubtype(typeMirror, parcelableType)) {
            statement += "getParcelableExtra($S)";
        } else if (typeMirror.toString().equals(Consts.STRING)) {
            statement += "getStringExtra($S)";
        } else if (typeUtils.isSubtype(typeMirror, iServiceType)) {
//            TestService testService = (TestService) DNRouter.getInstance().build("/main/service1")
//                    .navigation();
//            testService.test();
            statement = "t." + fieldName + " = ($T) $T.getInstance().build($S).navigation()";
            builder.addStatement(statement, TypeName.get(element.asType()), Consts.ROUTER,
                    extraName);
            return;
        } else {
            //List
            TypeName typeName = ClassName.get(typeMirror);
            //泛型
            if (typeName instanceof ParameterizedTypeName) {
                //list 或 arraylist
                ClassName rawType = ((ParameterizedTypeName) typeName).rawType;
                //泛型类型
                List<TypeName> typeArguments = ((ParameterizedTypeName) typeName)
                        .typeArguments;
                if (!rawType.toString().equals(Consts.ARRAYLIST) && !rawType.toString()
                        .equals(Consts.LIST)) {
                    throw new RuntimeException("Not Support Inject Type:" + typeMirror + " " +
                            element);
                }
                if (typeArguments.isEmpty() || typeArguments.size() != 1) {
                    throw new RuntimeException("List Must Specify Generic Type:" + typeArguments);
                }
                TypeName typeArgumentName = typeArguments.get(0);
                TypeElement typeElement = elementUtils.getTypeElement(typeArgumentName
                        .toString());
                // Parcelable 类型
                if (typeUtils.isSubtype(typeElement.asType(), parcelableType)) {
                    statement += "getParcelableArrayListExtra($S)";
                } else if (typeElement.asType().toString().equals(Consts.STRING)) {
                    statement += "getStringArrayListExtra($S)";
                } else if (typeElement.asType().toString().equals(Consts.INTEGER)) {
                    statement += "getIntegerArrayListExtra($S)";
                } else {
                    throw new RuntimeException("Not Support Generic Type : " + typeMirror + " " +
                            element);
                }
            } else {
                throw new RuntimeException("Not Support Extra Type : " + typeMirror + " " +
                        element);
            }
        }
        builder.addStatement(statement, extraName);
    }

    /**
     * 添加数组
     *
     * @param statement
     * @param fieldName
     * @param typeMirror
     * @param element
     */
    private void addArrayStatement(String statement, String fieldName, String extraName, TypeMirror
            typeMirror, Element element) {
        //数组
        switch (typeMirror.toString()) {
            case Consts.BOOLEANARRAY:
                statement += "getBooleanArrayExtra($S)";
                break;
            case Consts.INTARRAY:
                statement += "getIntArrayExtra($S)";
                break;
            case Consts.SHORTARRAY:
                statement += "getShortArrayExtra($S)";
                break;
            case Consts.FLOATARRAY:
                statement += "getFloatArrayExtra($S)";
                break;
            case Consts.DOUBLEARRAY:
                statement += "getDoubleArrayExtra($S)";
                break;
            case Consts.BYTEARRAY:
                statement += "getByteArrayExtra($S)";
                break;
            case Consts.CHARARRAY:
                statement += "getCharArrayExtra($S)";
                break;
            case Consts.LONGARRAY:
                statement += "getLongArrayExtra($S)";
                break;
            case Consts.STRINGARRAY:
                statement += "getStringArrayExtra($S)";
                break;
            default:
                //Parcelable 数组
                String defaultValue = "t." + fieldName;
                //object数组 componentType获得object类型
                ArrayTypeName arrayTypeName = (ArrayTypeName) ClassName.get(typeMirror);
                TypeElement typeElement = elementUtils.getTypeElement(arrayTypeName
                        .componentType.toString());
                //是否为 Parcelable 类型
                if (!typeUtils.isSubtype(typeElement.asType(), parcelableType)) {
                    throw new RuntimeException("Not Support Extra Type:" + typeMirror + " " +
                            element);
                }
                statement = "$T[] " + fieldName + " = t.getIntent()" +
                        ".getParcelableArrayExtra" +
                        "($S)";
                builder.addStatement(statement, parcelableType, extraName);
                builder.beginControlFlow("if( null != $L)", fieldName);
                statement = defaultValue + " = new $T[" + fieldName + ".length]";
                builder.addStatement(statement, arrayTypeName.componentType)
                        .beginControlFlow("for (int i = 0; i < " + fieldName + "" +
                                ".length; " +
                                "i++)")
                        .addStatement(defaultValue + "[i] = ($T)" + fieldName + "[i]",
                                arrayTypeName.componentType)
                        .endControlFlow();
                builder.endControlFlow();
                return;
        }
        builder.addStatement(statement, extraName);
    }

}
