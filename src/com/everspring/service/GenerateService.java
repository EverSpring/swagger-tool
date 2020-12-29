package com.everspring.service;

import com.intellij.psi.PsiClass;

import java.util.List;

/**
 * Description： 生成set的方法名
 * Date： 2020/12/28 15:54
 *
 * @author changchun.xue
 */
public interface GenerateService {
    int MAX_CYCLE_Count = 3;

    List<String> cycleMethod(List<String> methodList, PsiClass psiClass, int count);

    void addMethod(List<String> methodList, PsiClass clazz);
}