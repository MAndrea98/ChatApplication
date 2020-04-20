$(document).ready(function() {
	let data = localStorage.getItem('profile');
	document.getElementById("profile_username").innerHTML = "@" + data;
	allUsers();
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