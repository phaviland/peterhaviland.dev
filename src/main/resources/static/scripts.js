var timeout;
function loadMorePosts() {
    if (timeout)
        clearTimeout(timeout);
    
    $('#loadMorePosts').popover('hide')
    var articles = document.getElementsByTagName("article");
    var minArticleId = articles.item(articles.length-1).id;

    $.ajax({
        type: "POST",
        data: "minArticleId=" + minArticleId,
        url: "/blog/loadMorePosts",
        success: function(result) {
            if (!$.trim(result)){
                $('#loadMorePosts').popover('show')
                timeout = setTimeout(function(){$('#loadMorePosts').popover('hide')},1000);
            }
            else {
                $('#articles').append(result);
            }
        }
    });
}

$(function () {
    $('[data-toggle="popover"]').popover({trigger: "manual"})
    
    $("#loadMorePosts").click(function() {
        loadMorePosts();
    });
})

/*
var ajaxPending = false;
$(window).scroll(function() { 
    if (!ajaxPending && $(window).scrollTop() >= $(document).height() - $(window).height() - 50) {
        ajaxPending = true;
        loadMorePosts();
    }
});
*/