$(document).ready(function() {
    function connect() {
        ws = new WebSocket('ws://localhost:' + location.port +'/events');
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
        if(data.startsWith("t:")) {
            $('.progress-bar').css('width', data.split(':')[1] + '%');
            return;
        }

        switch(data) {
            case "action:play":
                $('#play').load('/controls');
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
                break;
            default:
            //
        }
    }

    $(document).on('click', '#addQueue', function() {
        ws.send("action:load,value:"+$('#addQueueInput').val());
    });

    $('#volume').on('input', function(event) {
        ws.send("action:volume,value:"+$('#volume').val());
    });

    $('#play').click(function(){
        ws.send("action:play");
    });

    $('#skip').click(function(){
        ws.send("action:skip");
    });

    $('#shuffle').click(function(){
        ws.send("action:shuffle");
    });

    $(document).on('click', '#clearQueue', function() {
        ws.send("action:clearQueue");
    });

    $(document).on('click', '#clearHistory', function() {
        ws.send("action:clearHistory");
    });

    $(document).on('click', '.queue', function() {
        ws.send("action:load,value:"+$(this).data('url'));
    });

    $(document).on('click', '.remove', function() {
        ws.send("action:remove,value:"+$(this).data('index'));
    });

    /* Grabbed (haha) from https://jsfiddle.net/7ko9ay1e/ */
    $(document).on('mousedown', '.table-grabable tbody .grabable', function(e) {
        var tr = $(e.target).closest('tr');
        var sy = e.pageY;
        var drag;

        if($(e.target).is('tr')) {
            tr = $(e.target);
        }
        var index = tr.index();
        $(tr).addClass('grabbed');

        function move(e) {
            if(!drag && Math.abs(e.pageY - sy) < 10) {
                return;
            }
            drag = true;
            tr.siblings().each(function() {
                var s = $(this), i = s.index(), y = s.offset().top;
                if(e.pageY >= y && e.pageY < y + s.outerHeight()) {
                    if(i < tr.index()) {
                        s.insertAfter(tr);
                    } else {
                        s.insertBefore(tr);
                    }
                    return false;
                }
            });
        }

        function up(e) {
            if(drag && index != tr.index()) {
              drag = false;
            }
            ws.send("action:move,value:"+$(tr).data('index')+",extra:"+$(tr).index());
            $(document).unbind('mousemove', move).unbind('mouseup', up);
            $(tr).removeClass('grabbed');
        }

        $(document).mousemove(move).mouseup(up);
    });

    connect();
});