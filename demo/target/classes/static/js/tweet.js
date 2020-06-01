function tweet (type,id) {
	if (!window.confirm( ((type == 0)? "新着" : "注目") + "ツイートしますか？") ) {
		return;
	}
	$.ajax({
		url:location.href + "/tweet",
		type:'GET',
		dataType: 'text',
		data:{
			'movieid':id,
			'type' :type
		}
	})
	// Ajaxリクエストが成功した時発動
	.done( (data) => {
		$('.result').html(data);
		console.log(data);
		alert("ツイートに成功");
	})
	// Ajaxリクエストが失敗した時発動
	.fail( (data) => {
		$('.result').html(data);
		console.log(data);
		alert("ツイートに失敗");
	})
	// Ajaxリクエストが成功・失敗どちらでも発動
	.always( (data) => {

	});
}
