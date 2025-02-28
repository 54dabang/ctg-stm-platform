package com.ctg.stm.util;

import io.swagger.models.auth.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: leixingbang@qiyi.com
 * @create: 2021/01/13 16:07
 * @description:
 */
public class ProjectEnum {
/*    public static enum ProBpmStatus {
        PROJECT_APPROVAL(1, "立项中"), PROCESSING(2, "执行中"), PROCESSING_ACCEPTANCE(3, "验收中"), ACCEPTANCE(4, "已验收");
        private int value;
        private String desc;

        ProBpmStatus(int value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public int value() {
            return value;
        }

        public String desc() {
            return desc;
        }

        public static Map<Integer, String> CODE_NAME_MAP = new HashMap<>();
        public static Map<String, Integer> DESC_CODE_MAP = new HashMap<>();

        public static List<String> NAME_DESC = new ArrayList<>();

        static {
            ProBpmStatus[] enums = ProBpmStatus.values();
            for (ProBpmStatus enumElem : enums) {
                CODE_NAME_MAP.put(enumElem.value(), enumElem.desc);
                DESC_CODE_MAP.put(enumElem.desc, enumElem.value);
                NAME_DESC.add(enumElem.desc());
            }
        }

        public static ProBpmStatus getByValue(int value) {
            for (ProBpmStatus projectGroupStatus : ProBpmStatus.values()) {
                if (projectGroupStatus.value() == value) {
                    return projectGroupStatus;
                }
            }
            return null;
        }

        public static ProBpmStatus getByDesc(String desc) {
            return getByValue(DESC_CODE_MAP.get(desc));
        }

        public static List<String> getDescList() {
            return NAME_DESC;
        }

        public static List<Integer> getValueList() {
            List<Integer> list = new ArrayList<>();
            for (ProBpmStatus projectGroupStatus : ProBpmStatus.values()) {
                list.add(projectGroupStatus.value());
            }
            return list;
        }
    }*/


    public static enum ProjectStatus {
        PROJECT_APPROVAL(1, "立项阶段"), PROCESSING(2, "实施阶段"), ACCEPTANCE(3, "验收阶段");
        private int value;
        private String desc;

        ProjectStatus(int value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public int value() {
            return value;
        }

        public String desc() {
            return desc;
        }

        public static Map<Integer, String> CODE_NAME_MAP = new HashMap<>();
        public static Map<String, Integer> DESC_CODE_MAP = new HashMap<>();

        public static List<String> NAME_DESC = new ArrayList<>();

        static {
            ProjectStatus[] enums = ProjectStatus.values();
            for (ProjectStatus enumElem : enums) {
                CODE_NAME_MAP.put(enumElem.value(), enumElem.desc);
                DESC_CODE_MAP.put(enumElem.desc, enumElem.value);
                NAME_DESC.add(enumElem.desc());
            }
        }

        public static ProjectStatus getByValue(int value) {
            for (ProjectStatus projectGroupStatus : ProjectStatus.values()) {
                if (projectGroupStatus.value() == value) {
                    return projectGroupStatus;
                }
            }
            return null;
        }

        public static ProjectStatus getByDesc(String desc) {
            return getByValue(DESC_CODE_MAP.get(desc));
        }

        public static List<String> getDescList() {
            return NAME_DESC;
        }

        public static List<Integer> getValueList() {
            List<Integer> list = new ArrayList<>();
            for (ProjectStatus projectGroupStatus : ProjectStatus.values()) {
                list.add(projectGroupStatus.value());
            }
            return list;
        }
    }

    public static enum ProjectCategory {
        NATIONAL(1, "国家级"), PROVINCIAL(2, "省部级"), GROUP(3, "集团级"), SUBSIDIARY_COMPANY(4, "子企业");
        private int value;
        private String desc;

        ProjectCategory(int value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public int value() {
            return value;
        }

        public String desc() {
            return desc;
        }

        public static Map<Integer, String> CODE_NAME_MAP = new HashMap<>();
        public static Map<String, Integer> DESC_CODE_MAP = new HashMap<>();

        public static List<String> NAME_DESC = new ArrayList<>();

        static {
            ProjectCategory[] enums = ProjectCategory.values();
            for (ProjectCategory enumElem : enums) {
                CODE_NAME_MAP.put(enumElem.value(), enumElem.desc);
                DESC_CODE_MAP.put(enumElem.desc, enumElem.value);
                NAME_DESC.add(enumElem.desc());
            }
        }

        public static ProjectCategory getByValue(int value) {
            for (ProjectCategory projectCategory : ProjectCategory.values()) {
                if (projectCategory.value() == value) {
                    return projectCategory;
                }
            }
            return null;
        }

        public static ProjectCategory getByDesc(String desc) {
            return getByValue(DESC_CODE_MAP.get(desc));
        }

        public static List<String> getDescList() {
            return NAME_DESC;
        }

        public static List<Integer> getValueList() {
            List<Integer> list = new ArrayList<>();
            for (ProjectCategory projectCategory : ProjectCategory.values()) {
                list.add(projectCategory.value());
            }
            return list;
        }
    }
}
