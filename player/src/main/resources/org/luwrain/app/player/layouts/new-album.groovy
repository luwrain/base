
def title = ''

wizard 'Подключение электронной почты', 'greeting', {


frame 'greeting', {
text getStrings().wizardNewAlbumIntro()
input 'album-title', getStrings().wizardNewAlbumTitle()
button getStrings().wizardNewAlbumDir(), { values ->
title = values.getText(0)
if (title.isEmpty())
{
error getStrings().wizardNewAlbumTitleCannotBeEmpty()
return 
}
show 'dir'
}

button getStrings().wizardNewAlbumPlaylist(), { values ->
title = values.getText(0)
if (title.isEmpty())
{
error getStrings().wizardNewAlbumTitleCannotBeEmpty()
return 
}
}

button getStrings().wizardNewAlbumStream(), { values ->
title = values.getText(0)
if (title.isEmpty())
{
error getStrings().wizardNewAlbumTitleCannotBeEmpty()
return 
}

}


}

frame 'dir', {
input 'dir-path', 'kakak'
}
}


