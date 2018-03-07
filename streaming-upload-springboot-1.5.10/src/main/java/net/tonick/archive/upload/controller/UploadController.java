package net.tonick.archive.upload.controller;

import java.io.FileOutputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.util.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Oli Thissen <oli@tonick.net>
 */
@RestController
@RequestMapping("/upload")
public class UploadController {
	private static final Logger LOGGER = LoggerFactory.getLogger(UploadController.class);

	@PostMapping()
    public @ResponseBody ResponseEntity upload(HttpServletRequest request) {
        try {
            ServletFileUpload upload = new ServletFileUpload();
            
            FileItemIterator iter = upload.getItemIterator(request);
            while (iter.hasNext()) {
            	FileItemStream item = iter.next();
                if (!item.isFormField()) {
                	FileOutputStream outputStream = new FileOutputStream("test.dat");
                	InputStream inputStream = item.openStream();
                    Streams.copy(inputStream, outputStream, true);
                    inputStream.close();
                }
            }
        } catch (Exception ex) {
        	LOGGER.error("Dammit", ex);
    		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
		return new ResponseEntity<>(HttpStatus.OK);
    }
}
