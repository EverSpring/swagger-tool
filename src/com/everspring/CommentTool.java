package com.everspring;

import com.everspring.service.AnnotationTransformService;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.codeStyle.CodeStyleManager;

/**
 * Description： 转换类
 * Date： 2020/11/11 16:16
 *
 * @author changchun.xue
 */
public class CommentTool extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        // TODO: insert action logic here
        Editor editor = event.getData(LangDataKeys.EDITOR);
        Document document = editor.getDocument();
        String text = document.getText();
        String trans = new AnnotationTransformService().trans(text,document);
        document.setText(trans);
    }
}
