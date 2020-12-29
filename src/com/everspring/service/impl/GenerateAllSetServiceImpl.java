package com.everspring.service.impl;

import com.everspring.service.GenerateService;
import com.everspring.view.SetDialogForm;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiTypesUtil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Description： 生成所有
 * Date： 2020/12/27 16:10
 *
 * @author changchun.xue
 */
public class GenerateAllSetServiceImpl extends AbstractSetService implements ActionListener {

    private GenerateService generateService = new GenerateServiceImpl();

    private AnActionEvent e;
    private SetDialogForm form;
    public GenerateAllSetServiceImpl(AnActionEvent e, SetDialogForm form) {
        this.e = e;
        this.form = form;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        this.invoke(e);
        form.close(form.getExitCode());
    }

    @Override
    public List<String> getGenerateSetName(PsiClass psiClass) {
        List<String> methodList = new ArrayList<>();
        generateService.cycleMethod(methodList, psiClass,0);
        generateService.addMethod(methodList, psiClass);
        return methodList;
    }
}
