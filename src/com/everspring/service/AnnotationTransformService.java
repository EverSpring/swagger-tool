package com.everspring.service;


import com.intellij.openapi.editor.Document;

/**
 * 注释转换服务
 *
 * @author everspring
 * @date 2020/11/11
 */
public class AnnotationTransformService {
    private static final String beginPrefix = "public class";
    private static final String importStr = "import";
    private static final String importProperty = "import io.swagger.annotations.ApiModelProperty;";
    private static final String template = "@ApiModelProperty(value = \"%s\")";


    public String trans(String oldText, Document document) {
        int i = 0;
        StringBuilder sb = new StringBuilder();
        String sep = "\n";
        String[] list = oldText.split("\n");
        if (list == null || list.length < 2) {
            list = oldText.split("\n\n");
            sep = "\n\n";
        }
        boolean start = false;
        boolean isImport = true;
        for (int j = 0; j < list.length; j++) {
            String line = list[j];
            if (line.startsWith(beginPrefix)) {
                start = true;
                sb.append(line).append(sep);
                continue;
            }
            if (!start) {
                if (line.startsWith(importStr) && isImport) {
                    sb.append(importProperty).append(sep);
                    isImport = false;
                }
                sb.append(line).append(sep);
                continue;
            }
            String tmpLine = line.trim();
            if (tmpLine.startsWith("* ")) {
                String format = String.format(template, tmpLine.substring(1).trim());
                sb.append("    ").append(format.trim()).append(sep);
                String code = list[j + 2].trim();
                if (code.startsWith("@")) {
                    sb.append(code.trim()).append(sep);
                    sb.append(list[j + 3].trim()).append(sep);
                }
            } else if (!tmpLine.startsWith("/*") && !tmpLine.startsWith("*/")) {
                sb.append(line).append(sep);
            }
            i++;
        }
        return sb.toString();
    }
}
