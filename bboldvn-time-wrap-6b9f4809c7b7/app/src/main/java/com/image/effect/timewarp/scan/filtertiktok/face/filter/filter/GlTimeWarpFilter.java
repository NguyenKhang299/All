package com.image.effect.timewarp.scan.filtertiktok.face.filter.filter;

import android.opengl.GLES20;

import com.daasuu.gpuv.egl.EglUtil;
import com.daasuu.gpuv.egl.GlFramebufferObject;
import com.daasuu.gpuv.egl.filter.GlFilter;

public class GlTimeWarpFilter extends GlFilter {
    private GlFramebufferObject framebufferObject;
    private float progress = 0F;
    private int type = 0;

    public GlTimeWarpFilter() {
        super("" +
                "attribute highp vec4 aPosition;" +
                "\nattribute highp vec4 aTextureCoord;" +
                "\nvarying highp vec2 vTextureCoord;" +
                "\nvoid main() {" +
                "\ngl_Position = aPosition;" +
                "\nvTextureCoord = aTextureCoord.xy;" +
                "\n}" +
                "\n", "precision mediump float; varying vec2 vTextureCoord;" +
                "\n " +
                "\n uniform lowp sampler2D sTexture;" +
                "\n uniform lowp sampler2D uTexture;" +
                "\n uniform lowp float progress;" +
                "\n uniform lowp float typeScan;" +
                "\n void main()" +
                "\n {" +
                "\n      highp vec4 textureColor = texture2D(sTexture, vTextureCoord);" +
                "\n      highp vec4 texture = texture2D(uTexture, vTextureCoord);" +
                "\n      if(typeScan==0.0){" +
                "\n          if(vTextureCoord.x > progress){" +
                "\n              gl_FragColor = textureColor;" +
                "\n          }else{" +
                "\n              gl_FragColor = texture;" +
                "\n          }" +
                "\n      }else if(typeScan==1.0){" +
                "\n          if(vTextureCoord.y > progress){" +
                "\n              gl_FragColor = texture;" +
                "\n          }else{" +
                "\n              gl_FragColor = textureColor;" +
                "\n          }" +
                "\n      }else if(typeScan==2.0){" +
                "\n          if(vTextureCoord.x > progress){" +
                "\n              gl_FragColor = texture;" +
                "\n          }else{" +
                "\n              gl_FragColor = textureColor;" +
                "\n          }" +
                "\n      }else if(typeScan==3.0){" +
                "\n          if(vTextureCoord.y > progress){" +
                "\n              gl_FragColor = textureColor;" +
                "\n          }else{" +
                "\n              gl_FragColor = texture;" +
                "\n          }" +
                "\n      }" +
                "\n }" +
                "\n");
    }


    private void activateTexture(int var1, int var2, int var3) {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + var2);
        GLES20.glBindTexture(3553, var1);
        GLES20.glUniform1i(var3, var2);
    }

    private void scan(int var1, GlFramebufferObject var2, float var3) {
        this.useProgram();
        GLES20.glBindBuffer(34962, getVertexBufferName());
        GLES20.glEnableVertexAttribArray(this.getHandle("aPosition"));
        GLES20.glVertexAttribPointer(this.getHandle("aPosition"), 3, 5126, false, 20, 0);
        GLES20.glEnableVertexAttribArray(this.getHandle("aTextureCoord"));
        GLES20.glVertexAttribPointer(this.getHandle("aTextureCoord"), 2, 5126, false, 20, 12);
        GLES20.glUniform1f(this.getHandle("progress"), var3);
        GLES20.glUniform1f(this.getHandle("typeScan"), (float) this.type);
        this.activateTexture(var1, 0, this.getHandle("sTexture"));
        this.activateTexture(var2.getTexName(), 1, this.getHandle("uTexture"));
        EglUtil.setupSampler(3553, 9728, 9728);
        GLES20.glDrawArrays(5, 0, 4);
    }

    public void draw(int var1, GlFramebufferObject var2) {
        GlFramebufferObject var3 = this.framebufferObject;
        if (var3 != null) {
            var3.enable();
            this.scan(var1, this.framebufferObject, this.progress);
            var2.enable();
            this.scan(var1, this.framebufferObject, this.progress);
        }

    }

    public void setFrameSize(int w, int h) {
        GlFramebufferObject var3 = this.framebufferObject;
        if (var3 != null) {
            var3.setup(w, h);
        }

    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public void setTypeScan(int type) {
        this.type = type;
    }

    public void setup() {
        super.setup();
        this.framebufferObject = new GlFramebufferObject();
        this.getHandle("uTexture");
        this.getHandle("progress");
        this.getHandle("typeScan");
    }
}

