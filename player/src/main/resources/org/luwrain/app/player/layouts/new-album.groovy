
def title = ""

wizard 'Подключение электронной почты', 'greeting', {
def s = getStrings()

frame 'greeting', {
text s.wizardNewAlbumIntro()
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
text s.wizardNewAlbumDirIntro(title)
input 'dir-path', s.wizardNewAlbumDirPath()
button s.wizardNewAlbumCreate(), { values ->
def path = values.getText(0).trim()
if (path.isEmpty())
{
error s.wizardNewAlbumDirPathCannotBeEmpty()
return
}
addDirAlbum(title, path);
}
}
}


