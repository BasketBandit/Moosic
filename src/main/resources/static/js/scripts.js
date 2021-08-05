$(document).ready(function() {
    $("#play").click(function(){
        $.post("action", { parameter: "play" }, function(data, status){
            $('#play').load("/controls");
        });
    });

    $("#skip").click(function(){
        $.post("action", { parameter: "skip" }, function(data, status) {});
    });

    $("#shuffle").click(function(){
        $.post("action", { parameter: "shuffle" }, function(data, status) {});
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

    $(document).on('click', '.queue', function(){
        $.post("load", { url: $(this).data('url') }, function(data, status){
            $('#queueCollapse').load("/queue");
        });
    });

    $(document).on('click', '.remove', function(){
        $.post("action", { parameter: "remove", value: $(this).data('index') }, function(data, status){
            $('#queueCollapse').load("/queue");
        });
    });

    function refreshProgress() {
       $('#progress').load("/progress");
       setTimeout(refreshProgress, 100)
    }
    refreshProgress();
});