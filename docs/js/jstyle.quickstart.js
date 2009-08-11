//add code style
jStyle.addStyle("code");
jStyle.styles.code.filter="*";
jStyle.styles.code.disabled=false;
jStyle.styles.code.render=function(style,element)//render jstyle
{    var HTMLEncode= function (str){  
			 var s = "";
			 if(str.length == 0) return "";
			 s    =    str.replace(/&/g,"&amp;");
			 s    =    s.replace(/</g,"&lt;");
			 s    =    s.replace(/>/g,"&gt;");
			 s    =    s.replace(/ /g,"&nbsp;");
			 s    =    s.replace(/\'/g,"&#39;");
			 s    =    s.replace(/\"/g,"&quot;");
			 return   s;  
	   } ;
	    var source_element;
		source_element=cs$.find("#"+style.source)[0];
		if(source_element) code_element_innerHTML=source_element.innerHTML;
		else {code_element_innerHTML="Can not find the source!";}
		code_element_innerHTML=HTMLEncode(code_element_innerHTML);
		code_element_innerHTML=code_element_innerHTML.replace(/\n/mg,"<br/>");
		element.innerHTML=code_element_innerHTML;
};//render

var __chapter_index=0;
//add chapter style
jStyle.addStyle("chapter");
jStyle.styles.chapter.filter="*";
jStyle.styles.chapter.disabled=false;
jStyle.styles.chapter.render=function(style,element)//render jstyle
{ 
	if(!style) return false;
	var table_conetens=cs$.find("#table_conetens")[0];
	cs$(element).cls("+","chapter");
	cs$(table_conetens).mp("+>","<li class='table_conetens_item'><a href=\"#chapter_"+__chapter_index+"\">"+element.innerHTML+"</a></li>");
	cs$(element).mp("+<","<A NAME=chapter_"+__chapter_index+" />");
	cs$(element).mp(">+","<A href=\"#table_conetens_anchor\" class='back_to_table_conetens'>Back to table contents.</a>");
	__chapter_index++;
};//render