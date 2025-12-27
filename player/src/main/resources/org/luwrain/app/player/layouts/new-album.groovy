
wizard 'Подключение электронной почты', 'greeting', {


frame 'greeting', {
text getStrings().newAlbumIntro()
input 'album-title', getStrings().wizardNewAlbumTitle()
button getStrings().wizardNewAlbumDir(), { values ->
}
button getStrings().wizardNewAlbumPlaylist(), { values ->
}
button getStrings().wizardNewAlbumStream(), { values ->
}


}
}


