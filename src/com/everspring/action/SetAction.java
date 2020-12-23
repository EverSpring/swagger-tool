package com.everspring.action;

import com.intellij.openapi.actionSystem.AnAction;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Description： 生成set的实现
 * Date： 2020/12/22 14:38
 *
 * @author changchun.xue
 */
public class SetAction extends AnAction {

    private final static int MAX_CYCLE_Count = 3;

    @Override
    public void actionPerformed(AnActionEvent e) {
        final Project project = e.getProject();

        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        PsiFile psiFile = e.getRequiredData(CommonDataKeys.PSI_FILE);
        PsiElement psiElement = psiFile.findElementAt(editor.getCaretModel().getOffset());
        PsiElement parentElement = psiElement.getParent();
        PsiElement psiParent = PsiTreeUtil.getParentOfType(psiElement,PsiLocalVariable.class,PsiReferenceExpression.class);
        if (psiParent instanceof PsiLocalVariable) {
            PsiLocalVariable localVariable = (PsiLocalVariable) psiParent;
            PsiClass psiClass = PsiTypesUtil.getPsiClass(localVariable.getType());
            String paramName = localVariable.getName();
            this.generateSetter(project, editor, parentElement.getParent(), paramName, psiClass);
        }else if(psiParent instanceof PsiReferenceExpression) {
            PsiReferenceExpression localVariable = (PsiReferenceExpression) parentElement;
            String paramName = localVariable.getReferenceName();
            PsiClass psiClass = PsiTypesUtil.getPsiClass(localVariable.getType());
            //parentElement.getParent() 需要取当前属性的行元素（也就是Class）
            this.generateSetter(project, editor, parentElement.getParent(), paramName, psiClass);
        }
    }

    /**
     * 生成set的主要方法
     * @param project
     * @param editor
     * @param parentElement
     * @param paramName
     * @param psiClass
     */
    private void generateSetter(Project project, Editor editor, PsiElement parentElement, String paramName, PsiClass psiClass) {
        List<String> methodList = new ArrayList<>();
        //todo 父类的method
        this.cycleMethod(methodList, psiClass,0);
        addMethod(methodList, psiClass);
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

    private List<String> cycleMethod(List<String> methodList,PsiClass psiClass,int count) {
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

    private void addMethod(List<String> methodList, PsiClass clazz) {
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
