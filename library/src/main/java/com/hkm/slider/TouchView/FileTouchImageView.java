package com.hkm.slider.TouchView;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import java.io.File;
import java.io.FileInputStream;

public class FileTouchImageView extends UrlTouchImageView 
{
	
    public FileTouchImageView(Context ctx)
    {
        super(ctx);

    }
    public FileTouchImageView(Context ctx, AttributeSet attrs)
    {
        super(ctx, attrs);
    }

    public void setUrl(String imagePath)
    {
       // new ImageLoadTask().execute(imagePath);
    }
    //No caching load
   /* public class ImageLoadTask extends UrlTouchImageView.ImageLoadTask
    {
        @Override
        protected Bitmap doInBackground(String... strings) {
            String path = strings[0];
            Bitmap bm = null;
            try {
            	File file = new File(path);
            	FileInputStream fis = new FileInputStream(file);
                InputStreamWrapper bis = new InputStreamWrapper(fis, 8192, file.length());
                bis.setProgressListener(new InputStreamWrapper.InputStreamProgressListener()
				{					
					@Override
					public void onProgress(float progressValue, long bytesLoaded,
							long bytesTotal)
					{
						publishProgress((int)(progressValue * 100));
					}
				});
                bm = BitmapFactory.decodeStream(bis);
                bis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bm;
        }
    }*/
}
