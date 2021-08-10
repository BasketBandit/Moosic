$(document).ready(function() {
    $('#play').click(function(){
        $.post('action', { parameter: 'play' }, function(data, status){
            $('#play').load('/controls');
        });
    });

    $('#skip').click(function(){
        $.post('action', { parameter: 'skip' }, function(data, status) {});
    });

    $('#shuffle').click(function(){
        $.post('action', { parameter: 'shuffle' }, function(data, status) {});
    });

    $('#volume').on('input', function(event) {
        $.post('action', { parameter: 'volume', value: $('#volume').val() }, function(data, status) {});
    });

    function refreshProgress() {
       $('#progress').load('/progress');
       setTimeout(refreshProgress, 100)
    }
    refreshProgress();
});