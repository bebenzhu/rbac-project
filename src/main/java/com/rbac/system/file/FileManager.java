package com.rbac.system.file;

import com.rbac.system.file.entity.FileEntity;
import com.rbac.utils.JdbcTemplateUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件管理类
 */
public class FileManager {

    /**
     * 获取文件实体类
     * @param fileId
     * @return
     */
    public FileEntity getFileEntity(String fileId){
        String sql = "select fileid,filename,contenttype,filesize,storedcontent from pub_file where fileId=:fileId";
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("fileId",fileId);
        Map<String,Object> fileMap = JdbcTemplateUtils.queryForMap(sql,param);
        if(fileMap!=null){
            FileEntity fileEntity = new FileEntity();
            fileEntity.setFileId(JdbcTemplateUtils.getString(fileMap.get("fileid")));
            fileEntity.setFileName(JdbcTemplateUtils.getString(fileMap.get("filename")));
            fileEntity.setFileSize(JdbcTemplateUtils.getInteger(fileMap.get("filesize")));
            fileEntity.setContentType(JdbcTemplateUtils.getString(fileMap.get("contenttype")));
            fileEntity.setStoredContent(JdbcTemplateUtils.getString(fileMap.get("storedcontent")));
            return fileEntity;
        }else{
            return null;
        }
    }


}
