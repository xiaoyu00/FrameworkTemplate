package com.luck.picture.engine;

import com.luck.picture.entity.LocalMedia;
import com.luck.picture.interfaces.OnInjectLayoutResourceListener;
import com.luck.picture.interfaces.OnResultCallbackListener;

/**
 * @author：luck
 * @date：2020/4/22 11:36 AM
 * @describe：PictureSelectorEngine
 */
public interface PictureSelectorEngine {

    /**
     * Create ImageLoad Engine
     *
     * @return
     */
    ImageEngine createImageLoaderEngine();

    /**
     * Create compress Engine
     *
     * @return
     */
    CompressEngine createCompressEngine();

    /**
     * Create loader data Engine
     *
     * @return
     */
    ExtendLoaderEngine createLoaderDataEngine();

    /**
     * Create SandboxFileEngine  Engine
     *
     * @return
     */
    SandboxFileEngine createSandboxFileEngine();

    /**
     * Create LayoutResource  Listener
     *
     * @return
     */
    OnInjectLayoutResourceListener createLayoutResourceListener();

    /**
     * Create Result Listener
     *
     * @return
     */
    OnResultCallbackListener<LocalMedia> getResultCallbackListener();
}
