package com.luck.picture.adapter.holder;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;
import com.luck.picture.lib.R;
import com.luck.picture.config.PictureMimeType;
import com.luck.picture.config.PictureSelectionConfig;
import com.luck.picture.entity.LocalMedia;

import java.io.File;


/**
 * @author：luck
 * @date：2021/12/15 5:12 下午
 * @describe：PreviewVideoHolder
 */
public class PreviewVideoHolder extends BasePreviewHolder {
    public ImageView ivPlayButton;
    public PlayerView mPlayerView;
    public ProgressBar progress;

    public PreviewVideoHolder(@NonNull View itemView) {
        super(itemView);
        ivPlayButton = itemView.findViewById(R.id.iv_play_video);
        mPlayerView = itemView.findViewById(R.id.playerView);
        progress = itemView.findViewById(R.id.progress);
        PictureSelectionConfig config = PictureSelectionConfig.getInstance();
        ivPlayButton.setVisibility(config.isPreviewZoomEffect ? View.GONE : View.VISIBLE);
    }

    @Override
    public void bindData(LocalMedia media, int position) {
        super.bindData(media, position);
        String path = media.getAvailablePath();
        mPlayerView.setUseController(false);
        ivPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.setVisibility(View.VISIBLE);
                ivPlayButton.setVisibility(View.GONE);
                mPreviewEventListener.onPreviewVideoTitle(media.getFileName());
                ExoPlayer player = new ExoPlayer.Builder(itemView.getContext()).build();
                mPlayerView.setPlayer(player);
                MediaItem mediaItem = PictureMimeType.isContent(path)
                        ? MediaItem.fromUri(Uri.parse(path)) : MediaItem.fromUri(Uri.fromFile(new File(path)));
                player.setMediaItem(mediaItem);
                player.prepare();
                player.play();
                player.addListener(mPlayerListener);
            }
        });
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPreviewEventListener != null) {
                    mPreviewEventListener.onBackPressed();
                }
            }
        });
    }

    private final Player.Listener mPlayerListener = new Player.Listener() {
        @Override
        public void onPlayerError(@NonNull PlaybackException error) {
            playerDefaultUI();
        }

        @Override
        public void onPlaybackStateChanged(int playbackState) {
            if (playbackState == Player.STATE_READY) {
                playerIngUI();
            } else if (playbackState == Player.STATE_ENDED) {
                playerDefaultUI();
            }
        }
    };

    private void playerDefaultUI() {
        ivPlayButton.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
        coverImageView.setVisibility(View.VISIBLE);
        mPlayerView.setVisibility(View.GONE);
        if (mPreviewEventListener != null) {
            mPreviewEventListener.onPreviewVideoTitle(null);
        }
    }

    private void playerIngUI() {
        if (progress.getVisibility() == View.VISIBLE) {
            progress.setVisibility(View.GONE);
        }
        if (ivPlayButton.getVisibility() == View.VISIBLE) {
            ivPlayButton.setVisibility(View.GONE);
        }
        if (coverImageView.getVisibility() == View.VISIBLE) {
            coverImageView.setVisibility(View.GONE);
        }
        if (mPlayerView.getVisibility() == View.GONE) {
            mPlayerView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 释放VideoView
     */
    public void releaseVideo() {
        if (mPlayerView.getPlayer() != null) {
            mPlayerView.getPlayer().removeListener(mPlayerListener);
            mPlayerView.getPlayer().release();
            playerDefaultUI();
        }
    }
}
