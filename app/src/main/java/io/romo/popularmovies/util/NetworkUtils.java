/*
 * Copyright 2017 Juan Romo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.romo.popularmovies.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class NetworkUtils {
    
    public enum ImageSize {
        MOBILE("w185"),
        SMALL("w300"),
        MEDIUM("w500"),
        LARGE("w780");
        
        private String imagePath;
        
        ImageSize(String imagePath) {
            this.imagePath = imagePath;
        }
        
        public String getImagePath() {
            return imagePath;
        }
    }
    
    public static String createImageUrl(String filePath, ImageSize imageSize) {
        Uri uri = new Uri.Builder().scheme("https")
                .authority("image.tmdb.org")
                .appendPath("t")
                .appendPath("p")
                .appendPath(imageSize.getImagePath())
                .appendEncodedPath(filePath)
                .build();
        return uri.toString();
    }
    
    public static void watchYoutubeVideo(Context context, String id) {
        
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        if (appIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(appIntent);
        } else {
            Uri uri = new Uri.Builder().scheme("https")
                    .authority("www.youtube.com")
                    .appendPath("watch")
                    .appendQueryParameter("v", id)
                    .build();
            
            Intent webIntent = new Intent(Intent.ACTION_VIEW, uri);
            if (webIntent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(webIntent);
            }
        }
    }
}
