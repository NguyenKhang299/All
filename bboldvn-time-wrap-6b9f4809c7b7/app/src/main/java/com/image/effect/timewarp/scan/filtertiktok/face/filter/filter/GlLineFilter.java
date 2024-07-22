package com.image.effect.timewarp.scan.filtertiktok.face.filter.filter;

import static com.image.effect.timewarp.scan.filtertiktok.face.filter.util.Utils.Companion;

import android.opengl.GLES20;

import com.daasuu.gpuv.egl.filter.GlFilter;
import com.image.effect.timewarp.scan.filtertiktok.face.filter.util.Utils;

public class GlLineFilter extends GlFilter {
    private float lineWidth = 1F;
    private float progress = 0F;
    private int typeScan = 0;

    private int width;
    private int height;

    public GlLineFilter() {
        super("" +
                "attribute highp vec4 aPosition;" +
                "\nattribute highp vec4 aTextureCoord;" +
                "\nvarying highp vec2 vTextureCoord;" +
                "\nvoid main() {" +
                "\ngl_Position = aPosition;" +
                "\nvTextureCoord = aTextureCoord.xy;" +
                "\n}" +
                "\n", "precision mediump float; varying vec2 vTextureCoord;" +
                "\n  " +
                "\n uniform lowp sampler2D sTexture;" +
                "\n  uniform highp float progress;" +
                "\n  uniform highp float typeScan;" +
                "\n  uniform highp float lineWidth;" +
                "\n  " +
                "\n  void main()" +
                "\n  {" +
                "\n      highp vec4 textureColor = texture2D(sTexture, vTextureCoord);" +
                "\n      " +
                "\n      gl_FragColor = textureColor;" +
                "\n       if(progress != 1.0 && progress != 0.0){" +
                "\n         if(typeScan==0.0 ||typeScan==2.0){" +
                "\n              if(abs(progress - vTextureCoord.x)<lineWidth){" +
                "\n                  gl_FragColor = vec4(0.22,1.0,0.90,1.0);" +
                "\n              }" +
                "\n          }else{" +
                "\n              if(abs(progress - vTextureCoord.y)<lineWidth){" +
                "\n                  gl_FragColor = vec4(0.22,1.0,0.90,1.0);" +
                "\n              }" +
                "\n          }" +
                "\n       }" +
                "\n  }" +
                "\n");
    }

    private void updateLineWidth() {
        int type = this.typeScan;
        if (type != 0) {
            if (type != 1) {
                if (type != 2 && type != 3) {
                    return;
                }

                this.lineWidth = Utils.Companion.dpToPx(1) / (float) this.width;
            }

            this.lineWidth = Utils.Companion.dpToPx(1) / (float) this.height;
        }

        this.lineWidth = Utils.Companion.dpToPx(1) / (float) this.width;
    }

    public void onDraw() {
        GLES20.glUniform1f(this.getHandle("progress"), this.progress);
        GLES20.glUniform1f(this.getHandle("typeScan"), (float) this.typeScan);
        GLES20.glUniform1f(this.getHandle("lineWidth"), this.lineWidth);
    }

    public void setFrameSize(int w, int h) {
        super.setFrameSize(w, h);
        this.width = w;
        this.height = h;
        this.updateLineWidth();
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public void setTypeScan(int type) {
        this.typeScan = type;
    }

}
