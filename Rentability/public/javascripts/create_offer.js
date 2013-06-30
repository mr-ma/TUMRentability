

$(document).ready(function(){
	$('#maincates').change(function() {
		alert("selected");
		  $('#txt').val($(this).find(":selected").text());
	});
}
