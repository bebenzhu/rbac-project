package com.rbac.system.file.entity;

import java.io.File;
import com.rbac.utils.FileUtils;

public class FileEntity {

    /** 文件ID */
    private String fileId;
    /** 文件名称 */
    private String fileName;
    /** 文件ContentType */
    private String contentType;
    /** 文件大小 */
    private long fileSize;
    /** 文件存储内容,文件URL或地址或其他存储相关的ID */
    private String storedContent;
    /** 创建人 */
    private String inputUserId;
    /** 创建时间 */
    private String inputTime;
    /** 更新人 */
    private String updateUserId;
    /** 更新时间 */
    private String updateTime;

    public FileEntity(){
    }

    public FileEntity(File file){
        this.fileId = file.getName();
        this.fileName = file.getName();
        this.storedContent = file.getAbsolutePath();
        this.fileSize = FileUtils.getFileSize(file);
//        this.updateTime = DateUtils.formatDateTime(new Date(file.lastModified()));
    }

    /** @param 文件ID */
    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
    /** @return 文件ID */
    public String getFileId() {
        return this.fileId;
    }
    /** @param 文件名称 */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    /** @return 文件名称 */
    public String getFileName() {
        return this.fileName;
    }
    /** @param 文件ContentType */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    /** @return 文件ContentType */
    public String getContentType() {
        return this.contentType;
    }
    /** @param 文件大小 */
    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
    /** @return 文件大小 */
    public long getFileSize() {
        return this.fileSize;
    }
    /** @param 文件存储内容,文件URL或地址或其他存储相关的ID */
    public void setStoredContent(String storedContent) {
        this.storedContent = storedContent;
    }
    /** @return 文件存储内容,文件URL或地址或其他存储相关的ID */
    public String getStoredContent() {
        return this.storedContent;
    }
    /** @param 创建人 */
    public void setInputUserId(String inputUserId) {
        this.inputUserId = inputUserId;
    }
    /** @return 创建人 */
    public String getInputUserId() {
        return this.inputUserId;
    }
    /** @param 创建时间 */
    public void setInputTime(String inputTime) {
        this.inputTime = inputTime;
    }
    /** @return 创建时间 */
    public String getInputTime() {
        return this.inputTime;
    }
    /** @param 更新人 */
    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }
    /** @return 更新人 */
    public String getUpdateUserId() {
        return this.updateUserId;
    }
    /** @param 更新时间 */
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    /** @return 更新时间 */
    public String getUpdateTime() {
        return this.updateTime;
    }

    public String getExtName(){
        return FileUtils.getFileSuffix(fileName);
    }

    public String getSizeText() {
        return FileUtils.convertSizeText(fileSize);
    }


}
