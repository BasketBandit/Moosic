$(document).ready(function() {
    $("#play").click(function(){
        $.post("action", { value: "play" }, function(data, status){});
    });

    $("#skip").click(function(){
        $.post("action", { value: "skip" }, function(data, status){});
    });

    $("#stop").click(function(){
        $.post("action", { value: "stop" }, function(data, status){});
    });

    $("#clear-queue").click(function(){
        $.post("action", { value: "clear-queue" }, function(data, status){});
    });

    $("#clear-history").click(function(){
        $.post("action", { value: "clear-history" }, function(data, status){});
    });

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