package com.example.ipcplayer.service;

interface IPlayback{
   void start();
   void pause();
   void stop();
   void release();
   void previous();
   void next();
   int getId();
   String getTitle();
   String getArtist();
   String getAlbumn();
   int getDuration();
   int getCurrentTime();
   boolean isPlaying();
   boolean isPaused();
   void seekTo(int position);
}