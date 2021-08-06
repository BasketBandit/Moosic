$(document).ready(function() {
    function connect() {
        ws = new WebSocket('ws://localhost/events');
        ws.onmessage = function(data){
            handleCommand(data.data);
        }
        console.log("Connected");
    }

    function disconnect() {
        if(ws != null) {
            ws.close();
        }
        console.log("Disconnected");
    }

    function send() {
        ws.send();
    }

    function handleCommand(data) {
        switch(data) {
            case "trackStarted":
                $('#active').load("/active");
                $('#queueCollapse').load("/queue");
                break;
            case "queueShuffled":
            case "trackQueued":
            case "trackMoved":
            case "trackRemoved":
                $('#queueCollapse').load("/queue");
                break;
            case "trackEnded":
                $('#active').load("/active");
                $('#historyCollapse').load("/history");
                break;
            case "trackError":
                $("#trackError").fadeIn(100).delay(3000).fadeOut(400);
            default:
            //
        }
    }

    connect();
});