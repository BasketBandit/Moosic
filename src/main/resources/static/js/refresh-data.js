$(document).ready(function() {
    var queue = document.querySelector('#queueCollapseButton');
    queue.addEventListener('click', function(event) {
      event.target.classList.toggle('down');
    });

    var history = document.querySelector('#historyCollapseButton');
    history.addEventListener('click', function(event) {
      event.target.classList.toggle('down');
    });

    function refreshData(){
        $('#queue').load("/queue");
        $('#history').load("/history");
        $('#status').load("/status");
        setTimeout(refreshData, 1000);
    }
    refreshData();
});