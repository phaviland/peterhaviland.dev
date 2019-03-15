function loadMorePosts() {
    var articles = document.getElementsByTagName("article");
    var minArticleId = articles.item(articles.length-1).id;

    $.ajax({
        type: "POST",
        data: "minArticleId=" + minArticleId,
        url: "/blog/loadMorePosts",
        success: function(result) {
            $('#articles').append(result);
			//ajaxPending = false;
        }
    });
}

/*
var ajaxPending = false;
$(window).scroll(function() { 
	if (!ajaxPending && $(window).scrollTop() >= $(document).height() - $(window).height() - 50) {
		ajaxPending = true;
		loadMorePosts();
	}
});
*/