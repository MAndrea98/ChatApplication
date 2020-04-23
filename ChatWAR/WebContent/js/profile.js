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