$(document).ready(function() {
    $('#historyCollapseButton').click(function(event) {
        event.target.classList.toggle('down');
    });

    $(document).on('click', '#clearHistory', function() {
        $.post('action', { parameter: 'clearHistory' }, function(data, status){});
    });
});