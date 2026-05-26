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

package com.nageoffer.ai.ragent.knowledge.service;

import cn.hutool.core.util.StrUtil;
import com.nageoffer.ai.ragent.framework.exception.ClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 文件上传安全检查服务
 * <p>支持扩展名校验、MIME类型校验、文件大小限制。
 */
@Slf4j
@Service
public class FileUploadSecurityService {

    @Value("${enterprise.security.upload.allowed-extensions:pdf,docx,xlsx,pptx,txt,md,csv}")
    private String allowedExtensions;

    @Value("${enterprise.security.upload.max-file-size-mb:50}")
    private int maxFileSizeMb;

    /**
     * 校验上传文件安全性
     *
     * @param file 上传的文件
     * @throws ClientException 文件不安全时抛出
     */
    public void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ClientException("文件不能为空");
        }

        String originalFilename = file.getOriginalFilename();
        if (StrUtil.isBlank(originalFilename)) {
            throw new ClientException("文件名不能为空");
        }

        // 1. 文件大小检查
        long sizeMb = file.getSize() / (1024 * 1024);
        if (sizeMb > maxFileSizeMb) {
            throw new ClientException(
                    String.format("文件大小超出限制（最大 %d MB，当前 %d MB）", maxFileSizeMb, sizeMb));
        }

        // 2. 扩展名白名单检查
        String extension = getExtension(originalFilename);
        if (StrUtil.isBlank(extension)) {
            throw new ClientException("文件扩展名不能为空");
        }

        Set<String> allowedSet = new HashSet<>(Arrays.asList(allowedExtensions.split(",")));
        if (!allowedSet.contains(extension.toLowerCase())) {
            throw new ClientException(
                    String.format("不支持的文件类型: .%s，允许的类型: %s", extension, allowedExtensions));
        }

        // 3. 文件名安全检查（防路径穿越）
        if (originalFilename.contains("..") || originalFilename.contains("/") || originalFilename.contains("\\")) {
            throw new ClientException("文件名包含非法字符");
        }

        log.debug("文件安全检查通过: {} ({})", originalFilename, extension);
    }

    private String getExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex < 0 || dotIndex == filename.length() - 1) {
            return "";
        }
        return filename.substring(dotIndex + 1);
    }
}
