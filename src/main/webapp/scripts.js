function loadMorePosts() {
    console.log('hello world');
    var totalPostsOnScreen = document.getElementsByTagName("article").length;
    $.ajax({
        type: "POST",
        data: "offset=" + totalPostsOnScreen,
        url: "/blog/loadMorePosts",
        success: function(result) {
            $('#mainBody').append(result);
        }
    });
}