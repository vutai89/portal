package com.mcredit.model.alfresco.object;

import java.io.Serializable;
import java.util.Date;

public class ResultLinkDTO implements Serializable {
	private static final long serialVersionUID = -3519679491599866404L;
	public String objectId;
	public Date expiresDate;
	public String shareId;
	public String linkShare;
	private String contentFrame;

	public ResultLinkDTO(String objectId, Date expiresDate, String shareId, String linkShare, String contentFrame) {
		super();
		this.objectId = objectId;
		this.expiresDate = expiresDate;
		this.shareId = shareId;
		this.linkShare = linkShare;
		this.contentFrame = contentFrame;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public Date getExpiresDate() {
		return expiresDate;
	}

	public void setExpiresDate(Date expiresDate) {
		this.expiresDate = expiresDate;
	}

	public String getShareId() {
		return shareId;
	}

	public void setShareId(String shareId) {
		this.shareId = shareId;
	}

	public String getLinkShare() {
		return linkShare;
	}

	public void setLinkShare(String linkShare) {
		this.linkShare = linkShare;
	}

	public String getContentFrame() {
		return contentFrame;
	}

	public void setContentFrame(String contentFrame) {
		this.contentFrame = contentFrame;
	}

}
