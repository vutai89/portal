package com.mcredit.model.object.system;

import java.io.Serializable;
import java.util.ArrayList;

public class BPMDocument implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String folderName;
	private String path;
	ArrayList<BPMDocumentFile> files = new ArrayList<BPMDocumentFile>();

	public ArrayList<BPMDocumentFile> getFiles() {
		return files;
	}

	public void setFiles(ArrayList<BPMDocumentFile> files) {
		this.files = files;
	}

	public String getFolderName() {
		return folderName;
	}

	public String getPath() {
		return path;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
