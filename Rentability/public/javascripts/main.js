$(document).ready(function(){
	// activate the OBSM sub-menu icon
	$('.obsm').obsm();
	$('.sublan').click(function () {
	    $('#language').text($(this).text());
	});
});


