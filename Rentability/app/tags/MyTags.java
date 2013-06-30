package tags;

import groovy.lang.Closure;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import models.Category;
import play.templates.FastTags;
import play.templates.GroovyTemplate.ExecutableTemplate;
import controllers.Inventory;

@FastTags.Namespace("mytags")
public class MyTags extends play.templates.FastTags {
	public static void _getSubCate(Map<?, ?> args, Closure body, PrintWriter out, 
		      ExecutableTemplate template, int fromLine) {
		long cateID = Long.parseLong(args.get("arg").toString());
		List<Category> subCates = Inventory.getSubCategories(cateID);
		for(int i = 0; i<subCates.size(); i++){
			out.println("<li><a href=\"/search/"+subCates.get(i).name+"\">"+subCates.get(i).name+"</a></li>");
		}
	}
}
