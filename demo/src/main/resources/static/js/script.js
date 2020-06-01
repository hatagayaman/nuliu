function selectPage(obj) {
	var idx = obj.selectedIndex;
	var value = obj.options[idx].value;
	var url = location.href;
	var p = url.split("?");
	if (p.length == 1) {
		url += "?page=" + value;
	} else {
		for (var i = 0; i < p.length; i++) {
			// 前方一致
			if (p[i].indexOf("page=") === 0) {
				p[i] = "page=" + value;
				break;
			}
		}
		url = p.join("?");
	}
	location.href = url;
}