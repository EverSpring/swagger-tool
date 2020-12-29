package com.everspring.service.impl;

import com.everspring.service.GenerateService;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;

import java.util.List;

/**
 * Description： 生成set的方法名
 * Date： 2020/12/28 15:56
 *
 * @author changchun.xue
 */
public class GenerateServiceImpl implements GenerateService {
    @Override
    public List<String> cycleMethod(List<String> methodList, PsiClass psiClass, int count) {
        if (count >= MAX_CYCLE_Count) {
            return methodList;
        }
        PsiClass superClass = psiClass.getSuperClass();
        if (superClass != null && !superClass.getQualifiedName().startsWith("java.")) {
            addMethod(methodList, superClass);
            return this.cycleMethod(methodList, superClass, count++);
        }
        return methodList;
    }

    @Override
    public void addMethod(List<String> methodList, PsiClass clazz) {
        PsiMethod[] methods = clazz.getMethods();
        for (int i = 0; i < methods.length; i++) {
            PsiMethod method = methods[i];
            String methodName = method.getName();
            if (!methodName.startsWith("set")) {
                continue;
            }
            methodList.add(methodName);
        }
    }

}
