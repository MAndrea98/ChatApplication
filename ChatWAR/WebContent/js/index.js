var socket;
var host = "ws://localhost:8080/ChatWAR/ws";
$(document).ready(function() {
	$('#btnPost').click(function() {
		$.ajax({
			url: "rest/chat/post/asdfasdf",
			type: "POST",
			data: {},
			contentType: "application/json",
			dataType: "json",
			complete: function(data) {
				console.log('Sent message to the server.');
			}
		});
	}); 
	
	try {
		socket = new WebSocket(host);
		console.log('conect: Socket Status: ' + socket.readyState);
		
		socket.onopen = function() {
			console.log('onopen: Socket Status: ' + socket.readyState + ' (open)');
		}
		
		socket.onmessage = function(msg) {
			console.log('onmessage: Received: ' + msg.data);
		}
		
		socket.onclose = function() {
			socket = null; 
		}
		
	} catch(exception) {
		console.log('Error' + exception);
	}
});