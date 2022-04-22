package app.com.util;
 
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public class FileUtil{
	
    public boolean uploadFile(MultipartHttpServletRequest mre) throws Exception {

		if (mre.getContentType() == null || !mre.getContentType().startsWith("multipart/form-data")) {
	        return false;
		}
		String OS = System.getProperty("os.name");

        MultipartFile mf = mre.getFile("file");
        
        if (mf.getName().matches("(?i).+\\.jsp|.+\\.php|.+\\.cgi|.+\\.sh")) {
			throw new Exception("not upload file type: jsp, php, cgi, sh)");
		}

        String uploadPath = "";

        String realPath = "/app/data/";

		if (!realPath.endsWith("/")) {
			realPath += "/";
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String fileClassCd = sdf.format(new Date());

		if(OS.indexOf("Win") >=0) realPath = "C:\\"+"upload\\"+"test\\";
		
		File saveDir = new File(realPath);
		if (!saveDir.exists()) saveDir.mkdirs();
            
        String original = mf.getOriginalFilename(); // 업로드하는 파일 name
        String ext = original.substring(original.lastIndexOf(".")+1, original.length());
        
        uploadPath = realPath+fileClassCd+"."+ext;
        
        try {
            mf.transferTo(new File(uploadPath)); // 파일을 위에 지정 경로로 업로드
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    	
        return true;
    }
}