package com.profitles.framwork.cusviews.view.css;

import com.profitles.framwork.BaseObject;

public class ViewBaseCss extends BaseObject {

	private int width = MainCss.def_emp_int_value;
	private int height = MainCss.def_emp_int_value;
	private int padding = MainCss.def_emp_int_value;
	private int paddingLeft = MainCss.def_emp_int_value;
	private int paddingRight = MainCss.def_emp_int_value;
	private int paddingTop = MainCss.def_emp_int_value;
	private int paddingBottom = MainCss.def_emp_int_value;
	private int margin = MainCss.def_emp_int_value;
	private int marginLeft = MainCss.def_emp_int_value;
	private int marginRight = MainCss.def_emp_int_value;
	private int marginTop = MainCss.def_emp_int_value;
	private int marginBottom = MainCss.def_emp_int_value;
	private int backgroundImgId = MainCss.def_emp_int_value;
	private int parenBackgroundImgId = MainCss.def_emp_int_value;
	private float fontSize = MainCss.def_emp_int_value;
	private int textColor = MainCss.def_emp_int_value;
	private int bgColor = MainCss.def_emp_int_value;
	private int gravity = MainCss.def_emp_int_value;
	private int imgWidth = MainCss.def_emp_int_value;
	private int imgHeight = MainCss.def_emp_int_value;
	private int[] bgColors = null;
	private int[] textColors = null;
	private float weight = MainCss.def_emp_int_value;
	
	public ViewBaseCss(){ }
	
	public ViewBaseCss(int width, int height){ 
		this.width = width;
		this.height = height;
	}
	
	public ViewBaseCss(int width, int height, int paddingLeft, int paddingTop, int paddingRight, int paddingBottom){
		this(width, height);
		this.paddingLeft = paddingLeft;
		this.paddingTop = paddingTop;
		this.paddingRight = paddingRight;
		this.paddingBottom = paddingBottom;
	}
	
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getPadding() {
		return padding;
	}
	public ViewBaseCss setPadding(int padding) {
		this.padding = padding;
		return this;
	}
	public int getPaddingLeft() {
		return paddingLeft;
	}
	public ViewBaseCss setPaddingLeft(int paddingLeft) {
		this.paddingLeft = paddingLeft;
		return this;
	}
	public int getPaddingRight() {
		return paddingRight;
	}
	public ViewBaseCss setPaddingRight(int paddingRight) {
		this.paddingRight = paddingRight;
		return this;
	}
	public int getPaddingTop() {
		return paddingTop;
	}
	public ViewBaseCss setPaddingTop(int paddingTop) {
		this.paddingTop = paddingTop;
		return this;
	}
	public int getPaddingBottom() {
		return paddingBottom;
	}
	public void setPaddingBottom(int paddingBottom) {
		this.paddingBottom = paddingBottom;
	}
	public int getMargin() {
		return margin;
	}
	public void setMargin(int margin) {
		this.margin = margin;
	}
	public int getMarginLeft() {
		return marginLeft;
	}
	public void setMarginLeft(int marginLeft) {
		this.marginLeft = marginLeft;
	}
	public int getMarginRight() {
		return marginRight;
	}
	public void setMarginRight(int marginRight) {
		this.marginRight = marginRight;
	}
	public int getMarginTop() {
		return marginTop;
	}
	public void setMarginTop(int marginTop) {
		this.marginTop = marginTop;
	}
	public int getMarginBottom() {
		return marginBottom;
	}
	public void setMarginBottom(int marginBottom) {
		this.marginBottom = marginBottom;
	}

	public int getBackgroundImgId() {
		return backgroundImgId;
	}

	public ViewBaseCss setBackgroundImgId(int backgroundImgId) {
		this.backgroundImgId = backgroundImgId;
		return this;
	}

	public int getParenBackgroundImgId() {
		return parenBackgroundImgId;
	}

	public ViewBaseCss setParenBackgroundImgId(int parenBackgroundImgId) {
		this.parenBackgroundImgId = parenBackgroundImgId;
		return this;
	}

	public float getWeight() {
		return weight;
	}

	public ViewBaseCss setWeight(float weight) {
		this.weight = weight;
		return this;
	}

	public float getFontSize() {
		return fontSize;
	}

	public ViewBaseCss setFontSize(float fontSize) {
		this.fontSize = fontSize;
		return this;
	}

	public int getBgColor() {
		return bgColor;
	}

	public ViewBaseCss setBgColor(int bgColor) {
		this.bgColor = bgColor;
		return this;
	}

	public int getGravity() {
		return gravity;
	}

	public ViewBaseCss setGravity(int gravity) {
		this.gravity = gravity;
		return this;
	}

	public int getImgWidth() {
		return imgWidth;
	}

	public ViewBaseCss setImgWidth(int imgWidth) {
		this.imgWidth = imgWidth;
		return this;
	}

	public int getImgHeight() {
		return imgHeight;
	}

	public ViewBaseCss setImgHeight(int imgHeight) {
		this.imgHeight = imgHeight;
		return this;
	}

	public int[] getBgColors() {
		return bgColors;
	}

	public ViewBaseCss setBgColors(int[] bgColors) {
		this.bgColors = bgColors;
		return this;
	}

	public int[] getTextColors() {
		return textColors;
	}

	public ViewBaseCss setTextColors(int[] textColors) {
		this.textColors = textColors;
		return this;
	}

	public int getTextColor() {
		return textColor;
	}

	public ViewBaseCss setTextColor(int textColor) {
		this.textColor = textColor;
		return this;
	}
}
