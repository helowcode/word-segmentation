package ats.spring.test;

import lombok.NonNull;

import java.util.*;

/**
 * <b>Description : </b> 中文分词
 *
 * @author CPF
 * @date 2019/11/7 19:15
 **/
public class ChinaMatcher {

    /**
     *  词典表
     */
    private Map<String, Integer> dictMap;

    public ChinaMatcher(@NonNull Map<String, Integer> dictMap) {
        this.dictMap = dictMap;
        max_dict_in_dict = 3;
        min_dict_in_dict = 1;
    }

    public int check(String str) {
        return dictMap.getOrDefault(str, 0);
    }

    public int max_dict_in_dict;

    public int min_dict_in_dict;

    public List<String> positiveParticiple(String text) {
        List<String> list = new ArrayList<>();
        int idx = 0;
        int len = text.length();
        while (idx < len) {
            // 分词是否匹配到
            int check = 0;
            for (int i = max_dict_in_dict; i >= min_dict_in_dict; i--) {
                String w = text.substring(idx, Math.min(i + idx, len));
                check = check(w);
                if (check > 0) {
                    list.add(w);
                    idx += w.length();
                    break;
                }
            }
            if (check == 0) {
                idx++;
            }
        }
        return list;
    }

    public List<String> negativeParticiple(String text) {
        List<String> list = new ArrayList<>();
        int idx = text.length();
        while (idx > 0) {
            // 分词是否匹配到
            int check = 0;
            for (int i = max_dict_in_dict; i >= min_dict_in_dict; i--) {
                String w = text.substring(Math.max(idx - i, 0), idx);
                check = check(w);
                if (check > 0) {
                    list.add(w);
                    idx -= w.length();
                    break;
                }
            }
            if (check == 0) {
                idx--;
            }
        }
        Collections.reverse(list);
        return list;
    }

    public List<String> doubleParticiple(String text) {
        List<String> positive = positiveParticiple(text);
        List<String> negative = negativeParticiple(text);
        if (positive.size() < negative.size()) {
            return positive;
        }
        if (positive.size() > negative.size()) {
            return negative;
        }
        int pSingleWordCnt = 0;
        int nSingleWordCnt = 0;
        boolean same = true;
        for (int i = 0, len = positive.size(); i < len; i++) {
            final String p = positive.get(i);
            final String n = negative.get(i);
            if (same) {
                if (!p.equals(n)) {
                    same = false;
                }
            }
            if (p.length() == 1) {
                pSingleWordCnt++;
            }
            if (n.length() == 1) {
                nSingleWordCnt++;
            }
        }
        if (pSingleWordCnt > nSingleWordCnt) {
            return negative;
        } else {
            return positive;
        }
    }

    public static void main(String[] args) {
        Map<String, Integer> dictMap = new HashMap<>();
            dictMap.put("研究", 1);
            dictMap.put("研究生", 1);
            dictMap.put("起源", 1);
            dictMap.put("的", 1);
        final ChinaMatcher chinaMatcher = new ChinaMatcher(dictMap);
        final String text = "研究生命和明月的起源";
        System.out.println(chinaMatcher.doubleParticiple(text));
    }

}
