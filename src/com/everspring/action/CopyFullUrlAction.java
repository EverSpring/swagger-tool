package com.everspring.action;

import com.everspring.annotatioin.SpringControllerAnnotation;
import com.everspring.annotatioin.SpringRequestMethodAnnotation;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.apache.commons.lang3.StringUtils;

import java.awt.datatransfer.StringSelection;

/**
 * Description： 拷贝restful的完整url
 * Date： 2021/2/22 14:37
 *
 * @author changchun.xue
 */
public class CopyFullUrlAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        PsiFile psiFile = e.getRequiredData(CommonDataKeys.PSI_FILE);
        PsiElement psiElement = psiFile.findElementAt(editor.getCaretModel().getOffset());

        PsiElement psiParent = PsiTreeUtil.getParentOfType(psiElement, PsiClass.class);
        StringBuilder urlSb = new StringBuilder();
        //类上的地址
        if (psiParent instanceof PsiClass) {
            PsiClass psiClass = (PsiClass) psiParent;
            String url = getClassRestUrl(psiClass);
            if (StringUtils.isNotBlank(url)) {
                urlSb.append(removeQuotation(url));
            }
        }

        psiParent = PsiTreeUtil.getParentOfType(psiElement, PsiAnnotation.class);
        //方法的地址
        if(psiParent instanceof PsiAnnotation){
            PsiAnnotation annotation = (PsiAnnotation) psiParent;
            String qualifiedName = annotation.getQualifiedName();
            if (SpringRequestMethodAnnotation.getByQualifiedName(qualifiedName) != null) {
                String url = getUrl(annotation);
                if (StringUtils.isNotBlank(url)) {
                    urlSb.append(removeQuotation(url));
                }
            }
        }
        CopyPasteManager.getInstance().setContents(new StringSelection(urlSb.toString()));
    }

    private String getUrl(PsiAnnotation annotation) {
        PsiAnnotationMemberValue memberValue = annotation.findAttributeValue("path");
        ASTNode node = memberValue.getNode();
        String url = "";
        if (node == null) {
            memberValue = annotation.findAttributeValue("value");
            node = memberValue.getNode();
            if (node != null) {
                url = node.getText();
            }
        } else {
            url = node.getText();
        }
        //path中可能是多个地址，取第一条
        if (url.contains(",")) {
            url = url.split(",")[0];
        }
        return url;
    }

    /**
     * 是否包含"RestController" "Controller"
     * @param containingClass
     * @return
     */
    private String getClassRestUrl(PsiClass containingClass) {
        PsiModifierList modifierList = containingClass.getModifierList();
        PsiAnnotation controllerAnnotation = modifierList.findAnnotation(SpringControllerAnnotation.REST_CONTROLLER.getQualifiedName());
        if (controllerAnnotation == null) {
            controllerAnnotation = modifierList.findAnnotation(SpringControllerAnnotation.CONTROLLER.getQualifiedName());
            if (controllerAnnotation == null) {
                controllerAnnotation = modifierList.findAnnotation(SpringControllerAnnotation.FEIGN_CLIENT.getQualifiedName());
            }
        }
        if (controllerAnnotation != null) {
            PsiAnnotation requestAnnotation = modifierList.findAnnotation(SpringRequestMethodAnnotation.REQUEST_MAPPING.getQualifiedName());
            if (requestAnnotation == null) {
                requestAnnotation = modifierList.findAnnotation(SpringRequestMethodAnnotation.FEIGN_CLIENGT.getQualifiedName());
            }
            if (requestAnnotation != null) {
                return getUrl(requestAnnotation);
            }
        }
        return null;
    }

    /**
     * 删除引号、大扩号，没有/开头自动添加
     * @param url
     * @return
     */
    private String removeQuotation(String url) {
        String s = url.replaceAll("\"", "").replaceAll("\\{","").replaceAll("}","");
        if (!s.startsWith("/")) {
            s = "/" + s;
        }
        return s;
    }
}
