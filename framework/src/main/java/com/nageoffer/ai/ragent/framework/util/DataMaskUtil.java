/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nageoffer.ai.ragent.framework.util;

/**
 * 数据脱敏工具类
 * <p>支持手机号、邮箱、身份证、银行卡号等敏感信息脱敏。
 * 管理员角色可见原文，普通用户只能看到脱敏后内容。
 */
public final class DataMaskUtil {

    private static final String MASK = "****";
    private static final String STAR = "*";

    private DataMaskUtil() {}

    /**
     * 手机号脱敏（保留前3后4）：138****1234
     */
    public static String maskPhone(String phone) {
        if (phone == null || phone.length() < 7) {
            return phone;
        }
        return phone.substring(0, 3) + MASK + phone.substring(phone.length() - 4);
    }

    /**
     * 邮箱脱敏（保留首字母和域名）：j***@example.com
     */
    public static String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return email;
        }
        int atIndex = email.indexOf('@');
        String prefix = email.substring(0, atIndex);
        String domain = email.substring(atIndex);
        if (prefix.length() <= 1) {
            return STAR + domain;
        }
        return prefix.charAt(0) + "***" + domain;
    }

    /**
     * 身份证号脱敏（保留前6后4）：110101****1234
     */
    public static String maskIdCard(String idCard) {
        if (idCard == null || idCard.length() < 10) {
            return idCard;
        }
        return idCard.substring(0, 6) + "********" + idCard.substring(idCard.length() - 4);
    }

    /**
     * 银行卡号脱敏（仅显示后4位）：****5678
     */
    public static String maskBankCard(String bankCard) {
        if (bankCard == null || bankCard.length() < 4) {
            return bankCard;
        }
        return MASK + bankCard.substring(bankCard.length() - 4);
    }

    /**
     * 姓名脱敏（保留姓，隐藏名）：张**
     */
    public static String maskName(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        if (name.length() == 1) {
            return STAR;
        }
        return name.charAt(0) + STAR.repeat(Math.min(name.length() - 1, 2));
    }

    /**
     * IP地址脱敏（隐藏最后两段）：192.168.*.*
     */
    public static String maskIp(String ip) {
        if (ip == null || !ip.contains(".")) {
            return ip;
        }
        int lastDot = ip.lastIndexOf('.');
        int secondLastDot = ip.lastIndexOf('.', lastDot - 1);
        if (secondLastDot < 0) {
            return ip;
        }
        return ip.substring(0, secondLastDot) + ".*.*";
    }
}
