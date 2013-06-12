import models.Category;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;


@OnApplicationStart
public class Bootstrap extends Job {

	public void doJob(){
		if(Category.count() == 0){
		Fixtures.loadModels("categories.yml");
		}
	}
	
}
