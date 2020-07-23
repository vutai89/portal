package com.mcredit.model.object;

public class CodeCategory {

	private String codeGroup;

	private String category;
	
	private int productCatId;

	private int productGroupId;

	private int productId;

	private int startIndex;

	private int endIndex;
	
	public CodeCategory(String cdGrp, String cat, int sIdx) {
		this.codeGroup = cdGrp;
		this.category = cat;
		this.startIndex = sIdx;
	}

	public CodeCategory(String cdGrp, String cat, int prodId, int prodGrp, int prodCat, int sIdx) {
		this.codeGroup = cdGrp;
		this.category = cat;
		this.productId = prodId;
		this.productGroupId = prodGrp;
		this.productCatId = prodCat;
		this.startIndex = sIdx;
	}

	public String getCodeGroup() {
		return codeGroup;
	}

	public void setCodeGroup(String codeGroup) {
		this.codeGroup = codeGroup;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	public int getProductCatId() {
		return productCatId;
	}

	public void setProductCatId(int productCatId) {
		this.productCatId = productCatId;
	}

	public int getProductGroupId() {
		return productGroupId;
	}

	public void setProductGroupId(int productGroupId) {
		this.productGroupId = productGroupId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}
	
}
