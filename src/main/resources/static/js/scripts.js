$(document).ready(function() {
    $("#play").click(function(){
        $.post("action", { parameter: "play" }, function(data, status){});
    });

    $("#skip").click(function(){
        $.post("action", { parameter: "skip" }, function(data, status){});
    });

    $("#pause").click(function(){
        $.post("action", { parameter: "pause" }, function(data, status){});
    });

    $("#clearQueue").click(function(){
        $.post("action", { parameter: "clearQueue" }, function(data, status){});
    });

    $("#clearHistory").click(function(){
        $.post("action", { parameter: "clearHistory" }, function(data, status){});
    });

    $("#queueCollapseButton").click(function(event) {
        event.target.classList.toggle('down');
    });

    $("#historyCollapseButton").click(function(event) {
        event.target.classList.toggle('down');
    });

    $("#volume").on("input", function(event) {
        $.post("action", { parameter: "volume", value: $("#volume").val() }, function(data, status){});
    });

    $(document).on('click', '.requeue', function(){
        $.post("load", { url: $(this).data('url') }, function(data, status){});
    });

    function refreshData(){
        $('#queueCollapse').load("/queue");
        $('#historyCollapse').load("/history");
        $('#current').load("/current");
        setTimeout(refreshData, 1000);
    }
    refreshData();

    function refreshProgress() {
       $('#progress').load("/progress");
       setTimeout(refreshProgress, 100)
    }
    refreshProgress();
});