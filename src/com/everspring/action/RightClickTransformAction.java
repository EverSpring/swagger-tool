package com.everspring.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageDialogBuilder;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ThrowableRunnable;

/**
 * Description： 右键按钮
 * Date： 2020/11/16 10:02
 *
 * @author changchun.xue
 */
public class RightClickTransformAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(LangDataKeys.PROJECT);
        if (project == null) {
            return;
        }
        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        PsiFile psiFile = e.getRequiredData(CommonDataKeys.PSI_FILE);
        FileType fileType = psiFile.getViewProvider().getVirtualFile().getFileType();
        if (!"java".equals(fileType.getDefaultExtension())) {
            MessageDialogBuilder.yesNoCancel("提示", "只能转换java文件").show();
        }
        //获取java文件
        PsiElement referenceAt = psiFile.findElementAt(editor.getCaretModel().getOffset());
        PsiJavaFile psiJavaFile = PsiTreeUtil.getParentOfType(referenceAt, PsiJavaFile.class);
        PsiElement[] children = psiJavaFile.getChildren();
        PsiElementFactory factory = PsiElementFactory.SERVICE.getInstance(project);
        boolean wasImport = false;
        for (PsiElement child : children) {
            //判断是否import
            wasImport = this.importClass(project, factory, wasImport, child);

            //获取java正式内容
            if (!(child instanceof PsiClass)) {
                continue;
            }
            generateAnnotation(project, child);
            break;
        }
    }

    /**
     * 生成注释
     *
     * @param project
     * @param child
     */
    private void generateAnnotation(Project project, PsiElement child) {
        PsiElement[] clsEle = child.getChildren();
        for (PsiElement psiElement : clsEle) {
            if (!(psiElement instanceof PsiField)) {
                continue;
            }
            PsiField psiField = (PsiField) psiElement;
            String name = psiField.getName();
            PsiDocComment docComment = psiField.getDocComment();
            if (docComment != null && docComment.getText() != null && docComment.getText().trim() != "") {
                boolean hasSwaggerAnnotation = false;
                PsiAnnotation[] annotations = psiField.getAnnotations();
                for (PsiAnnotation annotation : annotations) {
                    String qualifiedName = annotation.getQualifiedName();
                    if ("io.swagger.annotations.ApiModelProperty".equals(qualifiedName)) {
                        //已经有这个注解就跳过
                        hasSwaggerAnnotation = true;
                        continue;
                    }
                }
                if (!hasSwaggerAnnotation) {
                    //没有swagger注解，获取注释内容，生成注解
                    PsiElement[] descriptionElements = docComment.getDescriptionElements();
                    for (PsiElement desc : descriptionElements) {
                        String text = desc.getText();
                        if (text != null && !"".equals(text.trim())) {
                            try {
                                WriteCommandAction.writeCommandAction(project).run(
                                        (ThrowableRunnable<Throwable>) () -> {
                                            if (psiElement.getContainingFile() == null) {
                                                return;
                                            }

                                            // 写入注解
                                            psiField.getModifierList().addAnnotation(String.format("ApiModelProperty(\"%s\")", text.trim()));

                                        });
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * 导入class
     *
     * @param project
     * @param factory
     * @param wasImport
     * @param child
     * @return boolean
     */
    private boolean importClass(Project project, PsiElementFactory factory, boolean wasImport, PsiElement child) {
        if (child instanceof PsiImportList) {
            PsiImportList psiImportList = (PsiImportList) child;
            PsiElement[] importList = child.getChildren();
            for (PsiElement importEle : importList) {
                if (importEle.getText().contains("io.swagger.annotations.ApiModelProperty")) {
                    wasImport = true;
                    break;
                }
            }
            if (!wasImport) {
                PsiImportStatement importStatement = factory.createImportStatementOnDemand("io.swagger.annotations");
                try {
                    WriteCommandAction.writeCommandAction(project).run(
                            (ThrowableRunnable<Throwable>) () -> {
                                if (child.getContainingFile() == null) {
                                    return;
                                }
                                // 写入import
                                psiImportList.add(importStatement);
                            });
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                wasImport = true;
            }
        }
        return wasImport;
    }
}
