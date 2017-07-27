package com.dyan.weiyan.util;

import jdk.nashorn.internal.ir.WhileNode;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dyan on 17/7/26.
 */
public class Filter {

    //敏感词转换为字典树
    public static Map<Object, Object> convertSensitiveWordToDictTree() {
        //敏感词列表
        List<String> sensitiveWords = new ArrayList<>();
        sensitiveWords.add("中国");
        sensitiveWords.add("日本女孩");
        sensitiveWords.add("漂亮");

        if (sensitiveWords.equals(null) || sensitiveWords.size() == 0) {
            return new HashMap<>();
        }
        //初始化敏感词容器，减少扩容操作
        Map<Object, Object> sensitiveWordDictTree = new HashMap<>(sensitiveWords.size());
        Map<Object, Object> currentNode;
        Map<Object, Object> node;
        for (String word : sensitiveWords) {
            if (word == null || "".equals(word)) {
                continue;
            }
            currentNode = sensitiveWordDictTree;
            for (int i = 0, length = word.length(); i < length; i++) {
                //获取当前字符
                char charNow = word.charAt(i); 
                Object temp = currentNode.get(charNow);
                if (temp == null) {
                    node = new HashMap<>();
                    node.put("isEnd", false);
                    currentNode.put(charNow, node);
                    currentNode = node;
                } else {
                    currentNode = (Map<Object, Object>) temp;
                }
                //如是最后一个字符，设置 isEnd=true
                if ((length - 1) == i) {
                    currentNode.put("isEnd", true);
                }
            }
        }
        return sensitiveWordDictTree;
    }


    private static String replaceSensitiveWord(String text) {
        Map<Object, Object> dictTree = convertSensitiveWordToDictTree();
        Map<Object, Object> currentNode;
        int base = 0;
        char word;
        int length = text.length();
        StringBuilder goodText = new StringBuilder();
        StringBuilder temp = new StringBuilder();
        while(base < length){
            currentNode = dictTree;
            for (int i = base ; i < length; i++) {
                word = text.charAt(i);
                currentNode = (Map<Object, Object>) currentNode.get(word);
                if (currentNode == null) {
                    if ( temp != null){
                        goodText.append(temp);
                        temp = new StringBuilder();
                    }
                    goodText.append(word);
                    base = i + 1;
                    break;
                } else {
                    if ((boolean) currentNode.get("isEnd")) {
                        for (int j=base; j<i+1; j++){
                            goodText.append("*");
                        }
                        temp.delete(0,i-base);
                        base = i+1;
                        break;
                    } else {
                        temp.append(word);
                    }
                }
            }
        }
        return goodText.toString();
        //return false;
    }

    public static void main(String[] args) {

        System.out.println(replaceSensitiveWord("中国动漫和日本动漫里的女孩很漂亮！"));

    }
}
