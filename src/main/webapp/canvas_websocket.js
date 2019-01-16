//canvas stuff
var canvas, ctx, flag = false,
        prevX = 0,
        currX = 0,
        prevY = 0,
        currY = 0,
        dot_flag = false;

var x = "black",
        y = 2;

var prevBorderedColor;
function init() {
    canvas = document.getElementById("can");
    ctx = canvas.getContext("2d");
    w = canvas.width;
    h = canvas.height;
    document.getElementById("black").style.border = "2px solid purple";
    prevBorderedColor = "black";
    canvas.addEventListener("mousemove", function (e) {
        findxy("move", e);
    }, false);
    canvas.addEventListener("mousedown", function (e) {
        findxy("down", e);
    }, false);
    canvas.addEventListener("mouseup", function (e) {
        findxy("up", e);
    }, false);
    canvas.addEventListener("mouseout", function (e) {
        findxy("out", e);
    }, false);
}

function color(obj) {
    var colorTextHolder = document.getElementById("currentColor");
    switch (obj.id) {
        case "green":
            x = "green";
            break;
        case "blue":
            x = "blue";
            break;
        case "red":
            x = "red";
            break;
        case "yellow":
            x = "yellow";
            break;
        case "orange":
            x = "orange";
            break;
        case "black":
            x = "black";
            break;
    }
    colorTextHolder.innerHTML = "Current color - " + x;
    document.getElementById(x).style.border = "2px solid purple";
    document.getElementById(prevBorderedColor).style.border = "";
    prevBorderedColor = x;
}


function erase() {
    ctx.clearRect(0, 0, w, h);
    sendEraseMsg();
}


var sentReqs = 0;
function findxy(res, e) {
    if (res === "down") {
        prevX = currX;
        prevY = currY;
        currX = e.clientX - canvas.offsetLeft;
        currY = e.clientY - canvas.offsetTop;
        flag = true;
    }
    if (res === "up" || res === "out") {
        flag = false;
    }
    if (res === "move") {
        if (flag) {
            prevX = currX;
            prevY = currY;
            currX = e.clientX - canvas.offsetLeft;
            currY = e.clientY - canvas.offsetTop;
            respDraw(prevX, prevY, currX, currY, x, y);
            send(username, prevX, prevY, currX, currY, x, res);
        }
    }

}


//websocket stuff
var ws;
var username;
var usersNum = 0;

function connect() {
    username = document.getElementById("username").value;
    ws = new WebSocket("ws://" + window.location.host + window.location.pathname + "join/" + username);
    ws.onmessage = function (event) {
        var message = JSON.parse(event.data);
        if (message.res === "erase") {
            ctx.clearRect(0, 0, w, h);
        } else if (message.res === "connected") {
            usersNum++;
            document.getElementById("playersList").innerHTML += "<li id = \"u" + message.from + "\">" + message.from + "</li>";
        } else if (message.res === "disconnected") {
            console.log("disconnecting : " + message.from);
            usersNum--;
            document.getElementById("u" + message.from).remove();
        } else {
            respDraw(message.prevX, message.prevY, message.currX, message.currY, message.color);
        }
    };
    document.getElementById("username").disabled = true;
    document.getElementById("connectBtn").disabled = true;
}

function disconnect() {
    ws.close();
}

function respDraw(respPrevX, respPrevY, respCurrX, respCurrY, respColor, respLineWidth) {
    ctx.beginPath();
    ctx.moveTo(respPrevX, respPrevY);
    ctx.lineTo(respCurrX, respCurrY);
    ctx.strokeStyle = respColor;
    ctx.lineWidth = respLineWidth;
    ctx.stroke();
    ctx.closePath();
}


function send(respFrom, respPrevX, respPrevY, respCurrX, respCurrY, respColor, respRes) {
    if (typeof ws !== 'undefined') {
        var json = JSON.stringify({
            "from": respFrom,
            "prevX": respPrevX,
            "prevY": respPrevY,
            "currX": respCurrX,
            "currY": respCurrY,
            "color": respColor,
            "res": respRes
        });
        ws.send(json);
    }
}

function sendEraseMsg() {
    if (typeof ws !== 'undefined') {
        send(null, null, null, null, null, null, "erase");
    }
}

function resetMouse() {
    flag = false;
    prevX = 0;
    currX = 0;
    prevY = 0;
    currY = 0;
    dot_flag = false;
}
