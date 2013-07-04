package tags;

import groovy.lang.Closure;

import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import models.Category;
import play.i18n.Lang;
import play.i18n.Messages;
import play.templates.FastTags;
import play.templates.GroovyTemplate.ExecutableTemplate;
import tools.StringExtensions;
import controllers.Inventory;

@FastTags.Namespace("mytags")
public class MyTags extends play.templates.FastTags {
	public static void _getSubCate(Map<?, ?> args, Closure body, PrintWriter out, 
		      ExecutableTemplate template, int fromLine) {
		long cateID = Long.parseLong(args.get("arg").toString());
		List<Category> subCates = Inventory.getSubCategories(cateID);
		for(int i = 0; i<subCates.size(); i++){
			out.println("<li><a href=\"/search/"+subCates.get(i).name+"\">"+Messages.get("view.main.subcate."+StringExtensions.joinWithDot(subCates.get(i).name))+"</a></li>");
		}
	}
	
	public static void _getLang(Map<?, ?> args, Closure body, PrintWriter out, 
		      ExecutableTemplate template, int fromLine) {
		if (Lang.getLocale().getLanguage().equals(new Locale("de", "", "").getLanguage())){
			out.println("Deutsch");
		}else {
			out.println("English");
		}
		
	}
}
