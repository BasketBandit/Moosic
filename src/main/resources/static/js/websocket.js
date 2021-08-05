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
            case "trackMoved":
                $('#queueCollapse').load("/queue");
                break;
            case "trackEnded":
                $('#historyCollapse').load("/history");
                break;
            default:
            //
        }
    }

    connect();
});