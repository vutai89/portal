/**
 * 
 */
package com.mcredit.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.io.FileUtils;

import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.data.exception.NoRecordFoundException;
import com.mcredit.model.dto.DowloadZipDTO;
import com.mcredit.model.dto.debt_home.DebtHomeAllFile;
import com.mcredit.model.enums.AuthorizationToken;
import com.mcredit.model.enums.Commons;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.enums.ServiceName;
import com.mcredit.model.object.AuthorizationTokenResult;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.dto.UserDTO;
import com.mcredit.sharedbiz.manager.AuthorizationManager;
import com.mcredit.sharedbiz.manager.UserManager;
import com.mcredit.sharedbiz.validation.AuthorizationException;
import com.mcredit.sharedbiz.validation.PermissionException;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.DateUtil;

/**
 * @author anhdv.ho
 *
 */
public class BasedService {

	private BaseAuthorization _authorization = null;

	public BasedService() {
	}

	public BasedService(@Context HttpHeaders headers) {
		this.headers = headers;
		_authorization = new BaseAuthorization(headers);
	}

	protected HttpHeaders headers = null;
	protected UserDTO currentUser = new UserDTO();

	protected Response ok(Object object) {
		return Response.status(Status.OK.getStatusCode()).entity(object).build();
	}

	protected Response file(String url, String filename, Boolean isCleanUp) throws ValidationException {
		File file = new File(url);

		if (!file.exists()) {
			throw new ValidationException(
					Messages.getString("validation.field.mainMessage", Labels.getString("label.xsell.file.download")));
		}

		ResponseBuilder response = Response.ok((Object) file);
		response.header("Content-Disposition", String.format("attachment; filename=\"%s\"", filename));

		// clean up
		if (isCleanUp) {
			file.deleteOnExit();
		}

		return response.build();
	}

