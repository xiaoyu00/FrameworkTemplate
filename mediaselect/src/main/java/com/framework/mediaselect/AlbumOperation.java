package com.framework.mediaselect;

import com.luck.picture.config.SelectMimeType;
import com.luck.picture.config.SelectModeConfig;

public class AlbumOperation {
    public int selectMimeType = SelectMimeType.ofAll();
    public int selectMode = SelectModeConfig.SINGLE;
    public long selectMaxFileSize = 307200;
    public int recordVideoMaxSecond=15;
    public int maxSelectNum = 9;
    public boolean isCompress = true;
    public boolean isCrop = false;
    public int compressSize = 300;
}
