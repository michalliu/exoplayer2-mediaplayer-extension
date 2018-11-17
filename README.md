# Who should this extension?
Since android mediaplayer has lots of problems. If you wish to swtiching the player from mediaplayer to ExoPlayer seamlessly, you are at the right place.

# What the extension does?
ExoPlayer has a totally new API design. If you already using mediaplayer, you need to change a lot of code in order to use ExoPlayer.
This extension does the heavy work for you. It provides the same API,callbacks,lifecycle with android mediaplayer with exoplayer behind the scenes.
It makes developers using exoplayer in their project much easier.

# The dependencies
No Dependency except Exoplayer.  
Tested on ExoPlayer 2.7.1 (the newest at the time of writting), compatible with newer version therotical.

# Show me the code
This project contains a simple demo, play video using SurfaceView just like the [MediaPlayer API](https://developer.android.com/reference/android/media/MediaPlayer.html)

# Other Info
1. We slightly modify exoplayer for our own usage,(minor bugfixes, add audiolevel support, performance enhancement etc) the code is located at https://github.com/michalliu/Google_ExoPlayer, This extension should work on vanilla ExoPlayer, you just need comment out some code, the main functionality is not affected.
2. The code is pretty mature and we already use in our production environment in the scale of billions users.
3. The extension added supports for soft decoding hevc/h265 video using OpenHevc (Not mature, playable though), code located at https://github.com/michalliu/exoplayer2-hevc-extension
4. This extension supports concating play multiple media files playing just like one video.

Any questions, feel free to submit an issue