	protected Response responseFile(File file, Boolean isCleanUp) throws ValidationException, IOException {

		if (!file.exists())
			throw new ValidationException(Messages.getString("File do not exists"));

		InputStream inputStream = new FileInputStream(file);

		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream outputStream) throws IOException {
				try {

					byte[] bytesArray = new byte[4096];
					int bytesRead = -1;
					while ((bytesRead = inputStream.read(bytesArray)) != -1) {
						outputStream.write(bytesArray, 0, bytesRead);
					}

					inputStream.close();

					if (isCleanUp)
						file.delete();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		return Response.ok(stream).header("content-disposition", "attachment; filename = " + file.getName()).build();
	}

	protected Response created(Object object) {
		return Response.status(Status.CREATED.getStatusCode()).entity(object).build();
	};

	protected Response accepted(Object object) {
		return Response.status(Status.ACCEPTED.getStatusCode()).entity(object).build();
	}

	protected Response noContent(Object object) {
		return Response.status(Status.NO_CONTENT.getStatusCode()).entity(object).build();
	}

	protected Response internalServerError(Object object) {
		return Response.status(Status.INTERNAL_SERVER_ERROR.getStatusCode()).entity(object).build();
	}

	protected Response unauthorize(Object object) {
		return Response.status(Status.UNAUTHORIZED.getStatusCode()).entity(object).build();
	}

	protected Response badRequest(Object object) {
		return Response.status(Status.BAD_REQUEST.getStatusCode()).entity(object).build();
	}

	protected Response returnObject(Object object) {
		return Response.ok(object, MediaType.APPLICATION_JSON).build();
	}

	protected <T> T authorize(Callable<T> func) throws Exception {

		validateToken();

		return func.call();
	}

	protected <T> T authorize(ServiceName serviceName, Callable<T> func) throws Exception {
		System.out.println("SERVICE NAME: " + serviceName);

		validateToken();

		validatePermission(serviceName);

		return func.call();
	}

	protected <T> T authorize(ServiceName serviceName, String user, Callable<T> func) throws Exception {

		validateToken();

		validatePermission(serviceName, user);

		return func.call();
	}

	protected <T> T anonymous(Callable<T> func) throws Exception {
		return func.call();
	}

	private void validatePermission(ServiceName serviceName, String user) throws Exception {
		try (UserManager manager = new UserManager()) {

			if (!manager.isServiceAllow(user, serviceName))
				throw new PermissionException("Permission denied.");
		}
	}

	private void validateToken() throws Exception {

		try (AuthorizationManager manager = new AuthorizationManager()) {

			IAuthorization authorize = null;
			AuthorizationTokenResult result = _authorization.processHeader();
			UserDTO user = null;

			if (result.getTokenEnum() == AuthorizationToken.BEARER) {
				authorize = new BearerAuthorization();
				user = authorize.authorize(result.getToken());
			}

			if (result.getTokenEnum() == AuthorizationToken.BASIC) {
				authorize = new BasicAuthorization();
				user = authorize.authorize(result.getToken());
			}

			if (result.getTokenEnum() == AuthorizationToken.OTT) {
				authorize = new OTTAuthorization();
				user = authorize.authorize(result.getToken());
			}

			this.currentUser = user;

		} catch (NoRecordFoundException ex) {
			throw new AuthorizationException("Unauthorized");
		}
	}

	private void validatePermission(ServiceName serviceName) throws Exception {
		try (UserManager manager = new UserManager()) {

			if (!manager.isServiceAllow(this.currentUser.getLoginId().toString(), serviceName))
				throw new PermissionException("Permission denied.");
		}
	}

	protected Response responseFileDownload(File file) throws ValidationException, IOException {

		if (!file.exists()) {
			throw new ValidationException(
					Messages.getString("validation.field.mainMessage", Labels.getString("label.xsell.file.download")));
		}

		InputStream inputStream = new FileInputStream(file);

		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream outputStream) throws IOException {
				try {

					byte[] bytesArray = new byte[4096];
					int bytesRead = -1;
					while ((bytesRead = inputStream.read(bytesArray)) != -1) {
						outputStream.write(bytesArray, 0, bytesRead);
					}

					inputStream.close();
					// Delete file after response
					// file.delete();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		return Response.ok(stream).header("content-disposition", "attachment; filename = " + file.getName()).build();
	}

	protected Response responseDownloadAllFiles(List<DebtHomeAllFile> documentLst, String contractNumber)
			throws ValidationException, IOException {

		String fileName = CacheManager.Parameters().findParamValueAsString(ParametersName.TMP_FOLDER_UPLOAD_DEBT_HOME)
				+ contractNumber + ".zip";

		FileOutputStream fos = new FileOutputStream(fileName);

		ZipOutputStream zipOut = new ZipOutputStream(fos);

		int i = 1;
		for (DebtHomeAllFile item : documentLst) {

			File fileOrigin = new File(item.getFilePath());

			if (!fileOrigin.exists()) {
				zipOut.close();
				fos.close();
				// throw new ValidationException(Messages.getString("validation.not.exists",
				// "File: \"'\"" + item.getFilePath() + "\"'\""));
				System.out.println(
						Messages.getString("validation.not.exists", "File: \"'\"" + item.getFilePath() + "\"'\""));
				continue;
			}

			String tmpFile = "";
			try {
				tmpFile = CacheManager.Parameters().findParamValueAsString(ParametersName.TMP_FOLDER_UPLOAD_DEBT_HOME)
						+ DateUtil.toString(new Date(), "yyyyMMdd") + i + "_" + item.getDocName();
			} catch (ParseException ex) {
				System.out.println("DebtHomeManager.zipDocument.ex.105: " + ex.toString());
			}

			File fileToZip = new File(tmpFile);

			FileUtils.copyFile(fileOrigin, fileToZip);

			FileInputStream fis = new FileInputStream(fileToZip);

			ZipEntry zipEntry = new ZipEntry(fileToZip.getName());

			zipOut.putNextEntry(zipEntry);

			byte[] bytes = new byte[1024];
			int length;
			while ((length = fis.read(bytes)) >= 0) {
				zipOut.write(bytes, 0, length);
			}
			fis.close();

			fileToZip.delete();

			i++;
		}

		zipOut.close();

		fos.close();

		/*
		 * return Response.ok().entity(new StreamingOutput() {
		 * 
		 * @Override public void write(final OutputStream output) throws IOException,
		 * WebApplicationException { File file = null; try { file = new File(fileName);
		 * Files.copy(file, output); } finally { if( file!=null ) file.delete(); } }
		 * }).build();
		 */

		InputStream inputStream = new FileInputStream(new File(fileName));

		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream outputStream) throws IOException {
				try {

					byte[] bytesArray = new byte[4096];
					int bytesRead = -1;
					while ((bytesRead = inputStream.read(bytesArray)) != -1) {
						outputStream.write(bytesArray, 0, bytesRead);
					}

					inputStream.close();
					// Delete file after response
					// file.delete();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		return Response.ok(stream).header("content-disposition", "attachment; filename = " + contractNumber).build();
	}

	protected Response responseExcelFile(File file) throws ValidationException, IOException {
		if (!file.exists()) {
			throw new ValidationException(Messages.getString("audit.excel.report.file.not.exist"));
		}

		InputStream inputStream = new FileInputStream(file);

		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream outputStream) throws IOException {
				try {

					byte[] bytesArray = new byte[4096];
					int bytesRead = -1;
					while ((bytesRead = inputStream.read(bytesArray)) != -1) {
						outputStream.write(bytesArray, 0, bytesRead);
					}

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (null != inputStream) {
						inputStream.close();
					}
					// Delete file after response
					file.delete();
				}
			}
		};

		return Response.ok(stream, "application/vnd.ms-excel")
				.header("content-disposition", "attachment; filename = " + file.getName()).build();
	}

	protected Response dowloadZip(DowloadZipDTO dowloadZipDTO, Boolean isCleanUp, String type)
			throws ValidationException, IOException {
		File file = new File(dowloadZipDTO.getFilePath());

		if (!file.exists()) {
			throw new ValidationException(
					Messages.getString("validation.field.mainMessage", Labels.getString("label.xsell.file.download")));
		}

		ResponseBuilder response = Response.ok(FileUtils.readFileToByteArray(file)).type(type);
		response.header("Content-Disposition", "attachment; filename=\"" + dowloadZipDTO.getFilename() + "\"");

		if (isCleanUp) {
			String pathStr = dowloadZipDTO.getFilePath().replace(Commons.SLASH.value() + dowloadZipDTO.getFilename(),
					"");
			FileUtils.deleteDirectory(new File(pathStr));
		}
		return response.build();
	}
}
