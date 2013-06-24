package controllers;

import controllers.elasticsearch.ElasticSearchController;
import models.Offer;
import play.mvc.*;

@ElasticSearchController.For(Offer.class)
public class SearchAdmin extends ElasticSearchController {

}
