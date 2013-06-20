function calculateAdjustedPrice(pricePerDay) {

  if (document.getElementById("startTime").value != "" && document.getElementById("endTime").value != "") {

		var startDate = new Date(document.getElementById("startTime").value);
		var endDate = new Date(document.getElementById("endTime").value);
		
		var timeDiff = Math.abs(endDate.getTime() - startDate.getTime());
		var diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24)) + 1; 
		
		document.getElementById("calculatedPrice").value = roundPrice((diffDays * pricePerDay), 2);
	}
}

function roundPrice(x, n) {
  if (n < 1 || n > 14) return false;
  var e = Math.pow(10, n);
  var k = (Math.round(x * e) / e).toString();
  if (k.indexOf('.') == -1) k += '.';
  k += e.toString().substring(1);
  return k.substring(0, k.indexOf('.') + n+1);
}
