package com.everspring.service.impl;

import com.everspring.service.GenerateService;
import com.everspring.view.SetSelectListView;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Description： 获取set方法
 * Date： 2020/12/28 16:18
 *
 * @author changchun.xue
 */
public class GetSetImpl extends AbstractSetService{

    private SetSelectListView setSelectListView;

    public GetSetImpl(SetSelectListView setSelectListView) {
        this.setSelectListView = setSelectListView;
    }

    private GenerateService generateService = new GenerateServiceImpl();

    @Override
    public void generateSetter(Project project, Editor editor, PsiElement parentElement, String paramName, PsiClass psiClass, List<String> setList) {

    }

    @Override
    public List<String> getGenerateSetName(PsiClass psiClass) {
        List<String> methodList = new ArrayList<>();
        generateService.cycleMethod(methodList, psiClass, 0);
        generateService.addMethod(methodList, psiClass);
        if (methodList != null && methodList.size() > 0) {
            Object[] listData = methodList.toArray();
            setSelectListView.getNameList().setListData(listData);
            return methodList;
        }
        return null;
    }
}
