package com.profitles.framwork.cusviews.view.css;

import android.graphics.Color;
import android.view.View;

public class MainCss {
	

	public static final int ReadOnlyBgColor = Color.rgb(231, 231, 231);		//只读背景色
	public static int def_emp_int_value = -1111101; 						//数字为默认空值
	public static int item_name_txtcolor = Color.rgb(182, 250, 242);		//物品名称颜色
	public static int item_docc_txtcolor = Color.rgb(92, 169, 75);			//物品间接字体颜色
	public static int item_money_txtcolor = Color.YELLOW;					//物品金币字体颜色
	public static int item_sklName_txtcolor = Color.YELLOW;					//物品技能名称字体颜色
	public static int item_sklDoc_txtcolor = Color.rgb(108, 197, 205);		//物品技能说明字体颜色
	public static int total_txt_att_high_color = Color.rgb(6, 142, 8);	//统计属性大于0字体颜色
	public static int total_txt_att_zoon_color = Color.rgb(67,151,174); 	//统计属性为0字体颜色
	
	public static void setcss(View v){}
	
	public static void setViewBaseCssByGL(View v, ViewBaseCss vBaseCss){
		android.view.ViewGroup.LayoutParams lp = v.getLayoutParams();
		if(vBaseCss.getWidth() != MainCss.def_emp_int_value){
			lp.width = vBaseCss.getWidth();
		}
		if(vBaseCss.getHeight() != MainCss.def_emp_int_value){
			lp.height = vBaseCss.getHeight();
		}
		if(vBaseCss.getWidth()!= MainCss.def_emp_int_value || vBaseCss.getHeight() != MainCss.def_emp_int_value){
			v.setLayoutParams(lp);
		}
		setViewBaseCssPd(v, vBaseCss);
	}
	
	public static void setViewBaseCssByLL(View v, ViewBaseCss vBaseCss){
		android.widget.LinearLayout.LayoutParams lp = null;
		if(vBaseCss.getWidth() != MainCss.def_emp_int_value){
			lp = new android.widget.LinearLayout.LayoutParams(vBaseCss.getWidth(), vBaseCss.getHeight());
			if(vBaseCss.getMargin() != MainCss.def_emp_int_value){
				lp.setMargins(vBaseCss.getMargin(), vBaseCss.getMargin(), vBaseCss.getMargin(), vBaseCss.getMargin());
			}else{
				lp.setMargins(vBaseCss.getMarginLeft() == MainCss.def_emp_int_value ? 0 : vBaseCss.getMarginLeft(), 
					vBaseCss.getMarginTop() == MainCss.def_emp_int_value ? 0 : vBaseCss.getMarginTop(), 
					vBaseCss.getMarginRight() == MainCss.def_emp_int_value ? 0 : vBaseCss.getMarginRight(), 
					vBaseCss.getMarginBottom() == MainCss.def_emp_int_value ? 0 : vBaseCss.getMarginBottom());
			}
			if(vBaseCss.getWeight() != MainCss.def_emp_int_value){
				lp.weight = vBaseCss.getWeight();
			}
			v.setLayoutParams(lp);
		}
		setViewBaseCssPd(v, vBaseCss);
	}
	
	private static void setViewBaseCssPd(View v, ViewBaseCss vBaseCss){
		if(vBaseCss.getPadding() != MainCss.def_emp_int_value){
			v.setPadding(vBaseCss.getPadding(), vBaseCss.getPadding(), vBaseCss.getPadding(), vBaseCss.getPadding());
		}else{
			v.setPadding(vBaseCss.getPaddingLeft() == MainCss.def_emp_int_value ? 0 : vBaseCss.getPaddingLeft(), 
					vBaseCss.getPaddingTop() == MainCss.def_emp_int_value ? 0 : vBaseCss.getPaddingTop(), 
					vBaseCss.getPaddingRight() == MainCss.def_emp_int_value ? 0 : vBaseCss.getPaddingRight(), 
					vBaseCss.getPaddingBottom() == MainCss.def_emp_int_value ? 0 : vBaseCss.getPaddingBottom());
		}
		if(vBaseCss.getBgColor() != MainCss.def_emp_int_value){
			v.setBackgroundColor(vBaseCss.getBgColor());
		}
		if(vBaseCss.getBackgroundImgId() != MainCss.def_emp_int_value){
			v.setBackgroundResource(vBaseCss.getBackgroundImgId());
		}
	}
}
