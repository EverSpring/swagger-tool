package com.everspring.action;

import com.everspring.config.WordMapConfigComponent;
import com.everspring.data.DataList;
import com.everspring.service.impl.TranslateService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ThrowableRunnable;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;


/**
 * 抽象转换
 *
 * @author changchun.xue
 * @date 2020/11/17
 */
public abstract class AbstractTransformAction extends AnAction {

    private TranslateService translatorService = ServiceManager.getService(TranslateService.class);
    /**
     * 必须手动显示调用一次，不然idea启动时不会调用loadState，要打开设置界面时才加载
     */
    final WordMapConfigComponent instance = ServiceManager.getService(WordMapConfigComponent.class);

    public void actionPerformed(AnActionEvent e,Boolean isTranslator) {
        Project project = e.getData(LangDataKeys.PROJECT);
        if (project == null) {
            return;
        }
        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        PsiFile psiFile = e.getRequiredData(CommonDataKeys.PSI_FILE);
        //获取java文件
        PsiElement referenceAt = psiFile.findElementAt(editor.getCaretModel().getOffset());
        PsiJavaFile psiJavaFile = PsiTreeUtil.getParentOfType(referenceAt, PsiJavaFile.class);
        PsiElement[] allElement = psiJavaFile.getChildren();
        PsiElementFactory factory = PsiElementFactory.SERVICE.getInstance(project);
        boolean wasImport = false;
        for (PsiElement everyTypeElement : allElement) {
            //判断是否import
            wasImport = this.importClass(project, factory, wasImport, everyTypeElement);

            //获取java正式内容
            if (!(everyTypeElement instanceof PsiClass)) {
                continue;
            }
            this.generateAnnotation(project, everyTypeElement,isTranslator);
            break;
        }
    }

    public void update(@NotNull AnActionEvent e) {
        super.update(e);
        PsiFile psiFile = e.getRequiredData(CommonDataKeys.PSI_FILE);
        FileType fileType = psiFile.getViewProvider().getVirtualFile().getFileType();
        if (!"java".equals(fileType.getDefaultExtension())) {
            e.getPresentation().setEnabledAndVisible(false);
        } else {
            e.getPresentation().setEnabledAndVisible(true);
        }
    }

    /**
     * 生成注释
     *
     * @param project
     * @param everyTypeElement 具体的class代码element
     */
    private void generateAnnotation(Project project, PsiElement everyTypeElement,Boolean isTranslator) {
        PsiElement[] clsEle = everyTypeElement.getChildren();
        for (PsiElement psiElement : clsEle) {
            if (!(psiElement instanceof PsiField)) {
                continue;
            }
            PsiField psiField = (PsiField) psiElement;
            String name = psiField.getName();
            PsiDocComment docComment = psiField.getDocComment();
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
                if (docComment != null && StringUtils.isNotBlank(docComment.getText().trim())) {
                    PsiElement[] descriptionElements = docComment.getDescriptionElements();
                    for (PsiElement desc : descriptionElements) {
                        String text = desc.getText();
                        if (text != null && !"".equals(text.trim())) {
                            this.write(project, psiElement, psiField, text);
                        }
                    }
                } else {
                    // 获取单行注释的内容
                    String allText = psiField.getText();
                    String comment = "";
                    String[] split = allText.split("\n");
                    for (String s : split) {
                        if (s.contains("//")) {
                            comment = s.substring(s.indexOf("//") + 2, s.length() - 1);
                            continue;
                        }
                    }
                    if (StringUtils.isNotBlank(comment)) {
                        this.write(project, psiElement, psiField, comment.trim());
                    } else {
                        String text = "";
                        if (DataList.wordMap.containsKey(name)) {
                            text = DataList.wordMap.get(name);
                        } else if (isTranslator)  {
                            text = translatorService.translate(name);
                        }
                        if (StringUtils.isNotBlank(text)) {
                            this.write(project, psiElement, psiField, text);
                        }
                    }
                }
            }
        }
    }

    /**
     * 注解写进java文件
     * @param project
     * @param psiElement
     * @param psiField
     * @param text
     */
    private void write(Project project, PsiElement psiElement, PsiField psiField, String text) {
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
