import java.util.List;

import models.*;
import play.db.DB;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;


@OnApplicationStart
public class Bootstrap extends Job {

	public void doJob(){
		if(Category.count() == 0){
		Fixtures.loadModels("categories.yml");
		}
		
		if(User.count() == 0){
			Fixtures.loadModels("users.yml");
			}
		
		if(Article.count() == 0){
			Fixtures.loadModels("articles.yml");
			
			  List<Article> allArticles = Article.findAll();
	            for (Article a: allArticles){
	                DB.execute("UPDATE `Article` SET image='item_" + a.name.toLowerCase() + ".jpg|image/jpg' WHERE id=" + a.getId());
	            }
			
			
			
		}
		
		if(Offer.count() == 0){
			
			Fixtures.loadModels("offers.yml");
			
			
		}
		
		
	}
	
}
