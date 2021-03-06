package com.everspring.service.impl;

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

import java.util.List;

/**
 * Description： set抽象类
 * Date： 2020/12/28 16:10
 *
 * @author changchun.xue
 */
public abstract class AbstractSetService {

    /**
     * 调用
     * @param e
     */
    public void invoke(AnActionEvent e){
        final Project project = e.getProject();

        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        PsiFile psiFile = e.getRequiredData(CommonDataKeys.PSI_FILE);
        PsiElement psiElement = psiFile.findElementAt(editor.getCaretModel().getOffset());
        PsiElement parentElement = psiElement.getParent();
        PsiElement psiParent = PsiTreeUtil.getParentOfType(psiElement, PsiLocalVariable.class, PsiReferenceExpression.class);
        if (psiParent instanceof PsiLocalVariable) {
            PsiLocalVariable localVariable = (PsiLocalVariable) psiParent;
            PsiClass psiClass = PsiTypesUtil.getPsiClass(localVariable.getType());
            String paramName = localVariable.getName();
            List<String> setNameList = this.getGenerateSetName(psiClass);
            this.generateSetter(project, editor, parentElement.getParent(), paramName, psiClass,setNameList);
        }else if(psiParent instanceof PsiReferenceExpression) {
            PsiReferenceExpression localVariable = (PsiReferenceExpression) parentElement;
            String paramName = localVariable.getReferenceName();
            PsiClass psiClass = PsiTypesUtil.getPsiClass(localVariable.getType());
            //parentElement.getParent() 需要取当前属性的行元素（也就是Class）
            List<String> setNameList = this.getGenerateSetName(psiClass);
            this.generateSetter(project, editor, parentElement.getParent(), paramName, psiClass,setNameList);
        }
    }

    protected void generateSetter(Project project, Editor editor, PsiElement parentElement, String paramName, PsiClass psiClass,List<String> methodList){
        if (methodList == null || methodList.size() == 0) {
            return;
        }
        final Document document = editor.getDocument();
        String preTab = this.calculateSplitText(document, parentElement.getTextOffset());

        //得到选中字符串的起始和结束位置
        final int endOffSet = editor.getCaretModel().getOffset();

        //得到最大插入字符串（即生成set函数字符串）位置
        int maxOffset = document.getTextLength() - 1;

        //计算选中字符串所在的行号，并通过行号得到下一行的第一个字符的起始偏移量
        int curLineNumber = document.getLineNumber(endOffSet);
        //获取指定行的第一个字符在全文中的偏移量
        int nextLineStartOffset = document.getLineStartOffset(curLineNumber + 1);

        //计算字符串的插入位置
        int insertOffset = maxOffset > nextLineStartOffset ? nextLineStartOffset : maxOffset;
        StringBuilder sb = new StringBuilder(preTab);
        if (methodList.size() > 0) {
            for (int i = 0; i < methodList.size(); i++) {
                String methodName = methodList.get(i);
                if (i == (methodList.size() - 1)) {
                    sb.append(String.format("%s.%s();", paramName, methodName)).append("\n");
                } else {
                    sb.append(String.format("%s.%s();", paramName, methodName)).append(preTab);
                }
            }
        }
        WriteCommandAction.runWriteCommandAction(project, new Runnable() {
            @Override
            public void run() {
                document.insertString(insertOffset, sb.toString());
            }
        });
        PsiDocumentManager.getInstance(project).commitDocument(document);
    }

    public abstract List<String> getGenerateSetName(PsiClass psiClass);



    /**
     * 计算代码开始行前的空格
     * 从光标所在行第一个非空文本往上推到第一个换行或空格，就是需要空格的长度
     * @param document
     * @param statementOffset
     * @return
     */
    public String calculateSplitText(Document document, int statementOffset) {
        String splitText = "";
        int cur = statementOffset;
        String text = document.getText(new TextRange(cur - 1, cur));
        while (text.equals(" ") || text.equals("\t")) {
            splitText = text + splitText;
            cur--;
            if (cur < 1) {
                break;
            }
            text = document.getText(new TextRange(cur - 1, cur));
        }
        splitText = "\n" + splitText;
        return splitText;
    }
}
