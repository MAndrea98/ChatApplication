$(document).ready(function() {
	let user = JSON.parse(localStorage.getItem('user'));
	document.getElementById("profile_username").innerHTML = "@" + user.username;
	allUsers();
	allMessages();
});

function logout() {
	let data = localStorage.getItem('profile');
	$.ajax({
		url: "rest/chat/loggedIn/" + data,
		type: "DELETE",
		success: function() {
			localStorage.clear();
			window.location = './login.html';	
		}
	});
}

function addUser(username, j, active) {
	let user = $('<div class="media">' +
                    '<div class="media-left media-middle">' +
                        '<a href="#"><img class="media-object img-circle img-thumbnail thumb48" src="https://bootdey.com/img/Content/avatar/avatar'+j+'.png" alt="Contact"></a>'+
                    '</div>'+
                    '<div class="media-body pt-sm">'+
                        '<div class="text-bold">@'+ username +
                            '<div class="text-sm text-muted">'+active+'</div>'+
                        '</div>'+
                   '</div>'+
                '</div>');
	$('#users_panel').append(user);
	let option = $('<option>@'+username+'</option>');
	$('#select').append(option);
}

function allUsers() {
	$.ajax({
		url: "rest/chat/registered",
		type: "GET",
		success: function(users) {
			$.ajax({
				url: "rest/chat/loggedIn",
				type: "GET",
				success: function(loggedIn) {
					let element = document.getElementById("users_panel");
					while (element.firstChild) {
						element.removeChild(element.firstChild);
					}
					for (let i = 0; i < users.length; i++) {
						for (let j = 0; j < loggedIn.length; j++) {
							let k = i % 8 + 1;
							if (loggedIn[j].username == users[i].username) {
								addUser(users[i].username, k, "Active now");
								continue;
							}
							else {
								addUser(users[i].username, k, "Offline");
							}
						}
						
					}
				}
			});
		},
		error: function() {
			alert('Desila se greska');
		}
	});
}

function sendMessage() {
	let text = $('#message_to_all_text').val();
	let sender = JSON.parse(localStorage.getItem('user'));
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
				alert('Greska');
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
				alert('Greska');
			}
		});
	}
	
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
	let user = JSON.parse(localStorage.getItem('user'));
	$.ajax({
		url: "rest/messages/" + user.username,
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
			alert('Greska');
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