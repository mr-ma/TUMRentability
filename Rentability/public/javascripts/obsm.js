/**
 * Author: Vee w.
 * 
 * Licensed under GNU/GPL v3.0
 * http://www.gnu.org/licenses/gpl-3.0.html
 */

jQuery.fn.obsm = function( options ) {
	
	// Create some defaults, extending them with any options that were provided
	var settings = jQuery.extend({
		'arrow_class' : 'arrow'
	}, options);
	
	return this.each(function() {
		$(' ul', this).each(function() {
			var $this = $(this);
			if ( $this.children().length > 0 ) {
				$this.parent().addClass( settings.arrow_class );
				
			}
		});
		
		$(' li',this).click(function() {
			if ( $(this).hasClass( 'expand' ) ) {
				$(this).removeClass( 'expand' );
			} else {
				$(this).addClass( 'expand' );
			}
		});
	});
	
}