var timeout;
var page = 1;
function loadMorePosts() {
    if (timeout)
        clearTimeout(timeout);
    
    $('#loadMorePosts').popover('hide')
    var articles = document.getElementsByTagName('article');
    var minArticleId = articles.item(articles.length-1).id;
    var startingPage = parseInt($('#articles').attr('startingPage'));
    
    $.ajax({
        type: 'POST',
        data: 'minArticleId=' + minArticleId,
        url: '/blog/loadMorePosts',
        success: function(result) {
            if (!$.trim(result)) {
                $('#loadMorePosts').popover('show')
                timeout = setTimeout(function(){$('#loadMorePosts').popover('hide')}, 1000);
            }
            else {
                $('#articles').append(result);
                history.replaceState(null, '', '/blog/' + (startingPage + page++));
            }
        }
    });
}

$(function () {
    $('[data-toggle="popover"]').popover({trigger: 'manual'})
    
    $('#loadMorePosts').click(function() {
        loadMorePosts();
    });
    
    var textarea = document.getElementById('body');
    if (textarea != null) {
        sceditor.create(textarea, {
            format: 'bbcode',
            style: 'minified/themes/content/default.min.css',
            toolbar: 'bold,italic,underline,removeformat,link,unlink,code,image',
            width: '100%',
            resizeWidth: 'False'
        });
    }
})