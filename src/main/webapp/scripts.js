function loadMorePosts() {
    var articles = document.getElementsByTagName("article");
    var minArticleId = articles.item(articles.length-1).id;

    $.ajax({
        type: "POST",
        data: "minArticleId=" + minArticleId,
        url: "/blog/loadMorePosts",
        success: function(result) {
            $('#mainBody').append(result);
        }
    });
}