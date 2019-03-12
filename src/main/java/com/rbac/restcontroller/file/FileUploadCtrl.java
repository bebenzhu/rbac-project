package com.rbac.restcontroller.file;


import com.rbac.system.annotation.ApiExplain;
import com.rbac.system.file.FileManager;
import com.rbac.system.file.entity.FileEntity;
import com.rbac.utils.FileUtils;
import com.rbac.utils.JdbcTemplateUtils;
import com.rbac.utils.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/FileUploadCtrl")
@ApiExplain("文件上传")
public class FileUploadCtrl {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @POST
    @Path("/uploadToDisks")
    public List<FileEntity> uploadToDisks(@Context HttpServletRequest request) throws IOException {
        List<FileEntity> fileEntities = RequestUtils.getUploadFileEntitys(request);
        List<Map<String,Object>> paramMapList = new ArrayList<Map<String,Object>>();
        String sql = "insert into PUB_FILE(fileid,filename,contenttype,filesize,storedcontent,inputuserid,inputtime) " +
                "values(:fileid,:filename,:contenttype,:filesize,:storedcontent,:inputuserid,:inputtime)";
        for(FileEntity fileEntity:fileEntities){
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("fileid",fileEntity.getFileId());
            paramMap.put("filename",fileEntity.getFileName());
            paramMap.put("contenttype",fileEntity.getContentType());
            paramMap.put("filesize",fileEntity.getFileSize());
            paramMap.put("storedcontent",fileEntity.getStoredContent());
            paramMap.put("inputuserid","system");
            paramMap.put("inputtime","");
            paramMapList.add(paramMap);
        }
        JdbcTemplateUtils.insertForList(sql, paramMapList.toArray(new Map[paramMapList.size()]));
        return fileEntities;
    }


    @GET
    @Path("/downLoadByFileId")
    public void downLoadByFileId(@Context HttpServletRequest request, @Context HttpServletResponse response,@QueryParam("fileId") String fileId){
        FileManager fileManager = new FileManager();
        FileEntity fileEntity = fileManager.getFileEntity(fileId);
        String downFileName = fileEntity.getFileName();
        Map<String,String> headers = new HashMap<String,String>();
        headers.put("Content-Disposition", "attachment; filename="+ FileUtils.iso8859(downFileName,request));
        InputStream inputStream = FileUtils.openFile(fileEntity.getStoredContent());
        response.reset();
        FileUtils.renderStream(response, inputStream,"octets/stream", headers);
    }


}
