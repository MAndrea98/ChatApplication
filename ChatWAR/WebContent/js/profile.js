var socket;
var host = "ws://localhost:8080/ChatWAR/ws";
$(document).ready(function() {
	let user = JSON.parse(localStorage.getItem('user'));
	document.getElementById("profile_username").innerHTML = "@" + user.username;
	allUsers();
	allMessages();
	
	try {
		socket = new WebSocket(host);
		console.log('conect: Socket Status: ' + socket.readyState);
		
		socket.onopen = function() {
			console.log('onopen: Socket Status: ' + socket.readyState + ' (open)');
		}
		
		socket.onmessage = function(msg) {
			console.log('onmessage: Received: ' + msg.data);
			allUsers();
			allMessages();
		}
		
		socket.onclose = function() {
			socket = null; 
		}
		
	} catch(exception) {
		console.log('Error' + exception);
	}
});

function logout() {
	let logouter = document.getElementById("profile_username").innerHTML;
	logouter = logouter.substring(1);
	$.ajax({
		url: "rest/chat/loggedIn/" + logouter,
		type: "DELETE",
		success: function() {
			localStorage.removeItem('user');
			window.location = './login.html';	
		}
	});
}

function addActiveUsers(username, j) {
	let user = $('<div class="media">' +
                    '<div class="media-left media-middle">' +
                        '<a href="#"><img class="media-object img-circle img-thumbnail thumb48" src="https://bootdey.com/img/Content/avatar/avatar'+j+'.png" alt="Contact"></a>'+
                    '</div>'+
                    '<div class="media-body pt-sm">'+
                        '<div class="text-bold">@'+ username +
                            '<div class="text-sm text-muted">Active now</div>'+
                        '</div>'+
                   '</div>'+
                '</div>');
	$('#users_panel').append(user);
	
}

function addUsers(username) {
	let option = $('<option>@'+username+'</option>');
	$('#select').append(option);
} 

function removeOptions(selectElement) {
    let i, L = selectElement.options.length - 1;
    for(i = L; i >= 0; i--) {
       selectElement.remove(i);
    }
}

function allUsers() {
	$.ajax({
		url: "rest/chat/registered",
		type: "GET",
		success: function(users) {
			removeOptions(document.getElementById('select'));
			let option = $('<option>All users</option>');
			$('#select').append(option);
			for (let i = 0; i < users.length; i++) {
				let k = i % 8 + 1;
				addUsers(users[i].username);
			}
		},
		error : function () {
			alert('Error');
		}
	});
	
	$.ajax({
		url: "rest/chat/loggedIn",
		type: "GET",
		success: function(loggedIn) {
			let element = document.getElementById("users_panel");
			while (element.firstChild) {
				element.removeChild(element.firstChild);
			}
			for (let i = 0; i < loggedIn.length; i++) {
				let k = i % 8 + 1;
				addActiveUsers(loggedIn[i].username, k);
				
			}
		}
	});
}

function sendMessage() {
	let text = $('#message_to_all_text').val();
	let username = document.getElementById("profile_username").innerHTML;
	username = username.substring(1);
	$.ajax({
		url: "rest/chat/get_user/" + username,
		type: "GET",
		success: function(sender) {
			let receiver = $('#select').val();
			if(receiver == "All users") {
				$.ajax({
					url: "rest/messages/all",
					type: "POST",
					data: JSON.stringify({text, sender}),
					contentType: "application/json",
					success: function() {
						alert('Sent');
						allMessages();
					},
					error: function() {
						alert('Error');
					}
				});
			}
			else {
				receiver = receiver.substring(1);
				$.ajax({
					url: "rest/messages/" + receiver,
					type: "POST",
					data: JSON.stringify({text, sender}),
					contentType: "application/json",
					success: function() {
						alert('Sent');
						allMessages();
					},
					error: function() {
						alert('Error');
					}
				});
			}
		},
		error: function() {
			alert('Error');
		}
	});
	
	
}



function showAll(message) {
	let time = new Date().getTime();
	let date = new Date(message.date);
	let message_post = $('<div class="panel-heading no-border"> ' +
		    '<div class="pull-left half"> ' +
	        '<div class="media"> ' +
	         '   <div class="media-object pull-left"> ' +
	          '      <img src="https://bootdey.com/img/Content/avatar/avatar7.png" alt="..." class="img-circle img-post"> ' + 
	           ' </div> ' +
	            '<div class="media-body"> ' +
	             '   <a href="#" class="media-heading block mb-0 h4 text-white">@'+message.sender.username+'</a> ' +
	              '  <span class="text-white h6">'+date.toString()+'</span> ' +
	            '</div> ' +
	        '</div> ' +
	    '</div> ' +
	   
	    '<div class="clearfix"></div> ' +
	'</div> ' +
	'<div class="panel-body no-padding"> ' +
	 '   <div class="inner-all block"> ' +
	 '      <h4>'+message.subject+'</h4> ' +
	   '     <p>'+message.text +       
	    '    </p> ' +
	       
	   ' </div> ' +
	    
	'</div> ' +
	'<hr class="new1">'); 

	$('#messages_timeline').append(message_post);
}

function allMessages() {
	let username = document.getElementById("profile_username").innerHTML;
	username = username.substring(1);
	$.ajax({
		url: "rest/messages/" + username,
		type: "GET",
		success: function(messages) {
			let element = document.getElementById("messages_timeline");
			while (element.firstChild) {
				element.removeChild(element.firstChild);
			}
			for (let message of messages) {
				showAll(message);
			}	
		},
		error: function() {
			alert('Error');
		}
	});
}

function proba() {
	$.ajax({
		url: "rest/messages/test",
		type: "GET",
		success: function() {
			alert('OK');
		},
		error: function() {
			alert('Greska');
		}
	});
}

